package it.eurotn.panjea.lotti.rich.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.factory.MenuFactory;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.rich.editors.LottoPage;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.rich.binding.searchtext.MenuItemCommand;
import it.eurotn.rich.binding.searchtext.SearchTextField;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.search.AbstractSearchObject;

public class LottoSearchObject extends AbstractSearchObject {

    private class CaricaSoloLottiApertiCommand extends MenuItemCommand {

        public static final String COMMAND_ID = "caricaSoloLottiApertiCommand";

        private JCheckBoxMenuItem menuItem;

        public CaricaSoloLottiApertiCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        public JMenuItem createMenuItem(String faceDescriptorId, MenuFactory menuFactory,
                org.springframework.richclient.command.config.CommandButtonConfigurer buttonConfigurer) {
            menuItem = new JCheckBoxMenuItem();
            attach(menuItem, faceDescriptorId, buttonConfigurer);
            return menuItem;
        }

        @Override
        protected void doExecuteCommand() {
            StringBuilder key = new StringBuilder(200).append(LottoSearchObject.this.searchPanel.getFormModel().getId())
                    .append(".").append(LottoSearchObject.this.searchPanel.getFormPropertyPath()).append(".")
                    .append(CaricaSoloLottiApertiCommand.COMMAND_ID);
            getSettings().setBoolean(key.toString(), menuItem.isSelected());
        }

        /**
         * @return the menuItem
         */
        public JCheckBoxMenuItem getMenuItem() {
            return menuItem;
        }

    }

    private static Logger logger = Logger.getLogger(LottoSearchObject.class);

    private static final String SEARCH_OBJECT_ID = "lottoSearchObject";

    public static final String ARTICOLO_KEY = "articolo_key";

    public static final String DEPOSITO_KEY = "deposito_key";
    public static final String TIPO_MOVIMENTO_KEY = "tipoMovimento_key";
    public static final String STORNO_KEY = "storno_key";
    public static final String FILTER_LIST_KEY = "filterList_key";
    private ILottiBD lottiBD;

    private CaricaSoloLottiApertiCommand caricaSoloLottiApertiCommand;
    private DataPartenzaRicercaCommand dataPartenzaRicercaCommand;

    private List<AbstractCommand> customCommands = null;

    /**
     * Costruttore.
     *
     */
    public LottoSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public void configureSearchText(SearchTextField searchTextField) {
        super.configureSearchText(searchTextField);

        boolean caricaSoloAperti = true;
        StringBuilder key = new StringBuilder(200).append(LottoSearchObject.this.searchPanel.getFormModel().getId())
                .append(".").append(LottoSearchObject.this.searchPanel.getFormPropertyPath()).append(".")
                .append(CaricaSoloLottiApertiCommand.COMMAND_ID);
        if (getSettings().contains(key.toString())) {
            caricaSoloAperti = getSettings().getBoolean(key.toString());
        }
        caricaSoloLottiApertiCommand.getMenuItem().setSelected(caricaSoloAperti);

        dataPartenzaRicercaCommand.loadPeriodoFromSettings();

    }

    @Override
    public Object createNewInstance() {

        ArticoloLite articoloLite = (ArticoloLite) searchPanel.getMapParameters().get(ARTICOLO_KEY);

        TipoLotto tipoLotto = TipoLotto.NESSUNO;

        // se il sto editando/inserendo un lotto interno la search text potrà
        // creare solo il suo lotto altrimenti dipende dal tipo lotto impostato
        // sull articolo
        if (searchPanel.getFormModel().getFormObject() instanceof LottoInterno) {
            tipoLotto = TipoLotto.LOTTO;
        } else {
            // ERRORE..Se articolo lite è null la seconda or lancia una npe. Usare IsNew
            if (articoloLite == null || articoloLite.getId() == null) {
                tipoLotto = TipoLotto.LOTTO;
            } else {
                tipoLotto = articoloLite.getTipoLotto();
            }
        }

        Lotto lotto = null;
        switch (tipoLotto) {
        case LOTTO:
            lotto = new Lotto();
            break;
        case LOTTO_INTERNO:
            lotto = new LottoInterno();
            break;
        default:
            throw new UnsupportedOperationException("L'articolo non prevede lotti!!");
        }

        lotto.setArticolo(articoloLite);
        return lotto;
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        logger.debug("--> Enter getCustomCommands");
        if (customCommands == null) {
            customCommands = new ArrayList<AbstractCommand>();

            caricaSoloLottiApertiCommand = new CaricaSoloLottiApertiCommand();
            customCommands.add(caricaSoloLottiApertiCommand);

            dataPartenzaRicercaCommand = new DataPartenzaRicercaCommand(searchPanel, getSettings());
            customCommands.add(dataPartenzaRicercaCommand);
        }
        logger.debug("--> Exit getCustomCommands");
        return customCommands;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        Map<String, Object> parameters = searchPanel.getMapParameters();

        ArticoloLite articolo = (ArticoloLite) parameters.get(ARTICOLO_KEY);
        DepositoLite deposito = (DepositoLite) parameters.get(DEPOSITO_KEY);
        TipoMovimento tipoMovimento = (TipoMovimento) parameters.get(TIPO_MOVIMENTO_KEY);
        boolean storno = (Boolean) ObjectUtils.defaultIfNull(parameters.get(STORNO_KEY), false);

        List<Lotto> filterLotti = new ArrayList<Lotto>();
        if (parameters.get(FILTER_LIST_KEY) != null) {
            filterLotti = (List<Lotto>) parameters.get(FILTER_LIST_KEY);
        }

        String codiceLotto = null;
        Date dataScadenza = null;
        if ("dataScadenza".equals(fieldSearch)) {
            try {
                dataScadenza = new SimpleDateFormat("dd/MM/yyyy").parse(valueSearch.replaceAll("%", ""));
            } catch (Exception e) {
                return new ArrayList<Lotto>();
            }
        } else {
            codiceLotto = valueSearch;
        }

        Date dataInizioRicerca = dataPartenzaRicercaCommand.getPeriodo().getDataIniziale();

        List<Lotto> lotti = lottiBD.caricaLotti(articolo, deposito, tipoMovimento, storno, codiceLotto, dataScadenza,
                dataInizioRicerca, caricaSoloLottiApertiCommand.getMenuItem().isSelected());
        lotti.removeAll(filterLotti);

        logger.debug("--> Exit getData");
        return lotti;
    }

    @Override
    public void openEditor(Object object) {
        Lotto lotto = (Lotto) object;

        if (lotto != null) {
            if (!lotto.isNew()) {
                // ricarico il lotto per non avere l'articolo inizializzato
                lotto = lottiBD.caricaLotto((Lotto) object);
            }

            if (lotto.getArticolo() == null || lotto.getArticolo().isNew()) {
                Message message = new DefaultMessage("Selezionare l'articolo prima di poter creare il lotto.",
                        Severity.WARNING);
                MessageDialog dialog = new MessageDialog("ATTENZIONE", message);
                dialog.showDialog();
            } else {
                IPageEditor dialogPage = new LottoPage(lotto);

                if (searchPanel != null) {
                    dialogPage.addPropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, searchPanel);
                }

                DefaultTitledPageApplicationDialog dialog = new DefaultTitledPageApplicationDialog(lotto, null,
                        dialogPage);
                dialog.showDialog();

                if (searchPanel != null) {
                    dialogPage.removePropertyChangeListener(IPageLifecycleAdvisor.OBJECT_CHANGED, searchPanel);
                }
            }
        }
    }

    /**
     * @param lottiBD
     *            the lottiBD to set
     */
    public void setLottiBD(ILottiBD lottiBD) {
        this.lottiBD = lottiBD;
    }
}
