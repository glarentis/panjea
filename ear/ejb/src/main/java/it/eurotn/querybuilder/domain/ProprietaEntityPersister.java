package it.eurotn.querybuilder.domain;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;

public class ProprietaEntityPersister extends ProprietaEntity {

    public ProprietaEntityPersister(Class<?> type) {
        super();
        this.type = type;
        this.aliasBean = type.getSimpleName().toLowerCase();
        this.pathBean = type.getSimpleName();
        this.path = "";
    }

    /**
     * @param nome
     * @param type
     * @param entity
     * @param collection
     * @param parentType
     * @param path
     * @param pathBean
     * @param aliasBean
     */
    public ProprietaEntityPersister(String nome, Class<?> type, boolean entity, boolean collection,
            final ProprietaEntity parentProprietaEntity) {
        super(nome, type, entity, collection, parentProprietaEntity);
        this.aliasBean = parentProprietaEntity.getAliasBean() + nome.toLowerCase();
        this.pathBean = parentProprietaEntity.getPathBean() + "." + type.getSimpleName();
        String separator = StringUtils.isEmpty(parentProprietaEntity.getPath()) ? "" : "$";
        this.path = parentProprietaEntity.getPath() + separator + nome;
        if (StringUtils.isEmpty(path)) {
            this.path = nome;
        }
    }

    @Override
    public Object getFrom() {
        if (StringUtils.isEmpty(path)) {
            return " FROM " + pathBean + " " + aliasBean;
        }
        return " JOIN " + parentProprietaEntity.getAliasBean() + "." + nome + " " + aliasBean;
    }

    @Override
    public Object getSelect() {
        String result;

        if (StringUtils.isEmpty(path)) {
            result = aliasBean + ".id  as id, " + aliasBean + ".version  as version, ";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(aliasBean + ".id  as " + path + "$id,");
            sb.append(aliasBean + ".version  as " + path + "$version, ");
            EntityConverter converter = type.getAnnotation(EntityConverter.class);
            if (converter != null) {
                String[] prop = converter.properties().split(",");
                for (String propBean : prop) {
                    sb.append(aliasBean + "." + propBean + " as " + path + "$" + propBean.replace(".", "$") + ",");
                }
            }
            result = StringUtils.chop(sb.toString());
        }

        return result;
    }

    @Override
    public String getWhere() {
        if (filtro == null) {
            return null;
        }

        return aliasBean + ".id = " + ((EntityBase) filtro).getId() + " ";
    }

}
