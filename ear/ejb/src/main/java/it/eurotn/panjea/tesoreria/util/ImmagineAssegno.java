package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.tesoreria.domain.AreaAssegno;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * Contiene l'immagine fronte/retro di un assegno.<br/>
 * 
 * @author giangi
 * @version 1.0, 14/ott/2011
 * 
 */
public class ImmagineAssegno implements Serializable {

	private enum LATO {
		Fronte("F"), Retro("R");

		private final String suffissoFile;

		/**
		 * Costruttore.
		 * 
		 * @param suffissoFile
		 *            definisce il suffisso per il nome de file
		 */
		LATO(final String suffissoFile) {
			this.suffissoFile = suffissoFile;

		}

		/**
		 * @return Returns the suffissoFile.
		 */
		public String getSuffissoFile() {
			return suffissoFile;
		}
	}

	private static Logger logger = Logger.getLogger(ImmagineAssegno.class);

	private static final long serialVersionUID = 2493939143427524347L;
	private byte[] frontByte;
	private byte[] retroByte;
	private AreaAssegno areaAssegno = null;
	public static final String PATH_FOLDER_ASSEGNI = "dirAssegni";

	/**
	 * Costruttore di default se devo caricare l'immagine assegno di un assegno esistente.
	 */
	public ImmagineAssegno() {
		super();
	}

	/**
	 * Costruisce l'immagine assegno caricandole dai due file.
	 * 
	 * @param imageFronteFile
	 *            immagine del fronte dell'assegno
	 * @param imageRetroFile
	 *            immagine del retro dell'assegno
	 */
	public ImmagineAssegno(final File imageFronteFile, final File imageRetroFile) {
		// Carico l'immagine e la inserisco come array di byte per poterla serializzare.
		frontByte = getByteFromImage(imageFronteFile);
		retroByte = getByteFromImage(imageRetroFile);
	}

	/**
	 * Cancella dal repository le immagini assegno.
	 * 
	 * @param pathFolderRepository
	 *            path del repository degli assegni
	 * @throws FileNotFoundException
	 *             path del repository non corretto
	 */
	public void cancella(String pathFolderRepository) throws FileNotFoundException {
		if (areaAssegno == null) {
			throw new IllegalStateException("Area assegno null");
		}

		File file = new File(pathFolderRepository);
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("Il percorso " + pathFolderRepository
					+ " non corrisponde ad una direcroty");
		}
		if (!file.exists()) {
			throw new FileNotFoundException(pathFolderRepository);
		}

		String fronteFileName = generaNome(LATO.Fronte);
		File frontFile = new File(pathFolderRepository + fronteFileName);
		if (frontFile.exists()) {
			frontFile.delete();
		}

		String retroFileName = generaNome(LATO.Retro);
		File retroFile = new File(pathFolderRepository + retroFileName);
		if (retroFile.exists()) {
			retroFile.delete();
		}
	}

	/**
	 * Carica dal repository le immagini dell'assegno.
	 * 
	 * @param pathFolderRepository
	 *            path del repository degli assegni
	 * @throws FileNotFoundException
	 *             path del repository non corretto
	 */
	public void carica(String pathFolderRepository) throws FileNotFoundException {
		if (areaAssegno == null) {
			throw new IllegalStateException("Area assegno null");
		}

		File file = new File(pathFolderRepository);
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("Il percorso " + pathFolderRepository
					+ " non corrisponde ad una direcroty");
		}
		if (!file.exists()) {
			throw new FileNotFoundException(pathFolderRepository);
		}

		String fronteFileName = generaNome(LATO.Fronte);
		File frontFile = new File(pathFolderRepository + fronteFileName);
		if (frontFile.exists()) {
			frontByte = getByteFromImage(frontFile);
		}

		String retroFileName = generaNome(LATO.Retro);
		File retroFile = new File(pathFolderRepository + retroFileName);
		if (retroFile.exists()) {
			retroByte = getByteFromImage(retroFile);
		}
	}

	/**
	 * 
	 * @param lato
	 *            lato per il quale generare il nome.
	 * @return nome del file
	 */
	private String generaNome(LATO lato) {
		String dataDoc = new SimpleDateFormat("ddMMyyyy").format(areaAssegno.getDocumento().getDataDocumento());
		StringBuilder sb = new StringBuilder(areaAssegno.getNumeroAssegno()).append(dataDoc)
				.append(lato.getSuffissoFile()).append(".jpg");
		return sb.toString();
	}

	/**
	 * 
	 * @return areaAssegno associata all'immagine
	 */
	public AreaAssegno getAreaAssegno() {
		return areaAssegno;
	}

	/**
	 * 
	 * @param imgFile
	 *            file dell'immagine immagine da trasformare
	 * @return array di byte dell'immagine.
	 */
	private byte[] getByteFromImage(File imgFile) {
		byte[] imageInByte;
		try {
			BufferedImage originalImage = ImageIO.read(imgFile);
			// convert BufferedImage to byte array
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			logger.error("-->errore nel trasformare l'immagine in byte", e);
			throw new RuntimeException(e);
		}
		return imageInByte;
	}

	/**
	 * 
	 * @return immagine fronte dell'assegno.
	 */
	public Image getFronte() {
		BufferedImage immagine = null;
		if (frontByte != null) {
			try {
				InputStream in = new ByteArrayInputStream(frontByte);
				immagine = ImageIO.read(in);
			} catch (IOException e) {
				logger.error("-->errore nel trasformare i byte dell'immagine in image", e);
				throw new RuntimeException(e);
			}
		}
		return immagine;
	}

	/**
	 * 
	 * @return immagine retro dell'assegno
	 */
	public Image getRetro() {
		BufferedImage immagine = null;
		if (retroByte != null) {
			try {
				InputStream in = new ByteArrayInputStream(retroByte);
				immagine = ImageIO.read(in);
			} catch (IOException e) {
				logger.error("-->errore nel trasformare i byte dell'immagine in image", e);
				throw new RuntimeException(e);
			}
		}
		return immagine;

	}

	/**
	 * Salva le immagini dell'assegno nel repository.
	 * 
	 * @param pathFolderRepository
	 *            folder del repository degli assegni.
	 * @throws FileNotFoundException
	 *             non esiste il repository degli assegni.
	 */
	public void salva(String pathFolderRepository) throws FileNotFoundException {
		if (areaAssegno == null) {
			throw new IllegalStateException("Area assegno null");
		}
		File file = new File(pathFolderRepository);
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("Il percorso " + pathFolderRepository
					+ " non corrisponde ad una directory");
		}
		if (!file.exists()) {
			throw new FileNotFoundException(pathFolderRepository);
		}

		File fileImmagineFront = new File(pathFolderRepository + generaNome(LATO.Fronte));
		try {
			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(frontByte);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "jpg", fileImmagineFront);
		} catch (IOException e) {
			logger.error("-->errore nel salvare l'immagine del front dell'assegno. Percorso dove provo a salvare "
					+ fileImmagineFront, e);
		}

		File fileImmagineRetro = new File(pathFolderRepository + generaNome(LATO.Retro));
		try {
			// convert byte array back to BufferedImage
			InputStream in = new ByteArrayInputStream(retroByte);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "jpg", fileImmagineRetro);
		} catch (IOException e) {
			logger.error("-->errore nel salvare l'immagine del front dell'assegno. Percorso dove provo a salvare "
					+ fileImmagineFront, e);
		}
	}

	/**
	 * @param areaAssegno
	 *            the areaAssegno to set
	 */
	public void setAreaAssegno(AreaAssegno areaAssegno) {
		this.areaAssegno = areaAssegno;
	}
}
