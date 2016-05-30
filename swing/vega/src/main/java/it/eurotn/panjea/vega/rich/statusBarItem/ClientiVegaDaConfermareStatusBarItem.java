/**
 *
 */
package it.eurotn.panjea.vega.rich.statusBarItem;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.Timer;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;

import com.jidesoft.status.LabelStatusBarItem;

import it.eurotn.panjea.rich.pages.PanjeaDockingApplicationPage;
import it.eurotn.panjea.vega.rich.bd.IVegaBD;
import it.eurotn.rich.dialog.MessageAlert;

/**
 * @author leonardo
 */
public class ClientiVegaDaConfermareStatusBarItem extends LabelStatusBarItem implements ApplicationListener {

	private class CheckClientiListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			checkClienti();
		}
	}

	private class CheckMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			if (MouseEvent.BUTTON1 == e.getButton()) {
				Map<String, Object> mapParam = new HashMap<String, Object>();
				ApplicationPage applicationPage = Application.instance().getActiveWindow().getPage();
				((PanjeaDockingApplicationPage) applicationPage).openResultView("clientiVega", mapParam);
			} else if (MouseEvent.BUTTON2 == e.getButton()) {
				checkClienti();
			} else if (MouseEvent.BUTTON3 == e.getButton()) {
				if (timerCheck != null) {
					nascondi();
					stopCheck();
					try {
						getSettingManager().getUserSettings().setBoolean(CHECK_CLIENTI_VEGA, Boolean.FALSE);
						getSettingManager().getUserSettings().save();
					} catch (SettingsException | IOException e1) {
						logger.error("-->errore nel salvare la chiave " + CHECK_CLIENTI_VEGA, e1);
						throw new RuntimeException("-->errore nel salvare la chiave " + CHECK_CLIENTI_VEGA, e1);
					}
				} else {
					try {
						getSettingManager().getUserSettings().setBoolean(CHECK_CLIENTI_VEGA, Boolean.FALSE);
						getSettingManager().getUserSettings().save();
					} catch (SettingsException | IOException e1) {
						logger.error("-->errore nel salvare la chiave " + CHECK_CLIENTI_VEGA, e1);
						throw new RuntimeException("-->errore nel salvare la chiave " + CHECK_CLIENTI_VEGA, e1);
					}
					setToolTipText("Check dei clienti importati vega attivo.");
					setText("");
					setOpaque(true);
					startCheck();
				}
			}

		}
	}

	private static final String CHECK_CLIENTI_VEGA = "checkClientiVega";

	private static Logger logger = Logger.getLogger(ClientiVegaDaConfermareStatusBarItem.class);

	private static final long serialVersionUID = -4926608394168995628L;
	private static final Color COLOR_Clienti_PRESENTI_BLINK = Color.ORANGE;
	private Timer timerCheck;
	private IVegaBD vegaBD;
	private CheckMouseListener checkMouseListener = null;
	private MessageAlert messageAlert = null;

	/**
	 * Costruttore.
	 */
	public ClientiVegaDaConfermareStatusBarItem() {
		super();
		setIcon(null);
		setToolTipText("Nessun cliente rilevato.");
		setText("");
		setOpaque(true);
		messageAlert = null;
	}

	/**
	 * Aggiorna i controlli in accordo al risultato della verifica degli Clienti
	 * presenti da importare.
	 */
	private void checkClienti() {
		// Lo swingInteceptor me lo porta fuori dall' edt
		long numClienti = -1;
		try {
			numClienti = vegaBD.verificaClientiDaImportare();
			if (numClienti != 0) {
				setVisible(true);
				setIcon(RcpSupport.getIcon("menu.gruppo.aton.onSale.icon"));
				setBackground(COLOR_Clienti_PRESENTI_BLINK);
				setText(numClienti + " clienti da verificare importati da Vega");
				setToolTipText("Check ogni minuto");
			} else {
				nascondi();
			}
		} catch (Exception e1) {
			setVisible(true);
			setBackground(Color.RED);
			if (Application.isLoaded()
					&& (messageAlert == null || (messageAlert != null && !messageAlert.isVisible()))) {
				messageAlert = new MessageAlert(new DefaultMessage(e1.getMessage()));
				messageAlert.addCloseCommandVisible();
				messageAlert.showAlert();
			}
			setText("!");
		}
	}

	@Override
	protected void configureLabel(JLabel arg0) {
		super.configureLabel(arg0);
		((JLabel) getComponent()).setOpaque(true);
	}

	@Override
	public String getItemName() {
		return "PanjeaUpdateStatusBarItem";
	}

	private SettingsManager getSettingManager() {
		return RcpSupport.getBean("settingManagerLocal");
	}

	/**
	 * Nasconde i componenti del bar item.
	 */
	private void nascondi() {
		setIcon(null);
		Color stdColor = Color.GRAY;
		setBackground(stdColor);
		setText("*");
		setToolTipText("Check clienti vega disabilitato");
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (checkMouseListener == null) {
			checkMouseListener = new CheckMouseListener();
			addMouseListener(checkMouseListener);
		}

		if (event instanceof AuthenticationSuccessEvent) {

			Boolean checkClientiVega = false;
			try {
				checkClientiVega = ObjectUtils.defaultIfNull(
						getSettingManager().getUserSettings().getBoolean(CHECK_CLIENTI_VEGA), Boolean.FALSE);
			} catch (SettingsException e) {
				logger.error("-->errore nel caricare dalle preferenze utente la chiave" + CHECK_CLIENTI_VEGA, e);
				throw new RuntimeException("-->errore nel caricare dalle preferenze utente la chiave checkclientiVega",
						e);
			}
			if (checkClientiVega) {
				startCheck();
			} else {
				nascondi();
				stopCheck();
			}
		}
	}

	public void setVegaBD(IVegaBD vegaBD) {
		this.vegaBD = vegaBD;
	}

	/**
	 * Inizio il timer per verificare gli Clienti pronti.
	 */
	private void startCheck() {
		if (timerCheck == null) {
			timerCheck = new Timer(60000, new CheckClientiListener());
			timerCheck.setInitialDelay(1000);
		}
		timerCheck.start();
	}

	/**
	 * ferma il timer di controllo.
	 */
	private void stopCheck() {
		if (timerCheck != null) {
			timerCheck.stop();
			timerCheck = null;
		}
	}
}
