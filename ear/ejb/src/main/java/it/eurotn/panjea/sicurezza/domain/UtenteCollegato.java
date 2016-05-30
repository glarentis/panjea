package it.eurotn.panjea.sicurezza.domain;

import java.util.Calendar;
import java.util.Date;

public class UtenteCollegato {
	private String nome;
	private String mcAddress;
	private String azienda;
	private Date ultimoAggiornamento;
	private Date dataConnessione;

	/**
	 *
	 * @param nome
	 *            nome utente
	 * @param mcAddress
	 *            macAddress
	 * @param azienda
	 *            azienda loggata
	 */
	public UtenteCollegato(final String nome, final String mcAddress, final String azienda) {
		super();
		this.nome = nome;
		this.mcAddress = mcAddress;
		this.azienda = azienda;
		this.dataConnessione = Calendar.getInstance().getTime();
		this.ultimoAggiornamento = dataConnessione;
	}

	/**
	 *
	 */
	public void checkAlive() {
		ultimoAggiornamento = Calendar.getInstance().getTime();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UtenteCollegato other = (UtenteCollegato) obj;
		if (azienda == null) {
			if (other.azienda != null) {
				return false;
			}
		} else if (!azienda.equals(other.azienda)) {
			return false;
		}
		if (mcAddress == null) {
			if (other.mcAddress != null) {
				return false;
			}
		} else if (!mcAddress.equals(other.mcAddress)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		return true;
	}

	/**
	 * @return Returns the azienda.
	 */
	public String getAzienda() {
		return azienda;
	}

	/**
	 * @return Returns the mcAddress.
	 */
	public String getMcAddress() {
		return mcAddress;
	}

	/**
	 *
	 * @return minuti di connessione
	 */
	public long getMinutiConnessione() {
		long diff = Calendar.getInstance().getTime().getTime() - dataConnessione.getTime();
		return diff / (60 * 1000) % 60;
	}

	/**
	 *
	 * @return minuti passati dall'ultimo aggiornamento dello stato
	 */
	public long getMinutiUltimoAggiornamento() {
		long diff = Calendar.getInstance().getTime().getTime() - ultimoAggiornamento.getTime();
		return diff / (60 * 1000) % 60;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((azienda == null) ? 0 : azienda.hashCode());
		result = prime * result + ((mcAddress == null) ? 0 : mcAddress.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return nome + "," + mcAddress + "," + azienda + "," + getMinutiConnessione() + ","
				+ getMinutiUltimoAggiornamento();
	}
}
