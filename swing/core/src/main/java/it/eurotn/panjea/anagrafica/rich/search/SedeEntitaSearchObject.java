/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.editors.entita.SedeEntitaForm;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.search.AbstractSearchObject;

/**
 * SearchObject per {@link SedeEntita}.
 *
 * @author adriano
 * @version 1.0, 02/set/2008
 */
public class SedeEntitaSearchObject extends AbstractSearchObject {

    private static Logger logger = Logger.getLogger(SedeEntitaSearchObject.class);

    public static final String PARAMETER_ENTITA_ID = "paramEntita";
    public static final String PARAMETER_TIPO_SEDE_FILTER = "paramTipoSedeFilter";
    public static final String PARAMETER_SEDE_DISABILITATE = "paramDisabilitate";

    private static final String SEARCH_OBJECT_ID = "sedeEntitaSearchObject";

    private IAnagraficaBD anagraficaBD;

    /**
     * Costruttore.
     */
    public SedeEntitaSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return null;
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        Map<String, Object> parameters = searchPanel.getMapParameters();
        logger.debug("--> Enter getData");
        List<SedeEntita> sedi = new ArrayList<SedeEntita>();
        if (parameters.containsKey(PARAMETER_ENTITA_ID)) {
            logger.debug("--> trovato param entita ");
            EntitaLite entitaLite = (EntitaLite) parameters.get(PARAMETER_ENTITA_ID);
            // NPE MAIL
            if (entitaLite != null) {
                Integer idEntita = entitaLite.getId();
                CaricamentoSediEntita caricamentoSediEntita = CaricamentoSediEntita.ESCLUSE_SEDI_SPEDIZIONE_SERVIZI;
                Boolean caricaSedeDisabilitate = false;
                if (parameters.containsKey(PARAMETER_TIPO_SEDE_FILTER)) {
                    caricamentoSediEntita = (CaricamentoSediEntita) parameters.get(PARAMETER_TIPO_SEDE_FILTER);
                }
                if (parameters.containsKey(PARAMETER_SEDE_DISABILITATE)) {
                    caricaSedeDisabilitate = (Boolean) parameters.get(PARAMETER_SEDE_DISABILITATE);
                }
                if (idEntita != null) {
                    sedi = anagraficaBD.caricaSediEntita(valueSearch, idEntita, caricamentoSediEntita,
                            caricaSedeDisabilitate);
                }
            }
        }
        logger.debug("--> Exit getData");
        return sedi;
    }

    @Override
    public void openDialogPage(Object object) {
        // recupero l'entita' dal form di origine (areaMagazzinoForm)
        final String sedeEntitaPath = searchPanel.getFormPropertyPath();
        if (!sedeEntitaPath.endsWith("documento.sedeEntita")) {
            return;
        }
        String entitaPath = sedeEntitaPath.replace("sedeEntita", "entita");
        EntitaLite entitaLite = (EntitaLite) searchPanel.getFormModel().getValueModel(entitaPath).getValue();

        if (entitaLite.getId() != null) {

            SedeEntita sedeEntita = (SedeEntita) object;
            // verifico se la sede presente
            if (sedeEntita.isNew()) {
                // creo la sede con l'entita' di origine
                sedeEntita = anagraficaBD.creaSedeEntitaGenerica(entitaLite.getId());
            } else {
                // ricarico la sede entità con i valori lazy inizializzati
                sedeEntita = anagraficaBD.caricaSedeEntita(sedeEntita.getId(), false);
            }
            final SedeEntitaForm sedeEntitaForm = new SedeEntitaForm(new SedeEntita(), true);
            sedeEntitaForm.setTipoEntita(entitaLite.creaEntitaDocumento().getTipoEntita());

            sedeEntitaForm.getControl();
            sedeEntitaForm.setFormObject(sedeEntita);

            // creo il dialogo con il form sedeEntitaForm
            PanjeaTitledPageApplicationDialog dialogSede = new PanjeaTitledPageApplicationDialog(sedeEntitaForm,
                    Application.instance().getActiveWindow().getControl()) {

                @Override
                protected void onCancel() {
                    super.onCancel();
                }

                @Override
                protected boolean onFinish() {
                    // recupero dal form sedeEntitaForm la nuova sede da
                    // inserire per l'entita' selezionata
                    SedeEntita sedeEntitaDaSalvare = (SedeEntita) sedeEntitaForm.getFormObject();
                    SedeEntita sedeEntitaSalvata = null;
                    // salvo l'entita'
                    try {
                        sedeEntitaSalvata = anagraficaBD.salvaSedeEntita(sedeEntitaDaSalvare);
                    } catch (SedeEntitaPrincipaleAlreadyExistException e) {
                        throw new PanjeaRuntimeException(e);
                    }
                    searchPanel.getFormModel().getValueModel(sedeEntitaPath).setValue(sedeEntitaSalvata);
                    return true;
                }
            };
            dialogSede.setTitle("Inserimento sede da documento");
            dialogSede.showDialog();
        }
    }

    /**
     * @param anagraficaBD
     *            The anagraficaBD to set.
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

}
