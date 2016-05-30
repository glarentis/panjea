package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.giroclienti.rich.editors.scheda.SchedeGiroClientiPage;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class UtentiComboBox extends JComboBox<Utente> {

    private static final long serialVersionUID = -850505248302477587L;

    /**
     * Costruttore.
     *
     * @param schedePage
     *            schedePage
     */
    public UtentiComboBox(final SchedeGiroClientiPage schedePage) {
        super();

        ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        List<Utente> utenti = sicurezzaBD.caricaUtenti("userName", null);
        DefaultComboBoxModel<Utente> model = new DefaultComboBoxModel<Utente>(
                utenti.toArray(new Utente[utenti.size()]));
        setModel(model);
        setRenderer(new UtentiComboBoxRenderer());
        setSelectedItem(sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName()));
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                schedePage.loadData();
            }
        });
    }

}
