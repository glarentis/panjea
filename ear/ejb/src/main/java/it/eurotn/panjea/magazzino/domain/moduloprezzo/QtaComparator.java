package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import java.io.Serializable;
import java.util.Comparator;

public class QtaComparator implements Comparator<Double>, Serializable {
    private static final long serialVersionUID = 3695956167979121802L;

    @Override
    public int compare(Double o1, Double o2) {
        return o1.compareTo(o2);
    }
}