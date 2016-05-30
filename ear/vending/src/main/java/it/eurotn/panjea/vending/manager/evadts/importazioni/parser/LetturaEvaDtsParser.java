package it.eurotn.panjea.vending.manager.evadts.importazioni.parser;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder.EvaDtsFieldIDName;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.domain.evadts.RilevazioniFasceEva;

public class LetturaEvaDtsParser {

    private static final Logger LOGGER = Logger.getLogger(LetturaEvaDtsParser.class);

    private static final String FIELD_SEPARATOR = "*";

    private int ultimaRiga;

    private EvaDtsImportFolder evaDtsImportFolder;

    private List<String> righe = new ArrayList<>();

    /**
     * Aggiunge una riga al parser.
     *
     * @param riga
     *            riga
     */
    public void addRiga(String riga) {
        righe.add(riga);
    }

    private void calcolaCampoCA302(RilevazioneEvaDts rilevazione) {

        if (evaDtsImportFolder.isCalcolaCampoCA302()
                && ObjectUtils.defaultIfNull(rilevazione.getCa302(), BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal ca301 = ObjectUtils.defaultIfNull(rilevazione.getCa301(), BigDecimal.ZERO);
            BigDecimal ca303 = ObjectUtils.defaultIfNull(rilevazione.getCa303(), BigDecimal.ZERO);
            BigDecimal ca304 = ObjectUtils.defaultIfNull(rilevazione.getCa304(), BigDecimal.ZERO);
            BigDecimal ca309 = ObjectUtils.defaultIfNull(rilevazione.getCa309(), BigDecimal.ZERO);

            BigDecimal ca302 = ca301.subtract(ca303).subtract(ca309.compareTo(BigDecimal.ZERO) > 0 ? ca309 : ca304);
            rilevazione.setCa302(ca302);
        }
    }

    private RilevazioniFasceEva creaFascia(String rigaFascia) {
        DecimalFormat numFormat = new DecimalFormat("00");
        RilevazioniFasceEva fascia = new RilevazioniFasceEva();
        String[] rigaFasciaSplit = StringUtils.splitPreserveAllTokens(rigaFascia, FIELD_SEPARATOR);
        for (int x = 1; x < rigaFasciaSplit.length; x++) {
            String nomeCampoFascia = rigaFasciaSplit[0].toLowerCase() + numFormat.format(x);
            Object valore = LetturaEvaDtsValueParser.valoreInt(rigaFasciaSplit[x]);
            if (x == 3) {
                valore = LetturaEvaDtsValueParser.valoreDecimal(rigaFasciaSplit[x]);
            }
            setValore(fascia, nomeCampoFascia, valore);
        }
        return fascia;
    }

    private RilevazioneEvaDts creaRilevazioneEvaDts() {
        DecimalFormat numFormat = new DecimalFormat("00");

        RilevazioneEvaDts rilevazione = new RilevazioneEvaDts();
        rilevazione.setRigheFileImport(righe);
        rilevazione.setFasce(new ArrayList<RilevazioniFasceEva>());
        for (String riga : righe) {
            String[] rigaSplit = StringUtils.splitPreserveAllTokens(riga, FIELD_SEPARATOR);

            // il primo valore è l'id del campo
            String campo = rigaSplit[0];
            for (int i = 1; i < rigaSplit.length; i++) {
                String nomeCampoRilevazione = campo.toLowerCase() + numFormat.format(i);
                boolean valoreInserito = true;
                Object valore;
                switch (campo.toUpperCase()) {
                case "ID1":
                    valore = LetturaEvaDtsValueParser.valoreString(rigaSplit[i]);
                    valoreInserito = setValore(rilevazione, nomeCampoRilevazione, valore);
                    break;
                case "EA3":
                    // la riga è formata EA3**data*ora**data*ora
                    if (i == 2 || i == 5) {
                        valore = LetturaEvaDtsValueParser.valoreDataOra(rigaSplit[i], rigaSplit[i + 1]);
                        valoreInserito = setValore(rilevazione, nomeCampoRilevazione, valore);
                    }
                    break;
                case "ID4":
                    // prendo solo il primo
                    if (i == 1) {
                        valore = StringUtils.isBlank(rigaSplit[i]) ? 1
                                : LetturaEvaDtsValueParser.valoreInt(rigaSplit[i]);
                        valore = new Double(Math.pow(10, ((Integer) valore))).intValue();
                        valoreInserito = setValore(rilevazione, "divisore", valore);
                    }
                    break;
                case "LA1":
                    // in questo caso tutta la riga è una fascia
                    if (i == 1) {
                        RilevazioniFasceEva fascia = creaFascia(riga);
                        fascia.setRilevazioneEvaDts(rilevazione);
                        rilevazione.getFasce().add(fascia);
                    }
                    break;
                // @formatter:off
                case "CA2": case "CA3": case "CA4": case "CA7": case "CA8": case "CA10": case "CA15": case "DA2": case "DA3":
                case "DA4": case "DA5": case "DA6": case "DA9": case "VA1": case "VA2": case "VA3":
                 // @formatter:on
                    valore = LetturaEvaDtsValueParser.valoreDecimal(rigaSplit[i]);
                    valoreInserito = setValore(rilevazione, nomeCampoRilevazione, valore);
                    break;
                default:
                    break;
                }
                if (!valoreInserito) {
                    LOGGER.error("--> errore durante il set del valore nel campo " + nomeCampoRilevazione);
                }
            }
        }
        return rilevazione;
    }

    private void eliminaValoreDoppioCampoID106(RilevazioneEvaDts rilevazione) {

        boolean valoreDoppioConfigurato = evaDtsImportFolder.getFieldIDName() == EvaDtsFieldIDName.ID106
                && evaDtsImportFolder.isGestioneValoreIDDoppio();
        boolean id106Valido = !StringUtils.isBlank(rilevazione.getId106())
                && (rilevazione.getId106().length() % 2 == 0);

        if (valoreDoppioConfigurato && id106Valido) {
            int lunghezzaParti = rilevazione.getId106().length() / 2;
            String parteSx = rilevazione.getId106().substring(0, lunghezzaParti);
            String parteDx = rilevazione.getId106().substring(lunghezzaParti);

            if (Objects.equals(parteSx, parteDx)) {
                rilevazione.setId106(parteSx);
            }
        }
    }

    /**
     * Resitutisce la rilevazione EVA DTS
     *
     * @return rilevazione EVA DTS
     */
    public RilevazioneEvaDts getRilevazioneEvaDts() {

        RilevazioneEvaDts rilevazioneEvaDts = creaRilevazioneEvaDts();
        if (!rilevazioneEvaDts.isEmpty()) {
            rilevazioneEvaDts.applicaDivisore();
            calcolaCampoCA302(rilevazioneEvaDts);
            eliminaValoreDoppioCampoID106(rilevazioneEvaDts);
        }

        return rilevazioneEvaDts;
    }

    /**
     * @return the ultimaRiga
     */
    public int getUltimaRiga() {
        return ultimaRiga;
    }

    /**
     * @param evaDtsImportFolder
     *            the evaDtsImportFolder to set
     */
    public void setEvaDtsImportFolder(EvaDtsImportFolder evaDtsImportFolder) {
        this.evaDtsImportFolder = evaDtsImportFolder;
    }

    /**
     * @param ultimaRiga
     *            the ultimaRiga to set
     */
    public void setUltimaRiga(int ultimaRiga) {
        this.ultimaRiga = ultimaRiga;
    }

    private boolean setValore(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, fieldValue);
            return true;
        } catch (Exception e) { // NOSONAR
            return false;
        }
    }
}
