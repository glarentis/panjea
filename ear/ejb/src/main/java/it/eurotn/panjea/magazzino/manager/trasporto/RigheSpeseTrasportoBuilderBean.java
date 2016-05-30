package it.eurotn.panjea.magazzino.manager.trasporto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.DescrizionePoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloGenerata;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.domain.trasporto.RigaSpeseTrasportoArticolo;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoAnagraficaManager;
import it.eurotn.panjea.magazzino.manager.trasporto.interfaces.RigheSpeseTrasportoBuilder;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author leonardo
 *
 */
@Stateless(name = "Panjea.RigheSpeseTrasportoBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheSpeseTrasportoBuilder")
public class RigheSpeseTrasportoBuilderBean implements RigheSpeseTrasportoBuilder {

    private static Logger logger = Logger.getLogger(RigheSpeseTrasportoBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private MagazzinoAnagraficaManager magazzinoAnagraficaManager;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    /**
     * Non esistono le condizioni necessarie alla generazione della riga di spese trasporto se non è impostata una delle
     * proprietà seguenti.<br>
     * <ul>
     * <li>Trasporto a cura mittente(flag)</li>
     * <li>Richiesta tipo mezzo trasporto su tipo area magazzino, articolo</li>
     * <li>Trasporto su tipo mezzo con origine prezzo tipo mezzo</li>
     * </ul>
     *
     * @param areaMagazzino
     *            area magazzino da verificare
     * @param trasportoCura
     *            trasporto a cura da verificare
     * @return true se le condizioni sono sufficienti, false altrimenti
     */
    private boolean checkConditions(AreaMagazzino areaMagazzino, TrasportoCura trasportoCura) {
        if (areaMagazzino.getMezzoTrasporto() == null
                || !areaMagazzino.getTipoAreaMagazzino().isRichiestaMezzoTrasporto() || trasportoCura == null
                || !trasportoCura.isMittente()
                || areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getArticolo() == null) {

            logger.warn("--> Non ci sono le condizioni per la generazione della riga spese trasporto");
            return false;
        }
        return true;
    }

    /**
     * Genera la riga per le spese trasporto.
     *
     * @param areaMagazzino
     *            l'area magazzino
     * @param qtaTotale
     *            la qta da associare
     * @param unitaMisura
     *            'unità di misura
     * @return RigaSpeseTrasportoArticolo
     */
    private RigaSpeseTrasportoArticolo creaRigaSpeseTrasporto(AreaMagazzino areaMagazzino, double qtaTotale,
            String unitaMisura) {
        ArticoloLite articoloTrasporto = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getArticolo();
        Integer idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
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
        parametri.setIdArticolo(articoloTrasporto.getId());
        parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
        parametri.setIdEntita(idEntita);
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setIdListinoAlternativo(idListinoAlternativo);
        parametri.setIdListino(idListino);
        parametri.setImporto(areaMagazzino.getDocumento().getTotale().clone());
        parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
        parametri.setIdTipoMezzo(idTipoMezzo);
        parametri.setIdZonaGeografica(areaMagazzino.getIdZonaGeografica());
        parametri.setProvenienzaPrezzoArticolo(articoloTrasporto.getProvenienzaPrezzoArticolo());
        parametri.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());
        parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
        parametri.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
        parametri.setCodiceLingua(codiceLingua);
        parametri.setTipologiaCodiceIvaAlternativo(areaMagazzino.getTipologiaCodiceIvaAlternativo());
        parametri.setIdAgente(null);
        parametri.setPercentualeScontoCommerciale(null);
        parametri.setStrategiaTotalizzazioneDocumento(
                areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

        RigaSpeseTrasportoArticolo rigaArticolo = (RigaSpeseTrasportoArticolo) rigaMagazzinoManager
                .getDao(new RigaSpeseTrasportoArticolo()).creaRigaArticolo(parametri);
        rigaArticolo.setQta(qtaTotale);
        rigaArticolo.setUnitaMisura(unitaMisura);
        rigaArticolo.setQtaMagazzino(qtaTotale);
        rigaArticolo.setUnitaMisuraQtaMagazzino(unitaMisura);
        rigaArticolo.setAreaMagazzino(areaMagazzino);
        rigaArticolo.setRigaAutomatica(true);

        PoliticaPrezzo politicaPrezzo = rigaArticolo.getPoliticaPrezzo();
        if (politicaPrezzo != null) {

            StringBuffer bufferDescCalcolo = new StringBuffer();

            if (!politicaPrezzo.getPrezzi().isEmpty()) {
                RisultatoPrezzo<BigDecimal> sezionePrezzo = politicaPrezzo.getPrezzi()
                        .getRisultatoPrezzo(rigaArticolo.getQta());
                // posso non avere la combinazione settata per la qta.
                if (sezionePrezzo != null) {
                    BigDecimal prezzoUnitarioSelezionato = sezionePrezzo.getValue();
                    Integer numeroDecimali = sezionePrezzo.getNumeroDecimali();
                    bufferDescCalcolo.append(sezionePrezzo.toString());
                    Importo prezzoUnitarioReale = (rigaArticolo.getPrezzoUnitario()).clone();
                    prezzoUnitarioReale.setImportoInValuta(prezzoUnitarioSelezionato);
                    prezzoUnitarioReale.calcolaImportoValutaAzienda(numeroDecimali);
                    rigaArticolo.initPrezzoUnitarioReale(prezzoUnitarioReale);
                    rigaArticolo
                            .setPrezzoDeterminato(rigaArticolo.getPrezzoUnitario().clone().getImportoInValutaAzienda());
                    rigaArticolo.setNumeroDecimaliPrezzo(numeroDecimali);

                    if (bufferDescCalcolo.length() > 0) {
                        bufferDescCalcolo.append("#");
                    }
                }
            }

            if (!politicaPrezzo.getSconti().isEmpty()) {
                // aggiungo le variazioni
                RisultatoPrezzo<Sconto> sezioneSconto = politicaPrezzo.getSconti()
                        .getRisultatoPrezzo(rigaArticolo.getQta());

                if (sezioneSconto != null && sezioneSconto.getValue() != null) {
                    Sconto sconto = sezioneSconto.getValue();
                    try {
                        if (sconto.getSconto1() != null) {
                            rigaArticolo.setVariazione1(sconto.getSconto1());
                        }
                        if (sconto.getSconto2() != null) {
                            rigaArticolo.setVariazione2(sconto.getSconto2());
                        }
                        if (sconto.getSconto3() != null) {
                            rigaArticolo.setVariazione3(sconto.getSconto3());
                        }
                        if (sconto.getSconto4() != null) {
                            rigaArticolo.setVariazione4(sconto.getSconto4());
                        }
                    } catch (Exception e) {
                        logger.error("--> SCONTO: ", e);
                    }
                    if (bufferDescCalcolo.length() > 0) {
                        bufferDescCalcolo.append("#");
                    }
                    bufferDescCalcolo.append(sezioneSconto.toString());
                }
            }
            rigaArticolo.setDescrizionePoliticaPrezzo(new DescrizionePoliticaPrezzo());
            rigaArticolo.getDescrizionePoliticaPrezzo().setDescrizioneCalcolo(bufferDescCalcolo.toString());
        }
        return rigaArticolo;
    }

    @Override
    public List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino) {
        List<RigaMagazzino> righeGenerate = new ArrayList<>();

        TrasportoCura trasportoCura = magazzinoAnagraficaManager
                .caricaTrasportoCuraByDescrizione(areaMagazzino.getTrasportoCura());

        boolean iniziaGenerazioneRiga = checkConditions(areaMagazzino, trasportoCura);

        if (iniziaGenerazioneRiga) {

            // carico le righe magazzino per avere la somma delle quantità
            List<RigaArticolo> righeArticolo = rigaMagazzinoManager.getDao().caricaRigheArticolo(areaMagazzino);

            double qtaTotale = 0.0;
            String unitaMisura = null;
            for (RigaArticolo rigaArticolo : righeArticolo) {
                ArticoloLite articoloTipoMezzoTrasporto = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto()
                        .getArticolo();
                ArticoloLite articoloRiga = rigaArticolo.getArticolo();

                if (!articoloRiga.equals(articoloTipoMezzoTrasporto)) {
                    qtaTotale = qtaTotale + rigaArticolo.getQtaMagazzino();
                    unitaMisura = rigaArticolo.getUnitaMisuraQtaMagazzino();
                } else {
                    // se trovo già la riga con articolo spese trasporto e non è automatica esco altrimenti la cancello
                    // e vado avanti
                    if (rigaArticolo.isRigaAutomatica()) {
                        rigaMagazzinoManager.getDao(rigaArticolo).cancellaRigaMagazzino(rigaArticolo);
                    } else {
                        return righeGenerate;
                    }
                }
            }

            RigaSpeseTrasportoArticolo rigaSpeseTrasportoArticolo = creaRigaSpeseTrasporto(areaMagazzino, qtaTotale,
                    unitaMisura);

            RigaArticoloGenerata rigaArticoloGenerata;
            try {
                rigaArticoloGenerata = (RigaArticoloGenerata) rigaMagazzinoManager.getDao()
                        .salvaRigaMagazzino(rigaSpeseTrasportoArticolo);
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
        }

        return righeGenerate;
    }
}
