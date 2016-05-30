package it.eurotn.panjea.contabilita.manager.spesometro.builder.analitico;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroDataHandler;
import it.eurotn.panjea.contabilita.manager.spesometro.builder.SpesometroRecordBuilder;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaInvio;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
public class SpesometroAnaliticoBuilder extends SpesometroRecordBuilder {

    private Integer progressivo = new Integer(0);

    private SpesometroDataHandler data = new SpesometroDataHandler();

    private SpesometroAnaliticoManager spesometroManager;
    private SpesometroAnaliticoProcessor spesometroProcessor = new SpesometroAnaliticoProcessor();

    /**
     * Costruttore.
     *
     * @param panjeaDAO
     *            panjeaDAO
     * @param azienda
     *            azienda di riferimento
     * @param params
     *            parametri per la crazione dello spesometro
     */
    public SpesometroAnaliticoBuilder(final PanjeaDAO panjeaDAO, final AziendaAnagraficaDTO azienda,
            final ParametriCreazioneComPolivalente params) {
        super(panjeaDAO, azienda, params);
        this.spesometroManager = new SpesometroAnaliticoManager(panjeaDAO, params);

        spesometroManager.caricaDocumenti();
    }

    @Override
    public List<DocumentoSpesometro> getDocumenti() {
        return spesometroManager.getAllDocumenti();
    }

    @Override
    public String getRecordC() {
        // Non serve per lo spesometro analitico
        return null;
    }

    @Override
    public String getRecordD() {

        // se annullamento il tipo record non serve
        if (params.getTipologiaInvio() == TipologiaInvio.ANNULLAMENTO) {
            return null;
        }

        StringBuilder sbFooter = new StringBuilder();
        sbFooter.append(StringUtils.repeat(" ", 8));
        sbFooter.append("A");
        sbFooter.append("\r\n");

        StringBuilder sb = new StringBuilder();

        Map<Integer, List<DocumentoSpesometro>> mapDocumenti = spesometroManager.getDocumentiByModuloRecordD();
        for (int idx = 1; idx <= mapDocumenti.size(); idx++) {
            progressivo = idx;

            int progrFE = 0;
            int progrFR = 0;
            int progrNE = 0;
            int progrNR = 0;
            int progrFN = 0;
            int progrSE = 0;

            StringBuilder sbQuadro = new StringBuilder(1900);
            sbQuadro.append(spesometroProcessor.getHeaderRecordD(progressivo, azienda.getAzienda().getCodiceFiscale()));

            for (DocumentoSpesometro doc : mapDocumenti.get(idx)) {
                String[] record = null;
                switch (doc.getTipologiaDocumento()) {
                case FATTURA_ATTIVA:
                    progrFE++;
                    record = spesometroProcessor.getQuadroFE(doc, progrFE, azienda.getAzienda().getCodiceFiscale());
                    break;
                case FATTURA_PASSIVA:
                    progrFR++;
                    record = spesometroProcessor.getQuadroFR(doc, progrFR, azienda.getAzienda().getCodiceFiscale());
                    break;
                case NOTA_CREDITO_ATTIVA:
                    progrNE++;
                    record = spesometroProcessor.getQuadroNE(doc, progrNE);
                    break;
                case NOTA_CREDITO_PASSIVA:
                    progrNR++;
                    record = spesometroProcessor.getQuadroNR(doc, progrNR);
                    break;
                case FATTURA_ATTIVA_NON_RESIDENTI:
                    progrFN++;
                    record = spesometroProcessor.getQuadroFN(doc, progrFN);
                    break;
                case FATTURA_PASSIVA_SERVIZI_NON_RESIDENTI:
                    progrSE++;
                    record = spesometroProcessor.getQuadroSE(doc, progrSE);
                    break;
                default:
                    // tipologia documento non gestita
                    record = new String[] {};
                    break;
                }

                for (int i = 0; i < record.length; i++) {
                    String dupla = record[i];
                    sbQuadro.append(dupla);

                    if (sbQuadro.length() == 1889) {
                        sb.append(sbQuadro.toString());
                        sb.append(sbFooter.toString());

                        sbQuadro = new StringBuilder(1900);
                        sbQuadro.append(spesometroProcessor.getHeaderRecordD(progressivo,
                                azienda.getAzienda().getCodiceFiscale()));
                    }
                }
            }

            if (sbQuadro.length() != 89 && sbQuadro.length() < 1889) {
                sbQuadro.append(StringUtils.repeat(" ", 1889 - sbQuadro.length()));
                sbQuadro.append(sbFooter.toString());
                sb.append(sbQuadro.toString());

                sbQuadro = new StringBuilder(1900);
                sbQuadro.append(
                        spesometroProcessor.getHeaderRecordD(progressivo, azienda.getAzienda().getCodiceFiscale()));

                progrFE = 0;
                progrFR = 0;
                progrNE = 0;
                progrNR = 0;
            }

        }

        return sb.toString();
    }

    @Override
    public String getRecordE() {

        // se annullamento il tipo record non serve
        if (params.getTipologiaInvio() == TipologiaInvio.ANNULLAMENTO) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("E");
        sb.append(StringUtils.rightPad(azienda.getAzienda().getCodiceFiscale(), 16, " "));
        sb.append(StringUtils.leftPad("1", 8, "0"));
        sb.append(StringUtils.repeat(" ", 3));
        sb.append(StringUtils.repeat(" ", 25));
        sb.append(StringUtils.repeat(" ", 20));
        sb.append(StringUtils.rightPad("01398480226", 16, " "));

        int numeroFE = spesometroManager.getNumeroDocumenti(QuadroD.FE, false);
        if (numeroFE > 0) {
            sb.append("TA004001");
            sb.append(data.getNPNP(new BigDecimal(numeroFE)));
        }
        int numeroFERiep = spesometroManager.getNumeroDocumenti(QuadroD.FE, true);
        if (numeroFERiep > 0) {
            sb.append("TA004002");
            sb.append(data.getNPNP(new BigDecimal(numeroFERiep)));
        }
        int numeroFR = spesometroManager.getNumeroDocumenti(QuadroD.FR, false);
        if (numeroFR > 0) {
            sb.append("TA005001");
            sb.append(data.getNPNP(new BigDecimal(numeroFR)));
        }
        int numeroFRRiep = spesometroManager.getNumeroDocumenti(QuadroD.FR, true);
        if (numeroFRRiep > 0) {
            sb.append("TA005002");
            sb.append(data.getNPNP(new BigDecimal(numeroFRRiep)));
        }
        if (!spesometroManager.getDocumentoQuadro(QuadroD.NE).isEmpty()) {
            sb.append("TA006001");
            sb.append(data.getNPNP(new BigDecimal(spesometroManager.getDocumentoQuadro(QuadroD.NE).size())));
        }
        if (!spesometroManager.getDocumentoQuadro(QuadroD.NR).isEmpty()) {
            sb.append("TA007001");
            sb.append(data.getNPNP(new BigDecimal(spesometroManager.getDocumentoQuadro(QuadroD.NR).size())));
        }
        if (!spesometroManager.getDocumentoQuadro(QuadroD.FN).isEmpty()) {
            sb.append("TA009001");
            sb.append(data.getNPNP(new BigDecimal(spesometroManager.getDocumentoQuadro(QuadroD.FN).size())));
        }
        if (!spesometroManager.getDocumentoQuadro(QuadroD.SE).isEmpty()) {
            sb.append("TA010001");
            sb.append(data.getNPNP(new BigDecimal(spesometroManager.getDocumentoQuadro(QuadroD.SE).size())));
        }
        sb.append(StringUtils.repeat(" ", 1889 - sb.length()));

        sb.append(StringUtils.repeat(" ", 8));
        sb.append("A");
        sb.append("\r\n");

        return sb.toString();
    }

    @Override
    public String getRecordZ() {
        StringBuilder sb = new StringBuilder();

        sb.append("Z");
        sb.append(StringUtils.repeat(" ", 14));

        sb.append(StringUtils.leftPad("1", 9, "0")); // B
        if (params.getTipologiaInvio() != TipologiaInvio.ANNULLAMENTO) {
            sb.append(StringUtils.leftPad("0", 9, "0"));
            sb.append(StringUtils.leftPad(progressivo.toString(), 9, "0"));
            sb.append(StringUtils.leftPad("1", 9, "0"));
        } else {
            sb.append(StringUtils.leftPad("0", 9, "0"));
            sb.append(StringUtils.leftPad("0", 9, "0"));
            sb.append(StringUtils.leftPad("0", 9, "0"));
        }

        sb.append(StringUtils.repeat(" ", 1846));

        sb.append("A");
        sb.append("\r\n");

        return sb.toString();
    }

    @Override
    protected String quadroFEPresente() {
        return !spesometroManager.getDocumentoQuadro(QuadroD.FE).isEmpty() ? "1" : "0";
    }

    @Override
    protected String quadroFNPresente() {
        return !spesometroManager.getDocumentoQuadro(QuadroD.FN).isEmpty() ? "1" : "0";
    }

    @Override
    protected String quadroFRPresente() {
        return !spesometroManager.getDocumentoQuadro(QuadroD.FR).isEmpty() ? "1" : "0";
    }

    @Override
    protected String quadroNEPresente() {
        return !spesometroManager.getDocumentoQuadro(QuadroD.NE).isEmpty() ? "1" : "0";
    }

    @Override
    protected String quadroNRPresente() {
        return !spesometroManager.getDocumentoQuadro(QuadroD.NR).isEmpty() ? "1" : "0";
    }

    @Override
    protected String quadroSEPresente() {
        return !spesometroManager.getDocumentoQuadro(QuadroD.SE).isEmpty() ? "1" : "0";
    }

}
