package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.JideToggleButton;

import it.eurotn.panjea.giroclienti.rich.bd.ISchedeGiroClientiBD;
import it.eurotn.panjea.giroclienti.rich.bd.SchedeGiroClientiBD;
import it.eurotn.panjea.giroclienti.rich.editors.scheda.SchedeGiroClientiPage;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClientiDTO;
import it.eurotn.panjea.util.Giorni;
import it.eurotn.rich.command.JideToggleCommand;

public class SchedaGiroToggleCommand extends JideToggleCommand {

    private Date dataScheda;
    private Giorni giorno;

    private SchedeGiroClientiPage schedePage;
    private HeaderPanel headerPanel;

    private ISchedeGiroClientiBD schedeGiroClientiBD;

    /**
     * Costruttore.
     *
     * @param data
     *            data della scheda
     * @param schedeGiroClientiPage
     *            schedeGiroClientiPage
     * @param headerPanel
     *            headerPanel
     */
    public SchedaGiroToggleCommand(final Date data, final SchedeGiroClientiPage schedeGiroClientiPage,
            final HeaderPanel headerPanel) {
        super();
        this.schedeGiroClientiBD = RcpSupport.getBean(SchedeGiroClientiBD.BEAN_ID);
        this.dataScheda = data;
        this.schedePage = schedeGiroClientiPage;
        this.headerPanel = headerPanel;

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        this.giorno = Giorni.values()[dayOfWeek - 1];
    }

    @Override
    public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
            CommandButtonConfigurer configurer) {

        StringBuilder sb = new StringBuilder(100);
        sb.append("<html><p style=\"text-align: center;\">-&nbsp;<b>");
        sb.append(ObjectConverterManager.toString(giorno).toUpperCase());
        sb.append("</b>&nbsp;-<br>");
        sb.append(new SimpleDateFormat("dd/MM/yyyy").format(dataScheda));
        sb.append("<br>");
        sb.append("&nbsp;");
        sb.append("</p></html>");

        JideToggleButton button = (JideToggleButton) super.createButton(faceDescriptorId, buttonFactory, configurer);
        button.setText(sb.toString());
        return button;
    }

    /**
     * @return the giorno
     */
    public Giorni getGiorno() {
        return giorno;
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
        super.onButtonAttached(button);

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (isSelected()) {
                    onSelection(true);
                }
            }
        });
    }

    @Override
    protected boolean onSelection(boolean selected) {
        if (selected) {
            headerPanel.setGiornoCorrente(giorno);

            SchedaGiroClientiDTO schedaGiro = schedeGiroClientiBD
                    .caricaSchedaSettimana(headerPanel.getUtenteSelezionato().getId(), giorno);
            schedePage.getRigheGiroTable().setRows(schedaGiro.getRigheGiroCliente());
        }

        return super.onSelection(selected);
    }

}