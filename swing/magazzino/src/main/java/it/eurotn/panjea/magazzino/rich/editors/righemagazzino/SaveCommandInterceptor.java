package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.ToolbarPageEditor.SaveCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.InputApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

public class SaveCommandInterceptor extends ActionCommandInterceptorAdapter {

	private class ParametroNote {
		private String valore;
		private String nome;
		private final String segnaposto;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param segnaposto
		 *            valore del segnaposto
		 */
		public ParametroNote(final String segnaposto) {
			this.segnaposto = segnaposto;
			nome = segnaposto.substring(1).substring(0, segnaposto.length() - 2);
		}

		/**
		 * @return Returns the nome.
		 */
		public String getNome() {
			return nome;
		}

		/**
		 * @return Returns the segnaposto.
		 */
		public String getSegnaposto() {
			return segnaposto;
		}

		/**
		 * @return Returns the valore.
		 */
		public String getValore() {
			return valore;
		}

		/**
		 * @param valore
		 *            The valore to set.
		 */
		@SuppressWarnings("unused")
		public void setValore(String valore) {
			this.valore = valore;
		}

	}

	private List<TipoMovimento> tipiMovimentoLottiAutomatici;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	{
		tipiMovimentoLottiAutomatici = new ArrayList<TipoMovimento>();
		tipiMovimentoLottiAutomatici.add(TipoMovimento.CARICO);
		tipiMovimentoLottiAutomatici.add(TipoMovimento.TRASFERIMENTO);
		tipiMovimentoLottiAutomatici.add(TipoMovimento.CARICO_PRODUZIONE);
	}

	/**
	 * Costruttore.
	 * 
	 */
	public SaveCommandInterceptor() {
		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
	}

	@Override
	public boolean preExecution(ActionCommand command) {
		SaveCommand saveCommand = (SaveCommand) command;
		FormsBackedTabbedDialogPageEditor rigaArticoloPage = (FormsBackedTabbedDialogPageEditor) saveCommand
				.getPageEditor();

		IRigaArticoloDocumento rigaMovimento = (IRigaArticoloDocumento) rigaArticoloPage.getForm().getFormObject();

		if (rigaMovimento.getNoteRiga() != null && rigaMovimento.getNoteRiga().contains("$")) {
			rigaArticoloPage.setTabForm(2);
			Pattern pattern = Pattern.compile(TipoAttributo.PATTERN_SEARCH);
			Matcher matcher = pattern.matcher(rigaMovimento.getNoteRiga());
			while (matcher.find()) {
				ParametroNote parametro = new ParametroNote(matcher.group());
				// valoreSegnaposto);
				InputApplicationDialog inputDialog = new InputApplicationDialog(parametro, "valore");
				inputDialog.setTitle("Parametro note");
				inputDialog.setInputLabelMessage(parametro.getNome());
				inputDialog.showDialog();
				if (parametro.getValore() != null) {
					rigaArticoloPage
							.getForm()
							.getValueModel("noteRiga")
							.setValue(
									rigaMovimento.getNoteRiga().replace(parametro.getSegnaposto(),
											parametro.getValore()));
				}
			}
		}

		boolean lottiAutomatici = magazzinoAnagraficaBD.caricaMagazzinoSettings().isGestioneLottiAutomatici();

		TipoMovimento tipoMovimento = TipoMovimento.NESSUNO;
		boolean gestioneLottiTipoDocumento = false;
		if (rigaMovimento instanceof RigaArticolo) {
			RigaArticolo rigaArticolo = (RigaArticolo) rigaMovimento;
			gestioneLottiTipoDocumento = rigaArticolo.getAreaMagazzino().getDocumento().getTipoDocumento()
					.isGestioneLotti();
			tipoMovimento = rigaArticolo.getAreaMagazzino().getTipoAreaMagazzino().getTipoMovimento();

			if (gestioneLottiTipoDocumento
					&& (!lottiAutomatici || (lottiAutomatici && tipiMovimentoLottiAutomatici.contains(tipoMovimento)))) {
				if ((!rigaArticolo.isLottiValid())) {
					rigaArticoloPage.setTabForm(3);
					return false;
				}
			}

			if (rigaArticolo.isNew() && gestioneLottiTipoDocumento
					&& rigaArticolo.getArticolo().getTipoLotto() == TipoLotto.LOTTO
					&& magazzinoAnagraficaBD.caricaMagazzinoSettings().isNuovoDaUltimoLotto()
					&& rigaArticoloPage.getTabbedSelected() == 0
					&& tipiMovimentoLottiAutomatici.contains(tipoMovimento)) {
				rigaArticoloPage.setTabForm(3);
				return false;
			}
		}
		return true;
	}

}