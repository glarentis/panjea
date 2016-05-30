package it.eurotn.panjea.magazzino.rich.editors.articolo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.factory.ComponentFactory;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;

public class StatisticaDialog extends ApplicationDialog {
    @SuppressWarnings("serial")
    private class SpinnerAnnoModel extends SpinnerNumberModel {

        /**
         * Costruttore.
         */
        public SpinnerAnnoModel() {
            super(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.YEAR) - 5,
                    Calendar.getInstance().get(Calendar.YEAR) + 5, 1);
        }

        @Override
        public void setValue(Object value) {
            if (value == getValue()) {
                return;
            }
            super.setValue(value);
            int anno = (int) value;
            statisticheArticolo = magazzinoDocumentoBD.caricaStatisticheArticolo(articolo.creaProxyArticolo(), anno);
            tabbed.removeAll();
            createComponentForStatistica();
        }

    }

    private StatisticheArticolo statisticheArticolo;
    private DepositoLite deposito;
    private ArticoloLite articolo;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private JPanel rootPanel;
    private JTabbedPane tabbed;

    /**
     *
     * @param statisticheArticolo
     *            statistiche da visualizzare.
     * @param deposito
     *            deposito da visualizzare.
     * @param articolo
     *            articolo per la statistica.
     * @param magazzinoDocumentoBD
     *            bd documento magazzino
     */
    public StatisticaDialog(final StatisticheArticolo statisticheArticolo, final DepositoLite deposito,
            final ArticoloLite articolo, final IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        super();
        this.statisticheArticolo = statisticheArticolo;
        this.deposito = deposito;
        this.articolo = articolo;
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
        setPreferredSize(new Dimension(820, 600));
        setTitle("Statistiche");
    }

    private void createComponentForStatistica() {
        ComponentFactory componentFactory = (ComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        if (tabbed == null) {
            tabbed = componentFactory.createTabbedPane();
        }

        // se c'è il deposito aggiungo come primo tab quello del deposito e poi quello riepilogatico
        if (deposito != null) {
            StatisticaArticolo statisticaTutti = statisticheArticolo.getStatistichePerTipologiaDeposito()
                    .get(StatisticaArticolo.TUTTI_KEY).get(0);

            // Ciclo ed estraggo la statistica per il deposito se ci sono movimenti
            if (statisticheArticolo.getStatistichePerTipologiaDeposito()
                    .containsKey(deposito.getTipoDeposito().getCodice())) {

                List<StatisticaArticolo> statisticaArticoloTipoDeposito = statisticheArticolo
                        .getStatistichePerTipologiaDeposito().get(deposito.getTipoDeposito().getCodice());
                for (StatisticaArticolo statisticaArticolo : statisticaArticoloTipoDeposito) {
                    if (statisticaArticolo.getDepositoLite().getCodice().equals(deposito.getCodice())) {
                        JPanel pnlDeposito = new StatisticaDepositoComponent(statisticaArticolo,
                                articolo.getNumeroDecimaliPrezzo(), articolo.getNumeroDecimaliQta(),
                                magazzinoDocumentoBD);
                        tabbed.addTab(deposito.getCodice(), pnlDeposito);
                        continue;
                    }
                }
            }

            JPanel pnlTotaleDepositi = new StatisticaDepositoComponent(statisticaTutti,
                    articolo.getNumeroDecimaliPrezzo(), articolo.getNumeroDecimaliQta(), magazzinoDocumentoBD);

            tabbed.addTab(StatisticaArticolo.TUTTI_KEY, pnlTotaleDepositi);
        } else {
            // se non c'è il deposito vengono aggiunti tutti i depositi e come primo tab quello
            // riepilogativo
            StatisticaArticolo statisticaTutti = statisticheArticolo.getStatistichePerTipologiaDeposito()
                    .get(StatisticaArticolo.TUTTI_KEY).get(0);
            JPanel pnlTotaleDepositi = new StatisticaDepositoComponent(statisticaTutti,
                    articolo.getNumeroDecimaliPrezzo(), articolo.getNumeroDecimaliQta(), magazzinoDocumentoBD);

            tabbed.addTab(StatisticaArticolo.TUTTI_KEY, pnlTotaleDepositi);

            for (Entry<String, List<StatisticaArticolo>> statisticaArticoloTipologia : statisticheArticolo
                    .getStatistichePerTipologiaDeposito().entrySet()) {
                for (StatisticaArticolo statisticaArticolo : statisticaArticoloTipologia.getValue()) {
                    if (!StatisticaArticolo.TUTTI_KEY.equals(statisticaArticolo.getDepositoLite().getCodice())) {
                        JPanel pnlDeposito = new StatisticaDepositoComponent(statisticaArticolo,
                                articolo.getNumeroDecimaliPrezzo(), articolo.getNumeroDecimaliQta(),
                                magazzinoDocumentoBD);
                        tabbed.addTab(statisticaArticoloTipologia.getKey() + " - "
                                + statisticaArticolo.getDepositoLite().getCodice(), pnlDeposito);
                    }
                }
            }
        }
    }

    @Override
    protected JComponent createDialogContentPane() {
        rootPanel = new JPanel(new BorderLayout());
        createComponentForStatistica();
        JSpinner spinner = new JSpinner(new SpinnerAnnoModel());
        rootPanel.add(spinner, BorderLayout.NORTH);
        rootPanel.add(tabbed, BorderLayout.CENTER);
        return rootPanel;
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return (new AbstractCommand[] { getFinishCommand() });
    }

    @Override
    protected boolean onFinish() {
        return true;
    }

}
