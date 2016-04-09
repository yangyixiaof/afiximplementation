package cn.yyx.labtask.afix.rvparse;

// Generated from Rv.g4 by ANTLR 4.5.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RvParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RvVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RvParser#oneRaceReadPart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneRaceReadPart(RvParser.OneRaceReadPartContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#oneRaceWritePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneRaceWritePart(RvParser.OneRaceWritePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#oneRacePart}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneRacePart(RvParser.OneRacePartContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#oneRace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOneRace(RvParser.OneRaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#classDeclare}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclare(RvParser.ClassDeclareContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#variableType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableType(RvParser.VariableTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(RvParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#returnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnType(RvParser.ReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#methodSig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodSig(RvParser.MethodSigContext ctx);
	/**
	 * Visit a parse tree produced by {@link RvParser#lineNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLineNumber(RvParser.LineNumberContext ctx);
}