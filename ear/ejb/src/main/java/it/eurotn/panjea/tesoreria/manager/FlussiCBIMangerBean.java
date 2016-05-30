package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.tesoreria.domain.AreaChiusure;
import it.eurotn.panjea.tesoreria.manager.flussocbi.interfaces.FlussoExporter;
import it.eurotn.panjea.tesoreria.manager.interfaces.FlussiCBIManager;
import it.eurotn.panjea.tesoreria.service.exception.CodiceSIAAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.RapportoBancarioPerFlussoAssenteException;
import it.eurotn.panjea.tesoreria.service.exception.TipoDocumentoChiusuraAssenteException;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.FlussiCBIManger")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.FlussiCBIManger")
public class FlussiCBIMangerBean implements FlussiCBIManager {
	private static Logger logger = Logger.getLogger(FlussiCBIMangerBean.class.getName());
	@Resource
	protected SessionContext context;
	@EJB
	protected AziendeManager aziendeManager;

	@Override
	public String generaFlusso(AreaChiusure areaChiusure) throws RapportoBancarioPerFlussoAssenteException,
			CodiceSIAAssenteException, TipoDocumentoChiusuraAssenteException {
		logger.debug("--> Enter generaFlusso");

		if (aziendeManager.caricaAzienda().getCodiceSIA() == null
				|| aziendeManager.caricaAzienda().getCodiceSIA().isEmpty()) {
			throw new CodiceSIAAssenteException("Codice SIA della Azienda nullo ");
		}
		if (areaChiusure.getTipoAreaPartita().getTipoPagamentoChiusura() == null
				|| areaChiusure.getTipoAreaPartita().getTipoPagamentoChiusura().name().isEmpty()) {
			throw new TipoDocumentoChiusuraAssenteException(
					"Tipo pagamento di chiusura assente sul tipo area partita del documento con codice "
							+ areaChiusure.getDocumento().getCodice());
		}
		String progExporter = "Panjea.FlussoExporter."
				+ areaChiusure.getTipoAreaPartita().getTipoPagamentoChiusura().name();
		FlussoExporter exporter = (FlussoExporter) context.lookup(progExporter);
		String pathFileGenerato = exporter.esportaFlusso(areaChiusure);
		logger.debug("--> Exit generaFlusso con file " + pathFileGenerato);
		return pathFileGenerato;
	}
}
