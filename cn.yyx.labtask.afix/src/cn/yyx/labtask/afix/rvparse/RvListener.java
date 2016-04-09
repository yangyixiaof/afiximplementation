package cn.yyx.labtask.afix.rvparse;

// Generated from Rv.g4 by ANTLR 4.5.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RvParser}.
 */
public interface RvListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RvParser#oneRaceReadPart}.
	 * @param ctx the parse tree
	 */
	void enterOneRaceReadPart(RvParser.OneRaceReadPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#oneRaceReadPart}.
	 * @param ctx the parse tree
	 */
	void exitOneRaceReadPart(RvParser.OneRaceReadPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#oneRaceWritePart}.
	 * @param ctx the parse tree
	 */
	void enterOneRaceWritePart(RvParser.OneRaceWritePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#oneRaceWritePart}.
	 * @param ctx the parse tree
	 */
	void exitOneRaceWritePart(RvParser.OneRaceWritePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#oneRacePart}.
	 * @param ctx the parse tree
	 */
	void enterOneRacePart(RvParser.OneRacePartContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#oneRacePart}.
	 * @param ctx the parse tree
	 */
	void exitOneRacePart(RvParser.OneRacePartContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#oneRace}.
	 * @param ctx the parse tree
	 */
	void enterOneRace(RvParser.OneRaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#oneRace}.
	 * @param ctx the parse tree
	 */
	void exitOneRace(RvParser.OneRaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#classDeclare}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclare(RvParser.ClassDeclareContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#classDeclare}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclare(RvParser.ClassDeclareContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#variableType}.
	 * @param ctx the parse tree
	 */
	void enterVariableType(RvParser.VariableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#variableType}.
	 * @param ctx the parse tree
	 */
	void exitVariableType(RvParser.VariableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(RvParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(RvParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(RvParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(RvParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#methodSig}.
	 * @param ctx the parse tree
	 */
	void enterMethodSig(RvParser.MethodSigContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#methodSig}.
	 * @param ctx the parse tree
	 */
	void exitMethodSig(RvParser.MethodSigContext ctx);
	/**
	 * Enter a parse tree produced by {@link RvParser#lineNumber}.
	 * @param ctx the parse tree
	 */
	void enterLineNumber(RvParser.LineNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link RvParser#lineNumber}.
	 * @param ctx the parse tree
	 */
	void exitLineNumber(RvParser.LineNumberContext ctx);
}