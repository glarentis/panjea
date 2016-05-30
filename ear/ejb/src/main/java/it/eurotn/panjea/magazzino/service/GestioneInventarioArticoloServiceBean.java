package it.eurotn.panjea.magazzino.service;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.manager.interfaces.GestioneInventarioArticoloManager;
import it.eurotn.panjea.magazzino.service.interfaces.GestioneInventarioArticoloService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.InventarioArticoloDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.GestioneInventarioArticoloService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.GestioneInventarioArticoloService")
public class GestioneInventarioArticoloServiceBean implements GestioneInventarioArticoloService {

	private static Logger logger = Logger.getLogger(GestioneInventarioArticoloServiceBean.class);

	@EJB
	private GestioneInventarioArticoloManager gestioneInventarioArticoloManager;

	@Override
	public void avvaloraGiacenzaRealeInventarioArticolo(Date data, DepositoLite deposito) {
		gestioneInventarioArticoloManager.avvaloraGiacenzaRealeInventarioArticolo(data, deposito);
	}

	@Override
	public void cancellaInventarioArticolo(Date data, DepositoLite deposito) {
		gestioneInventarioArticoloManager.cancellaInventarioArticolo(data, deposito);
	}

	@Override
	public List<Deposito> caricaDepositiPerInventari() {
		return gestioneInventarioArticoloManager.caricaDepositiPerInventari();
	}

	@Override
	public List<InventarioArticoloDTO> caricaInventariiArticoli() {
		return gestioneInventarioArticoloManager.caricaInventariiArticoli();
	}

	@Override
	public List<InventarioArticolo> caricaInventarioArticolo(Date date, DepositoLite depositoLite,
			boolean caricaGiacenzeAZero) {
		return gestioneInventarioArticoloManager.caricaInventarioArticolo(date, depositoLite, caricaGiacenzeAZero);
	}

	@Override
	public InventarioArticolo caricaInventarioArticolo(InventarioArticolo inventarioArticolo) {
		return gestioneInventarioArticoloManager.caricaInventarioArticolo(inventarioArticolo);
	}

	@Override
	public List<InventarioArticolo> caricaInventarioArticolo(Map<Object, Object> parameters) {

		String dataString = (String) parameters.get("data");

		if (dataString == null) {
			throw new IllegalArgumentException("Parametri di ricerca non trovati nella mappa.");
		}

		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		Date data = null;
		try {
			data = df.parse(dataString);
		} catch (ParseException e) {
			logger.error("--> Errore durante il parse della data.", e);
		}

		Integer idDeposito = (Integer) parameters.get("idDeposito");
		DepositoLite deposito = new DepositoLite();
		deposito.setId(idDeposito);

		boolean caricaGiacenzeAZero = (Boolean) parameters.get("caricaGiacenzeAZero");

		List<InventarioArticolo> inventari = caricaInventarioArticolo(data, deposito, caricaGiacenzeAZero);

		return inventari;
	}

	@TransactionTimeout(value = 7200)
	@Override
	public void creaInventariArticolo(Date data, List<DepositoLite> depositi) {
		gestioneInventarioArticoloManager.creaInventariArticolo(data, depositi);
	}

	// @TransactionTimeout(value = 7200)
	@TransactionAttribute(TransactionAttributeType.NEVER)
	@Override
	public List<AreaMagazzinoRicerca> generaInventario(Date dataInventario, Date dataInventarioArticolo,
			DepositoLite deposito) throws TipoDocumentoBaseException {
		return gestioneInventarioArticoloManager.generaInventario(dataInventario, dataInventarioArticolo, deposito);
	}

	@Override
	public List<String> importaArticoliInventario(byte[] fileImportData, Integer idDeposito) {
		return gestioneInventarioArticoloManager.importaArticoliInventario(fileImportData, idDeposito);
	}

	@Override
	public InventarioArticolo salvaInventarioArticolo(InventarioArticolo inventarioArticolo) {
		return gestioneInventarioArticoloManager.salvaInventarioArticolo(inventarioArticolo);
	}
}
