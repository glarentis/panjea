package com.jidesoft.spring.richclient.docking.editor;

import org.springframework.richclient.application.PageComponent;

import com.jidesoft.document.DocumentComponent;

public class PanjeaDocumentComponent extends DocumentComponent {
	private PageComponent pageComponent;
	private boolean enableCache;
	private Integer numUtilizzi;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param id
	 *            id del document
	 * @param pageComponent
	 *            componente del document
	 */
	public PanjeaDocumentComponent(final String id, final PageComponent pageComponent) {
		super(pageComponent.getContext().getPane().getControl(), id, pageComponent.getDisplayName(), pageComponent
				.getIcon());
		setTooltip(pageComponent.getDescription());
		this.pageComponent = pageComponent;
		numUtilizzi = 0;
	}

	/**
	 * aggiunge il numero di utilizzi all'editor.
	 */
	public void addNumUtilizzi() {
		numUtilizzi++;
	}

	/**
	 * 
	 * @param paramEnableCache
	 *            indica se abilitare il document ad essere inserito in cache.
	 */
	public void enableCache(boolean paramEnableCache) {
		this.enableCache = paramEnableCache;
	}

	/**
	 * @return Returns the numUtilizzi.
	 */
	public Integer getNumUtilizzi() {
		return numUtilizzi;
	}

	/**
	 * @return Returns the pageComponent.
	 */
	public PageComponent getPageComponent() {
		return pageComponent;
	}

	/**
	 * 
	 * @return true se i componente può essere inserito in cache
	 */
	public boolean isCacheable() {
		return enableCache && ((AbstractEditor) pageComponent).isEnableCache();
	}

}
