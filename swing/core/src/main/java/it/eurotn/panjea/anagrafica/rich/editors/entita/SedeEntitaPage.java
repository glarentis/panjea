package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.awt.Component;
import java.awt.Window;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.selection.dialog.ListSelectionDialog;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * Pagina contenente i dati della sede entita'.
 *
 * @author Aracno
 * @version 1.0, 13-lug-2006
 */
public class SedeEntitaPage extends FormBackedDialogPageEditor {

    private static Logger logger = Logger.getLogger(SedeEntitaPage.class);

    public static final String PAGE_ID = "sedeEntitaPage";
    private static final String CAMBIA_SEDE_PRINCILALE_DIALOG_TITLE = "cambiaSedePrincipaleDialog.title";
    private static final String CAMBIA_SEDE_PRINCILALE_DIALOG_MESSAGE = "cambiaSedePrincipaleDialog.message";

    private final IAnagraficaBD anagraficaBD;
    private IAnagraficaTabelleBD anagraficaTabelleBD;
    private SedeEntita sedeEntitaPrincipale = null;
    private OpenMapCommand openMapCommand;

    private TipoEntita tipoEntita;

    /**
     * Costruttore di default.
     *
     * @param anagraficaBD
     *            il BD per accedere ai dati della sede
     */
    public SedeEntitaPage(final IAnagraficaBD anagraficaBD) {
        super(PAGE_ID, new SedeEntitaForm(new SedeEntita()));
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * Restituisce la sede entita' principale.
     *
     * @param e
     *            exception di sedePrincipale esistente
     * @return Object
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object createAndShowCambiaSedePrincipaleDialog(SedeEntitaPrincipaleAlreadyExistException e) {

        SedeEntita sedeEntitaPrincipaleAlreadyExist = (SedeEntita) e.getSedeEntitaPrincipale();
        MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                .getService(MessageSourceAccessor.class);

        String titolo = messageSourceAccessor.getMessage(CAMBIA_SEDE_PRINCILALE_DIALOG_TITLE, new Object[] {},
                Locale.getDefault());

        Object[] parameters = new Object[] { sedeEntitaPrincipaleAlreadyExist.getSede().getIndirizzo(),
                sedeEntitaPrincipaleAlreadyExist.getSede().getDatiGeografici().getDescrizioneLocalita(),
                sedeEntitaPrincipaleAlreadyExist.getSede().getDatiGeografici().getDescrizioneNazione() };
        String messaggio = messageSourceAccessor.getMessage(CAMBIA_SEDE_PRINCILALE_DIALOG_MESSAGE, parameters,
                Locale.getDefault());

        EventList listaTipiSedeEntita = new BasicEventList();
        List<TipoSedeEntita> tipiSedeSecondari = anagraficaTabelleBD.caricaTipiSedeSecondari();
        listaTipiSedeEntita.addAll(tipiSedeSecondari);
        FilterList filterList = new FilterList(listaTipiSedeEntita);

        ListSelectionDialog listSelectionDialog = new ListSelectionDialog(titolo, (Window) null, filterList) {

            @Override
            protected void onSelect(Object selection) {
                SedeEntita sedeEntitaResult = (SedeEntita) getForm().getFormObject();
                Entita entita = sedeEntitaResult.getEntita();
                TipoSedeEntita tipoSedeEntita = (TipoSedeEntita) selection;
                anagraficaBD.cambiaSedePrincipaleEntita(sedeEntitaResult, tipoSedeEntita);

                // ricarico la sede principale entita' salvata
                sedeEntitaPrincipale = anagraficaBD.caricaSedePrincipaleEntita(entita);
                SedeEntitaPage.logger.debug("--> sede entita' principale " + sedeEntitaPrincipale);
                getForm().setFormObject(sedeEntitaPrincipale);

                PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                        LifecycleApplicationEvent.MODIFIED, sedeEntitaPrincipale);
                Application.instance().getApplicationContext().publishEvent(event);
            }

        };
        listSelectionDialog.setRenderer(new DefaultListCellRenderer() {

            private static final long serialVersionUID = -6600465552132434943L;

            @Override
            public Component getListCellRendererComponent(JList arg0, Object arg1, int arg2, boolean arg3,
                    boolean arg4) {
                JLabel label = (JLabel) super.getListCellRendererComponent(arg0, arg1, arg2, arg3, arg4);
                label.setText(((TipoSedeEntita) arg1).getCodice() + " - " + ((TipoSedeEntita) arg1).getDescrizione());
                return label;
            }
        });

        listSelectionDialog.setDescription(messaggio);
        listSelectionDialog.setCloseAction(CloseAction.DISPOSE);
        listSelectionDialog.showDialog();
        return sedeEntitaPrincipale;
    }

    @Override
    protected Object doSave() {
        try {
            SedeEntita sedeEntitaDaSalvare = (SedeEntita) getForm().getFormObject();
            SedeEntita sedeEntitaResult = anagraficaBD.salvaSedeEntita(sedeEntitaDaSalvare);
            return sedeEntitaResult;
        } catch (SedeEntitaPrincipaleAlreadyExistException e) {
            // risetto la proprieta' perche' cosi il form risulta ancora dirty e si riabilita in
            // saveCommand
            Object object = getForm().getValueModel("abilitato").getValue();
            getForm().getValueModel("abilitato").setValue(false);
            getForm().commit();
            getForm().getValueModel("abilitato").setValue(object);
            return createAndShowCambiaSedePrincipaleDialog(e);
        }
    }

    @Override
    protected AbstractCommand[] getCommand() {
        logger.debug("--> Enter getCommand");
        AbstractCommand[] commands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(), getOpenMapCommand() };
        logger.debug("--> Exit getCommand");
        return commands;
    }

    /**
     *
     * @return comand per aprire la mappa della sede
     */
    public OpenMapCommand getOpenMapCommand() {
        if (openMapCommand == null) {
            openMapCommand = new OpenMapCommand();
            openMapCommand.addCommandInterceptor(new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand arg0) {
                }

                @Override
                public boolean preExecution(ActionCommand arg0) {
                    SedeEntita sedeEntita = (SedeEntita) getForm().getFormObject();
                    arg0.addParameter(OpenMapCommand.SEDE_ANAGRAFICA, sedeEntita.getSede());
                    return true;
                }
            });
        }
        return openMapCommand;
    }

    /**
     * @return the tipoEntita
     */
    public TipoEntita getTipoEntita() {
        return tipoEntita;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
        loadData();
    }

    /**
     * @param anagraficaTabelleBD
     *            The anagraficaTabelleBD to set.
     */
    public void setAnagraficaTabelleBD(IAnagraficaTabelleBD anagraficaTabelleBD) {
        this.anagraficaTabelleBD = anagraficaTabelleBD;
    }

    @Override
    public void setFormObject(Object object) {
        logger.debug("--> Enter setFormObject");
        SedeEntita sedeEntita = (SedeEntita) object;
        if (sedeEntita != null && !sedeEntita.isNew()) {
            sedeEntita = anagraficaBD.caricaSedeEntita(sedeEntita.getId(), false);
        }
        super.setFormObject(sedeEntita);
        logger.debug("--> Exit setFormObject");
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(TipoEntita tipoEntita) {
        this.tipoEntita = tipoEntita;
        ((SedeEntitaForm) getForm()).setTipoEntita(tipoEntita);
    }
}
