package it.eurotn.panjea.ordini.rich.editors.produzione;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.table.TableCellEditor;

import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoProduzione;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.produzione.evasione.ParametriEvasioneProduzione;
import it.eurotn.panjea.ordini.rich.editors.produzione.evasione.ParametriEvasioneProduzioneForm;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RisultatiRicercaProduzioneTablePage
    extends AbstractTablePageEditor<RigaDistintaCaricoProduzione> {

  private class CreaCaricoProduzioneCommand extends ActionCommand {

    public static final String COMMAND_ID = "creaCaricoProduzioneCommand";

    private ModificaDocumentiEvasioneDialog modificaDocumentiEvasioneDialog;

    /**
     * Costruttore.
     */
    public CreaCaricoProduzioneCommand() {
      super(COMMAND_ID);
      RcpSupport.configure(CreaCaricoProduzioneCommand.this);
      modificaDocumentiEvasioneDialog = new ModificaDocumentiEvasioneDialog();
    }

    @Override
    protected void doExecuteCommand() {
      TableCellEditor editor = getTable().getTable().getCellEditor();
      if (editor != null) {
        editor.stopCellEditing();
      }
      ParametriEvasioneProduzione paramsEvasione = (ParametriEvasioneProduzione) parametriEvasioneProduzioneForm
          .getFormObject();
      if (paramsEvasione == null) {
        return;
      }
      List<RigaDistintaCaricoProduzione> rows = getTable().getRows();
      if (rows.size() == 0) {
        return;
      }

      List<RigaDistintaCarico> righeDaEvadere = new ArrayList<RigaDistintaCarico>();
      for (RigaDistintaCaricoProduzione rigaDistintaCaricoProduzione : rows) {
        if (rigaDistintaCaricoProduzione.getQtaDaEvadere() > 0) {
          rigaDistintaCaricoProduzione.getDatiEvasioneDocumento()
              .setContoTerzi(paramsEvasione.isContoTerzi());
          rigaDistintaCaricoProduzione.getDatiEvasioneDocumento()
              .setTipoAreaEvasione(paramsEvasione.getTipoAreaMagazzino());
          rigaDistintaCaricoProduzione.getRigaArticolo().getAreaOrdine().getDepositoOrigine()
              .setVersion(0);
          // devo impostare la qta evasa altrimenti l'evasione non
          // considera il valore da evadere
          rigaDistintaCaricoProduzione.setQtaEvasa(rigaDistintaCaricoProduzione.getQtaDaEvadere());
          righeDaEvadere.add(rigaDistintaCaricoProduzione);
        }
      }
      try {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();

        ordiniDocumentoBD.evadiOrdini(righeDaEvadere, date);

        // parametri per la ricerca dei ddt generati
        ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino = new ParametriRicercaAreaMagazzino();
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        parametriRicercaAreaMagazzino.setAnnoCompetenza(calendar.get(Calendar.YEAR));
        parametriRicercaAreaMagazzino.setDataGenerazione(date);
        parametriRicercaAreaMagazzino.getTipiGenerazione().add(TipoGenerazione.EVASIONE);
        parametriRicercaAreaMagazzino.setEffettuaRicerca(true);

        // apro il dialog per la visualizzazione/modifica delle distinte
        modificaDocumentiEvasioneDialog.setParametri(parametriRicercaAreaMagazzino);
        modificaDocumentiEvasioneDialog.showDialog();

        // apro la lista dei documenti creati
        openAreeMagazzinoEditor(parametriRicercaAreaMagazzino);
      } catch (Exception e) {
        logger.error("--> errore nell'evasione", e);
      }
    }
  }

  private class CreaCaricoProduzioneCommandInterceptor implements ActionCommandInterceptor {

    @Override
    public void postExecution(ActionCommand arg0) {
      parametriRicercaProduzione.setEffettuaRicerca(true);
      refreshData();
    }

    @Override
    public boolean preExecution(ActionCommand arg0) {
      return true;
    }
  }

  public static final String PAGE_ID = "risultatiRicercaProduzioneTablePage";

  private ParametriRicercaProduzione parametriRicercaProduzione = null;
  private IOrdiniDocumentoBD ordiniDocumentoBD = null;
  private CreaCaricoProduzioneCommand caricoProduzioneCommand = null;
  private ParametriEvasioneProduzioneForm parametriEvasioneProduzioneForm = null;

  private CreaCaricoProduzioneCommandInterceptor creaCaricoProduzioneCommandInterceptor;

  /**
   * Costruttore.
   */
  public RisultatiRicercaProduzioneTablePage() {
    super(PAGE_ID, new ProduzioneTableModel());
  }

  @Override
  public JComponent createToolbar() {

    parametriEvasioneProduzioneForm = new ParametriEvasioneProduzioneForm();
    new PanjeaFormGuard(parametriEvasioneProduzioneForm.getFormModel(),
        getCreaCaricoProduzioneCommand());

    JPanel panelParametri = getComponentFactory().createPanel(new BorderLayout());

    panelParametri.add(new JPanel(), BorderLayout.CENTER);
    panelParametri.add(parametriEvasioneProduzioneForm.getControl(), BorderLayout.EAST);

    JPanel panel = getComponentFactory().createPanel(new BorderLayout());
    panel.add(panelParametri, BorderLayout.CENTER);
    panel.add(super.createToolbar(), BorderLayout.EAST);

    return panel;
  }

  @Override
  public void dispose() {
    if (caricoProduzioneCommand != null) {
      caricoProduzioneCommand.removeCommandInterceptor(getCreaCaricoProduzioneCommandInterceptor());
    }
    caricoProduzioneCommand = null;
    super.dispose();
  }

  @Override
  public AbstractCommand[] getCommands() {
    AbstractCommand[] abstractCommands = new AbstractCommand[] { getCreaCaricoProduzioneCommand() };
    return abstractCommands;
  }

  /**
   * @return CreaCaricoProduzioneCommand
   */
  private CreaCaricoProduzioneCommand getCreaCaricoProduzioneCommand() {
    if (caricoProduzioneCommand == null) {
      caricoProduzioneCommand = new CreaCaricoProduzioneCommand();
      caricoProduzioneCommand.addCommandInterceptor(getCreaCaricoProduzioneCommandInterceptor());
    }
    return caricoProduzioneCommand;
  }

  /**
   * @return creaCaricoProduzioneCommandInterceptor
   */
  private CreaCaricoProduzioneCommandInterceptor getCreaCaricoProduzioneCommandInterceptor() {
    if (creaCaricoProduzioneCommandInterceptor == null) {
      creaCaricoProduzioneCommandInterceptor = new CreaCaricoProduzioneCommandInterceptor();
    }
    return creaCaricoProduzioneCommandInterceptor;
  }

  @Override
  public AbstractCommand getEditorDeleteCommand() {
    return null;
  }

  @Override
  public List<RigaDistintaCaricoProduzione> loadTableData() {
    List<RigaDistintaCaricoProduzione> righe = null;
    if (parametriRicercaProduzione.isEffettuaRicerca()) {
      righe = refreshTableData();
    }
    return righe;
  }

  @Override
  public void onEditorEvent(ApplicationEvent event) {
    // se è una cancellazione di un area magazzino la trasformo in un
    // areamagazzinoricerca per cancellarla
    PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
    if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED)
        && panjeaEvent.getSource() instanceof AreaOrdine) {
      logger.debug("delete area " + panjeaEvent.getSource());
    } else {
      super.onEditorEvent(event);
    }
  }

  @Override
  public void onPostPageOpen() {
    // nothing to do
  }

  @Override
  public boolean onPrePageOpen() {
    return true;
  }

  /**
   * Visualizza l'editor della ricerca aree magazzino per visualizzare le aree create dalla
   * evasione.
   *
   * @param parametriRicercaAreaMagazzino
   *          data dell'evasione
   */
  private void openAreeMagazzinoEditor(
      ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino) {
    LifecycleApplicationEvent event = new OpenEditorEvent(parametriRicercaAreaMagazzino);
    Application.instance().getApplicationContext().publishEvent(event);
  }

  @Override
  public List<RigaDistintaCaricoProduzione> refreshTableData() {
    List<RigaDistintaCaricoProduzione> areeOrdine = Collections.emptyList();
    if (parametriRicercaProduzione.isEffettuaRicerca()) {
      areeOrdine = ordiniDocumentoBD.caricaRigheEvasioneProduzione(parametriRicercaProduzione);
    }
    return areeOrdine;
  }

  @Override
  public void restoreState(Settings settings) {
    super.restoreState(settings);
  }

  @Override
  public void setFormObject(Object object) {
    if (object instanceof ParametriRicercaProduzione) {
      this.parametriRicercaProduzione = (ParametriRicercaProduzione) object;
    } else {
      this.parametriRicercaProduzione = new ParametriRicercaProduzione();
    }
  }

  /**
   * @param ordiniDocumentoBD
   *          the ordiniDocumentoBD to set
   */
  public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
    this.ordiniDocumentoBD = ordiniDocumentoBD;
  }

}
