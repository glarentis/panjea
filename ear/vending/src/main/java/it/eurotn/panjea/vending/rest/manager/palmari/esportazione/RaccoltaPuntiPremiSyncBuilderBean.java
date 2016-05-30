package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.RaccoltaPuntiPremiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.RaccoltaPuntiPremiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RaccoltaPuntiPremiSyncBuilder")
public class RaccoltaPuntiPremiSyncBuilderBean extends SqlGeneratorBean implements RaccoltaPuntiPremiSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE RaccoltaPuntiPremi(" +
			"Raccolta nvarchar(6) NOT NULL," +
			"Prodotto nvarchar(13) NOT NULL," +
			"Punti int NOT NULL," +
			"Quantita int NOT NULL," +
			"Costo decimal(7, 2) NOT NULL DEFAULT (0)," +
			"Descrizione_aggiuntiva nvarchar(500) NULL," +
			"PRIMARY KEY (Raccolta,Prodotto,Punti))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "RaccoltaPuntiPremi";
	}

}
