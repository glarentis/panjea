package it.eurotn.panjea.anagrafica.rich.editors.entita.builder;

import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.rich.bd.DatiGeograficiBD;
import it.eurotn.panjea.anagrafica.rich.bd.IDatiGeograficiBD;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.List;

import javax.xml.rpc.holders.BooleanHolder;
import javax.xml.rpc.holders.StringHolder;

import org.apache.axis.holders.DateHolder;
import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

public class AnagraficaWebBuilder {

	private static Logger logger = Logger.getLogger(AnagraficaWebBuilder.class);

	private IDatiGeograficiBD datiGeograficiBD;

	/**
	 * Costruttore.
	 */
	public AnagraficaWebBuilder() {
		super();
		this.datiGeograficiBD = RcpSupport.getBean(DatiGeograficiBD.BEAN_ID);
	}

	/**
	 * Crea un'anagrafica in base alla partita iva fornita.
	 *
	 * @param stato
	 *            sigla dello stato. se <code>null</code> lo stato prende il valiere di "IT"
	 * @param partitaIva
	 *            partita iva
	 * @return anagrafica creata
	 */
	public Anagrafica createAnagrafica(String stato, String partitaIva) {
		stato = stato == null ? "IT" : stato;
		return downloadWebInformation(stato, partitaIva);
	};

	/**
	 * Scarica attraverso il web service le informazioni della partita iva.
	 *
	 * @param stato
	 *            stato
	 * @param partitaIva
	 *            partita iva
	 * @return anagrafica creata
	 */
	private Anagrafica downloadWebInformation(String stato, String partitaIva) {

		Anagrafica anagraficaResult = new Anagrafica();

		CheckVatServiceLocator locator = new CheckVatServiceLocator();
		try {
			CheckVatPortType checkVatPortType = locator.getcheckVatPort();

			StringHolder address = new StringHolder();
			StringHolder name = new StringHolder();
			DateHolder requestDate = new DateHolder();
			StringHolder partitaIvaHolder = new StringHolder(partitaIva);
			StringHolder statoHolder = new StringHolder(stato);
			checkVatPortType.checkVat(statoHolder, partitaIvaHolder, requestDate, new BooleanHolder(), name, address);

			anagraficaResult = parseInformation(name, address, stato);
		} catch (Exception e) {
			logger.info("--> errore durante il caricamento delle informazioni dell'entità", e);
			PanjeaSwingUtil.checkAndThrowException(new GenericException(
					"Impossibile caricare le informazioni per la partita iva richiesta."));
		}

		return anagraficaResult;
	}

	private Anagrafica parseInformation(StringHolder name, StringHolder address, String stato) {
		Anagrafica anagrafica = new Anagrafica();

		anagrafica.setDenominazione(name.value);

		// faccio un primo split per separare la via dal resto
		String[] addressSplit = address.value.split("\\n");
		anagrafica.getSedeAnagrafica().setIndirizzo(addressSplit[0]);

		try {
			if ("IT".equals(stato)) {
				// faccio un ulteriore split per trovare cap città e provincia.
				// La stringa è composta così: CAP CITTA
				// PROVINCIA
				String[] datiGeograficiSplit = addressSplit[1].split(" ");

				Cap cap = new Cap();
				cap.setDescrizione(datiGeograficiSplit[0]);
				DatiGeografici datiGeografici = new DatiGeografici();
				datiGeografici.setCap(cap);
				List<Cap> caps = datiGeograficiBD.caricaCap(datiGeografici);
				datiGeografici.setCap(null);
				if (!caps.isEmpty()) {
					// aggiorno il cap
					datiGeografici.setCap(datiGeograficiBD.caricaCap(caps.get(0).getId()));

					Localita localita = new Localita();
					if (datiGeograficiSplit.length > 1) {
						localita.setDescrizione(datiGeograficiSplit[1]);
						datiGeografici.setLocalita(localita);
						List<Localita> localitas = datiGeograficiBD.caricaLocalita(datiGeografici);
						datiGeografici.setLocalita(null);
						if (!localitas.isEmpty()) {
							// aggiorno la località
							datiGeografici.setLocalita(datiGeograficiBD.caricaLocalita(localitas.get(0).getId()));
						}
					}
				}

				anagrafica.getSedeAnagrafica().setDatiGeografici(datiGeografici);
			}
		} catch (Exception e) {
			// non rilancio nessuna eccezione perchè non riesco solamente ad
			// ottenere i dati di città cap e provincia
			logger.info("--> Non riesco a recuperare i dati per l'anagrafica " + name.value);
		}

		return anagrafica;
	}

}
