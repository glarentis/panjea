package it.eurotn.panjea.documenti.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.documenti.manager.interfaces.SpedizioneDocumentiManager;
import it.eurotn.panjea.documenti.util.EtichettaSpedizioneDocumentoDTO;
import it.eurotn.panjea.documenti.util.MovimentoEtichettaSpedizioneDTO;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.SpedizioneDocumentiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SpedizioneDocumentiManager")
public class SpedizioneDocumentiManagerBean implements SpedizioneDocumentiManager {

	private static Logger logger = Logger.getLogger(SpedizioneDocumentiManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@EJB
	private SicurezzaService sicurezzaService;

	@Override
	public StatoSpedizione cambiaStatoSpedizioneMovimento(IAreaDocumento areaDocumento) {
		// Il documento può passare da DA_VERIFICARE a VERIFICATO e viceversa o da NON_SPEDITO a SPEDITO.
		if (areaDocumento.getStatoSpedizione() == null) {
			return null;
		}

		StatoSpedizione statoOld = areaDocumento.getStatoSpedizione();

		StatoSpedizione statoSpedizione = null;
		if (statoOld == StatoSpedizione.DA_VERIFICARE || statoOld == StatoSpedizione.VERIFICATO) {
			statoSpedizione = statoOld == StatoSpedizione.DA_VERIFICARE ? StatoSpedizione.VERIFICATO
					: StatoSpedizione.DA_VERIFICARE;
		} else {
			statoSpedizione = statoOld == StatoSpedizione.NON_SPEDITO ? StatoSpedizione.SPEDITO
					: StatoSpedizione.NON_SPEDITO;
		}

		// se lo stato è cambiato lo aggiorno
		if (!Objects.equals(statoOld, statoSpedizione)) {
			Query query = panjeaDAO.prepareQuery("update " + areaDocumento.getClass().getName()
					+ " ad set ad.statoSpedizione = :paramStato where ad.id = :paramIdArea ");
			query.setParameter("paramStato", statoSpedizione);
			query.setParameter("paramIdArea", areaDocumento.getId());
			query.executeUpdate();
		}

		return statoSpedizione;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EtichettaSpedizioneDocumentoDTO> caricaEtichetteSpedizioneDocumenti(final List<Integer> idDocumenti,
			int numeroEtichettaIniziale) {

		StringBuffer hqlQuery = new StringBuffer(1000);
		hqlQuery.append("select doc as documento, ");
		hqlQuery.append("sedeSpedizione.id as idSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.descrizione as descrizioneSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzo as indirizzoSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzoMail as indirizzoMailSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzoPEC as indirizzoPecSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzoMailSpedizione as indirizzoMailSpedizioneSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.tipoSpedizioneDocumenti as tipoSpedizioneDocumentiSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.spedizioneDocumentiViaPEC as spedizioneDocumentiViaPECSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.datiGeografici as datiGeograficiSedeSpedizione, ");
		hqlQuery.append("tipoSedeSpedizione as tipoSedeSedeSpedizione ");
		hqlQuery.append("from Documento doc inner join doc.tipoDocumento tipoDocumento ");
		hqlQuery.append("inner join fetch doc.entita ent inner join fetch ent.anagrafica anag ");
		hqlQuery.append("inner join fetch doc.sedeEntita sede inner join fetch sede.sede sedeAnag left join sede.sedeSpedizione as sedeSpedizione left join sedeSpedizione.sede as sedeAnagSpedizione left join sedeSpedizione.tipoSede as tipoSedeSpedizione  ");
		hqlQuery.append("where doc.id in (:idDocumenti) ");

		Query query = panjeaDAO.prepareQuery(hqlQuery.toString(), MovimentoEtichettaSpedizioneDTO.class, null);
		query.setParameter("idDocumenti", idDocumenti);

		List<MovimentoEtichettaSpedizioneDTO> listMovimenti = new ArrayList<MovimentoEtichettaSpedizioneDTO>();

		try {
			listMovimenti = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dei movimenti per la spedizione delle etichette.", e);
			throw new RuntimeException(
					"Errore durante il caricamento dei movimenti per la spedizione delle etichette.", e);
		}

		// creo le etichette
		List<EtichettaSpedizioneDocumentoDTO> etichette = new ArrayList<EtichettaSpedizioneDocumentoDTO>();

		// creo le etichette vuote iniziali
		for (int i = 1; i < numeroEtichettaIniziale; i++) {
			EtichettaSpedizioneDocumentoDTO etichetta = new EtichettaSpedizioneDocumentoDTO();
			etichette.add(etichetta);
		}

		// devo restituire le etichette ordinate secondo la lista di documenti passata come parametro
		Collections.sort(listMovimenti, new Comparator<MovimentoEtichettaSpedizioneDTO>() {
			public int compare(MovimentoEtichettaSpedizioneDTO left, MovimentoEtichettaSpedizioneDTO right) {
				return Integer.compare(idDocumenti.indexOf(left.getDocumento().getId()),
						idDocumenti.indexOf(right.getDocumento().getId()));
			}
		});

		// aggiungo tutte le etichette dei documenti
		for (MovimentoEtichettaSpedizioneDTO movimento : listMovimenti) {
			EtichettaSpedizioneDocumentoDTO etichetta = new EtichettaSpedizioneDocumentoDTO(movimento);
			etichette.add(etichetta);
		}

		return etichette;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentoSpedizioneDTO> caricaMovimentiPerSpedizione(
			Class<? extends IAreaDocumento> areaDocumentoClass, List<Integer> idDocumenti) {

		StringBuffer hqlQuery = new StringBuffer(1000);
		hqlQuery.append("select ad as areaDocumento, ");
		hqlQuery.append("sedeSpedizione.id as idSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.descrizione as descrizioneSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzo as indirizzoSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzoMail as indirizzoMailSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzoPEC as indirizzoPecSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.indirizzoMailSpedizione as indirizzoMailSpedizioneSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.tipoSpedizioneDocumenti as tipoSpedizioneDocumentiSedeSpedizione, ");
		hqlQuery.append("sedeAnagSpedizione.spedizioneDocumentiViaPEC as spedizioneDocumentiViaPECSedeSpedizione, ");
		hqlQuery.append("(select sedePrinc from SedeEntita sedePrinc inner join sedePrinc.tipoSede tipoSPrinc where sedePrinc.entita.id = ent.id and tipoSPrinc.sedePrincipale = true)  as sedePrincipale ");
		hqlQuery.append("from " + areaDocumentoClass.getName()
				+ " ad inner join fetch ad.documento doc inner join doc.tipoDocumento tipoDocumento ");
		hqlQuery.append("left join fetch doc.entita ent left join fetch ent.anagrafica anag ");
		hqlQuery.append("left join fetch doc.sedeEntita sede left join fetch sede.sede sedeAnag left join sede.sedeSpedizione as sedeSpedizione left join sedeSpedizione.sede as sedeAnagSpedizione ");
		hqlQuery.append("where doc.id in (:idDocumenti) ");

		Query query = panjeaDAO.prepareQuery(hqlQuery.toString(), MovimentoSpedizioneDTO.class, null);
		query.setParameter("idDocumenti", idDocumenti);

		List<MovimentoSpedizioneDTO> listMovimenti = new ArrayList<MovimentoSpedizioneDTO>();

		try {
			listMovimenti = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dei movimenti per la spedizione.", e);
			throw new RuntimeException("Errore durante il caricamento dei movimenti per la spedizione.", e);
		}

		// aggiorno l'utente per avere i dati degli account mail
		try {
			Utente utente = sicurezzaService.caricaUtente(((JecPrincipal) context.getCallerPrincipal()).getUserName());
			for (MovimentoSpedizioneDTO movimento : listMovimenti) {
				movimento.setUtente(utente);
			}
		} catch (Exception e) {
			logger.error("--> errore  durante il caricamento dell'utente", e);
			throw new RuntimeException("errore  durante il caricamento dell'utente", e);
		}

		return listMovimenti;
	}

}
