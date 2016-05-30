package it.eurotn.panjea.contabilita.service;

import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaAnagraficaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.service.exception.TipoContoBaseEsistenteException;
import it.eurotn.panjea.contabilita.service.interfaces.ContabilitaAnagraficaService;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;

@Stateless(name = "Panjea.ContabilitaAnagraficaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ContabilitaAnagraficaService")
public class ContabilitaAnagraficaServiceBean implements ContabilitaAnagraficaService {

    @EJB
    private ContabilitaAnagraficaManager contabilitaAnagraficaManager;

    @EJB
    private TipiAreaContabileManager tipiAreaContabileManager;

    @EJB
    private PianoContiManager pianoContiManager;

    @EJB
    private StrutturaContabileManager strutturaContabileManager;

    @EJB
    private ContabilitaSettingsManager contabilitaSettingsManager;

    @Override
    public void cancellaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo) {
        contabilitaSettingsManager.cancellaCodiceIvaCorrispettivo(codiceIvaCorrispettivo);
    }

    @Override
    public void cancellaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente) {
        tipiAreaContabileManager.cancellaCodiceIvaPrevalente(codiceIvaPrevalente);
    }

    @Override
    public void cancellaConto(Conto conto) {
        pianoContiManager.cancellaConto(conto);
    }

    @Override
    public void cancellaContoBase(ContoBase contoBase) {
        pianoContiManager.cancellaContoBase(contoBase);
    }

    @Override
    public void cancellaControPartita(ControPartita controPartita) {
        strutturaContabileManager.cancellaControPartita(controPartita);
    }

    @Override
    public void cancellaMastro(Mastro mastro) {
        pianoContiManager.cancellaMastro(mastro);
    }

    @Override
    public void cancellaRegistroIva(RegistroIva registroIva) {
        contabilitaAnagraficaManager.cancellaRegistroIva(registroIva);
    }

    @Override
    public void cancellaSottoConto(SottoConto sottoConto) {
        pianoContiManager.cancellaSottoConto(sottoConto);
    }

    @Override
    public void cancellaStrutturaContabile(StrutturaContabile strutturaContabile) throws ContabilitaException {
        strutturaContabileManager.cancellaStrutturaContabile(strutturaContabile);
    }

    @Override
    public void cancellaTipoAreaContabile(TipoAreaContabile tipoAreaContabile) {
        tipiAreaContabileManager.cancellaTipoAreaContabile(tipoAreaContabile);
    }

    @Override
    public void cancellaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase) {
        tipiAreaContabileManager.cancellaTipoDocumentoBase(tipoDocumentoBase);
    }

    @Override
    public CodiceIvaPrevalente caricaCodiceIvaPrevalente(TipoAreaContabile tipoAreaContabile, EntitaLite entita) {
        return tipiAreaContabileManager.caricaCodiceIvaPrevalente(tipoAreaContabile, entita);
    }

    @Override
    public List<CodiceIvaCorrispettivo> caricaCodiciIvaCorrispettivo(TipoDocumento tipoDocumento) {
        return contabilitaSettingsManager.caricaCodiciIvaCorrispettivo(tipoDocumento);
    }

    @Override
    public ContabilitaSettings caricaContabilitaSettings() {
        return contabilitaSettingsManager.caricaContabilitaSettings();
    }

    @Override
    public List<ContoBase> caricaContiBase() throws ContabilitaException {
        return pianoContiManager.caricaContiBase();
    }

    @Override
    public Conto caricaConto(Integer idConto) throws ContabilitaException {
        return pianoContiManager.caricaConto(idConto);
    }

    @Override
    public ControPartita caricaControPartita(ControPartita controPartita) throws ContabilitaException {
        return strutturaContabileManager.caricaControPartita(controPartita);
    }

    @Override
    public List<ControPartita> caricaControPartite(AreaContabile areaContabile) throws ContabilitaException {
        return strutturaContabileManager.caricaControPartite(areaContabile);
    }

    @Override
    public List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento, EntitaLite entita)
            throws ContabilitaException {
        return strutturaContabileManager.caricaControPartite(tipoDocumento, entita);
    }

    @Override
    public List<ControPartita> caricaControPartiteConImporto(AreaContabile areaContabile)
            throws ContabilitaException, FormulaException {
        return strutturaContabileManager.caricaControPartiteConImporto(areaContabile);
    }

    @Override
    public List<EntitaLite> caricaEntitaConStrutturaContabile(TipoDocumento tipoDocumento) {
        return strutturaContabileManager.caricaEntitaConStrutturaContabile(tipoDocumento);
    }

    @Override
    public List<Mastro> caricaMastri() throws ContabilitaException {
        return pianoContiManager.caricaMastri();
    }

    @Override
    public Mastro caricaMastro(Integer idMastro) throws ContabilitaException {
        return pianoContiManager.caricaMastro(idMastro);
    }

    @Override
    public List<RegistroIva> caricaRegistriIva(String fieldSearch, String valueSearch) throws ContabilitaException {
        return contabilitaAnagraficaManager.caricaRegistriIva(fieldSearch, valueSearch);
    }

    @Override
    public RegistroIva caricaRegistroIva(Integer id) throws ContabilitaException {
        return contabilitaAnagraficaManager.caricaRegistroIva(id);
    }

    @Override
    public SottoConto caricaSottoConto(Integer idSottoConto) throws ContabilitaException {
        return pianoContiManager.caricaSottoConto(idSottoConto);
    }

    @Override
    public SottoConto caricaSottoContoPerEntita(SottotipoConto sottotipoConto, Integer codiceEntita)
            throws ContabilitaException {
        return pianoContiManager.caricaSottoContoPerEntita(sottotipoConto, codiceEntita);
    }

    @Override
    public List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento, EntitaLite entita)
            throws ContabilitaException {
        return strutturaContabileManager.caricaStrutturaContabile(tipoDocumento, entita, false);
    }

    @Override
    public List<TipoAreaContabile> caricaTipiAreaContabile(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentiDisabilitati) throws ContabilitaException {
        return tipiAreaContabileManager.caricaTipiAreaContabile(fieldSearch, valueSearch,
                loadTipiDocumentiDisabilitati);
    }

    @Override
    public ContiBase caricaTipiContoBase() throws ContabilitaException {
        return pianoContiManager.caricaTipiContoBase();
    }

    @Override
    public List<TipoDocumento> caricaTipiDocumentiContabili() throws ContabilitaException {
        return tipiAreaContabileManager.caricaTipiDocumentiContabili();
    }

    @Override
    public List<TipoDocumentoBase> caricaTipiDocumentoBase() throws ContabilitaException {
        return tipiAreaContabileManager.caricaTipiDocumentoBase();
    }

    @Override
    public Map<TipoOperazioneTipoDocumento, TipoAreaContabile> caricaTipiOperazione() throws ContabilitaException {
        return tipiAreaContabileManager.caricaTipiOperazione();
    }

    @Override
    public TipoAreaContabile caricaTipoAreaContabile(Integer id) throws ContabilitaException {
        return tipiAreaContabileManager.caricaTipoAreaContabile(id);
    }

    @Override
    public TipoAreaContabile caricaTipoAreaContabilePerTipoDocumento(Integer idTipoDocumento)
            throws ContabilitaException {
        return tipiAreaContabileManager.caricaTipoAreaContabilePerTipoDocumento(idTipoDocumento);
    }

    @Override
    public TipoAreaContabile caricaTipoAreaContabilePerTipoOperazione(TipoOperazioneTipoDocumento tipoOperazione)
            throws TipoDocumentoBaseException, ContabilitaException {
        return tipiAreaContabileManager.caricaTipoAreaContabilePerTipoOperazione(tipoOperazione);
    }

    @Override
    public TipoDocumentoBase caricaTipoDocumentoBase(Integer idTipoDocumentoBase) throws ContabilitaException {
        return tipiAreaContabileManager.caricaTipoDocumentoBase(idTipoDocumentoBase);
    }

    @Override
    public SottoConto creaSottoContoPerEntita(Entita entita) throws ContabilitaException {
        return pianoContiManager.creaSottoContoPerEntita(entita);
    }

    @Override
    public List<Conto> ricercaConti() {
        return pianoContiManager.ricercaConti();
    }

    @Override
    public List<Conto> ricercaConti(String codiceConto, SottotipoConto sottoTipoConto) throws ContabilitaException {
        return pianoContiManager.ricercaConti(codiceConto, sottoTipoConto);
    }

    @Override
    public List<SottoConto> ricercaSottoConti() throws ContabilitaException {
        return pianoContiManager.ricercaSottoConti();
    }

    @Override
    public List<SottoConto> ricercaSottoConti(ParametriRicercaSottoConti parametriRicercaSottoConti)
            throws ContabilitaException {
        return pianoContiManager.ricercaSottoConti(parametriRicercaSottoConti);
    }

    @Override
    public List<SottoConto> ricercaSottoConti(String codiceSottoConto) throws ContabilitaException {
        return pianoContiManager.ricercaSottoConti(codiceSottoConto);
    }

    @Override
    public List<SottoConto> ricercaSottoContiOrdinati() throws ContabilitaException {
        return pianoContiManager.ricercaSottoContiOrdinati();
    }

    @Override
    public List<SottoConto> ricercaSottoContiSearchObject(ParametriRicercaSottoConti parametriRicercaSottoConti) {
        return pianoContiManager.ricercaSottoContiSearchObject(parametriRicercaSottoConti);
    }

    @Override
    public CodiceIvaCorrispettivo salvaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo) {
        return contabilitaSettingsManager.salvaCodiceIvaCorrispettivo(codiceIvaCorrispettivo);
    }

    @Override
    public CodiceIvaPrevalente salvaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente) {
        return tipiAreaContabileManager.salvaCodiceIvaPrevalente(codiceIvaPrevalente);
    }

    @Override
    public ContabilitaSettings salvaContabilitaSettings(ContabilitaSettings contabilitaSettings) {
        return contabilitaSettingsManager.salvaContabilitaSettings(contabilitaSettings);
    }

    @Override
    public Conto salvaConto(Conto conto) {
        return pianoContiManager.salvaConto(conto);
    }

    @Override
    public ContoBase salvaContoBase(ContoBase contoBase) throws TipoContoBaseEsistenteException, ContabilitaException {
        return pianoContiManager.salvaContoBase(contoBase);
    }

    @Override
    public ControPartita salvaControPartita(ControPartita controPartita) throws FormulaException {
        return strutturaContabileManager.salvaControPartita(controPartita);
    }

    @Override
    public Mastro salvaMastro(Mastro mastro) {
        return pianoContiManager.salvaMastro(mastro);
    }

    @Override
    public RegistroIva salvaRegistroIva(RegistroIva registroIva) {
        return contabilitaAnagraficaManager.salvaRegistroIva(registroIva);
    }

    @Override
    public SottoConto salvaSottoConto(SottoConto sottoConto) {
        return pianoContiManager.salvaSottoConto(sottoConto);
    }

    @Override
    public StrutturaContabile salvaStrutturaContabile(StrutturaContabile strutturaContabile) throws FormulaException {
        return strutturaContabileManager.salvaStrutturaContabile(strutturaContabile);
    }

    @Override
    public TipoAreaContabile salvaTipoAreaContabile(TipoAreaContabile tipoAreaContabile)
            throws ModificaTipoAreaConDocumentoException {
        return tipiAreaContabileManager.salvaTipoAreaContabile(tipoAreaContabile);
    }

    @Override
    public TipoDocumentoBase salvaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase) {
        return tipiAreaContabileManager.salvaTipoDocumentoBase(tipoDocumentoBase);
    }
}
