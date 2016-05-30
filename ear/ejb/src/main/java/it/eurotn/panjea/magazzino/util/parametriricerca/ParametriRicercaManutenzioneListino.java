package it.eurotn.panjea.magazzino.util.parametriricerca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

/**
 * Parametri ricerca per USE CASE manutenzione listino.
 *
 * @author leonardo
 */
public class ParametriRicercaManutenzioneListino implements Serializable {

    public enum EntitaTipoRicercaEnum {
        ANAGRAFICAARTICOLO, DOCUMENTO
    }

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum ProvenienzaPrezzoManutenzioneListino {
        LISTINO, ULTIMO_COSTO_DEPOSITO, ULTIMO_COSTO_AZIENDA, COSTO_MEDIO_PONDERATO, FILE_ESTERNO
    }

    public enum TipoVariazione {
        PERCENTUALE, IMPORTO
    }

    private static final long serialVersionUID = -713211741048770573L;

    private Integer numeroInserimento;

    @Transient
    private byte[] dataFileEsterno;

    @Transient
    private transient File file;

    private VersioneListino versioneListinoDestinazione = null;

    private boolean effettuaRicerca = false;

    private ProvenienzaPrezzoManutenzioneListino provenienzaPrezzoManutenzioneListino = null;

    private String codiceAzienda = null;

    /**
     * Indica se devo filtrare l'articolo per entità nei documenti oppure nell'anagrafica
     * articolo/entità.
     */
    private EntitaTipoRicercaEnum entitaTipoRicerca = EntitaTipoRicercaEnum.ANAGRAFICAARTICOLO;

    private BigDecimal variazione = null;

    private TipoVariazione tipoVariazione;

    private Integer numeroDecimali = null;

    private VersioneListino versioneListino = null;

    private EntitaLite entita = null;

    private List<CategoriaLite> categorie = null;
    private List<ArticoloLite> articoli;

    private DepositoLite deposito = null;

    private String userManutenzione = null;

    private boolean tutteCategorie = false;

    {
        provenienzaPrezzoManutenzioneListino = ProvenienzaPrezzoManutenzioneListino.LISTINO;
        tipoVariazione = TipoVariazione.PERCENTUALE;
        categorie = new ArrayList<CategoriaLite>();
        articoli = new ArrayList<ArticoloLite>();
    }

    /**
     * Default constructor.
     */
    public ParametriRicercaManutenzioneListino() {
        super();
    }

    /**
     * @return the articoli
     */
    public List<ArticoloLite> getArticoli() {
        return articoli;
    }

    /**
     * @return the articoli
     */
    public String getArticoliId() {
        List<Integer> id = new ArrayList<Integer>();
        if (articoli != null) {
            for (ArticoloLite articoloLite : articoli) {
                id.add(articoloLite.getId());
            }
        }
        return StringUtils.join(id, ",");
    }

    /**
     * @return the categorie
     */
    public List<CategoriaLite> getCategorie() {
        return categorie;
    }

    /**
     * @return string sql per filtrare le categorie
     */
    public Integer[] getCategorieSql() {
        Integer[] result = new Integer[categorie.size()];
        for (int i = 0; i < categorie.size(); i++) {
            result[i] = categorie.get(i).getId();
        }
        return result;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return Returns the dataFileEsterno.
     */
    public byte[] getDataFileEsterno() {
        return dataFileEsterno;
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     *
     * @return descrizione dei parametri
     */
    public String getDescrizione() {
        StringBuilder sb = new StringBuilder("<HTML><br>");
        sb.append("<B>Provenienza prezzo: </B>");
        switch (this.provenienzaPrezzoManutenzioneListino) {
        case COSTO_MEDIO_PONDERATO:
            sb.append(" da costo medio ponderato<br/>");
            break;
        case ULTIMO_COSTO_AZIENDA:
            sb.append("da ultimo costo azienda<br/>");
            break;
        case ULTIMO_COSTO_DEPOSITO:
            sb.append("da ultimo costo per il deposito " + getDeposito().getCodice() + " - "
                    + getDeposito().getDescrizione() + "<br/>");
            break;
        case LISTINO:
            sb.append("da listino ");
            sb.append(versioneListino.getListino().getCodice());
            sb.append(" - ");
            sb.append(versioneListino.getListino().getDescrizione());
            sb.append(" - Versione ");
            sb.append(versioneListino.getCodice());
            sb.append("<br/>");
            break;
        default:
            break;
        }

        switch (this.tipoVariazione) {
        case PERCENTUALE:
            sb.append("<B>Variazione %: </B>");
            sb.append(variazione);
            sb.append("<br/>");
            break;
        case IMPORTO:
            sb.append("<B>Variazione €: </B>");
            sb.append(variazione);
            sb.append("<br/>");
            break;
        default:
            break;
        }

        if (numeroDecimali != null) {
            sb.append("<B>Numero decimali </B>");
            sb.append(numeroDecimali);
            sb.append("<br/>");
        }
        sb.append("<B>Filtri per anagrafica articoli:</B><br/>");
        if (getEntita() != null) {
            sb.append("<i>Entità: </i>");
            sb.append(entita.getCodice());
            sb.append(" - ");
            sb.append(entita.getAnagrafica().getDenominazione());
            sb.append(" - ");
            sb.append(entita.getDescrizioneDatiGeografici());
            sb.append("<br/>");
        }
        if (getArticoli() != null && !getArticoli().isEmpty()) {
            for (ArticoloLite articolo : articoli) {
                sb.append("<i>Articolo: </i>");
                sb.append(articolo.getCodice());
                sb.append(" - ");
                sb.append(articolo.getDescrizione());
                sb.append("<br/>");
            }
        }
        if (getVersioneListino() != null) {
            sb.append("<i>Listino </i>");
            sb.append(versioneListino.getListino().getCodice());
            sb.append(" - ");
            sb.append(versioneListino.getListino().getDescrizione());
            sb.append(" - Versione ");
            sb.append(versioneListino.getCodice());
            sb.append("<br/>");
        }
        if (getCategorie() != null && !getCategorie().isEmpty() && !tutteCategorie) {
            sb.append("<i>Categorie </i>");
            for (int i = 0; i < getCategorie().size(); i++) {
                sb.append(getCategorie().get(i).getDescrizione());
                if (i % 5 == 0) {
                    sb.append("<br/>");
                }
                if (i != getCategorie().size() - 1) {
                    sb.append(" - ");
                }
            }
        }
        sb.append("<br></HTML>");
        String result = sb.toString();
        // la taglio a 1000
        if (result.length() > 1000) {
            result = result.substring(0, 1000);
        }
        return result;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the entitaTipoRicerca
     */
    public EntitaTipoRicercaEnum getEntitaTipoRicerca() {
        return entitaTipoRicerca;
    }

    /**
     * @return Returns the file.
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the numeroDecimali
     */
    public Integer getNumeroDecimali() {
        return numeroDecimali;
    }

    /**
     * @return Returns the numeroInserimento.
     */
    public Integer getNumeroInserimento() {
        return numeroInserimento;
    }

    /**
     * @return the provenienzaPrezzoManutenzioneListino
     */
    public ProvenienzaPrezzoManutenzioneListino getProvenienzaPrezzoManutenzioneListino() {
        return provenienzaPrezzoManutenzioneListino;
    }

    /**
     *
     * @return nome della tabella temporanea dove vengono importati i dati del file esterno
     */
    public String getTableNameFileEsterno() {
        return "MANLISTINO" + getUserManutenzione().split("#")[0] + getNumeroInserimento();
    }

    /**
     * @return Returns the tipoVariazione.
     */
    public TipoVariazione getTipoVariazione() {
        return tipoVariazione;
    }

    /**
     * @return the userManutenzione
     */
    public String getUserManutenzione() {
        return userManutenzione;
    }

    /**
     * @return the variazione
     */
    public BigDecimal getVariazione() {
        return variazione;
    }

    /**
     * @return the versioneListino
     */
    public VersioneListino getVersioneListino() {
        return versioneListino;
    }

    /**
     * @return Returns the versioneListinoDestinazione.
     */
    public VersioneListino getVersioneListinoDestinazione() {
        return versioneListinoDestinazione;
    }

    /**
     * @return the effettuaRicerca
     */
    public boolean isEffettuaRicerca() {
        return effettuaRicerca;
    }

    /**
     * @return the tutteCategorie
     */
    public boolean isTutteCategorie() {
        return tutteCategorie;
    }

    /**
     * @param articoli
     *            the articoli to set
     */
    public void setArticoli(List<ArticoloLite> articoli) {
        this.articoli = articoli;
    }

    /**
     * @param categorie
     *            the categorie to set
     */
    public void setCategorie(List<CategoriaLite> categorie) {
        this.categorie = categorie;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param deposito
     *            the deposito to set
     */
    public void setDeposito(DepositoLite deposito) {
        this.deposito = deposito;
    }

    /**
     * @param effettuaRicerca
     *            the effettuaRicerca to set
     */
    public void setEffettuaRicerca(boolean effettuaRicerca) {
        this.effettuaRicerca = effettuaRicerca;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param entitaTipoRicerca
     *            the entitaTipoRicerca to set
     */
    public void setEntitaTipoRicerca(EntitaTipoRicercaEnum entitaTipoRicerca) {
        this.entitaTipoRicerca = entitaTipoRicerca;
    }

    /**
     * @param file
     *            The file to set.
     */
    public void setFile(File file) {
        try {
            try (BufferedReader brTest = new BufferedReader(new FileReader(file))) {
                String line = brTest.readLine();
                if (line == null) {
                    throw new GenericException("Formato file non valido");
                }
                if (line != null) {
                    String[] token = line.split("#");
                    if (token.length != 2) {
                        throw new GenericException("Formato file non valido");
                    }
                }
            }
            this.file = file;
            dataFileEsterno = IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param numeroDecimali
     *            the numeroDecimali to set
     */
    public void setNumeroDecimali(Integer numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param numeroInserimento
     *            The numeroInserimento to set.
     */
    public void setNumeroInserimento(Integer numeroInserimento) {
        this.numeroInserimento = numeroInserimento;
    }

    /**
     * @param provenienzaPrezzoManutenzioneListino
     *            the provenienzaPrezzoManutenzioneListino to set
     */
    public void setProvenienzaPrezzoManutenzioneListino(
            ProvenienzaPrezzoManutenzioneListino provenienzaPrezzoManutenzioneListino) {
        this.provenienzaPrezzoManutenzioneListino = provenienzaPrezzoManutenzioneListino;
    }

    /**
     * @param tipoVariazione
     *            The tipoVariazione to set.
     */
    public void setTipoVariazione(TipoVariazione tipoVariazione) {
        this.tipoVariazione = tipoVariazione;
    }

    /**
     * @param tutteCategorie
     *            the tutteCategorie to set
     */
    public void setTutteCategorie(boolean tutteCategorie) {
        this.tutteCategorie = tutteCategorie;
    }

    /**
     * @param userManutenzione
     *            the userManutenzione to set
     */
    public void setUserManutenzione(String userManutenzione) {
        this.userManutenzione = userManutenzione;
    }

    /**
     * @param variazione
     *            the variazione to set
     */
    public void setVariazione(BigDecimal variazione) {
        this.variazione = variazione;
    }

    /**
     * @param versioneListino
     *            the versioneListino to set
     */
    public void setVersioneListino(VersioneListino versioneListino) {
        this.versioneListino = versioneListino;
    }

    /**
     * @param versioneListinoDestinazione
     *            The versioneListinoDestinazione to set.
     */
    public void setVersioneListinoDestinazione(VersioneListino versioneListinoDestinazione) {
        this.versioneListinoDestinazione = versioneListinoDestinazione;
    }

}
