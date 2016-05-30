package it.eurotn.rich.editors;

import org.springframework.richclient.settings.support.Memento;

/**
 * Ciclo di vita di una dialog page editr<br>
 * .
 * <ul>
 * <li>preSetFormObject</li>
 * <li>setFormObject</li>
 * <li>postSetFormObject</li>
 * <li>onPrePageOpen</li>
 * <li>loadData o refreshData</li>
 * <li>onPostPageOpen</li>
 * </ul>
 *
 * @author Leonardo
 *
 */
public interface IPageLifecycleAdvisor extends Memento {

    String OBJECT_CHANGED = "formObjectChanged";

    /**
     * Metodo chiamato dall'AbstractEditorDialogPage dalla dispose() gestita a sua volta nella
     * WorkspaceView sulla chiusura del pageComponent quindi dell'editor. L'editor chiama questo
     * metodo per tutte le pagine che contiene.
     */
    void dispose();

    /**
     * carica i dati iniziali.
     */
    void loadData();

    /**
     * Metodo che viene chiamato dopo la visualizzazione della pagina, i controlli qui sono tutti
     * creati e quindi posso agire su qualsiasi elemento, pu� essere utile per agire sui dati del
     * form. (utilizzato principalmente in: nuova entit� da anagrafica esistente)
     */
    void onPostPageOpen();

    /**
     * verifica se si può procedere a processare il contenuto della pagina.
     *
     * @return true se posso processare la pagina.
     */
    boolean onPrePageOpen();

    /**
     * Metodo di comodo per eseguire eventuali operazioni dopo il setFormObject creato per
     * riaggiungere eventuali listener, ormai il setFormObject ha lanciato i suoi eventi sul form
     * model posso quindi ripristinare il normale stato di un form pronto alla modifica.
     *
     * @param object
     *            oggetto che è stato settato nel form
     */
    void postSetFormObject(Object object);

    /**
     * Metodo di comodo per eseguire eventuali operazioni prima del setFormObject creato per
     * rimuovere eventuali listener per evitare che vengano lanciati valueChanged quando sto
     * sostituendo il formObject dall'esterno (caso in cui il workspace non viene chiuso).
     *
     * @param object
     *            oggetto che deve essere settato nel form
     */
    void preSetFormObject(Object object);

    /**
     * aggiorna i dati esistenti.
     */
    void refreshData();

    /**
     * Utilizzato per settare l'oggetto gestito dalla pagina. * @param object oggetto che da settare
     * nel form
     */
    void setFormObject(Object object);

    /**
     * mette a readObly la pagina.
     *
     * @param readOnly
     *            true se voglio rendere la pagina read only
     */
    void setReadOnly(boolean readOnly);
}
