package it.eurotn.panjea.intra.rich.editors.dichiarazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;

import java.util.Arrays;

import org.springframework.richclient.selection.dialog.ListSelectionDialog;

public class NuovaDichiarazioneDialog extends ListSelectionDialog {

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public NuovaDichiarazioneDialog() {
		super("Tipo dichiarazione", null, Arrays.asList(TipoDichiarazione.values()));
	}
}
