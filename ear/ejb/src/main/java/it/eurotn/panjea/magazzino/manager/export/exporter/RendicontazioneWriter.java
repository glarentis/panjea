package it.eurotn.panjea.magazzino.manager.export.exporter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.log4j.Logger;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;

public class RendicontazioneWriter {

	private static Logger logger = Logger.getLogger(RendicontazioneWriter.class);

	private static final String COLUMN_HEADER = "columnHeader";
	private static final String COLUMN_FOOTER = "footerHeader";

	/**
	 * Restituisce il nome del writer da utilizzare in base al template. Il nome del writer è il nome del file di
	 * template senza l'estensione.
	 * 
	 * @param templatePath
	 *            template path
	 * @return nome del writer
	 */
	private String getWriterName(String templatePath) {
		File file = new File(templatePath);
		String writerName = file.getName();
		int dotIndex = writerName.indexOf(".");
		if (dotIndex != -1) {
			writerName = writerName.substring(0, dotIndex);
		}

		return writerName;
	}

	/**
	 * In base al template specificato restituisce l'array dy byte che rappresenta i valori di testa,corpo e piede
	 * passati come parametro.
	 * 
	 * @param template
	 *            template da utilizzare
	 * @param columnHeader
	 *            include la testata di colonna definita nel template con lo stream "columnHeader"
	 * @param values
	 *            valori da esportare
	 * @param columnFooter
	 *            include il piede di colonna definita nel template con lo stream "footerHeader"
	 * @return flusso generato
	 */
	public byte[] write(String template, boolean columnHeader, List<Object> values, boolean columnFooter) {

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(template);
		ByteArrayOutputStream osWriter = new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(osWriter));

		// esportazione dei valori di testata
		if (columnHeader && factory.isMapped(COLUMN_HEADER)) {
			try {
				ByteArrayOutputStream osWriterHeader = new ByteArrayOutputStream();
				BufferedWriter writerHeader = new BufferedWriter(new OutputStreamWriter(osWriterHeader));
				BeanWriter outHeader = factory.createWriter(COLUMN_HEADER, writerHeader);

				outHeader.write(values.get(0));
				outHeader.flush();
				outHeader.close();

				osWriterHeader.writeTo(osWriter);
			} catch (IOException e) {
				logger.error("--> errore durante la creazione dell'header di rendicontazione", e);
				throw new RuntimeException("errore durante la creazione dell'header di rendicontazione", e);
			}
		}

		// esportazione dei valori del corpo
		BeanWriter out = factory.createWriter(getWriterName(template), writer);

		for (Object rendicontazioneBeanExporter : values) {
			out.write(rendicontazioneBeanExporter);
		}
		out.flush();
		out.close();

		// esportazione dei valori di piede
		if (columnFooter && factory.isMapped(COLUMN_FOOTER)) {
			try {
				ByteArrayOutputStream osWriterHeader = new ByteArrayOutputStream();
				BufferedWriter writerHeader = new BufferedWriter(new OutputStreamWriter(osWriterHeader));
				BeanWriter outHeader = factory.createWriter(COLUMN_FOOTER, writerHeader);

				outHeader.write(values.get(0));
				outHeader.flush();
				outHeader.close();

				osWriterHeader.writeTo(osWriter);
			} catch (IOException e) {
				logger.error("--> errore durante la creazione del footer di rendicontazione", e);
				throw new RuntimeException("errore durante la creazione del footer di rendicontazione", e);
			}
		}

		return osWriter.toByteArray();

	}

	/**
	 * In base al template specificato restituisce l'array dy byte che rappresenta i valori passati come parametro.
	 * 
	 * @param template
	 *            template da utilizzare
	 * @param values
	 *            valori da esportare
	 * @return flusso generato
	 */
	public byte[] write(String template, List<Object> values) {
		return write(template, false, values, false);
	}

}
