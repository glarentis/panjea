package it.eurotn.panjea.bi.export.jasper;

public final class JasperReportUtils {

	/**
	 * Return the correct field type...
	 *
	 */
	public static Class<?> getJRFieldType(Class type) {
		if (type == null) {
			return Object.class;
		}
		if (type.getName().equals("java.lang.Boolean") || type.getName().equals("boolean")) {
			return Boolean.class;
		}
		if (type.getName().equals("java.lang.Byte") || type.getName().equals("byte")) {
			return Byte.class;
		}
		if (type.getName().equals("java.lang.Integer") || type.getName().equals("int")) {
			return Integer.class;
		}
		if (type.getName().equals("java.lang.Long") || type.getName().equals("long")) {
			return Long.class;
		}
		if (type.getName().equals("java.lang.Double") || type.getName().equals("double")) {
			return Double.class;
		}
		if (type.getName().equals("java.lang.Float") || type.getName().equals("float")) {
			return Float.class;
		}
		if (type.getName().equals("java.lang.Short") || type.getName().equals("short")) {
			return Short.class;
		}
		return type;
	}

	private JasperReportUtils() {
	}
}
