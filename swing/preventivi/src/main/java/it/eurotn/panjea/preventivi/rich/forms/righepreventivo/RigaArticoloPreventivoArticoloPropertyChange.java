package it.eurotn.panjea.preventivi.rich.forms.righepreventivo;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaArticoloDocumentoArticoloPropertyChange;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.bd.PreventiviBD;
import it.eurotn.util.PanjeaEJBUtil;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.util.RcpSupport;

public class RigaArticoloPreventivoArticoloPropertyChange extends RigaArticoloDocumentoArticoloPropertyChange {

	private static Logger logger = Logger.getLogger(RigaArticoloPreventivoArticoloPropertyChange.class);

	private final IPreventiviBD preventiviBD = RcpSupport.getBean(PreventiviBD.BEAN_ID);

	/**
	 * Costruttore.
	 *
	 * @param formModel
	 *            form model
	 */
	public RigaArticoloPreventivoArticoloPropertyChange(final FormModel formModel) {
		super(formModel);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public IRigaArticoloDocumento creaRigaArticoloDocumento(ArticoloLite articolo) {
		RigaArticolo rigaArticolo = null;
		AreaPreventivo areaPreventivo = null;
		CodicePagamento codicePagamento = null;
		java.util.Date dataConsegna = null;
		try {
			areaPreventivo = (AreaPreventivo) getFormModel().getValueModel("areaPreventivo").getValue();
			codicePagamento = (CodicePagamento) getFormModel().getValueModel("codicePagamento").getValue();
			dataConsegna = (java.util.Date) getFormModel().getValueModel("dataConsegna").getValue();
		} catch (Exception e) {
			logger.error("--> errore  in propertyChange", e);
			throw new RuntimeException("--> errore  in propertyChange", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> creaRigaArticolo " + articolo + " - " + areaPreventivo);
		}
		try {
			Integer idListinoAlternativo = null;
			Integer idListino = null;
			Integer idSedeEntita = null;
			Integer idTipoMezzo = null;
			Integer idZonaGeografica = null;
			String codiceLingua = null;
			Integer idAgente = null;
			BigDecimal percentualeScontoCommerciale = null;

			if (areaPreventivo.getListinoAlternativo() != null) {
				idListinoAlternativo = areaPreventivo.getListinoAlternativo().getId();
			}
			if (areaPreventivo.getListino() != null) {
				idListino = areaPreventivo.getListino().getId();
			}
			if (areaPreventivo.getDocumento().getSedeEntita() != null) {
				idSedeEntita = areaPreventivo.getDocumento().getSedeEntita().getId();
				codiceLingua = areaPreventivo.getDocumento().getSedeEntita().getLingua();
			}
			if (codicePagamento != null) {
				percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
			}

			ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
			parametri.setProvenienzaPrezzo(ProvenienzaPrezzo.LISTINO);
			parametri.setIdArticolo(articolo.getId());
			parametri.setData(areaPreventivo.getDocumento().getDataDocumento());
			parametri.setIdSedeEntita(idSedeEntita);
			parametri.setIdListinoAlternativo(idListinoAlternativo);
			parametri.setIdListino(idListino);
			parametri.setImporto(areaPreventivo.getDocumento().getTotale().clone());
			parametri.setCodiceIvaAlternativo(areaPreventivo.getCodiceIvaAlternativo());
			parametri.setIdTipoMezzo(idTipoMezzo);
			parametri.setIdZonaGeografica(idZonaGeografica);
			parametri.setProvenienzaPrezzoArticolo(articolo.getProvenienzaPrezzoArticolo());
			parametri.setNoteSuDestinazione(areaPreventivo.getTipoAreaPreventivo().isNoteSuDestinazione());
			parametri.setTipoMovimento(TipoMovimento.NESSUNO);
			parametri.setGestioneArticoloDistinta(false);
			parametri.setCodiceValuta(areaPreventivo.getDocumento().getTotale().getCodiceValuta());
			parametri.setCodiceLingua(codiceLingua);
			parametri.setIdAgente(idAgente);
			parametri.setTipologiaCodiceIvaAlternativo(areaPreventivo.getTipologiaCodiceIvaAlternativo());
			parametri.setPercentualeScontoCommerciale(percentualeScontoCommerciale);
			rigaArticolo = preventiviBD.creaRigaArticolo(parametri);
			// NPE Mail
			if (rigaArticolo != null) {
				rigaArticolo.setAreaPreventivo(areaPreventivo);
				rigaArticolo.setDataConsegna(dataConsegna);
			}
		} catch (Exception e) {
			logger.error("--> errore nel creare una riga articolo ", e);
			throw new PanjeaRuntimeException(e);
		}

		// Non sò perchè ma, a differenza degli ordini e magazzino, la creazione della riga non fa scattare il property
		// change che abilità/disabilità i controlli in base alle riga creata quindi forzo la cosa
		getFormModel().setFormObject(PanjeaEJBUtil.cloneObject(getFormModel().getFormObject()));

		return rigaArticolo;
	}

	@Override
	public void propertyChange(PropertyChangeEvent property) {
		logger.debug("--> Enter propertyChange articolo");

		// se non sono in editazione esco
		if (getFormModel().isReadOnly()) {
			logger.debug("--> Exit propertyChange. FormModel in sola lettura");
			return;
		}

		boolean sostituzioneArticolo = (boolean) getFormModel().getValueModel("sostituzioneArticolo").getValue();
		if (!sostituzioneArticolo) {
			super.propertyChange(property);
		} else {
			try {
				ArticoloLite articolo = (ArticoloLite) property.getNewValue();
				try {
					if (articolo == null) {
						// NPE mail, se l'articolo è null devo togliere gli attributi
						getFormModel().getValueModel("attributi").setValue(new ArrayList<AttributoRigaArticolo>());
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

				getFormModel().getFieldMetadata("politicaPrezzo").setEnabled(false);

				IRigaArticoloDocumento rigaArticolo = creaRigaArticoloDocumento(articolo);

				// NPE mail riga articolo sembra essere null
				if (rigaArticolo == null) {
					logger.error("rigaArticolo=null, articolo=" + articolo + ", ");
					return;
				}

				// se la riga articolo corrente nel form è presente nel db, devo
				// riportare sulla nuova riga creata id e
				// version in modo da rendere persistenti le modifiche sulla riga
				// scelta
				RigaArticolo rigaArticoloCurrent = (RigaArticolo) getFormModel().getFormObject();
				Integer oldId = rigaArticoloCurrent.getId();
				Integer oldVersion = rigaArticoloCurrent.getVersion();
				Double oldOrdinamento = (Double) getFormModel().getValueModel("ordinamento").getValue();
				if (!rigaArticoloCurrent.isNew() && !rigaArticoloCurrent.getClass().equals(rigaArticolo.getClass())) {
					RcpSupport.showWarningDialog("rigaarticolo.tipodiverso");
					getFormModel().revert();
					return;
				}
				// setto id a -1 perche dopo la setformobject lo risetto e cosè
				// sporco il form.
				// questo perchè dopo la selezione dell'articolo posso salvare
				// subito la riga e devo attivare il pulsante
				// salva.
				((EntityBase) rigaArticolo).setId(-1);
				rigaArticolo.setQta(rigaArticoloCurrent.getQta());
				rigaArticolo.setQtaMagazzino(rigaArticoloCurrent.getQtaMagazzino());
				rigaArticolo.setTipoOmaggio(rigaArticoloCurrent.getTipoOmaggio());
				rigaArticolo.setPrezzoUnitario(rigaArticoloCurrent.getPrezzoUnitario());
				rigaArticolo.setPrezzoDeterminato(rigaArticoloCurrent.getPrezzoDeterminato());
				rigaArticolo.setVariazione1(rigaArticoloCurrent.getVariazione1());
				rigaArticolo.setVariazione2(rigaArticoloCurrent.getVariazione2());
				rigaArticolo.setVariazione3(rigaArticoloCurrent.getVariazione3());
				rigaArticolo.setVariazione4(rigaArticoloCurrent.getVariazione4());
				rigaArticolo.setPrezzoUnitario(rigaArticoloCurrent.getPrezzoUnitario());

				getFormModel().setFormObject(rigaArticolo);

				// qui imposto id e version che sono avvalorati se modifico una riga
				// esistente altrimenti rimangono null
				// (nuova riga)
				getFormModel().getValueModel("id").setValue(oldId);
				getFormModel().getValueModel("version").setValue(oldVersion);
				if (oldOrdinamento != null) {
					getFormModel().getValueModel("ordinamento").setValue(oldOrdinamento);
				}

				if (rigaArticolo.getComponenti() != null) {
					// getFormModel().getFieldMetadata("qta").setReadOnly(true);
					getFormModel().getFieldMetadata("qtaMagazzino").setReadOnly(true);
				} else {
					getFormModel().getFieldMetadata("qtaMagazzino").setReadOnly(
							getFormModel().getValueModel("formulaTrasformazioneQtaMagazzino").getValue() != null);
				}
				getFormModel().getFieldMetadata("qta").setReadOnly(
						getFormModel().getValueModel("formulaTrasformazione").getValue() != null);

				if (logger.isDebugEnabled()) {
					logger.debug("--> Exit propertyChange con RigaArticolo " + rigaArticolo);
				}

				((ValidatingFormModel) getFormModel()).validate();
			} catch (Exception e) {
				logger.error("--> errore Exception in propertyChange", e);
			}
		}
	}

}
