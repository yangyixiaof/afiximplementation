package cn.yyx.labtask.rvparse;

// Generated from Rv.g4 by ANTLR 4.5.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RvParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, Number=2, ID=3, WS=4, RH=5, COMMA=6, LA=7, RA=8, ASSIGN=9, OR=10;
	public static final int
		RULE_oneRaceReadPart = 0, RULE_oneRaceWritePart = 1, RULE_oneRacePart = 2, 
		RULE_oneRace = 3, RULE_classDeclare = 4, RULE_variableType = 5, RULE_variable = 6, 
		RULE_returnType = 7, RULE_methodSig = 8, RULE_lineNumber = 9;
	public static final String[] ruleNames = {
		"oneRaceReadPart", "oneRaceWritePart", "oneRacePart", "oneRace", "classDeclare", 
		"variableType", "variable", "returnType", "methodSig", "lineNumber"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'-'", null, null, null, "'Race'", "':'", "'<'", "'>'", "'='", "'|'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, "Number", "ID", "WS", "RH", "COMMA", "LA", "RA", "ASSIGN", 
		"OR"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Rv.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RvParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class OneRaceReadPartContext extends ParserRuleContext {
		public TerminalNode RH() { return getToken(RvParser.RH, 0); }
		public List<TerminalNode> COMMA() { return getTokens(RvParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(RvParser.COMMA, i);
		}
		public TerminalNode LA() { return getToken(RvParser.LA, 0); }
		public List<ClassDeclareContext> classDeclare() {
			return getRuleContexts(ClassDeclareContext.class);
		}
		public ClassDeclareContext classDeclare(int i) {
			return getRuleContext(ClassDeclareContext.class,i);
		}
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public MethodSigContext methodSig() {
			return getRuleContext(MethodSigContext.class,0);
		}
		public List<TerminalNode> RA() { return getTokens(RvParser.RA); }
		public TerminalNode RA(int i) {
			return getToken(RvParser.RA, i);
		}
		public List<TerminalNode> OR() { return getTokens(RvParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(RvParser.OR, i);
		}
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(RvParser.ASSIGN, 0); }
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public LineNumberContext lineNumber() {
			return getRuleContext(LineNumberContext.class,0);
		}
		public OneRaceReadPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneRaceReadPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterOneRaceReadPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitOneRaceReadPart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitOneRaceReadPart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OneRaceReadPartContext oneRaceReadPart() throws RecognitionException {
		OneRaceReadPartContext _localctx = new OneRaceReadPartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_oneRaceReadPart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(20);
			match(RH);
			setState(21);
			match(COMMA);
			setState(22);
			match(LA);
			setState(23);
			classDeclare();
			setState(24);
			match(COMMA);
			setState(25);
			returnType();
			setState(26);
			methodSig();
			setState(27);
			match(RA);
			setState(28);
			match(OR);
			setState(29);
			match(ID);
			setState(30);
			match(ASSIGN);
			setState(31);
			match(RA);
			setState(32);
			classDeclare();
			setState(33);
			match(COMMA);
			setState(34);
			variableType();
			setState(35);
			variable();
			setState(36);
			match(RA);
			setState(37);
			match(OR);
			setState(38);
			lineNumber();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OneRaceWritePartContext extends ParserRuleContext {
		public TerminalNode RH() { return getToken(RvParser.RH, 0); }
		public List<TerminalNode> COMMA() { return getTokens(RvParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(RvParser.COMMA, i);
		}
		public TerminalNode LA() { return getToken(RvParser.LA, 0); }
		public List<ClassDeclareContext> classDeclare() {
			return getRuleContexts(ClassDeclareContext.class);
		}
		public ClassDeclareContext classDeclare(int i) {
			return getRuleContext(ClassDeclareContext.class,i);
		}
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public MethodSigContext methodSig() {
			return getRuleContext(MethodSigContext.class,0);
		}
		public List<TerminalNode> RA() { return getTokens(RvParser.RA); }
		public TerminalNode RA(int i) {
			return getToken(RvParser.RA, i);
		}
		public List<TerminalNode> OR() { return getTokens(RvParser.OR); }
		public TerminalNode OR(int i) {
			return getToken(RvParser.OR, i);
		}
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(RvParser.ASSIGN, 0); }
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public LineNumberContext lineNumber() {
			return getRuleContext(LineNumberContext.class,0);
		}
		public OneRaceWritePartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneRaceWritePart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterOneRaceWritePart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitOneRaceWritePart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitOneRaceWritePart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OneRaceWritePartContext oneRaceWritePart() throws RecognitionException {
		OneRaceWritePartContext _localctx = new OneRaceWritePartContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_oneRaceWritePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			match(RH);
			setState(41);
			match(COMMA);
			setState(42);
			match(LA);
			setState(43);
			classDeclare();
			setState(44);
			match(COMMA);
			setState(45);
			returnType();
			setState(46);
			methodSig();
			setState(47);
			match(RA);
			setState(48);
			match(OR);
			setState(49);
			match(RA);
			setState(50);
			classDeclare();
			setState(51);
			match(COMMA);
			setState(52);
			variableType();
			setState(53);
			variable();
			setState(54);
			match(RA);
			setState(55);
			match(ASSIGN);
			setState(56);
			match(ID);
			setState(57);
			match(OR);
			setState(58);
			lineNumber();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OneRacePartContext extends ParserRuleContext {
		public OneRaceReadPartContext oneRaceReadPart() {
			return getRuleContext(OneRaceReadPartContext.class,0);
		}
		public OneRaceWritePartContext oneRaceWritePart() {
			return getRuleContext(OneRaceWritePartContext.class,0);
		}
		public OneRacePartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneRacePart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterOneRacePart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitOneRacePart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitOneRacePart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OneRacePartContext oneRacePart() throws RecognitionException {
		OneRacePartContext _localctx = new OneRacePartContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_oneRacePart);
		try {
			setState(62);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(60);
				oneRaceReadPart();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(61);
				oneRaceWritePart();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OneRaceContext extends ParserRuleContext {
		public List<OneRacePartContext> oneRacePart() {
			return getRuleContexts(OneRacePartContext.class);
		}
		public OneRacePartContext oneRacePart(int i) {
			return getRuleContext(OneRacePartContext.class,i);
		}
		public OneRaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneRace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterOneRace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitOneRace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitOneRace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OneRaceContext oneRace() throws RecognitionException {
		OneRaceContext _localctx = new OneRaceContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_oneRace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			oneRacePart();
			setState(65);
			match(T__0);
			setState(66);
			oneRacePart();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ClassDeclareContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public ClassDeclareContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classDeclare; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterClassDeclare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitClassDeclare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitClassDeclare(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassDeclareContext classDeclare() throws RecognitionException {
		ClassDeclareContext _localctx = new ClassDeclareContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_classDeclare);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableTypeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public VariableTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterVariableType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitVariableType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitVariableType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableTypeContext variableType() throws RecognitionException {
		VariableTypeContext _localctx = new VariableTypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_variableType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReturnTypeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public ReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnTypeContext returnType() throws RecognitionException {
		ReturnTypeContext _localctx = new ReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_returnType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodSigContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public MethodSigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodSig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterMethodSig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitMethodSig(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitMethodSig(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodSigContext methodSig() throws RecognitionException {
		MethodSigContext _localctx = new MethodSigContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_methodSig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LineNumberContext extends ParserRuleContext {
		public TerminalNode Number() { return getToken(RvParser.Number, 0); }
		public LineNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lineNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterLineNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitLineNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitLineNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LineNumberContext lineNumber() throws RecognitionException {
		LineNumberContext _localctx = new LineNumberContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_lineNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			match(Number);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\fS\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\4\3\4\5\4A\n\4\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3"+
		"\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\2\2\f\2\4\6\b\n\f\16\20\22\24\2"+
		"\2I\2\26\3\2\2\2\4*\3\2\2\2\6@\3\2\2\2\bB\3\2\2\2\nF\3\2\2\2\fH\3\2\2"+
		"\2\16J\3\2\2\2\20L\3\2\2\2\22N\3\2\2\2\24P\3\2\2\2\26\27\7\7\2\2\27\30"+
		"\7\b\2\2\30\31\7\t\2\2\31\32\5\n\6\2\32\33\7\b\2\2\33\34\5\20\t\2\34\35"+
		"\5\22\n\2\35\36\7\n\2\2\36\37\7\f\2\2\37 \7\5\2\2 !\7\13\2\2!\"\7\n\2"+
		"\2\"#\5\n\6\2#$\7\b\2\2$%\5\f\7\2%&\5\16\b\2&\'\7\n\2\2\'(\7\f\2\2()\5"+
		"\24\13\2)\3\3\2\2\2*+\7\7\2\2+,\7\b\2\2,-\7\t\2\2-.\5\n\6\2./\7\b\2\2"+
		"/\60\5\20\t\2\60\61\5\22\n\2\61\62\7\n\2\2\62\63\7\f\2\2\63\64\7\n\2\2"+
		"\64\65\5\n\6\2\65\66\7\b\2\2\66\67\5\f\7\2\678\5\16\b\289\7\n\2\29:\7"+
		"\13\2\2:;\7\5\2\2;<\7\f\2\2<=\5\24\13\2=\5\3\2\2\2>A\5\2\2\2?A\5\4\3\2"+
		"@>\3\2\2\2@?\3\2\2\2A\7\3\2\2\2BC\5\6\4\2CD\7\3\2\2DE\5\6\4\2E\t\3\2\2"+
		"\2FG\7\5\2\2G\13\3\2\2\2HI\7\5\2\2I\r\3\2\2\2JK\7\5\2\2K\17\3\2\2\2LM"+
		"\7\5\2\2M\21\3\2\2\2NO\7\5\2\2O\23\3\2\2\2PQ\7\4\2\2Q\25\3\2\2\2\3@";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}