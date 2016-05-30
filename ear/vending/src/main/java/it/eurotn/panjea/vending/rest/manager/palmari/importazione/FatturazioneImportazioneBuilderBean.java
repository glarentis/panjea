package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.manager.interfaces.CodiciIvaManager;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.importer.manager.interfaces.GenerazioneImportazioneManager;
import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoManager;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.OperatoreSenzaTipoDocumentoFattura;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.FatturazioneImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.RigaImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Fatture;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.FattureRighe;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Rifornimenti;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RifornimentiProdotti;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.FatturazioneImporatzioneBuilderManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FatturazioneImporatzioneBuilderManager")
public class FatturazioneImportazioneBuilderBean implements FatturazioneImportazioneBuilder {

    private static final Logger LOGGER = Logger.getLogger(FatturazioneImportazioneBuilderBean.class);

    @EJB
    private FatturazioneImportazioneBuilder fatturazioneImportazioneBuilder;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private AreaIvaManager areaIvaManager;

    @EJB
    private CodiciIvaManager ivaManager;

    @EJB
    private AreaRifornimentoManager areaRifornimentoManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaIvaCancellaManager areaIvaCancellaManager;

    @EJB
    private GenerazioneImportazioneManager generazioneImportazioneManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private RigaImportazioneBuilder rigaImportazioneBuilder;

    private void collegaFattura(int numeroFattura, AreaMagazzino fattura, AreaRifornimento rifornimento,
            ImportazioneXml importazioneXml) {
        Query queryRigheRifornimento = panjeaDAO.prepareQuery(
                "select r from RigaArticolo r where r.areaMagazzino.id=" + rifornimento.getAreaMagazzino().getId());

        Query queryRigheFattura = panjeaDAO
                .prepareQuery("select r from RigaArticolo r where r.areaMagazzino.id=" + fattura.getId());

        try {
            @SuppressWarnings("unchecked")
            List<RigaArticolo> righeRifornimento = panjeaDAO.getResultList(queryRigheRifornimento);

            Map<String, FattureRighe> righeFatturaXml = raggruppaRigheFatturaXmlByArticolo(numeroFattura,
                    importazioneXml);

            Map<String, RigaArticolo> righeRifornimentoGroupByCodArticoloOmaggio = new HashMap<>();
            MapUtils.populateMap(righeRifornimentoGroupByCodArticoloOmaggio, righeRifornimento,
                    new Transformer<RigaArticolo, String>() {

                        @Override
                        public String transform(RigaArticolo input) {
                            return input.getArticolo().getCodice() + ":"
                                    + String.valueOf(input.getTipoOmaggio() != TipoOmaggio.NESSUNO);
                        }
                    });

            @SuppressWarnings("unchecked")
            List<RigaArticolo> righeFattura = panjeaDAO.getResultList(queryRigheFattura);
            for (RigaArticolo rigaArticoloFatturaPanjea : righeFattura) {
                FattureRighe rigaFatturaXml = righeFatturaXml.get(rigaArticoloFatturaPanjea.getArticolo().getCodice());
                RigaArticolo rigaArticoloRifornimentoPanjea = righeRifornimentoGroupByCodArticoloOmaggio
                        .get(rigaArticoloFatturaPanjea.getArticolo().getCodice() + ":"
                                + String.valueOf(rigaArticoloFatturaPanjea.getTipoOmaggio() != TipoOmaggio.NESSUNO));

                rigaArticoloFatturaPanjea.setAreaMagazzinoCollegata(rifornimento.getAreaMagazzino());
                rigaArticoloFatturaPanjea.setRigaMagazzinoCollegata(rigaArticoloRifornimentoPanjea);
                rigaArticoloFatturaPanjea.setSomministrazione(rigaFatturaXml.isSomministrazione());
                panjeaDAO.save(rigaArticoloFatturaPanjea);
                if (rigaArticoloFatturaPanjea.isSomministrazione()) {
                    rigaArticoloRifornimentoPanjea.setSomministrazione(true);
                    panjeaDAO.save(rigaArticoloRifornimentoPanjea);
                }
            }

        } catch (DAOException e) {
            LOGGER.error("-->errore nel caricare le righe di rif.", e);
            throw new GenericException("-->errore nel caricare le righe di rif.", e);
        }

    }

    private void creaAreaRifornimento(DocumentoVendingImport doc, AreaMagazzino areaMagazzino,
            Rifornimenti rifornimentoXML) {
        if (areaMagazzino.getTipoAreaMagazzino().isGestioneVending()) {
            AreaRifornimento areaRifornimento = new AreaRifornimento();
            areaRifornimento.setOperatore(doc.getOperatore());
            areaRifornimento.setInstallazione(doc.getInstallazione());
            areaRifornimento.setDistributore((Distributore) doc.getInstallazione().getArticolo());
            areaRifornimento.setAreaMagazzino(areaMagazzino);
            if (rifornimentoXML.getIncassato().compareTo(BigDecimal.ZERO) > 0) {
                areaRifornimento.setIncasso(rifornimentoXML.getIncassato());
            }
            areaRifornimento = areaRifornimentoManager.salva(areaRifornimento);
        }
    }

    private DocumentoVendingImport creaDocumento(String numeroDocumento, AreaRifornimento rifornimento,
            Rifornimenti rifornimentoPalmare) throws OperatoreSenzaTipoDocumentoFattura {
        DocumentoVendingImport doc = new DocumentoVendingImport();
        AreaMagazzino am = rifornimento.getAreaMagazzino();
        if (rifornimento.getOperatore().getTipoAreaMagazzinoFattura() == null) {
            String codiceDistributore = rifornimento.getDistributore() != null
                    ? rifornimento.getDistributore().getCodice() : "Non installato";
            throw new OperatoreSenzaTipoDocumentoFattura(rifornimento.getInstallazione().getCodice(),
                    codiceDistributore, rifornimentoPalmare.getProgressivo(),
                    am.getDocumento().getSedeEntita().getEntita().getAnagrafica().getDenominazione(),
                    rifornimento.getOperatore().getCodice());
        }
        doc.setNumeroDocumento(numeroDocumento);
        doc.setDataDocumento(am.getDocumento().getDataDocumento());
        doc.setIdSede(am.getDocumento().getSedeEntita().getId());
        doc.setIdEntita(am.getDocumento().getSedeEntita().getEntita().getId());
        doc.setDepositoDestinazione(am.getDepositoDestinazione());

        doc.setTipoAreaMagazzino(rifornimento.getOperatore().getTipoAreaMagazzinoFattura());

        doc.setInstallazione(rifornimento.getInstallazione());
        doc.setDepositoOrigine(rifornimento.getOperatore().getMezzoTrasporto().getDeposito());
        return doc;
    }

    private void creaRigaIva(AreaIva areaIva, double aliquota, BigDecimal imponibile, BigDecimal imposta,
            Map<Double, CodiceIva> codiciIva) {
        if (aliquota == 0.0) {
            return;
        }
        RigaIva riva = new RigaIva();
        riva.setAreaIva(areaIva);
        riva.setCodiceIva(codiciIva.get(aliquota));
        Importo imponibileImporto = new Importo("EUR", BigDecimal.ONE);
        imponibileImporto.setImportoInValuta(imponibile);
        imponibileImporto.calcolaImportoValutaAzienda(2);
        riva.setImponibile(imponibileImporto);

        Importo imponibileImposta = new Importo("EUR", BigDecimal.ONE);
        imponibileImposta.setImportoInValuta(imposta);
        imponibileImposta.calcolaImportoValutaAzienda(2);
        riva.setImposta(imponibileImposta);

        try {
            areaIvaManager.salvaRigaIva(riva, null);
        } catch (CodiceIvaCollegatoAssenteException e) {
            LOGGER.error("-->errore nel salvare la riga iva con aliquota iva " + aliquota, e);
            throw new GenericException("-->errore nel salvare la riga iva con aliquota iva " + aliquota, e);
        }
    }

    /**
     * @return userName loggato
     */
    protected String getUserName() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getUserName();
    }

    @Override
    public AreaMagazzino importaFattura(Integer numeroFattura, AreaRifornimento rifornimento,
            Rifornimenti rifornimentoPalmare, ImportazioneXml importazioneXml)
                    throws OperatoreSenzaTipoDocumentoFattura, DocumentoDuplicateException {

        Fatture fattura = importazioneXml.getFatture().get(numeroFattura);
        DocumentoVendingImport doc = creaDocumento(Integer.toString(numeroFattura), rifornimento, rifornimentoPalmare);
        doc.setTotaleDocumento(fattura.getTotaleFattura());
        doc.setTotaleImpostaDocumento(fattura.getTotaleImposta());

        // Le righe le creo partendo dalle righe del rifornimento così da avere un unico punto dove
        // crearle
        // le righe della fattura sono uguali a quelle del rifornimento
        Collection<RifornimentiProdotti> righeArticoloRifornimento = importazioneXml.getRifornimentiRighe()
                .get(rifornimentoPalmare.getProgressivo());
        Collection<RigaImport> righeImporta = rigaImportazioneBuilder.creaRigheImport(righeArticoloRifornimento);
        for (RigaImport rigaImport : righeImporta) {
            doc.getRighe().add(rigaImport);
        }
        SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = areaMagazzinoManager
                .caricaSedeAreaMagazzinoDTO(doc.getInstallazione().getDeposito().getSedeEntita());

        DatiGenerazione datiGenerazione = new DatiGenerazione();
        datiGenerazione.setDataGenerazione(Calendar.getInstance().getTime());
        datiGenerazione.setUtente(getUserName());
        datiGenerazione.setNote("Importazione da palmare");
        datiGenerazione.setTipoGenerazione(TipoGenerazione.ESTERNO);

        AreaMagazzino areaMagazzino = null;
        try {
            areaMagazzino = generazioneImportazioneManager.generaDocumento(doc,
                    doc.getInstallazione().getDeposito().getSedeEntita(), sedeAreaMagazzinoDTO, datiGenerazione);
        } catch (Exception ex) {
            if (ex.getCause().getCause().getCause().getCause() instanceof DocumentoDuplicateException) {
                throw (DocumentoDuplicateException) ex.getCause().getCause().getCause().getCause();
            }
            LOGGER.error("-->errore nel salvare l'area magazzino", ex);
            throw new GenericException("-->errore nel salvare l'area magazzino", ex);
        }

        creaAreaRifornimento(doc, areaMagazzino, rifornimentoPalmare);

        // Sistemo l'area Iva con quella sull XML
        AreaIva areaIva = areaIvaManager.caricaAreaIvaByDocumento(areaMagazzino.getDocumento());
        areaIvaCancellaManager.cancellaRigheIva(areaIva);
        Map<Double, CodiceIva> codiciIva = ivaManager.caricaCodiciIvaPalmare();

        creaRigaIva(areaIva, fattura.getAliquota1(), fattura.getImponibile1(), fattura.getImposta1(), codiciIva);
        creaRigaIva(areaIva, fattura.getAliquota2(), fattura.getImponibile2(), fattura.getImposta2(), codiciIva);

        // Devo legare le righe della fattura a quelle del rifornimento.
        collegaFattura(numeroFattura, areaMagazzino, rifornimento, importazioneXml);
        return areaMagazzino;
    }

    private Map<String, FattureRighe> raggruppaRigheFatturaXmlByArticolo(int numFattura, ImportazioneXml xml) {
        Collection<FattureRighe> righe = xml.getFattureRighe().get(numFattura);
        Map<String, FattureRighe> righeArticolo = new HashMap<>();
        MapUtils.populateMap(righeArticolo, righe, new Transformer<FattureRighe, String>() {

            @Override
            public String transform(FattureRighe input) {
                return input.getProdotto();
            }
        });
        return righeArticolo;
    }

}
