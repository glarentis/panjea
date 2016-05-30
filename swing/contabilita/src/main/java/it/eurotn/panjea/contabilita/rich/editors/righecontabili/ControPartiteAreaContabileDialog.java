/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.richclient.dialog.DialogPage;

public class ControPartiteAreaContabileDialog extends PanjeaTitledPageApplicationDialog {

	private static final String ID_DIALOG = "controPartiteAreaContabileDialog";

	private boolean finish = false;

	public ControPartiteAreaContabileDialog(DialogPage dialogPage) {
		super(dialogPage);
		setPreferredSize(new Dimension(500, 300));
		setTitle(getMessage(ID_DIALOG + ".title"));
	}

	/**
	 * Controlla che le tutte le contropaprtite di tipo conto siano ripartite nella varie contropartite di tipo
	 * sottoconto.
	 * 
	 * @return
	 */
	private boolean checkControPartiteValid() {
		Map<Conto, ControPartita> mapTmp = new HashMap<Conto, ControPartita>();

		List<ControPartita> listControPartite = ((ControPartiteAreaContabilePage) this.getDialogPage())
				.getControPartiteAll();

		for (ControPartita controPartita : listControPartite) {
			switch (controPartita.getTipologiaContoControPartita()) {
			case CONTO:
				Conto conto;
				if (controPartita.getContoAvere() != null && controPartita.getContoAvere().getId() != -1) {
					conto = controPartita.getContoAvere();
				} else {
					conto = controPartita.getContoDare();
				}

				// se la contro partita e' di tipo conto e l'importo e' zero nella mappa non lascio il valore a null
				if (controPartita.getImporto().equals(BigDecimal.ZERO)) {
					mapTmp.put(conto, new ControPartita());
				} else {
					mapTmp.put(conto, null);
				}
				break;
			case SOTTOCONTO:
				// se la contropartita non ha id ( significa che e' una controparita generata da una di tipo conto )
				// la inserisco nella mappa
				if (controPartita.getId() == null || controPartita.getId() == -1) {
					SottoConto sottoConto;
					if (controPartita.getAvere() != null && controPartita.getAvere().getId() != -1) {
						sottoConto = controPartita.getAvere();
					} else {
						sottoConto = controPartita.getDare();
					}
					mapTmp.put(sottoConto.getConto(), controPartita);
				}
				break;
			}
		}

		Set<Conto> chiavi = mapTmp.keySet();
		for (Conto conto : chiavi) {
			// se risulta che un valore nella mappa e' vuoto significa che per il relativo
			// conto non e' stata generata nessuna partita di tipo sottoconto.
			if (mapTmp.get(conto) == null) {
				return false;
			}
		}

		return true;
	}

	public List<ControPartita> getControPartiteCompilate() {
		return ((ControPartiteAreaContabilePage) this.getDialogPage()).getControPartiteCompilate();
	}

	public boolean isFinish() {
		return finish;
	}

	@Override
	protected void onCancel() {
		finish = false;
		super.onCancel();
	}

	@Override
	protected boolean onFinish() {
		finish = true;
		return checkControPartiteValid();
	}

}