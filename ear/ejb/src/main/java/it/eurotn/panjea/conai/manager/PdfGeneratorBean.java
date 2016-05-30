package it.eurotn.panjea.conai.manager;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiTipoImballo;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione.PERIODICITA;
import it.eurotn.panjea.conai.domain.ConaiTipoIscrizione;
import it.eurotn.panjea.conai.domain.StatisticaTipoImballo;
import it.eurotn.panjea.conai.manager.interfaces.DatiForm;
import it.eurotn.panjea.conai.manager.interfaces.DatiFormFactory;
import it.eurotn.panjea.conai.manager.interfaces.PdfGenerator;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.NumberToLetters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

@Stateless(name = "Panjea.PdfGeneratorBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PdfGeneratorBean")
public class PdfGeneratorBean implements PdfGenerator {

	private static Logger logger = Logger.getLogger(PdfGeneratorBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private AziendeManager aziendeManager;

	@EJB
	private SediAziendaManager sediAziendaManager;

	/**
	 *
	 * @return azienda correntemente loggata
	 */
	private AziendaLite caricaAzienda() {
		try {
			return aziendeManager.caricaAzienda(getAzienda(), true);
		} catch (AnagraficaServiceException e) {
			logger.error("-->errore nel caricare l'azienda loggata", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Compila la parte del modulo relativa al periodo scelto per la dichiarazione.
	 *
	 * @param parametri
	 *            i parametri per verificare il periodo scelto
	 * @param form
	 *            il form su cui scrivere
	 * @param datiForm
	 *            il form del materiale a cui chiedere il nome del campo
	 */
	private void fillPeriodo(ConaiParametriCreazione parametri, AcroFields form, DatiForm datiForm) {
		// String[] states = form.getAppearanceStates(datiForm.getControlName(DatiForm.PERIODO_ANNUALE));
		// for (String string : states) {
		// System.out.println("state annuale = " + string);
		// }
		PERIODICITA periodo = parametri.getPeriodicita();
		try {
			switch (periodo) {
			case ANNUALE:
				form.setField(datiForm.getControlName(DatiForm.PERIODO_ANNUALE), "Sì");
				break;
			case TRIMESTRALE:
				String trimestre = parametri.getTrimestre();
				if (trimestre.equals("1")) {
					form.setField(datiForm.getControlName(DatiForm.PERIODO_TRIMESTRE_1), "Sì");
				} else if (trimestre.equals("2")) {
					form.setField(datiForm.getControlName(DatiForm.PERIODO_TRIMESTRE_2), "Sì");
				} else if (trimestre.equals("3")) {
					form.setField(datiForm.getControlName(DatiForm.PERIODO_TRIMESTRE_3), "Sì");
				} else if (trimestre.equals("4")) {
					form.setField(datiForm.getControlName(DatiForm.PERIODO_TRIMESTRE_4), "Sì");
				}
				break;
			case MENSILE:
				switch (parametri.getMese()) {
				case 0:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_1), "Sì");
					break;
				case 1:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_2), "Sì");
					break;
				case 2:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_3), "Sì");
					break;
				case 3:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_3), "Sì");
					break;
				case 4:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_4), "Sì");
					break;
				case 5:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_5), "Sì");
					break;
				case 6:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_6), "Sì");
					break;
				case 7:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_7), "Sì");
					break;
				case 8:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_8), "Sì");
					break;
				case 9:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_9), "Sì");
					break;
				case 10:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_10), "Sì");
					break;
				case 11:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_11), "Sì");
					break;
				case 12:
					form.setField(datiForm.getControlName(DatiForm.PERIODO_MESE_12), "Sì");
					break;
				default:
					break;
				}
				break;
			default:
				throw new UnsupportedOperationException("Periodo non supportato");
			}
		} catch (IOException e) {
			logger.error("-->errore nel recuperare il field", e);
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			logger.error("-->errore nel recuperare il field", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param parametri
	 *            i parametri da cui recuperare alcune informazioni di testata
	 * @param form
	 *            form del pdf
	 * @param datiForm
	 *            dati per recuperare i nome dei controlli
	 */
	private void fillTestata(ConaiParametriCreazione parametri, AcroFields form, DatiForm datiForm) {
		AziendaLite azienda = caricaAzienda();
		Azienda aziendaSede = new Azienda();
		aziendaSede.setId(azienda.getId());
		aziendaSede.setVersion(azienda.getVersion());
		SedeAzienda sedeAzienda = null;
		try {
			sedeAzienda = sediAziendaManager.caricaSedePrincipaleAzienda(aziendaSede);
		} catch (AnagraficaServiceException e1) {
			logger.error("-->errore nel caricare la sede principale per l'azienda " + getAzienda(), e1);
			throw new RuntimeException(e1);
		}

		// String[] states = form.getAppearanceStates(datiForm.getControlName(DatiForm.ISCRITTO_PRODUTTORE));
		// for (String string : states) {
		// System.out.println("ISCRITTO_PRODUTTORE = " + string);
		// }

		try {
			form.setField(datiForm.getControlName(DatiForm.DENOMINAZIONE), azienda.getDenominazione());
			form.setField(datiForm.getControlName(DatiForm.CODICE_SOCIO),
					azienda.getCodiceSocioConai() != null ? azienda.getCodiceSocioConai() : "");
			if (azienda.getConaiTipoIscrizione() != null
					&& azienda.getConaiTipoIscrizione().equals(ConaiTipoIscrizione.PRODUTTORE)) {
				form.setField(datiForm.getControlName(DatiForm.ISCRITTO_PRODUTTORE), "Sì");
			} else {
				form.setField(datiForm.getControlName(DatiForm.ISCRITTO_UTILIZZATORE), "Sì");
			}

			form.setField(datiForm.getControlName(DatiForm.INDIRIZZO_FATTURAZIONE),
					parametri.getIndirizzoFatturazione());
			form.setField(datiForm.getControlName(DatiForm.PEC), azienda.getPec() != null ? azienda.getPec() : "");

			form.setField(datiForm.getControlName(DatiForm.CAP), sedeAzienda.getSede().getDatiGeografici()
					.getDescrizioneCap());
			form.setField(datiForm.getControlName(DatiForm.CITTA), sedeAzienda.getSede().getDatiGeografici()
					.getDescrizioneLocalita());
			form.setField(datiForm.getControlName(DatiForm.PROVINCIA), sedeAzienda.getSede().getDatiGeografici()
					.getDescrizioneLivelloAmministrativo2());
			form.setField(datiForm.getControlName(DatiForm.CF),
					azienda.getCodiceFiscale() != null ? azienda.getCodiceFiscale() : "");
			form.setField(datiForm.getControlName(DatiForm.PIVA),
					azienda.getPartitaIVA() != null ? azienda.getPartitaIVA() : "");
			form.setField(datiForm.getControlName(DatiForm.PEC), azienda.getPec() != null ? azienda.getPec() : "");

			form.setField(datiForm.getControlName(DatiForm.REFERENTE),
					parametri.getReferente() != null ? parametri.getReferente() : "");
			form.setField(datiForm.getControlName(DatiForm.TEL),
					parametri.getTelReferente() != null ? parametri.getTelReferente() : "");
			form.setField(datiForm.getControlName(DatiForm.FAX),
					parametri.getFaxReferente() != null ? parametri.getFaxReferente() : "");
			form.setField(datiForm.getControlName(DatiForm.EMAIL),
					parametri.getEmailReferente() != null ? parametri.getEmailReferente() : "");

			Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			form.setField(datiForm.getControlName(DatiForm.DATA), dateFormat.format(parametri.getData()));

			form.setField(datiForm.getControlName(DatiForm.ANNO), parametri.getAnno());
		} catch (IOException e) {
			logger.error("-->errore nel recuperare il field", e);
			throw new RuntimeException(e);
		} catch (DocumentException e) {
			logger.error("-->errore nel recuperare il field", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Riempie i campi relativi ai tipi imballo.
	 *
	 * @param parametri
	 *            i parametri da cui prendere i dati
	 * @param form
	 *            form da riempire
	 * @param datiForm
	 *            dati con i nomi dei campi
	 * @param pesi
	 *            contiene le statistiche per ogni tipoimballo.
	 */
	private void fillTipiImballo(ConaiParametriCreazione parametri, AcroFields form, DatiForm datiForm,
			List<StatisticaTipoImballo> pesi) {
		DecimalFormat format = new DecimalFormat("#,###.######");
		BigDecimal totaleEsente = BigDecimal.ZERO;
		BigDecimal totaleAssoggettato = BigDecimal.ZERO;
		for (StatisticaTipoImballo statisticaTipoImballo : pesi) {
			ConaiTipoImballo conaiTipoImballo = statisticaTipoImballo.getTipoImballo();
			try {
				Item item = form.getFields().get(datiForm.getControlName(DatiForm.TOT_ASSOGETTATO));
				item.getWidget(0).remove(PdfName.F);
				item.getWidget(1).remove(PdfName.F);

				int pos = pesi.indexOf(statisticaTipoImballo);
				if (pos > -1) {
					StatisticaTipoImballo statistica = pesi.get(pos);

					BigDecimal pesoEsenzione = statistica.getPesoPerEsenzione();
					if (pesoEsenzione.compareTo(BigDecimal.ZERO) != 0) {
						form.setField(conaiTipoImballo.getNomeQtaEsenteCampoPdf(), format.format(pesoEsenzione));
						totaleEsente = totaleEsente.add(pesoEsenzione);
					}

					BigDecimal peso = statistica.getPeso();
					if (peso.compareTo(BigDecimal.ZERO) != 0) {
						form.setField(conaiTipoImballo.getNomeCampoQtaPdf(), format.format(peso));
						totaleAssoggettato = totaleAssoggettato.add(peso);
					}

					BigDecimal pesoTotale = statistica.getPesoTotale();
					if (pesoTotale.compareTo(BigDecimal.ZERO) != 0) {
						form.setField(conaiTipoImballo.getNomeCampoQtaTot(), format.format(pesoTotale));
					} else {
						form.setField(conaiTipoImballo.getNomeCampoQtaTot(), null);
					}
				}
			} catch (Exception e) {
				logger.error("-->errore nel settare il campo per il tipoImballo" + conaiTipoImballo.name(), e);
				throw new RuntimeException(e);
			}
		}

		try {
			if (totaleEsente.compareTo(BigDecimal.ZERO) != 0) {
				Item item = form.getFields().get(datiForm.getControlName(DatiForm.TOT_ESENTE));
				item.getWidget(0).remove(PdfName.F);
				form.setField(datiForm.getControlName(DatiForm.TOT_ESENTE), format.format(totaleEsente));
			}
			if (totaleAssoggettato.compareTo(BigDecimal.ZERO) != 0) {
				Item item = form.getFields().get(datiForm.getControlName(DatiForm.TOT_ASSOGETTATO));
				item.getWidget(0).remove(PdfName.F);
				item.getWidget(1).remove(PdfName.F);
				form.setField(datiForm.getControlName(DatiForm.TOT_ASSOGETTATO), format.format(totaleAssoggettato));
			}

			// TOTALE PESO
			BigDecimal pesoTotale = totaleAssoggettato.add(totaleEsente);
			form.setField(datiForm.getControlName(DatiForm.TOTALE), format.format(pesoTotale));
			Item item = form.getFields().get(datiForm.getControlName(DatiForm.TOTALE));
			item.getWidget(0).remove(PdfName.F);

			format = new DecimalFormat("#,##0.00");

			// PREZZO MATERIALE DEL PERIODO SE != 0
			BigDecimal prezzoMateriale = parametri.getPrezzoMateriale();
			if (parametri.getPrezzoMateriale().compareTo(BigDecimal.ZERO) != 0) {
				form.setField(datiForm.getControlName(DatiForm.CONTRIBUTO), format.format(prezzoMateriale));
			}

			// PREZZO TOTALE CONTRIBUTO
			BigDecimal prezzoTotale = totaleAssoggettato.multiply(prezzoMateriale);
			form.setField(datiForm.getControlName(DatiForm.TOTALE_CONTRIBUTO), format.format(prezzoTotale));
			item = form.getFields().get(datiForm.getControlName(DatiForm.TOTALE_CONTRIBUTO));
			item.getWidget(0).remove(PdfName.F);

			// PREZZO TOTALE CONTRIBUTO IN LETTERE
			form.setField(datiForm.getControlName(DatiForm.TOTALE_CONTRIBUTO_LETTERE),
					NumberToLetters.numberToText(prezzoTotale.doubleValue()));
			// item = form.getFields().get(datiForm.getControlName(DatiForm.TOTALE_CONTRIBUTO));
			// item.getWidget(0).remove(PdfName.F);

		} catch (Exception e) {
			logger.error("-->errore nel settarei totali", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] generaFile(ConaiParametriCreazione parametri, List<StatisticaTipoImballo> pesi) {
		ConaiMateriale materiale = parametri.getMateriale();
		try (InputStream stream = ConaiManagerBean.class.getClassLoader().getResourceAsStream(
				"it/eurotn/panjea/conai/resource/" + materiale.name() + ".pdf")) {

			// Devo generare il file con pdfBox per copiare la prima pagina e sproteggerlo. Con iText non riesco a farlo
			PDFParser parser = new PDFParser(stream);
			parser.parse();
			PDDocument document = parser.getPDDocument();
			if (document.isEncrypted()) {
				document.decrypt("");
			}
			PDDocument gen = new PDDocument();
			gen.addPage((PDPage) document.getDocumentCatalog().getAllPages().get(0));
			gen.getDocumentCatalog().setAcroForm((document.getDocumentCatalog().getAcroForm()));
			ByteArrayOutputStream tmpFile = new ByteArrayOutputStream();
			gen.save(tmpFile);
			gen.close();

			ByteArrayOutputStream result = new ByteArrayOutputStream();
			PdfReader pdfReader = new PdfReader(new ByteArrayInputStream(tmpFile.toByteArray()));
			PdfStamper stamper = new PdfStamper(pdfReader, result);
			AcroFields acroForm = stamper.getAcroFields();

			DatiForm datiForm = DatiFormFactory.getDatiForm(materiale);
			fillTestata(parametri, acroForm, datiForm);
			fillPeriodo(parametri, acroForm, datiForm);
			fillTipiImballo(parametri, acroForm, datiForm, pesi);
			stamper.close();
			return result.toByteArray();
		} catch (IOException e) {
			logger.error("-->errore in apertura file it/eurotn/panjea/conai/resource/" + materiale.name() + ".pdf", e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			logger.error("-->errore in apertura file it/eurotn/panjea/conai/resource/" + materiale.name() + ".pdf", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return codice azienda loggata
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

}
