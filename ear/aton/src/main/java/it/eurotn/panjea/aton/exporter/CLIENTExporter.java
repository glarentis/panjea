package it.eurotn.panjea.aton.exporter;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.aton.domain.wrapper.SedeEntitaAton;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.CLIENTExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CLIENTExporter")
public class CLIENTExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.CLIENTExporter";

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void esporta() throws FileCreationException {
		StringBuilder sb = new StringBuilder();
		sb.append("select se ");
		sb.append("from SedeEntita se join fetch se.entita ent join fetch se.sede join fetch ent.anagrafica ");
		sb.append("left join fetch se.sedePagamento sp ");
		sb.append("left join fetch se.sedeMagazzino ");
		sb.append("where se.entita.class=it.eurotn.panjea.anagrafica.domain.Cliente");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setHint("org.hibernate.comment", new String("Carico le sediEntita,sediPagamento e sediMagazzino"));
		@SuppressWarnings("unchecked")
		List<SedeEntita> sedi = query.getResultList();

		// Creo una mappa con le sediPrincipali per le entità
		Map<Integer, SedeEntita> sediPrincipali = new HashMap<Integer, SedeEntita>();
		for (SedeEntita sedeEntita : sedi) {
			if (sedeEntita.getTipoSede().isSedePrincipale()) {
				sediPrincipali.put(sedeEntita.getEntita().getId(), sedeEntita);
			}
		}

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("clienti", getFileForExport());

		for (SedeEntita sedeEntita : sedi) {
			SedePagamento sp = sedeEntita.getSedePagamento();
			SedeMagazzino sm = sedeEntita.getSedeMagazzino();
			if (!sedeEntita.getTipoSede().isSedePrincipale() && sedeEntita.isEreditaDatiCommerciali()) {
				sp = sediPrincipali.get(sedeEntita.getEntita().getId()).getSedePagamento();
				sm = sediPrincipali.get(sedeEntita.getEntita().getId()).getSedeMagazzino();
			}

			out.write(new SedeEntitaAton(sedeEntita, sp, sm));
		}

		out.flush();
		out.close();
	}
}
