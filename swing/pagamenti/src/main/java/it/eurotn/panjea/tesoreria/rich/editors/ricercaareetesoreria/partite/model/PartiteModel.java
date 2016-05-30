package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria.partite.model;

import java.util.List;

import org.springframework.util.Assert;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;
import it.eurotn.panjea.tesoreria.domain.Effetto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

public class PartiteModel {

    private List<Effetto> effetti = null;
    private EPartitaGroup group;

    private List<Pagamento> pagamenti = null;

    /**
     * Costruttore.
     * 
     * @param effetti
     *            lista di effetti da gestire
     * @param pagamenti
     *            lista di pagamenti da gestire
     */
    public PartiteModel(final List<Effetto> effetti, final List<Pagamento> pagamenti) {
        super();
        Assert.isTrue(((pagamenti == null && effetti != null) || (pagamenti != null && effetti == null)),
                "E' possibile gestire solo pagamenti o solo effetti");
        this.pagamenti = pagamenti;
        this.effetti = effetti;
        this.group = EPartitaGroup.DATA_SCADENZA;
    }

    /**
     * Restituisce gli effetti raggruppati secondo quanto settato del metodo
     * {@link PartiteModel#setGroup(EPartitaGroup)}. Di default il raggruppamento sarà effettuato per data.
     * 
     * @return lista di effetti raggruppati
     */
    @SuppressWarnings("deprecation")
    public GroupingList<Effetto> getEffetti() {

        final EventList<Effetto> eventList = new BasicEventList<>();
        eventList.addAll(effetti);

        GroupingList<Effetto> groupingList = null;
        switch (group) {
        case DATA_VALUTA:
            groupingList = new GroupingList<>(eventList, new DataValutaEffettoComparator());
            break;
        case DATA_SCADENZA:
            groupingList = new GroupingList<>(eventList, new DataScadenzaEffettoComparator());
            break;
        default:
            throw new UnsupportedOperationException("Raggruppamento partite non gestito");
        }

        return groupingList;
    }

    /**
     * @return the group
     */
    public EPartitaGroup getGroup() {
        return group;
    }

    /**
     * Restituisce i pgamenti senza nessun raggruppamento.
     * 
     * @return lista di pagamenti
     */
    public List<Pagamento> getPagamenti() {
        return pagamenti;
    }

    /**
     * @param group
     *            the group to set
     */
    public void setGroup(EPartitaGroup group) {
        this.group = group;
    }
}
