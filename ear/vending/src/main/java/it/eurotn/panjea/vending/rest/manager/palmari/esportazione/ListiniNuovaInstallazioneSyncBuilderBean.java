package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ListiniNuovaInstallazioneSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ListiniNuovaInstallazioneSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListiniNuovaInstallazioneSyncBuilder")
public class ListiniNuovaInstallazioneSyncBuilderBean extends SqlGeneratorBean
		implements ListiniNuovaInstallazioneSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return "CREATE TABLE ListiniNuovaInstallazione(" +
			"Listino nvarchar(20) NOT NULL," +
			"Descrizione nvarchar(255) NOT NULL," +
			"PRIMARY KEY (Listino))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTableName() {
		return "ListiniNuovaInstallazione";
	}

}
