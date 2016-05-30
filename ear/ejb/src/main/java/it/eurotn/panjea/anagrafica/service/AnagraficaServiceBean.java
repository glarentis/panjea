package it.eurotn.panjea.anagrafica.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.CodicePatternManager;
import it.eurotn.codice.generator.interfaces.VariabiliCodiceManager;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.StaleObjectStateException;
import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.agenti.manager.interfaces.SediAgenteManager;
import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.classedocumento.manager.interfaces.ClasseTipoDocumentoManager;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.manager.depositi.ParametriRicercaDepositi;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.BancheManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.ContattiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.anagrafica.manager.rapportibancarisedeentita.interfaces.RapportiBancariSedeEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.anagrafica.service.exception.ContattoOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.service.exception.TipoSedeEntitaNonTrovataException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.security.JecPrincipal;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AnagraficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AnagraficaService")
public class AnagraficaServiceBean implements AnagraficaService {

    private static Logger LOGGER = Logger.getLogger(AnagraficaServiceBean.class);
    @Resource
    private SessionContext context;
    @EJB
    private AziendeManager aziendeManager;
    @EJB
    private SediAziendaManager sediAziendaManager;
    @EJB
    private SediEntitaManager sediEntitaManager;
    @EJB
    private EntitaManager entitaManager;
    @EJB
    private ContattiManager contattiManager;
    @EJB
    private BancheManager bancheManager;
    @EJB
    private AnagraficheManager anagraficheManager;
    @EJB
    private ClasseTipoDocumentoManager classeTipoDocumentoManager;
    @EJB
    private ValutaManager valutaManager;
    @EJB
    private SediAgenteManager sediAgenteManager;
    @EJB
    private RitenutaAccontoManager ritenutaAccontoManager;
    @EJB
    private VariabiliCodiceManager variabiliCodiceManager;
    @EJB
    private CodicePatternManager codicePatternManager;
    @EJB
    private DepositiManager depositiManager;
    @EJB
    private RapportiBancariSedeEntitaManager rapportiBancariSedeEntitaManager;

    @Override
    public void aggiornaTassi(byte[] byteArray) {
        valutaManager.aggiornaTassi(byteArray);
    }

    @Override
    public void cambiaSedePrincipaleAzienda(SedeAzienda nuovaSedeAziendaPrincipaleAzienda,
            TipoSedeEntita tipoSedeSostitutivaEntita) throws AnagraficaServiceException {
        aziendeManager.cambiaSedePrincipaleAzienda(nuovaSedeAziendaPrincipaleAzienda, tipoSedeSostitutivaEntita);
    }

    @Override
    public void cambiaSedePrincipaleEntita(SedeEntita sedeEntita, TipoSedeEntita tipoSedeEntita) {
        LOGGER.debug("--> Enter cambiaSedePrincipaleEntita");
        entitaManager.cambiaSedePrincipaleEntita(sedeEntita, tipoSedeEntita);
        LOGGER.debug("--> Exit cambiaSedePrincipaleEntita");
    }

    @Override
    @RolesAllowed("modificaDatiBancari")
    public void cancellaBanca(Banca banca) throws AnagraficaServiceException {
        bancheManager.cancellaBanca(banca);
    }

    @Override
    public void cancellaCambioValuta(CambioValuta cambioValuta) {
        valutaManager.cancellaCambioValuta(cambioValuta);
    }

    @Override
    public void cancellaCategoriaEntita(CategoriaEntita categoriaEntita) {
        anagraficheManager.cancellaCategoriaEntita(categoriaEntita);
    }

    @Override
    public void cancellaContattiPerEntita(Entita entita) {
        LOGGER.debug("--> Enter cancellaContattiPerEntita");
        contattiManager.cancellaContattiPerEntita(entita);
        LOGGER.debug("--> Exit cancellaContattiPerEntita");
    }

    @Override
    public void cancellaContattiPerSedeEntita(SedeEntita sedeEntita) {
        contattiManager.cancellaContattiPerSedeEntita(sedeEntita);
    }

    @Override
    public void cancellaContatto(Contatto contatto) {
        contattiManager.cancellaContatto(contatto);
    }

    @Override
    public void cancellaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita)
            throws AnagraficaServiceException, ContattoOrphanException {
        contattiManager.cancellaContattoSedeEntita(contattoSedeEntita);
    }

    @Override
    public void cancellaDeposito(Deposito deposito) {
        try {
            depositiManager.cancellaDeposito(deposito);
        } catch (DAOException e) {
            LOGGER.error("-->errore nel cancellare il deposito " + deposito.getId(), e);
            throw new GenericException("-->errore nel cancellare il deposito " + deposito.getId(), e);
        }
    }

    @Override
    public void cancellaEntita(Entita entita) throws AnagraficaServiceException, ObjectNotFoundException {
        LOGGER.debug("--> Enter cancellaEntita");
        entitaManager.cancellaEntita(entita);
        LOGGER.debug("--> Exit cancellaEntita");
    }

    @Override
    @RolesAllowed("modificaDatiBancari")
    public void cancellaFiliale(Filiale filiale) throws AnagraficaServiceException {
        bancheManager.cancellaFiliale(filiale);
    }

    @Override
    public void cancellaRapportoBancario(RapportoBancarioAzienda rapportoBancario) throws AnagraficaServiceException {
        aziendeManager.cancellaRapportoBancario(rapportoBancario);
    }

    @Override
    public void cancellaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancario)
            throws AnagraficaServiceException {
        rapportiBancariSedeEntitaManager.cancellaRapportoBancarioSedeEntita(rapportoBancario);
    }

    @Override
    public void cancellaSedeAzienda(SedeAzienda sedeAzienda)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException {
        sediAziendaManager.cancellaSedeAzienda(sedeAzienda);
    }

    @Override
    public void cancellaSedeAzienda(SedeAzienda sedeAzienda, boolean deleteOrphan)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException {
        sediAziendaManager.cancellaSedeAzienda(sedeAzienda, deleteOrphan);
    }

    @Override
    public void cancellaSedeEntita(SedeEntita sedeEntita, boolean deleteOrphan)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException {
        LOGGER.debug("--> Enter cancellaSedeEntita");
        sediEntitaManager.cancellaSedeEntita(sedeEntita, deleteOrphan);
        LOGGER.debug("--> Exit cancellaSedeEntita");
    }

    @Override
    public void cancellaValutaAzienda(ValutaAzienda valutaAzienda) {
        valutaManager.cancellaValutaAzienda(valutaAzienda);
    }

    @Override
    public List<Agente> caricaAgentiSenzaCapoArea() {
        return entitaManager.caricaAgentiSenzaCapoArea();
    }

    @Override
    public Anagrafica caricaAnagrafica(Integer idAnagrafica) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaAnagrafica");
        Anagrafica anagrafica = anagraficheManager.caricaAnagrafica(idAnagrafica);
        LOGGER.debug("--> Exit caricaAnagrafica");
        return anagrafica;
    }

    @Override
    public List<RubricaDTO> caricaAnagraficheFull() {
        return anagraficheManager.caricaAnagraficheFull();
    }

    @Override
    public List<AnagraficaLite> caricaAnagraficheSearchObject(String codice, String denominazione) {
        return anagraficheManager.caricaAnagraficheSearchObject(codice, denominazione);
    }

    @Override
    public AziendaLite caricaAzienda() throws AnagraficaServiceException {
        return caricaAzienda(getAzienda(), true);
    }

    @Override
    public AziendaLite caricaAzienda(Map<Object, Object> parametri) {
        String codiceAzienda = (String) parametri.get("azienda");
        try {
            return caricaAzienda(codiceAzienda, true);
        } catch (AnagraficaServiceException e) {
            LOGGER.error("--> errore nel recuperare l'azienda tramite una mappa di parametri", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AziendaLite caricaAzienda(String codiceAzienda) throws AnagraficaServiceException {
        return aziendeManager.caricaAzienda(codiceAzienda);
    }

    @Override
    public AziendaLite caricaAzienda(String codiceAzienda, boolean loadSede) throws AnagraficaServiceException {
        return aziendeManager.caricaAzienda(codiceAzienda, loadSede);
    }

    @Override
    public AziendaAnagraficaDTO caricaAziendaAnagrafica(String codice) throws AnagraficaServiceException {
        return aziendeManager.caricaAziendaAnagrafica(codice);
    }

    @Override
    public AziendaAnagraficaDTO caricaAziendaAnagraficaCorrente() throws AnagraficaServiceException {
        return aziendeManager.caricaAziendaAnagrafica(getAzienda());
    }

    @Override
    public Azienda caricaAziendaByCodice(String codice) throws AnagraficaServiceException {
        return aziendeManager.caricaAziendaByCodice(codice);
    }

    @Override
    public List<AziendaLite> caricaAziende() throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaAziende");
        List<AziendaLite> aziende = aziendeManager.caricaAziende();
        LOGGER.debug("--> Exit caricaAziende");
        return aziende;
    }

    @Override
    public Banca caricaBanca(Integer idBanca) throws AnagraficaServiceException {
        return bancheManager.caricaBanca(idBanca);
    }

    @Override
    public List<Banca> caricaBanche(String fieldSearch, String valueSearch) throws AnagraficaServiceException {
        return bancheManager.caricaBanche(fieldSearch, valueSearch);
    }

    @Override
    public CambioValuta caricaCambioValuta(String codiceValuta, Date date) throws CambioNonPresenteException {
        return valutaManager.caricaCambioValuta(codiceValuta, date);
    }

    @Override
    public List<CambioValuta> caricaCambiValute(Date date) {
        return valutaManager.caricaCambiValute(date);
    }

    @Override
    public List<CambioValuta> caricaCambiValute(String codiceValuta, int anno) {
        return valutaManager.caricaCambiValute(codiceValuta, anno);
    }

    @Override
    public List<CategoriaEntita> caricaCategoriaEntitaByEntita(Integer idEntita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaCategoriaEntitaByEntita");
        List<CategoriaEntita> categorie = anagraficheManager.caricaCategoriaEntitaByEntita(idEntita);
        LOGGER.debug("--> Exit caricaCategoriaEntitaByEntita");
        return categorie;
    }

    @Override
    public List<CategoriaEntita> caricaCategorieEntita(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaCategoriaEntita");
        List<CategoriaEntita> listCategoria = anagraficheManager.caricaCategorieEntita(fieldSearch, valueSearch);
        LOGGER.debug("--> Exit caricaCategoriaEntita");
        return listCategoria;
    }

    @Override
    public List<CausaleRitenutaAcconto> caricaCausaliRitenuteAcconto() {
        return ritenutaAccontoManager.caricaCausaliRitenuteAcconto(null);
    }

    @Override
    public List<IClasseTipoDocumento> caricaClassiTipoDocumento() {
        return classeTipoDocumentoManager.caricaClassiTipoDocumento();
    }

    @Override
    public List<String> caricaCodiciValuteAzienda() {
        return valutaManager.caricaCodiciValuteAzienda();
    }

    @Override
    public List<Contatto> caricaContattiPerEntita(Entita entita) {
        return contattiManager.caricaContattiPerEntita(entita);
    }

    @Override
    public List<ContattoSedeEntita> caricaContattiSedeEntitaPerEntita(Entita entita) throws AnagraficaServiceException {
        return contattiManager.caricaContattiSedeEntitaPerEntita(entita);
    }

    @Override
    public List<ContattoSedeEntita> caricaContattiSedeEntitaPerSedeEntita(SedeEntita sedeEntita)
            throws AnagraficaServiceException {
        return contattiManager.caricaContattiSedeEntitaPerSedeEntita(sedeEntita);
    }

    @Override
    public Contatto caricaContatto(Integer idContatto) throws AnagraficaServiceException {
        return contattiManager.caricaContatto(idContatto);
    }

    @Override
    public ContattoSedeEntita caricaContattoSedeEntita(Integer idMansioneSede) {
        return contattiManager.caricaContattoSedeEntita(idMansioneSede);
    }

    @Override
    public List<ContributoPrevidenziale> caricaContributiEnasarco() {
        return ritenutaAccontoManager.caricaContributiEnasarco();
    }

    @Override
    public List<ContributoPrevidenziale> caricaContributiINPS() {
        return ritenutaAccontoManager.caricaContributiINPS();
    }

    @Override
    public List<Deposito> caricaDepositi() throws AnagraficaServiceException {
        return depositiManager.caricaDepositi();
    }

    @Override
    public List<Deposito> caricaDepositi(EntitaLite entita) {
        return depositiManager.caricaDepositi(entita);
    }

    @Override
    public List<Deposito> caricaDepositi(SedeAzienda sedeAzienda) throws AnagraficaServiceException {
        return depositiManager.caricaDepositi(sedeAzienda);
    }

    @Override
    public List<Deposito> caricaDepositi(String fieldSearch, String valueSearch) {
        return depositiManager.caricaDepositi(fieldSearch, valueSearch);
    }

    @Override
    public List<DepositoLite> caricaDepositiAzienda(String fieldSearch, String valueSearch,
            boolean soloMezziTrasporto) {
        return depositiManager.caricaDepositiAzienda(fieldSearch, valueSearch, soloMezziTrasporto);
    }

    @Override
    public Deposito caricaDeposito(Integer idDeposito) throws AnagraficaServiceException {
        return depositiManager.caricaDeposito(idDeposito);
    }

    @Override
    public Deposito caricaDepositoPrincipale() {
        return depositiManager.caricaDepositoPrincipale();

    }

    @Override
    public List<EntitaLite> caricaEntita(AnagraficaLite anagraficaLite) {
        return entitaManager.caricaEntita(anagraficaLite);
    }

    @Override
    public Entita caricaEntita(Entita entita, Boolean caricaLazy) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaEntita");
        Entita entitaLoad = entitaManager.caricaEntita(entita, caricaLazy);
        LOGGER.debug("--> Exit caricaEntita");
        return entitaLoad;
    }

    @Override
    public Entita caricaEntita(EntitaLite entitaLite, Boolean caricaLazy) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaEntita");
        Entita entita = entitaManager.caricaEntita(entitaLite, caricaLazy);
        LOGGER.debug("--> Exit caricaEntita");
        return entita;
    }

    @Override
    public List<SedeEntita> caricaEntitaByCategorie(List<CategoriaEntita> categorie) {
        return anagraficheManager.caricaEntitaByCategorie(categorie);
    }

    @Override
    public EntitaLite caricaEntitaLite(EntitaLite entitaLite) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaEntita");
        EntitaLite entitaLoad = entitaManager.caricaEntitaLite(entitaLite);
        LOGGER.debug("--> Exit caricaEntita");
        return entitaLoad;
    }

    @Override
    public Filiale caricaFiliale(Integer idFiliale) throws AnagraficaServiceException {
        return bancheManager.caricaFiliale(idFiliale);
    }

    @Override
    public List<Filiale> caricaFiliali() throws AnagraficaServiceException {
        return bancheManager.caricaFiliali();
    }

    @Override
    public List<Filiale> caricaFiliali(Banca banca, String fieldSearch, String valueSearch)
            throws AnagraficaServiceException {
        return bancheManager.caricaFiliali(banca, fieldSearch, valueSearch);
    }

    @Override
    public List<Prestazione> caricaPrestazioni() {
        return ritenutaAccontoManager.caricaPrestazioni();
    }

    @Override
    public List<RapportoBancarioAzienda> caricaRapportiBancariAzienda(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException {
        return aziendeManager.caricaRapportiBancariAzienda(fieldSearch, valueSearch);
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaRapportiBancariSedeEntita");
        List<RapportoBancarioSedeEntita> rapporti = rapportiBancariSedeEntitaManager
                .caricaRapportiBancariSedeEntita(idSedeEntita);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit caricaRapportiBancariSedeEntita con risultato " + rapporti);
        }
        return rapporti;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita, boolean ignoraEredita)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaRapportiBancariSedeEntita");
        List<RapportoBancarioSedeEntita> rapporti = rapportiBancariSedeEntitaManager
                .caricaRapportiBancariSedeEntita(idSedeEntita, ignoraEredita);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit caricaRapportiBancariSedeEntita con risultato " + rapporti);
        }
        return rapporti;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntitaPrincipale(String fieldSearch,
            String valueSearch, Integer idEntita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaRapportiBancariSedeEntitaPrincipale");
        List<RapportoBancarioSedeEntita> rapporti = rapportiBancariSedeEntitaManager
                .caricaRapportiBancariSedeEntitaPricipale(fieldSearch, valueSearch, idEntita);
        LOGGER.debug("--> Exit caricaRapportiBancariSedeEntitaPrincipale");
        return rapporti;
    }

    @Override
    public RapportoBancarioAzienda caricaRapportoBancario(Integer idRapportoBancario, boolean initializeLazy) {
        return aziendeManager.caricaRapportoBancario(idRapportoBancario, initializeLazy);
    }

    @Override
    public RapportoBancarioSedeEntita caricaRapportoBancarioSedeEntita(Integer idRapportoBancario)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaRapportoBancarioSedeEntita");
        RapportoBancarioSedeEntita rapportoBancarioSedeEntita = rapportiBancariSedeEntitaManager
                .caricaRapportoBancarioSedeEntita(idRapportoBancario);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit caricaRapportoBancarioSedeEntita con risultato " + rapportoBancarioSedeEntita);
        }
        return rapportoBancarioSedeEntita;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRiepilogoDatiBancari() {
        return rapportiBancariSedeEntitaManager.caricaRiepilogoDatiBancari();
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntita() {
        return sediEntitaManager.caricaRiepilogoSediEntita();
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByAgente(AgenteLite agente) {
        return sediEntitaManager.caricaRiepilogoSediEntitaByAgente(agente);
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByVettore(VettoreLite vettore) {
        return sediEntitaManager.caricaRiepilogoSediEntitaByVettore(vettore);
    }

    @Override
    public SedeAzienda caricaSedeAzienda(Integer idSedeAzienda) throws AnagraficaServiceException {
        return sediAziendaManager.caricaSedeAzienda(idSedeAzienda);
    }

    @Override
    public SedeEntita caricaSedeEntita(Integer idSedeEntita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSedeEntita");
        SedeEntita sedeEntita = sediEntitaManager.caricaSedeEntita(idSedeEntita);
        LOGGER.debug("--> Exit caricaSedeEntita");
        return sedeEntita;
    }

    @Override
    public SedeEntita caricaSedeEntita(Integer idSedeEntita, boolean caricaLazy) throws AnagraficaServiceException {
        return sediEntitaManager.caricaSedeEntita(idSedeEntita, caricaLazy);
    }

    @Override
    public SedeEntita caricaSedePredefinitaEntita(Entita entita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSedePredefinitaEntita");
        SedeEntita sedeEntita = sediEntitaManager.caricaSedePredefinitaEntita(entita);
        LOGGER.debug("--> Exit caricaSedePrincipaleEntita");
        return sedeEntita;
    }

    @Override
    public SedeAzienda caricaSedePrincipaleAzienda(Azienda azienda) throws AnagraficaServiceException {
        return sediAziendaManager.caricaSedePrincipaleAzienda(azienda);
    }

    @Override
    public SedeEntita caricaSedePrincipaleEntita(Entita entita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSedePrincipaleEntita");
        SedeEntita sedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(entita);
        LOGGER.debug("--> Exit caricaSedePrincipaleEntita");
        return sedeEntita;
    }

    @Override
    public SedeEntita caricaSedePrincipaleEntita(Integer idEntita) throws AnagraficaServiceException {
        return sediEntitaManager.caricaSedePrincipaleEntita(idEntita);
    }

    @Override
    public List<SedeAnagrafica> caricaSediAnagrafica(Anagrafica anagrafica) throws AnagraficaServiceException {
        return anagraficheManager.caricaSediAnagrafica(anagrafica);
    }

    @Override
    public List<SedeAnagrafica> caricaSediAnagraficaAzienda(Azienda azienda) throws AnagraficaServiceException {
        return sediAziendaManager.caricaSediAnagraficaAzienda(azienda);
    }

    @Override
    public Set<SedeEntita> caricaSediAssociate(AgenteLite agente) {
        return sediAgenteManager.caricaSediAssociate(agente);
    }

    @Override
    public List<SedeAzienda> caricaSediAzienda() throws AnagraficaServiceException {
        return sediAziendaManager.caricaSediAzienda();
    }

    @Override
    public List<SedeAzienda> caricaSediAzienda(Azienda azienda) throws AnagraficaServiceException {
        return sediAziendaManager.caricaSediAzienda(azienda);
    }

    @Override
    public List<SedeEntitaLite> caricaSediEntita(Integer idEntita) {
        return sediEntitaManager.caricaSediEntita(idEntita);
    }

    @Override
    public List<SedeEntita> caricaSediEntita(String filtro, Integer idEntita,
            CaricamentoSediEntita caricamentoSediEntita, boolean caricaSedeDisabilitate)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Exit caricaSediEntita");

        List<SedeEntita> list = sediEntitaManager.caricaSediEntita(filtro, idEntita, caricamentoSediEntita,
                caricaSedeDisabilitate);

        LOGGER.debug("--> Exit caricaSediEntita");
        return list;
    }

    @Override
    public Set<SedeEntita> caricaSediEntitaAssociate(VettoreLite vettore) {
        return sediEntitaManager.caricaSediEntitaAssociate(vettore);
    }

    @Override
    public List<SedeEntita> caricaSediEntitaNonEreditaDatiComerciali(Integer idEntita)
            throws AnagraficaServiceException {
        LOGGER.debug("--> Exit caricaSediEntitaNonEreditaDatiComerciali");
        List<SedeEntita> list = sediEntitaManager.caricaSediEntitaNonEreditaDatiComerciali(idEntita);
        LOGGER.debug("--> Exit caricaSediEntitaNonEreditaDatiComerciali");
        return list;
    }

    @Override
    public List<SedeAzienda> caricaSediSecondarieAzienda(Azienda azienda) throws AnagraficaServiceException {
        return sediAziendaManager.caricaSediSecondarieAzienda(azienda);
    }

    @Override
    public List<SedeEntita> caricaSediSecondarieEntita(Entita entita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter caricaSediSecondarieEntita");
        List<SedeEntita> list = sediEntitaManager.caricaSediSecondarieEntita(entita);
        LOGGER.debug("--> Exit caricaSediSecondarieEntita");
        return list;
    }

    @Override
    public ValutaAzienda caricaValutaAzienda(String codiceValuta) {
        return valutaManager.caricaValutaAzienda(codiceValuta);
    }

    @Override
    public ValutaAzienda caricaValutaAziendaCorrente() {
        return valutaManager.caricaValutaAziendaCorrente();
    }

    @Override
    public List<ValutaAzienda> caricaValuteAzienda() {
        return valutaManager.caricaValuteAzienda();
    }

    @Override
    public Entita confermaClientePotenziale(Integer idEntita) {
        return entitaManager.confermaClientePotenziale(idEntita);
    }

    @Override
    public SedeEntita creaSedeEntitaGenerica(Integer idEntita) throws TipoSedeEntitaNonTrovataException {
        return sediEntitaManager.creaSedeEntitaGenerica(idEntita);
    }

    @Override
    public Map<String, String> creaVariabiliCodice(EntityBase entity) {
        return variabiliCodiceManager.creaMapVariabili(entity);
    }

    @Override
    public String generaCodice(String pattern, Map<String, String> mapVariabili) {
        return codicePatternManager.genera(pattern, mapVariabili);
    }

    /**
     * Recupera il codice azienda.
     *
     * @return String
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public String[] getVariabiliPatternCodiceDocumento() {
        return variabiliCodiceManager.getVariabili();
    }

    @Override
    public List<Contatto> ricercaContatti(Map<String, Object> parametri) {
        return contattiManager.ricercaContatti(parametri);
    }

    @Override
    public List<Contatto> ricercaContattiPerEntita(Entita entita, Map<String, Object> parametri) {
        return contattiManager.ricercaContattiPerEntita(entita, parametri);
    }

    @Override
    public List<Deposito> ricercaDepositi(ParametriRicercaDepositi parametri) {
        return depositiManager.ricercaDepositi(parametri);
    }

    @Override
    public List<EntitaLite> ricercaEntita(ParametriRicercaEntita parametriRicercaEntita) {
        LOGGER.debug("--> Enter ricercaEntita");
        List<EntitaLite> listEntita = entitaManager.ricercaEntita(parametriRicercaEntita);
        LOGGER.debug("--> Exit ricercaEntita");
        return listEntita;
    }

    @Override
    public List<EntitaLite> ricercaEntita(String codiceFiscale, String partitaIva) {
        return entitaManager.ricercaEntita(codiceFiscale, partitaIva);
    }

    @Override
    public List<EntitaLite> ricercaEntitaSearchObject(ParametriRicercaEntita parametriRicercaEntita) {
        return entitaManager.ricercaEntitaSearchObject(parametriRicercaEntita);
    }

    @Override
    public Azienda salvaAzienda(Azienda azienda) throws AnagraficaServiceException {
        return aziendeManager.salvaAzienda(azienda);
    }

    @Override
    public AziendaAnagraficaDTO salvaAziendaAnagrafica(AziendaAnagraficaDTO aziendaAnagraficaDTO)
            throws AnagraficaServiceException, DuplicateKeyObjectException, SedeEntitaPrincipaleAlreadyExistException,
            StaleObjectStateException {
        return aziendeManager.salvaAziendaAnagrafica(aziendaAnagraficaDTO);
    }

    @Override
    @RolesAllowed("modificaDatiBancari")
    public Banca salvaBanca(Banca banca) {
        return bancheManager.salvaBanca(banca);
    }

    @Override
    @RolesAllowed("modificaDatiBancari")
    public CambioValuta salvaCambioValuta(CambioValuta cambioValuta) {
        return valutaManager.salvaCambioValuta(cambioValuta);
    }

    @Override
    public CategoriaEntita salvaCategoriaEntita(CategoriaEntita categoriaEntita) {
        return anagraficheManager.salvaCategoriaEntita(categoriaEntita);
    }

    @Override
    public Contatto salvaContatto(Contatto contatto) {
        return contattiManager.salvaContatto(contatto);
    }

    @Override
    public ContattoSedeEntita salvaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita) {
        return contattiManager.salvaContattoSedeEntita(contattoSedeEntita);
    }

    @Override
    public Deposito salvaDeposito(Deposito deposito) {
        return depositiManager.salvaDeposito(deposito);
    }

    @Override
    public Entita salvaEntita(Entita entita) throws AnagraficaServiceException, AnagraficheDuplicateException {
        LOGGER.debug("--> Enter salvaEntita");
        Entita entitaSave = entitaManager.salvaEntita(entita);
        LOGGER.debug("--> Exit salvaEntita");
        return entitaSave;
    }

    @Override
    @RolesAllowed("modificaDatiBancari")
    public Filiale salvaFiliale(Filiale filiale) {
        return bancheManager.salvaFiliale(filiale);
    }

    @Override
    public RapportoBancarioAzienda salvaRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda)
            throws AnagraficaServiceException {
        return aziendeManager.salvaRapportoBancarioAzienda(rapportoBancarioAzienda);
    }

    @Override
    public RapportoBancarioSedeEntita salvaRapportoBancarioSedeEntita(
            RapportoBancarioSedeEntita rapportoBancarioSedeEntita) throws AnagraficaServiceException {
        LOGGER.debug("--> Enter salvaRapportoBancarioSedeEntita");
        rapportoBancarioSedeEntita = rapportiBancariSedeEntitaManager
                .salvaRapportoBancarioSedeEntita(rapportoBancarioSedeEntita);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit salvaRapportoBancarioSedeEntita con result " + rapportoBancarioSedeEntita);
        }
        return rapportoBancarioSedeEntita;
    }

    @Override
    public SedeAzienda salvaSedeAzienda(SedeAzienda sedeAzienda)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException {
        return sediAziendaManager.salvaSedeAzienda(sedeAzienda);
    }

    @Override
    public SedeEntita salvaSedeEntita(SedeEntita sedeEntita)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException {
        LOGGER.debug("--> Enter salvaSedeEntita");
        SedeEntita sedeEntitaSave = sediEntitaManager.salvaSedeEntita(sedeEntita);
        LOGGER.debug("--> Exit salvaSedeEntita");
        return sedeEntitaSave;
    }

    @Override
    public ValutaAzienda salvaValutaAzienda(ValutaAzienda valutaAzienda) {
        return valutaManager.salvaValutaAzienda(valutaAzienda);
    }

    @Override
    public void sostituisciDatiBancari(List<RapportoBancarioSedeEntita> rapporti, Banca banca, Filiale filiale) {
        rapportiBancariSedeEntitaManager.sostituisciDatiBancari(rapporti, banca, filiale);
    }

    @Override
    public List<?> test() {
        return anagraficheManager.test();
    }
}
