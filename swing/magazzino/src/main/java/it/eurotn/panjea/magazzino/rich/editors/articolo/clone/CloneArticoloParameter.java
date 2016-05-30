package it.eurotn.panjea.magazzino.rich.editors.articolo.clone;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

public class CloneArticoloParameter implements IDefProperty {
	private String nuovoCodice;
	private String nuovaDescrizione;
	private boolean copyDistinta;
	private boolean copyListino;
	private boolean azzeraPrezziListino;
	private Articolo articolo;
	private List<AttributoArticolo> attributiArticolo;

	/**
	 * 
	 * @param articoloToClone
	 *            articolo da clonare
	 */
	public CloneArticoloParameter(final Articolo articoloToClone) {
		articolo = articoloToClone;

		attributiArticolo = new ArrayList<AttributoArticolo>();
		for (AttributoArticolo attributo : articoloToClone.getAttributiArticolo()) {
			AttributoArticolo attributoCopiato = new AttributoArticolo();
			PanjeaEJBUtil.copyProperties(attributoCopiato, attributo);
			attributoCopiato.setId(null);
			attributoCopiato.setVersion(null);
			attributoCopiato.setArticolo(null);
			attributiArticolo.add(attributoCopiato);
		}
		nuovoCodice = ObjectUtils.defaultIfNull(articoloToClone.getCategoria().getGenerazioneCodiceArticoloData()
				.getMascheraCodiceArticolo(), "");
		nuovaDescrizione = ObjectUtils.defaultIfNull(articoloToClone.getCategoria().getGenerazioneCodiceArticoloData()
				.getMascheraDescrizioneArticolo(), "");
	}

	/**
	 * @return Returns the articolo.
	 */
	public Articolo getArticolo() {
		return articolo;
	}

	/**
	 * @return Returns the attributiArticolo.
	 */
	public List<AttributoArticolo> getAttributiArticolo() {
		return attributiArticolo;
	}

	@Override
	public String getDomainClassName() {
		return null;
	}

	@Override
	public Integer getId() {
		return null;
	}

	/**
	 * @return Returns the nuovaDescrizione.
	 */
	public String getNuovaDescrizione() {
		return nuovaDescrizione;
	}

	/**
	 * @return Returns the nuovoCodice.
	 */
	public String getNuovoCodice() {
		return nuovoCodice;
	}

	@Override
	public Integer getVersion() {
		return null;
	}

	/**
	 * @return Returns the azzeraPrezziListino.
	 */
	public boolean isAzzeraPrezziListino() {
		return azzeraPrezziListino;
	}

	/**
	 * @return Returns the copyDistinta.
	 */
	public boolean isCopyDistinta() {
		return copyDistinta;
	}

	/**
	 * @return Returns the copyListino.
	 */
	public boolean isCopyListino() {
		return copyListino;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	/**
	 * @param attributiArticolo
	 *            The attributiArticolo to set.
	 */
	public void setAttributiArticolo(List<AttributoArticolo> attributiArticolo) {
		this.attributiArticolo = attributiArticolo;
	}

	/**
	 * @param azzeraPrezziListino
	 *            The azzeraPrezziListino to set.
	 */
	public void setAzzeraPrezziListino(boolean azzeraPrezziListino) {
		this.azzeraPrezziListino = azzeraPrezziListino;
	}

	/**
	 * @param copyDistinta
	 *            The copyDistinta to set.
	 */
	public void setCopyDistinta(boolean copyDistinta) {
		this.copyDistinta = copyDistinta;
	}

	/**
	 * @param copyListino
	 *            The copyListino to set.
	 */
	public void setCopyListino(boolean copyListino) {
		this.copyListino = copyListino;
	}

	/**
	 * @param nuovaDescrizione
	 *            The nuovaDescrizione to set.
	 */
	public void setNuovaDescrizione(String nuovaDescrizione) {
		this.nuovaDescrizione = nuovaDescrizione;
	}

	/**
	 * @param nuovoCodice
	 *            The nuovoCodice to set.
	 */
	public void setNuovoCodice(String nuovoCodice) {
		this.nuovoCodice = nuovoCodice;
	}

}