package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;

import com.jidesoft.plaf.xerto.FrameBorder;
import com.jidesoft.swing.JideLabel;

/**
 * L'area magazzino ha una toolbar personalizzata.<br/>
 * La classe toglie quella di default e ne mette una personalizzata. Quando il documento passa in stato confermato la
 * toolbar di default viene nascosta e si <br/>
 * visualizza la richiesta di conferma documento
 * 
 * @author giangi
 * 
 */
public class ToolbarAreaMagazzinoPage {
	private class ConfermaDocumentoCommand extends ActionCommand {
		private static final String COMMAND_ID = "printCommand";
		private static final String CONTROLLER_ID = "printCommandDocumentoMagazzino";

		/**
		 * Costruttore.
		 * 
		 */
		public ConfermaDocumentoCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(CONTROLLER_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}
		
		@Override
		protected void onButtonAttached(AbstractButton button) {
			super.onButtonAttached(button);
			button.setName("ConfermaDocumentoCommand");
		}

		@Override
		protected void doExecuteCommand() {
			cardLayout.show(pannelloPrincipale, "toolbar");
		}
	}

	private AreaMagazzinoPage areaMagazzinoPage;
	private JTextField importoTextField;
	private JComponent toolbarComponent = null;
	private JPanel pannelloConferma = null;
	private JComponent pageComponent;
	private CardLayout cardLayout;
	private JPanel pannelloPrincipale;

	/**
	 * Costruttore.
	 * 
	 * @param areaMagazzinoPage
	 *            pagina che gestisce l'area magazzino
	 */
	public ToolbarAreaMagazzinoPage(final AreaMagazzinoPage areaMagazzinoPage) {
		this.areaMagazzinoPage = areaMagazzinoPage;
	}

	private void initComponent() {
		if (areaMagazzinoPage.isControlCreated() && pageComponent == null) {
			pageComponent = areaMagazzinoPage.getControl();
			for (Component component : pageComponent.getComponents()) {
				if ("toolBar".equals(component.getName())) {
					toolbarComponent = (JComponent) component;
				}
			}

			cardLayout = new CardLayout();
			pannelloPrincipale = new JPanel(cardLayout);

			pannelloConferma = new JPanel(new BorderLayout());
			pannelloConferma.setBorder(new FrameBorder());
			pannelloConferma.setSize(toolbarComponent.getSize());
			JPanel panelTotaleDocumento = new JPanel(new BorderLayout());
			importoTextField = new JTextField();
			panelTotaleDocumento.add(new JLabel("Totale Documento: "), BorderLayout.WEST);
			panelTotaleDocumento.add(importoTextField, BorderLayout.CENTER);
			// pannelloPrincipale.add(panelTotaleDocumento,
			// BorderLayout.CENTER);

			JideLabel title = new JideLabel();
			title.setText("CONFERMA OPERAZIONE DI CHIUSURA DOCUMENTO");
			title.setSize(toolbarComponent.getWidth(), title.getWidth());
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			pannelloConferma.add(title, BorderLayout.NORTH);

			CommandGroup commandGroup = new CommandGroup();
			commandGroup.add(new ConfermaDocumentoCommand());

			pannelloConferma.add(commandGroup.createButtonStack(), BorderLayout.SOUTH);

			cardLayout.addLayoutComponent(pannelloConferma, "conferma");
			cardLayout.addLayoutComponent(toolbarComponent, "toolbar");
			pageComponent.add(pannelloPrincipale, BorderLayout.LINE_START);
			cardLayout.show(pannelloPrincipale, "toolbar");
		}
	}

	public void showDialog() {// applico le preferenze di visualizzazione del
								// message alert.
		initComponent();
		AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) ((FormBackedDialogPageEditor) areaMagazzinoPage)
				.getForm().getFormObject();
		importoTextField.setText("prova");
		cardLayout.show(pannelloPrincipale, "conferma");
	}
}
