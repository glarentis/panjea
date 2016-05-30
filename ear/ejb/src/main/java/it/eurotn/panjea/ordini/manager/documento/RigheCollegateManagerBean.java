package it.eurotn.panjea.ordini.manager.documento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigheCollegateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.RigheOdineCollegateManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheOdineCollegateManager")
public class RigheCollegateManagerBean implements RigheCollegateManager {

    private static final Logger LOGGER = Logger.getLogger(RigheCollegateManagerBean.class);

    private static Logger logger = Logger.getLogger(RigheCollegateManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public void cancellaRigheCollegate(int rigaOrdineOrigine, int rigaOrdineDestinazione) {
        LOGGER.debug("--> Enter cancellaRigheCollegate");
        System.out.println(rigaOrdineOrigine + " : " + rigaOrdineDestinazione);
        SQLQuery query = panjeaDAO.prepareNativeQuery(
                "delete from ordi_righe_ordine_ordi_righe_ordine where ordi_righe_ordine_id=:rigaOrigine and righeOrdineCollegate_id=:rigaDestinazione");
        query.setParameter("rigaOrigine", rigaOrdineOrigine);
        query.setParameter("rigaDestinazione", rigaOrdineDestinazione);
        query.executeUpdate();

        // Giro la riga origine,destinazione per cancellare eventuali link doppi
        query.setParameter("rigaOrigine", rigaOrdineDestinazione);
        query.setParameter("rigaDestinazione", rigaOrdineOrigine);
        query.executeUpdate();

        LOGGER.debug("--> Exit cancellaRigheCollegate");
    }

    /**
     *
     * @param rigaOrdine
     *            .
     * @return righe ordine collegate alla riga ordine
     */
    @SuppressWarnings("unchecked")
    private List<RigaDestinazione> caricaRigaOrdineCollegateOrigine(RigaOrdine rigaOrdine) {
        StringBuilder sb = new StringBuilder();
        sb.append("select doc.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("doc.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("doc.tipoDocumento.id as idTipoDocumento, ");
        sb.append("doc.codice as numeroDocumento, ");
        sb.append("doc.dataDocumento as dataDocumento, ");
        sb.append("1 as canDeleteInt, ");
        sb.append("doc.id as idDocumento, ");
        sb.append("rigac.id as idRiga, ");
        sb.append("rigac.areaOrdine.id as idAreaOrdine, ");
        sb.append("art.codice as codiceArticolo, ");
        sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("rigac.qta as quantita, ");
        sb.append("rigac.numeroDecimaliQta as numeroDecimaliQta ");
        sb.append("from RigaArticoloOrdine ra ");
        sb.append("inner join ra.righeOrdineCollegate rigac ");
        sb.append("inner join rigac.articolo art inner join rigac.areaOrdine.documento doc ");
        sb.append("where ra.id = :paramIdRiga ");
        sb.append("order by doc.dataDocumento,doc.tipoDocumento.codice,doc.codice.codiceOrder ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((RigaDestinazione.class)));
        query.setParameter("paramIdRiga", rigaOrdine.getId());
        List<RigaDestinazione> righeDest = new ArrayList<RigaDestinazione>();
        try {
            righeDest = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(),
                    e);
            throw new RuntimeException(
                    "errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(), e);
        }
        return righeDest;
    }

    /**
     *
     * @param rigaOrdine
     *            .
     * @return righe preventivo collegate alla riga ordine
     */
    @SuppressWarnings("unchecked")
    private List<RigaDestinazione> caricaRigaPreventivoCollegate(RigaOrdine rigaOrdine) {
        StringBuilder sb = new StringBuilder();
        sb.append("select doc.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("doc.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("doc.tipoDocumento.id as idTipoDocumento, ");
        sb.append("doc.codice as numeroDocumento, ");
        sb.append("doc.dataDocumento as dataDocumento, ");
        sb.append("doc.id as idDocumento, ");
        sb.append("ra.rigaPreventivoCollegata.id as idRiga, ");
        sb.append("ra.rigaPreventivoCollegata.areaPreventivo.id as idAreaPreventivo, ");
        sb.append("art.codice as codiceArticolo, ");
        sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("ra.rigaPreventivoCollegata.qta as quantita, ");
        sb.append("ra.rigaPreventivoCollegata.numeroDecimaliQta as numeroDecimaliQta ");
        sb.append(
                "from RigaArticoloOrdine ra inner join ra.rigaPreventivoCollegata.articolo art inner join ra.rigaPreventivoCollegata.areaPreventivo.documento doc ");
        sb.append("where ra = :paramRiga  ");
        sb.append("order by doc.dataDocumento,doc.tipoDocumento.codice,doc.codice.codiceOrder ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((RigaDestinazione.class)));
        query.setParameter("paramRiga", rigaOrdine);
        List<RigaDestinazione> righeDest = new ArrayList<RigaDestinazione>();
        try {
            righeDest = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(),
                    e);
            throw new RuntimeException(
                    "errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(), e);
        }
        return righeDest;
    }

    @Override
    public List<RigaDestinazione> caricaRigheCollegate(RigaOrdine rigaOrdine) {
        List<RigaDestinazione> result = caricaRigheMagazzinoCollegate(rigaOrdine);
        result.addAll(caricaRigheOrdineCollegateDestinazione(rigaOrdine));
        result.addAll(caricaRigaOrdineCollegateOrigine(rigaOrdine));
        result.addAll(caricaRigaPreventivoCollegate(rigaOrdine));
        return result;
    }

    /**
     *
     * @param rigaOrdine
     *            .
     * @return righedi magazzino collegate
     */
    @SuppressWarnings("unchecked")
    private List<RigaDestinazione> caricaRigheMagazzinoCollegate(RigaOrdine rigaOrdine) {

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
        sb.append("where ra.rigaOrdineCollegata = :paramRiga ");
        sb.append("order by doc.dataDocumento,doc.tipoDocumento.codice,doc.codice.codiceOrder ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((RigaDestinazione.class)));

        query.setParameter("paramRiga", rigaOrdine);

        try {
            righeDest = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(),
                    e);
            throw new RuntimeException(
                    "errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(), e);
        }
        return righeDest;
    }

    /**
     *
     * @param rigaOrdine
     *            rigaOrdine
     * @return righe Ordine collegata
     */
    @SuppressWarnings("unchecked")
    private List<RigaDestinazione> caricaRigheOrdineCollegateDestinazione(RigaOrdine rigaOrdine) {
        StringBuilder sb = new StringBuilder();
        sb.append("select doc.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("doc.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("doc.tipoDocumento.id as idTipoDocumento, ");
        sb.append("doc.codice as numeroDocumento, ");
        sb.append("doc.dataDocumento as dataDocumento, ");
        sb.append("1 as canDeleteInt, ");
        sb.append("doc.id as idDocumento, ");
        sb.append("ra.id as idRiga, ");
        sb.append("ra.areaOrdine.id as idAreaOrdine, ");
        sb.append("art.codice as codiceArticolo, ");
        sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("ra.qta as quantita, ");
        sb.append("ra.numeroDecimaliQta as numeroDecimaliQta ");
        sb.append("from RigaArticoloOrdine ra ");
        sb.append(
                "inner join ra.righeOrdineCollegate rigac inner join ra.articolo art inner join ra.areaOrdine.documento doc ");
        sb.append("where rigac.id = :paramIdRiga ");
        sb.append("and ((art.distinta=true and  ra.class != RigaArticolo ) or (ra.class=RigaArticolo)) ");
        sb.append("order by doc.dataDocumento,doc.tipoDocumento.codice,doc.codice.codiceOrder ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((RigaDestinazione.class)));
        query.setParameter("paramIdRiga", rigaOrdine.getId());
        List<RigaDestinazione> righeDest = new ArrayList<RigaDestinazione>();
        try {
            righeDest = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(),
                    e);
            throw new RuntimeException(
                    "errore durante il caricamento delle righe destinazione della riga " + rigaOrdine.getId(), e);
        }
        return righeDest;
    }

}
