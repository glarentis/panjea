package it.eurotn.panjea.aton.exporter;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.aton.domain.TipologiaVendita;
import it.eurotn.panjea.aton.domain.wrapper.ArticoloAton;
import it.eurotn.panjea.aton.exporter.manager.interfaces.DataExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.manager.interfaces.CategorieManager;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.CodiceArticoloEntitaManager;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipoRicercaCodicePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.CodicePagamentoManager;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

import org.apache.log4j.Logger;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.TABELLExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TABELLExporter")
public class TABELLExporter extends AbstractDataExporter implements DataExporter {

	private static Logger logger = Logger.getLogger(TABELLExporter.class);

	public static final String BEAN_NAME = "Panjea.TABELLExporter";

	@EJB
	private ListinoManager listinoManager;

	@EJB
	private CategorieManager categorieManager;

	@EJB
	private CodicePagamentoManager codicePagamentoManager;

	@EJB
	private AnagraficaTabelleManager anagraficaTabelleManager;

	@EJB
	private TipiAreaPartitaManager tipiAreaPartitaManager;

	@EJB
	private CodiceArticoloEntitaManager codiceArticoloEntitaManager;

	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	/**
	 * @return List<AttributoArticolo>
	 */
	@SuppressWarnings("unchecked")
	private List<AttributoArticolo> caricaValoriAttributoCategoria() {
		List<AttributoArticolo> entitaAbituali = new ArrayList<>();
		try {
			org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO
					.getEntityManager()
					.createNativeQuery(
							"select aa.valore as valore from maga_attributi_articoli aa inner join maga_tipo_attributo ta on ta.id=aa.tipoAttributo_id where ta.codice=:paramCodiceTipoAttributo group by aa.valore");
			queryImpl.setParameter("paramCodiceTipoAttributo", ArticoloAton.ATTRIBUTO_CATEGORIA_ATON);
			SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
			sqlQuery.addScalar("valore");
			sqlQuery.setResultTransformer(Transformers.aliasToBean(AttributoArticolo.class));
			entitaAbituali = sqlQuery.list();
		} catch (Exception e) {
			logger.error("Errore in caricaValoriAttributoCategoria", e);
			throw new RuntimeException("Errore in caricaValoriAttributoCategoria", e);
		}
		return entitaAbituali;
	}

	@Override
	public void esporta() throws FileCreationException {
		try {
			StreamFactory factory = StreamFactory.newInstance();
			factory.load(getFilePathForTemplate());
			BeanWriter out = factory.createWriter("tabelle", getFileForExport());

			List<Listino> listini = listinoManager.caricaListini();
			for (Listino listino : listini) {
				out.write(listino);
			}

			if ("dolcelit".equalsIgnoreCase(getAzienda())) {
				List<AttributoArticolo> caricaValoriAttributoCategoria = caricaValoriAttributoCategoria();
				for (AttributoArticolo attributoArticolo : caricaValoriAttributoCategoria) {
					out.write(attributoArticolo);
				}
			} else {
				List<CategoriaLite> categorie = categorieManager.caricaCategorie();
				for (CategoriaLite categoriaLite : categorie) {
					out.write(categoriaLite);
				}
			}

			List<EntitaLite> fornitoriAbituali = codiceArticoloEntitaManager.caricaEntitaAbituali(FornitoreLite.TIPO);
			for (EntitaLite entitaLite : fornitoriAbituali) {
				out.write(entitaLite);
			}

			List<CodicePagamento> pagamenti = codicePagamentoManager.caricaCodiciPagamento(null,
					TipoRicercaCodicePagamento.TUTTO, false);
			for (CodicePagamento codicePagamento : pagamenti) {
				out.write(codicePagamento);
			}

			List<UnitaMisura> ums = anagraficaTabelleManager.caricaUnitaMisura();
			for (UnitaMisura unitaMisura : ums) {
				out.write(unitaMisura);
			}

			List<TipoAreaPartita> tipiAreaPartita = tipiAreaPartitaManager
					.caricaTipiAreaPartitaGenerazioneRate(TipoPartita.ATTIVA);
			for (TipoAreaPartita tipoAreaPartita : tipiAreaPartita) {
				if (tipoAreaPartita.getTipoDocumento().isAbilitato()) {
					out.write(tipoAreaPartita.getTipoDocumento());
				}
			}

			TipologiaVendita tv = new TipologiaVendita();
			tv.setCodice("VEN");
			tv.setDescrizione("VENDITA");
			out.write(tv);
			TipologiaVendita oma = new TipologiaVendita();
			oma.setCodice("OMA");
			oma.setDescrizione("OMAGGIO");
			out.write(oma);

			out.flush();
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return codice azienda
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}
}
