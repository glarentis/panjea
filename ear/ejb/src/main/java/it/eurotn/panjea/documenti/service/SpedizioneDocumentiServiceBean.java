package it.eurotn.panjea.documenti.service;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.documenti.manager.interfaces.SpedizioneDocumentiManager;
import it.eurotn.panjea.documenti.service.interfaces.SpedizioneDocumentiService;
import it.eurotn.panjea.documenti.util.EtichettaSpedizioneDocumentoDTO;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.SpedizioneDocumentiService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.SpedizioneDocumentiService")
public class SpedizioneDocumentiServiceBean implements SpedizioneDocumentiService {

	@EJB
	private SpedizioneDocumentiManager spedizioneDocumentiManager;

	@Override
	public List<EtichettaSpedizioneDocumentoDTO> caricaEtichetteSpedizioneDocumenti(Map<Object, Object> params) {

		int numeroEtichettaIniziale = (int) params.get("numeroEtichettaIniziale");

		String idDocString = (String) params.get("idDocumenti");
		String[] idDocArray = StringUtils.split(idDocString, ",");
		List<Integer> idDocumenti = new ArrayList<Integer>();
		for (String idDoc : idDocArray) {
			idDocumenti.add(Integer.parseInt(idDoc));
		}

		List<EtichettaSpedizioneDocumentoDTO> etichette = spedizioneDocumentiManager
				.caricaEtichetteSpedizioneDocumenti(idDocumenti, numeroEtichettaIniziale);

		return etichette;
	}

	@Override
	public List<MovimentoSpedizioneDTO> caricaMovimentiPerSpedizione(
			Class<? extends IAreaDocumento> areaDocumentoClass, List<Integer> idDocumenti) {
		return spedizioneDocumentiManager.caricaMovimentiPerSpedizione(areaDocumentoClass, idDocumenti);
	}
}
