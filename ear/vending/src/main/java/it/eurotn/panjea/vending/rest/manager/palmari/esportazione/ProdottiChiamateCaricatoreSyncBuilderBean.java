package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ProdottiChiamateCaricatoreSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ProdottiChiamateCaricatoreSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ProdottiChiamateCaricatoreSyncBuilder")
public class ProdottiChiamateCaricatoreSyncBuilderBean extends SqlGeneratorBean
		implements ProdottiChiamateCaricatoreSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE ProdottiChiamateCaricatore(" +
			"Progressivo int NOT NULL," +
			"Prodotto nvarchar(13) NOT NULL," +
			"Quantita float NOT NULL )";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "ProdottiChiamateCaricatore";
	}

}
