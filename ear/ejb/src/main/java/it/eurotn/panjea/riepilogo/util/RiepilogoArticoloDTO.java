package it.eurotn.panjea.riepilogo.util;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.magazzino.domain.Articolo.ETipoArticolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.io.Serializable;

public class RiepilogoArticoloDTO implements Serializable {

	private static final long serialVersionUID = 4363021756137044822L;

	private CategoriaLite categoria;
	private ArticoloLite articolo;

	private String codiceInterno;

	private String barCode;

	private ETipoArticolo tipoArticolo;

	private boolean articoloLibero;

	private CodiceIva codiceIva;

	private boolean ivaAlternativa;

	private CategoriaContabileArticolo categoriaContabileArticolo;

	private UnitaMisura unitaMisura;

	private UnitaMisura unitaMisuraQtaMagazzino;

	private FormulaTrasformazione formulaTrasformazioneQta;

	private FormulaTrasformazione formulaTrasformazioneQtaMagazzino;

	private Integer numeroDecimaliQta;

	private Integer numeroDecimaliPrezzo;

	private ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo;

	private CategoriaCommercialeArticolo categoriaCommercialeArticolo;

	private CategoriaCommercialeArticolo categoriaCommercialeArticolo2;

	/**
	 * Costruttore.
	 *
	 */
	public RiepilogoArticoloDTO() {
		super();
		this.categoria = new CategoriaLite();
		this.articolo = new ArticoloLite();
		this.codiceIva = new CodiceIva();
		this.categoriaContabileArticolo = new CategoriaContabileArticolo();
		this.unitaMisura = new UnitaMisura();
		this.unitaMisuraQtaMagazzino = new UnitaMisura();
		this.formulaTrasformazioneQta = new FormulaTrasformazione();
		this.formulaTrasformazioneQtaMagazzino = new FormulaTrasformazione();
		this.categoriaCommercialeArticolo = new CategoriaCommercialeArticolo();
		this.categoriaCommercialeArticolo2 = new CategoriaCommercialeArticolo();
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the barCode
	 */
	public String getBarCode() {
		return barCode;
	}

	/**
	 * @return the categoria
	 */
	public CategoriaLite getCategoria() {
		return categoria;
	}

	/**
	 * @return the categoriaCommercialeArticolo
	 */
	public CategoriaCommercialeArticolo getCategoriaCommercialeArticolo() {
		return categoriaCommercialeArticolo;
	}

	/**
	 * @return the categoriaCommercialeArticolo2
	 */
	public CategoriaCommercialeArticolo getCategoriaCommercialeArticolo2() {
		return categoriaCommercialeArticolo2;
	}

	/**
	 * @return the categoriaContabileArticolo
	 */
	public CategoriaContabileArticolo getCategoriaContabileArticolo() {
		return categoriaContabileArticolo;
	}

	/**
	 * @return Returns the codiceInterno.
	 */
	public String getCodiceInterno() {
		return codiceInterno;
	}

	/**
	 * @return the codiceIva
	 */
	public CodiceIva getCodiceIva() {
		return codiceIva;
	}

	/**
	 * @return the formulaTrasformazioneQta
	 */
	public FormulaTrasformazione getFormulaTrasformazioneQta() {
		return formulaTrasformazioneQta;
	}

	/**
	 * @return the formulaTrasformazioneQtaMagazzino
	 */
	public FormulaTrasformazione getFormulaTrasformazioneQtaMagazzino() {
		return formulaTrasformazioneQtaMagazzino;
	}

	/**
	 * @return the numeroDecimaliPrezzo
	 */
	public Integer getNumeroDecimaliPrezzo() {
		return numeroDecimaliPrezzo;
	}

	/**
	 * @return the numeroDecimaliQta
	 */
	public Integer getNumeroDecimaliQta() {
		return numeroDecimaliQta;
	}

	/**
	 * @return the provenienzaPrezzoArticolo
	 */
	public ProvenienzaPrezzoArticolo getProvenienzaPrezzoArticolo() {
		return provenienzaPrezzoArticolo;
	}

	/**
	 * @return the tipoArticolo
	 */
	public ETipoArticolo getTipoArticolo() {
		return tipoArticolo;
	}

	/**
	 * @return the unitaMisura
	 */
	public UnitaMisura getUnitaMisura() {
		return unitaMisura;
	}

	/**
	 * @return the unitaMisuraQtaMagazzino
	 */
	public UnitaMisura getUnitaMisuraQtaMagazzino() {
		return unitaMisuraQtaMagazzino;
	}

	/**
	 * @return the articoloLibero
	 */
	public boolean isArticoloLibero() {
		return articoloLibero;
	}

	/**
	 * @return the ivaAlternativa
	 */
	public boolean isIvaAlternativa() {
		return ivaAlternativa;
	}

	/**
	 * @param articoloLibero
	 *            the articoloLibero to set
	 */
	public void setArticoloLibero(boolean articoloLibero) {
		this.articoloLibero = articoloLibero;
	}

	/**
	 * @param barCode
	 *            the barCode to set
	 */
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	/**
	 * @param codiceArticolo
	 *            the codiceArticolo to set
	 */
	public void setCodiceArticolo(String codiceArticolo) {
		this.getArticolo().setCodice(codiceArticolo);
	}

	/**
	 * @param codiceCategoria
	 *            the codiceCategoria to set
	 */
	public void setCodiceCategoria(String codiceCategoria) {
		this.getCategoria().setCodice(codiceCategoria);
	}

	/**
	 * @param codiceCategoriaCommerciale
	 *            the codiceCategoriaCommerciale to set
	 */
	public void setCodiceCategoriaCommerciale(String codiceCategoriaCommerciale) {
		if (codiceCategoriaCommerciale != null) {
			this.categoriaCommercialeArticolo.setCodice(codiceCategoriaCommerciale);
		}
	}

	/**
	 * @param codiceCategoriaCommerciale2
	 *            the codiceCategoriaCommerciale2 to set
	 */
	public void setCodiceCategoriaCommerciale2(String codiceCategoriaCommerciale2) {
		if (codiceCategoriaCommerciale2 != null) {
			this.categoriaCommercialeArticolo2.setCodice(codiceCategoriaCommerciale2);
		}
	}

	/**
	 * @param codiceCategoriaContabileArticolo
	 *            the codiceCategoriaContabileArticolo to set
	 */
	public void setCodiceCategoriaContabileArticolo(String codiceCategoriaContabileArticolo) {
		if (getCategoriaContabileArticolo() != null) {
			this.getCategoriaContabileArticolo().setCodice(codiceCategoriaContabileArticolo);
		}
	}

	/**
	 * @param codiceCodiceIva
	 *            the codiceCodiceIva to set
	 */
	public void setCodiceCodiceIva(String codiceCodiceIva) {
		this.getCodiceIva().setCodice(codiceCodiceIva);
	}

	/**
	 * @param codiceFormulaTrasformazioneQta
	 *            the codiceFormulaTrasformazioneQta to set
	 */
	public void setCodiceFormulaTrasformazioneQta(String codiceFormulaTrasformazioneQta) {
		if (getFormulaTrasformazioneQta() != null) {
			this.getFormulaTrasformazioneQta().setCodice(codiceFormulaTrasformazioneQta);
		}
	}

	/**
	 * @param codiceFormulaTrasformazioneQtaMagazzino
	 *            the codiceFormulaTrasformazioneQtaMagazzino to set
	 */
	public void setCodiceFormulaTrasformazioneQtaMagazzino(String codiceFormulaTrasformazioneQtaMagazzino) {
		if (getFormulaTrasformazioneQtaMagazzino() != null) {
			this.getFormulaTrasformazioneQtaMagazzino().setCodice(codiceFormulaTrasformazioneQtaMagazzino);
		}
	}

	/**
	 * @param codiceInterno
	 *            The codiceInterno to set.
	 */
	public void setCodiceInterno(String codiceInterno) {
		this.codiceInterno = codiceInterno;
	}

	/**
	 * @param codiceUnitaMisura
	 *            the codiceUnitaMisura to set
	 */
	public void setCodiceUnitaMisura(String codiceUnitaMisura) {
		this.getUnitaMisura().setCodice(codiceUnitaMisura);
	}

	/**
	 * @param codiceUnitaMisuraQtaMagazzino
	 *            the codiceUnitaMisuraQtaMagazzino to set
	 */
	public void setCodiceUnitaMisuraQtaMagazzino(String codiceUnitaMisuraQtaMagazzino) {
		this.getUnitaMisuraQtaMagazzino().setCodice(codiceUnitaMisuraQtaMagazzino);
	}

	/**
	 * @param descrizioneArticolo
	 *            the descrizioneArticolo to set
	 */
	public void setDescrizioneArticolo(String descrizioneArticolo) {
		this.getArticolo().setDescrizione(descrizioneArticolo);
	}

	/**
	 * @param descrizioneCategoria
	 *            the descrizioneCategoria to set
	 */
	public void setDescrizioneCategoria(String descrizioneCategoria) {
		this.getCategoria().setDescrizione(descrizioneCategoria);
	}

	/**
	 * @param descrizioneCodiceIva
	 *            the descrizioneCodiceIva to set
	 */
	public void setDescrizioneCodiceIva(String descrizioneCodiceIva) {
		this.getCodiceIva().setDescrizioneInterna(descrizioneCodiceIva);
	}

	/**
	 * @param descrizioneUnitaMisura
	 *            the descrizioneUnitaMisura to set
	 */
	public void setDescrizioneUnitaMisura(String descrizioneUnitaMisura) {
		this.getUnitaMisura().setDescrizione(descrizioneUnitaMisura);
	}

	/**
	 * @param descrizioneUnitaMisuraQtaMagazzino
	 *            the descrizioneUnitaMisuraQtaMagazzino to set
	 */
	public void setDescrizioneUnitaMisuraQtaMagazzino(String descrizioneUnitaMisuraQtaMagazzino) {
		this.getUnitaMisuraQtaMagazzino().setDescrizione(descrizioneUnitaMisuraQtaMagazzino);
	}

	/**
	 * @param idArticolo
	 *            the idArticolo to set
	 */
	public void setIdArticolo(Integer idArticolo) {
		this.getArticolo().setId(idArticolo);
	}

	/**
	 * @param idCategoria
	 *            the idCategoria to set
	 */
	public void setIdCategoria(Integer idCategoria) {
		this.getCategoria().setId(idCategoria);
	}

	/**
	 * @param idCategoriaCommerciale
	 *            the idCategoriaCommerciale to set
	 */
	public void setIdCategoriaCommerciale(Integer idCategoriaCommerciale) {
		if (idCategoriaCommerciale == null) {
			this.categoriaCommercialeArticolo = null;
		} else {
			this.categoriaCommercialeArticolo.setId(idCategoriaCommerciale);
		}
	}

	/**
	 * @param idCategoriaCommerciale2
	 *            the idCategoriaCommerciale2 to set
	 */
	public void setIdCategoriaCommerciale2(Integer idCategoriaCommerciale2) {
		if (idCategoriaCommerciale2 == null) {
			this.categoriaCommercialeArticolo2 = null;
		} else {
			this.categoriaCommercialeArticolo2.setId(idCategoriaCommerciale2);
		}
	}

	/**
	 * @param idCategoriaContabileArticolo
	 *            the idCategoriaContabileArticolo to set
	 */
	public void setIdCategoriaContabileArticolo(Integer idCategoriaContabileArticolo) {
		if (idCategoriaContabileArticolo == null) {
			this.categoriaContabileArticolo = null;
		} else {
			this.getCategoriaContabileArticolo().setId(idCategoriaContabileArticolo);
		}
	}

	/**
	 * @param idCodiceIva
	 *            the idCodiceIva to set
	 */
	public void setIdCodiceIva(Integer idCodiceIva) {
		this.getCodiceIva().setId(idCodiceIva);
	}

	/**
	 * @param idFormulaTrasformazioneQta
	 *            the idFormulaTrasformazioneQta to set
	 */
	public void setIdFormulaTrasformazioneQta(Integer idFormulaTrasformazioneQta) {
		if (idFormulaTrasformazioneQta == null) {
			this.formulaTrasformazioneQta = null;
		} else {
			this.getFormulaTrasformazioneQta().setId(idFormulaTrasformazioneQta);
		}
	}

	/**
	 * @param idFormulaTrasformazioneQtaMagazzino
	 *            the idFormulaTrasformazioneQtaMagazzino to set
	 */
	public void setIdFormulaTrasformazioneQtaMagazzino(Integer idFormulaTrasformazioneQtaMagazzino) {
		if (idFormulaTrasformazioneQtaMagazzino == null) {
			this.formulaTrasformazioneQtaMagazzino = null;
		} else {
			this.getFormulaTrasformazioneQtaMagazzino().setId(idFormulaTrasformazioneQtaMagazzino);
		}
	}

	/**
	 * @param idUnitaMisura
	 *            the idUnitaMisura to set
	 */
	public void setIdUnitaMisura(Integer idUnitaMisura) {
		this.getUnitaMisura().setId(idUnitaMisura);
	}

	/**
	 * @param idUnitaMisuraQtaMagazzino
	 *            the idUnitaMisuraQtaMagazzino to set
	 */
	public void setIdUnitaMisuraQtaMagazzino(Integer idUnitaMisuraQtaMagazzino) {
		this.getUnitaMisuraQtaMagazzino().setId(idUnitaMisuraQtaMagazzino);
	}

	/**
	 * @param ivaAlternativa
	 *            the ivaAlternativa to set
	 */
	public void setIvaAlternativa(boolean ivaAlternativa) {
		this.ivaAlternativa = ivaAlternativa;
	}

	/**
	 * @param numeroDecimaliPrezzo
	 *            the numeroDecimaliPrezzo to set
	 */
	public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
		this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
	}

	/**
	 * @param numeroDecimaliQta
	 *            the numeroDecimaliQta to set
	 */
	public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
		this.numeroDecimaliQta = numeroDecimaliQta;
	}

	/**
	 * @param provenienzaPrezzoArticolo
	 *            the provenienzaPrezzoArticolo to set
	 */
	public void setProvenienzaPrezzoArticolo(ProvenienzaPrezzoArticolo provenienzaPrezzoArticolo) {
		this.provenienzaPrezzoArticolo = provenienzaPrezzoArticolo;
	}

	/**
	 * @param tipoArticolo
	 *            the tipoArticolo to set
	 */
	public void setTipoArticolo(ETipoArticolo tipoArticolo) {
		this.tipoArticolo = tipoArticolo;
	}

}
