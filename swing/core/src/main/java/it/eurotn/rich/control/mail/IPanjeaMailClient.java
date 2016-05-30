package it.eurotn.rich.control.mail;

import it.eurotn.panjea.anagrafica.domain.ParametriMail;
import it.eurotn.rich.report.editor.export.AbstractExportCommand;

/**
 * @author fattazzo
 *
 */
public interface IPanjeaMailClient {

    String BEAN_ID = "panjeaMailClient";

    /**
     * Invia il report indicato tramite mail nel formato gestito dall'export command.
     *
     * @param parametriMail
     *            parametri di invio mail
     * @param exportCommand
     *            export command
     */
    void send(ParametriMail parametriMail, AbstractExportCommand exportCommand);

    /**
     * Invia il report indicato tramite mail nel formato gestito dall'export command.
     *
     * @param parametriMail
     *            parametri di invio mail
     * @param exportCommand
     *            export command
     * @param throwException
     *            <code>true</code> per non gestire l'eccezione all'interno del metodo ma rilanciarla
     */
    void send(ParametriMail parametriMail, AbstractExportCommand exportCommand, boolean throwException);

    /**
     * Visualizza il dialogo per la gestione dei parametri di invio.
     *
     * @param paramParametriMail
     *            parametri email
     * @param exportCommands
     *            exportCommands
     */
    void show(ParametriMail paramParametriMail, AbstractExportCommand[] exportCommands);

}