package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.dialogs.PanjeaFilterListSelectionDialog;
import it.eurotn.rich.command.ICancellable;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.selection.dialog.FilterListSelectionDialog;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;

public class InserisciRaggruppamentoArticoliCommand extends ApplicationWindowAwareCommand implements ICancellable{

	protected class RaggruppamentoArticoliRender extends DefaultListCellRenderer {
		private static final long serialVersionUID = 8129263663324775797L;

		/**
		 * Costruttore.
		 * 
		 */
		public RaggruppamentoArticoliRender() {
			super();
		}

		@Override
		public Component getListCellRendererComponent(JList jlist, Object obj, int i, boolean flag, boolean flag1) {
			JLabel label = (JLabel) super.getListCellRendererComponent(jlist, obj, i, flag, flag1);
			label.setText(((RaggruppamentoArticoli) obj).getDescrizione());
			label.setIcon(RcpSupport.getIcon(RaggruppamentoArticoli.class.getName()));
			return label;
		}
	}

	public static final String AREA_DOCUMENTO_KEY = "areaDocumento";
	public static final String AREA_RATE_KEY = "areaRate";

	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
	private boolean cancelled;

	/**
	 * 
	 * Costruttore.
	 */
	public InserisciRaggruppamentoArticoliCommand() {
		super("openRaggruppamentoArticoliCommand");
		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
		CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services().getService(
				CommandConfigurer.class);
		c.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		FilterList<RaggruppamentoArticoli> raggruppamentiFilterList = new FilterList<RaggruppamentoArticoli>(
				GlazedLists.eventList(magazzinoAnagraficaBD.caricaRaggruppamenti()));
		FilterListSelectionDialog selectionDialog = new PanjeaFilterListSelectionDialog(
				"Seleziona il raggruppamento da inserire", Application.instance().getActiveWindow().getControl(),
				raggruppamentiFilterList) {
			@Override
			protected void onCancel() {
				super.onCancel();
				cancelled = true;
			}

			@Override
			protected void onSelect(Object selection) {
				cancelled = false;
				inserisciRighe((RaggruppamentoArticoli) selection);
			}
		};
		selectionDialog.setRenderer(new RaggruppamentoArticoliRender());
		selectionDialog.setFilterator(GlazedLists.textFilterator("descrizione"));
		selectionDialog.showDialog();
	}

	/**
	 * @return the magazzinoAnagraficaBD
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	/**
	 * Inserisce le righe per il raggruppamento.
	 * 
	 * @param raggruppamento
	 *            raggruppamento con le righe da inserire.
	 */
	protected void inserisciRighe(RaggruppamentoArticoli raggruppamento) {
		IMagazzinoDocumentoBD magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
		AreaMagazzino areaMagazzino = (AreaMagazzino) getParameter(AREA_DOCUMENTO_KEY);
		if (areaMagazzino == null) {
			throw new IllegalArgumentException("impostare l'area magazzino");
		}
		AreaRate areaRate = (AreaRate) getParameter(AREA_RATE_KEY, null);
		if (areaRate == null) {
			throw new IllegalArgumentException("Impostare l'area rate");
		}

		Integer idListinoAlternativo = null;
		Integer idListino = null;
		Integer idSedeEntita = null;
		Integer idTipoMezzo = null;
		String codiceLingua = null;
		Integer idRaggruppamento = raggruppamento.getId();
		BigDecimal percentualeScontoCommerciale = null;
		Integer idAgente = null;

		if (areaMagazzino.getListinoAlternativo() != null) {
			idListinoAlternativo = areaMagazzino.getListinoAlternativo().getId();
		}
		if (areaMagazzino.getListino() != null) {
			idListino = areaMagazzino.getListino().getId();
		}
		if (areaMagazzino.getDocumento().getSedeEntita() != null) {
			idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
			codiceLingua = areaMagazzino.getDocumento().getSedeEntita().getLingua();

			if (areaMagazzino.getDocumento().getSedeEntita().getAgente() != null) {
				idAgente = areaMagazzino.getDocumento().getSedeEntita().getAgente().getId();
			}
		}
		if (areaMagazzino.getMezzoTrasporto() != null) {
			idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
		}
		if (areaRate.getCodicePagamento() != null) {
			percentualeScontoCommerciale = areaRate.getCodicePagamento().getPercentualeScontoCommerciale();
		}

		magazzinoDocumentoBD.inserisciRaggruppamentoArticoli(areaMagazzino.getId(), areaMagazzino
				.getTipoAreaMagazzino().getProvenienzaPrezzo(), idRaggruppamento, areaMagazzino.getDocumento()
				.getDataDocumento(), idSedeEntita, idListinoAlternativo, idListino, areaMagazzino.getDocumento()
				.getTotale(), areaMagazzino.getCodiceIvaAlternativo(), idTipoMezzo,
				areaMagazzino.getIdZonaGeografica(), areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione(),
				areaMagazzino.getTipoAreaMagazzino().getTipoMovimento(), areaMagazzino.getDocumento().getTotale()
						.getCodiceValuta(), codiceLingua, idAgente, areaMagazzino.getTipologiaCodiceIvaAlternativo(),
				percentualeScontoCommerciale);
	}

	/**
	 * 
	 * @return true se è stata anullata l'operazione
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		// Tengo lo stesso ID del command sul menù
		// mi basta l'icona
		button.setText("");
		button.setName("RaggruppamentoArticoliMagazzinoCommand");
	}
}
