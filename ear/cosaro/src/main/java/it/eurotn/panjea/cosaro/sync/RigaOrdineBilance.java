package it.eurotn.panjea.cosaro.sync;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.cosaro.CosaroSettingsBean;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;

public class RigaOrdineBilance {

    private String chiave;
    private int numeroriga;
    private Date dataRegistrazione;
    private Date dataConsegna;
    private Double qtaDaEvadere;
    private String um;
    private String operazione;
    private String codArticolo;
    private String descrizioneArticolo;
    private String pezzi;
    private String barCode;
    private String barCodeCartone;
    private String pesoMinimo;
    private String pesoMassimo;
    private String taraProdotto;
    private String taraCartone;
    private String codCliente;
    private String ragSociale;
    private String indirizzo;
    private String citta;
    private String cap;
    private String provincia;
    private String nazione;
    private String codSede;
    private String denominazioneSede;
    private String indirizzo2;
    private String citta2;
    private String cap2;
    private String provincia2;
    private String nazione2;
    private String codVettore;
    private String denominazionevettore;
    private String prezzatura;

    /**
     *
     * Costruttore.
     *
     * @param rigaDistintaCarico
     *            rigaDistinta da trasformare
     * @param articolo
     *            articolo con gli attributi caricati.
     * @param um
     *            unità di misura dipendente dall'attributo.
     */
    public RigaOrdineBilance(final RigaDistintaCarico rigaDistintaCarico, final Articolo articolo, final String um) {

        AreaOrdine areaOrdine = rigaDistintaCarico.getRigaArticolo().getAreaOrdine();

        // Costruisco la chiave
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("yy").format(areaOrdine.getDataRegistrazione()));
        sb.append(areaOrdine.getTipoAreaOrdine().getTipoDocumento().getCodice().substring(0, 2));
        sb.append(StringUtils.leftPad(areaOrdine.getDocumento().getCodice().getCodice(), 6, "0"));
        chiave = sb.toString();

        this.um = um;

        numeroriga = rigaDistintaCarico.getRigaArticolo().getNumeroRiga();

        prezzatura = rigaDistintaCarico.getRigaArticolo().getNoteRiga();
        dataRegistrazione = areaOrdine.getDataRegistrazione();
        dataConsegna = rigaDistintaCarico.getDataConsegna();
        codArticolo = articolo.getCodice();
        descrizioneArticolo = articolo.getDescrizione();
        pezzi = getValoreAttributo(articolo, CosaroSettingsBean.PEZZI_CARTONE_ATTRIBUTO);
        barCode = "0000000000000";
        if (articolo.getBarCode() != null && articolo.getBarCode().length() == 13) {
            barCode = articolo.getBarCode();
        }

        pesoMinimo = getValoreAttributo(articolo, CosaroSettingsBean.PESO_MIN_ATTRIBUTO);
        pesoMassimo = getValoreAttributo(articolo, CosaroSettingsBean.PESO_MAX_ATTRIBUTO);
        taraCartone = getValoreAttributo(articolo, CosaroSettingsBean.TARA_CARTONE_ATTRIBUTO);
        taraProdotto = getValoreAttributo(articolo, CosaroSettingsBean.TARA_PRODOTTO_ATTRIBUTO);

        barCodeCartone = getValoreAttributo(articolo, CosaroSettingsBean.BAR_CODE_CARTONE);
        barCodeCartone = barCodeCartone != null ? barCodeCartone : "0000000000000";

        EntitaLite entita = areaOrdine.getDocumento().getEntita();
        codCliente = entita.getCodice().toString();
        ragSociale = entita.getAnagrafica().getDenominazione();
        indirizzo = entita.getAnagrafica().getSedeAnagrafica().getIndirizzo();
        DatiGeografici datiGeografici = entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici();
        if (datiGeografici != null) {
            citta = datiGeografici.getLocalita() == null ? "" : datiGeografici.getLocalita().getDescrizione();
            cap = datiGeografici.getCap() == null ? "" : datiGeografici.getCap().getDescrizione();
            provincia = datiGeografici.getLivelloAmministrativo2() == null ? ""
                    : datiGeografici.getLivelloAmministrativo2().getSigla();
            nazione = datiGeografici.getNazione() == null ? "" : datiGeografici.getNazione().getDescrizione();
        }

        if (!areaOrdine.getDocumento().getSedeEntita().getSede().equals(entita.getAnagrafica().getSedeAnagrafica())) {
            SedeAnagrafica sedeDestinazione = areaOrdine.getDocumento().getSedeEntita().getSede();
            denominazioneSede = sedeDestinazione.getDescrizione();
            codSede = areaOrdine.getDocumento().getSedeEntita().getCodice();
            indirizzo2 = sedeDestinazione.getIndirizzo();
            datiGeografici = sedeDestinazione.getDatiGeografici();
            if (datiGeografici != null) {
                citta2 = datiGeografici.getLocalita() == null ? "" : datiGeografici.getLocalita().getDescrizione();
                cap2 = datiGeografici.getCap() == null ? "" : datiGeografici.getCap().getDescrizione();
                provincia2 = datiGeografici.getLivelloAmministrativo2() == null ? ""
                        : datiGeografici.getLivelloAmministrativo2().getNome();
                nazione2 = datiGeografici.getNazione() == null ? "" : datiGeografici.getNazione().getCodice();
            }
        }

        if (areaOrdine.getVettore() != null) {
            denominazionevettore = areaOrdine.getVettore().getAnagrafica().getDenominazione();
            codVettore = areaOrdine.getVettore().getAnagrafica().getDenominazione();
        }

    }

    /**
     * @return Returns the barCode.
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @return Returns the barCodeCartone.
     */
    public String getBarCodeCartone() {
        return barCodeCartone;
    }

    /**
     * @return Returns the cap.
     */
    public String getCap() {
        return cap;
    }

    /**
     * @return Returns the cap2.
     */
    public String getCap2() {
        return cap2;
    }

    /**
     * @return Returns the chiave.
     */
    public String getChiave() {
        return chiave;
    }

    /**
     * @return Returns the citta.
     */
    public String getCitta() {
        return citta;
    }

    /**
     * @return Returns the citta2.
     */
    public String getCitta2() {
        return citta2;
    }

    /**
     * @return Returns the codArticolo.
     */
    public String getCodArticolo() {
        return codArticolo;
    }

    /**
     * @return Returns the codCliente.
     */
    public String getCodCliente() {
        return codCliente;
    }

    /**
     * @return Returns the codSede.
     */
    public String getCodSede() {
        return codSede;
    }

    /**
     * @return Returns the codVettore.
     */
    public String getCodVettore() {
        return codVettore;
    }

    /**
     * @return Returns the dataConsegna.
     */
    public Date getDataConsegna() {
        return dataConsegna;
    }

    /**
     * @return Returns the dataRegistrazione.
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return Returns the denominazioneSede.
     */
    public String getDenominazioneSede() {
        return denominazioneSede;
    }

    /**
     * @return Returns the denominazionevettore.
     */
    public String getDenominazionevettore() {
        return denominazionevettore;
    }

    /**
     * @return Returns the descrizioneArticolo.
     */
    public String getDescrizioneArticolo() {
        return descrizioneArticolo;
    }

    /**
     * @return Returns the indirizzo.
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @return Returns the indirizzo2.
     */
    public String getIndirizzo2() {
        return indirizzo2;
    }

    /**
     * @return Returns the nazione.
     */
    public String getNazione() {
        return nazione;
    }

    /**
     * @return Returns the nazione2.
     */
    public String getNazione2() {
        return nazione2;
    }

    /**
     * @return Returns the numeroriga.
     */
    public int getNumeroriga() {
        return numeroriga;
    }

    /**
     * @return Returns the operazione.
     */
    public String getOperazione() {
        return operazione;
    }

    /**
     * @return Returns the pesoMassimo.
     */
    public String getPesoMassimo() {
        return pesoMassimo;
    }

    /**
     * @return Returns the pesoMinimo.
     */
    public String getPesoMinimo() {
        return pesoMinimo;
    }

    /**
     * @return Returns the pezzi.
     */
    public String getPezzi() {
        return pezzi;
    }

    /**
     * @return Returns the prezzatura.
     */
    public String getPrezzatura() {
        return prezzatura;
    }

    /**
     * @return Returns the provincia.
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * @return Returns the provincia2.
     */
    public String getProvincia2() {
        return provincia2;
    }

    /**
     * @return Returns the qta.
     */
    public Double getQtaDaEvadere() {
        return qtaDaEvadere;
    }

    /**
     * @return Returns the ragSociale.
     */
    public String getRagSociale() {
        return ragSociale;
    }

    /**
     * @return Returns the taraCartone.
     */
    public String getTaraCartone() {
        return taraCartone;
    }

    /**
     * @return Returns the taraProdotto.
     */
    public String getTaraProdotto() {
        return taraProdotto;
    }

    /**
     * @return Returns the um.
     */
    public String getUm() {
        return um;
    }

    /**
     *
     * @param articolo
     *            articolo con gli attributi
     * @param codiceAttributo
     *            codice da trovare
     * @return attributo trovato. stringa vuota se non viene trovato l'attributo.
     */
    private String getValoreAttributo(Articolo articolo, String codiceAttributo) {
        for (AttributoArticolo attributo : articolo.getAttributiArticolo()) {
            if (attributo.getTipoAttributo().getCodice().equals(codiceAttributo)) {
                // return attributo.getValore() == null ? "0000000000000000000000000000000" :
                // attributo.getValore();
                return attributo.getValore() == null ? "" : attributo.getValore();
            }
        }
        return "";
    }

    /**
     * @param barCode
     *            The barCode to set.
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @param barCodeCartone
     *            The barCodeCartone to set.
     */
    public void setBarCodeCartone(String barCodeCartone) {
        this.barCodeCartone = barCodeCartone;
    }

    /**
     * @param cap
     *            The cap to set.
     */
    public void setCap(String cap) {
        this.cap = cap;
    }

    /**
     * @param cap2
     *            The cap2 to set.
     */
    public void setCap2(String cap2) {
        this.cap2 = cap2;
    }

    /**
     * @param chiave
     *            The chiave to set.
     */
    public void setChiave(String chiave) {
        this.chiave = chiave;
    }

    /**
     * @param citta
     *            The citta to set.
     */
    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * @param citta2
     *            The citta2 to set.
     */
    public void setCitta2(String citta2) {
        this.citta2 = citta2;
    }

    /**
     * @param codArticolo
     *            The codArticolo to set.
     */
    public void setCodArticolo(String codArticolo) {
        this.codArticolo = codArticolo;
    }

    /**
     * @param codCliente
     *            The codCliente to set.
     */
    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    /**
     * @param codSede
     *            The codSede to set.
     */
    public void setCodSede(String codSede) {
        this.codSede = codSede;
    }

    /**
     * @param codVettore
     *            The codVettore to set.
     */
    public void setCodVettore(String codVettore) {
        this.codVettore = codVettore;
    }

    /**
     * @param dataConsegna
     *            The dataConsegna to set.
     */
    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    /**
     * @param dataRegistrazione
     *            The dataRegistrazione to set.
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param denominazioneSede
     *            The denominazioneSede to set.
     */
    public void setDenominazioneSede(String denominazioneSede) {
        this.denominazioneSede = denominazioneSede;
    }

    /**
     * @param denominazionevettore
     *            The denominazionevettore to set.
     */
    public void setDenominazionevettore(String denominazionevettore) {
        this.denominazionevettore = denominazionevettore;
    }

    /**
     * @param descrizioneArticolo
     *            The descrizioneArticolo to set.
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.descrizioneArticolo = descrizioneArticolo;
    }

    /**
     * @param indirizzo
     *            The indirizzo to set.
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    /**
     * @param indirizzo2
     *            The indirizzo2 to set.
     */
    public void setIndirizzo2(String indirizzo2) {
        this.indirizzo2 = indirizzo2;
    }

    /**
     * @param nazione
     *            The nazione to set.
     */
    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    /**
     * @param nazione2
     *            The nazione2 to set.
     */
    public void setNazione2(String nazione2) {
        this.nazione2 = nazione2;
    }

    /**
     * @param numeroriga
     *            The numeroriga to set.
     */
    public void setNumeroriga(int numeroriga) {
        this.numeroriga = numeroriga;
    }

    /**
     * @param operazione
     *            The operazione to set.
     */
    public void setOperazione(String operazione) {
        this.operazione = operazione;
    }

    /**
     * @param pesoMassimo
     *            The pesoMassimo to set.
     */
    public void setPesoMassimo(String pesoMassimo) {
        this.pesoMassimo = pesoMassimo;
    }

    /**
     * @param pesoMinimo
     *            The pesoMinimo to set.
     */
    public void setPesoMinimo(String pesoMinimo) {
        this.pesoMinimo = pesoMinimo;
    }

    /**
     * @param pezzi
     *            The pezzi to set.
     */
    public void setPezzi(String pezzi) {
        this.pezzi = pezzi;
    }

    /**
     * @param prezzatura
     *            The prezzatura to set.
     */
    public void setPrezzatura(String prezzatura) {
        this.prezzatura = prezzatura;
    }

    /**
     * @param provincia
     *            The provincia to set.
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * @param provincia2
     *            The provincia2 to set.
     */
    public void setProvincia2(String provincia2) {
        this.provincia2 = provincia2;
    }

    /**
     * @param qta
     *            The qta to set.
     */
    public void setQtaDaEvadere(Double qta) {
        this.qtaDaEvadere = qta;
        operazione = qtaDaEvadere > 0 ? "1" : "2";
    }

    /**
     * @param ragSociale
     *            The ragSociale to set.
     */
    public void setRagSociale(String ragSociale) {
        this.ragSociale = ragSociale;
    }

    /**
     * @param taraCartone
     *            The taraCartone to set.
     */
    public void setTaraCartone(String taraCartone) {
        this.taraCartone = taraCartone;
    }

    /**
     * @param taraProdotto
     *            The taraProdotto to set.
     */
    public void setTaraProdotto(String taraProdotto) {
        this.taraProdotto = taraProdotto;
    }

    /**
     * @param um
     *            The um to set.
     */
    public void setUm(String um) {
        this.um = um;
    }
}
