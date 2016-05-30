package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.entity.IEntityCodiceAzienda;
import it.eurotn.panjea.anagrafica.documenti.service.exception.CodiceIvaRicorsivoException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.manager.interfaces.CrudManager;

@Local
public interface CodiciIvaManager extends CrudManager<CodiceIva> {

    /**
     * Carica il codice iva partendo dal codice identificativo del software Europa.
     *
     * @param codiceEuropa
     *            il codice identificativo del software Europa
     * @return CodiceIva
     * @throws ObjectNotFoundException
     *             oggetto non trovato
     */
    CodiceIva caricaCodiceIva(String codiceEuropa) throws ObjectNotFoundException;

    /**
     * carica una lista di codiciIva.
     *
     * @param codice
     *            codice o parte del codice da ricercare
     * @return List<CodiceIva>
     * @throws ContabilitaException
     */
    List<CodiceIva> caricaCodiciIva(String codice);

    /**
     *
     * @return codici Iva abilitati per palmare. La chiave è la % di applicazione
     */
    Map<Double, CodiceIva> caricaCodiciIvaPalmare();

    /**
     * Salva l'oggetto passato come parametro. Se l'oggetto implementa {@link IEntityCodiceAzienda}
     * viene avvalorata la proprietà relativa al codice azienda.
     *
     * @param codiceIva
     *            oggetto da salvare
     * @return oggetto salvato
     * @throws CodiceIvaRicorsivoException
     *             codici iva recorsivo
     */
    CodiceIva salvaCodiceIva(CodiceIva codiceIva) throws CodiceIvaRicorsivoException;

}