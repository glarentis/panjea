package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;
import org.springframework.richclient.layout.TableLayoutBuilder;

public class AttributiRigaArticoloBinding extends CustomBinding {

	/**
	 * 
	 * Comparator per gli attributi della riga articolo. Gli attributi vengono ordinati per numero riga, ordine e nome
	 * del tipo attributo.
	 * 
	 * @author fattazzo
	 * @version 1.0, 04/gen/2013
	 * 
	 */
	private class AttributoRigaArticoloComparator implements Comparator<AttributoRigaArticolo> {

		@Override
		public int compare(AttributoRigaArticolo a1, AttributoRigaArticolo a2) {
			if (a1.getRiga() == null) {
				return -1;
			}
			if (a1.getRiga().compareTo(a2.getRiga()) != 0) {
				return a1.getRiga().compareTo(a2.getRiga());
			} else if (a1.getOrdine().compareTo(a2.getOrdine()) != 0) {
				return a1.getOrdine().compareTo(a2.getOrdine());
			} else {
				return a1.getTipoAttributo().getNome().compareTo(a2.getTipoAttributo().getNome());
			}
		}

	}

	private class FocusListener extends FocusAdapter implements ItemListener {

		@Override
		public void focusGained(FocusEvent e) {
			// Controllo se il componente precedente che aveva il focus è quello che segue o quello che precede
			// Se è uno dei due sposto il focus di conseguenza. Se il focus era di un componente non attiguo non sposto
			// il focus
			boolean readOnly = false;
			if (e.getComponent() instanceof JTextField) {
				readOnly = !((JTextField) e.getComponent()).isEditable();
			} else {
				readOnly = e.getComponent().isEnabled();
			}
			if (readOnly) {
				Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
				Container root = c == null ? null : c.getFocusCycleRootAncestor();
				if (root != null) {
					FocusTraversalPolicy policy = root.getFocusTraversalPolicy();
					Component nextFocus = policy.getComponentAfter(root, c);
					Component prevFocus = policy.getComponentBefore(root, c);
					if (nextFocus == null) {
						nextFocus = policy.getDefaultComponent(root);
					}
					if (prevFocus == null) {
						prevFocus = policy.getDefaultComponent(root);
					}
					if (nextFocus == e.getOppositeComponent()) {
						e.getComponent().transferFocusBackward();
					} else if (prevFocus == e.getOppositeComponent()) {
						e.getComponent().transferFocus();
					}
				}
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			String codice = (String) ((JTextField) e.getSource()).getClientProperty(CODICE_TIPO_ATTRIBUTO_PROPERTY);
			String valore = ((JTextField) e.getSource()).getText();
			changeAttributo(codice, valore, false);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			String codice = (String) ((JCheckBox) e.getSource()).getClientProperty(CODICE_TIPO_ATTRIBUTO_PROPERTY);
			String valore = (e.getStateChange() == ItemEvent.SELECTED) ? "true" : "false";
			changeAttributo(codice, valore, false);
		}
	}

	protected static Logger logger = Logger.getLogger(AttributiRigaArticoloBinding.class);
	private JPanel rootPanel;
	private Map<String, JComponent> fields = new HashMap<String, JComponent>();
	private FocusListener attributoValueChange;

	public static final String CODICE_TIPO_ATTRIBUTO_PROPERTY = "codiceTipoAttributo";

	private FormuleTrasformazioneRigaArticoloFormCalculator formuleTrasformazioneCalcolator;

	/**
	 * Crea un binding per gli attributi dinamici della riga articolo.
	 * 
	 * @param component
	 *            component
	 * @param formModel
	 *            formModel della riga articolo
	 * @param formPropertyPath
	 *            formPropertyPath per la lista di attributi
	 * @param requiredSourceClass
	 *            Class della proprietà
	 */
	protected AttributiRigaArticoloBinding(final JComponent component, final FormModel formModel,
			final String formPropertyPath, final Class<?> requiredSourceClass) {
		super(formModel, formPropertyPath, requiredSourceClass);
		rootPanel = (JPanel) component;
		rootPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		attributoValueChange = new FocusListener();
		formuleTrasformazioneCalcolator = new FormuleTrasformazioneRigaArticoloFormCalculator();
		formuleTrasformazioneCalcolator.setFormModel(formModel);
	}

	/**
	 * Lancia l'evento di cambio attributo aggiornando il valore del codice del tipo attributo specificato.
	 * 
	 * @param codice
	 *            codice del tipo attributo
	 * @param valore
	 *            nuovo valore dell'attributo
	 * @param forzaCalcolo
	 *            true per fornzare il calcolo degli attributi anche se questi non sono cambiati.
	 */
	private void changeAttributo(String codice, String valore, boolean forzaCalcolo) {
		@SuppressWarnings("unchecked")
		List<AttributoRigaArticolo> attributi = (List<AttributoRigaArticolo>) getValueModel().getValue();
		List<AttributoRigaArticolo> list = new ArrayList<AttributoRigaArticolo>();

		for (AttributoRigaArticolo attributoRiga : attributi) {
			if (attributoRiga.getTipoAttributo().getCodice().equals(codice)) {
				if (attributoRiga.getValore() != null && attributoRiga.getValore().equals(valore) && !forzaCalcolo) {
					return;
				}
				attributoRiga.setValore(valore);
			}
			list.add(attributoRiga);
		}

		try {
			formuleTrasformazioneCalcolator.calcola();
		} catch (FormuleTipoAttributoException fex) {
			throw new RuntimeException(fex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		rootPanel.validate();
		controlValueChanged(getValueModel().getValue());
	}

	/**
	 * Aggiorna i controlli con gli attributi.
	 */
	@SuppressWarnings("unchecked")
	private void createAttributiControl() {
		logger.debug("--> Enter createAttributiControl");
		List<AttributoRigaArticolo> attributi = (List<AttributoRigaArticolo>) getValueModel().getValue();

		rootPanel.removeAll();

		TableLayoutBuilder tableLayoutBuilder = new TableLayoutBuilder(getComponentFactory().createPanel(
				new FlowLayout(FlowLayout.LEFT)));

		int riga = -1;

		Map<String, JComponent> nuoviControlli = new HashMap<String, JComponent>();
		Collections.sort(attributi, new AttributoRigaArticoloComparator());
		for (AttributoRigaArticolo attributoRiga : attributi) {

			if (riga != -1 && riga != attributoRiga.getRiga().intValue()) {
				tableLayoutBuilder.row();
			}
			riga = attributoRiga.getRiga();

			JComponent field = getFieldComponent(attributoRiga, !isReadOnly());
			nuoviControlli.put(attributoRiga.getTipoAttributo().getCodice(), field);
			JLabel nomeAttributo = getComponentFactory().createLabelFor(attributoRiga.getTipoAttributo().getNome(),
					field);

			ArticoloLite articolo = (ArticoloLite) getFormModel().getValueModel("articolo").getValue();
			// NPE Mail, succede quando "pulisco" l'articolo dalla search text e rimangono i controlli degli attributi,
			// modificato RigaArticoloDocumentoArticoloPropertyChange.propertyChange()
			// per pulire gli attributi insieme all'articolo
			String name = articolo.getCodice() + "." + nomeAttributo.getText().replaceAll(" ", "");

			nomeAttributo.setName(name + "Label");
			field.setName(name + "Component");

			tableLayoutBuilder.cell(nomeAttributo, "colSpec=left:pref");
			tableLayoutBuilder.cell("colSpec=left:2");
			tableLayoutBuilder.cell(field, "colSpec=fill:85");
			tableLayoutBuilder.cell("colSpec=left:2");

			UnitaMisura unitaMisura = attributoRiga.getTipoAttributo().getUnitaMisura();
			JLabel umAttr = new JLabel();
			if (unitaMisura != null) {
				umAttr = getComponentFactory().createLabel(unitaMisura.getCodice());
			}
			umAttr.setName(name + "UMLabel");
			tableLayoutBuilder.cell(umAttr, "colSpec=left:30");
		}

		fields.clear();
		fields = nuoviControlli;

		updateStateControl();

		JPanel pan = tableLayoutBuilder.getPanel();
		rootPanel.add(pan);
		rootPanel.updateUI();
		logger.debug("--> Exit createAttributiControl");
	}

	@Override
	protected JComponent doBindControl() {
		return rootPanel;
	}

	@Override
	protected void enabledChanged() {
		updateStateControl();
	}

	/**
	 * @param attributoRigaArticolo
	 *            attributo
	 * @param enabled
	 *            abilita o meno il campo craeto
	 * @return textfield per editing dell'attributo
	 */
	private JComponent getFieldComponent(AttributoRigaArticolo attributoRigaArticolo, boolean enabled) {
		logger.debug("--> Enter getFieldComponent enabled");

		JComponent component = fields.get(attributoRigaArticolo.getTipoAttributo().getCodice());
		if (component == null) {
			component = AttributoRigaComponentFactory.getComponent(attributoRigaArticolo);
			component.addFocusListener(attributoValueChange);
			if (component instanceof JCheckBox) {
				((JCheckBox) component).addItemListener(attributoValueChange);
			}
		}
		switch (attributoRigaArticolo.getTipoAttributo().getTipoDato()) {
		case BOOLEANO:
			JCheckBox checkBoxField = (JCheckBox) component;
			checkBoxField.setSelected((Boolean) attributoRigaArticolo.getValoreTipizzato());
			break;
		case NUMERICO:
			JFormattedTextField valoreNumericoField = (JFormattedTextField) component;
			if (attributoRigaArticolo.getValore() != null && !attributoRigaArticolo.getValore().isEmpty()) {
				DecimalFormat decimalFormat = new DecimalFormat();
				try {
					Number parsedNumber = decimalFormat.parse(attributoRigaArticolo.getValore());
					valoreNumericoField.setValue(parsedNumber.doubleValue());
				} catch (ParseException e) {
					valoreNumericoField.setText(attributoRigaArticolo.getValore());
				}
			}
			break;
		default:
			JTextField field = (JTextField) component;
			field.setText(attributoRigaArticolo.getValore());
			break;
		}
		component.putClientProperty(CODICE_TIPO_ATTRIBUTO_PROPERTY, attributoRigaArticolo.getTipoAttributo()
				.getCodice());
		logger.debug("--> Exit getFieldComponent");
		return component;
	}

	@Override
	protected void readOnlyChanged() {
		updateStateControl();
	}

	/**
	 * Aggiorna lo stato dei controlli.
	 */
	private void updateStateControl() {
		logger.debug("--> Enter updateStateControl readOnly " + isReadOnly() + " enabled " + isEnabled());
		@SuppressWarnings("unchecked")
		List<AttributoRigaArticolo> attributi = (List<AttributoRigaArticolo>) getValueModel().getValue();
		for (AttributoRigaArticolo attributoRigaArticolo : attributi) {
			boolean updatable = attributoRigaArticolo.isUpdatable();
			if (attributoRigaArticolo.getTipoAttributo().getTipoDato() == ETipoDatoTipoAttributo.NUMERICO
					|| attributoRigaArticolo.getTipoAttributo().getTipoDato() == ETipoDatoTipoAttributo.STRINGA) {
				if (fields.get(attributoRigaArticolo.getTipoAttributo().getCodice()) != null) {
					((JTextField) fields.get(attributoRigaArticolo.getTipoAttributo().getCodice()))
							.setEditable(updatable && !isReadOnly());
				}
			} else {
				fields.get(attributoRigaArticolo.getTipoAttributo().getCodice()).setEnabled(!isReadOnly());
			}
		}
	}

	@Override
	protected void valueModelChanged(Object arg0) {
		createAttributiControl();
	}
}
