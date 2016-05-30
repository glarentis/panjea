package it.eurotn.panjea.magazzino.rich.editors.listino;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.search.SedeMagazzinoSearchObject;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ListinoSediCollegatePage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class AssociaSediCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "associaSediCommand";

        /**
         * Costruttore.
         */
        public AssociaSediCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            sedeListinoPMForm.commit();
            SedeListiniPM sedeListiniPM = (SedeListiniPM) sedeListinoPMForm.getFormObject();
            if ((!sedeListiniPM.isListinoAlternativoAssociatoForm() && !sedeListiniPM.isListinoAssociatoForm())
                    || sedeListiniPM.getEntitaForm().isNew()) {
                return;
            }

            List<SedeMagazzino> sediDaSalvare = new ArrayList<SedeMagazzino>();

            // Se ho selezionato una sede magazzino associo solamente quella
            // altrimenti associo tutte le sedi dell'entità
            if (sedeListiniPM.getSedeMagazzinoForm().getId() != null) {
                sediDaSalvare.add(magazzinoAnagraficaBD
                        .caricaSedeMagazzinoBySedeEntita(sedeListiniPM.getSedeMagazzinoForm().getSedeEntita(), false));
            } else {
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put(SedeMagazzinoSearchObject.PARAMETER_ENTITA_ID, sedeListiniPM.getEntitaForm());
                sediDaSalvare = magazzinoAnagraficaBD.caricaSediMagazzino(parameters, false);
            }

            for (SedeMagazzino sedeMagazzino : sediDaSalvare) {
                if (sedeListiniPM.isListinoAssociatoForm()) {
                    sedeMagazzino.setListino(listino);
                }
                if (sedeListiniPM.isListinoAlternativoAssociatoForm()) {
                    sedeMagazzino.setListinoAlternativo(listino);
                }
            }

            magazzinoAnagraficaBD.salvaSediMagazzino(sediDaSalvare);

            sedeListinoPMForm.getNewFormObjectCommand().execute();
            loadData();
            sedeListinoPMForm.getControl().requestFocusInWindow();
        }
    }

    public static final String PAGE_ID = "listinoSediCollegatePage";

    private SedeListinoPMForm sedeListinoPMForm;

    private SediListiniPMTable sediListiniPMTable;

    private Listino listino;

    private AssociaSediCommand associaSediCommand;

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    /**
     * Costruttore.
     * 
     */
    protected ListinoSediCollegatePage() {
        super(PAGE_ID);
    }

    @Override
    protected JComponent createControl() {
        sedeListinoPMForm = new SedeListinoPMForm();
        sediListiniPMTable = new SediListiniPMTable();
        sediListiniPMTable.getTable()
                .addMouseListener(new SelectRowListener(this, sediListiniPMTable, magazzinoAnagraficaBD));

        JPanel inserimentoPanel = getComponentFactory().createPanel(new BorderLayout());
        inserimentoPanel.add(sedeListinoPMForm.getControl(), BorderLayout.WEST);
        JPanel buttonPanel = getComponentFactory().createPanel(new BorderLayout());
        buttonPanel.add(getAssociaSediCommand().createButton(), BorderLayout.SOUTH);
        inserimentoPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 20));
        rootPanel.add(inserimentoPanel, BorderLayout.NORTH);
        rootPanel.add(sediListiniPMTable.getComponent(), BorderLayout.CENTER);

        return rootPanel;
    }

    @Override
    public void dispose() {

    }

    /**
     * @return the associaSediCommand
     */
    public AssociaSediCommand getAssociaSediCommand() {
        if (associaSediCommand == null) {
            associaSediCommand = new AssociaSediCommand();
        }

        return associaSediCommand;
    }

    @Override
    public void loadData() {
        // Creo un listino nuovo per non passare un listino con tutte le proprietà al layer più
        // basso
        sediListiniPMTable.getOverlayTable().startSearch();
        try {
            Listino listinoProxy = new Listino();
            listinoProxy.setId(listino.getId());
            List<SedeListiniPM> result = new ArrayList<SedeListiniPM>();

            // se non ho un listino presente non devo caricare le sedi collegate o verranno cercate
            // tutte
            // listino id null
            if (listino.getId() != null) {
                List<RiepilogoSedeEntitaDTO> sedi = magazzinoAnagraficaBD.caricaSediMagazzinoByListino(listinoProxy);

                for (RiepilogoSedeEntitaDTO riepilogoSede : sedi) {
                    SedeListiniPM sedeListiniPM = new SedeListiniPM(riepilogoSede, listino);
                    result.add(sedeListiniPM);
                }
            }
            sediListiniPMTable.setRows(result);
            setReadOnly(listino.isNew());
        } finally {
            sediListiniPMTable.getOverlayTable().stopSearch();
        }
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {

    }

    @Override
    public void preSetFormObject(Object object) {

    }

    @Override
    public void refreshData() {
        if (listino.isNew()) {
            sediListiniPMTable.setRows(new ArrayList<SedeListiniPM>());
        }
        setReadOnly(listino.isNew());
    }

    @Override
    public void restoreState(Settings arg0) {
        sediListiniPMTable.restoreState(arg0);
    }

    @Override
    public void saveState(Settings arg0) {
        sediListiniPMTable.saveState(arg0);
    }

    @Override
    public void setFormObject(Object object) {
        this.listino = (Listino) object;
    }

    /**
     * @param magazzinoAnagraficaBD
     *            the magazzinoAnagraficaBD to set
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        sedeListinoPMForm.getFormModel().setReadOnly(readOnly);
    }

}
