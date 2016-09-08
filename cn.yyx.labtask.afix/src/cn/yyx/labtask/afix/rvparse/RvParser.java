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
		RULE_oneRaceValue = 0, RULE_oneRaceReadPart = 1, RULE_oneRaceWritePart = 2, 
		RULE_oneRacePart = 3, RULE_oneRace = 4, RULE_classDeclare = 5, RULE_variableType = 6, 
		RULE_variable = 7, RULE_returnType = 8, RULE_methodSig = 9, RULE_lineNumber = 10, 
		RULE_idOrNumber = 11;
	public static final String[] ruleNames = {
		"oneRaceValue", "oneRaceReadPart", "oneRaceWritePart", "oneRacePart", 
		"oneRace", "classDeclare", "variableType", "variable", "returnType", "methodSig", 
		"lineNumber", "idOrNumber"
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
	public static class OneRaceValueContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RvParser.ID, 0); }
		public ClassDeclareContext classDeclare() {
			return getRuleContext(ClassDeclareContext.class,0);
		}
		public VariableTypeContext variableType() {
			return getRuleContext(VariableTypeContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public OneRaceValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_oneRaceValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).enterOneRaceValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RvListener ) ((RvListener)listener).exitOneRaceValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RvVisitor ) return ((RvVisitor<? extends T>)visitor).visitOneRaceValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OneRaceValueContext oneRaceValue() throws RecognitionException {
		OneRaceValueContext _localctx = new OneRaceValueContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_oneRaceValue);
		int _la;
		try {
			setState(41);
			switch (_input.LA(1)) {
			case ID:
				enterOuterAlt(_localctx, 1);
				{
				setState(24);
				match(ID);
				setState(32);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(25);
					match(T__0);
					setState(26);
					classDeclare();
					setState(27);
					match(T__1);
					setState(28);
					variableType();
					setState(29);
					variable();
					setState(30);
					match(T__2);
					}
				}

				}
				break;
			case T__0:
				enterOuterAlt(_localctx, 2);
				{
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
				}
				break;
			default:
				throw new NoViableAltException(this);
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

	public static class OneRaceReadPartContext extends ParserRuleContext {
		public ClassDeclareContext classDeclare() {
			return getRuleContext(ClassDeclareContext.class,0);
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
		public OneRaceValueContext oneRaceValue() {
			return getRuleContext(OneRaceValueContext.class,0);
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
		enterRule(_localctx, 2, RULE_oneRaceReadPart);
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
			setState(50);
			idOrNumber();
			setState(51);
			match(T__4);
			setState(52);
			oneRaceValue();
			setState(53);
			match(T__3);
			setState(54);
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
		public ClassDeclareContext classDeclare() {
			return getRuleContext(ClassDeclareContext.class,0);
		}
		public ReturnTypeContext returnType() {
			return getRuleContext(ReturnTypeContext.class,0);
		}
		public MethodSigContext methodSig() {
			return getRuleContext(MethodSigContext.class,0);
		}
		public OneRaceValueContext oneRaceValue() {
			return getRuleContext(OneRaceValueContext.class,0);
		}
		public IdOrNumberContext idOrNumber() {
			return getRuleContext(IdOrNumberContext.class,0);
		}
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
		enterRule(_localctx, 4, RULE_oneRaceWritePart);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			match(T__0);
			setState(57);
			classDeclare();
			setState(58);
			match(T__1);
			setState(59);
			returnType();
			setState(60);
			methodSig();
			setState(61);
			match(T__2);
			setState(62);
			match(T__3);
			setState(63);
			oneRaceValue();
			setState(64);
			match(T__4);
			setState(65);
			idOrNumber();
			setState(66);
			match(T__3);
			setState(67);
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
		enterRule(_localctx, 6, RULE_oneRacePart);
		try {
			setState(71);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(69);
				oneRaceReadPart();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(70);
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
		enterRule(_localctx, 8, RULE_oneRace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			match(T__5);
			setState(74);
			oneRacePart();
			setState(75);
			match(T__6);
			setState(76);
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
		enterRule(_localctx, 10, RULE_classDeclare);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
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
		enterRule(_localctx, 12, RULE_variableType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
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
		enterRule(_localctx, 14, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
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
		enterRule(_localctx, 16, RULE_returnType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
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
		enterRule(_localctx, 18, RULE_methodSig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
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
		enterRule(_localctx, 20, RULE_lineNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
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
		enterRule(_localctx, 22, RULE_idOrNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\f_\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2#\n\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\5\2,\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\5\5"+
		"J\n\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\r\2\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\3\3\2\n\13U"+
		"\2+\3\2\2\2\4-\3\2\2\2\6:\3\2\2\2\bI\3\2\2\2\nK\3\2\2\2\fP\3\2\2\2\16"+
		"R\3\2\2\2\20T\3\2\2\2\22V\3\2\2\2\24X\3\2\2\2\26Z\3\2\2\2\30\\\3\2\2\2"+
		"\32\"\7\13\2\2\33\34\7\3\2\2\34\35\5\f\7\2\35\36\7\4\2\2\36\37\5\16\b"+
		"\2\37 \5\20\t\2 !\7\5\2\2!#\3\2\2\2\"\33\3\2\2\2\"#\3\2\2\2#,\3\2\2\2"+
		"$%\7\3\2\2%&\5\f\7\2&\'\7\4\2\2\'(\5\16\b\2()\5\20\t\2)*\7\5\2\2*,\3\2"+
		"\2\2+\32\3\2\2\2+$\3\2\2\2,\3\3\2\2\2-.\7\3\2\2./\5\f\7\2/\60\7\4\2\2"+
		"\60\61\5\22\n\2\61\62\5\24\13\2\62\63\7\5\2\2\63\64\7\6\2\2\64\65\5\30"+
		"\r\2\65\66\7\7\2\2\66\67\5\2\2\2\678\7\6\2\289\5\26\f\29\5\3\2\2\2:;\7"+
		"\3\2\2;<\5\f\7\2<=\7\4\2\2=>\5\22\n\2>?\5\24\13\2?@\7\5\2\2@A\7\6\2\2"+
		"AB\5\2\2\2BC\7\7\2\2CD\5\30\r\2DE\7\6\2\2EF\5\26\f\2F\7\3\2\2\2GJ\5\4"+
		"\3\2HJ\5\6\4\2IG\3\2\2\2IH\3\2\2\2J\t\3\2\2\2KL\7\b\2\2LM\5\b\5\2MN\7"+
		"\t\2\2NO\5\b\5\2O\13\3\2\2\2PQ\7\13\2\2Q\r\3\2\2\2RS\7\13\2\2S\17\3\2"+
		"\2\2TU\7\13\2\2U\21\3\2\2\2VW\7\13\2\2W\23\3\2\2\2XY\7\13\2\2Y\25\3\2"+
		"\2\2Z[\7\n\2\2[\27\3\2\2\2\\]\t\2\2\2]\31\3\2\2\2\5\"+I";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}