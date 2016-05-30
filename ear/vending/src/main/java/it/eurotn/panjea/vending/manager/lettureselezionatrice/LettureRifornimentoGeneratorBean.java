package it.eurotn.panjea.vending.manager.lettureselezionatrice;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.operatori.interfaces.OperatoriManager;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoManager;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LetturaAreaMagazzinoGenerator;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LetturaAreaPartiteGenerator;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LetturaAreaRifornimentoGenerator;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LettureRifornimentoGenerator;

@Stateless(name = "Panjea.LettureRifornimentoGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LettureRifornimentoGenerator")
public class LettureRifornimentoGeneratorBean implements LettureRifornimentoGenerator {

    private static final Logger LOGGER = Logger.getLogger(LettureRifornimentoGeneratorBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private LetturaAreaRifornimentoGenerator letturaAreaRifornimentoGenerator;

    @EJB
    private LetturaAreaMagazzinoGenerator letturaAreaMagazzinoGenerator;

    @EJB
    private LetturaAreaPartiteGenerator letturaAreaPartiteGenerator;

    @EJB
    private InstallazioniManager installazioniManager;

    @EJB
    private OperatoriManager operatoriManager;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private AreaRifornimentoManager areaRifornimentoManager;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void associaRifornimento(LetturaSelezionatrice letturaSelezionatrice) {
        LOGGER.debug("--> Enter associaRifornimento");

        if (letturaSelezionatrice.getRifornimento() == null || letturaSelezionatrice.getRifornimento().isNew()) {
            throw new GenericException("Rifornimento non presente sulla lettura, impossibile associarlo.");
        }

        StringBuilder sb = new StringBuilder(200);
        sb.append("update AreaRifornimento af ");
        sb.append("set af.incasso = :paramIncasso, ");
        sb.append("af.reso = :paramReso, ");
        sb.append("af.numeroSacchetto = :paramNumeroSacchetto ");
        sb.append("where af.areaMagazzino.id = :paramIdAreaMagazzino");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramIncasso", letturaSelezionatrice.getImporto());
        query.setParameter("paramReso", letturaSelezionatrice.getReso());
        query.setParameter("paramNumeroSacchetto", letturaSelezionatrice.getNumeroSacchetto());
        query.setParameter("paramIdAreaMagazzino", letturaSelezionatrice.getRifornimento().getId());

        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error(
                    "--> errore durante l'aggiornamento dell'incasso rifornimento dalla lettura della selezionatrice.",
                    e);
            throw new GenericException(
                    "errore durante l'aggiornamento dell'incasso rifornimento dalla lettura della selezionatrice.", e);
        }
        LOGGER.debug("--> Exit creaRifornimento");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void creaRifornimento(LetturaSelezionatrice letturaSelezionatrice) {
        LOGGER.debug("--> Enter creaRifornimento");

        // creo il rifornimento. Per ora non servono le righe quindi creo solamente AreaRifornimento e AreaMagazzino (
        // che attraverso i metodi di businness creerà tutto quello che serve [AreaRate, ecc...])

        // Mi devo creare un'installazione "finta" perchè i dati che devo usare di distributore e operatore sono quelli
        // della lettura
        try {
            Installazione installazione = installazioniManager
                    .caricaById(letturaSelezionatrice.getInstallazione().getId());
            Operatore caricatore = operatoriManager.caricaById(letturaSelezionatrice.getCaricatore().getId());

            AreaRifornimento areaRifornimento = letturaAreaRifornimentoGenerator.creaAreaRifornimento(installazione,
                    letturaSelezionatrice.getDistributore(), caricatore, letturaSelezionatrice.getImporto(),
                    letturaSelezionatrice.getReso(), letturaSelezionatrice.getNumeroSacchetto());
            AreaMagazzino areaMagazzino = letturaAreaMagazzinoGenerator.aggiornaAreaMagazzino(areaRifornimento,
                    letturaSelezionatrice);
            areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, false);
            AreaPartite areaPartite = letturaAreaPartiteGenerator.creaAreaPartite(areaMagazzino);
            areaMagazzino = areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, areaPartite, false, false);

            // il valida righe magazzino esegue una totalizzazione e quindi mi porta il totale documento a 0 non
            // essendoci righe. Per questo motivo mi tocca risettarlo e salvare l'area di nuovo
            areaMagazzino.getDocumento().getTotale().setImportoInValuta(letturaSelezionatrice.getImporto());
            areaMagazzino.getDocumento().getTotale().calcolaImportoValutaAzienda(2);
            areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, false);

            areaRifornimento.setAreaMagazzino(areaMagazzino);
            areaRifornimentoManager.salva(areaRifornimento);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la creazione del rifornimento.", e);
            throw new GenericException("errore durante la creazione del rifornimento.", e);
        }

        LOGGER.debug("--> Exit creaRifornimento");
    }

}
