package com.hubert;

import static org.junit.Assert.fail;

import java.io.*;
import java.util.*;

import org.junit.*;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.*;
import com.hubert.dto.Prescription;
import com.hubert.machinelearning.*;
import com.hubert.machinelearning.YiAn.*;

public class ClusterTest {

    @Test
    public void test1() {
        try {
            BookGenerator generator = new BookGenerator(sGrammarFile, "../DataGenerator/resource/临证指南医案",
                    sHerbAliasManager);
            Map<String, List<Prescription>> prescriptions = generator.doImport();
            for (Map.Entry<String, List<Prescription>> entry : prescriptions.entrySet()) {
                createCluster(entry.getKey(), entry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace(new PrintStream(System.out));
            fail(e.toString());
        }
    }

    private void createCluster(String pathPrefix, List<Prescription> prescriptions) {
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
        // visitor.split(root);
        for (CompositeNode<Prescription> node : visitor.getNodes(root, 3)) {
            // node.getCenter();
        }

        AverageLinkageDistanceCalculator<Prescription> distance2 = new AverageLinkageDistanceCalculator<Prescription>(
                leafDistance);
        root = analyzer.analyze(distance2);
        for (CompositeNode<Prescription> node : visitor.getNodes(root, 4)) {
            // node.getCenter();
        }

        String fileName = pathPrefix + "_cluster.js";
        String actualFilePath = getActualFilePath(fileName);
        ClusterRender<Prescription, String> render = new ClusterRender<Prescription, String>(actualFilePath,
                new IConverter<Prescription, String>() {
                    @Override
                    public Set<String> convert(Prescription x) {
                        return x.getHerbs();
                    }
                });
        render.rend(root);

        String expectFilePath = getExpectFilePath(fileName);
        File f = new File(expectFilePath);
        if (f.exists()) {
            sUtils.checkFile(expectFilePath, actualFilePath);
        } else {
            System.out.print(expectFilePath + " not exist\n");
        }
    }

    private String getActualFilePath(String fileName) {
        return "resource/ClusterTest/actual/" + fileName;
    }

    private String getExpectFilePath(String fileName) {
        return "resource/ClusterTest/expect/" + fileName;
    }

    private static String sGrammarFile = "../DataGenerator/resource/临证指南医案/grammar.xml";
    private static HerbAliasManager sHerbAliasManager = HerbAliasManager
            .getInstance("../DataGenerator/resource/常用中药处方别名.txt");

    private static Utils sUtils = new Utils();
}
