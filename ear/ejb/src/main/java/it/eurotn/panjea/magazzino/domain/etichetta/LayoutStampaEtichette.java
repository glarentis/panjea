package it.eurotn.panjea.magazzino.domain.etichetta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

public class LayoutStampaEtichette implements Serializable {

    private static final long serialVersionUID = 5019361539903231932L;
    private String reportName;
    private String descrizione;
    private List<String> codiciEntita;

    /**
     * 
     * @param reportName
     *            report dell'etichetta
     */
    public LayoutStampaEtichette(final String reportName) {
        this.reportName = reportName;

        List<String> parti = Arrays.asList(reportName.split("#"));
        descrizione = parti.get(0);
        codiciEntita = new ArrayList<String>(parti);
        codiciEntita.remove(descrizione);
    }

    /**
     * 
     * @return descrizione report
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * 
     * @return nome report
     */
    public String getReportName() {
        return reportName;
    }

    /***
     * Verifica se il report è valido o meno per l'entità.
     * 
     * @param entita
     *            L'entità da controllare
     * @return Vero se l'entità è null, se il report è assegnato all'entità oppure se non è assegnato a nessuna entità
     *         in particolare.
     */
    public boolean isReportValido(final EntitaLite entita) {
        // se il nome del report non contiente nessun codice entita allora lo considero
        // valido per tutte le entita

        return entita == null || codiciEntita.isEmpty() || codiciEntita.contains(entita.getCodice().toString());
    }

}
