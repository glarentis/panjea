package it.eurotn.panjea.aton.exporter;

import it.eurotn.panjea.aton.domain.wrapper.ArticoloAton;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.AtonARTICOExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AtonARTICOExporter")
public class ARTICOExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.AtonARTICOExporter";

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void esporta() throws FileCreationException {
		try {
			Query query = panjeaDAO
					.prepareQuery("select art from Articolo art left join fetch art.descrizioniLingua inner join fetch art.categoria left join fetch art.codiciArticoloEntita where art.abilitato=true");
			@SuppressWarnings("unchecked")
			List<Articolo> articoli = query.getResultList();

			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getFilePathForTemplate());
			BeanWriter out = factory.createWriter("articolo", getFileForExport());

			for (Articolo articolo : articoli) {
				out.write(new ArticoloAton(articolo));
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
