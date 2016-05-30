package it.eurotn.panjea.corrispettivi.rich.editors.calendario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.core.Guarded;

import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;

/**
 * CalendarioCorrispettiviPanel, pannello per presentare il calendario dei corrispettivi. Questa classe e' Guard e il
 * suo guarded e' un command che e' legato al valore di CalendarioCorrispettivo (se null il command risulta
 * disabilitato, altrimenti viene abilitato) il guardedCommand puo' cmq essere null.
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CalendarioCorrispettiviPanel extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = -7088461581431238409L;
    private static final String[] weekDays = { "lunedi", "martedi", "mercoledi", "giovedi", "venerdi", "sabato",
            "domenica" };
    public static final Color TODAY_COLOR = new Color(231, 238, 236);
    public static final Color BORDER_COLOR = new Color(63, 125, 145);
    public static final Color FERIALE_COLOR = new Color(254, 254, 254);
    public static final Color FESTIVO_COLOR = new Color(255, 249, 231);
    public static final Color DAY_NUMBER_COLOR = new Color(63, 125, 145);
    public static final Color DAY_LABEL_COLOR = new Color(63, 125, 145);
    public static final Color SELECTED_DAY_COLOR = new Color(255, 231, 156);
    private Guarded guardedCommand = null;
    private CalendarioCorrispettivo calendarioCorrispettivo;
    private JPanel daysPanel;
    private final MessageSource messageSource;
    private CalendarioCorrispettivoMediator calendarioCorrispettivoMediator;

    /**
     * Costruttore.
     *
     * @param guardedCommand
     *            guarded command
     */
    public CalendarioCorrispettiviPanel(final Guarded guardedCommand) {
        super(new BorderLayout());
        this.calendarioCorrispettivoMediator = new CalendarioCorrispettivoMediator();
        this.guardedCommand = guardedCommand;
        propertyChange(null);
        messageSource = (MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class);
        buildUI();
    }

    /**
     * Costruttore.
     *
     * @param guardedCommand
     *            guarded command
     * @param calendarioCorrispettivo
     *            calendario
     */
    public CalendarioCorrispettiviPanel(final Guarded guardedCommand,
            final CalendarioCorrispettivo calendarioCorrispettivo) {
        this(guardedCommand);
        this.calendarioCorrispettivo = calendarioCorrispettivo;
        redrawPanel();
    }

    /**
     * Aggiunge l'header panel che contiene i parametri e i controlli del mese e il pannello che contiene i giorni.
     */
    private void buildUI() {
        daysPanel = new JPanel(new GridBagLayout());
        daysPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        this.add(daysPanel, BorderLayout.CENTER);
    }

    /**
     * @return calendario corrispettivo
     */
    public CalendarioCorrispettivo getCalendarioCorrispettivo() {
        return calendarioCorrispettivo;
    }

    /**
     * Propertychange per aggiornare lo stato del guarded command di this (Guard)
     *
     * @param evt
     *            non viene considerato, puo' quindi essere null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (guardedCommand != null) {
            guardedCommand.setEnabled(calendarioCorrispettivo != null);
        }
    }

    private void redrawPanel() {

        // clear current days panel
        daysPanel.removeAll();

        // repaint per evitare artefatti
        daysPanel.repaint();

        calendarioCorrispettivoMediator = new CalendarioCorrispettivoMediator();

        // redraw days panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // add days of week
        for (int i = 0; i < weekDays.length; i++, gbc.gridx++) {
            JLabel label = new JLabel(messageSource.getMessage(weekDays[i], new Object[] {}, Locale.getDefault()),
                    SwingConstants.CENTER);
            Font font = label.getFont();
            label.setFont(new Font(font.getFontName(), font.getStyle(), 20));
            label.setForeground(DAY_LABEL_COLOR);
            daysPanel.add(label, gbc);
        }

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // Giorni del mese
        GiornoCorrispettivoPanel giornoCorrispettivoPanel; // pannello del mese
        Calendar today = Calendar.getInstance();
        Calendar cellDay = (Calendar) today.clone();

        int month = calendarioCorrispettivo.getMese();
        int year = calendarioCorrispettivo.getAnno();

        // parto con il primo giorno del mese
        cellDay.set(year, month, 1);

        if (cellDay.get(Calendar.DAY_OF_WEEK) > 1) {
            gbc.gridx = cellDay.get(Calendar.DAY_OF_WEEK) - 2;
        } else {
            gbc.gridx = 6;
        }

        while (cellDay.get(Calendar.MONTH) == month) {
            if (gbc.gridx > 6) {
                gbc.gridy++;
                gbc.gridx = 0;
            }
            giornoCorrispettivoPanel = new GiornoCorrispettivoPanel(
                    calendarioCorrispettivo.getGiorniCorrispettivo().get(cellDay.get(Calendar.DAY_OF_MONTH) - 1),
                    calendarioCorrispettivoMediator);

            // quando il giorno mi notifica che il corrispettivo e' cambiato rilancio
            // l'evento cosi' la pagina puo' riceverlo
            giornoCorrispettivoPanel.addPropertyChangeListener(GiornoCorrispettivoPanel.UPDATE_CORRISPETTIVO_PROPERTY,
                    new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            CalendarioCorrispettiviPanel.this.firePropertyChange(
                                    GiornoCorrispettivoPanel.UPDATE_CORRISPETTIVO_PROPERTY, null, evt.getNewValue());
                        }
                    });
            calendarioCorrispettivoMediator.addGiornoCorrispettivoPanel(giornoCorrispettivoPanel);
            if (cellDay.equals(today)) {
                giornoCorrispettivoPanel.setToday(true);
            }
            daysPanel.add(giornoCorrispettivoPanel, gbc);
            gbc.gridx++;
            cellDay.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * @param calendarioCorrispettivo
     *            calendario corrispettivo
     */
    public void setCalendarioCorrispettivo(CalendarioCorrispettivo calendarioCorrispettivo) {
        this.calendarioCorrispettivo = calendarioCorrispettivo;
        redrawPanel();
    }

    /**
     * @param guardedCommand
     *            guarded command
     */
    public void setGuardedCommand(Guarded guardedCommand) {
        this.guardedCommand = guardedCommand;
    }
}
