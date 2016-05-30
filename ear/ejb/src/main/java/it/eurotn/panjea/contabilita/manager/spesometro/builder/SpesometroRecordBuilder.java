package it.eurotn.panjea.contabilita.manager.spesometro.builder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaDati;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaInvio;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
public abstract class SpesometroRecordBuilder {

    public enum QuadroD {
        FE, FR, NE, NR, DF, FN, SE, TU
    }

    public enum QuadroRecordC {
        FA, SA, BL
    }

    protected PanjeaDAO panjeaDAO;

    protected AziendaAnagraficaDTO azienda;

    protected ParametriCreazioneComPolivalente params;
    private SpesometroDataHandler data = new SpesometroDataHandler();

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
    public SpesometroRecordBuilder(final PanjeaDAO panjeaDAO, final AziendaAnagraficaDTO azienda,
            final ParametriCreazioneComPolivalente params) {
        super();

        this.panjeaDAO = panjeaDAO;
        this.azienda = azienda;
        this.params = params;
    }

    /**
     * Restituisce la lista di documenti che saranno presenti nello spesometro.
     *
     * @return documenti
     */
    public abstract List<DocumentoSpesometro> getDocumenti();

    /**
     * Restituisce il valore del record dell'intermediario.
     *
     * @return record
     */
    private String getIntermediarioRecord() {
        StringBuilder sb = new StringBuilder();
        if (params.isIntermediarioPresente()) {
            sb.append(StringUtils.rightPad(params.getIntermediario().getCodiceFiscale(), 16, " "));
            String numeroIscrizioneCAF = StringUtils.defaultString(params.getIntermediario().getNumeroIscrizioneCAF());
            sb.append(StringUtils.leftPad(numeroIscrizioneCAF.trim(), 5, "0"));
            sb.append(params.getIntermediario().isImpegnoATrasmettere() ? "2" : "1");
            sb.append(" ");
            sb.append(data.getDTP(params.getIntermediario().getDataImpegno()));
        } else {
            sb.append(data.getFiller(" ", 16));
            sb.append(data.getFiller("0", 5));
            sb.append(data.getFiller("0", 1));
            sb.append(data.getFiller(" ", 1));
            sb.append(data.getFiller("0", 8));
        }

        return sb.toString();
    }

    /**
     * Restituisce il valore del record della persona fisica dell'azienda.
     *
     * @return record
     */
    public String getPersonaFisicaAziendaRecord() {
        String pfCognome = data.getFiller(" ", 24);
        String pfNome = data.getFiller(" ", 20);
        String pfSesso = " ";
        String pfDataNascita = data.getDTP(null);
        String pfComune = data.getFiller(" ", 40);
        String pfProvincia = data.getFiller(" ", 2);

        if (azienda.getAzienda().getPersonaFisica().getNome() != null
                && !azienda.getAzienda().getPersonaFisica().getNome().isEmpty()) {
            pfCognome = StringUtils.rightPad(azienda.getAzienda().getPersonaFisica().getCognome(), 24, " ");
            pfNome = StringUtils.rightPad(azienda.getAzienda().getPersonaFisica().getNome(), 20, " ");
            pfSesso = azienda.getAzienda().getPersonaFisica().getSessoStringFormat();
            pfDataNascita = data.getDTP(azienda.getAzienda().getPersonaFisica().getDataNascita());
            if ("IT".equals(
                    azienda.getAzienda().getPersonaFisica().getDatiGeograficiNascita().getNazione().getCodice())) {
                pfComune = StringUtils.rightPad(azienda.getAzienda().getPersonaFisica().getDatiGeograficiNascita()
                        .getDescrizioneLivelloAmministrativo3(), 40, " ");
                pfProvincia = StringUtils.rightPad(
                        azienda.getAzienda().getPersonaFisica().getDatiGeograficiNascita().getSiglaProvincia(), 2, " ");
            } else {
                pfComune = azienda.getAzienda().getPersonaFisica().getDatiGeograficiNascita().getNazione()
                        .getDescrizione();
                pfProvincia = "EE";
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(pfCognome);
        sb.append(pfNome);
        sb.append(pfSesso);
        sb.append(pfDataNascita);
        sb.append(pfComune);
        sb.append(pfProvincia);

        return sb.toString();
    }

    /**
     * Restituisce il valore del record della persona giuridica dell'azienda.
     *
     * @return record
     */
    public String getPersonaGiuridicaAziendaRecord() {

        String pgDenominazione = StringUtils.repeat(" ", 60);

        if (azienda.getAzienda().getPersonaFisica().getNome() == null
                || azienda.getAzienda().getPersonaFisica().getNome().isEmpty()) {
            pgDenominazione = StringUtils.rightPad(azienda.getAzienda().getDenominazione(), 60, " ");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(pgDenominazione);

        return sb.toString();
    }

    /**
     * Restituisce il record A.
     *
     * @return record A
     */
    public String getRecordA() {

        StringBuilder sb = new StringBuilder();
        sb.append("A");
        sb.append(data.getFiller(" ", 14));
        sb.append("NSP00");
        if (params.isIntermediarioPresente()) {
            sb.append("10");
            sb.append(StringUtils.rightPad(params.getIntermediario().getCodiceFiscale(), 16, " "));
        } else {
            sb.append("01");
            sb.append(StringUtils.rightPad(azienda.getAzienda().getCodiceFiscale(), 16, " "));
        }

        sb.append(data.getFiller(" ", 483));
        sb.append(data.getFiller("0", 4));
        sb.append(data.getFiller("0", 4));
        sb.append(data.getFiller(" ", 100));
        sb.append(data.getFiller(" ", 1068));
        sb.append(data.getFiller(" ", 200));
        sb.append("A");
        sb.append("\r\n");

        return sb.toString();
    }

    /**
     * Restituisce il record B.
     *
     * @return record B
     */
    public String getRecordB() {
        StringBuilder sb = new StringBuilder();
        sb.append("B");
        sb.append(StringUtils.rightPad(azienda.getAzienda().getCodiceFiscale(), 16, " "));
        sb.append(StringUtils.leftPad("1", 8, "0"));
        sb.append(StringUtils.repeat(" ", 3));
        sb.append(StringUtils.repeat(" ", 25));
        sb.append(StringUtils.repeat(" ", 20));
        sb.append(StringUtils.rightPad("01398480226", 16, " "));

        TipologiaInvio tipologiaInvio = params.getTipologiaInvio();
        sb.append(TipologiaInvio.INVIO_ORDINARIO == tipologiaInvio ? "1" : "0");
        sb.append(TipologiaInvio.INVIO_SOSTITUIVO == tipologiaInvio ? "1" : "0");
        sb.append(TipologiaInvio.ANNULLAMENTO == tipologiaInvio ? "1" : "0");

        if (tipologiaInvio != TipologiaInvio.INVIO_ORDINARIO) {
            sb.append(StringUtils.leftPad(params.getProtocolloComunicazione().toString(), 17, "0"));
            sb.append(StringUtils.leftPad(params.getProtocolloDocumento().toString(), 6, "0"));
        } else {
            sb.append(StringUtils.repeat("0", 17));
            sb.append(StringUtils.repeat("0", 6));
        }

        sb.append(processTipologiaInvio(tipologiaInvio));

        sb.append(StringUtils.rightPad(azienda.getAzienda().getPartitaIVA(), 11, " "));
        sb.append(StringUtils.rightPad(azienda.getAzienda().getCodiceAttivitaPrevalente(), 6, " "));

        sb.append(
                StringUtils.rightPad(azienda.getSedeAzienda().getSede().getTelefono().replaceAll("\\D+", ""), 12, " "));
        sb.append(StringUtils.rightPad(azienda.getSedeAzienda().getSede().getFax().replaceAll("\\D+", ""), 12, " "));
        sb.append(StringUtils.rightPad(azienda.getSedeAzienda().getSede().getIndirizzoMail(), 50, " "));

        sb.append(getPersonaFisicaAziendaRecord());
        sb.append(getPersonaGiuridicaAziendaRecord());

        sb.append(params.getAnnoRiferimento());
        sb.append(StringUtils.repeat(" ", 2));

        sb.append(StringUtils.repeat(" ", 16));
        sb.append(StringUtils.repeat("0", 2));
        sb.append(StringUtils.repeat("0", 8));
        sb.append(StringUtils.repeat("0", 8));

        sb.append(StringUtils.repeat(" ", 24));
        sb.append(StringUtils.repeat(" ", 20));
        sb.append(StringUtils.repeat(" ", 1));
        sb.append(StringUtils.repeat("0", 8));
        sb.append(StringUtils.repeat(" ", 40));
        sb.append(StringUtils.repeat(" ", 2));

        sb.append(StringUtils.repeat(" ", 60));

        sb.append(getIntermediarioRecord());

        sb.append(StringUtils.repeat(" ", 1258));
        sb.append(StringUtils.repeat(" ", 20));
        sb.append(StringUtils.repeat(" ", 18));

        sb.append("A");
        sb.append("\r\n");

        return sb.toString();
    }

    /**
     * Restituisce il record C.
     *
     * @return record C
     */
    public abstract String getRecordC();

    /**
     * Restituisce il record D.
     *
     * @return record D
     */
    public abstract String getRecordD();

    /**
     * Restituisce il record E.
     *
     * @return record E
     */
    public abstract String getRecordE();

    /**
     * Restituisce il record Z.
     *
     * @return record Z
     */
    public abstract String getRecordZ();

    private String processTipologiaInvio(TipologiaInvio tipologiaInvio) {
        StringBuilder sb = new StringBuilder();

        if (tipologiaInvio != TipologiaInvio.ANNULLAMENTO) {
            sb.append(params.getTipologiaDati() == TipologiaDati.AGGREGATI ? "1" : "0");
            sb.append(params.getTipologiaDati() == TipologiaDati.ANALITICI ? "1" : "0");
            sb.append(quadroFAPresente());
            sb.append(quadroSAPresente());
            sb.append(quadroBLPresente());
            sb.append(quadroFEPresente());
            sb.append(quadroFRPresente());
            sb.append(quadroNEPresente());
            sb.append(quadroNRPresente());
            sb.append(quadroDFPresente());
            sb.append(quadroFNPresente());
            sb.append(quadroSEPresente());
            sb.append(quadroTUPresente());
            sb.append(quadroTAPresente());
        } else {
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
            sb.append("0");
        }

        return sb.toString();
    }

    /**
     * @return <code>1</code> se il quadro BL è presente, <code>0</code> altrimenti
     */
    protected String quadroBLPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro DF è presente, <code>0</code> altrimenti
     */
    protected String quadroDFPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro FA è presente, <code>0</code> altrimenti
     */
    protected String quadroFAPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro FE è presente, <code>0</code> altrimenti
     */
    protected String quadroFEPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro FN è presente, <code>0</code> altrimenti
     */
    protected String quadroFNPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro FR è presente, <code>0</code> altrimenti
     */
    protected String quadroFRPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro NE è presente, <code>0</code> altrimenti
     */
    protected String quadroNEPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro NR è presente, <code>0</code> altrimenti
     */
    protected String quadroNRPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro SA è presente, <code>0</code> altrimenti
     */
    protected String quadroSAPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro SE è presente, <code>0</code> altrimenti
     */
    protected String quadroSEPresente() {
        return "0";
    }

    /**
     * @return <code>1</code> se il quadro tA è presente, <code>0</code> altrimenti
     */
    protected String quadroTAPresente() {
        return "1";
    }

    /**
     * @return <code>1</code> se il quadro TU è presente, <code>0</code> altrimenti
     */
    protected String quadroTUPresente() {
        return "0";
    }

}
