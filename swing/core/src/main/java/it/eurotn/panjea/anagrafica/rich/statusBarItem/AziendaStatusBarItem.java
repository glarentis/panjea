/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.statusBarItem;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.rich.factory.PanjeaMenuFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.security.JecPrincipal;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.JPopupMenu;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.status.ButtonStatusBarItem;

/**
 * Oggetto della status bar che visualizza un pulsante con icona e nome dell'azienda e che premendolo apre il relativo
 * editor.
 * 
 * @author fattazzo,Leonardo
 * @version 1.0, 14/nov/07
 * 
 */
public final class AziendaStatusBarItem extends ButtonStatusBarItem {

	/**
	 * Command per editare in un editor l'azienda corrente.
	 */
	private class EditAziendaCommand extends ActionCommand {

		/**
		 * 
		 * Costruttore.
		 */
		public EditAziendaCommand() {
			super(EDIT_AZIENDA_COMMAND_ID);
			CommandConfigurer commandConfigurer = (CommandConfigurer) Application.services().getService(
					CommandConfigurer.class);
			commandConfigurer.configure(this);
		}

		@Override
		public void attach(AbstractButton button) {
			super.attach(button);
			button.setName(EDIT_AZIENDA_COMMAND_ID);
		}

		@Override
		public void doExecuteCommand() {
			JecPrincipal principal = PanjeaSwingUtil.getUtenteCorrente();
			AziendaAnagraficaDTO aziendaAnagraficaDTO = anagraficaBD.caricaAziendaAnagrafica(principal
					.getCodiceAzienda());
			LifecycleApplicationEvent event = new OpenEditorEvent(aziendaAnagraficaDTO);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	private IAnagraficaBD anagraficaBD;
	private static final long serialVersionUID = 1674481953513240909L;

	private CommandGroup commandGroup = null;
	private JPopupMenu popupMenu = null;
	private static final String EDIT_AZIENDA_COMMAND_ID = "openEditorAziendaCommand";
	public static final String AZIENDA_CORRENTE = "aziendaCorrenteCambiata";

	/**
	 * 
	 * Costruttore.
	 */
	private AziendaStatusBarItem() {
		super();
		IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
		setIcon(iconSource.getIcon("azienda.image"));

		MessageSource ms = (MessageSource) ApplicationServicesLocator.services().getService(MessageSource.class);
		setToolTip(ms.getMessage("azienda.statusBarItem.tooltip.text", null, Locale.getDefault()));

		// sul click del button chiamo la popup
		addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (popupMenu == null) {
					// crea il popup dal command
					popupMenu = getCommandGroup().createPopupMenu(new PanjeaMenuFactory());
				}
				Component mainFrame = Application.instance().getActiveWindow().getControl();
				int x = 5;
				// setto la posizione y come la totale dell'applicazione - l'altezza di this - l'altezza del popup - 5
				// (scelto a occhio)
				// in modo da presentare la popup appena sopra la status bar nella posizione del tasto per le operazioni
				// sull'azienda
				int y = mainFrame.getHeight() - getComponent().getHeight() - popupMenu.getHeight() - 5;
				popupMenu.show(mainFrame, x, y);
			}
		});
	}

	@Override
	protected void customizeButton(AbstractButton paramAbstractButton) {
		super.customizeButton(paramAbstractButton);
		paramAbstractButton.setName("AziendaStatusBarItem");
	}

	/**
	 * Command group per le funzioni di edit e cambio dell'azienda corrente --> Modifica --> Cambia --> azienda 1 -->
	 * azienda 2.
	 * 
	 * @return CommandGroup
	 */
	public CommandGroup getCommandGroup() {
		if (commandGroup == null) {
			commandGroup = new CommandGroup();
			commandGroup.add(new EditAziendaCommand());
		}
		return commandGroup;
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}
}
