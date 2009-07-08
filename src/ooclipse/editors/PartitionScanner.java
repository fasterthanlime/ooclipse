package ooclipse.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class PartitionScanner extends RuleBasedPartitionScanner {
	
	private final static String[] KEYWORDS = new String[] {
		"class", "cover", "func", "abstract",
		"from", "this", "super", "new", "const",
		"static", "include", "import", "break",
		"continue", "fallthrough", "implement",
		"override", "if", "else", "for", "while",
		"do", "switch", "case", "version",
		"return",
	};
	
	private final static String[] TYPES = new String[] {
		"String", "Object", "Bool", "Char", "Int", "UInt",
		"Float", "Double", "LDouble", "Short", "Long", "LLong",
		"Void", "Func", "Size", "Octet"
	};
	
	public final static String KEYWORD = "__keyword";
	public final static String TYPE = "__builtin_type";
	public final static String COMMENT = "__comment";
	public final static String STRING = "__string_literal";
	public final static String XML_TAG = "__xml_tag";

	public PartitionScanner() {

		IToken comment = new Token(COMMENT);
		IToken stringLiteral = new Token(STRING);
		//IToken tag = new Token(XML_TAG);
		
		IToken keyword = new Token(KEYWORD);
        WordPredicateRule keywordRule = new WordPredicateRule(keyword);
        for(String kw: KEYWORDS) {
			keywordRule.addWord(kw, keyword);
		}
        
        IToken type = new Token(TYPE);
        WordPredicateRule typeRule = new WordPredicateRule(type);
        for(String ty: TYPES) {
			typeRule.addWord(ty, type);
		}

		setPredicateRules(new IPredicateRule[] {
				keywordRule,
				typeRule,
				new SingleLineRule("//", null, comment),
				new MultiLineRule("/*", "*/", comment),
				new SingleLineRule("\"", "\"", stringLiteral, '\\'),
				new SingleLineRule("'", "'", stringLiteral, '\\'),
				//new TagRule(tag),
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
			fSuccessToken= successToken;
			addWord("/**/", fSuccessToken); //$NON-NLS-1$
		}

		/*
		 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(ICharacterScanner, boolean)
		 */
		public IToken evaluate(ICharacterScanner scanner, boolean resume) {
			return super.evaluate(scanner);
		}

		/*
		 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
		 */
		public IToken getSuccessToken() {
			return fSuccessToken;
		}
	}


}
