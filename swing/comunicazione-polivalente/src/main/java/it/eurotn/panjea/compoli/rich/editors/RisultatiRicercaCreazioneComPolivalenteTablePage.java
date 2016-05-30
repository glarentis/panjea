package it.eurotn.panjea.compoli.rich.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.compoli.rich.bd.IComunicazionePolivalenteBD;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author fattazzo
 *
 */
public class RisultatiRicercaCreazioneComPolivalenteTablePage extends AbstractTablePageEditor<DocumentoSpesometro> {

    private class OpenAreeDocumentoCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            DocumentoSpesometro documento = getTable().getSelectedObject();
            if (documento != null) {
                command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, documento.getIdDocumento());
                return true;
            }
            return false;
        }
    }

    private IComunicazionePolivalenteBD comunicazionePolivalenteBD;

    private ParametriCreazioneComPolivalente parametriCreazione;

    private OpenAreeDocumentoCommand openAreeDocumentoCommand = null;
    private OpenAreeDocumentoCommandInterceptor openAreeDocumentoCommandInterceptor = null;

    /**
     * Costruttore.
     */
    public RisultatiRicercaCreazioneComPolivalenteTablePage() {
        super("risultatiRicercaCreazioneComPolivalenteTablePage",
                new RisultatiRicercaCreazioneComPolivalenteTableModel());
        getTable().setPropertyCommandExecutor(getOpenAreeDocumentoCommand());
    }

    @Override
    public void dispose() {
        getOpenAreeDocumentoCommand().removeCommandInterceptor(openAreeDocumentoCommandInterceptor);
        openAreeDocumentoCommandInterceptor = null;
        openAreeDocumentoCommand = null;
        super.dispose();
    }

    /**
     * @return the openAreaDocumentoRataCommand
     */
    public OpenAreeDocumentoCommand getOpenAreeDocumentoCommand() {
        if (openAreeDocumentoCommand == null) {
            openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
            openAreeDocumentoCommandInterceptor = new OpenAreeDocumentoCommandInterceptor();
            openAreeDocumentoCommand.addCommandInterceptor(openAreeDocumentoCommandInterceptor);
        }
        return openAreeDocumentoCommand;
    }

    @Override
    public Collection<DocumentoSpesometro> loadTableData() {
        List<DocumentoSpesometro> documenti = new ArrayList<>();
        if (this.parametriCreazione != null && this.parametriCreazione.isEffettuaRicerca()) {
            documenti = comunicazionePolivalenteBD.caricaDocumenti(parametriCreazione);
        }

        return documenti;
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public Collection<DocumentoSpesometro> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param comunicazionePolivalenteBD
     *            the comunicazionePolivalenteBD to set
     */
    public void setComunicazionePolivalenteBD(IComunicazionePolivalenteBD comunicazionePolivalenteBD) {
        this.comunicazionePolivalenteBD = comunicazionePolivalenteBD;
    }

    @Override
    public void setFormObject(Object object) {
        this.parametriCreazione = (ParametriCreazioneComPolivalente) object;
    }

}
