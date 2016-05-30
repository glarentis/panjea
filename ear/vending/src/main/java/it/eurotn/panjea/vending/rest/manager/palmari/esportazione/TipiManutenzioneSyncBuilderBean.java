package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.TipiManutenzioneSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.TipiManutenzioneSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiManutenzioneSyncBuilder")
public class TipiManutenzioneSyncBuilderBean extends SqlGeneratorBean implements TipiManutenzioneSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE TipiManutenzione(" +
			"Manutenzione nvarchar(5) NOT NULL," +
			"Descrizione nvarchar(50) NULL," +
			"PRIMARY KEY (Manutenzione))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "TipiManutenzione";
	}

}
