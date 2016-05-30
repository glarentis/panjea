package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.eurotn.panjea.giroclienti.rich.editors.scheda.SchedeGiroClientiPage;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.header.copiascheda.CopiaSchedaCommand;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

public class HeaderPanel extends JPanel {

    private static final long serialVersionUID = 6167629778555730459L;

    private UtentiComboBox utentiComboBox;

    private SchedeGiroClientiPage schedePage;

    private AggiungiGiroClienteCommand aggiungiGiroClienteCommand;
    private RimuoviGiroClienteCommand rimuoviGiroClienteCommand;
    private CancellaSchedeGiroClienteCommand cancellaSchedeGiroClienteCommand;
    private CopiaSchedaCommand copiaSchedaCommand;

    private GiorniSettimanaPanel giorniSettimanaPanel;

    private Giorni giornoCorrente;

    /**
     * Costruttore.
     *
     * @param schedeGiroClientiPage
     *            pagina delle schede cliente
     */
    public HeaderPanel(final SchedeGiroClientiPage schedeGiroClientiPage) {
        super(new BorderLayout(5, 10));
        this.schedePage = schedeGiroClientiPage;

        init();
    }

    /**
     * @return pannello contenente tutti i command per gestire il giro clienti della scheda
     */
    private JComponent createCommandPanel() {

        JPanel commandsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        commandsPanel.add(getCopiaSchedaCommand().createButton());
        commandsPanel.add(getAggiungiGiroClienteCommand().createButton());
        commandsPanel.add(getRimuoviGiroClienteCommand().createButton());

        return commandsPanel;
    }

    private JComponent createUtentiPanel() {

        utentiComboBox = new UtentiComboBox(schedePage);

        JPanel utentiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        utentiPanel.add(new JLabel("Utente"));
        utentiPanel.add(utentiComboBox);
        utentiPanel.add(getCancellaSchedeGiroClienteCommand().createButton());

        return utentiPanel;
    }

    /**
     * @return the aggiungiGiroClienteCommand
     */
    private AggiungiGiroClienteCommand getAggiungiGiroClienteCommand() {
        if (aggiungiGiroClienteCommand == null) {
            aggiungiGiroClienteCommand = new AggiungiGiroClienteCommand();
            aggiungiGiroClienteCommand
                    .addCommandInterceptor(new AggiungiGiroClienteCommandInterceptor(schedePage, this));
        }

        return aggiungiGiroClienteCommand;
    }

    /**
     * @return the aggiungiGiroClienteCommand
     */
    private CancellaSchedeGiroClienteCommand getCancellaSchedeGiroClienteCommand() {
        if (cancellaSchedeGiroClienteCommand == null) {
            cancellaSchedeGiroClienteCommand = new CancellaSchedeGiroClienteCommand();
            cancellaSchedeGiroClienteCommand
                    .addCommandInterceptor(new CancellaSchedeGiroClienteCommandInterceptor(schedePage, this));
        }

        return cancellaSchedeGiroClienteCommand;
    }

    private CopiaSchedaCommand getCopiaSchedaCommand() {
        if (copiaSchedaCommand == null) {
            copiaSchedaCommand = new CopiaSchedaCommand();
            copiaSchedaCommand.addCommandInterceptor(new CopiaSchedaCommandInterceptor(this));
        }

        return copiaSchedaCommand;
    }

    /**
     * @return the giornoCorrente
     */
    public Giorni getGiornoCorrente() {
        return giornoCorrente;
    }

    /**
     * @return the rimuoviGiroClienteCommand
     */
    private RimuoviGiroClienteCommand getRimuoviGiroClienteCommand() {
        if (rimuoviGiroClienteCommand == null) {
            rimuoviGiroClienteCommand = new RimuoviGiroClienteCommand();
            rimuoviGiroClienteCommand.addCommandInterceptor(new RimuoviGiroClienteCommandInterceptor(schedePage));
        }

        return rimuoviGiroClienteCommand;
    }

    /**
     * @return utente attualmente selezionato
     */
    public Utente getUtenteSelezionato() {
        return (Utente) utentiComboBox.getSelectedItem();
    }

    private void init() {

        add(createUtentiPanel(), BorderLayout.WEST);
        add(createCommandPanel(), BorderLayout.EAST);

        giorniSettimanaPanel = new GiorniSettimanaPanel(this);
        add(giorniSettimanaPanel, BorderLayout.PAGE_END);
    }

    /**
     * Ricarica i dati del pannello.
     */
    public void reloadData() {

        giorniSettimanaPanel.reloadData(schedePage);
    }

    /**
     * @param giornoCorrente
     *            the giornoCorrente to set
     */
    public void setGiornoCorrente(Giorni giornoCorrente) {
        this.giornoCorrente = giornoCorrente;
    }

}
