package it.eurotn.panjea.magazzino.manager.export.exporter.sait;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaitAreaRendicontazioneBeanExporter {

	private String codiceCliente;

	private AreaMagazzino areaMagazzino;

	private IAreaDocumento areaCollegata;

	private int progressivo;

	/**
	 * Costruttore.
	 * 
	 * @param codiceCliente
	 *            codice cliente
	 * @param areaMagazzino
	 *            area magazzino
	 * @param areaCollegata
	 *            area collegata
	 * @param progressivo
	 *            progressivo
	 */
	public SaitAreaRendicontazioneBeanExporter(final String codiceCliente, final AreaMagazzino areaMagazzino,
			final IAreaDocumento areaCollegata, final int progressivo) {
		super();
		this.areaMagazzino = areaMagazzino;
		this.areaCollegata = areaCollegata;
		this.progressivo = progressivo;
		this.codiceCliente = codiceCliente;
	}

	/**
	 * @return Returns the areaCollegata.
	 */
	public IAreaDocumento getAreaCollegata() {
		return areaCollegata;
	}

	/**
	 * @return Returns the areaMagazzino.
	 */
	public AreaMagazzino getAreaMagazzino() {
		return areaMagazzino;
	}

	/**
	 * @return Returns the codiceCliente.
	 */
	public String getCodiceCliente() {
		return codiceCliente;
	}

	/**
	 * @return Returns the codiceSocio.
	 */
	public String getCodiceSocio() {
		if (areaCollegata != null) {
			return ((AreaMagazzino) areaCollegata).getDocumento().getSedeEntita().getCodice();
		} else {
			return areaMagazzino.getDocumento().getSedeEntita().getCodice();
		}
	}

	/**
	 * @return Returns the areaMagazzino.
	 */
	public Date getDataDocumento() {
		return areaMagazzino.getDocumento().getDataDocumento();
	}

	/**
	 * @return Returns the dataDocumentoCollegato.
	 */
	public Date getDataDocumentoCollegato() {
		if (areaCollegata != null) {
			return areaCollegata.getDocumento().getDataDocumento();
		}

		return null;
	}

	/**
	 * @return Returns the numeroDocumento.
	 */
	public Integer getNumeroDocumento() {
		int codiceDocumento = 0;
		// prendo il primo numero che trovo nel codice documento
		Pattern pattern = Pattern.compile("([0-9]+)");
		Matcher matcher = pattern.matcher(areaMagazzino.getDocumento().getCodice().getCodice());
		if (matcher.find()) {
			codiceDocumento = Integer.parseInt(matcher.group(0));
		}
		return codiceDocumento;
	}

	/**
	 * @return Returns the numeroDocumentoCollegato.
	 */
	public Integer getNumeroDocumentoCollegato() {
		if (areaCollegata != null) {
			int codiceDocumento = 0;
			// prendo il primo numero che trovo nel codice documento
			Pattern pattern = Pattern.compile("([0-9]+)");
			Matcher matcher = pattern.matcher(areaCollegata.getDocumento().getCodice().getCodice());
			if (matcher.find()) {
				codiceDocumento = Integer.parseInt(matcher.group(0));
			}
			return codiceDocumento;
		}

		return null;
	}

	/**
	 * @return Returns the progressivo.
	 */
	public int getProgressivo() {
		return progressivo;
	}

	/**
	 * @return Returns the tipoDocumento.
	 */
	public String getTipoDocumento() {
		if (areaMagazzino.getDocumento().getTipoDocumento().isNotaCreditoEnable()) {
			return "N";
		}

		return "F";
	}

	/**
	 * @return Returns the tipoRecord.
	 */
	public int getTipoRecord() {
		return 1;
	}

	/**
	 * @param areaCollegata
	 *            The areaCollegata to set.
	 */
	public void setAreaCollegata(AreaMagazzino areaCollegata) {
	}

	/**
	 * @param areaMagazzino
	 *            The areaMagazzino to set.
	 */
	public void setAreaMagazzino(AreaMagazzino areaMagazzino) {
	}

	/**
	 * @param codiceCliente
	 *            The codiceCliente to set.
	 */
	public void setCodiceCliente(String codiceCliente) {
	}

	/**
	 * @param codiceSocio
	 *            The codiceSocio to set.
	 */
	public void setCodiceSocio(String codiceSocio) {
	}

	/**
	 * @param dataDocumento
	 *            The dataDocumento to set.
	 */
	public void setDataDocumento(Date dataDocumento) {
	}

	/**
	 * @param dataDocumentoCollegato
	 *            The dataDocumentoCollegato to set.
	 */
	public void setDataDocumentoCollegato(Date dataDocumentoCollegato) {
	}

	/**
	 * @param numeroDocumento
	 *            The numeroDocumento to set.
	 */
	public void setNumeroDocumento(Integer numeroDocumento) {
	}

	/**
	 * @param numeroDocumentoCollegato
	 *            The numeroDocumentoCollegato to set.
	 */
	public void setNumeroDocumentoCollegato(Integer numeroDocumentoCollegato) {
	}

	/**
	 * @param progressivo
	 *            The progressivo to set.
	 */
	public void setProgressivo(int progressivo) {
	}

	/**
	 * @param tipoDocumento
	 *            The tipoDocumento to set.
	 */
	public void setTipoDocumento(String tipoDocumento) {
	}

	/**
	 * @param tipoRecord
	 *            The tipoRecord to set.
	 */
	public void setTipoRecord(int tipoRecord) {
	}

}
