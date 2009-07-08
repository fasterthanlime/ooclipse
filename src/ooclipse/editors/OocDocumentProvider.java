package ooclipse.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class OocDocumentProvider extends FileDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new Scanner(),
					new String[] {
						Scanner.KEYWORD,
						Scanner.STRING,
						Scanner.COMMENT,
						Scanner.TYPE,
					});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		
		return document;
		
	}
}