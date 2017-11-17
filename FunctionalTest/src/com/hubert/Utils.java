package com.hubert;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;

public class Utils {
    public void checkFile(String expectFile, String actualFile) {

        try {
            String expectContent = new String(Files.readAllBytes(Paths.get(expectFile)));
            String actualContent = new String(Files.readAllBytes(Paths.get(actualFile)));
            String message = String.format("compare '%s' and '%s'", expectFile, actualFile);
            assertEquals(message, expectContent, actualContent);
        } catch (IOException e) {
            e.printStackTrace(new PrintStream(System.out));
            fail(e.toString());
        }
    }

}
