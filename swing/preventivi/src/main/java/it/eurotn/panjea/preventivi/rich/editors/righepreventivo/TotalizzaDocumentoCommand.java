package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;

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

	private AreaPreventivoFullDTO areaPreventivoFullDTO;

	private final IPreventiviBD preventiviBD;

	public static final String COMMAND_ID = "totalizzaDocumentoCommand";

	public static final String KEY_TOTALIZZAZIONE_AUTOMATICA = "keyTotalizzazioneAutomaticaPreventivo";

	private JRadioButtonMenuItem manualeMenuItem;

	private JRadioButtonMenuItem automaticoMenuItem;

	/**
	 * Costruttore.
	 * 
	 * @param preventiviBD
	 *            preventiviBD
	 */
	public TotalizzaDocumentoCommand(final IPreventiviBD preventiviBD) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.preventiviBD = preventiviBD;
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
		AreaPreventivo areaPreventivo = preventiviBD.totalizzaDocumento(areaPreventivoFullDTO.getAreaPreventivo(),
				areaPreventivoFullDTO.getAreaRate());
		areaPreventivoFullDTO.setAreaPreventivo(areaPreventivo);
	}

	/**
	 * @return the areaOrdineFullDTO
	 */
	public AreaPreventivoFullDTO getAreaPreventivoFullDTO() {
		return areaPreventivoFullDTO;
	}

	/**
	 * @return the totalizzazioneAutomatica
	 */
	public boolean isTotalizzazioneAutomatica() {
		return automaticoMenuItem.isSelected();
	}

	/**
	 * @param areaPreventivoFullDTO
	 *            the areaOrdineFullDTO to set
	 */
	public void setAreaPreventivoFullDTO(AreaPreventivoFullDTO areaPreventivoFullDTO) {
		this.areaPreventivoFullDTO = areaPreventivoFullDTO;
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
