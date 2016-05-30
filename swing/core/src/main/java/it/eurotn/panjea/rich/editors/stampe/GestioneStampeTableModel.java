package it.eurotn.panjea.rich.editors.stampe;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.bd.ILayoutStampeBD;
import it.eurotn.panjea.rich.bd.LayoutStampeBD;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.editor.CodeEditorContextSensiviteEditorFactory;
import it.eurotn.rich.control.table.editor.PrinterContextSensitiveEditorFactory;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class GestioneStampeTableModel extends DefaultBeanTableModel<LayoutStampa> {

    private static final long serialVersionUID = 1601307199053986653L;

    private ILayoutStampeManager layoutStampeManager;
    private ILayoutStampeBD layoutStampeBD;

    /**
     * Costruttore.
     */
    public GestioneStampeTableModel() {
        super("gestioneStampeTableModel", new String[] { "predefinito", "reportName", "stampante", "formulaNumeroCopie",
                "soloTesto", "batch", "confermaNumeroCopie", "preview" }, LayoutStampa.class);
        this.layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
        this.layoutStampeBD = RcpSupport.getBean(LayoutStampeBD.BEAN_ID);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        default:
            return null;
        }
    }

    @Override
    public EditorContext getEditorContextAt(int row, int column) {
        switch (column) {
        case 0:
            return GestioneStampeButtonsCellEditorRenderer.CONTEXT;
        case 1:
            return ReportNameHyperlinkCellRenderer.CONTEXT;
        case 2:
            return PrinterContextSensitiveEditorFactory.CONTEXT;
        case 3:
            EditorContext editorContext = CodeEditorContextSensiviteEditorFactory.CONTEXT;
            editorContext.setUserObject(getElementAt(row).getPossibleVariables());
            return editorContext;
        default:
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
        case 0:
        case 2:
        case 3:
            return true;
        default:
            return false;
        }
    }

    @Override
    public void removeObject(LayoutStampa object) {
        layoutStampeBD.cancellaLayoutStampa(object);
        super.removeObject(object);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        super.setValueAt(value, row, col);
        LayoutStampa layoutStampa = getObject(row);
        PanjeaLifecycleApplicationEvent event;
        switch (col) {
        case 2:
            layoutStampeManager.salvaAssociazioneStampante(layoutStampa.getChiave(), layoutStampa.getStampante());
            event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
            Application.instance().getApplicationContext().publishEvent(event);
            break;
        case 0:
            // in questo caso devo cambiare il report predefinito per il tipo area quindi una volta cambiato sostituisco
            // tutti i layout del tipo area per aggiornarli ( il predecente layout predefinito ora non lo è più)
            // List<LayoutStampa> layouts = layoutStampeManager.setLayoutAsDefault(layoutStampa);
            //
            // for (LayoutStampa layout : layouts) {
            // event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
            // Application.instance().getApplicationContext().publishEvent(event);
            //
            // int index = this.getObjects().indexOf(layout);
            // if (index != -1) {
            // setObject(layout, index);
            // }
            // }
            break;
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
            String stampante = layoutStampa.getStampante();
            layoutStampa = layoutStampeBD.salvaLayoutStampa(layoutStampa);
            layoutStampa.setStampante(stampante);
            event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
            Application.instance().getApplicationContext().publishEvent(event);
            setObject(layoutStampa, row);
            break;
        default:
            break;
        }
    }

}
