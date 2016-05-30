package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Embeddable
public class OrdinamentoFatturazione implements Serializable {
    private static final long serialVersionUID = 5880302072792356152L;

    public static final String CLIENTE_CODICE = "cod. cliente";
    public static final String CLIENTE_DENOMINAZIONE = "denominazione cliente";
    public static final String AGENTE = "agente";
    public static final String ZONA = "zona";
    public static final String SEDE = "sede";

    public static final List<String> ORDINAMENTO_DEFAULT = Arrays.asList(CLIENTE_CODICE, CLIENTE_DENOMINAZIONE, AGENTE,
            ZONA, SEDE);

    private static final Map<String, String> CAMPI_SQL = new ConcurrentHashMap<String, String>(10);

    static {
        CAMPI_SQL.put("cod. cliente", "ent.codice");
        CAMPI_SQL.put("denominazione cliente", "anag.denominazione");
        CAMPI_SQL.put("agente", "entAgente.codice");
        CAMPI_SQL.put("zona", "zone.codice");
        CAMPI_SQL.put("sede", "sa.descrizione,sa.id");
    }

    @Column(length = 100, nullable = true)
    private String ordinamentoFatturazione;

    @Transient
    private List<String> result;

    /**
     *
     * @return lista dei campi ordinati per la fatturazione
     */
    public List<String> getCampiOrdinamento() {
        if (ordinamentoFatturazione == null) {
            ordinamentoFatturazione = "";
        }
        if (result == null) {
            result = Arrays.asList(StringUtils.split(ordinamentoFatturazione, ","));
        }
        return result;
    }

    /**
     *
     * @return ritorna la stringa per l'sql di ordinamento da -
     */
    public String getSqlOrdinamento() {
        if (getCampiOrdinamento().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" order by ");
        for (String campo : getCampiOrdinamento()) {
            sb.append(CAMPI_SQL.get(campo));
            sb.append(",");
        }
        // rimuovo l'ultimo carattere
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

    /**
     *
     * @param campiOrdinati
     *            nuovo ordinamento per i campi
     */
    public void setCampiOrdinamento(List<String> campiOrdinati) {
        // verifico che i campi siano corretti
        for (String campoOrdinamento : campiOrdinati) {
            if (!ORDINAMENTO_DEFAULT.contains(campoOrdinamento)) {
                throw new IllegalArgumentException("il campo " + campoOrdinamento + "non è valido");
            }
        }
        ordinamentoFatturazione = StringUtils.join(campiOrdinati.toArray(), ",");
        result = null;
    }
}
