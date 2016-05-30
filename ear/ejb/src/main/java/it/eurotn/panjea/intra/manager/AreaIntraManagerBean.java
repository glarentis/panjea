/**
 *
 */
package it.eurotn.panjea.intra.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.GruppoCondizioneConsegna;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.ModalitaIncasso;
import it.eurotn.panjea.intra.domain.ModalitaTrasporto;
import it.eurotn.panjea.intra.domain.NaturaTransazione;
import it.eurotn.panjea.intra.manager.interfaces.AreaIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.IntraSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 */
@Stateless(mappedName = "Panjea.AreaIntraManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaIntraManager")
public class AreaIntraManagerBean implements AreaIntraManager {

	private static Logger logger = Logger.getLogger(AreaIntraManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private IntraSettingsManager intraSettingsManager;

	@EJB
	protected AziendeManager aziendeManager;

	@Override
	public AreaIntra avvaloraDatiGeograficiAreaIntra(AreaIntra areaIntra, Documento documento, Deposito depositoOrigine) {
		String paesePagamento = null;
		String provincia = null;
		String paese = null;
		TipoEntita tipoEntita = documento.getTipoDocumento().getTipoEntita();
		AziendaLite aziendaCorrente = aziendeManager.caricaAzienda(true);

		if (tipoEntita.equals(TipoEntita.CLIENTE)) {
			// VENDITE
			// provincia di origine delle merci; nel caso delle vendite è la provincia del deposito di origine
			if (depositoOrigine != null) {
				provincia = depositoOrigine.getDatiGeografici().getSiglaProvincia();
			}

			// paese di destinazione delle merci, il codice della nazione del cliente
			paese = documento.getSedeEntita().getSede().getDatiGeografici().getNazione().getCodice();

			paesePagamento = aziendaCorrente.getNazione().getCodice();

		} else if (tipoEntita.equals(TipoEntita.FORNITORE)) {
			// ACQUISTI
			// la sigla della provincia in cui le merci sono destinate al consumo/commercializzazione
			// provincia dell'azienda corrente
			provincia = aziendaCorrente.getLivelloAmministrativo2() != null ? aziendaCorrente
					.getLivelloAmministrativo2().getSigla() : null;

			// paese dal quale le merci sono state spedite
			paese = documento.getSedeEntita().getSede().getDatiGeografici().getNazione().getCodice();

			paesePagamento = documento.getSedeEntita().getSede().getDatiGeografici().getNazione().getCodice();
		}
		// per i servizi
		areaIntra.setPaesePagamento(paesePagamento);

		// per i beni
		areaIntra.setProvincia(provincia);
		areaIntra.setPaese(paese);

		return areaIntra;
	}

	@Override
	public void cancellaAreaIntra(AreaIntra areaIntra) {
		logger.debug("--> Enter cancellaAreaOrdine");
		Documento documento = areaIntra.getDocumento();
		try {
			panjeaDAO.delete(areaIntra);
		} catch (Exception e) {
			logger.error("-->errore nel cancellare l'area ordine per il documento con codice " + documento.getCodice(),
					e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaAreaOrdine");
	}

	@Override
	public void cancellaAreaIntra(Documento documento) {
		AreaIntra areaIntra = caricaAreaIntraEsistente(documento);
		if (areaIntra != null) {
			cancellaAreaIntra(areaIntra);
		}
	}

	@Override
	public AreaIntra caricaAreaIntraByDocumento(Documento documento) {
		logger.debug("--> Enter caricaAreaIntraByDocumento");
		AreaIntra areaIntra = caricaAreaIntraEsistente(documento);
		if (areaIntra == null) {
			areaIntra = generaAreaIntra(documento);
		}
		logger.debug("--> Exit caricaAreaIntraByDocumento");
		return areaIntra;
	}

	/**
	 * Carica l'area intra del documento scelto e ritorna l'area intra trovata o null nel caso in cui non esiste.
	 * 
	 * @param documento
	 *            il documento per cui caricare l'area intra
	 * @return AreaIntra o null se non esiste
	 */
	private AreaIntra caricaAreaIntraEsistente(Documento documento) {
		logger.debug("--> Enter caricaAreaIntraByDocumento");
		Query query = panjeaDAO
				.prepareQuery("select a from AreaIntra a inner join fetch a.documento d where d.id = :paramIdDocumento");
		query.setParameter("paramIdDocumento", documento.getId());
		AreaIntra areaIntra = null;
		try {
			areaIntra = (AreaIntra) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.warn("Non esiste l'area intra per il documento " + documento.getId());
		} catch (DAOException e) {
			logger.error("--> errore nel caricare l'area intra per il documento " + documento.getId(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaAreaIntraByDocumento");
		return areaIntra;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Documento> caricaDocumentiSenzaIntra(ParametriRicercaAreaIntra parametri) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("d from AreaIntra ai ");
		sb.append("right join ai.documento d ");
		sb.append("join fetch d.tipoDocumento td ");
		sb.append("join fetch d.entita e ");
		sb.append("join fetch e.anagrafica an ");
		sb.append("join fetch an.sedeAnagrafica sedeA ");
		sb.append("join fetch sedeA.datiGeografici.nazione nazioneA ");
		sb.append("join fetch sedeA.datiGeografici.localita localitaA ");
		sb.append("join fetch sedeA.datiGeografici.cap capA ");
		sb.append("join fetch d.sedeEntita sed ");
		sb.append("join fetch sed.sede sede ");
		sb.append("join fetch sede.datiGeografici.localita localita ");
		sb.append("join fetch sede.datiGeografici.cap cap ");
		sb.append("where ai is null ");
		sb.append("and nazioneA.intra=true ");
		sb.append("and nazioneA.codice<>:codiceNazioneAzienda ");
		if (parametri.getPeriodoRegistrazione().getDataIniziale() != null) {
			sb.append("and d.dataDocumento>=:dataInizio ");
		}
		if (parametri.getPeriodoRegistrazione().getDataFinale() != null) {
			sb.append("and d.dataDocumento<=:dataFine ");
		}
		sb.append("and td.gestioneIntra=true ");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("codiceNazioneAzienda", parametri.getCodiceNazioneAzienda());
		if (parametri.getPeriodoRegistrazione().getDataIniziale() != null) {
			query.setParameter("dataInizio", parametri.getPeriodoRegistrazione().getDataIniziale());
		}
		if (parametri.getPeriodoRegistrazione().getDataFinale() != null) {
			query.setParameter("dataFine", parametri.getPeriodoRegistrazione().getDataFinale());
		}

		List<Documento> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore nel caricare i documenti senza aree intra", e);
			throw new RuntimeException("-->errore nel caricare i documenti senza aree intra", e);
		}
		return result;
	}

	@Override
	public AreaIntra generaAreaIntra(Documento documento) {
		documento = panjeaDAO.loadLazy(Documento.class, documento.getId());

		ModalitaTrasporto modalitaTrasporto = documento.getSedeEntita().getSedeMagazzino().getModalitaTrasporto();
		GruppoCondizioneConsegna gruppoCondizioneConsegna = documento.getSedeEntita().getSedeMagazzino().getTipoPorto() != null ? documento
				.getSedeEntita().getSedeMagazzino().getTipoPorto().getGruppoCondizioneConsegna()
				: null;
		NaturaTransazione naturaTransazione = documento.getSedeEntita().getSedeMagazzino().getCausaleTrasporto() != null ? documento
				.getSedeEntita().getSedeMagazzino().getCausaleTrasporto().getNaturaTransazione()
				: null;

		// genera l'area intra con i dati dell'area magazzino
		AreaIntra areaIntra = new AreaIntra();
		areaIntra.setDocumento(documento);
		areaIntra.setModalitaIncasso(ModalitaIncasso.B);
		areaIntra.setNaturaTransazione(naturaTransazione);
		areaIntra.setOperazioneTriangolare(false);
		areaIntra.setModalitaTrasporto(modalitaTrasporto);
		areaIntra.setGruppoCondizioneConsegna(gruppoCondizioneConsegna);

		// avvalora a seconda di acquisto/vendita - fornitore/cliente i dati relativi all'origine o destinazione delle
		// merci, al paese di pagamento, provincia e paese dell'azienda
		areaIntra = avvaloraDatiGeograficiAreaIntra(areaIntra, documento, null);

		// il periodo influisce su alcuni dati dell'area intra
		// nel caso in cui sia trimestrale, paese,provincia, modalità trasporto e cond consegna non devono essere
		// avvalorati; vedi setTipoPeriodo su areaIntra
		// mi sembra un po' nascosto, ma almeno è una regola di dominio di areaIntra e nessun'altro deve preoccuparsi di
		// valorizzare o meno quei dati
		IntraSettings settings = intraSettingsManager.caricaIntraSettings();
		areaIntra.setTipoPeriodo(settings.getTipoPeriodo());

		return areaIntra;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaContabile> ricercaAreeContabiliConIntra(ParametriRicercaAreaIntra parametri) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ac from AreaIntra ai,AreaContabile ac ");
		sb.append("join fetch ac.documento doc ");
		sb.append("join fetch doc.tipoDocumento join fetch doc.entita ");
		sb.append("where ai.documento=ac.documento ");
		if (parametri.getPeriodoRegistrazione().getDataIniziale() != null) {
			sb.append(" and ac.dataRegistrazione>=:dataInizio");
		}
		if (parametri.getPeriodoRegistrazione().getDataFinale() != null) {
			sb.append(" and ac.dataRegistrazione<=:dataFine");
		}
		Query query = panjeaDAO.prepareQuery(sb.toString());
		if (parametri.getPeriodoRegistrazione().getDataIniziale() != null) {
			query.setParameter("dataInizio", parametri.getPeriodoRegistrazione().getDataIniziale());
		}
		if (parametri.getPeriodoRegistrazione().getDataFinale() != null) {
			query.setParameter("dataFine", parametri.getPeriodoRegistrazione().getDataFinale());
		}
		List<AreaContabile> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore in ricercaAreeIntra", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public AreaIntra salvaAreaIntra(AreaIntra areaIntra) {
		logger.debug("--> Exit salvaAreaIntra");
		try {
			areaIntra = panjeaDAO.save(areaIntra);
			// fetch del documento
			areaIntra = caricaAreaIntraByDocumento(areaIntra.getDocumento());
		} catch (Exception e) {
			logger.error("--> errore nel salvare l'area intra", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaAreaIntra");
		return areaIntra;
	}

}
