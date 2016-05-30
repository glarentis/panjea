package it.eurotn.panjea.fatturepa.service;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.domain.AreaMagazzinoFatturaPA;
import it.eurotn.panjea.fatturepa.domain.AreaNotificaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.manager.exception.XMLCreationException;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturaPASettingsManager;
import it.eurotn.panjea.fatturepa.manager.interfaces.FatturePAManager;
import it.eurotn.panjea.fatturepa.service.interfaces.FatturePAService;
import it.eurotn.panjea.fatturepa.solutiondoc.manager.interfaces.SolutionDocFatturaPAInvioManager;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.fatturepa.util.AreaNotificaFatturaPADTO;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.FatturePAService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.FatturePAService")
public class FatturePAServiceBean implements FatturePAService {

    @EJB
    private FatturePAManager fatturePAManager;

    @EJB
    private SolutionDocFatturaPAInvioManager solutionDocFatturaPAInvioManager;

    @EJB
    private FatturaPASettingsManager fatturaPASettingsManager;

    @Override
    public void cancellaXMLFatturaPA(Integer idAreaMagazzino) throws PreferenceNotFoundException {
        fatturePAManager.cancellaXMLFatturaPA(idAreaMagazzino);
    }

    @Override
    public AreaMagazzinoFatturaPA caricaAreaMagazzinoFatturaPA(Integer idAreaMagazzino) {
        return fatturePAManager.caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
    }

    @Override
    public AreaNotificaFatturaPA caricaAreaNotificaFatturaPA(Integer id) {
        return fatturePAManager.caricaAreaNotificaFatturaPA(id);
    }

    @Override
    public List<AreaNotificaFatturaPADTO> caricaAreaNotificheFatturaPA(Integer idAreaMagazzinoFatturaPA) {
        return fatturePAManager.caricaAreaNotificheFatturaPA(idAreaMagazzinoFatturaPA);
    }

    @Override
    public IFatturaElettronicaType caricaFatturaElettronicaType(String xmlContent) {
        return fatturePAManager.caricaFatturaElettronicaType(xmlContent);
    }

    @Override
    public void checkEsitiFatturePA() {

        FatturaPASettings fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();

        if (!fatturaPASettings.isGestioneFirmaElettronica() && fatturaPASettings.isAttivaConservazioneSostitutiva()) {
            solutionDocFatturaPAInvioManager.checkEsitiFatturePA();
        }
    }

    @Override
    public AreaMagazzinoFatturaPA creaXMLFattura(Integer idAreaMagazzino) throws XMLCreationException {
        return fatturePAManager.creaXMLFatturaPA(idAreaMagazzino);
    }

    @Override
    public AreaMagazzinoFatturaPA creaXMLFatturaPA(Integer idAreaMagazzino,
            IFatturaElettronicaType fatturaElettronicaType) throws XMLCreationException, PreferenceNotFoundException {
        return fatturePAManager.creaXMLFatturaPA(idAreaMagazzino, fatturaElettronicaType);
    }

    @Override
    public byte[] downloadXMLFirmato(String fileName) throws PreferenceNotFoundException {
        return fatturePAManager.downloadXMLFirmato(fileName);
    }

    @Override
    public void invioSdiFatturaPA(Integer idAreaMagazzino) {

        FatturaPASettings fatturaPASettings = fatturaPASettingsManager.caricaFatturaPASettings();

        if (!fatturaPASettings.isGestioneFirmaElettronica()) {

            String idSdi = solutionDocFatturaPAInvioManager.invioSdiFatturaPA(idAreaMagazzino);
            if (idSdi != null) {
                AreaMagazzinoFatturaPA areaMagazzinoFatturaPA = fatturePAManager
                        .caricaAreaMagazzinoFatturaPA(idAreaMagazzino);
                areaMagazzinoFatturaPA.setIdentificativoSDI(new BigInteger(idSdi));
                areaMagazzinoFatturaPA.setConservazioneSostitutivaEseguita(true);
                fatturePAManager.salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);
                fatturePAManager.salvaFatturaPAComeInviata(idAreaMagazzino);

                checkEsitiFatturePA();
            }
        } else {
            throw new GenericException("L'invio della fattura è previsto solo per l'XML firmato e via PEC");
        }
    }

    @Override
    public List<AreaMagazzinoFatturaPARicerca> ricercaFatturePA(ParametriRicercaFatturePA parametri) {
        return fatturePAManager.ricercaFatturePA(parametri);
    }

    @Override
    public AreaMagazzinoFatturaPA salvaAreaMagazzinoFatturaPA(AreaMagazzinoFatturaPA areaMagazzinoFatturaPA) {
        return fatturePAManager.salvaAreaMagazzinoFatturaPA(areaMagazzinoFatturaPA);
    }

    @Override
    public void salvaFatturaPAComeInviata(Integer idAreaMagazzino) {
        fatturePAManager.salvaFatturaPAComeInviata(idAreaMagazzino);
    }

    @Override
    public void salvaXMLFatturaFirmato(Integer idAreaMagazzino, byte[] xmlContent, String xmlFileName)
            throws PreferenceNotFoundException {
        fatturePAManager.salvaXMLFatturaFirmato(idAreaMagazzino, xmlContent, xmlFileName);
    }

}
