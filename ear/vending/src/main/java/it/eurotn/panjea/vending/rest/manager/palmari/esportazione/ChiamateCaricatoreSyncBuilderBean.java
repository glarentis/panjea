package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ChiamateCaricatoreSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ChiamateCaricatoreSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ChiamateCaricatoreSyncBuilder")
public class ChiamateCaricatoreSyncBuilderBean extends SqlGeneratorBean implements ChiamateCaricatoreSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE ChiamateCaricatore(" +
			"Progressivo int NOT NULL," +
			"Data_chiamata datetime NOT NULL," +
			"Numero_interno nvarchar(15) NULL," +
			"Cliente nvarchar(6) NULL," +
			"Installazione int NOT NULL," +
			"Matricola nvarchar(13) NOT NULL," +
			"Persona nvarchar(30) NULL," +
			"Descrizione_aggiuntiva nvarchar(255) NULL," +
			"Flag_chiamata_interna bit NOT NULL DEFAULT 0," +
			"Flag_chiamata_chiusa bit NOT NULL DEFAULT 0," +
			"Flag_chiamata_fatturata bit NOT NULL," +
			"PRIMARY KEY (Progressivo))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		/*
		select testa.id, righe.qta, righe.importoInValutaAzienda, doc.codice, doc.dataDocumento, art.codice, ent.codice, coalesce(tipimag.valoriFatturato, 0) as fatturato
		from ordi_righe_ordine as righe
		join ordi_area_ordine as testa on righe.areaOrdine_id = testa.id
		join docu_documenti as doc on doc.id = testa.documento_id
		join maga_articoli as art on art.id = righe.articolo_id
		join anag_entita as ent on ent.id = doc.entita_id
		left join maga_righe_magazzino as righemag on righemag.rigaOrdineCollegata_Id = righe.id
		left join maga_area_magazzino as mag on mag.id = righemag.areaMagazzino_id
		left join maga_tipi_area_magazzino tipimag on tipimag.id = mag.tipoAreaMagazzino_id
		where righe.qta is not null and righe.evasioneForzata = 0 and righe.bloccata = 0 and
		testa.statoAreaOrdine = 1 and (righemag.id is null or (tipimag.valoriFatturato = 1 and tipimag.tipoMovimento = 0))
		*/
		return null;
	}

	@Override
	protected String getTableName() {
		return "ChiamateCaricatore";
	}

}
