package com.hubert.machinelearning.agens;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

// Similar with LogVisitor, merge later
public class ClusterRender<T, U> {
    public ClusterRender(String fileName, IConverter<T, U> converter) {
        mLogFilePath = fileName;
        mConverter = converter;
    }

    public void rend(CompositeNode<T> root) {

        try {
            open();
            renderCore(root, true);
            close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getNodeValue(CompositeNode<T> node) {
        String value = Integer.toString(node.getLeafNodes().size()); // + ",";
        
        List<Set<U>> temp = new Vector<Set<U>>();
        for(LeafNode<T> leaf : node.getLeafNodes()){
            temp.add(mConverter.convert(leaf.getValue()));
        }

        CenterCalculator<U> centerCalculator = new CenterCalculator<U>(temp);
        Set<U> center = centerCalculator.getCenter();
        for (U item : center) {
            value += " " + item;
        }
        return value;
    }

    private void renderCore(CompositeNode<T> node, boolean isLast) throws IOException {
        String prefix = "";
        for (int i = 0; i < mIndent; ++i) {
            prefix += "    ";
        }

        mFileWriter.write(prefix + " {\"value\":\"" + getNodeValue(node) + "\",\n");

        List<CompositeNode<T>> children = node.getCompositeNodes();
        if (children.isEmpty()) {
            mFileWriter.write(prefix + " \"children\": []\n");
        } else {
            mFileWriter.write(prefix + " \"children\": [\n");
            mIndent += 1;

            for (int i = 0; i < children.size(); ++i) {
                CompositeNode<T> child = children.get(i);
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
    private IConverter<T, U> mConverter;
    private int mIndent = 0;
}
