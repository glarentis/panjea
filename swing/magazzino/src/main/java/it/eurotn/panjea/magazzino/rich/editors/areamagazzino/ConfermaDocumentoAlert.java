package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.components.ImportoTextField;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;

import com.jidesoft.alert.Alert;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.swing.JideLabel;
import com.jidesoft.utils.PortingUtils;

public class ConfermaDocumentoAlert {
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
		protected void doExecuteCommand() {
			alert.hidePopup();
		}
	}

	protected Alert alert;
	private AreaMagazzinoPage areaMagazzinoPage;
	private ImportoTextField importoTextField;

	/**
	 * Costruttore.
	 * 
	 * @param areaMagazzinoPage
	 *            pagina che gestisce l'area magazzino
	 */
	public ConfermaDocumentoAlert(final AreaMagazzinoPage areaMagazzinoPage) {
		this.areaMagazzinoPage = areaMagazzinoPage;
		Component toolbarComponent = null;

		JComponent pageComponent = areaMagazzinoPage.getControl();
		for (Component component : pageComponent.getComponents()) {
			if ("toolBar".equals(component.getName())) {
				toolbarComponent = component;
			}
		}

		alert = new Alert();
		alert.setResizable(false);
		alert.setMovable(false);
		alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));
		alert.setAlwaysOnTop(false);
		alert.setOwner(pageComponent);
		alert.setTransient(true);
		alert.getContentPane().setPreferredSize(toolbarComponent.getSize());
		// animazione di entrata
		CustomAnimation showAnimation = new CustomAnimation();
		showAnimation.setEffect(CustomAnimation.EFFECT_ZOOM);
		showAnimation.setSpeed(CustomAnimation.SPEED_FAST);
		showAnimation.setSmoothness(CustomAnimation.SMOOTHNESS_ROUGH);
		showAnimation.setFunctionFade(CustomAnimation.FUNC_LINEAR);

		showAnimation.setType(CustomAnimation.TYPE_ENTRANCE);

		showAnimation.setDirection(CustomAnimation.RIGHT);
		showAnimation.setVisibleBounds(PortingUtils.getScreenBounds(pageComponent));
		alert.setShowAnimation(showAnimation);

		// animazione di uscita
		CustomAnimation hideAnimation = new CustomAnimation();
		hideAnimation.setEffect(CustomAnimation.EFFECT_FADE);
		hideAnimation.setSpeed(CustomAnimation.SPEED_FAST);
		hideAnimation.setSmoothness(CustomAnimation.SMOOTHNESS_ROUGH);
		hideAnimation.setFunctionFade(CustomAnimation.FUNC_LINEAR);

		hideAnimation.setType(CustomAnimation.TYPE_EXIT);

		hideAnimation.setDirection(CustomAnimation.RIGHT);
		hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());

		alert.setHideAnimation(hideAnimation);

		alert.getContentPane().setLayout(new BorderLayout());
		JPanel panelTotaleDocumento = new JPanel(new BorderLayout());
		importoTextField = new ImportoTextField();
		panelTotaleDocumento.add(new JLabel("Totale Documento: "), BorderLayout.WEST);
		panelTotaleDocumento.add(importoTextField, BorderLayout.CENTER);

		JideLabel title = new JideLabel();
		title.setText("CONFERMA OPERAZIONE DI CHIUSURA DOCUMENTO");
		title.setSize(toolbarComponent.getWidth(), title.getWidth());
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		CommandGroup commandGroup = new CommandGroup();
		commandGroup.add(new ConfermaDocumentoCommand());
		alert.getContentPane().add(commandGroup.createButtonStack(), BorderLayout.CENTER);
		alert.getContentPane().addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	/**
	 * Visualizza il message alert.
	 */
	public void show() {
		// applico le preferenze di visualizzazione del message alert.
		AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) ((FormBackedDialogPageEditor) areaMagazzinoPage)
				.getForm().getFormObject();
		importoTextField.setValue(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getTotale()
				.getImportoInValuta());
		this.alert.showPopup(areaMagazzinoPage.getControl());
	}
}
