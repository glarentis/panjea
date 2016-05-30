package it.eurotn.panjea.conai.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

/**
 * Definisce l'articolo associato al materiale conai specifico.
 * 
 * @author leonardo
 */
@Entity
@Audited
@Table(name = "maga_conai_articoli", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceazienda", "materiale" }))
@NamedQueries({
		@NamedQuery(name = "ConaiArticolo.caricaAll", query = "select ca from ConaiArticolo ca where ca.codiceAzienda=:paramCodiceAzienda"),
		@NamedQuery(name = "ConaiArticolo.caricaByMateriale", query = "select ca from ConaiArticolo ca left join fetch ca.esenzioni es left join fetch ca.listiniConai lc where ca.codiceAzienda=:paramCodiceAzienda and ca.materiale=:paramMateriale"),
		@NamedQuery(name = "ConaiArticolo.caricaByArticolo", query = "select c from ConaiArticolo c where c.articolo.id=:paramIdArticolo ", hints = {
				@QueryHint(name = "org.hibernate.cacheable", value = "true"),
				@QueryHint(name = "org.hibernate.cacheRegion", value = "conaiArticolo") }) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "conaiArticolo")
public class ConaiArticolo extends EntityBase implements java.io.Serializable {

	/**
	 * Materiali Conai.
	 * <ul>
	 * <li>ACCIAIO</li>
	 * <li>ALLUMINIO</li>
	 * <li>CARTA</li>
	 * <li>LEGNO</li>
	 * <li>PLASTICA</li>
	 * <li>VETRO</li>
	 * </ul>
	 * 
	 * @author leonardo
	 */
	public enum ConaiMateriale {
		/**
		 * ACCIAIO.
		 */
		ACCIAIO(new ConaiTipoImballo[] { ConaiTipoImballo.BOMBOLE_AEROSOL, ConaiTipoImballo.CAPSULE_ACCIAIO,
				ConaiTipoImballo.CONTENITORI_GENERAL_LINE, ConaiTipoImballo.CONTENITORI_OPEN_TOP,
				ConaiTipoImballo.FUSTI_IN_ACCIAIO, ConaiTipoImballo.TAPPI_CORONA,
				ConaiTipoImballo.POLIACCOPPIATI_A_PREVALENZA_ACCIAIO,
				ConaiTipoImballo.REGGETTA_E_O_FILO_DI_FERRO_PER_IMBALLAGGIO, ConaiTipoImballo.ALTRO_ACCIAIO },
				"Acciaio"),

		/**
		 * ALLUMINIO.
		 */
		ALLUMINIO(new ConaiTipoImballo[] { ConaiTipoImballo.ALLUMINIO_FLESSIBILE_PER_ALIMENTI,
				ConaiTipoImballo.BOMBOLETTE, ConaiTipoImballo.CAPSULE_ALLUMINIO, ConaiTipoImballo.LATTINE_PER_BEVANDE,
				ConaiTipoImballo.FOGLIO_DI_ALLUMINIO, ConaiTipoImballo.SCATOLAME, ConaiTipoImballo.TUBETTI,
				ConaiTipoImballo.VASCHETTE_VASSOI, ConaiTipoImballo.POLIACCOPPIATI_A_PREVALENZA_ALLUMINIO,
				ConaiTipoImballo.ALTRO_ALLUMINIO }, "Alluminio"),

		/**
		 * CARTA.
		 */
		CARTA(new ConaiTipoImballo[] { ConaiTipoImballo.BARATTOLI_TUBI, ConaiTipoImballo.BUSTE,
				ConaiTipoImballo.CARTE_DA_IMBALLO, ConaiTipoImballo.IMBALLAGGI_IN_CARTONE_ONDULATO,
				ConaiTipoImballo.IMBALLAGGI_IN_CARTONE_TESO, ConaiTipoImballo.POLIACCOPPIATI_A_PREVALENZA_CARTA,
				ConaiTipoImballo.SACCHI, ConaiTipoImballo.ETICHETTE, ConaiTipoImballo.SHOPPER_SACCHETTI,
				ConaiTipoImballo.ALTRO_CARTA }, "Carta"),
		/**
		 * LEGNO.
		 */
		LEGNO(new ConaiTipoImballo[] { ConaiTipoImballo.INDUSTRIALI, ConaiTipoImballo.SUGHERO,
				ConaiTipoImballo.BOBINE_E_DOGHE, ConaiTipoImballo.ORTOFRUTTICOLI, ConaiTipoImballo.PALLET,
				ConaiTipoImballo.PALLET_REIMMESSO_AL_CONSUMO_PROVENIENTE_DA_RIFIUTO_RICONDIZIONAMENTO,
				ConaiTipoImballo.MATERIE_PRIME_PER_IMBALLAGGIO_AD_AUTOPRODUTTORI_LEGNO, ConaiTipoImballo.ALTRO_LEGNO },
				"Legno"),
		/**
		 * PLASTICA.
		 */
		PLASTICA(new ConaiTipoImballo[] { ConaiTipoImballo.FILM_ESTENSIBILE_TERMORETRAIBILE,
				ConaiTipoImballo.SHOPPERS_SACCHETTAME, ConaiTipoImballo.SACCONI_BIG_BAGS,
				ConaiTipoImballo.BOTTIGLIE_E_PREFORME, ConaiTipoImballo.FLACONI_PLASTICA,
				ConaiTipoImballo.CONTENITORI_E_VASCHETTE, ConaiTipoImballo.ARTICOLI_MONOUSO_PIATTI_E_BICCHIERI,
				ConaiTipoImballo.CONTENITORI_INDUSTRIALI, ConaiTipoImballo.ALTRI_IMBALLAGGI_DI_PROTEZIONE_E_TRASPORTO,
				ConaiTipoImballo.TAPPI_E_CHIUSURE, ConaiTipoImballo.POLIACCOPPIATI_A_PREVALENZA_PLASTICA,
				ConaiTipoImballo.MATERIE_PRIME_PER_IMBALLAGGIO_AD_AUTOPRODUTTORI_PLASTICA,
				ConaiTipoImballo.ALTRO_PLASTICA }, "Plastica"),
		/**
		 * VETRO.
		 */
		VETRO(new ConaiTipoImballo[] { ConaiTipoImballo.BOTTIGLIE, ConaiTipoImballo.CONTENITORI_AD_USO_FARMACEUTICO,
				ConaiTipoImballo.FIALE_VETRO_TUBO, ConaiTipoImballo.FLACONI_VETRO, ConaiTipoImballo.VASI,
				ConaiTipoImballo.ALTRO_VETRO }, "Vetro");

		private ConaiTipoImballo[] tipiImballo;

		private String descrizione;

		/**
		 * Costruttore.
		 * 
		 * @param tipiImballo
		 *            l'array di tipi imballo per il materiale
		 * @param descrizione
		 *            descrizione
		 */
		ConaiMateriale(final ConaiTipoImballo[] tipiImballo, final String descrizione) {
			this.tipiImballo = tipiImballo;
			this.descrizione = descrizione;
		}

		/**
		 * @return Returns the descrizione.
		 */
		public String getDescrizione() {
			return descrizione;
		}

		/**
		 * @return the tipiImballo
		 */
		public ConaiTipoImballo[] getTipiImballo() {
			return tipiImballo;
		}

	}

	/**
	 * Tipi imballo Conai.
	 * 
	 * @author leonardo
	 */
	public enum ConaiTipoImballo {
		/*
		 * ACCIAIO.
		 */
		BOMBOLE_AEROSOL("Testo1", "Testo16", "Testo17", "Bombole aerosol"), CAPSULE_ACCIAIO("Testo2", "Testo19",
				"Testo20", "Capsule acciaio"), CONTENITORI_GENERAL_LINE("Testo3", "Testo22", "Testo23",
				"Contenitori general line"), CONTENITORI_OPEN_TOP("Testo4", "Testo25", "Testo26",
				"Contenitori open top"), FUSTI_IN_ACCIAIO("Testo5", "Testo28", "Testo29", "Fusti in acciaio"), TAPPI_CORONA(
				"Testo6", "Testo31", "Testo32", "Tappi corona"), POLIACCOPPIATI_A_PREVALENZA_ACCIAIO("Testo7",
				"Testo34", "Testo35", "Poliaccoppiati a prevalenza acciaio"), REGGETTA_E_O_FILO_DI_FERRO_PER_IMBALLAGGIO(
				"Testo8", "Testo38", "Testo39", "Reggetta e o filo di ferro per imballaggio"), ALTRO_ACCIAIO("Testo9",
				"Testo53", "Testo54", "altro acciaio"),
		/*
		 * ALLUMINIO.
		 */
		ALLUMINIO_FLESSIBILE_PER_ALIMENTI("Testo1", "Testo2", "Testo3", "Alluminio flessibile per alimenti"), BOMBOLETTE(
				"Testo9", "Testo10", "Testo11", "Bombolette"), CAPSULE_ALLUMINIO("Testo13", "Testo14", "Testo15",
				"Capsule alluminio"), LATTINE_PER_BEVANDE("Testo17", "Testo18", "Testo19", "Lattine per bevande"), FOGLIO_DI_ALLUMINIO(
				"Testo21", "Testo22", "Testo23", "Foglio di alluminio"), SCATOLAME("Testo25", "Testo26", "Testo27",
				"Scatolame"), TUBETTI("Testo29", "Testo30", "Testo31", "Tubetti"), VASCHETTE_VASSOI("Testo33",
				"Testo34", "Testo35", "Vaschette vassoi"), POLIACCOPPIATI_A_PREVALENZA_ALLUMINIO("Testo37", "Testo38",
				"Testo39", "Poliaccoppiati a prevalenza alluminio"), ALTRO_ALLUMINIO("Testo41", "Testo42", "Testo43",
				"Altro alluminio"),

		/*
		 * CARTA.
		 */
		BARATTOLI_TUBI("Testo1", "Testo2", "Testo3", "Barattoli tubi"), BUSTE("Testo9", "Testo10", "Testo11", "Buste"), CARTE_DA_IMBALLO(
				"Testo13", "Testo14", "Testo15", "Carte da imballo"), IMBALLAGGI_IN_CARTONE_ONDULATO("Testo17",
				"Testo18", "Testo19", "Imballaggi in cartone ondulato"), IMBALLAGGI_IN_CARTONE_TESO("Testo21",
				"Testo22", "Testo23", "Imballaggi in cartone teso"), POLIACCOPPIATI_A_PREVALENZA_CARTA("Testo25",
				"Testo26", "Testo27", "Poliaccoppiati a prevalenza carta"), SACCHI("Testo29", "Testo30", "Testo31",
				"Sacchi"), ETICHETTE("Testo33", "Testo34", "Testo35", "Etichette"), SHOPPER_SACCHETTI("Testo37",
				"Testo38", "Testo39", "Shopper sacchetti"), ALTRO_CARTA("Testo41", "Testo42", "Testo43", "Altro carta"),
		/*
		 * LEGNO.
		 */
		INDUSTRIALI("Testo13", "Testo14", "Testo15", "Industriali"), SUGHERO("Testo17", "Testo18", "Testo19", "Sughero"), BOBINE_E_DOGHE(
				"Testo21", "Testo22", "Testo23", "Bobine e doghe"), ORTOFRUTTICOLI("Testo25", "Testo26", "Testo27",
				"Ortofrutticoli"), PALLET("Testo29", "Testo30", "Testo31", "Pallet"), PALLET_REIMMESSO_AL_CONSUMO_PROVENIENTE_DA_RIFIUTO_RICONDIZIONAMENTO(
				"Testo33", "Testo34", "Testo35", "Pallet reimmesso al consumo proveniente da rifiuto ricondizionamento"), MATERIE_PRIME_PER_IMBALLAGGIO_AD_AUTOPRODUTTORI_LEGNO(
				"Testo37", "Testo38", "Testo39", "Materie prime per imballaggio ad autoproduttori legno"), ALTRO_LEGNO(
				"Testo41", "Testo42", "Testo43", "Altro legno"),
		/*
		 * PLASTICA.
		 */
		FILM_ESTENSIBILE_TERMORETRAIBILE("Testo1", "Testo2", "Testo3", "Film estensibile termoretraibile"), SHOPPERS_SACCHETTAME(
				"Testo9", "Testo10", "Testo11", "Shoppers sacchettame"), SACCONI_BIG_BAGS("Testo13", "Testo14",
				"Testo15", "Sacconi big bags"), BOTTIGLIE_E_PREFORME("Testo17", "Testo18", "Testo19",
				"Bottiglie e preforme"), FLACONI_PLASTICA("Testo21", "Testo22", "Testo23", "Flaconi in plastica"), CONTENITORI_E_VASCHETTE(
				"Testo25", "Testo26", "Testo27", "Contenitori e vaschette"), ARTICOLI_MONOUSO_PIATTI_E_BICCHIERI(
				"Testo29", "Testo30", "Testo31", "Articoli monouso piatti e bicchieri"), CONTENITORI_INDUSTRIALI(
				"Testo33", "Testo34", "Testo35", "Contenitori industriali"), ALTRI_IMBALLAGGI_DI_PROTEZIONE_E_TRASPORTO(
				"Testo37", "Testo38", "Testo39", "Altri imballaggi di protezione e trasporto"), TAPPI_E_CHIUSURE(
				"Testo41", "Testo42", "Testo43", "Tappi e chiusure"), POLIACCOPPIATI_A_PREVALENZA_PLASTICA("Testo7",
				"Testo50", "Testo51", "Poliaccoppiati a prevalenza plastica"), MATERIE_PRIME_PER_IMBALLAGGIO_AD_AUTOPRODUTTORI_PLASTICA(
				"Testo53", "Testo54", "Testo55", "Materie prime per imballaggio ad autoproduttori plastica"), ALTRO_PLASTICA(
				"Testo57", "Testo58", "Testo59", "Altra plastica"),
		/*
		 * VETRO.
		 */
		BOTTIGLIE("Testo33", "Testo34", "Testo35", "Bottiglie"), CONTENITORI_AD_USO_FARMACEUTICO("Testo37", "Testo38",
				"Testo39", "Contenitori ad uso farmaceutico"), FIALE_VETRO_TUBO("Testo41", "Testo42", "Testo43",
				"Fiale vetro tubo"), FLACONI_VETRO("Testo7", "Testo50", "Testo51", "Flaconi in vetro"), VASI("Testo53",
				"Testo54", "Testo55", "Vasi"), ALTRO_VETRO("Testo57", "Testo58", "Testo59", "Altro vetro");

		private String nomeQtaEsenteCampoPdf;
		private String nomeCampoQtaPdf;
		private final String nomeCampoQtaTot;

		private String descrizione;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param nomeQtaEsenteCampoPdf
		 *            nome del field pdf
		 * @param nomeCampoQtaPdf
		 *            nome del field pdf
		 * @param nomeCampoQtaTot
		 *            qta tot
		 * @param descrizione
		 *            descrizione
		 */
		private ConaiTipoImballo(final String nomeQtaEsenteCampoPdf, final String nomeCampoQtaPdf,
				final String nomeCampoQtaTot, final String descrizione) {
			this.nomeQtaEsenteCampoPdf = nomeQtaEsenteCampoPdf;
			this.nomeCampoQtaPdf = nomeCampoQtaPdf;
			this.nomeCampoQtaTot = nomeCampoQtaTot;
			this.descrizione = descrizione;
		}

		/**
		 * @return Returns the descrizione.
		 */
		public String getDescrizione() {
			return descrizione;
		}

		/**
		 * @return Returns the nomeCampoQtaPdf.
		 */
		public String getNomeCampoQtaPdf() {
			return nomeCampoQtaPdf;
		}

		/**
		 * @return Returns the nomeCampoQtaTot.
		 */
		public String getNomeCampoQtaTot() {
			return nomeCampoQtaTot;
		}

		/**
		 * @return Returns the nomeQtaEsenteCampoPdf.
		 */
		public String getNomeQtaEsenteCampoPdf() {
			return nomeQtaEsenteCampoPdf;
		}

	}

	private static final long serialVersionUID = -8579247122050278565L;

	@Enumerated
	private ConaiMateriale materiale;

	@ManyToOne
	@JoinColumn(name = "articolo_id", nullable = false)
	private ArticoloLite articolo;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "conaiArticolo", cascade = { CascadeType.ALL })
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OrderBy(value = "dataFine desc")
	private Set<ConaiListino> listiniConai;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "conaiArticolo", cascade = { CascadeType.ALL })
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ConaiEsenzione> esenzioni;

	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda = null;

	/**
	 * Costruttore.
	 */
	public ConaiArticolo() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param materiale
	 *            il materiale associato
	 */
	public ConaiArticolo(final ConaiMateriale materiale) {
		super();
		this.materiale = materiale;
	}

	/**
	 * Aggiorna la referenza dell'articolo nei listini.
	 */
	public void aggiornaReferenzeConaiArticolo() {
		if (listiniConai != null) {
			for (ConaiListino conaiListino : listiniConai) {
				conaiListino.setConaiArticolo(this);
			}
		}
		if (esenzioni != null) {
			for (ConaiEsenzione esenzione : esenzioni) {
				esenzione.setConaiArticolo(this);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ConaiArticolo other = (ConaiArticolo) obj;
		if (materiale != other.materiale) {
			return false;
		}
		return true;
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * Restituisce se presente l'esenzione associata all'entita' scelta.
	 * 
	 * @param entita
	 *            l'entita' per cui trovare l'esenzione
	 * @return null o l'esenzione trovata per l'entita' scelta
	 */
	public ConaiEsenzione getEsenzione(EntitaLite entita) {
		ConaiEsenzione esenzioneTrovata = null;
		if (esenzioni != null) {
			for (ConaiEsenzione esenzione : esenzioni) {
				if (esenzione.getEntita().getId().equals(entita.getId())) {
					esenzioneTrovata = esenzione;
					break;
				}
			}
		}
		return esenzioneTrovata;
	}

	/**
	 * @return the esenzioni
	 */
	public Set<ConaiEsenzione> getEsenzioni() {
		if (esenzioni == null) {
			esenzioni = new HashSet<ConaiEsenzione>();
		}
		return esenzioni;
	}

	/**
	 * @return the listiniConai
	 */
	public Set<ConaiListino> getListiniConai() {
		if (listiniConai == null) {
			listiniConai = new HashSet<ConaiListino>();
		}
		return listiniConai;
	}

	/**
	 * @return the materiale
	 */
	public ConaiMateriale getMateriale() {
		return materiale;
	}

	/**
	 * Recupera il prezzo del primo listino con data operazione compresa tra data inizio vigore e data fine periodo del
	 * listino.
	 * 
	 * @param dataOperazione
	 *            la data di riferimento dell'operazione per cui trovare il prezzo
	 * @return il prezzo del listino trovato o 0 se non viene trovato nessun listino per la data scelta (o se non ci
	 *         sono listini definiti)
	 */
	public BigDecimal getPrezzo(Date dataOperazione) {
		BigDecimal importo = BigDecimal.ZERO;
		if (listiniConai != null) {
			for (ConaiListino listino : listiniConai) {
				// uso le compareTo invece di after e before perchè la dataOperazione deve essere inclusa
				if (dataOperazione.compareTo(listino.getDataInizio()) >= 0
						&& dataOperazione.compareTo(listino.getDataFine()) <= 0) {
					importo = listino.getPrezzo();
				}
			}
		}
		return importo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((materiale == null) ? 0 : materiale.hashCode());
		return result;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param esenzioni
	 *            the esenzioni to set
	 */
	public void setEsenzioni(Set<ConaiEsenzione> esenzioni) {
		this.esenzioni = esenzioni;
	}

	/**
	 * @param listiniConai
	 *            the listiniConai to set
	 */
	public void setListiniConai(Set<ConaiListino> listiniConai) {
		this.listiniConai = listiniConai;
	}

	/**
	 * @param materiale
	 *            the materiale to set
	 */
	public void setMateriale(ConaiMateriale materiale) {
		this.materiale = materiale;
	}

}
