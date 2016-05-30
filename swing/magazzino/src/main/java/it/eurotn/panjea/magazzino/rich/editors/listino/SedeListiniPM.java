package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * PM per la sede Listini. Mantengo anche delle variabili per bindare il form di associazione.
 * 
 * @author giangi
 * @version 1.0, 27/mar/2012
 * 
 */
public class SedeListiniPM implements IDefProperty {

	private List<TipoEntita> tipiEntita;

	private RiepilogoSedeEntitaDTO sedeRiepilogo;

	private Listino listino;

	private boolean listinoAssociatoForm;
	private boolean listinoAlternativoAssociatoForm;
	private EntitaLite entitaForm;

	private SedeMagazzino sedeMagazzinoForm;

	/**
	 * Costruttore.
	 * 
	 */
	public SedeListiniPM() {
		super();
		this.sedeRiepilogo = new RiepilogoSedeEntitaDTO();
		this.listino = new Listino();

		entitaForm = new ClienteLite();
		listinoAlternativoAssociatoForm = false;
		listinoAssociatoForm = false;
		sedeMagazzinoForm = new SedeMagazzino();
		this.tipiEntita = new ArrayList<TipoEntita>();
		this.tipiEntita.add(TipoEntita.FORNITORE);
		this.tipiEntita.add(TipoEntita.CLIENTE);
	}

	/**
	 * Costruttore.
	 * 
	 * @param riepilogoSede
	 *            sede magazzino
	 * @param listino
	 *            listino di riferimento
	 * 
	 */
	public SedeListiniPM(final RiepilogoSedeEntitaDTO riepilogoSede, final Listino listino) {
		this();
		this.sedeRiepilogo = riepilogoSede;
		this.listino = listino;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SedeListiniPM other = (SedeListiniPM) obj;
		if (sedeRiepilogo == null) {
			if (other.sedeRiepilogo != null) {
				return false;
			}
		} else if (!sedeRiepilogo.equals(other.sedeRiepilogo)) {
			return false;
		}
		return true;
	}

	@Override
	public String getDomainClassName() {
		return this.getClass().getName();
	}

	/**
	 * @return Returns the entitaForm.
	 */
	public EntitaLite getEntitaForm() {
		return entitaForm;
	}

	@Override
	public Integer getId() {
		return sedeRiepilogo.getSedeEntita().getId();
	}

	/**
	 * @return the listino
	 */
	public Listino getListino() {
		return listino;
	}

	/**
	 * @return Returns the sedeMagazzinoForm.
	 */
	public SedeMagazzino getSedeMagazzinoForm() {
		return sedeMagazzinoForm;
	}

	/**
	 * @return Returns the sedeRiepilogo.
	 */
	public RiepilogoSedeEntitaDTO getSedeRiepilogo() {
		return sedeRiepilogo;
	}

	/**
	 * @return the tipiEntita
	 */
	public List<TipoEntita> getTipiEntita() {
		return tipiEntita;
	}

	@Override
	public Integer getVersion() {
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sedeRiepilogo == null) ? 0 : sedeRiepilogo.hashCode());
		return result;
	}

	/**
	 * @return the listinoAlternativoAssociato
	 */
	public boolean isListinoAlternativoAssociato() {
		return listino.getId() != null && listino.getId().equals(sedeRiepilogo.getIdListinoAlternativo());
	}

	/**
	 * @return Returns the listinoAlternativoAssociatoForm.
	 */
	public boolean isListinoAlternativoAssociatoForm() {
		return listinoAlternativoAssociatoForm;
	}

	/**
	 * @return the listinoAssociato
	 */
	public boolean isListinoAssociato() {
		return listino.getId() != null && listino.getId().equals(sedeRiepilogo.getIdListino());
	}

	/**
	 * @return Returns the listinoAssociatoForm.
	 */
	public boolean isListinoAssociatoForm() {
		return listinoAssociatoForm;
	}

	@Override
	public boolean isNew() {
		return getId() == null;
	}

	/**
	 * @param entitaForm
	 *            The entitaForm to set.
	 */
	public void setEntitaForm(EntitaLite entitaForm) {
		this.entitaForm = entitaForm;
	}

	/**
	 * @param listinoAlternativoAssociatoForm
	 *            The listinoAlternativoAssociatoForm to set.
	 */
	public void setListinoAlternativoAssociatoForm(boolean listinoAlternativoAssociatoForm) {
		this.listinoAlternativoAssociatoForm = listinoAlternativoAssociatoForm;
	}

	/**
	 * @param listinoAssociatoForm
	 *            The listinoAssociatoForm to set.
	 */
	public void setListinoAssociatoForm(boolean listinoAssociatoForm) {
		this.listinoAssociatoForm = listinoAssociatoForm;
	}

	/**
	 * @param sedeMagazzino
	 *            the sedeMagazzino to set
	 */
	public void setSedeMagazzino(SedeMagazzino sedeMagazzino) {
		// this.sedeRiepilogo = sedeMagazzino;
	}

	/**
	 * @param sedeMagazzinoForm
	 *            The sedeMagazzinoForm to set.
	 */
	public void setSedeMagazzinoForm(SedeMagazzino sedeMagazzinoForm) {
		this.sedeMagazzinoForm = sedeMagazzinoForm;
	}

	/**
	 * @param sedeRiepilogo
	 *            The sedeRiepilogo to set.
	 */
	public void setSedeRiepilogo(RiepilogoSedeEntitaDTO sedeRiepilogo) {
		this.sedeRiepilogo = sedeRiepilogo;
	}

	/**
	 * @param tipiEntita
	 *            the tipiEntita to set
	 */
	public void setTipiEntita(List<TipoEntita> tipiEntita) {
		this.tipiEntita = tipiEntita;
	}

}
