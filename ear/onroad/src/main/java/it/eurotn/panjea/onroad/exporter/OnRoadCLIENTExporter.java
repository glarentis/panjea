package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.onroad.domain.wrapper.SedeEntitaOnRoad;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
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

@Stateful(mappedName = "Panjea.OnRoadCLIENTExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadCLIENTExporter")
public class OnRoadCLIENTExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.OnRoadCLIENTExporter";

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void esporta() throws FileCreationException {
		StringBuilder sb = new StringBuilder();
		sb.append("select se ");
		sb.append("from SedeEntita se join fetch se.entita ent join fetch se.sede join fetch ent.anagrafica ");
		sb.append("left join fetch se.sedePagamento sp ");
		sb.append("left join fetch se.sedeMagazzino ");
		sb.append("left join fetch se.sedeOrdine ");
		sb.append("where se.entita.class=it.eurotn.panjea.anagrafica.domain.Cliente");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setHint("org.hibernate.comment", new String(
				"Carico le sediEntita,sediPagamento,sediMagazzino e sediOrdine"));
		@SuppressWarnings("unchecked")
		List<SedeEntita> sedi = query.getResultList();

		// carico i fornitori collegati ai codici articolo entità
		StringBuilder sb2 = new StringBuilder();
		sb2.append("select se from SedeEntita se join fetch se.entita f join fetch se.sede join fetch f.anagrafica ");
		sb2.append("left join fetch se.sedePagamento sp ");
		sb2.append("left join fetch se.sedeMagazzino ");
		sb2.append("left join fetch se.sedeOrdine ");
		sb2.append("where f.id in (select cae.entita.id from CodiceArticoloEntita cae where cae.consegnaContoTerzi=true and cae.entita.class=it.eurotn.panjea.anagrafica.domain.Fornitore group by cae.entita.id) ");
		sb2.append("and se.tipoSede.sedePrincipale=true ");
		Query query2 = panjeaDAO.prepareQuery(sb2.toString());
		query2.setHint("org.hibernate.comment", new String(
				"Carico le sediEntita,sediPagamento,sediMagazzino e sediOrdine"));
		@SuppressWarnings("unchecked")
		List<SedeEntita> sediFornitori = query2.getResultList();

		sedi.addAll(sediFornitori);

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
			SedeOrdine so = sedeEntita.getSedeOrdine();
			if (!sedeEntita.getTipoSede().isSedePrincipale() && sedeEntita.isEreditaDatiCommerciali()) {
				sp = sediPrincipali.get(sedeEntita.getEntita().getId()).getSedePagamento();
				sm = sediPrincipali.get(sedeEntita.getEntita().getId()).getSedeMagazzino();
				so = sediPrincipali.get(sedeEntita.getEntita().getId()).getSedeOrdine();
			}

			out.write(new SedeEntitaOnRoad(sedeEntita, sp, sm, so));
		}

		out.flush();
		out.close();
	}
}
