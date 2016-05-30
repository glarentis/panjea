package it.eurotn.panjea.magazzino.manager.export.typehandler;

import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;

public class AttributoArticoloTypeHandler implements TypeHandler {

	private static Logger logger = Logger.getLogger(AttributoArticoloTypeHandler.class);

	private DefaultNumberFormatterFactory f = new DefaultNumberFormatterFactory("#,##0", 4, Double.class);

	private String nome;

	private String decimalSeparator;

	private String pattern;

	@Override
	public String format(Object obj) {

		String result = "";

		@SuppressWarnings("unchecked")
		List<AttributoArticolo> attributi = (List<AttributoArticolo>) obj;

		if (attributi != null) {
			for (AttributoArticolo attributoArticolo : attributi) {

				// cerco l'attributo che corrisponde al nome settato
				if (attributoArticolo.getTipoAttributo().getCodice().equals(nome)) {

					switch (attributoArticolo.getTipoAttributo().getTipoDato()) {
					case STRINGA:
						result = attributoArticolo.getValore();
						break;
					case NUMERICO:
						Double object = 0.0;
						try {
							object = (Double) f.getDefaultFormatter().stringToValue(attributoArticolo.getValore());
						} catch (ParseException e) {
							logger.error("-->errore durante la conversione del valore attributo numerico", e);
						}

						result = object.toString();
						if (pattern != null) {
							result = new DecimalFormat(pattern).format(object);
						}

						if (decimalSeparator != null) {
							result = result.replaceAll(",", decimalSeparator);
						}
						break;
					case BOOLEANO:
						Boolean bool = new Boolean(attributoArticolo.getValore());
						result = bool ? "S" : "N";
						break;
					default:
						break;
					}
					break;
				}
			}
		}

		return result;
	}

	/**
	 * @return Returns the decimalSeparator.
	 */
	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the pattern.
	 */
	public String getPattern() {
		return pattern;
	}

	@Override
	public Class<?> getType() {
		return List.class;
	}

	@Override
	public Object parse(String s) throws TypeConversionException {
		return null;
	}

	/**
	 * @param decimalSeparator
	 *            The decimalSeparator to set.
	 */
	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param pattern
	 *            The pattern to set.
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
