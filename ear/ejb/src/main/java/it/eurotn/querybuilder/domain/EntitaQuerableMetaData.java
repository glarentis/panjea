package it.eurotn.querybuilder.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntitaQuerableMetaData implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<ProprietaEntity> proprieta;

    public void addAllProprieta(List<ProprietaEntity> proprietaEntity) {
        if (proprieta == null) {
            proprieta = new ArrayList<>();
        }
        proprieta.addAll(proprietaEntity);
    }

    public void addProprieta(ProprietaEntity proprietaEntity) {
        if (proprieta == null) {
            proprieta = new ArrayList<>();
        }
        proprieta.add(proprietaEntity);
    }

    /**
     * @return Returns the proprieta.
     */
    public List<ProprietaEntity> getProprieta() {
        return proprieta;
    }
}
