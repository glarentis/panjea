package it.eurotn.panjea.onroad.importer.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.onroad.domain.DocumentoOnRoad;
import it.eurotn.panjea.onroad.importer.manager.interfaces.OnroadDocumentoTransformer;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.DocumentoOnroadTransformer")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentoOnroadTransformer")
public class OnroadDocumentoTransformerBean implements OnroadDocumentoTransformer {

	private static Logger logger = Logger.getLogger(OnroadDocumentoTransformerBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private CodicePagamentoManager codicePagamentoManager;

	@EJB
	private SediEntitaManager sediEntitaManager;

	@EJB
	private MagazzinoDocumentoService magazzinoDocumentoService;

	/**
	 * @param codicePagamento
	 *            il codice del codice pagamento da caricare
	 * @return CodicePagamento o null
	 */
	private CodicePagamento caricaCodicePagamento(String codicePagamento) {
		System.out.println("codicePagamento " + codicePagamento);
		if (codicePagamento != null && !codicePagamento.trim().isEmpty()) {
			return codicePagamentoManager.caricaCodicePagamento(codicePagamento);
		}
		return null;
	}

	/**
	 * 
	 * @param codiceCliente
	 *            il codice cliente da ricercare (può essere un codice del tipo NU001 nel caso in cui venga creato sul
	 *            terminale)
	 * @param codiceSede
	 *            il codice sede o il codice cliente nel caso in cui sia sulla sede principale (se creato sul terminale
	 *            avrà lo stesso codice cliente NU001)
	 * @return SedeEntita o null
	 */
	private SedeEntita caricaSedeCliente(String codiceCliente, String codiceSede) {
		SedeEntita sede = null;
		ClienteLite cliente = null;

		if (codiceCliente != null && !codiceCliente.isEmpty()) {
			Integer codice = null;
			try {
				Pattern pattern = Pattern.compile("[A-Za-z]+");
				Matcher m = pattern.matcher(codiceCliente);
				if (!m.find()) {
					codice = Integer.parseInt(codiceCliente);
					Query queryCliente = panjeaDAO
							.prepareQuery("select c from ClienteLite c where c.codice=:paramCodiceCliente");
					queryCliente.setParameter("paramCodiceCliente", codice);
					cliente = (ClienteLite) panjeaDAO.getSingleResult(queryCliente);

					if (cliente != null && codiceCliente != null && codiceCliente.equals(codiceSede)) {
						sede = sediEntitaManager.caricaSedePrincipaleEntita(cliente.creaProxyEntita());
					} else if (cliente != null && codiceCliente != null && !codiceCliente.equals(codiceSede)) {
						Query querySedeCliente = panjeaDAO
								.prepareQuery("select se from SedeEntita se where se.entita.id=:paramIdEntita and se.codice=:paramCodiceSedeCliente");
						querySedeCliente.setParameter("paramCodiceSedeCliente", codiceSede);
						querySedeCliente.setParameter("paramIdEntita", cliente.getId());

						sede = (SedeEntita) panjeaDAO.getSingleResult(querySedeCliente);
					}
				} else {
					Query querySedeCliente = panjeaDAO
							.prepareQuery("select se from SedeEntita se where se.codiceImportazione=:paramCodiceImportazione");
					querySedeCliente.setParameter("paramCodiceImportazione", codiceCliente);

					sede = (SedeEntita) panjeaDAO.getSingleResult(querySedeCliente);
				}

			} catch (NonUniqueResultException e) {
				System.out.println("cliente " + codiceCliente + " sede " + codiceSede);
			} catch (ObjectNotFoundException e) {
				System.out.println("cliente " + codiceCliente + " sede " + codiceSede);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return sede;
	}

	/**
	 * @param codiceTipoDocumento
	 *            il codice del tipo documento panjea da caricare.
	 * @return TipoAreaMagazzino
	 */
	private TipoAreaMagazzino caricaTipoAreaMagazzino(String codiceTipoDocumento) {
		TipoAreaMagazzino tipoAreaMagazzino = null;
		try {
			Query queryCliente = panjeaDAO
					.prepareQuery("select tam from TipoAreaMagazzino tam where tam.tipoDocumento.codice=:paramCodiceTipoDocumento");
			queryCliente.setParameter("paramCodiceTipoDocumento", codiceTipoDocumento);
			tipoAreaMagazzino = (TipoAreaMagazzino) panjeaDAO.getSingleResult(queryCliente);
		} catch (ObjectNotFoundException e) {
			logger.warn("TipoAreaMagazzino non trovata");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return tipoAreaMagazzino;
	}

	/**
	 * @param codiceTipoDocumento
	 *            il codice del tipo documento panjea da caricare.
	 * @return TipoAreaMagazzino
	 */
	private TipoAreaPartita caricaTipoAreaPartita(String codiceTipoDocumento) {
		TipoAreaPartita tipoAreaPartita = null;
		try {
			Query queryTap = panjeaDAO
					.prepareQuery("select tap from TipoAreaPartita tap where tap.tipoDocumento.codice=:paramCodiceTipoDocumento");
			queryTap.setParameter("paramCodiceTipoDocumento", codiceTipoDocumento);
			tipoAreaPartita = (TipoAreaPartita) panjeaDAO.getSingleResult(queryTap);
		} catch (ObjectNotFoundException e) {
			logger.warn("TipoAreaPartita non trovata");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return tipoAreaPartita;
	}

	/**
	 * @return principal
	 */
	private JecPrincipal getJecPrincipal() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal());
	}

	/**
	 * Salva l'area magazzino.
	 * 
	 * @param areaMagazzinoFullDTO
	 *            l'area da salvare
	 * @return AreaMagazzinoFullDTO salvata
	 */
	private AreaMagazzinoFullDTO salva(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
		AreaMagazzinoFullDTO areaMagazzinoSalvata = null;
		try {
			areaMagazzinoSalvata = magazzinoDocumentoService.salvaAreaMagazzino(
					areaMagazzinoFullDTO.getAreaMagazzino(), areaMagazzinoFullDTO.getAreaRate(), false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return areaMagazzinoSalvata;
	}

	@Override
	public AreaMagazzinoFullDTO trasforma(DocumentoOnRoad documentoOnRoad,
			Map<String, String> conversioniTipiDocumentoOnRoadToPanjea) {

		// C:valido /A:annullato/T:trasmesso
		String statoDocumento = documentoOnRoad.getStatoDocumento();

		// se il documento è annullato non lo importo!
		if (statoDocumento != null && statoDocumento.equals("A")) {
			return null;
		}

		// codice panjea/onroad diverso nel caso di nuovo cliente palmare
		String codiceCliente = documentoOnRoad.getCodiceCliente();
		// codice=codice cliente se sede principale
		String codiceSedeCliente = documentoOnRoad.getCodiceDestinatario();
		SedeEntita sedeEntita = caricaSedeCliente(codiceCliente, codiceSedeCliente);

		// codice cessionario per vendite conto terzi
		String codiceCessionario = documentoOnRoad.getCodiceCessionario();
		// codice panjea/onroad diverso
		String tipoDocumentoOnRoad = documentoOnRoad.getTipoDocumento();
		// se il codice cessionario è presente imposto il tipo documento onroad = CT (conto terzi) e quindi imposto il
		// tipo documento Panjea associato (deve essere impostato il valore nelle preferenze globali)
		if (codiceCessionario != null && !codiceCessionario.trim().isEmpty()) {
			tipoDocumentoOnRoad = "CT" + codiceCessionario;
		}
		String codiceTipoDocumentoPanjea = conversioniTipiDocumentoOnRoadToPanjea.get(tipoDocumentoOnRoad);
		TipoAreaMagazzino tipoAreaMagazzino = caricaTipoAreaMagazzino(codiceTipoDocumentoPanjea);
		TipoAreaPartita tipoAreaPartita = caricaTipoAreaPartita(codiceTipoDocumentoPanjea);

		// codice panjea/onroad uguale
		String codiceCodicePagamento = documentoOnRoad.getCodicePagamento();
		CodicePagamento codicePagamento = caricaCodicePagamento(codiceCodicePagamento);

		String codiceDocumento = documentoOnRoad.getIdentificativoDocumento();
		Date dataDocumento = documentoOnRoad.getDataDocumento();
		String oraDocumento = documentoOnRoad.getOraDocumento();

		// totale lordo - sconti
		BigDecimal imponibile = documentoOnRoad.getImponibile();

		// imponibile + totale iva
		BigDecimal totaleDocumento = documentoOnRoad.getTotaleDocumento();

		// sconto di testata in %
		BigDecimal scontoTestata = documentoOnRoad.getScontoTestata();

		BigDecimal imposta = totaleDocumento.subtract(imponibile);

		// sconto di testata in valuta
		BigDecimal scontoValuta = documentoOnRoad.getScontoValuta();

		BigDecimal scontoPagamento = documentoOnRoad.getScontoPagamento();

		// vuoto
		String codiceVettore = documentoOnRoad.getCodiceVettore();
		// 0
		BigDecimal imponibileNS = documentoOnRoad.getImponibileNS();
		String numeroIncasso = documentoOnRoad.getNumeroIncasso();
		String numeroDDTRiferimento = documentoOnRoad.getNumeroDDTRiferimento();
		String dataDDTRiferimento = documentoOnRoad.getDataDDTRiferimento();
		Integer numeroColli = documentoOnRoad.getNumeroColli();
		String numeroPagineStampate = documentoOnRoad.getNumeroPagine();

		AreaMagazzino areaMagazzino = new AreaMagazzino();
		areaMagazzino.setDataRegistrazione(dataDocumento);
		areaMagazzino.getDocumento().setDataDocumento(dataDocumento);
		areaMagazzino.getDocumento().setEntita(sedeEntita != null ? sedeEntita.getEntita().getEntitaLite() : null);
		areaMagazzino.getDocumento().setSedeEntita(sedeEntita);
		Importo tot = new Importo();
		tot.setImportoInValuta(totaleDocumento);
		tot.setImportoInValutaAzienda(totaleDocumento);
		tot.setCodiceValuta(sedeEntita != null ? sedeEntita.getCodiceValuta() : "EUR");
		areaMagazzino.getDocumento().setTotale(tot);

		Importo impos = tot.clone();
		impos.setImportoInValuta(imposta);
		impos.setImportoInValutaAzienda(imposta);

		Importo totMerce = tot.clone();
		totMerce.setImportoInValuta(imponibile);
		totMerce.setImportoInValutaAzienda(imponibile);

		areaMagazzino.getDocumento().setImposta(impos);
		areaMagazzino.setTotaleMerce(totMerce);
		areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.CONFERMATO);
		areaMagazzino.getDatiValidazioneRighe().valida(getJecPrincipal().getUserName());

		if (sedeEntita != null) {
			areaMagazzino.setListino(sedeEntita.getSedeMagazzino().getListino());
			areaMagazzino.setRaggruppamentoBolle(sedeEntita.getSedeMagazzino().isRaggruppamentoBolle());
			areaMagazzino.setCausaleTrasporto(sedeEntita.getSedeMagazzino().getCausaleTrasporto() != null ? sedeEntita
					.getSedeMagazzino().getCausaleTrasporto().getDescrizione() : null);
			areaMagazzino.setTrasportoCura(sedeEntita.getSedeMagazzino().getTrasportoCura() != null ? sedeEntita
					.getSedeMagazzino().getTrasportoCura().getDescrizione() : null);
			areaMagazzino.setAspettoEsteriore(sedeEntita.getSedeMagazzino().getAspettoEsteriore() != null ? sedeEntita
					.getSedeMagazzino().getAspettoEsteriore().getDescrizione() : null);
			areaMagazzino.setTipoPorto(sedeEntita.getSedeMagazzino().getTipoPorto() != null ? sedeEntita
					.getSedeMagazzino().getTipoPorto().getDescrizione() : null);
			areaMagazzino.setVettore(sedeEntita.getVettore());
			SedeEntita sedeVettore = null;
			try {
				if (sedeEntita.getVettore() != null) {
					sedeVettore = sediEntitaManager.caricaSedePrincipaleEntita(sedeEntita.getVettore().getId());
				}
			} catch (AnagraficaServiceException e1) {
				logger.warn("Errore nel caricare la sede principale entita");
			}
			areaMagazzino.setSedeVettore(sedeVettore);
		}

		areaMagazzino.setNumeroColli(numeroColli);

		Calendar cal = Calendar.getInstance();
		cal.setTime(dataDocumento);
		String hh = oraDocumento.substring(0, 2);
		String mm = oraDocumento.substring(2);
		cal.set(Calendar.HOUR_OF_DAY, new Integer(hh));
		cal.set(Calendar.MINUTE, new Integer(mm));
		cal.set(Calendar.SECOND, 0);
		areaMagazzino.setDataInizioTrasporto(cal.getTime());

		areaMagazzino.getDocumento().setTipoDocumento(tipoAreaMagazzino.getTipoDocumento());
		areaMagazzino.setTipoAreaMagazzino(tipoAreaMagazzino);
		areaMagazzino.getDocumento().getCodice().setCodice(codiceDocumento);
		try {
			String numericCode = codiceDocumento.replaceAll("[^0-9]", "");
			areaMagazzino.getDocumento().setValoreProtocollo(Integer.valueOf(numericCode));
		} catch (Exception e) {
			logger.warn("--> Impossibile converire il codice in un valore protocollo numerico", e);
		}
		areaMagazzino.setDepositoOrigine(tipoAreaMagazzino.getDepositoOrigine());
		areaMagazzino.setDepositoDestinazione(tipoAreaMagazzino.getDepositoDestinazione());
		areaMagazzino.setAnnoMovimento(cal.get(Calendar.YEAR));
		areaMagazzino.getDatiGenerazione().setTipoGenerazione(TipoGenerazione.ATON);

		AreaRate areaRate = null;
		if (tipoAreaPartita != null) {
			areaRate = new AreaRate();
			areaRate.setCodicePagamento(codicePagamento);
			areaRate.setDocumento(areaMagazzino.getDocumento());
			areaRate.setTipoAreaPartita(tipoAreaPartita);
		}
		AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
		areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzino);
		areaMagazzinoFullDTO.setAreaRate(areaRate);

		areaMagazzinoFullDTO = salva(areaMagazzinoFullDTO);
		return areaMagazzinoFullDTO;
	}
}
