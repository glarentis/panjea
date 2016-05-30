package it.eurotn.panjea.magazzino.manager.trasporto.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.IGeneratoreRigheArticolo;

/**
 * Genero la riga di spese trasporto, una sola per documento;<br>
 * la creazione è vincolata da alcune proprietà del documento:
 * <ul>
 * <li>Richiesta del mezzo di trasporto sul tipo area magazzino</li>
 * <li>Trasporto a cura di un mittente per il documento (flag mittente abilitato su anagrafica 'trasporti a cura')</li>
 * <li>Zona geografica impostata sulla sede dell'entità del documento</li>
 * </ul>
 * Inoltre per impostare correttamente il prezzo della riga di spesa del trasporto, nel listino tipi mezzo zone
 * geografiche deve essere impostata la giusta combinazione delle due variabili.
 *
 * @author leonardo
 */
@Local
public interface RigheSpeseTrasportoBuilder extends IGeneratoreRigheArticolo {

}
