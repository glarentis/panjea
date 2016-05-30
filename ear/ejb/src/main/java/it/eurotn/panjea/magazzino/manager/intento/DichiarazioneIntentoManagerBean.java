package it.eurotn.panjea.magazzino.manager.intento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.AddebitoDichiarazioneIntentoSettings;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.intento.RigaAddebitoDichiarazioneIntentoArticolo;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.intento.exception.ArticoloAddebitoDichiarazioneIntentoNonPresenteException;
import it.eurotn.panjea.magazzino.manager.intento.interfaces.DichiarazioneIntentoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author fattazzo
 *
 */
@Stateless(mappedName = "Panjea.DichiarazioneIntentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DichiarazioneIntentoManager")
public class DichiarazioneIntentoManagerBean implements DichiarazioneIntentoManager {

    private static Logger logger = Logger.getLogger(DichiarazioneIntentoManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    /**
     * Carica le righe addebito per la dichiarazione di intento.
     *
     * @param areaMagazzino
     *            areaMagazzino
     * @param addebitoDichiarazioneIntentoInVigore
     *            addebitoDichiarazioneIntentoInVigore
     * @return List<RigaAddebitoDichiarazioneIntentoArticolo>
     */
    @SuppressWarnings("unchecked")
    private List<RigaArticolo> caricaRigheAddebitoDichiarazioneIntento(AreaMagazzino areaMagazzino,
            AddebitoDichiarazioneIntentoSettings addebitoDichiarazioneIntentoInVigore) {

        ArticoloLite articolo = addebitoDichiarazioneIntentoInVigore.getArticolo();

        Query query = panjeaDAO.prepareQuery(
                "select r from RigaArticolo r where r.areaMagazzino.id=:idAreaMagazzino and r.articolo.id=:idArticolo");
        query.setParameter("idAreaMagazzino", areaMagazzino.getId());
        query.setParameter("idArticolo", articolo.getId());

        List<RigaArticolo> list = new ArrayList<RigaArticolo>();
        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dei listini", e);
            throw new RuntimeException("Errore durante il caricamento dei listini", e);
        }
        return list;
    }

    /**
     * Genera la riga di addebito per la dichiarazione di intento prendendo prezzo e articolo dal settings del
     * magazzino.
     *
     * @param areaMagazzino
     *            areaMagazzino
     * @param addebitoDichiarazioneIntentoInVigore
     *            addebitoDichiarazioneIntentoInVigore
     * @return RigaAddebitoDichiarazioneIntentoArticolo
     */
    private RigaAddebitoDichiarazioneIntentoArticolo creaRigaAddebitoDichiarazioneIntento(AreaMagazzino areaMagazzino,
            AddebitoDichiarazioneIntentoSettings addebitoDichiarazioneIntentoInVigore) {
        ArticoloLite articolo = addebitoDichiarazioneIntentoInVigore.getArticolo();

        Integer idTipoMezzo = null;
        if (areaMagazzino.getMezzoTrasporto() != null) {
            idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
        }
        Integer idListinoAlternativo = null;
        if (areaMagazzino.getListinoAlternativo() != null) {
            idListinoAlternativo = areaMagazzino.getListinoAlternativo().getId();
        }
        Integer idListino = null;
        if (areaMagazzino.getListino() != null) {
            idListino = areaMagazzino.getListino().getId();
        }
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
        parametri.setIdArticolo(articolo.getId());
        parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
        parametri.setIdEntita(idEntita);
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setIdListinoAlternativo(idListinoAlternativo);
        parametri.setIdListino(idListino);
        parametri.setImporto(areaMagazzino.getDocumento().getTotale().clone());
        parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
        parametri.setIdTipoMezzo(idTipoMezzo);
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

        RigaAddebitoDichiarazioneIntentoArticolo rigaArticolo = (RigaAddebitoDichiarazioneIntentoArticolo) rigaMagazzinoManager
                .getDao(new RigaAddebitoDichiarazioneIntentoArticolo()).creaRigaArticolo(parametri);

        rigaArticolo.setQta(1.0);
        rigaArticolo.setUnitaMisura(articolo.getUnitaMisura().getCodice());
        rigaArticolo.setQtaMagazzino(1.0);
        rigaArticolo.setUnitaMisuraQtaMagazzino(articolo.getUnitaMisura().getCodice());
        rigaArticolo.setAreaMagazzino(areaMagazzino);
        rigaArticolo.setRigaAutomatica(true);

        Importo importo = new Importo();
        importo.setImportoInValuta(addebitoDichiarazioneIntentoInVigore.getPrezzo());
        importo.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());

        rigaArticolo.setPrezzoUnitario(importo);

        return rigaArticolo;
    }

    @Override
    public List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino) {
        List<RigaMagazzino> righeGenerate = new ArrayList<>();

        try {
            areaMagazzino = panjeaDAO.load(AreaMagazzino.class, areaMagazzino.getId());
        } catch (Exception e) {
            logger.error("--> Errore durante il caricamento dell'area magazzino " + areaMagazzino.getId(), e);
            throw new RuntimeException("Errore durante il caricamento dell'area magazzino " + areaMagazzino.getId(), e);
        }

        org.hibernate.Session session = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
        session.enableFilter(MagazzinoSettings.ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE)
                .setParameter("paramDataRegistrazione", areaMagazzino.getDataRegistrazione());
        MagazzinoSettings magazzinoSettings = null;
        try {
            magazzinoSettings = magazzinoSettingsManager.caricaMagazzinoSettings();
        } finally {
            session.disableFilter(MagazzinoSettings.ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE);
        }

        if (magazzinoSettings == null || CollectionUtils.isEmpty(magazzinoSettings.getAddebitiDichiarazioneIntento())) {
            throw new ArticoloAddebitoDichiarazioneIntentoNonPresenteException();
        }

        // carico l'addebito in vigore
        AddebitoDichiarazioneIntentoSettings addebitoDichiarazioneIntentoInVigore = magazzinoSettings
                .getAddebitoDichiarazioneIntentoInVigore();

        // cancello le righe automatiche per l'addebito dichiarazione di intento o esco se è stata modificata
        List<RigaArticolo> caricaRigheAddebitoDichiarazioneIntento = caricaRigheAddebitoDichiarazioneIntento(
                areaMagazzino, addebitoDichiarazioneIntentoInVigore);
        for (RigaArticolo rigaArticolo : caricaRigheAddebitoDichiarazioneIntento) {
            if (rigaArticolo.isRigaAutomatica()) {
                rigaMagazzinoManager.getDao(rigaArticolo).cancellaRigaMagazzino(rigaArticolo);
            } else {
                return righeGenerate;
            }
        }

        RigaAddebitoDichiarazioneIntentoArticolo rigaAddebitoDichiarazioneIntento = creaRigaAddebitoDichiarazioneIntento(
                areaMagazzino, addebitoDichiarazioneIntentoInVigore);

        RigaMagazzino rigaArticoloGenerata;
        try {
            rigaArticoloGenerata = rigaMagazzinoManager.getDao().salvaRigaMagazzino(rigaAddebitoDichiarazioneIntento);
            righeGenerate.add(rigaArticoloGenerata);
        } catch (RimanenzaLottiNonValidaException e) {
            logger.error("--> Attenzione si sta cercando di salvare una riga trasporto con dei lotti", e);
            throw new IllegalArgumentException(e);
        } catch (RigheLottiNonValideException e) {
            logger.error("--> Attenzione si sta cercando di salvare una riga trasporto con dei lotti", e);
            throw new IllegalArgumentException(e);
        } catch (QtaLottiMaggioreException e) {
            logger.error("--> Attenzione si sta cercando di salvare una riga trasporto con dei lotti", e);
            throw new IllegalArgumentException(e);
        }

        return righeGenerate;
    }

}
