package com.hubert.dataprovider.parser.tokenextractor;

import java.util.*;

import com.hubert.dataprovider.parser.*;

//import javafx.util.Pair;

public interface ITokenExtractor {
	//public Pair<Token, String> extract(String source);
	//public Collection<Token> extract(String source);
	public boolean extract(String text, Collection<Token> container);
}
