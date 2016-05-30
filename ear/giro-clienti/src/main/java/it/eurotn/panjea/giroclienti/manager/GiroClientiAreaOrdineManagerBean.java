package it.eurotn.panjea.giroclienti.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.exception.GiroClientiException;
import it.eurotn.panjea.giroclienti.manager.interfaces.GiroClientiAreaOrdineManager;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.AreaOrdineManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;

@Stateless(name = "Panjea.GiroClientiAreaOrdineManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GiroClientiAreaOrdineManager")
public class GiroClientiAreaOrdineManagerBean implements GiroClientiAreaOrdineManager {

    private static final Logger LOGGER = Logger.getLogger(GiroClientiAreaOrdineManagerBean.class);

    @EJB
    private DocumentiManager documentiManager;

    @EJB
    private AreaOrdineManager areaOrdineManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private TipiAreaPartitaManager tipiAreaPartitaManager;

    @EJB
    private AreaRateManager areaRateManager;

    @Override
    public AreaOrdine creaAreaOrdine(RigaGiroCliente rigaGiroCliente, Documento documento,
            TipoAreaOrdine tipoAreaOrdine, DepositoLite deposito) {

        AreaOrdine areaOrdine = new AreaOrdine();
        areaOrdine.setDepositoOrigine(deposito);
        Calendar calendar = Calendar.getInstance();
        areaOrdine.setDataRegistrazione(calendar.getTime());
        areaOrdine.setAnnoMovimento(calendar.get(Calendar.YEAR));
        areaOrdine.setDocumento(documento);
        areaOrdine.setTipoAreaOrdine(tipoAreaOrdine);
        areaOrdine.setDataConsegna(calendar.getTime());

        SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = areaMagazzinoManager
                .caricaSedeAreaMagazzinoDTO(rigaGiroCliente.getSedeEntita());
        if (sedeAreaMagazzinoDTO != null) {
            areaOrdine.setListinoAlternativo(sedeAreaMagazzinoDTO.getListinoAlternativo());
            areaOrdine.setListino(sedeAreaMagazzinoDTO.getListino());
            areaOrdine.setVettore(sedeAreaMagazzinoDTO.getVettore());
            areaOrdine.setSedeVettore(sedeAreaMagazzinoDTO.getSedeVettore());
            areaOrdine.setCausaleTrasporto(sedeAreaMagazzinoDTO.getCausaleTrasporto());
            areaOrdine.setTrasportoCura(sedeAreaMagazzinoDTO.getTrasportoCura());
            areaOrdine.setTipoPorto(sedeAreaMagazzinoDTO.getTipoPorto());
            areaOrdine.setAddebitoSpeseIncasso(sedeAreaMagazzinoDTO.isCalcoloSpese());
            areaOrdine.setInserimentoBloccato(sedeAreaMagazzinoDTO.isInserimentoBloccato());
            areaOrdine.setRaggruppamentoBolle(sedeAreaMagazzinoDTO.isRaggruppamentoBolle());
            areaOrdine.setAgente(sedeAreaMagazzinoDTO.getAgente());

            ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = sedeAreaMagazzinoDTO
                    .getTipologiaCodiceIvaAlternativo();

            CodiceIva codiceIvaAlternativo = sedeAreaMagazzinoDTO.getCodiceIvaAlternativo();
            // se la tipologia del codice iva alternativo è
            // ESENZIONE_DICHIARAZIONE_INTENTO controllo che la
            // data del documento rientri nella scadenza altrimenti
            // imposto come tipologia NESSUNA e rimuovo il
            // codice iv alternativo
            if (sedeAreaMagazzinoDTO
                    .getTipologiaCodiceIvaAlternativo() == ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO) {
                Date dataScadenza = sedeAreaMagazzinoDTO.getDataScadenzaDichiarazioneIntento();
                if (dataScadenza == null
                        || (documento.getDataDocumento() != null && documento.getDataDocumento().after(dataScadenza))) {
                    tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
                    codiceIvaAlternativo = null;
                }

            }
            areaOrdine.setTipologiaCodiceIvaAlternativo(tipologiaCodiceIvaAlternativo);
            areaOrdine.setCodiceIvaAlternativo(codiceIvaAlternativo);

            creaAreaRate(documento, sedeAreaMagazzinoDTO.getCodicePagamento());
        }

        areaOrdine = areaOrdineManager.salvaAreaOrdine(areaOrdine);
        return areaOrdine;
    }

    private void creaAreaRate(Documento documento, CodicePagamento codicePag) {

        // Se ho l'area contabile lite non devo cambiare nulla sui pagamenti e se l'area magazzino è nuova ma ho un
        // documento associato significa che eredito l'area pagamento dalla contabilità quindi non devo modificarla
        TipoAreaPartita tipoAreaPartita = tipiAreaPartitaManager
                .caricaTipoAreaPartitaPerTipoDocumento(documento.getTipoDocumento());

        boolean areaRateEnabled = (tipoAreaPartita != null) && (tipoAreaPartita.getId() != null)
                && ((TipoAreaPartita.TipoOperazione.GENERA.equals(tipoAreaPartita.getTipoOperazione()))
                        || (TipoAreaPartita.TipoOperazione.NESSUNA.equals(tipoAreaPartita.getTipoOperazione())));

        if (areaRateEnabled) {
            AreaRate areaRate = new AreaRate();
            areaRate.setDocumento(documento);
            areaRate.setCodicePagamento(codicePag);
            areaRate.setPercentualeSconto(codicePag == null ? null : codicePag.getPercentualeSconto());
            areaRate.setGiorniLimite(codicePag == null ? null : codicePag.getGiorniLimite());
            areaRateManager.salvaAreaRate(areaRate);
        }
    }

    @Override
    public Documento creaDocumento(RigaGiroCliente rigaGiroCliente, TipoDocumento tipoDocumento) {

        Documento documento = new Documento();
        documento.setImposta(new Importo("EUR", BigDecimal.ONE));
        documento.setTotale(new Importo("EUR", BigDecimal.ONE));
        Calendar calendar = Calendar.getInstance();
        documento.setDataDocumento(calendar.getTime());
        documento.setEntita(rigaGiroCliente.getSedeEntita().getEntita().getEntitaLite());
        documento.setSedeEntita(rigaGiroCliente.getSedeEntita());
        documento.setTipoDocumento(tipoDocumento);
        try {
            documento = documentiManager.salvaDocumento(documento);
        } catch (DocumentoDuplicateException e) {
            LOGGER.error("--> errore durante il salvataggio del documento.", e);
            throw new GiroClientiException("errore durante il salvataggio del documento.", e);
        }

        return documento;
    }

}
