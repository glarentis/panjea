package it.eurotn.panjea.tesoreria.solleciti;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.util.PanjeaEJBUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "part_solleciti")
@NamedQueries({
		@NamedQuery(name = "Solleciti.deleteOrphan", query = "delete from Sollecito s where size(s.righeSollecito)=0"),
		@NamedQuery(name = "Solleciti.caricaSollecitiByCliente", query = "select distinct s from Sollecito s inner join s.righeSollecito righe inner join righe.rata r inner join r.areaRate a inner join a.documento d where d.entita.codice= :paramIdEntita"),
		@NamedQuery(name = "Solleciti.caricaSolleciti", query = "select distinct s from Sollecito s inner join s.righeSollecito righe inner join righe.rata r inner join r.areaRate a inner join a.documento d inner join d.entita e where s.codiceAzienda= :paramCodiceAzienda "),
		@NamedQuery(name = "Solleciti.caricaSollecitiByRata", query = "select s from Sollecito s inner join s.righeSollecito righe inner join righe.rata r where r.id= :paramIdRata") })
public class Sollecito extends EntityBase {

	private static final long serialVersionUID = -8431112909074009688L;

	/**
	 * Crea un sollecito di test.
	 * 
	 * @param templateSolleciti
	 *            template da usare per generare il sollecito
	 * @return sollecito creato
	 */
	public static Sollecito createTestSollecito(TemplateSolleciti templateSolleciti) {
		Sollecito sollecito = new Sollecito();
		sollecito.setId(-1);
		// sollecito.setCodiceAzienda(getPrincipal().getCodiceAzienda());
		sollecito.setEmail(false);
		Importo importo = new Importo("EUR", BigDecimal.ONE);
		importo.setImportoInValuta(BigDecimal.TEN);
		importo.calcolaImportoValutaAzienda(2);
		sollecito.setImporto(importo);
		sollecito.setNota("Nota sollecito di test");
		sollecito.setStampa(true);
		sollecito.setDataCreazione(Calendar.getInstance().getTime());

		sollecito.setTesto(templateSolleciti.getTesto());
		sollecito.setTestoFooter(templateSolleciti.getTestoFooter());

		// Rata
		Rata rata = new Rata();
		Cap cap = new Cap();
		cap.setDescrizione("00000");
		Localita localita = new Localita();
		localita.setDescrizione("Località test");
		LivelloAmministrativo1 livelloAmministrativo1 = new LivelloAmministrativo1();
		livelloAmministrativo1.setNome("Livello 1 test");
		LivelloAmministrativo2 livelloAmministrativo2 = new LivelloAmministrativo2();
		livelloAmministrativo2.setNome("livello 2 test");
		livelloAmministrativo2.setSigla("AA");
		LivelloAmministrativo3 livelloAmministrativo3 = new LivelloAmministrativo3();
		livelloAmministrativo3.setNome("livello 3 test");
		LivelloAmministrativo4 livelloAmministrativo4 = new LivelloAmministrativo4();
		livelloAmministrativo4.setNome("livello 4 test");

		SedeAnagrafica sedeAnagrafica = new SedeAnagrafica();
		sedeAnagrafica.setIndirizzo("Indirizzo test");
		sedeAnagrafica.getDatiGeografici().setCap(cap);
		sedeAnagrafica.getDatiGeografici().setLocalita(localita);
		sedeAnagrafica.getDatiGeografici().setLivelloAmministrativo1(livelloAmministrativo1);
		sedeAnagrafica.getDatiGeografici().setLivelloAmministrativo2(livelloAmministrativo2);
		sedeAnagrafica.getDatiGeografici().setLivelloAmministrativo3(livelloAmministrativo3);
		sedeAnagrafica.getDatiGeografici().setLivelloAmministrativo4(livelloAmministrativo4);

		AnagraficaLite anagrafica = new AnagraficaLite();
		anagrafica.setDenominazione("Cliente test");
		anagrafica.setSedeAnagrafica(sedeAnagrafica);
		EntitaLite entita = new ClienteLite();
		entita.setAnagrafica(anagrafica);
		Documento documento = new Documento();
		documento.setEntita(entita);
		documento.setDataDocumento(Calendar.getInstance().getTime());
		CodiceDocumento codiceDocumento = new CodiceDocumento();
		codiceDocumento.setCodice("1");
		documento.setCodice(codiceDocumento);
		AreaRate areaRate = new AreaRate();
		areaRate.setDocumento(documento);
		rata.setAreaRate(areaRate);

		// Riga sollecito
		RigaSollecito rigaSollecito = new RigaSollecito();
		rigaSollecito.setSollecito(sollecito);
		rigaSollecito.setRata(rata);
		rigaSollecito.setDataScadenza(Calendar.getInstance().getTime());
		rigaSollecito.setResiduo(importo.clone());
		Importo importoRata = importo.clone();
		importoRata.setImportoInValuta(new BigDecimal(308));
		importoRata.calcolaImportoValutaAzienda(2);
		rigaSollecito.setImporto(importoRata);

		Importo importoSollecito = importo.clone();

		List<RigaSollecito> righeSollecito = new ArrayList<RigaSollecito>();
		righeSollecito.add(rigaSollecito);

		for (int i = 0; i < 3; i++) {
			RigaSollecito rigaSollecitoNew = (RigaSollecito) PanjeaEJBUtil.cloneObject(rigaSollecito);
			Importo importoNew = importo.clone();
			importoNew.setImportoInValuta(BigDecimal.valueOf(Math.random()).multiply(Importo.HUNDRED));
			importoNew.calcolaImportoValutaAzienda(2);
			rigaSollecitoNew.setResiduo(importoNew);

			Importo importoRataNew = rigaSollecito.getImporto().clone();
			importoRataNew.setImportoInValuta(Importo.HUNDRED.add(BigDecimal.valueOf(Math.random())).multiply(
					new BigDecimal(1000)));
			importoRataNew.calcolaImportoValutaAzienda(2);
			rigaSollecitoNew.setImporto(importoRataNew);
			righeSollecito.add(rigaSollecitoNew);

			importoSollecito = importoSollecito.add(importoNew, 2);
		}
		sollecito.setImporto(importoSollecito);

		for (RigaSollecito rigaSollecito2 : righeSollecito) {
			rigaSollecito2.setSollecito(sollecito);
		}

		sollecito.setRigheSollecito(righeSollecito);

		return sollecito;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "sollecito")
	@Fetch(FetchMode.JOIN)
	private List<RigaSollecito> righeSollecito;

	@Column(length = 120)
	private String nota;

	@Embedded
	private Importo importo;

	@Lob
	private String testo;

	@Lob
	private String testoFooter;

	@Column(length = 120)
	private String oggettoMail;

	@Lob
	private String testoMail;

	@ManyToOne
	private TemplateSolleciti template;

	@Index(name = "azienda")
	private java.lang.String codiceAzienda;

	@Temporal(TemporalType.DATE)
	private java.util.Date dataCreazione;

	private boolean stampa;

	private boolean email;

	private boolean telefono;

	private boolean fax;

	/**
	 * costrutore.
	 */
	public Sollecito() {

		this.stampa = false;
		this.email = false;
		this.telefono = false;
		this.fax = false;
	}

	/**
	 * 
	 * @return cliente legato al sollecito
	 */
	public ClienteLite getCliente() {

		if (righeSollecito != null && !righeSollecito.isEmpty()) {
			// carico la rata
			Rata rata = getRigheSollecito().iterator().next().getRata();
			// carico la anagrafica
			return (ClienteLite) rata.getAreaRate().getDocumento().getEntita();
		}

		return null;
	}

	/**
	 * @return the codiceAzienda
	 */
	public java.lang.String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the dataCreazione
	 */
	public java.util.Date getDataCreazione() {
		return dataCreazione;
	}

	/**
	 * @return the importo
	 */
	public Importo getImporto() {
		return importo;
	}

	/**
	 * @return the nota
	 */
	public String getNota() {
		return nota;
	}

	/**
	 * @return the oggettoMail
	 */
	public String getOggettoMail() {
		return oggettoMail;
	}

	/**
	 * @return the righeSollecito
	 */
	public List<RigaSollecito> getRigheSollecito() {
		return righeSollecito;
	}

	/**
	 * @return the template
	 */
	public TemplateSolleciti getTemplate() {
		return template;
	}

	/**
	 * @return the testo
	 */
	public String getTesto() {
		return testo;
	}

	/**
	 * @return the testoFooter
	 */
	public String getTestoFooter() {
		return testoFooter;
	}

	/**
	 * @return the testoMail
	 */
	public String getTestoMail() {
		return testoMail;
	}

	/**
	 * @return <code>true</code> se l'entità legata alle rate ha un'indirizzo mail configurato
	 */
	public boolean hasEmail() {
		// carico la rata
		Rata rata = getRigheSollecito().iterator().next().getRata();
		// carico la anagrafica
		AnagraficaLite anagrafica = rata.getAreaRate().getDocumento().getEntita().getAnagrafica();
		// controllo se il cliente ha settato l'indirizo mail
		if (anagrafica.getSedeAnagrafica().getIndirizzoMail() != null
				&& !(anagrafica.getSedeAnagrafica().getIndirizzoMail()).isEmpty()) {
			return true;
		}

		return false;
	}

	/**
	 * @return the email
	 */
	public boolean isEmail() {
		return email;
	}

	/**
	 * @return the fax
	 */
	public boolean isFax() {
		return fax;
	}

	/**
	 * @return the stampa
	 */
	public boolean isStampa() {
		return stampa;
	}

	/**
	 * @return the telefono
	 */
	public boolean isTelefono() {
		return telefono;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(java.lang.String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param dataCreazione
	 *            the dataCreazione to set
	 */
	public void setDataCreazione(java.util.Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(boolean email) {
		this.email = email;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(boolean fax) {
		this.fax = fax;
	}

	/**
	 * @param importo
	 *            the importo to set
	 */
	public void setImporto(Importo importo) {
		this.importo = importo;
	}

	/**
	 * @param nota
	 *            the nota to set
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @param oggettoMail
	 *            the oggettoMail to set
	 */
	public void setOggettoMail(String oggettoMail) {
		this.oggettoMail = oggettoMail;
	}

	/**
	 * @param righeSollecito
	 *            the righeSollecito to set
	 */
	public void setRigheSollecito(List<RigaSollecito> righeSollecito) {
		this.righeSollecito = righeSollecito;
	}

	/**
	 * @param stampa
	 *            the stampa to set
	 */
	public void setStampa(boolean stampa) {
		this.stampa = stampa;
	}

	/**
	 * @param telefono
	 *            the telefono to set
	 */
	public void setTelefono(boolean telefono) {
		this.telefono = telefono;
	}

	/**
	 * @param template
	 *            the template to set
	 */
	public void setTemplate(TemplateSolleciti template) {
		this.template = template;
	}

	/**
	 * @param testo
	 *            the testo to set
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}

	/**
	 * @param testoFooter
	 *            the testoFooter to set
	 */
	public void setTestoFooter(String testoFooter) {
		this.testoFooter = testoFooter;
	}

	/**
	 * @param testoMail
	 *            the testoMail to set
	 */
	public void setTestoMail(String testoMail) {
		this.testoMail = testoMail;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Soleciti[");
		buffer.append(" codice = ").append(getId());
		buffer.append("]");
		return buffer.toString();
	}
}
