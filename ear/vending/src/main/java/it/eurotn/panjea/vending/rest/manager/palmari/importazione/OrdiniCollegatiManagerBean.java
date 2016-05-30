package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigheCollegateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.OrdiniCollegatiManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.PagamentiManager;

@Stateless(name = "Panjea.OrdiniCollegatiManagerManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OrdiniCollegatiManagerManager")
public class OrdiniCollegatiManagerBean implements OrdiniCollegatiManager {
    private static final Logger LOGGER = Logger.getLogger(OrdiniCollegatiManagerBean.class);

    @EJB
    private AreaOrdineManager areaOrdineManager;

    @EJB
    private RigheCollegateManager righeCollegateManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private PagamentiManager pagamentiManager;

    private Map<String, RigaArticolo> caricaRigheFatturaCollegateByArticolo(Set<RigaOrdine> righe) {
        Map<String, RigaArticolo> result = new HashMap<>();
        for (RigaOrdine rigaOrdine : righe) {
            if (rigaOrdine instanceof it.eurotn.panjea.ordini.domain.RigaArticolo) {
                it.eurotn.panjea.ordini.domain.RigaArticolo rigaArticolo = (it.eurotn.panjea.ordini.domain.RigaArticolo) rigaOrdine;
                List<RigaDestinazione> righeCollegate = righeCollegateManager.caricaRigheCollegate(rigaArticolo);
                // L'evasione dell'ordine DEVE sempre essere eseguito 1->1 sulle righe. Quindi se ho
                // righe collegate prendo
                // sempre la prima (teoricamente ho solo quella)
                if (!CollectionUtils.isEmpty(righeCollegate)) {
                    RigaMagazzino rm = new RigaArticolo();
                    rm.setId(righeCollegate.get(0).getIdRiga());
                    RigaArticolo rigaArticoloFattura = (RigaArticolo) rigaMagazzinoManager.getDao()
                            .caricaRigaMagazzino(rm);
                    result.put(rigaArticolo.getArticolo().getId() + "#" + rigaArticolo.getTipoOmaggio().ordinal(),
                            rigaArticoloFattura);
                }
            }
        }
        return result;
    }

    @Override
    public void collega(Integer idAreaOrdine, AreaRifornimento areaRifornimento) {
        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setId(idAreaOrdine);
        areaOrdine = areaOrdineManager.caricaAreaOrdine(areaOrdine);

        List<RigaMagazzino> righeRifornimento = areaRifornimento.getAreaMagazzino().getRigheMagazzino();

        Map<String, RigaArticolo> righeRifornimentoByArticolo = raggruppaRigheMagazzinoByArticolo(righeRifornimento);
        Map<String, it.eurotn.panjea.ordini.domain.RigaArticolo> righeOrdineByArticolo = raggruppaRigheOrdineByArticolo(
                areaOrdine.getRighe());
        Map<String, RigaArticolo> righeFatturaByArticolo = caricaRigheFatturaCollegateByArticolo(areaOrdine.getRighe());

        for (it.eurotn.panjea.ordini.domain.RigaArticolo rigaArticoloOrdine : righeOrdineByArticolo.values()) {
            RigaArticolo rigaArticoloFattura = righeFatturaByArticolo.get(
                    rigaArticoloOrdine.getArticolo().getId() + "#" + rigaArticoloOrdine.getTipoOmaggio().ordinal());
            RigaArticolo rigaArticoloRifornimento = righeRifornimentoByArticolo.get(
                    rigaArticoloOrdine.getArticolo().getId() + "#" + rigaArticoloOrdine.getTipoOmaggio().ordinal());
            // Sposto un'eventuale fattura legata all'ordine sul rifornimento
            if (rigaArticoloFattura != null && rigaArticoloRifornimento != null) {
                rigaArticoloFattura.setAreaCollegata(areaRifornimento.getAreaMagazzino());
                rigaArticoloFattura.setRigaMagazzinoCollegata(rigaArticoloRifornimento);
                rigaArticoloFattura.setRigaOrdineCollegata(null);
                try {
                    panjeaDAO.save(rigaArticoloFattura);
                } catch (DAOException e) {
                    LOGGER.error("-->errore nello spostare il collegamento della riga fattura sul rifornimento", e);
                    throw new GenericException(
                            "-->errore nello spostare il collegamento della riga fattura sul rifornimento", e);
                }
            }
            if (rigaArticoloRifornimento != null) {
                rigaArticoloRifornimento.setAreaCollegata(areaOrdine);
                rigaArticoloRifornimento.setRigaOrdineCollegata(rigaArticoloOrdine);
                try {
                    panjeaDAO.save(rigaArticoloRifornimento);
                } catch (DAOException e) {
                    LOGGER.error("-->errore nel salvare l'ordine collegato al rifornimento", e);
                    throw new GenericException("-->errore nel salvare l'ordine collegato al rifornimento", e);
                }
            }
        }
        if (!righeFatturaByArticolo.isEmpty()) {
            // l'ordine non può essere evaso su più fatture quindi prendo la prima riga legata.
            AreaMagazzino fattura = righeFatturaByArticolo.values().iterator().next().getAreaMagazzino();
            pagamentiManager.creaPagamenti(fattura, areaRifornimento.getIncasso());
        }
    }

    private Map<String, RigaArticolo> raggruppaRigheMagazzinoByArticolo(List<RigaMagazzino> righeRifornimento) {
        Map<String, RigaArticolo> result = new HashMap<>();
        for (RigaMagazzino rigaMagazzino : righeRifornimento) {
            if (rigaMagazzino instanceof RigaArticolo) {
                RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzino;
                result.put(rigaArticolo.getArticolo().getId() + "#" + rigaArticolo.getTipoOmaggio().ordinal(),
                        rigaArticolo);
            }
        }
        return result;
    }

    private Map<String, it.eurotn.panjea.ordini.domain.RigaArticolo> raggruppaRigheOrdineByArticolo(
            Set<RigaOrdine> righe) {
        Map<String, it.eurotn.panjea.ordini.domain.RigaArticolo> result = new HashMap<>();
        for (RigaOrdine rigaOrdine : righe) {
            if (rigaOrdine instanceof it.eurotn.panjea.ordini.domain.RigaArticolo) {
                it.eurotn.panjea.ordini.domain.RigaArticolo rigaArticolo = (it.eurotn.panjea.ordini.domain.RigaArticolo) rigaOrdine;
                result.put(rigaArticolo.getArticolo().getId() + "#" + rigaArticolo.getTipoOmaggio().ordinal(),
                        rigaArticolo);
            }
        }
        return result;
    }

}
