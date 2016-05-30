package it.eurotn.panjea.anagrafica.rich.bd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
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
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.manager.depositi.ParametriRicercaDepositi;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * Implementazione del Business Delegate di {@link AnagraficaService}.
 *
 * @author adriano
 * @version 1.0, 18/dic/07
 *
 */
public class AnagraficaBD extends AbstractBaseBD implements IAnagraficaBD {

    private static final Logger LOGGER = Logger.getLogger(AnagraficaBD.class);

    /**
     *
     * @return {@link AnagraficaBD}
     */
    public static String getBeanId() {
        return "anagraficaBD";
    }

    private AnagraficaService anagraficaService;

    /**
     * Costruttore.
     */

    public AnagraficaBD() {
        super();
    }

    @Override
    public void cambiaSedePrincipaleAzienda(SedeAzienda nuovaSedeAziendaPrincipaleAzienda,
            TipoSedeEntita tipoSedeSostitutivaEntita) {
        LOGGER.debug("--> Enter cambiaSedePrincipaleAzienda");
        start("cambiaSedePrincipaleAzienda");
        try {
            anagraficaService.cambiaSedePrincipaleAzienda(nuovaSedeAziendaPrincipaleAzienda, tipoSedeSostitutivaEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaSedePrincipaleAzienda");
        }
        LOGGER.debug("--> Exit cambiaSedePrincipaleAzienda ");

    }

    @Override
    public void cambiaSedePrincipaleEntita(SedeEntita sedeEntita, TipoSedeEntita tipoSedeEntita) {
        LOGGER.debug("--> Enter cambiaSedePrincipaleEntita");
        start("cambiaSedePrincipaleEntita");
        try {
            anagraficaService.cambiaSedePrincipaleEntita(sedeEntita, tipoSedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaSedePrincipaleEntita");
        }
        LOGGER.debug("--> Exit cambiaSedePrincipaleEntita ");

    }

    @Override
    public void cancellaBanca(Banca banca) {
        LOGGER.debug("--> Enter cancellaBanca");
        start("cancellaBanca");
        try {
            anagraficaService.cancellaBanca(banca);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaBanca");
        }
        LOGGER.debug("--> Exit cancellaBanca ");

    }

    @Override
    public void cancellaCategoriaEntita(CategoriaEntita categoriaEntita) {
        LOGGER.debug("--> Enter cancellaCategoriaEntita");
        start("cancellaCategoriaEntita");
        try {
            anagraficaService.cancellaCategoriaEntita(categoriaEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaCategoriaEntita");
        }
        LOGGER.debug("--> Exit cancellaCategoriaEntita ");

    }

    @Override
    public void cancellaContattiPerEntita(Entita entita) {
        LOGGER.debug("--> Enter cancellaContattiPerEntita");
        start("cancellaContattiPerEntita");
        try {
            anagraficaService.cancellaContattiPerEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaContattiPerEntita");
        }
        LOGGER.debug("--> Exit cancellaContattiPerEntita ");

    }

    @Override
    public void cancellaContattiPerSedeEntita(SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter cancellaContattiPerSedeEntita");
        start("cancellaContattiPerSedeEntita");
        try {
            anagraficaService.cancellaContattiPerSedeEntita(sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaContattiPerSedeEntita");
        }
        LOGGER.debug("--> Exit cancellaContattiPerSedeEntita ");

    }

    @Override
    public void cancellaContatto(Contatto contatto) {
        LOGGER.debug("--> Enter cancellaContatto");
        start("cancellaContatto");
        try {
            anagraficaService.cancellaContatto(contatto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaContatto");
        }
        LOGGER.debug("--> Exit cancellaContatto ");

    }

    @Override
    public void cancellaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita) {
        LOGGER.debug("--> Enter cancellaContattoSedeEntita");
        start("cancellaContattoSedeEntita");
        try {
            anagraficaService.cancellaContattoSedeEntita(contattoSedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaContattoSedeEntita");
        }
        LOGGER.debug("--> Exit cancellaContattoSedeEntita ");

    }

    @Override
    public void cancellaDeposito(Deposito deposito) {
        LOGGER.debug("--> Enter cancellaDeposito");
        start("cancellaDeposito");
        try {
            anagraficaService.cancellaDeposito(deposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaDeposito");
        }
        LOGGER.debug("--> Exit cancellaDeposito ");

    }

    @Override
    public void cancellaEntita(Entita entita) {
        LOGGER.debug("--> Enter cancellaEntita");
        start("cancellaEntita");
        try {
            anagraficaService.cancellaEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaEntita");
        }
        LOGGER.debug("--> Exit cancellaEntita ");

    }

    @Override
    public void cancellaFiliale(Filiale filiale) {
        LOGGER.debug("--> Enter cancellaFiliale");
        start("cancellaFiliale");
        try {
            anagraficaService.cancellaFiliale(filiale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaFiliale");
        }
        LOGGER.debug("--> Exit cancellaFiliale ");

    }

    @Override
    public void cancellaRapportoBancario(RapportoBancarioAzienda rapportoBancario) {
        LOGGER.debug("--> Enter cancellaRapportoBancario");
        start("cancellaRapportoBancario");
        try {
            anagraficaService.cancellaRapportoBancario(rapportoBancario);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRapportoBancario");
        }
        LOGGER.debug("--> Exit cancellaRapportoBancario ");

    }

    @Override
    public void cancellaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancario) {
        LOGGER.debug("--> Enter cancellaRapportoBancarioSedeEntita");
        start("cancellaRapportoBancarioSedeEntita");
        try {
            anagraficaService.cancellaRapportoBancarioSedeEntita(rapportoBancario);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaRapportoBancarioSedeEntita");
        }
        LOGGER.debug("--> Exit cancellaRapportoBancarioSedeEntita ");

    }

    @Override
    public void cancellaSedeAzienda(SedeAzienda sedeAzienda) throws SedeAnagraficaOrphanException {
        LOGGER.debug("--> Enter cancellaSedeAzienda");
        start("cancellaSedeAzienda");
        try {
            anagraficaService.cancellaSedeAzienda(sedeAzienda);
        } catch (SedeAnagraficaOrphanException e) {
            throw e;
        } catch (Exception e1) {
            PanjeaSwingUtil.checkAndThrowException(e1);
        } finally {

            end("cancellaSedeAzienda");
        }
        LOGGER.debug("--> Exit cancellaSedeAzienda ");

    }

    @Override
    public void cancellaSedeAzienda(SedeAzienda sedeAzienda, boolean deleteOrphan) {
        LOGGER.debug("--> Enter cancellaSedeAzienda");
        start("cancellaSedeAzienda");
        try {
            anagraficaService.cancellaSedeAzienda(sedeAzienda, deleteOrphan);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaSedeAzienda");
        }
        LOGGER.debug("--> Exit cancellaSedeAzienda ");

    }

    @Override
    public void cancellaSedeEntita(SedeEntita sedeEntita, boolean deleteOrphan) throws SedeAnagraficaOrphanException {
        LOGGER.debug("--> Enter cancellaSedeEntita");
        start("cancellaSedeEntita");
        try {
            anagraficaService.cancellaSedeEntita(sedeEntita, deleteOrphan);
        } catch (SedeAnagraficaOrphanException e) {
            LOGGER.error("--> Errore sede anagrafica rimasta orfana", e);
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaSedeEntita");
        }
        LOGGER.debug("--> Exit cancellaSedeEntita ");

    }

    @Override
    public List<Agente> caricaAgentiSenzaCapoArea() {
        LOGGER.debug("--> Enter caricaAgentiSenzaCapoArea");
        start("caricaAgentiSenzaCapoArea");
        List<Agente> agenti = null;
        try {
            agenti = anagraficaService.caricaAgentiSenzaCapoArea();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAgentiSenzaCapoArea");
        }
        LOGGER.debug("--> Exit caricaAgentiSenzaCapoArea ");
        return agenti;
    }

    @Override
    public Anagrafica caricaAnagrafica(Integer idAnagrafica) {
        LOGGER.debug("--> Enter caricaAnagrafica");
        start("caricaAnagrafica");
        Anagrafica anagrafica = null;
        try {
            anagrafica = anagraficaService.caricaAnagrafica(idAnagrafica);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAnagrafica");
        }
        LOGGER.debug("--> Exit caricaAnagrafica ");
        return anagrafica;
    }

    @Override
    public List<RubricaDTO> caricaAnagraficheFull() {
        LOGGER.debug("--> Enter caricaAnagraficheFull");
        start("caricaAnagraficheFull");
        List<RubricaDTO> result = null;
        try {
            result = anagraficaService.caricaAnagraficheFull();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAnagraficheFull");
        }
        LOGGER.debug("--> Exit caricaAnagraficheFull ");
        return result;
    }

    @Override
    public List<AnagraficaLite> caricaAnagraficheSearchObject(String codice, String denominazione) {
        LOGGER.debug("--> Enter caricaAnagrafiche");
        start("caricaAnagrafiche");
        List<AnagraficaLite> anagrafiche = new ArrayList<>();
        try {
            anagrafiche = anagraficaService.caricaAnagraficheSearchObject(codice, denominazione);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAnagrafiche");
        }
        LOGGER.debug("--> Exit caricaAnagrafiche ");
        return anagrafiche;
    }

    @Override
    public AziendaLite caricaAzienda() {
        LOGGER.debug("--> Enter caricaAziendaCorrente");
        start("caricaAziendaCorrente");
        AziendaLite aziendaLite = null;
        try {
            aziendaLite = anagraficaService.caricaAzienda();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAziendaCorrente");
        }
        LOGGER.debug("--> Exit caricaAziendaCorrente ");
        return aziendaLite;
    }

    @Override
    public AziendaLite caricaAzienda(String codiceAzienda) {
        LOGGER.debug("--> Enter caricaAzienda");
        start("caricaAzienda");
        AziendaLite aziendaLite = null;
        try {
            aziendaLite = anagraficaService.caricaAzienda(codiceAzienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAzienda");
        }
        LOGGER.debug("--> Exit caricaAzienda ");
        return aziendaLite;
    }

    @Override
    public AziendaAnagraficaDTO caricaAziendaAnagrafica(String codice) {
        LOGGER.debug("--> Enter caricaAziendaAnagrafica");
        start("caricaAziendaAnagrafica");
        AziendaAnagraficaDTO aziendaAnagraficaDTO = null;
        try {
            aziendaAnagraficaDTO = anagraficaService.caricaAziendaAnagrafica(codice);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAziendaAnagrafica");
        }
        LOGGER.debug("--> Exit caricaAziendaAnagrafica ");
        return aziendaAnagraficaDTO;
    }

    @Override
    public Azienda caricaAziendaByCodice(String codice) {
        LOGGER.debug("--> Enter caricaAziendaByCodice");
        start("caricaAziendaByCodice");
        Azienda azienda = null;
        try {
            azienda = anagraficaService.caricaAziendaByCodice(codice);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAziendaByCodice");
        }
        LOGGER.debug("--> Exit caricaAziendaByCodice ");
        return azienda;
    }

    @Override
    public List<AziendaLite> caricaAziende() {
        LOGGER.debug("--> Enter caricaAziende");
        start("caricaAziende");
        List<AziendaLite> aziendaLista = null;
        try {
            aziendaLista = anagraficaService.caricaAziende();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaAziende");
        }
        LOGGER.debug("--> Exit caricaAziende ");
        return aziendaLista;
    }

    @Override
    public Banca caricaBanca(Integer idBanca) {
        LOGGER.debug("--> Enter caricaBanca");
        start("caricaBanca");
        Banca banca = null;
        try {
            banca = anagraficaService.caricaBanca(idBanca);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaBanca");
        }
        LOGGER.debug("--> Exit caricaBanca ");
        return banca;
    }

    @Override
    public List<Banca> caricaBanche(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaBanche");
        start("caricaBanche");
        List<Banca> banche = null;
        try {
            banche = anagraficaService.caricaBanche(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaBanche");
        }
        LOGGER.debug("--> Exit caricaBanche ");
        return banche;
    }

    @Override
    public List<CategoriaEntita> caricaCategoriaEntitaByEntita(Integer idEntita) {
        LOGGER.debug("--> Enter caricaCategoriaEntitaByEntita");
        start("caricaCategoriaEntitaByEntita");
        List<CategoriaEntita> categorie = null;
        try {
            categorie = anagraficaService.caricaCategoriaEntitaByEntita(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategoriaEntitaByEntita");
        }
        LOGGER.debug("--> Exit caricaCategoriaEntitaByEntita ");
        return categorie;
    }

    @Override
    public List<CategoriaEntita> caricaCategorieEntita(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaCategoriaEntita");
        start("caricaCategoriaEntita");
        List<CategoriaEntita> listCategoria = null;
        try {
            listCategoria = anagraficaService.caricaCategorieEntita(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCategoriaEntita");
        }
        LOGGER.debug("--> Exit caricaCategoriaEntita ");
        return listCategoria;
    }

    @Override
    public List<CausaleRitenutaAcconto> caricaCausaliRitenuteAcconto() {
        LOGGER.debug("--> Enter caricaCausaliRitenuteAcconto");
        start("caricaCausaliRitenuteAcconto");

        List<CausaleRitenutaAcconto> result = null;
        try {
            result = anagraficaService.caricaCausaliRitenuteAcconto();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCausaliRitenuteAcconto");
        }
        LOGGER.debug("--> Exit caricaCausaliRitenuteAcconto ");
        return result;
    }

    @Override
    public List<IClasseTipoDocumento> caricaClassiTipoDocumento() {
        LOGGER.debug("--> Enter caricaClassiTipoDocumento");
        start("caricaClassiTipoDocumento");
        List<IClasseTipoDocumento> classiTipoDocumento = new ArrayList<>();
        try {
            classiTipoDocumento = anagraficaService.caricaClassiTipoDocumento();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaClassiTipoDocumento");
        }
        LOGGER.debug("--> Exit caricaClassiTipoDocumento ");
        return classiTipoDocumento;
    }

    @Override
    public List<String> caricaCodiciValuteAzienda() {
        LOGGER.debug("--> Enter caricaCodiciValuteAzienda");
        start("caricaCodiciValuteAzienda");
        List<String> list = new ArrayList<>();
        try {
            list = anagraficaService.caricaCodiciValuteAzienda();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaCodiciValuteAzienda");
        }
        LOGGER.debug("--> Exit caricaCodiciValuteAzienda ");
        return list;
    }

    @Override
    public List<Contatto> caricaContattiPerEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaContattiPerEntita");
        start("caricaContattiPerEntita");
        List<Contatto> contatti = null;
        try {
            contatti = anagraficaService.caricaContattiPerEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContattiPerEntita");
        }
        LOGGER.debug("--> Exit caricaContattiPerEntita ");
        return contatti;
    }

    @Override
    public List<ContattoSedeEntita> caricaContattiSedeEntitaPerEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaContattiSedeEntitaPerEntita");
        start("caricaContattiSedeEntitaPerEntita");
        List<ContattoSedeEntita> contatti = null;
        try {
            contatti = anagraficaService.caricaContattiSedeEntitaPerEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContattiSedeEntitaPerEntita");
        }
        LOGGER.debug("--> Exit caricaContattiSedeEntitaPerEntita ");
        return contatti;
    }

    @Override
    public List<ContattoSedeEntita> caricaContattiSedeEntitaPerSedeEntita(SedeEntita sedeEntita) {
        LOGGER.debug("--> Enter caricaContattiSedeEntitaPerSedeEntita");
        start("caricaContattiSedeEntitaPerSedeEntita");
        List<ContattoSedeEntita> contatti = null;
        try {
            contatti = anagraficaService.caricaContattiSedeEntitaPerSedeEntita(sedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContattiSedeEntitaPerSedeEntita");
        }
        LOGGER.debug("--> Exit caricaContattiSedeEntitaPerSedeEntita ");
        return contatti;
    }

    @Override
    public Contatto caricaContatto(Integer idContatto) {
        LOGGER.debug("--> Enter caricaContatto");
        start("caricaContatto");
        Contatto contatto = null;
        try {
            contatto = anagraficaService.caricaContatto(idContatto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContatto");
        }
        LOGGER.debug("--> Exit caricaContatto ");
        return contatto;
    }

    @Override
    public ContattoSedeEntita caricaContattoSedeEntita(Integer idMansioneSede) throws DAOException {
        LOGGER.debug("--> Enter caricaContattoSedeEntita");
        start("caricaContattoSedeEntita");
        ContattoSedeEntita contattoSedeEntita = null;
        try {
            contattoSedeEntita = anagraficaService.caricaContattoSedeEntita(idMansioneSede);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContattoSedeEntita");
        }
        LOGGER.debug("--> Exit caricaContattoSedeEntita ");
        return contattoSedeEntita;
    }

    @Override
    public List<ContributoPrevidenziale> caricaContributiEnasarco() {
        LOGGER.debug("--> Enter caricaContributiEnasarco");
        start("caricaContributiEnasarco");

        List<ContributoPrevidenziale> result = null;
        try {
            result = anagraficaService.caricaContributiEnasarco();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContributiEnasarco");
        }
        LOGGER.debug("--> Exit caricaContributiEnasarco ");
        return result;
    }

    @Override
    public List<ContributoPrevidenziale> caricaContributiINPS() {
        LOGGER.debug("--> Enter caricaContributiINPS");
        start("caricaContributiINPS");

        List<ContributoPrevidenziale> result = null;
        try {
            result = anagraficaService.caricaContributiINPS();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaContributiINPS");
        }
        LOGGER.debug("--> Exit caricaContributiINPS ");
        return result;
    }

    @Override
    public List<Deposito> caricaDepositi() {
        LOGGER.debug("--> Enter caricaDepositi");
        start("caricaDepositi");
        List<Deposito> depositi = null;
        try {
            depositi = anagraficaService.caricaDepositi();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDepositi");
        }
        LOGGER.debug("--> Exit caricaDepositi ");
        return depositi;
    }

    @Override
    public List<Deposito> caricaDepositi(EntitaLite entita) {
        LOGGER.debug("--> Enter caricaDepositi");
        start("caricaDepositi");
        List<Deposito> depositi = null;
        try {
            depositi = anagraficaService.caricaDepositi(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDepositi");
        }
        LOGGER.debug("--> Exit caricaDepositi ");
        return depositi;
    }

    @Override
    public List<Deposito> caricaDepositi(SedeAzienda sedeAzienda) {
        LOGGER.debug("--> Enter caricaDepositi");
        start("caricaDepositi");
        List<Deposito> depositi = null;
        try {
            depositi = anagraficaService.caricaDepositi(sedeAzienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDepositi");
        }
        LOGGER.debug("--> Exit caricaDepositi ");
        return depositi;
    }

    @Override
    public List<Deposito> caricaDepositi(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaDepositi");
        start("caricaDepositi");

        List<Deposito> depositi = null;
        try {
            depositi = anagraficaService.caricaDepositi(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDepositi");
        }
        LOGGER.debug("--> Exit caricaDepositi ");
        return depositi;
    }

    @Override
    public List<DepositoLite> caricaDepositiAzienda(String fieldSearch, String valueSearch,
            boolean soloConMezziTrasporto) {
        LOGGER.debug("--> Enter ricercaDepositi");
        start("ricercaDepositi");
        List<DepositoLite> depositi = null;
        try {
            depositi = anagraficaService.caricaDepositiAzienda(fieldSearch, valueSearch, soloConMezziTrasporto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaDepositi");
        }
        LOGGER.debug("--> Exit ricercaDepositi");
        return depositi;
    }

    @Override
    public Deposito caricaDeposito(Integer idDeposito) {
        LOGGER.debug("--> Enter caricaDeposito");
        start("caricaDeposito");
        Deposito deposito = null;
        try {
            deposito = anagraficaService.caricaDeposito(idDeposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDeposito");
        }
        LOGGER.debug("--> Exit caricaDeposito ");
        return deposito;
    }

    @Override
    public Deposito caricaDepositoPrincipale() {
        LOGGER.debug("--> Enter caricaDepositoPrincipale");
        start("caricaDepositoPrincipale");
        Deposito depositoPrincipale = null;
        try {
            depositoPrincipale = anagraficaService.caricaDepositoPrincipale();
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof ObjectNotFoundException) {
                return null;
            }
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDepositoPrincipale");
        }
        return depositoPrincipale;

    }

    @Override
    public List<EntitaLite> caricaEntita(AnagraficaLite anagraficaLite) {
        LOGGER.debug("--> Enter caricaEntita");
        start("caricaEntita");

        List<EntitaLite> entita = null;
        try {
            entita = anagraficaService.caricaEntita(anagraficaLite);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntita");
        }
        LOGGER.debug("--> Exit caricaEntita ");
        return entita;
    }

    @Override
    public Entita caricaEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaEntita");
        start("caricaEntita");
        Entita entitaLoad = null;
        try {
            entitaLoad = anagraficaService.caricaEntita(entita, false);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntita");
        }
        LOGGER.debug("--> Exit caricaEntita ");
        return entitaLoad;
    }

    @Override
    public Entita caricaEntita(EntitaLite entitaLite, boolean caricaLazy) {
        LOGGER.debug("--> Enter caricaEntita");
        start("caricaEntita");
        Entita entitaLoad = null;
        try {
            entitaLoad = anagraficaService.caricaEntita(entitaLite, caricaLazy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntita");
        }
        LOGGER.debug("--> Exit caricaEntita ");
        return entitaLoad;
    }

    @Override
    public List<SedeEntita> caricaEntitaByCategorie(List<CategoriaEntita> categorie) {
        LOGGER.debug("--> Enter caricaEntitaByCategorie");
        start("caricaEntitaByCategorie");
        List<SedeEntita> result = null;
        try {
            result = anagraficaService.caricaEntitaByCategorie(categorie);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntitaByCategorie");
        }
        LOGGER.debug("--> Exit caricaEntitaByCategorie ");
        return result;
    }

    @Override
    public EntitaLite caricaEntitaLite(EntitaLite entitaLite) {
        LOGGER.debug("--> Enter caricaEntita");
        start("caricaEntita");
        EntitaLite entitaLiteLoad = null;
        try {
            entitaLiteLoad = anagraficaService.caricaEntitaLite(entitaLite);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaEntita");
        }
        LOGGER.debug("--> Exit caricaEntita ");
        return entitaLiteLoad;
    }

    @Override
    public Filiale caricaFiliale(Integer idFiliale) {
        LOGGER.debug("--> Enter caricaFiliale");
        start("caricaFiliale");
        Filiale filiale = null;
        try {
            filiale = anagraficaService.caricaFiliale(idFiliale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFiliale");
        }
        LOGGER.debug("--> Exit caricaFiliale ");
        return filiale;
    }

    @Override
    public List<Filiale> caricaFiliali() {
        LOGGER.debug("--> Enter caricaFiliali");
        start("caricaFiliali");
        List<Filiale> filiali = null;
        try {
            filiali = anagraficaService.caricaFiliali();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFiliali");
        }
        LOGGER.debug("--> Exit caricaFiliali ");
        return filiali;
    }

    @Override
    public List<Filiale> caricaFiliali(Banca banca, String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaFiliali");
        start("caricaFiliali");
        List<Filiale> filiali = null;
        try {
            filiali = anagraficaService.caricaFiliali(banca, fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaFiliali");
        }
        LOGGER.debug("--> Exit caricaFiliali ");
        return filiali;
    }

    @Override
    public List<Prestazione> caricaPrestazioni() {
        LOGGER.debug("--> Enter caricaPrestazioni");
        start("caricaPrestazioni");

        List<Prestazione> prestazioni = null;
        try {
            prestazioni = anagraficaService.caricaPrestazioni();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaPrestazioni");
        }
        LOGGER.debug("--> Exit caricaPrestazioni ");
        return prestazioni;
    }

    @Override
    public List<RapportoBancarioAzienda> caricaRapportiBancariAzienda(String fieldSearch, String valueSearch) {
        LOGGER.debug("--> Enter caricaRapportiBancariAzienda");
        start("caricaRapportiBancariAzienda");
        List<RapportoBancarioAzienda> rapportiBancari = null;
        try {
            rapportiBancari = anagraficaService.caricaRapportiBancariAzienda(fieldSearch, valueSearch);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRapportiBancariAzienda");
        }
        LOGGER.debug("--> Exit caricaRapportiBancariAzienda ");
        return rapportiBancari;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita) {
        LOGGER.debug("--> Enter caricaRapportiBancariSedeEntita");
        start("caricaRapportiBancariSedeEntita");
        List<RapportoBancarioSedeEntita> rapportiBancari = null;
        try {
            rapportiBancari = anagraficaService.caricaRapportiBancariSedeEntita(idSedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRapportiBancariSedeEntita");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit caricaRapportiBancariSedeEntita con result " + rapportiBancari);
        }
        return rapportiBancari;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita,
            boolean ignoraEredita) {
        LOGGER.debug("--> Enter caricaRapportiBancariSedeEntita");
        start("caricaRapportiBancariSedeEntita");
        List<RapportoBancarioSedeEntita> result = null;
        try {
            result = anagraficaService.caricaRapportiBancariSedeEntita(idSedeEntita, ignoraEredita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRapportiBancariSedeEntita");
        }
        LOGGER.debug("--> Exit caricaRapportiBancariSedeEntita ");
        return result;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntitaPrincipale(String fieldSearch,
            String valueSearch, Integer idEntita) {
        LOGGER.debug("--> Enter caricaRapportiBancariSedeEntitaPrincipale");
        start("caricaRapportiBancariSedeEntitaPrincipale");
        List<RapportoBancarioSedeEntita> rapportiBancari = null;
        try {
            rapportiBancari = anagraficaService.caricaRapportiBancariSedeEntitaPrincipale(fieldSearch, valueSearch,
                    idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRapportiBancariSedeEntitaPrincipale");
        }
        LOGGER.debug("--> Exit caricaRapportiBancariSedeEntitaPrincipale ");
        return rapportiBancari;
    }

    @Override
    public RapportoBancarioAzienda caricaRapportoBancario(Integer idRapportoBancario, boolean initializeLazy) {
        LOGGER.debug("--> Enter caricaRapportoBancario");
        start("caricaRapportoBancario");
        RapportoBancarioAzienda rapportoBancarioAzienda = null;
        try {
            rapportoBancarioAzienda = anagraficaService.caricaRapportoBancario(idRapportoBancario, initializeLazy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRapportoBancario");
        }
        LOGGER.debug("--> Exit caricaRapportoBancario ");
        return rapportoBancarioAzienda;
    }

    @Override
    public List<RapportoBancarioSedeEntita> caricaRiepilogoDatiBancari() {
        LOGGER.debug("--> Enter caricaRiepilogoDatiBancari");
        start("caricaRiepilogoDatiBancari");

        List<RapportoBancarioSedeEntita> riepilogo = null;
        try {
            riepilogo = anagraficaService.caricaRiepilogoDatiBancari();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRiepilogoDatiBancari");
        }
        LOGGER.debug("--> Exit caricaRiepilogoDatiBancari ");
        return riepilogo;
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntita() {
        LOGGER.debug("--> Enter caricaRiepilogoSediEntita");
        start("caricaRiepilogoSediEntita");
        List<RiepilogoSedeEntitaDTO> riepilogo = new ArrayList<>();
        try {
            riepilogo = anagraficaService.caricaRiepilogoSediEntita();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRiepilogoSediEntita");
        }
        LOGGER.debug("--> Exit caricaRiepilogoSediEntita ");
        return riepilogo;
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByAgente(AgenteLite agente) {
        LOGGER.debug("--> Enter caricaRiepilogoSediEntitaByAgente");
        start("caricaRiepilogoSediEntitaByAgente");
        List<RiepilogoSedeEntitaDTO> riepilogo = new ArrayList<>();
        try {
            riepilogo = anagraficaService.caricaRiepilogoSediEntitaByAgente(agente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRiepilogoSediEntitaByAgente");
        }
        LOGGER.debug("--> Exit caricaRiepilogoSediEntitaByAgente ");
        return riepilogo;
    }

    @Override
    public List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByVettore(VettoreLite vettore) {
        LOGGER.debug("--> Enter caricaRiepilogoSediEntitaByVettore");
        start("caricaRiepilogoSediEntitaByVettore");
        List<RiepilogoSedeEntitaDTO> riepilogo = new ArrayList<>();
        try {
            riepilogo = anagraficaService.caricaRiepilogoSediEntitaByVettore(vettore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaRiepilogoSediEntitaByVettore");
        }
        LOGGER.debug("--> Exit caricaRiepilogoSediEntitaByVettore ");
        return riepilogo;
    }

    @Override
    public SedeAzienda caricaSedeAzienda(Integer idSedeAzienda) {
        LOGGER.debug("--> Enter caricaSedeAzienda");
        start("caricaSedeAzienda");
        SedeAzienda sedeAzienda = null;
        try {
            sedeAzienda = anagraficaService.caricaSedeAzienda(idSedeAzienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedeAzienda");
        }
        LOGGER.debug("--> Exit caricaSedeAzienda ");
        return sedeAzienda;
    }

    @Override
    public SedeEntita caricaSedeEntita(Integer idSedeEntita) {
        LOGGER.debug("--> Enter caricaSedeEntita");
        start("caricaSedeEntita");
        SedeEntita sedeEntita = null;
        try {
            sedeEntita = anagraficaService.caricaSedeEntita(idSedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedeEntita");
        }
        LOGGER.debug("--> Exit caricaSedeEntita ");
        return sedeEntita;
    }

    @Override
    public SedeEntita caricaSedeEntita(Integer idSedeEntita, boolean caricaLazy) {
        LOGGER.debug("--> Enter caricaSedeEntita");
        start("caricaSedeEntita");
        SedeEntita sedeEntita = null;
        try {
            sedeEntita = anagraficaService.caricaSedeEntita(idSedeEntita, caricaLazy);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedeEntita");
        }
        LOGGER.debug("--> Exit caricaSedeEntita ");
        return sedeEntita;
    }

    @Override
    public SedeEntita caricaSedePredefinitaEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaSedePrincipaleEntita");
        start("caricaSedePrincipaleEntita");
        SedeEntita sedeEntita = null;
        try {
            sedeEntita = anagraficaService.caricaSedePredefinitaEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedePrincipaleEntita");
        }
        LOGGER.debug("--> Exit caricaSedePrincipaleEntita ");
        return sedeEntita;
    }

    @Override
    public SedeAzienda caricaSedePrincipaleAzienda(Azienda azienda) {
        LOGGER.debug("--> Enter caricaSedePrincipaleAzienda");
        start("caricaSedePrincipaleAzienda");
        SedeAzienda sedeAzienda = null;
        try {
            sedeAzienda = anagraficaService.caricaSedePrincipaleAzienda(azienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedePrincipaleAzienda");
        }
        LOGGER.debug("--> Exit caricaSedePrincipaleAzienda ");
        return sedeAzienda;
    }

    @Override
    public SedeEntita caricaSedePrincipaleEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaSedePrincipaleEntita");
        start("caricaSedePrincipaleEntita");
        SedeEntita sedeEntita = null;
        try {
            sedeEntita = anagraficaService.caricaSedePrincipaleEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSedePrincipaleEntita");
        }
        LOGGER.debug("--> Exit caricaSedePrincipaleEntita ");
        return sedeEntita;
    }

    @Override
    public List<SedeAnagrafica> caricaSediAnagrafica(Anagrafica anagrafica) {
        LOGGER.debug("--> Enter caricaSediAnagrafica");
        start("caricaSediAnagrafica");
        List<SedeAnagrafica> sedi = null;
        try {
            sedi = anagraficaService.caricaSediAnagrafica(anagrafica);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediAnagrafica");
        }
        LOGGER.debug("--> Exit caricaSediAnagrafica ");
        return sedi;
    }

    @Override
    public List<SedeAnagrafica> caricaSediAnagraficaAzienda(Azienda azienda) {
        LOGGER.debug("--> Enter caricaSediAnagraficaAzienda");
        start("caricaSediAnagraficaAzienda");
        List<SedeAnagrafica> sedi = null;
        try {
            sedi = anagraficaService.caricaSediAnagraficaAzienda(azienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediAnagraficaAzienda");
        }
        LOGGER.debug("--> Exit caricaSediAnagraficaAzienda ");
        return sedi;
    }

    @Override
    public Set<SedeEntita> caricaSediAssociate(AgenteLite agente) {
        LOGGER.debug("--> Enter caricaSediAssociate");
        start("caricaSediAssociate");
        Set<SedeEntita> result = null;
        try {
            result = anagraficaService.caricaSediAssociate(agente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediAssociate");
        }
        LOGGER.debug("--> Exit caricaSediAssociate ");
        return result;
    }

    @Override
    public List<SedeAzienda> caricaSediAzienda() {
        LOGGER.debug("--> Enter caricaSediAzienda");
        start("caricaSediAzienda");
        List<SedeAzienda> result = null;
        try {
            result = anagraficaService.caricaSediAzienda();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediAzienda");
        }
        LOGGER.debug("--> Exit caricaSediAzienda ");
        return result;
    }

    @Override
    public List<SedeAzienda> caricaSediAzienda(Azienda azienda) {
        LOGGER.debug("--> Enter caricaSediAzienda");
        start("caricaSediAzienda");
        List<SedeAzienda> sedi = null;
        try {
            sedi = anagraficaService.caricaSediAzienda(azienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediAzienda");
        }
        LOGGER.debug("--> Exit caricaSediAzienda ");
        return sedi;
    }

    @Override
    public List<SedeEntitaLite> caricaSediEntita(Integer idEntita) {
        LOGGER.debug("--> Enter caricaSediEntita");
        start("caricaSediEntita");
        List<SedeEntitaLite> sedi = new ArrayList<>();
        try {
            sedi = anagraficaService.caricaSediEntita(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediEntita");
        }
        LOGGER.debug("--> Exit caricaSediEntita ");
        return sedi;
    }

    @Override
    public List<SedeEntita> caricaSediEntita(String filtro, Integer idEntita,
            CaricamentoSediEntita caricamentoSediEntita, Boolean caricaSedeDisabilitate) {
        LOGGER.debug("--> Enter caricaSediEntita");
        start("caricaSediEntita");
        List<SedeEntita> sedi = null;
        try {
            sedi = anagraficaService.caricaSediEntita(filtro, idEntita, caricamentoSediEntita, caricaSedeDisabilitate);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediEntita");
        }
        LOGGER.debug("--> Exit caricaSediEntita ");
        return sedi;
    }

    @Override
    public Set<SedeEntita> caricaSediEntitaAssociate(VettoreLite vettore) {
        LOGGER.debug("--> Enter caricaSediEntitaAssociate");
        start("caricaSediEntitaAssociate");
        Set<SedeEntita> result = null;
        try {
            result = anagraficaService.caricaSediEntitaAssociate(vettore);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediEntitaAssociate");
        }
        LOGGER.debug("--> Exit caricaSediEntitaAssociate ");
        return result;
    }

    @Override
    public List<SedeEntita> caricaSediEntitaNonEreditaDatiComerciali(Integer idEntita) {
        LOGGER.debug("--> Enter caricaSediEntitaNonEreditaDatiComerciali");
        start("caricaSediEntitaNonEreditaDatiComerciali");
        List<SedeEntita> sedi = null;
        try {
            sedi = anagraficaService.caricaSediEntitaNonEreditaDatiComerciali(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediEntitaNonEreditaDatiComerciali");
        }
        LOGGER.debug("--> Exit caricaSediEntitaNonEreditaDatiComerciali ");
        return sedi;
    }

    @Override
    public List<SedeAzienda> caricaSediSecondarieAzienda(Azienda azienda) {
        LOGGER.debug("--> Enter caricaSediSecondarieAzienda");
        start("caricaSediSecondarieAzienda");
        List<SedeAzienda> sediAzienda = null;
        try {
            sediAzienda = anagraficaService.caricaSediSecondarieAzienda(azienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediSecondarieAzienda");
        }
        LOGGER.debug("--> Exit caricaSediSecondarieAzienda ");
        return sediAzienda;
    }

    @Override
    public List<SedeEntita> caricaSediSecondarieEntita(Entita entita) {
        LOGGER.debug("--> Enter caricaSediSecondarieEntita");
        start("caricaSediSecondarieEntita");
        List<SedeEntita> sediEntita = null;
        try {
            sediEntita = anagraficaService.caricaSediSecondarieEntita(entita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaSediSecondarieEntita");
        }
        LOGGER.debug("--> Exit caricaSediSecondarieEntita ");
        return sediEntita;
    }

    @Override
    public Entita confermaClientePotenziale(Integer idEntita) {
        LOGGER.debug("--> Enter confermaClientePotenziale");
        start("confermaClientePotenziale");
        Entita entita = null;
        try {
            entita = anagraficaService.confermaClientePotenziale(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("confermaClientePotenziale");
        }
        LOGGER.debug("--> Exit confermaClientePotenziale ");
        return entita;
    }

    @Override
    public SedeEntita creaSedeEntitaGenerica(Integer idEntita) {
        LOGGER.debug("--> Enter creaSedeEntitaGenerica");
        start("creaSedeEntitaGenerica");
        SedeEntita sedeEntita = null;
        try {
            sedeEntita = anagraficaService.creaSedeEntitaGenerica(idEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("creaSedeEntitaGenerica");
        }
        LOGGER.debug("--> Exit creaSedeEntitaGenerica");
        return sedeEntita;
    }

    @Override
    public String[] getVariabiliPatternCodiceDocumento() {
        LOGGER.debug("--> Enter getVariabiliPatternCodiceDocumento");
        start("getVariabiliPatternCodiceDocumento");

        String[] result = null;
        try {
            result = anagraficaService.getVariabiliPatternCodiceDocumento();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("getVariabiliPatternCodiceDocumento");
        }
        LOGGER.debug("--> Exit getVariabiliPatternCodiceDocumento ");
        return result;
    }

    @Override
    public List<Contatto> ricercaContatti(Map<String, Object> parametri) {
        LOGGER.debug("--> Enter ricercaContatti");
        start("ricercaContatti");
        List<Contatto> contatti = null;
        try {
            contatti = anagraficaService.ricercaContatti(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaContatti");
        }
        LOGGER.debug("--> Exit ricercaContatti ");
        return contatti;
    }

    @Override
    public List<Contatto> ricercaContattiPerEntita(Entita entita, Map<String, Object> parametri) {
        LOGGER.debug("--> Enter ricercaContattiPerEntita");
        start("ricercaContattiPerEntita");
        List<Contatto> contatti = null;
        try {
            contatti = anagraficaService.ricercaContattiPerEntita(entita, parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaContattiPerEntita");
        }
        LOGGER.debug("--> Exit ricercaContattiPerEntita ");
        return contatti;
    }

    @Override
    public List<Deposito> ricercaDepositi(ParametriRicercaDepositi parametri) {
        LOGGER.debug("--> Enter ricercaDepositi");
        start("ricercaDepositi");

        List<Deposito> depositi = null;
        try {
            depositi = anagraficaService.ricercaDepositi(parametri);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaDepositi");
        }
        LOGGER.debug("--> Exit ricercaDepositi ");
        return depositi;
    }

    @Override
    public List<EntitaLite> ricercaEntita(ParametriRicercaEntita parametriRicercaEntita) {
        LOGGER.debug("--> Enter ricercaEntita");
        start("ricercaEntitaDTO");
        List<EntitaLite> result = null;
        try {
            result = anagraficaService.ricercaEntita(parametriRicercaEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaEntitaDTO");
        }
        LOGGER.debug("--> Exit ricercaEntita");
        return result;
    }

    @Override
    public List<EntitaLite> ricercaEntita(String codiceFiscale, String partitaIva) {
        LOGGER.debug("--> Enter ricercaEntita");
        start("ricercaEntita");
        List<EntitaLite> result = null;
        try {
            result = anagraficaService.ricercaEntita(codiceFiscale, partitaIva);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaEntita");
        }
        LOGGER.debug("--> Exit ricercaEntita");
        return result;
    }

    @Override
    public List<EntitaLite> ricercaEntitaSearchObject(ParametriRicercaEntita parametriRicercaEntita) {
        LOGGER.debug("--> Enter ricercaEntitaSearchObject");
        start("ricercaEntitaSearchObject");
        List<EntitaLite> result = null;
        try {
            result = anagraficaService.ricercaEntitaSearchObject(parametriRicercaEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("ricercaEntitaSearchObject");
        }
        LOGGER.debug("--> Exit ricercaEntitaSearchObject ");
        return result;
    }

    @Override
    public Azienda salvaAzienda(Azienda azienda) {
        LOGGER.debug("--> Enter salvaAzienda");
        start("salvaAzienda");
        Azienda aziendaSave = null;
        try {
            aziendaSave = anagraficaService.salvaAzienda(azienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAzienda");
        }
        LOGGER.debug("--> Exit salvaAzienda ");
        return aziendaSave;
    }

    @Override
    public AziendaAnagraficaDTO salvaAziendaAnagrafica(AziendaAnagraficaDTO aziendaAnagraficaDTO) {
        LOGGER.debug("--> Enter salvaAziendaAnagrafica");
        start("salvaAziendaAnagrafica");
        AziendaAnagraficaDTO aziendaAnagraficaDTOSave = null;
        try {
            aziendaAnagraficaDTOSave = anagraficaService.salvaAziendaAnagrafica(aziendaAnagraficaDTO);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaAziendaAnagrafica");
        }
        LOGGER.debug("--> Exit salvaAziendaAnagrafica ");
        return aziendaAnagraficaDTOSave;
    }

    @Override
    public Banca salvaBanca(Banca banca) {
        LOGGER.debug("--> Enter salvaBanca");
        start("salvaBanca");
        Banca bancaSave = null;
        try {
            bancaSave = anagraficaService.salvaBanca(banca);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaBanca");
        }
        LOGGER.debug("--> Exit salvaBanca ");
        return bancaSave;
    }

    @Override
    public CategoriaEntita salvaCategoriaEntita(CategoriaEntita categoriaEntita) {
        LOGGER.debug("--> Enter salvaCategoriaEntita");
        start("salvaCategoriaEntita");
        CategoriaEntita categoriaEntitaSave = null;
        try {
            categoriaEntitaSave = anagraficaService.salvaCategoriaEntita(categoriaEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaCategoriaEntita");
        }
        LOGGER.debug("--> Exit salvaCategoriaEntita ");
        return categoriaEntitaSave;
    }

    @Override
    public Contatto salvaContatto(Contatto contatto) {
        LOGGER.debug("--> Enter salvaContatto");
        start("salvaContatto");
        Contatto contattoSave = null;
        try {
            contattoSave = anagraficaService.salvaContatto(contatto);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaContatto");
        }
        LOGGER.debug("--> Exit salvaContatto ");
        return contattoSave;
    }

    @Override
    public ContattoSedeEntita salvaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita) {
        LOGGER.debug("--> Enter salvaContattoSedeEntita");
        start("salvaContattoSedeEntita");
        ContattoSedeEntita contattoSedeEntitaSave = null;
        try {
            contattoSedeEntitaSave = anagraficaService.salvaContattoSedeEntita(contattoSedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaContattoSedeEntita");
        }
        LOGGER.debug("--> Exit salvaContattoSedeEntita ");
        return contattoSedeEntitaSave;
    }

    @Override
    public Deposito salvaDeposito(Deposito deposito) {
        LOGGER.debug("--> Enter salvaDeposito");
        start("salvaDeposito");
        Deposito depositoSave = null;
        try {
            depositoSave = anagraficaService.salvaDeposito(deposito);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaDeposito");
        }
        LOGGER.debug("--> Exit salvaDeposito ");
        return depositoSave;
    }

    @Override
    public Entita salvaEntita(Entita entita) throws AnagraficheDuplicateException {
        LOGGER.debug("--> Enter salvaEntita");

        start("salvaEntita");
        Entita entitaSave = null;
        try {
            entitaSave = anagraficaService.salvaEntita(entita);
        } catch (AnagraficheDuplicateException e) {
            throw e;

        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaEntita");
        }
        LOGGER.debug("--> Exit salvaEntita ");
        return entitaSave;
    }

    @Override
    public Filiale salvaFiliale(Filiale filiale) {
        LOGGER.debug("--> Enter salvaFiliale");
        start("salvaFiliale");
        Filiale filialeSave = null;
        try {
            filialeSave = anagraficaService.salvaFiliale(filiale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaFiliale");
        }
        LOGGER.debug("--> Exit salvaFiliale ");
        return filialeSave;
    }

    @Override
    public RapportoBancarioAzienda salvaRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
        LOGGER.debug("--> Enter salvaRapportoBancarioAzienda");
        start("salvaRapportoBancarioAzienda");
        RapportoBancarioAzienda rapportoBancarioAzienda2 = null;
        try {
            rapportoBancarioAzienda2 = anagraficaService.salvaRapportoBancarioAzienda(rapportoBancarioAzienda);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRapportoBancarioAzienda");
        }
        LOGGER.debug("--> Exit salvaRapportoBancarioAzienda ");
        return rapportoBancarioAzienda2;
    }

    @Override
    public RapportoBancarioSedeEntita salvaRapportoBancarioSedeEntita(
            RapportoBancarioSedeEntita rapportoBancarioSedeEntita) {
        LOGGER.debug("--> Enter salvaRapportoBancarioSedeEntita");
        start("salvaRapportoBancarioSedeEntita");
        try {
            rapportoBancarioSedeEntita = anagraficaService.salvaRapportoBancarioSedeEntita(rapportoBancarioSedeEntita);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRapportoBancarioSedeEntita");
        }
        LOGGER.debug("--> Exit salvaRapportoBancarioSedeEntita ");
        return rapportoBancarioSedeEntita;
    }

    @Override
    public SedeAzienda salvaSedeAzienda(SedeAzienda sedeAzienda) throws SedeEntitaPrincipaleAlreadyExistException {
        LOGGER.debug("--> Enter salvaSedeAzienda");
        start("salvaSedeAzienda");
        SedeAzienda sedeAziendaSave = null;
        try {
            sedeAziendaSave = anagraficaService.salvaSedeAzienda(sedeAzienda);
        } catch (SedeEntitaPrincipaleAlreadyExistException e) {
            LOGGER.error("--> errore, sedeazienda non salvata: SedeEntita principale gi� esistente  ", e);
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSedeAzienda");
        }
        LOGGER.debug("--> Exit salvaSedeAzienda ");
        return sedeAziendaSave;
    }

    @Override
    public SedeEntita salvaSedeEntita(SedeEntita sedeEntita) throws SedeEntitaPrincipaleAlreadyExistException {
        LOGGER.debug("--> Enter salvaSedeEntita");
        start("salvaSedeEntita");
        SedeEntita sedeEntitaSave = null;
        try {
            sedeEntitaSave = anagraficaService.salvaSedeEntita(sedeEntita);
        } catch (SedeEntitaPrincipaleAlreadyExistException e) {
            LOGGER.error("--> errore, sede entita non salvata: SedeEntita principale gi� esistente  ", e);
            throw e;
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaSedeEntita");
        }
        LOGGER.debug("--> Exit salvaSedeEntita ");
        return sedeEntitaSave;
    }

    /**
     * @param anagraficaService
     *            The anagraficaService to set.
     */
    public void setAnagraficaService(AnagraficaService anagraficaService) {
        this.anagraficaService = anagraficaService;
    }

    @Override
    public void sostituisciDatiBancari(List<RapportoBancarioSedeEntita> rapporti, Banca banca, Filiale filiale) {
        LOGGER.debug("--> Enter sostituisciDatiBancari");
        start("sostituisciDatiBancari");
        try {
            anagraficaService.sostituisciDatiBancari(rapporti, banca, filiale);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("sostituisciDatiBancari");
        }
        LOGGER.debug("--> Exit sostituisciDatiBancari ");

    }

}
