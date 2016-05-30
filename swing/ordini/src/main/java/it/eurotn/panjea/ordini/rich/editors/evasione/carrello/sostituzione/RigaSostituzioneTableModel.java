package it.eurotn.panjea.ordini.rich.editors.evasione.carrello.sostituzione;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.math.BigDecimal;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;

public class RigaSostituzioneTableModel extends DefaultBeanEditableTableModel<RigaDistintaCarico> implements
		NavigableTableModel {

	private static final long serialVersionUID = 7894342321804120977L;

	private RigaDistintaCarico rigaEvasione;

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;
	private IOrdiniDocumentoBD ordiniDocumentoBD;

	private static final ConverterContext NUMBERQTACONVERSIONCONTEXT = new NumberWithDecimalConverterContext();

	private static final EditorContext NUMBERQTAEDITORCONTEXT = new EditorContext("numberQtaEditorContext");
	private static final SearchContext SEARCH_ARTICOLO_CODICE_CONTEXT = new SearchContext("codice", "articolo");
	private static final SearchContext SEARCH_ARTICOLO_DESCRIZIONE_CONTEXT = new SearchContext("descrizione",
			"articolo");
	private static EditorContext numberPrezzoEditorContext;
	private static ConverterContext numberPrezzoContext;
	private static ConverterContext numberPrezzoTotaleContext;
	{
		NUMBERQTACONVERSIONCONTEXT.setUserObject(6);
		NUMBERQTAEDITORCONTEXT.setUserObject(6);

		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
		numberPrezzoTotaleContext = new NumberWithDecimalConverterContext();
		numberPrezzoTotaleContext.setUserObject(new Integer(2));
		numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);
	}

	/**
	 * Costruttore.
	 */
	public RigaSostituzioneTableModel() {
		super("rigaSostituzioneTableModel", new String[] { "articolo.codice", "articolo.descrizione", "qtaDaEvadere",
				"moltiplicatoreQta", "prezzoUnitario", "prezzoTotale" }, RigaDistintaCarico.class);
		this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
		this.ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
	}

	/**
	 * Calcola il prezzo unitario dell'articolo di riferimento usando come riferimento la riga distinta carico.
	 * 
	 * @param rigaDistintaCarico
	 *            riga distinta
	 * @param articolo
	 *            articolo di riferimento
	 * @return prezzo unitario
	 */
	private BigDecimal calcolaPrezzoUnitario(RigaDistintaCarico rigaDistintaCarico, ArticoloLite articolo) {

		// ricarico l'area ordine perchè quella nella riga distinta non ha tutte le proprietà avvalorate
		AreaOrdine areaOrdine = ordiniDocumentoBD.caricaRigaOrdine(rigaDistintaCarico.getRigaArticolo())
				.getAreaOrdine();
		Integer idListinoAlternativo = null;
		Integer idListino = null;
		Integer idSedeEntita = null;
		Integer idTipoMezzo = null;
		Integer idZonaGeografica = null;
		String codiceLingua = null;
		Integer idAgente = null;
		BigDecimal percentualeScontoCommerciale = null;

		if (areaOrdine.getListinoAlternativo() != null) {
			idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
		}
		if (areaOrdine.getListino() != null) {
			idListino = areaOrdine.getListino().getId();
		}
		if (areaOrdine.getDocumento().getSedeEntita() != null) {
			idSedeEntita = areaOrdine.getDocumento().getSedeEntita().getId();
			codiceLingua = areaOrdine.getDocumento().getSedeEntita().getLingua();
		}
		if (areaOrdine.getAgente() != null) {
			idAgente = areaOrdine.getAgente().getId();
		}
		AreaRate areaRate = magazzinoDocumentoBD.caricaAreaRateByDocumento(areaOrdine.getDocumento());
		if (areaRate != null && areaRate.getCodicePagamento() != null && areaRate.getCodicePagamento().getId() != null) {
			percentualeScontoCommerciale = areaRate.getCodicePagamento().getPercentualeScontoCommerciale();
		}

		ParametriCreazioneRigaArticolo paramCreazione = new ParametriCreazioneRigaArticolo();
		paramCreazione.setProvenienzaPrezzo(ProvenienzaPrezzo.LISTINO);
		paramCreazione.setIdArticolo(articolo.getId());
		paramCreazione.setData(areaOrdine.getDocumento().getDataDocumento());
		paramCreazione.setIdSedeEntita(idSedeEntita);
		paramCreazione.setIdListinoAlternativo(idListinoAlternativo);
		paramCreazione.setIdListino(idListino);
		paramCreazione.setImporto(areaOrdine.getDocumento().getTotale().clone());
		paramCreazione.setCodiceIvaAlternativo(areaOrdine.getCodiceIvaAlternativo());
		paramCreazione.setIdTipoMezzo(idTipoMezzo);
		paramCreazione.setIdZonaGeografica(idZonaGeografica);
		paramCreazione.setProvenienzaPrezzoArticolo(articolo.getProvenienzaPrezzoArticolo());
		paramCreazione.setNoteSuDestinazione(areaOrdine.getTipoAreaOrdine().isNoteSuDestinazione());
		paramCreazione.setCodiceValuta(areaOrdine.getDocumento().getTotale().getCodiceValuta());
		paramCreazione.setCodiceLingua(codiceLingua);
		paramCreazione.setIdAgente(idAgente);
		paramCreazione.setTipologiaCodiceIvaAlternativo(areaOrdine.getTipologiaCodiceIvaAlternativo());
		paramCreazione.setPercentualeScontoCommerciale(percentualeScontoCommerciale);
		paramCreazione.setTipoMovimento(TipoMovimento.NESSUNO);
		ParametriCalcoloPrezzi paramCalcolo = new ParametriCalcoloPrezzi(paramCreazione);

		PoliticaPrezzo politicaPrezzo = magazzinoDocumentoBD.calcolaPrezzoArticolo(paramCalcolo);

		RisultatoPrezzo<BigDecimal> risultatoPrezzo = politicaPrezzo.getPrezzi().getRisultatoPrezzo(0.0);

		BigDecimal prezzo = BigDecimal.ZERO;
		if (risultatoPrezzo != null) {
			prezzo = risultatoPrezzo.getValue();
		}

		return prezzo;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 2:
			return NUMBERQTACONVERSIONCONTEXT;
		case 4:
			ArticoloLite articolo = getElementAt(row).getArticolo();
			Integer numDec = (articolo != null && articolo.getId() != null) ? articolo.getNumeroDecimaliPrezzo() : 0;
			numberPrezzoContext.setUserObject(numDec);
			return numberPrezzoContext;
		case 5:
			return numberPrezzoTotaleContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return SEARCH_ARTICOLO_CODICE_CONTEXT;
		case 1:
			return SEARCH_ARTICOLO_DESCRIZIONE_CONTEXT;
		case 2:
			return NUMBERQTAEDITORCONTEXT;
		case 4:
			ArticoloLite articolo = getElementAt(row).getArticolo();
			Integer numDec = (articolo != null && articolo.getId() != null) ? articolo.getNumeroDecimaliPrezzo() : 0;
			numberPrezzoEditorContext.setUserObject(numDec);
			return numberPrezzoEditorContext;
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column < 5;
	}

	/**
	 * @param rigaEvasione
	 *            The rigaEvasione to set.
	 */
	public void setRigaEvasione(RigaDistintaCarico rigaEvasione) {
		this.rigaEvasione = rigaEvasione;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		// Le righe sono poche quindi posso permettermi di risettare sempre quelle non valide
		switch (column) {
		case 0:
		case 1:
			// Aggiorno il numero di decimali prezzo sulla riga distinta carico per il calcolo del prezzo
			ArticoloLite articolo = (ArticoloLite) value;
			Integer numDec = 0;
			BigDecimal prezzoUnitario = BigDecimal.ZERO;
			if (articolo != null && articolo.getId() != null) {
				numDec = articolo.getNumeroDecimaliPrezzo();

				// calcolo il prezzo unitario dell'articolo
				prezzoUnitario = calcolaPrezzoUnitario(rigaEvasione, articolo);
			}
			getElementAt(row).setNumeroDecimaliPrezzo(numDec);
			getElementAt(row).setPrezzoUnitario(prezzoUnitario);
			break;
		case 3:
			// non posso avere il moltiplicatore qta nullo
			if (value == null) {
				value = 0.0;
			}
			break;
		default:
			break;
		}
		super.setValueAt(value, row, column);
	}
}
