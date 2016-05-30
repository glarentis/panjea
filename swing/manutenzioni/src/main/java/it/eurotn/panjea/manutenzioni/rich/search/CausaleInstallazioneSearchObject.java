package it.eurotn.panjea.manutenzioni.rich.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione.TipoInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione.TipoMovimento;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class CausaleInstallazioneSearchObject extends AbstractSearchObject {

    public static final String CAUSALI_TIPO_PARAM_KEY = "installazioni";
    public static final String TIPO_MOVIMENTO_PARAM_KEY = "tipoMovimento";
    private IManutenzioniBD manutenzioniBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        List<CausaleInstallazione> causali = manutenzioniBD.caricaCausaliInstallazione();

        Integer tipoInstallazione = (Integer) searchPanel.getMapParameters().get(CAUSALI_TIPO_PARAM_KEY);
        TipoMovimento tipoMovimento = (TipoMovimento) searchPanel.getMapParameters().get(TIPO_MOVIMENTO_PARAM_KEY);
        boolean isSostituzione = tipoMovimento != null && tipoMovimento == TipoMovimento.SOSTITUZIONE;
        List<CausaleInstallazione> result = new ArrayList<>();
        for (CausaleInstallazione causaleInstallazione : causali) {
            if (test(causaleInstallazione, tipoInstallazione, isSostituzione, fieldSearch, valueSearch)) {
                result.add(causaleInstallazione);
            }
        }
        Collections.sort(result, new BeanComparator("ordinamento"));
        return result;
    }

    /**
     * @return Returns the manutenzioniBD.
     */
    public IManutenzioniBD getManutenzioniBD() {
        return manutenzioniBD;
    }

    /**
     * @param manutenzioniBD
     *            The manutenzioniBD to set.
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

    private boolean test(CausaleInstallazione causale, Integer tipoInstallazione, boolean sostituzione,
            String fieldSearch, // NOSONAR
            String valueSearch) {
        String searchString = ObjectUtils.defaultIfNull(valueSearch, "");
        final String valueSearchWithoutJolly = searchString.replaceAll("%", "").toUpperCase();
        String inputValue = "";
        switch (fieldSearch) {
        case "codice":
            inputValue = causale.getCodice().toUpperCase();
            break;
        case "descrizione":
            inputValue = causale.getDescrizione().toUpperCase();
            break;
        default:
            throw new UnsupportedOperationException("ricerca non supportata per il campo " + fieldSearch);
        }

        if (inputValue.startsWith(valueSearchWithoutJolly)) {
            TipoInstallazione tipoInstallazionecausale = causale.getTipoInstallazione();
            if (tipoInstallazione == 0) {
                return sostituzione
                        ? tipoInstallazionecausale.isInstallazione()
                                && tipoInstallazionecausale != TipoInstallazione.PRIMA_INSTALLAZIONE
                        : tipoInstallazionecausale.isInstallazione();
            }
            if (tipoInstallazione == 2) {
                return sostituzione
                        ? tipoInstallazionecausale.isRitiro()
                                && causale.getTipoInstallazione() != TipoInstallazione.RITIRO_DEFINITIVO
                        : tipoInstallazionecausale.isRitiro();
            }
        }
        return false;
    }
}
