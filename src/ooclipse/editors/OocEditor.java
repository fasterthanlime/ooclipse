package ooclipse.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class OocEditor extends TextEditor {

	private ColorManager colorManager;

	public OocEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new OocConfiguration(colorManager));
		setDocumentProvider(new OocDocumentProvider());
	}
	
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
