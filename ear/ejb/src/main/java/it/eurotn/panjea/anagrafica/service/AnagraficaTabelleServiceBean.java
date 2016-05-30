package it.eurotn.panjea.anagrafica.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.contratto.manager.interfaces.ContrattiSpesometroManager;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.CodiceIvaRicorsivoException;
import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.TipiDepositoManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficaTabelleManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheTipologieManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.CodiciIvaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.LinguaAziendaleDeleteException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaTabelleService;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AnagraficaTabelleService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AnagraficaTabelleService")
public class AnagraficaTabelleServiceBean implements AnagraficaTabelleService {

    @EJB
    private CodiciIvaManager codiciIvaManager;

    @EJB
    protected AnagraficheTipologieManager anagraficheTipologieManager;

    @EJB
    protected TipiDepositoManager tipiDepositoManager;

    @EJB
    protected AnagraficaTabelleManager anagraficaTabelleManager;

    @EJB
    protected ContrattiSpesometroManager contrattiSpesometroManager;

    @Override
    public Documento aggiungiContrattoADocumento(ContrattoSpesometro contratto, Documento documento) {
        return contrattiSpesometroManager.aggiungiContrattoADocumento(contratto, documento);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public void cancellaCarica(Carica carica) throws AnagraficaServiceException {
        anagraficheTipologieManager.cancellaCarica(carica);
    }

    @Override
    @RolesAllowed("modificaCodiciIva")
    public void cancellaCodiceIva(CodiceIva codiceIva) {
        codiciIvaManager.cancella(codiceIva);
    }

    @Override
    public void cancellaContratto(ContrattoSpesometro contratto) {
        contrattiSpesometroManager.cancellaContratto(contratto);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public void cancellaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura) {
        anagraficaTabelleManager.cancellaConversioneUnitaMisura(conversioneUnitaMisura);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public void cancellaFormaGiuridica(FormaGiuridica formaGiuridica) throws AnagraficaServiceException {
        anagraficheTipologieManager.cancellaFormaGiuridica(formaGiuridica);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public void cancellaLingua(Lingua lingua) throws LinguaAziendaleDeleteException {
        anagraficaTabelleManager.cancellaLingua(lingua);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public void cancellaMansione(Mansione mansione) throws AnagraficaServiceException {
        anagraficheTipologieManager.cancellaMansione(mansione);
    }

    @Override
    public void cancellaNotaAnagrafica(NotaAnagrafica notaAnagrafica) {
        anagraficaTabelleManager.cancellaNotaAnagrafica(notaAnagrafica);
    }

    @Override
    public void cancellaTipoDeposito(TipoDeposito tipoDeposito) {
        tipiDepositoManager.cancella(tipoDeposito);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public void cancellaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) throws AnagraficaServiceException {
        anagraficheTipologieManager.cancellaTipoSedeEntita(tipoSedeEntita);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public void cancellaUnitaMisura(UnitaMisura unitaMisura) {
        anagraficaTabelleManager.cancellaUnitaMisura(unitaMisura);
    }

    @Override
    @RolesAllowed("modificaDatiGeografici")
    public void cancellaZonaGeografica(ZonaGeografica zonaGeografica) {
        anagraficaTabelleManager.cancellaZonaGeografica(zonaGeografica);
    }

    @Override
    public List<Carica> caricaCariche(String fieldSearch, String valueSearch) throws AnagraficaServiceException {
        return anagraficheTipologieManager.caricaCariche(fieldSearch, valueSearch);
    }

    @Override
    public CodiceIva caricaCodiceIva(Integer id) {
        return codiciIvaManager.caricaById(id);
    }

    @Override
    public CodiceIva caricaCodiceIva(String codiceEuropa) throws ObjectNotFoundException {
        return codiciIvaManager.caricaCodiceIva(codiceEuropa);
    }

    @Override
    public List<CodiceIva> caricaCodiciIva(String codice) {
        return codiciIvaManager.caricaCodiciIva(codice);
    }

    @Override
    public List<ContrattoSpesometro> caricaContratti(EntitaLite entita) {
        return contrattiSpesometroManager.caricaContratti(entita);
    }

    @Override
    public ContrattoSpesometro caricaContratto(Integer idContratto) {
        return contrattiSpesometroManager.caricaContratto(idContratto);
    }

    @Override
    public ContrattoSpesometro caricaContratto(Integer idContratto, boolean loadCollection) {
        return contrattiSpesometroManager.caricaContratto(idContratto, loadCollection);
    }

    @Override
    public ConversioneUnitaMisura caricaConversioneUnitaMisura(String unitaMisuraOrigine,
            String unitaMisuraDestinazione) {
        return anagraficaTabelleManager.caricaConversioneUnitaMisura(unitaMisuraOrigine, unitaMisuraDestinazione);
    }

    @Override
    public List<ConversioneUnitaMisura> caricaConversioniUnitaMisura() {
        return anagraficaTabelleManager.caricaConversioniUnitaMisura();
    }

    @Override
    public List<Documento> caricaDocumentiContratto(Integer idContratto) {
        return contrattiSpesometroManager.caricaDocumentiContratto(idContratto);
    }

    @Override
    public FormaGiuridica caricaFormaGiuridica(Integer idFormaGiuridica)
            throws AnagraficaServiceException, ObjectNotFoundException {
        return anagraficheTipologieManager.caricaFormaGiuridica(idFormaGiuridica);
    }

    @Override
    public List<FormaGiuridica> caricaFormeGiuridiche(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException {
        return anagraficheTipologieManager.caricaFormeGiuridiche(fieldSearch, valueSearch);
    }

    @Override
    public Lingua caricaLingua(Lingua lingua) {
        return anagraficaTabelleManager.caricaLingua(lingua);
    }

    @Override
    public List<Lingua> caricaLingue() {
        return anagraficaTabelleManager.caricaLingue();
    }

    @Override
    public List<Mansione> caricaMansioni(String descrizione)
            throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException {
        return anagraficheTipologieManager.caricaMansioni(descrizione);
    }

    @Override
    public List<NotaAnagrafica> caricaNoteAnagrafica() {
        return anagraficaTabelleManager.caricaNoteAnagrafica();
    }

    @Override
    public List<TipoDeposito> caricaTipiDepositi() {
        return tipiDepositoManager.caricaAll();
    }

    @Override
    public List<TipoSedeEntita> caricaTipiSede(String codice) throws AnagraficaServiceException {
        return anagraficheTipologieManager.caricaTipiSede(codice);
    }

    @Override
    public List<TipoSedeEntita> caricaTipiSedeSecondari() throws AnagraficaServiceException {
        return anagraficheTipologieManager.caricaTipiSedeSecondari();
    }

    @Override
    public List<UnitaMisura> caricaUnitaMisura() {
        return anagraficaTabelleManager.caricaUnitaMisura();
    }

    @Override
    public List<UnitaMisura> caricaUnitaMisura(String codice) {
        return anagraficaTabelleManager.caricaUnitaMisura(codice);
    }

    @Override
    public UnitaMisura caricaUnitaMisura(UnitaMisura unitaMisura) {
        return anagraficaTabelleManager.caricaUnitaMisura(unitaMisura);
    }

    @Override
    public List<ZonaGeografica> caricaZoneGeografiche(String fieldSearch, String valueSearch) {
        return anagraficaTabelleManager.caricaZoneGeografiche(fieldSearch, valueSearch);
    }

    @Override
    public void rimuovContrattoDaDocumento(Documento documento) {
        contrattiSpesometroManager.rimuovContrattoDaDocumento(documento);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public Carica salvaCarica(Carica carica) {
        return anagraficheTipologieManager.salvaCarica(carica);
    }

    @Override
    @RolesAllowed("modificaCodiciIva")
    public CodiceIva salvaCodiceIva(CodiceIva codiceIva) throws CodiceIvaRicorsivoException {
        return codiciIvaManager.salvaCodiceIva(codiceIva);
    }

    @Override
    public ContrattoSpesometro salvaContratto(ContrattoSpesometro contratto) {
        return contrattiSpesometroManager.salvaContratto(contratto);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public ConversioneUnitaMisura salvaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura) {
        return anagraficaTabelleManager.salvaConversioneUnitaMisura(conversioneUnitaMisura);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public FormaGiuridica salvaFormaGiuridica(FormaGiuridica formaGiuridica) {
        return anagraficheTipologieManager.salvaFormaGiuridica(formaGiuridica);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public Lingua salvaLingua(Lingua lingua) {
        return anagraficaTabelleManager.salvaLingua(lingua);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public Mansione salvaMansione(Mansione mansione) {
        return anagraficheTipologieManager.salvaMansione(mansione);
    }

    @Override
    public NotaAnagrafica salvaNotaAnagrafica(NotaAnagrafica notaAnagrafica) {
        return anagraficaTabelleManager.salvaNotaAnagrafica(notaAnagrafica);
    }

    @Override
    public TipoDeposito salvaTipoDeposito(TipoDeposito tipoDeposito) {
        return tipiDepositoManager.salva(tipoDeposito);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public TipoSedeEntita salvaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) throws AnagraficaServiceException {
        return anagraficheTipologieManager.salvaTipoSedeEntita(tipoSedeEntita);
    }

    @Override
    @RolesAllowed("modificaTipologie")
    public UnitaMisura salvaUnitaMisura(UnitaMisura unitaMisura) {
        return anagraficaTabelleManager.salvaUnitaMisura(unitaMisura);
    }

    @Override
    @RolesAllowed("modificaDatiGeografici")
    public ZonaGeografica salvaZonaGeografica(ZonaGeografica zonaGeografica) {
        return anagraficaTabelleManager.salvaZonaGeografica(zonaGeografica);
    }

}
