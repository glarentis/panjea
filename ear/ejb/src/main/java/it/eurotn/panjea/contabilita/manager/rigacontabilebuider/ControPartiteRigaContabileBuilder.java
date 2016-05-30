package it.eurotn.panjea.contabilita.manager.rigacontabilebuider;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControPartiteRigaContabileBuilder implements IRigaContabileBuilder {

	private AreaContabileManager areaContabileManager;

	/**
	 * Costruttore.
	 * 
	 * @param areaContabileManager
	 *            {@link AreaContabileManager}
	 */
	public ControPartiteRigaContabileBuilder(final AreaContabileManager areaContabileManager) {
		super();
		this.areaContabileManager = areaContabileManager;
	}

	@Override
	public List<RigaContabile> creaRigheContabili(StrutturaContabile strutturaContabile, AreaContabile areaContabile,
			Map<String, BigDecimal> mapSC, long ordinamentoRiga, List<ControPartita> controPartite,
			boolean rigaAutomatica) throws FormulaException, ContabilitaException, ContoEntitaAssenteException,
			ContoRapportoBancarioAssenteException {
		return creaRigheContabiliDaControPartite(areaContabile, controPartite, ordinamentoRiga);
	}

	/**
	 * Crea le righe contabili in base alla lista di contro partite.
	 * 
	 * @param areaContabile
	 *            areaContabile
	 * @param ordinamentoRiga
	 *            ordinamento da cui partire per generare le righe
	 * @param list
	 *            <code>List</code> di <code>ControPartita</code>
	 * 
	 * @return Righe contabili generate
	 */
	private List<RigaContabile> creaRigheContabiliDaControPartite(AreaContabile areaContabile,
			List<ControPartita> list, long ordinamentoRiga) {

		List<RigaContabile> righeResult = new ArrayList<RigaContabile>();

		Map<String, RigaContabile> map = new HashMap<String, RigaContabile>();
		List<String> sottoconti = new ArrayList<String>();
		String dareAvere = "";
		SottoConto sottoConto = null;
		String noteAdd = null;

		Collections.sort(list, new Comparator<ControPartita>() {

			@Override
			public int compare(ControPartita o1, ControPartita o2) {
				return o1.getOrdine().compareTo(o2.getOrdine());
			}
		});

		for (ControPartita controPartita : list) {

			switch (controPartita.getTipologiaContoControPartita()) {
			case SOTTOCONTO:
				if (controPartita.getAvere() != null && controPartita.getAvere().getId().intValue() != -1) {
					dareAvere = "A";
					sottoConto = controPartita.getAvere();
				} else {
					dareAvere = "D";
					sottoConto = controPartita.getDare();
				}
				break;
			case CONTO_BASE:
				if (controPartita.getContoBaseAvere() != null
						&& controPartita.getContoBaseAvere().getId().intValue() != -1) {
					dareAvere = "A";
					sottoConto = controPartita.getContoBaseAvere().getSottoConto();
				} else {
					dareAvere = "D";
					sottoConto = controPartita.getContoBaseDare().getSottoConto();
				}
				break;
			default:
				break;
			}

			noteAdd = null;
			if (controPartita.getNote() != null && controPartita.getNote().trim().length() > 0) {
				noteAdd = controPartita.getNote().trim();
			}

			if (areaContabile.getDocumento().getTipoDocumento().isNotaCreditoEnable()) {
				controPartita.setImporto(controPartita.getImporto().negate());
			}

			if (!map.containsKey(sottoConto.getSottoContoCodice() + dareAvere)) {
				RigaContabile rigaContabile = RigaContabile.creaRigaContabile(areaContabile, sottoConto,
						(dareAvere.equals("D")), controPartita.getImporto(), noteAdd, ordinamentoRiga++, false);
				map.put(sottoConto.getSottoContoCodice() + dareAvere, rigaContabile);
				sottoconti.add(sottoConto.getSottoContoCodice() + dareAvere);
			} else {
				RigaContabile rigaContabile = map.get(sottoConto.getSottoContoCodice() + dareAvere);
				rigaContabile.setImporto(rigaContabile.getImporto().add(controPartita.getImporto()));
				if (rigaContabile.getRigheCentroCosto().size() > 0) {
					RigaCentroCosto rigaCentroCostoEsistente = rigaContabile.getRigheCentroCosto().iterator().next();
					rigaCentroCostoEsistente.setImporto(rigaCentroCostoEsistente.getImporto().add(
							controPartita.getImporto()));
				}

				if (dareAvere.equals("D")) {
					rigaContabile.setContoDare(sottoConto);
				} else {
					rigaContabile.setContoAvere(sottoConto);
				}
				map.put(sottoConto.getSottoContoCodice() + dareAvere, rigaContabile);

			}
		}

		for (String chiave : sottoconti) {
			RigaContabile rigaContabileDaSalvare = map.get(chiave);

			// se il totale riga è negativo o se il tipo documento è uno storno devo girare il dare in avere e
			// viceversa. Viene invertito solo se una delle 2 condizioni è vera, per questo uso una xor.
			boolean totaleNegativo = (BigDecimal.ZERO.compareTo(rigaContabileDaSalvare.getImporto()) > 0);
			boolean storno = areaContabile.getDocumento().getTipoDocumento().isNotaCreditoEnable();
			if (totaleNegativo ^ storno) {
				if (dareAvere.equals("A")) {
					rigaContabileDaSalvare.setContoDare(rigaContabileDaSalvare.getContoAvere());
				} else {
					rigaContabileDaSalvare.setContoAvere(rigaContabileDaSalvare.getContoDare());
				}
			}
			// le righe contabili non possono avere importi negativi quindi dopo aver sistemato dare/avere faccio l'abs
			// dell'importo
			rigaContabileDaSalvare.setImporto(rigaContabileDaSalvare.getImporto().abs());
			if (rigaContabileDaSalvare.getRigheCentroCosto().size() > 0) {
				RigaCentroCosto rigaCentroCostoEsistente = rigaContabileDaSalvare.getRigheCentroCosto().iterator()
						.next();
				rigaCentroCostoEsistente.setImporto(rigaCentroCostoEsistente.getImporto().abs());
			}

			if (rigaContabileDaSalvare.getImporto().compareTo(BigDecimal.ZERO) != 0) {
				RigaContabile rigaContabileSalvata = areaContabileManager.salvaRigaContabile(map.get(chiave));
				righeResult.add(rigaContabileSalvata);
			}
		}

		return righeResult;
	}
}
