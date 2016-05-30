package it.eurotn.panjea.vending.manager.arearifornimento.interceptors;

import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.SQLQuery;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

public class AreaRifornimentoEvasioneOrdineInterceptor {

    @EJB
    private PanjeaDAO panjeaDAO;

    /**
     *
     * @param ctx
     *            context interceprot
     * @return lista area magazzino
     * @throws Exception
     *             .
     */
    @AroundInvoke
    public Object creaAreaRifornimento(InvocationContext ctx) throws Exception {
        SQLQuery query = prepareQuery();

        @SuppressWarnings("unchecked")
        List<AreaMagazzino> areeMagazzino = (List<AreaMagazzino>) ctx.proceed();
        for (AreaMagazzino areaMagazzino : areeMagazzino) {
            if (areaMagazzino.getTipoAreaMagazzino().isGestioneVending()) {
                query.setParameter("idAreaMagazzino", areaMagazzino.getId());
                @SuppressWarnings("unchecked")
                List<Integer> idAreeRifornimento = query.list();
                if (!CollectionUtils.isEmpty(idAreeRifornimento)) {
                    // Utilizzo solo la prima perchè un ordine lo evado solamente su un'area
                    // magazzino
                    creaAreaRifornimentoMagazzino(idAreeRifornimento.get(0), areaMagazzino);
                }
            }
        }
        return areeMagazzino;
    }

    /**
     *
     * @param integer
     *            id Area rifornimento legato all'ordine
     * @param areaMagazzino
     *            areaMagazzino alla quale legarla
     */
    private void creaAreaRifornimentoMagazzino(Integer idAreaRifornimento, AreaMagazzino areaMagazzino) {
        try {
            AreaRifornimento arifornimentoOrdine = panjeaDAO.load(AreaRifornimento.class, idAreaRifornimento);
            AreaRifornimento arifornimentoMagazzino = arifornimentoOrdine.copia(areaMagazzino);
            panjeaDAO.save(arifornimentoMagazzino);
        } catch (DAOException e) {
            throw new GenericException(e);
        }
    }

    private SQLQuery prepareQuery() {
        StringBuilder sbSql = new StringBuilder(400);
        sbSql.append("select arif.id ");
        sbSql.append("from vend_area_rifornimento arif ");
        sbSql.append("inner join ordi_area_ordine ao on ao.id=arif.areaOrdine_id ");
        sbSql.append(
                "inner join maga_righe_magazzino rm on rm.areaOrdineCollegata_Id=ao.id where rm.areaMagazzino_id=:idAreaMagazzino group by arif.id ");
        return panjeaDAO.prepareNativeQuery(sbSql.toString());
    }
}
