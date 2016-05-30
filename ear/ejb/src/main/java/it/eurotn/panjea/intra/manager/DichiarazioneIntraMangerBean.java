package it.eurotn.panjea.intra.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.panjea.intra.domain.DichiarazioneIntra.TipoDichiarazione;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.domain.RigaSezione1Intra;
import it.eurotn.panjea.intra.domain.RigaSezione2Intra;
import it.eurotn.panjea.intra.domain.RigaSezione3Intra;
import it.eurotn.panjea.intra.domain.RigaSezione4Intra;
import it.eurotn.panjea.intra.domain.RigaSezioneIntra;
import it.eurotn.panjea.intra.domain.TipoPeriodo;
import it.eurotn.panjea.intra.domain.TotaliDichiarazione;
import it.eurotn.panjea.intra.manager.interfaces.DichiarazioneIntraManager;
import it.eurotn.panjea.intra.manager.interfaces.IntraSettingsManager;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione1AcquistiBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione1CessioniBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione2AcquistiBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione2CessioniBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione3AcquistiBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione3CessioniBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione4AcquistiBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.Sezione4CessioniBuilder;
import it.eurotn.panjea.intra.manager.sezionigenerator.SezioneRigheBuilder;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.DichiarazioneIntraManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DichiarazioneIntraManager")
public class DichiarazioneIntraMangerBean implements DichiarazioneIntraManager {

	private static Logger logger = Logger.getLogger(DichiarazioneIntraMangerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private IntraSettingsManager intraSettingsManager;

	@SuppressWarnings("unchecked")
	@Override
	public TotaliDichiarazione calcolaTotaliDichiarazione(Integer id) {
		StringBuilder sb = new StringBuilder();
		sb.append("select riga.sezione, count(riga.id), sum(coalesce(riga.importo, 0)) from intr_righe_dichiarazioni riga ");
		sb.append("where riga.dichiarazione_id= ");
		sb.append(id);
		sb.append(" group by riga.sezione order by riga.sezione ");
		Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		List<Object[]> result = null;
		BigDecimal[][] totali = new BigDecimal[5][2];
		try {
			result = panjeaDAO.getResultList(query);
			for (Object[] totale : result) {
				Integer numSezione = (Integer) totale[0];
				BigDecimal numRighe = BigDecimal.valueOf(((BigInteger) totale[1]).doubleValue());
				BigDecimal importo = (BigDecimal) totale[2];
				importo = importo.setScale(0, RoundingMode.HALF_UP);
				totali[numSezione][0] = numRighe == null ? BigDecimal.ZERO : numRighe;
				totali[numSezione][1] = importo == null ? BigDecimal.ZERO : importo;
			}
		} catch (DAOException e) {
			logger.error("-->errore nel recuperare i totali della dichiarazione con id" + id, e);
			throw new RuntimeException("-->errore nel recuperare i totali della dichiarazione con id" + id, e);
		}
		return new TotaliDichiarazione(totali);
	}

	@Override
	public void cancellaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		StringBuilder sb = new StringBuilder("delete from RigaSezioneIntra r where r.dichiarazione.id=");
		sb.append(dichiarazioneIntra.getId());
		try {
			panjeaDAO.executeQuery(panjeaDAO.prepareQuery(sb.toString()));
		} catch (DAOException e) {
			logger.error("-->errore nel creare le righe per la sezione intra", e);
			throw new RuntimeException(e);
		}
		try {
			panjeaDAO.delete(dichiarazioneIntra);
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare la dichiarazione con id " + dichiarazioneIntra.getId(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void cancellaRigaSezioneDichiarazione(RigaSezioneIntra rigaSezioneIntra) {
		try {
			panjeaDAO.delete(rigaSezioneIntra);
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare una rigasezioneintra con id " + rigaSezioneIntra.getId(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public DichiarazioneIntra caricaDichiarazioneIntra(int id) {
		DichiarazioneIntra result = null;
		try {
			result = panjeaDAO.load(DichiarazioneIntra.class, id);
		} catch (ObjectNotFoundException e) {
			logger.error("-->dichiarazione intra non trovata. Id " + id, e);
			throw new RuntimeException("-->dichiarazione intra non trovata. Id " + id, e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DichiarazioneIntra> caricaDichiarazioniIntra() {
		Query query = panjeaDAO.prepareNamedQuery("DichiarazioneIntra.caricaDichiarazioni");
		List<DichiarazioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("-->errore nel caricare l'intra acquisti", e);
			throw new RuntimeException("-->errore nel caricare l'intra acquisti", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DichiarazioneIntra> caricaDichiarazioniIntraDaPresentare() {
		Query query = panjeaDAO.prepareNamedQuery("DichiarazioneIntra.caricaDichiarazioniDaPresentare");
		List<DichiarazioneIntra> result = null;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("-->errore nel caricare l'intra acquisti", e);
			throw new RuntimeException("-->errore nel caricare l'intra acquisti", e);
		}
		return result;
	}

	@Override
	public <T extends RigaSezioneIntra> List<T> caricaRigheSezioniDichiarazione(DichiarazioneIntra dichiarazioneIntra,
			Class<T> classeSezione) {
		StringBuilder sb = new StringBuilder("select s from ");
		sb.append(classeSezione.getSimpleName());
		sb.append(" s where s.class=" + classeSezione.getName());
		sb.append(" and s.dichiarazione=");
		sb.append(dichiarazioneIntra.getId());
		Query query = panjeaDAO.prepareQuery(sb.toString());
		@SuppressWarnings("unchecked")
		List<T> result = query.getResultList();
		return result;
	}

	/**
	 * Carica l'ultima dichiarazione intra creata indipendentemente del tipo dichiarazione scelto.
	 * 
	 * @return ultima dichiarazione intra. NULL se non ho dichiarazioni.
	 */
	private DichiarazioneIntra caricaUltimaDichiarazione() {
		Query query = panjeaDAO.prepareNamedQuery("DichiarazioneIntra.caricaUltimaDichiarazione");
		query.setMaxResults(1);
		try {
			DichiarazioneIntra dichiarazione = (DichiarazioneIntra) panjeaDAO.getSingleResult(query);
			return dichiarazione;
		} catch (ObjectNotFoundException e) {
			logger.debug("--> Nessuna dichiarazione intra presente");
			return null;
		} catch (Exception e) {
			logger.error("--> Errore nel caricare l'ultima dichiarazione intra presente ", e);
			throw new RuntimeException("--> Errore nel caricare l'ultima dichiarazione intra presente ", e);
		}
	}

	/**
	 * Carica l'ultima dichiarazione intra creata a seconda del tipo dichiarazione scelto.
	 * 
	 * @param tipoDichiarazione
	 *            tipo dichiarazione da caricare.
	 * @return ultima dichiarazione intra. NULL se non ho dichiarazioni.
	 */
	private DichiarazioneIntra caricaUltimaDichiarazione(TipoDichiarazione tipoDichiarazione) {
		Query query = panjeaDAO.prepareNamedQuery("DichiarazioneIntra.caricaDichiarazioniByClasse");
		query.setParameter("classeDichiarazione",
				tipoDichiarazione.getClasseDichiarazione().getAnnotation(DiscriminatorValue.class).value());
		query.setMaxResults(1);
		try {
			DichiarazioneIntra dichiarazione = (DichiarazioneIntra) panjeaDAO.getSingleResult(query);
			return dichiarazione;
		} catch (ObjectNotFoundException e) {
			logger.debug("--> Nessuna dichiarazione intra presente");
			return null;
		} catch (Exception e) {
			logger.error("--> Errore nel caricare l'ultima dichiarazione intra presente ", e);
			throw new RuntimeException("--> Errore nel caricare l'ultima dichiarazione intra presente ", e);
		}
	}

	@Override
	public DichiarazioneIntra compilaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		// Genero la dichiarazione
		if (dichiarazioneIntra.getTipoDichiarazione().equals(TipoDichiarazione.ACQUISTI)) {
			generaRigheSezioneIntra(new Sezione1AcquistiBuilder(), dichiarazioneIntra, RigaSezione1Intra.class);
			generaRigheSezioneIntra(new Sezione2AcquistiBuilder(), dichiarazioneIntra, RigaSezione2Intra.class);
			generaRigheSezioneIntra(new Sezione3AcquistiBuilder(), dichiarazioneIntra, RigaSezione3Intra.class);
			generaRigheSezioneIntra(new Sezione4AcquistiBuilder(), dichiarazioneIntra, RigaSezione4Intra.class);
		} else {
			generaRigheSezioneIntra(new Sezione1CessioniBuilder(), dichiarazioneIntra, RigaSezione1Intra.class);
			generaRigheSezioneIntra(new Sezione2CessioniBuilder(), dichiarazioneIntra, RigaSezione2Intra.class);
			generaRigheSezioneIntra(new Sezione3CessioniBuilder(), dichiarazioneIntra, RigaSezione3Intra.class);
			generaRigheSezioneIntra(new Sezione4CessioniBuilder(), dichiarazioneIntra, RigaSezione4Intra.class);
		}
		try {
			dichiarazioneIntra = panjeaDAO.load(DichiarazioneIntra.class, dichiarazioneIntra.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("Dichiarazione con id " + dichiarazioneIntra.getId() + " non trovata!", e);
			throw new RuntimeException(e);
		}

		return dichiarazioneIntra;
	}

	@Override
	public DichiarazioneIntra creaDichiarazioneIntra(TipoDichiarazione tipoDichiarazione) {
		IntraSettings intraSettings = intraSettingsManager.caricaIntraSettings();

		// carico l'ultima dichiarazione indipendentemente dal tipo
		DichiarazioneIntra ultimaDichiarazione = caricaUltimaDichiarazione();
		// e l'ultima per tipo, la quale mi interessa solo per recuperare il
		// mese/trimestre della dichiarazione
		DichiarazioneIntra ultimaDichiarazionePerTipo = caricaUltimaDichiarazione(tipoDichiarazione);
		DichiarazioneIntra nuovaDichiarazione = tipoDichiarazione.istanziaDichiarazione();
		if (ultimaDichiarazione != null) {
			// copio i dati dell'ultima dichiarazione sulla nuova;
			// Tolgo id,version e aumento codice
			nuovaDichiarazione.fillByDichiarazione(ultimaDichiarazione);
		}
		if (ultimaDichiarazionePerTipo != null) {
			ultimaDichiarazione.setAnno(ultimaDichiarazionePerTipo.getAnno());
			int month = ultimaDichiarazionePerTipo.getMese();
			int trimestre = ultimaDichiarazionePerTipo.getTrimestre();
			if (month > 0) {
				month++;
				nuovaDichiarazione.setMese(month);
			}
			if (trimestre > 0) {
				trimestre++;
				nuovaDichiarazione.setTrimestre(trimestre);
			}
		}
		nuovaDichiarazione.setPercValoreStatistico(intraSettings.getPercValoreStatistico());
		nuovaDichiarazione.setTipoPeriodo(intraSettings.getTipoPeriodo());

		return nuovaDichiarazione;

	}

	/**
	 * 
	 * @param builder
	 *            builder per la creazione della stringa hql
	 * @param dichiarazioneIntra
	 *            intestazione della dichiarazione intra
	 * @param classSezione
	 *            classe delle righe da generare e salvare
	 */
	private void generaRigheSezioneIntra(SezioneRigheBuilder<? extends RigaSezioneIntra> builder,
			DichiarazioneIntra dichiarazioneIntra, Class<? extends RigaSezioneIntra> classSezione) {
		// Se la dichiarazione non è stata ancora salvata la salvo
		if (dichiarazioneIntra.isNew()) {
			throw new IllegalArgumentException("la dichiarazione intra deve essere salvata.");
		}
		builder.generaRighe(panjeaDAO, dichiarazioneIntra);
	}

	@Override
	public DichiarazioneIntra salvaDichiarazioneIntra(DichiarazioneIntra dichiarazioneIntra) {
		try {
			if (dichiarazioneIntra.isNew()) {
				// Verifico che la dichiarazione sia univoca nel periodo
				StringBuilder sb = new StringBuilder("select count(d) from ");
				sb.append(dichiarazioneIntra.getClass().getName());
				sb.append(" d where d.anno=");
				sb.append(dichiarazioneIntra.getAnno());
				if (dichiarazioneIntra.getTipoPeriodo() == TipoPeriodo.M) {
					sb.append(" and d.mese=");
					sb.append(dichiarazioneIntra.getMese());
				}
				if (dichiarazioneIntra.getTipoPeriodo() == TipoPeriodo.T) {
					sb.append(" and d.trimestre=");
					sb.append(dichiarazioneIntra.getTrimestre());
				}
				Query query = panjeaDAO.prepareQuery(sb.toString());
				Long numDichiarazioni = (Long) panjeaDAO.getSingleResult(query);
				if (numDichiarazioni > 0) {
					throw new DuplicateKeyObjectException(new String[] { "anno", "mese", "trimestre" });
				}
			}
			return panjeaDAO.save(dichiarazioneIntra);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare la dichiarazione intra", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public RigaSezioneIntra salvaRigaSezioneDichiarazione(RigaSezioneIntra riga) {
		RigaSezioneIntra result = null;
		try {
			result = panjeaDAO.save(riga);
		} catch (DAOException e) {
			logger.error("-->errore nel caricare la riga sezione con id " + riga.getId(), e);
			throw new RuntimeException(e);
		}
		return result;
	}
}
