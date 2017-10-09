package com.hubert.dataprovider;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

// split a single txt to section directories.
public class BookSpliter {
    public BookSpliter(String sourcePath, String output) {
        mOutputDirectory = output;
        mSource = sourcePath;
        init();
    }

    private void init() {
        String numbers = "一二三四五六七八九十";
        for (int i = 0; i < numbers.length(); ++i) {
            mKeywords.add("卷" + Character.toString(numbers.charAt(i)));
        }
    }

    public void split() throws IOException {
        String currentFolderName = String.valueOf(1) + "." + mKeywords.get(0);
        int currentFileIndex = 3;
        FileWriter currentWriter = null;
        Path filePath = Paths.get(mSource);
        Charset utf8 = Charset.forName("UTF-8");
        List<String> lines = Files.readAllLines(filePath, utf8);

        for (String temp : lines) {
            System.out.println(temp);
            String line = StringUtils.strip(temp);
            int keywordIndex = mKeywords.indexOf(line);
            if (keywordIndex >= 0) {
                currentFolderName = String.valueOf(keywordIndex + 1) + "." + line;
                currentFileIndex = 1;
                continue;
            }
            if (!line.isEmpty() && line.equals(temp) && !line.contains("（")) {
                if (currentWriter != null) {
                    currentWriter.close();
                }

                File directory = Paths.get(mOutputDirectory, currentFolderName).toFile();
                directory.mkdirs();

                String fileName = String.valueOf(currentFileIndex) + "." + line + ".txt";
                currentWriter = new FileWriter(
                        Paths.get(directory.getAbsolutePath(), fileName).toFile().getAbsolutePath());
                continue;
            }

            currentWriter.write(line + "\n");

        }
    }

    private List<String> mKeywords = new ArrayList<String>();

    private String mOutputDirectory;
    private String mSource;
}
