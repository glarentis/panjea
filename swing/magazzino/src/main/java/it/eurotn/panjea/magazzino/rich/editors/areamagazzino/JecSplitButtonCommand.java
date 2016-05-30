package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;

import com.jidesoft.swing.JideSplitButton;

public class JecSplitButtonCommand extends AbstractCommand {

	private final AbstractCommand defaultCommand;
	private final AbstractCommand[] popupCommand;

	/**
	 * Costruttore.
	 * 
	 * @param defaultCommand
	 *            azione di default associata allo split command
	 * @param popupCommands
	 *            lista dei command che saranno contenuti nel popup
	 */
	public JecSplitButtonCommand(final AbstractCommand defaultCommand, final AbstractCommand[] popupCommands) {
		super();
		this.defaultCommand = defaultCommand;
		this.popupCommand = popupCommands;
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {

		JideSplitButton button = new JideSplitButton();

		if (this.popupCommand != null) {
			for (AbstractCommand element : this.popupCommand) {
				button.add(element.createMenuItem());
			}
		}
		button.setAction(new AbstractAction() {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				JecSplitButtonCommand.this.defaultCommand.execute();
			}
		});
		button.setText(this.defaultCommand.getText());
		button.setIcon(this.defaultCommand.getIcon());

		return button;
	}

	@Override
	public void execute() {
		this.defaultCommand.execute();
	}
}
