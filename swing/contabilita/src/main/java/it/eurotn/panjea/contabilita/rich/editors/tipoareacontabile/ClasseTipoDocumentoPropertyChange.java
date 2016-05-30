/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.tipoareacontabile;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseIncassoPagamento;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

/**
 * FormModelPropertyChange legata alla proprieta' classeTipoDocumentoInstance per abilitare/disabilitare i controlli di
 * {@link TipoAreaContabile} <br>
 * per la gestione dei dati per la generazione dell'AreaContabile attraverso gli incassi pagamento
 * 
 * @author adriano
 * @version 1.0, 01/dic/2008
 * 
 */
public class ClasseTipoDocumentoPropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	static Logger logger = Logger.getLogger(ClasseTipoDocumentoPropertyChange.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		IClasseTipoDocumento classeTipoDocumento = (IClasseTipoDocumento) evt.getNewValue();
		if (classeTipoDocumento instanceof ClasseIncassoPagamento) {
			formModel.getFieldMetadata("contoCassa").setEnabled(true);
		} else {
			formModel.getFieldMetadata("contoCassa").setEnabled(false);
		}
		logger.debug("--> Exit propertyChange");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eurotn.rich.form.FormModelPropertyChangeListeners#setFormModel(org.springframework.binding.form.FormModel)
	 */
	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
