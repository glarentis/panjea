package it.eurotn.panjea.magazzino.rich.editors.righemagazzino.importarigheordini;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoLotto;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.beans.BeanUtils;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.converter.ObjectConverterManager;

public class AssegnaLottiDialog extends ApplicationDialog {

	private class AnnullaCommand extends ActionCommand {

		public static final String COMMAND_ID = "annullaCommand";

		/**
		 * Costruttore.
		 */
		public AnnullaCommand() {
			super(COMMAND_ID);
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			rigaArticolo = null;
			result = 2;
			hide();
		}
	}

	private AnnullaCommand annullaCommand;

	private RigaArticolo rigaArticolo;

	private FormModel rigaArticoloFormModel;

	private PanjeaAbstractForm lottiForm = null;

	private RigaDistintaCarico rigaDistintaCarico;

	private LottiException exception;

	private int result;

	/**
	 * Costruttore.
	 * 
	 * @param rigaDistintaCarico
	 *            riga distitna articolo su cui assegnare i lotti
	 * @param magazzinoDocumentoBD
	 *            magazzinoDocumentoBD
	 * @param exception
	 *            eccezione relativa alla riga.
	 * @param documentoEvasione
	 *            documento dove evado i lotti
	 */
	public AssegnaLottiDialog(final RigaDistintaCarico rigaDistintaCarico, final LottiException exception,
			final IMagazzinoDocumentoBD magazzinoDocumentoBD, final AreaMagazzino documentoEvasione) {
		super("Gestione lotti", null);
		this.exception = exception;
		if (rigaDistintaCarico.getRigheDistintaLotto() == null) {
			rigaDistintaCarico.setRigheDistintaLotto(new ArrayList<RigaDistintaCaricoLotto>());
		}
		this.rigaDistintaCarico = rigaDistintaCarico;
		rigaArticolo = new RigaArticolo();
		rigaArticolo.setArticolo(rigaDistintaCarico.getArticolo());
		rigaArticolo.setQta(rigaDistintaCarico.getQtaDaEvadere());
		rigaArticolo.setAreaMagazzino(documentoEvasione);
		rigaArticolo.setRigheLotto(new HashSet<RigaLotto>());
		for (RigaDistintaCaricoLotto rigaDistintaCaricoLotto : rigaDistintaCarico.getRigheDistintaLotto()) {
			RigaLotto rigaLotto = new RigaLotto();
			Lotto lotto = new Lotto();
			lotto.setCodice(rigaDistintaCaricoLotto.getCodiceLotto());
			lotto.setDataScadenza(rigaDistintaCaricoLotto.getDataScadenza());
			rigaLotto.setLotto(lotto);
			rigaLotto.setQuantita(rigaDistintaCaricoLotto.getQuantita());
			rigaArticolo.getRigheLotto().add(rigaLotto);
		}
		setCloseAction(CloseAction.HIDE);
	}

	@Override
	protected JComponent createDialogContentPane() {
		FormLayout layout = new FormLayout("right:default,4dlu,left:default:grow",
				"4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default,4dlu,default");
		JPanel rootPanel = getComponentFactory().createPanel(layout);
		// JPanel rootPanel = new FormDebugPanel(layout);

		CellConstraints cc = new CellConstraints();
		rootPanel.add(new JLabel(RcpSupport.getMessage(Documento.class.getName())), cc.xy(1, 2));
		JLabel documentoLabel = new JLabel(RcpSupport.getIcon(Documento.class.getName()));
		documentoLabel.setText("N°" + rigaDistintaCarico.getNumeroDocumento().getCodice() + " del "
				+ rigaDistintaCarico.getDataRegistrazione());
		rootPanel.add(documentoLabel, cc.xy(3, 2));

		rootPanel.add(new JLabel(RcpSupport.getMessage(Articolo.class.getName())), cc.xy(1, 4));
		JLabel articoloLabel = new JLabel(RcpSupport.getIcon(Articolo.class.getName()));
		articoloLabel.setText(ObjectConverterManager.toString(rigaArticolo.getArticolo()));
		rootPanel.add(articoloLabel, cc.xy(3, 4));

		rootPanel.add(new JLabel(RcpSupport.getMessage("quantita")), cc.xy(1, 6));
		rootPanel.add(new JLabel(rigaArticolo.getQta().toString()), cc.xy(3, 6));

		if (rigaArticolo.getRigheLotto() == null) {
			rigaArticolo.setRigheLotto(new HashSet<RigaLotto>());
			rigaArticolo.getRigheLotto().add(new RigaLotto());
		}
		rigaArticoloFormModel = PanjeaFormModelHelper.createFormModel(rigaArticolo, false, "assegnaLottiFormModel");

		try {
			// devo instanzare e disabilitare il command in questo modo perchè potrei non avere il plugin dei lotti
			// quindi evito il ClassNotFound di un eventuale cast
			lottiForm = (PanjeaAbstractForm) BeanUtils.instantiateClass(
					Class.forName("it.eurotn.panjea.lotti.rich.forms.rigaLotto.RigheLottiForm").getConstructor(
							FormModel.class), new Object[] { rigaArticoloFormModel });
			lottiForm.getControl();
			Field commandField = lottiForm.getClass().getDeclaredField("creaRigaArticoloCommand");
			commandField.setAccessible(true);
			((ActionCommand) commandField.get(lottiForm)).setVisible(false);
		} catch (Exception e) {
			logger.error("-->errore durante la creazione del form per la gestione dei lotti.", e);
			PanjeaSwingUtil.checkAndThrowException(e);
		}

		rootPanel.add(lottiForm.getControl(), cc.xyw(1, 10, 3));
		((Focussable) lottiForm).grabFocus();

		String messageException = RcpSupport.getMessage(exception.getClass().getName() + ".description");
		messageException = MessageFormat.format(messageException, rigaArticolo.getArticolo().getCodice());
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<b><font color='red'>");
		sb.append(messageException);
		sb.append("</font color='red'></b>");
		sb.append("</html>");
		rootPanel.add(new JLabel(sb.toString()), cc.xyw(1, 12, 3));

		sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<b>Chiudi</b>: salta l'importazione della riga corrente.<br>");
		sb.append("<b>Annulla</b>: annulla l'importazione di tutte le righe selezionate.");
		sb.append("</html>");
		JLabel legendaLabel = new JLabel((sb.toString()));
		rootPanel.add(legendaLabel, cc.xyw(1, 14, 3));

		rootPanel.setPreferredSize(new Dimension(500, 300));
		return rootPanel;
	}

	/**
	 * @return Returns the annullaCommand.
	 */
	public AnnullaCommand getAnnullaCommand() {
		if (annullaCommand == null) {
			annullaCommand = new AnnullaCommand();
		}

		return annullaCommand;
	}

	@Override
	protected String getCancelCommandId() {
		return "closeCommand";
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return (new AbstractCommand[] { getFinishCommand(), getCancelCommand(), getAnnullaCommand() });
	}

	/**
	 * @return risultato del dialogo. 0 ok, 1 cancel, 2 annulla
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @return Returns the rigaArticolo.
	 */
	public RigaArticolo getRigaArticolo() {
		return rigaArticolo;
	}

	/**
	 * 
	 * @return rigaDistinta con i lotti
	 */
	public RigaDistintaCarico getRigaDistintaCarico() {
		return rigaDistintaCarico;
	}

	@Override
	protected void onCancel() {
		super.onCancel();
		rigaDistintaCarico = null;
		result = 1;
	}

	@Override
	protected boolean onFinish() {
		lottiForm.commit();
		rigaDistintaCarico.setRigheDistintaLotto(new ArrayList<RigaDistintaCaricoLotto>());
		for (RigaLotto rigaLotto : rigaArticolo.getRigheLotto()) {
			RigaDistintaCaricoLotto rigaDistintaCaricoLotto = new RigaDistintaCaricoLotto();
			rigaDistintaCaricoLotto.setRigaDistintaCarico(rigaDistintaCarico);
			rigaDistintaCaricoLotto.setDataScadenza(rigaLotto.getLotto().getDataScadenza());
			rigaDistintaCaricoLotto.setQuantita(rigaLotto.getQuantita());
			rigaDistintaCaricoLotto.setCodiceLotto(rigaLotto.getLotto().getCodice());
			rigaDistintaCarico.getRigheDistintaLotto().add(rigaDistintaCaricoLotto);
		}
		result = 0;
		return true;
	}

}
