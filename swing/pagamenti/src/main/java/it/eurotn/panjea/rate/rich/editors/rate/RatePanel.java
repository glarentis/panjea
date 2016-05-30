/**
 * 
 */
package it.eurotn.panjea.rate.rich.editors.rate;

import it.eurotn.panjea.rate.rich.forms.AbstractAreaRateModel;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.image.IconSource;

/**
 * @author Leonardo
 * 
 */
public class RatePanel extends JPanel {

	private class RataAggiornataChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt != null) {
				update();
			}
		}

	}

	private static final long serialVersionUID = 1979102430788899566L;

	private AbstractAreaRateModel areaRateModel = null;
	private JLabel labelSquadratura = null;
	private MessageSource messageSource = null;

	private RataAggiornataChangeListener rataAggiornataChangeListener = null;

	/**
	 * Costruttore.
	 */
	public RatePanel() {
		super();
		initialize();
	}

	/**
	 * @return RigheAggiornateChangeListener
	 */
	private PropertyChangeListener getRataAggiornataChangeListener() {
		if (rataAggiornataChangeListener == null) {
			rataAggiornataChangeListener = new RataAggiornataChangeListener();
		}
		return rataAggiornataChangeListener;
	}

	/**
	 * init della classe.
	 */
	private void initialize() {
		setLayout(new BorderLayout());
		ComponentFactory componentFactory = (ComponentFactory) Application.services()
				.getService(ComponentFactory.class);

		labelSquadratura = componentFactory.createLabel("");
		labelSquadratura.setName("TotaleRatePanel.squadratura.label");
		messageSource = (MessageSource) Application.services().getService(MessageSource.class);

		add(labelSquadratura, BorderLayout.LINE_START);
	}

	/**
	 * setter areaRateModel.
	 * 
	 * @param areaRateModel
	 *            modello area rate
	 */
	public void setAreaRateModel(AbstractAreaRateModel areaRateModel) {
		this.areaRateModel = areaRateModel;
		this.areaRateModel.removePropertyChangeListener(AbstractAreaRateModel.RIGA_AGGIORNATA,
				getRataAggiornataChangeListener());
		this.areaRateModel.addPropertyChangeListener(AbstractAreaRateModel.RIGA_AGGIORNATA,
				getRataAggiornataChangeListener());
	}

	/**
	 * Aggiorna il pannello.
	 */
	public void update() {
		labelSquadratura.setText("");
		labelSquadratura.setIcon(null);
		if (areaRateModel.isAreaRatePresente() && !areaRateModel.isAreaRateQuadrata()) {
			String textMessage = messageSource.getMessage("rateTablePage.nonQuadrate", null, Locale.getDefault());
			IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);
			Icon icon = iconSource.getIcon("severity.warning");
			labelSquadratura.setText(textMessage);
			labelSquadratura.setIcon(icon);
		}
	}

}
