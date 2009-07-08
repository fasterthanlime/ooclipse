package ooclipse.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class OocScanner extends RuleBasedScanner {

	public OocScanner(ColorManager manager) {
		
		IToken procInstr =
			new Token(
				new TextAttribute(
					manager.getColor(ColorConstants.PROC_INSTR)));

		setRules(new IRule[] {
				// Add rule for processing instructions
				new SingleLineRule("<?", "?>", procInstr),
				// Add generic whitespace rule.
				new WhitespaceRule(new WhitespaceDetector()),
		});

	}
		
}
