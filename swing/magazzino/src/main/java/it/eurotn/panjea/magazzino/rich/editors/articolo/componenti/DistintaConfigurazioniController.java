package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.action.CommandBar;
import com.jidesoft.swing.JideButton;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistintaBase;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta.NuovaConfigurazioneDistintaCommand;
import it.eurotn.panjea.magazzino.rich.factory.table.CustomArticoloRenderer;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.command.JideToggleCommand;

public class DistintaConfigurazioniController extends AbstractControlFactory {

  private class ConfigurazioneDeleteActionCommand extends AbstractDeleteCommand {

    private ConfigurazioneDistinta configurazioneDistinta = null;

    /**
     * Costruttore.
     *
     * @param configurazioneDistinta
     *          configurazioneDistinta
     */
    public ConfigurazioneDeleteActionCommand(final ConfigurazioneDistinta configurazioneDistinta) {
      super("deleteCommand");
      RcpSupport.configure(this);
      this.configurazioneDistinta = configurazioneDistinta;
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
      super.onButtonAttached(button);
      button.setText("");
      button.setFocusable(false);
    }

    @Override
    public Object onDelete() {
      magazzinoAnagraficaBD.cancellaConfigurazioneDistinta(configurazioneDistinta);
      updateModelConfigurazioni();
      return configurazioneDistinta;
    }

  }

  public class ConfigurazioneLoadActionCommand extends JideToggleCommand {

    private ConfigurazioneDistinta configurazioneDistinta = null;
    private JideButton jideButton = null;

    /**
     * Costruttore.
     *
     * @param configurazioneDistinta
     *          configurazioneDistinta
     */
    public ConfigurazioneLoadActionCommand(final ConfigurazioneDistinta configurazioneDistinta) {
      super("configurazioneDistinta" + configurazioneDistinta.getNome());
      RcpSupport.configure(this);
      this.configurazioneDistinta = configurazioneDistinta;
    }

    /**
     * @return the configurazioneDistinta
     */
    public ConfigurazioneDistinta getConfigurazioneDistinta() {
      return configurazioneDistinta;
    }

    @Override
    public String getText() {
      return configurazioneDistinta.getNome();
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
      super.onButtonAttached(button);
      jideButton = (JideButton) button;
      jideButton.setAlwaysShowHyperlink(false);
      jideButton.setText(configurazioneDistinta.getNome());
      jideButton.setHorizontalAlignment(10);
      jideButton.setButtonStyle(3);
      jideButton.setContentAreaFilled(false);
      jideButton.setOpaque(true);
      jideButton.setForegroundOfState(2, Color.BLUE);
      jideButton.setFocusable(false);
      jideButton.setRequestFocusEnabled(false);
    }

    @Override
    protected void onDeselection() {
      super.onDeselection();
      // jideButton.setBorder(DESELECTED_BORDER);
      jideButton.setBackground(UIManager.getColor("Panel.background"));
      jideButton.setForeground(Color.black);
    }

    @Override
    protected void onSelection() {
      ArticoloConfigurazioneDistinta articoloDistinta = magazzinoAnagraficaBD
          .caricaArticoloConfigurazioneDistinta(configurazioneDistinta);
      articoloDistintaValueHolder.setValue(articoloDistinta);
      // jideButton.setBorder(SELECTED_BORDER);
      jideButton.setBackground(Color.GRAY);
      jideButton.setForeground(Color.white);
    }

    /**
     * @param configurazioneDistinta
     *          the configurazioneDistinta to set
     */
    public void setConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
      this.configurazioneDistinta = configurazioneDistinta;
    }
  }

  public class NuovaConfigurazioneCommandInterceptor implements ActionCommandInterceptor {

    @Override
    public void postExecution(ActionCommand command) {
      ConfigurazioneDistinta nuovaConfigurazione = ((NuovaConfigurazioneDistintaCommand) command)
          .getConfigurazioneDistinta();
      if (nuovaConfigurazione != null && !nuovaConfigurazione.isNew()) {
        setArticoloConfigurazione(articolo, nuovaConfigurazione.getId());
      }
    }

    @Override
    public boolean preExecution(ActionCommand command) {
      command.addParameter(NuovaConfigurazioneDistintaCommand.CONF_DISTINTA_PARAMETER,
          articolo.creaProxyArticolo());
      return true;
    }

  }

  // private static final Border DESELECTED_BORDER = BorderFactory.createEmptyBorder(2, 6, 2, 6);
  // private static final Border SELECTED_BORDER = BorderFactory.createCompoundBorder(
  // BorderFactory.createRaisedSoftBevelBorder(), BorderFactory.createEmptyBorder(2, 6, 2, 6));

  private ValueHolder articoloDistintaValueHolder;
  private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

  private NuovaConfigurazioneDistintaCommand nuovaConfigurazioneCommand;
  private List<ConfigurazioneDistinta> configurazioni;
  private CommandBar toolbar;

  private ArticoloLite articolo;
  private Map<Integer, ConfigurazioneLoadActionCommand> configurazioniCommand;

  /**
   *
   * @param magazzinoAnagraficaBD
   *          db dell'anagrafica
   *
   */
  public DistintaConfigurazioniController(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
    super();
    this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    articoloDistintaValueHolder = new ValueHolder();
  }

  @Override
  protected JComponent createControl() {
    nuovaConfigurazioneCommand = new NuovaConfigurazioneDistintaCommand(magazzinoAnagraficaBD);
    nuovaConfigurazioneCommand.addCommandInterceptor(new NuovaConfigurazioneCommandInterceptor());
    toolbar = new CommandBar();
    return toolbar;
  }

  /**
   * @return Returns the configurazioneCorrenteValueHolder.
   */
  public ValueHolder getConfigurazioneCorrenteValueHolder() {
    return articoloDistintaValueHolder;
  }

  /**
   * @param articolo
   *          The articolo to set.
   */
  public void setArticolo(ArticoloLite articolo) {
    setArticoloConfigurazione(articolo, null);
  }

  /**
   * Imposta l'articolo/configurazione per mostrare la configurazione distinta scelta.
   *
   * @param articoloSelezionato
   *          articolo
   * @param idConfigurazioneSelezionata
   *          idConfigurazione
   */
  public void setArticoloConfigurazione(ArticoloLite articoloSelezionato,
      Integer idConfigurazioneSelezionata) {
    this.articolo = articoloSelezionato;
    if (articolo.getId() == null) {
      toolbar.removeAll();
    } else {
      updateModelConfigurazioni();
      if (configurazioniCommand.get(idConfigurazioneSelezionata) != null) {
        configurazioniCommand.get(idConfigurazioneSelezionata).setSelected(true);
      }
    }
  }

  /**
   * Aggiorna la toolbar con le configurazioni distinte.
   */
  private void updateModelConfigurazioni() {
    toolbar.removeAll();
    configurazioniCommand = new HashMap<>();
    toolbar.add(nuovaConfigurazioneCommand.createButton());
    configurazioni = magazzinoAnagraficaBD.caricaConfigurazioniDistinta(articolo);
    ExclusiveCommandGroup exclusiveCommandGroup = new ExclusiveCommandGroup(
        "configurazioniDistinta");
    for (ConfigurazioneDistinta configurazioneDistinta : configurazioni) {
      toolbar.addSeparator();
      ConfigurazioneLoadActionCommand configurazioneLoadActionCommand = new ConfigurazioneLoadActionCommand(
          configurazioneDistinta);
      configurazioniCommand.put(configurazioneDistinta.getId(), configurazioneLoadActionCommand);
      exclusiveCommandGroup.add(configurazioneLoadActionCommand);
      AbstractButton buttonCaricaConfigurazione = configurazioneLoadActionCommand.createButton();
      buttonCaricaConfigurazione.setIcon(RcpSupport.getIcon(ArticoloLite.class.getName()));
      toolbar.add(buttonCaricaConfigurazione);
      if (configurazioneDistinta instanceof ConfigurazioneDistintaBase) {
        buttonCaricaConfigurazione
            .setIcon(RcpSupport.getIcon(CustomArticoloRenderer.KEY_ICON_ARTICOLO_DISTINTA));
      } else {
        ConfigurazioneDeleteActionCommand configurazioneDeleteActionCommand = new ConfigurazioneDeleteActionCommand(
            configurazioneDistinta);
        AbstractButton buttonCancellaConfigurazione = configurazioneDeleteActionCommand
            .createButton();
        toolbar.add(buttonCancellaConfigurazione);
      }
    }
    toolbar.validate();
    toolbar.repaint();
  }
}
