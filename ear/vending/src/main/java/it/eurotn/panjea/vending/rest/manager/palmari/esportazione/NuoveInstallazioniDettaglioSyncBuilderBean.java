package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.NuoveInstallazioniDettaglioSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.NuoveInstallazioniDettaglioSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.NuoveInstallazioniDettaglioSyncBuilder")
public class NuoveInstallazioniDettaglioSyncBuilderBean extends SqlGeneratorBean implements NuoveInstallazioniDettaglioSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE NuoveInstallazioniDettaglio(" +
			"Installazione int NOT NULL," +
			"Ragione_sociale nvarchar(50) NULL," +
			"Indirizzo nvarchar(50) NULL," +
			"Cap nvarchar(6) NULL," +
			"Citta nvarchar(50) NULL," +
			"Provincia nvarchar(2) NULL," +
			"Listino_prodotti_1 nvarchar(6) NOT NULL," +
			"Listino_prodotti_2 nvarchar(6) NOT NULL," +
			"Listino_prodotti_3 nvarchar(6) NOT NULL," +
			"PRIMARY KEY (Installazione))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "NuoveInstallazioniDettaglio";
	}

}
