package it.eurotn.panjea.magazzino.rich.editors.rifatturazione;

import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class SediMagazzinoPerRifatturazioneAssociateTablePage extends AbstractTablePageEditor<SedeMagazzinoLite> {

	private class RimuoviAssociazioneSedeCommand extends ApplicationWindowAwareCommand {

		private static final String COMMAND_ID = "rimuoviAssociazioneSedeCommand";

		/**
		 * Costruttore.
		 */
		public RimuoviAssociazioneSedeCommand() {
			super(COMMAND_ID);
			this.setSecurityControllerId(GestioneRifatturazionePage.PAGE_ID + ".controller");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {

			SedeMagazzinoLite sedeMagazzino = getTable().getSelectedObject();

			if (sedeMagazzino != null) {
				magazzinoAnagraficaBD.rimuoviSedePerRifatturazione(sedeMagazzino);
				refreshData();
			}
		}

	}

	private static final String PAGE_ID = "sediMagazzinoPerRifatturazioneAssociatePage";

	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	private RimuoviAssociazioneSedeCommand rimuoviAssociazioneSedeCommand;

	/**
	 * Costruttore.
	 * 
	 * @param magazzinoAnagraficaBD
	 *            magazzinoAnagraficaBD
	 */
	protected SediMagazzinoPerRifatturazioneAssociateTablePage(final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super(PAGE_ID, new String[] { "sedePerRifatturazione", "sedeEntita.entita", "sedeEntita.sede.localita",
				"sedeEntita.sede.indirizzo" }, SedeMagazzinoLite.class);
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		setShowTitlePane(false);
		getTable().setAggregatedColumns(new String[] { "sedePerRifatturazione" });
	}

	@Override
	public AbstractCommand[] getCommands() {
		return new AbstractCommand[] { getRimuoviAssociazioneSedeCommand() };
	}

	/**
	 * @return the rimuoviAssociazioneSedeCommand
	 */
	public RimuoviAssociazioneSedeCommand getRimuoviAssociazioneSedeCommand() {
		if (rimuoviAssociazioneSedeCommand == null) {
			rimuoviAssociazioneSedeCommand = new RimuoviAssociazioneSedeCommand();
		}

		return rimuoviAssociazioneSedeCommand;
	}

	@Override
	public Collection<SedeMagazzinoLite> loadTableData() {
		return magazzinoAnagraficaBD.caricaSediRifatturazioneAssociate();
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public Collection<SedeMagazzinoLite> refreshTableData() {
		return loadTableData();
	}

	@Override
	public void setFormObject(Object object) {
		// Non utilizzato, carico tutte le sedi associate
	}

}
