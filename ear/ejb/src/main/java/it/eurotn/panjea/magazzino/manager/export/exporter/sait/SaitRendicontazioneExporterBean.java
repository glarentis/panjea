package it.eurotn.panjea.magazzino.manager.export.exporter.sait;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneWriter;
import it.eurotn.panjea.magazzino.manager.export.exporter.interfaces.RendicontazioneExporter;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.CodiceArticoloEntitaManager;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RendicontazioneExporter.Sait")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RendicontazioneExporter.Sait")
public class SaitRendicontazioneExporterBean implements RendicontazioneExporter {

	private static Logger logger = Logger.getLogger(SaitRendicontazioneExporterBean.class);

	@EJB
	private CodiceArticoloEntitaManager codiceArticoloEntitaManager;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) {

		int progressivo = 0;

		Integer idAreaMagazzino = 0;
		Integer idAreaMagazzinoCollegata = null;

		List<Object> rendicontazioneBeans = new ArrayList<Object>();
		for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {
			AreaMagazzino areaMagazzinoLoad;
			try {
				areaMagazzinoLoad = panjeaDAO.load(AreaMagazzino.class, areaMagazzinoRicerca.getIdAreaMagazzino());

				for (RigaMagazzino rigaMagazzino : areaMagazzinoLoad.getRigheMagazzino()) {

					// se cambia l'area magazzino/area collegata inserisco nella lista l'area
					if (idAreaMagazzino != rigaMagazzino.getAreaMagazzino().getId()
							|| (rigaMagazzino.getAreaCollegata() != null && idAreaMagazzinoCollegata != rigaMagazzino
									.getAreaCollegata().getId())) {

						progressivo++;

						rendicontazioneBeans.add(new SaitAreaRendicontazioneBeanExporter(tipoEsportazione
								.getCodiceCliente(), areaMagazzinoLoad, rigaMagazzino.getAreaCollegata(), progressivo));

						idAreaMagazzino = rigaMagazzino.getAreaMagazzino().getId();
						idAreaMagazzinoCollegata = rigaMagazzino.getAreaCollegata() != null ? rigaMagazzino
								.getAreaCollegata().getId() : null;
					}

					if (rigaMagazzino instanceof RigaArticolo) {

						ArticoloLite articolo = ((RigaArticolo) rigaMagazzino).getArticolo();
						String codiceArticolo = articolo.getCodice();
						CodiceArticoloEntita codiceArticoloEntita = codiceArticoloEntitaManager
								.caricaCodiceArticoloEntita(articolo.getId(), areaMagazzinoLoad.getDocumento()
										.getEntita().getId());
						if (codiceArticoloEntita != null) {
							codiceArticolo = codiceArticoloEntita.getCodice();
						}

						rendicontazioneBeans.add(new SaitRigaRendicontazioneBeanExporter((RigaArticolo) rigaMagazzino,
								progressivo, codiceArticolo));
					}
				}
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dell'area magazzino.", e);
				throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e);
			}
		}

		RendicontazioneWriter writer = new RendicontazioneWriter();
		List<byte[]> flussi = new ArrayList<byte[]>();
		flussi.add(writer.write(tipoEsportazione.getTemplate(), rendicontazioneBeans));

		return flussi;
	}

}
