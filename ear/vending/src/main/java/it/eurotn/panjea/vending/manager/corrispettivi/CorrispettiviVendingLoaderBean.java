package it.eurotn.panjea.vending.manager.corrispettivi;

import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer.CorrispettivoImportDTO;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.manager.corrispettivi.interfaces.CorrispettiviVendingLoader;

@Stateless(name = "Panjea.CorrispettiviVendingLoader")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CorrispettiviVendingLoader")
public class CorrispettiviVendingLoaderBean implements CorrispettiviVendingLoader {

    private static final Logger LOGGER = Logger.getLogger(CorrispettiviVendingLoaderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    @Override
    public List<CorrispettivoImportDTO> caricaTotaliCorrispettivi(Date data) {
        LOGGER.debug("--> Enter caricaTotaliCorrispettivi");

        StringBuilder sb = new StringBuilder(200);
        sb.append("select doc.tipoDocumento as tipoDocumento, ");
        sb.append("am.codiceIvaAlternativo as codiceIva, ");
        sb.append("sum(coalesce(ar.incasso,0)) as importo ");
        sb.append("from AreaRifornimento ar inner join ar.areaMagazzino am ");
        sb.append("inner join am.documento doc inner join am.tipoAreaMagazzino tam ");
        sb.append("where tam.valoriFatturato = false and tam.tipoDocumentoPerFatturazione is null ");
        sb.append("and am.dataRegistrazione = :paramData ");
        sb.append("group by doc.tipoDocumento,am.codiceIvaAlternativo ");

        Query query = panjeaDAO.prepareQuery(sb.toString(), CorrispettivoImportDTO.class, null);
        query.setParameter("paramData", data);

        List<CorrispettivoImportDTO> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del totale corrispettivi del vendign per il giorno " + data,
                    e);
            throw new GenericException(
                    "errore durante il caricamento del totale corrispettivi del vendign per il giorno " + data, e);
        }

        LOGGER.debug("--> Exit caricaTotaliCorrispettivi");
        return result;
    }

}
