package it.eurotn.panjea.intra.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.manager.datigeografici.interfaces.DatiGeograficiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.intra.domain.ModalitaErogazione;
import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.panjea.intra.manager.interfaces.ServizioManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ServizioManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ServizioManager")
public class ServizioManagerBean implements ServizioManager {

	private static Logger logger = Logger.getLogger(ServizioManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private DatiGeograficiManager datiGeograficiManager;

	@EJB
	private AnagraficaTabelleManager anagraficaTabelleManager;

	@SuppressWarnings("unchecked")
	@Override
	public String associaNomenclatura(byte[] file) {
		File fileTmp = null;
		BufferedReader br = null;
		StringBuilder sbResult = new StringBuilder("-- RISULTATI IMPORTAZIONE --\n");
		StringBuilder sbError = new StringBuilder("");
		try {
			fileTmp = File.createTempFile("assnomenclature", "txt");
			FileOutputStream outputStream = new FileOutputStream(fileTmp);
			outputStream.write(file);
			outputStream.flush();
			outputStream.close();
			FileInputStream fstream = new FileInputStream(fileTmp);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			Query query = panjeaDAO.prepareNamedQuery("Articolo.caricaByCodice");

			List<Nomenclatura> nomenclature = (List<Nomenclatura>) caricaServizi(Nomenclatura.class, "codice", null);
			Map<String, Nomenclatura> nomenclatureMap = new HashMap<String, Nomenclatura>();
			for (Nomenclatura nomenclatura : nomenclature) {
				nomenclatureMap.put(nomenclatura.getCodice(), nomenclatura);
			}

			List<Nazione> nazioni = datiGeograficiManager.caricaNazioni(null);
			Map<String, Nazione> nazioniMap = new HashMap<String, Nazione>();
			for (Nazione nazione : nazioni) {
				nazioniMap.put(nazione.getCodice(), nazione);
			}

			while ((strLine = br.readLine()) != null) {
				String[] dati = strLine.split(",");
				String codiceArticolo = dati[0];
				query.setParameter("codice", codiceArticolo);
				List<Articolo> articoli = panjeaDAO.getResultList(query);
				if (articoli.size() == 0) {
					sbError.append("Articolo ");
					sbError.append(codiceArticolo);
					sbError.append(" non trovato\n");
					continue;
				}
				Articolo articolo = articoli.get(0);

				String codiceNomenclatura = dati[1];
				if (!nomenclatureMap.containsKey(codiceNomenclatura)) {
					sbError.append("Nomenclatura ");
					sbError.append(codiceNomenclatura);
					sbError.append(" non trovata\n");
				} else {
					articolo.getDatiIntra().setServizio(nomenclatureMap.get(codiceNomenclatura));
				}

				if (dati.length >= 3) {
					String massaNettaString = dati[2];
					if (!StringUtils.isNumeric(massaNettaString)) {
						sbError.append("Massa netta per l'articolo ");
						sbError.append(articolo.getCodice());
						sbError.append(" non intera.Valore trovato:");
						sbError.append(dati[2]);
						sbError.append("\n");
					} else if (!massaNettaString.trim().isEmpty()) {
						articolo.getDatiIntra().setMassaNetta(new BigDecimal(massaNettaString));
					}
				}
				if (dati.length >= 4) {
					String nazioneProvenienza = dati[3];
					if (!nazioniMap.containsKey(nazioneProvenienza)) {
						sbError.append("Nazione ");
						sbError.append(nazioneProvenienza);
						sbError.append(" non trovata\n");
					} else {
						articolo.getDatiIntra().setNazione(nazioniMap.get(nazioneProvenienza));
					}
				}

				if (dati.length >= 5) {
					String modalitaErogazione = dati[4];
					if ("R".equals(modalitaErogazione)) {
						articolo.getDatiIntra().setModalitaErogazione(ModalitaErogazione.RIPETUTA);
					} else if ("I".equals(modalitaErogazione)) {
						articolo.getDatiIntra().setModalitaErogazione(ModalitaErogazione.ISTANTANEA);
					} else {
						sbError.append("Modalità di erogazione non corretta per l'articolo (solo I o R) ");
						sbError.append(articolo.getCodice());
						sbError.append(" valore:");
						sbError.append(modalitaErogazione);
						sbError.append(" \n");
					}

				}
				panjeaDAO.save(articolo);
			}
		} catch (IOException e) {
			throw new RuntimeException("Errore nel creare il file temporaneo per l'importazione delle nomenclature", e);
		} catch (DAOException e) {
			throw new RuntimeException("Errore nel salvare la nomenclatura", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.error("-->errore nel chiudere il reader", e);
				}
			}
			if (fileTmp != null) {
				fileTmp.delete();
			}
		}
		if (sbError.length() == 0) {
			sbResult.append("importazione avvenuta senza errori");
		} else {
			sbResult.append(sbError.toString());
		}
		return sbResult.toString();
	}

	@Override
	public void cancellaServizio(Servizio servizio) {
		try {
			panjeaDAO.delete(servizio);
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare il servizio " + servizio, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<?> caricaServizi(Class<?> classServizio, String fieldSearch, String filtro) {
		StringBuilder sb = new StringBuilder();
		if (classServizio == null) {
			sb.append("select s from Servizio s where ");

			logger.debug("Carica servizi e beni");
		} else if (Nomenclatura.class.equals(classServizio)) {
			sb.append("select s from Nomenclatura s left join fetch s.umsupplementare where ");
			logger.debug("Carica beni");
		} else if (Servizio.class.equals(classServizio)) {
			sb.append("select s from Servizio s where s.class!=it.eurotn.panjea.intra.domain.Nomenclatura and ");
			logger.debug("Carica servizi");
		} else {
			throw new UnsupportedOperationException("Classe specificata non valida per il caricamento dei servizi");
		}
		if (filtro == null) {
			filtro = "%";
		}
		sb.append(" s.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(filtro));
		sb.append(" order by s.").append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		@SuppressWarnings("unchecked")
		List<Servizio> result = query.getResultList();
		return result;
	}

	@Override
	public Servizio caricaServizio(Servizio servizio) {
		try {
			servizio = panjeaDAO.load(Servizio.class, servizio.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("-->errore. Servizio non trovato (ricerca con id)." + servizio, e);
			throw new RuntimeException(e);
		}
		return servizio;
	}

	@Override
	public void importaNomenclatura(byte[] file) {
		File fileTmp = null;
		try {
			fileTmp = File.createTempFile("nomenclature", "txt");
			FileOutputStream outputStream = new FileOutputStream(fileTmp);
			outputStream.write(file);
			outputStream.flush();
			outputStream.close();
			FileInputStream fstream = new FileInputStream(fileTmp);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			@SuppressWarnings("unchecked")
			List<Nomenclatura> nomenclature = (List<Nomenclatura>) caricaServizi(Nomenclatura.class, "codice", null);
			Map<String, Nomenclatura> nomenclatureMap = new HashMap<String, Nomenclatura>();
			for (Nomenclatura nomenclatura : nomenclature) {
				nomenclatureMap.put(nomenclatura.getCodice(), nomenclatura);
			}
			while ((strLine = br.readLine()) != null) {
				// il file è composto da codice - descrizione - umsupplementare
				String[] dati = strLine.split("#");
				Nomenclatura nomenclatura = new Nomenclatura();
				if (!StringUtils.isNumeric(dati[0])) {
					continue;
				}
				nomenclatura.setCodice(dati[0]);
				if (nomenclatureMap.containsKey(nomenclatura.getCodice())) {
					nomenclatura = nomenclatureMap.get(nomenclatura.getCodice());
				}

				if (dati.length >= 2) {
					nomenclatura.setDescrizione(dati[1]);
				}

				if (dati.length == 3) {
					UnitaMisura unitaMisura = anagraficaTabelleManager.caricaUnitaMisuraByCodice(StringUtils.substring(
							dati[2], 0, 15));
					if (unitaMisura == null) {
						unitaMisura = new UnitaMisura();
						unitaMisura.setCodice(dati[2]);
						unitaMisura.setIntra(Boolean.TRUE);
						unitaMisura = anagraficaTabelleManager.salvaUnitaMisura(unitaMisura);
					}
					nomenclatura.setUmsupplementare(unitaMisura);
				}
				panjeaDAO.save(nomenclatura);
			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("Errore nel creare il file temporaneo per l'importazione delle nomenclature", e);
		} catch (DAOException e) {
			throw new RuntimeException("Errore nel salvare la nomenclatura", e);
		} finally {
			if (fileTmp != null) {
				fileTmp.delete();
			}
		}

	}

	@Override
	public void importaServizi(byte[] file) {
		File fileTmp = null;
		try {
			fileTmp = File.createTempFile("nomenclature", "txt");
			FileOutputStream outputStream = new FileOutputStream(fileTmp);
			outputStream.write(file);
			outputStream.flush();
			outputStream.close();
			FileInputStream fstream = new FileInputStream(fileTmp);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			@SuppressWarnings("unchecked")
			List<Servizio> servizi = (List<Servizio>) caricaServizi(Servizio.class, "codice", null);
			Map<String, Servizio> serviziMap = new HashMap<String, Servizio>();
			for (Servizio servizio : servizi) {
				serviziMap.put(servizio.getCodice(), servizio);
			}

			while ((strLine = br.readLine()) != null) {
				String[] dati = strLine.split("#");
				Servizio servizio = new Servizio();
				servizio.setCodice(dati[0]);
				if (serviziMap.containsKey(servizio.getCodice())) {
					servizio = serviziMap.get(servizio.getCodice());
				}
				if (dati.length >= 2) {
					servizio.setDescrizione(dati[1]);
				}
				panjeaDAO.save(servizio);
			}
			br.close();
		} catch (IOException e) {
			throw new RuntimeException("Errore nel creare il file temporaneo per l'importazione delle nomenclature", e);
		} catch (DAOException e) {
			throw new RuntimeException("Errore nel salvare la nomenclatura", e);
		} finally {
			if (fileTmp != null) {
				fileTmp.delete();
			}
		}

	}

	@Override
	public Servizio salvaServizio(Servizio servizio) {
		try {
			servizio = panjeaDAO.save(servizio);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare il servizio " + servizio, e);
			throw new RuntimeException(e);
		}
		return servizio;
	}

}
