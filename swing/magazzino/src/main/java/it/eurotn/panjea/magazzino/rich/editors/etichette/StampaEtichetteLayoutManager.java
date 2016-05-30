package it.eurotn.panjea.magazzino.rich.editors.etichette;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.magazzino.domain.etichetta.ParametriStampaEtichetteArticolo;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.layout.DefaultTableLayoutManager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.utils.ByteArrayOutputStream;

import com.jidesoft.grid.TableColumnChooser;

public class StampaEtichetteLayoutManager extends DefaultTableLayoutManager {

	private ParametriStampaEtichetteArticolo parametriStampa;

	private static final String DEFAULT_LAYOUT = "Standard";
	private static final String LOTTI_LAYOUT = "Gestione lotti";

	private TableLayout tableLayoutRef;

	/**
	 * Costruttore.
	 * 
	 * @param jideTableWidget
	 *            table widget
	 * @param parametri
	 *            parametri stampa etichette
	 */
	public StampaEtichetteLayoutManager(final JideTableWidget<?> jideTableWidget,
			final ParametriStampaEtichetteArticolo parametri) {
		super(jideTableWidget);
		this.parametriStampa = parametri;
	}

	@Override
	public void applica(TableLayout layout) {

		// se non ho un layout di default vado a crearlo
		if (getLayouts().isEmpty()) {
			createDefaultLayout();
		}
		super.applica(tableLayoutRef);
	}

	/**
	 * Crea il layout di default in base ai parametri di ricerca.
	 * 
	 * @return layout creato
	 */
	private AbstractLayout createDefaultLayout() {

		TableColumnChooser.resetColumnsToDefault(getTableWidget().getTable());

		tableLayoutRef = new TableLayout();
		tableLayoutRef.setChiave("stampaEtichetteArticoloTablePageWidget");
		tableLayoutRef.setGlobal(true);
		tableLayoutRef.setUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

		if (parametriStampa.isGestioneLotti()) {
			tableLayoutRef.setName(LOTTI_LAYOUT);
		} else {
			tableLayoutRef.setName(DEFAULT_LAYOUT);
			TableColumnChooser.hideColumns(getTableWidget().getTable(), new int[] { 7, 8, 9 });
		}

		return salva(tableLayoutRef);

	}

	/**
	 * @return nome del layout in base ai parametri di ricerca
	 */
	private String getLayoutName() {
		if (parametriStampa.isGestioneLotti()) {
			return LOTTI_LAYOUT;
		}

		return DEFAULT_LAYOUT;
	}

	@Override
	public List<TableLayout> getLayouts() {

		List<TableLayout> layoutsFiltered = new ArrayList<TableLayout>();
		List<TableLayout> layouts = super.getLayouts();

		for (TableLayout tableLayout : layouts) {
			if (tableLayout.getName().equals(getLayoutName())) {
				layoutsFiltered.add(tableLayout);
				tableLayoutRef = tableLayout;
				break;
			}
		}

		return layoutsFiltered;
	}

	@Override
	public boolean isReadOnly() {
		// sovrascrivo il metodo perchè l'utente non può inserire/modificare/cancellare nessun layout
		return Boolean.TRUE;
	}

	@Override
	public AbstractLayout salva(TableLayout layout) {

		// anche se il layout manager è in readOnly salvo comunque il layout come è stato impostato dall'utente
		layout = tableLayoutRef;

		OutputStream dataLayout = new ByteArrayOutputStream();

		((ITable<?>) this.tableWidget.getTable()).saveLayout(dataLayout);

		AbstractLayout al = null;
		if (layout != null) {
			layout.setData(dataLayout.toString());
			al = getTableLayoutCache().salva(layout);
		}
		return al;
	}

	/**
	 * @param parametriStampaEtichetteArticolo
	 *            The parametriStampaEtichetteArticolo to set.
	 */
	public void setParametriStampaEtichetteArticolo(ParametriStampaEtichetteArticolo parametriStampaEtichetteArticolo) {
		this.parametriStampa = parametriStampaEtichetteArticolo;
	}
}
