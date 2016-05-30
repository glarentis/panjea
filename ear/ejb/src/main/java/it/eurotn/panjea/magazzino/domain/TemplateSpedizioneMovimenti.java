package it.eurotn.panjea.magazzino.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;

@Entity
@Audited
@Table(name = "maga_template_spedizione_movimenti")
public class TemplateSpedizioneMovimenti extends EntityBase {

    private static final long serialVersionUID = 1071859949120922196L;

    public static final String VAR_NUMERO_DOC = "$NumDoc$";
    public static final String VAR_DATA_DOC = "$DataDoc$";
    public static final String VAR_COD_TIPO_DOC = "$CodTipoDoc$";
    public static final String VAR_DESC_TIPO_DOC = "$DescTipoDoc$";
    public static final String VAR_DESC_STAMPA_TIPO_AREA = "$DescPerStampa$";
    public static final String VAR_ENTITA = "$Entita$";

    @Column(length = 120)
    private String oggetto;

    @Lob
    private String testo;

    /**
     * @return the oggetto
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * @return the testo
     */
    public String getTesto() {
        return testo;
    }

    /**
     * @return il template è valido se ci sono testo e oggetto
     */
    public boolean isValid() {
        return !StringUtils.isBlank(testo) && !StringUtils.isBlank(oggetto);
    }

    /**
     * Restituisce il testo sostituendo alle variabili il loro valore.
     *
     * @param text
     *            testo
     * @param areaDocumento
     *            area documento
     * @return testo
     */
    public String replaceVariables(String text, IAreaDocumento areaDocumento) {
        text = StringUtils.replace(text, VAR_DATA_DOC,
                DateFormatUtils.format(areaDocumento.getDocumento().getDataDocumento(), "dd/MM/yyyy"));
        text = StringUtils.replace(text, VAR_NUMERO_DOC, areaDocumento.getDocumento().getCodice().getCodice());
        text = StringUtils.replace(text, VAR_COD_TIPO_DOC, areaDocumento.getDocumento().getTipoDocumento().getCodice());
        text = StringUtils.replace(text, VAR_DESC_TIPO_DOC,
                areaDocumento.getDocumento().getTipoDocumento().getDescrizione());
        text = StringUtils.replace(text, VAR_DESC_STAMPA_TIPO_AREA,
                StringUtils.defaultString(areaDocumento.getTipoAreaDocumento().getDescrizionePerStampa()));
        text = StringUtils.replace(text, VAR_ENTITA,
                areaDocumento.getDocumento().getEntita().getAnagrafica().getDenominazione());

        return text;
    }

    /**
     * @param oggetto
     *            the oggetto to set
     */
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    /**
     * @param testo
     *            the testo to set
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }
}
