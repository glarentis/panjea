package it.eurotn.panjea.ordini.rich.editors.areaordine;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.binding.value.support.AbstractPropertyChangePublisher;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Controller/Container dei Command incaricati di eseguire la variazione dello stato di
 * {@link AreaOrdine}.<br>
 * Da questa classe la Page preleva i command per inserirli nella sua toolbar. <br>
 * La classe dovrà essere registrata al {@link IPageLifecycleAdvisor} .OBJECT_CHANGED per
 * intercettare le variazioni {@link AreaOrdineFullDTO} <br>
 * e variare di conseguenza lo stato enabled degli {@link ActionCommand} al suo interno
 *
 * @author adriano
 * @version 1.0, 09/ott/2008
 */
public class StatiAreaOrdineCommandController extends AbstractPropertyChangePublisher {

    private abstract class StatoAreaOrdineCommand extends JideToggleCommand {

        /**
         * Costruttore.
         *
         * @param commandId
         *            id del comando
         */
        public StatoAreaOrdineCommand(final String commandId) {
            super(commandId);
            setSecurityControllerId(commandId);
            RcpSupport.configure(this);
        }

        /**
         * get dello stato attuale del command.
         *
         * @return Stato
         */
        public abstract StatoAreaOrdine getStatoAreaOrdine();

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(this.getId());
        }

        /**
         * seleziona lo stato del pulsante in base allo stato dell'area.
         *
         * @param statoAreaOrdine
         *            stato dell'area ordine
         */
        public void setSelected(StatoAreaOrdine statoAreaOrdine) {
            setSelected(getStatoAreaOrdine().equals(statoAreaOrdine));
            setEnabled(getStatoAreaOrdine().equals(statoAreaOrdine));
        }
    }

    /**
     * Command per la variazione dello stato di {@link AreaOrdine} da PROVVISORIO in CONFERMATO.
     */
    public class StatoBloccatoCommand extends StatoAreaOrdineCommand {

        public static final String COMMAND_ID = "statoBloccatoCommand";

        /**
         * Costruttore.
         */
        public StatoBloccatoCommand() {
            super(COMMAND_ID);
        }

        @Override
        public StatoAreaOrdine getStatoAreaOrdine() {
            return StatoAreaOrdine.BLOCCATO;
        }
    }

    /**
     * Command per la variazione dello stato di {@link AreaOrdine} da PROVVISORIO in CONFERMATO.
     */
    public class StatoConfermatoCommand extends StatoAreaOrdineCommand {

        public static final String COMMAND_ID = "statoConfermatoCommand";

        /**
         * Costruttore.
         */
        public StatoConfermatoCommand() {
            super(COMMAND_ID);
        }

        @Override
        public StatoAreaOrdine getStatoAreaOrdine() {
            return StatoAreaOrdine.CONFERMATO;
        }
    }

    /**
     * Command per la variazione dello stato di {@link AreaOrdine} da CONFERMATO in PROVVISORIO.
     */
    public class StatoProvvisorioCommand extends StatoAreaOrdineCommand {

        public static final String COMMAND_ID = "statoProvvisorioCommand";

        /**
         * Costruttore.
         */
        public StatoProvvisorioCommand() {
            super(COMMAND_ID);
        }

        @Override
        public StatoAreaOrdine getStatoAreaOrdine() {
            return StatoAreaOrdine.PROVVISORIO;
        }

    }

    public static final String PROPERTY_STATO_AREA_ORDINE = "propertyStatoAreaOrdineChange";

    private static Logger logger = Logger.getLogger(StatiAreaOrdineCommandController.class);

    private ExclusiveCommandGroup statiCommandGroup;
    private StatoAreaOrdineCommand statoProvvisorioCommand;
    private StatoAreaOrdineCommand statoConfermatoCommand;
    private StatoAreaOrdineCommand statoBloccatoCommand;
    private AreaOrdine areaOrdine;

    /**
     * Costruttore.
     *
     */
    public StatiAreaOrdineCommandController() {
        super();
        initialize();
    }

    /**
     * @return comandi creati per gli stati dell'area ordine
     */
    public ActionCommand[] getCommands() {
        return new ActionCommand[] { getStatoProvvisorioCommand(), getStatoConfermatoCommand(),
                getStatoBloccatoCommand() };
    }

    /**
     * @return statoBloccatoCommand
     */
    public StatoAreaOrdineCommand getStatoBloccatoCommand() {
        if (statoBloccatoCommand == null) {
            statoBloccatoCommand = new StatoBloccatoCommand();
        }
        return statoBloccatoCommand;
    }

    /**
     * @return statoConfermatoCommand
     */
    public StatoAreaOrdineCommand getStatoConfermatoCommand() {
        if (statoConfermatoCommand == null) {
            statoConfermatoCommand = new StatoConfermatoCommand();
        }
        return statoConfermatoCommand;
    }

    /**
     * @return statoProvvisorioCommand
     */
    public StatoAreaOrdineCommand getStatoProvvisorioCommand() {
        if (statoProvvisorioCommand == null) {
            statoProvvisorioCommand = new StatoProvvisorioCommand();
        }
        return statoProvvisorioCommand;
    }

    /**
     * Inizializzazione dei comandi.
     */
    private void initialize() {
        statiCommandGroup = new ExclusiveCommandGroup();

        statiCommandGroup.add(getStatoProvvisorioCommand());
        statiCommandGroup.add(getStatoConfermatoCommand());
    }

    /**
     * selected dei commands all'interno di questa classe in base al valore statoAreaOrdine dell'
     * AreaOrdine corrente<br>
     * se l'area ordine è in stato di inserimento disabilita i commands.
     *
     */
    protected void setSelected() {
        logger.debug("--> Enter setSelected");
        getStatoConfermatoCommand().setEnabled(areaOrdine.getId() != null);
        getStatoProvvisorioCommand().setEnabled(areaOrdine.getId() != null);
        getStatoBloccatoCommand().setEnabled(areaOrdine.getId() != null);
        getStatoConfermatoCommand().setSelected(areaOrdine.getStatoAreaOrdine());
        getStatoProvvisorioCommand().setSelected(areaOrdine.getStatoAreaOrdine());
        getStatoBloccatoCommand().setSelected(areaOrdine.getStatoAreaOrdine());
        logger.debug("--> Exit setSelected");
    }

    /**
     * Aggiorna l'area ordine gestita.
     *
     * @param areaOrdineUpdated
     *            area ordine aggiornata
     */
    public void updateAreaOrdine(AreaOrdine areaOrdineUpdated) {
        this.areaOrdine = areaOrdineUpdated;
        setSelected();
    }

}
