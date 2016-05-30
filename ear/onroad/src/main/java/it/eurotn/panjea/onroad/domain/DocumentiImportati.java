package it.eurotn.panjea.onroad.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DocumentiImportati {

	private Map<String, DocumentoOnRoad> documenti;
	private List<RigaDocumentoOnroad> righeDocumento;
	private List<RigaIvaOnRoad> righeIva;

	private static Logger logger = Logger.getLogger(DocumentiImportati.class);

	/**
	 * Costruttore.
	 */
	public DocumentiImportati() {
		documenti = new HashMap<String, DocumentoOnRoad>();
		righeDocumento = new ArrayList<RigaDocumentoOnroad>();
		righeIva = new ArrayList<RigaIvaOnRoad>();
	}

	/**
	 * Aggiunge una riga ai documenti.
	 * 
	 * @param riga
	 *            riga importata
	 */
	public void aggiungiRigaDocumento(RigaDocumentoOnroad riga) {
		righeDocumento.add(riga);
	}

	/**
	 * Aggiunge una riga iva onroad ai documenti.
	 * 
	 * @param rigaIva
	 *            riga iva importata
	 */
	public void aggiungiRigaIvaDocumento(RigaIvaOnRoad rigaIva) {
		righeIva.add(rigaIva);
	}

	/**
	 * aggiunge una testata documento.
	 * 
	 * @param documentoImportato
	 *            testata documento importato
	 */
	public void aggiungiTestata(DocumentoOnRoad documentoImportato) {
		documenti.put(documentoImportato.getChiaveDocumento(), documentoImportato);
	}

	/**
	 * Restituisce la lista di documenti onRoad con associate le relative righe e righe iva.
	 * 
	 * @return List<DocumentoOnRoad>
	 */
	public List<DocumentoOnRoad> getDocumenti() {
		int numDocs = 0;
		int numRigheDoc = 0;
		int numRigheIva = 0;
		List<DocumentoOnRoad> docs = new ArrayList<DocumentoOnRoad>();
		for (String chiaveDocumento : documenti.keySet()) {

			DocumentoOnRoad doc = documenti.get(chiaveDocumento);

			for (RigaDocumentoOnroad rigaDocu : righeDocumento) {
				String chiaveRiga = rigaDocu.getChiaveDocumento();
				if (chiaveRiga.equals(chiaveDocumento)) {
					doc.addRigaDocumento(rigaDocu);
				}
			}

			for (RigaIvaOnRoad rigaIva : righeIva) {
				String chiaveRiga = rigaIva.getChiaveDocumento();
				if (chiaveRiga.equals(chiaveDocumento)) {
					doc.addRigaIva(rigaIva);
				}
			}

			numDocs = numDocs + 1;
			numRigheDoc = numRigheDoc + doc.getRighe().size();
			numRigheIva = numRigheIva + doc.getRigheIva().size();

			docs.add(doc);
		}

		// i tipi documento onroad sono i valori dell'enum OnRoadTipoDocumento;
		// getOrdine restituisce l'ordinal dell'enum e quindi l'ordine dei tipi documento; per tipo documento uguale,
		// ordino per identificativo documento.
		// Prima devono esserci i DDT e poi gli altri tipi dato che senza il carico sul furgone non possono esserci
		// vendite.
		Collections.sort(docs, new Comparator<DocumentoOnRoad>() {

			@Override
			public int compare(DocumentoOnRoad o1, DocumentoOnRoad o2) {
				int compareOrdine = o1.getOrdine().compareTo(o2.getOrdine());
				if (compareOrdine == 0) {
					compareOrdine = o1.getIdentificativoDocumento().compareTo(o2.getIdentificativoDocumento());
				}
				return compareOrdine;
			}
		});

		return docs;
	}

}
