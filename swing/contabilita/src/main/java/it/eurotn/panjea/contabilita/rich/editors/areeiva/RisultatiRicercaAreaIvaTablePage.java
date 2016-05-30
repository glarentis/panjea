package it.eurotn.panjea.contabilita.rich.editors.areeiva;

import java.util.Collections;
import java.util.List;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * TablePage per la visualizzazione dei risultati della ricerca di {@link RigaIvaRicercaDTO}.
 *
 */
public class RisultatiRicercaAreaIvaTablePage extends AbstractTablePageEditor<RigaIvaRicercaDTO> {

    public static final String PAGE_ID = "risultatiRicercaAreaIvaTablePage";

    protected ParametriRicercaRigheIva parametriRicercaRigheIva = null;

    private IContabilitaBD contabilitaBD;

    private OpenAreeDocumentoCommand openAreeDocumentoCommand;

    /**
     * Costruttore.
     */
    public RisultatiRicercaAreaIvaTablePage() {
        super(PAGE_ID, new AreeIvaRicercaTableModel());
        getTable().setPropertyCommandExecutor(getOpenAreeDocumentoCommand());
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] {};
    }

    /**
     * @return the openAreeDocumentoCommand
     */
    public OpenAreeDocumentoCommand getOpenAreeDocumentoCommand() {
        if (openAreeDocumentoCommand == null) {
            openAreeDocumentoCommand = new OpenAreeDocumentoCommand();
            openAreeDocumentoCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {

                @Override
                public boolean preExecution(ActionCommand command) {
                    RigaIvaRicercaDTO rigaIvaRicercaDTO = getTable().getSelectedObject();
                    if (rigaIvaRicercaDTO != null) {
                        command.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO,
                                rigaIvaRicercaDTO.getIdDocumento());
                        return true;
                    }
                    return false;
                }
            });
        }

        return openAreeDocumentoCommand;
    }

    @Override
    public List<RigaIvaRicercaDTO> loadTableData() {
        List<RigaIvaRicercaDTO> righeIvaDTO = Collections.emptyList();

        if (parametriRicercaRigheIva.isEffettuaRicerca()) {
            righeIvaDTO = contabilitaBD.ricercaRigheIva(parametriRicercaRigheIva);
        }

        return righeIvaDTO;
    }

    @Override
    public void onPostPageOpen() {
        // nothing to do
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public List<RigaIvaRicercaDTO> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaRigheIva) {
            this.parametriRicercaRigheIva = (ParametriRicercaRigheIva) object;
        } else {
            this.parametriRicercaRigheIva = new ParametriRicercaRigheIva();
        }
    }
}
