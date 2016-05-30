package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAnticipoFatture;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipoFattureContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAnticipoFattureManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaAnticipoFattureManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAnticipoFattureManager")
public class AreaAnticipoFattureManagerBean implements AreaAnticipoFattureManager {

	private static Logger logger = Logger.getLogger(AreaAnticipoFattureManagerBean.class.getName());

	@Resource
	protected SessionContext context;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected DocumentiManager documentiManager;

	@EJB
	protected AreaPagamentiManager areaPagamentiManager;

	@EJB
	protected AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	protected AreaAnticipoFattureContabilitaManager areaAnticipoFattureContabilitaManager;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		areaPagamentiManager.cancellaAreaTesoreria(areaTesoreria, deleteAreeCollegate);
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		return areaPagamentiManager.caricaAreaTesoreria(idAreaTesoreria);
	}

	@Override
	public AreaAnticipoFatture creaAreaAnticipoFatture(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException {
		logger.debug("--> Enter creaAreaChiusure");

		AreaAnticipoFatture areaAnticipoFatture = new AreaAnticipoFatture();
		Map<Object, List<Pagamento>> pagamentiRaggruppati = null;

		switch (parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoOperazione()) {
		case ANTICIPO_FATTURE:
			pagamentiRaggruppati = organizzaPagamentiPerEntita(pagamenti);
			areaAnticipoFatture = (AreaAnticipoFatture) creaAreaPagamenti(areaAnticipoFatture, pagamentiRaggruppati,
					parametriCreazioneAreaChiusure, true);
			break;
		default:
			throw new UnsupportedOperationException("Tipo operazione diversio da Anticipo fatture non previsto");
		}

		logger.debug("--> Exit creaAreaChiusure");
		return areaAnticipoFatture;
	}

	/**
	 * metodo di utilita' che registra i documenti per pagamenti passivi con distinta.<br>
	 * (Pagamenti caso bonifico, rid, ecc.).<br>
	 * Un documento solo
	 * 
	 * @param areaPagamenti
	 *            areaPagamenti
	 * @param pagamentiPerEntita
	 *            pagamenti
	 * @param parametriCreazioneAreaChiusure
	 *            parametri di creazione
	 * @param importoPagamentoZero
	 *            importoPagamentoZero
	 * @return area pagamenti creata
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	private AreaPagamenti creaAreaPagamenti(AreaPagamenti areaPagamenti,
			Map<Object, List<Pagamento>> pagamentiPerEntita,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure, boolean importoPagamentoZero)
			throws DocumentoDuplicateException {
		logger.debug("--> Enter creaAreaPartitaConDistinta");
		if (parametriCreazioneAreaChiusure.getRapportoBancarioAzienda() == null) {
			throw new IllegalArgumentException("Rapporto bancario NULLO!");
		}

		// CALCOLO IL TOTALE DEL DOCUMENTO
		Importo totaleDocumento = new Importo();
		// Estraggo il primo pagamento per la prima entità per utilizzare i dati di codiceValuta e tassoDi cambio che
		// sono ugulali per tutti i pagamenti
		Pagamento pagamentoRiferimento = pagamentiPerEntita.values().iterator().next().get(0);
		totaleDocumento.setCodiceValuta(pagamentoRiferimento.getImporto().getCodiceValuta());
		totaleDocumento.setTassoDiCambio(pagamentoRiferimento.getImporto().getTassoDiCambio());

		for (List<Pagamento> arrays : pagamentiPerEntita.values()) {
			for (Pagamento pagamento : arrays) {
				// Sommo l'importo in valuta
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImporto().getImportoInValuta()));
				// All'importo sommo anche l'importo forzato
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImportoForzato().getImportoInValuta()));
				// Sommo l'importo in valuta azienda
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImporto().getImportoInValutaAzienda()));
				// All'importo sommo anche l'importo in valuta forzato
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImportoForzato().getImportoInValutaAzienda()));

				if (importoPagamentoZero) {
					pagamento.getImporto().setImportoInValuta(BigDecimal.ZERO);
					pagamento.getImporto().setImportoInValutaAzienda(BigDecimal.ZERO);
				}
			}
		}

		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(parametriCreazioneAreaChiusure.getDataDocumento());
		doc.setTipoDocumento(parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(parametriCreazioneAreaChiusure.getRapportoBancarioAzienda());
		doc.setTotale(totaleDocumento);
		doc.setContrattoSpesometro(null);
		Documento docSalvato;
		docSalvato = documentiManager.salvaDocumento(doc);
		if (areaPagamenti instanceof AreaAnticipoFatture) {
			((AreaAnticipoFatture) areaPagamenti).setDataScadenzaAnticipoFatture(parametriCreazioneAreaChiusure
					.getDataScadenzaAnticipoFatture());
		}
		areaPagamenti.setDocumento(docSalvato);
		areaPagamenti.setTipoAreaPartita(parametriCreazioneAreaChiusure.getTipoAreaPartita());
		areaPagamenti.setCodicePagamento(null);
		areaPagamenti.setSpeseIncasso(parametriCreazioneAreaChiusure.getSpeseIncasso());
		AreaPagamenti areaPagamentiSalvata = (AreaPagamenti) areaTesoreriaManager.salvaAreaTesoreria(areaPagamenti);

		for (Object entita : pagamentiPerEntita.keySet()) {
			// per ogni entita....
			List<Pagamento> pags = pagamentiPerEntita.get(entita);
			// aggiorno rate e salvo pagamenti
			for (Pagamento pagamento : pags) {
				pagamento.setDataPagamento(parametriCreazioneAreaChiusure.getDataDocumento());
				pagamento.setAreaChiusure(areaPagamentiSalvata);
				try {
					pagamento = panjeaDAO.save(pagamento);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			panjeaDAO.getEntityManager().refresh(areaPagamentiSalvata);
		}

		// creo l'area contabile
		try {
			areaPagamentiSalvata = areaAnticipoFattureContabilitaManager.creaAreaContabileAnticipoFatture(
					areaPagamentiSalvata, parametriCreazioneAreaChiusure);
		} catch (Exception e) {
			logger.error("--> Errore durante la creazione dell'area contabile dell'area pagamenti.", e);
			throw new RuntimeException("Errore durante la creazione dell'area contabile dell'area pagamenti.", e);
		}

		logger.debug("--> Exit creaAreaPartitaConDistinta");
		return areaPagamentiSalvata;
	}

	@Override
	public AreaPagamenti creaChiusuraAreaAnticipoFatture(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException {
		logger.debug("--> Enter creaAreaChiusure");

		AreaPagamenti areaPagamenti = new AreaPagamenti();
		Map<Object, List<Pagamento>> pagamentiRaggruppati = null;

		switch (parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoOperazione()) {
		case CHIUSURA_ANTICIPO_FATTURE:
			pagamentiRaggruppati = organizzaPagamentiPerEntita(pagamenti);
			areaPagamenti = creaAreaPagamenti(areaPagamenti, pagamentiRaggruppati, parametriCreazioneAreaChiusure, true);
			break;
		default:
			throw new UnsupportedOperationException("Tipo operazione diversio da Anticipo fatture non previsto");
		}

		logger.debug("--> Exit creaAreaChiusure");
		return areaPagamenti;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		return new ArrayList<>();
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return utente loggato
	 */
	private JecPrincipal getJecPrincipal() {
		logger.debug("--> Enter getJecPrincipal");
		return (JecPrincipal) context.getCallerPrincipal();
	}

	/**
	 * Metodo di utilita' che serve a raggruppare i pagamenti per ogni entita.
	 * 
	 * @param pagamenti
	 *            pagamenti da raggruppare
	 * @return pagamenti per entita'
	 */
	protected Map<Object, List<Pagamento>> organizzaPagamentiPerEntita(List<Pagamento> pagamenti) {
		logger.debug("--> Enter raggruppaPagamenti");
		Map<Object, List<Pagamento>> raggruppamenti = new HashMap<Object, List<Pagamento>>();
		for (Pagamento pagamento : pagamenti) {

			TipoEntita tipoEntitaPagamento = pagamento.getRata().getAreaRate().getDocumento().getTipoDocumento()
					.getTipoEntita();

			switch (tipoEntitaPagamento) {
			case CLIENTE:
			case FORNITORE:
				// Cliente o Fornitore
				EntitaLite entita = pagamento.getRata().getAreaRate().getDocumento().getEntita();

				List<Pagamento> pags = new ArrayList<Pagamento>();
				if (raggruppamenti.containsKey(entita)) {
					pags = raggruppamenti.get(entita);
				}
				pags.add(pagamento);
				raggruppamenti.put(entita, pags);
				break;
			case AZIENDA:
				// HACK creo un nuovo oggetto senza caricarlo dato che non sono necessari dati particolari per la
				// creazione del documento di pagamento
				AziendaLite aziendaLite = new AziendaLite();
				aziendaLite.setId(1);
				aziendaLite.setCodice(getJecPrincipal().getCodiceAzienda());

				List<Pagamento> pagsAzienda = new ArrayList<Pagamento>();
				if (raggruppamenti.containsKey(aziendaLite)) {
					pagsAzienda = raggruppamenti.get(aziendaLite);
				}
				pagsAzienda.add(pagamento);
				raggruppamenti.put(aziendaLite, pagsAzienda);
				break;
			default:
				break;
			}
		}
		logger.debug("--> Exit raggruppaPagamenti");
		return raggruppamenti;
	}

}
