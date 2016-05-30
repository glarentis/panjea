package it.eurotn.rich.command;

import java.util.List;

import org.springframework.richclient.command.GlueGroupMember;
import org.springframework.richclient.command.GroupContainerPopulator;
import org.springframework.richclient.command.config.CommandButtonConfigurer;

import com.jidesoft.action.CommandBar;

public class PanjeaGlueGroupMember extends GlueGroupMember {

    @Override
    protected void fill(GroupContainerPopulator parentContainer, Object factory, CommandButtonConfigurer configurer,
            List previousButtons) {
        if (parentContainer.getContainer() instanceof CommandBar) {
            ((CommandBar) parentContainer.getContainer()).addExpansion();
        } else {
            super.fill(parentContainer, factory, configurer, previousButtons);
        }
    }
}