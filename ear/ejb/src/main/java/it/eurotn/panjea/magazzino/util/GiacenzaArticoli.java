package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;

/**
 * @author Leonardo
 */
public class GiacenzaArticoli implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(GiacenzaArticoli.class);

    private static final long serialVersionUID = -6616220845344323545L;
    private final Map<Integer, GiacenzaArticolo> giacenzeArticoli = new HashMap<Integer, GiacenzaArticolo>();

    /**
     * @return lista di giacenzaArticolo, un elemento per ogni articolo movimentato
     */
    public List<GiacenzaArticolo> asList() {
        List<GiacenzaArticolo> result = new ArrayList<GiacenzaArticolo>();
        result.addAll(giacenzeArticoli.values());
        return result;
    }

    /**
     * @return giacenze articolo
     */
    public Set<Entry<Integer, GiacenzaArticolo>> entrySet() {
        return giacenzeArticoli.entrySet();
    }

    /**
     * @param saldoArticolo
     *            giacenza articolo
     * @return giacenza articolo
     */
    public GiacenzaArticolo get(GiacenzaArticolo saldoArticolo) {
        return get(saldoArticolo.getArticoloLite().getId());
    }

    /**
     * @param idRigaArticolo
     *            riga articolo
     * @return giacenza articolo
     */
    public GiacenzaArticolo get(Integer idRigaArticolo) {
        return giacenzeArticoli.get(idRigaArticolo);
    }

    /**
     * @param giacenzaArticolo
     *            giacenza articolo da aggiungere
     */
    public void put(GiacenzaArticolo giacenzaArticolo) {
        giacenzeArticoli.put(giacenzaArticolo.getArticoloLite().getId(), giacenzaArticolo);
    }

    /**
     * Somma la lista delle giacenze passate come parametro.<br/>
     * 
     * @param paramGiacenzaArticoli
     *            lista delle giacenze da sommare
     */
    public void sommaGiacenzaArticolo(Collection<GiacenzaArticolo> paramGiacenzaArticoli) {
        // se nella lista ho dei conti uguali sommo i loro valori.
        for (GiacenzaArticolo giacenzaArticolo : paramGiacenzaArticoli) {
            if (giacenzeArticoli.containsKey(giacenzaArticolo.getArticoloLite().getId())) {
                GiacenzaArticolo giacenzaArticoloPresente = giacenzeArticoli
                        .get(giacenzaArticolo.getArticoloLite().getId());
                giacenzaArticoloPresente.setCodiceCategoria(giacenzaArticolo.getCategoriaLite().getCodice());
                giacenzaArticoloPresente.setDescrizioneCategoria(giacenzaArticolo.getCategoriaLite().getDescrizione());
                giacenzaArticoloPresente.setCodiceDeposito(giacenzaArticolo.getDepositoLite().getCodice());
                giacenzaArticoloPresente.setDescrizioneDeposito(giacenzaArticolo.getDepositoLite().getDescrizione());
                giacenzaArticoloPresente
                        .aggiungiQtaMagazzinoCaricoTotale(giacenzaArticolo.getQtaMagazzinoCaricoTotale());
                giacenzaArticoloPresente
                        .aggiungiQtaMagazzinoScaricoTotale(giacenzaArticolo.getQtaMagazzinoScaricoTotale());
                giacenzaArticoloPresente.aggiungiQtaMagazzinoCarico(giacenzaArticolo.getQtaMagazzinoCarico());
                giacenzaArticoloPresente.aggiungiQtaMagazzinoScarico(giacenzaArticolo.getQtaMagazzinoScarico());
                giacenzaArticoloPresente.aggiungiQtaMagazzinoCaricoAltro(giacenzaArticolo.getQtaMagazzinoCaricoAltro());
                giacenzaArticoloPresente
                        .aggiungiQtaMagazzinoScaricoAltro(giacenzaArticolo.getQtaMagazzinoScaricoAltro());
                giacenzaArticolo = giacenzaArticoloPresente;
            }
            giacenzeArticoli.put(giacenzaArticolo.getArticoloLite().getId(), giacenzaArticolo);
        }
    }

    /**
     * Per ogni {@link RigaMagazzino} crea {@link GiacenzaArticolo} e li somma a quelli presenti.<br/>
     * 
     * @param righeMagazzino
     *            righe magazzino da sommare ai saldi
     */
    public void sommaRigheInventario(List<RigaMagazzino> righeMagazzino) {
        LOGGER.debug("--> Enter sommaRigheMagazzino");
        // se nella lista ho degli articoli uguali sommo i loro valori.
        for (RigaMagazzino rigaMagazzino : righeMagazzino) {
            try {
                if (rigaMagazzino instanceof RigaArticolo) {
                    RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzino;
                    if (!giacenzeArticoli.containsKey(rigaArticolo.getArticolo().getId())) {
                        GiacenzaArticolo giacenzaArticolo = new GiacenzaArticolo(rigaArticolo);
                        giacenzeArticoli.put(rigaArticolo.getArticolo().getId(), giacenzaArticolo);
                    }
                    GiacenzaArticolo giacenzaArticoloPresente = giacenzeArticoli
                            .get(rigaArticolo.getArticolo().getId());
                    giacenzaArticoloPresente.aggiungiQtaInventario(rigaArticolo.getQtaMagazzino());
                    giacenzaArticoloPresente.aggiungiQtaMagazzinoCaricoTotale(rigaArticolo.getQtaMagazzino());
                    giacenzaArticoloPresente.aggiungiQtaMagazzinoScaricoTotale(0.0);

                } else {
                    throw new UnsupportedOperationException("Errore non e' riga articolo!");
                }
            } catch (Exception ex) {
                LOGGER.error("--> errore ", ex);
            }
        }
        LOGGER.debug("--> Exit sommaRigheMagazzino");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GiacenzaArticoli [giacenzeArticoli=");
        builder.append(giacenzeArticoli);
        builder.append("]");
        return builder.toString();
    }
}
