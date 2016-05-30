package it.eurotn.panjea.preventivi.domain;

import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.panjea.preventivi.util.RigaTestataDTO;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

@Entity(name = "RigaTestataPreventivo")
@Audited
@DiscriminatorValue("T")
public class RigaTestata extends RigaPreventivo {
	private static final long serialVersionUID = 2L;

	private String descrizione;

	@Override
	protected RigaPreventivoDTO creaIstanzaRigaPreventivoDTO() {
		return new RigaTestataDTO();
	}

	/**
	 * 
	 * @return descrizione testata
	 */
	public String getDescrizione() {
		return descrizione;
	}
	
	@Override
	public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<html>");
		stringBuffer.append("<b>");
		stringBuffer.append(getDescrizione());
		stringBuffer.append("</b>");
		if (stampaNote && getNoteRiga() != null) {
			stringBuffer.append("<br>");
			stringBuffer.append("<i>");
			stringBuffer.append(getNoteRiga());
			stringBuffer.append("</i>");
		}
		stringBuffer.append("</html>");
		return stringBuffer.toString();
	}
	
	/**
	 * 
	 * @param descrizione
	 *            descrizione testata
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
