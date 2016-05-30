package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import java.util.EnumMap;
import java.util.Map;

import javax.swing.AbstractButton;

import org.springframework.binding.value.support.AbstractPropertyChangePublisher;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.panjea.preventivi.util.RegoleCambioStatoAreaDocumento;
import it.eurotn.rich.command.JideToggleCommand;

public abstract class AbstractStatiAreaDocumentoCommandController<E extends IAreaDocumentoTestata, T extends Enum<T>>
        extends AbstractPropertyChangePublisher {

    /**
     * @author fattazzo
     *
     */
    private final class DefaultStatoCommandInterceptor extends ActionCommandInterceptorAdapter {
        @Override
        public void postExecution(ActionCommand command) {
            super.postExecution(command);
            updateCommands();
        }
    }

    public class StatoAreaDocumentoCommand extends JideToggleCommand {

        /**
         * stato.
         */
        private T stato;

        /**
         * Costruttore.
         *
         * @param commandId
         *            id del comando
         *
         * @param stato
         *            stato
         */
        public StatoAreaDocumentoCommand(final String commandId, final T stato) {
            super(commandId);
            setSecurityControllerId(commandId);
            RcpSupport.configure(this);
            this.stato = stato;
        }

        /**
         *
         * @return stato
         */
        public T getStato() {
            return stato;
        }

        /**
         *
         * @return true se lo stato del command corrisponde a quello dell'area documento.
         */
        protected boolean isCommandOfStatoAttuale() {
            return stato.equals(getStatoAttuale());
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(this.getId());
        }

        @Override
        protected void onSelection() {
            if (!inUpdate && !stato.equals(areaDocumento.getStato())) {
                try {
                    areaDocumento = cambiaStatoAreaDocumento(stato);
                    fireStatoAreaDocumentoChanged();
                } catch (Exception ex) {
                    MessageDialog alert = new MessageDialog("Cambio stato non riuscito.",
                            ex.getMessage());
                    alert.showDialog();
                    updateCommands();
                    return;
                } finally {
                    updateCommands();
                }
            }
        }
    }

    public static final String PROPERTY_STATO_AREA_PREVENTIVO = "propertyStatoAreaPreventivoChange";

    public static final String PROPERTY_STATO_AREA_DOCUMENTO = "propertyStatoAreaDocumento";

    // private static Logger logger =
    // Logger.getLogger(AbstractStatiAreaDocumentoCommandController.class);

    private E areaDocumento;

    private final Map<T, StatoAreaDocumentoCommand> commands;

    private RegoleCambioStatoAreaDocumento<E, T> regoleCambioStato;

    private boolean inUpdate = false;

    private ActionCommandInterceptor defaultStatoCommandInterceptor;

    /**
     *
     * @param valori
     *            Gli stati per i quali devono essere creati i controlli. Verranno visualizzati
     *            nello stesso ordine in cui si trovano nell'array.
     *
     * @param clazz
     *            La classe dell'enum contenente gli stati
     *
     * @param regoleCambioStato
     *            regole da utilizzare per il cambio stato. Determinerà quali pulsanti dovranno
     *            essere abilitati.
     */
    AbstractStatiAreaDocumentoCommandController(final T[] valori, final Class<T> clazz,
            final RegoleCambioStatoAreaDocumento<E, T> regoleCambioStato) {
        commands = new EnumMap<T, StatoAreaDocumentoCommand>(clazz);
        this.regoleCambioStato = regoleCambioStato;
        this.defaultStatoCommandInterceptor = new DefaultStatoCommandInterceptor();
        for (T valore : valori) {
            String idCommand = formatIdCommand(valore);
            StatoAreaDocumentoCommand command = new StatoAreaDocumentoCommand(idCommand, valore);
            command.addCommandInterceptor(defaultStatoCommandInterceptor);
            commands.put(valore, command);
        }
    }

    /**
     * @param stato
     *            stato corrispondente al command selezionato
     *
     * @return true se devo le operazioni collegate al cambio di stato sono andate a buon fine e
     *         deve essere l'anciato l'evento di cambio stato.
     *
     */
    protected E cambiaStatoAreaDocumento(T stato) {
        return areaDocumento;
    }

    /**
     * Dispose.
     */
    public void dispose() {

        for (StatoAreaDocumentoCommand command : commands.values()) {
            command.removeCommandInterceptor(defaultStatoCommandInterceptor);
        }

        defaultStatoCommandInterceptor = null;
    }

    /**
     * Lancia l'evento che segnala il cambio di stato dell'area documento.
     */
    private void fireStatoAreaDocumentoChanged() {
        this.firePropertyChange(PROPERTY_STATO_AREA_DOCUMENTO, null, areaDocumento);
    }

    /**
     *
     * @param stato
     *            lo stato corrispondente al comando
     * @return la stessa parola con la prima lettera maiuscola ed il resto in minuscolo, senza
     *         caratteri speciali. .
     */
    protected String formatIdCommand(T stato) {
        String word = stato.toString();
        String firstChar = word.substring(0, 1).toUpperCase();
        String restOfTheWord = word.substring(1, word.length()).toLowerCase();
        String capitalized = firstChar + restOfTheWord;
        return "stato" + capitalized.replaceAll("_", "") + "Command";
    }

    /**
     *
     * @return areaDocumento
     */
    protected E getAreaDocumento() {
        return this.areaDocumento;
    }

    /**
     * @return comandi creati per gli stati dell'area ordine
     */
    public ActionCommand[] getCommands() {
        return commands.values().toArray(new ActionCommand[commands.size()]);
    }

    /**
     *
     * @return stato dell'area documento.
     */
    @SuppressWarnings("unchecked")
    protected T getStatoAttuale() {
        return (T) areaDocumento.getStato();
    }

    /**
     *
     * @param command
     *            command da abilitare o disabilitare
     * @return true se documento salvato e se deve essere selezionato.
     */
    protected boolean isCommandToBeEnabled(final StatoAreaDocumentoCommand command) {
        if (command.isCommandOfStatoAttuale()) {
            return true;
        }

        boolean documentoPuoCambiareStato = regoleCambioStato
                .isCambioStatoPossibileSuDocumento(areaDocumento);
        if (documentoPuoCambiareStato) {
            return regoleCambioStato.isCambioStatoPossibile(getStatoAttuale(), command.getStato());
        }

        return false;
    }

    /**
     *
     * @param command
     *            command da rendere selezionabile o meno.
     * @return true se il comando deve essere selezionato.
     */
    protected boolean isCommandToBeSelected(final StatoAreaDocumentoCommand command) {
        return command.isCommandOfStatoAttuale();
    }

    /**
     * Aggiorna l'area documento gestita.
     *
     * @param areaDocumentoUpdated
     *            area ordine aggiornata
     */
    public void setAreaDocumento(E areaDocumentoUpdated) {
        this.areaDocumento = areaDocumentoUpdated;
        updateCommands();
    }

    /**
     * abilita i pulsanti e seleziona quello corrispondente allo stato.
     */
    protected final void updateCommands() {
        logger.debug("--> Enter updateCommands");
        inUpdate = true;

        try {
            for (StatoAreaDocumentoCommand command : commands.values()) {
                boolean enable = isCommandToBeEnabled(command);
                boolean selected = isCommandToBeSelected(command);
                command.setEnabled(enable);
                command.setSelected(selected);
            }
        } finally {
            inUpdate = false;
        }

        logger.debug("--> Exit updateCommands");
    }
}
