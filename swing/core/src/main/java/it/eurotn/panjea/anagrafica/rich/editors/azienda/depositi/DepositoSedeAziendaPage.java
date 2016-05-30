package it.eurotn.panjea.anagrafica.rich.editors.azienda.depositi;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.editors.azienda.deposito.stampa.StampaValorizzazioneDepositoDialog;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class DepositoSedeAziendaPage extends FormBackedDialogPageEditor {

    private class StampaValorizzazioneCommand extends ActionCommand {

        private static final String COMMAND_ID = "stampaValorizzazioneCommand";

        /**
         * Costruttore.
         */
        public StampaValorizzazioneCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            Deposito deposito = (Deposito) getBackingFormPage().getFormObject();

            if (!deposito.isNew()) {
                stampaValorizzazioneDepositoDialog.setDeposito(deposito.creaLite());
                stampaValorizzazioneDepositoDialog.showDialog();
            }
        }

    }

    private static Logger logger = Logger.getLogger(DepositoSedeAziendaPage.class);
    public static final String ID = "depositoSedeAziendaPage";
    private final IAnagraficaBD anagraficaBD;
    private SedeAzienda sedeAzienda;

    private StampaValorizzazioneCommand valorizzazioneCommand;

    private StampaValorizzazioneDepositoDialog stampaValorizzazioneDepositoDialog = new StampaValorizzazioneDepositoDialog();

    /**
     * Costruttore.
     *
     * @param anagraficaBD
     *            anagraficaBD
     * @param anagraficaTabelleBD
     *            anagraficaTabelleBD
     */
    public DepositoSedeAziendaPage(final IAnagraficaBD anagraficaBD, final IAnagraficaTabelleBD anagraficaTabelleBD) {
        super(ID, new DepositoSedeAziendaForm(new Deposito(), anagraficaTabelleBD));
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    protected Object doDelete() {
        anagraficaBD.cancellaDeposito((Deposito) getBackingFormPage().getFormObject());
        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.DELETED,
                getBackingFormPage().getFormObject());
        Application.instance().getApplicationContext().publishEvent(event);
        return true;
    }

    @Override
    protected Object doSave() {
        Deposito deposito = (Deposito) getBackingFormPage().getFormObject();
        logger.debug("--> Salvo l'oggetto: " + deposito);
        if (sedeAzienda != null) {
            deposito.setSedeDeposito(sedeAzienda);
        }
        deposito = anagraficaBD.salvaDeposito(deposito);
        logger.debug("--> Oggetto salvato : " + deposito);
        return deposito;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        logger.debug("--> Enter getCommand");
        AbstractCommand[] commands = toolbarPageEditor.getDefaultCommand(true);

        commands = ArrayUtils.add(commands, getValorizzazioneCommand());
        logger.debug("--> Exit getCommand");
        return commands;
    }

    /**
     * @return AbstractCommand
     */
    public AbstractCommand getUndoCommand() {
        return toolbarPageEditor.getUndoCommand();
    }

    /**
     * @return the valorizzazioneCommand
     */
    private StampaValorizzazioneCommand getValorizzazioneCommand() {
        if (valorizzazioneCommand == null) {
            valorizzazioneCommand = new StampaValorizzazioneCommand();
        }

        return valorizzazioneCommand;
    }

    @Override
    public void loadData() {
        // Non utilizzato
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
        // Non utilizzato
    }

    @Override
    public void setFormObject(Object object) {
        Deposito deposito = anagraficaBD.caricaDeposito(((Deposito) object).getId());
        super.setFormObject(deposito);
    }

    /**
     * Setta la sede dell'azienda utilizzata per un nuovo deposito.
     *
     * @param sedeAzienda
     *            sede dell'azienda del deposito.
     */
    public void setSedeAzienda(SedeAzienda sedeAzienda) {
        this.sedeAzienda = sedeAzienda;
    }

    /**
     * @param sediAzienda
     *            the sediAzienda to set
     */
    public void setSediAzienda(List<SedeAzienda> sediAzienda) {
        ((DepositoSedeAziendaForm) getBackingFormPage()).setSediAzienda(sediAzienda);
    }
}
