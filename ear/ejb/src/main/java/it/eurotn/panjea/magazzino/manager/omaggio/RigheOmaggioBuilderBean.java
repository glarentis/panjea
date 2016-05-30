package it.eurotn.panjea.magazzino.manager.omaggio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.RigaArticoloGenerata;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.omaggio.RigaOmaggioArticolo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.manager.omaggio.interfaces.OmaggioManager;
import it.eurotn.panjea.magazzino.manager.omaggio.interfaces.RigheOmaggioBuilder;
import it.eurotn.panjea.magazzino.service.exception.ArticoloPerCodiceIvaAssenteException;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author leonardo
 */
@Stateless(name = "Panjea.RigheOmaggioBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheOmaggioBuilder")
public class RigheOmaggioBuilderBean implements RigheOmaggioBuilder {

    private static final Logger LOGGER = Logger.getLogger(RigheOmaggioBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private OmaggioManager omaggioManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    /**
     * Canella la riga omaggio trovata.
     *
     * @param areaMagazzino
     *            l'area magazzino di cui cancellare la riga omaggio generata se presente
     */
    private void cancellaRigaOmaggio(AreaMagazzino areaMagazzino) {
        RigaOmaggioArticolo rigaOmaggioArticolo = omaggioManager.caricaRigaOmaggio(areaMagazzino);
        if (rigaOmaggioArticolo != null) {
            rigaMagazzinoManager.getDao().cancellaRigaMagazzino(rigaOmaggioArticolo);
        }
    }

    /**
     * Carica l'id del primo articolo trovato che ha associato il codice iva specificato.
     *
     * @param codiceIva
     *            il codice iva di cui trovare l'articolo
     * @return l'id dell'articolo con codice iva specificato
     */
    @SuppressWarnings("unchecked")
    private Integer caricaIdArticoloByCodiceIva(CodiceIva codiceIva) {
        String hql = " select a.id from ArticoloLite a where a.codiceIva.id=:paramIdCodiceIva ";
        Query query = panjeaDAO.prepareQuery(hql);
        query.setParameter("paramIdCodiceIva", codiceIva.getId());

        List<Integer> idArticoli = null;
        try {
            idArticoli = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        Integer idArticolo = null;
        if (idArticoli != null && !idArticoli.isEmpty()) {
            idArticolo = idArticoli.iterator().next();
        } else {
            ArticoloPerCodiceIvaAssenteException articoloPerCodiceIvaAssenteException = new ArticoloPerCodiceIvaAssenteException(
                    codiceIva);
            LOGGER.warn("--> Articolo per codice iva assente, rilancio una ArticoloPerCodiceIvaAssenteException");
            throw new RuntimeException(articoloPerCodiceIvaAssenteException);
        }
        return idArticolo;
    }

    /**
     * Crea una singola riga omaggio chiamando il dao, costruendola secondo i parametri passati.
     *
     * @param areaMagazzino
     *            l'area magazzino per cui creare la riga
     * @param idArticoloOmaggio
     *            l'articolo da associare alla riga
     * @param importo
     *            l'importo da impostare alla riga
     * @return RigaArticolo
     */
    private RigaArticoloGenerata creaRigaArticoloOmaggio(AreaMagazzino areaMagazzino, Integer idArticoloOmaggio,
            Importo importo) {
        Integer idSedeEntita = null;
        Integer idEntita = null;
        String codiceLingua = null;
        if (areaMagazzino.getDocumento().getEntita() != null) {
            idEntita = areaMagazzino.getDocumento().getEntita().getId();
        }
        if (areaMagazzino.getDocumento().getSedeEntita() != null) {
            idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
            codiceLingua = areaMagazzino.getDocumento().getSedeEntita().getLingua();
        }

        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setIdArticolo(idArticoloOmaggio);
        parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
        parametri.setIdEntita(idEntita);
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setImporto(importo);
        parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
        parametri.setIdZonaGeografica(areaMagazzino.getIdZonaGeografica());
        parametri.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());
        parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
        parametri.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
        parametri.setCodiceLingua(codiceLingua);
        parametri.setTipologiaCodiceIvaAlternativo(areaMagazzino.getTipologiaCodiceIvaAlternativo());
        parametri.setIdAgente(null);
        parametri.setPercentualeScontoCommerciale(null);
        parametri.setStrategiaTotalizzazioneDocumento(
                areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

        RigaArticoloGenerata rigaArticolo = (RigaArticoloGenerata) rigaMagazzinoManager
                .getDao(new RigaOmaggioArticolo()).creaRigaArticolo(parametri);
        rigaArticolo.setRigaAutomatica(true);
        return rigaArticolo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino) {
        List<RigaMagazzino> righe = new ArrayList<>();
        // se il tipo area magazzino non ha impostato genera valori fatturato, non creo le righe conai
        boolean isValoriFatturato = areaMagazzino.getTipoAreaMagazzino().isValoriFatturato();

        // se provengo dalla conferma della fatturazione non creo le righe conai e l'omaggio perchè le ho già create,
        // ma se provengo da EVASIONE devo crearle
        TipoGenerazione tg = areaMagazzino.getDatiGenerazione().getTipoGenerazione();
        boolean isFatturazione = areaMagazzino.getDatiGenerazione().getDataCreazione() != null;

        if ((isValoriFatturato && !isFatturazione)
                || (isValoriFatturato && tg.compareTo(TipoGenerazione.EVASIONE) == 0)) {
            cancellaRigaOmaggio(areaMagazzino);

            // carico le righe con tipo omaggio 4, mi serve l'importo, mentre la qta e gli altri dati non sono rilevanti
            // per la creazione della riga omaggio
            String hql = " select ra.prezzoTotale from RigaArticolo ra where ra.tipoOmaggio=:paramTipoOmaggio and ra.areaMagazzino.id=:paramIdAreaMagazzino ";
            Query query = panjeaDAO.prepareQuery(hql);
            query.setParameter("paramIdAreaMagazzino", areaMagazzino.getId());
            query.setParameter("paramTipoOmaggio", TipoOmaggio.ALTRO_OMAGGIO);

            List<Importo> importiTotaliRiga = null;
            try {
                importiTotaliRiga = panjeaDAO.getResultList(query);
            } catch (DAOException e1) {
                LOGGER.error("--> Errore nel caricare gli importi per tipo omaggio", e1);
                throw new RuntimeException("--> Errore nel caricare gli importi per tipo omaggio", e1);
            }

            // se non ci sono righe con tipoOmaggio ALTRO, non genero nessuna riga
            if (importiTotaliRiga == null || (importiTotaliRiga != null && importiTotaliRiga.isEmpty())) {
                return null;
            }

            Importo importoTotale = new Importo();
            importoTotale.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
            for (Importo importo : importiTotaliRiga) {
                importoTotale = importoTotale.add(importo, importo.getImportoInValuta().scale());
            }

            // carico l'omaggio con tipoOmaggio 4 (ALTRO_OMAGGIO) per recuperare il codice iva e la descrizione da
            // mettere come nota in riga
            Omaggio omaggio = omaggioManager.caricaOmaggioByTipo(TipoOmaggio.ALTRO_OMAGGIO);

            if (omaggio.getCodiceIva() == null) {
                CodiceIvaPerTipoOmaggioAssenteException codiceIvaPerTipoOmaggioAssenteException = new CodiceIvaPerTipoOmaggioAssenteException();
                codiceIvaPerTipoOmaggioAssenteException.setTipoOmaggio(TipoOmaggio.ALTRO_OMAGGIO);
                LOGGER.warn(
                        "--> Codice iva per tipo omaggio assente, rilancio una CodiceIvaPerTipoOmaggioAssenteException");
                throw new RuntimeException(codiceIvaPerTipoOmaggioAssenteException);
            }

            // carico l'articolo con codice iva uguale al codice iva dell'omaggio con t.o.4 (prendo il primo se ne trovo
            // più di uno, errore se non ne trovo)
            Integer idArticoloOmaggio = caricaIdArticoloByCodiceIva(omaggio.getCodiceIva());

            // qta a 0 e x importo la somma delle righe con t.o.4 negata
            RigaArticoloGenerata rigaOmaggioArticolo = creaRigaArticoloOmaggio(areaMagazzino, idArticoloOmaggio,
                    importoTotale);
            rigaOmaggioArticolo.setQta(null);
            rigaOmaggioArticolo.setAreaMagazzino(areaMagazzino);
            rigaOmaggioArticolo.setPrezzoUnitario(importoTotale.negate());
            rigaOmaggioArticolo.setNoteRiga(omaggio.getDescrizionePerStampa());

            try {
                rigaOmaggioArticolo = (RigaArticoloGenerata) rigaMagazzinoManager.getDao()
                        .salvaRigaMagazzino(rigaOmaggioArticolo);
            } catch (RimanenzaLottiNonValidaException e) {
                LOGGER.error("--> Attenzione si sta cercando di salvare una riga omaggio con dei lotti", e);
                throw new IllegalStateException(e);
            } catch (RigheLottiNonValideException e) {
                LOGGER.error("--> Attenzione si sta cercando di salvare una riga omaggio con dei lotti", e);
                throw new IllegalStateException(e);
            } catch (QtaLottiMaggioreException e) {
                LOGGER.error("--> Attenzione si sta cercando di salvare una riga omaggio con dei lotti", e);
                throw new IllegalStateException(e);
            }

            // restituisco la riga in una collection visto che l'interfaccia prevede una lista di righe generate,
            // comunque la riga omaggio è una sola
            righe.add(rigaOmaggioArticolo);
        }
        return righe;
    }

}
