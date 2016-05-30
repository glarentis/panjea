package it.eurotn.panjea.anagrafica.rich.editors.audit;

import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.audit.envers.RevInf;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;
import org.springframework.binding.form.FieldFaceSource;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.MultiTableModel;
import com.jidesoft.grid.StyleModel;

public class AuditTableModel extends DefaultTableModel implements ContextSensitiveTableModel, StyleModel,
		MultiTableModel {
	private static final long serialVersionUID = 1729687498008375793L;
	private static Logger logger = Logger.getLogger(AuditTableModel.class);
	private Object[][] data;
	private String[] columnName;
	private FieldFaceSource fieldFaceSource;
	private List<String> proprietaDaEscludere = new ArrayList<String>();
	private List<String> proprietaFisse = new ArrayList<String>();
	public static final CellStyle PROPERTY_NAME_STYLE = new CellStyle();
	public static final CellStyle PROPERTY_NAME_DIVIDE = new CellStyle();
	public static final CellStyle PROPERTY_CHANGE = new CellStyle();
	private List<String> nomiProprieta;
	private final Object auditObject;

	{
		PROPERTY_NAME_STYLE.setFontStyle(Font.BOLD);
		PROPERTY_NAME_DIVIDE.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black));
		PROPERTY_NAME_DIVIDE.setFontStyle(Font.BOLD);
		PROPERTY_CHANGE.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
	}

	/**
	 *
	 * Costruttore.
	 *
	 * @param auditObject
	 *            object sul quale effetto l'audit
	 *
	 * @param auditData
	 *            dati sulle revisioni dell'oggetto
	 */
	public AuditTableModel(final Object auditObject, final Map<RevInf, List<Object>> auditData) {
		super();
		this.auditObject = auditObject;
		proprietaDaEscludere.add("version");
		proprietaDaEscludere.add("userInsert");
		proprietaDaEscludere.add("timeStamp");
		proprietaDaEscludere.add("domainClassName");
		proprietaDaEscludere.add("tipoAreaPartita");
		proprietaDaEscludere.add("tipoAreaContabile");
		proprietaDaEscludere.add("tipoAreaMagazzino");
		proprietaDaEscludere.add("tipoAreaOrdine");

		proprietaFisse.add("Data");
		proprietaFisse.add("Utente");

		data = new Object[getRowCountFromAuditData(auditData)][getColumnCountFromAuditData(auditData)];
		columnName = new String[data[0].length];

		// Aggiungo la colonna delle proprietà
		int column = 0;
		int row = 0;

		for (String propFissa : proprietaFisse) {
			data[row++][column] = propFissa;
		}

		nomiProprieta = estraiNomiProprieta();
		for (Object nomeProprieta : nomiProprieta) {
			data[row++][column] = nomeProprieta;
		}

		columnName[column] = "Proprietà";

		Map<Class<?>, Object> beansAuditable = new HashMap<Class<?>, Object>();
		column = auditData.keySet().size() + 1;
		for (RevInf revInf : getOrderListForRevision(auditData.keySet())) {
			column--;

			List<Object> propertyForRevision = new ArrayList<Object>();
			List<Object> beansAuditableRevision = auditData.get(revInf);
			for (Object object : beansAuditableRevision) {
				beansAuditable.put(object.getClass(), object);
			}

			columnName[column] = "Rev. n° " + revInf.getId();
			// // Aggiungo alla lista delle proprietà tutte le proprietà dell'oggetto per questa versione
			row = 0;
			data[row++][column] = revInf.getRevisionDate();
			data[row++][column] = revInf.getUsername();

			propertyForRevision = estraiProprieta(beansAuditable.values());
			for (Object object : propertyForRevision) {
				data[row++][column] = object;
			}
		}
		// for (Object beanAuditable : beansAuditable) {
		// row = 2;
		// for (String nomeProprieta : nomiProprieta) {
		// Object resultData = estraiProprieta(beanAuditable, nomeProprieta);
		// if (resultData != null) {
		// data[row][column] = resultData;
		// }
		// row++;
		// }
		// }
		// propertyForRevision = estraiProprieta(beansAuditable);
		//
		// for (Object object : propertyForRevision) {
		// data[row++][column] = object;
		// }
		// }
	}

	/**
	 *
	 * @param beansAuditable
	 *            lsita dei bean dal quale estrarre le proprietà
	 * @return collection con le proprietà del bean. Esclude le proprietà contenute nella lista PROPRIETA_DA_ESCLUDERE
	 */
	private List<String> estraiNomiProprieta() {
		List<String> result = new ArrayList<String>();
		if (auditObject.getClass().getAnnotation(Audited.class) != null) {
			result.addAll(estraiNomiProprieta(auditObject.getClass()));
		}
		if (auditObject.getClass().getAnnotation(AuditableProperties.class) != null) {
			AuditableProperties properties = auditObject.getClass().getAnnotation(AuditableProperties.class);
			for (String beanAuditedNested : properties.properties()) {
				Class<?> classBean = getBeanClassAuditable(beanAuditedNested, auditObject);
				result.addAll(estraiNomiProprieta(classBean));
			}
		}
		return result;
	}

	private List<String> estraiNomiProprieta(Class<?> beanAuditable) {
		List<String> result = new ArrayList<String>();
		List<String> proprietaDaEsculdereFromAnnotation = new ArrayList<String>();
		if (auditObject.getClass().getAnnotation(AuditableProperties.class) != null) {
			AuditableProperties properties = auditObject.getClass().getAnnotation(AuditableProperties.class);
			proprietaDaEsculdereFromAnnotation.addAll(Arrays.asList(properties.excludeProperties()));
		}
		// solo gli entityBase sono audited
		// Aggiungo la colonna delle proprietà
		Class<?> classeBase = beanAuditable;
		do {
			Field[] classFieldList = classeBase.getDeclaredFields();
			for (Field field : classFieldList) {
				if (!proprietaDaEscludere.contains(field.getName())
						&& !proprietaDaEsculdereFromAnnotation.contains(field.getName())
						&& !Collection.class.isAssignableFrom(field.getType())
						&& getterMethod(field.getName(), beanAuditable) != null
						&& field.getAnnotation(Transient.class) == null) {
					String nome = getFieldFaceSource().getFieldFace(field.getName()).getDisplayName();
					if (!nome.endsWith(".label")) {
						try {
							result.add(field.getName());
						} catch (Exception e) {
							logger.error("Errore nell'estrarre la proprieta " + field.getName());
						}
					}
				}
			}
			classeBase = classeBase.getSuperclass();
		} while (!(classeBase == Object.class));

		Collections.sort(result, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		String className = RcpSupport.getMessage(beanAuditable.getName());
		if (className.isEmpty()) {
			className = beanAuditable.getSimpleName();
		}
		result.add(0, className.toUpperCase());

		return result;
	}

	/**
	 *
	 * @param beansAuditable
	 *            lsita dei bean dal quale estrarre le proprietà
	 * @return collection con le proprietà del bean. Esclude le proprietà contenute nella lista PROPRIETA_DA_ESCLUDERE
	 */
	private List<Object> estraiProprieta(Collection<Object> beansAuditable) {
		List<Object> result = new ArrayList<Object>();
		if (auditObject.getClass().getAnnotation(Audited.class) != null) {

			Object auditNestedObject = null;
			for (Object object : beansAuditable) {
				if (auditObject.getClass() == object.getClass()) {
					auditNestedObject = object;
				}
			}

			result.addAll(estraiProprieta(auditNestedObject, auditObject.getClass()));
			// for (Object object : beansAuditable) {
			// result.addAll(estraiProprieta(object, object.getClass()));
			// }
		}
		if (auditObject.getClass().getAnnotation(AuditableProperties.class) != null) {
			AuditableProperties properties = auditObject.getClass().getAnnotation(AuditableProperties.class);
			for (String beanAuditedNested : properties.properties()) {
				// Method getter = getterMethod(beanAuditedNested, auditObject.getClass());
				// Class<?> classeAuditNestedObject = getter.getReturnType();
				Class<?> classeAuditNestedObject = getBeanClassAuditable(beanAuditedNested, auditObject);
				Object auditNestedObject = null;
				for (Object object : beansAuditable) {
					if (classeAuditNestedObject == object.getClass()) {
						auditNestedObject = object;
					}
				}
				result.addAll(estraiProprieta(auditNestedObject, classeAuditNestedObject));
			}
		}
		return result;
	}

	private List<Object> estraiProprieta(Object beanAuditable, Class<?> beanAuditableClass) {
		List<Object> result = new ArrayList<Object>();
		List<String> properties = estraiNomiProprieta(beanAuditableClass);
		// Rimuovo il nome della classe
		properties.remove(0);
		for (String nomeProprieta : properties) {
			if (beanAuditable == null) {
				result.add(null);
			} else {
				Method metodo = getterMethod(nomeProprieta, beanAuditableClass);
				if (metodo != null) {
					Object propertyValue = null;
					try {
						propertyValue = metodo.invoke(beanAuditable, (Object[]) null);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						result.add(propertyValue);
					}
				}
			}
		}
		result.add(0, beanAuditableClass);
		return result;
	}

	private Class<?> getBeanClassAuditable(String property, Object auditObjectParam) {

		String[] splitProp = property.split("\\.");

		Method getter;
		try {
			getter = getterMethod(splitProp[0], auditObjectParam.getClass());
			auditObjectParam = getter.invoke(auditObjectParam, (Object[]) null);

			if (splitProp.length == 1) {
				return getter.getReturnType();
			} else {
				property = property.replaceFirst(splitProp[0] + "\\.", "");
				return getBeanClassAuditable(property, auditObjectParam);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Class<?> getCellClassAt(int row, int column) {
		if (data[row][column] == null) {
			return String.class;
		}
		return data[row][column].getClass();
	}

	@Override
	public CellStyle getCellStyleAt(int i, int j) {
		if (j == 0) {
			if (data[i][1] instanceof Class) {
				return PROPERTY_NAME_DIVIDE;
			} else {
				return PROPERTY_NAME_STYLE;
			}
		} else if (i > 1 && j < getColumnCount() - 1 && data[i][j + 1] != null && !data[i][j + 1].equals(data[i][j])) {
			return PROPERTY_CHANGE;
		} else if (i > 1 && j < getColumnCount() - 1 && data[i][j + 1] == null && data[i][j] != null) {
			return PROPERTY_CHANGE;
		}
		if (data[i][j] instanceof Class) {
			return PROPERTY_NAME_DIVIDE;
		}

		return null;
	}

	@Override
	public int getColumnCount() {
		return columnName.length;
	}

	/**
	 *
	 * @param auditData
	 *            data per gli audit
	 * @return num di colonne dal numero di revisioni
	 */
	private int getColumnCountFromAuditData(Map<RevInf, List<Object>> auditData) {
		return auditData.keySet().size() + 1;
	}

	@Override
	public String getColumnName(int column) {
		return columnName[column];
	}

	@Override
	public int getColumnType(int column) {
		if (column == 0) {
			return HEADER_COLUMN;
		}
		return REGULAR_COLUMN;
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		Class<?> classe = getCellClassAt(i, j);
		if (classe == BigDecimal.class) {
			return new NumberWithDecimalConverterContext(6);
		} else if (classe == Date.class) {
			return new ConverterContext("", "dd/MM/yyyy HH:mm");
		}
		return null;
	}

	@Override
	public EditorContext getEditorContextAt(int i, int j) {
		return null;
	}

	/**
	 *
	 * @return service per recuperare i nomi I18N per i campi
	 */
	private FieldFaceSource getFieldFaceSource() {
		if (fieldFaceSource == null) {
			fieldFaceSource = (FieldFaceSource) ApplicationServicesLocator.services().getService(FieldFaceSource.class);
		}
		return fieldFaceSource;
	}

	/**
	 *
	 * @param revInf
	 *            revisioni
	 * @return lista ordinata delle revisioni in base al timeStampa
	 */
	private List<RevInf> getOrderListForRevision(Set<RevInf> revInf) {
		List<RevInf> revisioniOrdinate = new ArrayList<RevInf>(revInf);
		Collections.sort(revisioniOrdinate, new Comparator<RevInf>() {

			@Override
			public int compare(RevInf o1, RevInf o2) {
				return o1.getRevisionDate().compareTo(o2.getRevisionDate());
			}
		});
		return revisioniOrdinate;
	}

	@Override
	public int getRowCount() {
		if (data == null) {
			return 0;
		}
		return data.length;
	}

	/**
	 *
	 * @param auditData
	 *            data per gli audit
	 * @return num di righe dagli oggetti auditable.
	 */
	private int getRowCountFromAuditData(Map<RevInf, List<Object>> auditData) {
		return estraiNomiProprieta().size() + proprietaFisse.size();
	}

	@Override
	public int getTableIndex(int arg0) {
		return 0;
	}

	/**
	 *
	 * @param propertyName
	 *            proprietà per la quale ricavare il getter
	 * @param classe
	 *            classe alla quale appartiene la proprietà
	 * @return metodo getter.
	 */
	private Method getterMethod(String propertyName, Class<?> classe) {
		String proprieta = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
		Method metodo = null;
		String getterName = "get" + proprieta;
		try {
			metodo = classe.getMethod(getterName, (Class<?>[]) null);
		} catch (NoSuchMethodException e) {
			getterName = "is" + proprieta;
			try {
				metodo = classe.getMethod(getterName, (Class<?>[]) null);
			} catch (Exception e1) {
				metodo = null;
			}
		} catch (Exception ex) {
			metodo = null;
		}
		return metodo;
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (data != null && !(data[row][column] instanceof Class)) {
			return data[row][column];
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int i, int j) {
		return false;
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

}
