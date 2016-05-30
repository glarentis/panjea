package it.eurotn.querybuilder.domain;

public class ProprietaEmbeddedEntity extends ProprietaEntity {

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
    public ProprietaEmbeddedEntity(String nome, Class<?> type, boolean entity, boolean collection,
            final ProprietaEntity parentProprietaEntity) {
        super(nome, type, entity, collection, parentProprietaEntity);
    }

    @Override
    public Object getFrom() {
        return "";
    }

    @Override
    public Object getSelect() {
        return super.getSelect();
    }

    @Override
    public boolean isEmbedded() {
        return true;
    }

    @Override
    public boolean isQuerable() {
        return true;
    }

}
