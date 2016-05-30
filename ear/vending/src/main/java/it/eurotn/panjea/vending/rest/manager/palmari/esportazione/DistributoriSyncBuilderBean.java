package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.DistributoriSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.DistributoriSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DistributoriSyncBuilder")
public class DistributoriSyncBuilderBean extends SqlGeneratorBean implements DistributoriSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new HashMap<String, String>();
        fields.put("art.codice", "Matricola");
        fields.put("ins.codice", "Installazione");
        fields.put("ent.codice", "Cliente");
        fields.put("an.denominazione", "Ragione_sociale");
        fields.put("sa.indirizzo", "Indirizzo");
        fields.put("cap.descrizione", "Cap");
        fields.put("loc.descrizione", "Citta");
        fields.put("l2.sigla", "Provincia");
        fields.put("ubi.descrizione", "Ubicazione");
        fields.put("sa.telefono", "Telefono");
        fields.put("modelli.codice", "Modello");
        fields.put("IF(tmod.acqua or tmod.kit, 1, IF(tmod.caldo, 2, 3))", "Tipo_gestione");
        fields.put("coalesce(lis.codice, '.')", "Listino_prodotti_1");
        fields.put("coalesce(lisa.codice,'.')", "Listino_prodotti_2");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE Distributori(" +
			"Matricola nvarchar(13) NOT NULL,"+
			"Installazione nvarchar(10) NOT NULL,"+
			"Cliente nvarchar(6) NOT NULL,"+
			"Ragione_sociale nvarchar(50) NULL,"+
			"Indirizzo nvarchar(50) NULL,"+
			"Cap nvarchar(6) NULL,"+
			"Citta nvarchar(50) NULL,"+
			"Provincia nvarchar(2) NULL,"+
			"Ubicazione nvarchar(255) NULL,"+
			"Telefono nvarchar(50) NULL,"+
			"Contatto nvarchar(255) NULL,"+
			"Modello nvarchar(5) NOT NULL,"+
			"Magazzino bit NOT NULL DEFAULT 0,"+
			"Tipo_gestione int NOT NULL,"+
			"Ultima_lettura int NOT NULL DEFAULT 0,"+
			"Listino_prodotti_1 nvarchar(20) NOT NULL,"+
			"Listino_prodotti_2 nvarchar(20) NOT NULL,"+
			"Listino_prodotti_3 nvarchar(20) NOT NULL DEFAULT '.',"+
			"Prezzo_battuta money NOT NULL DEFAULT 0,"+
			"Ultima_visita datetime NULL,"+
			"Protocollo_IRda int NULL,"+
			"Codice_gestore int NULL,"+
			"Ultimo_inventario datetime NULL,"+
			"Telefono_1 nvarchar(15) NULL,"+
			"Telefono_2 nvarchar(15) NULL,"+
			"Installabile_da_caricatore bit NOT NULL DEFAULT 0,"+
			"Messaggio nvarchar(255) NULL,"+
			"Data_messaggio_visualizzato datetime NULL,"+
			"RendiResto bit NOT NULL DEFAULT 0,"+
			"Rx int NOT NULL DEFAULT 0,"+
			"Tx int NOT NULL DEFAULT 0,"+
			"Baud int NOT NULL DEFAULT 9600,"+
			"Start9600 bit NOT NULL DEFAULT 0,"+
			"ObbligaRitiroCassetta bit NOT NULL DEFAULT 0,"+
			"PRIMARY KEY  (Installazione))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		/*
		select art.codice as matricola, 
			ins.codice as installazione, 
			ent.codice as cliente, 
			an.denominazione as ragione_sociale, 
			sa.indirizzo,
			cap.descrizione as cap,
			loc.descrizione,
			l2.sigla, 
			ubi.descrizione as ubicazione,
			sa.telefono, 
			modelli.codice as modello,
			IF(tmod.acqua or tmod.kit, 1, IF(tmod.caldo, 2, 3)) as tipo_gestione,
			coalesce(lis.codice, '.') as listino_prodotti_1,
			coalesce(lisa.codice,'.') as listino_prodotti_2
		from vend_dati_vending_distributore dist
		join maga_articoli art on art.datiVending_id = dist.id
		join manu_installazioni ins on ins.articolo_id = art.id
		join anag_depositi dep on ins.deposito_id = dep.id
		join anag_sedi_entita se on se.id = dep.sedeDeposito_id
		join anag_sedi_anagrafica sa on sa.id = se.sede_anagrafica_id
		join anag_entita ent on ent.id = se.entita_id
		join anag_anagrafica an on an.id = ent.anagrafica_id
		join geog_livello_2 l2 on l2.id = sa.lvl2_id
		join geog_localita loc on loc.id = sa.localita_id
		left join geog_cap cap on cap.id = loc.id
		join manu_ubicazioni_installazione ubi on ubi.id = ins.ubicazione_id
		join vend_modelli modelli on modelli.id = dist.modello_id
		join vend_tipi_modello tmod on tmod.id = modelli.tipoModello_id
		left join maga_listini lis on lis.id = ins.listino_id
		left join maga_listini lisa on lisa.id = ins.listino_alternativo_id
	*/
		return 
			"from vend_dati_vending_distributore dist " +
			"join maga_articoli art on art.datiVending_id = dist.id " +
			"join manu_installazioni ins on ins.articolo_id = art.id " +
			"join anag_depositi dep on ins.deposito_id = dep.id " +
			"join anag_sedi_entita se on se.id = dep.sedeDeposito_id " +
			"join anag_sedi_anagrafica sa on sa.id = se.sede_anagrafica_id " +
			"join anag_entita ent on ent.id = se.entita_id " +
			"join anag_anagrafica an on an.id = ent.anagrafica_id " +
			"join geog_livello_2 l2 on l2.id = sa.lvl2_id " +
			"join geog_localita loc on loc.id = sa.localita_id " +
			"left join geog_cap cap on cap.id = loc.id " +
			"join manu_ubicazioni_installazione ubi on ubi.id = ins.ubicazione_id " +
			"join vend_modelli modelli on modelli.id = dist.modello_id " +
			"join vend_tipi_modello tmod on tmod.id = modelli.tipoModello_id " +
			"left join maga_listini lis on lis.id = ins.listino_id " +
			"left join maga_listini lisa on lisa.id = ins.listino_alternativo_id";
	}

	@Override
	protected String getTableName() {
		return "Distributori";
	}

}
