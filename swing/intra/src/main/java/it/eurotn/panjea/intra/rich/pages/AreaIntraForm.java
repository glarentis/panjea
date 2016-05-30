package it.eurotn.panjea.intra.rich.pages;

import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.rich.converter.DocumentoConverter;
import it.eurotn.panjea.intra.domain.AreaIntra;
import it.eurotn.panjea.intra.domain.RigaBeneIntra;
import it.eurotn.panjea.intra.domain.RigaServizioIntra;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableEditableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class AreaIntraForm extends PanjeaAbstractForm {

	private class TipoPeriodoChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			TipoPeriodo tipoPeriodo = (TipoPeriodo) evt.getNewValue();
			updateComponentsMensiliVisibility(tipoPeriodo);

			if (!getFormModel().isReadOnly()) {
				getValueModel("modalitaTrasporto").setValue(null);
				getValueModel("gruppoCondizioneConsegna").setValue(null);
				getValueModel("paese").setValue(null);
				getValueModel("provincia").setValue(null);
			}
		}

	}

	private static final String FORM_ID = "areaIntraForm";

	private RigheBeneTableModel righeBeneTableModel;

	private RigheServiziTableModel righeServiziTableModel;
	private List<JComponent> componentsMensile = null;

	private TipoPeriodoChangeListener tipoPeriodoChangeListener;

	/**
	 * Costruttore.
	 * 
	 * @param areaIntra
	 *            areaIntra da modificare
	 */
	public AreaIntraForm(final AreaIntra areaIntra) {
		super(PanjeaFormModelHelper.createFormModel(areaIntra, false, FORM_ID));

		componentsMensile = new ArrayList<JComponent>();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:90dlu,4dlu,left:default, 53dlu, right:pref,4dlu,left:default, 10dlu, fill:default:grow",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);
		DocumentoConverter dc = new DocumentoConverter();
		StringBuilder sb = new StringBuilder("<HTML><B>");
		sb.append(dc.toString(((AreaIntra) getFormObject()).getDocumento(), null));
		sb.append("</B></HYML>");
		JLabel labelDocumento = new JLabel(sb.toString());
		builder.addComponent(labelDocumento, 1, 2, 9, 1);
		builder.nextRow();

		builder.addPropertyAndLabel("operazioneTriangolare");
		builder.addPropertyAndLabel("tipoPeriodo", 5);
		builder.nextRow();

		JComponent[] modTrasportoComponents = builder.addPropertyAndLabel("modalitaTrasporto");
		componentsMensile.add(modTrasportoComponents[0]);
		componentsMensile.add(modTrasportoComponents[1]);
		builder.nextRow();

		JComponent[] condConsegnaComponents = builder.addPropertyAndLabel("gruppoCondizioneConsegna", 1, 8, 5);
		componentsMensile.add(condConsegnaComponents[0]);
		componentsMensile.add(condConsegnaComponents[1]);

		builder.nextRow();
		builder.addPropertyAndLabel("naturaTransazione");
		builder.addPropertyAndLabel("modalitaIncasso", 5);
		builder.nextRow();
		JLabel paeseLabel = builder.addLabel("paese", 1);
		componentsMensile.add(paeseLabel);
		SearchPanel searchPanelPaese = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("paese", null, Nazione.class), 3);
		componentsMensile.add(searchPanelPaese);
		searchPanelPaese.getTextFields().get(null).setColumns(4);

		JLabel provLabel = builder.addLabel("provincia", 5);
		componentsMensile.add(provLabel);
		SearchPanel searchPanelProvincia = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("provincia", null, LivelloAmministrativo2.class), 7);
		componentsMensile.add(searchPanelProvincia);
		searchPanelProvincia.getTextFields().get(null).setColumns(12);
		builder.nextRow();

		builder.setLabelAttributes("r,t");
		builder.addLabel("righeBene");
		righeBeneTableModel = new RigheBeneTableModel((AreaIntra) getFormObject());
		righeServiziTableModel = new RigheServiziTableModel((AreaIntra) getFormObject());
		TableEditableBinding<RigaBeneIntra> righeBenebinding = new TableEditableBinding<RigaBeneIntra>(getFormModel(),
				"righeBene", Set.class, righeBeneTableModel);
		righeBenebinding.getControl().setPreferredSize(new Dimension(100, 150));
		builder.addBinding(righeBenebinding, 3, 14, 7, 1);
		builder.nextRow();
		builder.addLabel("righeServizio");
		builder.nextRow();
		TableEditableBinding<RigaServizioIntra> righeServizioBinding = new TableEditableBinding<RigaServizioIntra>(
				getFormModel(), "righeServizio", Set.class, righeServiziTableModel);
		righeServizioBinding.getControl().setPreferredSize(new Dimension(100, 150));
		builder.addBinding(righeServizioBinding, 3, 16, 7, 1);

		updateComponentsMensiliVisibility(((AreaIntra) getFormObject()).getTipoPeriodo());
		addFormValueChangeListener("tipoPeriodo", getTipoPeriodoChangeListener());

		return builder.getPanel();
	}

	@Override
	public void dispose() {
		removeFormValueChangeListener("tipoPeriodo", getTipoPeriodoChangeListener());
		super.dispose();
	}

	/**
	 * @return TipoPeriodoChangeListener
	 */
	private TipoPeriodoChangeListener getTipoPeriodoChangeListener() {
		if (tipoPeriodoChangeListener == null) {
			tipoPeriodoChangeListener = new TipoPeriodoChangeListener();
		}
		return tipoPeriodoChangeListener;
	}

	@Override
	public void setFormObject(Object formObject) {
		if (isControlCreated()) {
			AreaIntra areaIntra = (AreaIntra) formObject;

			righeBeneTableModel.setAreaIntra(areaIntra);
			righeServiziTableModel.setAreaIntra(areaIntra);

			updateComponentsMensiliVisibility(areaIntra.getTipoPeriodo());
		}
		super.setFormObject(formObject);
	}

	/**
	 * @param tipoPeriodo
	 *            update the visibility of the components for the monthly report
	 */
	private void updateComponentsMensiliVisibility(TipoPeriodo tipoPeriodo) {
		boolean isMensile = true;
		if (tipoPeriodo != null) {
			isMensile = tipoPeriodo.equals(TipoPeriodo.M);
		}
		for (JComponent comp : componentsMensile) {
			comp.setVisible(isMensile);
		}
	}

}
