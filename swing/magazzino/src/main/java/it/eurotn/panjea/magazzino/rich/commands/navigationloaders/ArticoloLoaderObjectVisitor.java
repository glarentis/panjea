package it.eurotn.panjea.magazzino.rich.commands.navigationloaders;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

import org.springframework.richclient.util.RcpSupport;

public class ArticoloLoaderObjectVisitor {
	/**
	 *
	 * @param loaderObject
	 *            ogg. da visitare
	 * @return articolo
	 */
	public Articolo visit(Articolo loaderObject) {
		return loaderObject;
	}

	/**
	 *
	 * @param loaderObject
	 *            ogg. da visitare
	 * @return articolo
	 */
	public Articolo visit(ArticoloLite loaderObject) {
		return loaderObject.creaProxyArticolo();
	}

	/**
	 *
	 * @param loaderObject
	 *            ogg. da visitare
	 * @return articolo
	 */
	public Articolo visit(String loaderObject) {
		IMagazzinoAnagraficaBD bd = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
		ParametriRicercaArticolo par = new ParametriRicercaArticolo();
		par.setCodice(loaderObject);
		return bd.ricercaArticoli(par).get(0).createProxyArticolo();
	}
}