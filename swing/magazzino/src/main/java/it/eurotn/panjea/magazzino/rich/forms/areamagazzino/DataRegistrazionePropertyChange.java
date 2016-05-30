package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.util.Date;

import org.springframework.binding.form.FormModel;

public class DataRegistrazionePropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	private String areaDocumentoPropertyPath;
	private String dataDocLikeDataRegPropertyPath;

	/**
	 * @return the areaDocumentoPropertyPath
	 */
	public String getAreaDocumentoPropertyPath() {
		return areaDocumentoPropertyPath;
	}

	/**
	 * @return the dataDocLikeDataRegPropertyPath
	 */
	public String getDataDocLikeDataRegPropertyPath() {
		return dataDocLikeDataRegPropertyPath;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// a volte mi arriva una null pointer, probabilmente dallo stacktrace
		// dell'errore è il formModel.
		// per sicurezza testo il null
		if (formModel == null) {
			return;
		}

		if (formModel.isReadOnly()) {
			return;
		}
		Boolean dataDocLikeDataReg = (Boolean) formModel.getValueModel(getDataDocLikeDataRegPropertyPath()).getValue();

		Date dataDocumento = (Date) formModel
				.getValueModel(getAreaDocumentoPropertyPath() + ".documento.dataDocumento").getValue();
		Date dataRegistrazione = (Date) formModel.getValueModel(getAreaDocumentoPropertyPath() + ".dataRegistrazione")
				.getValue();

		// Setto la data documento solamente se non è settata
		if (dataDocLikeDataReg != null && dataDocLikeDataReg && dataDocumento == null) {
			formModel.getValueModel(getAreaDocumentoPropertyPath() + ".documento.dataDocumento").setValueSilently(
					dataRegistrazione, this);
		}
	}

	/**
	 * @param areaDocumentoPropertyPath
	 *            the areaDocumentoPropertyPath to set
	 */
	public void setAreaDocumentoPropertyPath(String areaDocumentoPropertyPath) {
		this.areaDocumentoPropertyPath = areaDocumentoPropertyPath;
	}

	/**
	 * @param dataDocLikeDataRegPropertyPath
	 *            the dataDocLikeDataRegPropertyPath to set
	 */
	public void setDataDocLikeDataRegPropertyPath(String dataDocLikeDataRegPropertyPath) {
		this.dataDocLikeDataRegPropertyPath = dataDocLikeDataRegPropertyPath;
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
