package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.SchedaArticolo.StatoScheda;

/**
 * @author fattazzo
 *
 */
public class ElaborazioneSchedaArticoloDTO implements Serializable {

    private static final long serialVersionUID = 8015529667119929570L;

    private String nota;

    private Integer mese;

    private Integer anno;

    private ArticoloLite articolo;

    private StatoScheda stato;

    {
        this.articolo = new ArticoloLite();
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
        ElaborazioneSchedaArticoloDTO other = (ElaborazioneSchedaArticoloDTO) obj;
        if (anno == null) {
            if (other.anno != null) {
                return false;
            }
        } else if (!anno.equals(other.anno)) {
            return false;
        }
        if (articolo == null) {
            if (other.articolo != null) {
                return false;
            }
        } else if (!articolo.equals(other.articolo)) {
            return false;
        }
        if (mese == null) {
            if (other.mese != null) {
                return false;
            }
        } else if (!mese.equals(other.mese)) {
            return false;
        }
        if (nota == null) {
            if (other.nota != null) {
                return false;
            }
        } else if (!nota.equals(other.nota)) {
            return false;
        }
        return true;
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the mese
     */
    public Integer getMese() {
        return mese;
    }

    /**
     * @return the nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * @return the stato
     */
    public StatoScheda getStato() {
        return stato;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((anno == null) ? 0 : anno.hashCode());
        result = prime * result + ((articolo == null) ? 0 : articolo.hashCode());
        result = prime * result + ((mese == null) ? 0 : mese.hashCode());
        result = prime * result + ((nota == null) ? 0 : nota.hashCode());
        return result;
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.articolo.setCodice(codiceArticolo);
    }

    /**
     * @param descrizioneArticolo
     *            the descrizioneArticolo to set
     */
    public void setDescrizioneArticolo(String descrizioneArticolo) {
        this.articolo.setDescrizione(descrizioneArticolo);
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.articolo.setId(idArticolo);
    }

    /**
     * @param mese
     *            the mese to set
     */
    public void setMese(Integer mese) {
        this.mese = mese;
    }

    /**
     * @param nota
     *            the nota to set
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @param stato
     *            the statoto set
     */
    public void setStato(Integer stato) {
        this.stato = StatoScheda.values()[stato];
    }

}
