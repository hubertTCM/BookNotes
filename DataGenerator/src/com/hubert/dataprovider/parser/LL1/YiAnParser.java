package com.hubert.dataprovider.parser.LL1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.hubert.dal.entity.*;
import com.hubert.dataprovider.parser.tokenextractor.*;

// reference: http://pandolia.net/tinyc/ch10_top_down_parse.html
public class YiAnParser {
	public YiAnParser(String grammarFile) throws Exception {
		mTerminalSymbols.add("Description");
		mTerminalSymbols.add("Abbreviation");
		mTerminalSymbols.add("RecipeHeader");
		mTerminalSymbols.add("RecipeComment");
		mTerminalSymbols.add("Herb");

		mTerminalSymbols.add("Unknown(FormattedRecipeText)");
		mTerminalSymbols.add("Unknown(RecipeComment)");
		mTerminalSymbols.add("SectionName");
		mTerminalSymbols.add("Empty");

		initGrammar(grammarFile);
	}

	public ASTNode parse(List<Token> tokens) throws IOException {
		reset();

		return null;
	}

	private void reset() {
		mTokenStack.clear();
		mNodeStack.clear();
	}

	private void initGrammar(String grammaFile) throws Exception {
		Path filePath = Paths.get(grammaFile);
		Charset utf8 = Charset.forName("UTF-8");
		List<String> lines = Files.readAllLines(filePath, utf8);

		boolean isGrammerSection = false;
		String currentNonterminalSymbol = "";
		for (String temp : lines) {
			String line = StringUtils.strip(temp);
			if (line.startsWith("// End of Grammar")) {
				break;
			}

			if (line.startsWith("// Start of Grammar")) {
				isGrammerSection = true;
				continue;
			}
			if (!isGrammerSection || line.isEmpty()) {
				continue;
			}
			System.out.println(line);
			String splitter = ":=";
			int index = line.indexOf(":=");
			String productExpression = line;
			if (index > 0) {
				currentNonterminalSymbol = line.substring(0, index);
				currentNonterminalSymbol = StringUtils.strip(currentNonterminalSymbol);

				productExpression = line.substring(index + splitter.length());
				productExpression = StringUtils.strip(productExpression);
			}
			AddProductExpression(currentNonterminalSymbol, productExpression);
		}

		initFirstSet();
		initFollowSet();
		initActionTable();
	}

	private void AddProductExpression(String symbol, String expression) {
		if (symbol.isEmpty() || expression.isEmpty()) {
			System.out.println("### Grammar error");
			return;
		}

		List<List<String>> productExpressions = null;
		if (mProduction.containsKey(symbol)) {
			productExpressions = mProduction.get(symbol);
		} else {
			productExpressions = new ArrayList<List<String>>();
			mProduction.put(symbol, productExpressions);
		}
		if (expression.indexOf(" ") > 0) {
			productExpressions.add(Arrays.asList(expression.split(" ")));
		} else {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(expression);
			productExpressions.add(temp);
		}
	}

	// reference: http://pandolia.net/tinyc/ch10_top_down_parse.html
	private void initFirstSet() {
		for (Map.Entry<String, List<List<String>>> kvp : mProduction.entrySet()) {
			calculateFirstSet(kvp.getKey());
		}
	}

	private void initFollowSet() {
		// TODO:
	}

	private Set<String> getFirstSet(String symbol) {
		if (mFirst.containsKey(symbol)) {
			return mFirst.get(symbol);
		}
		return calculateFirstSet(symbol);
	}

	private Set<String> calculateFirstSet(String symbol) {
		if (mFirst.containsKey(symbol)) {
			return mFirst.get(symbol);
		}
		Set<String> first = new HashSet<String>();
		List<List<String>> productions = mProduction.get(symbol);
		System.out.println(symbol);
		for (List<String> production : productions) {
			int index = 0;
			for (index = 0; index < production.size(); ++index) {
				String temp = production.get(index);
				if (mTerminalSymbols.contains(temp)) {
					first.add(temp);
					break;
				}
				Set<String> tempFirst = calculateFirstSet(temp);
				first.addAll(tempFirst);
				if (!tempFirst.contains(Constants.Empty)) {
					break;
				}

				if (index != production.size() - 1) {
					first.remove(Constants.Empty);
				}
			}
		}

		mFirst.put(symbol, first);
		return first;
	}

	private Set<String> getFollowSet(String symbol) {
		if (mFollow.containsKey(symbol)) {
			return mFollow.get(symbol);
		}
		return calculateFollowSet(symbol);
	}

	// 一个语法中所有非终结符的 follow set 的计算步骤如下：
	// （1） 将 $ 加入到 Follow(S) 中， S 为起始符号， $ 为结束符 EOF ；
	// （2） 对每条形如 A -> u B v 的产生式，将 First(v) - ε 加入到 Follow(B) ；
	// （3） 对每条形如 A -> u B 的产生式，或 A -> u B v 的产生式（其中 First(v) 含 ε ），将 Follow(A)
	// 加入到 Follow(B) 。
	private Set<String> calculateFollowSet(String symbol) {
		// TODO:
		Set<String> follow = new HashSet<String>();
		return follow;
	}

	private void initActionTable() throws Exception {
		// 对语法中的每条产生式： A -> u ：
		// （1） 对 First(u) 中的所有终结符 a （不含 ε ），置 M[A, a] = “A -> u” ；
		// （2） 若 First(u) 含 ε ，则对 Follow(A) 中的所有符号 a （可含 $ ），置 M[A, a] = “A->u”
		// 。

		for (Map.Entry<String, List<List<String>>> kvp : mProduction.entrySet()) {
			for (List<String> production : kvp.getValue()) {
				String symbol = production.get(0);
				if (mTerminalSymbols.contains(symbol)) {
					addAction(kvp.getKey(), symbol, production);
					continue;
				}
				Set<String> first = getFirstSet(symbol);
				for (String temp : first) {
					addAction(kvp.getKey(), temp, production);
				}
			}
		}
	}

	private void addAction(String symbol, String input, List<String> production) throws Exception {
		mMoveAction.addAction(symbol, input, production);
		// 若 First(u) 含 ε ，则对 Follow(A) 中的所有符号 a （可含 $ ），置 M[A, a] = “A->u”
		if (input.equals(Constants.Empty)) {
			Set<String> follow = getFollowSet(symbol);
			for (String key : follow) {
				mMoveAction.addAction(symbol, key, production);
			}
		}

	}

	private Set<String> mTerminalSymbols = new HashSet<String>();
	// S := abc saved as:
	// S => [a, b, c]
	private Map<String, List<List<String>>> mProduction = new HashMap<String, List<List<String>>>();
	private Map<String, Set<String>> mFirst = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> mFollow = new HashMap<String, Set<String>>();
	private ActionTable mMoveAction = new ActionTable();

	private Stack<Token> mTokenStack = new Stack<Token>();
	private Stack<ASTNode> mNodeStack = new Stack<ASTNode>();
}
