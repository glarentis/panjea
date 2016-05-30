package it.eurotn.panjea.magazzino.rich.editors.validazionerighe.commands;

import it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation;
import it.eurotn.rich.command.JideToggleCommand;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;

public class RulesValidationCommand extends JideToggleCommand {

	private final AbstractRigaArticoloRulesValidation rule;

	/**
	 * Costruttore di default.
	 * 
	 * @param rule
	 *            regola del toggle command
	 */
	public RulesValidationCommand(final AbstractRigaArticoloRulesValidation rule) {
		super(rule.getClass().getName() + "Command");

		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		this.setSecurityControllerId(rule.getClass().getName());
		c.configure(this);

		this.rule = rule;
	}

	/**
	 * @return the rule
	 */
	public AbstractRigaArticoloRulesValidation getRule() {
		return rule;
	}
}
