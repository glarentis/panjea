package it.eurotn.panjea.magazzino.rich.editors.entita;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.editors.table.EditFrame;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

public class ArticoliEntitaTablePage extends AbstractTablePageEditor<CodiceArticoloEntita> {

	public static final String PAGE_ID = "articoliEntitaTablePage";

	private Entita entita;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private IAnagraficaBD anagraficaBD;

	private JCheckBox assortimentoCheckBox;

	/**
	 * Costruttore.
	 * 
	 */
	protected ArticoliEntitaTablePage() {
		super(PAGE_ID, new String[] { "articolo", "codice" }, CodiceArticoloEntita.class);
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

		assortimentoCheckBox = getComponentFactory().createCheckBox(RcpSupport.getMessage("assortimentoArticoli"));
		assortimentoCheckBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				boolean select = assortimentoCheckBox.isSelected();

				if (select ^ entita.isAssortimentoArticoli()) {
					entita.setAssortimentoArticoli(select);
					try {
						entita = anagraficaBD.salvaEntita(entita);
					} catch (AnagraficheDuplicateException e1) {
						// eccezione mai sollevata perchè vado sempre in modifica di una entità esistente
						logger.error("-->errore, anagrafica duplicata", e1);
					}
					((ArticoloEntitaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setEntita(entita);
					ArticoliEntitaTablePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, entita);
					logger.debug("Salvo entita con flag a " + select);
				}
			}
		});
		rootPanel.add(assortimentoCheckBox, BorderLayout.NORTH);

		GuiStandardUtils.attachBorder(rootPanel);

		return rootPanel;
	}

	@Override
	public Collection<CodiceArticoloEntita> loadTableData() {
		List<CodiceArticoloEntita> codici = Collections.emptyList();

		if (entita != null && entita.getId() != null) {
			codici = magazzinoAnagraficaBD.caricaCodiciArticoloEntita(entita);
		}

		return codici;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return this.entita.getId() != null;
	}

	@Override
	public Collection<CodiceArticoloEntita> refreshTableData() {
		return loadTableData();
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
	}

	@Override
	public void setFormObject(Object object) {
		this.entita = (Entita) object;
		assortimentoCheckBox.setSelected(this.entita.isAssortimentoArticoli());

		((ArticoloEntitaPage) getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME)).setEntita(entita);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
