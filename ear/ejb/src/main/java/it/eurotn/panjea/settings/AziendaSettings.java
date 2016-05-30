package it.eurotn.panjea.settings;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.sicurezza.domain.UtenteCollegato;
import it.eurotn.panjea.sicurezza.service.UtentiFileCryptValidator;
import it.eurotn.panjea.sicurezza.service.UtentiPanjeaLiteValidator;
import it.eurotn.panjea.sicurezza.service.UtentiValidator;

public class AziendaSettings {

    private int numUtenti;
    private Date dataScadenza;
    private String codiceAzienda;
    private String license;

    private UtentiValidator utentiValidator;

    /**
     * @param codiceAzienda
     *            codice azienda
     * @param dataScadenza
     *            indica la data di scadenza del contratto
     * @param numUtenti
     *            numero utenti contemporanei
     * @param license
     *            tipo di licenza (L,P,E)
     */
    public AziendaSettings(final String codiceAzienda, final Date dataScadenza, final int numUtenti,
            final String license) {
        super();
        this.codiceAzienda = codiceAzienda;
        this.dataScadenza = dataScadenza;
        this.numUtenti = numUtenti;
        this.license = StringUtils.defaultString(license);
        // la licenza deve essere L,P o E. Se così non fosse la imposto in autoatico a E
        if (!this.license.equals("L") && !this.license.equals("P") && !this.license.equals("E")) {
            this.license = "E";
        }

        if (isLite()) {
            utentiValidator = new UtentiPanjeaLiteValidator();
        } else {
            utentiValidator = new UtentiFileCryptValidator(numUtenti);
        }
    }

    /**
     *
     * @param utenti
     *            utenti già presenti
     * @param utente
     *            utente da validare
     * @return true se posso aggiungere l'utente.
     */
    public boolean canAddUser(List<UtenteCollegato> utenti, UtenteCollegato utente) {
        return utentiValidator.canAddUser(utenti, utente);
    }

    /**
     * @return the dataScadenza
     */
    public Date getDataScadenza() {
        return dataScadenza;
    }

    /**
     * @return Returns the nUtenti.
     */
    public int getnUtenti() {
        return numUtenti;
    }

    /**
     *
     * @return versione enterprise
     */
    public boolean isEnterprise() {
        return license.equals("E");
    }

    /**
     * @return Returns the licenseValid.
     */
    public boolean isLicenseValid() {
        if (isLite()) {
            return true;
        }
        if (dataScadenza == null) {
            return false;
        }
        return dataScadenza.after(Calendar.getInstance().getTime());
    }

    /**
     *
     * @return versione lite
     */
    public boolean isLite() {
        return license.equals("L");
    }

    /**
     *
     * @return versione professional
     */
    public boolean isProfessional() {
        return license.equals("P");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AziendaSettings [nUtenti=" + numUtenti + ", dataScadenza=" + dataScadenza + ", codiceAzienda="
                + codiceAzienda + ", isEnterprise()=" + isEnterprise() + ", isLicenseValid()=" + isLicenseValid()
                + ", isLite()=" + isLite() + ", isProfessional()=" + isProfessional() + "]";
    }

}
