package com.hubert;

import java.util.*;

import org.junit.Test;

import com.hubert.machinelearning.FPTree;

import javafx.util.Pair;

public class FPGrowthTest {

    @Test
    public void test1() {
        List<String> data = new ArrayList<String>();
        data.add("AB");
        data.add("BCD");
        data.add("ACDE");
        data.add("ADE");
        data.add("ABC");
        data.add("ABCD");
        data.add("A");
        data.add("ABC");
        data.add("ABD");
        data.add("BCE");
        
        Collection<Collection<Character>> source = new ArrayList<Collection<Character>>();
        for(String item : data){
            List<Character> listC = new ArrayList<Character>();
            for(int i = 0; i < item.length(); ++i){
                Character c = item.charAt(i);
                listC.add(c);
            }
            source.add(listC);
        }
        
        FPTree<Character> tree = new FPTree<Character>(source, 2);
        tree.setDumpFile("resource/FPGrowthTest/debug/tree.json");
        Collection<Pair<Integer, List<Character>>> result =  tree.getAll();
        for(Pair<Integer, List<Character>> item : result){
            System.out.print(item.getKey());
            System.out.print(": ");
            for(Character c : item.getValue()){
                System.out.print(c);
                System.out.print(" ");
            }
            System.out.println("");
        }
    }
}
