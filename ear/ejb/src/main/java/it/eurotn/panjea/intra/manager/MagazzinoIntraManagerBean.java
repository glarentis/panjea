/**
 *
 */
package it.eurotn.panjea.intra.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.DatiArticoloIntra;
import it.eurotn.panjea.intra.domain.GruppoCondizioneConsegna;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.ModalitaIncasso;
import it.eurotn.panjea.intra.domain.ModalitaTrasporto;
import it.eurotn.panjea.intra.domain.NaturaTransazione;
import it.eurotn.panjea.intra.domain.RigaBeneIntra;
import it.eurotn.panjea.intra.domain.RigaIntra;
import it.eurotn.panjea.intra.manager.interfaces.AreaIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.IntraSettingsManager;
import it.eurotn.panjea.intra.manager.interfaces.MagazzinoIntraManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoAnagraficaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author leonardo
 */
@Stateless(mappedName = "Panjea.MagazzinoIntraManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoIntraManager")
public class MagazzinoIntraManagerBean implements MagazzinoIntraManager {

	private static Logger logger = Logger.getLogger(MagazzinoIntraManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	protected AreaIntraManager areaIntraManager;

	@EJB
	protected IntraSettingsManager intraSettingsManager;

	@EJB
	protected MagazzinoAnagraficaManager magazzinoAnagraficaManager;

	@EJB
	private RigaMagazzinoManager rigaMagazzinoManager;

	@EJB
	protected AziendeManager aziendeManager;

	/**
	 * @return il codice della nazione azienda, per verificare se creare l'area intra del documento
	 */
	private String caricaCodiceNazioneAzienda() {
		AziendaLite aziendaLite = aziendeManager.caricaAzienda(true);
		String codiceNazioneAzienda = aziendaLite.getNazione().getCodice();
		return codiceNazioneAzienda;
	}

	@EJB
	@Override
	public List<GruppoCondizioneConsegna> caricaGruppiCondizioneConsegna() {

		GruppoCondizioneConsegna[] gruppiCondizione = GruppoCondizioneConsegna.values();
		List<GruppoCondizioneConsegna> gruppi = new ArrayList<GruppoCondizioneConsegna>();
		for (GruppoCondizioneConsegna gruppoCondizioneConsegna : gruppiCondizione) {
			gruppi.add(gruppoCondizioneConsegna);
		}

		return gruppi;
	}

	@Override
	public GruppoCondizioneConsegna caricaGruppoCondizioneConsegna(String tipoPorto) {
		StringBuilder hql = new StringBuilder();
		hql.append("select tp.gruppoCondizioneConsegna ");
		hql.append(" from TipoPorto tp ");
		hql.append(" where tp.descrizione=:paramDescrizione and tp.codiceAzienda=:paramCodiceAzienda ");

		Query query = panjeaDAO.prepareQuery(hql.toString());
		query.setParameter("paramDescrizione", tipoPorto);
		query.setParameter("paramCodiceAzienda", getAzienda());

		GruppoCondizioneConsegna result = null;
		try {
			result = (GruppoCondizioneConsegna) query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("Nessun gruppoCondizioneConsegna trovato per il tipo porto " + tipoPorto);
		}
		return result;
	}

	@Override
	public NaturaTransazione caricaNaturaTransazione(String causaleTrasporto) {
		StringBuilder hql = new StringBuilder();
		hql.append("select ct.naturaTransazione ");
		hql.append(" from CausaleTrasporto ct ");
		hql.append(" where ct.descrizione=:paramDescrizione and ct.codiceAzienda=:paramCodiceAzienda ");

		Query query = panjeaDAO.prepareQuery(hql.toString());
		query.setParameter("paramDescrizione", causaleTrasporto);
		query.setParameter("paramCodiceAzienda", getAzienda());

		NaturaTransazione result = null;
		try {
			result = (NaturaTransazione) query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("Nessuna naturaTransazione trovata per la causale trasporto " + causaleTrasporto);
		}
		return result;
	}

	@Override
	public List<NaturaTransazione> caricaNatureTransazione() {
		NaturaTransazione[] natureTransazione = NaturaTransazione.values();
		List<NaturaTransazione> nature = new ArrayList<NaturaTransazione>();
		for (NaturaTransazione naturaTransazione : natureTransazione) {
			nature.add(naturaTransazione);
		}
		return nature;
	}

	/**
	 * Carica le righe articolo dell'area magazzino.
	 *
	 * @param areaMagazzino
	 *            areaMagazzino
	 * @return List<RigaArticolo>
	 */
	private List<RigaArticolo> caricaRigheMagazzino(AreaMagazzino areaMagazzino) {
		return rigaMagazzinoManager.getDao().caricaRigheArticolo(areaMagazzino);
	}

	@Override
	public AreaIntra generaAreaIntra(AreaMagazzino areaMagazzino) {
		String codiceNazioneAzienda = caricaCodiceNazioneAzienda();
		boolean intraAbilitata = areaMagazzino.getDocumento().isAreaIntraAbilitata(codiceNazioneAzienda);

		if (!intraAbilitata) {
			return null;
		}

		AreaIntra areaIntraByDocumento = areaIntraManager.caricaAreaIntraByDocumento(areaMagazzino.getDocumento());
		if (areaIntraByDocumento.getId() != null) {
			areaIntraManager.cancellaAreaIntra(areaIntraByDocumento);
		}

		String causaleTrasporto = areaMagazzino.getCausaleTrasporto();
		String tipoPorto = areaMagazzino.getTipoPorto();
		ModalitaTrasporto modalitaTrasporto = areaMagazzino.getDocumento().getSedeEntita().getSedeMagazzino()
				.getModalitaTrasporto();

		GruppoCondizioneConsegna gruppoCondizioneConsegna = caricaGruppoCondizioneConsegna(tipoPorto);
		NaturaTransazione naturaTransazione = caricaNaturaTransazione(causaleTrasporto);

		// genera l'area intra con i dati dell'area magazzino
		AreaIntra areaIntra = new AreaIntra();
		areaIntra.setDocumento(areaMagazzino.getDocumento());
		areaIntra.setModalitaIncasso(ModalitaIncasso.B);
		areaIntra.setNaturaTransazione(naturaTransazione);
		areaIntra.setOperazioneTriangolare(false);
		areaIntra.setModalitaTrasporto(modalitaTrasporto);
		areaIntra.setGruppoCondizioneConsegna(gruppoCondizioneConsegna);

		Deposito depositoOrigine = null;
		try {
			depositoOrigine = panjeaDAO.load(Deposito.class, areaMagazzino.getDepositoOrigine().getId());
		} catch (ObjectNotFoundException e) {
			throw new RuntimeException(e);
		}

		// avvalora a seconda di acquisto/vendita - fornitore/cliente i dati relativi all'origine o destinazione delle
		// merci, al paese di pagamento, provincia e paese dell'azienda
		areaIntra = areaIntraManager.avvaloraDatiGeograficiAreaIntra(areaIntra, areaMagazzino.getDocumento(),
				depositoOrigine);

		// il periodo influisce su alcuni dati dell'area intra
		// nel caso in cui sia trimestrale, paese,provincia, modalità trasporto e cond consegna non devono essere
		// avvalorati; vedi setTipoPeriodo su areaIntra
		// mi sembra un po' nascosto, ma almeno è una regola di dominio di areaIntra e nessun'altro deve preoccuparsi di
		// valorizzare o meno quei dati
		IntraSettings settings = intraSettingsManager.caricaIntraSettings();
		areaIntra.setTipoPeriodo(settings.getTipoPeriodo());

		// prendo solo le righe articolo
		List<RigaArticolo> righeArticolo = caricaRigheMagazzino(areaMagazzino);

		// raccoglie le righe intra generate
		Set<RigaIntra> righeIntraGenerate = new HashSet<RigaIntra>();

		// preparo le righe articolo raggruppate per nomenclatura/codice Iva
		// se non è impostata la nomenclatura non considero la riga
		Map<String, RigaIntra> righeIntraNomenclatura = new HashMap<String, RigaIntra>();

		for (RigaArticolo rigaArticolo : righeArticolo) {

			// carico l'articolo per avere i dati intra
			Articolo articolo = null;
			try {
				articolo = panjeaDAO.load(Articolo.class, rigaArticolo.getArticolo().getId());
			} catch (ObjectNotFoundException e) {
				throw new RuntimeException(e);
			}
			DatiArticoloIntra datiIntra = articolo.getDatiIntra();

			// se e' associato un servizio/nomenclatura considero la riga articolo
			if (datiIntra.getServizio() != null) {
				String codiceServizio = datiIntra.getServizio().getCodice();
				String codiceNazione = datiIntra.getNazione() != null ? datiIntra.getNazione().getCodice() : "";
				String codiceIva = rigaArticolo.getCodiceIva().getCodice();

				// creo la chiave della mappa codiceServizio#codiceIva
				String key = new String(codiceServizio + "#" + codiceIva + "#" + codiceNazione);

				// recupero la riga intra dalla mappa (può non esserci)
				RigaIntra rigaIntraPresente = righeIntraNomenclatura.get(key);

				// creo la riga intra dalla riga articolo
				RigaIntra rigaIntraCorrente = RigaIntraBuilder.creaRigaIntra(areaIntra, datiIntra, rigaArticolo);

				if(rigaIntraCorrente != null ){
					// se già presente una riga con servizio/nomenclatura e codice iva,
					// aggiungo l'importo,la qta e la massa della riga corrente alla riga presente
					if (rigaIntraPresente != null) {
						// solo nel caso in cui sia riga bene
						if (rigaIntraPresente instanceof RigaBeneIntra) {
							RigaBeneIntra rbiPresente = (RigaBeneIntra) rigaIntraPresente;
							rbiPresente.setPaeseOrigineArticolo(codiceNazione);
							rbiPresente
							.setMassa(rbiPresente.getMassa().add(((RigaBeneIntra) rigaIntraCorrente).getMassa()));
						}
						// sia per riga bene che per servizio
						rigaIntraPresente.setImporto(rigaIntraCorrente.getImporto().add(rigaIntraPresente.getImporto(), 1));
					} else {
						rigaIntraPresente = rigaIntraCorrente;
					}
					rigaIntraPresente.setAreaIntra(areaIntra);

					// aggiungo sempre in mappa la riga intra, in caso esista già la chiave viene sovrascritto l'oggetto
					righeIntraNomenclatura.put(key, rigaIntraPresente);
				}
			}
		}

		righeIntraGenerate.addAll(righeIntraNomenclatura.values());

		areaIntra.setRigheIntra(righeIntraGenerate);
		areaIntra = areaIntraManager.salvaAreaIntra(areaIntra);
		return areaIntra;
	}

	@Override
	public void generaAreeIntra(List<Integer> documenti) {
		Query query = panjeaDAO.prepareQuery("select am from AreaMagazzino am where am.documento in (:paramDocumenti)");
		List<Documento> documentiParam = new ArrayList<Documento>();
		for (Integer idDocumento : documenti) {
			try {
				documentiParam.add(panjeaDAO.load(Documento.class, idDocumento));
			} catch (ObjectNotFoundException e) {
				logger.error("-->errore nel caricare il documento per generare l'area intra. documento non trovato. ID"
						+ idDocumento, e);
				throw new RuntimeException(
						"-->errore nel caricare il documento per generare l'area intra. documento non trovato. ID"
								+ idDocumento, e);
			}
		}
		query.setParameter("paramDocumenti", documentiParam);
		@SuppressWarnings("unchecked")
		List<AreaMagazzino> areeMagazzino = query.getResultList();
		for (AreaMagazzino areaMagazzino : areeMagazzino) {
			// cancello un eventuale area intra già presente
			try {
				Query queryDelete = panjeaDAO
						.prepareQuery("delete from AreaIntra ai where ai.documento=:paramDocumento");
				queryDelete.setParameter("paramDocumento", areaMagazzino.getDocumento());
				panjeaDAO.executeQuery(queryDelete);
			} catch (DAOException e) {
				logger.error("-->errore nel cancellare l'area intra per il documento "
						+ areaMagazzino.getDocumento().getId(), e);
				throw new RuntimeException(e);
			}
			generaAreaIntra(areaMagazzino);
		}
	}

	/**
	 * @return codice dell'azienda loggata
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

}
