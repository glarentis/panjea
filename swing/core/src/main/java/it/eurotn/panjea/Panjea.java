package it.eurotn.panjea;

/**
 * Applicazione PanJea: vengono caricati i file di configurazione dell'applicazione dove sono
 * contenuti i bean che definiscono viste, editor, commands e la sicurezza; viene eseguito il check
 * sull'esistenza dei file di configurazione e viene controllata la disponibilità di aggiornamenti
 * per panjea.
 *
 * @author Leonardo
 */
public final class Panjea {

    /**
     *
     * @param args
     *            argomenti riga di comando
     */
    private Panjea() {
    }

    /**
     * Main per avviare l'applicazione Panjea.
     *
     * @param args
     *            args
     */
    public static void main(String[] args) {
        PanjeaStart panjeaStart = new PanjeaStart();
        panjeaStart.start(args);
    }
}
