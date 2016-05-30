package it.eurotn.panjea.dms.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Categoria;

@Entity
@Audited
@Table(name = "dms_settings")
@NamedQueries({
        @NamedQuery(name = "DmsSettings.caricaAll", query = "from DmsSettings dmss where dmss.codiceAzienda = :codiceAzienda", hints = {
                @QueryHint(name = "org.hibernate.cacheable", value = "true"),
                @QueryHint(name = "org.hibernate.cacheRegion", value = "dmsSettings") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "dmsSettings")
public class DmsSettings extends EntityBase {

    private static final Logger LOGGER = Logger.getLogger(DmsSettings.class);

    private static final long serialVersionUID = -8377949189359497002L;

    private static final String ROOT_FOLDER = "/Default/";

    public static final List<String> EMAIL_VARIABLES = Collections.unmodifiableList(Arrays.asList(new String[] {
            "$denominazioneEntita$", "$codiceEntita$", "$anno2$", "$anno4$", "$mese$", "$codAzienda$" }));

    public static final List<String> ARTICOLI_VARIABLES = Collections.unmodifiableList(
            Arrays.asList(new String[] { "$codiceCat$", "$descCat$", "$codiceArt$", "$codAzienda$" }));

    public static final List<String> ENTITA_VARIABLES = Collections.unmodifiableList(
            Arrays.asList(new String[] { "$tipoEntita$", "$codiceEntita$", "$denominazioneEntita$", "$codAzienda$" }));

    public static final List<String> DOCUMENTI_VARIABLES = Collections
            .unmodifiableList(Arrays.asList(new String[] { "$dataDoc$", "$anno2$", "$anno4$", "$mese$",
                    "$codiceTipoDoc$", "$numDoc$", "$codiceEntita$", "$denominazioneEntita$", "$codAzienda$" }));

    private String codiceAzienda;

    private String emailFolderPattern;

    private String articoliFolderPattern;

    private String entitaFolderPattern;

    private String serviceUrl;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "dmsSettings_id")
    private List<TipoDocumentoDmsSettings> tipiDocumentoDmsSettings;

    private String altroFolder;

    /**
     * @return the altroFolder
     */
    public String getAltroFolder() {
        return altroFolder;
    }

    /**
     * @return the articoliFolderPattern
     */
    public String getArticoliFolderPattern() {
        return articoliFolderPattern;
    }

    /**
     * @return the codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return the emailFolderPattern
     */
    public String getEmailFolderPattern() {
        return emailFolderPattern;
    }

    /**
     * @return the entitaFolderPattern
     */
    public String getEntitaFolderPattern() {
        return entitaFolderPattern;
    }

    /**
     * Folder per il salvataggio dell'articolo basata sul pattern impostato.
     *
     * @param articolo
     *            articolo
     * @return folder
     */
    private String getFolder(Articolo articolo) {
        if (articolo == null) {
            throw new IllegalArgumentException("articolo non può essere null");
        }
        Categoria categoria = articolo.getCategoria();
        String codiceCategoria = categoria != null ? StringUtils.defaultString(categoria.getCodice()) : "_nessuno";
        String descCategoria = categoria != null ? StringUtils.defaultString(categoria.getDescrizione()) : "_nessuno";
        String codiceArticolo = articolo != null ? StringUtils.defaultString(articolo.getCodice()) : "_nessuno";
        String codAzienda = articolo.getCodiceAzienda().toLowerCase();

        String[] variabiliArticoli = ARTICOLI_VARIABLES.toArray(new String[ARTICOLI_VARIABLES.size()]);
        return ROOT_FOLDER + StringUtils.replaceEach(StringUtils.defaultString(articoliFolderPattern),
                variabiliArticoli,
                new String[] { codiceCategoria.trim(), descCategoria.trim(), codiceArticolo.trim(), codAzienda });
    }

    /**
     *
     * @param documento
     *            documento
     * @return cartella di destinazione nel dms per il documento
     */
    private String getFolder(Documento documento) {
        if (documento == null || StringUtils.isEmpty(documento.getCodiceAzienda())) {
            throw new IllegalArgumentException("Documento non può essere null");
        }

        String codAzienda = documento.getCodiceAzienda().toLowerCase();
        String dataDocumento = DateFormatUtils.format(documento.getDataDocumento(), "yyyy/MM/dd");
        String anno2 = DateFormatUtils.format(documento.getDataDocumento(), "yy");
        String anno4 = DateFormatUtils.format(documento.getDataDocumento(), "yyyy");
        String mese = DateFormatUtils.format(documento.getDataDocumento(), "MM");
        String codTipoDoc = documento.getTipoDocumento().getCodice();
        String codiceDoc = documento.getCodice().getCodice();
        String codiceEntita = documento.getEntita() != null ? String.valueOf(documento.getEntita().getCodice())
                : "_nessuno";
        String denominazioneEntita = documento.getEntita() != null
                ? String.valueOf(documento.getEntita().getAnagrafica().getDenominazione()) : "_nessuno";

        // per trovare la definizione della directory di pubblicazione uso in quest'ordine:
        // quella definita per il tipo documento
        // quella definita per qualsiasi documento
        // quella definita in "altro"
        String tipoDocFolderPattern = null;
        String allTipoDocFolderPattern = null;
        for (TipoDocumentoDmsSettings tipoDocSettings : tipiDocumentoDmsSettings) {
            if (tipoDocSettings.getTipoDocumento() == null) {
                allTipoDocFolderPattern = tipoDocSettings.getFolderPattern();
            } else if (tipoDocSettings.getTipoDocumento().equals(documento.getTipoDocumento())) {
                tipoDocFolderPattern = tipoDocSettings.getFolderPattern();
            }
        }
        String dirValida = StringUtils.defaultString(tipoDocFolderPattern, allTipoDocFolderPattern);

        if (dirValida != null) {
            String[] variabiliDocumento = DOCUMENTI_VARIABLES.toArray(new String[DOCUMENTI_VARIABLES.size()]);

            return ROOT_FOLDER + StringUtils.replaceEach(StringUtils.defaultString(dirValida), variabiliDocumento,
                    new String[] { dataDocumento, anno2, anno4, mese, codTipoDoc, codiceDoc, codiceEntita,
                            denominazioneEntita, codAzienda });
        } else {
            return ROOT_FOLDER + getAltroFolder();
        }
    }

    /**
     * Folder per il salvataggio dell'entità basata sul pattern impostato.
     *
     * @param entita
     *            entità
     * @return folder
     */
    private String getFolder(Entita entita) {
        if (entita == null) {
            throw new IllegalArgumentException("entità non può essere null");
        }
        String tipoEntita = entita.getClass().getSimpleName();
        Integer codiceEntita = entita.getCodice();
        String denominazione = entita.getAnagrafica().getDenominazione();
        String codAzienda = entita.getAnagrafica().getCodiceAzienda().toLowerCase();

        String[] variabiliEntita = ENTITA_VARIABLES.toArray(new String[ENTITA_VARIABLES.size()]);
        return ROOT_FOLDER + StringUtils.replaceEach(StringUtils.defaultString(entitaFolderPattern), variabiliEntita,
                new String[] { tipoEntita, String.valueOf(codiceEntita), denominazione, codAzienda });
    }

    /**
     * Folder per il salvataggio della mail basata sul pattern impostato.
     *
     * @param message
     *            messaggio
     * @param entita
     *            entità
     * @param codAzienda
     *            codice azienda
     * @return folder
     */
    public String getFolder(MimeMessage message, EntitaLite entita, String codAzienda) {
        if (message == null) {
            throw new IllegalArgumentException("message non può essere null");
        }
        String denominazione = entita != null ? entita.getAnagrafica().getDenominazione() : "_nessuno";
        String codiceEntita = entita != null ? String.valueOf(entita.getCodice()) : "_nessuno";
        String anno2 = "_nessuno";
        String anno4 = "_nessuno";
        String mese = "_nessuno";
        try {
            Date data = message.getSentDate();
            anno2 = DateFormatUtils.format(data, "yy");
            anno4 = DateFormatUtils.format(data, "yyyy");
            mese = DateFormatUtils.format(data, "MM");
        } catch (MessagingException e) {
            LOGGER.error("--> errore durante il ecupero della data dal mime message", e);
        }

        String[] variabiliEmail = EMAIL_VARIABLES.toArray(new String[EMAIL_VARIABLES.size()]);
        return ROOT_FOLDER + StringUtils.replaceEach(StringUtils.defaultString(emailFolderPattern), variabiliEmail,
                new String[] { denominazione, codiceEntita, anno2, anno4, mese,
                        StringUtils.defaultIfBlank(codAzienda, "_nessuno").toLowerCase() });
    }

    /**
     * Restituisce il path dove pubblicare l'oggetto
     *
     * @param formObject
     *            oggetto per il quale caricare il path
     * @return pathe della cartella di destinazione per l'allegato
     */
    public String getFolder(Object formObject) {
        // Se aumenta implementare il visitors
        if (formObject instanceof Articolo) {
            return getFolder((Articolo) formObject);
        } else if (formObject instanceof Documento) {
            return getFolder((Documento) formObject);
        } else if (formObject instanceof Entita) {
            return getFolder((Entita) formObject);
        }
        return ROOT_FOLDER + getAltroFolder();
    }

    /**
     * @return Returns the serviceUrl.
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * @return the tipiDocumentoDmsSettings
     */
    public List<TipoDocumentoDmsSettings> getTipiDocumentoDmsSettings() {
        return tipiDocumentoDmsSettings;
    }

    /**
     * @param altroFolder
     *            the altroFolder to set
     */
    public void setAltroFolder(String altroFolder) {
        this.altroFolder = altroFolder;
    }

    /**
     * @param articoliFolderPattern
     *            the articoliFolderPattern to set
     */
    public void setArticoliFolderPattern(String articoliFolderPattern) {
        this.articoliFolderPattern = articoliFolderPattern;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param emailFolderPattern
     *            the emailFolderPattern to set
     */
    public void setEmailFolderPattern(String emailFolderPattern) {
        this.emailFolderPattern = emailFolderPattern;
    }

    /**
     * @param entitaFolderPattern
     *            the entitaFolderPattern to set
     */
    public void setEntitaFolderPattern(String entitaFolderPattern) {
        this.entitaFolderPattern = entitaFolderPattern;
    }

    /**
     * @param serviceUrl
     *            The serviceUrl to set.
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * @param tipiDocumentoDmsSettings
     *            the tipiDocumentoDmsSettings to set
     */
    public void setTipiDocumentoDmsSettings(List<TipoDocumentoDmsSettings> tipiDocumentoDmsSettings) {
        this.tipiDocumentoDmsSettings = tipiDocumentoDmsSettings;
    }

}
