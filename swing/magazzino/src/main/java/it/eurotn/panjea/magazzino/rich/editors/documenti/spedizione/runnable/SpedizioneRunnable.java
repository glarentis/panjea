package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.runnable;

import java.io.Serializable;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.rich.control.table.JideTableWidget;

@SuppressWarnings("serial")
public abstract class SpedizioneRunnable implements Runnable, Serializable {

    protected class EsitoSpedizione implements Serializable {

        private static final long serialVersionUID = 1033407225219351402L;

        private boolean spedizioneOk;
        private String esito;

        /**
         * Costruttore.
         *
         * @param spedizioneOk
         *            stato spedizione
         * @param esito
         *            esito
         */
        public EsitoSpedizione(final boolean spedizioneOk, final String esito) {
            super();
            this.spedizioneOk = spedizioneOk;
            this.esito = esito;
        }

        /**
         * @return the esito
         */
        public String getEsito() {
            return esito;
        }

        /**
         * @return the spedizioneOk
         */
        public boolean isSpedizioneOk() {
            return spedizioneOk;
        }
    }

    private MovimentoSpedizioneDTO movimento;

    private JideTableWidget<MovimentoSpedizioneDTO> tableWidget;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    protected boolean layoutInterno;

    /**
     * Costruttore.
     *
     * @param movimento
     *            movimento
     * @param tableWidget
     *            tabella dei movimenti
     * @param layoutInterno
     *            indica se deve essere preferito il layout interno per la stampa
     */
    public SpedizioneRunnable(final MovimentoSpedizioneDTO movimento,
            final JideTableWidget<MovimentoSpedizioneDTO> tableWidget, final boolean layoutInterno) {
        super();
        this.movimento = movimento;
        this.tableWidget = tableWidget;
        this.layoutInterno = layoutInterno;

        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    /**
     * @return esito della spedizione
     */
    protected abstract EsitoSpedizione doRun();

    /**
     * @return the movimento
     */
    public MovimentoSpedizioneDTO getMovimento() {
        return movimento;
    }

    @Override
    public void run() {

        EsitoSpedizione esito = doRun();
        final StatoSpedizione statoSpedizione;

        if (esito.isSpedizioneOk() && !layoutInterno) {
            statoSpedizione = magazzinoDocumentoBD.cambiaStatoSpedizioneMovimento(movimento.getAreaDocumento());
        } else {
            statoSpedizione = movimento.getAreaDocumento().getStatoSpedizione();
        }

        final String descrizioneEsito;
        if (layoutInterno) {
            descrizioneEsito = "Interno: " + esito.getEsito();
        } else {
            descrizioneEsito = "Originale: " + esito.getEsito();
        }

        // aggiorno l'esito di spedizione
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                int idx = tableWidget.getRows().indexOf(movimento);
                if (idx != -1) {
                    movimento = tableWidget.getRows().get(idx);
                }

                movimento.getAreaDocumento().setStatoSpedizione(statoSpedizione);

                movimento.setEsitoSpedizione((StringUtils.isBlank(movimento.getEsitoSpedizione()) ? ""
                        : movimento.getEsitoSpedizione() + "\n") + descrizioneEsito);

                tableWidget.replaceRowObject(movimento, movimento, null);
            }
        });
    }
}
