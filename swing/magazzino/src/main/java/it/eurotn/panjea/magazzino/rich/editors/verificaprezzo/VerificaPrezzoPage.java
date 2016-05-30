package it.eurotn.panjea.magazzino.rich.editors.verificaprezzo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.plaf.xerto.VerticalLabelUI;
import com.jidesoft.swing.JideScrollPane;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoModuloPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.rich.control.DropShadowCollapsiblePane;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * Pagina per visualizzare la politica prezzo per un articolo.
 *
 * @author giangi
 *
 */
public class VerificaPrezzoPage extends AbstractDialogPage implements IPageLifecycleAdvisor, Focussable {

    public static final String STR_ZERI = "0000000000000000000000000";
    public static final String NUMBER_FORMAT = "###,###,###,##0";
    private static final String PAGE_ID = "verificaPrezzoPage";
    private JPanel rootPanel;
    private ParametriCalcoloPrezziPM parametriCalcoloPrezziPM;
    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    /**
     * Costruttore.
     */
    public VerificaPrezzoPage() {
        super(PAGE_ID);
    }

    /**
     *
     * @param scaglione
     *            scaglione interessato
     * @param politicaPrezzo
     *            risultato calcolo prezzo
     * @return pannello per contenere i risultati per lo scaglione
     */
    private JPanel creaPannelloScaglione(Double scaglione, PoliticaPrezzo politicaPrezzo) {
        // trovo lo scaglione successivo
        // Double scaglioneSuccessivo = null;
        // Double[] scaglioni = politicaPrezzo.getScaglioni().toArray(new Double[0]);
        // for (int i = 0; i < scaglioni.length; i++) {
        // if (scaglioni[i] == scaglione && i < scaglioni.length - 1) {
        // scaglioneSuccessivo = scaglioni[i + 1];
        // break;
        // }
        // }
        // String descrizioneScaglione = "";
        // Format format = new DecimalFormat("#0");
        // if (scaglione == 0 && scaglioneSuccessivo == null) {
        // descrizioneScaglione = "NESSUN SCAGLIONE IMPOSTATO";
        // } else if (scaglioneSuccessivo != null) {
        // descrizioneScaglione = "DA " + format.format(scaglione) + " A " +
        // format.format(scaglioneSuccessivo - 1);
        // } else {
        // descrizioneScaglione = "OLTRE " + format.format(scaglione);
        // }

        StringBuilder descrizioneScaglione = new StringBuilder(100);
        if (scaglione == 0) {
            descrizioneScaglione.append("NESSUNO SCAGLIONE IMPOSTATO");
        } else if (ScaglioneListino.MAX_SCAGLIONE.equals(scaglione)) {
            descrizioneScaglione.append(ScaglioneListino.MAX_SCAGLIONE_LABEL);
        } else {
            descrizioneScaglione.append("FINO A ").append(scaglione);
        }
        DropShadowCollapsiblePane panelScaglione = new DropShadowCollapsiblePane(new BorderLayout());
        JLabel titleLabel = new JLabel(descrizioneScaglione.toString());
        Font font = titleLabel.getFont();
        titleLabel.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        titleLabel.setUI(new VerticalLabelUI(false));
        titleLabel.setName(StringUtils.deleteWhitespace(descrizioneScaglione.toString()));
        panelScaglione.setTitleComponent(titleLabel);
        panelScaglione.setSlidingDirection(SwingConstants.EAST);
        panelScaglione.setStyle(CollapsiblePane.DROPDOWN_STYLE);
        panelScaglione.setEmphasized(true);
        panelScaglione.setShowExpandButton(false);
        panelScaglione.setContentAreaFilled(false);
        panelScaglione.setBackground(new Color(201, 223, 245));
        panelScaglione.getContentPane().setBorder(BorderFactory.createEtchedBorder());
        panelScaglione.setBorder(BorderFactory.createRaisedBevelBorder());
        return panelScaglione;
    }

    @Override
    protected JComponent createControl() {
        rootPanel = new CollapsiblePanes();
        rootPanel.setLayout(new HorizontalLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder());
        rootPanel.setOpaque(false);
        return new JideScrollPane(rootPanel);
    }

    @Override
    public void dispose() {
        rootPanel.removeAll();
        rootPanel = null;
    }

    /**
     * formatta un bigDecimal .
     *
     * @param value
     *            valore da formattare
     * @param numeroDecimali
     *            numero decimali
     * @return stringa formattata
     */
    private String formatta(BigDecimal value, int numeroDecimali) {
        Format format = new DecimalFormat(NUMBER_FORMAT + "." + STR_ZERI.substring(0, numeroDecimali));
        return format.format(value);
    }

    /**
     * @return Returns the magazzinoAnagraficaBD.
     */
    public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
        return magazzinoAnagraficaBD;
    }

    /**
     * @return the magazzinoDocumentoBD
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    @Override
    public void grabFocus() {
    }

    @Override
    public void loadData() {
        if (parametriCalcoloPrezziPM.getArticolo() == null) {
            rootPanel.removeAll();
            return;
        }
        this.setVisible(true);
        if (parametriCalcoloPrezziPM.isEffettuaRicerca()) {
            PoliticaPrezzo politicaPrezzo = magazzinoDocumentoBD
                    .calcolaPrezzoArticolo(parametriCalcoloPrezziPM.getParametriCalcoloPrezzo());

            rootPanel.removeAll();

            // Carico l'iva per l'articolo
            Articolo articoloSearchObject = parametriCalcoloPrezziPM.getArticolo().creaProxyArticolo();

            // Carico un articolo lite perchè l'articolo nei parametriCalcoloPrezzo non ha tutti i
            // valori settati
            ArticoloLite articolo = magazzinoAnagraficaBD.caricaArticoloLite(articoloSearchObject.getId());

            // Carico una politica prezzo con la provenienza da ultimo costo
            ParametriCalcoloPrezziPM calcoloPrezziPMUltimoCosto = parametriCalcoloPrezziPM.clone();
            calcoloPrezziPMUltimoCosto.setProvenienzaPrezzo(ProvenienzaPrezzo.ULTIMO_COSTO);
            PoliticaPrezzo politicaPrezzoUltimoCosto = magazzinoDocumentoBD
                    .calcolaPrezzoArticolo(calcoloPrezziPMUltimoCosto.getParametriCalcoloPrezzo());
            RisultatoPrezzo<BigDecimal> risultatoUltimoCosto = politicaPrezzoUltimoCosto.getPrezzi()
                    .getRisultatoPrezzo(0.0);

            String ultimoCostoString = "0";
            BigDecimal ultimoCosto = BigDecimal.ZERO;
            if (risultatoUltimoCosto != null) {
                ultimoCosto = risultatoUltimoCosto.getValue();
                ultimoCostoString = formatta(ultimoCosto, articolo.getNumeroDecimaliPrezzo());
            }

            // ciclo sugli scaglioni di prezzo solamente, gli sconti sono inclusi
            for (Double scaglione : politicaPrezzo.getPrezzi().keySet()) {

                RisultatoPrezzo<BigDecimal> prezzo = politicaPrezzo.getPrezzi().getRisultatoPrezzo(scaglione);
                RisultatoPrezzo<Sconto> variazione = politicaPrezzo.getSconti().getRisultatoPrezzo(scaglione);
                RisultatoPrezzo<BigDecimal> provvigione = politicaPrezzo.getProvvigioni().getRisultatoPrezzo(scaglione);

                JPanel panelScaglione = creaPannelloScaglione(scaglione, politicaPrezzo);
                String prezzoNettoString = "n.d.";
                String prezzoIvatoString = "Cod. iva non definito";
                String ricaricoString = "n.d.";
                String percRicaricoString = "n.d.";
                String percScontoMassimoString = "n.d.";

                BigDecimal prezzoNetto = politicaPrezzo.getPrezzoNetto(scaglione,
                        articolo.getCodiceIva().getPercApplicazione());
                if (prezzoNetto != null && prezzo != null) {
                    if (!ultimoCosto.equals(BigDecimal.ZERO)) {
                        BigDecimal ricarico = prezzoNetto.subtract(ultimoCosto);
                        ricarico = ricarico.setScale(prezzo.getNumeroDecimali(), BigDecimal.ROUND_HALF_UP);
                        BigDecimal percRicarico = ricarico.divide(ultimoCosto, RoundingMode.HALF_UP)
                                .multiply(Importo.HUNDRED);
                        percRicarico = percRicarico.setScale(2, BigDecimal.ROUND_HALF_UP);

                        BigDecimal percScontoMassimo = BigDecimal.ZERO;
                        if (prezzoNetto.compareTo(BigDecimal.ZERO) != 0) {
                            percScontoMassimo = ricarico.multiply(Importo.HUNDRED).divide(prezzoNetto,
                                    BigDecimal.ROUND_HALF_UP);
                            percScontoMassimo = percScontoMassimo.setScale(4, BigDecimal.ROUND_HALF_UP);
                            percScontoMassimoString = formatta(percScontoMassimo, 4);
                        }

                        ricaricoString = formatta(ricarico, prezzo.getNumeroDecimali());
                        percRicaricoString = formatta(percRicarico, 2).concat(" %");

                    }

                    prezzoNettoString = formatta(prezzoNetto, prezzo.getNumeroDecimali());

                    if (articolo.getCodiceIva() != null && prezzo != null) {
                        BigDecimal prezzoIvato = BigDecimal.ZERO;
                        if (politicaPrezzo.isPrezzoIvato()) {
                            prezzoIvato = prezzo.getValue();
                        } else {
                            prezzoIvato = articolo.getCodiceIva().applica(prezzoNetto, prezzo.getNumeroDecimali());
                        }
                        prezzoIvatoString = formatta(prezzoIvato, prezzo.getNumeroDecimali());
                    }
                }

                JPanel panelDescrizioneModuliPrezzo = new JPanel(new VerticalLayout());
                panelDescrizioneModuliPrezzo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                panelDescrizioneModuliPrezzo.setOpaque(false);
                JPanel panelDescrizioneModuliVariazione = new JPanel(new VerticalLayout());
                panelDescrizioneModuliVariazione.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                panelDescrizioneModuliVariazione.setOpaque(false);
                JPanel panelDescrizioneModuliProvvigione = new JPanel(new VerticalLayout());
                panelDescrizioneModuliProvvigione.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                panelDescrizioneModuliProvvigione.setOpaque(false);

                JPanel pannelloScaglioneContent = new JPanel(new BorderLayout());
                pannelloScaglioneContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                pannelloScaglioneContent.setOpaque(false);

                pannelloScaglioneContent.add(new RisultatoPanel(prezzo, variazione, provvigione, prezzoNettoString,
                        prezzoIvatoString, ultimoCostoString, ricaricoString, percRicaricoString,
                        percScontoMassimoString, articolo.getUnitaMisura().getCodice()), BorderLayout.NORTH);

                boolean added = false;
                // pannello per calcolo prezzo
                if (prezzo != null) {
                    JLabel lblIntestazioneStategiaPrezzo = new JLabel("<html><b>STRATEGIA PREZZO</b></html>",
                            SwingConstants.CENTER);
                    lblIntestazioneStategiaPrezzo.setBorder(BorderFactory.createRaisedBevelBorder());
                    lblIntestazioneStategiaPrezzo.setOpaque(true);
                    for (RisultatoModuloPrezzo<BigDecimal> risultatoModuloPrezzo : prezzo.getRisultatiModuloPrezzo()) {
                        if (!risultatoModuloPrezzo.getTipoModulo().equals("MODULO_FILLER")
                                && risultatoModuloPrezzo.getStrategia() != null) {
                            DescrizioneCalcoloModuloPrezzoPanel<BigDecimal> pannelloCalcoloModuloPrezzo = new DescrizioneCalcoloModuloPrezzoPanel<BigDecimal>(
                                    risultatoModuloPrezzo);
                            panelDescrizioneModuliPrezzo.add((pannelloCalcoloModuloPrezzo));
                            added = true;
                        }
                    }

                    if (added) {
                        panelDescrizioneModuliPrezzo.add(lblIntestazioneStategiaPrezzo, 0);
                    }
                }

                // Pannelli per calcolo variazione
                if (variazione != null) {
                    JLabel lblIntestazioneStategiaVariazione = new JLabel("<html><b>STRATEGIA VARIAZIONE</b></html>",
                            SwingConstants.CENTER);
                    lblIntestazioneStategiaVariazione.setBorder(BorderFactory.createRaisedBevelBorder());
                    lblIntestazioneStategiaVariazione.setOpaque(true);
                    panelDescrizioneModuliVariazione.add(lblIntestazioneStategiaVariazione);
                    for (RisultatoModuloPrezzo<Sconto> risultatoModuloVariazione : variazione
                            .getRisultatiModuloPrezzo()) {
                        if (!risultatoModuloVariazione.getTipoModulo().equals("MODULO_FILLER")) {
                            DescrizioneCalcoloModuloPrezzoPanel<Sconto> pannelloCalcoloModuloPrezzo = new DescrizioneCalcoloModuloPrezzoPanel<Sconto>(
                                    risultatoModuloVariazione);
                            panelDescrizioneModuliVariazione.add((pannelloCalcoloModuloPrezzo));
                        }
                    }
                }

                // Pannelli per calcolo della provvigione
                if (provvigione != null) {
                    JLabel lblIntestazioneStategiaProvvigione = new JLabel("<html><b>PROVVIGIONE AGENTE</b></html>",
                            SwingConstants.CENTER);
                    lblIntestazioneStategiaProvvigione.setBorder(BorderFactory.createRaisedBevelBorder());
                    lblIntestazioneStategiaProvvigione.setOpaque(true);
                    panelDescrizioneModuliProvvigione.add(lblIntestazioneStategiaProvvigione);
                    for (RisultatoModuloPrezzo<BigDecimal> risultatoModuloProvvigione : provvigione
                            .getRisultatiModuloPrezzo()) {
                        if (!risultatoModuloProvvigione.getTipoModulo().equals("MODULO_FILLER")) {
                            DescrizioneCalcoloModuloPrezzoPanel<BigDecimal> pannelloCalcoloModuloProvvigione = new DescrizioneCalcoloModuloPrezzoPanel<BigDecimal>(
                                    risultatoModuloProvvigione);
                            panelDescrizioneModuliProvvigione.add((pannelloCalcoloModuloProvvigione));
                        }
                    }
                }

                pannelloScaglioneContent.add(panelDescrizioneModuliPrezzo, BorderLayout.WEST);
                pannelloScaglioneContent.add(panelDescrizioneModuliVariazione, BorderLayout.CENTER);
                pannelloScaglioneContent.add(panelDescrizioneModuliProvvigione, BorderLayout.EAST);
                panelScaglione.add(pannelloScaglioneContent, BorderLayout.CENTER);
                rootPanel.add(panelScaglione);
            }

            rootPanel.repaint();
        }
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {

    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setFormObject(Object object) {
        parametriCalcoloPrezziPM = (ParametriCalcoloPrezziPM) object;
    }

    /**
     * @param magazzinoAnagraficaBD
     *            The magazzinoAnagraficaBD to set.
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    /**
     * @param magazzinoDocumentoBD
     *            the magazzinoDocumentoBD to set
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}
