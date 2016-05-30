package it.eurotn.panjea.dms.manager.allegati.attributi;

import org.apache.commons.lang.StringUtils;

import it.eurotn.panjea.anagrafica.util.EntitaDocumento;

public class AttributoAllegatoEntita extends AttributoAllegatoPanjea {
    private static final long serialVersionUID = -4267044634567268783L;

    /**
     *
     * @param entitaDocumento
     *            entita
     * @param codiceAzienda
     *            codice azienda
     */
    public AttributoAllegatoEntita(final EntitaDocumento entitaDocumento, final String codiceAzienda) {
        super("ext_CODENTITA", "", null, codiceAzienda);// Setto dopo il valore

        value = null;
        if (entitaDocumento != null && entitaDocumento.getTipoEntita() != null) {
            setId(entitaDocumento.getId());
            StringBuilder sb = new StringBuilder();
            if (entitaDocumento.getCodice() != null) {
                sb.append(entitaDocumento.getCodice());
            }

            sb.append(separatorField);
            sb.append(entitaDocumento.getTipoEntita().name().substring(0, 1));

            if (!StringUtils.isEmpty(entitaDocumento.getDescrizione())) {
                if (sb.length() > 0) {
                    sb.append(separatorField);
                }
                sb.append(entitaDocumento.getDescrizione());
            }
            sb.append(separatorField);
            sb.append(entitaDocumento.getId());
            sb.append(separatorField);
            sb.append(getCodiceAzienda());
            value = sb.toString();
        }
    }

    @Override
    public String getValueSearch() {
        return "*" + (getId() != null ? separatorField + String.valueOf(getId()) + separatorField + getCodiceAzienda()
                : "");
    }

}
