/**
 * 
 */
package it.eurotn.panjea.preventivi.manager.evasione;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.manager.noteautomatiche.interfaces.NoteAutomaticheManager;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloEvasioneCalculator;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaTestata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.RigaPreventivoManager;
import it.eurotn.panjea.preventivi.manager.evasione.interfaces.PreventiviEvasioneGenerator;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.PreventiviEvasioneGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PreventiviEvasioneGenerator")
public class PreventiviEvasioneGeneratorBean implements PreventiviEvasioneGenerator {

	private static Logger logger = Logger.getLogger(PreventiviEvasioneGeneratorBean.class);

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	@EJB
	private AreaOrdineManager areaOrdineManager;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private AreaRateManager areaRateManager;

	@EJB
	private RigaPreventivoManager rigaPreventivoManager;

	@EJB
	private RigaOrdineManager rigaOrdineManager;

	@EJB
	private NoteAutomaticheManager noteAutomaticheManager;

	@EJB
	private AnagraficaTabelleManager anagraficaTabelleManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	private AreaOrdine creaAreaOrdine(AreaPreventivo areaPreventivo, TipoAreaOrdine tipoAreaOrdine,
			DepositoLite deposito, SedeAreaMagazzinoDTO sedeAreaMagazzino, Date dataOrdine, AgenteLite agente,
			Date dataConsegnaOrdine) {
		Importo importo = new Importo(areaPreventivo.getDocumento().getTotale().getCodiceValuta(), BigDecimal.ONE);
		importo.calcolaImportoValutaAzienda(2);

		Documento documento = new Documento();
		documento.setDataDocumento(dataOrdine);
		documento.setEntita(areaPreventivo.getDocumento().getEntita());
		documento.setSedeEntita(areaPreventivo.getDocumento().getSedeEntita());
		documento.setTipoDocumento(tipoAreaOrdine.getTipoDocumento());
		documento.setTotale(importo);

		// Creo l'area ordine
		AreaOrdine areaOrdine = new AreaOrdine();
		areaOrdine.setDocumento(documento);
		areaOrdine.setTipoAreaOrdine(tipoAreaOrdine);
		areaOrdine.setAgente(agente);
		areaOrdine.setDataRegistrazione(dataOrdine);
		areaOrdine.setDataConsegna(dataConsegnaOrdine);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataOrdine);
		areaOrdine.setAnnoMovimento(calendar.get(Calendar.YEAR));
		areaOrdine.setDepositoOrigine(deposito);

		if (sedeAreaMagazzino != null) {
			areaOrdine.setListino(sedeAreaMagazzino.getListino());
			areaOrdine.setListinoAlternativo(sedeAreaMagazzino.getListinoAlternativo());
			areaOrdine.setVettore(sedeAreaMagazzino.getVettore());
			areaOrdine.setCausaleTrasporto(sedeAreaMagazzino.getCausaleTrasporto());
			areaOrdine.setTrasportoCura(sedeAreaMagazzino.getTrasportoCura());
			areaOrdine.setTipoPorto(sedeAreaMagazzino.getTipoPorto());
			areaOrdine.setAddebitoSpeseIncasso(sedeAreaMagazzino.isCalcoloSpese());
			areaOrdine.setRaggruppamentoBolle(sedeAreaMagazzino.isRaggruppamentoBolle());
			areaOrdine.setTipologiaCodiceIvaAlternativo(sedeAreaMagazzino.getTipologiaCodiceIvaAlternativo());
			areaOrdine.setCodiceIvaAlternativo(sedeAreaMagazzino.getCodiceIvaAlternativo());
			areaOrdine.setSedeVettore(sedeAreaMagazzino.getSedeVettore());
		}
		return areaOrdine;
	}

	private AreaRate creaAreaRate(AreaPreventivo areaPreventivo, AreaOrdine areaOrdine,
			SedeAreaMagazzinoDTO sedeAreaMagazzino) {
		// controllo se l'ordine prevede un'area rate
		TipoAreaPartita tap = tipiAreaPartitaManager.caricaTipoAreaPartitaPerTipoDocumento(areaOrdine.getDocumento()
				.getTipoDocumento());
		AreaRate areaRateOrdine = null;
		if (tap.getId() != null) {
			CodicePagamento codicePagamento = null;
			// carico l'area rate del preventivo per avere il codice pagamento
			AreaRate areaRatePreventivo = areaRateManager.caricaAreaRate(areaPreventivo.getDocumento());
			codicePagamento = areaRatePreventivo.getCodicePagamento();

			// Se non ho il codice pagamento settato imposto quello predefinito sulla sedeMagazzino
			if (codicePagamento == null && sedeAreaMagazzino != null) {
				codicePagamento = sedeAreaMagazzino.getCodicePagamento();
			}

			areaRateOrdine = new AreaRate();
			areaRateOrdine.setTipoAreaPartita(tap);
			areaRateOrdine.setDocumento(areaOrdine.getDocumento());
			areaRateOrdine.setCodicePagamento(codicePagamento);
			if (codicePagamento != null) {
				areaRateOrdine.setSpeseIncasso(codicePagamento.getImportoSpese());
			}
		}
		return areaRateOrdine;
	}

	private RigaArticolo creaRigaArticolo(RigaEvasione rigaEvasione, AreaOrdine areaOrdine, RigaTestata rigaTestata,
			int ordinamento, FormuleRigaArticoloCalculator formuleRigaArticoloCalculator, AreaPreventivo areaPreventivo) {

		it.eurotn.panjea.preventivi.domain.RigaArticolo rigaArticoloPreventivo = new it.eurotn.panjea.preventivi.domain.RigaArticolo();
		rigaArticoloPreventivo.setId(rigaEvasione.getRigaMovimentazione().getIdRiga());
		rigaArticoloPreventivo = (it.eurotn.panjea.preventivi.domain.RigaArticolo) rigaPreventivoManager.getDao()
				.caricaRigaPreventivo(rigaArticoloPreventivo);

		RigaArticolo rigaArticoloOrdine = rigaArticoloPreventivo.creaRigaArticoloOrdine();
		rigaArticoloOrdine.setRigaTestataCollegata(rigaTestata);
		rigaArticoloOrdine.setAreaPreventivoCollegata(areaPreventivo);
		rigaArticoloOrdine.setOrdinamento(ordinamento);
		rigaArticoloOrdine.setAreaOrdine(areaOrdine);
		rigaArticoloOrdine.setLivello(1);

		// uso data consegna e quantità presenti sulla riga evasione perchè l'utente potrebbe averli cambiati
		rigaArticoloOrdine.setDataConsegna(rigaEvasione.getDataConsegna());
		rigaArticoloOrdine.setQta(rigaEvasione.getQuantitaEvasione());

		// Calcolo le formule su qta e attributi
		try {
			rigaArticoloOrdine = (RigaArticolo) formuleRigaArticoloCalculator.calcola(rigaArticoloOrdine);
		} catch (FormuleTipoAttributoException e) {
			logger.error("-->errore nel calcolare le formule durante l'evasione ", e);
			throw new RuntimeException(e);
		}
		return rigaArticoloOrdine;
	}

	/**
	 * Se sono state configurate delle note automatiche per il documento di destinazione, viene creata una riga nota
	 * automatice.
	 * 
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 */
	private void creaRigaNotaAutomatica(AreaOrdine areaOrdine) {

		List<NotaAutomatica> noteAutomatiche = noteAutomaticheManager.caricaNoteAutomatiche(
				areaOrdine.getDataRegistrazione(), areaOrdine.getDocumento());

		if (!noteAutomatiche.isEmpty()) {
			List<NotaAnagrafica> noteAnagrafiche = anagraficaTabelleManager.caricaNoteAnagrafica();

			StringBuilder sb = new StringBuilder();
			for (NotaAutomatica nota : noteAutomatiche) {

				if (!sb.toString().isEmpty()) {
					sb.append("<BR>");
				}
				sb.append(nota.getNotaElaborata(noteAnagrafiche));
			}

			rigaOrdineManager.creaRigaNoteAutomatica(areaOrdine, sb.toString());
		}
	}

	/**
	 * Crea una riga testata del preventivo sull'rdine.
	 * 
	 * @param areaPreventivo
	 *            area preventivo
	 * @param areaOrdine
	 *            area ordine
	 * @return {@link RigaTestata} creata
	 */
	private RigaTestata creaRigaTestataOrdine(AreaPreventivo areaPreventivo, AreaOrdine areaOrdine) {

		RigaTestata rigaTestata = new RigaTestata();
		rigaTestata.setAreaPreventivoCollegata(areaPreventivo);
		rigaTestata.setCodiceTipoDocumentoCollegato(areaPreventivo.getDocumento().getTipoDocumento().getCodice());
		rigaTestata.setDataAreaMagazzinoCollegata(areaPreventivo.getDataRegistrazione());
		rigaTestata.setLivello(0);
		rigaTestata.setOrdinamento(0);
		rigaTestata.setDescrizione(rigaTestata.generaDescrizioneTestata());
		rigaTestata.setAreaOrdine(areaOrdine);
		return rigaTestata;
	}

	@Override
	public void evadiPreventivi(List<RigaEvasione> righeEvasione, TipoAreaOrdine tipoAreaOrdine, DepositoLite deposito,
			Date dataOrdine, AgenteLite agente, Date dataConsegnaOrdine) {

		AreaPreventivo areaPreventivo = null;
		try {
			areaPreventivo = panjeaDAO.load(AreaPreventivo.class, righeEvasione.get(0).getRigaMovimentazione()
					.getAreaPreventivoId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore area preventivo non esistente con id "
					+ righeEvasione.get(0).getRigaMovimentazione().getAreaPreventivoId(), e);
			throw new RuntimeException("errore area preventivo non esistente con id "
					+ righeEvasione.get(0).getRigaMovimentazione().getAreaPreventivoId(), e);
		}

		SedeAreaMagazzinoDTO sedeAreaMagazzino = null;
		if (areaPreventivo.getDocumento().getSedeEntita() != null) {
			sedeAreaMagazzino = areaMagazzinoManager.caricaSedeAreaMagazzinoDTO(areaPreventivo.getDocumento()
					.getSedeEntita());
		}
		FormuleRigaArticoloCalculator formuleRigaArticoloCalculator = new FormuleRigaArticoloEvasioneCalculator();

		// creo l'area ordine
		AreaOrdine areaOrdine = creaAreaOrdine(areaPreventivo, tipoAreaOrdine, deposito, sedeAreaMagazzino, dataOrdine,
				agente, dataConsegnaOrdine);
		areaOrdine = areaOrdineManager.salvaAreaOrdine(areaOrdine);

		// Crea l'area rate se prevista
		AreaRate areaRate = creaAreaRate(areaPreventivo, areaOrdine, sedeAreaMagazzino);
		if (areaRate != null) {
			areaRate = areaRateManager.salvaAreaRate(areaRate);
		}

		// creo la riga testata
		RigaTestata rigaTestata = creaRigaTestataOrdine(areaPreventivo, areaOrdine);
		try {
			rigaTestata = panjeaDAO.save(rigaTestata);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare la riga testata", e);
			throw new RuntimeException(e);
		}

		int ordinamento = 1;
		// creo le righe ordine per le righe evasione selezionate
		for (RigaEvasione rigaEvasione : righeEvasione) {
			if (rigaEvasione.isSelezionata()) {
				RigaArticolo rigaArticolo = creaRigaArticolo(rigaEvasione, areaOrdine, rigaTestata, ordinamento,
						formuleRigaArticoloCalculator, areaPreventivo);
				rigaOrdineManager.getDao(rigaArticolo).salvaRigaOrdine(rigaArticolo);
				ordinamento++;
			}
		}

		// creo le righe note automatiche
		creaRigaNotaAutomatica(areaOrdine);

		try {
			areaOrdine = areaOrdineManager.validaRigheOrdine(areaOrdine, areaRate, true);
		} catch (Exception e) {
			logger.error("-->errore durante la validazione delle righe ordine del documento", e);
			throw new RuntimeException("errore durante la validazione delle righe ordine del documento", e);
		}
	}
}
