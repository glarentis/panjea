package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.RaccoltaPuntiPremiClientiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.RaccoltaPuntiPremiClientiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RaccoltaPuntiPremiClientiSyncBuilder")
public class RaccoltaPuntiPremiClientiSyncBuilderBean extends SqlGeneratorBean
		implements RaccoltaPuntiPremiClientiSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE RaccoltaPuntiPremiClienti(" +
			"Progressivo int NOT NULL," +
			"Raccolta nvarchar(6) NOT NULL," +
			"Prodotto nvarchar(13) NOT NULL," +
			"Punti int NOT NULL," +
			"Cliente nvarchar(6) NOT NULL," +
			"Quantita int NOT NULL," +
			"Data_ordine datetime NULL," +
			"Data_consegna datetime NULL," +
			"Valore decimal(7, 2) NOT NULL," +
			"Sospeso decimal(7, 2) NOT NULL," +
			"Pagato decimal(7, 2) NOT NULL," +
			"Ordine int NULL," +
			"Rifornimento int NOT NULL," +
			"Nuovo bit NOT NULL," +
			"PRIMARY KEY (Progressivo))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "RaccoltaPuntiPremiClienti";
	}

}
