package it.eurotn.panjea.mrp.rich.editors.risultato;

import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.rich.bd.IMrpBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class EditFrameRisultatoMrpFlat extends EditFrame<RisultatoMrpFlat> {
	private static final long serialVersionUID = 4877257829689338379L;
	private IMrpBD mrpBD;

	/**
	 * Costruttore di default per il frame del dettaglio.
	 *
	 * @param pageEditor
	 *            pagina che visualizza/modifica il bean
	 * @param mrpBD
	 *            bd mrp
	 */
	public EditFrameRisultatoMrpFlat(final AbstractTablePageEditor<RisultatoMrpFlat> pageEditor, final IMrpBD mrpBD) {
		super(EEditPageMode.POPUP, pageEditor, EditFrame.QUICK_ACTION_DEFAULT);
		this.mrpBD = mrpBD;
	}

	@Override
	public RisultatoMrpFlat getTableManagedObject(Object object) {
		List<RisultatoMrpFlat> selectedObjects = tableWidget.getSelectedObjects();
		RisultatoMrpFlat rigaModificata = (RisultatoMrpFlat) object;
		List<RisultatoMrpFlat> risultatiDaSalvare = new ArrayList<RisultatoMrpFlat>();
		for (RisultatoMrpFlat risultatoMrpFlat : selectedObjects) {
			if (rigaModificata.getTipoAreaOrdineDaGenerare() != null) {
				risultatoMrpFlat.setTipoAreaOrdineDaGenerare(rigaModificata.getTipoAreaOrdineDaGenerare());
			}
			if (rigaModificata.getFornitore() != null) {
				risultatoMrpFlat.setFornitore(rigaModificata.getFornitore());
			}
			if (rigaModificata.getDataConsegna() != null) {
				risultatoMrpFlat.setDataConsegna(rigaModificata.getDataConsegna());
			}
			if (rigaModificata.getDataDocumento() != null) {
				risultatoMrpFlat.setDataDocumento(rigaModificata.getDataDocumento());
			}
			if (rigaModificata.getQtaCalcolata() != null) {
				risultatoMrpFlat.setQtaCalcolata(rigaModificata.getQtaCalcolata());
			}
			risultatiDaSalvare.add(risultatoMrpFlat);
		}
		risultatiDaSalvare = mrpBD.salvaRigheRisultato(risultatiDaSalvare);
		tableWidget.setObjects(risultatiDaSalvare);
		return null;
	}

	@Override
	public void update(Observable o, Object obj) {
		List<RisultatoMrpFlat> selectedObjects = tableWidget.getSelectedObjects();
		if (selectedObjects.size() > 1) {
			RisultatoMrpFlat risultatoMerge = (RisultatoMrpFlat) PanjeaSwingUtil.cloneObject(selectedObjects.get(0));
			for (RisultatoMrpFlat risultatoMrpFlat : selectedObjects) {
				if (risultatoMerge.getDataConsegna() != null
						&& !risultatoMerge.getDataConsegna().equals(risultatoMrpFlat.getDataConsegna())) {
					risultatoMerge.setDataConsegna(null);
				}
				if (risultatoMerge.getDataDocumento() != null
						&& !risultatoMerge.getDataDocumento().equals(risultatoMrpFlat.getDataDocumento())) {
					risultatoMerge.setDataDocumento(null);
				}
				if (risultatoMerge.getFornitore() != null
						&& !risultatoMerge.getFornitore().equals(risultatoMrpFlat.getFornitore())) {
					risultatoMerge.setFornitore(null);
				}
				if (risultatoMerge.getQtaCalcolata() != null
						&& risultatoMerge.getQtaCalcolata() != risultatoMrpFlat.getQtaCalcolata()) {
					risultatoMerge.setQtaCalcolata(null);
				}
				if (risultatoMerge.getTipoAreaOrdineDaGenerare() != null
						&& !risultatoMerge.getTipoAreaOrdineDaGenerare().equals(
								risultatoMrpFlat.getTipoAreaOrdineDaGenerare())) {
					risultatoMerge.setTipoAreaOrdineDaGenerare(null);
				}
			}
			obj = risultatoMerge;
		}
		super.update(o, obj);
	}
}
