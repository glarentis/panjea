package it.eurotn.panjea.magazzino.manager.omaggio.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.IGeneratoreRigheArticolo;

/**
 * Crea la riga omaggio se una riga articolo dell'area magazzino scelta ha impostato il tipoOmaggio ALTRO_OMAGGIO.<br>
 * La riga prende l'articolo che ha codice iva uguale al codice iva impostato per l'Omaggio di tipo equivalente (il
 * primo se è definito lo stesso codice iva per più articoli) e il relativo codice iva.
 *
 * @author leonardo
 */
@Local
public interface RigheOmaggioBuilder extends IGeneratoreRigheArticolo {

}
