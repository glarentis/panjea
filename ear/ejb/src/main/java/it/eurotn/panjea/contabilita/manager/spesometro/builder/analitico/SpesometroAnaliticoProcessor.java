package it.eurotn.panjea.contabilita.manager.spesometro.builder.analitico;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroDataHandler;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroRecordBuilder.QuadroD;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;

public class SpesometroAnaliticoProcessor {

    private SpesometroDataHandler dataHandler = new SpesometroDataHandler();

    /**
     * Costruttore.
     */
    public SpesometroAnaliticoProcessor() {
        super();
    }

    private List<String> getAnagraficaNonResidenti(QuadroD quadro, int progr, DocumentoSpesometro doc) {

        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder(24);

        String quadroString = quadro.name().toUpperCase();

        // Persona fisica e anagrafica sono mutualmente esclusivi. Se la persona fisica esiste prendo quella
        if (!StringUtils.isBlank(doc.getAnagrafica().getPersonaFisica().getCognome())
                && !StringUtils.isBlank(doc.getAnagrafica().getPersonaFisica().getNome())) {
            sb.append(dataHandler.formatQuadro(quadroString, progr, "001"));
            sb.append(dataHandler.getANNP(doc.getAnagrafica().getPersonaFisica().getCognome()));
            list.add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro(quadroString, progr, "002"));
            sb.append(dataHandler.getANNP(doc.getAnagrafica().getPersonaFisica().getNome()));
            list.add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro(quadroString, progr, "003"));
            sb.append(dataHandler.getDTNP(doc.getAnagrafica().getPersonaFisica().getDataNascita()));
            list.add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro(quadroString, progr, "004"));
            sb.append(dataHandler.getANNP(
                    doc.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().getDescrizioneNazione()));
            list.add(sb.toString());

            sb.delete(0, sb.length());
            // TODO provincia
            list.add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro(quadroString, progr, "006"));
            Integer statoIUC = doc.getAnagrafica().getPersonaFisica().getDatiGeograficiNascita().getNazione()
                    .getCodiceNazioneUIC();
            String statoIUCString = statoIUC == null ? "" : StringUtils.leftPad(String.valueOf(statoIUC), 3, '0');
            sb.append(StringUtils.leftPad(statoIUCString, 16, " "));
            list.add(sb.toString());
        } else {
            sb.append(dataHandler.formatQuadro(quadroString, progr, "007"));
            sb.append(dataHandler.getANNP(doc.getAnagrafica().getDenominazione()));
            list.add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro(quadroString, progr, "008"));
            sb.append(dataHandler
                    .getANNP(doc.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getDescrizioneLocalita()));
            list.add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro(quadroString, progr, "009"));
            Integer statoIUC = doc.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getNazione()
                    .getCodiceNazioneUIC();
            String statoIUCString = statoIUC == null ? "" : StringUtils.leftPad(String.valueOf(statoIUC), 3, '0');
            sb.append(StringUtils.leftPad(statoIUCString, 16, " "));
            list.add(sb.toString());

            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro(quadroString, progr, "010"));
            sb.append(dataHandler.getANNP(doc.getAnagrafica().getSedeAnagrafica().getIndirizzo()));
            list.add(sb.toString());
        }

        return list;
    }

    /**
     * Restituisce l'header per il record D dei campi posizionali.
     *
     * @param progressivoParam
     *            progressivo
     * @param codiceFiscaleAzienda
     *            codice fiscale dell'azienda
     * @return header
     */
    public String getHeaderRecordD(Integer progressivoParam, String codiceFiscaleAzienda) {
        StringBuilder sbHeader = new StringBuilder();
        sbHeader.append("D");
        sbHeader.append(StringUtils.rightPad(codiceFiscaleAzienda, 16, " "));
        sbHeader.append(StringUtils.leftPad(progressivoParam.toString(), 8, "0"));
        sbHeader.append(StringUtils.repeat(" ", 3));
        sbHeader.append(StringUtils.repeat(" ", 25));
        sbHeader.append(StringUtils.repeat(" ", 20));
        sbHeader.append(StringUtils.rightPad("01398480226", 16, " "));

        return sbHeader.toString();
    }

    /**
     * Restituisce il quadro FE per il documento indicato.
     *
     * @param doc
     *            documento
     * @param progr
     *            progressivo
     * @param codiceFiscaleAzienda
     *            codice fiscale dell'azienda
     * @return record
     */
    public String[] getQuadroFE(DocumentoSpesometro doc, int progr, String codiceFiscaleAzienda) {
        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder(24);

        // p.iva / cf / riep.
        String partitaIva = doc.getEntita().getAnagrafica().getPartiteIVA();
        String codiceFiscale = doc.getEntita().getAnagrafica().getCodiceFiscale();
        if (doc.getEntita().isRiepilogativo()) {
            sb.append(dataHandler.formatQuadro("FE", progr, "003"));
            sb.append(dataHandler.getCBNP(true));
        } else if (!StringUtils.isEmpty(partitaIva)) {
            sb.append(dataHandler.formatQuadro("FE", progr, "001"));
            sb.append(dataHandler.getPINP(partitaIva));
        } else if (!StringUtils.isEmpty(codiceFiscale)) {
            sb.append(dataHandler.formatQuadro("FE", progr, "002"));
            sb.append(dataHandler.getCFNP(codiceFiscale));
        }
        list.add(sb.toString());

        // autofattura
        if (!StringUtils.isBlank(codiceFiscale)
                && StringUtils.defaultString(codiceFiscaleAzienda).equals(codiceFiscale)) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FE", progr, "006"));
            sb.append(dataHandler.getCBNP(true));
            list.add(sb.toString());
        }

        // data registrazione
        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("FE", progr, "008"));
        sb.append(dataHandler.getDTNP(doc.getDataRegistrazione()));
        list.add(sb.toString());

        // numero documento
        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("FE", progr, "009"));
        sb.append(dataHandler.getANNP(doc.getCodiceDocumento().getCodice()));
        list.add(sb.toString());

        // importo
        if (doc.getImponibile().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FE", progr, "010"));
            sb.append(dataHandler.getImportoNPNP(doc.getImponibile()));
            list.add(sb.toString());
        }

        // imposta
        if (doc.getImposta().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FE", progr, "011"));
            sb.append(dataHandler.getImportoNPNP(doc.getImposta()));
            list.add(sb.toString());
        }

        if (doc.getImponibile().compareTo(new BigDecimal(999999)) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FE", progr, "012"));
            sb.append(dataHandler.getCBNP(true));
            list.add(sb.toString());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Restituisce il quadro FN per il documento indicato.
     *
     * @param doc
     *            documento
     * @param progr
     *            progressivo
     * @return record
     */
    public String[] getQuadroFN(DocumentoSpesometro doc, int progr) {
        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder(24);
        list.addAll(getAnagraficaNonResidenti(QuadroD.FN, progr, doc));

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("FN", progr, "012"));
        sb.append(dataHandler.getDTNP(doc.getDataRegistrazione()));
        list.add(sb.toString());

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("FN", progr, "013"));
        sb.append(dataHandler.getANNP(doc.getCodiceDocumento().getCodice()));
        list.add(sb.toString());

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("FN", progr, "015"));
        sb.append(dataHandler.getImportoNPNP(doc.getImponibile()));
        list.add(sb.toString());

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("FN", progr, "016"));
        sb.append(dataHandler.getImportoNPNP(doc.getImposta()));
        list.add(sb.toString());

        if (doc.getImponibile().compareTo(new BigDecimal(999999)) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FN", progr, "017"));
            sb.append(dataHandler.getCBNP(true));
            list.add(sb.toString());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Restituisce il quadro FR per il documento indicato.
     *
     * @param doc
     *            documento
     * @param progr
     *            progressivo
     * @param codiceFiscaleAzienda
     *            codice fiscale dell'azienda
     * @return record
     */
    public String[] getQuadroFR(DocumentoSpesometro doc, int progr, String codiceFiscaleAzienda) {
        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder(24);

        // p.iva / cf / riep.
        String partitaIva = doc.getEntita().getAnagrafica().getPartiteIVA();
        String codiceFiscale = doc.getEntita().getAnagrafica().getCodiceFiscale();
        if (doc.getEntita().isRiepilogativo()) {
            sb.append(dataHandler.formatQuadro("FR", progr, "002"));
            sb.append(dataHandler.getCBNP(doc.getEntita().isRiepilogativo()));
        } else {
            sb.append(dataHandler.formatQuadro("FR", progr, "001"));
            sb.append(dataHandler.getPINP(partitaIva));
        }
        list.add(sb.toString());

        // data registrazione
        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("FR", progr, "004"));
        sb.append(dataHandler.getDTNP(doc.getDataRegistrazione()));
        list.add(sb.toString());

        if (doc.isReverseCharge()) {
            // reverse charge
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FR", progr, "006"));
            sb.append(dataHandler.getCBNP(doc.isReverseCharge()));
            list.add(sb.toString());
        } else {
            if (!doc.getEntita().isRiepilogativo() && !StringUtils.isBlank(codiceFiscale)
                    && StringUtils.defaultString(codiceFiscaleAzienda).equals(codiceFiscale)) {
                // autofattura
                sb.delete(0, sb.length());
                sb.append(dataHandler.formatQuadro("FR", progr, "007"));
                sb.append(dataHandler.getCBNP(true));
                list.add(sb.toString());
            }
        }

        // importo
        if (doc.getImponibile().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FR", progr, "008"));
            sb.append(dataHandler.getImportoNPNP(doc.getImponibile()));
            list.add(sb.toString());
        }

        // imposta
        if (doc.getImposta().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FR", progr, "009"));
            sb.append(dataHandler.getImportoNPNP(doc.getImposta()));
            list.add(sb.toString());
        }

        if (doc.getImponibile().compareTo(new BigDecimal(999999)) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("FR", progr, "012"));
            sb.append(dataHandler.getCBNP(true));
            list.add(sb.toString());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Restituisce il quadro NE per il documento indicato.
     *
     * @param doc
     *            documento
     * @param progr
     *            progressivo
     * @return record
     */
    public String[] getQuadroNE(DocumentoSpesometro doc, int progr) {
        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder(24);

        // p.iva / cf / riep.
        String partitaIva = doc.getEntita().getAnagrafica().getPartiteIVA();
        String codiceFiscale = doc.getEntita().getAnagrafica().getCodiceFiscale();
        if (!StringUtils.isEmpty(partitaIva)) {
            sb.append(dataHandler.formatQuadro("NE", progr, "001"));
            sb.append(dataHandler.getPINP(partitaIva));
        } else if (!StringUtils.isEmpty(codiceFiscale)) {
            sb.append(dataHandler.formatQuadro("NE", progr, "002"));
            sb.append(dataHandler.getCFNP(codiceFiscale));
        }
        list.add(sb.toString());

        // data registrazione
        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("NE", progr, "004"));
        sb.append(dataHandler.getDTNP(doc.getDataRegistrazione()));
        list.add(sb.toString());

        // numero documento
        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("NE", progr, "005"));
        sb.append(dataHandler.getANNP(doc.getCodiceDocumento().getCodice()));
        list.add(sb.toString());

        // importo
        if (doc.getImponibile().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("NE", progr, "006"));
            sb.append(dataHandler.getImportoNPNP(doc.getImponibile()));
            list.add(sb.toString());
        }

        // imposta
        if (doc.getImposta().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("NE", progr, "007"));
            sb.append(dataHandler.getImportoNPNP(doc.getImposta()));
            list.add(sb.toString());
        }

        if (doc.getImponibile().compareTo(new BigDecimal(999999)) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("NE", progr, "008"));
            sb.append(dataHandler.getCBNP(true));
            list.add(sb.toString());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Restituisce il quadro NR per il documento indicato.
     *
     * @param doc
     *            documento
     * @param progr
     *            progressivo
     * @return record
     */
    public String[] getQuadroNR(DocumentoSpesometro doc, int progr) {
        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder(24);

        // p.iva
        String partitaIva = doc.getEntita().getAnagrafica().getPartiteIVA();
        sb.append(dataHandler.formatQuadro("NR", progr, "001"));
        sb.append(dataHandler.getPINP(partitaIva));
        list.add(sb.toString());

        // data registrazione
        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("NR", progr, "003"));
        sb.append(dataHandler.getDTNP(doc.getDataRegistrazione()));
        list.add(sb.toString());

        // importo
        if (doc.getImponibile().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("NR", progr, "004"));
            sb.append(dataHandler.getImportoNPNP(doc.getImponibile()));
            list.add(sb.toString());
        }

        // imposta
        if (doc.getImposta().compareTo(BigDecimal.ZERO) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("NR", progr, "005"));
            sb.append(dataHandler.getImportoNPNP(doc.getImposta()));
            list.add(sb.toString());
        }

        if (doc.getImponibile().compareTo(new BigDecimal(999999)) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("NR", progr, "006"));
            sb.append(dataHandler.getCBNP(true));
            list.add(sb.toString());
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Restituisce il quadro SE per il documento indicato.
     *
     * @param doc
     *            documento
     * @param progr
     *            progressivo
     * @return record
     */
    public String[] getQuadroSE(DocumentoSpesometro doc, int progr) {
        List<String> list = new ArrayList<String>();

        StringBuilder sb = new StringBuilder(24);
        list.addAll(getAnagraficaNonResidenti(QuadroD.SE, progr, doc));

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("SE", progr, "013"));
        sb.append(dataHandler.getDTNP(doc.getDataRegistrazione()));
        list.add(sb.toString());

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("SE", progr, "014"));
        sb.append(dataHandler.getANNP(doc.getCodiceDocumento().getCodice()));
        list.add(sb.toString());

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("SE", progr, "015"));
        sb.append(dataHandler.getImportoNPNP(doc.getImponibile()));
        list.add(sb.toString());

        sb.delete(0, sb.length());
        sb.append(dataHandler.formatQuadro("SE", progr, "016"));
        sb.append(dataHandler.getImportoNPNP(doc.getImposta()));
        list.add(sb.toString());

        if (doc.getImponibile().compareTo(new BigDecimal(999999)) > 0) {
            sb.delete(0, sb.length());
            sb.append(dataHandler.formatQuadro("SE", progr, "017"));
            sb.append(dataHandler.getCBNP(true));
            list.add(sb.toString());
        }

        return list.toArray(new String[list.size()]);
    }

}
