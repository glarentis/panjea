package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.OperatoreSenzaTipoDocumentoFattura;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Rifornimenti;

@Local
public interface FatturazioneImportazioneBuilder {

    /**
     * Importa la fattura legata al rifornimento
     *
     * @param numeroFattura
     *            numeroFattura
     * @param rifornimento
     *            rifornimento salvato
     * @param rifornimentoPalmare
     *            rifornimento con i dati dell'XML
     * @param importazioneXml
     *            XML deserializzato
     * @return
     * @throws OperatoreSenzaTipoDocumentoFattura
     *             rilanciata se un operatore non ha un tipo documenti fattura
     * @return {@link AreaMagazzino} fattura salvata
     * @throws DocumentoDuplicateException
     *             doc dup.
     */
    AreaMagazzino importaFattura(Integer numeroFattura, AreaRifornimento rifornimento, Rifornimenti rifornimentoPalmare,
            ImportazioneXml importazioneXml) throws OperatoreSenzaTipoDocumentoFattura, DocumentoDuplicateException;
}
