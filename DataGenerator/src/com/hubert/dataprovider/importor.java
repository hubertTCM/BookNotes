package com.hubert.dataprovider;

import java.io.IOException;
import java.util.*;

import com.hubert.dal.*;
//import com.hubert.dal.DbBuilder;
import com.hubert.dal.entity.*;
import com.hubert.machinelearning.*;
import com.hubert.machinelearning.YiAn.*;
//import com.hubert.parser.AST.*;
//import com.hubert.parser.AST.YiAn.*;
//import com.hubert.parser.tokenextractor.*;
import com.hubert.parser.*;

public class importor {

    public static void main(String[] args) {
        try {
            //https://stackoverflow.com/questions/40740819/cannot-disable-ormlites-logging
            System.setProperty("com.j256.ormlite.logger.type", "LOCAL");
            System.setProperty("com.j256.ormlite.logger.level", "ERROR");
            
            BookSpliter s = new BookSpliter("resource/临证指南医案/临证指南医案_summary.txt", "resource/临证指南医案/test");
            s.split();

            BookGenerator generator = new BookGenerator("resource/临证指南医案/grammar.xml", "resource/临证指南医案",
                    HerbAliasManager.getInstance());
            Map<String, List<YiAnEntity>> temp = generator.doImport();
            List<YiAnEntity> yiAns = Utils.merge(temp);
            
            DbBuilder builder = new DbBuilder();
            builder.build();
            BookEntity book = generator.getBook();
            YiAnImporter yiAnImporter = new YiAnImporter();
            yiAnImporter.save(book, yiAns);

            List<YiAnPrescriptionEntity> prescriptions = new ArrayList<YiAnPrescriptionEntity>();
            for (YiAnEntity yiAn : yiAns) {
                for (YiAnDetailEntity detail : yiAn.details) {
                    prescriptions.addAll(detail.prescriptions);
                }
            }

            DistanceCacheProxy<PrescriptionClusterLeafNode> leafDistance = new DistanceCacheProxy<PrescriptionClusterLeafNode>(
                    new IDistanceCalculator<PrescriptionClusterLeafNode>() {

                        @Override
                        public double distance(PrescriptionClusterLeafNode x, PrescriptionClusterLeafNode y) {
                            JaccardDistanceCalculator<Set<String>, String> core = new JaccardDistanceCalculator<Set<String>, String>();
                            return core.distance(x.getHerbs(), y.getHerbs());

                        }
                    }, new IStringConverter<PrescriptionClusterLeafNode>() {

                        @Override
                        public String convert(PrescriptionClusterLeafNode x) {
                            return x.getSummary();
                        }
                    });

            PrescriptionAnalyzer analyzer = new PrescriptionAnalyzer(prescriptions);
            SingleLinkageDistanceCalculator singleDistance = new SingleLinkageDistanceCalculator(leafDistance);
            PrescriptionClusterCompositeNode root = analyzer.analyze(singleDistance);
            ClusterAnalayer visitor = new ClusterAnalayer();
            // visitor.split(root);
            for (PrescriptionClusterCompositeNode node : visitor.getNodes(root, 3)) {
                node.getCenter();
            }

            System.out.println("*****");

            AverageLinkageDistanceCalculator distance2 = new AverageLinkageDistanceCalculator(leafDistance);
            root = analyzer.analyze(distance2);
            for (PrescriptionClusterCompositeNode node : visitor.getNodes(root, 4)) {
                node.getCenter();
            }

            ClusterRender render = new ClusterRender("resource/debug/cluster.js");
            render.rend(root);

            System.out.println("done");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
