package it.eurotn.panjea.rich.converter;

import com.jidesoft.converter.ConverterContext;

public class NumberWithDecimalConverterContext extends ConverterContext {

    private static final long serialVersionUID = -2417565294427083434L;
    private String postfisso;
    private Boolean visualizzaZero;

    {
        visualizzaZero = true;
    }

    /**
     * 
     * Costruttore.
     */
    public NumberWithDecimalConverterContext() {
        super("NumberWithDecimalConverterContext");
    }

    /**
     * 
     * Costruttore con i decimali impostati.
     * 
     * @param numDecimali
     *            numer odi decimali per il contesto
     */
    public NumberWithDecimalConverterContext(final Integer numDecimali) {
        super("NumberWithDecimalConverterContext", numDecimali);
    }

    /**
     * 
     * Costruttore.
     * 
     * @param numDecimali
     *            decimali per formattazione
     * @param postfisso
     *            postfisso da aggiungere alla toString
     */
    public NumberWithDecimalConverterContext(final Integer numDecimali, final String postfisso) {
        this(numDecimali);
        this.postfisso = postfisso;
    }

    /**
     * 
     * Costruttore.
     * 
     * @param numDecimali
     *            decimali per formattazione
     * @param postfisso
     *            postfisso da aggiungere alla toString
     * @param visualizzaZero
     *            true per visualizzare valori a zero,false non visualizza valori a zero.
     */
    public NumberWithDecimalConverterContext(final Integer numDecimali, final String postfisso,
            final Boolean visualizzaZero) {
        this(numDecimali, postfisso);
        this.visualizzaZero = visualizzaZero;
    }

    /**
     * @return Returns the postfisso.
     */
    public String getPostfisso() {
        return postfisso;
    }

    /**
     * @return Returns the visualizzaZero.
     */
    public Boolean getVisualizzaZero() {
        return visualizzaZero;
    }

    /**
     * @param postfisso
     *            The postfisso to set.
     */
    public void setPostfisso(String postfisso) {
        this.postfisso = postfisso;
    }

    /**
     * @param visualizzaZero
     *            The visualizzaZero to set.
     */
    public void setVisualizzaZero(Boolean visualizzaZero) {
        this.visualizzaZero = visualizzaZero;
    }

}