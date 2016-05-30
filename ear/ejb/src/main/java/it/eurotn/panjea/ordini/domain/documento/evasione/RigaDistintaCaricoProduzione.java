package it.eurotn.panjea.ordini.domain.documento.evasione;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;

import java.util.Date;

public class RigaDistintaCaricoProduzione extends RigaDistintaCarico {

	private static final long serialVersionUID = -4437285811514422531L;

	private Date dataProduzione;
	private RigaArticolo rigaOrdineCollegata;

	{
		rigaOrdineCollegata = new RigaArticolo();
		rigaOrdineCollegata.setArticolo(new ArticoloLite());
		rigaOrdineCollegata.getAreaOrdine().setTipoAreaOrdine(new TipoAreaOrdine());
		rigaOrdineCollegata.getAreaOrdine().getDocumento().setEntita(new ClienteLite());
		setGestioneGiacenza(GestioneGiacenza.NESSUNO);
	}

	/**
	 * Costruttore.
	 */
	public RigaDistintaCaricoProduzione() {
		super();
	}

	/**
	 * @return the dataProduzione
	 */
	public Date getDataProduzione() {
		return dataProduzione;
	}

	/**
	 * @return the qtaResidua
	 */
	@Override
	public Double getQtaResidua() {
		Double qtaResidua = getQtaOrdinata();
		if (getQtaDaEvadere() != null) {
			qtaResidua = qtaResidua - getQtaDaEvadere() - getQtaEvasa();
		}
		return qtaResidua;
	}

	/**
	 * @return the rigaOrdineCollegata
	 */
	public RigaArticolo getRigaOrdineCollegata() {
		return rigaOrdineCollegata;
	}

	/**
	 * @param codiceArticoloRigaOrdineCollegata
	 *            the codiceArticoloRigaOrdineCollegata to set
	 */
	public void setcodiceArticoloRigaOrdineCollegata(String codiceArticoloRigaOrdineCollegata) {
		rigaOrdineCollegata.getArticolo().setCodice(codiceArticoloRigaOrdineCollegata);
	}

	/**
	 * @param codiceEntitaDocumentoOrdineCollegato
	 *            the codiceEntitaDocumentoOrdineCollegato to set
	 */
	public void setCodiceEntitaDocumentoOrdineCollegato(Integer codiceEntitaDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getEntita().setCodice(codiceEntitaDocumentoOrdineCollegato);
	}

	/**
	 * @param codiceTipoDocumentoOrdineCollegato
	 *            the codiceTipoDocumentoOrdineCollegato to set
	 */
	public void setCodiceTipoDocumentoOrdineCollegato(String codiceTipoDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getTipoDocumento()
				.setCodice(codiceTipoDocumentoOrdineCollegato);
		rigaOrdineCollegata.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento()
				.setCodice(codiceTipoDocumentoOrdineCollegato);
	}

	/**
	 * @param dataDocumentoOrdineCollegato
	 *            the dataDocumentoOrdineCollegato to set
	 */
	public void setDataDocumentoOrdineCollegato(Date dataDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().setDataDocumento(dataDocumentoOrdineCollegato);
	}

	/**
	 * @param dataProduzione
	 *            the dataProduzione to set
	 */
	public void setDataProduzione(Date dataProduzione) {
		this.dataProduzione = dataProduzione;
	}

	/**
	 * @param dataRegistrazioneOrdineCollegato
	 *            the dataRegistrazioneOrdineCollegato to set
	 */
	public void setDataRegistrazioneOrdineCollegato(Date dataRegistrazioneOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().setDataRegistrazione(dataRegistrazioneOrdineCollegato);
	}

	/**
	 * @param denominazioneEntitaDocumentoOrdineCollegato
	 *            the denominazioneEntitaDocumentoOrdineCollegato to set
	 */
	public void setDenominazioneEntitaDocumentoOrdineCollegato(String denominazioneEntitaDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getEntita().getAnagrafica()
				.setDenominazione(denominazioneEntitaDocumentoOrdineCollegato);
	}

	/**
	 * @param descrizioneRigaOrdineCollegata
	 *            the descrizioneRigaOrdineCollegata to set
	 */
	public void setDescrizioneRigaOrdineCollegata(String descrizioneRigaOrdineCollegata) {
		rigaOrdineCollegata.setDescrizione(descrizioneRigaOrdineCollegata);
	}

	/**
	 * @param descrizioneTipoDocumentoOrdineCollegato
	 *            the descrizioneTipoDocumentoOrdineCollegato to set
	 */
	public void setDescrizioneTipoDocumentoOrdineCollegato(String descrizioneTipoDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getTipoDocumento()
				.setDescrizione(descrizioneTipoDocumentoOrdineCollegato);
		rigaOrdineCollegata.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento()
				.setDescrizione(descrizioneTipoDocumentoOrdineCollegato);
	}

	/**
	 * @param idAreaOrdineCollegata
	 *            the idAreaOrdineCollegata to set
	 */
	public void setIdAreaOrdineCollegata(Integer idAreaOrdineCollegata) {
		rigaOrdineCollegata.getAreaOrdine().setId(idAreaOrdineCollegata);
	}

	/**
	 * @param idArticoloRigaOrdineCollegata
	 *            the idArticoloRigaOrdineCollegata to set
	 */
	public void setIdArticoloRigaOrdineCollegata(Integer idArticoloRigaOrdineCollegata) {
		rigaOrdineCollegata.getArticolo().setId(idArticoloRigaOrdineCollegata);
	}

	/**
	 * @param idDocumentoOrdineCollegato
	 *            the idDocumentoOrdineCollegato to set
	 */
	public void setIdDocumentoOrdineCollegato(Integer idDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().setId(idDocumentoOrdineCollegato);
	}

	/**
	 * @param idEntitaDocumentoOrdineCollegato
	 *            the idEntitaDocumentoOrdineCollegato to set
	 */
	public void setIdEntitaDocumentoOrdineCollegato(Integer idEntitaDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getEntita().setId(idEntitaDocumentoOrdineCollegato);
	}

	/**
	 * @param idRigaOrdineCollegata
	 *            the idRigaOrdineCollegata to set
	 */
	public void setIdRigaOrdineCollegata(Integer idRigaOrdineCollegata) {
		rigaOrdineCollegata.setId(idRigaOrdineCollegata);
	}

	/**
	 * @param idTipoDocumentoOrdineCollegato
	 *            the idTipoDocumentoOrdineCollegato to set
	 */
	public void setIdTipoDocumentoOrdineCollegato(Integer idTipoDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getTipoDocumento().setId(idTipoDocumentoOrdineCollegato);
		rigaOrdineCollegata.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento()
				.setId(idTipoDocumentoOrdineCollegato);
	}

	/**
	 * @param numeroDocumentoOrdineCollegato
	 *            the numeroDocumentoOrdineCollegato to set
	 */
	public void setNumeroDocumentoOrdineCollegato(CodiceDocumento numeroDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().setCodice(numeroDocumentoOrdineCollegato);
	}

	/**
	 * @param rigaOrdineCollegata
	 *            the rigaOrdineCollegata to set
	 */
	public void setRigaOrdineCollegata(RigaArticolo rigaOrdineCollegata) {
		this.rigaOrdineCollegata = rigaOrdineCollegata;
	}

	/**
	 * @param versionEntitaDocumentoOrdineCollegato
	 *            the versionEntitaDocumentoOrdineCollegato to set
	 */
	public void setVersionEntitaDocumentoOrdineCollegato(Integer versionEntitaDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getEntita()
				.setVersion(versionEntitaDocumentoOrdineCollegato);
	}

	/**
	 * @param versionTipoDocumentoOrdineCollegato
	 *            the versionTipoDocumentoOrdineCollegato to set
	 */
	public void setVersionTipoDocumentoOrdineCollegato(Integer versionTipoDocumentoOrdineCollegato) {
		rigaOrdineCollegata.getAreaOrdine().getDocumento().getTipoDocumento()
				.setVersion(versionTipoDocumentoOrdineCollegato);
		rigaOrdineCollegata.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento()
				.setVersion(versionTipoDocumentoOrdineCollegato);
	}

}
