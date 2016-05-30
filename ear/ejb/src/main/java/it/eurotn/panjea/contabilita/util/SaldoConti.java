package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.contabilita.domain.Conto.TipoConto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Contiene i saldi di una serie di conti.<br/>
 * Utilizzata per poter gestire in blocco i saldi (ad esempio sommarli ad una lista di altri saldi).<br/>
 * 
 * 
 * @author giangi
 * 
 */
public class SaldoConti {
	private static final long serialVersionUID = 9043188233254802337L;

	private Map<String, SaldoConto> conti = new HashMap<String, SaldoConto>();

	/**
	 * @return lista di {@link SaldoConti}
	 */
	public List<SaldoConto> asList() {
		// Ordino i conti
		List<SaldoConto> result = new ArrayList<SaldoConto>();
		result.addAll(values());
		Collections.sort(result);
		return result;
	}

	public Set<Entry<String, SaldoConto>> entrySet() {
		return conti.entrySet();
	}

	public SaldoConto get(SaldoConto saldoConto) {
		return get(saldoConto.getSottoContoCodiceCompleto());
	}

	public SaldoConto get(String sottoContoCodiceCompleto) {
		return conti.get(sottoContoCodiceCompleto);
	}

	public void put(SaldoConto saldoConto) {
		conti.put(saldoConto.getSottoContoCodiceCompleto(), saldoConto);
	}

	public void remove(SaldoConto saldoConto) {
		remove(saldoConto.getSottoContoCodiceCompleto());
	}

	public void remove(String sottoContoCodiceCompleto) {
		conti.remove(sottoContoCodiceCompleto);
	}

	/**
	 * Per ogni {@link RigaContabile} crea {@link SaldoConto} e li somma a quelli presenti.<br/>
	 * Se il conto non è presente lo aggiunge ai saldi gestiti.<br/>
	 * Viene utilizzato quando trovo un documento di apertura o chiusura che contiene già il saldo del conto nelle sue
	 * righe
	 * 
	 * @param saldoConti
	 */
	public void sommaRigheContabili(List<RigaContabile> righeContabile) {
		// se nella lista ho dei conti uguali sommo i loro valori.
		for (RigaContabile rigaContabile : righeContabile) {
			SaldoConto saldoConto = new SaldoConto(rigaContabile);
			if (conti.containsKey(saldoConto.getSottoContoCodiceCompleto())) {
				SaldoConto saldoContoPresente = conti.get(saldoConto.getSottoContoCodiceCompleto());
				saldoConto.aggiungiImportoAvere(saldoContoPresente.getImportoAvere());
				saldoConto.aggiungiImportoDare(saldoContoPresente.getImportoDare());
			}
			conti.put(saldoConto.getSottoContoCodiceCompleto(), saldoConto);
		}
	}

	/**
	 * Somma la lista dei saldi passati come parametro.<br/>
	 * Se il conto non è presente lo aggiunge ai saldi gestiti.
	 * 
	 * @param saldoConti
	 */
	public void sommaSaldi(Collection<SaldoConto> saldoConti) {
		sommaSaldi(saldoConti, null);
	}

	/**
	 * Somma la lista dei saldi passati come parametro.<br/>
	 * Considera solamente i conti con la tipologia interessata<br/>
	 * Se il conto non è presente lo aggiunge ai saldi gestiti.
	 * 
	 * @param saldoConti
	 * @param tipoConto
	 *            tipoConto per filtrare i conti presenti in saldoConti
	 */
	public void sommaSaldi(Collection<SaldoConto> saldoConti, TipoConto tipoConto) {
		// se nella lista ho dei conti uguali sommo i loro valori.
		for (SaldoConto saldoConto : saldoConti) {
			if (tipoConto == null || (tipoConto != null && tipoConto.equals(saldoConto.getTipoConto()))) {
				if (conti.containsKey(saldoConto.getSottoContoCodiceCompleto())) {
					SaldoConto saldoContoPresente = conti.get(saldoConto.getSottoContoCodiceCompleto());
					saldoConto.aggiungiImportoAvere(saldoContoPresente.getImportoAvere());
					saldoConto.aggiungiImportoDare(saldoContoPresente.getImportoDare());
				}
				conti.put(saldoConto.getSottoContoCodiceCompleto(), saldoConto);
			}
		}
	}

	/**
	 * Somma tra due oggetti {@link SaldoConti}.
	 * 
	 * @param saldoConti
	 */
	public void sommaSaldi(SaldoConti saldoConti) {
		sommaSaldi(saldoConti.values());
	}

	public Collection<SaldoConto> values() {
		return conti.values();
	}
}
