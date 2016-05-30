package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.entity.EntityBase;
import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.util.RcpSupport;

/**
 * PropertyChange per la gestione della variazione di {@link Articolo} all'interno di RigaArticolo.
 * 
 * @author adriano
 * @version 1.0, 24/ott/2008
 * 
 */
public abstract class RigaArticoloDocumentoArticoloPropertyChange implements PropertyChangeListener, InitializingBean {

	private static Logger logger = Logger.getLogger(RigaArticoloDocumentoArticoloPropertyChange.class);

	private final FormModel formModel;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 */
	public RigaArticoloDocumentoArticoloPropertyChange(final FormModel formModel) {
		super();
		this.formModel = formModel;
	}

	/**
	 * Crea la riga articolo.
	 * 
	 * @param articoloLite
	 *            articolo di riferimento
	 * @return riga creata
	 */
	public abstract IRigaArticoloDocumento creaRigaArticoloDocumento(ArticoloLite articoloLite);

	/**
	 * @return the formModel
	 */
	public FormModel getFormModel() {
		return formModel;
	}

	@Override
	public void propertyChange(PropertyChangeEvent property) {
		logger.debug("--> Enter propertyChange articolo");
		try {
			// se non sono in editazione esco
			if (formModel.isReadOnly()) {
				logger.debug("--> Exit propertyChange. FormModel in sola lettura");
				return;
			}

			ArticoloLite articolo = (ArticoloLite) property.getNewValue();
			try {
				if (articolo == null) {
					// NPE mail, se l'articolo è null devo togliere gli attributi
					formModel.getValueModel("attributi").setValue(new ArrayList<AttributoRigaArticolo>());
					logger.debug("--> Exit propertyChange articolo.id==null ");
					return;
				}
				if (property.getNewValue().equals(property.getOldValue())) {
					logger.debug("--> Exit propertyChange articolo not change ");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// NOTA: non faccio nulla in questo catch se è relativo
			// all'eccezione FormuleTipoAttributoException (nested
			// in una runtime)
			// Essendo nell'articolo change listener voglio finire di impostare
			// la riga del form model anche se ho
			// questo errore; esso verrà comunque rilanciato impostando la qta e
			// in quel caso sarà mostrato all'utente
			try {
				getFormModel().getValueModel("qta").setValue(0.0);
			} catch (RuntimeException e) {
				if (e != null && e.getCause() != null && e.getCause() instanceof FormuleTipoAttributoException) {
					logger.warn("Errore in forumla, non faccio nulla per continuare la setRigaArticoloDocumento nel form model");
				} else {
					throw e;
				}
			}

			getFormModel().getValueModel("qtaMagazzino").setValue(0.0);
			getFormModel().getValueModel("variazione1").setValue(BigDecimal.ZERO);
			getFormModel().getValueModel("variazione2").setValue(BigDecimal.ZERO);
			getFormModel().getValueModel("variazione3").setValue(BigDecimal.ZERO);
			getFormModel().getValueModel("variazione4").setValue(BigDecimal.ZERO);
			formModel.getFieldMetadata("politicaPrezzo").setEnabled(false);

			IRigaArticoloDocumento rigaArticolo = creaRigaArticoloDocumento(articolo);

			// NPE mail riga articolo sembra essere null
			if (rigaArticolo == null) {
				logger.error("rigaArticolo=null, articolo=" + articolo + ", ");
				return;
			}

			// se ci sono righe articolo figlie contenute nella rigaArticolo
			// ritornata da creaRigaArticolo, allora
			// chiedo all'utente le quantità per ogni riga figlia esistente.
			// rigaArticolo = creaRigheFiglie(rigaArticolo);

			if (logger.isDebugEnabled()) {
				for (Entry<Double, RisultatoPrezzo<Sconto>> entry : rigaArticolo.getPoliticaPrezzo().getSconti()
						.entrySet()) {
					logger.debug("Qta : " + entry.getKey() + " Sconti:" + entry.getValue().getValue().getSconto1()
							+ ":" + entry.getValue().getValue().getSconto2() + ":"
							+ entry.getValue().getValue().getSconto3() + ":" + entry.getValue().getValue().getSconto4());
				}
			}

			// se la riga articolo corrente nel form è presente nel db, devo
			// riportare sulla nuova riga creata id e
			// version in modo da rendere persistenti le modifiche sulla riga
			// scelta
			IDefProperty rigaArticoloCurrent = (IDefProperty) formModel.getFormObject();
			Integer oldId = rigaArticoloCurrent.getId();
			Integer oldVersion = rigaArticoloCurrent.getVersion();
			String codiceEntitaArticolo = ((IRigaArticoloDocumento) rigaArticoloCurrent).getArticolo()
					.getCodiceEntita();
			Double oldOrdinamento = (Double) formModel.getValueModel("ordinamento").getValue();
			if (!rigaArticoloCurrent.isNew() && !rigaArticoloCurrent.getClass().equals(rigaArticolo.getClass())) {
				RcpSupport.showWarningDialog("rigaarticolo.tipodiverso");
				formModel.revert();
				return;
			}
			// setto id a -1 perche dopo la setformobject lo risetto e cosè
			// sporco il form.
			// questo perchè dopo la selezione dell'articolo posso salvare
			// subito la riga e devo attivare il pulsante
			// salva.
			((EntityBase) rigaArticolo).setId(-1);
			rigaArticolo.getArticolo().setCodiceEntita(codiceEntitaArticolo);
			getFormModel().setFormObject(rigaArticolo);

			// qui imposto id e version che sono avvalorati se modifico una riga
			// esistente altrimenti rimangono null
			// (nuova riga)
			formModel.getValueModel("id").setValue(oldId);
			formModel.getValueModel("version").setValue(oldVersion);
			if (oldOrdinamento != null) {
				formModel.getValueModel("ordinamento").setValue(oldOrdinamento);
			}

			if (rigaArticolo.getComponenti() != null) {
				// formModel.getFieldMetadata("qta").setReadOnly(true);
				formModel.getFieldMetadata("qtaMagazzino").setReadOnly(true);
			} else {
				formModel.getFieldMetadata("qtaMagazzino").setReadOnly(
						formModel.getValueModel("formulaTrasformazioneQtaMagazzino").getValue() != null);
			}
			formModel.getFieldMetadata("qta").setReadOnly(
					formModel.getValueModel("formulaTrasformazione").getValue() != null);

			if (logger.isDebugEnabled()) {
				logger.debug("--> Exit propertyChange con RigaArticolo " + rigaArticolo);
			}

			((ValidatingFormModel) formModel).validate();
		} catch (Exception e) {
			logger.error("--> errore Exception in propertyChange", e);
		}

	}

}
