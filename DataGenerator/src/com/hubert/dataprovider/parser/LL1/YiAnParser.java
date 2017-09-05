package com.hubert.dataprovider.parser.LL1;

import java.util.*;

import com.hubert.dataprovider.parser.tokenextractor.*;

//reference: http://pandolia.net/tinyc/ch10_top_down_parse.html
public class YiAnParser {

	// （1） 将结束符 $ 和起始符号 S 压入栈中；
	// （2） 从输入流中读入下一个终结符，赋给 a ，也就是执行一次 a = yylex() ；
	// （3） 设栈顶符号为 X ，有以下三种情况：
	// 		情况 A ： X == a 且 a == $ ，解析成功，终止解析；
	// 		情况 B ： X == a 且 a != $ ，执行 Match 动作，将 X 出栈，转到（2）；
	// 		情况 C ： X != a 且 X 是非终结符，有两种情况：
	// 			情况 C1 ： M[X, a] = “X -> u”，执行 Predict 动作，将 X 出栈，压入 u ，转到（3）；
	// 			情况 C2 ： M[X, a] 未定义，输入不合语法，终止解析；
	// 		情况 D ： X != a 且 X 是终结符，输入不合语法，终止解析。
	public void parse(ActionTable actionTable, List<Token> tokens) {
		mTokens.addAll(tokens);
		mActionTable = actionTable;
		mNodeStack.push(new ASTNode(new Token(TokenType.End, "")));
		mRoot = new ASTNode(new Token(TokenType.Start, ""));
		mNodeStack.push(mRoot);
	}

	private int mCurrentTokenIndex = 0;
	private ArrayList<Token> mTokens = new ArrayList<Token>();
	private ActionTable mActionTable;
	private ASTNode mRoot = null;
	private Stack<ASTNode> mNodeStack = new Stack<ASTNode>();
}
