package it.eurotn.panjea.magazzino.interceptor;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.ejb.EJB;
import javax.interceptor.InvocationContext;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class AbstractDataWarehouseSync {

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	/**
	 * Costruttore.
	 * 
	 */
	public AbstractDataWarehouseSync() {
		super();
	}

	/**
	 * 
	 * @param ctx
	 *            contesto della chiamata
	 * @return area magazzino passata come parametro al metodo
	 */
	protected AreaMagazzino getParameterAreaMagazzino(InvocationContext ctx) {
		for (Object parametro : ctx.getParameters()) {
			if (parametro.getClass().equals(AreaMagazzino.class)) {
				return (AreaMagazzino) parametro;
			}
		}
		throw new IllegalArgumentException("Il metodo " + ctx.getMethod().getName()
				+ " non contiene un parametri di tipo AreaMagazzino");
	}

	/**
	 * 
	 * @param areaMagazzino
	 *            area magazzino
	 * @return <code>true</code> se movimenta il DW
	 */
	protected boolean movimentaDatawarehouse(AreaMagazzino areaMagazzino) {
		return areaMagazzino.getStatoAreaMagazzino() != StatoAreaMagazzino.PROVVISORIO
				&& areaMagazzino.getTipoAreaMagazzino().isMovimentaDatawarehouse();
	}

}