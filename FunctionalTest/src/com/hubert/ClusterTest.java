package com.hubert;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.*;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.machinelearning.*;
import com.hubert.machinelearning.YiAn.*;

public class ClusterTest {

    @Test
    public void test1() {
        try {
            BookGenerator generator = new BookGenerator(sGrammarFile, "../DataGenerator/resource/临证指南医案",
                    sHerbAliasManager);
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

            String fileName = "cluster.js";
            ClusterRender render = new ClusterRender(getActualFilePath(fileName));
            render.rend(root);
            
            sUtils.checkFile(getExpectFilePath(fileName), getActualFilePath(fileName));
            
        } catch (Exception e) {
            fail(e.toString());
        }
    }
    
    private String getActualFilePath(String fileName) {
        return "resource/ClusterTest/actual/" + fileName;
    }

    private String getExpectFilePath(String fileName) {
        return "resource/ClusterTest/expect/" + fileName;
    }

    private static String sGrammarFile = "../DataGenerator/resource/临证指南医案/format_ignore.txt";
    private static HerbAliasManager sHerbAliasManager = HerbAliasManager
            .getInstance("../DataGenerator/resource/常用中药处方别名.txt");
    
    private static Utils sUtils = new Utils();
}
