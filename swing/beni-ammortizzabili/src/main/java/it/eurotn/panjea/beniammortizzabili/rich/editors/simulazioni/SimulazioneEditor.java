package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import javax.swing.JComponent;

import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

public class SimulazioneEditor extends AbstractEditor implements Memento {

	public static final String EDITOR_ID = "simulazioneEditor";

	private final SimulazionePage simulazionePage = new SimulazionePage();

	@Override
	public JComponent getControl() {
		return simulazionePage.getControl();
	}

	@Override
	public String getId() {
		return EDITOR_ID;
	}

	@Override
	public void initialize(Object editorObject) {
		simulazionePage.getControl();
		simulazionePage.setFormObject(editorObject);
	}

	@Override
	public void restoreState(Settings settings) {
		simulazionePage.restoreState(settings);
	}

	@Override
	public void save(ProgressMonitor saveProgressTracker) {

	}

	@Override
	public void saveState(Settings settings) {
		simulazionePage.saveState(settings);
	}

}
