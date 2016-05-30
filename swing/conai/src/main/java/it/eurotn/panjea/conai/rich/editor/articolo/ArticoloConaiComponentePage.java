package it.eurotn.panjea.conai.rich.editor.articolo;

import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.rich.bd.IConaiBD;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

import org.springframework.richclient.command.AbstractCommand;

public class ArticoloConaiComponentePage extends FormBackedDialogPageEditor {

	public static final String PAGE_ID = "articoloComponentePage";

	private Articolo articoloCorrente;

	private IConaiBD conaiBD;

	/**
	 * Costruttore.
	 */
	public ArticoloConaiComponentePage() {
		super("articoloConaiComponentePage", new ConaiComponenteForm());
	}

	@Override
	protected Object doDelete() {
		ConaiComponente componente = (ConaiComponente) this.getForm().getFormObject();
		conaiBD.cancellaComponenteConai(componente);
		return componente;
	}

	@Override
	protected Object doSave() {
		ConaiComponente componente = (ConaiComponente) this.getForm().getFormObject();
		componente.setArticolo(articoloCorrente.getArticoloLite());
		componente = conaiBD.salvaComponenteConai(componente);
		return componente;
	}

	@Override
	protected AbstractCommand[] getCommand() {
		return toolbarPageEditor.getDefaultCommand(true);
	}

	/**
	 * @return Returns the conaiBD.
	 */
	public IConaiBD getConaiBD() {
		return conaiBD;
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

	/**
	 * @param articoloCorrente
	 *            The articoloCorrente to set.
	 */
	public void setArticoloCorrente(Articolo articoloCorrente) {
		this.articoloCorrente = articoloCorrente;
	}

	/**
	 * @param conaiBD
	 *            The conaiBD to set.
	 */
	public void setConaiBD(IConaiBD conaiBD) {
		this.conaiBD = conaiBD;
	}
}
