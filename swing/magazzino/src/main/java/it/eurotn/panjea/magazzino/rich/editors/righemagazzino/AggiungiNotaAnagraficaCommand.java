package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaTabelleBD;
import it.eurotn.panjea.rich.dialogs.PanjeaFilterListSelectionDialog;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.selection.dialog.FilterListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;

public class AggiungiNotaAnagraficaCommand extends ApplicationWindowAwareCommand {

	protected class NoteRender extends DefaultListCellRenderer {
		private static final long serialVersionUID = 8129263663324775797L;

		/**
		 * Costruttore.
		 * 
		 */
		public NoteRender() {
			super();
		}

		@Override
		public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1) {
			JLabel label = (JLabel) super.getListCellRendererComponent(jlist, obj, i, flag, flag1);

			StringBuilder sb = new StringBuilder();
			sb.append("<HTML><B>");
			sb.append(((NotaAnagrafica) obj).getCodice());
			sb.append("</B>");
			sb.append(" - ");

			String desc = ((NotaAnagrafica) obj).getDescrizione();
			desc = desc.replaceAll("\\<.*?\\>", "");
			desc = desc.replaceAll("\\n", "").trim();
			if (desc.length() > 50) {
				desc = desc.trim().substring(0, 50) + "...";
			}
			sb.append(desc);
			sb.append("</HTML>");

			label.setText(sb.toString());
			label.setIcon(RcpSupport.getIcon(NotaAnagrafica.class.getName()));
			return label;
		}
	}

	public static final String COMMAND_ID = "aggiungiNotaAnagraficaCommand";

	private Closure selectClosure;

	private IAnagraficaTabelleBD anagraficaTabelleBD;

	/**
	 * Costruttore.
	 * 
	 * @param selectClosure
	 *            closure chiamata alla selezione della nota anagrafica
	 */
	public AggiungiNotaAnagraficaCommand(final Closure selectClosure) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.selectClosure = selectClosure;
		this.anagraficaTabelleBD = RcpSupport.getBean("anagraficaTabelleBD");
	}

	@Override
	protected void doExecuteCommand() {
		FilterList<NotaAnagrafica> noteFilterList = new FilterList<NotaAnagrafica>(
				GlazedLists.eventList(anagraficaTabelleBD.caricaNoteAnagrafica()));
		// IOOBE Mail, aggiunto controllo su onFinish di PanjeaFilterListSelectionDialog, da verificare cmq se ci sono
		// altri errori; eseguito il controllo sulla presenza di elementi nella lista filtrata, l'unico caso in cui sono
		// riuscito a riprodurre l'eccezione è stato nel caso in cui la stessa risultava vuota.
		FilterListSelectionDialog selectionDialog = new PanjeaFilterListSelectionDialog(
				"Seleziona la nota da inserire", Application.instance().getActiveWindow().getControl(), noteFilterList) {

			@Override
			protected void onSelect(Object selection) {
				selectClosure.call(selection);
			}
		};
		selectionDialog.setRenderer(new NoteRender());
		selectionDialog.setFilterator(GlazedLists.textFilterator("codice"));
		selectionDialog.showDialog();
	}

}
