package it.eurotn.panjea.anagrafica.service;

import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.ChartLayout;
import it.eurotn.panjea.anagrafica.domain.DockedLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.anagrafica.manager.interfaces.LayoutManager;
import it.eurotn.panjea.anagrafica.service.interfaces.LayoutService;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.LayoutService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.LayoutService")
public class LayoutServiceBean implements LayoutService {

	/**
	 * @uml.property name="layoutManager"
	 * @uml.associationEnd
	 */
	@EJB
	protected LayoutManager layoutManager;

	@Override
	public void cancella(AbstractLayout layout) {
		layoutManager.cancella(layout);
	}

	@Override
	public List<ChartLayout> caricaChartLayout(String key) {
		return layoutManager.caricaChartLayout(key);
	}

	@Override
	public DockedLayout caricaDefaultDockedLayout(String key) {
		return layoutManager.caricaDefaultDockedLayout(key);
	}

	@Override
	public DockedLayout caricaDockedLayout(String key) {
		return layoutManager.caricaDockedLayout(key);
	}

	@Override
	public List<TableLayout> caricaTableLayout(String key) {
		return layoutManager.caricaTableLayout(key);
	}

	@Override
	public AbstractLayout salva(AbstractLayout layout) {
		return layoutManager.salva(layout);
	}

}
