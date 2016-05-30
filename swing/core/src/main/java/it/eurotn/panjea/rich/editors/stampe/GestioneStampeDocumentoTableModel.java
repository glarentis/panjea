package it.eurotn.panjea.rich.editors.stampe;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.bd.ILayoutStampeBD;
import it.eurotn.panjea.rich.bd.LayoutStampeBD;
import it.eurotn.panjea.rich.converter.I18NConverterContext;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.editor.CodeEditorContextSensiviteEditorFactory;
import it.eurotn.rich.control.table.editor.PrinterContextSensitiveEditorFactory;

import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class GestioneStampeDocumentoTableModel extends DefaultBeanTableModel<LayoutStampaDocumento> {

    private static final long serialVersionUID = 1601307199053986653L;

    private static I18NConverterContext context;

    private ILayoutStampeManager layoutStampeManager;
    private ILayoutStampeBD layoutStampeBD;

    static {
        context = new I18NConverterContext();
    }

    /**
     * Costruttore.
     */
    public GestioneStampeDocumentoTableModel() {
        super("gestioneStampeDocumentoTableModel",
                new String[] { "tipoDocumento.classeTipoDocumento", "tipoDocumento", "entita", "sedeEntita",
                        "predefinito", "reportName", "stampante", "formulaNumeroCopie", "soloTesto", "batch",
                        "confermaNumeroCopie", "preview", "mailLayout", "interno" },
                LayoutStampaDocumento.class);
        this.layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
        this.layoutStampeBD = RcpSupport.getBean(LayoutStampeBD.BEAN_ID);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 0:
            return context;
        default:
            return null;
        }
    }

    @Override
    public EditorContext getEditorContextAt(int row, int column) {
        switch (column) {
        case 4:
            return GestioneStampeButtonsCellEditorRenderer.CONTEXT;
        case 5:
            return ReportNameHyperlinkCellRenderer.CONTEXT;
        case 6:
            return PrinterContextSensitiveEditorFactory.CONTEXT;
        case 7:
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
        case 4:
        case 6:
        case 7:
            return true;
        default:
            return false;
        }
    }

    @Override
    public void removeObject(LayoutStampaDocumento object) {
        layoutStampeBD.cancellaLayoutStampa(object);
        super.removeObject(object);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        super.setValueAt(value, row, col);
        LayoutStampaDocumento layoutStampa = getObject(row);
        PanjeaLifecycleApplicationEvent event;
        switch (col) {
        case 6:
            layoutStampeManager.salvaAssociazioneStampante(layoutStampa.getChiave(), layoutStampa.getStampante());
            event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
            Application.instance().getApplicationContext().publishEvent(event);
            break;
        case 4:
            // in questo caso devo cambiare il report predefinito per il tipo area quindi una volta cambiato sostituisco
            // tutti i layout del tipo area per aggiornarli ( il predecente layout predefinito ora non lo è più)
            List<LayoutStampaDocumento> layouts = layoutStampeManager.setLayoutAsDefault(layoutStampa);

            for (LayoutStampa layout : layouts) {
                event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
                Application.instance().getApplicationContext().publishEvent(event);

                int index = this.getObjects().indexOf(layout);
                if (index != -1) {
                    setObject((LayoutStampaDocumento) layout, index);
                }
            }
            fireTableDataChanged();
            break;
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
            String stampante = layoutStampa.getStampante();
            layoutStampa = (LayoutStampaDocumento) layoutStampeBD.salvaLayoutStampa(layoutStampa);
            layoutStampa.setStampante(stampante);
            event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
            Application.instance().getApplicationContext().publishEvent(event);
            setObject(layoutStampa, row);
            break;
        case 12:
            // in questo caso devo cambiare il report predefinito per il tipo area quindi una volta cambiato sostituisco
            // tutti i layout del tipo area per aggiornarli ( il predecente layout predefinito ora non lo è più)
            List<LayoutStampaDocumento> layoutsMail = layoutStampeManager.setLayoutForInvioMail(layoutStampa);

            for (LayoutStampa layout : layoutsMail) {
                event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
                Application.instance().getApplicationContext().publishEvent(event);

                int index = this.getObjects().indexOf(layout);
                if (index != -1) {
                    setObject((LayoutStampaDocumento) layout, index);
                }
            }

            fireTableDataChanged();
            break;
        case 13:
            // in questo caso devo cambiare il report predefinito per il tipo area quindi una volta cambiato sostituisco
            // tutti i layout del tipo area per aggiornarli ( il predecente layout predefinito ora non lo è più)
            List<LayoutStampaDocumento> layoutsInterno = layoutStampeManager.setLayoutForUsoInterno(layoutStampa,
                    layoutStampa.isInterno());

            for (LayoutStampa layout : layoutsInterno) {
                event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED, layoutStampa, this);
                Application.instance().getApplicationContext().publishEvent(event);

                int index = this.getObjects().indexOf(layout);
                if (index != -1) {
                    setObject((LayoutStampaDocumento) layout, index);
                }
            }

            fireTableDataChanged();
            break;
        default:
            break;
        }
    }

}
