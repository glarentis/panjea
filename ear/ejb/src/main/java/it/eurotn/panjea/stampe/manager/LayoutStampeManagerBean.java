package it.eurotn.panjea.stampe.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseDdt;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseMovimentoMagazzinoGenerico;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseOrdine;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClassePreventivo;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.panjea.stampe.manager.interfaces.LayoutStampeManager;
import it.eurotn.panjea.stampe.util.LayoutStampaFactory;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(mappedName = "Panjea.LayoutStampeManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LayoutStampeManager")
public class LayoutStampeManagerBean implements LayoutStampeManager {

    private static Logger logger = Logger.getLogger(LayoutStampeManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public LayoutStampa aggiungiLayoutStampa(ITipoAreaDocumento tipoAreaDocumento, String reportName, EntitaLite entita,
            SedeEntita sedeEntita) {
        logger.debug("--> Enter aggiungiLayoutStampa");

        LayoutStampa layoutStampa = LayoutStampaFactory.create(tipoAreaDocumento, reportName, entita, sedeEntita);
        layoutStampa = salvaLayoutStampa(layoutStampa);

        logger.debug("--> Exit aggiungiLayoutStampa");
        return layoutStampa;
    }

    @Override
    public void cancellaLayoutStampa(ITipoAreaDocumento tipoArea) {
        logger.debug("--> Enter cancellaLayoutStampa");

        Query query = panjeaDAO
                .prepareQuery("delete from LayoutStampaDocumento lsd where lsd.tipoAreaDocumento =:paramTipoArea");
        try {
            query.setParameter("paramTipoArea", tipoArea);
            query.executeUpdate();
        } catch (Exception e) {
            logger.error("--> errore durante la cancellazione del layout di stampa associato al tipo area.", e);
            throw new RuntimeException("errore durante la cancellazione del layout di stampa associato al tipo area.",
                    e);
        }

        logger.debug("--> Exit cancellaLayoutStampa");
    }

    @Override
    public void cancellaLayoutStampa(LayoutStampa layoutStampa) {
        logger.debug("--> Enter cancellaLayoutStampa");
        try {
            panjeaDAO.delete(layoutStampa);
        } catch (Exception e) {
            logger.error("--> errore durante la cancellazione del layout di stampa " + layoutStampa.getId(), e);
            throw new RuntimeException("errore durante la cancellazione del layout di stampa " + layoutStampa.getId(),
                    e);
        }
        logger.debug("--> Exit cancellaLayoutStampa");
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampaBatch(ITipoAreaDocumento tipoAreaDocumento, EntitaLite entita,
            SedeEntita sedeEntita) {
        return caricaLayoutStampeDocumento(tipoAreaDocumento, entita, sedeEntita, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampaPerDocumenti() {
        Query query = panjeaDAO.prepareNamedQuery("LayoutStampaDocumento.caricaAll");
        List<LayoutStampaDocumento> result = null;
        try {
            result = panjeaDAO.getResultList(query);
            for (LayoutStampaDocumento layoutStampa : result) {
                if (layoutStampa.getTipoAreaDocumento() != null) {
                    layoutStampa.getTipoDocumento().getId();
                }
            }
        } catch (DAOException e) {
            logger.error("-->Errore nel caricare i layoutStampe", e);
            throw new RuntimeException("Errore nel caricare i layoutStampe", e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LayoutStampa> caricaLayoutStampe() {
        Query query = panjeaDAO.prepareNamedQuery("LayoutStampa.caricaAll");
        List<LayoutStampa> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->Errore nel caricare i layoutStampe", e);
            throw new RuntimeException("Errore nel caricare i layoutStampe", e);
        }
        return result;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampe(final Integer idEntita) {
        if (idEntita == null) {
            throw new IllegalArgumentException("Id entità null");
        }
        List<LayoutStampaDocumento> layoutStampe = caricaLayoutStampaPerDocumenti();
        CollectionUtils.filter(layoutStampe, new Predicate() {

            @Override
            public boolean evaluate(Object paramObject) {
                LayoutStampaDocumento layout = (LayoutStampaDocumento) paramObject;
                return layout.getEntita() != null && layout.getEntita().getId().equals(idEntita);
            }
        });

        return layoutStampe;
    }

    @Override
    public List<LayoutStampaDocumento> caricaLayoutStampe(final ITipoAreaDocumento tipoAreaDocumento,
            final EntitaLite entita, final SedeEntita sedeEntita) {
        return caricaLayoutStampeDocumento(tipoAreaDocumento, entita, sedeEntita, null);
    }

    @Override
    public LayoutStampa caricaLayoutStampe(String reportName) {
        Query queryLayout = panjeaDAO.prepareQuery("select l from LayoutStampa l where l.reportName=:reportName");
        queryLayout.setParameter("reportName", reportName);
        LayoutStampa layoutStampa = null;
        try {
            @SuppressWarnings("unchecked")
            List<LayoutStampa> layouts = panjeaDAO.getResultList(queryLayout);
            if (!layouts.isEmpty()) {
                layoutStampa = layouts.get(0);
            }
        } catch (DAOException e) {
            logger.error("-->errore nel recuperare il layout stampa con nome " + reportName, e);
            throw new RuntimeException(e);
        }
        return layoutStampa;
    }

    private List<LayoutStampaDocumento> caricaLayoutStampeDocumento(final ITipoAreaDocumento tipoAreaDocumento,
            final EntitaLite entita, final SedeEntita sedeEntita, final Boolean stampaBatch) {
        if (tipoAreaDocumento == null || tipoAreaDocumento.getId() == null) {
            return new ArrayList<>();
        }

        List<LayoutStampaDocumento> layoutStampe = caricaLayoutStampaPerDocumenti();
        CollectionUtils.filter(layoutStampe, new Predicate() {

            @Override
            public boolean evaluate(Object paramObject) {

                LayoutStampaDocumento layout = (LayoutStampaDocumento) paramObject;

                if (layout.getTipoAreaDocumento() == null) {
                    return false;
                }

                if (!layout.getTipoAreaDocumento().getClass().getName()
                        .startsWith(tipoAreaDocumento.getClass().getName())) {
                    return false;
                }

                if (stampaBatch != null) {
                    Boolean batch = layout.getBatch();
                    if (batch == null || (batch != null && !stampaBatch.equals(batch))) {
                        return false;
                    }
                }

                if (!tipoAreaDocumento.getId().equals(layout.getTipoAreaDocumento().getId())) {
                    return false;
                }

                if (layout.getSedeEntita() != null) {
                    if (sedeEntita == null) {
                        return false;
                    }
                    return layout.getSedeEntita().getId().equals(sedeEntita.getId());
                }

                if (layout.getEntita() != null) {
                    if (entita == null) {
                        return false;
                    }
                    return layout.getEntita().getId().equals(entita.getId());
                }
                return true;
            }
        });
        Collections.sort(layoutStampe, new Comparator<LayoutStampaDocumento>() {

            @Override
            public int compare(LayoutStampaDocumento l1, LayoutStampaDocumento l2) {
                Integer peso1 = 0;
                if (l1.getPredefinito()) {
                    peso1 += 1;
                }
                if (l1.getEntita() != null) {
                    peso1 += 2;
                }
                if (l1.getSedeEntita() != null) {
                    peso1 += 3;
                }
                Integer peso2 = 0;
                if (l2.getPredefinito()) {
                    peso2 += 1;
                }
                if (l2.getEntita() != null) {
                    peso2 += 2;
                }
                if (l2.getSedeEntita() != null) {
                    peso2 += 3;
                }
                return peso2.compareTo(peso1);
            }
        });
        return layoutStampe;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ITipoAreaDocumento> caricaTipoAree(String classeTipoDocumento) {
        logger.debug("--> Enter caricaTipoAree");

        List<ITipoAreaDocumento> tipiAree = new ArrayList<ITipoAreaDocumento>();

        String tipoAreaDocumento = getNomeTipoAreaDocumento(classeTipoDocumento);

        if (tipoAreaDocumento != null) {
            Query query = panjeaDAO.prepareQuery("select distinct tad from " + tipoAreaDocumento
                    + " tad inner join fetch tad.tipoDocumento tipoDoc where tipoDoc.classeTipoDocumento = :classeTipoDoc");
            query.setParameter("classeTipoDoc", classeTipoDocumento);

            try {
                tipiAree = panjeaDAO.getResultList(query);
            } catch (Exception e) {
                logger.error("--> errore durante il caricamento dei tipi area", e);
                throw new RuntimeException("errore durante il caricamento dei tipi area", e);
            }
        }

        logger.debug("--> Exit caricaTipoAree");
        return tipiAree;
    }

    @Override
    public void cloneLayoutStampa(ITipoAreaDocumento tipoAreaDocumentoDestinazione,
            ITipoAreaDocumento tipoAreaDocumentoOrigine) {
        List<LayoutStampaDocumento> layoutsToClone = caricaLayoutStampe(tipoAreaDocumentoOrigine, null, null);
        for (LayoutStampaDocumento layoutToClone : layoutsToClone) {
            LayoutStampaDocumento layout = new LayoutStampaDocumento();
            PanjeaEJBUtil.copyProperties(layout, layoutToClone);
            layout.setId(null);
            salvaLayoutStampa(layout);
        }
    }

    private String getNomeTipoAreaDocumento(String classeTipoDocumento) {
        String tipoareaDocumento = null;

        if (ClasseOrdine.class.getName().equals(classeTipoDocumento)) {
            tipoareaDocumento = "TipoAreaOrdine";
        } else if (ClassePreventivo.class.getName().equals(classeTipoDocumento)) {
            tipoareaDocumento = "TipoAreaPreventivo";
        } else if (ClasseDdt.class.getName().equals(classeTipoDocumento)
                || ClasseFattura.class.getName().equals(classeTipoDocumento)
                || ClasseMovimentoMagazzinoGenerico.class.getName().equals(classeTipoDocumento)) {
            tipoareaDocumento = "TipoAreaMagazzino";
        }

        return tipoareaDocumento;
    }

    @Override
    public LayoutStampa salvaLayoutStampa(LayoutStampa layoutStampa) {
        logger.debug("--> Enter salvaLayoutStampa");
        LayoutStampa layoutStampaSalvato = null;
        try {
            if (layoutStampa.isNew()) {
                // se è un layout stampa verifico se è già presente uno legato al report
                // specificato, altrimenti se si
                // tratta di un layout stampa documento devo verificare tutta la chiave di dominio
                LayoutStampa layoutPresente = null;
                if (layoutStampa instanceof LayoutStampaDocumento) {
                    LayoutStampaDocumento layout = (LayoutStampaDocumento) layoutStampa;
                    List<LayoutStampaDocumento> layouts = caricaLayoutStampe(layout.getTipoAreaDocumento(),
                            layout.getEntita(), layout.getSedeEntita());
                    for (LayoutStampaDocumento layoutDocumento : layouts) {
                        boolean entSedeEquals = Objects.equals(layoutDocumento.getSedeEntita(), layout.getSedeEntita())
                                && Objects.equals(layoutDocumento.getEntita(), layout.getEntita());
                        if (entSedeEquals && layoutStampa.getReportName().equals(layoutDocumento.getReportName())) {
                            layoutPresente = layoutDocumento;
                            break;
                        }
                    }
                } else {
                    layoutPresente = caricaLayoutStampe(layoutStampa.getReportName());
                }

                // trovato un layout di stampa che rispecchia già la chiave di dominio quindi non lo
                // salvo e lo
                // restituisco
                if (layoutPresente != null) {
                    layoutStampa = layoutPresente;
                }
            }
            layoutStampaSalvato = panjeaDAO.save(layoutStampa);
            if (layoutStampaSalvato instanceof LayoutStampaDocumento
                    && ((LayoutStampaDocumento) layoutStampaSalvato).getTipoDocumento() != null) {
                ((LayoutStampaDocumento) layoutStampaSalvato).getTipoDocumento().getId();
            }
        } catch (Exception e) {
            logger.error("--> errore durante salvaLayoutStampa del layout di stampa " + layoutStampa.getId(), e);
            throw new RuntimeException(
                    "errore durante la salvaLayoutStampa del layout di stampa " + layoutStampa.getId(), e);
        }

        logger.debug("--> Exit salvaLayoutStampa");
        return layoutStampaSalvato;
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutAsDefault(LayoutStampaDocumento layoutStampa) {
        logger.debug("--> Enter setLayoutAsDefault");

        Set<LayoutStampaDocumento> layoutSalvati = new TreeSet<LayoutStampaDocumento>();

        // carico i layout del tipo area e tolgo il layout predefinito esistente
        List<LayoutStampaDocumento> layouts = caricaLayoutStampe(layoutStampa.getTipoAreaDocumento(),
                layoutStampa.getEntita(), layoutStampa.getSedeEntita());

        // solo se il layout che non hanno entità gestiscono il flag "predefinito"
        if (layoutStampa.getEntita() != null) {
            layoutSalvati.addAll(layouts);
        } else {
            for (LayoutStampaDocumento layout : layouts) {
                if (layout.getPredefinito()) {
                    layout.setPredefinito(false);
                    layout = (LayoutStampaDocumento) salvaLayoutStampa(layout);
                }
                if (layout.equals(layoutStampa)) {
                    layout.setPredefinito(true);
                    layoutSalvati.add((LayoutStampaDocumento) salvaLayoutStampa(layout));
                }
                layoutSalvati.add(layout);
            }
        }

        logger.debug("--> Exit setLayoutAsDefault");
        return new ArrayList<LayoutStampaDocumento>(layoutSalvati);
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutForInvioMail(LayoutStampaDocumento layoutStampa) {
        logger.debug("--> Enter setLayoutForInvioMail");

        Set<LayoutStampaDocumento> layoutSalvati = new TreeSet<LayoutStampaDocumento>();

        // carico i layout del tipo area e tolgo il layout predefinito per l'invio delle email
        // esistente
        List<LayoutStampaDocumento> layouts = caricaLayoutStampe(layoutStampa.getTipoAreaDocumento(),
                layoutStampa.getEntita(), layoutStampa.getSedeEntita());

        for (LayoutStampaDocumento layout : layouts) {
            if (Objects.equals(layout.getEntita(), layoutStampa.getEntita())
                    && Objects.equals(layout.getSedeEntita(), layoutStampa.getSedeEntita())) {
                if (layout.isMailLayout()) {
                    layout.setMailLayout(false);
                    layout = (LayoutStampaDocumento) salvaLayoutStampa(layout);
                }
                if (layout.equals(layoutStampa)) {
                    layout.setMailLayout(true);
                }
                layoutSalvati.add((LayoutStampaDocumento) salvaLayoutStampa(layout));
            } else {
                layoutSalvati.add(layout);
            }
        }
        // }

        logger.debug("--> Exit setLayoutForInvioMail");
        return new ArrayList<LayoutStampaDocumento>(layoutSalvati);
    }

    @Override
    public List<LayoutStampaDocumento> setLayoutForUsoInterno(LayoutStampaDocumento layoutStampa, boolean usoInterno) {
        logger.debug("--> Enter setLayoutForUsoInterno");

        Set<LayoutStampaDocumento> layoutSalvati = new TreeSet<LayoutStampaDocumento>();

        // carico i layout del tipo area e tolgo il layout predefinito per luso interno
        List<LayoutStampaDocumento> layouts = caricaLayoutStampe(layoutStampa.getTipoAreaDocumento(),
                layoutStampa.getEntita(), layoutStampa.getSedeEntita());

        for (LayoutStampaDocumento layout : layouts) {
            if (Objects.equals(layout.getEntita(), layoutStampa.getEntita())
                    && Objects.equals(layout.getSedeEntita(), layoutStampa.getSedeEntita())) {
                if (layout.isInterno()) {
                    layout.setInterno(false);
                    layout = (LayoutStampaDocumento) salvaLayoutStampa(layout);
                }
                if (layout.equals(layoutStampa)) {
                    layout.setInterno(usoInterno);
                }
                layoutSalvati.add((LayoutStampaDocumento) salvaLayoutStampa(layout));
            } else {
                layoutSalvati.add(layout);
            }
        }

        logger.debug("--> Exit setLayoutForUsoInterno");
        return new ArrayList<LayoutStampaDocumento>(layoutSalvati);
    }
}
