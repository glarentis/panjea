package it.eurotn.panjea.anagrafica.classedocumento.manager;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;

public abstract class AbstractClasseTipoDocumento implements IClasseTipoDocumento {

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
        AbstractClasseTipoDocumento other = (AbstractClasseTipoDocumento) obj;
        if (!getClass().getName().equals(other.getClass().getName())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getClass().getName().hashCode();
        return result;
    }

}
