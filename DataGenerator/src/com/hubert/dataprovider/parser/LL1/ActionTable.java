package com.hubert.dataprovider.parser.LL1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionTable {

	public void addAction(String symbol, String input, List<String> production) throws Exception {
		String key = getActionKey(symbol, input);
		if (mMoveAction.containsKey(key)) {
			throw new Exception(key + " Not LL(1) grammar");
		}
		mMoveAction.put(key, production);
		return ;
	}
	
	public List<String> getAction(String symbol, String input){
		String key = getActionKey(symbol, input);
		if (mMoveAction.containsKey(key)){
			return mMoveAction.get(key);
		}
		return null;
	}

	private String getActionKey(String symbol, String input){
		return symbol + "####" + input;
	}

	//M[A, a] = “A->u”
	private Map<String, List<String>> mMoveAction = new HashMap<String, List<String>>();
}
