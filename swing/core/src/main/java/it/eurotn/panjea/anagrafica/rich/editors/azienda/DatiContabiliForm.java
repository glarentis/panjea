/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * 
 * @author Aracno
 * @version 1.0, 15-mag-2006
 * 
 */
public class DatiContabiliForm extends PanjeaAbstractForm {

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param formId
	 *            id del form
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public DatiContabiliForm(final FormModel formModel, final String formId, final IAnagraficaBD anagraficaBD) {
		super(formModel, formId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		builder.addSeparator("rapportoBancarioAziendaSeparator");
		builder.row();
		builder.add("azienda.rapportoBancarioAzienda.codiceRapporto", "colSpan=1 align=left");
		builder.add("azienda.rapportoBancarioAzienda.tipoRapporto", "colSpan=1 align=left");
		builder.row();
		builder.add(bf.createBoundSearchText("azienda.rapportoBancarioAzienda.banca",
				new String[] { Banca.PROP_DESCRIZIONE }));
		builder.add(bf.createBoundSearchText("azienda.rapportoBancarioAzienda.filiale",
				new String[] { Filiale.PROP_DESCRIZIONE }, new String[] { "azienda.rapportoBancarioAzienda.banca" },
				new String[] { Banca.REF }));

		builder.row();
		builder.add("azienda.rapportoBancarioAzienda.numeroRapporto", "colSpan=1 align=left");
		builder.row();
		builder.addSeparator("datiProtocolloSeparator");
		builder.row();
		builder.add("azienda.datiProtocollo.codice", "colSpan=1 align=left");
		builder.add("azienda.datiProtocollo.descrizione", "colSpan=1 align=left");
		builder.row();
		builder.add("azienda.datiProtocollo.incremento", "colSpan=1 align=left");
		return builder.getForm();
	}

}
