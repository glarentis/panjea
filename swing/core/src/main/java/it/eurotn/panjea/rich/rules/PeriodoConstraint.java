/**
 * 
 */
package it.eurotn.panjea.rich.rules;

import it.eurotn.panjea.parametriricerca.domain.Periodo;

import java.util.Calendar;
import java.util.Date;

import org.springframework.rules.closure.BinaryConstraint;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.LessThanEqualTo;
import org.springframework.rules.constraint.TypeResolvableConstraint;

public class PeriodoConstraint extends TypeResolvableConstraint implements Constraint {

    private boolean periodoRequired = false;

    private BinaryConstraint dateConstraint;

    {
        dateConstraint = LessThanEqualTo.instance();
    }

    /**
     * Costruttore.
     */
    public PeriodoConstraint() {
        super();
        this.periodoRequired = false;
    }

    /**
     * Costruttore.
     * 
     * @param periodoRequired
     *            indica se il periodo deve essere richiesto
     */
    public PeriodoConstraint(final boolean periodoRequired) {
        super();
        this.periodoRequired = periodoRequired;
    }

    private boolean isDateAfter(Date dataInizio, Date dataFine) {
        // creo due calendari per azzerare ore,minuti e secondi per fare in
        // modo di non avere problemi sulla validazione confronto delle date
        Calendar calendarDa = Calendar.getInstance();
        calendarDa.setTime(dataInizio);
        calendarDa.set(Calendar.HOUR, 0);
        calendarDa.set(Calendar.HOUR_OF_DAY, 0);
        calendarDa.set(Calendar.MINUTE, 0);
        calendarDa.set(Calendar.SECOND, 0);
        calendarDa.set(Calendar.MILLISECOND, 0);
        Calendar calendarA = Calendar.getInstance();
        calendarA.setTime(dataFine);
        calendarA.set(Calendar.HOUR, 0);
        calendarA.set(Calendar.HOUR_OF_DAY, 0);
        calendarA.set(Calendar.MINUTE, 0);
        calendarA.set(Calendar.SECOND, 0);
        calendarA.set(Calendar.MILLISECOND, 0);

        Date dateDa = calendarDa.getTime();
        Date dateA = calendarA.getTime();

        return dateConstraint.test(dateDa, dateA);
    }

    private boolean isDatePresenti(Periodo periodo) {
        return periodo.getDataIniziale() != null && periodo.getDataFinale() != null;
    }

    @Override
    public boolean test(Object argument) {
        boolean result = true;

        if (argument instanceof Periodo) {
            Periodo periodo = (Periodo) argument;

            // se il periodo è richiesto verifico che le date ci siano
            if (periodoRequired) {
                result = result && isDatePresenti(periodo);
            }

            if (isDatePresenti(periodo)) {
                result = result && isDateAfter(periodo.getDataIniziale(), periodo.getDataFinale());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        if (periodoRequired) {
            return ": intervallo date richiesto o non valido";
        }
        return ": intervallo date non valido";
    }

}
