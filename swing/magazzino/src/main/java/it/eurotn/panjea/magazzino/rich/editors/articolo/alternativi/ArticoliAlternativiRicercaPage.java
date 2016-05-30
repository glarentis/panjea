package it.eurotn.panjea.magazzino.rich.editors.articolo.alternativi;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.articolo.GiacenzaCellRenderer;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ParametriRicercaArticoloForm;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.util.RcpSupport;

public class ArticoliAlternativiRicercaPage extends AbstractTablePageEditor<ArticoloRicerca> {

	private class FiltroPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (articoloCorrente != null) {
				applyFilter();
				// se la tabella non ha un propertyCommandExecutor collegato sono nell'anagrafica ed allora salvo il
				// filtro quando cambia.
				// se ho un propertyCommandExecutor sto selezionando l'articolo per la sostutizione e quindi non devo
				// aggiornare il filtro
				if (getTable().getPropertyCommandExecutor() == null) {
					ParametriRicercaArticolo parametriRicercaArticolo = (ParametriRicercaArticolo) parametriRicercaArticoloForm
							.getFormObject();
					magazzinoAnagraficaBD.aggiornaArticoliAlternativiFiltro(articoloCorrente.getId(),
							parametriRicercaArticolo.getFiltro());
				}
			}
		}
	}

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private ParametriRicercaArticoloForm parametriRicercaArticoloForm;
	private FiltroPropertyChangeListener filtroPropertyChangeListener = null;
	private Articolo articoloCorrente;

	/**
	 * Costruttore.
	 */
	public ArticoliAlternativiRicercaPage() {
		super("articoliAlternativiRicercaPage", new String[] { "codice", "descrizione", "abilitato", "giacenza" },
				ArticoloRicerca.class);
		GiacenzaCellRenderer giacenzaCellRenderer = new GiacenzaCellRenderer();
		getTable().getTable().getColumnModel().getColumn(3).setCellRenderer(giacenzaCellRenderer);
		getTable().getTable().getColumnModel().getColumn(3).setCellEditor(giacenzaCellRenderer);
	}

	/**
	 * Esegue la ricerca articoli.
	 */
	private void applyFilter() {
		ParametriRicercaArticolo parametriRicercaArticolo = (ParametriRicercaArticolo) parametriRicercaArticoloForm
				.getFormObject();

		AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
		parametriRicercaArticolo.setIdDeposito(aziendaCorrente.getDepositoPrincipale().getId());
		parametriRicercaArticolo.setOrdinaPerAttributi(true);
		List<ArticoloRicerca> articoliRicerca = new ArrayList<ArticoloRicerca>();
		if (parametriRicercaArticolo.getFiltro() != null && !parametriRicercaArticolo.getFiltro().isEmpty()) {
			articoliRicerca = magazzinoAnagraficaBD.ricercaArticoli(parametriRicercaArticolo);
		}
		setRows(articoliRicerca);
	}

	@Override
	protected JComponent createTableWidget() {
		JComponent tableWidget = super.createTableWidget();
		parametriRicercaArticoloForm.setDownCommand(getTable().getNavigationCommands()[JideTableWidget.NAVIGATE_NEXT]);
		parametriRicercaArticoloForm
				.setUpCommand(getTable().getNavigationCommands()[JideTableWidget.NAVIGATE_PREVIOUS]);
		return tableWidget;
	}

	@Override
	public JComponent getHeaderControl() {
		if (parametriRicercaArticoloForm == null) {
			parametriRicercaArticoloForm = new ParametriRicercaArticoloForm("ricercaArticoliSostitutiviForm");
			filtroPropertyChangeListener = new FiltroPropertyChangeListener();
			parametriRicercaArticoloForm.getValueModel("filtro").addValueChangeListener(filtroPropertyChangeListener);
			parametriRicercaArticoloForm.getValueModel("calcolaGiacenza").addValueChangeListener(
					filtroPropertyChangeListener);
		}
		return parametriRicercaArticoloForm.getControl();
	}

	/**
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	@Override
	public Collection<ArticoloRicerca> loadTableData() {
		applyFilter();
		return null;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public Collection<ArticoloRicerca> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {
		articoloCorrente = (Articolo) object;
		ParametriRicercaArticolo parametri = (ParametriRicercaArticolo) parametriRicercaArticoloForm.getFormObject();
		parametri.setIdArticolo(articoloCorrente.getId());
		parametri.setFiltro(articoloCorrente.getArticoliAlternativiFiltro());
		parametri.setRicercaCodiceOrDescrizione(true);
		parametriRicercaArticoloForm.setFormObject(parametri);
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
