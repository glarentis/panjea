package it.eurotn.panjea.anagrafica.documenti.service;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.TipoDocumentoManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.documenti.service.interfaces.DocumentiService;
import it.eurotn.panjea.anagrafica.documenti.util.ParametriRicercaDocumento;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.DocumentiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.DocumentiService")
public class DocumentiServiceBean implements DocumentiService {

	@EJB
	private DocumentiManager documentiManager;

	@EJB
	private TipoDocumentoManager tipoDocumentoManager;

	@Override
	public void cancellaDocumento(Documento documento) throws AnagraficaServiceException {
		documentiManager.cancellaDocumento(documento);
	}

	@Override
	public void cancellaTipoDocumento(TipoDocumento tipoDocumento) throws AnagraficaServiceException {
		tipoDocumentoManager.cancellaTipoDocumento(tipoDocumento);
	}

	@Override
	public List<Object> caricaAreeDocumento(Integer idDocumento) {
		return documentiManager.caricaAreeDocumento(idDocumento);
	}

	@Override
	public Documento caricaDocumento(int idDocumento) throws AnagraficaServiceException {
		return documentiManager.caricaDocumento(idDocumento);
	}

	@Override
	public Documento caricaDocumento(int idDocumento, boolean initDocumentiCollegati) {
		return documentiManager.caricaDocumento(idDocumento, initDocumentiCollegati);
	}

	@Override
	public List<TipoDocumento> caricaTipiDocumento(String fieldSearch, String valueSearch, boolean caricaNonAbilitati)
			throws AnagraficaServiceException {
		return tipoDocumentoManager.caricaTipiDocumento(fieldSearch, valueSearch, caricaNonAbilitati);
	}

	@Override
	public List<TipoDocumento> caricaTipiDocumentoPerNoteAutomatiche() {
		return tipoDocumentoManager.caricaTipiDocumentoPerNoteAutomatiche();
	}

	@Override
	public TipoDocumento caricaTipoDocumento(int idTipoDocumento) throws AnagraficaServiceException {
		return tipoDocumentoManager.caricaTipoDocumento(idTipoDocumento);
	}

	@Override
	public TipoDocumento copiaTipoDocumento(String codiceNuovoTipoDocumento, String descrizioneNuovoTipoDocumento,
			TipoDocumento tipoDocumento) {
		return tipoDocumentoManager.copiaTipoDocumento(codiceNuovoTipoDocumento, descrizioneNuovoTipoDocumento,
				tipoDocumento);
	}

	@Override
	public List<Documento> ricercaDocumenti(ParametriRicercaDocumento parametriRicercaDocumento) {
		return documentiManager.ricercaDocumenti(parametriRicercaDocumento);
	}

	@Override
	public Documento salvaDocumento(Documento documento) throws DocumentoDuplicateException {
		return documentiManager.salvaDocumento(documento);
	}

	@Override
	public TipoDocumento salvaTipoDocumento(TipoDocumento tipoDocumento) throws ModificaTipoAreaConDocumentoException {
		return tipoDocumentoManager.salvaTipoDocumento(tipoDocumento);
	}

}
