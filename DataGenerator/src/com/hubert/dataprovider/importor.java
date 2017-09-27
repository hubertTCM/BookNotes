package com.hubert.dataprovider;

import java.io.IOException;
import java.util.*;

//import com.hubert.dal.DbBuilder;
import com.hubert.dal.entity.*;
import com.hubert.machinelearning.*;
import com.hubert.machinelearning.YiAn.*;
import com.hubert.parser.AST.*;
import com.hubert.parser.AST.YiAn.*;
import com.hubert.parser.tokenextractor.*;

public class importor {

    public static void main(String[] args) {
        try {
            // TODO Auto-generated method stub
            // DbBuilder builder = new DbBuilder();
            // builder.build();

            // gu_jin_yi_an_an_importer importor = new
            // gu_jin_yi_an_an_importer();
            // importor.doImport();

            // HerbAliasManager aliasManager = new HerbAliasManager();
            // aliasManager.load();
            // HerbAliasManager.getInstance().trace();

            BookGenerator generator = new BookGenerator("resource/临证指南医案/format_ignore.txt", "临证指南医案");
            List<YiAnEntity> yiAns = generator.doImport();

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

            ClusterRender render = new ClusterRender("resource/debug/Cluster.js");
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
