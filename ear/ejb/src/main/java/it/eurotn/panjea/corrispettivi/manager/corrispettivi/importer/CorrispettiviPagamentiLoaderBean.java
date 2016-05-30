package it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

@Stateless(name = "Panjea.CorrispettiviPagamentiLoaderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CorrispettiviPagamentiLoaderBean")
public class CorrispettiviPagamentiLoaderBean implements CorrispettiviLoader {

    private static final Logger LOGGER = Logger.getLogger(CorrispettiviPagamentiLoaderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    private List<Pagamento> caricaPagamenti(Date data) {

     // @formatter:off
        String queryString = "select p "
                + "from Pagamento p, TipoAreaPartita tap, TipoAreaMagazzino tam "
                + "inner join p.rata rata "
                + "inner join rata.areaRate ar "
                + "inner join ar.documento doc "
                + "inner join doc.tipoDocumento tipoDoc "
                + "where tap.tipoDocumento = tipoDoc "
                + "and tam.tipoDocumento = tipoDoc "
                + "and p.dataPagamento = :paramData "
                + "and tap.gestioneCorrispettivi = true "
                + "and tam.valoriFatturato = false "
                + "and tam.tipoDocumentoPerFatturazione is null";
        // @formatter:on

        Query query = panjeaDAO.prepareQuery(queryString);
        query.setParameter("paramData", data);

        List<Pagamento> pagamenti = new ArrayList<>();
        try {
            pagamenti = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dei pagamenti.", e);
            throw new GenericException("errore durante il caricamento dei pagamenti.", e);
        }

        return pagamenti;
    }

    @SuppressWarnings("unchecked")
    private List<CorrispettivoImportDTO> caricaTotaliCodiciIva(Integer idDocumento) {
        LOGGER.debug("--> Enter caricaTotaliCodiciIva");

        StringBuilder sb = new StringBuilder(200);
        sb.append("select tipoDoc as tipoDocumento, ");
        sb.append("ri.codiceIva as codiceIva, ");
        sb.append("sum(ri.imponibile.importoInValutaAzienda + ri.imposta.importoInValutaAzienda) as importo ");
        sb.append("from RigaIva ri  ");
        sb.append("inner join ri.areaIva.documento doc ");
        sb.append("inner join doc.tipoDocumento tipoDoc ");
        sb.append("where ri.areaIva.documento.id = :paramIdDocumento ");
        sb.append("group by tipoDoc,ri.codiceIva");

        Query query = panjeaDAO.prepareQuery(sb.toString(), CorrispettivoImportDTO.class, null);
        query.setParameter("paramIdDocumento", idDocumento);

        List<CorrispettivoImportDTO> result = null;
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento del totale codici iva documento", e);
            throw new GenericException("errore durante il caricamento del totale codici iva documento", e);
        }

        LOGGER.debug("--> Exit caricaTotaliCodiciIva");
        return result;
    }

    @Override
    public List<CorrispettivoImportDTO> caricaTotaliCorrispettivi(Date data) {
        LOGGER.debug("--> Enter caricaTotaliCorrispettivi");
        Map<String, CorrispettivoImportDTO> totaliCorrispettivi = new HashMap<>();

        // carico tutti i pagamenti
        List<Pagamento> pagamenti = caricaPagamenti(data);

        for (Pagamento pagamento : pagamenti) {
            BigDecimal totaleDocumento = pagamento.getRata().getAreaRate().getDocumento().getTotale()
                    .getImportoInValutaAzienda();
            // Carico i totali per codice iva
            List<CorrispettivoImportDTO> totaliIva = caricaTotaliCodiciIva(
                    pagamento.getRata().getAreaRate().getDocumento().getId());

            // non ho più di un codice iva
            if (totaliIva.size() == 1) {
                CorrispettivoImportDTO corrispettivoImportDTO = new CorrispettivoImportDTO();
                corrispettivoImportDTO.setCodiceIva(totaliIva.get(0).getCodiceIva());
                corrispettivoImportDTO.setTipoDocumento(totaliIva.get(0).getTipoDocumento());
                corrispettivoImportDTO.setImporto(pagamento.getImporto().getImportoInValutaAzienda());

                totaliCorrispettivi.put(
                        totaliIva.get(0).getTipoDocumento().getId() + "$" + totaliIva.get(0).getCodiceIva().getId(),
                        corrispettivoImportDTO);
            } else {
                // l'importo del pagamento va suddiviso in base alla percentuale dei codici iva
                BigDecimal totalePercentuali = BigDecimal.ZERO;
                BigDecimal totaleImporti = BigDecimal.ZERO;
                Map<CodiceIva, BigDecimal> percentuali = new HashMap<>();
                for (CorrispettivoImportDTO totIva : totaliIva) {

                    CorrispettivoImportDTO corrispettivoImportDTO = totaliCorrispettivi
                            .get(totIva.getTipoDocumento().getId() + "$" + totIva.getCodiceIva().getId());
                    if (corrispettivoImportDTO == null) {
                        corrispettivoImportDTO = new CorrispettivoImportDTO();
                        corrispettivoImportDTO.setCodiceIva(totIva.getCodiceIva());
                        corrispettivoImportDTO.setTipoDocumento(totIva.getTipoDocumento());
                        corrispettivoImportDTO.setImporto(BigDecimal.ZERO);
                    }

                    if (percentuali.size() < totaliIva.size() - 1) {
                        BigDecimal percentuale = totIva.getImporto().multiply(new BigDecimal(100))
                                .divide(totaleDocumento, 2, RoundingMode.HALF_UP);
                        totalePercentuali = totalePercentuali.add(percentuale);
                        percentuali.put(totIva.getCodiceIva(), percentuale);
                        BigDecimal importoPerc = percentuali.get(totIva.getCodiceIva())
                                .multiply(pagamento.getImporto().getImportoInValutaAzienda())
                                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        totaleImporti = totaleImporti.add(importoPerc);
                        corrispettivoImportDTO.setImporto(corrispettivoImportDTO.getImporto().add(importoPerc));
                    } else {
                        percentuali.put(totIva.getCodiceIva(), new BigDecimal(100).subtract(totalePercentuali));
                        corrispettivoImportDTO.setImporto(corrispettivoImportDTO.getImporto()
                                .add(pagamento.getImporto().getImportoInValutaAzienda().subtract(totaleImporti)));
                    }

                    totaliCorrispettivi.put(totIva.getTipoDocumento().getId() + "$" + totIva.getCodiceIva().getId(),
                            corrispettivoImportDTO);
                }
            }

        }

        LOGGER.debug("--> Exit caricaTotaliCorrispettivi");
        return new ArrayList<>(totaliCorrispettivi.values());
    }

}
