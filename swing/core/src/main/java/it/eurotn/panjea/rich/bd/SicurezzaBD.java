package it.eurotn.panjea.rich.bd;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.Authentication;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.rich.login.ConnessioneUtente;
import it.eurotn.panjea.service.interfaces.JpaUtils;
import it.eurotn.panjea.sicurezza.JecPrincipalSpring;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Permesso;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.service.interfaces.SicurezzaService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.security.JecPrincipal;

public class SicurezzaBD extends AbstractBaseBD implements ISicurezzaBD {

    private static final Logger LOGGER = Logger.getLogger(SicurezzaBD.class);
    public static final String BEAN_ID = "sicurezzaBD";
    private SicurezzaService sicurezzaService;
    private JpaUtils jpaUtils;

    private ConnessioneUtente connessioneUtente;

    @Override
    public void cambiaPasswordUtenteLoggato(String oldPassword, String newPassword) {
        LOGGER.debug("--> Enter cambiaPasswordUtenteLoggato");
        start("cambiaPasswordUtenteLoggato");
        try {
            sicurezzaService.cambiaPasswordUtenteLoggato(oldPassword, newPassword);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cambiaPasswordUtenteLoggato");
        }
        LOGGER.debug("--> Exit cambiaPasswordUtenteLoggato ");
    }

    @Override
    public void cancellaDatiMail(DatiMail datiMail) {
        LOGGER.debug("--> Enter cancellaDatiMail");
        start("cancellaDatiMail");
        try {
            sicurezzaService.cancellaDatiMail(datiMail);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("cancellaDatiMail");
        }
        LOGGER.debug("--> Exit cancellaDatiMail ");
    }

    @Override
    public boolean cancellaRuolo(Ruolo ruolo) {
        LOGGER.debug("---> Enter cancellaRuolo " + ruolo);
        try {
            sicurezzaService.cancellaRuolo(ruolo);
            LOGGER.debug("---> Exit cancellaRuolo");
            return true;
        } catch (Exception e) {
            LOGGER.error("---> Errore nel cancellare il ruolo");
            throw new PanjeaRuntimeException(e);
        }
    }

    @Override
    public boolean cancellaUtente(Utente utente) {
        LOGGER.debug("---> Enter cancellaUtente " + utente);
        try {
            sicurezzaService.cancellaUtente(utente);
            LOGGER.debug("---> Exit cancellaUtente");
            return true;
        } catch (Exception e) {
            LOGGER.error("---> Errore nel cancellare l'utente");
            throw new PanjeaRuntimeException(e);
        }
    }

    @Override
    public List<String> caricaAziendeDeployate() {
        return jpaUtils.getAziendeDeployate();
    }

    @Override
    public DatiMail caricaDatiMail(DatiMail datiMail) {
        LOGGER.debug("--> Enter caricaDatiMail");
        start("caricaDatiMail");

        DatiMail datiMailLoad = null;
        try {
            datiMailLoad = sicurezzaService.caricaDatiMail(datiMail);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDatiMail");
        }
        LOGGER.debug("--> Exit caricaDatiMail ");
        return datiMailLoad;
    }

    @Override
    public List<DatiMail> caricaDatiMail(Integer idUtente) {
        LOGGER.debug("--> Enter caricaDatiMail");
        start("caricaDatiMail");

        List<DatiMail> datiMail = null;
        try {
            datiMail = sicurezzaService.caricaDatiMail(idUtente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaDatiMail");
        }
        LOGGER.debug("--> Exit caricaDatiMail ");
        return datiMail;
    }

    @Override
    public List<Permesso> caricaPermessi() {
        LOGGER.debug("---> Enter caricaPermessi");
        List<Permesso> permessi = null;
        try {
            permessi = sicurezzaService.caricaPermessi();
        } catch (Exception e) {
            LOGGER.error("---> Errore nel caricare i permessi", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("---> Exit caricaPermessi " + permessi);
        return permessi;
    }

    @Override
    public List<Ruolo> caricaRuoliAziendaCorrente() {
        LOGGER.debug("---> Enter caricaRuoliAziendaCorrente");
        List<Ruolo> ruoli = null;
        try {
            ruoli = sicurezzaService.caricaRuoliAziendaCorrente();
        } catch (Exception e) {
            LOGGER.error("---> Errore nel caricare i ruoli dell'azineda utente loggato", e);
            throw new PanjeaRuntimeException(e);
        }
        LOGGER.debug("---> Exit caricaRuoliAziendaCorrente " + ruoli);
        return ruoli;
    }

    @Override
    public Ruolo caricaRuolo(Integer idRuolo) {
        try {
            return sicurezzaService.caricaRuolo(idRuolo);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento del ruolo.");
            throw new PanjeaRuntimeException(e);
        }
    }

    @Override
    public Utente caricaUtente(Integer idUtente) {
        try {
            return sicurezzaService.caricaUtente(idUtente);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dell'utente.");
            throw new PanjeaRuntimeException(e);
        }
    }

    @Override
    public Utente caricaUtente(String codiceUtente) {
        LOGGER.debug("--> Enter caricaUtente");
        start("caricaUtente");
        Utente utente = null;
        try {
            utente = sicurezzaService.caricaUtente(codiceUtente);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("caricaUtente");
        }
        LOGGER.debug("--> Exit caricaUtente ");
        return utente;
    }

    @Override
    public List<Utente> caricaUtenti(String fieldSearch, String valueSearch) {
        LOGGER.debug("---> Enter caricaUtenti");
        List<Utente> utenti = null;
        try {
            utenti = sicurezzaService.caricaUtenti(fieldSearch, valueSearch);
        } catch (Exception e) {
            LOGGER.error("---> Errore nel aricare gli utenti");
            throw new PanjeaRuntimeException(e);
        }
        LOGGER.debug("---> Exit caricaUtenti " + utenti);
        return utenti;
    }

    @Override
    public void flushCache() {
        try {
            sicurezzaService.flushCache();
        } catch (Exception e) {
            LOGGER.error("---> Errore nel flush della security cache di JBoss", e);
            throw new PanjeaRuntimeException(e);
        }
    }

    /**
     * @return Returns the connessioneUtente.
     */
    public ConnessioneUtente getConnessioneUtente() {
        return connessioneUtente;
    }

    @Override
    public String getCredential() {
        LOGGER.debug("---> Enter getCredential");
        try {
            return sicurezzaService.getCredential();
        } catch (Exception e) {
            LOGGER.error("---> Errore nel caricare credential utente loggato", e);
            throw new PanjeaRuntimeException(e);
        }
    }

    @Override
    public Authentication login() {
        LOGGER.debug("-->Enter DoLogin");
        try {
            JecPrincipal jecPrincipal = sicurezzaService.login();
            if (!connessioneUtente.login()) {
                throw new PanjeaRuntimeException("Utenti max superati");
            }
            LOGGER.debug("-->Exit  DoLogin con jecPrincipal " + jecPrincipal);
            return new JecPrincipalSpring(jecPrincipal);
        } catch (Exception e) {
            LOGGER.error("--> Errore nel fare il login", e);
            PanjeaSwingUtil.checkAndThrowException(e);
            return null;
        }
    }

    @Override
    public DatiMail salvaDatiMail(DatiMail datiMail) {
        LOGGER.debug("--> Enter salvaDatiMail");
        start("salvaDatiMail");

        DatiMail datiMailSave = null;
        try {
            datiMailSave = sicurezzaService.salvaDatiMail(datiMail);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaDatiMail");
        }
        LOGGER.debug("--> Exit salvaDatiMail ");
        return datiMailSave;
    }

    @Override
    public Ruolo salvaRuolo(Ruolo ruolo) {
        LOGGER.debug("--> Enter salvaRuolo");
        start("salvaRuolo");

        Ruolo ruoloSalvato = null;
        try {
            ruoloSalvato = sicurezzaService.salvaRuolo(ruolo);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        } finally {
            end("salvaRuolo");
        }
        LOGGER.debug("--> Exit salvaRuolo ");
        return ruoloSalvato;
    }

    @Override
    public Ruolo salvaRuolo(Ruolo ruolo, List<Permesso> permessiDaAggiungere, List<Permesso> permessiDaRimuovere) {
        LOGGER.debug("---> Enter salvaRuolo " + ruolo);
        LOGGER.debug("---> permessi da aggiungere al ruolo " + permessiDaAggiungere);
        LOGGER.debug("---> permessi da rimuovere al ruolo " + permessiDaRimuovere);
        Ruolo ruoloSave = null;
        try {
            ruoloSave = sicurezzaService.salvaRuolo(ruolo, permessiDaAggiungere, permessiDaRimuovere);
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("---> Exit salvaRuolo " + ruolo);
        return ruoloSave;
    }

    @Override
    public Utente salvaUtente(Utente utente, List<Ruolo> ruoliDaAggiungere, List<Ruolo> ruoliDaRimuovere) {
        LOGGER.debug("---> Enter salvaUtente " + utente);
        LOGGER.debug("---> ruoli da aggiungere all'utente " + ruoliDaAggiungere);
        LOGGER.debug("---> ruoli da rimuovere all'utente " + ruoliDaRimuovere);
        Utente utenteSave = null;
        try {
            utenteSave = sicurezzaService.salvaUtente(utente, ruoliDaAggiungere, ruoliDaRimuovere);
        } catch (Exception e) {
            LOGGER.error("---> Errore nel salvare l'utente", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("---> Exit salvaUtente " + utente);
        return utenteSave;
    }

    /**
     * @param connessioneUtente
     *            The connessioneUtente to set.
     */
    public void setConnessioneUtente(ConnessioneUtente connessioneUtente) {
        this.connessioneUtente = connessioneUtente;
    }

    /**
     * @param jpaUtils
     *            The jpaUtils to set.
     */
    public void setJpaUtils(JpaUtils jpaUtils) {
        this.jpaUtils = jpaUtils;
    }

    /**
     * @param sicurezzaService
     *            The sicurezzaService to set.
     */
    public void setSicurezzaService(SicurezzaService sicurezzaService) {
        this.sicurezzaService = sicurezzaService;
    }

}
