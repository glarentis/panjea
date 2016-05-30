package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ArticoliSyncBuilder;

@Stateless(name = "Panjea.ArticoliSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ArticoliSyncBuilder")
public class ArticoliSyncBuilderBean implements ArticoliSyncBuilder {

    @Override
    public void esporta(OutputStream output, String codiceOperatore) {
        // TODO Auto-generated method stub
        for (int i = 0; i < 10000; i++) {
            try {
                output.write("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
                        .getBytes());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        try {
            output.flush();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public String esporta(String codiceOperatore) {
        return null;
    }

    protected Map<String, String> getSelectInsertFields() {
        final Map<String, String> fields = new TreeMap<>();
        fields.put("art.codice", "Prodotto");
        fields.put("art.descrizioneLinguaAziendale", "Descrizione");
        fields.put("um.codice", "Unita_misura");
        fields.put("100", "Ordine_elenco");
        fields.put("art.barCode", "Barcode");
        fields.put("iva.percApplicazione", "Aliquota_IVA");
        fields.put("1", "Scaricabile_da_pozzetto");
        return fields;
    }

    protected String getSQLCreateTable(String codiceOperatore) {
        return "CREATE TABLE Prodotti(" + "Prodotto nvarchar(13) NOT NULL," + "Descrizione nvarchar(255) NULL,"
                + "Unita_misura nvarchar(3) NULL," + "Ordine_elenco int NOT NULL," + "Barcode nvarchar(255) NULL,"
                + "Aliquota_IVA float NOT NULL," + "Scaricabile_da_pozzetto bit NOT NULL," + "PRIMARY KEY (Prodotto))";
    }

    protected String getSQLSelectRows(String codiceOperatore) {
        return "from maga_articoli art " + "join anag_unita_misura um on um.id = art.unitaMisura_id "
                + "join anag_codici_iva iva on iva.id = art.codiceIva_id ";
    }

    protected String getTableName() {
        return "Prodotti";
    }

}
