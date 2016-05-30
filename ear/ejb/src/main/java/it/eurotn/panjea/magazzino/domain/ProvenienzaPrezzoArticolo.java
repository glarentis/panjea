package it.eurotn.panjea.magazzino.domain;

/**
 * Definisce per l'articolo la provenienza del calcolo del prezzo.<br/>
 * I valori disponibili sono:<br/>
 * <ul>
 * <li>DOCUMENTO: valore di default, calcola il prezzo sulla base dei parametri del documento, considera quindi listini
 * e contratti</li>
 * <li>TIPOMEZZOZONAGEOGRAFICA: calcola il prezzo sulla base di tipo mezzo e zona geografica</li>
 * </ul>
 *
 * @author Leonardo
 */
public enum ProvenienzaPrezzoArticolo {

    DOCUMENTO, TIPOMEZZOZONAGEOGRAFICA
}
