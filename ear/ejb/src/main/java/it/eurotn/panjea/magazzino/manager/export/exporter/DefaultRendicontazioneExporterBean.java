package it.eurotn.panjea.magazzino.manager.export.exporter;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.interfaces.RendicontazioneExporter;
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

@Stateless(name = "Panjea.RendicontazioneExporter.Default")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RendicontazioneExporter.Default")
public class DefaultRendicontazioneExporterBean implements RendicontazioneExporter {

	private static Logger logger = Logger.getLogger(DefaultRendicontazioneExporterBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) {

		List<Object> rendicontazioneBeans = new ArrayList<Object>();
		for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {
			AreaMagazzino areaMagazzinoLoad;
			try {
				areaMagazzinoLoad = panjeaDAO.load(AreaMagazzino.class, areaMagazzinoRicerca.getIdAreaMagazzino());

				for (RigaMagazzino rigaMagazzino : areaMagazzinoLoad.getRigheMagazzino()) {
					if (rigaMagazzino instanceof RigaArticolo) {

						rendicontazioneBeans.add(new RendicontazioneBeanExporter(tipoEsportazione.getCodiceCliente(),
								(RigaArticolo) rigaMagazzino));
					}
				}
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dell'area magazzino.", e);
				throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e);
			}
		}

		RendicontazioneWriter writer = new RendicontazioneWriter();
		List<byte[]> flussi = new ArrayList<byte[]>();
		flussi.add(writer.write(tipoEsportazione.getTemplate(), true, rendicontazioneBeans, true));

		return flussi;
	}
}
