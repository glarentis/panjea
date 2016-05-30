package it.eurotn.panjea.fatturepa.manager.xml.v1_0;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.ObjectUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLBodyManager;
import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiBeniServiziType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDDTType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiGeneraliDocumentoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiGeneraliType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiRiepilogoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DettaglioLineeType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1.NaturaType;
import it.gov.fatturapa.sdi.fatturapa.v1.ScontoMaggiorazioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.TipoDocumentoType;
import it.gov.fatturapa.sdi.fatturapa.v1.TipoScontoMaggiorazioneType;

@Stateless(mappedName = "FatturaElettronicaXMLSDI10BodyManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "FatturaElettronicaXMLSDI10BodyManager")
public class FatturaElettronicaXMLSDI10BodyBean implements FatturaElettronicaXMLBodyManager {

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    protected RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    protected AreaIvaManager areaIvaManager;

    private DettaglioLineeType addSconti(DettaglioLineeType dettaglio, RigaArticolo rigaArticolo) {
        BigDecimal importoUnitario = rigaArticolo.getPrezzoUnitario().getImportoInValuta();
        BigDecimal importoSconto = BigDecimal.ZERO;
        Sconto sconto = new Sconto();
        // Sconti/Maggiorazioni
        if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(rigaArticolo.getVariazione1(), BigDecimal.ZERO)) != 0) {
            sconto.setSconto1(rigaArticolo.getVariazione1());
            importoSconto = importoUnitario
                    .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
            dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione1(), 1));
            importoUnitario = importoUnitario.subtract(importoSconto);
        }
        if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(rigaArticolo.getVariazione2(), BigDecimal.ZERO)) != 0) {
            sconto.setSconto1(rigaArticolo.getVariazione2());
            importoSconto = importoUnitario
                    .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
            dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione2(), 1));
            importoUnitario = importoUnitario.subtract(importoSconto);
        }
        if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(rigaArticolo.getVariazione3(), BigDecimal.ZERO)) != 0) {
            sconto.setSconto1(rigaArticolo.getVariazione3());
            importoSconto = importoUnitario
                    .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
            dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione3(), 1));
            importoUnitario = importoUnitario.subtract(importoSconto);
        }
        if (BigDecimal.ZERO.compareTo(ObjectUtils.defaultIfNull(rigaArticolo.getVariazione4(), BigDecimal.ZERO)) != 0) {
            sconto.setSconto1(rigaArticolo.getVariazione4());
            importoSconto = importoUnitario
                    .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
            dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione4(), 1));
            importoUnitario = importoUnitario.subtract(importoSconto);
        }

        return dettaglio;
    }

    @Override
    public IFatturaElettronicaBodyType getBody(AreaMagazzino areaMagazzino) {

        FatturaElettronicaBodyType body = new FatturaElettronicaBodyType();
        // Dati obbligatori
        body.setDatiGenerali(getDatiGenerali(areaMagazzino));
        body.setDatiBeniServizi(getDatiBeniServizi(areaMagazzino));

        // Presenti nei casi di cessioni tra paesi membri di mezzi di trasporto nuovi.
        // Aggiungere dati veicoli

        return body;
    }

    private DatiBeniServiziType getDatiBeniServizi(AreaMagazzino areaMagazzino) {

        DatiBeniServiziType datiBeniServizi = new DatiBeniServiziType();
        // Dati obbligatori
        datiBeniServizi.getDettaglioLinee().addAll(getDettaglioLinee(areaMagazzino));
        datiBeniServizi.getDatiRiepilogo().addAll(getDatiRiepilogo(areaMagazzino));

        return datiBeniServizi;
    }

    protected List<DatiDDTType> getDatiDDT(AreaMagazzino areaMagazzino) {

        List<AreaMagazzino> aree = new ArrayList<AreaMagazzino>();
        aree.add(areaMagazzino);
        List<AreaMagazzinoLite> areeMagazzinoCollegate = areaMagazzinoManager.caricaAreeMagazzinoCollegate(aree);

        List<DatiDDTType> datiDDT = new ArrayList<DatiDDTType>();
        for (AreaMagazzinoLite areaMagazzinoLite : areeMagazzinoCollegate) {
            DatiDDTType datiArea = new DatiDDTType();
            datiArea.setId(areaMagazzinoLite.getDocumento().getId().toString());
            datiArea.setNumeroDDT(areaMagazzinoLite.getDocumento().getCodice().getCodice());
            datiArea.setDataDDT(
                    FatturazionePAUtils.getXMLGregorianCalendar(areaMagazzinoLite.getDocumento().getDataDocumento()));
            datiDDT.add(datiArea);
        }

        return datiDDT;
    }

    protected DatiGeneraliType getDatiGenerali(AreaMagazzino areaMagazzino) {
        DatiGeneraliType datiGenerali = new DatiGeneraliType();
        datiGenerali.setDatiGeneraliDocumento(getDatiGeneraliDocumento(areaMagazzino.getDocumento()));

        // Nei casi in cui sia presente un documento di trasporto collegato alla fattura, casi di
        // fatturazione differita, vanno valorizzati i seguenti campi per ogni documento di
        // trasporto.
        datiGenerali.getDatiDDT().addAll(getDatiDDT(areaMagazzino));

        // Presente nei casi di fatture per operazioni accessorie, emesse dagli ‘autotrasportatori’ per usufruire delle
        // agevolazioni in materia di registrazione e pagamento IVA.
        // aggiungere datiGenerali --> FatturaPrincipale

        return datiGenerali;
    }

    protected DatiGeneraliDocumentoType getDatiGeneraliDocumento(Documento documento) {

        String codTipoDoc = "TD01";
        if (documento.getTipoDocumento().isNotaCreditoEnable()) {
            switch (documento.getTipoDocumento().getTipoEntita()) {
            case FORNITORE:
                codTipoDoc = "TD05";
                break;
            case CLIENTE:
                codTipoDoc = "TD04";
                break;
            default:
                codTipoDoc = "TD01";
                break;
            }
        }

        DatiGeneraliDocumentoType datiGeneraliDocumento = new DatiGeneraliDocumentoType();

        // dati obbligatori
        datiGeneraliDocumento.setTipoDocumento(TipoDocumentoType.fromValue(codTipoDoc));
        datiGeneraliDocumento.setDivisa(documento.getTotale().getCodiceValuta());
        datiGeneraliDocumento.setData(FatturazionePAUtils.getXMLGregorianCalendar(documento.getDataDocumento()));
        datiGeneraliDocumento.setNumero(documento.getCodice().getCodice());

        // Obbligatorio solo in caso di presenza di ritenuta
        // datiGeneraliDocumento -- >DatiRitenuta

        // Obbligatorio se prevista l'imposta di bollo
        // datiGeneraliDocumento --> DatiBollo

        // Obbligatorio se è previsto un contributo previdenziale
        // datiGeneraliDocumento -->DatiCassaPrevidenziale() --> add(e)

        // Dati non obbligatori
        // datiGeneraliDocumento --> Arrotondamento
        // datiGeneraliDocumento --> Art73
        // datiGeneraliDocumento --> Causale
        datiGeneraliDocumento.setImportoTotaleDocumento(documento.getTotale().getImportoInValuta());

        return datiGeneraliDocumento;
    }

    private List<DatiRiepilogoType> getDatiRiepilogo(AreaMagazzino areaMagazzino) {

        List<DatiRiepilogoType> datiRiepilogo = new ArrayList<DatiRiepilogoType>();

        List<RigaIva> righeIva = areaIvaManager.caricaAreaIvaByDocumento(areaMagazzino.getDocumento()).getRigheIva();
        for (RigaIva rigaIva : righeIva) {
            DatiRiepilogoType riepilogo = new DatiRiepilogoType();
            // Dati obbligatori
            riepilogo.setAliquotaIVA(rigaIva.getCodiceIva().getPercApplicazione());

            NaturaType natura;
            switch (rigaIva.getCodiceIva().getTipoCodiceIva()) {
            case ESCLUSO:
                natura = NaturaType.N_1;
                break;
            case NONSOGGETTO:
                natura = NaturaType.N_2;
                break;
            case NONIMPONIBILE:
                natura = NaturaType.N_3;
                break;
            case ESENTE:
                natura = NaturaType.N_4;
                break;
            default:
                natura = null;
                break;
            }
            if (natura != null) {
                riepilogo.setNatura(natura);
                riepilogo.setRiferimentoNormativo(rigaIva.getCodiceIva().getDescrizioneDocumenti());
            }
            // riepilogo --> SpeseAccessorie
            // riepilogo --> Arrotondamento
            // riepilogo --> EsigibilitaIVA
            // ---------------------------------------------------------------------------------------------------------
            riepilogo.setImponibileImporto(rigaIva.getImponibile().getImportoInValuta());
            riepilogo.setImposta(rigaIva.getImposta().getImportoInValuta());

            datiRiepilogo.add(riepilogo);
        }

        return datiRiepilogo;
    }

    private List<DettaglioLineeType> getDettaglioLinee(AreaMagazzino areaMagazzino) {

        List<DettaglioLineeType> dettaglioLinee = new ArrayList<DettaglioLineeType>();

        int numeroRiga = 1;

        List<RigaArticolo> righeArticolo = rigaMagazzinoManager.getDao().caricaRigheArticolo(areaMagazzino);
        for (RigaArticolo rigaArticolo : righeArticolo) {
            DettaglioLineeType dettaglio = new DettaglioLineeType();
            // Dati obbligatori
            dettaglio.setNumeroLinea(numeroRiga);
            // dettaglio --> TipoCessionePrestazione
            dettaglio.setDescrizione(rigaArticolo.getDescrizione());
            dettaglio.setQuantita(BigDecimal.valueOf(rigaArticolo.getQta()));
            dettaglio.setUnitaMisura(rigaArticolo.getUnitaMisura());
            dettaglio.setDataInizioPeriodo(FatturazionePAUtils.getXMLGregorianCalendar(new Date()));
            dettaglio.setDataFinePeriodo(FatturazionePAUtils.getXMLGregorianCalendar(new Date()));

            dettaglio.setPrezzoUnitario(rigaArticolo.getPrezzoUnitario().getImportoInValuta());
            dettaglio.setPrezzoTotale(rigaArticolo.getPrezzoTotale().getImportoInValuta());
            dettaglio.setAliquotaIVA(rigaArticolo.getCodiceIva().getPercApplicazione());

            NaturaType natura;
            switch (rigaArticolo.getCodiceIva().getTipoCodiceIva()) {
            case ESCLUSO:
                natura = NaturaType.N_1;
                break;
            case NONSOGGETTO:
                natura = NaturaType.N_2;
                break;
            case NONIMPONIBILE:
                natura = NaturaType.N_3;
                break;
            case ESENTE:
                natura = NaturaType.N_4;
                break;
            default:
                natura = null;
                break;
            }
            dettaglio.setNatura(natura);

            // Sconti/Maggiorazioni
            dettaglio = addSconti(dettaglio, rigaArticolo);

            dettaglioLinee.add(dettaglio);

            numeroRiga++;
        }

        return dettaglioLinee;
    }

    protected ScontoMaggiorazioneType getScontoType(BigDecimal importo, BigDecimal percentuale, int numeroSconto) {
        ScontoMaggiorazioneType scontoType = new ScontoMaggiorazioneType();
        scontoType.setTipo(BigDecimal.ZERO.compareTo(percentuale) < 0 ? TipoScontoMaggiorazioneType.MG
                : TipoScontoMaggiorazioneType.SC);
        scontoType.setId("sconto" + numeroSconto);
        scontoType.setPercentuale(percentuale.abs());
        scontoType.setImporto(importo);
        return scontoType;
    }
}
