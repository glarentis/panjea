package it.eurotn.panjea.contabilita.rich.pm;

import java.io.Serializable;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ApplicationServicesLocator;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ETipologiaConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;

public class StrutturaContabilePM implements IDefProperty, Serializable {

    private static final long serialVersionUID = -5825262862333704418L;

    private static Logger logger = Logger.getLogger(StrutturaContabilePM.class);

    private StrutturaContabile strutturaContabile;

    private SottoConto sottoContoDare;

    private SottoConto sottoContoAvere;
    private ContoBase contoBaseDare;
    private ContoBase contoBaseAvere;
    private boolean entitaDare;
    private boolean entitaAvere;

    /**
     * Costruttore.
     *
     */
    public StrutturaContabilePM() {
        this(new StrutturaContabile());
    }

    /**
     * Costruttore.
     *
     * @param strutturaContabile
     *            struttura contabile
     */
    public StrutturaContabilePM(final StrutturaContabile strutturaContabile) {
        sottoContoDare = new SottoConto();
        sottoContoDare.setCodice("");
        sottoContoDare.getConto().setCodice("");
        sottoContoDare.getConto().getMastro().setCodice("");

        sottoContoAvere = new SottoConto();
        sottoContoAvere.setCodice("");
        sottoContoAvere.getConto().setCodice("");
        sottoContoAvere.getConto().getMastro().setCodice("");

        contoBaseDare = new ContoBase();
        contoBaseDare.setId(-2);

        contoBaseAvere = new ContoBase();
        contoBaseAvere.setId(-2);

        entitaDare = false;
        entitaAvere = false;
        this.strutturaContabile = strutturaContabile;

        if (strutturaContabile.getId() != null) {
            switch (strutturaContabile.getTipologiaConto()) {
            case CONTO:
                String conto;
                if (strutturaContabile.getDare() != null) {
                    conto = strutturaContabile.getDare();
                    String[] contoArray = conto.split("\\.");
                    sottoContoDare.getConto().getMastro().setCodice(contoArray[0]);
                    sottoContoDare.getConto().setCodice(contoArray[1]);
                    sottoContoDare.setCodice(contoArray[2]);
                    sottoContoDare.setDescrizione(strutturaContabile.getDescrizioneDare());
                } else {
                    conto = strutturaContabile.getAvere();
                    String[] contoArray = conto.split("\\.");
                    sottoContoAvere.getConto().getMastro().setCodice(contoArray[0]);
                    sottoContoAvere.getConto().setCodice(contoArray[1]);
                    sottoContoAvere.setCodice(contoArray[2]);
                    sottoContoAvere.setDescrizione(strutturaContabile.getDescrizioneAvere());
                }
                break;
            case CONTO_BASE:
                if (strutturaContabile.getDare() != null) {
                    contoBaseDare.setDescrizione(strutturaContabile.getDare());
                    contoBaseAvere.setDescrizione("");
                } else {
                    contoBaseAvere.setDescrizione(strutturaContabile.getAvere());
                    contoBaseDare.setDescrizione("");
                }
                break;
            case ENTITA:
                if (strutturaContabile.getDare() != null) {
                    entitaDare = true;
                    entitaAvere = false;
                } else {
                    entitaDare = false;
                    entitaAvere = true;
                }
                break;
            default:
                break;
            }
        }
    }

    /**
     * @return contoBaseAvere
     */
    public ContoBase getContoBaseAvere() {
        return contoBaseAvere;
    }

    /**
     * @return contoBaseDare
     */
    public ContoBase getContoBaseDare() {
        return contoBaseDare;
    }

    @Override
    public String getDomainClassName() {
        return strutturaContabile.getDomainClassName();
    }

    /**
     * @return formula
     */
    public String getFormula() {
        return strutturaContabile.getFormula();
    }

    @Override
    public Integer getId() {
        return strutturaContabile.getId();
    }

    /**
     * @return Returns the ordine.
     */
    public Integer getOrdine() {
        return strutturaContabile.getOrdine();
    }

    /**
     * @return sottoContoAvere
     */
    public SottoConto getSottoContoAvere() {
        return sottoContoAvere;
    }

    /**
     * @return sottoContoDare
     */
    public SottoConto getSottoContoDare() {
        return sottoContoDare;
    }

    /**
     * @return strutturaContabile
     */
    public StrutturaContabile getStrutturaContabile() {
        if (strutturaContabile.getTipologiaConto() != null) {
            switch (strutturaContabile.getTipologiaConto()) {
            case CONTO:
                if (!sottoContoDare.isNew()) {
                    strutturaContabile
                            .setDare(sottoContoDare.getSottoContoCodice() + " " + sottoContoDare.getDescrizione());
                    strutturaContabile.setAvere(null);
                } else {
                    if (!sottoContoAvere.isNew()) {
                        strutturaContabile.setAvere(
                                sottoContoAvere.getSottoContoCodice() + " " + sottoContoAvere.getDescrizione());
                        strutturaContabile.setDare(null);
                    }
                }
                break;
            case CONTO_BASE:
                if (contoBaseDare.getId() != -1 && contoBaseDare.getId() != -2) {
                    strutturaContabile.setDare(contoBaseDare.getDescrizione());
                    strutturaContabile.setAvere(null);
                } else {
                    if (contoBaseAvere.getId() != -1 && contoBaseAvere.getId() != -2) {
                        strutturaContabile.setAvere(contoBaseAvere.getDescrizione());
                        strutturaContabile.setDare(null);
                    }
                }
                break;
            case ENTITA:
                MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                        .getService(MessageSource.class);
                if (entitaDare) {
                    strutturaContabile.setDare(
                            messageSource.getMessage(ETipologiaConto.class.getName() + "." + ETipologiaConto.ENTITA,
                                    new Object[] {}, Locale.getDefault()));
                    strutturaContabile.setAvere(null);
                } else {
                    strutturaContabile.setAvere(
                            messageSource.getMessage(ETipologiaConto.class.getName() + "." + ETipologiaConto.ENTITA,
                                    new Object[] {}, Locale.getDefault()));
                    strutturaContabile.setDare(null);
                }
                break;
            default:
                throw new RuntimeException("Tipologia conto non gestita.");
            }
        }
        return strutturaContabile;
    }

    /**
     * @return tipologiaConto
     */
    public ETipologiaConto getTipologiaConto() {
        return strutturaContabile.getTipologiaConto();
    }

    @Override
    public Integer getVersion() {
        return strutturaContabile.getVersion();
    }

    /**
     * @return entitaAvere
     */
    public boolean isEntitaAvere() {
        return entitaAvere;
    }

    /**
     * @return entitaDare
     */
    public boolean isEntitaDare() {
        return entitaDare;
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * Verifica se la struttura contabile contenuta è valida.
     *
     * @return <code>true</code> se valida
     */
    public boolean isValid() {
        logger.debug("--> Enter isValid");

        StrutturaContabile strutturaContabileDaVerificare = getStrutturaContabile();

        if (strutturaContabileDaVerificare.getTipologiaConto() == null) {
            return false;
        } else {
            switch (strutturaContabileDaVerificare.getTipologiaConto()) {
            case CONTO:
                if ((strutturaContabileDaVerificare.getDare() == null
                        || strutturaContabileDaVerificare.getDare().equals("000.000.0000"))
                        && (strutturaContabileDaVerificare.getAvere() == null
                                || strutturaContabileDaVerificare.getAvere().equals("000.000.000000"))) {
                    return false;
                }
            default:
                if (strutturaContabileDaVerificare.getDare() == null
                        && strutturaContabileDaVerificare.getAvere() == null) {
                    return false;
                }
            }

        }
        if (strutturaContabileDaVerificare.getOrdine() == null) {
            return false;
        }

        return true;
    }

    /**
     * @param contoBaseAvere
     *            the contoBaseAvere to set
     */
    public void setContoBaseAvere(ContoBase contoBaseAvere) {
        this.contoBaseAvere = contoBaseAvere;
    }

    /**
     * @param contoBaseDare
     *            the contoBaseDare to set
     */
    public void setContoBaseDare(ContoBase contoBaseDare) {
        this.contoBaseDare = contoBaseDare;
    }

    /**
     * @param entitaAvere
     *            the entitaAvere to set
     */
    public void setEntitaAvere(boolean entitaAvere) {
        this.entitaAvere = entitaAvere;
    }

    /**
     * @param entitaDare
     *            the entitaDare to set
     */
    public void setEntitaDare(boolean entitaDare) {
        this.entitaDare = entitaDare;
    }

    /**
     * @param formula
     *            the formula to set
     */
    public void setFormula(String formula) {
        strutturaContabile.setFormula(formula);
    }

    /**
     * @param ordine
     *            The ordine to set.
     */
    public void setOrdine(Integer ordine) {
        strutturaContabile.setOrdine(ordine);
    }

    /**
     * @param sottoContoAvere
     *            the sottoContoAvere to set
     */
    public void setSottoContoAvere(SottoConto sottoContoAvere) {
        this.sottoContoAvere = sottoContoAvere;
    }

    /**
     * @param sottoContoDare
     *            the sottoContoDare to set
     */
    public void setSottoContoDare(SottoConto sottoContoDare) {
        this.sottoContoDare = sottoContoDare;
    }

    /**
     * @param strutturaContabile
     *            the strutturaContabile to set
     */
    public void setStrutturaContabile(StrutturaContabile strutturaContabile) {
        this.strutturaContabile = strutturaContabile;
    }

    /**
     * @param tipologiaConto
     *            the tipologiaConto to set
     */
    public void setTipologiaConto(ETipologiaConto tipologiaConto) {
        strutturaContabile.setTipologiaConto(tipologiaConto);
    }
}
