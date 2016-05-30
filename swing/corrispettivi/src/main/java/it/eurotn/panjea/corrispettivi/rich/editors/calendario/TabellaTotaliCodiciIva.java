package it.eurotn.panjea.corrispettivi.rich.editors.calendario;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.TotaliCodiceIvaDTO;
import it.eurotn.panjea.corrispettivi.rich.bd.ICorrispettiviBD;

/**
 * Tabella che presenta i totali importo dei codici iva disponibili per tipo documento nell'editor giornale
 * corrispettivi, prende in considerazione anche i documenti esistenti creati manualmente dello stesso tipo documento.
 *
 * @author Fattazzo,Leonardo
 */
public class TabellaTotaliCodiciIva extends JPanel {

    private static final long serialVersionUID = 2481643493425493410L;
    private static final String TABELLA_TOTALI_IVA_CODICE_IVA = CalendarioCorrispettivoPage.PAGE_ID
            + ".tabellaTotaliCodiciIva.codiceIva.label";
    private static final String TABELLA_TOTALI_IVA_DESCRIZIONE_IVA = CalendarioCorrispettivoPage.PAGE_ID
            + ".tabellaTotaliCodiciIva.descrizioneIva.label";
    private static final String TABELLA_TOTALI_IVA_IMPORTO = CalendarioCorrispettivoPage.PAGE_ID
            + ".tabellaTotaliCodiciIva.importo.label";
    private static final String TABELLA_TOTALI_IVA_TOTALE = CalendarioCorrispettivoPage.PAGE_ID
            + ".tabellaTotaliCodiciIva.totale.label";
    private List<TotaliCodiceIvaDTO> listTotaliCodiceIvaDTO;

    private ICorrispettiviBD corrispettiviBD = null;

    private ComponentFactory componentFactory = null;
    private MessageSource messageSource = null;

    /**
     * Costruttore.
     */
    public TabellaTotaliCodiciIva() {
        super();
        initialize();
    }

    /**
     * Costruttore.
     *
     * @param calendarioCorrispettivo
     *            calendarioCorrispettivo
     * @param corrispettiviBD
     *            corrispettiviBD
     */
    public TabellaTotaliCodiciIva(final CalendarioCorrispettivo calendarioCorrispettivo,
            final ICorrispettiviBD corrispettiviBD) {
        this();
        this.corrispettiviBD = corrispettiviBD;
        listTotaliCodiceIvaDTO = this.corrispettiviBD.caricaTotaliCalendarioCorrispettivi(calendarioCorrispettivo);
        initialize();
        builUI();
    }

    /**
     * Creo le intestazioni.
     */
    private void addIntestazioni() {
        JLabel labelCodiceIva = componentFactory.createTitleLabel(
                messageSource.getMessage(TABELLA_TOTALI_IVA_CODICE_IVA, new Object[] {}, Locale.getDefault()));
        labelCodiceIva.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel labelDescIva = componentFactory.createTitleLabel(
                messageSource.getMessage(TABELLA_TOTALI_IVA_DESCRIZIONE_IVA, new Object[] {}, Locale.getDefault()));
        labelDescIva.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel labelImporto = componentFactory.createTitleLabel(
                messageSource.getMessage(TABELLA_TOTALI_IVA_IMPORTO, new Object[] {}, Locale.getDefault()));
        labelImporto.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelCodiceIva = componentFactory.createPanel(new BorderLayout());
        panelCodiceIva.add(labelCodiceIva, BorderLayout.CENTER);
        panelCodiceIva.add(new JSeparator(), BorderLayout.SOUTH);
        this.add(panelCodiceIva);

        JPanel panelDescIva = componentFactory.createPanel(new BorderLayout());
        panelDescIva.add(labelDescIva, BorderLayout.CENTER);
        panelDescIva.add(new JSeparator(), BorderLayout.SOUTH);
        this.add(panelDescIva);

        JPanel panelImporto = componentFactory.createPanel(new BorderLayout());
        panelImporto.add(labelImporto, BorderLayout.CENTER);
        panelImporto.add(new JSeparator(), BorderLayout.SOUTH);
        this.add(panelImporto);
    }

    /**
     * Imposto il totale.
     *
     * @param totale
     *            totale
     */
    private void addTotale(BigDecimal totale) {

        JLabel labelDescTotale = componentFactory.createTitleLabel(
                messageSource.getMessage(TABELLA_TOTALI_IVA_TOTALE, new Object[] {}, Locale.getDefault()));
        Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
        JLabel labelTotale = componentFactory.createTitleLabel(format.format(totale));
        labelTotale.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel panelDescTotale = componentFactory.createPanel(new BorderLayout());
        panelDescTotale.add(labelDescTotale, BorderLayout.CENTER);
        panelDescTotale.add(new JSeparator(), BorderLayout.NORTH);
        this.add(panelDescTotale);

        JPanel panelVuoto = componentFactory.createPanel(new BorderLayout());
        panelVuoto.add(componentFactory.createLabel(""), BorderLayout.CENTER);
        panelVuoto.add(new JSeparator(), BorderLayout.NORTH);
        this.add(panelVuoto);

        JPanel panelTotale = componentFactory.createPanel(new BorderLayout());
        panelTotale.add(labelTotale, BorderLayout.CENTER);
        panelTotale.add(new JSeparator(), BorderLayout.NORTH);
        this.add(panelTotale);
    }

    /**
     * Creo la tabella riassuntiva per ogni codice iva presente con importo per riga e importo totale.
     */
    private void builUI() {
        // pulisco il pannello da tutti i componenti
        this.removeAll();

        if (listTotaliCodiceIvaDTO.size() > 0) {
            // creo il layout
            GridLayout layout = new GridLayout(listTotaliCodiceIvaDTO.size() + 2, 3, 2, 2);
            this.setLayout(layout);

            addIntestazioni();

            // aggiungo i dati
            BigDecimal totale = BigDecimal.ZERO;
            for (TotaliCodiceIvaDTO totaliCodiceIvaDTO : listTotaliCodiceIvaDTO) {
                JLabel labelCodiceIva = componentFactory.createLabel(totaliCodiceIvaDTO.getCodiceIva());
                labelCodiceIva.setHorizontalAlignment(SwingConstants.LEFT);

                JLabel labelDescIva = componentFactory.createLabel(totaliCodiceIvaDTO.getDescrizioneRegistro());
                labelDescIva.setHorizontalAlignment(SwingConstants.LEFT);

                Format format = new DecimalFormat(ValutaAzienda.MASCHERA_VALUTA_GENERICA);
                JLabel labelImporto = componentFactory.createLabel(format.format(totaliCodiceIvaDTO.getTotale()));
                labelImporto.setHorizontalAlignment(SwingConstants.RIGHT);

                this.add(labelCodiceIva);
                this.add(labelDescIva);
                this.add(labelImporto);

                totale = totale.add(totaliCodiceIvaDTO.getTotale());
            }

            addTotale(totale);
        }
    }

    /**
     * Inizializza le risorse.
     */
    private void initialize() {
        componentFactory = (ComponentFactory) Application.services().getService(ComponentFactory.class);
        messageSource = (MessageSource) Application.services().getService(MessageSource.class);
    }

    /**
     * Aggiorna la tabella di riepilogo totali importo per codice iva.
     *
     * @param calendarioCorrispettivo
     *            calendarioCorrispettivo
     */
    public void setCalendarioCorrispettivo(CalendarioCorrispettivo calendarioCorrispettivo) {
        listTotaliCodiceIvaDTO = corrispettiviBD.caricaTotaliCalendarioCorrispettivi(calendarioCorrispettivo);
        builUI();
    }

    /**
     * @param corrispettiviBD
     *            the corrispettiviBD to set
     */
    public void setCorrispettiviBD(ICorrispettiviBD corrispettiviBD) {
        this.corrispettiviBD = corrispettiviBD;
    }
}