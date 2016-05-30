/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.ValutazioneBene;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author Aracno,Leonardo
 * @version 1.0, 09/ott/06
 * 
 */
public class ValutazioneBeneForm extends PanjeaAbstractForm {

	private static Logger logger = Logger.getLogger(ValutazioneBeneForm.class);
	private static final String FORM_ID = "valutazioneBeneForm";

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD = null;

	private BeneAmmortizzabile beneAmmortizzabile = null;

	/**
	 * Costruttore.
	 * 
	 * @param beneAmmortizzabile
	 *            bene
	 * @param beniAmmortizzabiliBD
	 *            beniAmmortizzabiliBD
	 */
	public ValutazioneBeneForm(final BeneAmmortizzabile beneAmmortizzabile,
			final IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		super(PanjeaFormModelHelper.createFormModel(new ValutazioneBene(), false, FORM_ID), FORM_ID);
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Creo controlli per il form della valutazione bene");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add(ValutazioneBene.PROP_ANNO, "colSpan=1 align=left")[1]).setColumns(4);
		builder.row();
		JTextField textImportoValutazBene = ((JTextField) builder.add(ValutazioneBene.PROP_IMPORTO_VALUTAZIONE_BENE,
				"colSpan=1 align=left")[1]);
		textImportoValutazBene.setColumns(10);
		builder.row();
		JTextField textImportoValutazFondo = ((JTextField) builder.add(ValutazioneBene.PROP_IMPORTO_VALUTAZIONE_FONDO,
				"colSpan=1 align=left")[1]);
		textImportoValutazFondo.setColumns(10);
		builder.row();
		((JTextField) builder.add(ValutazioneBene.PROP_NOTE, "colSpan=2 align=left")[1]).setColumns(51);
		builder.row();
		return builder.getForm();
	}

	@Override
	protected Object createNewObject() {
		return beniAmmortizzabiliBD.creaNuovaValutazioneBene(beneAmmortizzabile);
	}

	/**
	 * @param beneAmmortizzabile
	 *            the beneAmmortizzabile to set
	 */
	public void setBeneAmmortizzabile(BeneAmmortizzabile beneAmmortizzabile) {
		this.beneAmmortizzabile = beneAmmortizzabile;
	}
}
