package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

public class RigaArticoloArticoloPropertyChange extends RigaArticoloDocumentoArticoloPropertyChange {

    private static Logger logger = Logger.getLogger(RigaArticoloArticoloPropertyChange.class);

    private IMagazzinoDocumentoBD magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
    private Boolean calcolaGiacenza;

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public RigaArticoloArticoloPropertyChange(final FormModel formModel) {
        super(formModel);
        calcolaGiacenza = null;
    }

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     * @param calcolaGiacenza
     *            indica se calcolare la giacenza dell'articolo quando creo la riga
     */
    public RigaArticoloArticoloPropertyChange(final FormModel formModel, final boolean calcolaGiacenza) {
        super(formModel);
        this.calcolaGiacenza = calcolaGiacenza;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(magazzinoDocumentoBD, "magazzinoDocumentoBD nullo");
    }

    @Override
    public IRigaArticoloDocumento creaRigaArticoloDocumento(ArticoloLite articolo) {
        IRigaArticoloDocumento rigaArticolo = null;
        AreaMagazzino areaMagazzino = null;
        CodicePagamento codicePagamento = null;
        try {
            if (calcolaGiacenza == null) {
                calcolaGiacenza = magazzinoAnagraficaBD.caricaMagazzinoSettings().getCalcolaGiacenzeInCreazioneRiga();
            }

            areaMagazzino = (AreaMagazzino) getFormModel().getValueModel("areaMagazzino").getValue();
            if (getFormModel().hasValueModel("codicePagamento")) {
                codicePagamento = (CodicePagamento) getFormModel().getValueModel("codicePagamento").getValue();
            }

            AgenteLite agente = null;
            if (getFormModel().hasValueModel("agente")) {
                agente = (AgenteLite) getFormModel().getValueModel("agente").getValue();
            }

            if (logger.isDebugEnabled()) {
                logger.debug("--> creaRigaArticolo " + articolo + " - " + areaMagazzino);
            }
            Integer idListinoAlternativo = null;
            Integer idListino = null;
            Integer idSedeEntita = null;
            Integer idTipoMezzo = null;
            Integer idEntita = null;
            String codiceLingua = null;
            Integer idAgente = null;
            BigDecimal percentualeScontoCommerciale = null;

            if (areaMagazzino.getListinoAlternativo() != null) {
                idListinoAlternativo = areaMagazzino.getListinoAlternativo().getId();
            }
            if (areaMagazzino.getListino() != null) {
                idListino = areaMagazzino.getListino().getId();
            }
            if (areaMagazzino.getDocumento().getSedeEntita() != null) {
                idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
                codiceLingua = areaMagazzino.getDocumento().getSedeEntita().getLingua();
                idEntita = areaMagazzino.getDocumento().getSedeEntita().getEntita().getId();
            }
            if (areaMagazzino.getMezzoTrasporto() != null) {
                idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
            }

            if (agente != null) {
                idAgente = agente.getId();
            }

            if (codicePagamento != null) {
                percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
            }

            ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
            parametri.setProvenienzaPrezzo(areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo());
            parametri.setIdArticolo(articolo.getId());
            parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
            parametri.setIdSedeEntita(idSedeEntita);
            parametri.setIdEntita(idEntita);
            parametri.setIdListinoAlternativo(idListinoAlternativo);
            parametri.setIdListino(idListino);
            parametri.setImporto(areaMagazzino.getDocumento().getTotale());
            parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
            parametri.setIdTipoMezzo(idTipoMezzo);
            parametri.setIdZonaGeografica(areaMagazzino.getIdZonaGeografica());
            parametri.setProvenienzaPrezzoArticolo(articolo.getProvenienzaPrezzoArticolo());
            parametri.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());
            parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
            parametri.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
            parametri.setCodiceLingua(codiceLingua);
            parametri.setIdAgente(idAgente);
            parametri.setTipologiaCodiceIvaAlternativo(areaMagazzino.getTipologiaCodiceIvaAlternativo());
            parametri.setPercentualeScontoCommerciale(percentualeScontoCommerciale);
            parametri.setGestioneConai(areaMagazzino.getTipoAreaMagazzino().isGestioneConai());
            parametri.setNotaCredito(areaMagazzino.getTipoAreaMagazzino().getTipoDocumento().isNotaCreditoEnable());
            parametri.setStrategiaTotalizzazioneDocumento(
                    areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

            Integer idDeposito = areaMagazzino.getDepositoOrigine().getId();
            parametri.setIdDeposito(idDeposito);

            parametri.setCalcolaGiacenza(calcolaGiacenza);

            rigaArticolo = magazzinoDocumentoBD.creaRigaArticolo(parametri);
            if (rigaArticolo != null) {
                ((RigaArticolo) rigaArticolo).setAreaMagazzino(areaMagazzino);
                ((RigaArticolo) rigaArticolo).setAgente(agente);

                // Se la qta è null allora ho un carico solamente a valore, quindi disabilito
                // l'input per la qta
                getFormModel().getFieldMetadata("qta").setEnabled(rigaArticolo.getQta() != null);
            }
        } catch (Exception e) {
            logger.error("--> errore nel creare una riga articolo ", e);
            throw new PanjeaRuntimeException(e);
        }

        return rigaArticolo;
    }

    /**
     *
     * @param magazzinoDocumentoBD
     *            IMagazzinoDocumentoBD
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

}
