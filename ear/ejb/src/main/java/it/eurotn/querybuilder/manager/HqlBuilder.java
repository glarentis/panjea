package it.eurotn.querybuilder.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.lang3.StringUtils;

import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityPersister;

public class HqlBuilder {

    private List<ProprietaEntity> listaproprieta;

    private StringBuilder sbFrom = new StringBuilder(3000);
    private StringBuilder sbSelect = new StringBuilder(3000);
    private ListOrderedSet aliases = new ListOrderedSet();

    private StringBuilder sbWhere = new StringBuilder(3000);
    private Set<String> whereEntityBaseAlias = new TreeSet<>();
    private List<String> wheres = new ArrayList<>();

    private ProprietaEntityPersister proprietaBase;
    private List<String> selectedFieldName = new ArrayList<>();

    public HqlBuilder(final ProprietaEntityPersister proprietaBase, final List<ProprietaEntity> proprieta) {
        this.proprietaBase = proprietaBase;
        this.listaproprieta = proprieta;
    }

    public String build() {
        sbSelect.append(" SELECT ");
        sbSelect.append(proprietaBase.getSelect());
        for (ProprietaEntity proprietaEntity : listaproprieta) {
            elaboraEntity(proprietaEntity);
        }
        sbFrom.append(proprietaBase.getFrom());
        for (Object alias : aliases) {
            sbFrom.append(alias.toString());
        }
        if (!wheres.isEmpty()) {
            sbWhere.append(" where ");
            for (String where : wheres) {
                sbWhere.append(where);
            }
        }
        return StringUtils.chop(sbSelect.toString().trim()) + sbFrom.toString() + sbWhere.toString();
    }

    private void elaboraAlias(ProprietaEntity prop) {
        if (prop.getParentProprietaEntity() != null) {
            elaboraAlias(prop.getParentProprietaEntity());
        }
        if (prop.isEntity()) {
            aliases.add(prop.getFrom());
        }
    }

    private void elaboraEntity(ProprietaEntity prop) {
        if (prop.isInSelect() || prop.getFiltro() != null) {
            sbSelect.append(prop.getSelect());
            sbSelect.append(", ");
            selectedFieldName.add(prop.getNomeIndexed());
            elaboraAlias(prop);
        }

        String whereProp = prop.getWhere();
        if (whereProp != null) {
            if (!whereEntityBaseAlias.contains(prop.getAliasBean())) {
                wheres.add(wheres.isEmpty() ? whereProp : " and " + whereProp);
            }
            if (prop.isEntity()) {
                whereEntityBaseAlias.add(prop.getAliasBean());
            }
        }
    }

    public String[] getSelectFieldsName() {
        return selectedFieldName.toArray(new String[selectedFieldName.size()]);
    }

}
