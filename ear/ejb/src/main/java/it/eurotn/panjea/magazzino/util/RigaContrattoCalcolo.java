package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import it.eurotn.panjea.magazzino.domain.RigaContratto.Azione;
import it.eurotn.panjea.magazzino.domain.RigaContrattoStrategiaPrezzo.TipoValore;

/**
 * Contiene le informazioni per una riga di contratto per il calcolo del prezzo.<br/>
 * DTO utilizzato per caricare meno dati possibili.<br/>
 * Il calcolo deve essere il più veloce possibile, quindi mantengo solamente dati primitivi. <br/>
 * Il peso viene calcolato in base al collegamento come in tabella<br/>
 *
 * <table border="1">
 * <tr>
 * <td>Link articolo</td>
 * <td>Link cliente</td>
 * <td>Peso</td>
 * </tr>
 * <tr>
 * <td>Tutti</td>
 * <td>Qualsiasi cliente (anche clienti "anonimi")</td>
 * <td>0</td>
 * </tr>
 * <tr>
 * <td>Tutti</td>
 * <td>Tutti</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>Tutti</td>
 * <td>Categoria</td>
 * <td>2</td>
 * </tr>
 * <tr>
 * <td>Tutti</td>
 * <td>Cliente</td>
 * <td>3</td>
 * </tr>
 * <tr>
 * <td>Tutti</td>
 * <td>Sede</td>
 * <td>4</td>
 * </tr>
 * <tr>
 * <td>Categoria</td>
 * <td>Qualsiasi cliente (anche clienti "anonimi")</td>
 * <td>5</td>
 * </tr>
 * <tr>
 * <td>Categoria</td>
 * <td>Tutti</td>
 * <td>6</td>
 * </tr>
 * <tr>
 * <td>Categoria</td>
 * <td>Categoria</td>
 * <td>7</td>
 * </tr>
 * <tr>
 * <td>Categoria</td>
 * <td>Cliente</td>
 * <td>8</td>
 * </tr>
 * <tr>
 * <td>Categoria</td>
 * <td>Sede</td>
 * <td>9</td>
 * </tr>
 * <tr>
 * <td>Categoria2</td>
 * <td>Qualsiasi cliente (anche clienti "anonimi")</td>
 * <td>10</td>
 * </tr>
 * <tr>
 * <td>Categoria2</td>
 * <td>Tutti</td>
 * <td>11</td>
 * </tr>
 * <tr>
 * <td>Categoria2</td>
 * <td>Categoria</td>
 * <td>12</td>
 * </tr>
 * <tr>
 * <td>Categoria2</td>
 * <td>Cliente</td>
 * <td>13</td>
 * </tr>
 * <tr>
 * <td>Categoria2</td>
 * <td>Sede</td>
 * <td>14</td>
 * </tr>
 * <tr>
 * <td>Articolo</td>
 * <td>Qualsiasi cliente (anche clienti "anonimi")</td>
 * <td>15</td>
 * </tr>
 * <tr>
 * <td>Articolo</td>
 * <td>Tutti</td>
 * <td>16</td>
 * </tr>
 * <tr>
 * <td>Articolo</td>
 * <td>Categoria</td>
 * <td>17</td>
 * </tr>
 * <tr>
 * <td>Articolo</td>
 * <td>Cliente</td>
 * <td>18</td>
 * </tr>
 * <tr>
 * <td>Articolo</td>
 * <td>Sede</td>
 * <td>19</td>
 * </tr>
 * </table>
 * <br/>
 * A parità di peso si controlla la data dei contratti, <br/>
 * Peso minore al contratto con data di inizio meno recente,<br/>
 * A parità di data inizio peso minore al contratto con periodo più lungo.<br/>
 * <b>Il calcolo viene eseguito dalla riga con peso minore a quella con peso maggiore</b>
 *
 * @author giangi
 */
public class RigaContrattoCalcolo implements Serializable, Comparable<RigaContrattoCalcolo> {

    public static class RigaContrattoBaseComparator implements Comparator<RigaContrattoCalcolo> {

        @Override
        public int compare(RigaContrattoCalcolo o1, RigaContrattoCalcolo o2) {
            int compareResult = compareQta(o1, o2);
            if (compareResult == 0) {
                if (o1.getPeso().compareTo(o2.getPeso()) == 0) {
                    if (o1.getDataInizioContratto().compareTo(o2.getDataInizioContratto()) == 0) {
                        return o1.getDataFineContratto().compareTo(o2.getDataFineContratto());
                    } else {
                        return o1.getDataInizioContratto().compareTo(o2.getDataInizioContratto());
                    }
                } else {
                    return o1.getPeso().compareTo(o2.getPeso());
                }
            }
            return compareResult;
        }

        /**
         * Compare per qta soglia prezzo.
         * 
         * @param o1
         *            rigaContrattoCalcolo 1
         * @param o2
         *            rigaContrattoCalcolo 2
         * @return 0, 1, -1
         */
        protected int compareQta(RigaContrattoCalcolo o1, RigaContrattoCalcolo o2) {
            return new Double(o1.getQuantitaSogliaPrezzo()).compareTo(o2.getQuantitaSogliaPrezzo());
        }
    }

    public static class RigaContrattoQtaSogliaScontoComparator extends RigaContrattoBaseComparator {

        @Override
        protected int compareQta(RigaContrattoCalcolo o1, RigaContrattoCalcolo o2) {
            return new Double(o1.getQuantitaSogliaSconto()).compareTo(o2.getQuantitaSogliaSconto());
        }

    }

    private static final long serialVersionUID = 4223169123759609236L;
    private static RigaContrattoBaseComparator rigaContrattoBaseComparator = new RigaContrattoBaseComparator();
    private static RigaContrattoQtaSogliaScontoComparator rigaContrattoQtaSogliaScontoComparator = new RigaContrattoQtaSogliaScontoComparator();

    private boolean tutteLeCategorieSediMagazzino;

    private Integer idSedeMagazzino;

    private int idContratto;

    private String codiceContratto;

    private String descrizioneContratto;

    private Integer idCategoriaSede;

    private Integer idEntita;

    private int id;

    private Integer idArticolo;

    private Integer idCategoriaCommercialeArticolo;

    private Integer idCategoriaCommercialeArticolo2;

    private boolean tutteLeCategorieArticolo;

    private Integer azionePrezzo;

    private boolean bloccoPrezzo;

    private boolean ignoraBloccoPrezzoPrecedente;

    private double quantitaSogliaPrezzo;

    private BigDecimal valorePrezzo;

    private Integer azioneSconto;

    private boolean bloccoSconto;

    private Integer tipoValorePrezzo;

    private double quantitaSogliaSconto;

    private boolean ignoraBloccoScontoPrecedente;

    private Integer numeroDecimaliPrezzo;

    private Integer numeroDecimaliPrezzoArticolo;

    private BigDecimal sconto1;

    private BigDecimal sconto2;

    private BigDecimal sconto3;

    private BigDecimal sconto4;

    private boolean strategiaPrezzoAbilitata;

    private boolean strategiaScontoAbilitata;

    private Date dataInizioContratto;

    private Date dataFineContratto;

    private Integer idAgente;

    private BigDecimal provvigineAgente;

    private Integer azioneProvvigione;

    private boolean bloccoProvvigione;

    private boolean ignoraBloccoProvvigionePrecedente;

    /**
     * @return rigaContrattoQtaSogliaScontoComparator
     */
    public static RigaContrattoQtaSogliaScontoComparator getRigaContrattoQtaSogliaScontoComparator() {
        return rigaContrattoQtaSogliaScontoComparator;
    }

    @Override
    public int compareTo(RigaContrattoCalcolo valueToCompare) {
        return rigaContrattoBaseComparator.compare(this, valueToCompare);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RigaContrattoCalcolo other = (RigaContrattoCalcolo) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    /**
     * @return the azionePrezzo
     */
    public Integer getAzionePrezzo() {
        return azionePrezzo;
    }

    /**
     * @return the Azione Prezzo come enum <code>Azione</code>
     */
    public Azione getAzionePrezzoEnum() {
        switch (azionePrezzo) {
        case 0:
            return Azione.SOSTITUZIONE;
        case 1:
            return Azione.VARIAZIONE;
        default:
            throw new UnsupportedOperationException();
        }
    }

    /**
     * @return the azioneProvvigione
     */
    public Integer getAzioneProvvigione() {
        return azioneProvvigione;
    }

    /**
     * @return the Azione Provvigione come enum <code>Azione</code>
     */
    public Azione getAzioneProvvigioneEnum() {
        switch (azioneProvvigione) {
        case 0:
            return Azione.SOSTITUZIONE;
        case 1:
            return Azione.VARIAZIONE;
        default:
            throw new UnsupportedOperationException();
        }
    }

    /**
     * @return the azioneSconto
     */
    public Integer getAzioneSconto() {
        return azioneSconto;
    }

    /**
     * @return the Azione Sconto come enum <code>Azione</code>
     */
    public Azione getAzioneScontoEnum() {
        switch (azioneSconto) {
        case 0:
            return Azione.SOSTITUZIONE;
        case 1:
            return Azione.VARIAZIONE;
        default:
            throw new UnsupportedOperationException();
        }
    }

    /**
     * @return the codiceContratto
     */
    public String getCodiceContratto() {
        return codiceContratto;
    }

    /**
     * @return descrizione dell'oggetto collegato
     */
    public String getCodiceDescrizioneContratto() {
        return new StringBuilder(codiceContratto).append(" - ").append(descrizioneContratto).toString();
    }

    /**
     * @return the dataFineContratto
     */
    public Date getDataFineContratto() {
        return dataFineContratto;
    }

    /**
     * @return the dataInizioContratto
     */
    public Date getDataInizioContratto() {
        return dataInizioContratto;
    }

    /**
     * @return the descrizioneContratto
     */
    public String getDescrizioneContratto() {
        return descrizioneContratto;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the idAgente
     */
    public Integer getIdAgente() {
        return idAgente;
    }

    /**
     * @return the idArticolo
     */
    public Integer getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return Returns the idCategoriaCommercialeArticolo.
     */
    public Integer getIdCategoriaCommercialeArticolo() {
        return idCategoriaCommercialeArticolo;
    }

    /**
     * @return the idCategoriaCommercialeArticolo2
     */
    public Integer getIdCategoriaCommercialeArticolo2() {
        return idCategoriaCommercialeArticolo2;
    }

    /**
     * @return the idCategoriaSede
     */
    public Integer getIdCategoriaSede() {
        return idCategoriaSede;
    }

    /**
     * @return the idContratto
     */
    public int getIdContratto() {
        return idContratto;
    }

    /**
     * @return the idEntita
     */
    public Integer getIdEntita() {
        return idEntita;
    }

    /**
     * @return the idSedeMagazzino
     */
    public Integer getIdSedeMagazzino() {
        return idSedeMagazzino;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDecimaliPrezzoArticolo
     */
    public Integer getNumeroDecimaliPrezzoArticolo() {
        return numeroDecimaliPrezzoArticolo;
    }

    /**
     * @return il peso della riga in base alle sue caratteristiche.
     */
    public Integer getPeso() {
        if (tutteLeCategorieArticolo && (!tutteLeCategorieSediMagazzino && idCategoriaSede == null && idEntita == null
                && idSedeMagazzino == null)) {
            // tutti-nessuno
            return 0;
        }
        if (tutteLeCategorieArticolo && tutteLeCategorieSediMagazzino) {
            // tutti-tutti
            return 1;
        } else if (tutteLeCategorieArticolo && idCategoriaSede != null) {
            // tutti-categoria cliente
            return 2;
        } else if (tutteLeCategorieArticolo && idEntita != null) {
            // tutti-cliente
            return 3;
        } else if (tutteLeCategorieArticolo && idSedeMagazzino != null) {
            // tutti-sede
            return 4;
        } else if (idCategoriaCommercialeArticolo != null && (!tutteLeCategorieSediMagazzino && idCategoriaSede == null
                && idEntita == null && idSedeMagazzino == null)) {
            // categoria art-nessuno
            return 5;
        } else if (idCategoriaCommercialeArticolo != null && tutteLeCategorieSediMagazzino) {
            // categoria articolo-tutti
            return 6;
        } else if (idCategoriaCommercialeArticolo != null && idCategoriaSede != null) {
            // categoria articolo-categoria cliente
            return 7;
        } else if (idCategoriaCommercialeArticolo != null && idEntita != null) {
            // categoria articolo-cliente
            return 8;
        } else if (idCategoriaCommercialeArticolo != null && idSedeMagazzino != null) {
            // categoria articolo-sede
            return 9;
        } else if (idCategoriaCommercialeArticolo2 != null && (!tutteLeCategorieSediMagazzino && idCategoriaSede == null
                && idEntita == null && idSedeMagazzino == null)) {
            // categoria articolo 2-nessuno
            return 10;
        } else if (idCategoriaCommercialeArticolo2 != null && tutteLeCategorieSediMagazzino) {
            // categoria articolo 2-tutti
            return 11;
        } else if (idCategoriaCommercialeArticolo2 != null && idCategoriaSede != null) {
            // categoria articolo 2-categoria cliente
            return 12;
        } else if (idCategoriaCommercialeArticolo2 != null && idEntita != null) {
            // categoria articolo 2-cliente
            return 13;
        } else if (idCategoriaCommercialeArticolo2 != null && idSedeMagazzino != null) {
            // categoria articolo 2-sede
            return 14;
        } else if (idArticolo != null && (!tutteLeCategorieSediMagazzino && idCategoriaSede == null && idEntita == null
                && idSedeMagazzino == null)) {
            // art-nessuno
            return 15;
        } else if (idArticolo != null && tutteLeCategorieSediMagazzino) {
            // art-tutti
            return 16;
        } else if (idArticolo != null && idCategoriaSede != null) {
            // art-categoria sede
            return 17;
        } else if (idArticolo != null && idEntita != null) {
            // art-entita
            return 18;
        } else if (idArticolo != null && idSedeMagazzino != null) {
            // art-sede
            return 19;
        }
        throw new UnsupportedOperationException("Combinazione dei pesi non trovata " + this.toString());
    }

    /**
     * @return the provvigineAgente
     */
    public BigDecimal getProvvigineAgente() {
        return provvigineAgente;
    }

    /**
     * @return the quantitaSogliaPrezzo
     */
    public double getQuantitaSogliaPrezzo() {
        return quantitaSogliaPrezzo;
    }

    /**
     * @return the quantitaSogliaSconto
     */
    public double getQuantitaSogliaSconto() {
        return quantitaSogliaSconto;
    }

    /**
     * @return the sconto1
     */
    public BigDecimal getSconto1() {
        return sconto1;
    }

    /**
     * @return the sconto2
     */
    public BigDecimal getSconto2() {
        return sconto2;
    }

    /**
     * @return the sconto3
     */
    public BigDecimal getSconto3() {
        return sconto3;
    }

    /**
     * @return the sconto4
     */
    public BigDecimal getSconto4() {
        return sconto4;
    }

    /**
     * @return the tipoValorePrezzo
     */
    public Integer getTipoValorePrezzo() {
        return tipoValorePrezzo;
    }

    /**
     * @return tipoValore
     */
    public TipoValore getTipoValorePrezzoEnum() {
        switch (tipoValorePrezzo) {
        case 0:
            return TipoValore.IMPORTO;
        case 1:
            return TipoValore.PERCENTUALE;
        default:
            throw new UnsupportedOperationException();
        }
    }

    /**
     * @return the valorePrezzo
     */
    public BigDecimal getValorePrezzo() {
        return valorePrezzo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /**
     * @return the bloccoPrezzo
     */
    public boolean isBloccoPrezzo() {
        return bloccoPrezzo;
    }

    /**
     * @return the bloccoProvvigione
     */
    public boolean isBloccoProvvigione() {
        return bloccoProvvigione;
    }

    /**
     * @return the bloccoSconto
     */
    public boolean isBloccoSconto() {
        return bloccoSconto;
    }

    /**
     * @return the ignoraBloccoPrezzoPrecedente
     */
    public boolean isIgnoraBloccoPrezzoPrecedente() {
        return ignoraBloccoPrezzoPrecedente;
    }

    /**
     * @return the ignoraBloccoProvvigionePrecedente
     */
    public boolean isIgnoraBloccoProvvigionePrecedente() {
        return ignoraBloccoProvvigionePrecedente;
    }

    /**
     * @return the ignoraBloccoScontoPrecedente
     */
    public boolean isIgnoraBloccoScontoPrecedente() {
        return ignoraBloccoScontoPrecedente;
    }

    /**
     * @return the strategiaPrezzoAbilitata
     */
    public boolean isStrategiaPrezzoAbilitata() {
        return strategiaPrezzoAbilitata;
    }

    /**
     * @return the strategiaScontoAbilitata
     */
    public boolean isStrategiaScontoAbilitata() {
        return strategiaScontoAbilitata;
    }

    /**
     * @return the tutteLeCategorieArticolo
     */
    public boolean isTutteLeCategorieArticolo() {
        return tutteLeCategorieArticolo;
    }

    /**
     * @return the tutteLeCategorieSedi
     */
    public boolean isTutteLeCategorieSedi() {
        return tutteLeCategorieSediMagazzino;
    }

    /**
     * @param azionePrezzo
     *            the azionePrezzo to set
     */
    public void setAzionePrezzo(Integer azionePrezzo) {
        this.azionePrezzo = azionePrezzo;
    }

    /**
     * @param azioneProvvigione
     *            the azioneProvvigione to set
     */
    public void setAzioneProvvigione(Integer azioneProvvigione) {
        this.azioneProvvigione = azioneProvvigione;
    }

    /**
     * @param azioneSconto
     *            the azioneSconto to set
     */
    public void setAzioneSconto(Integer azioneSconto) {
        this.azioneSconto = azioneSconto;
    }

    /**
     * @param bloccoPrezzo
     *            the bloccoPrezzo to set
     */
    public void setBloccoPrezzo(boolean bloccoPrezzo) {
        this.bloccoPrezzo = bloccoPrezzo;
    }

    /**
     * @param bloccoProvvigione
     *            the bloccoProvvigione to set
     */
    public void setBloccoProvvigione(boolean bloccoProvvigione) {
        this.bloccoProvvigione = bloccoProvvigione;
    }

    /**
     * @param bloccoSconto
     *            the bloccoSconto to set
     */
    public void setBloccoSconto(boolean bloccoSconto) {
        this.bloccoSconto = bloccoSconto;
    }

    /**
     * @param codiceContratto
     *            the codiceContratto to set
     */
    public void setCodiceContratto(String codiceContratto) {
        this.codiceContratto = codiceContratto;
    }

    /**
     * @param dataFineContratto
     *            the dataFineContratto to set
     */
    public void setDataFineContratto(Date dataFineContratto) {
        this.dataFineContratto = dataFineContratto;
    }

    /**
     * @param dataInizioContratto
     *            the dataInizioContratto to set
     */
    public void setDataInizioContratto(Date dataInizioContratto) {
        this.dataInizioContratto = dataInizioContratto;
    }

    /**
     * @param descrizioneContratto
     *            the descrizioneContratto to set
     */
    public void setDescrizioneContratto(String descrizioneContratto) {
        this.descrizioneContratto = descrizioneContratto;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param idAgente
     *            the idAgente to set
     */
    public void setIdAgente(Integer idAgente) {
        this.idAgente = idAgente;
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idCategoriaCommercialeArticolo
     *            The idCategoriaCommercialeArticolo to set.
     */
    public void setIdCategoriaCommercialeArticolo(Integer idCategoriaCommercialeArticolo) {
        this.idCategoriaCommercialeArticolo = idCategoriaCommercialeArticolo;
    }

    /**
     * @param idCategoriaCommercialeArticolo2
     *            the idCategoriaCommercialeArticolo2 to set
     */
    public void setIdCategoriaCommercialeArticolo2(Integer idCategoriaCommercialeArticolo2) {
        this.idCategoriaCommercialeArticolo2 = idCategoriaCommercialeArticolo2;
    }

    /**
     * @param idCategoriaSede
     *            the idCategoriaSede to set
     */
    public void setIdCategoriaSede(Integer idCategoriaSede) {
        this.idCategoriaSede = idCategoriaSede;
    }

    /**
     * @param idContratto
     *            the idContratto to set
     */
    public void setIdContratto(int idContratto) {
        this.idContratto = idContratto;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    /**
     * @param idSedeMagazzino
     *            the idSedeMagazzino to set
     */
    public void setIdSedeMagazzino(Integer idSedeMagazzino) {
        this.idSedeMagazzino = idSedeMagazzino;
    }

    /**
     * @param ignoraBloccoPrezzoPrecedente
     *            the ignoraBloccoPrezzoPrecedente to set
     */
    public void setIgnoraBloccoPrezzoPrecedente(boolean ignoraBloccoPrezzoPrecedente) {
        this.ignoraBloccoPrezzoPrecedente = ignoraBloccoPrezzoPrecedente;
    }

    /**
     * @param ignoraBloccoProvvigionePrecedente
     *            the ignoraBloccoProvvigionePrecedente to set
     */
    public void setIgnoraBloccoProvvigionePrecedente(boolean ignoraBloccoProvvigionePrecedente) {
        this.ignoraBloccoProvvigionePrecedente = ignoraBloccoProvvigionePrecedente;
    }

    /**
     * @param ignoraBloccoScontoPrecedente
     *            the ignoraBloccoScontoPrecedente to set
     */
    public void setIgnoraBloccoScontoPrecedente(boolean ignoraBloccoScontoPrecedente) {
        this.ignoraBloccoScontoPrecedente = ignoraBloccoScontoPrecedente;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliPrezzoArticolo
     *            the numeroDecimaliPrezzoArticolo to set
     */
    public void setNumeroDecimaliPrezzoArticolo(Integer numeroDecimaliPrezzoArticolo) {
        this.numeroDecimaliPrezzoArticolo = numeroDecimaliPrezzoArticolo;
    }

    /**
     * @param provvigineAgente
     *            the provvigineAgente to set
     */
    public void setProvvigineAgente(BigDecimal provvigineAgente) {
        this.provvigineAgente = provvigineAgente;
    }

    /**
     * @param quantitaSogliaPrezzo
     *            the quantitaSogliaPrezzo to set
     */
    public void setQuantitaSogliaPrezzo(double quantitaSogliaPrezzo) {
        this.quantitaSogliaPrezzo = quantitaSogliaPrezzo;
    }

    /**
     * @param quantitaSogliaSconto
     *            the quantitaSogliaSconto to set
     */
    public void setQuantitaSogliaSconto(double quantitaSogliaSconto) {
        this.quantitaSogliaSconto = quantitaSogliaSconto;
    }

    /**
     * @param sconto1
     *            the sconto1 to set
     */
    public void setSconto1(BigDecimal sconto1) {
        this.sconto1 = sconto1;
    }

    /**
     * @param sconto2
     *            the sconto2 to set
     */
    public void setSconto2(BigDecimal sconto2) {
        this.sconto2 = sconto2;
    }

    /**
     * @param sconto3
     *            the sconto3 to set
     */
    public void setSconto3(BigDecimal sconto3) {
        this.sconto3 = sconto3;
    }

    /**
     * @param sconto4
     *            the sconto4 to set
     */
    public void setSconto4(BigDecimal sconto4) {
        this.sconto4 = sconto4;
    }

    /**
     * @param strategiaPrezzoAbilitata
     *            the strategiaPrezzoAbilitata to set
     */
    public void setStrategiaPrezzoAbilitata(boolean strategiaPrezzoAbilitata) {
        this.strategiaPrezzoAbilitata = strategiaPrezzoAbilitata;
    }

    /**
     * @param strategiaScontoAbilitata
     *            the strategiaScontoAbilitata to set
     */
    public void setStrategiaScontoAbilitata(boolean strategiaScontoAbilitata) {
        this.strategiaScontoAbilitata = strategiaScontoAbilitata;
    }

    /**
     * @param tipoValorePrezzo
     *            the tipoValorePrezzo to set
     */
    public void setTipoValorePrezzo(Integer tipoValorePrezzo) {
        this.tipoValorePrezzo = tipoValorePrezzo;
    }

    /**
     * @param tutteLeCategorieArticolo
     *            the tutteLeCategorieArticolo to set
     */
    public void setTutteLeCategorieArticolo(boolean tutteLeCategorieArticolo) {
        this.tutteLeCategorieArticolo = tutteLeCategorieArticolo;
    }

    /**
     * @param tutteLeCategorieSedi
     *            the tutteLeCategorieSedi to set
     */
    public void setTutteLeCategorieSediMagazzino(boolean tutteLeCategorieSedi) {
        this.tutteLeCategorieSediMagazzino = tutteLeCategorieSedi;
    }

    /**
     * @param valorePrezzo
     *            the valorePrezzo to set
     */
    public void setValorePrezzo(BigDecimal valorePrezzo) {
        this.valorePrezzo = valorePrezzo;
    }

    @Override
    public String toString() {
        return "RigaContrattoCalcolo [azionePrezzo=" + azionePrezzo + ", azioneSconto=" + azioneSconto
                + ", bloccoPrezzo=" + bloccoPrezzo + ", bloccoSconto=" + bloccoSconto + ", codiceContratto="
                + codiceContratto + ", descrizioneContratto=" + descrizioneContratto + ", id=" + id + ", idArticolo="
                + idArticolo + ", idCategoriaArticolo=" + idCategoriaCommercialeArticolo + ", idCategoriaSede="
                + idCategoriaSede + ", idContratto=" + idContratto + ", idSedeMagazzino=" + idSedeMagazzino
                + ", ignoraBloccoPrezzoPrecedente=" + ignoraBloccoPrezzoPrecedente + ", ignoraBloccoScontoPrecedente="
                + ignoraBloccoScontoPrecedente + ", numeroDecimaliPrezzo=" + numeroDecimaliPrezzo
                + ", quantitaSogliaPrezzo=" + quantitaSogliaPrezzo + ", quantitaSogliaSconto=" + quantitaSogliaSconto
                + ", sconto1=" + sconto1 + ", sconto2=" + sconto2 + ", sconto3=" + sconto3 + ", sconto4=" + sconto4
                + ", tipoValorePrezzo=" + tipoValorePrezzo + ", tutteLeCategorieArticolo=" + tutteLeCategorieArticolo
                + ", tutteLeCategorieSediMagazzino=" + tutteLeCategorieSediMagazzino + ", valorePrezzo=" + valorePrezzo
                + "]";
    }
}
