package it.eurotn.panjea.magazzino.rulesvalidation;

/**
 * Classe che contiene tutte le regole di validazione per la fatturazione.
 * 
 * @author fattazzo
 * 
 */
public class FatturazioneRulesChecker extends RigheRulesChecker {

	private static final long serialVersionUID = 7373013070793996216L;

	/**
	 * Costruttore di default.
	 */
	public FatturazioneRulesChecker() {
		super();
		addRules(new RigaMagazzinoPrezzoZeroRulesValidation());
		addRules(new RigaMagazzinoQuantitaZeroRulesValidation());
		addRules(new RigaMagazzinoProvvisoriaRulesValidation());
	}

}
