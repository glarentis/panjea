package it.eurotn.panjea.documenti.util;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;

import java.io.Serializable;

public class EtichettaSpedizioneDocumentoDTO implements Serializable {

	private static final long serialVersionUID = 4836967398395727857L;

	private String destinatarioCap = null;
	private String destinatarioDenominazione = null;
	private String destinatarioDescrizione = null;
	private String destinatarioEmail = null;
	private String destinatarioFax = null;
	private String destinatarioIndirizzo = null;
	private String destinatarioLocalita = null;
	private String destinatarioPec = null;
	private String destinatarioProvincia = null;
	private String destinatarioTel = null;

	private DatiGeografici datiGeograficiDestinatario = null;

	/**
	 * Costruttore.
	 */
	public EtichettaSpedizioneDocumentoDTO() {
		super();
	}

	/**
	 * Costruttore.
	 *
	 * @param movimento
	 *            movimento di cui creare l'etichetta
	 */
	public EtichettaSpedizioneDocumentoDTO(final MovimentoEtichettaSpedizioneDTO movimento) {

		destinatarioDenominazione = movimento.getDocumento().getEntita().getAnagrafica().getDenominazione();
		if (movimento.getSedeSpedizione() != null && movimento.getSedeSpedizione().getId() != null) {
			datiGeograficiDestinatario = movimento.getSedeSpedizione().getSede().getDatiGeografici();
			destinatarioIndirizzo = movimento.getSedeSpedizione().getSede().getIndirizzo();
			destinatarioCap = movimento.getSedeSpedizione().getSede().getDatiGeografici().getDescrizioneCap();
			destinatarioLocalita = movimento.getSedeSpedizione().getSede().getDatiGeografici().getDescrizioneLocalita();
			destinatarioProvincia = movimento.getSedeSpedizione().getSede().getDatiGeografici().getSiglaProvincia();
			destinatarioFax = movimento.getSedeSpedizione().getSede().getFax();
			destinatarioTel = movimento.getSedeSpedizione().getSede().getTelefono();
			if (movimento.getSedeSpedizione().getTipoSede().getTipoSede() == TipoSede.SERVIZIO) {
				destinatarioDescrizione = movimento.getSedeSpedizione().getSede().getDescrizione();
			} else if (movimento.getSedeSpedizione().getTipoSede().getTipoSede() == TipoSede.INDIRIZZO_SPEDIZIONE) {
				destinatarioDenominazione = movimento.getSedeSpedizione().getSede().getDescrizione();
			}
			destinatarioEmail = movimento.getSedeSpedizione().getSede().getIndirizzoMail();
			destinatarioPec = movimento.getSedeSpedizione().getSede().getIndirizzoPEC();
		} else {
			SedeAnagrafica sedeAnagrafica = movimento.getDocumento().getEntita().getAnagrafica().getSedeAnagrafica();
			datiGeograficiDestinatario = sedeAnagrafica.getDatiGeografici();
			destinatarioIndirizzo = sedeAnagrafica.getIndirizzo();
			destinatarioCap = sedeAnagrafica.getDatiGeografici().getDescrizioneCap();
			destinatarioLocalita = sedeAnagrafica.getDatiGeografici().getDescrizioneLocalita();
			destinatarioProvincia = sedeAnagrafica.getDatiGeografici().getSiglaProvincia();
			destinatarioFax = movimento.getDocumento().getSedeEntita().getSede().getFax();
			destinatarioTel = movimento.getDocumento().getSedeEntita().getSede().getTelefono();
			destinatarioEmail = sedeAnagrafica.getIndirizzoMail();
			destinatarioPec = sedeAnagrafica.getIndirizzoPEC();
		}
	}

	/**
	 * @return the datiGeograficiDestinatario
	 */
	public DatiGeografici getDatiGeograficiDestinatario() {
		return datiGeograficiDestinatario;
	}

	/**
	 * @return the destinatarioCap
	 */
	public String getDestinatarioCap() {
		return destinatarioCap;
	}

	/**
	 * @return the destinatarioDenominazione
	 */
	public String getDestinatarioDenominazione() {
		return destinatarioDenominazione;
	}

	/**
	 * @return the destinatarioDescrizione
	 */
	public String getDestinatarioDescrizione() {
		return destinatarioDescrizione;
	}

	/**
	 * @return the destinatarioEmail
	 */
	public String getDestinatarioEmail() {
		return destinatarioEmail;
	}

	/**
	 * @return the destinatarioFax
	 */
	public String getDestinatarioFax() {
		return destinatarioFax;
	}

	/**
	 * @return the destinatarioIndirizzo
	 */
	public String getDestinatarioIndirizzo() {
		return destinatarioIndirizzo;
	}

	/**
	 * @return the destinatarioLocalita
	 */
	public String getDestinatarioLocalita() {
		return destinatarioLocalita;
	}

	/**
	 * @return the destinatarioPec
	 */
	public String getDestinatarioPec() {
		return destinatarioPec;
	}

	/**
	 * @return the destinatarioProvincia
	 */
	public String getDestinatarioProvincia() {
		return destinatarioProvincia;
	}

	/**
	 * @return the destinatarioTel
	 */
	public String getDestinatarioTel() {
		return destinatarioTel;
	}

}
