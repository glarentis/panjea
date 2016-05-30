package it.eurotn.querybuilder.domain;

public final class ProprietaEntityFactory {
    private ProprietaEntityFactory() {
    }

    public static ProprietaEntity getInstance(boolean embedded, final String nome, final Class<?> type,
            final boolean entity, final boolean collection, final ProprietaEntity parentProprietaEntity) {
        if (embedded) {
            return new ProprietaEmbeddedEntity(nome, type, entity, collection, parentProprietaEntity);
        }
        if (entity) {
            return new ProprietaEntityPersister(nome, type, entity, collection, parentProprietaEntity);
        }
        return new ProprietaEntity(nome, type, entity, collection, parentProprietaEntity);
    }
}
