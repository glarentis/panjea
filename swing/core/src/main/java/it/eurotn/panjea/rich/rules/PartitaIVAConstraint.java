package it.eurotn.panjea.rich.rules;

import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.reporting.TypeResolvableSupport;

/**
 * Constraint per la validazione della Partita IVA.
 * 
 * @author adriano
 */
public class PartitaIVAConstraint extends TypeResolvableSupport implements Constraint {

    private static Logger logger = Logger.getLogger(PartitaIVAConstraint.class);

    /**
     * Costruttore.
     */
    public PartitaIVAConstraint() {
        super();
    }

    @Override
    public boolean test(Object argument) {
        logger.debug("--> test PartitaIVAConstraint " + argument);
        String partitaIVA = (String) argument;
        if (partitaIVA != null && partitaIVA.length() == 11) {
            return validaPartitaIVA(partitaIVA);
        } else {
            // con questo controllo non � obbligatorio inserire la partita iva,
            // per renderla obbligatoria basta aggiungere la requiredConstraint
            // nelle validation rules.
            if (partitaIVA == null || (partitaIVA != null && partitaIVA.length() == 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica della partita IVA il risultato delle operazioni svolte sulle prime 10 cifre va confrontato con
     * l'undiciesima cifra , se uguale la partita IVA � valida.
     * 
     * @param partitaIVA
     *            la partita iva da validare
     * @return true se valida, false altrimenti
     */
    private boolean validaPartitaIVA(String partitaIVA) {
        int sommaTotale = 0;
        int sommaDispari = 0;
        int sommaPari = 0;
        int cifraDiVerificaPartitaIVA = new Integer(partitaIVA.charAt(10) + "");

        // ciclo sulle cifre dall' 1 al 10 e sommo distintamente le cifre pari e dispari seguendo
        // due metodi diversi
        for (int i = 0; i < 10; i++) {
            char c = partitaIVA.charAt(i);
            int cifra;
            try {
                cifra = new Integer(c + "");
            } catch (NumberFormatException e) {
                // Ritorno false dato che nella validazione della partita iva posso ritrovarmi un codice fiscale non
                // valido o una partita estera o qls altra stringa non standard. vedi campo codice fiscale di entita' in
                // cui posso inserire la partita iva, valido quindi sia cod fisc che p.iva
                return false;
            }
            // sommo le cifre pari da 2 a 10
            if (i % 2 != 0) {
                // moltiplico per due la cifra e se > 9 sottraggo 9
                int ris = cifra * 2;
                if (ris > 9) {
                    ris = ris - 9;
                }
                // sommo il risultato alla somma delle cifre pari
                sommaPari = sommaPari + ris;
            } else {
                // sommo le cifre dispari da 1 a 9
                sommaDispari = sommaDispari + cifra;
            }
        }

        // somma totale delle parziali fatte precedentemente
        sommaTotale = sommaDispari + sommaPari;

        int restoDaTotale = sommaTotale % 10;
        int risultatoDaVerificare = 0;

        if (restoDaTotale != 0) {
            risultatoDaVerificare = 10 - restoDaTotale;
        }

        // il la cifra calcolata deve essere uguale all' undiciesima cifra della partita IVA
        if (risultatoDaVerificare == cifraDiVerificaPartitaIVA) {
            logger.debug("--> Verificata partita IVA");
            return true;
        }
        return false;
    }

}
