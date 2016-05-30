package it.eurotn.panjea.anagrafica.rich.editors.rubrica;

import it.eurotn.panjea.anagrafica.util.RubricaDTO;

import com.jidesoft.grid.DefaultExpandableRow;

public class RubricaRow extends DefaultExpandableRow {

	private final RubricaDTO rubricaDTO;

	/**
	 * Costruttore.
	 *
	 * @param rubricaDTO
	 *            riga della rubrica da visualizzare
	 */
	public RubricaRow(final RubricaDTO rubricaDTO) {
		super();
		this.rubricaDTO = rubricaDTO;
	}

	/**
	 * @return Returns the rubricaDTO.
	 */
	public RubricaDTO getRubricaDTO() {
		return rubricaDTO;
	}

	@Override
	public Object getValueAt(int colonna) {
		switch (colonna) {
		case 0:
			return rubricaDTO.getDenominazione() + " - " + rubricaDTO.getCodice();
		case 1:
			if (rubricaDTO.getPiva() != null && rubricaDTO.getPiva().length() > 0) {
				return rubricaDTO.getPiva();
			} else {
				return rubricaDTO.getCodiceFiscale();
			}
		case 2:
			return rubricaDTO.getIndirizzo();
		case 3:
			return rubricaDTO.getLocalita();
		case 4:
			return rubricaDTO.getCap();
		case 5:
			return rubricaDTO.getLivelloAmministrativo4();
		case 6:
			return rubricaDTO.getLivelloAmministrativo3();
		case 7:
			return rubricaDTO.getLivelloAmministrativo2();
		case 8:
			return rubricaDTO.getLivelloAmministrativo1();
		case 9:
			return rubricaDTO.getNazione();
		case 10:
			return rubricaDTO.getEmail();
		case 11:
			return rubricaDTO.getIndirizzoPEC();
		case 12:
			return rubricaDTO.getIndirizzoMailSpedizione();
		case 13:
			return rubricaDTO.getTelefono();
		case 14:
			return rubricaDTO.getCell();
		case 15:
			return rubricaDTO.getFax();
		default:
			return null;
		}
	}
}
