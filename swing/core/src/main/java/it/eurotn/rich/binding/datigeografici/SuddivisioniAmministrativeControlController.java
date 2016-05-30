package it.eurotn.rich.binding.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.rich.binding.searchtext.SearchPanel;

import javax.swing.JLabel;

/**
 * Controller che gestisce i controlli dei livelli amministrativi.
 * 
 * @author leonardo
 * 
 */
public class SuddivisioniAmministrativeControlController {

	private SearchPanel lvl1SearchPanel = null;
	private SearchPanel lvl2SearchPanel = null;
	private SearchPanel lvl3SearchPanel = null;
	private SearchPanel lvl4SearchPanel = null;

	private JLabel lvl1Label = null;
	private JLabel lvl2Label = null;
	private JLabel lvl3Label = null;
	private JLabel lvl4Label = null;

	/**
	 * @param lvl1Label
	 *            the lvl1Label to set
	 */
	public void setLvl1Label(JLabel lvl1Label) {
		this.lvl1Label = lvl1Label;
	}

	/**
	 * @param lvl1SearchPanel
	 *            the lvl1SearchPanel to set
	 */
	public void setLvl1SearchPanel(SearchPanel lvl1SearchPanel) {
		this.lvl1SearchPanel = lvl1SearchPanel;
	}

	/**
	 * @param lvl2Label
	 *            the lvl2Label to set
	 */
	public void setLvl2Label(JLabel lvl2Label) {
		this.lvl2Label = lvl2Label;
	}

	/**
	 * @param lvl2SearchPanel
	 *            the lvl2SearchPanel to set
	 */
	public void setLvl2SearchPanel(SearchPanel lvl2SearchPanel) {
		this.lvl2SearchPanel = lvl2SearchPanel;
	}

	/**
	 * @param lvl3Label
	 *            the lvl3Label to set
	 */
	public void setLvl3Label(JLabel lvl3Label) {
		this.lvl3Label = lvl3Label;
	}

	/**
	 * @param lvl3SearchPanel
	 *            the lvl3SearchPanel to set
	 */
	public void setLvl3SearchPanel(SearchPanel lvl3SearchPanel) {
		this.lvl3SearchPanel = lvl3SearchPanel;
	}

	/**
	 * @param lvl4Label
	 *            the lvl4Label to set
	 */
	public void setLvl4Label(JLabel lvl4Label) {
		this.lvl4Label = lvl4Label;
	}

	/**
	 * @param lvl4SearchPanel
	 *            the lvl4SearchPanel to set
	 */
	public void setLvl4SearchPanel(SearchPanel lvl4SearchPanel) {
		this.lvl4SearchPanel = lvl4SearchPanel;
	}

	/**
	 * Visualizza tutti i controlli.
	 */
	public void showControls() {
		if (lvl1SearchPanel != null) {
			lvl1SearchPanel.setVisible(true);
		}
		if (lvl2SearchPanel != null) {
			lvl2SearchPanel.setVisible(true);
		}
		if (lvl3SearchPanel != null) {
			lvl3SearchPanel.setVisible(true);
		}
		if (lvl4SearchPanel != null) {
			lvl4SearchPanel.setVisible(true);
		}
	}

	/**
	 * @param nazione
	 *            la nazione dalla quale recuperare i livelli disponibili
	 */
	public void updateLivelliVisibility(Nazione nazione) {
		String lvl1 = "";
		String lvl2 = "";
		String lvl3 = "";
		String lvl4 = "";
		boolean lvl1presente = false;
		boolean lvl2presente = false;
		boolean lvl3presente = false;
		boolean lvl4presente = false;
		if (nazione != null && nazione.getId() != null) {
			lvl1presente = nazione.hasLivelloAmministrativo1();
			lvl2presente = nazione.hasLivelloAmministrativo2();
			lvl3presente = nazione.hasLivelloAmministrativo3();
			lvl4presente = nazione.hasLivelloAmministrativo4();

			lvl1 = nazione.getLivelloAmministrativo1() != null ? nazione.getLivelloAmministrativo1() : "";
			lvl2 = nazione.getLivelloAmministrativo2() != null ? nazione.getLivelloAmministrativo2() : "";
			lvl3 = nazione.getLivelloAmministrativo3() != null ? nazione.getLivelloAmministrativo3() : "";
			lvl4 = nazione.getLivelloAmministrativo4() != null ? nazione.getLivelloAmministrativo4() : "";
		}
		if (lvl1SearchPanel != null) {
			lvl1SearchPanel.setVisible(lvl1presente);
		}
		if (lvl1SearchPanel != null) {
			lvl2SearchPanel.setVisible(lvl2presente);
		}
		if (lvl1SearchPanel != null) {
			lvl3SearchPanel.setVisible(lvl3presente);
		}
		if (lvl1SearchPanel != null) {
			lvl4SearchPanel.setVisible(lvl4presente);
		}
		if (lvl1Label != null) {
			lvl1Label.setText(lvl1);
		}
		if (lvl2Label != null) {
			lvl2Label.setText(lvl2);
		}
		if (lvl3Label != null) {
			lvl3Label.setText(lvl3);
		}
		if (lvl4Label != null) {
			lvl4Label.setText(lvl4);
		}
	}

}
