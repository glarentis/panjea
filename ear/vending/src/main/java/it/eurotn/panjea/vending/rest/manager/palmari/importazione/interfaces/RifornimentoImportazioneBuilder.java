package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.DocumentoFatturatoNonPrevistoException;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Rifornimenti;

@Local
public interface RifornimentoImportazioneBuilder {
    /**
     * @param importazioneXml
     *            dati xml
     * @param codiceOperatore
     *            codice operatore
     * @param rifornimentoXML
     *            rifornimento in xml da importare
     * @return rifornimento importato
     * @throws DocumentoFatturatoNonPrevistoException
     *             rilanciata se il tipo doc su installazione non ha una documentodifatturazione ed
     *             è presente una fattura sul palmare.
     * @throws ImportazioneException
     *             exception generica
     */
    AreaRifornimento importa(ImportazioneXml importazioneXml, String codiceOperatore, Rifornimenti rifornimentoXML)
            throws DocumentoFatturatoNonPrevistoException, ImportazioneException;
}
