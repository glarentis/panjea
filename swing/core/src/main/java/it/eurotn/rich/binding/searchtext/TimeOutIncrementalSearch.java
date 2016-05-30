package it.eurotn.rich.binding.searchtext;

import it.eurotn.rich.dialog.InputApplicationDialog;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;

/**
 * Command per la cancellazione del valueModel della searchtextfield.
 */
public class TimeOutIncrementalSearch extends MenuItemCommand {
    private static final String ID = "timeOutIncrementalSearch";

    /**
     * @uml.property name="searchPanel"
     * @uml.associationEnd
     */

    /**
     * Costruttore.
     *
     * @param searchPanel
     *            pannello di ricerca
     */
    public TimeOutIncrementalSearch(final SearchPanel searchPanel) {
        super(ID);
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        InputApplicationDialog dialog = new InputApplicationDialog("Inserire il tempo di attesa in millisecondi",
                Application.instance().getActiveWindow().getControl()) {
            @Override
            protected void onFinish(Object inputValue) {
                super.onFinish(inputValue);
                if (NumberUtils.isNumber(inputValue.toString())) {
                    Integer delay = Integer.parseInt(inputValue.toString());
                    getSearchTextField().setSearchingDelayAndSave(delay);
                }
            }
        };
        dialog.setInputLabelMessage(
                "<HTML>Inserire il tempo di attesa in millisecondi.<br>valore minimo 200.<br>-1 per disabilitare il popup.</HTML>");
        dialog.showDialog();
    }

    @Override
    public void setSearchTextField(SearchTextField searchTextField) {
        super.setSearchTextField(searchTextField);
        getFaceDescriptor().setButtonLabelInfo(
                new StringBuilder("Tempo ric. inc. ").append(searchTextField.getSearchingDelay()).toString());
    }
}