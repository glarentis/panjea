/**
 * 
 */
package it.eurotn.rich.report;

/**
 * Classe utilizzata per passare le informazioni al subreport del piu' di pagina
 * 
 * @author adriano
 * @version 1.0, 27/nov/06
 * 
 */
public class FooterBean {

    private String rifSQ;
    private String note;

    public String getNote() {
        return note;
    }

    public String getRifSQ() {
        return rifSQ;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setRifSQ(String rifSQ) {
        this.rifSQ = rifSQ;
    }

}
