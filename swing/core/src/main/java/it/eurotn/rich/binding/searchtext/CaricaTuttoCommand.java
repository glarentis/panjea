package it.eurotn.rich.binding.searchtext;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;

public class CaricaTuttoCommand extends MenuItemCommand {

    public static final String CARICATUTTOCOMMAND_ID = "searchCaricaTuttoCommand";

    /**
     * Costruttore.
     */
    public CaricaTuttoCommand() {
        super(CARICATUTTOCOMMAND_ID);
        CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        c.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        // sul post esecution il SearchTextcontroller lancia la ricerca
    }

}