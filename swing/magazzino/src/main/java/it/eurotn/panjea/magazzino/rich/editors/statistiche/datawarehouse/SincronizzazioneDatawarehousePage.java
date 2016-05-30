/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.statistiche.datawarehouse;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.binding.form.support.DefaultFormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.settings.Settings;

import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactoryProvider;
import it.eurotn.rich.editors.IEditorCommands;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Pagina che contiene tutti i controlli per la sincronizzazione di tutti i dati del datawarehouse.
 *
 * @author fattazzo
 */
public class SincronizzazioneDatawarehousePage extends AbstractDialogPage
        implements IEditorCommands, IPageLifecycleAdvisor {

    private static final int MESSAGE_DIALOG_HEIGHT = 70;

    private static final int MESSAGE_DIALOG_WIDTH = 300;

    public static final String PAGE_ID = "sincronizzazioneDatawarehousePage";

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    private JDateChooser dateChooser;

    /**
     * Costruttore di default.
     */
    protected SincronizzazioneDatawarehousePage() {
        super(PAGE_ID);
    }

    /**
     * Crea tutti i controlli per l'aggiornamento dei dati delle anagrafiche.
     *
     * @return Controlli creati
     */
    private JComponent createAnagraficheControl() {

        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));

        JButton button = getComponentFactory().createButton("Aggiorna");
        button.setAction(new AbstractAction() {
            private static final long serialVersionUID = -3358739656486375665L;

            @Override
            public void actionPerformed(ActionEvent e) {
                magazzinoAnagraficaBD.sincronizzaAnagrafiche();
                openMessageDialog("Sincronizzazione conclusa.", "Sincronizzazione anagrafiche eseguita con successo.");
            }
        });
        button.setText("Aggiorna anagrafiche");
        button.setIcon(getIconSource().getIcon("refreshCommand.icon"));

        panel.add(button);

        return panel;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.richclient.dialog.AbstractDialogPage#createControl()
     */
    @Override
    protected JComponent createControl() {

        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((PanjeaSwingBindingFactoryProvider) Application
                .services().getService(BindingFactoryProvider.class)).getBindingFactory(new DefaultFormModel());
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");

        builder.row();
        builder.addSeparator("Date");
        builder.row();
        builder.getLayoutBuilder().cell(createDimensionedataControl());
        builder.row();
        builder.addSeparator("Anagrafiche");
        builder.row();
        builder.getLayoutBuilder().cell(createAnagraficheControl());
        builder.row();
        builder.addSeparator("Movimenti di magazzino");
        builder.row();
        builder.getLayoutBuilder().cell(createMovimentiMagazzinoControl());
        builder.row();
        builder.addSeparator("Anagrafiche gestione documentale");
        builder.row();
        builder.getLayoutBuilder().cell(createDMSControl());

        builder.row();

        return builder.getForm();
    }

    /**
     * Crea tutti i controlli per l'aggiornamento dei dati della dimensione data.
     *
     * @return Controlli creati
     */
    private JComponent createDimensionedataControl() {

        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));

        JButton button = getComponentFactory().createButton("Aggiorna");
        button.setAction(new AbstractAction() {
            private static final long serialVersionUID = -3358739656486375665L;

            @Override
            public void actionPerformed(ActionEvent e) {
                magazzinoAnagraficaBD.sincronizzaDimensionedata();
                openMessageDialog("Sincronizzazione conclusa.", "Sincronizzazione date eseguita con successo.");
            }
        });
        button.setText("Aggiorna date");
        button.setIcon(getIconSource().getIcon("refreshCommand.icon"));

        panel.add(button);

        return panel;
    }

    /**
     * Crea tutti i controlli per l'aggiornamento degli attributi nel DMS
     *
     * @return Controlli creati
     */
    private JComponent createDMSControl() {

        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));

        JButton button = getComponentFactory().createButton("Aggiorna");
        button.setAction(new AbstractAction() {
            private static final long serialVersionUID = -3358739656486375665L;

            @Override
            public void actionPerformed(ActionEvent event) {
                magazzinoAnagraficaBD.sincronizzaDMS();
                openMessageDialog("Sincronizzazione conclusa.",
                        "La sincronizzazione della gestione documentale in esecuzione. E' possibile continuare ad usare panjea.");
            }
        });
        button.setText("Aggiorna anagrafiche DMS");
        button.setIcon(getIconSource().getIcon("refreshCommand.icon"));

        panel.add(button);

        return panel;
    }

    /**
     * Crea tutti i controlli per l'aggiornamento dei dati dei movimenti magazzino.
     *
     * @return pannello con i controlli dei movimenti
     */
    private JComponent createMovimentiMagazzinoControl() {

        JButton button = getComponentFactory().createButton("Aggiorna");
        button.setAction(new AbstractAction() {
            private static final long serialVersionUID = -3358739656486375665L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (dateChooser.getDate() != null) {
                    magazzinoAnagraficaBD.asyncAggiornaMovimenti(dateChooser.getDate());
                    openMessageDialog("Sincronizzazione conclusa.",
                            "Sincronizzazione movimenti di magazzino eseguita con successo.");
                }
            }
        });
        button.setText("Aggiorna movimenti di magazzino");
        button.setIcon(getIconSource().getIcon("refreshCommand.icon"));

        dateChooser = new JDateChooser(Calendar.getInstance().getTime());
        dateChooser.setDateFormatString("dd/MM/yyyy");

        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(getComponentFactory().createLabel("Data inizio aggiornamento"));
        panel.add(dateChooser);
        panel.add(button);

        return panel;
    }

    @Override
    public void dispose() {
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        return null;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return false;
    }

    /**
     * Metodo di comodità che apre un dialogo con titolo e messaggio passati come parametri.
     *
     * @param title
     *            titolo del dialogo
     * @param message
     *            messaggio del dialogo
     */
    private void openMessageDialog(String title, String message) {
        MessageDialog dialog = new MessageDialog(title, message);
        dialog.setModal(true);
        dialog.setPreferredSize(new Dimension(MESSAGE_DIALOG_WIDTH, MESSAGE_DIALOG_HEIGHT));
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.showDialog();
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    @Override
    public void setFormObject(Object object) {
    }

    /**
     *
     * @param magazzinoAnagraficaBD
     *            magazzinoAnagraficaBD
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}
