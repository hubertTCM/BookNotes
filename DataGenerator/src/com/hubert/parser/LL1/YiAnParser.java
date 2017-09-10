package com.hubert.parser.LL1;

import java.util.*;

import com.hubert.parser.AST.ASTNode;
import com.hubert.parser.tokenextractor.*;

//reference: http://pandolia.net/tinyc/ch10_top_down_parse.html
public class YiAnParser {
	public YiAnParser() {
		mTokenExtractor.put("FormattedRecipeText", new PrescriptionItemTokenExtractor());
	}

	// （1） 将结束符 $ 和起始符号 S 压入栈中；
	// （2） 从输入流中读入下一个终结符，赋给 a ，也就是执行一次 a = yylex() ；
	// （3） 设栈顶符号为 X ，有以下三种情况：
	// 情况 A ： X == a 且 a == $ ，解析成功，终止解析；
	// 情况 B ： X == a 且 a != $ ，执行 Match 动作，将 X 出栈，转到（2）；
	// 情况 C ： X != a 且 X 是非终结符，有两种情况：
	// 情况 C1 ： M[X, a] = “X -> u”，执行 Predict 动作，将 X 出栈，压入 u ，转到（3）；
	// 情况 C2 ： M[X, a] 未定义，输入不合语法，终止解析；
	// 情况 D ： X != a 且 X 是终结符，输入不合语法，终止解析。
	public ASTNode parse(Grammar grammar, List<Token> tokens) throws Exception {
		mTokens.addAll(tokens);
		mGrammar = grammar;
		mRoot = new ASTNode("Root");
		parseInternal();
		return mRoot;
	}

	private void parseInternal() throws Exception {
		mNodeStack.push(new ASTNode(Constants.End));
		ASTNode start = new ASTNode(TokenType.S.name());
		mRoot.addChild(start);
		mNodeStack.push(start);

		while (mCurrentTokenIndex < mTokens.size()) {
			ASTNode node = mNodeStack.pop();
			String tag = node.getTag();
			if (tag.equals(Constants.Empty)) {
				node.remove();
				continue;
			}

			Token token = mTokens.get(mCurrentTokenIndex);
			String tokenType = token.getType().name();
			System.out.println(token.getType() + ":" + token.getValue());
			if (tag.equals(tokenType)) {
				if (tokenType.equals(Constants.End)) {
					mCurrentTokenIndex += 1;
					// TODO:
					if (mCurrentTokenIndex < mTokens.size()) {
						parseInternal();
					}
					return;
				}
				match(node, token);
				mCurrentTokenIndex += 1;
				continue;
			}

			if (tokenType.equals(TokenType.LiteralText.name())) {
				if (!tag.startsWith(tokenType)) {
					predict(node, token);
					continue;
				}
				
				//TODO: Change LiteralText(FormattedRecipeText) to LiteralText(RecipeContent)
				//      in grammar, invoke getStandardSymbol twice is weird.
				// LiteralText(FormattedRecipeText) => FormattedRecipeText
				String standardSymbol = mGrammar.getStandardSymbol(tag);
				mTokens.remove(mCurrentTokenIndex);
				if (mTokenExtractor.containsKey(standardSymbol)) {
					ArrayList<Token> temp = new ArrayList<Token>();
					mTokenExtractor.get(standardSymbol).extract(token.getValue(), temp);
					mTokens.addAll(mCurrentTokenIndex, temp);
				} else {
					mTokens.add(mCurrentTokenIndex, new Token(TokenType.valueOf(standardSymbol), token.getValue()));
				}
				// FormattedRecipeText => RecipeContent
				standardSymbol = mGrammar.getStandardSymbol(standardSymbol);
				ASTNode tempNode = new ASTNode(standardSymbol);
				ASTNode parent = node.getParent();
				parent.replaceChild(node, tempNode);
				mNodeStack.push(tempNode);
				continue;
			}

			if (mGrammar.getIsTerminalSymbol(tag)) {
				throw new Exception("Invalid tokenType:" + tokenType + " expect:" + tag + " value:" + token.getValue());
			}

			predict(node, token);
		}
	}

	private void match(ASTNode node, Token token) {
		node.setValue(token.getValue());
	}

	private void predict(ASTNode node, Token token) throws Exception {
		String tokenType = token.getType().name();
		String tag = node.getTag();
		List<String> action = mGrammar.getAction(tag, tokenType);
		if (action == null) {
			throw new Exception("Invalid tokenType:" + tokenType + " tag:" + tag + " value:" + token.getValue());
		}

		int index = node.childCount();
		for (int i = action.size() - 1; i >= 0; --i) {
			ASTNode child = new ASTNode(action.get(i));
			node.addChild(index, child);
			mNodeStack.push(child);
		}
	}

	private int mCurrentTokenIndex = 0;
	private ArrayList<Token> mTokens = new ArrayList<Token>();
	private Map<String, ITokenExtractor> mTokenExtractor = new HashMap<String, ITokenExtractor>();
	private Grammar mGrammar;
	private ASTNode mRoot = null;
	private Stack<ASTNode> mNodeStack = new Stack<ASTNode>();
}
