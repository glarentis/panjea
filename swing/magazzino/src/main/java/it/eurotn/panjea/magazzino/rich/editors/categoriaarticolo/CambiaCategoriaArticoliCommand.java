package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class CambiaCategoriaArticoliCommand extends ApplicationWindowAwareCommand {

	private class CategoriaForm extends PanjeaAbstractForm {
		/**
		 * Costruttore.
		 */
		public CategoriaForm() {
			super(PanjeaFormModelHelper.createFormModel(new CategoriaPM(), false, "categoriaForm"), "categoriaForm");
		}

		@Override
		protected JComponent createFormControl() {
			final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
			FormLayout layout = new FormLayout("right:pref,4dlu,fill:default:grow", "default");
			FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
			builder.setLabelAttributes("r, c");
			builder.addLabel("categoria");
			Binding categoriaBinding = bf.createBoundSearchText("categoria", new String[] { "codice", "descrizione" });
			builder.addBinding(categoriaBinding, 3);
			return builder.getPanel();
		}

	}

	public class CategoriaPM {
		private Categoria categoria;

		/**
		 * @return Returns the categoria.
		 */
		public Categoria getCategoria() {
			return categoria;
		}

		/**
		 * @param categoria
		 *            The categoria to set.
		 */
		public void setCategoria(Categoria categoria) {
			this.categoria = categoria;
		}
	}

	public static final String COMMAND_ID = "cambiaCategoriaArticoliCommand";
	public static final String PARAM_ARTICOLI = "paramArticoli";
	private final IMagazzinoAnagraficaBD iMagazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param iMagazzinoAnagraficaBD
	 *            bd anagarfica magazzino
	 */
	public CambiaCategoriaArticoliCommand(final IMagazzinoAnagraficaBD iMagazzinoAnagraficaBD) {
		super(COMMAND_ID);
		this.iMagazzinoAnagraficaBD = iMagazzinoAnagraficaBD;
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {

		@SuppressWarnings("unchecked")
		final List<ArticoloRicerca> articoli = (List<ArticoloRicerca>) getParameter(PARAM_ARTICOLI,
				new ArrayList<ArticoloRicerca>());
		if (!articoli.isEmpty()) {
			PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(new CategoriaForm(), null) {

				@Override
				protected boolean isMessagePaneVisible() {
					return false;
				}

				@Override
				protected boolean onFinish() {
					Categoria categoria = ((CategoriaPM) ((FormBackedDialogPage) this.getDialogPage())
							.getBackingFormPage().getFormObject()).getCategoria();
					if (categoria != null && categoria.getId() != null) {
						iMagazzinoAnagraficaBD.cambiaCategoriaAdArticoli(articoli, categoria);
					}
					return categoria != null && categoria.getId() != null;
				}
			};
			dialog.getDialog().setTitle(RcpSupport.getMessage("selezionaCategoriaDestinazione"));
			dialog.showDialog();
		}

	}

}
