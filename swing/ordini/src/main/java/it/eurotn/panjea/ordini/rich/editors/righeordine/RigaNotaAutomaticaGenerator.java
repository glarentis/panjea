package it.eurotn.panjea.ordini.rich.editors.righeordine;

import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.anagrafica.rich.bd.INoteAutomaticheBD;
import it.eurotn.panjea.anagrafica.rich.bd.NoteAutomaticheBD;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.AggiungiNotaAnagraficaCommand;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.RigaNotaDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.rich.components.htmleditor.HTMLEditorPane;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class RigaNotaAutomaticaGenerator {

	private class NoteAutomaticheDialog extends ConfirmationDialog {

		private HTMLEditorPane htmlPane;

		private List<NotaAutomatica> noteAutomatiche;

		private AggiungiNotaAnagraficaCommand aggiungiNotaAnagraficaCommand;

		/**
		 * Costruttore.
		 * 
		 * @param noteAutomatiche
		 *            note
		 */
		public NoteAutomaticheDialog(final List<NotaAutomatica> noteAutomatiche) {
			super();
			this.noteAutomatiche = noteAutomatiche;
			setTitle("Conferma inserimento delle note automatiche");
		}

		@Override
		protected JComponent createDialogContentPane() {
			FormLayout layout = new FormLayout("f:260dlu", "4dlu,f:300dlu");
			JPanel panel = new JPanel(layout);
			CellConstraints cc = new CellConstraints();

			htmlPane = new HTMLEditorPane();
			htmlPane.getWysEditor().setName("NoteAutomaticheDialog.note");
			htmlPane.setText(createNote());
			htmlPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
					"Note automatiche"));

			panel.add(htmlPane, cc.xy(1, 2));

			return panel;
		}

		/**
		 * Crea le note da inserire in base alle note automatiche configurate.
		 * 
		 * @return note
		 */
		private String createNote() {

			List<NotaAnagrafica> noteAnagrafiche = anagraficaTabelleBD.caricaNoteAnagrafica();

			StringBuilder sb = new StringBuilder();
			for (NotaAutomatica nota : noteAutomatiche) {

				if (!sb.toString().isEmpty()) {
					sb.append("<BR>");
				}
				sb.append(nota.getNotaElaborata(noteAnagrafiche));
			}
			// rimuovo i tag HTML dei vari blocchi
			return sb.toString().replace("<HTML>", "").replace("</HTML>", "");
		}

		/**
		 * @return the aggiungiNotaAnagraficaCommand
		 */
		public AggiungiNotaAnagraficaCommand getAggiungiNotaAnagraficaCommand() {
			if (aggiungiNotaAnagraficaCommand == null) {
				aggiungiNotaAnagraficaCommand = new AggiungiNotaAnagraficaCommand(new Closure() {

					@Override
					public Object call(Object obj) {
						htmlPane.setText(htmlPane.getText() + "<BR>" + ((NotaAnagrafica) obj).getDescrizione());
						return null;
					}
				});

			}

			return aggiungiNotaAnagraficaCommand;
		}

		@Override
		protected String getCancelCommandId() {
			return "cancelCommand";
		}

		@Override
		protected Object[] getCommandGroupMembers() {
			return (new AbstractCommand[] { getAggiungiNotaAnagraficaCommand(), getFinishCommand(), getCancelCommand() });
		}

		@Override
		protected String getFinishCommandId() {
			return "okCommand";
		}

		@Override
		protected void onConfirm() {
			StringBuilder sb = new StringBuilder();
			sb.append("<HTML>");
			sb.append(htmlPane.getText());
			sb.append("</HTML>");
			success = ordiniDocumentoBD.creaRigaNoteAutomatica(areaOrdineRif, sb.toString());
		}

	}

	private boolean success = false;

	private IOrdiniDocumentoBD ordiniDocumentoBD;
	private INoteAutomaticheBD noteAutomaticheBD;
	private IAnagraficaTabelleBD anagraficaTabelleBD;

	private AreaOrdine areaOrdineRif;

	/**
	 * Costruttore.
	 * 
	 */
	public RigaNotaAutomaticaGenerator() {
		super();
		noteAutomaticheBD = RcpSupport.getBean(NoteAutomaticheBD.BEAN_ID);
		ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
		anagraficaTabelleBD = RcpSupport.getBean("anagraficaTabelleBD");
	}

	/**
	 * Se tutte le condizioni sono rispettate, genera la riga nota automatica per l'area ordine.
	 * 
	 * @param areaOrdine
	 *            area ordine di riferimento
	 * @param righe
	 *            righe dell'area
	 * @return <code>true</code> se la riga note è stata generata
	 */
	public boolean genera(AreaOrdine areaOrdine, List<RigaOrdineDTO> righe) {

		this.areaOrdineRif = areaOrdine;

		boolean rigaAutomaticaPresente = false;

		for (RigaOrdineDTO rigaOrdineDTO : righe) {
			if (rigaOrdineDTO instanceof RigaNotaDTO && ((RigaNotaDTO) rigaOrdineDTO).isRigaAutomatica()) {
				rigaAutomaticaPresente = Boolean.TRUE;
				break;
			}
		}

		// verifico prima se esiste una riga automatica. In questo modo se già esiste non devo fare la chiamata al
		// server per caricare le eventuali note automatiche configurate per il tipo documento.
		if (!rigaAutomaticaPresente) {
			List<NotaAutomatica> note = noteAutomaticheBD.caricaNoteAutomatiche(areaOrdine.getDataRegistrazione(),
					areaOrdine.getDocumento());

			if (note != null && !note.isEmpty()) {
				NoteAutomaticheDialog dialog = new NoteAutomaticheDialog(note);
				dialog.showDialog();
			}
		}

		return success;
	}

}
