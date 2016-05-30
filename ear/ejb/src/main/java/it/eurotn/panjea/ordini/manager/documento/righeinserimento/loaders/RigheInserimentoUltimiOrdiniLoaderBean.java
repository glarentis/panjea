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

@Stateless(mappedName = "Panjea.RigheInserimentoUltimiOrdiniLoaderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheInserimentoUltimiOrdiniLoaderBean")
public class RigheInserimentoUltimiOrdiniLoaderBean implements RigheInserimentoLoader {

    private static final Logger LOGGER = Logger.getLogger(RigheInserimentoUltimiOrdiniLoaderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaOrdineInserimento> caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri) {

        String query = "select " + "ro.articolo_id as idArticolo, " + "art.codice as codiceArticolo, "
                + "art.descrizioneLinguaAziendale as descrizioneArticolo, "
                + "min(ao.dataRegistrazione) as dataIniziale, " + "max(ao.dataRegistrazione) as dataFinale, "
                + "sum(ro.qta) as qta, " + "group_concat(ro.id separator ',') as idRigheOrdine, "
                + "ro.numeroDecimaliQta as numeroDecimaliQuantita " + "from ordi_righe_ordine ro "
                + "inner join ordi_area_ordine as ao on ao.id = ro.areaOrdine_id "
                + "inner join maga_articoli art on art.id = ro.articolo_id " + "where ro.areaOrdine_id in "
                + "(select * from ( " + "      select " + "      aod.id " + "      from ordi_area_ordine aod "
                + "      inner join docu_documenti doc on doc.id = aod.documento_id "
                + "      where aod.statoAreaOrdine > 0 " + "      and doc.entita_id = "
                + parametri.getSedeEntita().getEntita().getId()
                + "      order by aod.dataRegistrazione desc, doc.codiceOrder desc limit "
                + parametri.getNumeroOrdiniDaConsiderare() + "   ) as t) " + "group by ro.articolo_id ";

        SQLQueryImpl sqlQuery = (SQLQueryImpl) panjeaDAO.prepareNativeQuery(query, RigaOrdineInserimento.class);
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
