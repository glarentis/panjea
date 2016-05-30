package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.control.table.layout.DefaultTableLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.richclient.settings.Settings;

import com.jidesoft.pivot.AggregateTable;

public class EffettiLayoutManager extends DefaultTableLayoutManager {

	public enum Mode {
		GENERALE(new int[] { -1 }), ACCREDITI(new int[] { 0 }), INSOLUTI(new int[] { 4 }), ANTICIPI(new int[] { 1 });

		private int[] colonneAbilitatePerSelezione;

		/**
		 *
		 * Costruttore.
		 *
		 * @param colonneAbilitatePerSelezione
		 *            indica l'indice delle colonne abilitate per poter selezionare l'effetto od un gruppo di effetti.
		 */
		private Mode(final int[] colonneAbilitatePerSelezione) {
			this.colonneAbilitatePerSelezione = colonneAbilitatePerSelezione;
		}

		/**
		 * @return Returns the colonneAbilitatePerSelezione.
		 */
		public int[] getColonneAbilitatePerSelezione() {
			return colonneAbilitatePerSelezione;
		}
	}

	private Map<String, int[]> customLayoutMap;

	private it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.EffettiLayoutManager.Mode mode;

	private Settings settings;

	{
		customLayoutMap = new TreeMap<String, int[]>();
		customLayoutMap.put("ACC.data valuta e banca", new int[] { 0, 1, 2 });
		customLayoutMap.put("ACC.banca e data valuta", new int[] { 1, 0, 2 });
		customLayoutMap.put("ACC.dopo incasso", new int[] { 3 });
		customLayoutMap.put("ANT.banca e data valuta", new int[] { 1, 0, 2 });
	}

	/**
	 * Costruttore.
	 *
	 * @param jideTableWidget
	 *            table widget di riferimento
	 */
	public EffettiLayoutManager(final JideTableWidget<?> jideTableWidget) {
		super(jideTableWidget);
	}

	@Override
	public void applica(TableLayout layout) {
		if (layout != null) {
			if (layout.getName() == null) {
				String layoutSettingsSaveKey = settings.getString("accreditiLayout");
				layout.setChiave(layoutSettingsSaveKey);
			}

			if (layout.getChiave().startsWith("ACC") || layout.getChiave().startsWith("ANT")) {
				((AggregateTable) tableWidget.getTable()).getAggregateTableModel().setAggregatedColumns(
						customLayoutMap.get(layout.getChiave()));
				((AggregateTable) tableWidget.getTable()).getAggregateTableModel().aggregate();
				settings.setString("accreditiLayout", layout.getChiave());
				try {
					settings.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				super.applica(layout);
			}
		}
	}

	@Override
	public List<TableLayout> getLayouts() {

		if (mode == Mode.GENERALE) {
			return super.getLayouts();
		}

		List<TableLayout> layouts = new ArrayList<TableLayout>();

		// aggiungo i layout per Accrediti e Anticipi
		for (Entry<String, int[]> entry : customLayoutMap.entrySet()) {
			if (mode == Mode.ACCREDITI && !entry.getKey().startsWith("ACC")) {
				continue;
			}

			if (mode == Mode.ANTICIPI && !entry.getKey().startsWith("ANT")) {
				continue;
			}

			TableLayout customLayout = new TableLayout();
			customLayout.setChiave(entry.getKey());
			customLayout.setName(entry.getKey().substring(4, entry.getKey().length()));
			layouts.add(customLayout);

		}
		return layouts;
	}

	@Override
	public boolean isReadOnly() {
		return Mode.GENERALE != mode;
	}

	/**
	 *
	 * @param settingsParam
	 *            settings.
	 */
	public void restoreState(Settings settingsParam) {
		this.settings = settingsParam;
	}

	/**
	 * @param mode
	 *            azione
	 */
	public void setMode(Mode mode) {
		this.mode = mode;
	}

}
