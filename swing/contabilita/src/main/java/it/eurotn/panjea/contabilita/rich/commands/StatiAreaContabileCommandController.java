package it.eurotn.panjea.contabilita.rich.commands;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.binding.value.support.AbstractPropertyChangePublisher;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.rich.command.JideToggleCommand;

/**
 * Controller responsabile della configurazione e gestione dei command per il cambio dello stato di
 * {@link AreaContabile}.
 *
 * @author adriano
 * @version 1.0, 03/ott/07
 */
public class StatiAreaContabileCommandController extends AbstractPropertyChangePublisher {

    /**
     * {@link JideToggleCommand} responsabile dell'esecuzione del cambio di stato per l' {@link AreaContabile}.
     *
     * @author adriano
     * @version 1.0, 02/ott/07
     */
    public abstract class StatoAreaContabileCommand extends JideToggleCommand {

        /**
         * Costruttore di default.
         *
         * @param commandId
         *            l'id del command per la configurazione del command
         */
        public StatoAreaContabileCommand(final String commandId) {
            super(commandId);
            final CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            setSecurityControllerId(commandId);
            c.configure(this);
        }

        /**
         * @return decide se abilitare il command con un nuovo oggetto in editazione; false di default.
         */
        public boolean enableOnNew() {
            return false;
        }

        /**
         * Definisce la relazione tra this e gli altri stato command.
         *
         * @return array di {@link StatoAreaContabileCommand} per i quali il command e' enabled
         */
        public abstract StatoAreaContabile[] getStatiAreaContabileEnabled();

        /**
         * @return lo StatoAreaContabile attuale del command
         */
        public abstract StatoAreaContabile getStatoAreaContabile();

        /**
         * Imposta lo stato enabled del command.
         */
        public void setEnabledByStato() {
            if (areaContabile == null) {
                setEnabled(enableOnNew());
            } else {
                boolean enabled = ArrayUtils.contains(getStatiAreaContabileEnabled(),
                        areaContabile.getStatoAreaContabile());
                setEnabled(enabled);
            }

            if (this.getStatoAreaContabile().equals(areaContabile.getStatoAreaContabile())) {
                setSelected(true);
            }
        }
    }

    /**
     * {@link ActionCommand} per la variazione dello stato in confermato.
     *
     * @author adriano
     * @version 1.0, 04/ott/07
     */
    public class StatoConfermatoCommand extends StatoAreaContabileCommand {

        private final StatoAreaContabile[] statiAreaContabileEnabled = new StatoAreaContabile[] {
                StatoAreaContabile.CONFERMATO, StatoAreaContabile.VERIFICATO };

        /**
         * Costruttore di default.
         */
        public StatoConfermatoCommand() {
            super(StatiAreaContabileCommandController.STATO_CONFERMATO_COMMAND_ID);
        }

        @Override
        public StatoAreaContabile[] getStatiAreaContabileEnabled() {
            return statiAreaContabileEnabled;
        }

        @Override
        public StatoAreaContabile getStatoAreaContabile() {
            return StatoAreaContabile.CONFERMATO;
        }

        @Override
        protected void onSelection() {
            if (!areaContabile.getStatoAreaContabile().equals(getStatoAreaContabile())) {
                areaContabile = StatiAreaContabileCommandController.this.contabilitaBD
                        .cambiaStatoAreaContabileInConfermato(areaContabile);
                try {
                    areaContabile = StatiAreaContabileCommandController.this.contabilitaBD
                            .salvaAreaContabile(areaContabile);
                } catch (AreaContabileDuplicateException e) {
                    LOGGER.error("Area contabile duplicata dopo il cambio stato a Confermato!");
                    throw new RuntimeException(e);
                } catch (DocumentoDuplicateException e) {
                    LOGGER.error("Documento duplicato dopo il cambio stato a Confermato!");
                    throw new RuntimeException(e);
                }
                StatiAreaContabileCommandController.this.fireStatoAreaContabileChanged(areaContabile);
            }
        }
    }

    /**
     * {@link ActionCommand} per la variazione dello stato in Provvisorio.
     *
     * @author adriano
     * @version 1.0, 04/ott/07
     */
    public class StatoProvvisorioCommand extends StatoAreaContabileCommand {

        private final StatoAreaContabile[] statiAreaContabileEnabled = new StatoAreaContabile[] {
                StatoAreaContabile.SIMULATO, StatoAreaContabile.PROVVISORIO, StatoAreaContabile.CONFERMATO };

        /**
         * Costruttore di default.
         */
        public StatoProvvisorioCommand() {
            super(StatiAreaContabileCommandController.STATO_PROVVISORIO_COMMAND_ID);

        }

        @Override
        public boolean enableOnNew() {
            return false;
        }

        @Override
        public StatoAreaContabile[] getStatiAreaContabileEnabled() {
            return statiAreaContabileEnabled;
        }

        @Override
        public StatoAreaContabile getStatoAreaContabile() {
            return StatoAreaContabile.PROVVISORIO;
        }

        @Override
        protected void onSelection() {
            if (!areaContabile.getStatoAreaContabile().equals(getStatoAreaContabile())) {
                areaContabile = StatiAreaContabileCommandController.this.contabilitaBD
                        .cambiaStatoAreaContabileInProvvisorio(areaContabile);
                StatiAreaContabileCommandController.this.fireStatoAreaContabileChanged(areaContabile);
            }
        }
    }

    /**
     * {@link ActionCommand} per la variazione dello stato in Simulato.
     *
     * @author adriano
     * @version 1.0, 04/ott/07
     */
    public class StatoSimulatoCommand extends StatoAreaContabileCommand {

        private final StatoAreaContabile[] statiAreaContabileEnabled = new StatoAreaContabile[] {
                StatoAreaContabile.PROVVISORIO, StatoAreaContabile.SIMULATO };

        /**
         * Costruttore di default.
         */
        public StatoSimulatoCommand() {
            super(StatiAreaContabileCommandController.STATO_SIMULATO_COMMAND_ID);
        }

        @Override
        public boolean enableOnNew() {
            return false;
        }

        @Override
        public StatoAreaContabile[] getStatiAreaContabileEnabled() {
            return statiAreaContabileEnabled;
        }

        @Override
        public StatoAreaContabile getStatoAreaContabile() {
            return StatoAreaContabile.SIMULATO;
        }

        @Override
        protected void onSelection() {
            if (!areaContabile.getStatoAreaContabile().equals(getStatoAreaContabile())) {
                areaContabile = StatiAreaContabileCommandController.this.contabilitaBD
                        .cambiaStatoAreaContabileInSimulato(areaContabile);
                StatiAreaContabileCommandController.this.fireStatoAreaContabileChanged(areaContabile);
            }
        }

    }

    /**
     * {@link ActionCommand} per la variazione dello stato in Verificato.
     *
     * @author adriano
     * @version 1.0, 04/ott/07
     */
    public class StatoVerificatoCommand extends StatoAreaContabileCommand {

        private final StatoAreaContabile[] statiAreaContabileEnabled = new StatoAreaContabile[] {
                StatoAreaContabile.CONFERMATO, StatoAreaContabile.VERIFICATO };

        /**
         * Costruttore di default.
         */
        public StatoVerificatoCommand() {
            super(StatiAreaContabileCommandController.STATO_VERIFICATO_COMMAND_ID);
        }

        @Override
        public StatoAreaContabile[] getStatiAreaContabileEnabled() {
            return statiAreaContabileEnabled;
        }

        @Override
        public StatoAreaContabile getStatoAreaContabile() {
            return StatoAreaContabile.VERIFICATO;
        }

        @Override
        protected void onSelection() {
            if (!areaContabile.getStatoAreaContabile().equals(getStatoAreaContabile())) {
                areaContabile = StatiAreaContabileCommandController.this.contabilitaBD
                        .cambiaStatoAreaContabileInVerificato(areaContabile);
                try {
                    areaContabile = StatiAreaContabileCommandController.this.contabilitaBD
                            .salvaAreaContabile(areaContabile);
                } catch (AreaContabileDuplicateException e) {
                    LOGGER.error("Area contabile duplicata dopo il cambio stato a Confermato!");
                    throw new RuntimeException(e);
                } catch (DocumentoDuplicateException e) {
                    LOGGER.error("Documento duplicato dopo il cambio stato a Confermato!");
                    throw new RuntimeException(e);
                }
                StatiAreaContabileCommandController.this.fireStatoAreaContabileChanged(areaContabile);
            }
        }

    }

    private static final Logger LOGGER = Logger.getLogger(StatiAreaContabileCommandController.class);

    public static final String PROPERTY_STATO_AREA_CONTABILE = "propertyStatoAreaContabile";
    private static final String STATO_SIMULATO_COMMAND_ID = "statoSimulatoCommand";
    private static final String STATO_PROVVISORIO_COMMAND_ID = "statoProvvisorioCommand";
    private static final String STATO_CONFERMATO_COMMAND_ID = "statoConfermatoCommand";
    private static final String STATO_VERIFICATO_COMMAND_ID = "statoVerificatoCommand";
    private StatoSimulatoCommand statoSimulatoCommand;
    private StatoProvvisorioCommand statoProvvisorioCommand;
    private StatoConfermatoCommand statoConfermatoCommand;
    private StatoVerificatoCommand statoVerificatoCommmand;

    private ExclusiveCommandGroup statiAreaContabileCommandGroup;

    private StatoAreaContabileCommand[] statiAreaContabileCommand;

    private IContabilitaBD contabilitaBD;

    private AreaContabile areaContabile;

    /**
     * Costruttore di default.
     *
     * @param areaContabile
     *            l'area contabile corrente per il gruppo di commands.
     */
    public StatiAreaContabileCommandController(final AreaContabile areaContabile) {
        initialize(areaContabile);
    }

    /**
     * Metodo che abilita/disabilita i command di stato a seconda del valore di {@link StatoAreaContabile} come
     * argomento se l'argomento forcedDisabled è true disabilita i command.
     *
     * @param forcedDisabled
     *            forza lo stato disabled a tutti i command
     */
    private void enabledStatiCommand(boolean forcedDisabled) {
        for (StatoAreaContabileCommand statoAreaContabileCommand : statiAreaContabileCommand) {
            if (forcedDisabled) {
                statoAreaContabileCommand.setEnabled(false);
            } else {
                statoAreaContabileCommand.setEnabledByStato();
            }
        }
    }

    /**
     * Notifica il cambio di area contabile.
     *
     * @param paramAreaContabile
     *            l'area contabile aggiornata da inviare
     */
    private void fireStatoAreaContabileChanged(AreaContabile paramAreaContabile) {
        LOGGER.debug("--> Enter doExecuteCommand");
        enabledStatiCommand(false);
        firePropertyChange(PROPERTY_STATO_AREA_CONTABILE, null, paramAreaContabile);
        LOGGER.debug("--> Exit doExecuteCommand");
    }

    /**
     * @return i commands di questo controller; in ordine i commands per gli stati: Simulato,Provvisorio,Confermato e
     *         Verificato
     */
    public ActionCommand[] getCommands() {
        return new ActionCommand[] { getStatoSimulatoCommand(), getStatoProvvisorioCommand(),
                getStatoConfermatoCommand(), getStatoVerificatoCommmand() };
    }

    /**
     * @return Returns the statiAreaContabileCommand.
     */
    public StatoAreaContabileCommand[] getStatiAreaContabileCommand() {
        return statiAreaContabileCommand;
    }

    /**
     * @return Returns the statiAreaContabileCommandGroup.
     */
    public ExclusiveCommandGroup getStatiAreaContabileCommandGroup() {
        return statiAreaContabileCommandGroup;
    }

    /**
     * @return Returns the statoConfermatoCommand.
     */
    public StatoConfermatoCommand getStatoConfermatoCommand() {
        return statoConfermatoCommand;
    }

    /**
     * @return Returns the statoProvvisorioCommand.
     */
    public StatoProvvisorioCommand getStatoProvvisorioCommand() {
        return statoProvvisorioCommand;
    }

    /**
     * @return Returns the statoSimulatoCommand.
     */
    public StatoSimulatoCommand getStatoSimulatoCommand() {
        return statoSimulatoCommand;
    }

    /**
     * @return Returns the statoVerificatoCommmand.
     */
    public StatoVerificatoCommand getStatoVerificatoCommmand() {
        return statoVerificatoCommmand;
    }

    /**
     * Init del controller con l'area contabile passata.
     *
     * @param paramAreaContabile
     *            l'area contabile su cui definire il controller
     */
    private void initialize(AreaContabile paramAreaContabile) {

        this.areaContabile = paramAreaContabile;

        statiAreaContabileCommandGroup = new ExclusiveCommandGroup();

        statoSimulatoCommand = new StatoSimulatoCommand();
        statiAreaContabileCommandGroup.add(statoSimulatoCommand);

        statoProvvisorioCommand = new StatoProvvisorioCommand();
        statiAreaContabileCommandGroup.add(statoProvvisorioCommand);

        statoConfermatoCommand = new StatoConfermatoCommand();
        statiAreaContabileCommandGroup.add(statoConfermatoCommand);

        statoVerificatoCommmand = new StatoVerificatoCommand();
        statiAreaContabileCommandGroup.add(statoVerificatoCommmand);

        statiAreaContabileCommand = new StatoAreaContabileCommand[] { statoSimulatoCommand, statoProvvisorioCommand,
                statoConfermatoCommand, statoVerificatoCommmand };

        enabledStatiCommand(false);
    }

    /**
     * @param contabilitaBD
     *            The contabilitaBD to set.
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    /**
     * @param paramAreaContabile
     *            area contabile con cui aggiornare lo stato del controller
     */
    public void updateAreaContabile(AreaContabile paramAreaContabile) {
        this.areaContabile = paramAreaContabile;
        enabledStatiCommand(false);
    }

}
