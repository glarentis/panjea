package it.eurotn.panjea.onroad.exporter;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.manager.interfaces.CategorieManager;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.onroad.domain.TipologiaVendita;
import it.eurotn.panjea.onroad.exporter.manager.interfaces.DataExporter;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateful(mappedName = "Panjea.OnRoadTABELLExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OnRoadTABELLExporter")
public class OnRoadTABELLExporter extends AbstractDataExporter implements DataExporter {

	public static final String BEAN_NAME = "Panjea.OnRoadTABELLExporter";

	@EJB
	private ListinoManager listinoManager;
	@EJB
	private CategorieManager categorieManager;

	@Override
	public void esporta() throws FileCreationException {

		StreamFactory factory = StreamFactory.newInstance();
		factory.load(getFilePathForTemplate());
		BeanWriter out = factory.createWriter("tabelle", getFileForExport());

		List<Listino> listini = listinoManager.caricaListini();
		for (Listino listino : listini) {
			out.write(listino);
		}

		List<CategoriaLite> categorie = categorieManager.caricaCategorie();
		for (CategoriaLite categoriaLite : categorie) {
			out.write(categoriaLite);
		}

		TipoDocumento ddt = new TipoDocumento();
		ddt.setCodice("DDT");
		ddt.setDescrizione("DOCUMENTO DI TRASPORTO");
		out.write(ddt);
		TipoDocumento ft = new TipoDocumento();
		ft.setCodice("FT");
		ft.setDescrizione("FATTURA");
		out.write(ft);
		TipoDocumento acr = new TipoDocumento();
		acr.setCodice("ACR");
		acr.setDescrizione("NOTA DI ACCREDITO");
		out.write(acr);
		TipoDocumento not = new TipoDocumento();
		not.setCodice("NOT");
		not.setDescrizione("NOTA DI CONSEGNA NON VALORIZZATA");
		out.write(not);
		TipoDocumento nva = new TipoDocumento();
		nva.setCodice("NVA");
		nva.setDescrizione("NOTA DI CONSEGNA VALORIZZATA");
		out.write(nva);

		TipologiaVendita tv = new TipologiaVendita();
		tv.setCodice("VEN");
		tv.setDescrizione("VENDITA");
		out.write(tv);
		TipologiaVendita oma = new TipologiaVendita();
		oma.setCodice("OMA");
		oma.setDescrizione("OMAGGIO");
		out.write(oma);
		TipologiaVendita rev = new TipologiaVendita();
		rev.setCodice("REV");
		rev.setDescrizione("RESO VENDIBILE");
		out.write(rev);
		TipologiaVendita rei = new TipologiaVendita();
		rei.setCodice("REI");
		rei.setDescrizione("RESO INVENDIBILE");
		out.write(rei);
		TipologiaVendita tre = new TipologiaVendita();
		tre.setCodice("TRE");
		tre.setDescrizione("TRASFERIMENTO ENTRATA");
		out.write(tre);
		TipologiaVendita tru = new TipologiaVendita();
		tru.setCodice("TRU");
		tru.setDescrizione("TRASFERIMENTO USCITA");
		out.write(tru);

		out.flush();
		out.close();

	}
}
