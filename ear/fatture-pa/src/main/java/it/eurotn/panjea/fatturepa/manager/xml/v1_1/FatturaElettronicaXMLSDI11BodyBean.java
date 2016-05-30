package it.eurotn.panjea.fatturepa.manager.xml.v1_1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.fatturepa.manager.xml.v1_0.FatturaElettronicaXMLSDI10BodyBean;
import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1.CondizioniPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1.TipoDocumentoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiBeniServiziType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiGeneraliDocumentoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiGeneraliType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiRiepilogoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DettaglioLineeType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DettaglioPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.EsigibilitaIVAType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.ModalitaPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.NaturaType;

@Stateless(mappedName = "FatturaElettronicaXMLSDI11BodyManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "FatturaElettronicaXMLSDI11BodyManager")
public class FatturaElettronicaXMLSDI11BodyBean extends FatturaElettronicaXMLSDI10BodyBean {

    @EJB
    private AreaRateManager areaRateManager;

    @Override
    public IFatturaElettronicaBodyType getBody(AreaMagazzino areaMagazzino) {

        FatturaElettronicaBodyType body = new FatturaElettronicaBodyType();
        // Dati obbligatori
        body.setDatiGenerali(getDatiGeneraliV11(areaMagazzino));
        body.setDatiBeniServizi(getDatiBeniServizi(areaMagazzino));

        // Presenti nei casi di cessioni tra paesi membri di mezzi di trasporto nuovi.
        // body --> DatiVeicoli

        body.getDatiPagamento().addAll(getDatiPagamento(areaMagazzino));

        return body;
    }

    private DatiBeniServiziType getDatiBeniServizi(AreaMagazzino areaMagazzino) {

        DatiBeniServiziType datiBeniServizi = new DatiBeniServiziType();
        // Dati obbligatori
        datiBeniServizi.getDettaglioLinee().addAll(getDettaglioLinee(areaMagazzino));
        datiBeniServizi.getDatiRiepilogo().addAll(getDatiRiepilogo(areaMagazzino));

        return datiBeniServizi;
    }

    protected DatiGeneraliDocumentoType getDatiGeneraliDocumentoV11(AreaMagazzino areaMagazzino) {

        Documento documento = areaMagazzino.getDocumento();

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

        // Obbligatorio se prevista l'imposta di bollo
        // datiGeneraliDocumento --> DatiBollo

        // Obbligatorio se è previsto un contributo previdenziale
        // datiGeneraliDocumento.getDatiCassaPrevidenziale().add(e)

        // Dati non obbligatori
        // datiGeneraliDocumento --> Arrotondamento
        // datiGeneraliDocumento --> Art73
        // datiGeneraliDocumento --> Causale
        datiGeneraliDocumento.setImportoTotaleDocumento(documento.getTotale().getImportoInValuta());

        return datiGeneraliDocumento;
    }

    private DatiGeneraliType getDatiGeneraliV11(AreaMagazzino areaMagazzino) {
        DatiGeneraliType datiGenerali = new DatiGeneraliType();
        datiGenerali.setDatiGeneraliDocumento(getDatiGeneraliDocumentoV11(areaMagazzino));

        // Nei casi in cui sia presente un documento di trasporto collegato alla fattura, casi di
        // fatturazione differita, vanno valorizzati i seguenti campi per ogni documento di
        // trasporto.
        datiGenerali.getDatiDDT().addAll(getDatiDDT(areaMagazzino));

        // Presente nei casi di fatture per operazioni accessorie, emesse dagli ‘autotrasportatori’ per usufruire delle
        // agevolazioni in materia di registrazione e pagamento IVA.
        // datiGenerali --> FatturaPrincipale

        return datiGenerali;
    }

    private List<DatiPagamentoType> getDatiPagamento(AreaMagazzino areaMagazzino) {
        List<DatiPagamentoType> pagamenti = new ArrayList<DatiPagamentoType>();

        AreaRate areaRate = areaRateManager.caricaAreaRate(areaMagazzino.getDocumento());
        if (areaRate != null && areaRate.getRate() != null && !areaRate.getRate().isEmpty()) {

            DatiPagamentoType datiPagamento = new DatiPagamentoType();
            datiPagamento.setCondizioniPagamento(CondizioniPagamentoType.TP_02);
            if (areaRate.getRate().size() > 1) {
                datiPagamento.setCondizioniPagamento(CondizioniPagamentoType.TP_01);
            }
            for (Rata rata : areaRate.getRate()) {
                DettaglioPagamentoType dettaglio = new DettaglioPagamentoType();
                switch (rata.getTipoPagamento()) {
                case BOLLETTINO_FRECCIA:
                    dettaglio.setModalitaPagamento(ModalitaPagamentoType.MP_07);
                    break;
                case RIBA:
                    dettaglio.setModalitaPagamento(ModalitaPagamentoType.MP_12);
                    break;
                case RID:
                    dettaglio.setModalitaPagamento(ModalitaPagamentoType.MP_09);
                    break;
                case RIMESSA_DIRETTA:
                    dettaglio.setModalitaPagamento(ModalitaPagamentoType.MP_01);
                    break;
                default:
                    dettaglio.setModalitaPagamento(ModalitaPagamentoType.MP_05);
                    break;
                }

                dettaglio.setDataRiferimentoTerminiPagamento(
                        FatturazionePAUtils.getXMLGregorianCalendar(areaMagazzino.getDocumento().getDataDocumento()));
                long diff = rata.getDataScadenza().getTime()
                        - areaMagazzino.getDocumento().getDataDocumento().getTime();
                Long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                dettaglio.setGiorniTerminiPagamento(days.intValue());
                dettaglio.setDataScadenzaPagamento(FatturazionePAUtils.getXMLGregorianCalendar(rata.getDataScadenza()));
                dettaglio.setImportoPagamento(rata.getImporto().getImportoInValutaAzienda());

                RapportoBancarioAzienda rappBancarioAzienda = rata.getRapportoBancarioAzienda();
                if (rappBancarioAzienda != null && !rappBancarioAzienda.isNew()) {
                    dettaglio.setIstitutoFinanziario(
                            StringUtils.left(rappBancarioAzienda.getBanca().getDescrizione(), 80));
                    dettaglio.setIBAN(StringUtils.replace(rappBancarioAzienda.getIban(), " ", ""));
                    dettaglio.setABI(rappBancarioAzienda.getBanca().getCodice());
                    dettaglio.setCAB(rappBancarioAzienda.getFiliale().getCodice());
                    dettaglio.setBIC(rappBancarioAzienda.getBic());
                }

                datiPagamento.getDettaglioPagamento().add(dettaglio);
            }
            pagamenti.add(datiPagamento);
        }

        return pagamenti;
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
            }
            riepilogo.setRiferimentoNormativo(StringUtils.left(rigaIva.getCodiceIva().getDescrizioneDocumenti(), 100));
            // riepilogo --> SpeseAccessorie
            riepilogo.setArrotondamento(BigDecimal.ZERO);

            riepilogo.setEsigibilitaIVA(EsigibilitaIVAType.I);
            if (rigaIva.getCodiceIva().getSplitPayment() != null
                    && Objects.equals(Boolean.TRUE, rigaIva.getCodiceIva().getSplitPayment())) {
                riepilogo.setEsigibilitaIVA(EsigibilitaIVAType.S);
            } else if (rigaIva.getCodiceIva().isIvaSospesa()) {
                riepilogo.setEsigibilitaIVA(EsigibilitaIVAType.D);
            }
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
            String descrizione = rigaArticolo.getDescrizione();
            if (!StringUtils.isBlank(rigaArticolo.getNoteRiga())) {
                descrizione = descrizione.concat(" ").concat(rigaArticolo.getNoteRiga().replaceAll("\\<[^>]*>", ""));
                descrizione = descrizione.replaceAll("  ", "");
                descrizione = descrizione.replaceAll("\n", " ");
                descrizione = StringUtils.left(descrizione, 1000);
            }
            dettaglio.setDescrizione(descrizione);
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

            BigDecimal importoUnitario = rigaArticolo.getPrezzoUnitario().getImportoInValuta();
            BigDecimal importoSconto = BigDecimal.ZERO;
            Sconto sconto = new Sconto();
            // Sconti/Maggiorazioni
            if (rigaArticolo.getVariazione1() != null
                    && BigDecimal.ZERO.compareTo(rigaArticolo.getVariazione1()) != 0) {
                sconto.setSconto1(rigaArticolo.getVariazione1());
                importoSconto = importoUnitario
                        .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
                dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione1(), 1));
                importoUnitario = importoUnitario.subtract(importoSconto);
            }
            if (rigaArticolo.getVariazione2() != null
                    && BigDecimal.ZERO.compareTo(rigaArticolo.getVariazione2()) != 0) {
                sconto.setSconto1(rigaArticolo.getVariazione2());
                importoSconto = importoUnitario
                        .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
                dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione2(), 1));
                importoUnitario = importoUnitario.subtract(importoSconto);
            }
            if (rigaArticolo.getVariazione3() != null
                    && BigDecimal.ZERO.compareTo(rigaArticolo.getVariazione3()) != 0) {
                sconto.setSconto1(rigaArticolo.getVariazione3());
                importoSconto = importoUnitario
                        .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
                dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione3(), 1));
                importoUnitario = importoUnitario.subtract(importoSconto);
            }
            if (rigaArticolo.getVariazione4() != null
                    && BigDecimal.ZERO.compareTo(rigaArticolo.getVariazione4()) != 0) {
                sconto.setSconto1(rigaArticolo.getVariazione4());
                importoSconto = importoUnitario
                        .subtract(sconto.applica(importoUnitario, rigaArticolo.getNumeroDecimaliPrezzo()));
                dettaglio.getScontoMaggiorazione().add(getScontoType(importoSconto, rigaArticolo.getVariazione4(), 1));
                importoUnitario = importoUnitario.subtract(importoSconto);
            }

            dettaglioLinee.add(dettaglio);

            numeroRiga++;
        }

        return dettaglioLinee;
    }
}
