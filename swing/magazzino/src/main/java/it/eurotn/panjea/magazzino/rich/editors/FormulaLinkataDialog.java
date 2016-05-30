package it.eurotn.panjea.magazzino.rich.editors;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.exception.FormuleLinkateException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class FormulaLinkataDialog extends MessageDialog {

	public static final String DIALOG_ID = "formulaLinkataDialog";

	private FormuleLinkateException formuleLinkateException;

	/**
	 * Costruttore.
	 * 
	 * @param formuleLinkateException
	 *            eccezione da gestire
	 */
	public FormulaLinkataDialog(final FormuleLinkateException formuleLinkateException) {
		super(RcpSupport.getMessage(DIALOG_ID + ".title"), "a");
		this.formuleLinkateException = formuleLinkateException;
	}

	@Override
	protected JComponent createDialogContentPane() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 5));
		GuiStandardUtils.attachBorder(rootPanel);

		rootPanel.add(createTipoAttributoFormulaComponent(), BorderLayout.NORTH);
		rootPanel.add(createFormuleLinkateComponent(), BorderLayout.CENTER);

		return rootPanel;
	}

	/**
	 * @return crea i componenti per le formule linkate
	 */
	private JComponent createFormuleLinkateComponent() {

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
				"Formule con riferimento circolare:"));

		DefaultListModel model = new DefaultListModel();
		for (Entry<TipoAttributo, FormulaTrasformazione> entry : formuleLinkateException.getTipiAttributoInConflitto()
				.entrySet()) {
			model.addElement(entry.getKey().getNome() + "    ( " + entry.getValue().getFormula() + " )");
		}
		JList list = new JList(model);
		panel.add(list, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * @return crea i componenti per il tipo attributo di riferimento
	 */
	private JComponent createTipoAttributoFormulaComponent() {

		FormLayout layout = new FormLayout("left:pref,10dlu,left:pref", "4dlu,default,default,4dlu,default");
		PanelBuilder builder = new PanelBuilder(layout); // , new FormDebugPanel());
		CellConstraints cc = new CellConstraints();

		builder.addLabel("Tipo attributo", cc.xy(1, 2));
		builder.addLabel("Formula", cc.xy(3, 2));
		builder.addSeparator("", cc.xyw(1, 3, 3));

		builder.addLabel(formuleLinkateException.getTipoAttributoDaControllare().getNome(), cc.xy(1, 5));
		builder.addLabel(formuleLinkateException.getFormulaTrasformazioneDaControllare().getFormula(), cc.xy(3, 5));

		JPanel panel = builder.getPanel();
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK)));
		return panel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return (new Object[] { getFinishCommand() });
	}
}
