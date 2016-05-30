package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.springframework.binding.form.FormModel;

/**
 * Quando cambia il documento o l'area contabile Lite devo disabilitare tutti i campi che non posso modificare perchè
 * collegati ad altre aree.
 * 
 * @author giangi
 * 
 */
public class DocumentoPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	private String areaDocumentoPropertyPath;
	private String tipoAreaDocumentoPropertyPath;

	/**
	 * @return the areaDocumentoPropertyPath
	 */
	public String getAreaDocumentoPropertyPath() {
		return areaDocumentoPropertyPath;
	}

	/**
	 * @return the tipoAreaDocumentoPropertyPath
	 */
	public String getTipoAreaDocumentoPropertyPath() {
		return tipoAreaDocumentoPropertyPath;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean enabled = true;

		IAreaDocumento areaDocumento = (IAreaDocumento) formModel.getValueModel(getAreaDocumentoPropertyPath())
				.getValue();

		formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".documento.codice").setEnabled(true);

		if (areaDocumento.isNew()) {
			return;
		}

		AreaContabileLite areaContabileLite = null;
		if (formModel.hasValueModel("areaContabileLite")) {
			areaContabileLite = (AreaContabileLite) formModel.getValueModel("areaContabileLite").getValue();
		}
		Documento documento = areaDocumento.getDocumento();

		TipoDocumento tipoDocumento = (TipoDocumento) formModel.getValueModel(
				getTipoAreaDocumentoPropertyPath() + ".tipoDocumento").getValue();

		enabled = !(((areaDocumento.isNew() && documento.getId() != null) || areaContabileLite != null));
		// se l'area magazzino è nuova ma il documento ha un id significa che ho
		// collegato un documento
		// oppure ho un area contabile collegata
		// Quindi non posso modificare i campi del documento
		formModel.getFieldMetadata(getTipoAreaDocumentoPropertyPath()).setEnabled(enabled);
		formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".documento.dataDocumento").setEnabled(enabled);
		formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".documento.codice").setEnabled(enabled);
		formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".documento.totale").setEnabled(enabled);
		formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".documento.entita").setEnabled(enabled);

		if (formModel.hasValueModel(getAreaDocumentoPropertyPath() + ".percScontoIncasso")) {
			formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".percScontoIncasso").setEnabled(enabled);
		}
		if ((Boolean) formModel.getValueModel("areaRateEnabled").getValue()) {
			formModel.getFieldMetadata("areaRate.codicePagamento").setEnabled(enabled);
			// non disabilito le spese perche' altrimenti viene nascosto il
			// campo "areaRate.speseIncasso"
		}

		// Per l'entita verifico il tipoDocumento. Se è azienda non la
		// riabilito, altrimenti ricomparirebbe
		if (tipoDocumento != null) {
			if (TipoEntita.CLIENTE.equals(tipoDocumento.getTipoEntita())
					|| (TipoEntita.FORNITORE.equals(tipoDocumento.getTipoEntita()))) {
				formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".documento.entita").setEnabled(enabled);
			} else {
				formModel.getFieldMetadata(getAreaDocumentoPropertyPath() + ".documento.entita").setEnabled(false);
			}
		}
	}

	/**
	 * @param areaDocumentoPropertyPath
	 *            the areaDocumentoPropertyPath to set
	 */
	public void setAreaDocumentoPropertyPath(String areaDocumentoPropertyPath) {
		this.areaDocumentoPropertyPath = areaDocumentoPropertyPath;
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

	/**
	 * @param tipoAreaDocumentoPropertyPath
	 *            the tipoAreaDocumentoPropertyPath to set
	 */
	public void setTipoAreaDocumentoPropertyPath(String tipoAreaDocumentoPropertyPath) {
		this.tipoAreaDocumentoPropertyPath = tipoAreaDocumentoPropertyPath;
	}

}
