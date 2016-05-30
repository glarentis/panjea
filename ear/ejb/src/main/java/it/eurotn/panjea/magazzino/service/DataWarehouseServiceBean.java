package it.eurotn.panjea.magazzino.service;

import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.manager.interfaces.DataWarehouseManager;
import it.eurotn.panjea.magazzino.service.interfaces.DataWarehouseService;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.DataWarehouseService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.DataWarehouseService")
public class DataWarehouseServiceBean implements DataWarehouseService {
    @EJB
    protected DataWarehouseManager dataWarehouseManager;

    @Override
    public void sincronizzaAnagrafiche() {
        dataWarehouseManager.sincronizzaAnagrafiche();
    }

    @Override
    public void sincronizzaDimensionedata() {
        dataWarehouseManager.sincronizzaDimensionedata();
    }

    @Override
    public void sincronizzaDMS() {
        dataWarehouseManager.sincronizzaDMS();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void sincronizzaMovimenti(Date dataAggiornamento) {
        dataWarehouseManager.sincronizzaMovimenti(dataAggiornamento);
    }
}
