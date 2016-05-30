package it.eurotn.panjea.cosaro.rendicontazione;

import it.eurotn.panjea.cosaro.RigaFileCoop;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RendicontazioneExporter.Coop")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RendicontazioneExporter.Coop")
public class CoopRendicontazioneExporterBean extends AbstractCoopRendicontazioneExporterBean {

	@EJB
	private RigaMagazzinoManager rigaMagazzinoManager;

	@Override
	public List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) {

		Set<String> uuiToExport = getRigheToExport(areeMagazzino);

		ArrayList<byte[]> result = new ArrayList<byte[]>();
		Query queryFile = panjeaDAO
				.prepareQuery("select r from RigaFileCoop r where r.uuid=:uuid order by r.numeroRiga ");
		for (String uuidFile : uuiToExport) {
			queryFile.setParameter("uuid", uuidFile);
			try {
				File file = File.createTempFile("coop", ".txt");
				file.deleteOnExit();
				@SuppressWarnings("unchecked")
				List<RigaFileCoop> righeFile = panjeaDAO.getResultList(queryFile);
				List<String> righeTestoFile = new ArrayList<String>();
				Date dataConsegnaPrevista = (Date) parametri.get("data");
				String data = new SimpleDateFormat("yyMMddHHmm").format(dataConsegnaPrevista);
				for (RigaFileCoop rigaFile : righeFile) {
					righeTestoFile.add(rigaFile.getTextRendicontazione(data));
				}
				FileUtils.writeLines(file, righeTestoFile);
				result.add(FileUtils.readFileToByteArray(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}
