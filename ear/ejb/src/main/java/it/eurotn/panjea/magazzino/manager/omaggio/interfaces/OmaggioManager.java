package it.eurotn.panjea.magazzino.manager.omaggio.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.omaggio.RigaOmaggioArticolo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;

/**
 * @author leonardo
 */
@Local
public interface OmaggioManager {

    /**
     * Carica la lista di omaggi disponibili.
     * 
     * @return List<Omaggio>
     */
    List<Omaggio> caricaOmaggi();

    /**
     * Carica l'omaggio associato al tipo omaggio specificato.
     * 
     * @param tipoOmaggio
     *            il tipo omaggio di cui caricare l'omaggio
     * @return Omaggio
     */
    Omaggio caricaOmaggioByTipo(TipoOmaggio tipoOmaggio);

    /**
     * Carica la riga omaggio dell'area magazzino se presente, altrimenti null. Viene generata una sola riga omaggio per
     * documento.
     * 
     * @param areaMagazzino
     *            l'area magazzino di cui caricare la riga omaggio generata
     * @return la riga omaggio articolo generata
     */
    RigaOmaggioArticolo caricaRigaOmaggio(AreaMagazzino areaMagazzino);

    /**
     * Salva l'omaggio.
     * 
     * @param omaggio
     *            omaggio da salvare
     * @return l'maggio salvato
     */
    Omaggio salvaOmaggio(Omaggio omaggio);

}
