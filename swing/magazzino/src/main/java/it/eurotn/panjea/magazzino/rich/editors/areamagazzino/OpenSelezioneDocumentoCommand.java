package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * Command per l'apertuta della DialogPage per la ricerca e selezione di {@link Documento}.
 *
 * @author adriano
 * @version 1.0, 10/set/2008
 */
public class OpenSelezioneDocumentoCommand extends ActionCommand {

    private static Logger logger = Logger.getLogger(OpenSelezioneDocumentoCommand.class);
    private static final String COMMAND_ID = "openSelezioneDocumentoCommand";
    public static final String PARAM_DOCUMENTI_ESISTENTI = "documenti";
    private final IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private final FormModel formModel;
    private boolean documentoScelto = false;

    /**
     * @param magazzinoDocumentoBD
     *            magazzinoDocumentoBD
     * @param formModel
     *            formModel
     */
    public OpenSelezioneDocumentoCommand(final IMagazzinoDocumentoBD magazzinoDocumentoBD, final FormModel formModel) {
        super(COMMAND_ID);
        setSecurityControllerId(COMMAND_ID);
        CommandConfigurer commandConfigurer = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        commandConfigurer.configure(this);
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
        this.formModel = formModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doExecuteCommand() {
        logger.debug("--> Enter doExecuteCommand " + COMMAND_ID);
        documentoScelto = false;
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) formModel.getFormObject();
        if (areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino() == null) {
            return;
        }

        Documento documento = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento();
        // tipoDocumento non è valorizzato all'interno di Documento ma è presente in
        // TipoAreaMagazzino: aggiorno
        // tipoDocumento in Documento
        documento.setTipoDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino().getTipoDocumento());

        Documento documentoFromForm = (Documento) PanjeaSwingUtil.cloneObject(documento);
        // aggiorna l'oggetto Documento da passare alla page SelezionaDocumentoPage
        SelezioneDocumentoPage selezioneDocumentoPage = new SelezioneDocumentoPage();
        selezioneDocumentoPage.setFormObject(documentoFromForm);
        selezioneDocumentoPage.setMagazzinoDocumentoBD(magazzinoDocumentoBD);

        List<Documento> documenti = (List<Documento>) getParameter(PARAM_DOCUMENTI_ESISTENTI,
                new ArrayList<Documento>());
        selezioneDocumentoPage.getRisultatiDocumentoTablePage().setRows(documenti);

        SelezioneDocumentoDialog selezionaDocumentoDialog = new SelezioneDocumentoDialog(selezioneDocumentoPage);
        selezionaDocumentoDialog.showDialog();

        documento = selezionaDocumentoDialog.getDocumentoSelected();
        if (documento != null) {

            // set del nuovo Documento all'interno dell'areaMagazzino di AreaMagazzinoFullDTO

            // aggiorno documento all'interno del FormModel
            try {

                // recupera il TipoAreaMagazzino legato al TipoDocumento di Documento e lo aggiorna
                TipoAreaMagazzino tipoAreaMagazzino = magazzinoDocumentoBD
                        .caricaTipoAreaMagazzinoPerTipoDocumento(documento.getTipoDocumento().getId());

                // ripristino gli attributi null ad istanze vuote
                formModel.getValueModel("areaMagazzino.tipoAreaMagazzino").setValue(tipoAreaMagazzino);

                formModel.getValueModel("areaMagazzino.documento").setValue(documento);

                // Settando il documento non mi lancia il propertyChange per l'importoBinding su
                // Totale.
                // e non si capisce il perchè. Risetto il valore per notificare il cambio del totale
                formModel.getValueModel("areaMagazzino.documento.totale").setValue(documento.getTotale());
            } catch (Exception e) {
                logger.error("--> errore ", e);
            }

            // Se presente carico l'area pagamento e la associo al form
            AreaRate areaRate = magazzinoDocumentoBD.caricaAreaRateByDocumento(documento);
            if (areaRate.getId() != null) {
                formModel.getValueModel("areaRate").setValue(areaRate);
            }
            documentoScelto = true;
            // HACK: se non risetto l'intero documento rimangono delle proprieta' a null che
            // impediscono il salvataggio;
            // dato il numero di combinazione di tipi documenti possibili non posso controllare
            // tutte le proprieta' e
            // non posso neanche eliminare i setValue sul form model che mi servono per attivare i
            // propertyChange del
            // magazzino.
            // Nota: chiamare la removeNullValue soltanto senza risettare l'oggetto nel formmodel
            // non ha effetto.
            formModel.setFormObject(areaMagazzinoFullDTO);
        }

        logger.debug("--> Exit doExecuteCommand " + COMMAND_ID);
    }

    /**
     * @return documentoScelto
     */
    public boolean isDocumentoScelto() {
        return documentoScelto;
    }

}