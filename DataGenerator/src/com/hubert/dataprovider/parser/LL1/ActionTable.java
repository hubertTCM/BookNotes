package com.hubert.dataprovider.parser.LL1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.*;

public class ActionTable {

	public void addAction(String symbol, String input, List<String> production) throws Exception {
		String key = getActionKey(symbol, input);
		if (mMoveAction.containsKey(key)) {
			List<String> existingValue = mMoveAction.get(key);

			if (existingValue.size() == production.size() && existingValue.containsAll(production)) {
				return;
			}

			throw new Exception("Not LL(1) grammar " + key + " => " + convert(existingValue) + "; " + key + " => "
					+ convert(production));
		}
		mMoveAction.put(key, production);
		return;
	}

	public List<String> getAction(String symbol, String input) {
		String key = getActionKey(symbol, input);
		if (mMoveAction.containsKey(key)) {
			return mMoveAction.get(key);
		}
		return null;
	}

	public void dump() {

		for (Map.Entry<String, List<String>> kvp : mMoveAction.entrySet()) {
			System.out.print(kvp.getKey() + " => ");
			for (String temp : kvp.getValue()) {
				System.out.print(" " + temp);
			}
			System.out.print(" \n");
		}

	}

	private String convert(List<String> source) {
		String to = "";
		for (String temp : source) {
			to += " " + temp;
		}
		return to += " ";
	}

	private String getActionKey(String symbol, String input) {
		return "M[" + symbol + "," + input + "]";
	}

	// M[A, a] = “A->u”
	private Map<String, List<String>> mMoveAction = new HashMap<String, List<String>>();
}
