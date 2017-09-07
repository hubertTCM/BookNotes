package com.hubert.dataprovider.parser.LL1;

import java.util.*;

import com.hubert.dataprovider.parser.tokenextractor.*;

//reference: http://pandolia.net/tinyc/ch10_top_down_parse.html
public class YiAnParser {
	public YiAnParser(){
		mTokenExtractor.put("FormattedRecipeText", new FormattedPrescriptionExtractor(new PrescriptionItemTokenExtractor()));
	}

	// （1） 将结束符 $ 和起始符号 S 压入栈中；
	// （2） 从输入流中读入下一个终结符，赋给 a ，也就是执行一次 a = yylex() ；
	// （3） 设栈顶符号为 X ，有以下三种情况：
	// 		情况 A ： X == a 且 a == $ ，解析成功，终止解析；
	// 		情况 B ： X == a 且 a != $ ，执行 Match 动作，将 X 出栈，转到（2）；
	// 		情况 C ： X != a 且 X 是非终结符，有两种情况：
	// 			情况 C1 ： M[X, a] = “X -> u”，执行 Predict 动作，将 X 出栈，压入 u ，转到（3）；
	// 			情况 C2 ： M[X, a] 未定义，输入不合语法，终止解析；
	// 		情况 D ： X != a 且 X 是终结符，输入不合语法，终止解析。
	public void parse(Grammar grammar, List<Token> tokens) throws Exception {
		mTokens.addAll(tokens);
		mGrammar = grammar;
		mNodeStack.push(new ASTNode(TokenType.End.name(), ""));
		mRoot = new ASTNode(TokenType.Start.name(), "");
		mNodeStack.push(mRoot);

		while (mCurrentTokenIndex < mTokens.size()) {
			Token token = mTokens.get(mCurrentTokenIndex);
			ASTNode node = mNodeStack.pop();
			String tag = node.getTag();
			TokenType tokenType = token.getType();

			if (tag.equals(tokenType.name())) {
				if (tokenType == TokenType.End) {
					// TODO:
					break;
				}
				// TODO:
				match(node, token);
				mCurrentTokenIndex += 1;
				continue;
			}

			if (mGrammar.getIsTerminalSymbol(tag)) {
				throw new Exception(
						"Invalid tokenType:" + tokenType.name() + " expect:" + tag + " value:" + token.getValue());
			}

			if (tokenType == TokenType.LiteralText) {
				// LiteralText(FormattedRecipeText) => FormattedRecipeText
				String standardSymbol = mGrammar.getStandardSymbol(tag);
				if (mTokenExtractor.containsKey(standardSymbol)) {
					ArrayList<Token> temp = new ArrayList<Token>();
					mTokenExtractor.get(standardSymbol).extract(token.getValue(), temp);
					mTokens.addAll(mCurrentTokenIndex, temp);
				}
				// FormattedRecipeText => RecipeContent
				standardSymbol = mGrammar.getStandardSymbol(standardSymbol);
				ASTNode tempNode = new ASTNode(standardSymbol);
				node.getParent().replaceChild(node, tempNode);
				continue;
			}

			List<String> action = mGrammar.getAction(tag, tokenType.name());
			if (action == null) {
				throw new Exception(
						"Invalid tokenType:" + tokenType.name() + " tag:" + tag + " value:" + token.getValue());
			}
			
			for(int i = action.size() - 1; i >= 0; --i){
				mNodeStack.push(new ASTNode(action.get(i)));	
			}
		}
	}

	private void match(ASTNode node, Token token) {
		node.setValue(token.getValue());
	}

	private int mCurrentTokenIndex = 0;
	private ArrayList<Token> mTokens = new ArrayList<Token>();
	private Map<String, ITokenExtractor> mTokenExtractor = new HashMap<String, ITokenExtractor>();
	private Grammar mGrammar;
	private ASTNode mRoot = null;
	private Stack<ASTNode> mNodeStack = new Stack<ASTNode>();
}
