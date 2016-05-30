package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;

@Local
public interface ImportazioneEvaDtsManager {
    /**
     * Importa le letture evadts e le lega al rifornimento
     * 
     * @param rifornimento
     *            rifornimento con le letture
     * @param importazioneXml
     *            dati xml
     * @param progressivoRifornimento
     *            progressivo del rifornimento
     * @return true se importate
     */
    boolean importaEvaDts(AreaRifornimento rifornimento, ImportazioneXml importazioneXml, int progressivoRifornimento);
}
