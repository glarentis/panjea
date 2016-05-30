package it.eurotn.panjea.intra.manager.sezionigenerator;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;

public abstract class SezioneRigheBuilder<T extends RigaSezioneIntra> {

	private static Logger logger = Logger.getLogger(SezioneRigheBuilder.class);

	/**
	 * Metodo per eseguire eventuali calcoli sulle righe sezioni prima di salvarle.
	 * 
	 * @param rigaSezioneIntra
	 *            rigaSezioneIntra
	 * @return RigaSezioneIntra
	 */
	protected RigaSezioneIntra calcolaRigaSezione(RigaSezioneIntra rigaSezioneIntra) {
		return rigaSezioneIntra;
	}

	/**
	 * Aggiorna la massa della riga sezione 1.<br>
	 * L' aggiornamento dei valori viene eseguito secondo queste specifiche:<br>
	 * 
	 * Se esiste l'u.m. suppl. sulla nomenclatura e sono in una dichiarazione intra mensile, devo impostare la massa
	 * supplementare e non la massa.<br>
	 * 
	 * @param rigaSezione1
	 *            la riga sezione 1
	 * @return la RigaSezione1Intra con i relativi valori di massa e massa supplementare
	 */
	protected RigaSezione1Intra calcolaRigaSezione1(RigaSezione1Intra rigaSezione1) {
		// Nota che metto qui il metodo solo per evitare di duplicarlo perchè è utilizzato sia da sezione1acquisti che
		// sezione1cessioni

		String umOrigineArticolo = rigaSezione1.getUm();
		umOrigineArticolo = umOrigineArticolo.toLowerCase();
		String umDestinazione = "kg";
		Nomenclatura nomenclatura = rigaSezione1.getNomenclatura();
		UnitaMisura umSupplementare = nomenclatura.getUmsupplementare();

		if (umSupplementare != null) {
			umDestinazione = umSupplementare.getCodice();
			umDestinazione = umDestinazione.toLowerCase();
		}

		// se l'unità di misura/u.m. supplementare è quella della nomenclatura (di default kg), allora tengo la massa
		// netta come massa/massa supplementare
		Long massaNetta = rigaSezione1.getMassaNetta();
		if (massaNetta == null) {
			massaNetta = Long.valueOf(0);
		}
		BigDecimal massa = BigDecimal.valueOf(massaNetta);

		if (umSupplementare != null) {
			rigaSezione1.setMassaNetta(null);
			if (rigaSezione1.getDichiarazione().getTipoPeriodo().equals(TipoPeriodo.M)) {
				rigaSezione1.setMassaSupplementare(massa.longValue());
			}
		} else {
			rigaSezione1.setMassaNetta(massa.longValue());
			rigaSezione1.setMassaSupplementare(null);
		}

		return rigaSezione1;
	}

	/**
	 * Calcola il valore statistico per l'importo e la percentuale scelti.
	 * 
	 * @param importo
	 *            importo
	 * @param percValStat
	 *            percValStat
	 * @return importo + (importo * percentuale / 100)
	 */
	protected BigDecimal calcolaValoreStatistico(BigDecimal importo, Integer percValStat) {
		if (percValStat == null) {
			percValStat = 0;
		}
		BigDecimal valoreStatisticoEuro = importo.multiply(new BigDecimal(percValStat)).divide(Importo.HUNDRED);
		valoreStatisticoEuro = importo.add(valoreStatisticoEuro);
		return valoreStatisticoEuro;
	}

	/**
	 * 
	 * @param panjeaDAO
	 *            dao
	 * @param dichiarazioneIntra
	 *            dichiarazione per le righe
	 * @return Map<Long, RigaSezioneIntra>
	 */
	protected abstract Map<Long, RigaSezioneIntra> creaRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra);

	/**
	 * Cancella le righe della sezione dalla dichiarazione.
	 * 
	 * @param panjeaDAO
	 *            dao
	 * @param dichiarazioneIntra
	 *            dichiarazione dalla quale cancellare la sezione
	 * 
	 */
	protected void deleteRow(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		@SuppressWarnings("unchecked")
		Class<T> persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		StringBuilder sb = new StringBuilder("delete from ");
		sb.append(persistentClass.getName());
		sb.append(" r where r.dichiarazione.id=");
		sb.append(dichiarazioneIntra.getId());
		try {
			panjeaDAO.executeQuery(panjeaDAO.prepareQuery(sb.toString()));
		} catch (DAOException e) {
			logger.error("-->errore nel creare le righe per la sezione intra", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * genera e salva le righe per il tipo di sezione. Cancella eventuali righe presenti.
	 * 
	 * @param panjeaDAO
	 *            dao
	 * @param dichiarazioneIntra
	 *            dichiarazione per le righe
	 */
	public final void generaRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra) {
		// Cancello le righe già presenti
		deleteRow(panjeaDAO, dichiarazioneIntra);
		Map<Long, RigaSezioneIntra> righe = creaRighe(panjeaDAO, dichiarazioneIntra);
		salvaRighe(panjeaDAO, dichiarazioneIntra, righe);
	}

	/**
	 * Cicla per le righe, associa un progressivo, la dichiarazione e salva la riga.
	 * 
	 * @param panjeaDAO
	 *            dao
	 * @param dichiarazioneIntra
	 *            dichiarazione per le righe
	 * @param righe
	 *            le righe da salvare
	 */
	protected void salvaRighe(PanjeaDAO panjeaDAO, DichiarazioneIntra dichiarazioneIntra,
			Map<Long, RigaSezioneIntra> righe) {
		int progressivo = 1;
		for (RigaSezioneIntra rigaSezioneIntra : righe.values()) {
			rigaSezioneIntra.setProgressivo(progressivo++);
			rigaSezioneIntra.setDichiarazione(dichiarazioneIntra);

			rigaSezioneIntra = calcolaRigaSezione(rigaSezioneIntra);

			try {
				panjeaDAO.saveWithoutFlush(rigaSezioneIntra);
			} catch (DAOException e) {
				logger.error("-->errore nel salvare la rigaSezioneIntra1 ", e);
				throw new RuntimeException(e);
			}
		}
		panjeaDAO.getEntityManager().flush();
	}
}
