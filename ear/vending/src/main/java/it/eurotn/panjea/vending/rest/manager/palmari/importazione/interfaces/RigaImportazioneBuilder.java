package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import java.util.Collection;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RifornimentiProdotti;

@Local
public interface RigaImportazioneBuilder {

    /**
     *
     * @param righeArticolo
     *            righe da convertire
     * @return lista riga import.
     */
    Collection<RigaImport> creaRigheImport(Collection<RifornimentiProdotti> righeArticolo);

}
