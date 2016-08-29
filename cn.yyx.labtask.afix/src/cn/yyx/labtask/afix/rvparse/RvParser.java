package cn.yyx.labtask.afix.rvparse;

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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, NUMBER=8, ID=9, 
		WS=10;
	public static final int
		RULE_oneRaceReadPart = 0, RULE_oneRaceWritePart = 1, RULE_oneRacePart = 2, 
		RULE_oneRace = 3, RULE_classDeclare = 4, RULE_variableType = 5, RULE_variable = 6, 
		RULE_returnType = 7, RULE_methodSig = 8, RULE_lineNumber = 9, RULE_idOrNumber = 10;
	public static final String[] ruleNames = {
		"oneRaceReadPart", "oneRaceWritePart", "oneRacePart", "oneRace", "classDeclare", 
		"variableType", "variable", "returnType", "methodSig", "lineNumber", "idOrNumber"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'<'", "':'", "'>'", "'|'", "'='", "'Race:'", "'-'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, "NUMBER", "ID", "WS"
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
		public IdOrNumberContext idOrNumber() {
			return getRuleContext(IdOrNumberContext.class,0);
		}
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public LineNumberContext lineNumber() {
			return getRuleContext(LineNumberContext.class,0);
		}
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			match(T__0);
			setState(23);
			classDeclare();
			setState(24);
			match(T__1);
			setState(25);
			returnType();
			setState(26);
			methodSig();
			setState(27);
			match(T__2);
			setState(28);
			match(T__3);
			setState(29);
			idOrNumber();
			setState(30);
			match(T__4);
			setState(32);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(31);
				match(ID);
				}
			}

			setState(34);
			match(T__0);
			setState(35);
			classDeclare();
			setState(36);
			match(T__1);
			setState(37);
			variableType();
			setState(38);
			variable();
			setState(39);
			match(T__2);
			setState(40);
			match(T__3);
			setState(41);
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
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public IdOrNumberContext idOrNumber() {
			return getRuleContext(IdOrNumberContext.class,0);
		}
		public LineNumberContext lineNumber() {
			return getRuleContext(LineNumberContext.class,0);
		}
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(T__0);
			setState(44);
			classDeclare();
			setState(45);
			match(T__1);
			setState(46);
			returnType();
			setState(47);
			methodSig();
			setState(48);
			match(T__2);
			setState(49);
			match(T__3);
			setState(51);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(50);
				match(ID);
				}
			}

			setState(53);
			match(T__0);
			setState(54);
			classDeclare();
			setState(55);
			match(T__1);
			setState(56);
			variableType();
			setState(57);
			variable();
			setState(58);
			match(T__2);
			setState(59);
			match(T__4);
			setState(60);
			idOrNumber();
			setState(61);
			match(T__3);
			setState(62);
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
			setState(66);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(64);
				oneRaceReadPart();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
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
			setState(68);
			match(T__5);
			setState(69);
			oneRacePart();
			setState(70);
			match(T__6);
			setState(71);
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
			setState(73);
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
			setState(75);
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
			setState(77);
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
			setState(79);
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
			setState(81);
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
		public TerminalNode NUMBER() { return getToken(RvParser.NUMBER, 0); }
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
			setState(83);
			match(NUMBER);
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

	public static class IdOrNumberContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public TerminalNode NUMBER() { return getToken(RvParser.NUMBER, 0); }
		public IdOrNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idOrNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterIdOrNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitIdOrNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitIdOrNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdOrNumberContext idOrNumber() throws RecognitionException {
		IdOrNumberContext _localctx = new IdOrNumberContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_idOrNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			_la = _input.LA(1);
			if ( !(_la==NUMBER || _la==ID) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\fZ\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2#\n\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\66\n\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\5\4E\n\4\3\5\3\5\3\5"+
		"\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f"+
		"\2\2\r\2\4\6\b\n\f\16\20\22\24\26\2\3\3\2\n\13Q\2\30\3\2\2\2\4-\3\2\2"+
		"\2\6D\3\2\2\2\bF\3\2\2\2\nK\3\2\2\2\fM\3\2\2\2\16O\3\2\2\2\20Q\3\2\2\2"+
		"\22S\3\2\2\2\24U\3\2\2\2\26W\3\2\2\2\30\31\7\3\2\2\31\32\5\n\6\2\32\33"+
		"\7\4\2\2\33\34\5\20\t\2\34\35\5\22\n\2\35\36\7\5\2\2\36\37\7\6\2\2\37"+
		" \5\26\f\2 \"\7\7\2\2!#\7\13\2\2\"!\3\2\2\2\"#\3\2\2\2#$\3\2\2\2$%\7\3"+
		"\2\2%&\5\n\6\2&\'\7\4\2\2\'(\5\f\7\2()\5\16\b\2)*\7\5\2\2*+\7\6\2\2+,"+
		"\5\24\13\2,\3\3\2\2\2-.\7\3\2\2./\5\n\6\2/\60\7\4\2\2\60\61\5\20\t\2\61"+
		"\62\5\22\n\2\62\63\7\5\2\2\63\65\7\6\2\2\64\66\7\13\2\2\65\64\3\2\2\2"+
		"\65\66\3\2\2\2\66\67\3\2\2\2\678\7\3\2\289\5\n\6\29:\7\4\2\2:;\5\f\7\2"+
		";<\5\16\b\2<=\7\5\2\2=>\7\7\2\2>?\5\26\f\2?@\7\6\2\2@A\5\24\13\2A\5\3"+
		"\2\2\2BE\5\2\2\2CE\5\4\3\2DB\3\2\2\2DC\3\2\2\2E\7\3\2\2\2FG\7\b\2\2GH"+
		"\5\6\4\2HI\7\t\2\2IJ\5\6\4\2J\t\3\2\2\2KL\7\13\2\2L\13\3\2\2\2MN\7\13"+
		"\2\2N\r\3\2\2\2OP\7\13\2\2P\17\3\2\2\2QR\7\13\2\2R\21\3\2\2\2ST\7\13\2"+
		"\2T\23\3\2\2\2UV\7\n\2\2V\25\3\2\2\2WX\t\2\2\2X\27\3\2\2\2\5\"\65D";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}