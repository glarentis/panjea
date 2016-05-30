/**
 *
 */
package it.eurotn.panjea.intra.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "intr_aree_intra")
public class AreaIntra extends EntityBase implements IAreaDocumento {

	private static final long serialVersionUID = 3811760036016476600L;

	@OneToOne(fetch = FetchType.LAZY)
	private Documento documento;

	/**
	 * Paese di pagamento.
	 */
	@Column(length = 3)
	private String paesePagamento;

	private boolean operazioneTriangolare;

	/**
	 * Valorizzare solo in caso di dichiarazione mensile.
	 */
	@Enumerated
	private ModalitaTrasporto modalitaTrasporto;

	/**
	 * Valorizzare solo in caso di dichiarazione mensile.
	 */
	@Enumerated
	private GruppoCondizioneConsegna gruppoCondizioneConsegna;

	@Enumerated
	private NaturaTransazione naturaTransazione;

	@Enumerated
	private ModalitaIncasso modalitaIncasso;

	@Enumerated
	private TipoPeriodo tipoPeriodo;

	/**
	 * Provincia di destinazione (acquisti), di origine (cessioni).<br>
	 * Acquisti: la sigla della provincia in cui le merci sono destinate al consumo/commercializzazione.<br>
	 * Cessioni: la sigla della provincia di origine/produzione delle merci.
	 */
	private String provincia;

	/**
	 * Paese di origine (acquisti), di destinazione (cessioni).
	 */
	private String paese;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "areaIntra", cascade = { CascadeType.ALL })
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<RigaIntra> righeIntra;

	@Transient
	private Set<RigaServizioIntra> righeServizio;

	@Transient
	private Set<RigaBeneIntra> righeBene;

	/**
	 * Init di valori di default.
	 */
	{
		operazioneTriangolare = false;
		righeServizio = new HashSet<RigaServizioIntra>();
		righeBene = new HashSet<RigaBeneIntra>();
	}

	/**
	 * Costruttore.
	 */
	public AreaIntra() {
		super();
	}

	@Override
	public Map<String, Object> fillVariables() {
		return new HashMap<String, Object>();
	}

	@Override
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the gruppoCondizioneConsegna
	 */
	public GruppoCondizioneConsegna getGruppoCondizioneConsegna() {
		return gruppoCondizioneConsegna;
	}

	/**
	 * @return the modalitaIncasso
	 */
	public ModalitaIncasso getModalitaIncasso() {
		return modalitaIncasso;
	}

	/**
	 * @return the modalitaTrasporto
	 */
	public ModalitaTrasporto getModalitaTrasporto() {
		return modalitaTrasporto;
	}

	/**
	 * @return the naturaTransazione
	 */
	public NaturaTransazione getNaturaTransazione() {
		return naturaTransazione;
	}

	/**
	 * @return Returns the paese.
	 */
	public String getPaese() {
		return paese;
	}

	/**
	 * @return Returns the paesePagamento.
	 */
	public String getPaesePagamento() {
		return paesePagamento;
	}

	/**
	 * @return Returns the provincia.
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @return Returns the righeBene.
	 */
	public Set<RigaBeneIntra> getRigheBene() {
		initRigheIntra();
		return righeBene;
	}

	/**
	 * @return the righeIntra
	 */
	public Set<RigaIntra> getRigheIntra() {
		return righeIntra;
	}

	/**
	 * @return Returns the righeServizio.
	 */
	public Set<RigaServizioIntra> getRigheServizio() {
		initRigheIntra();
		return righeServizio;
	}

	@Override
	public IStatoDocumento getStato() {
		return null;
	}

	@Override
	public StatoSpedizione getStatoSpedizione() {
		return null;
	}

	@Override
	public ITipoAreaDocumento getTipoAreaDocumento() {
		return null;
	}

	/**
	 * @return the tipoPeriodo
	 */
	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	/**
	 * inizializza le righe dell'intra separandole in servizi o beni.
	 */
	private void initRigheIntra() {
		righeServizio.clear();
		righeBene.clear();
		if (righeIntra != null) {
			for (RigaIntra rigaIntra : righeIntra) {
				if (rigaIntra instanceof RigaServizioIntra) {
					righeServizio.add((RigaServizioIntra) rigaIntra);
				} else {
					righeBene.add((RigaBeneIntra) rigaIntra);
				}
			}
		}
	}

	/**
	 * @return the operazioneTriangolare
	 */
	public boolean isOperazioneTriangolare() {
		return operazioneTriangolare;
	}

	@Override
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param gruppoCondizioneConsegna
	 *            the gruppoCondizioneConsegna to set
	 */
	public void setGruppoCondizioneConsegna(GruppoCondizioneConsegna gruppoCondizioneConsegna) {
		this.gruppoCondizioneConsegna = gruppoCondizioneConsegna;
	}

	/**
	 * @param modalitaIncasso
	 *            the modalitaIncasso to set
	 */
	public void setModalitaIncasso(ModalitaIncasso modalitaIncasso) {
		this.modalitaIncasso = modalitaIncasso;
	}

	/**
	 * @param modalitaTrasporto
	 *            the modalitaTrasporto to set
	 */
	public void setModalitaTrasporto(ModalitaTrasporto modalitaTrasporto) {
		this.modalitaTrasporto = modalitaTrasporto;
	}

	/**
	 * @param naturaTransazione
	 *            the naturaTransazione to set
	 */
	public void setNaturaTransazione(NaturaTransazione naturaTransazione) {
		this.naturaTransazione = naturaTransazione;
	}

	/**
	 * @param operazioneTriangolare
	 *            the operazioneTriangolare to set
	 */
	public void setOperazioneTriangolare(boolean operazioneTriangolare) {
		this.operazioneTriangolare = operazioneTriangolare;
	}

	/**
	 * @param paese
	 *            The paese to set.
	 */
	public void setPaese(String paese) {
		this.paese = paese;
	}

	/**
	 * @param paesePagamento
	 *            The paesePagamento to set.
	 */
	public void setPaesePagamento(String paesePagamento) {
		this.paesePagamento = paesePagamento;
	}

	/**
	 * @param provincia
	 *            The provincia to set.
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	/**
	 *
	 * @param righeBene
	 *            righeBene da settare
	 */
	public void setRigheBene(Set<RigaBeneIntra> righeBene) {
		if (righeIntra == null) {
			righeIntra = new HashSet<RigaIntra>();
		}
		righeIntra.removeAll(this.righeBene);
		this.righeBene = righeBene;
		righeIntra.addAll(this.righeBene);
	}

	/**
	 * @param righeIntra
	 *            the righeIntra to set
	 */
	public void setRigheIntra(Set<RigaIntra> righeIntra) {
		this.righeIntra = righeIntra;
	}

	/**
	 *
	 * @param righeServizio
	 *            righe servizio da modificare
	 */
	public void setRigheServizio(Set<RigaServizioIntra> righeServizio) {
		if (righeIntra == null) {
			righeIntra = new HashSet<RigaIntra>();
		}
		righeIntra.removeAll(this.righeServizio);
		this.righeServizio = righeServizio;
		righeIntra.addAll(this.righeServizio);
	}

	@Override
	public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
	}

	@Override
	public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {

	}

	/**
	 * @param tipoPeriodo
	 *            the tipoPeriodo to set
	 */
	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
		if (tipoPeriodo != null) {
			// se trimestrale, questi campi dell'area intra non devono essere avvalorati
			// TODO annullare i valori anche sui relativi set nel caso in cui tipo periodo sia trimestrale ??
			if (tipoPeriodo.equals(TipoPeriodo.T)) {
				modalitaTrasporto = null;
				gruppoCondizioneConsegna = null;
				paese = null;
				provincia = null;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Nomenclatura [nomenclatura=");
		sb.append(", operazioneTriangolare=");
		sb.append(operazioneTriangolare);
		sb.append(", getId()=");
		sb.append(getId());
		sb.append("]");
		return sb.toString();
	}

}
