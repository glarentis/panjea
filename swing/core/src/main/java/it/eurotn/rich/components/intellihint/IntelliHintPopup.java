/**
 *
 */
package it.eurotn.rich.components.intellihint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.list.FilteredListModel;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.rules.constraint.Constraint;

import com.jidesoft.popup.JidePopup;

/**
 * Popup che si posiziona relativamente al {@link JTextField} e ne visualizza tutti i valori selezionabili forniti dalla
 * classe {@link IntelliHintDecorator}.
 * 
 * @author fattazzo
 */
public class IntelliHintPopup extends JidePopup {

	private class FilterConstraint implements Constraint {

		private String filterValue = "";

		/**
		 * @param filterValue
		 *            the filter value to set
		 */
		public void setFilterValue(String filterValue) {
			this.filterValue = filterValue;
			createListValues();
		}

		@Override
		public boolean test(Object argument) {
			return ((String) argument).toUpperCase().startsWith(filterValue.toUpperCase());
		}

	}

	private class RefreshCommand extends ApplicationWindowAwareCommand {

		public static final String COMMAND_ID = "refreshCommand";

		/**
		 * Costruttore di default.
		 */
		public RefreshCommand() {
			super(COMMAND_ID);
			setSecurityControllerId(COMMAND_ID);
			CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
					CommandConfigurer.class);
			c.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			createListValues();
		}

	}

	private static final long serialVersionUID = 5336772226093610877L;

	private final IntelliHintDecorator intelliHintDecorator;

	@SuppressWarnings("rawtypes")
	private final JList valuesList = new JList();
	private FilteredListModel valuesListModel = null;
	private final FilterConstraint filterConstraint = new FilterConstraint();

	private static final int POPUP_HEIGHT = 200;

	/**
	 * Costruttore di default.
	 * 
	 * @param paramIntellHintDecorator
	 *            IntellihintDecorator
	 */
	public IntelliHintPopup(final IntelliHintDecorator paramIntellHintDecorator) {
		super();
		this.intelliHintDecorator = paramIntellHintDecorator;

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(createControl(), BorderLayout.CENTER);

		configurePopup();

		addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				valuesList.requestFocusInWindow();
			}

			@Override
			public void focusLost(FocusEvent e) {
				IntelliHintPopup.this.hidePopup();
			}
		});
	}

	/**
	 * Configura posizione e dimensioni del popup.
	 */
	private void configurePopup() {
		this.setMovable(false);
		this.setOwner(intelliHintDecorator.getTextFieldComponent());
		this.setPreferredSize(new Dimension(300, POPUP_HEIGHT));
	}

	/**
	 * Crea tutti i controlli da inserire nella popup.
	 * 
	 * @return Controlli creati
	 */
	private JComponent createControl() {

		createListValues();

		JPanel panel = new JPanel(new BorderLayout());

		// lista dei valori
		panel.add(valuesList, BorderLayout.CENTER);

		// pulsante ricarica
		CommandGroup commandGroup = new CommandGroup();
		commandGroup.add(new RefreshCommand());
		JComponent buttonBar = commandGroup.createButtonBar();
		buttonBar.setBorder(GuiStandardUtils.createTopAndBottomBorder(3));
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(buttonBar, BorderLayout.CENTER);

		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * Carica tutti i valori nella list.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createListValues() {

		DefaultListModel listModel = new DefaultListModel();

		listModel.clear();

		// carico i valori dalla closure
		List<String> listaValori = (List<String>) intelliHintDecorator.getClosure().call(null);

		for (String valore : listaValori) {
			listModel.addElement(valore);
		}

		valuesListModel = new FilteredListModel(listModel, filterConstraint);

		valuesList.setModel(valuesListModel);

		if (listaValori.size() > 0) {
			valuesList.setSelectedIndex(0);
		}
	}

	/**
	 * Restituisce il valore selezionato della lista.
	 * 
	 * @return String
	 */
	public String getSelectedValue() {

		if (valuesList.getSelectedValue() == null) {
			return null;
		} else {
			String selValue = (String) valuesList.getSelectedValue();
			String[] selValueSplit = selValue.split(IntelliHintDecorator.INTELLIHINT_VALUE_SEPARATOR);
			return selValueSplit[0];
		}
	}

	/**
	 * Seleziona nella lista il valore precedente a quello attualmente selezionato.
	 * 
	 */
	public void selecPriorValue() {
		if (valuesList.getSelectedIndex() > 0) {
			valuesList.setSelectedIndex(valuesList.getSelectedIndex() - 1);
		}
	}

	/**
	 * Seleziona nella lista il valore successivo a quello attualmente selezionato.
	 * 
	 */
	public void selectNextValue() {
		if (valuesList.getSelectedIndex() < valuesList.getModel().getSize()) {
			valuesList.setSelectedIndex(valuesList.getSelectedIndex() + 1);
		}
	}

	/**
	 * @param paramKey
	 *            the param for the filter
	 */
	public void setFilterKey(String paramKey) {
		this.filterConstraint.setFilterValue(paramKey);
		createListValues();
	}
}
