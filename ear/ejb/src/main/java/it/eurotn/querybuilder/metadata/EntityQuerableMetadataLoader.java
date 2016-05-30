package it.eurotn.querybuilder.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;

import it.eurotn.entity.annotation.ExcludeFromQueryBuilder;
import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityFactory;

public class EntityQuerableMetadataLoader {

    private static final int DEPTH = 6;

    /**
     *
     * @param entity
     *            entita da caricare
     * @param classi
     *            classi configurate in hibernate
     * @return entità da poter interrogare con le loro proprieta
     */
    public EntitaQuerableMetaData caricaAll(ProprietaEntity entity, Map<String, AbstractEntityPersister> classi) {
        EntitaQuerableMetaData metaData = new EntitaQuerableMetaData();
        metaData.addAllProprieta(caricaProperties(entity, classi, 0));
        return metaData;
    }

    private List<ProprietaEntity> caricaProperties(ProprietaEntity entity, Map<String, AbstractEntityPersister> classi,
            int currentDepth) {
        List<ProprietaEntity> properties = new ArrayList<>();

        AbstractEntityPersister persister = null;
        String[] nomiProp = null;
        String prefixEmbedded = "";
        if (entity.isEmbedded()) {
            persister = classi.get(entity.getParentProprietaEntity().getType().getName());
            if (persister == null) {
                return new ArrayList<>();
            }
            ComponentType componentType = (ComponentType) persister.getPropertyType(entity.getNome());
            nomiProp = componentType.getPropertyNames();
            prefixEmbedded = entity.getNome() + ".";
        } else {
            persister = classi.get(entity.getType().getName());
            nomiProp = persister.getPropertyNames();
        }

        for (String nome : nomiProp) {

            if (excludeFromQueryBuilderPresent(entity.getType(), nome)) {
                continue;
            }

            ProprietaEntity proprietaEntity = null;
            // controllo se la proprietà è già presente altrimenti la creo e la aggiungo
            if (entity.getProprieta() != null) {
                for (ProprietaEntity propPresente : entity.getProprieta()) {
                    if (nome.equals(propPresente.getNome())) {
                        proprietaEntity = propPresente;
                        break;
                    }
                }
            }

            if (proprietaEntity == null) {
                Type propertyPersistentType = persister.getPropertyType(prefixEmbedded + nome);
                boolean propertyEmbeded = ComponentType.class.isAssignableFrom(propertyPersistentType.getClass());
                Class<?> propertyClass = propertyPersistentType.getReturnedClass();
                boolean isCollection = false;
                if (propertyPersistentType instanceof CollectionType) {
                    isCollection = true;
                    propertyClass = ((CollectionType) propertyPersistentType).getElementType(persister.getFactory())
                            .getReturnedClass();
                }
                boolean isEntity = classi.containsKey(propertyClass.getName());
                if (isEntity) {
                    proprietaEntity = ProprietaEntityFactory.getInstance(propertyEmbeded, nome, propertyClass, isEntity,
                            isCollection, entity);
                } else {
                    proprietaEntity = ProprietaEntityFactory.getInstance(propertyEmbeded, nome, propertyClass, isEntity,
                            isCollection, entity);
                }

                if (proprietaEntity.getParentProprietaEntity() != null
                        && proprietaEntity.getParentProprietaEntity().getParentProprietaEntity() != null
                        && proprietaEntity.getType().equals(
                                proprietaEntity.getParentProprietaEntity().getParentProprietaEntity().getType())) {
                    proprietaEntity.setProprieta(null);
                } else {
                    if ((isEntity || propertyEmbeded)
                            && StringUtils.countMatches(proprietaEntity.getPathBean(), ".") < DEPTH) {
                        proprietaEntity.setProprieta(caricaProperties(proprietaEntity, classi, currentDepth + 1));
                    }
                }
            }
            properties.add(proprietaEntity);
        }

        return properties;
    }

    private boolean excludeFromQueryBuilderPresent(Class<?> parentClass, String property) {
        try {
            return FieldUtils.getField(parentClass, property, true).isAnnotationPresent(ExcludeFromQueryBuilder.class);
        } catch (Exception e) {
            return false;
        }
    }
}
