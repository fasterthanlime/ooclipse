package ooclipse.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class Scanner extends RuleBasedPartitionScanner {
	
	private final static String[] KEYWORDS = new String[] {
		"class", "cover", "func", "abstract",
		"from", "this", "super", "new", "const",
		"static", "include", "import", "break",
		"continue", "fallthrough", "implement",
		"override", "if", "else", "for", "while",
		"do", "switch", "case", "version",
		"return", "ctype", "typedef",
	};
	
	private final static String[] TYPES = new String[] {
		"String", "Object", "Bool", "Char", "Int", "UInt",
		"Float", "Double", "LDouble", "Short", "Long", "LLong",
		"Void", "Func", "Size", "Octet"
	};
	
	public final static String KEYWORD = "__keyword";
	public final static String TYPE = "__type";
	public final static String COMMENT = "__comment";
	public final static String STRING = "__string";

	public Scanner() {

		IToken comment = new Token(COMMENT);
		IToken stringLiteral = new Token(STRING);
		
		IToken keyword = new Token(KEYWORD);
        WordPredicateRule wordRule = new WordPredicateRule(keyword);
        for(String kw: KEYWORDS) {
			wordRule.addWord(kw, keyword);
		}
        
        IToken type = new Token(TYPE);
        for(String ty: TYPES) {
			wordRule.addWord(ty, type);
		}

		setPredicateRules(new IPredicateRule[] {
				wordRule,
				new SingleLineRule("//", null, comment),
				new MultiLineRule("/**", "*/", comment),
				new MultiLineRule("/*", "*/", comment),
				new SingleLineRule("\"", "\"", stringLiteral, '\\'),
				new SingleLineRule("'", "'", stringLiteral, '\\'),
				new WhitespacePredicateRule(new Token("__whitespace")),
		});
	}
	
	/**
	 *
	 */
	static class WordPredicateRule extends WordRule implements IPredicateRule {

		private IToken fSuccessToken;

		public WordPredicateRule(IToken successToken) {
			super(new IWordDetector() {
	            public boolean isWordStart(char c) { 
	            	return Character.isJavaIdentifierStart(c); 
	            }
	            
	            public boolean isWordPart(char c) {   
	            	return Character.isJavaIdentifierPart(c); 
	            }
	        });
			fSuccessToken = successToken;
			addWord("/**/", fSuccessToken); //$NON-NLS-1$
		}

		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return super.evaluate(scanner);
		}

		public IToken getSuccessToken() {
			return fSuccessToken;
		}
	}
	
	/**
	 *
	 */
	static class WhitespacePredicateRule extends WhitespaceRule implements IPredicateRule {

		private IToken fSuccessToken;

		public WhitespacePredicateRule(IToken successToken) {
			super(new WhitespaceDetector());
			fSuccessToken = successToken;
		}

		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return super.evaluate(scanner);
		}

		public IToken getSuccessToken() {
			return fSuccessToken;
		}
	}


}
