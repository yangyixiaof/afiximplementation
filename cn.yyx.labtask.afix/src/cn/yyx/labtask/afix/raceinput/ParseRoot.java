package cn.yyx.labtask.afix.raceinput;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import cn.yyx.labtask.afix.rvparse.RvBaseVisitor;
import cn.yyx.labtask.afix.rvparse.RvLexer;
import cn.yyx.labtask.afix.rvparse.RvParser;

public class ParseRoot 
{
	public static String ParseOneSentence(String onesentence, RvBaseVisitor<Integer> evalVisitor, boolean returnTreeContent) throws Exception {
		ANTLRInputStream input = new ANTLRInputStream(onesentence);
		RvLexer lexer = new RvLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		RvParser parser = new RvParser(tokens);
		parser.setBuildParseTree(true); // tell ANTLR to build a parse tree
		ParseTree tree = parser.oneRace(); // parse
		if (evalVisitor != null)
		{
			evalVisitor.visit(tree);
		}
		if (returnTreeContent)
		{
			return tree.toStringTree(parser);
		}
		return null;
    }
}