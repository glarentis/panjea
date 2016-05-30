package it.eurotn.panjea.anagrafica.service;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.service.interfaces.AuditService;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.audit.envers.RevInf;
import it.eurotn.panjea.audit.manager.interfaces.AuditManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.envers.entities.mapper.relation.lazy.proxy.MapProxy;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AuditService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.AuditService")
public class AuditServiceBean implements AuditService {
	private static Logger logger = Logger.getLogger(AuditServiceBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private AuditManager auditManager;

	/**
	 * Aggiorna la mappa delle revisioni con le revisioni caricate del bean.
	 *
	 * @param revisioni
	 *            mappa contenente le revisioni dei vari bean
	 * @param versioniOfBean
	 *            versioni del singolo bean
	 * @return revisioni aggiornate
	 */
	private Map<RevInf, List<Object>> aggiornaMappaRevisioni(Map<RevInf, List<Object>> revisioni,
			List<Object[]> versioniOfBean) {
		for (Object[] versioneOfBean : versioniOfBean) {
			List<Object> beansAtRevision = revisioni.get(versioneOfBean[1]);
			if (beansAtRevision == null) {
				beansAtRevision = new ArrayList<Object>();
				revisioni.put((RevInf) versioneOfBean[1], beansAtRevision);
			}
			beansAtRevision.add(versioneOfBean[0]);
		}
		return revisioni;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void cancellaAuditPrecedente(Date data) {
		auditManager.cancellaAuditPrecedente(data);
	}

	@Override
	public Integer caricaNumeroRevInf() {
		return auditManager.caricaNumeroRevInf();
	}

	/**
	 * Carica le versioni per una classe e inizializza le proprietà lazy (ricorsivamente).
	 *
	 * @param classe
	 *            classe auditable
	 * @param id
	 *            identificativo della classe
	 * @return lista di array: 0:oggetto 1:versione 2:tipo operazione (inserimento,modifica,cancellazione)
	 */
	private List<Object[]> caricaVersioni(Class<?> classe, int id) {
		AuditReader reader = getAuditReader();
		AuditQuery query = reader.createQuery().forRevisionsOfEntity(classe, false, true);
		query.add(AuditEntity.id().eq(id));
		@SuppressWarnings("unchecked")
		List<Object[]> versioni = query.getResultList();
		for (Object[] object : versioni) {
			initilizeBean(object[0]);
		}
		return versioni;
	}

	/**
	 *
	 * @return reader per l'audit
	 */
	private AuditReader getAuditReader() {
		EntityManager entityManager = this.panjeaDAO.getEntityManager();
		AuditReader reader = AuditReaderFactory.get(entityManager);
		return reader;
	}

	private Object getObjectFromAuditableProp(String property, Object auditBean, Map<RevInf, List<Object>> revisioni) {

		String[] splitProp = property.split("\\.");

		Method getter;
		try {
			Class<?> classeBase = auditBean.getClass();
			do {
				try {
					getter = getterMethod(classeBase.getDeclaredField(splitProp[0]), classeBase);
					break;
				} catch (NoSuchFieldException e) {
					getter = null;
				}
				classeBase = classeBase.getSuperclass();
			} while (!(classeBase == Object.class));

			if (getter != null) {
				auditBean = getter.invoke(auditBean, (Object[]) null);
			}

			if (splitProp.length == 1) {
				// solo gli entityBase sono audited
				Integer id = null;
				if (auditBean instanceof HibernateProxy) {
					id = (Integer) ((HibernateProxy) auditBean).getHibernateLazyInitializer().getIdentifier();
				} else {
					id = ((EntityBase) auditBean).getId();
				}
				if (id != null) {
					aggiornaMappaRevisioni(revisioni,
							caricaVersioni(classeBase.getDeclaredField(splitProp[0]).getType(), id));
				}
				return auditBean;
			} else {
				property = property.replaceFirst(splitProp[0] + "\\.", "");
				return getObjectFromAuditableProp(property, auditBean, revisioni);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 *
	 * @param fld
	 *            proprietà per la quale ricavare il getter
	 * @param classe
	 *            classe alla quale appartiene la proprietà
	 * @return metodo getter.
	 */
	private Method getterMethod(Field fld, Class<?> classe) {
		String proprieta = Character.toUpperCase(fld.getName().charAt(0)) + fld.getName().substring(1);
		Method metodo;
		String getterName = "get" + proprieta;
		if (fld.getType() == Boolean.class || fld.getType() == boolean.class) {
			getterName = "is" + proprieta;
		}
		try {
			metodo = classe.getMethod(getterName, (Class<?>[]) null);
		} catch (NoSuchMethodException nsme) {
			metodo = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return metodo;
	}

	@Override
	public Map<RevInf, List<Object>> getVersioni(Object auditBean) {

		Class<?> classe = auditBean.getClass();
		Map<RevInf, List<Object>> revisioni = new HashMap<RevInf, List<Object>>();

		// Controllo se il bean è auditable, altrimenti cerco le sue proprietà e creo le versioni per le proprietà
		// auditable
		if (classe.getAnnotation(AuditableProperties.class) == null && classe.getAnnotation(Audited.class) == null) {
			throw new IllegalArgumentException("Entità senza dati di Audit");
		}

		if (classe.getAnnotation(Audited.class) != null) {
			// solo gli entityBase sono audited
			Integer id = null;
			if (auditBean instanceof HibernateProxy) {
				id = (Integer) ((HibernateProxy) auditBean).getHibernateLazyInitializer().getIdentifier();
			} else {
				id = ((EntityBase) auditBean).getId();
			}
			if (id != null) {
				revisioni = aggiornaMappaRevisioni(revisioni, caricaVersioni(classe, id));
			}
		}

		if (classe.getAnnotation(AuditableProperties.class) != null) {
			AuditableProperties prop = classe.getAnnotation(AuditableProperties.class);
			for (String property : prop.properties()) {
				try {
					getObjectFromAuditableProp(property, auditBean, revisioni);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return revisioni;
	}

	/**
	 * inizializza il bean ricercando e caricando le proprietà lazy.<br/>
	 * NB.le collection non vendono inizializzate.
	 *
	 * @param bean
	 *            bean da inizializzare
	 */
	private void initilizeBean(Object bean) {
		Class<?> classe = bean.getClass();
		if (bean instanceof HibernateProxy) {
			classe = ((HibernateProxy) bean).getHibernateLazyInitializer().getPersistentClass();
		}

		Class<?> classeBase = classe;
		List<Field> fieldList = new ArrayList<Field>();
		do {
			Field[] classFieldList = classeBase.getDeclaredFields();
			for (Field field : classFieldList) {
				fieldList.add(field);
			}
			classeBase = classeBase.getSuperclass();
		} while (!(classeBase == Object.class));

		Field[] fieldArray = fieldList.toArray(new Field[fieldList.size()]);
		for (int i = 0; i < fieldArray.length; i++) {
			Field fld = null;
			Object result = null;
			try {
				fld = fieldArray[i];
				Method metodo = getterMethod(fld, classe);
				if (metodo == null) {
					continue;
				}
				result = metodo.invoke(bean, (Object[]) null);
				if (result instanceof HibernateProxy) {
					Hibernate.initialize(result);
					initilizeBean(result);
				} else if (result instanceof MapProxy<?, ?>) {
					// La versione di Envers ha un bug sulle mappe perchè non serializzabili
					// Sostituisco il valore con una mappa vuota perchè non mi serve
					Method setter = setterMethod(fld, bean.getClass());
					setter.invoke(bean, (Object) null);
				}
			} catch (EntityNotFoundException enf) {
				// non ho nessun oggetto per la versione richiesta.
				// Carico l'oggetto allo stato attuale.
				if (bean instanceof EntityBase) {
					Object actualBean = null;

					try {
						actualBean = panjeaDAO.getEntityManager().find(fld.getType(),
								((HibernateProxy) result).getHibernateLazyInitializer().getIdentifier());

						// fld.setAccessible(true);
						// fld.set(bean, actualBean);
						String proprieta = Character.toUpperCase(fld.getName().charAt(0)) + fld.getName().substring(1);
						Method metodo;
						String setterName = "set" + proprieta;
						try {
							metodo = classe.getMethod(setterName, fld.getType());
							metodo.invoke(bean, actualBean);
						} catch (NoSuchMethodException nsme) {
							// manca il setter, pazienza
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("--> Errore nel recuperare la versione dell'oggetto collegato " + enf.toString());
				}
			} catch (Exception e) {
				logger.error("-->errore ", e);
				logger.error("-->errore nel chiamare il metodo del bean " + classe);
			}
		}
	}

	private Method setterMethod(Field fld, Class<?> classe) {
		String proprieta = Character.toUpperCase(fld.getName().charAt(0)) + fld.getName().substring(1);
		Method metodo;
		String getterName = "set" + proprieta;
		try {
			metodo = classe.getMethod(getterName, fld.getType());
		} catch (NoSuchMethodException nsme) {
			metodo = null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return metodo;
	}
}
