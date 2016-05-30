package it.eurotn.panjea.bi.util;

import java.io.Serializable;
import java.util.List;

public class Mappa implements Serializable {

	private static final long serialVersionUID = 2398195025751426690L;

	private String fileName;

	private String categoria;
	private String descrizione;

	private byte[] image;

	private List<String> managedFields;

	/**
	 * Costruttore.
	 *
	 * @param fileName
	 *            nome file
	 * @param categoria
	 *            categoria
	 * @param descrizione
	 *            descrizione della mappa
	 * @param managedFields
	 *            fields gestiti
	 * @param image
	 *            immagine
	 */
	public Mappa(final String fileName, final String categoria, final String descrizione,
			final List<String> managedFields, final byte[] image) {
		super();
		this.fileName = fileName;
		this.categoria = categoria;
		this.descrizione = descrizione;
		this.managedFields = managedFields;
		this.image = image;
	}

	/**
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @return the managedFields
	 */
	public List<String> getManagedFields() {
		return managedFields;
	}
}