package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.jidesoft.converter.ConverterContext;

public class RigheContabiliTableModel extends DefaultBeanTableModel<RigaContabile> {
	private static final long serialVersionUID = 1288798852250285150L;

	private static Logger logger = Logger.getLogger(RigheContabiliTableModel.class);

	private static ConverterContext numberPrezzoContext;
	// per diferenciare se debo far vedere solo il codice o anche la descrizione(context= null)
	private static ConverterContext onlyCodiceConverter;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));
		onlyCodiceConverter = new ConverterContext("onlyCodice");
	}

	/**
	 * 
	 * @param id
	 *            id del tableModel
	 */
	public RigheContabiliTableModel(final String id) {
		super(id, new String[] { "importo", "contoDare", "contoAvere", "conto.descrizione", "note" },
				RigaContabile.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 0:
			return numberPrezzoContext;
		case 1:
			return onlyCodiceConverter;
		case 2:
			return onlyCodiceConverter;
		default:
			return super.getConverterContextAt(i, j);
		}
	}

	/**
	 * Calcola il saldo del conto in tabella.
	 * 
	 * @param sottoConto
	 *            il conto di cui calcolare il saldo
	 * @return il saldo o zero se non ci sonorighe contabili sul conto specificato
	 */
	public BigDecimal getSaldoRighe(SottoConto sottoConto) {
		logger.debug("--> Enter getSaldoRighe");
		BigDecimal saldo = BigDecimal.ZERO;
		// se ci sono righe contabili
		for (RigaContabile rigaContabile : getObjects()) {
			SottoConto contoDare = rigaContabile.getContoDare();
			SottoConto contoAvere = rigaContabile.getContoAvere();

			int sottoContoId = sottoConto.getId().intValue();

			// devo verificare se conto dare o avere per sapere se sommare o sottrarre
			if (contoDare != null && contoDare.getId().intValue() == sottoContoId) {
				logger.debug("--> trovato conto dare uguale a conto scelto, sommo importo riga");
				saldo = saldo.add(rigaContabile.getImporto());
			} else {
				if (contoAvere != null && contoAvere.getId().intValue() == sottoContoId) {
					logger.debug("--> trovato conto avere uguale a conto scelto, sottraggo importo riga");
					saldo = saldo.add(rigaContabile.getImporto().negate());
				}
			}
		}
		logger.debug("--> Exit getSaldoRighe " + saldo);
		return saldo;
	}

	/**
	 * 
	 * @return sbilancio fra dare e avere
	 */
	public BigDecimal getSbilancio() {
		return getTotaleDare().subtract(getTotaleAvere());
	}

	/**
	 * Restituisce il totale avere delle righe contabili associate all'areaContabile.
	 * 
	 * @return totale avere
	 */
	public BigDecimal getTotaleAvere() {
		logger.debug("--> Enter getTotaleAvere");
		BigDecimal totAvere = BigDecimal.ZERO;
		for (RigaContabile rigaContabile : getObjects()) {
			totAvere = totAvere.add(rigaContabile.getImportoAvere());
		}
		logger.debug("--> Exit getTotaleAvere");
		return totAvere;
	}

	/**
	 * Restituisce il totale dare delle righe contabili associate all'areaContabile.
	 * 
	 * @return totale dare
	 */
	public BigDecimal getTotaleDare() {
		logger.debug("--> Enter getTotaleDare");
		BigDecimal totDare = BigDecimal.ZERO;
		for (RigaContabile rigaContabile : this.getObjects()) {
			totDare = totDare.add(rigaContabile.getImportoDare());
		}
		logger.debug("--> Exit getTotaleDare");
		return totDare;
	}

	/**
	 * Verifica se le righe contabili sono quadrate (D-A=0).
	 * 
	 * @return true se lo sbilancio e' uguale a 0, false altrimenti
	 */
	public boolean isRigheQuadrate() {
		logger.debug("--> Enter isRigheQuadrate");
		boolean quadrate = false;
		quadrate = getSbilancio().compareTo(BigDecimal.ZERO) == 0;
		logger.debug("--> Exit isRigheQuadrate " + quadrate);
		return quadrate;
	}
}
