/**
 *
 */
package it.eurotn.panjea.partite.rich.commands;

import it.eurotn.panjea.rate.rich.bd.IRateBD;
import it.eurotn.panjea.rate.rich.editors.rate.riemissione.RiemettiRataPage;
import it.eurotn.panjea.tesoreria.util.RataRiemessa;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.util.List;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.JideButton;

/**
 * @author leonardo
 */
public class RiemettiRataCommand extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "riemettiRataCommand";
	public static final String PARAM_RATE = "rateParam";

	private IRateBD rateBD;
	private boolean riemesse;

	/**
	 * Costruttore.
	 */
	public RiemettiRataCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		setEnabled(false);
		this.rateBD = RcpSupport.getBean("rateBD");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doExecuteCommand() {
		riemesse = false;
		List<RataRiemessa> rateDaRiemettere = (List<RataRiemessa>) getParameter(PARAM_RATE);
		final RiemettiRataPage riemettiRataPage = new RiemettiRataPage(rateBD, rateDaRiemettere);
		PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(riemettiRataPage) {

			@Override
			protected boolean onFinish() {
				if (riemettiRataPage.isRateValid()) {
					List<RataRiemessa> rateModificate = riemettiRataPage.getInsoluti();
					for (RataRiemessa rataRiemessaDaGenerare : rateModificate) {
						rateBD.riemettiRate(rataRiemessaDaGenerare);
					}
				}
				riemesse = true;
				return true;
			}

		};
		dialog.showDialog();
	}

	/**
	 * @return RataRiemessaDTO
	 */
	public boolean isRiemesse() {
		return riemesse;
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		// per la toolbar viene creato un jidebutton, per il menù contestuale un JideMenuItem,
		// rimuovo la label del button solo per la toolbar
		if (button instanceof JideButton) {
			button.setText(" ");
		}
	}
}