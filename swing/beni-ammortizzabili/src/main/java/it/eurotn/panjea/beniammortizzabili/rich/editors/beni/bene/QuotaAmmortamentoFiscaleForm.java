/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni.bene;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.editors.beni.PoliticaCalcoloBeneForm;
import it.eurotn.panjea.beniammortizzabili2.domain.PoliticaCalcoloBene;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamento;
import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

/**
 * 
 * @author Aracno
 * @version 1.0, 03/ott/06
 * 
 */
public class QuotaAmmortamentoFiscaleForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "quotaAmmortamentoFiscaleForm";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private PoliticaCalcoloBeneForm politicaCalcoloBeneForm;
	private JPanel politicaDiCalcoloPanel;
	public static final String CARD_EMPTY = "emptyCard";
	public static final String CARD_POLITICA_CALCOLO = "politicaCalcoloCard";

	/**
	 * Costruttore.
	 * 
	 * @param quotaAmmortamentoFiscale
	 *            quota
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public QuotaAmmortamentoFiscaleForm(final QuotaAmmortamentoFiscale quotaAmmortamentoFiscale,
			final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PanjeaFormModelHelper.createFormModel(quotaAmmortamentoFiscale, false, FORM_ID), FORM_ID);
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
		politicaCalcoloBeneForm = new PoliticaCalcoloBeneForm(beniAmmortizzabiliBD);
		politicaDiCalcoloPanel = createPoliticaDiCalcoloPanel();
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		((JTextField) builder.add(QuotaAmmortamento.PROP_PROGRESSIVO_ANNO_AMMORTAMENTO)[1]).setColumns(4);
		((JTextField) builder.add(QuotaAmmortamento.PROP_ANNO_SOLARE_AMMORTAMENTO)[1]).setColumns(4);
		builder.row();
		((JTextField) builder.add(QuotaAmmortamento.PROP_PERC_QUOTA_AMMORTAMENTO_ORDINARIO)[1]).setColumns(4);
		((JTextField) builder.add(QuotaAmmortamento.PROP_IMP_QUOTA_AMMORTAMENTO_ORDINARIO)[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add(QuotaAmmortamentoFiscale.PROP_PERC_QUOTA_AMMORTAMENTO_ANTICIPATO)[1]).setColumns(4);
		((JTextField) builder.add(QuotaAmmortamentoFiscale.PROP_IMP_QUOTA_AMMORTAMENTO_ANTICIPATO)[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add(QuotaAmmortamentoFiscale.PROP_PERC_QUOTA_AMMORTAMENTO_ACCELERATO)[1]).setColumns(4);
		((JTextField) builder.add(QuotaAmmortamentoFiscale.PROP_IMP_QUOTA_AMMORTAMENTO_ACCELERATO)[1]).setColumns(10);
		builder.row();
		((JTextField) builder
				.add(QuotaAmmortamentoFiscale.PROP_PERC_QUOTA_AMMORTAMENTO_RIDOTTO, "colSpan=1 align=left")[1])
				.setColumns(4);
		builder.row();
		builder.add(QuotaAmmortamento.PROP_ANNOTAZIONI);
		builder.row();

		// se la quota ammortamento e' dirty aggiungo la label che segnala che i
		// valori sono stati modificati a mano e possono essere diversi da
		// quelli generati con le politiche di calcolo
		if (((QuotaAmmortamento) this.getFormObject()).isDirty()) {
			String titolo = getMessageSource().getMessage(FORM_ID + ".labelQuotaDirty", new Object[] {},
					Locale.getDefault());
			JLabel label = new JLabel(titolo);
			label.setForeground(Color.RED);
			builder.getLayoutBuilder().cell(label);
		}

		JPanel quotaPanel = (JPanel) builder.getForm();
		quotaPanel.setBorder(BorderFactory.createTitledBorder(RcpSupport.getMessage(FORM_ID + ".quota.title")));

		JPanel rootPanel = getComponentFactory().createPanel(new GridLayout(1, 2));
		rootPanel.add(quotaPanel);
		rootPanel.add(politicaDiCalcoloPanel);

		return rootPanel;
	}

	/**
	 * Crea il pannello per la visualizzazione delle politica di calcolo del
	 * bene.
	 * 
	 * @return pannello creato
	 */
	private JPanel createPoliticaDiCalcoloPanel() {

		JPanel rootPanel = getComponentFactory().createPanel(new CardLayout());
		rootPanel.setBorder(BorderFactory.createTitledBorder(getMessage(FORM_ID + ".politicaCalcolo.title")));

		JPanel emptyPanel = getComponentFactory().createPanel(new BorderLayout());
		JLabel emptyLabel = getComponentFactory().createLabel("Nessuna politica di calcolo presente.");
		emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		emptyPanel.add(emptyLabel, BorderLayout.CENTER);

		JPanel politicaPanel = getComponentFactory().createPanel(new BorderLayout());
		politicaPanel.add(politicaCalcoloBeneForm.getControl(), BorderLayout.CENTER);

		rootPanel.add(emptyPanel, CARD_EMPTY);
		rootPanel.add(politicaPanel, CARD_POLITICA_CALCOLO);

		return rootPanel;
	}

	@Override
	public void setFormObject(Object formObject) {
		super.setFormObject(formObject);

		PoliticaCalcoloBene politica = beniAmmortizzabiliBD
				.caricaPoliticaCalcoloBeneByQuotaAmmortamento((QuotaAmmortamento) formObject);

		CardLayout cl = (CardLayout) (politicaDiCalcoloPanel.getLayout());
		if (politica == null) {
			cl.show(politicaDiCalcoloPanel, CARD_EMPTY);
		} else {
			politicaCalcoloBeneForm.setFormObject(politica);
			politicaCalcoloBeneForm.getFormModel().setReadOnly(true);
			cl.show(politicaDiCalcoloPanel, CARD_POLITICA_CALCOLO);
		}
	}
}
