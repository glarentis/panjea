package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideSplitButton;

public class TotalizzaDocumentoCommand extends ApplicationWindowAwareCommand {

	private AreaOrdineFullDTO areaOrdineFullDTO;

	private final IOrdiniDocumentoBD ordiniDocumentoBD;

	public static final String COMMAND_ID = "totalizzaDocumentoCommand";

	public static final String KEY_TOTALIZZAZIONE_AUTOMATICA = "keyTotalizzazioneAutomaticaOrdine";

	private JRadioButtonMenuItem manualeMenuItem;

	private JRadioButtonMenuItem automaticoMenuItem;

	/**
	 * Costruttore.
	 * 
	 * @param ordiniDocumentoBD
	 *            ordiniDocumentoBD
	 */
	public TotalizzaDocumentoCommand(final IOrdiniDocumentoBD ordiniDocumentoBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.ordiniDocumentoBD = ordiniDocumentoBD;
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {

		JideSplitButton button = new JideSplitButton();

		manualeMenuItem = new JRadioButtonMenuItem("Manuale", true);
		manualeMenuItem.setName("totalizzazioneManualeCommand");
		automaticoMenuItem = new JRadioButtonMenuItem("Automatico");
		automaticoMenuItem.setName("totalizzazioneAutomaticaCommand");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(manualeMenuItem);
		buttonGroup.add(automaticoMenuItem);

		button.add(manualeMenuItem);
		button.add(automaticoMenuItem);

		button.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TotalizzaDocumentoCommand.this.execute();
			}
		});
		RcpSupport.configure(this);
		// button.setText(this.getText());
		button.setIcon(this.getIcon());
		button.setToolTipText(this.getText());

		return button;
	}

	@Override
	protected void doExecuteCommand() {
		AreaOrdine areaOrdine = ordiniDocumentoBD.totalizzaDocumento(areaOrdineFullDTO.getAreaOrdine(),
				areaOrdineFullDTO.getAreaRate());
		areaOrdineFullDTO.setAreaOrdine(areaOrdine);
	}

	/**
	 * @return the areaOrdineFullDTO
	 */
	public AreaOrdineFullDTO getAreaOrdineFullDTO() {
		return areaOrdineFullDTO;
	}

	/**
	 * @return the totalizzazioneAutomatica
	 */
	public boolean isTotalizzazioneAutomatica() {
		return automaticoMenuItem.isSelected();
	}

	/**
	 * @param areaOrdineFullDTO
	 *            the areaOrdineFullDTO to set
	 */
	public void setAreaOrdineFullDTO(AreaOrdineFullDTO areaOrdineFullDTO) {
		this.areaOrdineFullDTO = areaOrdineFullDTO;
	}

	/**
	 * @param totalizzazioneAutomatica
	 *            the totalizzazioneAutomatica to set
	 */
	public void setTotalizzazioneAutomatica(boolean totalizzazioneAutomatica) {
		manualeMenuItem.setSelected(!totalizzazioneAutomatica);
		automaticoMenuItem.setSelected(totalizzazioneAutomatica);
	}

}
