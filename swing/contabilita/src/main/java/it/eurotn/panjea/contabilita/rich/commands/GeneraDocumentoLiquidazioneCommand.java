package it.eurotn.panjea.contabilita.rich.commands;

import java.util.Date;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * Genera il documento di liquidazione per il periodo scelto.
 *
 * @author giangi
 *
 */
public class GeneraDocumentoLiquidazioneCommand extends ActionCommand {
    private static final String COMMAND_ID = "generaDocumentoLiquidazioneCommand";
    private IContabilitaBD contabilitaBD = null;
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
    private AziendaCorrente aziendaCorrente = null;
    private Integer anno;
    private Date dataDocumento;

    /**
     * Costruttore.
     */
    public GeneraDocumentoLiquidazioneCommand() {
        super(COMMAND_ID);
    }

    @Override
    protected void doExecuteCommand() {
        // Carico il tipo documento base per la liquidazione

        List<TipoDocumentoBase> tipiDocumentoBase = contabilitaAnagraficaBD.caricaTipiDocumentoBase();
        TipoDocumentoBase tipoDocumentoBase = null;
        for (TipoDocumentoBase tipoDocumentoBaseItem : tipiDocumentoBase) {
            if (tipoDocumentoBaseItem.getTipoOperazione() == TipoOperazioneTipoDocumento.LIQUIDAZIONE_IVA) {
                tipoDocumentoBase = tipoDocumentoBaseItem;
                break;
            }
        }
        if (tipoDocumentoBase == null) {
            PanjeaSwingUtil.checkAndThrowException(new TipoDocumentoBaseException(
                    new String[] { "Tipo operazione " + TipoOperazioneTipoDocumento.LIQUIDAZIONE_IVA.name() }));
        }

        // Creo il documento con il tipoAreaContabile trovato
        AreaContabile areaContabile = new AreaContabile(anno, dataDocumento, tipoDocumentoBase.getTipoAreaContabile());
        String codiceValuta = aziendaCorrente.getCodiceValuta();
        // Imposto il codice valuta dato che e' obbligatorio
        areaContabile.getDocumento().getTotale().setCodiceValuta(codiceValuta);
        contabilitaBD.salvaDocumentoLiquidazione(areaContabile);
    }

    /**
     * @return anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return azienda corrente
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    /**
     * @return contabilitaAnagraficaBD
     */
    public IContabilitaAnagraficaBD getContabilitaAnagraficaBD() {
        return contabilitaAnagraficaBD;
    }

    /**
     * @return contabilitaBD
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    /**
     * @return data documento
     */
    public Date getDataDocumento() {
        return dataDocumento;
    }

    /**
     * @param anno
     *            anno
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param aziendaCorrente
     *            azienda corrente
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * @param contabilitaBD
     *            contabilitaBD
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    /**
     * @param dataDocumento
     *            data documento
     */
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

}
