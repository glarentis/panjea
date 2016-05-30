package it.eurotn.panjea.magazzino.manager.export.exporter.sisa;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneBeanExporter;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneWriter;
import it.eurotn.panjea.magazzino.manager.export.exporter.interfaces.RendicontazioneExporter;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RendicontazioneExporter.Sisa")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RendicontazioneExporter.Sisa")
public class SisaRendicontazioneExporterBean implements RendicontazioneExporter {

	private static Logger logger = Logger.getLogger(SisaRendicontazioneExporterBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	/**
	 * Crea il flusso per le aree magazzino e tipo esportazione passate come parametro.
	 * 
	 * @param areeMagazzino
	 *            aree
	 * @param tipoEsportazione
	 *            tipo esportazione
	 * @return flusso creato
	 */
	private byte[] creaFlusso(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione) {

		List<Object> rendicontazioneBeans = new ArrayList<Object>();
		for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {
			AreaMagazzino areaMagazzinoLoad;
			try {
				areaMagazzinoLoad = panjeaDAO.load(AreaMagazzino.class, areaMagazzinoRicerca.getIdAreaMagazzino());

				for (RigaMagazzino rigaMagazzino : areaMagazzinoLoad.getRigheMagazzino()) {
					if (rigaMagazzino instanceof RigaArticolo) {

						rendicontazioneBeans.add(new RendicontazioneBeanExporter(null, (RigaArticolo) rigaMagazzino));
					}
				}
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dell'area magazzino.", e);
				throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e);
			}
		}

		RendicontazioneWriter writer = new RendicontazioneWriter();
		byte[] flusso = writer.write(tipoEsportazione.getTemplate(), rendicontazioneBeans);

		return flusso;
	}

	@Override
	public List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) {

		List<byte[]> flussi = new ArrayList<byte[]>();

		Map<SedeEntita, List<AreaMagazzinoRicerca>> mapAree = new HashMap<SedeEntita, List<AreaMagazzinoRicerca>>();

		// raggruppo per sede entita perchè devo fare un flusso per ogni sede
		for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {
			List<AreaMagazzinoRicerca> areeSede = mapAree.get(areaMagazzinoRicerca.getSedeEntita());

			if (areeSede == null) {
				areeSede = new ArrayList<AreaMagazzinoRicerca>();
			}
			areeSede.add(areaMagazzinoRicerca);
			mapAree.put(areaMagazzinoRicerca.getSedeEntita(), areeSede);
		}

		for (Entry<SedeEntita, List<AreaMagazzinoRicerca>> entry : mapAree.entrySet()) {

			byte[] flusso = creaFlusso(entry.getValue(), tipoEsportazione);
			flussi.add(flusso);
		}

		return flussi;
	}

}
