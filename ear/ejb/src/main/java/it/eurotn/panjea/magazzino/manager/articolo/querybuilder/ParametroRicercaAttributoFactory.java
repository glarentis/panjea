package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributo;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributoBooleano;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributoNumerico;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributoStringa;

import java.util.HashMap;
import java.util.Map;

public final class ParametroRicercaAttributoFactory {

	private static Map<ETipoDatoTipoAttributo, ParametroRicercaAttributo> parametriTipoDato = new HashMap<TipoAttributo.ETipoDatoTipoAttributo, ParametroRicercaAttributo>();

	static {
		parametriTipoDato.put(ETipoDatoTipoAttributo.BOOLEANO, new ParametroRicercaAttributoBooleano());
		parametriTipoDato.put(ETipoDatoTipoAttributo.NUMERICO, new ParametroRicercaAttributoNumerico());
		parametriTipoDato.put(ETipoDatoTipoAttributo.STRINGA, new ParametroRicercaAttributoStringa());
	}

	/**
	 * 
	 * @param codice
	 *            codice attributo
	 * @param operatore
	 *            operatore per valore attributo
	 * @param valore
	 *            il valore dell'attributo
	 * @param tipoDato
	 *            il tipo di dato dell'attributo
	 * @return ParametroRicercaAttributo
	 */
	public static ParametroRicercaAttributo create(String codice, String operatore, String valore,
			ETipoDatoTipoAttributo tipoDato) {
		// ho i parametri in una map, l'oggetto che ritorna è sempre lo stesso quindi chiamo la newInstance per avere un
		// nuovo oggetto
		ParametroRicercaAttributo parametroRicercaAttributo = parametriTipoDato.get(tipoDato);
		if (parametroRicercaAttributo != null) {
			parametroRicercaAttributo = parametroRicercaAttributo.getNewInstance();

			parametroRicercaAttributo.setNome(codice);
			parametroRicercaAttributo.setOperatore(operatore);
			parametroRicercaAttributo.setValore(valore);
		}
		return parametroRicercaAttributo;
	}

	/**
	 * Costruttore.
	 */
	private ParametroRicercaAttributoFactory() {
		super();
	}

}
