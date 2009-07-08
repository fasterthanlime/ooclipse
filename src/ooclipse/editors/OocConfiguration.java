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
	private OocTagScanner tagScanner;
	private OocScanner scanner;
	private ColorManager colorManager;

	public OocConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			PartitionScanner.COMMENT,
			PartitionScanner.XML_TAG,
			PartitionScanner.KEYWORD,
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

	protected OocScanner getOocScanner() {
		if (scanner == null) {
			scanner = new OocScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected OocTagScanner getOocTagScanner() {
		if (tagScanner == null) {
			tagScanner = new OocTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ColorConstants.TAG))));
		}
		return tagScanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getOocScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.COMMENT)));
		reconciler.setDamager(ndr, PartitionScanner.COMMENT);
		reconciler.setRepairer(ndr, PartitionScanner.COMMENT);
		
		ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.KEYWORD), null, SWT.BOLD));
		reconciler.setDamager(ndr, PartitionScanner.KEYWORD);
		reconciler.setRepairer(ndr, PartitionScanner.KEYWORD);
		
		ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.STRING)));
		reconciler.setDamager(ndr, PartitionScanner.STRING);
		reconciler.setRepairer(ndr, PartitionScanner.STRING);
		
		ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ColorConstants.TYPE), null, SWT.BOLD));
		reconciler.setDamager(ndr, PartitionScanner.TYPE);
		reconciler.setRepairer(ndr, PartitionScanner.TYPE);

		return reconciler;
	}

}