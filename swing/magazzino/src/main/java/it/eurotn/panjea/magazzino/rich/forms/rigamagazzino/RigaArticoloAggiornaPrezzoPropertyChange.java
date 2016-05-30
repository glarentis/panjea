package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

/**
 * Al cambiamento di una proprietà che ricalcola i vari importi <br>
 * ricarica i valori interessati (es cambiando la qta viene ricalcolato automaticamente <br>
 * il prezzo netto e totale)
 */
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

public class RigaArticoloAggiornaPrezzoPropertyChange implements FormModelPropertyChangeListeners {
	private static Logger logger = Logger.getLogger(RigaArticoloAggiornaPrezzoPropertyChange.class);
	private FormModel formModel;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter RigaArticoloAggiornaPrezzoPropertyChange.propertyChange " + evt.getPropertyName());
		try {
			if (formModel == null || formModel.isReadOnly()) {
				logger.debug("--> Exit RigaArticoloAggiornaPrezzoPropertyChange form model is read only");
				return;
			}

			Importo importo = (Importo) formModel.getValueModel("prezzoNetto").getValue();
			formModel.getValueModel("prezzoNetto").setValue(importo.clone());
			importo = (Importo) formModel.getValueModel("prezzoTotale").getValue();
			formModel.getValueModel("prezzoTotale").setValue(importo.clone());
		} catch (Exception e) {
			logger.error("--> errore nel property change", e);
		}
		logger.debug("--> Exit RigaArticoloAggiornaPrezzoPropertyChange.propertyChange");
	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

}
