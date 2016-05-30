package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.ContoPartita;
import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartita;
import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartitaConti;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Comparator;

import org.apache.log4j.Logger;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.binding.value.support.ValueHolder;

public class StrategiaCalcoloPartitaContiComponent extends AbstractTablePageEditor<ContoPartita> {

	private static Logger logger = Logger.getLogger(StrategiaCalcoloPartitaContiComponent.class);
	private static final String PAGE_ID = "strategiaCalcoloPartitaContiTablePage";

	/**
	 * Costruttore.
	 * 
	 * @param strategie
	 *            valueHolder con strategie
	 * @param parentModel
	 *            formModel del parent
	 */
	public StrategiaCalcoloPartitaContiComponent(final ValueHolder strategie, final HierarchicalFormModel parentModel) {
		super(PAGE_ID, new String[] { "codiceConto", "dareAvere", "tipoOperazione" }, ContoPartita.class);
		setShowTitlePane(false);
		setEditPage(new ContoPartitaPage());
		strategie.addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				logger.debug("--> Enter propertyChange. Cambio il formObject con la struttura partita 0.Lista strutture "
						+ evt);
				// Se e' cambiata la lista di selezione delle strategie mi recupero la strategia che devo gestire
				StrategiaCalcoloPartita[] strategieNew = (StrategiaCalcoloPartita[]) evt.getNewValue();
				// La strutturaPartitaConti e' sempre all'indice 1
				setRows(((StrategiaCalcoloPartitaConti) strategieNew[1]).getContiPartita());
				logger.debug("--> Exit propertyChange");
			}
		});
	}

	/**
	 * 
	 * @return comparator del conto per il codice del sottoconto
	 */
	protected Comparator<ContoPartita> createTableComparator() {
		return new Comparator<ContoPartita>() {

			@Override
			public int compare(ContoPartita conto0, ContoPartita conto1) {
				return conto0.getSottoConto().getSottoContoCodice()
						.compareTo(conto1.getSottoConto().getSottoContoCodice());
			}
		};
	}

	@Override
	public Collection<ContoPartita> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<ContoPartita> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
	}

}
