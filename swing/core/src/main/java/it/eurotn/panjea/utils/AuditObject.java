package it.eurotn.panjea.utils;

/**
 * Utilizzato per wrappare un object e poter aprire l'editor dell'audit.
 *
 * @author giangi
 * @version 1.0, 23/mag/2011
 *
 */
public class AuditObject {

    private Object auditObject;

    /**
     * Costruttore.
     * 
     * @param auditObject
     *            oggetto per il quale visualizzare l'audit
     */
    public AuditObject(final Object auditObject) {
        super();
        this.auditObject = auditObject;
    }

    /**
     * 
     * @return oggetto per il quale visualizzare l'audit
     */
    public Object getAuditObject() {
        return auditObject;
    }
}
