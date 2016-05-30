package it.eurotn.panjea.magazzino.rich.editors.articolo;

import java.awt.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.listino.RigaListinoPage;
import it.eurotn.panjea.magazzino.rich.editors.listino.RigheListinoTablePage;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.editors.ToolbarPageEditor;
import it.eurotn.rich.editors.table.EditFrame;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public class RigheListinoArticoloTablePage extends RigheListinoTablePage implements InitializingBean {
    private class NuovaRigaListinoCommand extends ActionCommand {
        /**
         *
         * Costruttore.
         *
         */
        public NuovaRigaListinoCommand() {
            super(ToolbarPageEditor.NEW_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            RigaListino rigaListino = new RigaListino();
            rigaListino.setVersioneListino(new VersioneListino());
            rigaListino.setArticolo(articolo.getArticoloLite());
            rigaListino.getVersioneListino().setDataVigore(Calendar.getInstance().getTime());
            rigaListino.getVersioneListino().setListino(new Listino());

            final ValidatingFormModel formModel = (ValidatingFormModel) PanjeaFormModelHelper
                    .createFormModel(rigaListino, false, "");
            // aggiungo la finta proprietà tipi listino per far si che la search text del listino mi
            // selezioni solo
            // quelli
            // normali e non a scaglioni
            // ValueModel tipoListinoValueModel = new ValueHolder(ETipoListino.NORMALE);
            // DefaultFieldMetadata tipiListinoData = new DefaultFieldMetadata(formModel,
            // new FormModelMediatingValueModel(tipoListinoValueModel), ETipoListino.class, true,
            // null);
            // formModel.add("tipoListino", tipoListinoValueModel, tipiListinoData);

            InputApplicationDialog inputDialog = new InputApplicationDialog(formModel, "versioneListino") {
                @Override
                public Object getInputValue() {
                    return formModel.getValueModel("versioneListino").getValue();
                }
            };
            final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) ((BindingFactoryProvider) getService(
                    BindingFactoryProvider.class)).getBindingFactory(formModel);
            Binding bindVersioneListino = bf.createBoundSearchText("versioneListino",
                    new String[] { "listino.codice", "listino.descrizione", "codice" });
            inputDialog.setInputField(bindVersioneListino.getControl());
            inputDialog.setInputLabelMessage("Seleziona la versione del listino da associare");
            inputDialog.setFinishAction(new Closure() {

                @Override
                public Object call(Object paramObject) {
                    VersioneListino versioneListino = (VersioneListino) paramObject;
                    if (!versioneListino.isNew()) {
                        RigaListino rigaListino = new RigaListino();
                        rigaListino.setVersioneListino(versioneListino);
                        rigaListino.setArticolo(articolo.getArticoloLite());
                        rigaListino.setNumeroDecimaliPrezzo(articolo.getNumeroDecimaliPrezzo());
                        RigaListinoPage rigaListinoPage = (RigaListinoPage) getEditPages()
                                .get(EditFrame.DEFAULT_OBJECT_CLASS_NAME);
                        rigaListinoPage.setFormObject(rigaListino);
                    }
                    return null;
                }
            });
            inputDialog.showDialog();
        }
    }

    private class UltimoCostroCellRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = -1395180943442039207L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            JLabel ultimoCostoLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                    row, column);

            RigheListinoArticoloTableModel model = (RigheListinoArticoloTableModel) TableModelWrapperUtils
                    .getActualTableModel(table.getModel());
            int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

            if (actualRow != -1) {
                RigaListinoDTO rigaListinoDTO = model.getObject(actualRow);
                BigDecimal ultimoCosto = ObjectUtils.defaultIfNull(rigaListinoDTO.getUltimoCosto(), BigDecimal.ZERO);

                if (BigDecimal.ZERO.compareTo(ultimoCosto) != 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<html>");
                    sb.append(ultimoCostoLabel.getText());
                    sb.append("   (<font ");
                    BigDecimal perc = rigaListinoDTO.getPrezzo().subtract(ultimoCosto).multiply(new BigDecimal(100))
                            .divide(ultimoCosto, 2, RoundingMode.HALF_UP);
                    if (BigDecimal.ZERO.compareTo(perc) > 0) {
                        sb.append("color='red'");
                    }
                    sb.append(">");
                    sb.append(new DecimalFormat("#00.00").format(perc));
                    sb.append("%</font>)</html>");

                    ultimoCostoLabel.setText(sb.toString());
                }
                ultimoCostoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            }

            return ultimoCostoLabel;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(RigheListinoArticoloTablePage.class);

    public static final String RIGHE_LISTINO_ARTICOLO_PAGE_ID = "righeListinoArticoloTablePage";
    private Articolo articolo;

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public RigheListinoArticoloTablePage() {
        super(RIGHE_LISTINO_ARTICOLO_PAGE_ID, new RigheListinoArticoloTableModel());
        getTable().setAggregatedColumns(new String[] { "versioneListino.listino.codice", "versioneListino.codice" });
        getTable().getTable().getColumnModel().getColumn(4).setCellRenderer(new UltimoCostroCellRenderer());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RigaListinoPage rigaListinoPage = (RigaListinoPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME);
        rigaListinoPage.setNewCommand(new NuovaRigaListinoCommand());
        rigaListinoPage.setArticoloEnabled(false);
    }

    @Override
    public Collection<RigaListinoDTO> loadTableData() {
        List<RigaListinoDTO> righe = Collections.emptyList();

        if (articolo != null && articolo.getId() != null) {
            righe = magazzinoAnagraficaBD.caricaRigheListinoByArticolo(articolo.getId());
        }

        return righe;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return articolo.getId() != null;
    }

    @Override
    protected void onRefresh() {
        if (articolo != null && articolo.getId() != null) {
            setRows(magazzinoAnagraficaBD.caricaRigheListinoByArticolo(articolo.getId()), false);
        } else {
            List<RigaListinoDTO> emptyList = Collections.emptyList();
            setRows(emptyList);
        }
    }

    @Override
    public void processTableData(Collection<RigaListinoDTO> results) {
        super.processTableData(results);
        try {
            getTable().getTable().getColumnModel().getColumn(4).setCellRenderer(new UltimoCostroCellRenderer());
        } catch (Exception e) {
            LOGGER.error("-->errore ", e);
        }
    }

    @Override
    public Collection<RigaListinoDTO> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        articolo = (Articolo) object;
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
