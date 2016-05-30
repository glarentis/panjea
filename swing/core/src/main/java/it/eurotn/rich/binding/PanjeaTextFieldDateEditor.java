package it.eurotn.rich.binding;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.UIManager;
import javax.swing.event.CaretEvent;

import com.toedter.calendar.JTextFieldDateEditor;

/**
 * Classe text field editor per poter cambiare il comportamento del date field. Normalmente il set del value sul form
 * model avviene solo quando viene perso il focus del campo data; sovrascrivendo il caretUpdate, lancio un
 * firepropertychange sulla property date per rendere effettivo il cambiamento quando la data viene validata (il testo
 * diventa verde).<br>
 * Allo stesso modo per evitare di committare valori non validi quando sono in presenza di errori lancio un
 * firepropertychange con value a null.
 */
public class PanjeaTextFieldDateEditor extends JTextFieldDateEditor {

    private static final long serialVersionUID = -7823457999602669740L;
    private final Calendar calendar;
    private int hours;
    private int minutes;
    private int seconds;
    private int millis;

    private boolean manageTime = false;

    /**
     * @param datePattern
     *            datePattern
     * @param maskPattern
     *            maskPattern
     * @param placeholder
     *            placeholder
     */
    public PanjeaTextFieldDateEditor(final String datePattern, final String maskPattern, final char placeholder) {
        super(datePattern, maskPattern, placeholder);
        calendar = Calendar.getInstance();
        // Hack per nimbus
        if (UIManager.getDefaults().getColor("TextField.inactiveBackground") != null) {
            super.setBackground(new Color(UIManager.getDefaults().getColor("TextField.inactiveBackground").getRGB()));
        }
    }

    @Override
    public void caretUpdate(CaretEvent event) {
        String text = getText().trim();
        String emptyMask = maskPattern.replace('#', placeholder);

        if (this.date == null && (text.length() == 0 || text.equals(emptyMask))) {
            setForeground(Color.BLACK);
            // devo lanciare il firepropertychange anche quando non ho inserito alcun valore
            // altrimenti non viene fatto il commit del nuovo value e mi ritrovo la data
            // precedente immessa invece della data null
            firePropertyChange("date", null, null);
            return;
        }

        try {
            Date oldDate = this.date != null ? (Date) this.date.clone() : null;

            String textToCheck = text.replaceAll("_", "");

            // la data deve essere valida ed essere dello stesso numero di caratteri
            // del pattern impostato per il componente
            if ((textToCheck.length() == datePattern.length())) {
                this.date = dateFormatter.parse(textToCheck);
                setForeground(Color.BLACK);
                firePropertyChange("date", oldDate, date);
            } else {
                setForeground(Color.RED);
                // vedi bug 380
                // annullo il valore in modo che se il parametro e' richiesto almeno ho la certezza
                // che il valore errato inserito come data rende il form non committable
                date = null;
                firePropertyChange("date", null, null);
            }
        } catch (Exception e) {
            setForeground(Color.RED);
            // vedi bug 380
            // annullo il valore in modo che se il parametro e' richiesto almeno ho la certezza
            // che il valore errato inserito come data rende il form non committable
            date = null;
            firePropertyChange("date", null, null);
        }
    }

    @Override
    public Date getDate() {
        try {
            calendar.setTime(dateFormatter.parse(getText()));
            if (!manageTime) {
                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);
                calendar.set(Calendar.SECOND, seconds);
                calendar.set(Calendar.MILLISECOND, millis);
            }
            date = calendar.getTime();
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
    }

    /**
     * Sets the date.<br/>
     * La setDate della classe base rendeva il form dirty, la sovrascrivo per evitare che venga sostituito il valore nel
     * caso in cui non cambia rispetto al valore precedente.
     *
     * @param date
     *            the date
     * @param firePropertyChange
     *            true, if the date property should be fired.
     */
    @Override
    protected void setDate(Date date, boolean firePropertyChange) {
        Date oldDate = this.date != null ? (Date) this.date.clone() : null;
        this.date = date != null ? (Date) date.clone() : null;

        if (date == null) {
            setText("");
        } else {
            calendar.setTime(date);
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minutes = calendar.get(Calendar.MINUTE);
            seconds = calendar.get(Calendar.SECOND);
            millis = calendar.get(Calendar.MILLISECOND);

            String formattedDate = dateFormatter.format(date);
            try {
                if (oldDate == null || (oldDate != null && !oldDate.equals(date))) {
                    setText(formattedDate);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        if (date != null && dateUtil.checkDate(date)) {
            setForeground(Color.BLACK);

        }

        if (firePropertyChange) {
            firePropertyChange("date", oldDate, date);
        }
    }

    /**
     * @param manageTime
     *            the manageTime to set
     */
    public void setManageTime(boolean manageTime) {
        this.manageTime = manageTime;
    }

}