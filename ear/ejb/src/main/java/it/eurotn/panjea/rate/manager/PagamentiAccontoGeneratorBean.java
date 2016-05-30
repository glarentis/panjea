package it.eurotn.panjea.rate.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.manager.interfaces.PagamentiAccontoGenerator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAccontoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaChiusureManager;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.PagamentiAccontoGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PagamentiAccontoGenerator")
public class PagamentiAccontoGeneratorBean implements PagamentiAccontoGenerator {

	private static Logger logger = Logger.getLogger(PagamentiAccontoGeneratorBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AreaAccontoManager areaAccontoManager;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private AreaChiusureManager areaChiusureManager;

	@EJB
	private AreaContabileManager areaContabileManager;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	private TipoDocumentoBasePartite tipoDocumentoBaseFornitore;

	private TipoDocumentoBasePartite tipoDocumentoBaseCliente;

	@Override
	public List<AreaChiusure> creaPagamentiConAcconto(List<Pagamento> pagamenti, List<AreaAcconto> acconti)
			throws TipoDocumentoBaseException {
		List<AreaChiusure> areePagamentiAcconto = new ArrayList<AreaChiusure>();
		if (pagamenti.size() > 0) {

			// Controllo se ho il tipoDocumento base per il tipo partita della prima rata contenuta.
			// Nella lista ci devono essere rate di un solo tipo.
			Pagamento primoImportoRataAcconto = pagamenti.iterator().next();
			Rata rata = primoImportoRataAcconto.getRata();

			TipoPartita tipoPartita = rata.getAreaRate().getTipoAreaPartita().getTipoPartita();
			TipoAreaPartita tipoAreaPartitaBasePerUtilizzoAcconto = getTipoDocumentoBase(tipoPartita);

			for (Pagamento pagamento : pagamenti) {
				if (acconti == null) {
					Documento documento = pagamento.getRata().getAreaRate().getDocumento();
					acconti = areaAccontoManager.caricaAccontiAutomaticiPerPagamenti(documento.getEntita(), documento
							.getTotale().getCodiceValuta());
				}

				if (acconti != null && !acconti.isEmpty()) {
					List<AreaChiusure> areeAccontoPerRata = creaPagamentiRataConAcconto(pagamento, acconti,
							tipoAreaPartitaBasePerUtilizzoAcconto);
					areePagamentiAcconto.addAll(areeAccontoPerRata);
				}
			}
		}
		return areePagamentiAcconto;
	}

	/**
	 * Usando tutti gli acconti disponibili vengono creati i pagamenti per la rata.
	 *
	 * @param pag
	 *            pagamento con la rata
	 * @param acconti
	 *            gli acconti per i quali generare i pagamenti
	 * @param tipoAreaPartitaBasePerUtilizzoAcconto
	 *            il tipo area partita associato al tipo documento base per utilizzo acconto (cliente o fornitore)
	 * @return aree create
	 */
	private List<AreaChiusure> creaPagamentiRataConAcconto(Pagamento pag, List<AreaAcconto> acconti,
			TipoAreaPartita tipoAreaPartitaBasePerUtilizzoAcconto) {
		List<AreaChiusure> pagamentiAcconto = new ArrayList<AreaChiusure>();

		BigDecimal importoDaPagare = pag.getImporto().getImportoInValuta();
		Rata rata = pag.getRata();

		BigDecimal residuoRata = rata.getResiduo().getImportoInValuta();

		List<Pagamento> pagamenti = new ArrayList<Pagamento>();
		Date dataPagamento = getDataPagamento(rata.getAreaRate().getDocumento());
		for (AreaAcconto areaAcconto : acconti) {

			// devo ricaricare l'area acconto, il residuo dell'acconto è basato sull'importoUtilizzato il quale è un
			// campo formula (senza la refresh non viene ricalcolato); faccio la refresh solo se l'areaAcconto è già in
			// sessione, altrimenti viene dal client e non serve eseguire la refresh
			boolean contains = panjeaDAO.getEntityManager().contains(areaAcconto);
			if (contains) {
				panjeaDAO.getEntityManager().refresh(areaAcconto);
			}

			BigDecimal importoPagamento = BigDecimal.ZERO;
			BigDecimal residuoAcconto = areaAcconto.getResiduo();
			// se ho raggiunto l'importo delle rata interrompo la creazione, così come se non ho più importo residuo
			// dell'acconto
			if (residuoRata.compareTo(BigDecimal.ZERO) == 0 || residuoAcconto.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}

			if (importoDaPagare.compareTo(residuoAcconto) <= 0 && importoDaPagare.compareTo(residuoRata) <= 0) {
				// l'importo che voglio pagare è minore o uguale del residuo
				// acconto e del residuo rata quindi lo uso per creare il pagamento
				importoPagamento = importoDaPagare;
				residuoRata = residuoRata.subtract(importoDaPagare);
			} else if (residuoAcconto.compareTo(residuoRata) <= 0) {
				// l'importo dell'acconto è minore o uguale al residuo quindi lo uso
				// per creare il pagamento
				importoPagamento = residuoAcconto;
				residuoRata = residuoRata.subtract(residuoAcconto);
			} else {
				// l'importo dell'acconto è maggiore del residuo della rata
				// quindi ne uso solo una parte
				importoPagamento = residuoRata;
				residuoRata = BigDecimal.ZERO;
			}

			Pagamento pagamento = new Pagamento();
			pagamento.setChiusuraForzataRata(false);
			pagamento.setDataCreazione(dataPagamento);
			pagamento.setDataPagamento(dataPagamento);

			Importo importo = new Importo();
			importo.setImportoInValuta(importoPagamento);
			pagamento.setImporto(importo);
			Importo importoForzato = new Importo();
			importoForzato.setImportoInValuta(BigDecimal.ZERO);
			pagamento.setImportoForzato(importoForzato);

			pagamento.setInsoluto(false);
			pagamento.setRata(rata);
			pagamento.setAreaAcconto(areaAcconto);

			pagamenti.add(pagamento);
		}

		ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure = new ParametriCreazioneAreaChiusure();
		parametriCreazioneAreaChiusure.setDataDocumento(dataPagamento);
		parametriCreazioneAreaChiusure.setTipoAreaPartita(tipoAreaPartitaBasePerUtilizzoAcconto);
		if (!pagamenti.isEmpty()) {
			try {
				pagamentiAcconto = areaChiusureManager.creaAreaChiusure(parametriCreazioneAreaChiusure, pagamenti);
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug("--> Errore durante la creazione dell'area di chiusura.", e);
				}
				throw new RuntimeException("Errore durante la creazione dell'area di chiusura.", e);
			}
		}

		return pagamentiAcconto;
	}

	/**
	 * @param documento
	 *            documento
	 * @return data pagamento valida
	 */
	private Date getDataPagamento(Documento documento) {
		Date dataRegistrazione = null;
		AreaContabile areaContabile = areaContabileManager.caricaAreaContabileByDocumento(documento.getId());
		if (areaContabile != null) {
			dataRegistrazione = areaContabile.getDataRegistrazione();
		}
		if (dataRegistrazione == null) {
			AreaMagazzino areaMagazzino = areaMagazzinoManager.caricaAreaMagazzinoByDocumento(documento);
			if (areaMagazzino != null) {
				dataRegistrazione = areaMagazzino.getDataRegistrazione();
			}
		}
		if (dataRegistrazione == null) {
			dataRegistrazione = documento.getDataDocumento();
		}
		return dataRegistrazione;
	}

	/**
	 * @param tipoPartita
	 *            tipoPartita della rata
	 * @return tipoAreaPartita per l'utilizzo acconto
	 * @throws TipoDocumentoBaseException
	 *             TipoDocumentoBaseException
	 */
	private TipoAreaPartita getTipoDocumentoBase(TipoPartita tipoPartita) throws TipoDocumentoBaseException {
		TipoAreaPartita result = null;
		switch (tipoPartita) {
		case PASSIVA:
			if (tipoDocumentoBaseFornitore == null) {
				try {
					tipoDocumentoBaseFornitore = tipiAreaPartitaManager
							.caricaTipoDocumentoBase(TipoOperazione.UTILIZZO_ACCONTO_FORNITORE);
					result = tipoDocumentoBaseFornitore.getTipoAreaPartita();
				} catch (TipoDocumentoBaseException e2) {
					logger.debug("--> Tipo documento base per UTILIZZO_ACCONTO_FORNITORE non trovato.");
					throw e2;
				}
			} else {
				result = tipoDocumentoBaseFornitore.getTipoAreaPartita();
			}
			return result;

		case ATTIVA:
			if (tipoDocumentoBaseCliente == null) {
				try {
					tipoDocumentoBaseCliente = tipiAreaPartitaManager
							.caricaTipoDocumentoBase(TipoOperazione.UTILIZZO_ACCONTO_CLIENTE);
					result = tipoDocumentoBaseCliente.getTipoAreaPartita();
				} catch (TipoDocumentoBaseException e2) {
					logger.debug("--> Tipo documento base per UTILIZZO_ACCONTO_CLIENTE non trovato.");
					throw e2;
				}
			} else {
				result = tipoDocumentoBaseCliente.getTipoAreaPartita();
			}
			return result;
		default:
			throw new UnsupportedOperationException("Tipo Partita errato: " + tipoPartita);
		}
	}
}
