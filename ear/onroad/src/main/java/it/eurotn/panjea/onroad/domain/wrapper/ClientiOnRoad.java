package it.eurotn.panjea.onroad.domain.wrapper;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.onroad.domain.ClienteOnRoad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientiOnRoad implements Serializable {

	private static final long serialVersionUID = 9000763817239322477L;
	private List<ClienteOnRoad> clientiDuplicati;
	private List<Cliente> clienti;

	/**
	 * @param e
	 * @return
	 */
	public boolean addClienteDuplicato(ClienteOnRoad e) {
		return getClientiDuplicati().add(e);
	}

	/**
	 * @param e
	 * @return
	 */
	public boolean addClienteImportato(Cliente e) {
		return getClienti().add(e);
	}

	/**
	 * @return the clienti
	 */
	public List<Cliente> getClienti() {
		if (clienti == null) {
			clienti = new ArrayList<Cliente>();
		}
		return clienti;
	}

	/**
	 * @return the clientiDuplicati
	 */
	public List<ClienteOnRoad> getClientiDuplicati() {
		if (clientiDuplicati == null) {
			clientiDuplicati = new ArrayList<ClienteOnRoad>();
		}
		return clientiDuplicati;
	}

	/**
	 * @return la stringa che riporta le informazioni relative ai clienti onroad importati
	 */
	public String getLog() {
		StringBuilder sb = new StringBuilder();
		sb.append("Clienti importati: ");
		sb.append(getClienti().size());
		sb.append("\n");
		for (Cliente cliente : getClienti()) {
			sb.append("nuovo codice associato: ");
			sb.append(cliente.getCodice());
			sb.append(" - ");
			sb.append("ragione sociale: ");
			sb.append(cliente.getAnagrafica().getDenominazione());
			if (cliente.getAnagrafica().getPartiteIVA() != null && !cliente.getAnagrafica().getPartiteIVA().isEmpty()) {
				sb.append(" - ");
				sb.append("partita IVA: ");
				sb.append(cliente.getAnagrafica().getPartiteIVA());
			}
			if (cliente.getAnagrafica().getCodiceFiscale() != null
					&& !cliente.getAnagrafica().getCodiceFiscale().isEmpty()) {
				sb.append(" - ");
				sb.append("codice fiscale: ");
				sb.append(cliente.getAnagrafica().getCodiceFiscale());
			}
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("Clienti non importati perchè già presenti: ");
		sb.append(getClientiDuplicati().size());
		sb.append("\n");
		for (ClienteOnRoad cliente : getClientiDuplicati()) {
			sb.append("codice: ");
			sb.append(cliente.getCodiceCliente());
			sb.append(" - ");
			sb.append("ragione sociale: ");
			sb.append(cliente.getRagioneSociale());
			sb.append(cliente.getRagioneSociale1());
			if (cliente.getPartitaIva() != null && !cliente.getPartitaIva().isEmpty()) {
				sb.append(" - ");
				sb.append("partita IVA: ");
				sb.append(cliente.getPartitaIva());
			}
			if (cliente.getCodiceFiscale() != null && !cliente.getCodiceFiscale().isEmpty()) {
				sb.append(" - ");
				sb.append("codice fiscale: ");
				sb.append(cliente.getCodiceFiscale());
			}
			sb.append("\n");
		}
		sb.append("\n");

		return sb.toString();
	}

}
