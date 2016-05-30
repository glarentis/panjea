package it.eurotn.panjea.ordini.rich.forms.righeordine;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaArticoloDocumentoArticoloPropertyChange;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;

public class RigaArticoloOrdineArticoloPropertyChange extends RigaArticoloDocumentoArticoloPropertyChange {

	private static Logger logger = Logger.getLogger(RigaArticoloOrdineArticoloPropertyChange.class);

	private final IOrdiniDocumentoBD ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	private Boolean calcolaGiacenza;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 */
	public RigaArticoloOrdineArticoloPropertyChange(final FormModel formModel) {
		super(formModel);
		calcolaGiacenza = null;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public IRigaArticoloDocumento creaRigaArticoloDocumento(ArticoloLite articolo) {
		IRigaArticoloDocumento rigaArticolo = null;
		AreaOrdine areaOrdine = null;
		CodicePagamento codicePagamento = null;
		java.util.Date dataConsegna = null;
		try {
			areaOrdine = (AreaOrdine) getFormModel().getValueModel("areaOrdine").getValue();
			codicePagamento = (CodicePagamento) getFormModel().getValueModel("codicePagamento").getValue();
			dataConsegna = (java.util.Date) getFormModel().getValueModel("dataConsegna").getValue();
		} catch (Exception e) {
			logger.error("--> errore  in propertyChange", e);
			throw new RuntimeException("--> errore  in propertyChange", e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> creaRigaArticolo " + articolo + " - " + areaOrdine);
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

			if (areaOrdine.getListinoAlternativo() != null) {
				idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
			}
			if (areaOrdine.getListino() != null) {
				idListino = areaOrdine.getListino().getId();
			}
			if (areaOrdine.getDocumento().getSedeEntita() != null) {
				idSedeEntita = areaOrdine.getDocumento().getSedeEntita().getId();
				codiceLingua = areaOrdine.getDocumento().getSedeEntita().getLingua();
			}
			if (areaOrdine.getAgente() != null) {
				idAgente = areaOrdine.getAgente().getId();
			}
			if (codicePagamento != null) {
				percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
			}

			if (calcolaGiacenza == null) {
				calcolaGiacenza = magazzinoAnagraficaBD.caricaMagazzinoSettings().getCalcolaGiacenzeInCreazioneRiga();
			}

			rigaArticolo = ordiniDocumentoBD.creaRigaArticolo(ProvenienzaPrezzo.LISTINO, articolo.getId(), areaOrdine
					.getDocumento().getDataDocumento(), idSedeEntita, idListinoAlternativo, idListino, areaOrdine
					.getDocumento().getTotale().clone(), areaOrdine.getCodiceIvaAlternativo(), idTipoMezzo,
					idZonaGeografica, articolo.getProvenienzaPrezzoArticolo(), areaOrdine.getTipoAreaOrdine()
							.isNoteSuDestinazione(), areaOrdine.getDocumento().getTotale().getCodiceValuta(),
					codiceLingua, idAgente, areaOrdine.getTipologiaCodiceIvaAlternativo(),
					percentualeScontoCommerciale, areaOrdine.getTipoAreaOrdine().isOrdineProduzione(), areaOrdine
							.getDepositoOrigine().getId(), calcolaGiacenza);

			// NPE MAIL
			if (rigaArticolo != null) {
				((RigaOrdine) rigaArticolo).setAreaOrdine(areaOrdine);
				((RigaArticolo) rigaArticolo).setDataConsegna(dataConsegna);

				if (areaOrdine.getTipoAreaOrdine().isOrdineProduzione()) {
					((RigaArticolo) rigaArticolo).setDataProduzione(dataConsegna);
				}
			}
		} catch (Exception e) {
			logger.error("--> errore nel creare una riga articolo ", e);
			throw new PanjeaRuntimeException(e);
		}

		return rigaArticolo;
	}

}
