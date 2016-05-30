/**
 * 
 */
package it.eurotn.panjea.lotti.rich.editors.riepilogolotto;

import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.util.ParametriRicercaLotti;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.Guarded;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.SimpleScrollPane;

/**
 * @author fattazzo
 * 
 */
public class ArticoliLottoTablePage extends AbstractTablePageEditor<ArticoloLite> {

	private class CaricaRiepilogoLottoCommand extends ActionCommand implements Guarded {

		/**
		 * Costruttore.
		 * 
		 */
		public CaricaRiepilogoLottoCommand() {
			super("searchCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			parametriRiepilogoLottoForm.getFormModel().commit();
			ParametriRicercaLotti parametriRicerca = (ParametriRicercaLotti) parametriRiepilogoLottoForm
					.getFormObject();
			parametriRicerca.setEffettuaRicerca(true);

			ArticoliLottoTablePage.this
					.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, parametriRicerca);

			// devo fare una setFormObject perchè l'evento lanciato non viene ovviamente rigirato a chi l'ha lanciato
			setFormObject(parametriRicerca);
			refreshData();
		}
	}

	public static final String PAGE_ID = "articoliLottoTablePage";

	private ParametriRicercaLotti parametriRicercaLotti;

	private ParametriRiepilogoLottoForm parametriRiepilogoLottoForm;

	private JCheckBox filtraDataScadenzaCheckBox;

	private ILottiBD lottiBD;

	private CaricaRiepilogoLottoCommand caricaRiepilogoLottoCommand;

	{
		parametriRiepilogoLottoForm = new ParametriRiepilogoLottoForm();
		filtraDataScadenzaCheckBox = new JCheckBox("Considera la data di scadenza");
	}

	/**
	 * Costruttore.
	 */
	protected ArticoliLottoTablePage() {
		super(PAGE_ID, new String[] { "codice", "descrizione" }, ArticoloLite.class);
	}

	/**
	 * @return the caricaRiepilogoLottoCommand
	 */
	private CaricaRiepilogoLottoCommand getCaricaRiepilogoLottoCommand() {
		if (caricaRiepilogoLottoCommand == null) {
			caricaRiepilogoLottoCommand = new CaricaRiepilogoLottoCommand();
		}

		return caricaRiepilogoLottoCommand;
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout(0, 5));
		panel.add(parametriRiepilogoLottoForm.getControl(), BorderLayout.NORTH);

		JPanel panelButton = getComponentFactory().createPanel(new BorderLayout());
		panelButton.add(filtraDataScadenzaCheckBox, BorderLayout.WEST);
		panelButton.add(getCaricaRiepilogoLottoCommand().createButton(), BorderLayout.EAST);
		panel.add(panelButton, BorderLayout.CENTER);
		new PanjeaFormGuard(parametriRiepilogoLottoForm.getFormModel(), getCaricaRiepilogoLottoCommand());

		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return new SimpleScrollPane(panel);
	}

	@Override
	public List<ArticoloLite> loadTableData() {

		List<ArticoloLite> articoli = new ArrayList<ArticoloLite>();
		if (parametriRicercaLotti.isEffettuaRicerca()) {
			articoli = lottiBD.caricaArticoliByCodiceLotto(parametriRicercaLotti.getLotto(),
					filtraDataScadenzaCheckBox.isSelected());
		}

		return articoli;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public List<ArticoloLite> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		parametriRicercaLotti = (ParametriRicercaLotti) object;
		parametriRiepilogoLottoForm.setFormObject(parametriRicercaLotti);
	}

	/**
	 * @param lottiBD
	 *            the lottiBD to set
	 */
	public void setLottiBD(ILottiBD lottiBD) {
		this.lottiBD = lottiBD;
	}

	@Override
	public void update(java.util.Observable observable, Object obj) {
		parametriRicercaLotti.setArticolo((ArticoloLite) obj);

		ArticoliLottoTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
				parametriRicercaLotti);

		super.update(observable, obj);
	};
}
