package it.eurotn.panjea.magazzino.manager.moduloprezzo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces.ModuloPrezzoCalculator;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.UltimoCostoModuloPrezzoCalculator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.UltimoCostoModuloPrezzoCalculator")
public class UltimoCostoModuloPrezzoCalculator implements ModuloPrezzoCalculator {

    private static final Logger LOGGER = Logger.getLogger(UltimoCostoModuloPrezzoCalculator.class);

    public static final String TIPO_MODULO = "ULTIMO COSTO";

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public ParametriCalcoloPrezzi calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        // Trovo l'ultimo costo per l'articolo
        Query query = panjeaDAO.prepareNamedQuery("Articolo.caricaUltimoCosto");
        query.setParameter("dataRegistrazione", parametriCalcoloPrezzi.getData());
        query.setParameter("articolo_id", parametriCalcoloPrezzi.getIdArticolo());
        query.setMaxResults(1);
        Object[] datiUltimoCosto = null;
        try {
            datiUltimoCosto = (Object[]) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // non ho nessun movimento....pazienza
            LOGGER.warn("Nessun movimento trovato per determinare il costo ultimo");
        } catch (Exception e) {
            LOGGER.error("-->errore nel caricare l'ultimo costo", e);
            throw new RuntimeException(e);
        }

        // pulisco eventuali prezzi
        parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().clear();
        RisultatoPrezzo<BigDecimal> risultatoPrezzo = new RisultatoPrezzo<BigDecimal>();

        Articolo articolo;
        try {
            articolo = panjeaDAO.load(Articolo.class, parametriCalcoloPrezzi.getIdArticolo());
        } catch (ObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
        risultatoPrezzo.setNumeroDecimali(
                articolo.getNumeroDecimaliPrezzo() != null ? articolo.getNumeroDecimaliPrezzo() : new Integer(6));

        if (datiUltimoCosto != null) {

            risultatoPrezzo.setValue((BigDecimal) datiUltimoCosto[0]);
        } else {
            risultatoPrezzo.setValue(BigDecimal.ZERO);
        }
        // Carico l'articolo per trovare il numero di decimali settati.

        parametriCalcoloPrezzi.getPoliticaPrezzo().getPrezzi().put(0.0, risultatoPrezzo);
        return parametriCalcoloPrezzi;
    }

    @Override
    public Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi) {
        return new HashSet<ArticoloLite>();
    }
}
