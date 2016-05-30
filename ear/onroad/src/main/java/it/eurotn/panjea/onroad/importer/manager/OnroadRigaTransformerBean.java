package it.eurotn.panjea.onroad.importer.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.lotti.manager.interfaces.LottiAssegnazioneAutomaticaManager;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRiga;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.onroad.domain.RigaDocumentoOnroad;
import it.eurotn.panjea.onroad.importer.manager.interfaces.OnroadRigaTransformer;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.OnroadRigaTransformer")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnroadRigaTransformer")
public class OnroadRigaTransformerBean implements OnroadRigaTransformer {

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private RigaMagazzinoManager rigaMagazzinoManager;

	@EJB
	private MagazzinoDocumentoService magazzinoDocumentoService;

	@EJB
	private LottiAssegnazioneAutomaticaManager lottiAssegnazioneAutomaticaManager;

	private ArticoloLite caricaArticolo(String codiceArticolo) {
		ArticoloLite articolo = null;
		try {
			Query queryArticolo = panjeaDAO
					.prepareQuery("select a from ArticoloLite a where a.codice=:paramCodiceArticolo");
			queryArticolo.setParameter("paramCodiceArticolo", codiceArticolo);
			articolo = (ArticoloLite) panjeaDAO.getSingleResult(queryArticolo);
		} catch (ObjectNotFoundException e) {
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return articolo;
	}

	private ParametriCreazioneRigaArticolo getParametriCreazioneRigaArticolo(AreaMagazzino areaMagazzino,
			ArticoloLite articolo, Integer idAgente) {
		ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
		parametri.setProvenienzaPrezzo(areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo());
		parametri.setIdArticolo(articolo.getId());
		parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
		parametri.setIdSedeEntita(areaMagazzino.getDocumento().getSedeEntita() != null ? areaMagazzino.getDocumento()
				.getSedeEntita().getId() : null);
		parametri.setIdEntita(areaMagazzino.getDocumento().getEntita() != null ? areaMagazzino.getDocumento()
				.getEntita().getId() : null);
		parametri.setIdListinoAlternativo(areaMagazzino.getListinoAlternativo() != null ? areaMagazzino
				.getListinoAlternativo().getId() : null);
		parametri.setIdListino(areaMagazzino.getListino() != null ? areaMagazzino.getListino().getId() : null);
		parametri.setImporto(areaMagazzino.getDocumento().getTotale());
		parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
		parametri.setIdTipoMezzo(areaMagazzino.getMezzoTrasporto() != null ? areaMagazzino.getMezzoTrasporto()
				.getTipoMezzoTrasporto().getId() : null);
		parametri.setIdZonaGeografica(areaMagazzino.getIdZonaGeografica());
		parametri.setProvenienzaPrezzoArticolo(articolo.getProvenienzaPrezzoArticolo());
		parametri.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());
		parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
		parametri.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
		parametri.setCodiceLingua("it");
		parametri.setIdAgente(idAgente);
		parametri.setTipologiaCodiceIvaAlternativo(areaMagazzino.getTipologiaCodiceIvaAlternativo());
		// parametri.setPercentualeScontoCommerciale(percentualeScontoCommerciale);
		parametri.setGestioneConai(areaMagazzino.getTipoAreaMagazzino().isGestioneConai());
		parametri.setNotaCredito(areaMagazzino.getTipoAreaMagazzino().getTipoDocumento().isNotaCreditoEnable());
		parametri.setStrategiaTotalizzazioneDocumento(areaMagazzino.getTipoAreaMagazzino()
				.getStrategiaTotalizzazioneDocumento());
		return parametri;
	}

	private TipoOmaggio getTipoOmaggio(String tipologiaVendita) {
		TipoOmaggio tipoOmaggio = TipoOmaggio.NESSUNO;
		if (tipologiaVendita != null && tipologiaVendita.equals("OMA")) {
			tipoOmaggio = TipoOmaggio.OMAGGIO;
		}
		return tipoOmaggio;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RigaMagazzino trasforma(RigaDocumentoOnroad rigaDocumentoOnroad, AreaMagazzino areaMagazzino,
			AgenteLite agente) throws QtaLottiMaggioreException, RimanenzaLottiNonValidaException {

		// valorizzata nel caso di vendita o ddt
		Double quantitaVendita = rigaDocumentoOnroad.getQuantita();

		// valorizzata nel caso di ddt di carico aton, indica solo la giacenza prima del documento emesso; posso
		// ignorarla
		Double qtaInevasa = rigaDocumentoOnroad.getQtaInevasa();

		// se la qta vendita è 0.0 sono nel caso di un carico
		if (quantitaVendita == 0.0) {
			return null;
		}

		String codiceArticolo = rigaDocumentoOnroad.getCodiceArticolo();
		ArticoloLite articolo = caricaArticolo(codiceArticolo);

		// prendo queste info dall'articolo
		// String unitaMisura = rigaDocumentoOnroad.getUnitaMisura();
		// String descrizioneArticolo = rigaDocumentoOnroad.getDescrizioneArticolo();
		// String codiceIVA = rigaDocumentoOnroad.getCodiceIVA();

		String tipologiaVendita = rigaDocumentoOnroad.getTipologiaVendita();
		TipoOmaggio tipoOmaggio = getTipoOmaggio(tipologiaVendita);

		// prezzo unitario
		BigDecimal prezzoUVB = rigaDocumentoOnroad.getPrezzoUVB();
		// prezzo netto
		// BigDecimal prezzoUV = rigaDocumentoOnroad.getPrezzoUV();

		// sconto1
		BigDecimal sconto1 = rigaDocumentoOnroad.getSconto1();
		// sconto2
		BigDecimal sconto2 = rigaDocumentoOnroad.getSconto2();
		// 0
		// BigDecimal scontoValuta = rigaDocumentoOnroad.getScontoValuta();

		// ? di onroad, non mi serve a nulla
		// String codiceRicerca = rigaDocumentoOnroad.getCodiceRicerca();

		// valore riga compresi sconti
		// BigDecimal valoreRiga = rigaDocumentoOnroad.getValoreRiga();

		// 0:indifferente - 1_confezione - 2:componente confezione
		// String articoloConfezione = rigaDocumentoOnroad.getArticoloConfezione();
		// A: annullato - C: valido - T:trasmesso
		// String statoDocumento = rigaDocumentoOnroad.getStatoDocumento();

		// BigDecimal prezzoOriginale = rigaDocumentoOnroad.getPrezzoOriginale();
		// BigDecimal sconto1Originale = rigaDocumentoOnroad.getSconto1Originale();
		// BigDecimal sconto2Originale = rigaDocumentoOnroad.getSconto2Originale();

		// a che servono ?
		// String unitaMisura2 = rigaDocumentoOnroad.getUnitaMisura2();
		// Double quantita2 = rigaDocumentoOnroad.getQuantita2();

		ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo = getParametriCreazioneRigaArticolo(
				areaMagazzino, articolo, agente.getId());
		RigaArticolo rigaArticolo = magazzinoDocumentoService.creaRigaArticolo(parametriCreazioneRigaArticolo);
		rigaArticolo.setQta(quantitaVendita);
		rigaArticolo.setQtaMagazzino(quantitaVendita);
		rigaArticolo.setAreaMagazzino(areaMagazzino);
		rigaArticolo.setAgente(agente);

		Importo importoUVB = new Importo();
		importoUVB.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
		importoUVB.setImportoInValuta(prezzoUVB);
		importoUVB.setImportoInValutaAzienda(prezzoUVB);
		rigaArticolo.setPrezzoUnitario(importoUVB);

		rigaArticolo.setVariazione1(sconto1.negate());
		rigaArticolo.setVariazione2(sconto2.negate());

		// il tipo omaggio cambia le variazioni quindi lo imposto dopo prezzo e sconti
		rigaArticolo.setTipoOmaggio(tipoOmaggio);
		// if (tipoOmaggio.compareTo(TipoOmaggio.NESSUNO) != 0) {
		// rigaArticolo.setPercProvvigione(BigDecimal.ZERO);
		// rigaArticolo.setPrezzoUnitarioBaseProvvigionale(BigDecimal.ZERO);
		// }

		FormuleRigaArticoloCalculator formuleRigaArticoloCalculator = new FormuleRigaArticoloCalculator();
		IRigaArticoloDocumento rigaCalcolata;
		try {
			rigaCalcolata = formuleRigaArticoloCalculator.calcola(rigaArticolo);
			rigaArticolo.setQta(rigaCalcolata.getQta());
			rigaArticolo.setQtaMagazzino(rigaCalcolata.getQtaMagazzino());
			List<AttributoRiga> attributi = (List<AttributoRiga>) rigaCalcolata.getAttributi();
			for (AttributoRiga attributoRiga : attributi) {
				Boolean obbligatorio = attributoRiga.getObbligatorio();
				String valore = attributoRiga.getValore();
				boolean updatable = attributoRiga.isUpdatable();

				if (obbligatorio.booleanValue() && updatable && valore.isEmpty()) {
					attributoRiga.setValore("1");
				}
			}
			rigaArticolo.setAttributi(attributi);
			rigaArticolo.setComponenti(rigaCalcolata.getComponenti());
		} catch (FormuleTipoAttributoException e) {
			throw new RuntimeException(e);
		}
		if (articolo.getTipoLotto().compareTo(TipoLotto.NESSUNO) != 0) {
			try {
				// questo chiama la salva riga articolo, se non ho sull'articolo lottoFacoltativo
				rigaArticolo = (RigaArticolo) lottiAssegnazioneAutomaticaManager.assegnaLotti(rigaArticolo);
			} catch (RimanenzaLottiNonValidaException e1) {
				throw e1;
			} catch (QtaLottiMaggioreException e1) {
				throw e1;
			}
		}
		rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(rigaArticolo).salvaRigaMagazzinoNoCheck(rigaArticolo);

		// se reso invendibile devo mettere la qta, la qta del lotto e degli attibuti negativa; non lo faccio prima
		// perchè il metodo assegna lotti non prevede qta negative e mi rilancia QtaLottiMaggioreException
		if (tipologiaVendita != null && tipologiaVendita.equals("REI")) {
			rigaArticolo.setQta(-rigaArticolo.getQta());
			rigaArticolo.setQtaMagazzino(-rigaArticolo.getQtaMagazzino());
			Set<RigaLotto> righeLotto = rigaArticolo.getRigheLotto();
			for (RigaLotto rigaLotto : righeLotto) {
				rigaLotto.setQuantita(-rigaLotto.getQuantita());
			}
			List<AttributoRiga> attributi = rigaArticolo.getAttributi();
			for (AttributoRiga attributoRiga : attributi) {
				Boolean obbligatorio = attributoRiga.getObbligatorio();
				boolean hasFormula = attributoRiga.getFormula() != null;
				if (obbligatorio.booleanValue() && hasFormula) {
					attributoRiga.setValore("-".concat(attributoRiga.getValore()));
				}
			}
			rigaArticolo = (RigaArticolo) rigaMagazzinoManager.getDao(rigaArticolo).salvaRigaMagazzinoNoCheck(
					rigaArticolo);
		}
		return rigaArticolo;
	}

}
