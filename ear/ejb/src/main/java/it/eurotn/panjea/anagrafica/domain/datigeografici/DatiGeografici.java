package it.eurotn.panjea.anagrafica.domain.datigeografici;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;

@Embeddable
public class DatiGeografici implements Serializable {

	private static final long serialVersionUID = 1714344809785700548L;

	@ManyToOne(optional = true)
	@JoinColumn(name = "nazione_id")
	private Nazione nazione;

	@ManyToOne(optional = true)
	@JoinColumn(name = "lvl1_id")
	private LivelloAmministrativo1 livelloAmministrativo1;

	@ManyToOne(optional = true)
	@JoinColumn(name = "lvl2_id")
	private LivelloAmministrativo2 livelloAmministrativo2;

	@ManyToOne(optional = true)
	@JoinColumn(name = "lvl3_id")
	private LivelloAmministrativo3 livelloAmministrativo3;

	@ManyToOne(optional = true)
	@JoinColumn(name = "lvl4_id")
	private LivelloAmministrativo4 livelloAmministrativo4;

	@ManyToOne(optional = true)
	@JoinColumn(name = "localita_id")
	private Localita localita;

	@ManyToOne(optional = true)
	@JoinColumn(name = "cap_localita_id")
	private Cap cap;

	/**
	 * @return the cap
	 */
	public Cap getCap() {
		return cap;
	}

	/**
	 * @return codice nazione
	 */
	public String getCodiceNazione() {
		String cod = "";
		if (nazione != null && nazione.getId() != null) {
			cod = nazione.getCodice();
		}
		return cod;
	}

	/**
	 * @return descrizione cap
	 */
	public String getDescrizioneCap() {
		String desc = "";
		if (cap != null) {
			desc = cap.getDescrizione();
		}
		return desc;
	}

	/**
	 * @return nome livello amministrativo 1
	 */
	public String getDescrizioneLivelloAmministrativo1() {
		String desc = "";
		if (hasLivelloAmministrativo1()) {
			desc = livelloAmministrativo1.getNome();
		}
		return desc;
	}

	/**
	 * @return nome livello amministrativo 2
	 */
	public String getDescrizioneLivelloAmministrativo2() {
		String desc = "";
		if (hasLivelloAmministrativo2()) {
			desc = livelloAmministrativo2.getNome();
		}
		return desc;
	}

	/**
	 * @return nome livello amministrativo 3
	 */
	public String getDescrizioneLivelloAmministrativo3() {
		String desc = "";
		if (hasLivelloAmministrativo3()) {
			desc = livelloAmministrativo3.getNome();
		}
		return desc;
	}

	/**
	 * @return nome livello amministrativo 4
	 */
	public String getDescrizioneLivelloAmministrativo4() {
		String desc = "";
		if (hasLivelloAmministrativo4()) {
			desc = livelloAmministrativo4.getNome();
		}
		return desc;
	}

	/**
	 * @return descrizione localita
	 */
	public String getDescrizioneLocalita() {
		String desc = "";
		if (localita != null && Hibernate.isInitialized(localita)) {
			desc = localita.getDescrizione();
		}
		return desc;
	}

	/**
	 * @return nome descrizione nazione
	 */
	public String getDescrizioneNazione() {
		String desc = "";
		if (nazione != null) {
			desc = nazione.getDescrizione();
		}
		return desc;
	}

	public String getIndirizzoLivelloAmministrativo() {
		String indirizzo = "";
		if (nazione != null && nazione.getMascheraIndirizzo() != null && !nazione.getMascheraIndirizzo().isEmpty()) {
			indirizzo = nazione.getMascheraIndirizzo();
			indirizzo = indirizzo.replaceAll(Nazione.LIVELLO1_PLACEHOLDER, getLivelloAmministrativo1() == null ? ""
					: StringUtils.defaultString(getLivelloAmministrativo1().getNome()));
			indirizzo = indirizzo.replaceAll(Nazione.LIVELLO2_SIGLA_PLACEHOLDER,
					getLivelloAmministrativo2() == null ? "" : StringUtils.defaultString(getLivelloAmministrativo2().getSigla()));
			indirizzo = indirizzo.replaceAll(Nazione.LIVELLO2_PLACEHOLDER, getLivelloAmministrativo2() == null ? ""
					: StringUtils.defaultString(getLivelloAmministrativo2().getNome()));
			indirizzo = indirizzo.replaceAll(Nazione.LIVELLO3_PLACEHOLDER, getLivelloAmministrativo3() == null ? ""
					: StringUtils.defaultString(getLivelloAmministrativo3().getNome()));
			indirizzo = indirizzo.replaceAll(Nazione.LIVELLO4_PLACEHOLDER, getLivelloAmministrativo4() == null ? ""
					: StringUtils.defaultString(getLivelloAmministrativo4().getNome()));
			indirizzo = indirizzo
					.replaceAll(Nazione.CAP_PLACEHOLDER, getCap() == null ? "" : StringUtils.defaultString(getCap().getDescrizione()));
			indirizzo = indirizzo.replaceAll(Nazione.LOCALITA_PLACEHOLDER, getLocalita() == null ? "" : StringUtils.defaultString(getLocalita()
					.getDescrizione()));
		}
		return indirizzo;
	}

	/**
	 * @return the livelloAmministrativo1
	 */
	public LivelloAmministrativo1 getLivelloAmministrativo1() {
		return livelloAmministrativo1;
	}

	/**
	 * @return the livelloAmministrativo2
	 */
	public LivelloAmministrativo2 getLivelloAmministrativo2() {
		return livelloAmministrativo2;
	}

	/**
	 * @return the livelloAmministrativo3
	 */
	public LivelloAmministrativo3 getLivelloAmministrativo3() {
		return livelloAmministrativo3;
	}

	/**
	 * @return the livelloAmministrativo4
	 */
	public LivelloAmministrativo4 getLivelloAmministrativo4() {
		return livelloAmministrativo4;
	}

	/**
	 * @return the localita
	 */
	public Localita getLocalita() {
		return localita;
	}

	/**
	 * @return the nazione
	 */
	public Nazione getNazione() {
		return nazione;
	}

	/**
	 * @return restituisce il numero di livelli disponibili per i dati geografici
	 */
	public int getNumeroLivelli() {
		int numeroLivelli = 0;
		if (hasLivelloAmministrativo1()) {
			numeroLivelli += 1;
		}
		if (hasLivelloAmministrativo2()) {
			numeroLivelli += 1;
		}
		if (hasLivelloAmministrativo3()) {
			numeroLivelli += 1;
		}
		if (hasLivelloAmministrativo4()) {
			numeroLivelli += 1;
		}
		return numeroLivelli;
	}

	/**
	 * @return la sigla del livello amministrativo 2 che equivale, per l'italia, alla provincia
	 */
	public String getSiglaProvincia() {
		String codice = "";
		if (hasLivelloAmministrativo2()) {
			codice = livelloAmministrativo2.getSigla();
		}
		return codice;
	}

	/**
	 * @return restituisce la suddivisione amministrativa più dettagliata presente per i dati geografici partendo dal
	 *         livello 4 fino al livello 1, null se non ci sono livelli amministrativi avvalorati.
	 */
	public SuddivisioneAmministrativa getUltimaSuddivisioneAmministrativa() {
		SuddivisioneAmministrativa suddivisioneAmministrativa = null;
		if (hasLivelloAmministrativo4()) {
			suddivisioneAmministrativa = livelloAmministrativo4;
		} else if (hasLivelloAmministrativo3()) {
			suddivisioneAmministrativa = livelloAmministrativo3;
		} else if (hasLivelloAmministrativo2()) {
			suddivisioneAmministrativa = livelloAmministrativo2;
		} else if (hasLivelloAmministrativo1()) {
			suddivisioneAmministrativa = livelloAmministrativo1;
		}
		return suddivisioneAmministrativa;
	}

	/**
	 *
	 * @return url per aprire google map
	 */
	public String getUrlMap() {
		StringBuffer address = new StringBuffer("");
		if (hasLivelloAmministrativo4()) {
			address.append(livelloAmministrativo4);
		} else if (hasLivelloAmministrativo3()) {
			address.append(" ");
			address.append(livelloAmministrativo1.getNome());
		} else if (hasLivelloAmministrativo2()) {
			address.append(" ");
			address.append(livelloAmministrativo1.getNome());
		} else if (hasLivelloAmministrativo1()) {
			address.append(" ");
			address.append(livelloAmministrativo1.getNome());
		}
		if (cap != null) {
			address.append(" ");
			address.append(cap.getDescrizione());
		}
		if (nazione != null) {
			address.append(" ");
			address.append(nazione.getCodice());
		}
		if (localita != null) {
			address.append(" ");
			address.append(localita.getDescrizione());
		}
		return address.toString();
	}

	/**
	 * @return se il livelloAmministrativo1 è valorizzato
	 */
	public boolean hasLivelloAmministrativo1() {
		return livelloAmministrativo1 != null && livelloAmministrativo1.getId() != null;
	}

	/**
	 * @return se il livelloAmministrativo2 è valorizzato
	 */
	public boolean hasLivelloAmministrativo2() {
		return livelloAmministrativo2 != null && livelloAmministrativo2.getId() != null;
	}

	/**
	 * @return se il livelloAmministrativo3 è valorizzato
	 */
	public boolean hasLivelloAmministrativo3() {
		return livelloAmministrativo3 != null && livelloAmministrativo3.getId() != null;
	}

	/**
	 * @return se il livelloAmministrativo4 è valorizzato
	 */
	public boolean hasLivelloAmministrativo4() {
		return livelloAmministrativo4 != null && livelloAmministrativo4.getId() != null;
	}

	/**
	 * @param cap
	 *            the cap to set
	 */
	public void setCap(Cap cap) {
		this.cap = cap;
	}

	/**
	 * Codice cap per la ricerca dei dati geografici, prepara un cap fittizio da cui viene letto il codice per la
	 * ricerca dei dati geografici.
	 *
	 * @param codiceCap
	 *            codiceCap
	 */
	public void setCodiceCap(String codiceCap) {
		Cap capFakePerRicerca = new Cap();
		capFakePerRicerca.setDescrizione(codiceCap);
		setCap(capFakePerRicerca);
	}

	/**
	 * Codice localita per la ricerca dei dati geografici, prepara una localita fittizia da cui viene letto il codice
	 * per la ricerca dei dati geografici.
	 *
	 * @param codiceLocalita
	 *            codiceLocalita
	 */
	public void setCodiceLocalita(String codiceLocalita) {
		Localita localitaFakePerRicerca = new Localita();
		localitaFakePerRicerca.setDescrizione(codiceLocalita);
		setLocalita(localitaFakePerRicerca);
	}

	/**
	 * Codice nazione per la ricerca dei dati geografici, prepara una nazione fittizia da cui viene letto il codice per
	 * la ricerca dei dati geografici.
	 *
	 * @param codiceNazione
	 *            codiceNazione
	 */
	public void setCodiceNazione(String codiceNazione) {
		Nazione nazioneFakePerRicerca = new Nazione();
		nazioneFakePerRicerca.setCodice(codiceNazione);
		setNazione(nazioneFakePerRicerca);
	}

	/**
	 * @param livelloAmministrativo1
	 *            the livelloAmministrativo1 to set
	 */
	public void setLivelloAmministrativo1(LivelloAmministrativo1 livelloAmministrativo1) {
		this.livelloAmministrativo1 = livelloAmministrativo1;
	}

	/**
	 * @param livelloAmministrativo2
	 *            the livelloAmministrativo2 to set
	 */
	public void setLivelloAmministrativo2(LivelloAmministrativo2 livelloAmministrativo2) {
		this.livelloAmministrativo2 = livelloAmministrativo2;
	}

	/**
	 * @param livelloAmministrativo3
	 *            the livelloAmministrativo3 to set
	 */
	public void setLivelloAmministrativo3(LivelloAmministrativo3 livelloAmministrativo3) {
		this.livelloAmministrativo3 = livelloAmministrativo3;
	}

	/**
	 * @param livelloAmministrativo4
	 *            the livelloAmministrativo4 to set
	 */
	public void setLivelloAmministrativo4(LivelloAmministrativo4 livelloAmministrativo4) {
		this.livelloAmministrativo4 = livelloAmministrativo4;
	}

	/**
	 * @param localita
	 *            the localita to set
	 */
	public void setLocalita(Localita localita) {
		this.localita = localita;
	}

	/**
	 * @param nazione
	 *            the nazione to set
	 */
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("DatiGeografici{");
		buffer.append(" [nazione=" + (nazione != null ? nazione.getCodice() : "n.d.") + "]");
		buffer.append(" [lvl1=" + (livelloAmministrativo1 != null ? livelloAmministrativo1.getNome() : "n.d.") + "]");
		buffer.append(" [lvl2=" + (livelloAmministrativo2 != null ? livelloAmministrativo2.getNome() : "n.d.") + "]");
		buffer.append(" [lvl3=" + (livelloAmministrativo3 != null ? livelloAmministrativo3.getNome() : "n.d.") + "]");
		buffer.append(" [lvl4=" + (livelloAmministrativo4 != null ? livelloAmministrativo4.getNome() : "n.d.") + "]");
		buffer.append("}");
		return buffer.toString();
	}

}
