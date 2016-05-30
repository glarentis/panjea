package it.eurotn.panjea.contabilita.manager.ivasospesa;

import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author  giangi
 * @version  1.0, 10/nov/2010
 */
public class IvaSospesaManagerCalculator {

	/**
	 * @uml.property  name="areaIva"
	 * @uml.associationEnd  
	 */
	private AreaIva areaIva;
	/**
	 * @uml.property  name="areaRate"
	 */
	private AreaRate areaRate;

	/**
	 * Gestisce il calcolo dei dati per trovare i risultati di iva sospesa.
	 */
	public IvaSospesaManagerCalculator() {
		super();
	}

	/**
	 * Calcola per ogni pagamento che rientra nel periodo i totali per codice iva.
	 * 
	 * @param dataInizioPeriodo
	 *            dataInizioPeriodo
	 * @param dataFinePeriodo
	 *            dataFinePeriodo
	 * @return List<TotaliCodiceIvaDTO>
	 */
	public List<TotaliCodiceIvaDTO> calcola(Date dataInizioPeriodo, Date dataFinePeriodo) {
		dataInizioPeriodo = PanjeaEJBUtil.getDateTimeToZero(dataInizioPeriodo);
		dataFinePeriodo = PanjeaEJBUtil.getDateTimeToZero(dataFinePeriodo);
		List<TotaliCodiceIvaDTO> codiciIva = new ArrayList<TotaliCodiceIvaDTO>();
		for (Pagamento pagamento : getPagamenti()) {
			Date dataPagamento = pagamento.getDataPagamento();
			if (dataPagamento.equals(dataInizioPeriodo)
					|| (dataPagamento.after(dataInizioPeriodo) && dataPagamento.before(dataFinePeriodo))
					|| dataPagamento.equals(dataFinePeriodo)) {
				IvaSospesaCalculatorStrategy strategy = new IvaSospesaCalculatorStrategy();
				IvaSospesaCalculator calculator = strategy.getCalculator(isUltimo(pagamento));
				List<TotaliCodiceIvaDTO> codiciIvaPagamento = calculator.calcola(pagamento, areaIva, areaRate);
				codiciIva.addAll(codiciIvaPagamento);
			}
		}
		return codiciIva;
	}

	/**
	 * @return  the areaIva
	 * @uml.property  name="areaIva"
	 */
	public AreaIva getAreaIva() {
		return areaIva;
	}

	/**
	 * @return  the areaRate
	 * @uml.property  name="areaRate"
	 */
	public AreaRate getAreaRate() {
		return areaRate;
	}

	/**
	 * @return restituisce la lista dei pagamenti dell'area rate
	 */
	private List<Pagamento> getPagamenti() {
		return areaRate.getPagamenti();
	}

	/**
	 * @param pagamento
	 *            il pagamento da verificare
	 * @return definisce se il pagamento e' l'ultimo dato che questo influisce sul calcolo dell'iva sospesa pagata; se
	 *         c'è un solo pagamento deve essere fatto il calcolo percentuale quindi ritorno di default false.
	 */
	private boolean isUltimo(Pagamento pagamento) {
		if (getPagamenti().size() == 1) {
			return false;
		} else {
			return getPagamenti().indexOf(pagamento) == getPagamenti().size() - 1;
		}
	}

	/**
	 * @param areaIva  the areaIva to set
	 * @uml.property  name="areaIva"
	 */
	public void setAreaIva(AreaIva areaIva) {
		this.areaIva = areaIva;
	}

	/**
	 * @param areaRate  the areaRate to set
	 * @uml.property  name="areaRate"
	 */
	public void setAreaRate(AreaRate areaRate) {
		this.areaRate = areaRate;
	}

}
