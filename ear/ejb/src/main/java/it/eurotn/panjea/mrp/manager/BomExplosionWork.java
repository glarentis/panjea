package it.eurotn.panjea.mrp.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import commonj.work.Work;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.manager.interfaces.MrpBomExplosionManager;

public class BomExplosionWork implements Work {

    private List<Object[]> adjacentListReadOnly;

    private int start;
    private int end;

    private final BomResult result;

    /**
     * BomExplosionWork.
     * 
     * @param adjacentListReadOnly
     *            lista con i collegamenti distinta componenti
     * @param start
     *            indice iniziale
     * @param end
     *            indice finale
     * @param result
     *            risultati dell'esplosione.
     */
    public BomExplosionWork(final List<Object[]> adjacentListReadOnly, final int start, final int end,
            final BomResult result) {
        super();
        this.adjacentListReadOnly = adjacentListReadOnly;
        this.start = start;
        this.end = end;
        this.result = result;
    }

    /**
     * Aggiunge padri.
     * 
     * @param codiciAttributoDaTrasmettere
     *            codiciAttributoDaTrasmettere
     * @param valoriAttributoDaTrasmettere
     *            valoriAttributoDaTrasmettere
     * @param numPadri
     *            numPadri
     * @param padriComponenti
     *            padriComponenti
     */
    private void addPadri(String codiciAttributoDaTrasmettere, String valoriAttributoDaTrasmettere, int numPadri,
            Bom[] padriComponenti) {
        for (int j = 0; j < numPadri; j++) {
            Bom padreComponente = padriComponenti[j];
            if (!codiciAttributoDaTrasmettere.isEmpty()) {
                String codiciAttributoPadre = padreComponente.getCodiciAttributi();
                String valoriAttributoPadre = padreComponente.getValoriAttributi();

                codiciAttributoPadre = StringUtils
                        .join(new String[] { codiciAttributoPadre, codiciAttributoDaTrasmettere }, ",");
                valoriAttributoPadre = StringUtils
                        .join(new String[] { valoriAttributoPadre, valoriAttributoDaTrasmettere }, ",");

                padreComponente.setCodiciAttributi(codiciAttributoPadre);
                padreComponente.setValoriAttributi(valoriAttributoPadre);
                padreComponente.setTrasmettiAttributi(true);
            }
            Bom bomFiglio = result.getBom(padreComponente.getIdArticolo(), padreComponente.getIdConfigurazione(), null,
                    null);
            bomFiglio.addPadre(padreComponente);
        }
    }

    // /**
    // * @return Mappa con le distinte esplose
    // */
    // public Map<ArticoloConfigurazioneKey, Bom> getBomExplose() {
    // return bomExplose;
    // }

    @Override
    public boolean isDaemon() {
        return false;
    }

    @Override
    public void release() {
    }

    @Override
    public void run() {
        Bom bomArticolo = new Bom(-1, null, null, null);
        Bom padreCorrente = null;
        Bom[] padriComponenti = new Bom[100];
        int numPadri = 0;
        String codiciAttributoDaTrasmettere = "";
        String valoriAttributoDaTrasmettere = "";

        try {
            for (int i = start; i < end; i++) {
                int idDistinta = (int) adjacentListReadOnly.get(i)[MrpBomExplosionManager.COLUMN_ID_DISTINTA];

                if ((padreCorrente != null && !padreCorrente.getIdDistinta().equals(idDistinta))) {
                    addPadri(codiciAttributoDaTrasmettere, valoriAttributoDaTrasmettere, numPadri, padriComponenti);
                    padriComponenti = new Bom[100];
                    numPadri = 0;
                }

                Integer idConfigurazione = (Integer) adjacentListReadOnly
                        .get(i)[MrpBomExplosionManager.COLUMN_ID_CONFIGURAZIONE];
                int idArticolo = (int) adjacentListReadOnly.get(i)[MrpBomExplosionManager.COLUMN_ID_ARTICOLO];
                String formula = (String) adjacentListReadOnly.get(i)[MrpBomExplosionManager.COLUMN_FORMULA];
                Integer idComponente = (Integer) adjacentListReadOnly.get(i)[MrpBomExplosionManager.COLUMN_ID];
                Integer idComponentePadre = (Integer) adjacentListReadOnly
                        .get(i)[MrpBomExplosionManager.COLUMN_ID_COMPONENTE_PADRE];
                String codiciAttributi = (String) adjacentListReadOnly
                        .get(i)[MrpBomExplosionManager.COLUMN_CODICI_ATTRIBUTI];
                String valoriAttributi = (String) adjacentListReadOnly
                        .get(i)[MrpBomExplosionManager.COLUMN_VALORI_ATTRIBUTI];
                Double qtaAttrezzaggioDistinta = (Double) adjacentListReadOnly
                        .get(i)[MrpBomExplosionManager.COLUMN_QTA_ATTREZZAGGIO_DISTINTA];
                Double qtaAttrezzaggioArticolo = (Double) adjacentListReadOnly
                        .get(i)[MrpBomExplosionManager.COLUMN_QTA_ATTREZZAGGIO_ARTICOLO];

                if (codiciAttributi.contains(Articolo.ATTRIBUTO_TRASMETTI_ATTRIBUTI)) {
                    codiciAttributoDaTrasmettere = StringUtils
                            .join(new String[] { codiciAttributoDaTrasmettere, codiciAttributi }, ",");
                    valoriAttributoDaTrasmettere = StringUtils
                            .join(new String[] { valoriAttributoDaTrasmettere, valoriAttributi }, ",");
                }

                // Se ho già la distinta non la cerco in mappa
                if (idDistinta != bomArticolo.getIdDistinta()) {
                    bomArticolo = result.getBom(idDistinta, idConfigurazione, qtaAttrezzaggioDistinta,
                            qtaAttrezzaggioArticolo);
                }
                Bom figlio = new Bom(idConfigurazione, idArticolo, formula, qtaAttrezzaggioDistinta,
                        qtaAttrezzaggioArticolo);
                bomArticolo.addFiglio(figlio);

                Bom padre = new Bom(idComponente, idDistinta, formula, qtaAttrezzaggioDistinta, qtaAttrezzaggioArticolo,
                        idArticolo, idComponentePadre, idConfigurazione);
                if (numPadri < 100) {
                    padriComponenti[numPadri] = padre;
                    numPadri++;
                }
                padreCorrente = padre;
            }
            addPadri(codiciAttributoDaTrasmettere, valoriAttributoDaTrasmettere, numPadri, padriComponenti);
        } catch (Exception e) {
            throw new RuntimeException("errore nell'esplodere le disinte", e);
        }
    }
}
