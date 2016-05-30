/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.domain.DescrizionePoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;

/**
 * @author fattazzo
 * 
 */
public class DescrizioneCalcoloModuloPrezzoDialog extends ApplicationDialog {

	private static final String PAGE_ID = "descrizioneCalcoloModuloPrezzoDialog.title";

	private final DescrizionePoliticaPrezzo descrizionePoliticaPrezzo;

	/**
	 * Crea un dialogo che descrive come è stato generato un importo per la riga articolo.
	 * 
	 * @param descrizionePoliticaPrezzo
	 *            descrizione per la riga
	 */
	public DescrizioneCalcoloModuloPrezzoDialog(final DescrizionePoliticaPrezzo descrizionePoliticaPrezzo) {
		super();
		this.descrizionePoliticaPrezzo = descrizionePoliticaPrezzo;
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel(new VerticalLayout(0));
		if (this.descrizionePoliticaPrezzo != null) {
			for (RisultatoPrezzo risultatoPrezzo : this.descrizionePoliticaPrezzo.getValoriModuliPrezzo()) {
				// DescrizioneCalcoloModuloPrezzoPanel prezzoPanel = new DescrizioneCalcoloModuloPrezzoPanel(
				// risultatoPrezzo);
				// rootPanel.add(prezzoPanel);
			}
		}
		getCancelCommand().setVisible(false);
		return rootPanel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new AbstractCommand[] { getFinishCommand() };
	}

	@Override
	protected String getTitle() {
		return getMessage(PAGE_ID);
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

}
