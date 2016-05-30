package it.eurotn.panjea.partite.rich.tabelle.righestruttura;

import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartita;
import it.eurotn.panjea.partite.domain.StrategiaCalcoloPartitaFormula;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.BindingFactory;
import org.springframework.richclient.form.binding.BindingFactoryProvider;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.popup.JidePopup;

/**
 * Gestisce la strategia di calcolo formula.
 * 
 * @author giangi
 * 
 */
public class StrategiaCalcoloPartitaFormulaComponent extends AbstractControlFactory {
	private static Logger logger = Logger.getLogger(StrategiaCalcoloPartitaFormulaComponent.class);
	private FormModel formModel;// utilizzato per bindare i campi
	private JidePopup legendVariablesPopup;

	/**
	 * Costruttore.
	 * 
	 * @param strategie
	 *            valueHolder con strategie
	 * @param parentModel
	 *            formModel del parent
	 */
	public StrategiaCalcoloPartitaFormulaComponent(final ValueHolder strategie, final HierarchicalFormModel parentModel) {
		super();
		formModel = FormModelHelper.createFormModel(new StrategiaCalcoloPartitaFormula(), false);
		parentModel.addChild((HierarchicalFormModel) formModel);

		strategie.addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				logger.debug("--> Enter propertyChange. Cambio il formObject con la struttura partita 0.Lista strutture "
						+ evt);
				// Se è cambiata la lista di selezione delle strategie mi recupero la strategia che devo gestire
				StrategiaCalcoloPartita[] strategieNew = (StrategiaCalcoloPartita[]) evt.getNewValue();
				// La strutturaPartitaFormula è sempre al primo posto
				formModel.setFormObject(strategieNew[0]);
				logger.debug("--> Exit propertyChange");
			}
		});
	}

	@Override
	protected JComponent createControl() {
		BindingFactory bf = ((BindingFactoryProvider) getService(org.springframework.richclient.form.binding.BindingFactoryProvider.class))
				.getBindingFactory(formModel);
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.add("formula");

		final JLabel labelLegend = getComponentFactory().createLabel("");
		labelLegend.setIcon(RcpSupport.getIcon("propertyCommand.icon"));
		createLegendaPopupMenu(labelLegend);
		labelLegend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				legendVariablesPopup.setOwner(labelLegend);
				legendVariablesPopup.showPopup();
			}
		});

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(builder.getForm(), BorderLayout.CENTER);
		panel.add(labelLegend, BorderLayout.EAST);

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(panel, BorderLayout.NORTH);

		return rootPanel;
	}

	/**
	 * Crea la legenda per le variabili possibili per la maschera di evasione.
	 * 
	 * @return maschera in formato html
	 */
	private String createLegenda() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");
		stringBuffer.append("<B>");
		stringBuffer.append(getMessage("legenda").toUpperCase());
		stringBuffer.append("</B><BR><hr></hr>");
		stringBuffer.append("<ul>");

		stringBuffer.append("<li><B>IMP</B> = Totale Imponibile</li>");
		stringBuffer.append("<li><B>IVA</B> = Totale imposta</li>");
		stringBuffer.append("<li><B>TOT</B> = Totale documento</li>");
		stringBuffer.append("<li><B>LIQDEBITO</B> = Totale debito liquidazione</li>");
		stringBuffer.append("<li><B>LIQCREDITO</B> = Totale credito liquidazione</li>");
		stringBuffer.append("<li><B>LIQRISULTATO</B> = Risultato del periodo (credito-debito)</li>");
		stringBuffer.append("<li><B>LIQTOTALE</B> = Totale liquidazione (credito-debito+/-periodo precedente)</li>");

		stringBuffer.append("</ul>");
		stringBuffer.append("</html>");

		return stringBuffer.toString();
	}

	private void createLegendaPopupMenu(JComponent owner) {
		legendVariablesPopup = new JidePopup();
		legendVariablesPopup.setResizable(false);
		legendVariablesPopup.setMovable(false);
		legendVariablesPopup.setOwner(owner);
		legendVariablesPopup.getContentPane().setLayout(new VerticalLayout(10));
		legendVariablesPopup.setBorder(BorderFactory.createEmptyBorder());
		legendVariablesPopup.add(new JLabel(createLegenda()));
	}
}
