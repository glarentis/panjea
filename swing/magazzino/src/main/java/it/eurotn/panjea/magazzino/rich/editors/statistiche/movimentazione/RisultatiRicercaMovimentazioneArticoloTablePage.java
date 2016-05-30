/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.statistiche.movimentazione;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.SortableTable;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * TablePage per la visualizzazione dei risultati della ricerca della movimentazione magazzino.
 * 
 * @author fattazzo
 */
public class RisultatiRicercaMovimentazioneArticoloTablePage extends AbstractTablePageEditor<RigaMovimentazione>
		implements InitializingBean {

	private class OpenAreaMagazzinoEditorCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "openAreaMagazzinoCommand";

		private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;

		/**
		 * Costruisce il comando per aprire l'area di magazzino selezionata.
		 * 
		 * @param magazzinoDocumentoBD
		 *            BD per caricare i dati dal service
		 */
		public OpenAreaMagazzinoEditorCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD) {
			super(COMMAND_ID);
			RcpSupport.configure(this);
			this.magazzinoDocumentoBD = magazzinoDocumentoBD;
		}

		@Override
		protected void doExecuteCommand() {
			RigaMovimentazione rigaMovimentazione = getTable().getSelectedObject();

			if (rigaMovimentazione != null) {
				AreaMagazzino areaMagazzino = new AreaMagazzino();
				areaMagazzino.setId(rigaMovimentazione.getAreaMagazzinoId());
				AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD
						.caricaAreaMagazzinoFullDTO(areaMagazzino);
				LifecycleApplicationEvent event = new OpenEditorEvent(areaMagazzinoFullDTO);
				Application.instance().getApplicationContext().publishEvent(event);
			}
		}
	}

	public static final String PAGE_ID = "risultatiRicercaMovimentazioneArticoloTablePage";

	private ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private JTextField textFieldGiacenzaAttuale;
	private JTextField textFieldGiacenzaPrecedente;
	private JTextField textFieldGiacenzaFinale;
	private DecimalFormat decimalFormat = null;

	private static MovimentazioneArticoloBeanTableModel movimentazioneArticoloBeanTableModel;

	private OpenAreaMagazzinoEditorCommand openAreaMagazzinoEditorCommand;

	public static final String STR_ZERI = "0000000000000000000000000";

	/**
	 * Costruttore di default.
	 */
	public RisultatiRicercaMovimentazioneArticoloTablePage() {
		super(PAGE_ID, movimentazioneArticoloBeanTableModel = new MovimentazioneArticoloBeanTableModel(PAGE_ID));
		getTable().setAggregatedColumns(new String[] { "dataRegistrazione" });
		((SortableTable) getTable().getTable()).setSortable(false);
		decimalFormat = new DecimalFormat("###,###.00");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		getTable().setPropertyCommandExecutor(getOpenAreaMagazzinoEditor());
	}

	/**
	 * 
	 * @param value
	 *            valore da formattare
	 * @return valore formattato
	 */
	private String doubleToString(double value) {
		return decimalFormat.format(value);
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getOpenAreaMagazzinoEditor() };
	}

	@Override
	public JComponent getHeaderControl() {
		JPanel panel = getComponentFactory().createPanel(new BorderLayout());

		JPanel panelTitle = getComponentFactory().createPanel(new BorderLayout());
		JComponent pannelloTotali = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JLabel labelGiacenzaAttuale = getComponentFactory().createLabel(getMessage("giacenzaAttuale"));
		JLabel labelGiacenzaFinale = getComponentFactory().createLabel(getMessage("giacenzaFinale"));
		JLabel labelGiacenzaPeriodo = getComponentFactory().createLabel(getMessage("giacenzaPeriodo"));

		textFieldGiacenzaAttuale = getComponentFactory().createTextField();
		textFieldGiacenzaAttuale.setColumns(10);
		textFieldGiacenzaAttuale.setFocusable(false);
		textFieldGiacenzaAttuale.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldGiacenzaAttuale.setName("giacenzaAttuale");

		textFieldGiacenzaPrecedente = getComponentFactory().createTextField();
		textFieldGiacenzaPrecedente.setColumns(10);
		textFieldGiacenzaPrecedente.setFocusable(false);
		textFieldGiacenzaPrecedente.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldGiacenzaPrecedente.setName("giacenzaPrecedente");
		textFieldGiacenzaFinale = getComponentFactory().createTextField();
		textFieldGiacenzaFinale.setColumns(10);
		textFieldGiacenzaFinale.setFocusable(false);
		textFieldGiacenzaFinale.setHorizontalAlignment(SwingConstants.RIGHT);
		textFieldGiacenzaFinale.setName("giacenzaFinale");

		pannelloTotali.add(labelGiacenzaAttuale);
		pannelloTotali.add(textFieldGiacenzaAttuale);

		pannelloTotali.add(labelGiacenzaPeriodo);
		pannelloTotali.add(textFieldGiacenzaPrecedente);

		pannelloTotali.add(labelGiacenzaFinale);
		pannelloTotali.add(textFieldGiacenzaFinale);
		panelTitle.add(pannelloTotali, BorderLayout.EAST);

		panel.add(panelTitle, BorderLayout.SOUTH);

		return panel;
	}

	/**
	 * 
	 * @return Command per aprire l'area di magazzino selezionata
	 */
	private OpenAreaMagazzinoEditorCommand getOpenAreaMagazzinoEditor() {
		if (openAreaMagazzinoEditorCommand == null) {
			openAreaMagazzinoEditorCommand = new OpenAreaMagazzinoEditorCommand(this.magazzinoDocumentoBD);
		}
		return openAreaMagazzinoEditorCommand;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SwingWorker getRefreshDataSwingWorker() {
		return new SwingWorker<MovimentazioneArticolo, MovimentazioneArticolo>() {

			@Override
			protected MovimentazioneArticolo doInBackground() throws Exception {
				SwingUtilities.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						showSearcInProgressMessage();
					}
				});
				MovimentazioneArticolo movimentazioneArticolo = null;
				if (parametriRicercaMovimentazioneArticolo.isEffettuaRicerca()) {
					movimentazioneArticolo = magazzinoDocumentoBD
							.caricaMovimentazioneArticolo(parametriRicercaMovimentazioneArticolo);
				}
				publish(movimentazioneArticolo);
				return movimentazioneArticolo;
			}

			@Override
			protected final void done() {
				super.done();
				if (!isCancelled()) {
					if (isControlCreated()) {
						hideSearcInProgressMessage();
					}
				}
			};

			@Override
			protected void process(List<MovimentazioneArticolo> chunks) {
				movimentazioneArticoloBeanTableModel.setNumeroDecimaliPrezzo(0);
				movimentazioneArticoloBeanTableModel.setNumeroDecimaliQta(0);
				setRows(new ArrayList<RigaMovimentazione>());
				MovimentazioneArticolo movimentazioneArticolo = chunks != null ? chunks.get(0) : null;
				if (movimentazioneArticolo != null) {

					movimentazioneArticoloBeanTableModel.setNumeroDecimaliPrezzo(movimentazioneArticolo
							.getMaxNumeroDecimaliPrezzo());
					int numeroDecimaliQta = 0;
					if (!movimentazioneArticolo.getRigheMovimentazione().isEmpty()
							&& movimentazioneArticolo.getRigheMovimentazione().get(0).getNumeroDecimaliQuantita() != null) {
						numeroDecimaliQta = movimentazioneArticolo.getRigheMovimentazione().get(0)
								.getNumeroDecimaliQuantita();
					} else {
						numeroDecimaliQta = 8;
					}
					movimentazioneArticoloBeanTableModel.setNumeroDecimaliQta(numeroDecimaliQta);
					updateQtaFormat(numeroDecimaliQta);

					setRows(movimentazioneArticolo.getRigheMovimentazione());

					textFieldGiacenzaAttuale.setText(doubleToString(movimentazioneArticolo.getGiacenzaAttuale()));
					textFieldGiacenzaPrecedente.setText(doubleToString(movimentazioneArticolo.getGiacenzaPrecedente()));
					textFieldGiacenzaFinale.setText(doubleToString(movimentazioneArticolo.getGiacenzaFinale()));
				}
			}

		};
	}

	@Override
	public List<RigaMovimentazione> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	};

	@Override
	public List<RigaMovimentazione> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
		if (object instanceof ParametriRicercaMovimentazioneArticolo) {
			this.parametriRicercaMovimentazioneArticolo = (ParametriRicercaMovimentazioneArticolo) object;
		} else {
			this.parametriRicercaMovimentazioneArticolo = new ParametriRicercaMovimentazioneArticolo();
		}
	}

	/**
	 * setter per magazzinoDocumentoBD.
	 * 
	 * @param magazzinoDocumentoBD
	 *            magazzinoDocumentoBD
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

	/**
	 * Aggiorna il formato di visualizzazione della quantità in base ai decimali richiesti.
	 * 
	 * @param decimal
	 *            numero di decimali da visualizzare
	 */
	private void updateQtaFormat(int decimal) {
		decimalFormat = new DecimalFormat("###,###,###,##0");

		if (decimal > STR_ZERI.length()) {
			decimalFormat = new DecimalFormat("###,###,###,##0." + STR_ZERI.substring(0, STR_ZERI.length() - 1));
		} else {
			String parteDecimale = decimal != 0 ? "." + STR_ZERI.substring(0, decimal) : "";
			decimalFormat = new DecimalFormat("###,###,###,##0" + parteDecimale);
		}
	}
}
