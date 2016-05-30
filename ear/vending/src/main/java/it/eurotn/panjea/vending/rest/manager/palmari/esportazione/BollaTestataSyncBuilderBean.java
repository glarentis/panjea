package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.BollaTestataSyncBuilder;

@Stateless(name = "Panjea.BollaTestataSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.BollaTestataSyncBuilder")
public class BollaTestataSyncBuilderBean implements BollaTestataSyncBuilder {

    @Override
    public String esporta(String codiceOperatore) {
    	Date today = new Date();
    	SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
    	
        return "CREATE TABLE BollaTestata(" + "Chiave nchar(1) NOT NULL DEFAULT 'K',"
                + "Numero_XE nvarchar(10) NULL DEFAULT '1'," + "Data_XE datetime NULL)"
        		+ "\r\n" 
                + "Insert into BollaTestata (Data_XE) values ('" + dt.format(today) + "')";
    }

}
