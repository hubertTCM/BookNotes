package com.hubert.parser.AST;

import java.io.*;

public class LogVisitor implements IVisitor {
    public LogVisitor(String fileName) {
        mLogFilePath = fileName;
    }

    @Override
    public void visit(ASTNode node) {
        try {
            visitCore(node);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void visitCore(ASTNode node) throws IOException {
        if (node.getParent() == null) {
            open();
            mIsLastChild = true;
        }
        String prefix = "";
        for (int i = 0; i < mIndent; ++i) {
            prefix += "    ";
        }

        boolean isLastChildOrignalValue = mIsLastChild;

        mFileWriter.write(prefix + "{\"tag\":\"" + node.getTag() + "\",\n");
        mFileWriter.write(prefix + " \"value\":\"" + node.getValue() + "\",\n");

        int childrenCount = node.childCount();
        if (childrenCount == 0) {
            mFileWriter.write(prefix + " \"children\": []\n");
        } else {
            mFileWriter.write(prefix + " \"children\": [\n");
            mIndent += 1;
            for (int i = 0; i < node.childCount(); ++i) {
                mIsLastChild = (i == node.childCount() - 1);
                node.getChild(i).accept(this);
            }
            mFileWriter.write(prefix + "    ]\n");
            mIndent -= 1;
        }

        mIsLastChild = isLastChildOrignalValue;
        if (mIsLastChild) {
            mFileWriter.write(prefix + "}\n");
        } else {
            mFileWriter.write(prefix + "},\n");
        }

        if (node.getParent() == null) {
            close();
        }
    }

    private void open() throws IOException {
        mFileWriter = new FileWriter(mLogFilePath);
    }

    private void close() throws IOException {
        mFileWriter.close();
    }

    private FileWriter mFileWriter;
    private String mLogFilePath;
    private int mIndent = 0;
    private boolean mIsLastChild = false;
}
