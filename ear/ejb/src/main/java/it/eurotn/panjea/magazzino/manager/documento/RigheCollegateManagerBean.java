package it.eurotn.panjea.magazzino.manager.documento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.SortedList;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigheCollegateManager;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.RigheCollegateManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheCollegateManager")
public class RigheCollegateManagerBean implements RigheCollegateManager {

    private static Logger logger = Logger.getLogger(RigheCollegateManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    /**
     * Cancella le righe collegata alla riga di riferimento.
     *
     * @param rigaMagazzino
     *            riga di riferimento
     */
    private void cancellaRigaCollegata(RigaMagazzino rigaMagazzino) {
        logger.debug("--> Enter cancellaRigaCollegata");

        try {
            panjeaDAO.delete(rigaMagazzino);
        } catch (Exception e) {
            logger.error("--> Errore durante la cancellazione della riga magazzino.", e);
            throw new RuntimeException("Errore durante la cancellazione della riga magazzino.", e);
        }

        logger.debug("--> Exit cancellaRigaCollegata");
    }

    /**
     * Carica la riga magazzino che è collegata alla riga passata come parametro.
     *
     * @param rigaCollegata
     *            riga di riferimento
     * @return riga magazzino caricata
     */
    private RigaMagazzino caricaRigaMagazzinoDaRigaCollegata(RigaMagazzino rigaCollegata) {
        logger.debug("--> Enter caricaRigaMagazzinoDaRigaCollegata");

        Query query = panjeaDAO.prepareNamedQuery("RigaMagazzino.caricaByRigaCollegata");
        query.setParameter("paramRigaMagazzinoCollegataId", rigaCollegata.getId());

        RigaMagazzino rigaMagazzino = null;

        try {
            rigaMagazzino = (RigaMagazzino) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            logger.error(
                    "--> Errore, la riga " + rigaCollegata.getId() + " non risulta essere collegata a nessuna riga", e);
            throw new RuntimeException("Errore, la riga " + rigaCollegata.getId()
                    + " non risulta essere collegata a nessuna riga magazzino", e);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento della riga magazzino.", e);
            throw new RuntimeException("Errore durante il caricamento della riga magazzino.", e);
        }

        logger.debug("--> Exit caricaRigaMagazzinoDaRigaCollegata");
        return rigaMagazzino;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaDestinazione> caricaRigheMagazzinoCollegate(RigaMagazzino rigaMagazzino) {
        List<RigaDestinazione> righeDest = new ArrayList<RigaDestinazione>();

        StringBuilder sb = new StringBuilder();
        sb.append("select doc.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("doc.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("doc.tipoDocumento.id as idTipoDocumento, ");
        sb.append("doc.codice as numeroDocumento, ");
        sb.append("doc.dataDocumento as dataDocumento, ");
        sb.append("doc.id as idDocumento, ");
        sb.append("ra.id as idRiga, ");
        sb.append("ra.areaMagazzino.id as idAreaMagazzino, ");
        sb.append("art.codice as codiceArticolo, ");
        sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("ra.qta as quantita, ");
        sb.append("ra.numeroDecimaliQta as numeroDecimaliQta ");
        sb.append("from RigaArticolo ra inner join ra.articolo art inner join ra.areaMagazzino.documento doc ");
        sb.append("where ra.rigaMagazzinoCollegata = :paramRiga ");
        sb.append("order by doc.dataDocumento,doc.tipoDocumento.codice,doc.codice.codiceOrder ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((RigaDestinazione.class)));
        query.setParameter("paramRiga", rigaMagazzino);

        try {
            righeDest = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error(
                    "--> errore durante il caricamento delle righe destinazione della riga " + rigaMagazzino.getId(),
                    e);
            throw new RuntimeException(
                    "errore durante il caricamento delle righe destinazione della riga " + rigaMagazzino.getId(), e);
        }

        return righeDest;
    }

    @Override
    public void collegaRigaMagazzino(List<RigaMagazzino> righeDaCollegare, AreaMagazzino areaMagazzino,
            boolean calcolaOrdinamento) {

        Calendar calendarInizio = Calendar.getInstance();

        Map<RigaMagazzino, List<RigaMagazzino>> righeMap = new HashMap<RigaMagazzino, List<RigaMagazzino>>();

        // Carico le righe del documento di destinazione solo se ha la versione
        // maggiore di 0 altrimenti significa che è stato appena creato e non ha
        // righe
        if (areaMagazzino.getVersion() >= 0) {
            List<? extends RigaMagazzino> listRigheMagazzino = rigaMagazzinoManager.getDao()
                    .caricaRigheMagazzino(areaMagazzino);

            RigaMagazzino rigaMagazzinoLv0 = null;
            for (RigaMagazzino rigaMagazzino : listRigheMagazzino) {
                // se la riga è di livello 0 la inserisco come chiave della
                // mappa
                if (rigaMagazzino.getLivello() == 0) {
                    rigaMagazzinoLv0 = rigaMagazzino;
                    righeMap.put(rigaMagazzinoLv0, new ArrayList<RigaMagazzino>());
                } else {
                    // altrimenti aggiungo la riga alla lista della riga a
                    // livello 0
                    List<RigaMagazzino> list = righeMap.get(rigaMagazzinoLv0);
                    list.add(rigaMagazzino);
                    righeMap.put(rigaMagazzinoLv0, list);
                }
            }
        }

        // raggruppo le righe in base alla loro area
        int countAreeForFlush = 0;
        double ordinamento = 0;
        List<RigaMagazzino> listTmp = new ArrayList<RigaMagazzino>();
        Integer idarea = null;
        for (RigaMagazzino rigaMagazzino : righeDaCollegare) {

            if (idarea == null || rigaMagazzino.getAreaMagazzino().getId().equals(idarea)) {
                listTmp.add(rigaMagazzino);
            } else {
                if (!listTmp.isEmpty()) {
                    listTmp.get(0).setOrdinamento(ordinamento);
                    creaRigheCollegatePerArea(righeMap, listTmp, listTmp.get(0).getAreaMagazzino(), areaMagazzino,
                            calcolaOrdinamento);
                    ordinamento = ordinamento + 10000;
                    countAreeForFlush++;

                    // faccio il flush ogni 100 aree create
                    if (countAreeForFlush % 100 == 0) {
                        panjeaDAO.getEntityManager().flush();
                    }
                }
                listTmp = new ArrayList<RigaMagazzino>();
                listTmp.add(rigaMagazzino);
            }

            idarea = rigaMagazzino.getAreaMagazzino().getId();

        }

        if (!listTmp.isEmpty()) {
            listTmp.get(0).setOrdinamento(ordinamento + 10000);
            creaRigheCollegatePerArea(righeMap, listTmp, listTmp.get(0).getAreaMagazzino(), areaMagazzino,
                    calcolaOrdinamento);

            panjeaDAO.getEntityManager().flush();
        }

        logger.debug("--> Exit collegaRigaMagazzino. Tempo di esecuzione: "
                + (Calendar.getInstance().getTimeInMillis() - calendarInizio.getTimeInMillis()));
    }

    /**
     * Crea le righe collegate per l'area magazzino.
     *
     * @param mapRighe
     *            righe
     * @param righeDaCollegare
     *            righe da collegare
     * @param areaMagazzinoOrigine
     *            area da collegare
     * @param areaMagazzinoDestinazione
     *            area di origine
     * @param calcolaOrdinamento
     *            <code>true</code> se si deve calcolare l'ordinamento delle righe da collegare
     */
    private void creaRigheCollegatePerArea(Map<RigaMagazzino, List<RigaMagazzino>> mapRighe,
            List<RigaMagazzino> righeDaCollegare, AreaMagazzino areaMagazzinoOrigine,
            AreaMagazzino areaMagazzinoDestinazione, boolean calcolaOrdinamento) {

        try {
            areaMagazzinoOrigine.getDocumento().setSedeEntita(
                    panjeaDAO.load(SedeEntita.class, areaMagazzinoOrigine.getDocumento().getSedeEntita().getId()));
        } catch (ObjectNotFoundException e2) {
            e2.printStackTrace();
        }

        int nrRigheDaCollegare = righeDaCollegare.size();

        Set<RigaMagazzino> righeChiave = mapRighe.keySet();

        RigaMagazzino rigaMagazzinoTrovata = null;

        // cerco all'interno della mappa se esiste una riga testata per l'area
        // magazzino
        for (RigaMagazzino rigaMagazzino : righeChiave) {
            if (rigaMagazzino.getAreaCollegata().equals(areaMagazzinoOrigine)) {
                rigaMagazzinoTrovata = rigaMagazzino;
            }
        }

        // se viene trovata la riga devo caricarmi tutte le sue righe per
        // aggiungerle alle righe da collegare.
        if (rigaMagazzinoTrovata != null) {
            for (RigaMagazzino rigaMagazzino : mapRighe.get(rigaMagazzinoTrovata)) {

                righeDaCollegare.add(caricaRigaMagazzinoDaRigaCollegata(rigaMagazzino));
                nrRigheDaCollegare++;
            }
        }

        // ordino le righe da collegare in base al loro ordinamento se ne sono
        // state inserite
        if (nrRigheDaCollegare != righeDaCollegare.size()) {
            Collections.sort(righeDaCollegare, new Comparator<RigaMagazzino>() {

                @Override
                public int compare(RigaMagazzino o1, RigaMagazzino o2) {
                    return new Double(o1.getOrdinamento()).compareTo(new Double(o2.getOrdinamento()));
                }
            });
        }

        // se esistevano delle righe collegate le cancello
        if (rigaMagazzinoTrovata != null) {
            for (RigaMagazzino rigaMagazzino : mapRighe.get(rigaMagazzinoTrovata)) {
                cancellaRigaCollegata(rigaMagazzino);
            }
            // le rimuovo dalla mappa
            mapRighe.put(rigaMagazzinoTrovata, new ArrayList<RigaMagazzino>());
        }

        // cerco l'ordinamento che deve avere la riga testata e le sue righe
        // collegate. Se ho già trovato
        // precedentemente la riga testata per il documento prendo il suo
        // ordinamento altrimenti cerco all'interno delle
        // chiavi della mappa la riga testata precedente in base alla data e al
        // codice del tipo documento.
        double ordinamento = 0;
        if (rigaMagazzinoTrovata != null) {
            ordinamento = rigaMagazzinoTrovata.getOrdinamento();
        } else {
            // creo la riga testata per l'area da collagare
            RigaTestata rigaTestata = new RigaTestata();
            rigaTestata.setAreaMagazzino(areaMagazzinoDestinazione);
            rigaTestata.setAreaMagazzinoCollegata(areaMagazzinoOrigine);
            rigaTestata.setCodiceTipoDocumentoCollegato(
                    areaMagazzinoOrigine.getDocumento().getTipoDocumento().getCodice());
            rigaTestata.setDataAreaMagazzinoCollegata(areaMagazzinoOrigine.getDataRegistrazione());
            rigaTestata.setLivello(0);
            rigaTestata.setDescrizione(rigaTestata.generaDescrizioneTestata());
            try {
                rigaTestata = panjeaDAO.saveWithoutFlush(rigaTestata);
            } catch (Exception e) {
                logger.error("--> errore durante il salvataggio della riga testata.", e);
                throw new RuntimeException("errore durante il salvataggio della riga testata.", e);
            }
            rigaMagazzinoTrovata = rigaTestata;

            // controllo se devo calcolare l'ordinamento oppure no
            if (calcolaOrdinamento) {
                BasicEventList<RigaMagazzino> basicEventList = new BasicEventList<RigaMagazzino>();
                basicEventList.addAll(mapRighe.keySet());
                basicEventList.add(rigaTestata);

                Comparator<RigaMagazzino> comparator = new Comparator<RigaMagazzino>() {
                    @Override
                    public int compare(RigaMagazzino o1, RigaMagazzino o2) {

                        if (!(o1 instanceof RigaTestata) || !(o2 instanceof RigaTestata)) {
                            return -1;
                        }

                        int compareCodTipoDoc = ((RigaTestata) o1).getCodiceTipoDocumentoCollegato()
                                .compareTo(((RigaTestata) o2).getCodiceTipoDocumentoCollegato());

                        if (compareCodTipoDoc != 0) {
                            return compareCodTipoDoc;
                        } else {
                            return ((RigaTestata) o1).getDataAreaMagazzinoCollegata()
                                    .compareTo(((RigaTestata) o2).getDataAreaMagazzinoCollegata());
                        }
                    }
                };

                SortedList<RigaMagazzino> list = new SortedList<RigaMagazzino>(basicEventList, comparator);

                int idxRigaTst = list.sortIndex(rigaTestata);
                double ordPrev = 0, ordNext = 0;

                if (idxRigaTst > 0) {
                    ordPrev = list.get(idxRigaTst - 1).getOrdinamento();
                    ordPrev = list.get(idxRigaTst - 1).getOrdinamento();
                }

                if (idxRigaTst + 1 < list.size()) {
                    ordNext = list.get(idxRigaTst + 1).getOrdinamento();
                }

                if (ordNext != ordPrev) {
                    ordinamento = (ordNext + ordPrev) / 2;
                } else {
                    ordinamento = ordPrev + 10000;
                }
            } else {
                // se non devo calcolarlo lo ottengo dalla riga magazzino da
                // collegare
                ordinamento = righeDaCollegare.get(0).getOrdinamento();
            }
        }

        // creo tutto l'albero delle righe
        rigaMagazzinoTrovata.setOrdinamento(ordinamento);
        try {
            rigaMagazzinoTrovata = panjeaDAO.saveWithoutFlush(rigaMagazzinoTrovata);
        } catch (Exception e1) {
            logger.error("--> errore durante il salvataggio della riga", e1);
            throw new RuntimeException("errore durante il salvataggio della riga", e1);
        }
        mapRighe.put(rigaMagazzinoTrovata, new ArrayList<RigaMagazzino>());
        HashMap<Integer, RigaTestata> righeTestata = new HashMap<Integer, RigaTestata>();

        for (RigaMagazzino rigaMagazzino : righeDaCollegare) {

            if (rigaMagazzino instanceof RigaNota && !rigaMagazzino.isNoteSuDestinazione()) {
                // in questo caso non porto l'intera riga nota sul documento di
                // destinazione
                continue;
            }
            ordinamento++;

            if (rigaMagazzino instanceof RigaArticolo
                    && ((RigaArticolo) rigaMagazzino).getNumeroRigheConaiComponente() > 0) {
                rigaMagazzino = panjeaDAO.loadLazy(RigaMagazzino.class, rigaMagazzino.getId());
            }

            RigaMagazzino rigaMagazzinoCreata = rigaMagazzino.creaRigaCollegata(areaMagazzinoDestinazione, ordinamento);

            // Devo assegnare alla riga magazzino appena creata la rispettiva riga testata.
            // Devo quindi verificare se la riga collegata è legata ad una riga testata
            // e cercare in questo documento la riga testata corrispondente nel nuovo documento.
            // Non è detto che abbia importato tutte le righe testata nel nuovo documento, quindi
            // devo gestire il caso
            // in cui c'è ed assegnare la riga alla riga testata principale.
            RigaTestata rigaTestataMagazzinoCollegata = null;
            if (rigaMagazzino.getRigaTestataMagazzinoCollegata() != null) {
                rigaTestataMagazzinoCollegata = righeTestata
                        .get(rigaMagazzino.getRigaTestataMagazzinoCollegata().getId());
            }

            if (rigaTestataMagazzinoCollegata != null) {
                rigaMagazzinoCreata.setRigaTestataMagazzinoCollegata(rigaTestataMagazzinoCollegata);
            } else {
                rigaMagazzinoCreata.setRigaTestataMagazzinoCollegata((RigaTestata) rigaMagazzinoTrovata);
            }

            // azzero le note se non devo riportarle sul documento di
            // destinazione
            if (!rigaMagazzino.isNoteSuDestinazione()) {
                rigaMagazzinoCreata.setNoteRiga(null);
            }
            try {
                rigaMagazzinoCreata = panjeaDAO.saveWithoutFlush(rigaMagazzinoCreata);
            } catch (Exception e) {
                logger.error("--> errore durante il salvataggio della riga magazzino.", e);
                throw new RuntimeException("errore durante il salvataggio della riga magazzino.", e);
            }

            if (rigaMagazzinoCreata instanceof RigaTestata) {
                righeTestata.put(rigaMagazzino.getId(), (RigaTestata) rigaMagazzinoCreata);
            }
        }

    }
}
