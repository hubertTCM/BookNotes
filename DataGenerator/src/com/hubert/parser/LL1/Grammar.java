package com.hubert.parser.LL1;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;

import com.hubert.parser.tokenextractor.Constants;

// reference: http://pandolia.net/tinyc/ch10_top_down_parse.html
public class Grammar {
    public Grammar(String grammarFile) throws Exception {
        this(grammarFile, false);
    }

    public Grammar(String grammarFile, boolean enableTrace) throws Exception {
        mEnableTrace = enableTrace;
        parseXml(grammarFile);
    }

    public Grammar(List<String> terminals, List<String> expressions) throws Exception {
        this(terminals, expressions, false);
    }

    public Grammar(List<String> terminals, List<String> expressions, boolean enableTrace) throws Exception {
        initTerminalSymbols();
        mTerminalSymbols.addAll(terminals);

        ArrayList<String> temp = new ArrayList<String>();
        temp.add("// Start of Grammar");
        temp.addAll(expressions);
        temp.add("// End of Grammar");
        initExpressions(temp);
    }

    public List<String> getAction(String symbol, String input) {
        return mActionTable.getAction(symbol, input);
    }

    public String getStandardSymbol(String from) {
        if (mSymbolMap.containsKey(from)) {
            return mSymbolMap.get(from);
        }
        return from;
    }

    public boolean getIsTerminalSymbol(String symbol) {
        return mTerminalSymbols.contains(symbol);
    }

    // TODO: Read terminal symbols from config file
    private void initTerminalSymbols() {
        mTerminalSymbols.add("Description");
        mTerminalSymbols.add("Abbreviation");
        mTerminalSymbols.add("RecipeHeader");
        mTerminalSymbols.add("RecipeComment");
        mTerminalSymbols.add("Herb");

        mTerminalSymbols.add("LiteralText(FormattedRecipeText)");
        mTerminalSymbols.add("LiteralText(RecipeComment)");
        mTerminalSymbols.add("SectionName");
        mTerminalSymbols.add("Empty");
    }

    private void parseXml(String filePath) throws Exception {

        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        Node root = doc.getFirstChild();
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element element = (Element) node;
            String[] lines = element.getTextContent().split("\n");
            String nodeName = node.getNodeName();
            if (nodeName.equals("Expressions")) {
                initExpressions(Arrays.asList(lines));
                continue;
            }
            if (nodeName.equals("SymbolMap")) {
                initSymbolMap(Arrays.asList(lines));
            }
            if (nodeName.equals("TerminalSymbols")) {
                for (String item : lines) {
                    item = StringUtils.strip(item);
                    if (item.isEmpty()) {
                        continue;
                    }
                    mTerminalSymbols.add(item);
                }
                continue;
            }
        }

        initFirstSet();
        initFollowSet();
        initActionTable();

        dump();
    }

    private void initSymbolMap(List<String> lines) {
        for (String line : lines) {
            line = StringUtils.strip(line);
            if (line.isEmpty()) {
                continue;
            }
            String[] array = line.split(mSplitter);
            mSymbolMap.put(StringUtils.strip(array[0]), StringUtils.strip(array[1]));
        }
    }

    private void initExpressions(List<String> lines) throws Exception {
        String currentNonterminalSymbol = "";
        for (String temp : lines) {
            String line = StringUtils.strip(temp);
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("//")) {
                continue;
            }
            // System.out.println(line);
            int index = line.indexOf(mSplitter);
            String productExpression = line;
            if (index > 0) {
                currentNonterminalSymbol = line.substring(0, index);
                currentNonterminalSymbol = StringUtils.strip(currentNonterminalSymbol);

                productExpression = line.substring(index + mSplitter.length());
                productExpression = StringUtils.strip(productExpression);
            }
            AddProductExpression(currentNonterminalSymbol, productExpression);
        }
    }

    private void AddProductExpression(String symbol, String expression) {
        if (symbol.isEmpty() || expression.isEmpty()) {
            // System.out.println("### Grammar error");
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

    private void initFollowSet() throws Exception {
        boolean isChanged = true;
        while (isChanged) {
            isChanged = false;

            for (Map.Entry<String, List<List<String>>> kvp : mProduction.entrySet()) {
                Set<String> original = new HashSet<String>();
                String symbol = kvp.getKey();
                if (mFollow.containsKey(symbol)) {
                    original.addAll(mFollow.get(symbol));
                }
                calculateFollowSet(symbol);

                if (!mCalculatingFollowSetSymbols.isEmpty()) {
                    throw new Exception("Follow set calculation error");
                }
                Set<String> current = mFollow.get(symbol);
                if (original.size() != current.size() || !original.containsAll(current)) {
                    isChanged = true;
                }
            }
        }
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
        // System.out.println(symbol);
        Set<String> first = new HashSet<String>();
        mFirst.put(symbol, first);
        if (mTerminalSymbols.contains(symbol)) {
            first.add(symbol);
            return first;
        }
        List<List<String>> productions = mProduction.get(symbol);
        for (List<String> production : productions) {
            first.addAll(calculateFirst(production));
        }

        return first;
    }

    private Set<String> calculateFirst(List<String> production) {
        Set<String> first = new HashSet<String>();
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
        return first;
    }

    private Set<String> getFollowSet(String symbol) {
        if (mFollow.containsKey(symbol)) {
            return mFollow.get(symbol);
        }
        return calculateFollowSet(symbol);
    }

    // 一个语法中所有非终结符的 follow set 的计算步骤如下：
    // （1） 将$加入到Follow(S)中， S为起始符号， $为结束符EOF；
    // （2） 对每条形如A->uBv的产生式，将First(v)-ε加入到 Follow(B)；
    // （3） 对每条形如A->uB的产生式，或A->uBv的产生式（其中 First(v)含 ε ），将Follow(A)加入到 Follow(B)
    private Set<String> calculateFollowSet(String symbol) {
        mCalculatingFollowSetSymbols.add(symbol);
        Set<String> follow = null;
        if (mFollow.containsKey(symbol)) {
            follow = mFollow.get(symbol);
        } else {
            follow = new HashSet<String>();
        }

        if (symbol.equals(Constants.Start)) {
            follow.add(Constants.End);
        }

        for (Map.Entry<String, List<List<String>>> kvp : mProduction.entrySet()) {
            List<List<String>> productions = kvp.getValue();
            for (List<String> production : productions) {
                int followSymbolIndex = production.indexOf(symbol);
                if (followSymbolIndex < 0) {
                    continue;
                }
                followSymbolIndex += 1;
                while (followSymbolIndex < production.size() + 1) {
                    if (followSymbolIndex == production.size()) {
                        if (!mCalculatingFollowSetSymbols.contains(kvp.getKey())) {
                            follow.addAll(getFollowSet(kvp.getKey()));
                        }
                        break;
                    }

                    Set<String> temp = new HashSet<String>();
                    temp.addAll(getFirstSet(production.get(followSymbolIndex)));
                    if (!temp.contains(Constants.Empty)) {
                        follow.addAll(temp);
                        break;
                    }
                    temp.remove(Constants.Empty);
                    follow.addAll(temp);
                    followSymbolIndex += 1;
                }

            }
        }
        mFollow.put(symbol, follow);
        mCalculatingFollowSetSymbols.remove(symbol);
        return follow;
    }

    private void initActionTable() throws Exception {
        // 对语法中的每条产生式： A->u ：
        // （1）对First(u)中的所有终结符a（不含 ε ），置 M[A, a] = “A->u” ；
        // （2）若First(u)含ε，则对Follow(A)中的所有符号a（可含 $ ），置 M[A, a]=“A->u”。
        for (Map.Entry<String, List<List<String>>> kvp : mProduction.entrySet()) {
            for (List<String> production : kvp.getValue()) {
                String symbol = production.get(0);
                Set<String> first = getFirstSet(symbol);
                for (String temp : first) {
                    addAction(kvp.getKey(), temp, production);
                }
            }
        }
    }

    private void addAction(String symbol, String input, List<String> production) throws Exception {
        // 若 First(u) 含 ε ，则对 Follow(A) 中的所有符号 a （可含 $ ），置 M[A, a] = “A->u”
        if (input.equals(Constants.Empty)) {
            Set<String> follow = getFollowSet(symbol);
            for (String key : follow) {
                mActionTable.addAction(symbol, key, production);
            }
        } else {
            mActionTable.addAction(symbol, input, production);
        }
    }

    private void dump() {
        if (!mEnableTrace) {
            return;
        }
        System.out.println("First");
        for (Map.Entry<String, Set<String>> kvp : mFirst.entrySet()) {
            System.out.print("First(" + kvp.getKey() + ") = {");
            for (String temp : kvp.getValue()) {
                System.out.print(" " + temp);
            }
            System.out.print(" }\n");
        }

        System.out.println("Follow");
        for (Map.Entry<String, Set<String>> kvp : mFollow.entrySet()) {
            System.out.print("Follow(" + kvp.getKey() + ") = {");
            for (String temp : kvp.getValue()) {
                System.out.print(" " + temp);
            }
            System.out.print(" }\n");
        }
        mActionTable.dump();
    }

    private Set<String> mTerminalSymbols = new HashSet<String>();
    private Map<String, String> mSymbolMap = new HashMap<String, String>();
    private String mSplitter = ":=";

    private boolean mEnableTrace = false;
    // S := abc saved as: S => [a, b, c]
    private Map<String, List<List<String>>> mProduction = new HashMap<String, List<List<String>>>();
    private Map<String, Set<String>> mFirst = new HashMap<String, Set<String>>();
    private Map<String, Set<String>> mFollow = new HashMap<String, Set<String>>();
    private List<String> mCalculatingFollowSetSymbols = new ArrayList<String>();
    private ActionTable mActionTable = new ActionTable();
}
