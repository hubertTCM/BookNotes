package com.hubert.dataprovider.parser.AST;

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
		}
		String prefix = "";
		for (int i = 0; i < mIndent; ++i) {
			prefix += "    ";
		}

		mFileWriter.write(prefix + "{tag:'" + node.getTag() + "',\n");
		mFileWriter.write(prefix + " value:'" + node.getValue() + "',\n");

		int childrenCount = node.getChildCount();
		if (childrenCount == 0) {
			mFileWriter.write(prefix + " children: []\n");
		} else {
			mFileWriter.write(prefix + " children: [\n");
			mIndent += 1;
			for (int i = 0; i < node.getChildCount(); ++i) {
				node.getChild(i).accept(this);
			}
			mFileWriter.write(prefix + "    ]\n");
			mIndent -= 1;
		}

		mFileWriter.write(prefix + "}\n");

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
}
