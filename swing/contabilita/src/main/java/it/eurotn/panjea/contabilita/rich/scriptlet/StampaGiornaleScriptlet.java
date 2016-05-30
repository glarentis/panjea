package it.eurotn.panjea.contabilita.rich.scriptlet;

import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import org.apache.log4j.Logger;

public class StampaGiornaleScriptlet extends JRDefaultScriptlet {

	private Map<AreaContabileDTO, Integer> mapAreeContabiliDTO = null;

	private Map<RigaContabileDTO, List<Integer>> mapRigheContabiliDTO = null;

	private Date ultimaDataMovimento = null;
	private Integer ultimoNumeroMovimento = null;
	private Integer ultimoNumeroPagina = null;
	private BigDecimal saldoDareAttuale = null;
	private BigDecimal saldoAvereAttuale = null;
	private static Logger logger = Logger.getLogger(StampaGiornaleScriptlet.class);

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public StampaGiornaleScriptlet() {
		super();
		this.mapAreeContabiliDTO = new HashMap<AreaContabileDTO, Integer>();
		this.mapRigheContabiliDTO = new HashMap<RigaContabileDTO, List<Integer>>();

		Calendar calendar = Calendar.getInstance();
		calendar.set(1900, 1, 1);
		this.ultimaDataMovimento = calendar.getTime();

		this.ultimoNumeroMovimento = 0;
		this.ultimoNumeroPagina = 0;

		this.saldoAvereAttuale = BigDecimal.ZERO;
		this.saldoDareAttuale = BigDecimal.ZERO;
	}

	@Override
	public void afterDetailEval() throws JRScriptletException {
		super.afterDetailEval();
		logger.debug("--> Enter afterDetailEval");

		Integer paginaCorrente = (Integer) getVariableValue("paginaCorrente");
		this.ultimoNumeroPagina = paginaCorrente;

		Integer numeroRiga = (Integer) getVariableValue("COLUMN_COUNT");
		logger.debug("--> numeroRiga = " + numeroRiga);

		Boolean isRiga = (Boolean) getFieldValue("rigaContabile");
		logger.debug("--> isRigaContabile = " + isRiga);

		Boolean isArea = (Boolean) getFieldValue("areaContabile");
		logger.debug("--> isAreaContabile = " + isArea);

		Boolean isFiller = (Boolean) getFieldValue("filler");

		RigaContabileDTO rigaContabileDTO = (RigaContabileDTO) getFieldValue("rigaCont");

		if (isArea) {
			logger.debug("--> AreaContabile");
			mapAreeContabiliDTO.put(rigaContabileDTO.getAreaContabileDTO(), paginaCorrente);
			this.ultimaDataMovimento = rigaContabileDTO.getAreaContabileDTO().getDataRegistrazione();
			this.ultimoNumeroMovimento = (Integer) getFieldValue("numeroRegistrazione");
		}

		if (isRiga && !isFiller) {
			if (!mapRigheContabiliDTO.containsKey(rigaContabileDTO)) {
				// incremento saldo dare e avere solo se ho una riga
				this.saldoDareAttuale = saldoDareAttuale
						.add((rigaContabileDTO.getImportoDare() != null) ? rigaContabileDTO.getImportoDare()
								: BigDecimal.ZERO);
				this.saldoAvereAttuale = saldoAvereAttuale
						.add((rigaContabileDTO.getImportoAvere() != null) ? rigaContabileDTO.getImportoAvere()
								: BigDecimal.ZERO);
			}

			logger.debug("--> RigaContabile");
			List<Integer> list = new ArrayList<Integer>();
			logger.debug("--> rigaContabileDTO.getImportoDare() " + rigaContabileDTO.getImportoDare());
			logger.debug("--> rigaContabileDTO.getImportoDare() " + rigaContabileDTO.getImportoAvere());
			list.add(paginaCorrente);
			list.add(numeroRiga);
			mapRigheContabiliDTO.put(rigaContabileDTO, list);
		}

		logger.debug("--> Exit afterDetailEval");
	}

	@Override
	public void beforeReportInit() throws JRScriptletException {
		super.beforeReportInit();

		BigDecimal saldoAvereGiornalePrecedente = (BigDecimal) getParameterValue("saldoAvereGiornalePrecedente");
		logger.debug("--> saldoAvereGiornalePrecedente = " + saldoAvereGiornalePrecedente);

		BigDecimal saldoDareGiornalePrecedente = (BigDecimal) getParameterValue("saldoDareGiornalePrecedente");
		logger.debug("--> saldoDareGiornalePrecedente = " + saldoDareGiornalePrecedente);

		// il saldo avere e dare attuali sono sempre da inizializzare a 0 dato
		// che le
		// righe del report vengono processate per 2 volte (motivo ignoto)
		// comunque il valore del giornale precedente viene settato alle
		// variabili del report saldoDareGiornalePrecedente e
		// saldoAvereGiornalePrecedente
		// this.saldoAvereAttuale = BigDecimal.ZERO;
		// this.saldoDareAttuale = BigDecimal.ZERO;
	}

	/**
	 * @return mapAreeContabiliDTO
	 */
	public Map<AreaContabileDTO, Integer> getMapAreeContabili() {
		return mapAreeContabiliDTO;
	}

	/**
	 * @return mapRigheContabiliDTO
	 */
	public Map<RigaContabileDTO, List<Integer>> getMapRigheContabili() {
		return mapRigheContabiliDTO;
	}

	/**
	 * @return saldoAvereAttuale
	 */
	public BigDecimal getSaldoAvereAttuale() {
		return saldoAvereAttuale;
	}

	/**
	 * @return saldoDareAttuale
	 */
	public BigDecimal getSaldoDareAttuale() {
		return saldoDareAttuale;
	}

	/**
	 * @return ultimaDataMovimento
	 */
	public Date getUltimaDataMovimento() {
		return ultimaDataMovimento;
	}

	/**
	 * @return ultimoNumeroMovimento
	 */
	public Integer getUltimoNumeroMovimento() {
		return ultimoNumeroMovimento;
	}

	/**
	 * @return ultimoNumeroPagina
	 */
	public Integer getUltimoNumeroPagina() {
		return ultimoNumeroPagina;
	}
}
