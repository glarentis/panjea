package it.eurotn.panjea.anagrafica.rich.commands;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.bd.DocumentiBD;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;

/**
 * Comando che apre l'area del documento. Se esistono più aree verrà visualizzata una dialog per la scelta dell'area da
 * aprire.
 *
 * @author fattazzo
 *
 */
public class OpenAreeDocumentoCommand extends ApplicationWindowAwareCommand {

    public static final String COMMAND_ID = "openAreeDocumentoCommand";

    public static final String PARAM_ID_DOCUMENTO = "paramIdDocumento";

    private final IDocumentiBD documentiBD;

    /**
     * Costruttore.
     */
    public OpenAreeDocumentoCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
        documentiBD = RcpSupport.getBean(DocumentiBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {
        final Integer idDocumento = (Integer) getParameter(PARAM_ID_DOCUMENTO);
        final Documento documento = new Documento();
        documento.setId(idDocumento);

        if (idDocumento != null) {

            final List<Object> list = documentiBD.caricaAreeDocumento(documento.getId());

            if (!CollectionUtils.isEmpty(list)) {
                // se la lista contiene solo 1 area apro il suo editor
                if (list.size() == 1) {
                    final LifecycleApplicationEvent event = new OpenEditorEvent(list.get(0));
                    Application.instance().getApplicationContext().publishEvent(event);
                } else {
                    // altrimenti chiedo all'utente quale area caricare
                    final Documento documentoLoad = documentiBD.caricaDocumento(documento.getId());

                    final ApplicationDialog dialog = new ChooseAreaForDocumentoDialog(documentoLoad, list);
                    dialog.showDialog();
                }
            }

        }
    }
}
