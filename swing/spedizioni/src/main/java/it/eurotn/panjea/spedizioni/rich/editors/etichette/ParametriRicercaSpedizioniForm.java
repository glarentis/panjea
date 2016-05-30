/**
 * 
 */
package it.eurotn.panjea.spedizioni.rich.editors.etichette;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.spedizioni.util.ParametriRicercaSpedizioni;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per l'input dei parametri di ricerca di {@link AreaMagazzino}.
 * 
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class ParametriRicercaSpedizioniForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriRicercaSpedizioniForm";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private AziendaCorrente aziendaCorrente;

	/**
	 * @param parametriRicercaAreaMagazzino
	 *            .
	 */
	public ParametriRicercaSpedizioniForm(final ParametriRicercaSpedizioni parametriRicercaSpedizioni) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaSpedizioni, false, FORM_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi selezioni solo agenti e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaVettore = new ArrayList<TipoEntita>();
		tipiEntitaVettore.add(TipoEntita.VETTORE);

		ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaVettore);
		DefaultFieldMetadata tipiEntitaVettoreData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaVettore", tipiEntitaAgenteValueModel, tipiEntitaVettoreData);
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,f:50dlu,10dlu,right:pref,4dlu,f:50dlu,10dlu,fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");

		((JTextField) builder.addPropertyAndLabel("annoCompetenza", 1, 2)[1]).setColumns(4);
		builder.nextRow();

		builder.addLabel("numeroDocumentoIniziale", 1, 4);
		builder.addBinding(bf.createBoundCodice("numeroDocumentoIniziale", true, false), 3, 4);
		builder.addLabel("numeroDocumentoFinale", 5, 4);
		builder.addBinding(bf.createBoundCodice("numeroDocumentoFinale", true, false), 7, 4);

		Binding bindingEntita = bf.createBoundSearchText("vettore",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaVettore" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class);
		((SearchPanel) bindingEntita.getControl()).getTextFields().get("codice").setColumns(6);
		builder.addLabel("vettore", 1, 6);
		builder.addBinding(bindingEntita, 3, 6, 5, 1);

		builder.addPropertyAndLabel("speditiAlVettore", 1, 8);

		builder.setComponentAttributes("f, f");
		List<TipoAreaMagazzino> tipiAreeMagazzino = magazzinoDocumentoBD.caricaTipiAreaMagazzino(
				"tipoDocumento.codice", null, true);
		builder.addBinding(
				bf.createBoundCheckBoxTree("tipiAreaMagazzino", new String[] { "tipoDocumento.abilitato",
						"tipoDocumento.classeTipoDocumento" }, new ValueHolder(tipiAreeMagazzino)), 9, 1, 1, 9);
		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		logger.debug("--> Enter createNewObject");
		ParametriRicercaSpedizioni parametriRicercaSpedizioni = new ParametriRicercaSpedizioni();
		parametriRicercaSpedizioni.setStatiAreaMagazzino(new HashSet<AreaMagazzino.StatoAreaMagazzino>(Arrays
				.asList(new AreaMagazzino.StatoAreaMagazzino[] { StatoAreaMagazzino.CONFERMATO,
						StatoAreaMagazzino.FORZATO })));
		parametriRicercaSpedizioni.setAnnoCompetenza(-1);
		parametriRicercaSpedizioni.setTipiGenerazione(null);
		parametriRicercaSpedizioni.setSpeditiAlVettore(Boolean.FALSE);
		parametriRicercaSpedizioni.setSoloNonRendicontati(Boolean.TRUE);
		parametriRicercaSpedizioni.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
		logger.debug("--> Exit createNewObject");
		return parametriRicercaSpedizioni;
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
