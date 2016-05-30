package it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.RigaCorrispettivo;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer.interfaces.CorrispettiviImporterManager;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces.CorrispettiviManager;
import it.eurotn.panjea.corrispettivi.manager.corrispettivilinktipodocumento.interfaces.CorrispettiviLinkTipoDocumentoManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.CorrispettiviImporterManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CorrispettiviImporterManager")
public class CorrispettiviImporterManagerBean implements CorrispettiviImporterManager {

    @Resource
    private SessionContext context;

    @EJB
    private CorrispettiviManager corrispettiviManager;

    @EJB
    private CorrispettiviLinkTipoDocumentoManager corrispettiviLinkTipoDocumentoManager;

    @EJB(name = "Panjea.CorrispettiviPagamentiLoaderBean")
    private CorrispettiviLoader corrispettiviPagamentiLoaderBean;

    /**
     * Crea il corrispettivo in base ai parametri.
     *
     * @param data
     *            data corrispettivo
     * @param tipoDocumento
     *            tipo documento del corrispettivo
     * @param importi
     *            importi delle righe corrispettivo
     */
    private void creaCorrispettivo(Date data, TipoDocumento tipoDocumento, Map<CodiceIva, BigDecimal> importi) {

        Corrispettivo corrispettivo = new Corrispettivo();
        corrispettivo.setCodiceAzienda(getCodiceAzienda());
        corrispettivo.setData(data);
        corrispettivo.setTipoDocumento(tipoDocumento);
        corrispettivo.setRigheCorrispettivo(new ArrayList<RigaCorrispettivo>());
        BigDecimal totCorrispettivo = BigDecimal.ZERO;
        for (Entry<CodiceIva, BigDecimal> entry : importi.entrySet()) {
            totCorrispettivo = totCorrispettivo.add(entry.getValue());
            RigaCorrispettivo rigaCorrispettivo = new RigaCorrispettivo();
            rigaCorrispettivo.setCodiceIva(entry.getKey());
            rigaCorrispettivo.setImporto(entry.getValue());
            corrispettivo.getRigheCorrispettivo().add(rigaCorrispettivo);
        }
        corrispettivo.setTotale(totCorrispettivo);
        corrispettiviManager.salva(corrispettivo);
    }

    /**
     * @return codiceAzienda
     */
    protected String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

    private Map<TipoDocumento, TipoDocumento> getMapTipiDocumentoLink() {
        Map<TipoDocumento, TipoDocumento> map = new HashMap<>();

        List<CorrispettivoLinkTipoDocumento> tipiDocLink = corrispettiviLinkTipoDocumentoManager.caricaAll();
        for (CorrispettivoLinkTipoDocumento link : tipiDocLink) {
            map.put(link.getTipoDocumentoOrigine(), link.getTipoDocumentoDestinazione());
        }

        return map;
    }

    /**
     * Carica i tipi documento di destinazione configurati per i corrispettivi.
     *
     * @param totaliCorrispettivi
     *            totali corrispettivi
     * @return mappa contenente come chiave il tipo documento di destinazione e valore il corrispettivo associato
     */
    private ListValuedMap<TipoDocumento, CorrispettivoImportDTO> getTipiDocDestinazioneDorrispettivi(
            List<CorrispettivoImportDTO> totaliCorrispettivi) {
        ListValuedMap<TipoDocumento, CorrispettivoImportDTO> mapTotali = MultiMapUtils.newListValuedHashMap();

        Map<TipoDocumento, TipoDocumento> mapTipiDocLink = getMapTipiDocumentoLink();
        for (CorrispettivoImportDTO corrispettivoImportDTO : totaliCorrispettivi) {
            TipoDocumento tipoDocumentoDest = mapTipiDocLink.get(corrispettivoImportDTO.getTipoDocumento());
            if (tipoDocumentoDest == null) {
                throw new GenericException("Tipo documento per corrispettivi non configurato per il tipo documento: "
                        + corrispettivoImportDTO.getTipoDocumento().getCodice() + " - "
                        + corrispettivoImportDTO.getTipoDocumento().getDescrizione());
            }
            mapTotali.put(tipoDocumentoDest, corrispettivoImportDTO);
        }

        return mapTotali;
    }

    @Override
    public void importa(Date data) {

        // elimino tutti i corrispettivi presenti per il giorno
        corrispettiviManager.cancellaCorrispettivo(data);

        // Importo i corrispettivi dati dagli incassi dei rifornimenti. Per capire se c'è il vending faccio il lookup
        // sul bean. Al vending non interessa il tipo documento.
        CorrispettiviLoader vendingLoader = (CorrispettiviLoader) context.lookup("Panjea.CorrispettiviVendingLoader");
        List<CorrispettivoImportDTO> totaliCorrispettivi = new ArrayList<>();
        if (vendingLoader != null) {
            totaliCorrispettivi.addAll(vendingLoader.caricaTotaliCorrispettivi(data));
        }

        // Importo i corrispettivi provenienti dalle aree rate che gestiscono i corrispettivi
        totaliCorrispettivi.addAll(corrispettiviPagamentiLoaderBean.caricaTotaliCorrispettivi(data));

        // raggruppo tutti i totali corrispettivi per il tipo documento di destinazione configurato
        ListValuedMap<TipoDocumento, CorrispettivoImportDTO> mapTotali = getTipiDocDestinazioneDorrispettivi(
                totaliCorrispettivi);

        // per ogni tipo documento raggruppo i corrispettivi per codice iva
        for (TipoDocumento tipoDoc : mapTotali.keys()) {
            List<CorrispettivoImportDTO> corrispettiviDTO = mapTotali.get(tipoDoc);

            Map<CodiceIva, BigDecimal> mapImporti = new HashMap<>();
            for (CorrispettivoImportDTO dto : corrispettiviDTO) {
                BigDecimal totale = ObjectUtils.defaultIfNull(mapImporti.get(dto.getCodiceIva()), BigDecimal.ZERO);
                totale = totale.add(dto.getImporto());
                mapImporti.put(dto.getCodiceIva(), totale);
            }

            creaCorrispettivo(data, tipoDoc, mapImporti);
        }
    }

}
