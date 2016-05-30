package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;
import java.util.Comparator;

public class ScaglioneComparator implements Comparator<ScaglioneListino>, Serializable {

    private static final long serialVersionUID = -7156109699502450816L;

    @Override
    public int compare(ScaglioneListino o1, ScaglioneListino o2) {
        if (o1.getQuantita() == null && o2.getQuantita() == null) {
            return 0;
        }
        if (o1.getQuantita() == null && o2.getQuantita() != null) {
            return -1;
        }
        if (o1.getQuantita() != null && o2.getQuantita() == null) {
            return 1;
        }
        return o1.getQuantita().compareTo(o2.getQuantita());
    }

}
