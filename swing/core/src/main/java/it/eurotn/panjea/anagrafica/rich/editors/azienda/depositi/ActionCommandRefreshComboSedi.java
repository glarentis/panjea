package it.eurotn.panjea.anagrafica.rich.editors.azienda.depositi;

import java.util.List;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.SedeAzienda;

/**
 * Comando che serve per aggiornare la lista delle sedi nella combobox nel caso in cui vengano inserite o cancellate
 * nuove sedi.
 */
public class ActionCommandRefreshComboSedi extends ActionCommand {

    private DepositiSedeAziendaTablePage page;

    /**
     * Costruttore di default.
     *
     * @param commandId
     *            commandId
     */
    public ActionCommandRefreshComboSedi(final String commandId, DepositiSedeAziendaTablePage page) {
        super(commandId);
        RcpSupport.configure(this);

        this.page = page;
    }

    @Override
    protected void doExecuteCommand() {
        page.getComboBoxSedi().removeAllItems();

        if (page.getAzienda().getId() != null) {
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            page.getComboBoxSedi().addItem(
                    messageSourceAccessor.getMessage("combobox.allSedi", new Object[] {}, Locale.getDefault()));
            List<SedeAzienda> listSediAzienda = page.getAnagraficaBD().caricaSediAzienda(page.getAzienda());

            for (SedeAzienda sedeAziendaCorrente : listSediAzienda) {
                page.getComboBoxSedi().addItem(sedeAziendaCorrente);
            }
        }
    }

    @Override
    public void setSecurityControllerId(String controllerId) {
        super.setSecurityControllerId(getId());
    }
}