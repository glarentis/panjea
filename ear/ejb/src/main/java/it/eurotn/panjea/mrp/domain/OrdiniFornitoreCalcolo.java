package it.eurotn.panjea.mrp.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class OrdiniFornitoreCalcolo implements Serializable {

    private class RigaCollegataRev implements Comparable<RigaCollegataRev> {
        private Integer rev;
        private String idRiga;

        /**
         *
         * @param idRiga
         *            idriga
         * @param revisione
         *            revisione associata
         */
        public RigaCollegataRev(String idRiga, String revisione) {
        	rev=0;
            if(!StringUtils.isEmpty(revisione)){
        	rev = Integer.parseInt(revisione);
            }
            this.idRiga = idRiga;
        }
        
        @Override
        public int compareTo(RigaCollegataRev riga2) {
            return rev.compareTo(riga2.getRev());
        }

        public String getIdRiga() {
            return idRiga;
        }

        public Integer getRev() {
            return rev;
        }
    }

    private static final long serialVersionUID = -120630454007601689L;

    private int idDeposito;

    private int idRigaOrdineFornitore;
    private String ordiniProduzioneComponenti;

    private String ordiniProduzioneDistinte;

    private String ordiniClienti;

    private int idArticolo;

    private double qta;

    private Date dataConsegna;

    private double qtaConsumata;

    private Integer diffData;

    private String ordiniProduzioneComponentiREV;

    private String ordiniProduzioneDistinteREV;

    private String ordiniClientiREV;

    public void addQtaConsumata(double qtaConsumata) {
        this.qtaConsumata += qtaConsumata;
    }

    /**
     * @return Returns the dataConsegna.
     */
    public Date getDataConsegna() {
        return dataConsegna;
    }

    public Integer getDiffData() {
        return diffData;
    }

    /**
     * @return Returns the idArticolo.
     */
    public int getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return Returns the idDeposito.
     */
    public int getIdDeposito() {
        return idDeposito;
    }

    /**
     * @return Returns the idRigaOrdineFornitore.
     */
    public int getIdRigaOrdineFornitore() {
        return idRigaOrdineFornitore;
    }

    /**
     * @return Returns the ordiniClienti.
     */
    public String getOrdiniClienti() {
        return ordiniClienti;
    }

    public String getOrdiniClientiREV() {
        return ordiniClientiREV;
    }

    public String getOrdiniProduzioneComponenti() {
        return ordiniProduzioneComponenti;
    }

    public String getOrdiniProduzioneComponentiREV() {
        return ordiniProduzioneComponentiREV;
    }

    public String getOrdiniProduzioneDistinte() {
        return ordiniProduzioneDistinte;
    }

    public String getOrdiniProduzioneDistinteREV() {
        return ordiniProduzioneDistinteREV;
    }

    /**
     * @return Returns the qta.
     */
    public double getQta() {
        return qta;
    }

    public double getQtaConsumata() {
        return qtaConsumata;
    }

    public double getQtaRimanente() {
        return qta - qtaConsumata;
    }

    public int[] getRigheOrdiniCollegate() {
        List<RigaCollegataRev> righeCollegate = new ArrayList<>();
        if (getOrdiniProduzioneComponenti() != null) {
            String[] ordiniToken = getOrdiniProduzioneComponenti().split(",");
            String[] revCollegate = getOrdiniProduzioneComponentiREV().split(",");
            for (int i = 0; i < revCollegate.length; i++) {
                righeCollegate.add(new RigaCollegataRev(ordiniToken[i], revCollegate[i]));
            }
        }
        if (getOrdiniProduzioneDistinte() != null) {
            String[] ordiniToken = getOrdiniProduzioneDistinte().split(",");
            String[] revCollegate = getOrdiniProduzioneDistinteREV().split(",");
            for (int i = 0; i < revCollegate.length; i++) {
                righeCollegate.add(new RigaCollegataRev(ordiniToken[i], revCollegate[i]));
            }
        }
        if (getOrdiniClienti() != null) {
            String[] ordiniToken = getOrdiniClienti().split(",");
            String[] revCollegate = ((String) ObjectUtils.defaultIfNull(getOrdiniClientiREV(), ""))
                    .split(",");
            for (int i = 0; i < revCollegate.length; i++) {
                righeCollegate.add(new RigaCollegataRev(ordiniToken[i], revCollegate[i]));
            }
        }
        Collections.sort(righeCollegate);
        int[] result = new int[righeCollegate.size()];
        for (int i = 0; i < righeCollegate.size(); i++) {
            result[i] = Integer.parseInt(righeCollegate.get(i).getIdRiga());
        }
        return result;
    }

    /**
     * @param dataConsegna
     *            The dataConsegna to set.
     */
    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    public void setDiffData(Integer diffData) {
        this.diffData = diffData;
    }

    /**
     * @param idArticolo
     *            The idArticolo to set.
     */
    public void setIdArticolo(int idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idDeposito
     *            The idDeposito to set.
     */
    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }

    /**
     * @param idRigaOrdineFornitore
     *            The idRigaOrdineFornitore to set.
     */
    public void setIdRigaOrdineFornitore(int idRigaOrdineFornitore) {
        this.idRigaOrdineFornitore = idRigaOrdineFornitore;
    }

    /**
     * @param ordiniClienti
     *            The ordiniClienti to set.
     */
    public void setOrdiniClienti(String ordiniClienti) {
        this.ordiniClienti = ordiniClienti;
    }

    public void setOrdiniClientiREV(String ordiniClientiREV) {
        this.ordiniClientiREV = ordiniClientiREV;
    }

    public void setOrdiniProduzioneComponenti(String ordiniProduzioneComponenti) {
        this.ordiniProduzioneComponenti = ordiniProduzioneComponenti;
    }

    public void setOrdiniProduzioneComponentiREV(String ordiniProduzioneComponentiREV) {
        this.ordiniProduzioneComponentiREV = ordiniProduzioneComponentiREV;
    }

    public void setOrdiniProduzioneDistinte(String ordiniProduzioneDistinte) {
        this.ordiniProduzioneDistinte = ordiniProduzioneDistinte;
    }

    public void setOrdiniProduzioneDistinteREV(String ordiniProduzioneDistinteREV) {
        this.ordiniProduzioneDistinteREV = ordiniProduzioneDistinteREV;
    }

    /**
     * @param qta
     *            The qta to set.
     */
    public void setQta(double qta) {
        this.qta = qta;
    }

    @Override
    public String toString() {
        return "OrdiniFornitoreCalcolo [idDeposito=" + idDeposito + ", idRigaOrdineFornitore="
                + idRigaOrdineFornitore + ", ordiniProduzioneComponenti="
                + ordiniProduzioneComponenti + ", ordiniProduzioneDistinte="
                + ordiniProduzioneDistinte + ", ordiniClienti=" + ordiniClienti + ", idArticolo="
                + idArticolo + ", qta=" + qta + ", dataConsegna=" + dataConsegna + "]";
    }

}
