package it.eurotn.panjea.contabilita.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.contabilita.domain.EntitaCointestazione;
import it.eurotn.panjea.contabilita.manager.spesometro.entitacointestazione.interfaces.EntitaCointestazioneManager;
import it.eurotn.panjea.contabilita.manager.spesometro.interfaces.SpesometroManager;
import it.eurotn.panjea.contabilita.service.interfaces.ComunicazionePolivalenteService;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.ComunicazionePolivalenteService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ComunicazionePolivalenteService")
public class ComunicazionePolivalenteServiceBean implements ComunicazionePolivalenteService {

    @EJB
    private SpesometroManager spesometroManager;

    @EJB
    private EntitaCointestazioneManager entitaCointestazioneManager;

    @Override
    public void cancellaEntitaCointestazione(Integer id) {
        entitaCointestazioneManager.cancella(id);
    }

    @Override
    public List<DocumentoSpesometro> caricaDocumenti(ParametriCreazioneComPolivalente params) {
        return spesometroManager.caricaDocumentiSpesometro(params);
    }

    @Override
    public List<EntitaCointestazione> caricaEntitaCointestazione() {
        return entitaCointestazioneManager.caricaAll();
    }

    @Override
    public List<EntitaCointestazione> caricaEntitaCointestazioneByAreaContabile(Integer idAreaContabile) {
        return entitaCointestazioneManager.caricaByAreaContabile(idAreaContabile);
    }

    @Override
    public EntitaCointestazione caricaEntitaCointestazioneById(Integer id) {
        return entitaCointestazioneManager.caricaById(id);
    }

    @Override
    public byte[] genera(ParametriCreazioneComPolivalente params) {
        return spesometroManager.genera(params);
    }

    @Override
    public EntitaCointestazione salvaEntitaCointestazione(EntitaCointestazione entitaCointestazione) {
        return entitaCointestazioneManager.salva(entitaCointestazione);
    }
}
