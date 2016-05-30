package it.eurotn.panjea.magazzino.manager.moduloprezzo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo.EStrategia;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoTipoMezzoZonaGeograficaManager;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.ModuloPrezzoCalculator;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ListinoTipoMezzoZonaGeograficaPrezzoCalculator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoTipoMezzoZonaGeograficaPrezzoCalculator")
public class ListinoTipoMezzoZonaGeograficaPrezzoCalculator implements ModuloPrezzoCalculator {

    public static final String TIPO_MODULO = "LISTINOTIPOMEZZOZONAGEOGRAFICA";

    /**
     * @uml.property name="listinoTipoMezzoZonaGeograficaManager"
     * @uml.associationEnd
     */
    @EJB
    private ListinoTipoMezzoZonaGeograficaManager listinoTipoMezzoZonaGeograficaManager;

    @Override
    public ParametriCalcoloPrezzi calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {

        TipoMezzoTrasporto tipoMezzoTrasporto = new TipoMezzoTrasporto();
        tipoMezzoTrasporto.setId(parametriCalcoloPrezzi.getIdTipoMezzoTrasporto());

        ZonaGeografica zonaGeografica = new ZonaGeografica();
        zonaGeografica.setId(parametriCalcoloPrezzi.getIdZonaGeografica());

        ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica = listinoTipoMezzoZonaGeograficaManager
                .caricaListinoTipoMezzoZonaGeografica(tipoMezzoTrasporto, zonaGeografica);

        // ripulisco la mappa dei prezzi. Se ho qualcosa prima del listino questa va persa.
        parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().clear();

        // inserisco nella mappa dei prezzi i valori delle righe listino che ho trovato
        if (listinoTipoMezzoZonaGeografica != null && parametriCalcoloPrezzi
                .getProvenienzaPrezzoArticolo() == ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA) {
            RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo = new RisultatoModuloPrezzo<BigDecimal>();
            risultatoModuloPrezzo.setDescrizioneModulo("Zona geografica");
            risultatoModuloPrezzo.setCodiceModulo("Tipo mezzo");
            risultatoModuloPrezzo.setTipoModulo(TIPO_MODULO);
            risultatoModuloPrezzo.setStrategia(EStrategia.ASSEGNAZIONE);
            risultatoModuloPrezzo.setNumeroDecimali(2);
            risultatoModuloPrezzo.setValue(listinoTipoMezzoZonaGeografica.getPrezzo());
            risultatoModuloPrezzo.setQuantita("0");

            RisultatoPrezzo<BigDecimal> risultatoPrezzo = new RisultatoPrezzo<BigDecimal>();
            risultatoPrezzo.setNumeroDecimali(2);
            risultatoPrezzo.setValue(listinoTipoMezzoZonaGeografica.getPrezzo());
            risultatoPrezzo.setQuantita(0.0);
            risultatoPrezzo.addRisultatoModuloPrezzo(risultatoModuloPrezzo);
            parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().put(0.0, risultatoPrezzo);
        }

        return parametriCalcoloPrezzi;
    }

    @Override
    public Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        return new HashSet<ArticoloLite>();
    }

}
