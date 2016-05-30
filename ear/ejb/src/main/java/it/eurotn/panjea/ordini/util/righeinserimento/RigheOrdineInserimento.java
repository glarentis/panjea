package it.eurotn.panjea.ordini.util.righeinserimento;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class RigheOrdineInserimento implements Serializable {

    private static final long serialVersionUID = 3471137990183595455L;

    private List<RigaOrdineInserimento> righe;

    private Set<String> attributiPresenti;

    /**
     * @return the attributiPresenti
     */
    public Set<String> getAttributiPresenti() {
        return attributiPresenti;
    }

    /**
     * @return the righe
     */
    public List<RigaOrdineInserimento> getRighe() {
        return righe;
    }

    /**
     * @param attributiPresenti
     *            the attributiPresenti to set
     */
    public void setAttributiPresenti(Set<String> attributiPresenti) {
        this.attributiPresenti = attributiPresenti;
    }

    /**
     * @param righe
     *            the righe to set
     */
    public void setRighe(List<RigaOrdineInserimento> righe) {
        this.righe = righe;
    }
}
