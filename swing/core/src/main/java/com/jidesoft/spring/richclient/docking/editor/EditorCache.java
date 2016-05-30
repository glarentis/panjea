package com.jidesoft.spring.richclient.docking.editor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * Gestisce una cache per gli editor più utilizzati.
 * 
 * @author giangi
 * @version 1.0, 30/mar/2012
 * 
 */
public class EditorCache {

	private static Logger logger = Logger.getLogger(EditorCache.class);
	private static final int CACHE_SIZE = 10;
	private HashMap<String, PanjeaDocumentComponent> documentComponentCache;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public EditorCache() {
		documentComponentCache = new HashMap<String, PanjeaDocumentComponent>();
	}

	/**
	 * Svuota la cache dai documenti presenti.
	 */
	public void clear() {
		for (PanjeaDocumentComponent documentComponent : documentComponentCache.values()) {
			documentComponent.getPageComponent().dispose();
		}
		documentComponentCache.clear();
	}

	/**
	 * 
	 * @param id
	 *            id del documento da verificare.
	 * @return true se il documento è contenuto in cache, false altrimenti
	 */
	public boolean containDocument(String id) {
		return documentComponentCache.containsKey(id);
	}

	/**
	 * Ritorna il documento con l'id richiesto. Se trovato viene incrementato il contatore di utilizzo.
	 * 
	 * @param id
	 *            id del documento da ritornare.
	 * @return documento in cache, null se non ho nessun documento
	 */
	public PanjeaDocumentComponent getDocument(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("--> Enter getDocument con id " + id);
		}
		if (documentComponentCache.containsKey(id)) {
			PanjeaDocumentComponent document = documentComponentCache.get(id);
			document.addNumUtilizzi();
			documentComponentCache.put(document.getPageComponent().getId(), document);
			return document;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> Il documento non è in cache");
		}
		return null;
	}

	/**
	 * Inserisce il documento in mappa, se la mappa è piena rimuovo l'elemento con meno accessi.
	 * 
	 * @param id
	 *            id del componente da inserire
	 * @param component
	 *            document da inserire
	 * @param excludeEditorToRemove
	 *            se la lista è piena rimuove gli editor meno utilizzati, esclusi gli editor in excludeEditorToRemove
	 */
	public void putDocument(String id, PanjeaDocumentComponent component, String[] excludeEditorToRemove) {
		logger.debug("--> Enter putDocument con id " + id);
		if (documentComponentCache.size() == CACHE_SIZE && !rimuoviElementoMenoUsato(excludeEditorToRemove)) {
			return;
		}
		documentComponentCache.put(id, component);
		if (logger.isDebugEnabled()) {
			logger.debug("--> Inserisco il documento in cache");
			if (logger.isDebugEnabled()) {
				logger.debug("--> Contenuto cache " + this.toString());
			}
		}
	}

	/**
	 * 
	 * @param id
	 *            id del documento da rimuovere
	 */
	public void removeDocument(String id) {
		PanjeaDocumentComponent pdc = documentComponentCache.get(id);
		// se ripristino il layout di un editor senza averlo aggiunto alla cache pdc e' null
		if (pdc != null) {
			pdc.getPageComponent().dispose();
		}
		documentComponentCache.remove(id);
	}

	/**
	 * 
	 * @param excludeEditorToRemove
	 *            esclude l'editor dalla rimozione anche se è il meno utilizzato
	 * @return true se riesce a rimuovere un editor.
	 */
	private boolean rimuoviElementoMenoUsato(String[] excludeEditorToRemove) {
		if (logger.isDebugEnabled()) {
			logger.debug("--> Raggiungo la capacità della cache. Capacità " + CACHE_SIZE + " elementi in cache "
					+ documentComponentCache.size());
		}
		boolean result = false;

		// Rimuovo l'elemento meno utilizzato, il primo nella mappa
		Collection<PanjeaDocumentComponent> valori = new TreeSet<PanjeaDocumentComponent>(
				new Comparator<PanjeaDocumentComponent>() {

					@Override
					public int compare(PanjeaDocumentComponent o1, PanjeaDocumentComponent o2) {
						return o1.getNumUtilizzi().compareTo(o2.getNumUtilizzi());
					}
				});
		valori.addAll(documentComponentCache.values());
		List<String> editorDaEscludere = Arrays.asList(excludeEditorToRemove);
		for (PanjeaDocumentComponent panjeaDocumentComponent : valori) {
			if (!editorDaEscludere.contains(panjeaDocumentComponent.getPageComponent().getId())) {
				result = true;
				PanjeaDocumentComponent elementRemoved = documentComponentCache.remove(panjeaDocumentComponent
						.getPageComponent().getId());
				if (logger.isDebugEnabled()) {
					logger.debug("--> Rimuovo ed eseguo il dispose del documento "
							+ elementRemoved.getPageComponent().getId() + " con num utilizzi "
							+ elementRemoved.getNumUtilizzi());
				}
				if (elementRemoved.getPageComponent().getControl() != null) {
					elementRemoved.getPageComponent().getControl().removeAll();
				}
				elementRemoved.getPageComponent().dispose();
				elementRemoved = null;
				break;
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Contenuto cache:[");
		for (Entry<String, PanjeaDocumentComponent> entry : documentComponentCache.entrySet()) {
			sb.append("Id Editor:");
			sb.append(entry.getKey());
			sb.append(" num utilizzo: ");
			sb.append(entry.getValue().getNumUtilizzi());
			sb.append(" # ");
		}
		return sb.toString();
	}
}
