package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.InstallazioniRitiriDistributoriSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.InstallazioniRitiriDistributoriSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.InstallazioniRitiriDistributoriSyncBuilder")
public class InstallazioniRitiriDistributoriSyncBuilderBean extends SqlGeneratorBean implements InstallazioniRitiriDistributoriSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE InstallazioniRitiriDistributori(" +
			"Installazione nvarchar(10) NOT NULL," +
			"DataMovimento datetime NOT NULL," +
			"MatricolaInstallata nvarchar(13) NOT NULL," +
			"BattuteInstallata int NOT NULL," +
			"ModelloInstallato nvarchar(5) NOT NULL," +
			"Flag_nuova_installazione bit NOT NULL," +
			"MatricolaRitirata nvarchar(13) NOT NULL," +
			"BattuteRitirata int NOT NULL," +
			"ModelloRitirato nvarchar(5) NOT NULL," +
			"Flag_ritiro_definitivo bit NOT NULL," +
			"Armadietti int NOT NULL," +
			"Cestini int NOT NULL," +
			"Note nvarchar(255) NOT NULL," +
			"PRIMARY KEY (Installazione))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "InstallazioniRitiriDistributori";
	}

}
