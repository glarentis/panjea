package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.operatori.interfaces.OperatoriManager;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.BollaSyncBuilder;

@Stateless(name = "Panjea.BollaSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.BollaSyncBuilder")
public class BollaSyncBuilderBean implements BollaSyncBuilder {
    private static final Logger LOGGER = Logger.getLogger(BollaSyncBuilderBean.class);

    @EJB
    private GiacenzaManager giacenzaManager;
    @EJB
    private OperatoriManager operatoriManager;

    @Override
    public String esporta(String codiceOperatore) {
        LOGGER.debug("--> Enter esporta");
        Operatore operatore = operatoriManager.caricaByCodice(codiceOperatore);
        if (operatore == null) {
            LOGGER.error(
                    "-->Operatore non trovato. Errore nel caricare la valorizzazione per operatore " + codiceOperatore);
            return "";
        }
        if (operatore.getMezzoTrasporto() == null) {
            LOGGER.error("-->Operatore senza mezzo di trasporto . Errore nel caricare la valorizzazione per operatore "
                    + codiceOperatore);
            return "";
        }

        if (operatore.getMezzoTrasporto().getDeposito() == null) {
            LOGGER.error(
                    "-->Operatore con mezzo di trasporto senza deposito collegato. Errore nel caricare la valorizzazione per operatore "
                            + codiceOperatore);
            return "";
        }

        DepositoLite depositoOperatore = operatore.getMezzoTrasporto().getDeposito();
        Map<ArticoloLite, Double> giacenze = giacenzaManager.calcolaGiacenze(depositoOperatore,
                Calendar.getInstance().getTime());

        StringBuilder sb = new StringBuilder(getSQLCreateTable());
        for (Entry<ArticoloLite, Double> giacenza : giacenze.entrySet()) {
            sb.append("INSER INTO  Bolla (Prodotto,Quantita_residua) VALUES ('");
            sb.append(giacenza.getKey().getCodice());
            sb.append("',");
            sb.append(giacenza.getValue());
            sb.append(")");
        }

        LOGGER.debug("--> Exit esporta");
        return sb.toString();
    }

    private String getSQLCreateTable() {
        return "CREATE TABLE Bolla(" + "Prodotto nvarchar(13) NOT NULL," + "Quantita_caricata float NOT NULL DEFAULT 0,"
                + "Quantita_residua float NOT NULL," + "Quantita_scaricata float NOT NULL DEFAULT 0,"
                + "Quantita_resa int NOT NULL DEFAULT 0," + "PRIMARY KEY (Prodotto))\n";
    }

}
