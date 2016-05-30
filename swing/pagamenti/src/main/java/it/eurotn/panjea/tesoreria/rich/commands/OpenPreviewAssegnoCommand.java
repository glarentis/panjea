package it.eurotn.panjea.tesoreria.rich.commands;

import it.eurotn.panjea.tesoreria.util.ImmagineAssegno;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXImagePanel.Style;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Visualizza la preview delle immagini di un assegno.
 * 
 * @author giangi
 * @version 1.0, 18/ott/2011
 * 
 */
public class OpenPreviewAssegnoCommand extends ApplicationWindowAwareCommand {
	private class AssegnoViewer extends ApplicationDialog {
		private final ImmagineAssegno immagineAssegno;

		/**
		 * Costruttore.
		 * 
		 * @param immagineAssegno
		 *            immagine dell'assegno da visualizzare.
		 */
		public AssegnoViewer(final ImmagineAssegno immagineAssegno) {
			this.immagineAssegno = immagineAssegno;
			this.setTitle("Anteprima assegno");
			this.setModal(true);
			this.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		}

		@Override
		protected JComponent createDialogContentPane() {

			FormLayout layout = new FormLayout("f:d:g", "f:m:g(0.5), f:m:g(0.5)");
			PanelBuilder panelBuilder = new PanelBuilder(layout);
			CellConstraints cc = new CellConstraints();

			JXImagePanel frontView = new JXImagePanel();
			Image fronte = immagineAssegno.getFronte();
			if (fronte != null) {
				frontView.setImage(fronte);
			} else {
				frontView.setImage(getImageSource().getImage("noimage.image"));
			}
			frontView.setStyle(Style.SCALED_KEEP_ASPECT_RATIO);
			frontView.setBorder(BorderFactory.createTitledBorder("Fronte"));
			panelBuilder.add(frontView, cc.xy(1, 1));

			JXImagePanel retroView = new JXImagePanel();
			retroView.setBorder(BorderFactory.createTitledBorder("Retro"));
			retroView.setStyle(Style.SCALED_KEEP_ASPECT_RATIO);
			Image retro = immagineAssegno.getRetro();
			if (retro != null) {
				retroView.setImage(retro);
			} else {
				retroView.setImage(getImageSource().getImage("noimage.image"));
			}
			panelBuilder.add(retroView, cc.xy(1, 2));
			return panelBuilder.getPanel();
		}

		@Override
		protected boolean onFinish() {
			if (closureFinishAction != null) {
				closureFinishAction.call(null);
			}
			return true;
		}

		@Override
		protected void onInitialized() {
			super.onInitialized();
			addActionKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), getFinishCommandId());
		}

	}

	public static final String COMMAND_ID = "openPreviewAssegnoCommand";

	public static final String IMMAGINE_ASSEGNO_PARAMETER = "immagineAssegnoParameter";

	private Closure closureFinishAction = null;

	/**
	 * Costruttore.
	 */
	public OpenPreviewAssegnoCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	/**
	 * Costruttore.
	 * 
	 * @param closureFinishAction
	 *            azione da eseguire alla conferma del dialogo.
	 */
	public OpenPreviewAssegnoCommand(final Closure closureFinishAction) {
		this();
		this.closureFinishAction = closureFinishAction;
	}

	@Override
	protected void doExecuteCommand() {
		if (getParameter(IMMAGINE_ASSEGNO_PARAMETER) == null) {
			return;
		}
		AssegnoViewer assegnoViewer = new AssegnoViewer((ImmagineAssegno) getParameter(IMMAGINE_ASSEGNO_PARAMETER));
		assegnoViewer.showDialog();
		assegnoViewer = null;
	}
}
