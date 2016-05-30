package it.eurotn.panjea.corrispettivi.rich.editors.calendario;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * KeyAdapter per navigare nel calendario tramite i tasti cursore e modificare i dati tramite il tasto ENTER.
 *
 * @author Leonardo
 */
public class GiornoCorrispettivoPanelKeyListener extends KeyAdapter {

    private final GiornoCorrispettivoPanel giornoCorrispettivoPanel;

    /**
     * Costruttore.
     * 
     * @param giornoCorrispettivoPanel
     *            giornoCorrispettivoPanel
     */
    public GiornoCorrispettivoPanelKeyListener(final GiornoCorrispettivoPanel giornoCorrispettivoPanel) {
        this.giornoCorrispettivoPanel = giornoCorrispettivoPanel;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);

        switch (event.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            giornoCorrispettivoPanel.getCalendarioCorrispettivoMediator().selectPriorDay(giornoCorrispettivoPanel);
            break;
        case KeyEvent.VK_RIGHT:
            giornoCorrispettivoPanel.getCalendarioCorrispettivoMediator().selectNextDay(giornoCorrispettivoPanel);
            break;
        case KeyEvent.VK_ENTER:
            giornoCorrispettivoPanel.openCorrispettivoPage();
            break;
        case KeyEvent.VK_DOWN:
            giornoCorrispettivoPanel.getCalendarioCorrispettivoMediator().selectNextWeekDay(giornoCorrispettivoPanel);
            break;
        case KeyEvent.VK_UP:
            giornoCorrispettivoPanel.getCalendarioCorrispettivoMediator().selectPriorWeekDay(giornoCorrispettivoPanel);
            break;
        default:
            break;
        }
    }
}