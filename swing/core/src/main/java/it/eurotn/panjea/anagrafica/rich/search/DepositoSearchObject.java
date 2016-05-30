package it.eurotn.panjea.anagrafica.rich.search;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.rich.search.AbstractSearchObject;

public class DepositoSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "depositoSearchObject";
    public static final String SOLO_DEPOSITO_AZIENDA_PARAM = "solodepazienda";
    public static final String SOLO_DEPOSITO_MEZZO_TRASPORTO_PARAM = "solodeConMezziTrasporto";
    public static final String DEPOSITI_AGGIUNTIVI_PARAM = "depositiAggiuntivi";

    private IAnagraficaBD anagraficaBD = null;
    private AziendaCorrente aziendaCorrente = null;

    /**
     * Costruttore di default.
     */
    public DepositoSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    /**
     * @return the anagraficaBD
     */
    public IAnagraficaBD getAnagraficaBD() {
        return anagraficaBD;
    }

    /**
     * @return the aziendaCorrente
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return null;
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        Map<String, Object> parameters = searchPanel.getMapParameters();
        Boolean soloConMezzoTrasporto = (Boolean) parameters.get(SOLO_DEPOSITO_MEZZO_TRASPORTO_PARAM);
        if (soloConMezzoTrasporto == null) {
            soloConMezzoTrasporto = false;
        }

        List<DepositoLite> depositi = anagraficaBD.caricaDepositiAzienda(fieldSearch, valueSearch,
                soloConMezzoTrasporto);

        Iterator<DepositoLite> depositiIterator = depositi.iterator();
        Boolean soloDepositiAzienda = (Boolean) parameters.get(SOLO_DEPOSITO_AZIENDA_PARAM);
        if (soloDepositiAzienda == null) {
            soloDepositiAzienda = false;
        }
        while (depositiIterator.hasNext()) {
            DepositoLite deposito = depositiIterator.next();
            if (!deposito.getAttivo()) {
                depositiIterator.remove();
                continue;
            }
            if (soloDepositiAzienda && (deposito.isDepositoMezzoTrasporto() || !"DE".equals(deposito.getTipo()))) {
                depositiIterator.remove();
                continue;
            }
        }

        @SuppressWarnings("unchecked")
        List<DepositoLite> depositiAggiuntivi = (List<DepositoLite>) parameters.get(DEPOSITI_AGGIUNTIVI_PARAM);
        if (CollectionUtils.isNotEmpty(depositiAggiuntivi)) {
            depositi.addAll(depositiAggiuntivi);
        }

        return depositi;
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

}
