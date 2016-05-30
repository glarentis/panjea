package it.eurotn.panjea.fatturepa.manager.xml.v1_1;

import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.Trasmissione;
import it.eurotn.panjea.fatturepa.manager.xml.v1_0.FatturaElettronicaXMLSDI10HeaderBean;
import it.eurotn.panjea.fatturepa.util.FatturazionePAUtils;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaHeaderType;
import it.gov.fatturapa.sdi.fatturapa.v1.AnagraficaType;
import it.gov.fatturapa.sdi.fatturapa.v1.ContattiType;
import it.gov.fatturapa.sdi.fatturapa.v1.IdFiscaleType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.CedentePrestatoreType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiAnagraficiCedenteType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaHeaderType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.RegimeFiscaleType;

@Stateless(mappedName = "FatturaElettronicaXMLSDI11HeaderManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "FatturaElettronicaXMLSDI11HeaderManager")
public class FatturaElettronicaXMLSDI11HeaderBean extends FatturaElettronicaXMLSDI10HeaderBean {

    private static final Logger LOGGER = Logger.getLogger(FatturaElettronicaXMLSDI11HeaderBean.class);

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
        cedente.setContatti(getContattiCedente(aziendaPA));

        return cedente;
    }

    private ContattiType getContattiCedente(AziendaFatturaPA aziendaFatturaPA) {
        ContattiType contatti = null;

        if (!StringUtils.isBlank(aziendaFatturaPA.getTelefono()) || !StringUtils.isBlank(aziendaFatturaPA.getFax())
                || !StringUtils.isBlank(aziendaFatturaPA.getEMail())) {
            contatti = new ContattiType();
            contatti.setTelefono(aziendaFatturaPA.getTelefono());
            contatti.setFax(aziendaFatturaPA.getFax());
            contatti.setEmail(aziendaFatturaPA.getEMail());
        }

        return contatti;
    }

    private DatiAnagraficiCedenteType getDatiAnagraficiCedente(AziendaFatturaPA aziendaPA) {
        IdFiscaleType fiscaleType = new IdFiscaleType();
        fiscaleType.setIdCodice(aziendaPA.getCodiceIdentificativoFiscale());
        fiscaleType.setIdPaese(aziendaPA.getSedeNazione());

        AnagraficaType anagrafica = new AnagraficaType();
        // Dati obbligatori
        if (!StringUtils.isBlank(aziendaPA.getAzienda().getDenominazione())) {
            anagrafica.setDenominazione(aziendaPA.getAzienda().getDenominazione());
            // dati non obbligatori
            anagrafica.setCodEORI(aziendaPA.getDescrizioneCodiceEori());
            anagrafica.setTitolo(aziendaPA.getDescrizioneTitolare());
        } else {
            anagrafica.setCognome(aziendaPA.getLegaleRappresentanteCognome());
            anagrafica.setNome(aziendaPA.getLegaleRappresentanteNome());
            // dati non obbligatori
            anagrafica.setCodEORI(aziendaPA.getLegaleRappresentanteCodiceEori());
            anagrafica.setTitolo(aziendaPA.getLegaleRappresentanteTitolo());
        }

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

    @Override
    public FormatoTrasmissioneType getFormatoTrasmissione() {
        return FormatoTrasmissioneType.SDI_11;
    }

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
}
