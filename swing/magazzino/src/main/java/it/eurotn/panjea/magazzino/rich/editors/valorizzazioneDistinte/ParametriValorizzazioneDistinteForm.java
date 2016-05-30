package it.eurotn.panjea.magazzino.rich.editors.valorizzazioneDistinte;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.RicercaAvanzataArticoliCommand;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriValorizzazioneDistinteForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriValorizzazioneDistinteForm";
	private RicercaAvanzataArticoliCommand ricercaAvanzataArticoliCommand;
	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	/**
	 * Costruttore.
	 *
	 */
	public ParametriValorizzazioneDistinteForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriValorizzazioneDistinte(), false, FORM_ID), FORM_ID);
		magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);

	}

	private DefaultTreeModel caricaCategorie() {
		List<CategoriaLite> categorieRoot = magazzinoAnagraficaBD.caricaCategorie();
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new CategoriaLite());

		for (CategoriaLite categoriaLite : categorieRoot) {
			DefaultMutableTreeNode node = creaNodo(categoriaLite);
			rootNode.add(node);
		}
		return new DefaultTreeModel(rootNode);
	}

	private DefaultMutableTreeNode creaNodo(CategoriaLite categoria) {
		DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(categoria);
		for (CategoriaLite categoriaFiglia : categoria.getCategorieFiglie()) {
			nodo.add(creaNodo(categoriaFiglia));
		}
		return nodo;
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref,4dlu,fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
		// new
		// FormDebugPanel());
		builder.setLabelAttributes("r,c");

		builder.addPropertyAndLabel("modalitaValorizzazione", 1, 2);

		TableEditableBinding<ArticoloRicerca> bindingArticoli = new TableEditableBinding<ArticoloRicerca>(
				getFormModel(), "articoli", Set.class, new ArticoliRicercaValorizzazioneDistinteTableModel());
		builder.nextRow();
		builder.addComponent(getRicercaAvanzataArticoliCommand().createButton());
		builder.nextRow();
		builder.addBinding(bindingArticoli, 1, 6, 1, 1);
		getValueModel("articoli").setValue(new ArrayList<>());

		builder.addBinding(bf.createBoundCheckBoxTree("categorie", caricaCategorie()), 3, 6);
		return builder.getPanel();
	}

	/**
	 * @return the ricercaAvanzataArticoliCommand
	 */
	private RicercaAvanzataArticoliCommand getRicercaAvanzataArticoliCommand() {
		if (ricercaAvanzataArticoliCommand == null) {
			ricercaAvanzataArticoliCommand = new RicercaAvanzataArticoliCommand();
			ricercaAvanzataArticoliCommand.addCommandInterceptor(new ActionCommandInterceptor() {

				@Override
				public void postExecution(ActionCommand command) {
					List<ArticoloRicerca> articoliRicerca = ((RicercaAvanzataArticoliCommand) command)
							.getArticoliSelezionati();
					if (articoliRicerca != null && articoliRicerca.size() > 0) {
						List<ArticoloRicerca> articoli = new ArrayList<>(
								((ParametriValorizzazioneDistinte) getFormObject()).getArticoli());
						articoli.addAll(articoliRicerca);
						getValueModel("articoli").setValue(articoli);
					}

				}

				@Override
				public boolean preExecution(ActionCommand command) {
					return true;
				}
			});
		}

		return ricercaAvanzataArticoliCommand;
	}
}
