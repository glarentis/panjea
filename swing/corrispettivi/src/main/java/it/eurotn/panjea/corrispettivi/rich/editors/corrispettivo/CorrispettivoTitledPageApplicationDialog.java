package it.eurotn.panjea.corrispettivi.rich.editors.corrispettivo;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.GiornoCorrispettivo;
import it.eurotn.panjea.corrispettivi.rich.bd.ICorrispettiviBD;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 * Dialogo di inserimento dei corrispettivi che contiene una page ({@link CorrispettivoPage}) che presenta un campo
 * importo totale e i dati sotto forma di tabella editabile in cui si inseriscono gli importi per ogni singola riga dove
 * la somma degli importi non deve superare il totale.
 *
 * @author Fattazzo,Leonardo
 */
public class CorrispettivoTitledPageApplicationDialog extends PanjeaTitledPageApplicationDialog {

    /**
     * PropertyChange che chiude il dialogo corrente attivato da CorrispettivoPage quando sono attivati degli
     * automatismi che compilano in automatico alcuni dati.
     */
    private class ClosePropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            CorrispettivoTitledPageApplicationDialog.this.getFinishCommand().execute();
            CorrispettivoTitledPageApplicationDialog.this.dispose();
        }

    }

    private static final Logger LOGGER = Logger.getLogger(CorrispettivoTitledPageApplicationDialog.class);

    private ICorrispettiviBD corrispettiviBD = null;
    private CorrispettivoPage corrispettivoPage = null;
    private GiornoCorrispettivo giornoCorrispettivo = null;
    private BigDecimal totale = null;
    private boolean confirmed = false;

    /**
     * Costruttore.
     *
     * @param corrispettivoPage
     *            corrispettivoPage
     * @param giornoCorrispettivo
     *            giornoCorrispettivo
     * @param corrispettiviBD
     *            corrispettiviBD
     */
    public CorrispettivoTitledPageApplicationDialog(final CorrispettivoPage corrispettivoPage,
            final GiornoCorrispettivo giornoCorrispettivo, final ICorrispettiviBD corrispettiviBD) {
        super(corrispettivoPage);
        this.corrispettiviBD = corrispettiviBD;
        this.giornoCorrispettivo = giornoCorrispettivo;
        this.corrispettivoPage = corrispettivoPage;
        this.corrispettivoPage.addPropertyChangeListener(CorrispettivoPage.CLOSE_PROPERTY_CHANGE,
                new ClosePropertyChangeListener());
    }

    /**
     * Effettua il dispose della pagina.
     */
    public void disposeDialog() {
        super.dispose();
    }

    /**
     * @return the giornoCorrispettivo
     */
    public GiornoCorrispettivo getGiornoCorrispettivo() {
        return giornoCorrispettivo;
    }

    @Override
    protected Dimension getPreferredSize() {
        return new Dimension(500, 350);
    }

    @Override
    protected String getTitle() {
        return corrispettivoPage.getTitle();
    }

    /**
     * Valore di totale corrispettivo impostato sull'onFinish di questo dialog.
     *
     * @return il totale del corrispettivo
     */
    public BigDecimal getTotale() {
        return totale;
    }

    /**
     * @return the confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    protected void onCancel() {
        setConfirmed(false);
        super.onCancel();
    }

    @Override
    protected boolean onFinish() {
        LOGGER.debug("--> Enter onFinish");
        Corrispettivo corrispettivo = corrispettivoPage.getCorrispettivo();
        corrispettivo = corrispettiviBD.salvaCorrispettivo(corrispettivo);
        giornoCorrispettivo.setCorrispettivo(corrispettivo);
        setTotale(corrispettivo.getTotale());
        setConfirmed(true);
        LOGGER.debug("--> Exit onFinish");
        return isConfirmed();
    }

    /**
     * @param confirmed
     *            the confirmed to set
     */
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    /**
     * @param totale
     *            the totale to set
     */
    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

}