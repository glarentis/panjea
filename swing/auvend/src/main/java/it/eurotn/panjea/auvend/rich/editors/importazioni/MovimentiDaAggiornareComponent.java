package it.eurotn.panjea.auvend.rich.editors.importazioni;

import it.eurotn.panjea.auvend.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.factory.AbstractControlFactory;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class MovimentiDaAggiornareComponent extends AbstractControlFactory {

	private JList listDocumentiDaAggiornare;

	private final IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoDocumentoBD
	 *            magazzinoDocumentoBD
	 */
	public MovimentiDaAggiornareComponent(final IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		super();
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * @param documenti
	 *            documenti da aggiungere
	 */
	public void aggiungiDocumentiDaAggiornare(Collection<Documento> documenti) {
		((DefaultListModel) this.listDocumentiDaAggiornare.getModel()).removeAllElements();
		for (Documento documento : documenti) {
			((DefaultListModel) this.listDocumentiDaAggiornare.getModel()).addElement(documento);
		}
	}

	@Override
	protected JComponent createControl() {
		JPanel docPanel = new JPanel(new BorderLayout());
		docPanel.setBorder(BorderFactory.createTitledBorder("Documenti da aggiornare"));
		docPanel.setPreferredSize(new Dimension(300, 300));

		listDocumentiDaAggiornare = new JList(new DefaultListModel());
		listDocumentiDaAggiornare.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 7401952550712362189L;

			private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super
						.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				Documento documento = (Documento) value;

				label.setText("Numero: " + documento.getNumero() + " del " + format.format(documento.getData()));
				label.setIcon(getIconSource().getIcon(AreaMagazzino.class.getName()));

				return label;
			}
		});

		listDocumentiDaAggiornare.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Documento documento = (Documento) listDocumentiDaAggiornare.getSelectedValue();

					it.eurotn.panjea.anagrafica.documenti.domain.Documento documento2 = new it.eurotn.panjea.anagrafica.documenti.domain.Documento();
					documento2.setId(documento.getId());
					AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD
							.caricaAreaMagazzinoFullDTOByDocumento(documento2);
					LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
					Application.instance().getApplicationContext().publishEvent(event);
				}
			}
		});
		docPanel.add(getComponentFactory().createScrollPane(this.listDocumentiDaAggiornare), BorderLayout.CENTER);

		return docPanel;
	}

}
