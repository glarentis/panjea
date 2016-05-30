package it.eurotn.panjea.agenti.manager.provvigionestrategy;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.agenti.domain.AgentiSettings;
import it.eurotn.panjea.agenti.manager.interfaces.AgentiSettingsManager;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.ListinoCalcoloPrezzoProvvigioneStrategy")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoCalcoloPrezzoProvvigioneStrategy")
public class ListinoCalcoloPrezzoProvvigioneStrategy implements CalcoloPrezzoProvvigioneStrategy {

    @EJB
    private ListinoManager listinoManager;

    @EJB
    private AgentiSettingsManager agentiSettingsManager;

    @Override
    public BigDecimal calcolaPrezzoNetto(RigaArticolo rigaArticolo) {

        BigDecimal prezzoNettoBaseProvvigionale = BigDecimal.ZERO;

        if (!rigaArticolo.isOmaggio()) {
            BigDecimal prezzoNetto = rigaArticolo.getPrezzoNetto().getImportoInValutaAzienda();

            BigDecimal prezzoListino = caricaPrezzoRigaListinoProvvigioneSettings(rigaArticolo);

            prezzoNettoBaseProvvigionale = prezzoNetto.subtract(prezzoListino);
        }

        return prezzoNettoBaseProvvigionale;
    }

    @Override
    public BigDecimal calcolaPrezzoUnitario(RigaArticolo rigaArticolo) {

        BigDecimal prezzoUnitario = rigaArticolo.getPrezzoUnitario().getImportoInValutaAzienda();

        BigDecimal prezzoListino = caricaPrezzoRigaListinoProvvigioneSettings(rigaArticolo);

        return prezzoUnitario.subtract(prezzoListino);
    }

    /**
     * Carica il prezzo dell'articolo della riga sul listino specificato nella settings.
     * 
     * @param rigaArticolo
     *            riga articolo
     * @return prezzo trovato
     */
    private BigDecimal caricaPrezzoRigaListinoProvvigioneSettings(RigaArticolo rigaArticolo) {

        BigDecimal prezzoListino = BigDecimal.ZERO;

        // carico il listino dalle settings
        AgentiSettings agentiSettings = agentiSettingsManager.caricaAgentiSettings();

        // cerco la versione in vigore alla data documento
        VersioneListino versioneListino = listinoManager.caricaVersioneListinoByData(
                agentiSettings.getBaseProvvigionaleSettings().getListino(),
                rigaArticolo.getAreaMagazzino().getDocumento().getDataDocumento());
        List<RigaListino> righeListino = listinoManager.caricaRigheListinoByArticolo(versioneListino,
                rigaArticolo.getArticolo().getId());

        if (righeListino != null && !righeListino.isEmpty()) {
            prezzoListino = righeListino.get(0).getPrezzo();
        }

        return prezzoListino;
    }

}
