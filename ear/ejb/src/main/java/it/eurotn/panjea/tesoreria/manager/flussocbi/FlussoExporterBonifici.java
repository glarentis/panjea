package it.eurotn.panjea.tesoreria.manager.flussocbi;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.flussocbi.interfaces.FlussoExporter;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.FlussoExporter.BONIFICO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FlussoExporter.BONIFICO")
public class FlussoExporterBonifici extends AbstractFlussoExporter implements FlussoExporter {

	private static Logger logger = Logger.getLogger(FlussoExporterBonifici.class.getName());

	@Resource
	private SessionContext context;

	@EJB
	private AziendeManager aziendeManager;

	@EJB
	private RateManager rateManager;

	@EJB
	private AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	private AreaMagazzinoManager areaMagazzinoManager;

	private AreaPagamenti areaPagamenti;
	private List<EntitaLite> errors;
	private AziendaLite aziendaLite;
	private BigDecimal totale;
	private Pagamento pagamento;
	private Rata rata;
	private BigDecimal importo;
	private int progressivo;

	private RapportoBancarioAzienda rapportoBancarioAzienda;
	private String[] segmentiEntita;
	private final SimpleDateFormat format = new SimpleDateFormat("ddMMyy");

	private String cFPivaEntita;
	private String cFPivaAzienda;

	private EntitaLite entitaLite;
	private AreaPartite areaPartite;
	private RapportoBancarioSedeEntita rapportoBancarioSedeEntita;
	private Date dataDoc;

	@Override
	protected void creaFlusso(AreaChiusure areaChiusureParam) throws RapportoBancarioPerFlussoAssenteException {
		logger.debug("--> Enter creaFlusso");
		this.areaPagamenti = (AreaPagamenti) areaChiusureParam;
		errors = new ArrayList<EntitaLite>();
		try {
			aziendaLite = aziendeManager.caricaAzienda(getJecPrincipal().getCodiceAzienda());
		} catch (AnagraficaServiceException e) {
			throw new RuntimeException(e);
		}
		// controllo se ha il codice SIA
		if (aziendaLite.getCodiceSIA() == null) {
			throw new RuntimeException("L\'AZIENDA NON HA IL CODICE SIA");
		}
		// inizializzazioni
		totale = BigDecimal.ZERO;
		progressivo = 0;
		rapportoBancarioAzienda = areaPagamenti.getDocumento().getRapportoBancarioAzienda();
		// segmentiAziendali = valorizzaSegmentiAziendali();
		nRecords = 0;
		cFPivaAzienda = aziendaLite.getCodiceFiscale();
		if (cFPivaAzienda == null) {
			cFPivaAzienda = aziendaLite.getPartitaIVA();
		}
		// records testa
		Date now = new Date();

		creaRecordTestaPC(now);

		// corpo
		Collection<Pagamento> pagamenti;
		try {
			pagamenti = ((AreaPagamenti) areaTesoreriaManager.caricaAreaTesoreria(areaPagamenti)).getPagamenti();
		} catch (ObjectNotFoundException e) {
			throw new RuntimeException(e);
		}

		if (pagamenti.isEmpty()) {
			return;
		}

		for (Pagamento pagamentoCorrente : pagamenti) {
			this.pagamento = pagamentoCorrente;
			creaRecordsPagamento();
		}

		// fine
		creaRecordFineEF(now);
		if (!errors.isEmpty()) {
			throw new RapportoBancarioPerFlussoAssenteException(errors);
		}

	}

	/**
	 * crae il record tipo 10.
	 */
	private void creaRecord10() {
		logger.debug("--> Enter creaRecord10");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// codice fisso 10
		buffer.append("10");
		// numero progressivo
		String progressivoRiga = stringZeri + new Integer(this.progressivo++).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// filler 11-16
		buffer.append(stringTmp.substring(0, 6));
		// data documento 17-22
		buffer.append(format.format(dataDoc));
		// data valuta beneficiario (opzionale) 23-28
		buffer.append(stringTmp.substring(0, 6));
		// causale (fissa 48000) 29-33
		buffer.append("48000");
		// importi (centesiomi) 34-46
		// importo
		BigDecimal centesimi = importo.multiply(Importo.HUNDRED);
		String centString = stringZeri + new Integer(centesimi.intValue()).toString();
		buffer.append(centString.substring(centString.length() - 13, centString.length()));
		// segno 47
		buffer.append("-");
		// ABI banca assuntrice 48-52
		buffer.append(rapportoBancarioAzienda.getBanca().getCodice());
		// CAB banca assuntrice 53-57
		buffer.append(rapportoBancarioAzienda.getFiliale().getCodice());
		// cc 58-69
		String tt = rapportoBancarioAzienda.getNumero() + stringTmp;
		buffer.append(tt.subSequence(0, 12));
		// ABI banca del cliente 70-74
		buffer.append(rapportoBancarioSedeEntita.getBanca().getCodice());
		// CAB banca del cliente 75-79
		buffer.append(rapportoBancarioSedeEntita.getFiliale().getCodice());
		// cc 80-91
		tt = rapportoBancarioSedeEntita.getNumero() + stringTmp;
		buffer.append(tt.subSequence(0, 12));
		// codice SIA azienda 92-96
		buffer.append(aziendaLite.getCodiceSIA());
		// tipo codice 97
		buffer.append("3");
		// codice fornitore (CF) 98-113
		tt = cFPivaEntita + stringTmp;
		buffer.append(tt.substring(0, 16));
		// modalita di pagamento 114
		buffer.append(" ");
		// filler 115-119
		buffer.append(stringTmp.substring(0, 5));
		// codic divisa (E) 120
		buffer.append("E");
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord10");

	}

	/**
	 * crae il record tipo 16.
	 */
	private void creaRecord16() {
		logger.debug("--> Enter creaRecord16");
		// record riferito all'azienda
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// codice fisso 10
		buffer.append("16");
		// numero progressivo
		String progressivoRiga = stringZeri + new Integer(this.progressivo++).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// codice paese 11-12
		buffer.append(rapportoBancarioAzienda.getCodicePaese());
		// check digit 13-14
		buffer.append(rapportoBancarioAzienda.getCheckDigit());
		// CIN 15
		buffer.append(rapportoBancarioAzienda.getCin());
		// codice ABI 16-20
		buffer.append(rapportoBancarioAzienda.getBanca().getCodice());
		// codice CAB 21-25
		buffer.append(rapportoBancarioAzienda.getFiliale().getCodice());
		// conto corr 26-37
		String tt = rapportoBancarioAzienda.getNumero() + stringTmp;
		buffer.append(tt.subSequence(0, 12));
		// filler 38-120
		buffer.append(stringTmp.substring(0, 83));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord16");

	}

	/**
	 * crae il record tipo 17.
	 */
	private void creaRecord17() {
		logger.debug("--> Enter creaRecord17");
		// questo record e' riferito all'entita
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// codice fisso 10
		buffer.append("17");
		// numero progressivo
		String progressivoRiga = stringZeri + new Integer(this.progressivo++).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// codice paese 11-12
		buffer.append(rapportoBancarioSedeEntita.getCodicePaese());
		// check digit 13-14
		buffer.append(rapportoBancarioSedeEntita.getCheckDigit());
		// CIN 15
		buffer.append(rapportoBancarioSedeEntita.getCin());
		// codice ABI 16-20
		buffer.append(rapportoBancarioSedeEntita.getBanca().getCodice());
		// codice CAB 21-25
		buffer.append(rapportoBancarioSedeEntita.getFiliale().getCodice());
		// conto corr 26-37
		String tt = rapportoBancarioSedeEntita.getNumero() + stringTmp;
		buffer.append(tt.subSequence(0, 12));
		// filler 38-120
		buffer.append(stringTmp.substring(0, 83));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord17");

	}

	/**
	 * crae il record tipo 20.
	 */
	private void creaRecord20() {
		logger.debug("--> Enter creaRecord20");
		// questo record e' riferito all'entita
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// codice fisso 20
		buffer.append("20");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo++).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// denominazione azienda 11 -40
		String tt = aziendaLite.getDenominazione() + stringTmp;
		buffer.append(tt.substring(0, 30));
		// indirizzo 41-70
		tt = (aziendaLite.getIndirizzo() == null ? "" : aziendaLite.getIndirizzo()) + stringTmp;
		buffer.append(tt.substring(0, 30));
		// localita 71-100
		tt = (aziendaLite.getLocalita() == null ? "" : aziendaLite.getLocalita().getDescrizione()) + stringTmp;
		buffer.append(tt.substring(0, 30));
		// codice fiscale 101-116
		tt = cFPivaAzienda + stringTmp;
		buffer.append(tt.substring(0, 16));
		// filler 117 - 120
		buffer.append(stringTmp.substring(0, 4));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord20");

	}

	/**
	 * crae il record tipo 30.
	 */
	private void creaRecord30() {
		logger.debug("--> Enter creaRecord30");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// codice fisso 30
		buffer.append("30");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo++).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// 1 segmento 11-40
		buffer.append(segmentiEntita[0]);
		// 2 segmento 41-70
		buffer.append(segmentiEntita[1]);
		// 3 segmento 71-100
		buffer.append(segmentiEntita[2]);
		// codiceFiscale 101-116
		String tt = cFPivaEntita + stringTmp;
		buffer.append(tt.substring(0, 16));
		// filler 117-120
		buffer.append(stringTmp.substring(0, 4));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord30");

	}

	/**
	 * crae il record tipo 50.
	 */
	private void creaRecord50() {

		logger.debug("--> Enter creaRecord50");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		Set<Pagamento> pagamenti = new HashSet<>();
		pagamenti.add(pagamento);
		String riferimenti = creaDescrizioneDaMascheraFlusso(pagamenti, areaMagazzinoManager);
		StringBuffer buffer = new StringBuffer();
		// filler 1
		buffer.append(" ");
		// codice fisso 50 2-3
		buffer.append("50");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// riferimento al debito (2 seg 40)
		// 11-50
		String tt = riferimenti + stringTmp;
		buffer.append(tt.substring(0, 40));
		// 51-90
		buffer.append(tt.substring(40, 80));
		// filler 10 91-100
		buffer.append(stringTmp.substring(0, 10));
		// codice fiscale azienda 101-116
		String codFis = aziendaLite.getCodiceFiscale();
		if (codFis == null) {
			codFis = aziendaLite.getPartitaIVA();
		}
		tt = codFis + stringTmp;
		buffer.append(tt.substring(0, 16)); // normalizzato a 16
		// filler 4 117-120
		buffer.append(stringTmp.substring(0, 4));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord50");

		// logger.debug("--> Enter creaRecord50");
		// String stringTmp =
		// "                                                                                                                                  ";
		// String stringZeri = "00000000000000000000000000000000000000000000000000";
		// StringBuffer buffer = new StringBuffer();
		// // filler
		// buffer.append(" ");
		// // codice fisso 50
		// buffer.append("50");
		// // numero progressivo 4-10
		// String progressivoRiga = stringZeri + new Integer(this.progressivo++).toString();
		// buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// // riferimento operazione 3 segmenti
		// String data = format.format(areaPartite.getDocumento().getDataDocumento());
		// String tt = "RIFERIMENTO A "
		// + (areaPartite.getTipoAreaPartita().getDescrizionePerFlusso() == null ? "" : areaPartite
		// .getTipoAreaPartita().getDescrizionePerFlusso()) + " DEL " + data + stringTmp;
		// // 1° segmento 11-40
		// buffer.append(tt.substring(0, 40));
		// // 2° segmento 41 - 70
		// buffer.append(tt.substring(40, 80));
		// // 3° segmento 71 - 100
		// buffer.append(tt.substring(40, 80));
		// // filler 101-120
		// buffer.append(stringTmp.substring(0, 20));
		// // End record
		// fileWriteLine(buffer.toString());
		// logger.debug("--> Exit creaRecord50");

	}

	/**
	 * crae il record tipo 70.
	 */
	private void creaRecord70() {
		logger.debug("--> Enter creaRecord70");
		// nota bene E' VUOTO!!!!
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// codice fisso 70
		buffer.append("70");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo++).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// filler 11-25
		buffer.append(stringTmp.substring(0, 15));
		// non disponibile 26-30
		buffer.append(stringTmp.substring(0, 5));
		// qualificatore di flusso 31-37
		buffer.append(stringTmp.substring(0, 7));
		// codice MP 38-42
		buffer.append(stringTmp.substring(0, 5));
		// filler 43-69
		buffer.append(stringTmp.substring(0, 27));
		// flag richiesta 70
		buffer.append(stringTmp.substring(0, 1));
		// codice univoco 71-100
		buffer.append(stringTmp.substring(0, 30));
		// filler 101-110
		buffer.append(stringTmp.substring(0, 10));
		// cin coord bancarie 111
		buffer.append(stringTmp.substring(0, 1));
		// filler 112
		buffer.append(stringTmp.substring(0, 1));
		// chiavi controllo 113-120
		buffer.append(stringTmp.substring(0, 8));
		// End record
		fileWriteLine(buffer.toString());

		logger.debug("--> Exit creaRecord70");

	}

	/**
	 * crae il record fine.
	 * 
	 * @param now
	 *            la data dell'operazione di scrittura flusso
	 */
	private void creaRecordFineEF(Date now) {
		logger.debug("--> Enter creaRecordFineEF");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// tipo record
		buffer.append("EF");
		// codice SIA mittente
		buffer.append(aziendaLite.getCodiceSIA());
		// codice ABI baca azienda
		buffer.append(rapportoBancarioAzienda.getBanca().getCodice());
		// data creazione
		buffer.append(format.format(now));
		// nome supporto
		buffer.append("#####BONIFICI_" + format.format(now));
		// filler
		buffer.append(stringTmp.substring(0, 6));
		// numero progressivo (totale in questo caso)
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// importo totale negativo
		BigDecimal centesimi = totale.multiply(Importo.HUNDRED);
		String centString = stringZeri + new Integer(centesimi.intValue()).toString();
		buffer.append(centString.substring(centString.length() - 15, centString.length()));
		// importo totale positivo (zero fisso)
		buffer.append(stringZeri.substring(0, 15));
		// numero record
		String nRecStr = stringZeri + new Integer(nRecords).toString();
		buffer.append(progressivoRiga.substring(nRecStr.length() - 7, nRecStr.length()));
		// filler
		buffer.append(stringTmp.substring(0, 24));
		// codice divisa (fisso E)
		buffer.append("E");
		// filler
		buffer.append(stringTmp.substring(0, 6));
		// End record
		fileWriteLine(buffer.toString());

		logger.debug("--> Exit creaRecordFineEF");
	}

	/**
	 * crae il record per gli effetti.
	 */
	private void creaRecordsPagamento() {
		logger.debug("--> Enter creaRecordsEffetti");
		// valorizzo gli oggetti che servono

		boolean datiOk = valorizzaFieldsEntita();
		if (!datiOk) {
			return;
		}
		// creo record
		creaRecord10();
		creaRecord16();
		creaRecord17();
		creaRecord20();
		creaRecord30();
		creaRecord50();
		creaRecord70();
		logger.debug("--> Exit creaRecordsEffetti");
	}

	/**
	 * crae il record testata.
	 * 
	 * @param now
	 *            la data dell'operazione di scrittura flusso
	 */
	private void creaRecordTestaPC(Date now) {
		logger.debug("--> Enter creaRecordTestaPC");
		String stringTmp = "                                                                                                                                  ";
		// String stringZeri =
		// "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// tipo record
		buffer.append("PC");
		// codice SIA mittente
		buffer.append(aziendaLite.getCodiceSIA());
		// codice ABI baca azienda
		buffer.append(rapportoBancarioAzienda.getBanca().getCodice());
		// data creazione
		buffer.append(format.format(now));
		// nome supporto
		buffer.append("#####BONIFICI_" + format.format(now));
		// filler campi non obbligatori
		buffer.append(stringTmp.substring(0, 74));
		// codice divisa (fisso E)
		buffer.append("E ");
		// filler
		buffer.append("     ");
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecordTestaIB");
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return jecPrincipal loggato
	 */
	private JecPrincipal getJecPrincipal() {
		return (JecPrincipal) context.getCallerPrincipal();
	}

	/**
	 * valorizza campi dell'entità. In caso di errore aggiunge all'array degli errori.
	 * 
	 * @return true se è riuscito a valorizzare i campi. False altrimenti.
	 */
	private boolean valorizzaFieldsEntita() {
		logger.debug("--> Enter valorizzaFieldsEntita");
		String stringTmp = "                                                                                                                                  ";
		dataDoc = pagamento.getAreaChiusure().getDocumento().getDataDocumento();
		importo = pagamento.getImporto().getImportoInValuta();
		totale = totale.add(importo);
		rata = pagamento.getRata();
		areaPartite = rata.getAreaRate();
		entitaLite = rata.getAreaRate().getDocumento().getEntita();
		String cf = entitaLite.getAnagrafica().getCodiceFiscale();
		if (cf != null && cf.length() > 0) {
			cFPivaEntita = cf;
		}
		cf = entitaLite.getAnagrafica().getPartiteIVA();
		if (cf != null && cf.length() > 0) {
			cFPivaEntita = cf;
		}
		rapportoBancarioSedeEntita = rata.getRapportoBancarioEntita();
		if (rapportoBancarioSedeEntita == null) {
			rata = rateManager.associaRapportoBancario(rata, areaPartite, areaPagamenti.getTipoAreaPartita()
					.getTipoPagamentoChiusura(), true);
			rapportoBancarioSedeEntita = rata.getRapportoBancarioEntita();
			if (rata.getRapportoBancarioEntita() == null) {
				errors.add(entitaLite);
				return false;
			}
		}
		segmentiEntita = new String[3];
		String tt = entitaLite.getAnagrafica().getDenominazione() + stringTmp;
		segmentiEntita[0] = tt.substring(0, 30);
		segmentiEntita[1] = tt.substring(30, 60);
		segmentiEntita[2] = tt.substring(60, 90);
		logger.debug("--> Exit valorizzaFieldsEntita");
		return true;

	}

}
