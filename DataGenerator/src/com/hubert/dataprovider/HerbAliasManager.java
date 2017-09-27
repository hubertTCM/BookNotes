package com.hubert.dataprovider;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

public class HerbAliasManager {

	private static HerbAliasManager sInstance = new HerbAliasManager();

	private HerbAliasManager() {
	}

	public static HerbAliasManager getInstance(String fileName) {
		HerbAliasManager instance = new HerbAliasManager();
		instance.load(fileName);
		return instance;
	}

	public static HerbAliasManager getInstance() {
		// sInstance._fileName = "resource/常用中药处方别名.txt";
		sInstance.load("resource/常用中药处方别名.txt");
		return sInstance;
	}

	private void load(String fileName) {
		if (mInitialized){
			return;
		}
		
		Charset utf8 = Charset.forName("UTF-8");
		Path filePath = Paths.get(fileName);
		// Path filePath = Paths.get("resource/" + _fileName);
		try {
			List<String> lines = Files.readAllLines(filePath, utf8);
			for (String line : lines) {
				line = StringUtils.strip(line);
				parse(line);
			}
			mInitialized = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// used for debug
	int _lineIndex = 0;

	private void parse(String line) {
		// 混淆品：关白附：为毛茛科植物黄花乌头的块根。处方别名：关白附、竹节白附
		String ignorePrefix = "混淆品：";
		if (line.startsWith(ignorePrefix)) {
			parse(line.substring(ignorePrefix.length()));
			return;
		}
		_lineIndex += 1;
		if (line.isEmpty()) {
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
		String[] temp = aliaString.split("、");
		List<String> alias = new ArrayList<String>();
		for (String item : temp) {
			item = getHerb(item);
			if (item == name) {
				continue;
			}
			alias.add(item);
		}
		mAliasStorage.put(name, alias);
	}

	private String getHerb(String herb) {
		return herb.replaceAll("\\s", "");
	}

	public String getStandardName(String alias) {
		String standardName = getStandardName(alias, mHitCache);
		if (!standardName.isEmpty()) {
			return standardName;
		}

		standardName = getStandardName(alias, mAliasStorage);

		if (!standardName.isEmpty()) {
			List<String> cache = mHitCache.get(standardName);
			if (cache == null) {
				cache = new ArrayList<String>();
				mHitCache.put(standardName, cache);
			}
			cache.add(alias);

			return standardName;
		}

		mUnknownHerbs.add(alias);
		return alias;
	}

	public void trace() {
		try {
			traceCache();
			traceUnknownHerb();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void traceCache() throws IOException {
		BufferedWriter bw = null;
		FileWriter fw = null;

		fw = new FileWriter("resource/Herb/used.txt");
		bw = new BufferedWriter(fw);

		for (String herb : mHitCache.keySet()) {
			bw.write(herb + ": ");
			boolean first = true;

			for (String alias : mHitCache.get(herb)) {
				if (!first) {
					bw.write("、 " + alias);
				} else {
					bw.write(alias);
				}
				first = false;
			}
			bw.write("\r\n");
		}

		bw.close();
		fw.close();
	}

	private void traceUnknownHerb() throws IOException {
		BufferedWriter bw = null;
		FileWriter fw = null;

		fw = new FileWriter("resource/Herb/Unknow.txt");
		bw = new BufferedWriter(fw);

		for (String herb : mUnknownHerbs) {
			bw.write(herb + "\r\n");
		}

		bw.close();
		fw.close();
	}

	private String getStandardName(String herb, HashMap<String, List<String>> container) {
		if (container.containsKey(herb)) {
			return herb;
		}

		for (String key : container.keySet()) {
			List<String> alias = container.get(key);
			for (String temp : alias) {
				if (temp.equals(herb)) {
					return key;
				}
			}
		}
		return "";
	}

	// private String _fileName = "常用中药处方别名.txt";
	private boolean mInitialized = false;
	private HashMap<String, List<String>> mAliasStorage = new HashMap<String, List<String>>();
	private HashMap<String, List<String>> mHitCache = new HashMap<String, List<String>>();
	private HashSet<String> mUnknownHerbs = new HashSet<String>();
}
