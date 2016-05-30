/**
 *
 */
package it.eurotn.panjea.intra.domain;

/**
 * Identifica il codice che individua il gruppo nel quale le condizioni di consegna tra le parti sono clasificate.<br>
 * <table>
 * <tr>
 * <td>GRUPPO</td>
 * <td>DESCRIZIONE</td>
 * <td>VALORE STATISTICO</td>
 * </tr>
 * <tr>
 * <td>E</td>
 * <td>Franco fabbrica</td>
 * <td>Valore della merce + spese di consegna dal luogo di partenza fino al confine italiano</td>
 * </tr>
 * <tr>
 * <td>F</td>
 * <td>Franco vettore/Franco lungo bordo/Franco a bordo</td>
 * <td>Valore della merce + spese di consegna dal luogo di presa in carico della merce fino al confine italiano</td>
 * </tr>
 * <tr>
 * <td>C</td>
 * <td>Costo e nolo/Costo, assicurazione, nolo/Trasporto pagato fino a…/Trasporto e assicurazione pagati fino a…</td>
 * <td>Valore della merce - spese di consegna dal confine italiano fino al luogo di destinazione convenuto</td>
 * </tr>
 * <tr>
 * <td>D</td>
 * <td>Reso frontiera/Reso ex ship/Reso banchina/Reso non sdoganato</td>
 * <td>Valore della merce - spese di consegna dal confine italiano fino al luogo di destinazione convenuto, tranne per
 * la clausola DAF per la quale il valore statistico rimane uguale all’ammontare dell’operazione ai fini fiscali</td>
 * </tr>
 * </table>
 * 
 * @author leonardo
 */
public enum GruppoCondizioneConsegna {

	E, F, C, D

}
