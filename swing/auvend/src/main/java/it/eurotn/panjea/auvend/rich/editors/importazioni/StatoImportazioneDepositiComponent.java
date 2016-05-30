/**
 * 
 */
package it.eurotn.panjea.auvend.rich.editors.importazioni;

import it.eurotn.panjea.anagrafica.domain.Deposito;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.springframework.richclient.factory.AbstractControlFactory;

/**
 * @author fattazzo
 * 
 */
public class StatoImportazioneDepositiComponent extends AbstractControlFactory {

	private class DepositiImportatiRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -6859331101379473549L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			DepositoImportatoPM deposito = (DepositoImportatoPM) value;

			Color textColor = Color.BLACK;
			Icon icon = null;

			StringBuffer testo = new StringBuffer();
			testo.append(deposito.getDeposito().getCodice());
			testo.append(" - ");
			testo.append(deposito.getDeposito().getDescrizione());

			if (deposito.getImportato() == null) {
				if (!cancelImport) {
					testo.append(" in importazione...");
					icon = getIconSource().getIcon("depositoInImportazione");
				} else {
					testo.append(" importazione annullata.");
					icon = getIconSource().getIcon(Deposito.class.getName());
				}

			} else {
				if (deposito.getImportato()) {
					testo.append(" importato.");
					textColor = new Color(51, 153, 0);
				} else {
					testo.append(" non importato.");
					textColor = Color.RED;
				}
				icon = getIconSource().getIcon(Deposito.class.getName());
			}

			label.setText(testo.toString());
			label.setForeground(textColor);
			label.setIcon(icon);
			setOpaque(false);

			return label;
		}
	}

	private class DepositoImportatoPM {

		private final Deposito deposito;
		private Boolean importato;

		/**
		 * Costruttore.
		 * 
		 * @param deposito
		 *            deposito
		 */
		public DepositoImportatoPM(final Deposito deposito) {
			super();
			this.deposito = deposito;
			this.importato = null;
		}

		/**
		 * @return the deposito
		 */
		public Deposito getDeposito() {
			return deposito;
		}

		/**
		 * @return the importato
		 */
		public Boolean getImportato() {
			return importato;
		}

		/**
		 * @param importato
		 *            the importato to set
		 */
		public void setImportato(Boolean importato) {
			this.importato = importato;
		}

	}

	private JList listDepositiImportati;

	private JProgressBar progressBar;
	private boolean cancelImport = false;
	private JPanel panelProgress;

	/**
	 * Cerca nella lista il deposito e aggiorna il suo stato importazione.
	 * 
	 * @param deposito
	 *            deposito
	 * @param statoImportazione
	 *            stato
	 */
	public void aggiornaStatoDeposito(Deposito deposito, boolean statoImportazione) {

		List<DepositoImportatoPM> list = new ArrayList<DepositoImportatoPM>();

		for (@SuppressWarnings("unchecked")
		Enumeration<DepositoImportatoPM> e = (Enumeration<DepositoImportatoPM>) ((DefaultListModel) this.listDepositiImportati
				.getModel()).elements(); e.hasMoreElements();) {

			DepositoImportatoPM depositoImportatoPM = e.nextElement();

			if (depositoImportatoPM.getDeposito().getCodice().equals(deposito.getCodice())) {
				depositoImportatoPM.setImportato(statoImportazione);

			}
			list.add(depositoImportatoPM);
		}

		((DefaultListModel) this.listDepositiImportati.getModel()).removeAllElements();

		for (DepositoImportatoPM depositoImportatoPM : list) {
			((DefaultListModel) this.listDepositiImportati.getModel()).addElement(depositoImportatoPM);
		}

		progressBar.setValue(progressBar.getValue() + 1);

		if (progressBar.getValue() == progressBar.getMaximum()) {
			panelProgress.setVisible(false);
		}
	}

	/**
	 * Aggiunge un deposito alla lista.
	 * 
	 * @param deposito
	 *            deposito da aggiungere
	 */
	public void aggiungiDeposito(Deposito deposito) {
		// se la lista è vuota significa che sta partendo l'importazione quindi visualizzo il pannello.
		if (((DefaultListModel) this.listDepositiImportati.getModel()).isEmpty()) {
			panelProgress.setVisible(true);
		}

		((DefaultListModel) this.listDepositiImportati.getModel())
				.insertElementAt(new DepositoImportatoPM(deposito), 0);
	}

	/**
	 * Cancella tutto il contenuto della lista, azzera la progress bar e cancella le eccezioni di contabilizzazione.
	 */
	public void clearStatoDepositi() {
		((DefaultListModel) this.listDepositiImportati.getModel()).removeAllElements();
		progressBar.setValue(0);
		cancelImport = false;
		panelProgress.setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.factory.AbstractControlFactory#createControl()
	 */
	@Override
	protected JComponent createControl() {
		// pannello importazione
		JPanel panelimp = getComponentFactory().createPanel(new BorderLayout());

		listDepositiImportati = new JList(new DefaultListModel());
		listDepositiImportati.setCellRenderer(new DepositiImportatiRenderer());
		listDepositiImportati.setOpaque(false);

		panelimp.add(getComponentFactory().createScrollPane(listDepositiImportati), BorderLayout.CENTER);

		// pannello della progressbar
		panelProgress = getComponentFactory().createPanel(new BorderLayout());
		progressBar = new JProgressBar();
		progressBar.setValue(0);
		panelProgress.add(progressBar, BorderLayout.CENTER);

		JButton cancelButton = getComponentFactory().createButton("");
		cancelButton.setAction(new AbstractAction() {

			private static final long serialVersionUID = 4195219213862714114L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cancelImport = true;
				panelProgress.setVisible(false);
			}
		});
		cancelButton.setIcon(getIconSource().getIcon("deleteCommand.icon"));
		panelProgress.add(cancelButton, BorderLayout.LINE_END);
		panelProgress.setVisible(false);

		panelimp.add(panelProgress, BorderLayout.SOUTH);

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.setBorder(BorderFactory.createTitledBorder("Stato importazione"));
		rootPanel.setPreferredSize(new Dimension(400, 300));
		rootPanel.add(panelimp, BorderLayout.NORTH);

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(panelProgress, BorderLayout.SOUTH);
		rootPanel.add(panel, BorderLayout.CENTER);

		return rootPanel;
	}

	/**
	 * @return The cancelImport
	 */
	public boolean isCancelImport() {
		return cancelImport;
	}

	/**
	 * @param numeroDepositi
	 *            numero di depositi da importare
	 */
	public void setNumeroDepositiDaImportare(int numeroDepositi) {
		progressBar.setMaximum(numeroDepositi);
	}
}
