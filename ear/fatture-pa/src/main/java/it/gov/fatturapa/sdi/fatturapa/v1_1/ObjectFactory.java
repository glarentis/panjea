package it.gov.fatturapa.sdi.fatturapa.v1_1;

import it.gov.fatturapa.sdi.fatturapa.v1.AllegatiType;
import it.gov.fatturapa.sdi.fatturapa.v1.AltriDatiGestionaliType;
import it.gov.fatturapa.sdi.fatturapa.v1.AnagraficaType;
import it.gov.fatturapa.sdi.fatturapa.v1.CessionarioCommittenteType;
import it.gov.fatturapa.sdi.fatturapa.v1.CodiceArticoloType;
import it.gov.fatturapa.sdi.fatturapa.v1.ContattiTrasmittenteType;
import it.gov.fatturapa.sdi.fatturapa.v1.ContattiType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiCessionarioType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiRappresentanteType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiTerzoIntermediarioType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiVettoreType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiCassaPrevidenzialeType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDDTType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDocumentiCorrelatiType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiRitenutaType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiSALType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasportoType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiVeicoliType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaPrincipaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.IdFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.IndirizzoType;
import it.gov.fatturapa.sdi.fatturapa.v1.IscrizioneREAType;
import it.gov.fatturapa.sdi.fatturapa.v1.RappresentanteFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.ScontoMaggiorazioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.TerzoIntermediarioSoggettoEmittenteType;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * it.gov.fatturapa.sdi.fatturapa.v1 package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

	private static final QName FATTURAELETTRONICA_QNAME = new QName("http://www.fatturapa.gov.it/sdi/fatturapa/v1.1",
			"FatturaElettronica");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
	 * it.gov.fatturapa.sdi.fatturapa.v1.
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link AllegatiType }.
	 *
	 * @return allegati
	 *
	 */
	public AllegatiType createAllegatiType() {
		return new AllegatiType();
	}

	/**
	 * Create an instance of {@link AltriDatiGestionaliType }.
	 *
	 * @return altri dati
	 */
	public AltriDatiGestionaliType createAltriDatiGestionaliType() {
		return new AltriDatiGestionaliType();
	}

	/**
	 * Create an instance of {@link AnagraficaType }.
	 *
	 * @return anagrafica
	 *
	 */
	public AnagraficaType createAnagraficaType() {
		return new AnagraficaType();
	}

	/**
	 * Create an instance of {@link CedentePrestatoreType }.
	 *
	 * @return cedente
	 *
	 */
	public CedentePrestatoreType createCedentePrestatoreType() {
		return new CedentePrestatoreType();
	}

	/**
	 * Create an instance of {@link CessionarioCommittenteType }.
	 *
	 * @return {@link CessionarioCommittenteType}
	 *
	 */
	public CessionarioCommittenteType createCessionarioCommittenteType() {
		return new CessionarioCommittenteType();
	}

	/**
	 * Create an instance of {@link CodiceArticoloType }.
	 *
	 * @return {@link CodiceArticoloType}
	 *
	 */
	public CodiceArticoloType createCodiceArticoloType() {
		return new CodiceArticoloType();
	}

	/**
	 * Create an instance of {@link ContattiTrasmittenteType }.
	 *
	 * @return {@link ContattiTrasmittenteType}
	 *
	 */
	public ContattiTrasmittenteType createContattiTrasmittenteType() {
		return new ContattiTrasmittenteType();
	}

	/**
	 * Create an instance of {@link ContattiType }.
	 *
	 * @return {@link ContattiType}
	 *
	 */
	public ContattiType createContattiType() {
		return new ContattiType();
	}

	/**
	 * Create an instance of {@link DatiAnagraficiCedenteType }.
	 *
	 * @return {@link DatiAnagraficiCedenteType}
	 *
	 */
	public DatiAnagraficiCedenteType createDatiAnagraficiCedenteType() {
		return new DatiAnagraficiCedenteType();
	}

	/**
	 * Create an instance of {@link DatiAnagraficiCessionarioType }.
	 *
	 * @return {@link DatiAnagraficiCessionarioType}
	 *
	 */
	public DatiAnagraficiCessionarioType createDatiAnagraficiCessionarioType() {
		return new DatiAnagraficiCessionarioType();
	}

	/**
	 * Create an instance of {@link DatiAnagraficiRappresentanteType }.
	 *
	 * @return {@link DatiAnagraficiRappresentanteType}
	 *
	 */
	public DatiAnagraficiRappresentanteType createDatiAnagraficiRappresentanteType() {
		return new DatiAnagraficiRappresentanteType();
	}

	/**
	 * Create an instance of {@link DatiAnagraficiTerzoIntermediarioType }.
	 *
	 * @return {@link DatiAnagraficiTerzoIntermediarioType}
	 *
	 */
	public DatiAnagraficiTerzoIntermediarioType createDatiAnagraficiTerzoIntermediarioType() {
		return new DatiAnagraficiTerzoIntermediarioType();
	}

	/**
	 * Create an instance of {@link DatiAnagraficiVettoreType }.
	 *
	 * @return {@link DatiAnagraficiVettoreType}
	 *
	 */
	public DatiAnagraficiVettoreType createDatiAnagraficiVettoreType() {
		return new DatiAnagraficiVettoreType();
	}

	/**
	 * Create an instance of {@link DatiBeniServiziType }.
	 *
	 * @return {@link DatiBeniServiziType}
	 *
	 */
	public DatiBeniServiziType createDatiBeniServiziType() {
		return new DatiBeniServiziType();
	}

	/**
	 * Create an instance of {@link DatiBolloType }.
	 *
	 * @return {@link DatiBolloType}
	 *
	 */
	public DatiBolloType createDatiBolloType() {
		return new DatiBolloType();
	}

	/**
	 * Create an instance of {@link DatiCassaPrevidenzialeType }.
	 *
	 * @return {@link DatiCassaPrevidenzialeType}
	 *
	 */
	public DatiCassaPrevidenzialeType createDatiCassaPrevidenzialeType() {
		return new DatiCassaPrevidenzialeType();
	}

	/**
	 * Create an instance of {@link DatiDDTType }.
	 *
	 * @return {@link DatiDDTType}
	 *
	 */
	public DatiDDTType createDatiDDTType() {
		return new DatiDDTType();
	}

	/**
	 * Create an instance of {@link DatiDocumentiCorrelatiType }.
	 *
	 * @return {@link DatiDocumentiCorrelatiType}
	 *
	 */
	public DatiDocumentiCorrelatiType createDatiDocumentiCorrelatiType() {
		return new DatiDocumentiCorrelatiType();
	}

	/**
	 * Create an instance of {@link DatiGeneraliDocumentoType }.
	 *
	 * @return {@link DatiGeneraliDocumentoType}
	 *
	 */
	public DatiGeneraliDocumentoType createDatiGeneraliDocumentoType() {
		return new DatiGeneraliDocumentoType();
	}

	/**
	 * Create an instance of {@link DatiGeneraliType }.
	 *
	 * @return {@link DatiGeneraliType}
	 *
	 */
	public DatiGeneraliType createDatiGeneraliType() {
		return new DatiGeneraliType();
	}

	/**
	 * Create an instance of {@link DatiPagamentoType }.
	 *
	 * @return {@link DatiPagamentoType}
	 *
	 */
	public DatiPagamentoType createDatiPagamentoType() {
		return new DatiPagamentoType();
	}

	/**
	 * Create an instance of {@link DatiRiepilogoType }.
	 *
	 * @return {@link DatiRiepilogoType}
	 *
	 */
	public DatiRiepilogoType createDatiRiepilogoType() {
		return new DatiRiepilogoType();
	}

	/**
	 * Create an instance of {@link DatiRitenutaType }.
	 *
	 * @return {@link DatiRitenutaType}
	 *
	 */
	public DatiRitenutaType createDatiRitenutaType() {
		return new DatiRitenutaType();
	}

	/**
	 * Create an instance of {@link DatiSALType }.
	 *
	 * @return {@link DatiSALType}
	 *
	 */
	public DatiSALType createDatiSALType() {
		return new DatiSALType();
	}

	/**
	 * Create an instance of {@link DatiTrasmissioneType }.
	 *
	 * @return {@link DatiTrasmissioneType}
	 *
	 */
	public DatiTrasmissioneType createDatiTrasmissioneType() {
		return new DatiTrasmissioneType();
	}

	/**
	 * Create an instance of {@link DatiTrasportoType }.
	 *
	 * @return {@link DatiTrasmissioneType}
	 *
	 */
	public DatiTrasportoType createDatiTrasportoType() {
		return new DatiTrasportoType();
	}

	/**
	 * Create an instance of {@link DatiVeicoliType }.
	 *
	 * @return {@link DatiVeicoliType}
	 *
	 */
	public DatiVeicoliType createDatiVeicoliType() {
		return new DatiVeicoliType();
	}

	/**
	 * Create an instance of {@link DettaglioLineeType }.
	 *
	 * @return {@link DettaglioLineeType}
	 *
	 */
	public DettaglioLineeType createDettaglioLineeType() {
		return new DettaglioLineeType();
	}

	/**
	 * Create an instance of {@link DettaglioPagamentoType }.
	 *
	 * @return {@link DettaglioPagamentoType}
	 *
	 */
	public DettaglioPagamentoType createDettaglioPagamentoType() {
		return new DettaglioPagamentoType();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link FatturaElettronicaType }{@code >}.
	 *
	 * @param value
	 *            fattura elettronica type
	 * @return JAXBElement della fattura elettronica type
	 *
	 */
	@XmlElementDecl(namespace = "http://www.fatturapa.gov.it/sdi/fatturapa/v1.1", name = "FatturaElettronica")
	public JAXBElement<FatturaElettronicaType> createFatturaElettronica(FatturaElettronicaType value) {
		return new JAXBElement<FatturaElettronicaType>(FATTURAELETTRONICA_QNAME, FatturaElettronicaType.class, null,
				value);
	}

	/**
	 * Create an instance of {@link FatturaElettronicaBodyType }.
	 *
	 * @return {@link FatturaElettronicaBodyType}
	 *
	 */
	public FatturaElettronicaBodyType createFatturaElettronicaBodyType() {
		return new FatturaElettronicaBodyType();
	}

	/**
	 * Create an instance of {@link FatturaElettronicaHeaderType }.
	 *
	 * @return {@link FatturaElettronicaHeaderType}
	 *
	 */
	public FatturaElettronicaHeaderType createFatturaElettronicaHeaderType() {
		return new FatturaElettronicaHeaderType();
	}

	/**
	 * Create an instance of {@link FatturaElettronicaType }.
	 *
	 * @return {@link FatturaElettronicaType}
	 *
	 */
	public FatturaElettronicaType createFatturaElettronicaType() {
		return new FatturaElettronicaType();
	}

	/**
	 * Create an instance of {@link FatturaPrincipaleType }.
	 *
	 * @return {@link FatturaPrincipaleType}
	 *
	 */
	public FatturaPrincipaleType createFatturaPrincipaleType() {
		return new FatturaPrincipaleType();
	}

	/**
	 * Create an instance of {@link IdFiscaleType }.
	 *
	 * @return {@link IdFiscaleType}
	 *
	 */
	public IdFiscaleType createIdFiscaleType() {
		return new IdFiscaleType();
	}

	/**
	 * Create an instance of {@link IndirizzoType }.
	 *
	 * @return {@link IndirizzoType}
	 *
	 */
	public IndirizzoType createIndirizzoType() {
		return new IndirizzoType();
	}

	/**
	 * Create an instance of {@link IscrizioneREAType }.
	 *
	 * @return {@link IscrizioneREAType}
	 *
	 */
	public IscrizioneREAType createIscrizioneREAType() {
		return new IscrizioneREAType();
	}

	/**
	 * Create an instance of {@link RappresentanteFiscaleType }.
	 *
	 * @return {@link RappresentanteFiscaleType}
	 *
	 */
	public RappresentanteFiscaleType createRappresentanteFiscaleType() {
		return new RappresentanteFiscaleType();
	}

	/**
	 * Create an instance of {@link ScontoMaggiorazioneType }.
	 *
	 * @return {@link ScontoMaggiorazioneType}
	 *
	 */
	public ScontoMaggiorazioneType createScontoMaggiorazioneType() {
		return new ScontoMaggiorazioneType();
	}

	/**
	 * Create an instance of {@link TerzoIntermediarioSoggettoEmittenteType }.
	 *
	 * @return {@link TerzoIntermediarioSoggettoEmittenteType}
	 *
	 */
	public TerzoIntermediarioSoggettoEmittenteType createTerzoIntermediarioSoggettoEmittenteType() {
		return new TerzoIntermediarioSoggettoEmittenteType();
	}

}
