package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.datiaccompagnatori.RichiestaDatiAccompagnatoriForm;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

public class AreeMagazzinoDatiAccompagnatoriDialog extends ConfirmationDialog implements ListSelectionListener {

	private class AreaMagazzinoRicercaCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 2021918457989173366L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value != null) {
				AreaMagazzinoRicerca areaMagazzinoRicerca = (AreaMagazzinoRicerca) value;

				label.setIcon(RcpSupport.getIcon(AreaMagazzino.class.getName()));
				label.setText("<html>"
						+ ObjectConverterManager.toString(areaMagazzinoRicerca.getDocumento(), Documento.class, null)
						+ "<br>" + ObjectConverterManager.toString(areaMagazzinoRicerca.getEntitaDocumento())
						+ "</html>");
			}

			return label;
		}
	}

	private List<AreaMagazzinoRicerca> areeMagazzino;

	private JList<AreaMagazzinoRicerca> areeList;
	private HTMLEditorPane noteMagazzino;

	private JPanel panelDati;
	private JPanel rootPanel;

	private RichiestaDatiAccompagnatoriForm richiestaDatiAccompagnatoriForm;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	/**
	 * @param aree
	 *            aree
	 */
	public AreeMagazzinoDatiAccompagnatoriDialog(final List<AreaMagazzinoRicerca> aree) {
		super("Richiesta dati accompagnatori", "_");
		this.areeMagazzino = aree;

		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);

		setPreferredSize(new Dimension(1024, 400));
	}

	@Override
	protected JComponent createDialogContentPane() {
		rootPanel = getComponentFactory().createPanel(new BorderLayout());

		areeList = new JList<AreaMagazzinoRicerca>(
				areeMagazzino.toArray(new AreaMagazzinoRicerca[areeMagazzino.size()]));
		areeList.setCellRenderer(new AreaMagazzinoRicercaCellRenderer());
		areeList.setFixedCellHeight(40);
		areeList.addListSelectionListener(this);
		areeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		areeList.setName("areeList");
		rootPanel.add(new JScrollPane(areeList), BorderLayout.WEST);

		noteMagazzino = new HTMLEditorPane();
		noteMagazzino.getWysEditor().setEditable(false);
		noteMagazzino.getMenuBar().setVisible(false);
		noteMagazzino.getFormatToolBar().setVisible(false);
		noteMagazzino.getWysEditor().setBackground(UIManager.getColor("Panel.background"));
		noteMagazzino.setPreferredSize(new Dimension(500, 100));
		noteMagazzino.setMaximumSize(new Dimension(500, 100));
		JPanel paneNote = getComponentFactory().createPanel(new BorderLayout());
		paneNote.setBorder(BorderFactory.createTitledBorder("Note"));
		paneNote.add(noteMagazzino, BorderLayout.CENTER);

		panelDati = getComponentFactory().createPanel(new BorderLayout());
		panelDati.setBorder(BorderFactory.createTitledBorder("Dati accompagnatori"));

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(paneNote, BorderLayout.NORTH);
		panel.add(panelDati, BorderLayout.CENTER);

		rootPanel.add(panel, BorderLayout.CENTER);

		if (areeList.getModel().getSize() > 0) {
			areeList.setSelectedIndex(0);
		}

		return rootPanel;
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new Object[] { getFinishCommand() };
	}

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	}

	@Override
	protected String getTitle() {
		return getMessage("datiAccompagnatoriDialog.title.label");
	}

	@Override
	protected void onConfirm() {

		// se è stato cambiato qualcosa nel form salvo l'area sul cambio selezione
		if (richiestaDatiAccompagnatoriForm != null && richiestaDatiAccompagnatoriForm.getFormModel().isCommittable()
				&& richiestaDatiAccompagnatoriForm.isDirty()) {
			richiestaDatiAccompagnatoriForm.commit();
			AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) richiestaDatiAccompagnatoriForm
					.getFormObject();
			try {
				magazzinoDocumentoBD.salvaAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino(), null, true);
			} catch (Exception e1) {
				PanjeaSwingUtil.checkAndThrowException(e1);
			}
		}
	}

	@Override
	protected void onInitialized() {
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting()) {
			return;
		}

		// se è stato cambiato qualcosa nel form salvo l'area sul cambio selezione
		if (richiestaDatiAccompagnatoriForm != null && richiestaDatiAccompagnatoriForm.getFormModel().isCommittable()
				&& richiestaDatiAccompagnatoriForm.isDirty()) {
			richiestaDatiAccompagnatoriForm.commit();
			AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) richiestaDatiAccompagnatoriForm
					.getFormObject();
			try {
				magazzinoDocumentoBD.salvaAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino(), null, true);
			} catch (Exception e1) {
				PanjeaSwingUtil.checkAndThrowException(e1);
			}
		}

		@SuppressWarnings("unchecked")
		JList<AreaMagazzinoRicerca> list = (JList<AreaMagazzinoRicerca>) e.getSource();
		if (list.getSelectedValue() != null) {
			noteMagazzino.setText("");

			NoteAreaMagazzino note = magazzinoDocumentoBD.caricaNoteSede(list.getSelectedValue().getSedeEntita());

			StringBuilder sb = new StringBuilder();
			if (!StringUtils.isBlank(note.getNoteEntita())) {
				sb.append(note.getNoteEntita());
				sb.append("<BR>");
			}
			if (!StringUtils.isBlank(note.getNoteSede())) {
				sb.append(note.getNoteSede());
			}
			noteMagazzino.setText(sb.toString());

			AreaMagazzinoFullDTO areaMagazzinoFullDTO = new AreaMagazzinoFullDTO();
			AreaMagazzino areamagazzino = new AreaMagazzino();
			areamagazzino.setId(list.getSelectedValue().getIdAreaMagazzino());
			areaMagazzinoFullDTO.setAreaMagazzino(magazzinoDocumentoBD.caricaAreaMagazzino(areamagazzino));

			panelDati.removeAll();
			richiestaDatiAccompagnatoriForm = new RichiestaDatiAccompagnatoriForm(areaMagazzinoFullDTO);
			panelDati.add(richiestaDatiAccompagnatoriForm.getControl(), BorderLayout.CENTER);

			panelDati.validate();
			panelDati.repaint();
			getDialog().pack();
		}
	}
}
