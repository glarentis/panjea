package it.eurotn.panjea.auvend.rich.editors.importazioni;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.auvend.domain.Cliente;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class ClientiDaVerificareComponent extends AbstractControlFactory {

	private JList listClientiDaVerificare;

	private final IAnagraficaBD anagraficaBD;

	/**
	 * Costruttore.
	 * 
	 * @param anagraficaBD
	 *            anagraficaBD
	 */
	public ClientiDaVerificareComponent(final IAnagraficaBD anagraficaBD) {
		super();
		this.anagraficaBD = anagraficaBD;
	}

	/**
	 * @param clienti
	 *            clienti da verificare
	 */
	public void aggiungiClientiDaVerificare(Collection<Cliente> clienti) {
		((DefaultListModel) this.listClientiDaVerificare.getModel()).removeAllElements();
		for (Cliente cliente : clienti) {
			((DefaultListModel) this.listClientiDaVerificare.getModel()).addElement(cliente);
		}
	}

	@Override
	protected JComponent createControl() {
		JPanel cliPanel = new JPanel(new BorderLayout());
		cliPanel.setBorder(BorderFactory.createTitledBorder("Clienti da verificare"));
		cliPanel.setPreferredSize(new Dimension(300, 300));

		this.listClientiDaVerificare = new JList(new DefaultListModel());
		this.listClientiDaVerificare.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = -4065052882517619876L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel label = (JLabel) super
						.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				Cliente cliente = (Cliente) value;

				label.setText(cliente.getCodiceAuvend() + " - " + cliente.getDenominazioneAuvend());
				if (cliente.getIdPanjea() != null) {
					label.setIcon(getIconSource().getIcon(it.eurotn.panjea.anagrafica.domain.Cliente.class.getName()));
					label.setToolTipText("Codice pagamento sul cliente non presente.");
				} else {
					label.setIcon(getIconSource().getIcon("no.icon"));
					label.setToolTipText("Cliente non presente in panjea.");
				}

				return label;
			}
		});
		listClientiDaVerificare.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Cliente cliente = (Cliente) listClientiDaVerificare.getSelectedValue();

					if (cliente.getIdPanjea() != null) {

						it.eurotn.panjea.anagrafica.domain.Cliente cliente2 = new it.eurotn.panjea.anagrafica.domain.Cliente();
						cliente2.setId(cliente.getIdPanjea());
						LifecycleApplicationEvent event = new OpenEditorEvent(anagraficaBD.caricaEntita(cliente2));
						Application.instance().getApplicationContext().publishEvent(event);
					}
				}
			}
		});

		cliPanel.add(getComponentFactory().createScrollPane(this.listClientiDaVerificare), BorderLayout.CENTER);

		return cliPanel;
	}

}
