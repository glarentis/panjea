package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.commands.documento.SpedizioneDocumentiCommand;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.TipoLayout;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideSplitButton;

public class SpedizioneDocumentiFatturazioneCommand extends SpedizioneDocumentiCommand<AreaMagazzino> {

	private static Logger logger = Logger.getLogger(SpedizioneDocumentiFatturazioneCommand.class);

	public static final String COMMAND_ID = "spedizioneDocumentiFatturazioneCommand";
	private static final String KEY_TIPO_SPEDIZIONE_FATTURAZIONE = "fatturazioneSpedizioneDocumenti";

	private List<MovimentoFatturazioneDTO> movimentiDaSpedire;

	private JRadioButtonMenuItem tuttiMenuItem;

	private JRadioButtonMenuItem nonSpeditiMenuItem;

	private JideSplitButton button;

	private SettingsManager settingsManager;

	/**
	 * Costruttore.
	 */
	public SpedizioneDocumentiFatturazioneCommand() {
		super(COMMAND_ID, AreaMagazzino.class);
		RcpSupport.configure(this);

		settingsManager = (SettingsManager) Application.services().getService(SettingsManager.class);
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {

		button = new JideSplitButton();
		button.setName(COMMAND_ID);
		nonSpeditiMenuItem = new JRadioButtonMenuItem("Non spediti", true);
		nonSpeditiMenuItem.setName("spedizioneDocumentiNonSpeditiCommand");
		tuttiMenuItem = new JRadioButtonMenuItem("Tutti");
		tuttiMenuItem.setName("spedizioneDocumentiNonSpeditiCommand");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(nonSpeditiMenuItem);
		buttonGroup.add(tuttiMenuItem);

		button.add(nonSpeditiMenuItem);
		button.add(tuttiMenuItem);

		button.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					settingsManager.getUserSettings().setBoolean(KEY_TIPO_SPEDIZIONE_FATTURAZIONE,
							tuttiMenuItem.isSelected());
				} catch (SettingsException e1) {
					logger.error("-->errore durante il salvataggio delle preferenze di spedizione della fatturazione",
							e1);
				}

				SpedizioneDocumentiFatturazioneCommand.this.execute();
			}
		});
		RcpSupport.configure(this);
		button.setText(this.getText());
		button.setIcon(this.getIcon());

		boolean spedisciTutti = true;
		try {
			if (settingsManager.getUserSettings().contains(KEY_TIPO_SPEDIZIONE_FATTURAZIONE)) {
				spedisciTutti = settingsManager.getUserSettings().getBoolean(KEY_TIPO_SPEDIZIONE_FATTURAZIONE);
			}
		} catch (SettingsException e1) {
			spedisciTutti = true;
		}
		tuttiMenuItem.setSelected(spedisciTutti);
		nonSpeditiMenuItem.setSelected(!spedisciTutti);

		return button;
	}

	@Override
	protected void doExecuteCommand() {

		List<Integer> idDocumenti = new ArrayList<Integer>();
		for (MovimentoFatturazioneDTO movimento : movimentiDaSpedire) {
			if (tuttiMenuItem.isSelected()
					|| (nonSpeditiMenuItem.isSelected() && movimento.getAreaMagazzino().getStatoSpedizione() == StatoSpedizione.NON_SPEDITO)) {
				idDocumenti.add(movimento.getAreaMagazzino().getDocumento().getId());
			}
		}

		addParameter(PARAM_ID_DOCUMENTI_DA_SPEDIRE, idDocumenti);
		addParameter(PARAM_TIPO_LAYOUT, TipoLayout.CLIENTE_E_INTERNO);

		super.doExecuteCommand();

		movimentiDaSpedire = null;
	}

	@Override
	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @param movimentiDaSpedire
	 *            the movimentiDaSpedire to set
	 */
	public void setMovimentiDaSpedire(List<MovimentoFatturazioneDTO> movimentiDaSpedire) {
		this.movimentiDaSpedire = movimentiDaSpedire;
	}

	@Override
	public void setVisible(boolean value) {
		button.setVisible(value);
		super.setVisible(value);
	}

}
