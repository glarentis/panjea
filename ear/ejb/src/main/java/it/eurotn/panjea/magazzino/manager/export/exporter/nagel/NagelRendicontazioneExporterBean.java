/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.export.exporter.nagel;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneWriter;
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

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.RendicontazioneExporter.Nagel")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RendicontazioneExporter.Nagel")
public class NagelRendicontazioneExporterBean implements RendicontazioneExporter {

	private static Logger logger = Logger.getLogger(NagelRendicontazioneExporterBean.class);

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

				rendicontazioneBeans.add(new NagelAreaRendicontazioneBeanExporter(areaMagazzinoLoad));
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
