package it.eurotn.panjea.contabilita.manager.spesometro.builder.aggregato;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.domain.TipologiaCodiceIvaSpesometro;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroDataHandler;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometroAggregato;

public class SpesometroAggregatoProcessor {

    private SpesometroDataHandler dataHandler = new SpesometroDataHandler();

    /**
     * Restituisce la sezione del record del documento aggregato che riguarda i dati anagrafici.
     *
     * @param doc
     *            documento aggregato
     * @return sezione del record anagrafica
     */
    private String getBLSezioneAnagrafica(DocumentoSpesometroAggregato doc) {
        StringBuilder sbDoc = new StringBuilder();

        // Persona fisica e anagrafica sono mutualmente esclusivi. Se la persona fisica esiste prendo quella
        if (!StringUtils.isBlank(doc.getAnagrafica().getPersonaFisica().getCognome())
                && !StringUtils.isBlank(doc.getAnagrafica().getPersonaFisica().getNome())) {
            sbDoc.append("BL001001");
            sbDoc.append(dataHandler.getANNP(doc.getAnagrafica().getPersonaFisica().getCognome()));
            sbDoc.append("BL001002");
            sbDoc.append(dataHandler.getANNP(doc.getAnagrafica().getPersonaFisica().getNome()));
            sbDoc.append("BL001003");
            sbDoc.append(dataHandler.getDTNP(doc.getAnagrafica().getPersonaFisica().getDataNascita()));
            sbDoc.append("BL001004");
            sbDoc.append(dataHandler.getANNP(
                    doc.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().getDescrizioneNazione()));
            sbDoc.append("BL001006");
            Integer statoIUC = doc.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().getNazione()
                    .getCodiceNazioneUIC();
            String statoIUCString = statoIUC == null ? "" : StringUtils.leftPad(String.valueOf(statoIUC), 3, '0');
            sbDoc.append(StringUtils.leftPad(statoIUCString, 16, " "));
        } else {
            sbDoc.append("BL001007");
            sbDoc.append(dataHandler.getANNP(doc.getAnagrafica().getDenominazione()));
            sbDoc.append("BL001008");
            sbDoc.append(dataHandler
                    .getANNP(doc.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getDescrizioneLocalita()));
            sbDoc.append("BL001009");
            Integer statoIUC = doc.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getNazione()
                    .getCodiceNazioneUIC();
            String statoIUCString = statoIUC == null ? "" : StringUtils.leftPad(String.valueOf(statoIUC), 3, '0');
            sbDoc.append(StringUtils.leftPad(statoIUCString, 16, " "));
            sbDoc.append("BL001010");
            sbDoc.append(dataHandler.getANNP(doc.getAnagrafica().getSedeAnagrafica().getIndirizzo()));
        }

        return sbDoc.toString();
    }

    /**
     * Restituisce la sezione del record del documento aggregato che riguarda le operazioni attive del quadro FA.
     *
     * @param doc
     *            documento
     * @param currentFA
     *            progressivo corrente
     * @return sezione operazioni attive FA
     */
    private String getFASezioneOperazioniAttive(DocumentoSpesometroAggregato doc, Integer currentFA) {
        StringBuilder sbDoc = new StringBuilder();

        if (doc.getImponibileFattureAttive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "007"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImponibileFattureAttive()));
        }
        if (doc.getImpostaFattureAttive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "008"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImpostaFattureAttive()));
        }
        if (doc.getImponibileNoteCreditoAttive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "010"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImponibileNoteCreditoAttive()));
        }
        if (doc.getImpostaNoteCreditoAttive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "011"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImpostaNoteCreditoAttive()));
        }

        return sbDoc.toString();
    }

    /**
     * Restituisce la sezione del record del documento aggregato che riguarda le operazioni passive del quadro FA.
     *
     * @param doc
     *            documento
     * @param currentFA
     *            progressivo corrente
     * @return sezione operazioni attive FA
     */
    private String getFASezioneOperazioniPassive(DocumentoSpesometroAggregato doc, Integer currentFA) {
        StringBuilder sbDoc = new StringBuilder();

        if (doc.getImponibileFatturePassive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "012"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImponibileFatturePassive()));
        }
        if (doc.getImpostaFatturePassive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "013"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImpostaFatturePassive()));
        }
        if (doc.getImponibileNoteCreditoPassive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "015"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImponibileNoteCreditoPassive()));
        }
        if (doc.getImpostaNoteCreditoPassive().compareTo(BigDecimal.ZERO) > 0) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "016"));
            sbDoc.append(dataHandler.getImportoNPNP(doc.getImpostaNoteCreditoPassive()));
        }

        return sbDoc.toString();
    }

    /**
     * Restituisce la sezione del record del documento aggregato che riguarda la partita iva, il codice fiscale e il
     * flag riepilogativo.
     *
     * @param doc
     *            documento
     * @param currentFA
     *            progressivo corrente
     * @return sezione
     */
    private String getFASezionePIvaCFRiepilogativo(DocumentoSpesometroAggregato doc, Integer currentFA) {
        StringBuilder sbDoc = new StringBuilder();

        String partitaIva = doc.getAnagrafica().getPartiteIVA();
        String codiceFiscale = doc.getAnagrafica().getCodiceFiscale();
        if (doc.isRiepilogativo()) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "003"));
            sbDoc.append(StringUtils.leftPad("1", 16, " "));
        } else if (!StringUtils.isEmpty(partitaIva)) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "001"));
            sbDoc.append(StringUtils.rightPad(partitaIva, 16, " "));
        } else if (!StringUtils.isEmpty(codiceFiscale)) {
            sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "002"));
            sbDoc.append(StringUtils.rightPad(codiceFiscale, 16, " "));
        }

        return sbDoc.toString();
    }

    /**
     * Restituisce l'header per il record C dei campi posizionali.
     *
     * @param codiceFiscaleAzienda
     *            codice fiscale dell'azienda
     * @param progressivo
     *            progressivo
     * @return header
     */
    private String getHeaderRecordC(String codiceFiscaleAzienda, Integer progressivo) {
        StringBuilder sbHeader = new StringBuilder();
        sbHeader.append("C");
        sbHeader.append(StringUtils.rightPad(codiceFiscaleAzienda, 16, " "));
        sbHeader.append(StringUtils.leftPad(progressivo.toString(), 8, "0"));
        sbHeader.append(StringUtils.repeat(" ", 3));
        sbHeader.append(StringUtils.repeat(" ", 25));
        sbHeader.append(StringUtils.repeat(" ", 20));
        sbHeader.append(StringUtils.rightPad("01398480226", 16, " "));

        return sbHeader.toString();
    }

    /**
     * Restituisce la sezione del record riguardante il quadro BL dei documenti aggregati.
     *
     * @param documenti
     *            documenti
     * @param progressivo
     *            progressivo di partenza
     * @param codiceFiscaleAzienda
     *            codice fiscale dell'azienda
     * @return sezione quadro BL
     */
    public String getQuadroBL(List<DocumentoSpesometroAggregato> documenti, int progressivo,
            String codiceFiscaleAzienda) {

        StringBuilder sbQuadro = new StringBuilder();

        StringBuilder sbFooter = new StringBuilder();
        sbFooter.append(StringUtils.repeat(" ", 8));
        sbFooter.append("A");
        sbFooter.append("\r\n");

        int progressivoBL = progressivo;

        if (!documenti.isEmpty()) {

            for (DocumentoSpesometroAggregato doc : documenti) {
                StringBuilder sbDoc = new StringBuilder();

                progressivoBL++;

                sbDoc.append(getHeaderRecordC(codiceFiscaleAzienda, progressivoBL));

                // BL001
                sbDoc.append(getBLSezioneAnagrafica(doc));
                if (!StringUtils.isEmpty(doc.getAnagrafica().getPartiteIVA())) {
                    sbDoc.append("BL002001");
                    sbDoc.append(dataHandler.getANNP(doc.getAnagrafica().getPartiteIVA()));
                }
                // BL002
                sbDoc.append("BL002002");
                sbDoc.append(dataHandler.getCBNP(false));
                boolean acqSoggNonResidenti = doc
                        .getTipologiaCodiceIvaSpesometro() == TipologiaCodiceIvaSpesometro.SERVIZI;
                sbDoc.append("BL002003");
                sbDoc.append(dataHandler.getCBNP(!acqSoggNonResidenti));
                sbDoc.append("BL002004");
                sbDoc.append(dataHandler.getCBNP(acqSoggNonResidenti));
                if (!acqSoggNonResidenti && (doc.getImponibileFattureAttive().add(doc.getImponibileNoteCreditoAttive()))
                        .compareTo(BigDecimal.ZERO) > 0) {
                    // BL003
                    sbDoc.append("BL003001");
                    sbDoc.append(dataHandler.getImportoNPNP(
                            doc.getImponibileFattureAttive().add(doc.getImponibileNoteCreditoAttive())));
                    sbDoc.append("BL003002");
                    sbDoc.append(dataHandler
                            .getImportoNPNP(doc.getImpostaFattureAttive().add(doc.getImponibileNoteCreditoAttive())));
                }

                if (doc.getImponibileFatturePassive().add(doc.getImponibileNoteCreditoPassive())
                        .compareTo(BigDecimal.ZERO) > 0) {
                    // BL006
                    sbDoc.append("BL006001");
                    sbDoc.append(dataHandler.getImportoNPNP(
                            doc.getImponibileFatturePassive().add(doc.getImponibileNoteCreditoPassive())));
                    sbDoc.append("BL006002");
                    sbDoc.append(dataHandler
                            .getImportoNPNP(doc.getImpostaFatturePassive().add(doc.getImponibileNoteCreditoPassive())));
                }

                sbDoc.append(StringUtils.repeat(" ", 1889 - sbDoc.length()));
                sbDoc.append(sbFooter.toString());
                sbQuadro.append(sbDoc);
            }
        }

        return sbQuadro.toString();
    }

    /**
     * Restituisce la sezione del record riguardante il quadro FA dei documenti aggregati.
     *
     * @param documenti
     *            documenti
     * @param codiceFiscaleAzienda
     *            codice fiscale dell'azienda
     * @return sezione quadro FA
     */
    public String getQuadroFA(List<DocumentoSpesometroAggregato> documenti, String codiceFiscaleAzienda) {

        StringBuilder sbQuadro = new StringBuilder();

        Integer progressivo = 1;
        Integer currentFA = 1;
        int totalFA = 3;

        StringBuilder sbFooter = new StringBuilder();
        sbFooter.append(StringUtils.repeat(" ", 8));
        sbFooter.append("A");
        sbFooter.append("\r\n");

        if (!documenti.isEmpty()) {

            StringBuilder sbDoc = new StringBuilder();
            sbDoc.append(getHeaderRecordC(codiceFiscaleAzienda, progressivo));
            for (DocumentoSpesometroAggregato doc : documenti) {

                if (currentFA.intValue() - 1 == totalFA) {
                    sbDoc.append(StringUtils.repeat(" ", 1889 - sbDoc.length()));
                    sbDoc.append(sbFooter.toString());
                    sbQuadro.append(sbDoc);

                    progressivo++;

                    sbDoc = new StringBuilder();
                    sbDoc.append(getHeaderRecordC(codiceFiscaleAzienda, progressivo));
                    currentFA = 1;

                }

                // p.iva / cf / riep.
                sbDoc.append(getFASezionePIvaCFRiepilogativo(doc, currentFA));

                if (doc.getNumeroOperazioniAttive() > 0) {
                    sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "004"));
                    sbDoc.append(dataHandler.getNPNP(new BigDecimal(doc.getNumeroOperazioniAttive())));
                }
                if (doc.getNumeroOperazioniPassive() > 0) {
                    sbDoc.append(dataHandler.formatQuadro("FA", currentFA, "005"));
                    sbDoc.append(dataHandler.getNPNP(new BigDecimal(doc.getNumeroOperazioniPassive())));
                }

                if (doc.getNumeroOperazioniAttive() > 0) {
                    sbDoc.append(getFASezioneOperazioniAttive(doc, currentFA));
                }

                if (doc.getNumeroOperazioniPassive() > 0) {
                    sbDoc.append(getFASezioneOperazioniPassive(doc, currentFA));
                }

                currentFA++;
            }

            // se il buffer non contiene solo i caratteri dell'header lo aggiungo e inserico filler e footer
            if (sbDoc.length() != 89) {
                sbQuadro.append(sbDoc);
                sbQuadro.append(StringUtils.repeat(" ", 1889 - sbDoc.length()));
                sbQuadro.append(sbFooter.toString());
            }
        }

        return sbQuadro.toString();
    }
}
