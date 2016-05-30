package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.rich.bd.IModuloPrezzoBD;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

public class RigaArticoloAgentePropertyChange implements FormModelPropertyChangeListeners {

	private FormModel formModel;

	private static Logger logger = Logger.getLogger(RigaArticoloAgentePropertyChange.class);

	private IModuloPrezzoBD moduloPrezzoBD;

	/**
	 * @return Returns the moduloPrezzoBD.
	 */
	public IModuloPrezzoBD getModuloPrezzoBD() {
		return moduloPrezzoBD;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (formModel.isReadOnly()) {
			// se sono in sola lettura non devo fare nulla
			return;
		}

		AreaMagazzino areaMagazzino = null;
		ArticoloLite articolo = null;

		areaMagazzino = (AreaMagazzino) formModel.getValueModel("areaMagazzino").getValue();

		AgenteLite agente = (AgenteLite) formModel.getValueModel("agente").getValue();
		articolo = (ArticoloLite) formModel.getValueModel("articolo").getValue();
		CodicePagamento codicePagamento = (CodicePagamento) formModel.getValueModel("codicePagamento").getValue();

		// NPE Mail alla riga 84 (v0.5.0) riprodotto errore quando avendo nel dettaglio una nuova riga articolo, eseguo
		// il command nuova area magazzino
		if (articolo == null || areaMagazzino.getId() == null) {
			return;
		}

		try {
			Integer idListinoAlternativo = null;
			Integer idListino = null;
			Integer idSedeEntita = null;
			Integer idTipoMezzo = null;
			Integer idAgente = null;
			BigDecimal percentualeScontoCommerciale = null;

			if (areaMagazzino.getListinoAlternativo() != null) {
				idListinoAlternativo = areaMagazzino.getListinoAlternativo().getId();
			}
			if (areaMagazzino.getListino() != null) {
				idListino = areaMagazzino.getListino().getId();
			}
			if (areaMagazzino.getMezzoTrasporto() != null) {
				idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
			}
			if (areaMagazzino.getDocumento().getSedeEntita() != null) {
				idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
			}
			if (agente != null) {
				idAgente = agente.getId();
			}

			if (codicePagamento != null) {
				percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
			}

			ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(), areaMagazzino
					.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null, idSedeEntita, null,
					null, areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo(), idTipoMezzo,
					areaMagazzino.getIdZonaGeografica(), articolo.getProvenienzaPrezzoArticolo(), areaMagazzino
							.getDocumento().getTotale().getCodiceValuta(), idAgente, percentualeScontoCommerciale);
			IRigaArticoloDocumento rigaArticolo = (IRigaArticoloDocumento) formModel.getFormObject();

			PoliticaPrezzo politicaPrezzo = moduloPrezzoBD.calcola(parametriCalcoloPrezzi);
			Double qta = null;
			BigDecimal prezzoPerQta = null;
			try {
				// Ricevuta npe tramite mail, loggo per avere più debug e capire cos'è a null
				qta = (Double) formModel.getValueModel("qta").getValue();
				RisultatoPrezzo<BigDecimal> risultatoProvvigione = politicaPrezzo.getProvvigioni().getRisultatoPrezzo(
						qta);
				if (risultatoProvvigione != null) {
					prezzoPerQta = risultatoProvvigione.getValue();
				}

				formModel.getValueModel("percProvvigione").setValue(prezzoPerQta);
			} catch (NullPointerException e) {
				logger.error("NPE RigaArticoloAgentePropertyChange: politicaPrezzo.getProvvigioni() "
						+ politicaPrezzo.getProvvigioni());
				logger.error("NPE RigaArticoloAgentePropertyChange: politicaPrezzo.getProvvigioni().getRisultatoPrezzo(qta) "
						+ politicaPrezzo.getProvvigioni().getRisultatoPrezzo(qta));
				logger.error("NPE RigaArticoloAgentePropertyChange: qta " + qta + ", prezzoPerQta " + prezzoPerQta);
				logger.error(
						"-->errore nell'assegnare la pro. agente. policaPrezzo in rigaArticolo: "
								+ rigaArticolo.getPoliticaPrezzo() + " politica prezzo nel formmodel: "
								+ formModel.getValueModel("politicaPrezzo").getValue(), e);
			}
		} catch (Exception e) {
			logger.error("--> errore nel creare una riga articolo ", e);
			throw new PanjeaRuntimeException(e);
		}

	}

	@Override
	public void setFormModel(FormModel formModel) {
		this.formModel = formModel;
	}

	/**
	 * @param moduloPrezzoBD
	 *            The moduloPrezzoBD to set.
	 */
	public void setModuloPrezzoBD(IModuloPrezzoBD moduloPrezzoBD) {
		this.moduloPrezzoBD = moduloPrezzoBD;
	}

}
