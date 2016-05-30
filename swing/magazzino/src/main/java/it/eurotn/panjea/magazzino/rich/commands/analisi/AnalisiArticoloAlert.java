/**
 *
 */
package it.eurotn.panjea.magazzino.rich.commands.analisi;

import java.awt.Point;
import java.util.List;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.ui.tabbedui.VerticalLayout;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.alert.Alert;

import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO;
import it.eurotn.panjea.rich.bd.IParametriRicercaBD;
import it.eurotn.panjea.rich.bd.ParametriRicercaBD;

/**
 * @author fattazzo
 *
 */
public class AnalisiArticoloAlert extends Alert implements Closure {

    private class ParametriPanel extends JPanel {

        private static final long serialVersionUID = 5362730201007885011L;

        private Integer idArticolo;
        private Integer idEntita;

        /**
         * Costruttore.
         */
        public ParametriPanel() {
            super(new VerticalLayout());
        }

        /**
         * @return the idArticolo
         */
        public Integer getIdArticolo() {
            return idArticolo;
        }

        /**
         * @return the idEntita
         */
        public Integer getIdEntita() {
            return idEntita;
        }

        /**
         * @param idArticolo
         *            the idArticolo to set
         */
        public void setIdArticolo(Integer idArticolo) {
            this.idArticolo = idArticolo;
        }

        /**
         * @param idEntita
         *            the idEntita to set
         */
        public void setIdEntita(Integer idEntita) {
            this.idEntita = idEntita;
        }
    }

    private static final long serialVersionUID = -4755451610544558235L;

    private JComponent ownerComponent;

    private Integer idArticolo;
    private Integer idEntita;

    private IParametriRicercaBD parametriBD;

    private ParametriPanel rootPanel;

    {
        parametriBD = RcpSupport.getBean(ParametriRicercaBD.BEAN_ID);
    }

    /**
     * Costruttore.
     *
     * @param ownerComponent
     *            owner
     */
    public AnalisiArticoloAlert(final JComponent ownerComponent) {
        super();
        setName("analisiArticoloAlert");
        setFocusable(false);
        setAlwaysOnTop(false);

        this.ownerComponent = ownerComponent;

        rootPanel = new ParametriPanel();
        getContentPane().add(rootPanel);
    }

    @Override
    public Object call(Object arg0) {
        hidePopup();
        return null;
    }

    private void caricaParametri() {
        rootPanel.setIdArticolo(idArticolo);
        rootPanel.setIdEntita(idEntita);
        rootPanel.removeAll();

        List<ParametriRicercaDTO> parametriMagazzino = parametriBD
                .caricaParametri(it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione.class);
        if (!parametriMagazzino.isEmpty()) {
            ParametroRicercaComponent parametriComponent = new ParametroRicercaComponent("Analisi di magazzino",
                    RcpSupport.getIcon("openMovimentazioneMagazzinoCommand.icon"), parametriMagazzino,
                    it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione.class, idArticolo, idEntita, this);
            rootPanel.add(parametriComponent);
        }

        List<ParametriRicercaDTO> parametriOrdini = parametriBD.caricaParametri(ParametriRicercaMovimentazione.class);
        if (!parametriOrdini.isEmpty()) {
            ParametroRicercaComponent parametriComponent = new ParametroRicercaComponent("Analisi ordini",
                    RcpSupport.getIcon("openMovimentazioneMagazzinoOrdiniCommand.icon"), parametriOrdini,
                    ParametriRicercaMovimentazione.class, idArticolo, idEntita, this);
            rootPanel.add(parametriComponent);
        }
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        this.idEntita = idEntita;
    }

    @Override
    public void showPopup() {
        // se idarticolo e identita sono uguali non ricarico i parametri ma visualizzo quelli che ci
        // sono già
        if (!Objects.equals(idEntita, rootPanel.getIdEntita())
                || !Objects.equals(idArticolo, rootPanel.getIdArticolo())) {
            caricaParametri();
        }

        Point pt = ownerComponent.getLocationOnScreen();
        showPopup(pt.x, pt.y + ownerComponent.getHeight());
    }

}
