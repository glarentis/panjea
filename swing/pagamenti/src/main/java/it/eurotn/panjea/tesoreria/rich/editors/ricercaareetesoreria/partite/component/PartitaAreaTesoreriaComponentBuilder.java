package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.component;

import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.domain.RigaAnticipo;
import javax.swing.JPanel;

public class PartitaAreaTesoreriaComponentBuilder {

	/**
	 * Restituisce il componente dell'effetto.
	 * 
	 * @param effetto
	 *            effetto di riferimento
	 * @return componente creato
	 */
	public JPanel getPartitaAreaTesoreriaComponent(Effetto effetto) {
		EffettoAreaTesoreriaComponent component = new EffettoAreaTesoreriaComponent(effetto);
		return component;
	}

	/**
	 * Restituisce il componente del pagamento.
	 * 
	 * @param pagamento
	 *            pagamento di riferimento
	 * @return componente creato
	 */
	public JPanel getPartitaAreaTesoreriaComponent(Pagamento pagamento) {
		PagamentoAreaTesoreriaComponent component = new PagamentoAreaTesoreriaComponent(pagamento);
		return component;
	}

	/**
	 * Restituisce il componente dell'anticipo.
	 * 
	 * @param anticipo
	 *            anticipo di riferimento
	 * @return componente creato
	 */
	public JPanel getPartitaAreaTesoreriaComponent(RigaAnticipo anticipo) {
		AnticipoAreaTesoreriaComponent component = new AnticipoAreaTesoreriaComponent(anticipo);
		return component;
	}
}
