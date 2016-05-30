package it.eurotn.rich.command.config;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.config.DefaultCommandButtonConfigurer;

public class PanjeaCommandButtonConfigurer extends DefaultCommandButtonConfigurer {

    @Override
    public void configure(AbstractButton button, AbstractCommand command, CommandFaceDescriptor faceDescriptor) {
        super.configure(button, command, faceDescriptor);
        button.setName(command.getId());
    }

}
