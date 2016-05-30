package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.magazzino.rich.commands.documento.SpedizioneDocumentiCommand;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaAreaPreventivo;

public class RisultatiRicercaAreaPreventivoTablePage extends
        AbstractRisultatiRicercaAreaDocumentoTablePage<AreaPreventivo, AreaPreventivoFullDTO, TipoAreaPreventivo, AreaPreventivoRicerca, ParametriRicercaAreaPreventivo> {

    private class SpedizionePreventiviCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {

            List<AreaPreventivoRicerca> selectedObjects = getTable().getSelectedObjects();

            if (selectedObjects != null) {
                List<Integer> idDocumenti = new ArrayList<Integer>();
                for (AreaPreventivoRicerca areaPreventivoRicerca : selectedObjects) {
                    if (areaPreventivoRicerca.getStatoAreaPreventivo() != StatoAreaPreventivo.PROVVISORIO) {
                        idDocumenti.add(areaPreventivoRicerca.getDocumento().getId());
                    }
                }
                command.addParameter(SpedizioneDocumentiCommand.PARAM_ID_DOCUMENTI_DA_SPEDIRE, idDocumenti);
            }

            return selectedObjects != null && !selectedObjects.isEmpty();
        }
    }

    public static final String PAGE_ID = "risultatiRicercaAreaPreventivoTablePage";

    private SpedizioneDocumentiCommand<AreaPreventivo> spedizioneDocumentiCommand;
    private SpedizionePreventiviCommandInterceptor spedizioneOrdiniCommandInterceptor;

    /**
     * Costruttore.
     */
    public RisultatiRicercaAreaPreventivoTablePage() {
        super(PAGE_ID, new AreaPreventivoRicercaTableModel(), "openAreaPreventivoCommand");
    }

    @Override
    protected AreaPreventivo creaAreaDocumento() {
        return new AreaPreventivo();
    }

    @Override
    protected AreaPreventivoRicerca creaAreaDocumentoRicerca() {
        return new AreaPreventivoRicerca();
    }

    @Override
    protected List<AbstractCommand> creaCommandsList() {

        List<AbstractCommand> creaCommandsList = super.creaCommandsList();
        creaCommandsList.add(0, getSpedizioneDocumentiCommand());

        return creaCommandsList;
    }

    @Override
    protected ParametriRicercaAreaPreventivo creaParametriRicercaAreaDocumento() {
        return new ParametriRicercaAreaPreventivo();
    }

    @Override
    protected TipoAreaPreventivo creaTipoAreaDocumento() {
        return new TipoAreaPreventivo();
    }

    @Override
    public void dispose() {
        getSpedizioneDocumentiCommand().removeCommandInterceptor(spedizioneOrdiniCommandInterceptor);
        super.dispose();
    }

    /**
     *
     * @return areaBD
     */
    private IPreventiviBD getPreventiviBD() {
        return (IPreventiviBD) super.getAreaBD();
    }

    /**
     * @return the spedizioneDocumentiCommand
     */
    private SpedizioneDocumentiCommand<AreaPreventivo> getSpedizioneDocumentiCommand() {
        if (spedizioneDocumentiCommand == null) {
            spedizioneDocumentiCommand = new SpedizioneDocumentiCommand<AreaPreventivo>(AreaPreventivo.class);
            spedizioneOrdiniCommandInterceptor = new SpedizionePreventiviCommandInterceptor();
            spedizioneDocumentiCommand.addCommandInterceptor(spedizioneOrdiniCommandInterceptor);
        }

        return spedizioneDocumentiCommand;
    }

    @Override
    protected List<AreaPreventivoRicerca> ricercaAreeDocumento(ParametriRicercaAreaPreventivo parametri) {
        return getPreventiviBD().ricercaAreePreventivo(parametri);
    }

    /**
     * @param preventiviBD
     *            the preventiviBD to set
     */
    public void setPreventiviBD(IPreventiviBD preventiviBD) {
        super.setAreaBD(preventiviBD);
    }

}
