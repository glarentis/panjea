package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.ArticoliRicercaTablePage;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo.CustomFilter;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class ArticoliTablePage extends ArticoliRicercaTablePage implements Observer, PropertyChangeListener {

    private class FiltroPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            categoriaChanged = true;
            loadData();
        }

    }

    private class RefreshArticoliActionCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            // forzo il cambio della categoria perchè in questo modo la loadTableData ricarica
            // sempre gli articoli
            categoriaChanged = true;
            return super.preExecution(command);
        }

    }

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
    private RefreshArticoliActionCommandInterceptor refreshArticoliActionCommandInterceptor = null;

    private ParametriRicercaArticoloForm parametriRicercaArticoloForm = null;
    private CustomFilter customFilter = null;
    private ArticoloCategoriaDTO articoloCategoriaDTO = null;

    private boolean categoriaChanged = true;

    private FiltroPropertyChangeListener filtroPropertyChangeListener = null;

    /**
     * Costruttore.
     */
    public ArticoliTablePage() {
        super();
        this.getTable().setDelayForSelection(200);
        getTable().getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        getTable().getTable().setRowSelectionAllowed(true);

        getRefreshCommand().addCommandInterceptor(getRefreshArticoliActionCommandInterceptor());
    }

    @Override
    public void dispose() {
        if (parametriRicercaArticoloForm != null) {
            parametriRicercaArticoloForm.dispose();
        }
        getRefreshCommand().removeCommandInterceptor(getRefreshArticoliActionCommandInterceptor());

        super.dispose();
    }

    /**
     * @return Articolo corrente
     */
    public Articolo getArticoloCorrente() {
        Articolo art = null;
        if (articoloCategoriaDTO != null) {
            art = articoloCategoriaDTO.getArticolo();
        }
        return art;
    }

    /**
     * @return Categoria corrente
     */
    public Categoria getCategoriaCorrente() {
        Categoria cat = null;
        if (articoloCategoriaDTO != null) {
            cat = articoloCategoriaDTO.getCategoria();
        }
        return cat;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getRefreshCommand() };
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
    public JComponent getHeaderControl() {
        if (parametriRicercaArticoloForm == null) {
            parametriRicercaArticoloForm = new ParametriRicercaArticoloForm("ricercaArticoliForm");
            parametriRicercaArticoloForm
                    .setDownCommand(getTable().getNavigationCommands()[JideTableWidget.NAVIGATE_NEXT]);
            parametriRicercaArticoloForm
                    .setUpCommand(getTable().getNavigationCommands()[JideTableWidget.NAVIGATE_PREVIOUS]);

            filtroPropertyChangeListener = new FiltroPropertyChangeListener();
            parametriRicercaArticoloForm.getValueModel("filtro").addValueChangeListener(filtroPropertyChangeListener);
            parametriRicercaArticoloForm.getValueModel("statoArticolo")
                    .addValueChangeListener(filtroPropertyChangeListener);
            parametriRicercaArticoloForm.getValueModel("soloDistinte")
                    .addValueChangeListener(filtroPropertyChangeListener);
            parametriRicercaArticoloForm.getValueModel("includiArticoliCategorieFiglie")
                    .addValueChangeListener(filtroPropertyChangeListener);
            parametriRicercaArticoloForm.getValueModel("cliente").addValueChangeListener(filtroPropertyChangeListener);
            parametriRicercaArticoloForm.getValueModel("calcolaGiacenza")
                    .addValueChangeListener(filtroPropertyChangeListener);

        }
        return parametriRicercaArticoloForm.getControl();
    }

    /**
     * @return Returns the magazzinoAnagraficaBD.
     */
    @Override
    public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
        return magazzinoAnagraficaBD;
    }

    /**
     * @return ArticoloCategoriaDTO
     */
    protected ParametriRicercaArticolo getParametriRicercaArticolo() {
        return (ParametriRicercaArticolo) this.parametriRicercaArticoloForm.getFormObject();
    }

    /**
     * @return RefreshArticoliActionCommandInterceptor
     */
    private RefreshArticoliActionCommandInterceptor getRefreshArticoliActionCommandInterceptor() {
        if (refreshArticoliActionCommandInterceptor == null) {
            refreshArticoliActionCommandInterceptor = new RefreshArticoliActionCommandInterceptor();
        }
        return refreshArticoliActionCommandInterceptor;
    }

    @Override
    public void grabFocus() {
        // se prende il focus la text field del filtro non devo spostare il focus alla tabella,
        // quindi non chiamo la super
        if (parametriRicercaArticoloForm.isFiltroFocusOwner()) {
            return;
        }
        super.grabFocus();
    }

    @Override
    public List<ArticoloRicerca> loadTableData() {
        List<ArticoloRicerca> articoliRicerca = getTable().getRows();

        if (categoriaChanged) {
            // List<ArticoloRicerca> articoliList = new ArrayList<ArticoloRicerca>();
            // setRows(articoliList);

            ParametriRicercaArticolo parametriRicercaArticolo = getParametriRicercaArticolo();
            parametriRicercaArticolo.setRicercaCodiceOrDescrizione(true);
            parametriRicercaArticolo.setCustomFilter(customFilter);
            AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
            parametriRicercaArticolo.setIdDeposito(aziendaCorrente.getDepositoPrincipale().getId());
            articoliRicerca = magazzinoAnagraficaBD.ricercaArticoli(parametriRicercaArticolo);
        }

        return articoliRicerca;
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        super.onEditorEvent(event);
        // Se è stato cancellato l'ultimo articolo devo rispedire un messaggio
        // all'editor con un articolo a null
        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
        if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED) && getTable().getRows().size() == 0) {
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    new ArticoloCategoriaDTO(null, getCategoriaCorrente()));
        }
    }

    @Override
    public void onPostPageOpen() {
        parametriRicercaArticoloForm.grabFocus();
        super.onPostPageOpen();
    }

    @Override
    public void postSetFormObject(Object object) {
        categoriaChanged = false;
        ArticoloCategoriaDTO articoloCategoriaDTONew = (ArticoloCategoriaDTO) object;
        if ((articoloCategoriaDTONew.getCategoria() != null
                && !articoloCategoriaDTONew.getCategoria().equals(getCategoriaCorrente()))
                || (articoloCategoriaDTONew.getCategoria() != null && articoloCategoriaDTONew.getArticolo() != null)) {

            ParametriRicercaArticolo parametriRicercaArticolo = (ParametriRicercaArticolo) parametriRicercaArticoloForm
                    .getFormObject();
            parametriRicercaArticolo.setIdCategoria(articoloCategoriaDTONew.getCategoria().getId());
            parametriRicercaArticolo.setCustomFilter(customFilter);
            parametriRicercaArticoloForm.setFormObject(parametriRicercaArticolo);
            articoloCategoriaDTO = articoloCategoriaDTONew;
            categoriaChanged = true;
        }

        // articoloCategoriaDTO = articoloCategoriaDTONew;

        // se arriva una categoria null e un articolo so che devo aggiungere/aggiornare l'articolo
        // nella tabella
        if (articoloCategoriaDTONew.getCategoria() == null && articoloCategoriaDTONew.getArticolo() != null) {
            Articolo articolo = articoloCategoriaDTONew.getArticolo();
            if (articolo.getId() != null) {
                ArticoloRicerca articoloRicerca = articolo.getArticoloRicerca();
                getTable().replaceOrAddRowObject(articoloRicerca, articoloRicerca, this);
            }
        }
    }

    @Override
    public void processTableData(Collection<ArticoloRicerca> results) {
        if (getTable() == null) {
            return;
        }
        ArticoloRicerca articoloRicerca = getTable().getSelectedObject();
        super.processTableData(results);

        if (categoriaChanged) {
            // Seleziono il primo per mandare l'evento di articolo selezionato.
            if (!(getCategoriaCorrente() != null && articoloCategoriaDTO.getArticolo() != null)) {
                update(null, null);
            }
        }

        // se ho un articolo e categoria provengo da una searchObject, quindi
        // devo solamente selezionare l'articolo
        if (getCategoriaCorrente() != null && articoloCategoriaDTO.getArticolo() != null) {
            Articolo articolo = articoloCategoriaDTO.getArticolo();
            getTable().selectRowObject(articolo.getArticoloRicerca(), this);
        } else {
            if (articoloRicerca != null) {
                getTable().selectRowObject(articoloRicerca, this);
            }
        }
    }

    /**
     * @param categoriaChanged
     *            the categoriaChanged to set
     */
    protected void setCategoriaChanged(boolean categoriaChanged) {
        this.categoriaChanged = categoriaChanged;
    }

    /**
     * @param customFilter
     *            the customFilter to set
     */
    public void setCustomFilter(CustomFilter customFilter) {
        this.customFilter = customFilter;
    }

    /**
     * @param magazzinoAnagraficaBD
     *            The magazzinoAnagraficaBD to set.
     */
    @Override
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

}
