package it.eurotn.panjea.preventivi.rich.forms.righepreventivo;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public abstract class AbstractNoteRigaForm extends PanjeaAbstractForm {

	private class ArticoloPropertyChangeListener implements PropertyChangeListener {

		/**
		 * Restituisce la stringa passata come parametro con la prima lettera maiuscola e le rimanenti minuscole.
		 * 
		 * @param word
		 *            stringa da formattare
		 * @return stringa formattata
		 */
		private String capitalizeWord(String word) {

			String firstLetter = word.substring(0, 1); // Get first letter
			String remainder = word.substring(1); // Get remainder of word.
			String capitalized = firstLetter.toUpperCase() + remainder.toLowerCase();

			return capitalized;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			noteRigaLabel.setIcon(null);
			noteLinguaRigaLabel.setIcon(null);

			CardLayout cardLayout = (CardLayout) (descrizioniPanel.getLayout());

			String linguaSede = null;
			if (getDocumento().getSedeEntita() != null) {
				linguaSede = getDocumento().getSedeEntita().getLingua();
			}

			if (linguaSede == null || aziendaCorrente.getLingua().equals(linguaSede)) {
				cardLayout.show(descrizioniPanel, DESCRIZIONE_CARD);
			} else {
				// icona
				noteRigaLabel.setIcon(RcpSupport.getIcon(aziendaCorrente.getLingua()));
				noteLinguaRigaLabel.setIcon(RcpSupport.getIcon(linguaSede));

				// text
				Locale locale = new Locale(aziendaCorrente.getLingua());
				noteRigaLabel.setText(capitalizeWord(locale.getDisplayLanguage()));
				locale = new Locale(linguaSede);
				noteLinguaRigaLabel.setText(capitalizeWord(locale.getDisplayLanguage()));
				cardLayout.show(descrizioniPanel, DESCRIZIONI_LINGUA_CARD);
			}
		}

	}

	private JLabel noteRigaLabel;
	private JLabel noteLinguaRigaLabel;

	private final AziendaCorrente aziendaCorrente;

	private JPanel descrizioniPanel;

	public static final String DESCRIZIONE_CARD = "descrizioneCard";
	public static final String DESCRIZIONI_LINGUA_CARD = "descrizioniLinguaCard";

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param formID
	 *            formID
	 */
	public AbstractNoteRigaForm(final FormModel formModel, final String formID) {
		super(formModel, formID);

		this.aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
	}

	/**
	 * 
	 * @return crea i controlli per la gestione delle note in lingua aziendale ed entità
	 */
	private JComponent createDescrizioneLinguaPanel() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("default:grow,4dlu, default:grow", "default,fill:default:grow");
		layout.setColumnGroups(new int[][] { { 3, 1 } });

		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r,c");

		builder.setComponentAttributes("f, f");

		noteRigaLabel = new JLabel();
		builder.addComponent(noteRigaLabel, 1, 1);
		Binding noteBinding = bf.createBoundHTMLEditor("noteRiga");
		builder.addBinding(noteBinding, 1, 2);

		noteLinguaRigaLabel = new JLabel();
		builder.addComponent(noteLinguaRigaLabel, 3, 1);
		Binding noteLinguaBinding = bf.createBoundHTMLEditor("noteLinguaRiga");
		builder.addBinding(noteLinguaBinding, 3, 2);

		return builder.getPanel();
	}

	/**
	 * @return crea i controlli per la gestione della descrizione delle note.
	 */
	private JComponent createDescrizionePanel() {

		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("fill:pref:grow", "default,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r,c");
		builder.setComponentAttributes("f, f");

		noteRigaLabel = new JLabel();
		builder.addComponent(noteRigaLabel, 1, 1);
		Binding noteBinding = bf.createBoundHTMLEditor("noteRiga");
		builder.addBinding(noteBinding, 1, 2);

		return builder.getPanel();
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref, 4dlu, fill:pref:grow", "4dlu,default,4dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("noteSuDestinazione");
		builder.nextRow();

		descrizioniPanel = new JPanel(new CardLayout());
		descrizioniPanel.add(createDescrizionePanel(), DESCRIZIONE_CARD);
		descrizioniPanel.add(createDescrizioneLinguaPanel(), DESCRIZIONI_LINGUA_CARD);

		JPanel rootPanel = new JPanel(new BorderLayout());
		rootPanel.add(builder.getPanel(), BorderLayout.NORTH);
		rootPanel.add(descrizioniPanel, BorderLayout.CENTER);

		getFormModel().getValueModel("articolo").addValueChangeListener(new ArticoloPropertyChangeListener());

		return rootPanel;
	}

	/**
	 * 
	 * @return il Documento a cui è collegata la riga.
	 */
	protected abstract Documento getDocumento();

}
