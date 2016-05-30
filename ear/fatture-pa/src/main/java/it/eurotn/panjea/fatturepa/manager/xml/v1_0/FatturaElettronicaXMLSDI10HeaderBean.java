package it.eurotn.panjea.fatturepa.manager.xml.v1_0;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.DatiIscrizioneRea;
import it.eurotn.panjea.fatturepa.domain.Trasmissione;
import it.eurotn.panjea.fatturepa.manager.xml.interfaces.FatturaElettronicaXMLHeaderManager;
import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaHeaderType;
import it.gov.fatturapa.sdi.fatturapa.v1.AnagraficaType;
import it.gov.fatturapa.sdi.fatturapa.v1.CedentePrestatoreType;
import it.gov.fatturapa.sdi.fatturapa.v1.CessionarioCommittenteType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiCedenteType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiCessionarioType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiAnagraficiRappresentanteType;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaHeaderType;
import it.gov.fatturapa.sdi.fatturapa.v1.IdFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.IndirizzoType;
import it.gov.fatturapa.sdi.fatturapa.v1.IscrizioneREAType;
import it.gov.fatturapa.sdi.fatturapa.v1.RappresentanteFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.RegimeFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1.SocioUnicoType;
import it.gov.fatturapa.sdi.fatturapa.v1.StatoLiquidazioneType;

@Stateless(mappedName = "FatturaElettronicaXMLSDI10HeaderManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "FatturaElettronicaXMLSDI10HeaderManager")
public class FatturaElettronicaXMLSDI10HeaderBean implements FatturaElettronicaXMLHeaderManager {

    private static final Logger LOGGER = Logger.getLogger(FatturaElettronicaXMLSDI10HeaderBean.class);

    @EJB
    protected PanjeaDAO panjeaDAO;

    @EJB
    protected EntitaManager entitaManager;

    private CedentePrestatoreType getCedentePrestatore(AziendaFatturaPA aziendaPA) {
        CedentePrestatoreType cedente = new CedentePrestatoreType();
        // Dati obbligatori
        cedente.setDatiAnagrafici(getDatiAnagraficiCedente(aziendaPA));
        cedente.setSede(getIndirizzoCedente(aziendaPA));

        // I campi indicati di seguito devono essere obbligatoriamente valorizzati nei soli casi
        // in cui il cedente/prestatore è un soggetto non residente ed effettua la transazione
        // oggetto del documento tramite l’organizzazione residente sul territorio nazionale. Si
        // riferiscono alla stabile organizzazione in Italia.
        // cedente --> StabileOrganizzazione

        // Questi campi devono essere obbligatoriamente valorizzati nei casi di società
        // soggette al vincolo dell’iscrizione nel registro delle imprese ai sensi dell'art. 2250
        // del codice civile.
        if (aziendaPA.getDatiIscrizioneRea().isEnable()) {
            cedente.setIscrizioneREA(getIscrizioneREA(aziendaPA.getDatiIscrizioneRea()));
        }

        // Dati non obbligatori
        // cedente --> RiferimentoAmministrazione

        return cedente;
    }

    protected CessionarioCommittenteType getCessionarioCommittente(Entita entita) {
        CessionarioCommittenteType cessionarioCommittente = new CessionarioCommittenteType();
        // Dati Obbligatori
        cessionarioCommittente.setDatiAnagrafici(getDatiAnagraficiCessionario(entita));
        cessionarioCommittente.setSede(getIndirizzoCessionarioCommittente(entita));

        return cessionarioCommittente;
    }

    private DatiAnagraficiCedenteType getDatiAnagraficiCedente(AziendaFatturaPA aziendaPA) {
        IdFiscaleType fiscaleType = new IdFiscaleType();
        fiscaleType.setIdCodice(aziendaPA.getCodiceIdentificativoFiscale());
        fiscaleType.setIdPaese(aziendaPA.getSedeNazione());

        AnagraficaType anagrafica = new AnagraficaType();
        // Dati obbligatori
        anagrafica.setCognome(aziendaPA.getLegaleRappresentanteCognome());
        anagrafica.setNome(aziendaPA.getLegaleRappresentanteNome());
        anagrafica.setDenominazione(aziendaPA.getLegaleRappresentanteDenominazione());
        // dati non obbligatori
        anagrafica.setCodEORI(aziendaPA.getLegaleRappresentanteCodiceEori());
        anagrafica.setTitolo(aziendaPA.getLegaleRappresentanteTitolo());

        DatiAnagraficiCedenteType datiAnagCedente = new DatiAnagraficiCedenteType();
        // Dati Obbligatori
        datiAnagCedente.setIdFiscaleIVA(fiscaleType);
        datiAnagCedente.setAnagrafica(anagrafica);
        datiAnagCedente.setRegimeFiscale(RegimeFiscaleType.fromValue(aziendaPA.getRegimeFiscale().getLabel()));

        // Dati non obbligatori
        datiAnagCedente.setAlboProfessionale(aziendaPA.getDescrizioneAlbo());
        datiAnagCedente.setCodiceFiscale(aziendaPA.getCodiceFiscale());
        datiAnagCedente
                .setDataIscrizioneAlbo(FatturazionePAUtils.getXMLGregorianCalendar(aziendaPA.getDataIscrizioneAlbo()));
        datiAnagCedente.setNumeroIscrizioneAlbo(aziendaPA.getDescrizioneNumeroIscrizioneAlbo());
        datiAnagCedente.setProvinciaAlbo(aziendaPA.getProvinciaAlbo());

        return datiAnagCedente;
    }

    private DatiAnagraficiCessionarioType getDatiAnagraficiCessionario(Entita entita) {

        IdFiscaleType fiscaleType = null;
        if (!StringUtils.isBlank(entita.getCodiceIdentificativoFiscale())) {
            fiscaleType = new IdFiscaleType();
            fiscaleType.setIdCodice(entita.getCodiceIdentificativoFiscale());
            fiscaleType.setIdPaese(entita.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getCodiceNazione());
        }

        AnagraficaType anagrafica = new AnagraficaType();
        // Dati obbligatori
        if (!StringUtils.isBlank(entita.getAnagrafica().getDenominazione())) {
            anagrafica.setDenominazione(entita.getAnagrafica().getDenominazione());
        } else {
            anagrafica.setCognome(entita.getAnagrafica().getPersonaFisica().getCognome());
            anagrafica.setNome(entita.getAnagrafica().getPersonaFisica().getNome());
        }
        // Dati non obbligatori
        anagrafica.setCodEORI(entita.getCodiceEori());

        DatiAnagraficiCessionarioType datiAnagraficiCessionario = new DatiAnagraficiCessionarioType();
        // Dati obbligatori
        datiAnagraficiCessionario.setIdFiscaleIVA(fiscaleType);
        datiAnagraficiCessionario.setCodiceFiscale(entita.getAnagrafica().getCodiceFiscale());
        datiAnagraficiCessionario.setAnagrafica(anagrafica);

        return datiAnagraficiCessionario;
    }

    private DatiAnagraficiRappresentanteType getDatiAnagraficiRappresentanteFiscale(AziendaFatturaPA aziendaPA) {

        IdFiscaleType fiscaleType = new IdFiscaleType();
        fiscaleType.setIdCodice(aziendaPA.getRappresentanteFiscale().getCodiceIdentificativoFiscale());
        fiscaleType.setIdPaese(aziendaPA.getRappresentanteFiscale().getNazione().getCodice());

        AnagraficaType anagrafica = new AnagraficaType();
        // Dati obbligatori
        if (!StringUtils.isBlank(aziendaPA.getRappresentanteFiscale().getDenominazione())) {
            anagrafica.setDenominazione(aziendaPA.getRappresentanteFiscale().getDenominazione());
        } else {
            anagrafica.setCognome(aziendaPA.getRappresentanteFiscale().getCognome());
            anagrafica.setNome(aziendaPA.getRappresentanteFiscale().getNome());
        }

        DatiAnagraficiRappresentanteType datiAnagraficiRappresentante = new DatiAnagraficiRappresentanteType();
        // Dati obbligatori
        datiAnagraficiRappresentante.setIdFiscaleIVA(fiscaleType);
        datiAnagraficiRappresentante.setAnagrafica(anagrafica);

        return datiAnagraficiRappresentante;
    }

    protected DatiTrasmissioneType getDatiTrasmissione(Trasmissione trasmissione, AziendaFatturaPA aziendaPA) {

        IdFiscaleType fiscaleType = new IdFiscaleType();
        fiscaleType.setIdCodice(aziendaPA.getCodiceFiscale());
        fiscaleType.setIdPaese(aziendaPA.getSedeNazione());

        DatiTrasmissioneType datiTrasmissione = new DatiTrasmissioneType();
        // Dati obbligatori
        datiTrasmissione.setIdTrasmittente(fiscaleType);
        datiTrasmissione.setProgressivoInvio(trasmissione.getProgressivoInvio());
        datiTrasmissione.setFormatoTrasmissione(getFormatoTrasmissione());
        datiTrasmissione.setCodiceDestinatario(trasmissione.getCodiceDestinatario());

        return datiTrasmissione;
    }

    @Override
    public FormatoTrasmissioneType getFormatoTrasmissione() {
        return FormatoTrasmissioneType.SDI_10;
    }

    /**
     * @param trasmissione
     *            trasmissione
     * @param aziendaPA
     *            azienda PA
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @return header creato
     */
    @Override
    public IFatturaElettronicaHeaderType getHeader(Trasmissione trasmissione, AziendaFatturaPA aziendaPA,
            AreaMagazzino areaMagazzino) {

        Entita entita = null;
        try {
            entita = entitaManager.caricaEntita(areaMagazzino.getDocumento().getEntita(), false);
            ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(areaMagazzino);
            ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(entita);
        } catch (AnagraficaServiceException e) {
            LOGGER.error("--> errore durante il caricamento dell'entità del documento", e);
            throw new RuntimeException("errore durante il caricamento dell'entità del documento", e);
        }

        FatturaElettronicaHeaderType header = new FatturaElettronicaHeaderType();
        // Dati obbligatori
        header.setDatiTrasmissione(getDatiTrasmissione(trasmissione, aziendaPA));
        header.setCedentePrestatore(getCedentePrestatore(aziendaPA));
        header.setCessionarioCommittente(getCessionarioCommittente(entita));

        // Dati obbligatori, qualora il cedente/prestatore si avvalga di un rappresentante
        // fiscale in Italia, ai sensi del DPR 633 del 1972 e successive modifiche ed
        // integrazioni.
        if (aziendaPA.getRappresentanteFiscale().isEnable()) {
            header.setRappresentanteFiscale(getRappresentanteFiscale(aziendaPA));
        }

        // Nei casi di documenti emessi da un soggetto diverso dal cedente/prestatore va valorizzato il campo seguente.
        // header --> SoggettoEmittente

        return header;
    }

    protected IndirizzoType getIndirizzoCedente(AziendaFatturaPA aziendaPA) {
        IndirizzoType indirizzo = new IndirizzoType();
        // DAti obbligatori
        indirizzo.setIndirizzo(aziendaPA.getSedeIndirizzo());
        indirizzo.setNumeroCivico(aziendaPA.getSedeNumeroCivico());
        indirizzo.setCap(aziendaPA.getSedeCAP());
        indirizzo.setComune(aziendaPA.getSedeComune());
        indirizzo.setNazione(aziendaPA.getSedeNazione());
        // Dati non obbligatori
        indirizzo.setProvincia(aziendaPA.getSedeProvincia());

        return indirizzo;
    }

    private IndirizzoType getIndirizzoCessionarioCommittente(Entita entita) {

        SedeAnagrafica sede = entita.getAnagrafica().getSedeAnagrafica();

        IndirizzoType indirizzo = new IndirizzoType();
        // DAti obbligatori
        indirizzo.setIndirizzo(sede.getIndirizzo());
        indirizzo.setNumeroCivico(sede.getNumeroCivico());
        indirizzo.setCap(sede.getDatiGeografici().getCap().getDescrizione());
        indirizzo.setComune(sede.getDatiGeografici().getLocalita() != null
                ? sede.getDatiGeografici().getDescrizioneLocalita() : null);
        indirizzo.setNazione(sede.getDatiGeografici().getCodiceNazione());
        // Dati non obbligatori
        indirizzo.setProvincia(sede.getDatiGeografici().hasLivelloAmministrativo2()
                ? sede.getDatiGeografici().getLivelloAmministrativo2().getSigla() : null);

        return indirizzo;
    }

    protected IscrizioneREAType getIscrizioneREA(DatiIscrizioneRea datiIscrizioneRea) {
        IscrizioneREAType iscrizioneREA = new IscrizioneREAType();
        iscrizioneREA.setCapitaleSociale(datiIscrizioneRea.getImportoCapitaleSociale());
        iscrizioneREA.setNumeroREA(datiIscrizioneRea.getNumeroRea());
        iscrizioneREA.setSocioUnico(SocioUnicoType.fromValue(datiIscrizioneRea.getTipologiaSoci().name()));
        iscrizioneREA
                .setStatoLiquidazione(StatoLiquidazioneType.fromValue(datiIscrizioneRea.getStatoLiquidazione().name()));
        iscrizioneREA.setUfficio(datiIscrizioneRea.getProvincia());

        return iscrizioneREA;
    }

    protected RappresentanteFiscaleType getRappresentanteFiscale(AziendaFatturaPA aziendaPA) {
        RappresentanteFiscaleType rappresentanteFiscale = new RappresentanteFiscaleType();
        rappresentanteFiscale.setDatiAnagrafici(getDatiAnagraficiRappresentanteFiscale(aziendaPA));

        return rappresentanteFiscale;
    }
}
