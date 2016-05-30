package it.eurotn.panjea.magazzino.rich.forms.articolo;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

public class CalcolaEanCommand extends ApplicationWindowAwareCommand {

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private String codice;

	public static final String COMMAND_ID = "calcolaEanCommand";

	/**
	 * Costruttore.
	 * 
	 */
	public CalcolaEanCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		codice = this.magazzinoAnagraficaBD.calcolaEAN();
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the magazzinoAnagraficaBD
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            the magazzinoAnagraficaBD to set
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

}
