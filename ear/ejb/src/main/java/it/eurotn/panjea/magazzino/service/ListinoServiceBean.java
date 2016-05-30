package it.eurotn.panjea.magazzino.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.exception.ArticoliDuplicatiManutenzioneListinoException;
import it.eurotn.panjea.magazzino.exception.ListinoManutenzioneNonValidoException;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.ListinoStatisticheManager;
import it.eurotn.panjea.magazzino.manager.manutenzionelistino.interfaces.ManutenzioneListinoManager;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.interfaces.ListinoService;
import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ListinoService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ListinoService")
public class ListinoServiceBean implements ListinoService {

    @EJB
    private ListinoManager listinoManager;

    @EJB
    private ManutenzioneListinoManager manutenzionelistinoManager;

    @EJB
    private ListinoStatisticheManager listinoStatisticheManager;

    @Override
    @TransactionTimeout(value = 7200)
    public void aggiornaListinoDaManutenzione(ParametriAggiornaManutenzioneListino parametriAggiornaManutenzioneListino)
            throws ArticoliDuplicatiManutenzioneListinoException {
        manutenzionelistinoManager.aggiornaListinoDaManutenzione(parametriAggiornaManutenzioneListino);
    }

    @Override
    public void cancellaListino(Listino listino) {
        listinoManager.cancellaListino(listino);
    }

    @Override
    public void cancellaRigaListino(RigaListino rigaListino) {
        listinoManager.cancellaRigaListino(rigaListino);
    }

    @Override
    public void cancellaRigheListino(List<RigaListino> righeListino) {
        listinoManager.cancellaRigheListino(righeListino);
    }

    @Override
    public void cancellaRigheManutenzioneListino(List<RigaManutenzioneListino> righeManutenzioneListino) {
        manutenzionelistinoManager.cancellaRigheManutenzioneListino(righeManutenzioneListino);
    }

    @Override
    public void cancellaVersioneListino(VersioneListino versioneListino) {
        listinoManager.cancellaVersioneListino(versioneListino);
    }

    @Override
    public ConfrontoListinoDTO caricaConfrontoListino(ParametriRicercaConfrontoListino parametri) {
        return listinoStatisticheManager.caricaConfrontoListino(parametri);
    }

    @Override
    public BigDecimal caricaImportoListino(Integer idListino, Integer idArticolo) {
        return listinoManager.caricaImportoListino(idListino, idArticolo);
    }

    @Override
    public List<Listino> caricaListini() {
        return listinoManager.caricaListini();
    }

    @Override
    public List<Listino> caricaListini(ETipoListino tipoListino, String searchField, String searchValue) {
        return listinoManager.caricaListini(tipoListino, searchField, searchValue);
    }

    @Override
    public Listino caricaListino(Listino listino, boolean initializeRighe) {
        return listinoManager.caricaListino(listino, initializeRighe);
    }

    @Override
    public RigaListino caricaRigaListino(Integer idRiga) {
        return listinoManager.caricaRigaListino(idRiga);
    }

    @Override
    public List<RigaListinoDTO> caricaRigheListinoByArticolo(Integer idArticolo) {
        return listinoManager.caricaRigheListinoByArticolo(idArticolo);
    }

    @Override
    public List<RigaListinoDTO> caricaRigheListinoByVersione(Integer idVersioneListino) {
        VersioneListino versioneListino = new VersioneListino();
        versioneListino.setId(idVersioneListino);
        return listinoManager.caricaRigheListinoByVersione(versioneListino);
    }

    @Override
    public List<RigaListino> caricaRigheListinoDaAggiornare(Date data, List<ArticoloLite> articoli) {
        return listinoManager.caricaRigheListinoDaAggiornare(data, articoli);
    }

    @Override
    public List<RigaManutenzioneListino> caricaRigheManutenzioneListino() {
        return manutenzionelistinoManager.caricaRigheManutenzioneListino();
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaSediMagazzinoByListino(Listino listino) {
        return listinoManager.caricaSediMagazzinoByListino(listino);
    }

    @Override
    public List<ScaglioneListinoStorico> caricaStoricoScaglione(ScaglioneListino scaglioneListino,
            Integer numeroVersione) {
        return listinoManager.caricaStoricoScaglione(scaglioneListino, numeroVersione);
    }

    @Override
    public VersioneListino caricaVersioneListino(Map<Object, Object> parametri) {
        return listinoManager.caricaVersioneListino(parametri);
    }

    @Override
    public VersioneListino caricaVersioneListino(VersioneListino versioneListino, boolean initializeLazy) {
        return listinoManager.caricaVersioneListino(versioneListino, initializeLazy);
    }

    @Override
    public VersioneListino caricaVersioneListinoByData(Listino listino, Date data) {
        return listinoManager.caricaVersioneListinoByData(listino, data);
    }

    @Override
    public List<VersioneListino> caricaVersioniListino(String fieldSearch, String valueSearch,
            ETipoListino tipoListino) {
        return listinoManager.caricaVersioniListino(fieldSearch, valueSearch, tipoListino);
    }

    @Override
    public VersioneListino copiaVersioneListino(VersioneListino versioneListino, Date dataNuovaVersioneListino) {
        return listinoManager.copiaVersioneListino(versioneListino, dataNuovaVersioneListino);
    }

    @Override
    public void inserisciRigheRicercaManutenzioneListino(
            ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino)
                    throws ListinoManutenzioneNonValidoException {
        manutenzionelistinoManager.inserisciRigheRicercaManutenzioneListino(parametriRicercaManutenzioneListino);
    }

    @Override
    public Listino salvaListino(Listino listino) {
        return listinoManager.salvaListino(listino);
    }

    @Override
    public List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino)
            throws RigheListinoListiniCollegatiException {
        return listinoManager.salvaPrezzoRigheListino(listRigheListino);
    }

    @Override
    public List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino,
            boolean aggiornaListiniCollegati) {
        return listinoManager.salvaPrezzoRigheListino(listRigheListino, aggiornaListiniCollegati);
    }

    @Override
    public RigaListino salvaRigaListino(RigaListino rigaListino) throws RigaListinoListiniCollegatiException {
        return listinoManager.salvaRigaListino(rigaListino);
    }

    @Override
    public RigaListino salvaRigaListino(RigaListino rigaListino, boolean aggiornaListiniCollegati) {
        return listinoManager.salvaRigaListino(rigaListino, aggiornaListiniCollegati);
    }

    @Override
    public List<RigaManutenzioneListino> salvaRigaManutenzioneListino(RigaManutenzioneListino rigaManutenzioneListino) {
        return manutenzionelistinoManager.salvaRigaManutenzioneListino(rigaManutenzioneListino);
    }

    @Override
    public VersioneListino salvaVersioneListino(VersioneListino versioneListino) {
        return listinoManager.salvaVersioneListino(versioneListino);
    }
}
