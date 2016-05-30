package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;

import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.giroclienti.rich.bd.ISchedeGiroClientiBD;
import it.eurotn.panjea.giroclienti.rich.bd.SchedeGiroClientiBD;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.SchedeGiroClientiPage;

public class GiorniSettimanaPanel extends JPanel {

    private static final long serialVersionUID = -5341966055585626057L;

    private HeaderPanel headerPanel;

    private ISchedeGiroClientiBD schedeGiroClientiBD;

    /**
     * Costruttore.
     *
     * @param headerPanel
     *            headerPanel;
     */
    public GiorniSettimanaPanel(final HeaderPanel headerPanel) {
        super(new GridLayout(0, 7));
        this.headerPanel = headerPanel;
        this.schedeGiroClientiBD = RcpSupport.getBean(SchedeGiroClientiBD.BEAN_ID);
    }

    /**
     * Ricarica i dati del pannello.
     *
     * @param schedePage
     *            schedePage
     */
    public void reloadData(final SchedeGiroClientiPage schedePage) {

        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }

        ExclusiveCommandGroup commandGroup = new ExclusiveCommandGroup();

        Date[] dateSchedaSettimanale = schedeGiroClientiBD
                .caricaDateSchedaSettimanale(headerPanel.getUtenteSelezionato().getId());

        removeAll();
        for (int i = 0; i < dateSchedaSettimanale.length; i++) {
            SchedaGiroToggleCommand command = new SchedaGiroToggleCommand(dateSchedaSettimanale[i], schedePage,
                    headerPanel);
            commandGroup.add(command);
            add(command.createButton());

            if (command.getGiorno().ordinal() == dayOfWeek - 1) {
                command.setSelected(true);
            }
        }
    }

}
