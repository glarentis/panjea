package it.eurotn.panjea.magazzino.rich.editors.verificaprezzo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.CodicePagamentoModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.ContrattoModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.ListinoModuloPrezzoCalculator;
import it.eurotn.panjea.magazzino.manager.moduloprezzo.ListinoTipoMezzoZonaGeograficaPrezzoCalculator;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

/**
 * Classe che visualizza una <code>DescrizioneCalcoloModuloPrezzo</code> opportunamente formattata.
 *
 * @author fattazzo
 *
 */
public class DescrizioneCalcoloModuloPrezzoPanel<T> extends JXPanel {

    private static final float CHANNEL_ALPHA = .5f;

    private static final long serialVersionUID = 1L;

    private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

    private final RisultatoModuloPrezzo<T> risultatoModuloPrezzo;

    /**
     *
     * @param risultatoModuloPrezzo
     *            risultato da renderizzare nel pannello.
     */
    public DescrizioneCalcoloModuloPrezzoPanel(final RisultatoModuloPrezzo<T> risultatoModuloPrezzo) {
        super(new BorderLayout());
        setBorder(BorderFactory.createRaisedBevelBorder());
        this.risultatoModuloPrezzo = risultatoModuloPrezzo;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (!risultatoModuloPrezzo.isAbilitato()) {
            setAlpha(CHANNEL_ALPHA);
        }
        createPanelControl();
        this.setMinimumSize(new Dimension(245, 100));
        this.setPreferredSize(new Dimension(245, 100));
    }

    /**
     * creazione dei controlli.
     */
    private void createPanelControl() {
        this.add(getPanelControl(), BorderLayout.CENTER);
        this.add(getStrategiaControl(), BorderLayout.WEST);
    }

    /**
     *
     * @return controlli per l'header
     */
    private JComponent getPanelControl() {
        JPanel panel = new JPanel(new VerticalLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        JLabel labelCodiceModulo = new JLabel(risultatoModuloPrezzo.getCodiceModulo());
        Font f = labelCodiceModulo.getFont();
        // bold
        labelCodiceModulo.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));

        // panel.add(labelCodiceModulo);

        StringBuilder descrizioneBuilder = new StringBuilder();
        descrizioneBuilder.append("<html><b>");
        descrizioneBuilder.append(risultatoModuloPrezzo.getCodiceModulo());
        descrizioneBuilder.append("</b> - ");
        descrizioneBuilder.append(this.risultatoModuloPrezzo.getDescrizioneModulo());
        descrizioneBuilder.append("<br>");
        descrizioneBuilder.append("<b>Valore: </b>");
        descrizioneBuilder.append(this.risultatoModuloPrezzo.getStringValue());
        descrizioneBuilder.append("<br>");
        descrizioneBuilder.append("<b>Soglia: </b>");
        descrizioneBuilder.append(this.risultatoModuloPrezzo.getQuantita());
        descrizioneBuilder.append("</html>");

        JLabel labelDettaglio = new JLabel(descrizioneBuilder.toString());
        labelDettaglio.setName("strategiaPrezzoLabel");
        panel.add(labelDettaglio);
        return panel;
    }

    /**
     *
     * @return controlli per la strategia
     */
    private JComponent getStrategiaControl() {
        JPanel panelStrategiaControl = new JPanel(new BorderLayout());
        panelStrategiaControl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel labelIconaTipoModulo = new JLabel();

        if (ListinoModuloPrezzoCalculator.TIPO_MODULO.equals(risultatoModuloPrezzo.getTipoModulo())) {
            labelIconaTipoModulo.setIcon(RcpSupport.getIcon(Listino.class.getName()));
            panelStrategiaControl.setBackground(new Color(255, 240, 245));
            setBackground(new Color(255, 240, 245));
        } else {
            if (ContrattoModuloPrezzoCalculator.TIPO_MODULO.equals(risultatoModuloPrezzo.getTipoModulo())) {
                labelIconaTipoModulo.setIcon(RcpSupport.getIcon(Contratto.class.getName()));
                panelStrategiaControl.setBackground(new Color(153, 255, 255));
                setBackground(new Color(153, 255, 255));
            } else {
                if (ListinoTipoMezzoZonaGeograficaPrezzoCalculator.TIPO_MODULO
                        .equals(risultatoModuloPrezzo.getTipoModulo())) {
                    labelIconaTipoModulo.setIcon(RcpSupport.getIcon(TipoMezzoTrasporto.class.getName()));
                    panelStrategiaControl.setBackground(new Color(190, 225, 105));
                    setBackground(new Color(190, 225, 105));
                } else {
                    if (CodicePagamentoModuloPrezzoCalculator.TIPO_MODULO
                            .equals(risultatoModuloPrezzo.getTipoModulo())) {
                        labelIconaTipoModulo.setIcon(RcpSupport.getIcon(CodicePagamento.class.getName()));
                        panelStrategiaControl.setBackground(new Color(102, 204, 102));
                        setBackground(new Color(102, 204, 102));
                    }
                }
            }
        }

        panelStrategiaControl.add(labelIconaTipoModulo, BorderLayout.NORTH);

        String descrizioneStrategira = "";
        JLabel labelStrategia = new JLabel("");
        if (this.risultatoModuloPrezzo.getStrategia() != null) {
            descrizioneStrategira = messageSource
                    .getMessage(
                            this.risultatoModuloPrezzo.getStrategia().getClass().getName() + "."
                                    + this.risultatoModuloPrezzo.getStrategia().name(),
                            new Object[] {}, Locale.getDefault());
            labelStrategia.setText("<html><FONT size=5>" + descrizioneStrategira.substring(0, 1) + "</font></html>");
        }

        panelStrategiaControl.add(labelStrategia, BorderLayout.SOUTH);
        return panelStrategiaControl;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

    }

}
