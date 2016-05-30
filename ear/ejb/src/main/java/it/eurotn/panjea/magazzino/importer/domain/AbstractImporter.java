/**
 * 
 */
package it.eurotn.panjea.magazzino.importer.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.exporter.exception.ImportException;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author fattazzo
 * 
 */
@Entity
@Table(name = "maga_documenti_importer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING, length = 4)
@DiscriminatorValue("STRD")
@NamedQueries({
		@NamedQuery(name = "AbstractImporter.caricaByCodice", query = "select imp from AbstractImporter imp where imp.codice = :paramCodice"),
		@NamedQuery(name = "AbstractImporter.caricaAllCodici", query = "select imp.codice from AbstractImporter imp") })
public abstract class AbstractImporter extends EntityBase {

	private static final long serialVersionUID = 2367372554873290611L;

	@Column(length = 30)
	private String codice;

	@ManyToOne
	private TipoAreaMagazzino tipoAreaMagazzino;

	/**
	 * Carica tutti i documenti in base alle proprietà configurate.
	 * 
	 * @return documenti caricati
	 * @throws ImportException
	 *             rilanciata se ci sono problemi nella struttura del file.
	 */
	public abstract Collection<DocumentoImport> caricaDocumenti() throws ImportException;

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the tipoAreaMagazzino
	 */
	public TipoAreaMagazzino getTipoAreaMagazzino() {
		return tipoAreaMagazzino;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param tipoAreaMagazzino
	 *            the tipoAreaMagazzino to set
	 */
	public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
		this.tipoAreaMagazzino = tipoAreaMagazzino;
	}
}
