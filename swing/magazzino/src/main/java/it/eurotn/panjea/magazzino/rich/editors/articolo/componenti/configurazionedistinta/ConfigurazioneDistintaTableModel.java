package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.TreeTableModel;

public class ConfigurazioneDistintaTableModel extends TreeTableModel<ComponenteRow> {

	private static final long serialVersionUID = -1930483679348136481L;

	private Set<Componente> componenti;
	private IMagazzinoAnagraficaBD bd;
	private Set<FaseLavorazioneArticolo> fasiLivello0;

	/**
	 * @param articoloConfigurazioneDistinta
	 *            configurazione distinta
	 */
	public ConfigurazioneDistintaTableModel(final ArticoloConfigurazioneDistinta articoloConfigurazioneDistinta) {
		super();
		bd = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
		setArticoloConfigurazioneDistinta(articoloConfigurazioneDistinta);
	}

	/**
	 * @param componente
	 *            componente che la riga wrappa
	 * @return Row che wrappa il componente.
	 */
	public ComponenteRow creaRiga(Componente componente) {
		ComponenteRow result = new ComponenteRow(componente, bd);
		for (Componente componenteFiglio : componente.getArticolo().getComponenti()) {
			result.addChild(creaRiga(componenteFiglio));
		}
		for (FaseLavorazioneArticolo fase : componente.getFasiLavorazioneArticolo()) {
			result.addChild(new FaseLavorazioneRow(fase, bd));
		}
		return result;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return ArticoloLite.class;
		case 1:
			return String.class;
		case 2:
			return Double.class;
		case 3:
			return Integer.class;
		default:
			return super.getColumnClass(column);
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return RcpSupport.getMessage(Articolo.class.getName());
		case 1:
			return RcpSupport.getMessage("formula");
		case 2:
			return RcpSupport.getMessage("qtaAttrezzaggio");
		case 3:
			return RcpSupport.getMessage("ordinamento");
		default:
			return super.getColumnName(column);
		}
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		ComponenteRow componenteRow = getRowAt(row);
		return componenteRow.getConverterContextAt(column);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col > 0;
	}

	/**
	 * @param articoloConfigurazioneDistinta
	 *            the articoloConfigurazioneDistinta to set to initialize the model
	 */
	public void setArticoloConfigurazioneDistinta(ArticoloConfigurazioneDistinta articoloConfigurazioneDistinta) {
		this.componenti = articoloConfigurazioneDistinta.getComponenti();
		this.fasiLivello0 = articoloConfigurazioneDistinta.getFasi();

		List<ComponenteRow> result = new ArrayList<ComponenteRow>();
		if (componenti != null) {
			for (Componente componente : componenti) {
				result.add(creaRiga(componente));
			}
		}
		if (fasiLivello0 != null) {
			for (FaseLavorazioneArticolo fase : fasiLivello0) {
				result.add(new FaseLavorazioneRow(fase, bd));
			}
		}

		setOriginalRows(result);
	}
}
