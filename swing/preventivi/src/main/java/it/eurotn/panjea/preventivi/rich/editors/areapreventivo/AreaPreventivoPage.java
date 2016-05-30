package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.forms.areapreventivo.AreaPreventivoAltroForm;
import it.eurotn.panjea.preventivi.rich.forms.areapreventivo.AreaPreventivoForm;
import it.eurotn.panjea.preventivi.rich.forms.areapreventivo.PiedeAreaPreventivoForm;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documento.StampaAreaDocumentoSplitCommand;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

public class AreaPreventivoPage extends AbstractAreaDocumentoPage<AreaPreventivo, AreaPreventivoFullDTO> {

	private class TipoAreaPreventivoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			stampaDocumentoSplitCommand.updateCommand(((AreaPreventivoFullDTO) getForm().getFormObject())
					.getAreaPreventivo());
		}
	}

	public static final String PAGE_ID = "areaPreventivoPage";
	private AziendaCorrente aziendaCorrente;

	private StampaAreaDocumentoSplitCommand stampaDocumentoSplitCommand;
	private TipoAreaPreventivoChangeListener tipoAreaDocumentoChangeListener;

	/**
	 * costruttore.
	 */
	public AreaPreventivoPage() {
		super(PAGE_ID, new AreaPreventivoForm());
	}

	@Override
	public void addForms() {
		AreaPreventivoAltroForm altroForm = new AreaPreventivoAltroForm(getBackingFormPage().getFormModel());
		addForm(altroForm);

		PiedeAreaPreventivoForm piedeAreaPreventivoForm = new PiedeAreaPreventivoForm(getBackingFormPage()
				.getFormModel());
		addForm(piedeAreaPreventivoForm);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AreaPreventivoForm areaPreventivoForm = (AreaPreventivoForm) getForm();
		areaPreventivoForm.setAziendaCorrente(aziendaCorrente);
		super.afterPropertiesSet();
	}

	@Override
	protected List<JComponent> creaComponentsDaAggiungereAdErrorBar() {
		List<JComponent> components = super.creaComponentsDaAggiungereAdErrorBar();
		components.add(new StatoEvasionePreventivoLabel());
		return components;
	}

	@Override
	protected AbstractStatiAreaDocumentoCommandController<AreaPreventivo, AreaPreventivo.StatoAreaPreventivo> creaStatiAreaDocumentoCommandController() {
		return new StatiAreaPreventivoCommandController((IPreventiviBD) getAreaBD());
	}

	@Override
	protected List<AbstractCommand> createCommandsList() {
		List<AbstractCommand> list = super.createCommandsList();
		list.add(getEliminaCommand());
		list.add(getStampaDocumentoCommand());
		list.add(getCopiaAreaDocumentoCommand());
		return list;
	}

	@Override
	public void dispose() {
		if (stampaDocumentoSplitCommand == null) {
			getForm().getValueModel("areaPreventivo.tipoAreaPreventivo").removeValueChangeListener(
					tipoAreaDocumentoChangeListener);
		}
		tipoAreaDocumentoChangeListener = null;
		super.dispose();
	}

	@Override
	protected AreaPreventivoFullDTO getFormObject() {
		return (AreaPreventivoFullDTO) getForm().getFormObject();
	}

	@Override
	protected String getOnNewPropertyChangeEvent() {
		return "areaPreventivo.tipoAreaPreventivo";
	}

	/**
	 * @return comando per la stampa
	 */
	public StampaAreaDocumentoSplitCommand getStampaDocumentoCommand() {
		if (stampaDocumentoSplitCommand == null) {
			stampaDocumentoSplitCommand = new StampaAreaDocumentoSplitCommand();
			tipoAreaDocumentoChangeListener = new TipoAreaPreventivoChangeListener();
			getForm().getValueModel("areaPreventivo.tipoAreaPreventivo").addValueChangeListener(
					tipoAreaDocumentoChangeListener);
		}
		return stampaDocumentoSplitCommand;
	}

	@Override
	protected FormModelPropertyChangeListeners getTipoAreaDocumentoPropertyChange() {
		return RcpSupport.getBean("areaPreventivo.tipoAreaPreventivoPropertyChange");
	}

	@Override
	protected boolean isAreaDocumentoToBeReadOnly(AreaPreventivo areaDocumento) {
		return areaDocumento.getStatoAreaPreventivo() == StatoAreaPreventivo.ACCETTATO;
	}

	@Override
	protected boolean isCommandStampaDaAbilitare(AreaPreventivo areaDocumento) {
		return !areaDocumento.isNew() && areaDocumento.getStatoAreaPreventivo() != StatoAreaPreventivo.PROVVISORIO;
	}

	@Override
	protected boolean isRigheDaInvalidareOnCambioStato(AreaPreventivo areaDocumentoNuovo) {
		return areaDocumentoNuovo.getStatoAreaPreventivo() == StatoAreaPreventivo.PROVVISORIO;
	}

	@Override
	public void onEditorEvent(ApplicationEvent event) {

		if (event instanceof PanjeaLifecycleApplicationEvent) {
			Object eventObject = event.getSource();
			if ((eventObject instanceof LayoutStampa)) {
				getStampaDocumentoCommand().updateCommand(
						((AreaPreventivoFullDTO) getForm().getFormObject()).getAreaPreventivo());
			}
		}
	}

	@Override
	public void postSetFormObject(Object object) {
		super.postSetFormObject(object);

		AreaPreventivoFullDTO areaPreventivoFullDTO = (AreaPreventivoFullDTO) object;
		AreaPreventivo areaPreventivo = areaPreventivoFullDTO.getAreaPreventivo();
		setReadOnly(areaPreventivo.getStatoAreaPreventivo() == StatoAreaPreventivo.ACCETTATO
				|| areaPreventivo.getStatoAreaPreventivo() == StatoAreaPreventivo.RIFIUTATO);
		getNewCommand().setEnabled(true);
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 *
	 * @param preventiviBD
	 *            preventiviBD
	 */
	public void setPreventiviBD(IPreventiviBD preventiviBD) {
		super.setAreaBD(preventiviBD);
	}
}
