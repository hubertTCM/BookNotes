package com.hubert.machinelearning.YiAn;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

// Similar with LogVisitor, merge later
public class ClusterRender {
    public ClusterRender(String fileName) {
        mLogFilePath = fileName;
    }

    public void rend(PrescriptionClusterCompositeNode root) {

        try {
            open();
            renderCore(root, true);
            close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getNodeValue(PrescriptionClusterCompositeNode node) {
        String value = Integer.toString(node.getLeafNodes().size()); // + ",";
        Set<String> center = node.getCenter();
        for (String item : center) {
            value += " " + item;
        }
        return value;
    }

    private void renderCore(PrescriptionClusterCompositeNode node, boolean isLast) throws IOException {
        String prefix = "";
        for (int i = 0; i < mIndent; ++i) {
            prefix += "    ";
        }

        mFileWriter.write(prefix + " {\"value\":\"" + getNodeValue(node) + "\",\n");

        List<PrescriptionClusterCompositeNode> children = node.getCompositeNodes();
        if (children.isEmpty()) {
            mFileWriter.write(prefix + " \"children\": []\n");
        } else {
            mFileWriter.write(prefix + " \"children\": [\n");
            mIndent += 1;

            for (int i = 0; i < children.size(); ++i) {
                PrescriptionClusterCompositeNode child = children.get(i);
                renderCore(child, i == children.size() - 1);
            }
            mFileWriter.write(prefix + "    ]\n");
            mIndent -= 1;
        }

        if (isLast) {
            mFileWriter.write(prefix + "}\n");
        } else {
            mFileWriter.write(prefix + "},\n");
        }

    }

    private void open() throws IOException {
        Paths.get(mLogFilePath).getParent().toFile().mkdirs();
        mFileWriter = new FileWriter(mLogFilePath);
    }

    private void close() throws IOException {
        mFileWriter.close();
    }

    private FileWriter mFileWriter;
    private String mLogFilePath;
    private int mIndent = 0;
}
