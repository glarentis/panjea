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
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;
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
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010.
 */
@Stateless(name = "Panjea.FlussoExporter.RIBA")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FlussoExporter.RIBA")
public class FlussoExporterRiba extends AbstractFlussoExporter implements FlussoExporter {

	private static Logger logger = Logger.getLogger(FlussoExporterRiba.class.getName());

	private AziendaLite aziendaLite;
	private BigDecimal totale;

	private Effetto effetto;

	private Pagamento pagamento;

	private Rata rata;
	private BigDecimal importo;
	private int progressivo;
	/**
	 * formato delle date(ddMMyy).
	 */
	private final SimpleDateFormat format = new SimpleDateFormat("ddMMyy");

	private RapportoBancarioAzienda rapportoBancarioAzienda;

	private RapportoBancarioSedeEntita rapportoBancarioSedeEntita;
	private String[] segmentiAziendali;

	private EntitaLite entitaLite;

	private AreaRate areaRata;

	private AreaEffetti areaEffetti;
	private List<EntitaLite> errors;
	private String riferimenti;

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

	@Override
	protected void creaFlusso(AreaChiusure areaChiusureParam) throws RapportoBancarioPerFlussoAssenteException {
		logger.debug("--> Enter creaFlusso");

		this.areaEffetti = (AreaEffetti) areaChiusureParam;
		errors = new ArrayList<EntitaLite>();
		try {
			aziendaLite = aziendeManager.caricaAzienda(getJecPrincipal().getCodiceAzienda(), true);
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
		rapportoBancarioAzienda = areaEffetti.getDocumento().getRapportoBancarioAzienda();
		segmentiAziendali = valorizzaSegmentiAziendali();
		nRecords = 0;

		Date now = new Date();

		// records testa
		creaRecordTestaIB(now);
		// corpo
		Collection<Effetto> effetti;
		try {
			effetti = ((AreaEffetti) areaTesoreriaManager.caricaAreaTesoreria(areaEffetti)).getEffetti();
		} catch (ObjectNotFoundException e) {
			throw new RuntimeException(e);
		}
		if (effetti.isEmpty()) {
			return;
		}
		for (Effetto effettoCorrente : effetti) {
			this.effetto = effettoCorrente;
			creaRecordsEffetti();
		}
		// fine
		creaRecordFineEF(now);
		if (!errors.isEmpty()) {
			throw new RapportoBancarioPerFlussoAssenteException(errors);
		}
		logger.debug("--> Exit creaFlusso");
	}

	/**
	 * Creo sezione file.
	 */
	private void creaRecord14() {
		logger.debug("--> Enter creaRecord14");
		this.progressivo++;
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler 1
		buffer.append(" ");
		// codice fisso 14 2-3
		buffer.append("14");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// filler 11-22
		buffer.append(stringTmp.substring(0, 12));
		// data pagamento 23-28
		if (rata.getDataScadenza() == null) {
			buffer.append(StringUtils.repeat(" ", 6));
		} else {
			buffer.append(format.format(rata.getDataScadenza()));
		}
		// causale (30000) 29-33
		buffer.append("30000");
		// importo 34-46
		BigDecimal centesimi = importo.multiply(Importo.HUNDRED);
		String centString = stringZeri + new Integer(centesimi.intValue()).toString();
		buffer.append(centString.substring(centString.length() - 13, centString.length()));
		// segno 47
		buffer.append("-");
		// ABI banca assuntrice 48-52
		buffer.append(rapportoBancarioAzienda.getBanca().getCodice());
		// CAB banca assuntrice 53-57
		buffer.append(rapportoBancarioAzienda.getFiliale().getCodice());
		// c/c facoltativo a blank 58-69
		buffer.append(rapportoBancarioAzienda.getNumero());
		// ABI banca del cliente 70-74
		buffer.append(rapportoBancarioSedeEntita.getBanca().getCodice());
		// CAB banca del cliente 75-79
		buffer.append(rapportoBancarioSedeEntita.getFiliale().getCodice());
		// filler 80-91
		buffer.append(stringTmp, 0, 12);
		// codice azienda 92-96
		buffer.append(aziendaLite.getCodiceSIA());
		// tipo codice (idem)97
		buffer.append("4");
		// codice cliente debitore 98-113
		// allineato a sinistra
		String codice = new Integer(entitaLite.getCodice()).toString() + stringTmp;
		buffer.append(codice.substring(0, 16));
		// flag tipo debitore ( facoltativo a blank) 114
		buffer.append(" ");
		// filler 115-119
		buffer.append(stringTmp.substring(0, 5));
		// codice divisa fisso E
		buffer.append("E");
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord14");
	}

	/**
	 * Creo sezione file.
	 */
	private void creaRecord20() {
		logger.debug("--> Enter creaRecord20");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler 1
		buffer.append(" ");
		// codice fisso 20 2-3
		buffer.append("20");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// azienda ragione sociale 1° segmento 11-34
		buffer.append(segmentiAziendali[0]);
		// azienda ragione sociale 2° segmento 35-58
		buffer.append(segmentiAziendali[1]);
		// azienda ragione sociale 3° segmento 59-82
		buffer.append(segmentiAziendali[2]);
		// azienda ragione sociale 4° segmento 83-106
		buffer.append(segmentiAziendali[3]);
		// filler 107-120
		buffer.append(stringTmp.substring(0, 14));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord20");
	}

	/**
	 * Creo sezione file.
	 */
	private void creaRecord30() {
		logger.debug("--> Enter creaRecord30");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler 1
		buffer.append(" ");
		// codice fisso 30 2-3
		buffer.append("30");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// ragione sociale cliente due segmenti 11-40
		String tt = entitaLite.getAnagrafica().getDenominazione() + stringTmp;
		buffer.append(tt.substring(0, 30));
		// codice Fiscale 41-70
		buffer.append(tt.substring(30, 60));
		String codice = entitaLite.getAnagrafica().getPartiteIVA();
		if (codice == null || codice.isEmpty()) {
			codice = entitaLite.getAnagrafica().getCodiceFiscale();
		}
		tt = StringUtils.defaultString(codice) + stringTmp;
		buffer.append(tt.substring(0, 16)); // normalizzato a 16
		// filler (34) 71-86 + 87-120
		buffer.append(stringTmp.substring(0, 34));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord30");

	}

	/**
	 * Creo sezione file.
	 */
	private void creaRecord40() {
		logger.debug("--> Enter creaRecord40");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler 1
		buffer.append(" ");
		// codice fisso 40 2-3
		buffer.append("40");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// indirizzo 11-40
		String tt = entitaLite.getAnagrafica().getSedeAnagrafica().getIndirizzo() + stringTmp;
		buffer.append(tt.substring(0, 30));
		// cap 41-45
		tt = entitaLite.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getDescrizioneCap() + stringTmp;
		buffer.append(tt.substring(0, 5));
		// comune + provincia 46-70
		tt = entitaLite.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getDescrizioneLocalita() + " ("
				+ entitaLite.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getSiglaProvincia() + ")"
				+ stringTmp;
		buffer.append(tt.substring(0, 25));
		// Banca sportello domiciliataria (fac) 50 71-120
		buffer.append(stringTmp.substring(0, 50));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord40");

	}

	/**
	 * Creo sezione file.
	 */
	private void creaRecord50() {
		logger.debug("--> Enter creaRecord50");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
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
	}

	/**
	 * Creo sezione file.
	 */
	private void creaRecord51() {
		logger.debug("--> Enter creaRecord51");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler 1
		buffer.append(" ");
		// codice fisso 51 2-3
		buffer.append("51");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// numero ricevuta (num effetto) si mette idEff 11-20
		String tt = stringZeri + new Integer(effetto.getId()).toString();
		buffer.append(tt.substring(tt.length() - 10, tt.length()));
		// denominazione creditore (azienda) 21-40
		tt = aziendaLite.getDenominazione() + stringTmp;
		buffer.append(tt.substring(0, 20));
		// provincia (facoltativo) 41-55
		buffer.append(stringTmp.substring(0, 15));
		// numero autorizzazione (idem) 56-65
		buffer.append(stringTmp.substring(0, 10));
		// data autorizzazione (facoltativo) 66-71
		buffer.append(stringTmp.substring(0, 6));
		// filler 72-120
		buffer.append(stringTmp.substring(0, 49));
		// End record
		fileWriteLine(buffer.toString());
		logger.debug("--> Exit creaRecord51");

	}

	/**
	 * Creo sezione file.
	 */
	private void creaRecord70() {
		logger.debug("--> Enter creaRecord70");
		String stringTmp = "                                                                                                                                  ";
		String stringZeri = "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler 1
		buffer.append(" ");
		// codice fisso 70 2-3
		buffer.append("70");
		// numero progressivo 4-10
		String progressivoRiga = stringZeri + new Integer(this.progressivo).toString();
		buffer.append(progressivoRiga.substring(progressivoRiga.length() - 7, progressivoRiga.length()));
		// filler 11-88
		buffer.append(stringTmp.substring(0, 78));
		// indicatori di circuito (facoltativo) 89-100
		buffer.append(stringTmp.substring(0, 12));
		// indicatori richiesta incasso
		// tipo documento per debitore (1 fisso) 101
		buffer.append("1");
		// flag richiesta esito 102
		buffer.append(" ");
		// flag stampa avviso 103
		buffer.append(" ");
		// filler 104-120
		buffer.append(stringTmp.substring(0, 17));
		// End record
		fileWriteLine(buffer.toString());

		logger.debug("--> Exit creaRecord70");

	}

	/**
	 * Creo sezione file.
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
		SimpleDateFormat formatWithMinutes = new SimpleDateFormat("ddMMyyHHmm");
		buffer.append("RIBA_" + formatWithMinutes.format(now) + "     ");
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
		// numero record, aggiungo anche el record di chiusura( + 1)
		String nRecStr = stringZeri + new Integer(nRecords + 1).toString();
		buffer.append(nRecStr.substring(nRecStr.length() - 7, nRecStr.length()));
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
	 * Creo sezione file.
	 */
	private void creaRecordsEffetti() {
		logger.debug("--> Enter creaRecordsEffetti");
		// valorizzo gli oggetti che servono

		boolean datiOk = valorizzaFieldsEntita();
		if (!datiOk) {
			return;
		}
		// creo record
		creaRecord14();
		creaRecord20();
		creaRecord30();
		creaRecord40();
		creaRecord50();
		creaRecord51();
		creaRecord70();
		logger.debug("--> Exit creaRecordsEffetti");
	}

	/**
	 * Creo sezione file.
	 * 
	 * @param now
	 *            la data dell'operazione di scrittura flusso
	 */
	private void creaRecordTestaIB(Date now) {
		logger.debug("--> Enter creaRecordTestaIB");
		String stringTmp = "                                                                                                                                  ";
		// String stringZeri =
		// "00000000000000000000000000000000000000000000000000";
		StringBuffer buffer = new StringBuffer();
		// filler
		buffer.append(" ");
		// tipo record
		buffer.append("IB");
		// codice SIA mittente
		buffer.append(aziendaLite.getCodiceSIA());
		// codice ABI baca azienda
		buffer.append(rapportoBancarioAzienda.getBanca().getCodice());
		// data creazione
		buffer.append(format.format(now));
		// nome supporto
		SimpleDateFormat formatWithMinutes = new SimpleDateFormat("ddMMyyHHmm");
		buffer.append("RIBA_" + formatWithMinutes.format(now) + "     ");
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
	 * @return {@link JecPrincipal}
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
		logger.debug("--> Enter valorizzoFields");
		Set<Pagamento> pagamenti = effetto.getPagamenti();
		riferimenti = creaDescrizioneDaMascheraFlusso(pagamenti, areaMagazzinoManager);
		pagamento = pagamenti.iterator().next();
		importo = effetto.getImporto().getImportoInValuta();
		totale = totale.add(importo);
		rata = pagamento.getRata();
		areaRata = rata.getAreaRate();
		entitaLite = rata.getAreaRate().getDocumento().getEntita();
		rapportoBancarioSedeEntita = rata.getRapportoBancarioEntita();
		if (rapportoBancarioSedeEntita == null) {
			rata = rateManager.associaRapportoBancario(rata, areaRata, areaEffetti.getTipoAreaPartita()
					.getTipoPagamentoChiusura(), true);
			if (rata.getRapportoBancarioEntita() == null) {
				errors.add(entitaLite);
				return false;
			} else {
				rapportoBancarioSedeEntita = rata.getRapportoBancarioEntita();
			}
		}
		logger.debug("--> Exit valorizzoFields");
		return true;

	}

	/**
	 * Valorizza i segmenti aziendali.
	 * 
	 * @return segmenti valorizzati
	 */
	private String[] valorizzaSegmentiAziendali() {
		logger.debug("--> Enter valorizzaSegmentiAziendali");
		String stringTmp = "                                                                                                                                  ";
		String[] segmenti = new String[4];
		String temp = aziendaLite.getDenominazione() + "-"
				+ (aziendaLite.getIndirizzo() == null ? " " : aziendaLite.getIndirizzo()) + "-"
				+ (aziendaLite.getCap() == null ? "     " : aziendaLite.getCap().getDescrizione()) + " "
				+ (aziendaLite.getLocalita() == null ? "" : aziendaLite.getLocalita().getDescrizione()) + stringTmp;
		String tt = (temp + stringTmp).substring(0, 96);
		segmenti[0] = tt.substring(0, 24);
		segmenti[1] = tt.substring(24, 48);
		segmenti[2] = tt.substring(48, 72);
		segmenti[3] = tt.substring(72, 96);
		logger.debug("--> Exit valorizzaSegmentiAziendali");
		return segmenti;
	}

}
