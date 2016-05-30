package it.eurotn.panjea.lotti.rich.editors;

import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.rich.bd.ILottiBD;
import it.eurotn.panjea.lotti.rich.bd.LottiBD;
import it.eurotn.panjea.lotti.rich.forms.LottoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

public class LottoPage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "lottoPage";

	private ILottiBD lottiBD;

	/**
	 * Costruttore.
	 *
	 * @param lotto
	 *            lotto da gestire
	 *
	 */
	public LottoPage(final Lotto lotto) {
		super(PAGE_ID, new LottoForm(lotto));
		lottiBD = RcpSupport.getBean(LottiBD.BEAN_ID);
	}

	@Override
	protected Object doSave() {
		Lotto lotto = (Lotto) getBackingFormPage().getFormObject();

		lotto = lottiBD.salvaLotto(lotto);

		return lotto;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return new AbstractCommand[] { toolbarPageEditor.getSaveCommand() };
	}

	@Override
	public void loadData() {

	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void setFormObject(Object object) {
		super.setFormObject(object);
	}

	/**
	 * @param lottiBD
	 *            the lottiBD to set
	 */
	public void setLottiBD(ILottiBD lottiBD) {
		this.lottiBD = lottiBD;
	}
}
