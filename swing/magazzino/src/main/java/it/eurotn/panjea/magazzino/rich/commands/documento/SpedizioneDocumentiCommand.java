package it.eurotn.panjea.magazzino.rich.commands.documento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica.TipoSpedizioneDocumenti;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.SpedizioneMovimenti;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.TipoLayout;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class SpedizioneDocumentiCommand<T extends IAreaDocumento> extends ActionCommand {

    public static final String PARAM_ID_DOCUMENTI_DA_SPEDIRE = "idDocumentiDaSpedire";
    public static final String PARAM_TIPO_LAYOUT = "tipoLayout";

    public static final String PARAM_FORZA_STAMPA = "forzaStampa";

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private ISicurezzaBD sicurezzaBD;
    private PluginManager pluginManager;
    private ActionCommand openFatturazionePACommand;

    private Class<T> areaDocumentoClass;

    /**
     * Costruttore.
     *
     * @param areaDocumentoClass
     *            classe delle aree dei documenti
     */
    public SpedizioneDocumentiCommand(final Class<T> areaDocumentoClass) {
        this("spedizioneDocumentiCommand", areaDocumentoClass);
    }

    /**
     * Costruttore.
     *
     * @param commandId
     *            id del comando
     * @param areaDocumentoClass
     *            classe delle aree dei documenti
     */
    public SpedizioneDocumentiCommand(final String commandId, final Class<T> areaDocumentoClass) {
        super(commandId);
        RcpSupport.configure(this);

        this.areaDocumentoClass = areaDocumentoClass;

        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
        this.sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);

        this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

        openFatturazionePACommand = RcpSupport.getCommand("openIdAreeFatturaPARicercaCommand");
    }

    @Override
    protected void doExecuteCommand() {

        @SuppressWarnings("unchecked")
        List<Integer> idDocumentiDaSpedire = (List<Integer>) getParameter(PARAM_ID_DOCUMENTI_DA_SPEDIRE, null);
        if (idDocumentiDaSpedire == null || idDocumentiDaSpedire.isEmpty()) {
            return;
        }

        boolean forzaStampa = (boolean) getParameter(PARAM_FORZA_STAMPA, false);

        // controllo se presente il plugin della fatturazione PA perchè gli eventuali documenti
        // verranno aperti
        // nell'editor dedicato
        boolean pluginFatturazionePAPresente = pluginManager.isPresente(PluginManager.PLUGIN_FATTURAZIONE_PA);

        Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

        TipoLayout tipoLayout = (TipoLayout) getParameter(PARAM_TIPO_LAYOUT, null);
        SpedizioneMovimenti spedizioneMovimenti = new SpedizioneMovimenti();
        spedizioneMovimenti.setUtente(utente);
        spedizioneMovimenti.setTipoLayout(tipoLayout);

        List<Integer> idAreaFatturaPA = new ArrayList<Integer>();

        List<MovimentoSpedizioneDTO> movimentiPerSpedizione = magazzinoDocumentoBD
                .caricaMovimentiPerSpedizione(areaDocumentoClass, idDocumentiDaSpedire);
        for (MovimentoSpedizioneDTO movimentoSpedizioneDTO : movimentiPerSpedizione) {

            boolean isFatturazionePA = pluginFatturazionePAPresente && movimentoSpedizioneDTO.isFatturaPA();

            if (isFatturazionePA) {
                idAreaFatturaPA.add(movimentoSpedizioneDTO.getAreaDocumento().getId());
            } else {
                if (forzaStampa) {
                    movimentoSpedizioneDTO.setTipoSpedizioneDocumenti(TipoSpedizioneDocumenti.STAMPA);
                }
                spedizioneMovimenti.getMovimenti().add(movimentoSpedizioneDTO);
            }
        }

        // documenti da spedire
        if (!spedizioneMovimenti.getMovimenti().isEmpty()) {
            LifecycleApplicationEvent event = new OpenEditorEvent(spedizioneMovimenti);
            Application.instance().getApplicationContext().publishEvent(event);
        }

        // editor per gestione della fatturazione PA
        if (!idAreaFatturaPA.isEmpty() && openFatturazionePACommand != null) {
            openFatturazionePACommand.addParameter("paramIdAreeMagazzino", idAreaFatturaPA);
            openFatturazionePACommand.execute();
        }
    }

}
