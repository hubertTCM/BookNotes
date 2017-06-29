package com.hubert.dataprovider;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

public class HerbAliasManager {

	public void load() {
		Charset utf8 = Charset.forName("UTF-8");
		Path filePath = Paths.get("resource/" + _fileName);
		try {
			List<String> lines = Files.readAllLines(filePath, utf8);
			for (String line : lines) {
				line = StringUtils.strip(line);
				parse(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	int _lineIndex = 0;

	private void parse(String line) {
		// 混淆品：关白附：为毛茛科植物黄花乌头的块根。处方别名：关白附、竹节白附
		String ignorePrefix = "混淆品：";
		if (line.startsWith(ignorePrefix)) {
			parse(line.substring(ignorePrefix.length()));
			return;
		}
		_lineIndex += 1;
		if (line.isEmpty()){
			return;
		}

		// 枯萝卜：为十字花科植物莱菔的老根。处方别名：枯萝卜、气萝卜、仙人骨、仙人头、地骷髅、地枯萝、空莱菔、老人头
		int nameIndex = line.indexOf("：");
		if (nameIndex <= 0) {
			System.out.println(Integer.toString(_lineIndex) + " no herb in " + line);
			return;
		}
		String name = line.substring(0, nameIndex);
		name = getHerb(name);

		String aliasPrefix = "处方别名：";
		int aliasIndex = line.indexOf(aliasPrefix);
		if (aliasIndex <= 0) {
			System.out.println(Integer.toString(_lineIndex) + " no alias " + line);
			return;
		}

		String aliaString = line.substring(aliasIndex + aliasPrefix.length(), line.length());
		String[] temp = line.split("、");
		List<String> alias = new ArrayList<String>();
		for (String item : temp) {
			alias.add(getHerb(item));
		}
		_alias.put(name, alias);
	}

	private String getHerb(String from) {
		return from;
	}

	private String _fileName = "常用中药处方别名.txt";
	private HashMap<String, List<String>> _alias = new HashMap<String, List<String>>();
}
