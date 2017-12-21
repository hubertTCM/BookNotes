package com.hubert.dataprovider;

import java.io.IOException;
import java.util.*;

import com.hubert.dal.*;
//import com.hubert.dal.DbBuilder;
import com.hubert.dal.entity.*;
import com.hubert.dto.*;
import com.hubert.machinelearning.agens.*;
import com.hubert.machinelearning.distance.*;
//import com.hubert.parser.AST.*;
//import com.hubert.parser.AST.YiAn.*;
//import com.hubert.parser.tokenextractor.*;
import com.hubert.parser.*;

public class importor {

    public static void main(String[] args) {
        try {
            // https://stackoverflow.com/questions/40740819/cannot-disable-ormlites-logging
            System.setProperty("com.j256.ormlite.logger.type", "LOCAL");
            System.setProperty("com.j256.ormlite.logger.level", "ERROR");

            BookSpliter s = new BookSpliter("resource/临证指南医案/临证指南医案_summary.txt", "resource/临证指南医案/test");
            s.split();

            BookGenerator generator = new BookGenerator("resource/临证指南医案/grammar.xml", "resource/临证指南医案",
                    HerbAliasManager.getInstance());
            Map<String, List<Prescription>> temp = generator.doImport();
            List<Prescription> prescriptions = Utils.merge(temp);
            DbBuilder builder = new DbBuilder();
            builder.build();
            List<BlockGroupEntity> blockGroups = generator.getBlockGroups();
            BookEntity book = generator.getBook();
            YiAnImporter yiAnImporter = new YiAnImporter();

            List<PrescriptionEntity> prescriptionEntities = new Vector<PrescriptionEntity>();
            yiAnImporter.save(book, blockGroups, prescriptionEntities);

            DistanceCacheProxy<Prescription> leafDistance = new DistanceCacheProxy<Prescription>(
                    new IDistanceCalculator<Prescription>() {
                        @Override
                        public double distance(Prescription x, Prescription y) {
                            JaccardDistanceCalculator<Set<String>, String> core = new JaccardDistanceCalculator<Set<String>, String>();
                            return core.distance(x.getHerbs(), y.getHerbs());
                        }
                    }, new IStringConverter<Prescription>() {

                        @Override
                        public String convert(Prescription x) {
                            return x.getSummary();
                        }
                    });

            AgensAnalyzer<Prescription> analyzer = new AgensAnalyzer<Prescription>(prescriptions);
            SingleLinkageDistanceCalculator<Prescription> singleDistance = new SingleLinkageDistanceCalculator<Prescription>(
                    leafDistance);
            CompositeNode<Prescription> root = analyzer.analyze(singleDistance);
            ClusterAnalayer<Prescription> visitor = new ClusterAnalayer<Prescription>();

            System.out.println("*****");

            AverageLinkageDistanceCalculator<Prescription> distance2 = new AverageLinkageDistanceCalculator<Prescription>(
                    leafDistance);
            root = analyzer.analyze(distance2);
            // for (CompositeNode<Prescription> node : visitor.getNodes(root,
            // 4)) {
            // node.getCenter();
            // }

            ClusterRender<Prescription, String> render = new ClusterRender<Prescription, String>(
                    "resource/debug/cluster.js", new IConverter<Prescription, String>() {
                        @Override
                        public Set<String> convert(Prescription x) {
                            return x.getHerbs();
                        }
                    });
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
