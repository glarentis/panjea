package it.eurotn.panjea.ordini.manager.documento.righeinserimento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.FormuleRigaArticoloCalculator;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloManager;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.RigaOrdineManager;
import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.AttributiInserimentoLoader;
import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.RigheInserimentoLoader;
import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.RigheInserimentoManager;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento.TipoRicerca;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;

@Stateless(name = "Panjea.RigheInserimentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheInserimentoManager")
public class RigheInserimentoManagerBean implements RigheInserimentoManager {

    private static final Logger LOGGER = Logger.getLogger(RigheInserimentoManagerBean.class);

    @EJB(beanName = "RigheInserimentoAnalisiLoaderBean")
    private RigheInserimentoLoader righeInserimentoAnalisiLoader;

    @EJB(beanName = "RigheInserimentoAssortimentoLoaderBean")
    private RigheInserimentoLoader righeInserimentoAssortimentoLoader;

    @EJB(beanName = "RigheInserimentoUltimiOrdiniLoaderBean")
    private RigheInserimentoLoader righeInserimentoUltimiOrdiniLoader;

    @EJB(beanName = "RigheInserimentoOrdineLoaderBean")
    private RigheInserimentoLoader righeInserimentoOrdineLoader;

    @EJB(beanName = "AttributiArticoloInserimentoLoaderBean")
    private AttributiInserimentoLoader attributiArticoloInserimentoLoader;

    @EJB(beanName = "AttributiRigaOrdineInserimentoLoaderBean")
    private AttributiInserimentoLoader attributiRigaOrdineInserimentoLoader;

    @EJB
    private RigaOrdineManager rigaOrdineManager;

    @EJB
    private ArticoloManager articoloManager;

    @Override
    public RigheOrdineInserimento caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri) {

        List<RigaOrdineInserimento> righe = getLoaders().get(parametri.getTipoRicerca())
                .caricaRigheOrdineInserimento(parametri);

        Set<String> attributiPresenti = new TreeSet<String>();
        List<RigaOrdineInserimento> righeConAttributi = new ArrayList<>();

        for (RigaOrdineInserimento rigaOrdineInserimento : righe) {
            RigaOrdineInserimento rigaConAttributi = getAttributiLoader(rigaOrdineInserimento)
                    .fillAttributi(rigaOrdineInserimento);
            righeConAttributi.add(rigaConAttributi);

            attributiPresenti.addAll(rigaConAttributi.getAttributi().keySet());
        }

        RigheOrdineInserimento righeInserimento = new RigheOrdineInserimento();
        righeInserimento.setRighe(righeConAttributi);
        righeInserimento.setAttributiPresenti(attributiPresenti);

        return righeInserimento;
    }

    /**
     * Crea la riga articolo ordine in base alla riga inserimento.
     *
     * @param rigaInserimento
     *            riga inserimento
     * @param areaOrdine
     *            area ordine
     * @return riga creata
     */
    private RigaArticolo creaRigaArticolo(RigaOrdineInserimento rigaInserimento, AreaOrdine areaOrdine) {

        Articolo articolo = articoloManager.caricaArticolo(rigaInserimento.getArticolo().creaProxyArticolo(), true);
        Documento documento = areaOrdine.getDocumento();

        Integer idSedeEntita = null;
        String codiceLingua = null;
        if (documento.getSedeEntita() != null) {
            idSedeEntita = areaOrdine.getDocumento().getSedeEntita().getId();
            codiceLingua = areaOrdine.getDocumento().getSedeEntita().getLingua();
        }

        Integer idAgente = areaOrdine.getAgente() != null ? areaOrdine.getAgente().getId() : null;

        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setProvenienzaPrezzo(ProvenienzaPrezzo.LISTINO);
        parametri.setIdArticolo(articolo.getId());
        parametri.setData(areaOrdine.getDocumento().getDataDocumento());
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setIdListinoAlternativo(
                areaOrdine.getListinoAlternativo() != null ? areaOrdine.getListinoAlternativo().getId() : null);
        parametri.setIdListino(areaOrdine.getListino() != null ? areaOrdine.getListino().getId() : null);
        parametri.setImporto(areaOrdine.getDocumento().getTotale().clone());
        parametri.setCodiceIvaAlternativo(areaOrdine.getCodiceIvaAlternativo());
        parametri.setIdTipoMezzo(null);
        parametri.setIdZonaGeografica(null);
        parametri.setProvenienzaPrezzoArticolo(articolo.getProvenienzaPrezzoArticolo());
        parametri.setNoteSuDestinazione(areaOrdine.getTipoAreaOrdine().isNoteSuDestinazione());
        parametri.setTipoMovimento(TipoMovimento.NESSUNO);
        parametri.setGestioneArticoloDistinta(areaOrdine.getTipoAreaOrdine().isOrdineProduzione());
        parametri.setCodiceValuta(areaOrdine.getDocumento().getTotale().getCodiceValuta());
        parametri.setCodiceLingua(codiceLingua);
        parametri.setIdAgente(idAgente);
        parametri.setTipologiaCodiceIvaAlternativo(areaOrdine.getTipologiaCodiceIvaAlternativo());
        parametri.setPercentualeScontoCommerciale(null);
        parametri.setIdDeposito(areaOrdine.getDepositoOrigine().getId());
        parametri.setCalcolaGiacenza(false);

        RigaArticolo rigaArticolo = null;
        try {
            rigaArticolo = rigaOrdineManager.creaRigaArticolo(parametri);
            rigaArticolo.setDataConsegna(areaOrdine.getDataConsegna());
        } catch (Exception e) {
            LOGGER.error("--> errore durante la creazione della riga articolo.", e);
            throw new GenericException("Errore durante la creazione della riga articolo " + articolo.getDescrizione());
        }
        return rigaArticolo;
    }

    @Override
    public void generaRigheOrdine(List<RigaOrdineInserimento> righeInserimento, AreaOrdine areaOrdine) {

        FormuleRigaArticoloCalculator formuleCalculator = new FormuleRigaArticoloCalculator();

        for (RigaOrdineInserimento rigaInserimento : righeInserimento) {

            // creo la riga articolo dell'ordine
            RigaArticolo rigaArticolo = creaRigaArticolo(rigaInserimento, areaOrdine);
            rigaArticolo.setAreaOrdine(areaOrdine);
            rigaArticolo.setQta(rigaInserimento.getQtaInserimento());

            // avvaloro gli attributi in base a quelli della riga inserimento
            for (AttributoRiga attributo : rigaArticolo.getAttributi()) {
                AttributoRigaArticolo attributoInserimento = rigaInserimento.getAttributiInserimento()
                        .get(attributo.getTipoAttributo().getNome());
                if (attributoInserimento != null) {
                    attributo.setValore(attributoInserimento.getValore());
                }
            }

            // calcolo tutte le formule
            try {
                rigaArticolo = (RigaArticolo) formuleCalculator.calcola(rigaArticolo);
            } catch (FormuleTipoAttributoException e) {
                throw new RuntimeException(e);
            }

            rigaOrdineManager.getDao(rigaArticolo).salvaRigaOrdine(rigaArticolo);
        }

    }

    private AttributiInserimentoLoader getAttributiLoader(RigaOrdineInserimento riga) {
        if (!StringUtils.isBlank(riga.getIdRigheOrdine())) {
            return attributiRigaOrdineInserimentoLoader;
        }

        return attributiArticoloInserimentoLoader;
    }

    private Map<TipoRicerca, RigheInserimentoLoader> getLoaders() {

        Map<TipoRicerca, RigheInserimentoLoader> loaders = new HashMap<TipoRicerca, RigheInserimentoLoader>();
        loaders.put(TipoRicerca.ANALISI, righeInserimentoAnalisiLoader);
        loaders.put(TipoRicerca.ASSORTIMENTO, righeInserimentoAssortimentoLoader);
        loaders.put(TipoRicerca.ULTIMI_ORDINI, righeInserimentoUltimiOrdiniLoader);
        loaders.put(TipoRicerca.ORDINE, righeInserimentoOrdineLoader);

        return loaders;
    }
}
