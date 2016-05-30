package it.eurotn.panjea.ordini.manager.documento.righeinserimento.loaders;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.impl.SQLQueryImpl;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.RigheInserimentoLoader;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(mappedName = "Panjea.RigheInserimentoOrdineLoaderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheInserimentoOrdineLoaderBean")
public class RigheInserimentoOrdineLoaderBean implements RigheInserimentoLoader {

    private static final Logger LOGGER = Logger.getLogger(RigheInserimentoUltimiOrdiniLoaderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaOrdineInserimento> caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri) {

        StringBuilder sb = new StringBuilder(300);
        sb.append("select ");
        sb.append("ro.articolo_id as idArticolo, ");
        sb.append("art.codice as codiceArticolo, ");
        sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
        sb.append("ao.dataRegistrazione as dataIniziale, ");
        sb.append("ao.dataRegistrazione as dataFinale, ");
        sb.append("sum(ro.qta) as qta, ");
        sb.append("group_concat(ro.id separator ',') as idRigheOrdine, ");
        sb.append("ro.numeroDecimaliQta as numeroDecimaliQuantita ");
        sb.append("from ordi_righe_ordine ro ");
        sb.append("inner join ordi_area_ordine as ao on ao.id = ro.areaOrdine_id ");
        sb.append("inner join maga_articoli art on art.id = ro.articolo_id ");
        sb.append("where ro.areaOrdine_id = ");
        sb.append(parametri.getIdAreaOrdine());
        sb.append(" group by ro.articolo_id ");

        SQLQueryImpl sqlQuery = (SQLQueryImpl) panjeaDAO.prepareNativeQuery(sb.toString(), RigaOrdineInserimento.class);
        sqlQuery.addScalar("idArticolo");
        sqlQuery.addScalar("codiceArticolo");
        sqlQuery.addScalar("descrizioneArticolo");
        sqlQuery.addScalar("dataIniziale");
        sqlQuery.addScalar("dataFinale");
        sqlQuery.addScalar("qta");
        sqlQuery.addScalar("idRigheOrdine", Hibernate.STRING);
        sqlQuery.addScalar("numeroDecimaliQuantita");

        List<RigaOrdineInserimento> righe = new ArrayList<RigaOrdineInserimento>();
        try {
            righe = sqlQuery.list();
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle righe ordine inserimento analisi.", e);
            throw new RuntimeException("errore durante il caricamento delle righe ordine inserimento analisi.", e);
        }

        return righe;
    }

}
