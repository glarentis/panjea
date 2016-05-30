package it.eurotn.panjea.corrispettivi.rich.editors.calendario;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CalendarioCorrispettivoMediator {

    private static final Logger LOGGER = Logger.getLogger(CalendarioCorrispettivoMediator.class);

    private List<GiornoCorrispettivoPanel> listGiorniCorrispettivoPanel;
    private GiornoCorrispettivoPanel lastDaySelected;

    /**
     * Costruttore.
     */
    public CalendarioCorrispettivoMediator() {
        this.listGiorniCorrispettivoPanel = new ArrayList<GiornoCorrispettivoPanel>();
        this.lastDaySelected = null;
    }

    /**
     * Aggiunge un giorno.
     *
     * @param giornoCorrispettivoPanel
     *            giorno da aggiungere
     */
    public void addGiornoCorrispettivoPanel(GiornoCorrispettivoPanel giornoCorrispettivoPanel) {
        this.listGiorniCorrispettivoPanel.add(giornoCorrispettivoPanel);
    }

    /**
     * Seleziona il giorno precedente.
     *
     * @param giornoCorrispettivoPanel
     *            giorno
     */
    public void selectNextDay(GiornoCorrispettivoPanel giornoCorrispettivoPanel) {
        LOGGER.debug("--> Enter selectNextDay");
        int index = this.listGiorniCorrispettivoPanel.indexOf(giornoCorrispettivoPanel);

        if (index != -1 && index < this.listGiorniCorrispettivoPanel.size() - 1) {
            setSelectedDay(this.listGiorniCorrispettivoPanel.get(index + 1));
        }
        LOGGER.debug("--> Exit selectNextDay");
    }

    /**
     * Seleziona il giorno della settimana successiva.
     *
     * @param giornoCorrispettivoPanel
     *            giorno
     */
    public void selectNextWeekDay(GiornoCorrispettivoPanel giornoCorrispettivoPanel) {
        LOGGER.debug("--> Enter selectNextWeekDay");
        int index = this.listGiorniCorrispettivoPanel.indexOf(giornoCorrispettivoPanel);

        if (index != -1 && index < this.listGiorniCorrispettivoPanel.size() - 7) {
            setSelectedDay(this.listGiorniCorrispettivoPanel.get(index + 7));
        }
        LOGGER.debug("--> Exit selectNextWeekDay");
    }

    /**
     * Seleziona il giorno precedente.
     *
     * @param giornoCorrispettivoPanel
     *            giorno
     */
    public void selectPriorDay(GiornoCorrispettivoPanel giornoCorrispettivoPanel) {
        LOGGER.debug("--> Enter selectPriorDay");
        int index = this.listGiorniCorrispettivoPanel.indexOf(giornoCorrispettivoPanel);

        if (index != -1 && index > 0) {
            setSelectedDay(this.listGiorniCorrispettivoPanel.get(index - 1));
        }
        LOGGER.debug("--> Exit selectPriorDay");
    }

    /**
     * Seleziona il giorno della settimana precedente.
     *
     * @param giornoCorrispettivoPanel
     *            giorno
     */
    public void selectPriorWeekDay(GiornoCorrispettivoPanel giornoCorrispettivoPanel) {
        LOGGER.debug("--> Enter selectPriorWeekDay");
        int index = this.listGiorniCorrispettivoPanel.indexOf(giornoCorrispettivoPanel);

        if (index != -1 && index > 6) {
            setSelectedDay(this.listGiorniCorrispettivoPanel.get(index - 7));
        }
        LOGGER.debug("--> Exit selectPriorWeekDay");
    }

    /**
     * Seleziona il giorno passato come parametro e deseleziona ( se esiste ) il giorno precedentemente selezionato.
     *
     * @param giornoCorrispettivoPanel
     *            giorno
     */
    public void setSelectedDay(GiornoCorrispettivoPanel giornoCorrispettivoPanel) {
        LOGGER.debug("--> Enter setSelectedDay");
        if (lastDaySelected != null) {
            lastDaySelected.setSelected(false);
        }

        giornoCorrispettivoPanel.setSelected(true);
        lastDaySelected = giornoCorrispettivoPanel;
        LOGGER.debug("--> Exit setSelectedDay");
    }
}
