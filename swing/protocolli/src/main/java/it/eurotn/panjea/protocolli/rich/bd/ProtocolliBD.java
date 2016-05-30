/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.bd;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.protocolli.service.interfaces.ProtocolliService;
import it.eurotn.panjea.rich.bd.AbstractBaseBD;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Business Delegate per ProtocolliService.
 * 
 * @author adriano
 * @version 1.0, 10/mag/07
 */
public class ProtocolliBD extends AbstractBaseBD implements IProtocolliBD {

	private static Logger logger = Logger.getLogger(ProtocolliBD.class);

	private ProtocolliService protocolliService;

	/**
	 * Costruttore.
	 * 
	 */
	public ProtocolliBD() {
		super();
	}

	@Override
	public void cancellaProtocollo(Protocollo protocollo) {
		logger.debug("--> Enter cancellaProtocollo");
		start("cancellaProtocollo");
		try {
			this.protocolliService.cancellaProtocollo(protocollo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaProtocollo");
		}
		logger.debug("--> Exit cancellaProtocollo ");

	}

	@Override
	public void cancellaProtocolloAnno(ProtocolloAnno protocolloAnno) {
		logger.debug("--> Enter cancellaProtocolloAnno");
		start("cancellaProtocolloAnno");
		try {
			protocolliService.cancellaProtocolloAnno(protocolloAnno);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("cancellaProtocolloAnno");
		}
		logger.debug("--> Exit cancellaProtocolloAnno ");

	}

	@Override
	public List<Protocollo> caricaProtocolli(String filter) {
		logger.debug("--> Enter caricaProtocolli");
		start("caricaProtocolli");
		List<Protocollo> list = null;
		try {
			list = protocolliService.caricaProtocolli(filter);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaProtocolli");
		}
		logger.debug("--> Exit caricaProtocolli ");
		return list;
	}

	@Override
	public List<ProtocolloAnno> caricaProtocolliAnno(Integer anno, String filter) {
		logger.debug("--> Enter caricaProtocolliAnno");
		start("caricaProtocolliAnno");
		List<ProtocolloAnno> list = null;
		try {
			list = protocolliService.caricaProtocolliAnno(anno, filter);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaProtocolliAnno");
		}
		logger.debug("--> Exit caricaProtocolliAnno ");
		return list;
	}

	@Override
	public List<String> caricaProtocolliCodiceDescrizione(Integer anno) {
		List<ProtocolloAnno> protocolli = caricaProtocolliAnno(anno, null);
		List<String> protocolliString = new ArrayList<String>();
		for (ProtocolloAnno prot : protocolli) {
			protocolliString.add(prot.getProtocollo().getCodice() + " - " + prot.getProtocollo().getDescrizione());
		}
		return protocolliString;
	}

	@Override
	public List<ProtocolloValore> caricaProtocolliValore(String filter) {
		logger.debug("--> Enter caricaProtocolliValore");
		start("caricaProtocolliValore");
		List<ProtocolloValore> list = null;
		try {
			list = this.protocolliService.caricaProtocolliValore(filter);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaProtocolliValore");
		}
		logger.debug("--> Exit caricaProtocolliValore ");
		return list;
	}

	@Override
	public List<String> caricaProtocolliValoreCodiceDescrizione() {
		List<ProtocolloValore> protocolli = caricaProtocolliValore(null);
		List<String> protocolliString = new ArrayList<String>();
		for (ProtocolloValore prot : protocolli) {
			protocolliString.add(prot.getCodice() + " - " + prot.getDescrizione());
		}
		return protocolliString;
	}

	@Override
	public Protocollo caricaProtocollo(Integer id) {
		logger.debug("--> Enter caricaProtocollo");
		start("caricaProtocollo");
		Protocollo protocollo = null;
		try {
			protocollo = protocolliService.caricaProtocollo(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaProtocollo");
		}
		logger.debug("--> Exit caricaProtocollo ");
		return protocollo;
	}

	@Override
	public ProtocolloAnno caricaProtocolloAnno(Integer id) {
		logger.debug("--> Enter caricaProtocolloAnno");
		start("caricaProtocolloAnno");
		ProtocolloAnno protocolloAnno = null;
		try {
			protocolloAnno = protocolliService.caricaProtocolloAnno(id);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("caricaProtocolloAnno");
		}
		logger.debug("--> Exit caricaProtocolloAnno ");
		return protocolloAnno;
	}

	@Override
	public void creaProtocolliPerAnno(int anno) {
		logger.debug("--> Enter creaProtocolliPerAnno");
		start("creaProtocolliPerAnno");
		try {
			protocolliService.creaProtocolliPerAnno(anno);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("creaProtocolliPerAnno");
		}
		logger.debug("--> Exit creaProtocolliPerAnno ");
	}

	/**
	 * @return Returns the protocolliService.
	 */
	public ProtocolliService getProtocolliService() {
		return protocolliService;
	}

	@Override
	public ProtocolloValore salvaProtocollo(ProtocolloValore protocollo) {
		logger.debug("--> Enter salvaProtocollo");
		start("salvaProtocollo");
		ProtocolloValore protocollo2 = null;
		try {
			protocollo2 = protocolliService.salvaProtocollo(protocollo);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaProtocollo");
		}
		logger.debug("--> Exit salvaProtocollo ");
		return protocollo2;
	}

	@Override
	public ProtocolloAnno salvaProtocolloAnno(ProtocolloAnno protocolloAnno) {
		logger.debug("--> Enter salvaProtocolloAnno");
		start("salvaProtocolloAnno");
		ProtocolloAnno protocolloAnno2 = null;
		try {
			protocolloAnno2 = protocolliService.salvaProtocolloAnno(protocolloAnno);
		} catch (Exception e) {
			PanjeaSwingUtil.checkAndThrowException(e);
		} finally {
			end("salvaProtocolloAnno");
		}
		logger.debug("--> Exit salvaProtocolloAnno ");
		return protocolloAnno2;
	}

	/**
	 * @param protocolliService
	 *            The protocolliService to set.
	 */
	public void setProtocolliService(ProtocolliService protocolliService) {
		this.protocolliService = protocolliService;
	}

}
