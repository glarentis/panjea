package it.eurotn.panjea.corrispettivi.service;

import java.util.Date;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.TotaliCodiceIvaDTO;
import it.eurotn.panjea.corrispettivi.manager.calendariocorrispettivi.interfaces.CalendarioCorrispettiviManager;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.importer.interfaces.CorrispettiviImporterManager;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces.CorrispettiviAreaContabileGenerator;
import it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces.CorrispettiviManager;
import it.eurotn.panjea.corrispettivi.manager.corrispettivilinktipodocumento.interfaces.CorrispettiviLinkTipoDocumentoManager;
import it.eurotn.panjea.corrispettivi.service.interfaces.CorrispettiviService;

@Stateless(name = "Panjea.CorrispettiviService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.CorrispettiviService")
public class CorrispettiviServiceBean implements CorrispettiviService {

    @EJB
    private CorrispettiviManager corrispettiviManager;

    @EJB
    private CalendarioCorrispettiviManager calendarioCorrispettiviManager;

    @EJB
    private CorrispettiviAreaContabileGenerator corrispettiviAreaContabileGenerator;

    @EJB
    private CorrispettiviImporterManager corrispettiviImporterManager;

    @EJB
    private CorrispettiviLinkTipoDocumentoManager corrispettiviLinkTipoDocumentoManager;

    @Override
    public void cancellaCorrispettivo(Integer id) {
        corrispettiviManager.cancella(id);
    }

    @Override
    public void cancellaCorrispettivoLinkTipoDocumento(Integer id) {
        corrispettiviLinkTipoDocumentoManager.cancella(id);
    }

    @Override
    public CalendarioCorrispettivo caricaCalendarioCorrispettivo(int anno, int mese, TipoDocumento tipoDocumento) {
        calendarioCorrispettiviManager.aggiornaCodiciIvaCalendarioCorrispettivi(anno, mese, tipoDocumento);
        return calendarioCorrispettiviManager.caricaCalendarioCorrispettivo(anno, mese, tipoDocumento);
    }

    @Override
    public List<Corrispettivo> caricaCorrispettivi() {
        return corrispettiviManager.caricaAll();
    }

    @Override
    public List<CorrispettivoLinkTipoDocumento> caricaCorrispettiviLinkTipoDocumento() {
        return corrispettiviLinkTipoDocumentoManager.caricaAll();
    }

    @Override
    public Corrispettivo caricaCorrispettivoById(Integer id) {
        return corrispettiviManager.caricaById(id);
    }

    @Override
    public CorrispettivoLinkTipoDocumento caricaCorrispettivoLinkTipoDocumentoById(Integer id) {
        return corrispettiviLinkTipoDocumentoManager.caricaById(id);
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentoCorrispettivi() {
        return corrispettiviLinkTipoDocumentoManager.caricaTipiDocumentoCorrispettivi();
    }

    @Override
    public List<TotaliCodiceIvaDTO> caricaTotaliCalendarioCorrispettivi(
            CalendarioCorrispettivo calendarioCorrispettivo) {
        return calendarioCorrispettiviManager.caricaTotaliCalendarioCorrispettivi(calendarioCorrispettivo);
    }

    @Override
    public CalendarioCorrispettivo creaDocumenti(CalendarioCorrispettivo calendarioCorrispettivo) {
        corrispettiviAreaContabileGenerator.creaDocumenti(calendarioCorrispettivo);
        return calendarioCorrispettiviManager.caricaCalendarioCorrispettivo(calendarioCorrispettivo.getAnno(),
                calendarioCorrispettivo.getMese(), calendarioCorrispettivo.getTipoDocumento());
    }

    @Override
    public void importa(Date data) {
        corrispettiviImporterManager.importa(data);
    }

    @Override
    public Corrispettivo salvaCorrispettivo(Corrispettivo corrispettivo) {
        return corrispettiviManager.salva(corrispettivo);
    }

    @Override
    public CorrispettivoLinkTipoDocumento salvaCorrispettivoLinkTipoDocumento(
            CorrispettivoLinkTipoDocumento corrispettivoLinkTipoDocumento) {
        return corrispettiviLinkTipoDocumentoManager.salva(corrispettivoLinkTipoDocumento);
    }
}
