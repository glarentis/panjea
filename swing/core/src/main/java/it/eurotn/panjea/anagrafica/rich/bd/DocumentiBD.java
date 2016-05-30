package it.eurotn.panjea.anagrafica.rich.bd;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.interfaces.DocumentiService;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.service.interfaces.DocumentiGraphService;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class DocumentiBD extends AbstractBaseBD implements IDocumentiBD {

	public static final String BEAN_ID = "documentiBD";
	private static Logger logger = Logger.getLogger(DocumentiBD.class);

	private DocumentiService documentiService;
	private DocumentiGraphService documentiGraphService;

	@Override
	public void cancellaDocumento(Documento documento) {
		logger.debug("--> Enter cancellaDocumento");
		start("cancellaDocumento");
		try {
			documentiService.cancellaDocumento(documento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaDocumento");
		}
		logger.debug("--> Exit cancellaDocumento");
	}

	@Override
	public void cancellaTipoDocumento(TipoDocumento tipoDocumento) {
		logger.debug("--> Enter cancellaTipoDocumento");
		start("cancellaTipoDocumento");
		try {
			documentiService.cancellaTipoDocumento(tipoDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaTipoDocumento");
		}
		logger.debug("--> Exit cancellaTipoDocumento");
	}

	@Override
	public List<Object> caricaAreeDocumento(Integer idDocumento) {
		logger.debug("--> Enter caricaAreeDocumento");
		start("caricaAreeDocumento");
		List<Object> list = null;
		try {
			list = documentiService.caricaAreeDocumento(idDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaAreeDocumento");
		}
		logger.debug("--> Exit caricaAreeDocumento ");
		return list;
	}

	@Override
	public Documento caricaDocumento(int idDocumento) {
		logger.debug("--> Enter caricaDocumento");
		start("caricaDocumento");
		Documento documento = null;
		try {
			documento = documentiService.caricaDocumento(idDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDocumento");
		}
		logger.debug("--> Exit caricaDocumento");
		return documento;
	}

	@Override
	public Documento caricaDocumento(int idDocumento, boolean initDocumentiCollegati) {
		logger.debug("--> Enter caricaDocumento");
		start("caricaDocumento");
		Documento result = null;
		try {
			result = documentiService.caricaDocumento(idDocumento, initDocumentiCollegati);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaDocumento");
		}
		logger.debug("--> Exit caricaDocumento ");
		return result;
	}

	@Override
	public List<TipoDocumento> caricaTipiDocumento(String fieldSearch, String valueSearch, boolean caricaNonAbilitati) {
		logger.debug("--> Enter caricaTipiDocumento");
		start("caricaTipiDocumento");
		List<TipoDocumento> listTipiDoc = new ArrayList<TipoDocumento>();
		try {
			listTipiDoc = documentiService.caricaTipiDocumento(fieldSearch, valueSearch, caricaNonAbilitati);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiDocumento");
		}
		logger.debug("--> Exit caricaTipiDocumento");
		return listTipiDoc;
	}

	@Override
	public List<TipoDocumento> caricaTipiDocumentoPerNoteAutomatiche() {
		logger.debug("--> Enter caricaTipiDocumentoPerNoteAutomatiche");
		start("caricaTipiDocumentoPerNoteAutomatiche");
		List<TipoDocumento> tipiDoc = new ArrayList<TipoDocumento>();
		try {
			tipiDoc = documentiService.caricaTipiDocumentoPerNoteAutomatiche();
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipiDocumentoPerNoteAutomatiche");
		}
		logger.debug("--> Exit caricaTipiDocumentoPerNoteAutomatiche ");
		return tipiDoc;
	}

	@Override
	public TipoDocumento caricaTipoDocumento(int idTipoDocumento) {
		logger.debug("--> Enter caricaTipoDocumento");
		start("caricaTipoDocumento");
		TipoDocumento tipoDocumento = null;
		try {
			tipoDocumento = documentiService.caricaTipoDocumento(idTipoDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaTipoDocumento");
		}
		logger.debug("--> Exit caricaTipoDocumento");
		return tipoDocumento;
	}

	@Override
	public TipoDocumento copiaTipoDocumento(String codiceNuovoTipoDocumento, String descrizioneNuovoTipoDocumento,
			TipoDocumento tipoDocumento) {
		logger.debug("--> Enter copiaTipoDocumento");
		start("copiaTipoDocumento");
		TipoDocumento tipoDocumentoCopia = null;
		try {
			tipoDocumentoCopia = documentiService.copiaTipoDocumento(codiceNuovoTipoDocumento,
					descrizioneNuovoTipoDocumento, tipoDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("copiaTipoDocumento");
		}
		logger.debug("--> Exit copiaTipoDocumento ");
		return tipoDocumentoCopia;
	}

	@Override
	public DocumentoNode createNode(DocumentoGraph documentoGraph, boolean loadAllNodes) {
		logger.debug("--> Enter createNode");
		start("createNode");
		DocumentoNode documentoNode = null;
		try {
			documentoNode = documentiGraphService.createNode(documentoGraph, loadAllNodes);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("createNode");
		}
		logger.debug("--> Exit createNode ");
		return documentoNode;
	}

	@Override
	public List<Documento> ricercaDocumenti(ParametriRicercaDocumento parametriRicercaDocumento) {
		logger.debug("--> Enter ricercaDocumenti");
		start("ricercaDocumenti");
		List<Documento> documenti = new ArrayList<Documento>();
		try {
			documenti = documentiService.ricercaDocumenti(parametriRicercaDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("ricercaDocumenti");
		}
		logger.debug("--> Exit ricercaDocumenti ");
		return documenti;
	}

	@Override
	public Documento salvaDocumento(Documento documento) {
		logger.debug("--> Enter salvaDocumento");
		start("salvaDocumento");
		Documento documentoSalvato = null;
		try {
			documentoSalvato = documentiService.salvaDocumento(documento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaDocumento");
		}
		logger.debug("--> Exit salvaDocumento");
		return documentoSalvato;
	}

	@Override
	public TipoDocumento salvaTipoDocumento(TipoDocumento tipoDocumento) {
		logger.debug("--> Enter salvaTipoDocumento");
		start("salvaTipoDocumento");
		TipoDocumento tipoDocumentoSalvato = null;
		try {
			tipoDocumentoSalvato = documentiService.salvaTipoDocumento(tipoDocumento);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaTipoDocumento");
		}
		logger.debug("--> Exit salvaTipoDocumento");
		return tipoDocumentoSalvato;
	}

	/**
	 * @param documentiGraphService
	 *            The documentiGraphService to set.
	 */
	public void setDocumentiGraphService(DocumentiGraphService documentiGraphService) {
		this.documentiGraphService = documentiGraphService;
	}

	/**
	 * 
	 * @param documentiService
	 *            service dei diocumenti
	 */
	public void setDocumentiService(DocumentiService documentiService) {
		this.documentiService = documentiService;
	}

}
