/**
 * 
 */
package it.eurotn.rich.report;

/**
 * Bean utilizzato per passare le informazioni presenti all'interno del subreport dell'intestazione di pagina
 * 
 * @author adriano
 * @version 1.0, 27/nov/06
 * 
 */
public class HeaderBean {

    private String titotoReport;
    private String utenteCorrente;
    private String codiceAzienda;
    private String descrizioneAzienda;

    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    public String getUtenteCorrente() {
        return utenteCorrente;
    }

    public String getDescrizioneAzienda() {
        return descrizioneAzienda;
    }

    public String getTitoloReport() {
        return titotoReport;
    }

    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    public void setUtenteCorrente(String utenteCorrente) {
        this.utenteCorrente = utenteCorrente;
    }

    public void setDescrizioneAzienda(String descrizioneAzienda) {
        this.descrizioneAzienda = descrizioneAzienda;
    }

    public void setTitoloReport(String titoloReport) {
        this.titotoReport = titoloReport;
    }

}
