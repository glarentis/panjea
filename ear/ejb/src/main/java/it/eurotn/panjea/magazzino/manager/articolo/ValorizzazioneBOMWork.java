package it.eurotn.panjea.magazzino.manager.articolo;

import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.Moltiplicatore;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

import commonj.work.Work;

public class ValorizzazioneBOMWork implements Work {

	private Bom bom;
	private Map<ArticoloConfigurazioneKey, Bom> bomCalcolate;
	private ArticoloValorizzazioneBOM articoloValorizzazioneBOM;

	/**
	 *
	 * @param bom
	 *            bom da valorizzazione
	 * @param bomCalcolate
	 *            bom calcolate
	 * @param articoloValorizzazioneBOM
	 *            articolo con i dati di anagrafica
	 */
	public ValorizzazioneBOMWork(final Bom bom, final Map<ArticoloConfigurazioneKey, Bom> bomCalcolate,
			final ArticoloValorizzazioneBOM articoloValorizzazioneBOM) {
		this.bom = bom;
		this.bomCalcolate = bomCalcolate;
		this.articoloValorizzazioneBOM = articoloValorizzazioneBOM;
	}

	/**
	 * @return Returns the bom.
	 */
	public Bom getBom() {
		return bom;
	}

	@Override
	public boolean isDaemon() {
		return false;
	}

	@Override
	public void release() {
	}

	@Override
	public void run() {
		Moltiplicatore moltiplicatoreCalculator = new Moltiplicatore();
		int numeroDecimali = 4;// articoloValorizzazioneBOM == null ? 0 :
		// articoloValorizzazioneBOM.getNumDecimaliPrezzo();
		MathContext mathContext = new MathContext(numeroDecimali);

		for (Bom figlio : bom.getFigli()) {
			BigDecimal moltiplicatore = moltiplicatoreCalculator.calcola(figlio.getFormulaMolt(), BigDecimal.ONE,
					numeroDecimali, articoloValorizzazioneBOM.getCodiciAttributo(),
					articoloValorizzazioneBOM.getValoriAttributo());
			Bom bomFiglio = bomCalcolate.get(new ArticoloConfigurazioneKey(figlio.getIdArticolo(), null));
			BigDecimal costoFiglio = bomFiglio.getCosto();
			figlio.setCosto(bomFiglio.getCosto());
			costoFiglio = costoFiglio.multiply(moltiplicatore, mathContext);
			figlio.setMoltiplicatore(moltiplicatore);
			figlio.addFigli(bomFiglio.getFigli());
			bom.setCosto(bom.getCosto().add(costoFiglio));
			// bom.setMoltiplicatore(moltiplicatore);
		}
	}
}
