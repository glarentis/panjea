package it.eurotn.panjea.anagrafica.rich.editors.entita;

import java.awt.Dimension;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;

public class DeleteSedeEntitaCommand extends ApplicationWindowAwareCommand {

    private static final String COMMAN_ID = "deleteCommand";
    public static final String PARAM_SEDE_ENTITA = "paramSedeEntita";

    private final IAnagraficaBD anagraficaBD;

    /**
     * Costruttore.
     *
     * @param anagraficaBD
     *            anagraficaBD
     * @param page
     *            pagina che gestisce le sedi entità
     */
    public DeleteSedeEntitaCommand(final IAnagraficaBD anagraficaBD, final SediEntitaTablePage page) {
        super(COMMAN_ID);
        this.setSecurityControllerId(page.getPageSecurityEditorId() + ".controller");
        RcpSupport.configure(this);
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    protected void doExecuteCommand() {
        SedeEntita sedeEntita = (SedeEntita) getParameter(PARAM_SEDE_ENTITA);
        try {
            anagraficaBD.cancellaSedeEntita(sedeEntita, false);
            PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                    LifecycleApplicationEvent.DELETED, sedeEntita);
            Application.instance().getApplicationContext().publishEvent(event);
        } catch (SedeAnagraficaOrphanException e) {
            // nel caso in cui non ci sono altre sedi che usano la sede
            // anagrafica chiede di eliminare anche quella
            showConfirmationDialogDeleteOrphan(sedeEntita, e);
        }
    }

    /**
     * Visualizza il dialogo per conferma della cancellazione delle sedi che sono orfane.
     *
     * @param sedeEntita
     *            sede entita di riferimento
     * @param exception
     *            SedeAnagraficaOrphanException
     */
    private void showConfirmationDialogDeleteOrphan(final SedeEntita sedeEntita,
            SedeAnagraficaOrphanException exception) {
        final MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator
                .services().getService(MessageSourceAccessor.class);
        SedeAnagrafica sedeAnagraficaorphan = (SedeAnagrafica) exception.getSedeAnagraficaOrphan();
        Object[] parameters = new Object[] {
                messageSourceAccessor.getMessage(sedeEntita.getEntita().getDomainClassName(), new Object[] {},
                        Locale.getDefault()),
                "", sedeAnagraficaorphan.getDescrizione(),
                sedeAnagraficaorphan.getDatiGeografici().getDescrizioneLocalita(),
                sedeAnagraficaorphan.getDatiGeografici().getDescrizioneLivelloAmministrativo2(),
                sedeAnagraficaorphan.getDatiGeografici().getDescrizioneNazione() };

        String titolo = messageSourceAccessor.getMessage("sedeEntita.sedeAnagraficaOrphan.delete.confirm.title",
                new Object[] {}, Locale.getDefault());
        String messaggio = messageSourceAccessor.getMessage("sedeEntita.sedeAnagraficaOrphan.delete.confirm.message",
                parameters, Locale.getDefault());
        ConfirmationDialog dialog = new ConfirmationDialog(titolo, messaggio) {

            @Override
            protected void onConfirm() {
                // Catch dell'eccezione che cmq non dovrebbe essere lanciata
                // perche' cmq gia' usata nella chiamata precedente per mostrare
                // all'utente
                // la finestra per forzare la cancellazione;
                // qui catcho l'exc. perch� cmq il metodo cancella � uno solo
                // che uso
                // forzando la cancellazione con il parametro booleano.
                try {
                    anagraficaBD.cancellaSedeEntita(sedeEntita, true);
                } catch (SedeAnagraficaOrphanException e) {
                    throw new RuntimeException(e);
                }
                PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                        LifecycleApplicationEvent.DELETED, sedeEntita);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        };
        dialog.setPreferredSize(new Dimension(500, 200));
        dialog.showDialog();
    }
}