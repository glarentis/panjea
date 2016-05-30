package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.runnable;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica.TipoSpedizioneDocumenti;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.TipoLayout;
import it.eurotn.rich.control.table.JideTableWidget;

public final class SendMailFactory {
    /**
     * private. classe Utility
     */
    private SendMailFactory() {
    }

    /**
     *
     * @param tipoSpedizioneDocumenti
     *            tipoSpedizione
     * @param movimento
     *            movimento
     * @param tableWidget
     *            tabella per aggiornare i risultati
     * @param templateSpedizione
     *            template mail
     * @param tipoLayout
     *            tipoLayout da creare
     * @return executor per spedire o stampare il layout
     */
    public static SpedizioneRunnable getInstance(TipoSpedizioneDocumenti tipoSpedizioneDocumenti,
            MovimentoSpedizioneDTO movimento, JideTableWidget<MovimentoSpedizioneDTO> tableWidget,
            TemplateSpedizioneMovimenti templateSpedizione, TipoLayout tipoLayout) {
        switch (tipoSpedizioneDocumenti) {
        case EMAIL:
            return new SendMailRunnable(movimento, tableWidget, templateSpedizione);
        default:
            return new StampaRunnable(movimento, tableWidget, tipoLayout == TipoLayout.INTERNO);
        }
    }
}
