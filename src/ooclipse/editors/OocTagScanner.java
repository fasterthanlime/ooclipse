package ooclipse.editors;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;

public class OocTagScanner extends RuleBasedScanner {

	public OocTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(manager.getColor(ColorConstants.STRING)));

		setRules(new IRule[] {

				// Add rule for double quotes
				new SingleLineRule("\"", "\"", string, '\\'),
				// Add a rule for single quotes
				new SingleLineRule("'", "'", string, '\\'),
				// Add generic whitespace rule.
				new WhitespaceRule(new WhitespaceDetector()),

		});
	}
}
