package ooclipse.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;

public class OocConfiguration extends SourceViewerConfiguration {
	private DoubleClickStrategy doubleClickStrategy;
	private Scanner scanner;
	private ColorManager colorManager;

	public OocConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			Scanner.KEYWORD,
			Scanner.STRING,
			Scanner.COMMENT,
			Scanner.TYPE,
		};
	}
	
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected Scanner getScanner() {
		if (scanner == null) {
			scanner = new Scanner();
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ColorConstants.DEFAULT))));
		}
		return scanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.COMMENT)));
		reconciler.setDamager(ndr, Scanner.COMMENT);
		reconciler.setRepairer(ndr, Scanner.COMMENT);
		
		ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.KEYWORD), null, SWT.BOLD));
		reconciler.setDamager(ndr, Scanner.KEYWORD);
		reconciler.setRepairer(ndr, Scanner.KEYWORD);
		
		ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.STRING)));
		reconciler.setDamager(ndr, Scanner.STRING);
		reconciler.setRepairer(ndr, Scanner.STRING);
		
		ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.TYPE), null, SWT.BOLD));
		reconciler.setDamager(ndr, Scanner.TYPE);
		reconciler.setRepairer(ndr, Scanner.TYPE);

		return reconciler;
	}

}