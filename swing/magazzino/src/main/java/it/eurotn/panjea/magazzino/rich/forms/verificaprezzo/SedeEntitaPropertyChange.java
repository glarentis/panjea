package it.eurotn.panjea.magazzino.rich.forms.verificaprezzo;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

public class SedeEntitaPropertyChange implements FormModelPropertyChangeListeners {

    private static Logger logger = Logger.getLogger(SedeEntitaPropertyChange.class);
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private FormModel formModel;

    /**
     * @return the magazzinoDocumentoBD
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("--> Enter propertyChange");
        if (formModel.isReadOnly()) {
            return;
        }

        final SedeEntita sedeEntita = (SedeEntita) evt.getNewValue();
        // se ho selezionato un entità null esco
        if (sedeEntita == null) {
            return;
        }
        AsyncWorker.post(new AsyncTask() {

            @Override
            public void failure(Throwable error) {
                logger.error("--> Errore durante il caricamento della SedeAreaMagazzinoDTO.", error);
            }

            @Override
            public Object run() throws Exception {
                logger.debug("--> Carico la sedeMagazzinoDTO");
                SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = null;
                sedeAreaMagazzinoDTO = magazzinoDocumentoBD.caricaSedeAreaMagazzinoDTO(sedeEntita);
                return sedeAreaMagazzinoDTO;
            }

            @Override
            public void success(Object result) {
                logger.debug("--> Caricata la sedeMagazzinoDTO");
                SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = (SedeAreaMagazzinoDTO) result;
                Listino listino = new Listino();
                Listino listinoAlternativo = new Listino();
                CodicePagamento codicePagamento = new CodicePagamento();
                Integer idZonaGeografica = null;
                AgenteLite agente = null;
                if (sedeAreaMagazzinoDTO != null) {
                    idZonaGeografica = sedeAreaMagazzinoDTO.getIdZonaGeografica();
                    if (sedeAreaMagazzinoDTO.getListino() != null) {
                        listino = sedeAreaMagazzinoDTO.getListino();
                    }
                    if (sedeAreaMagazzinoDTO.getListinoAlternativo() != null) {
                        listinoAlternativo = sedeAreaMagazzinoDTO.getListinoAlternativo();
                    }
                    if (sedeAreaMagazzinoDTO.getCodicePagamento() != null) {
                        codicePagamento = sedeAreaMagazzinoDTO.getCodicePagamento();
                    }
                    if (sedeAreaMagazzinoDTO.getAgente() != null) {
                        agente = sedeAreaMagazzinoDTO.getAgente();
                    }
                }
                formModel.getValueModel("idZonaGeografica").setValue(idZonaGeografica);
                formModel.getValueModel("listino").setValue(listino);
                formModel.getValueModel("listinoAlternativo").setValue(listinoAlternativo);
                formModel.getValueModel("codicePagamento").setValue(codicePagamento);
                formModel.getValueModel("agente").setValue(agente);

                logger.debug("--> settata la sedeMagazzinoDTO");
            }
        });

        logger.debug("--> Exit setFormModel");
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

    /**
     * @param magazzinoDocumentoBD
     *            the magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }
}