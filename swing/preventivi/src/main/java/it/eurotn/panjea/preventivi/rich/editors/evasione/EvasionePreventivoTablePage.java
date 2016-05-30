/**
 *
 */
package it.eurotn.panjea.preventivi.rich.editors.evasione;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.collections4.Closure;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.bd.PreventiviBD;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author fattazzo
 *
 */
public class EvasionePreventivoTablePage extends AbstractTablePageEditor<RigaEvasione> {

    private class CancellaRigaAction extends AbstractAction {

        private static final long serialVersionUID = 8322987428226594681L;

        @Override
        public void actionPerformed(ActionEvent e) {

            RigaEvasione rigaEvasione = getTable().getSelectedObject();

            // se non ho niente di selezionato o se è una riga del preventivo ( non quindi una riga generata per una
            // nuova data consegna ) non faccio niente
            if (rigaEvasione == null || rigaEvasione.getRigaPadre() == null) {
                return;
            }

            // cancello la riga
            Double qta = rigaEvasione.getQuantitaRiga();
            RigaEvasione rigaEvasionePadre = rigaEvasione.getRigaPadre();
            getTable().removeRowObject(rigaEvasione);

            // riaggiungo la quantità della riga cancellata alla riga padre
            rigaEvasionePadre.setQuantitaRiga(rigaEvasionePadre.getQuantitaRiga() + qta);
        }

    }

    private class NuovaDataConsegnaCommand extends ActionCommand {

        private NuovaDataInputDialog dialog;

        /**
         * Costruttore.
         */
        public NuovaDataConsegnaCommand() {
            super("nuovaDataConsegnaCommand");
            RcpSupport.configure(this);
            dialog = new NuovaDataInputDialog(new Closure() {

                @Override
                public void execute(Object arg0) {
                    Object[] param = (Object[]) arg0;

                    RigaEvasione rigaEvasione = (RigaEvasione) param[0];
                    Date nuovaData = (Date) param[1];
                    Double qta = (Double) param[2];

                    if (qta != 0.0) {
                        // creo la nuova riga evasione con i parametri
                        RigaEvasione rigaEvasioneNew = new RigaEvasione(rigaEvasione.getRigaMovimentazione());
                        rigaEvasioneNew.setDataConsegna(nuovaData);
                        rigaEvasioneNew.setQuantitaRiga(qta);
                        rigaEvasioneNew.setQuantitaEvasione(qta);
                        rigaEvasioneNew.setSelezionata(true);
                        rigaEvasioneNew.setRigaPadre(rigaEvasione);
                        getTable().addRowObject(rigaEvasioneNew, null);

                        // modifico la riga esistente ( brutto! nella tabella la vedo modificata perchè è per
                        // riferimento)
                        rigaEvasione.setQuantitaRiga(rigaEvasione.getQuantitaRiga() - qta);
                    }
                }
            });
        }

        @Override
        protected void doExecuteCommand() {
            RigaEvasione rigaEvasione = getTable().getSelectedObject();
            if (rigaEvasione.getQuantitaEvasione().compareTo(rigaEvasione.getQuantitaRiga()) < 0) {
                dialog.setRigaEvasione(rigaEvasione);
                dialog.showDialog();
            }
        }

    }

    private class SelezionaTuttoCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public SelezionaTuttoCommand() {
            super("selezionaTuttoCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            for (RigaEvasione rigaEvasione : getTable().getRows()) {
                rigaEvasione.setSelezionata(true);
            }
            getTable().getTable().repaint();
        }

    }

    private IPreventiviBD preventiviBD;

    private AreaPreventivo areaPreventivo;

    private NuovaDataConsegnaCommand nuovaDataConsegnaCommand;
    private SelezionaTuttoCommand selezionaTuttoCommand;

    private AreaOrdineEvasionePreventivoForm areaOrdineEvasionePreventivoForm = new AreaOrdineEvasionePreventivoForm();

    /**
     * Costruttore.
     * 
     */
    public EvasionePreventivoTablePage() {
        super("evasionePreventivoTablePage", new EvasionePreventivoTableModel());
        setShowTitlePane(false);
        this.preventiviBD = RcpSupport.getBean(PreventiviBD.BEAN_ID);
        getTable().getTable().addMouseListener(new SelectRowListener(getTable().getTable()));
        getTable().setPropertyCommandExecutor(getNuovaDataConsegnaCommand());

        // sostituisco la action di cancellazione standard
        getTable().getTable().getActionMap().put("cancella", new CancellaRigaAction());
    }

    private JComponent createAreaOrdineEvasioneComponent() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.setBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Dati ordine"));
        rootPanel.add(areaOrdineEvasionePreventivoForm.getControl(), BorderLayout.CENTER);
        return rootPanel;
    }

    @Override
    protected JComponent createControl() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(super.createControl(), BorderLayout.CENTER);
        rootPanel.add(createAreaOrdineEvasioneComponent(), BorderLayout.SOUTH);
        return rootPanel;
    }

    /**
     * @return Area ordine di evasione
     */
    public AreaOrdine getAreaOrdineEvasione() {
        areaOrdineEvasionePreventivoForm.commit();
        return (AreaOrdine) areaOrdineEvasionePreventivoForm.getFormObject();
    }

    /**
     * @return the areaPreventivo
     */
    public AreaPreventivo getAreaPreventivo() {
        return areaPreventivo;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getSelezionaTuttoCommand(), getNuovaDataConsegnaCommand() };
    }

    /**
     * @return the nuovaDataConsegnaCommand
     */
    private NuovaDataConsegnaCommand getNuovaDataConsegnaCommand() {
        if (nuovaDataConsegnaCommand == null) {
            nuovaDataConsegnaCommand = new NuovaDataConsegnaCommand();
        }

        return nuovaDataConsegnaCommand;
    }

    /**
     * @return the selezionaTuttoCommand
     */
    private SelezionaTuttoCommand getSelezionaTuttoCommand() {
        if (selezionaTuttoCommand == null) {
            selezionaTuttoCommand = new SelezionaTuttoCommand();
        }

        return selezionaTuttoCommand;
    }

    /**
     * Verifica se tutti i dati per l'evasione dell'ordine sono validi. ( dati ordine e righe evasione selezionate).
     * 
     * @return <code>true</code> se sono validi
     */
    public boolean isDatiEvasioneValid() {
        // se non ci sono tutti i dati per l'eridone non è valida
        if (areaOrdineEvasionePreventivoForm.hasErrors()) {
            return false;
        }

        // veriico che tutte le righe selezionate abbiano la data di consegna
        for (RigaEvasione rigaEvasione : getTable().getRows()) {
            if (rigaEvasione.isSelezionata() && rigaEvasione.getDataConsegna() == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<RigaEvasione> loadTableData() {
        List<RigaEvasione> righeEvasione = new ArrayList<RigaEvasione>();

        righeEvasione = preventiviBD.caricaRigheEvasione(areaPreventivo.getId());

        return righeEvasione;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public Collection<RigaEvasione> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        this.areaPreventivo = (AreaPreventivo) object;

        AreaOrdine areaOrdineEvasione = new AreaOrdine();
        TipoAreaOrdine tipoAreaOrdineEvasione = areaPreventivo.getTipoAreaPreventivo().getTipoDocumentoEvasione();
        if (tipoAreaOrdineEvasione != null) {
            areaOrdineEvasione.setTipoAreaOrdine(tipoAreaOrdineEvasione);
            areaOrdineEvasione.setDepositoOrigine(tipoAreaOrdineEvasione.getDepositoOrigine());
        }
        areaOrdineEvasione.setDataConsegna(areaPreventivo.getDataConsegna());
        areaOrdineEvasione.setAgente(areaPreventivo.getDocumento().getSedeEntita().getAgente());
        areaOrdineEvasione.getDocumento().setDataDocumento(areaPreventivo.getDataAccettazione());
        areaOrdineEvasionePreventivoForm.setFormObject(areaOrdineEvasione);
        areaOrdineEvasionePreventivoForm
                .setTipoEntitaPreventivo(areaPreventivo.getTipoAreaPreventivo().getTipoDocumento().getTipoEntita());
    }
}
