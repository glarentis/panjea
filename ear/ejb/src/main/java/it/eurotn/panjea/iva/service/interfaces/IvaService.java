/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eurotn.panjea.iva.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.service.exception.IvaException;
import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
@Remote
public interface IvaService {

    /**
     * metodo che cancella {@link RigaIva}.
     *
     * @param rigaIva
     *            riga iva da cancellare
     */
    void cancellaRigaIva(RigaIva rigaIva);

    /**
     * Carica l'area iva associata all'area contabile scelta.
     *
     * @param areaContabile
     *            l'area contabile di cui richiedere l'area iva associata
     * @return AreaIva associata all'area contabile
     */
    AreaIva caricaAreaIva(AreaContabile areaContabile);

    /**
     * Carica l'area iva con un'area iva esistente che puo' essere una area iva con valorizzato solo
     * l'id.
     *
     * @param areaIva
     *            l'area iva da caricare, l'id deve essere valorizzato
     * @return AreaIva
     */
    AreaIva caricaAreaIva(AreaIva areaIva);

    /**
     * metodo che carica {@link RigaIva}.
     *
     * @param id
     *            id della riga iva da caricare
     * @return riga iva caricata
     * @throws IvaException
     *             eccezione generica
     */
    RigaIva caricaRigaIva(Integer id) throws IvaException;

    /**
     * Esegue la invalidazione delle righe iva.
     *
     * @param areaIva
     *            l'area da invalidare
     * @return AreaIva l'area invalidata
     */
    AreaIva invalidaAreaIva(AreaIva areaIva);

    /**
     * Esegue la ricerca delle righe iva in base ai parametri.
     *
     * @param parametriRicercaRigheIva
     *            parametri ri ricerca
     * @return righe iva caricate
     */
    List<RigaIvaRicercaDTO> ricercaRigheIva(ParametriRicercaRigheIva parametriRicercaRigheIva);

    /**
     * Salva una area Iva senza eseguire controlli.
     *
     * @param areaIva
     *            l'area da salvare
     * @return AreaIva aggiornata
     */
    AreaIva salvaAreaIva(AreaIva areaIva);

    /**
     * metodo che salva {@link RigaIva}.<br>
     * l'attributo tipoAreaContabile serve per controllare la presenza dell'attributo
     * codiceIvaCollegato se la gestioneIva è GestioneIva.INTRA o GestioneIva.ART17.
     *
     * @param rigaIva
     *            la riga da salvare
     * @param tipoAreaContabile
     *            il tipoAreacontabile
     * @return RigaIva la riga iva salvata
     * @throws CodiceIvaCollegatoAssenteException
     *             se manca il codice iva collegato
     */
    RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException;

}
