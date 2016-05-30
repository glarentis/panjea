package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.hibernate.proxy.HibernateProxy;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.importer.manager.interfaces.GenerazioneImportazioneManager;
import it.eurotn.panjea.magazzino.importer.util.RigaImport;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.domain.documento.Battute;
import it.eurotn.panjea.manutenzioni.domain.documento.DatiVendingArea;
import it.eurotn.panjea.manutenzioni.domain.documento.LettureContatore;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.InstallazioniManager;
import it.eurotn.panjea.manutenzioni.manager.operatori.interfaces.OperatoriManager;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.interfaces.AreaRifornimentoManager;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.DocumentoFatturatoNonPrevistoException;
import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.RifornimentoImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.RigaImportazioneBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.Rifornimenti;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RifornimentiProdotti;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.DocumentoImportazioneBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DocumentoImportazioneBuilder")
public class RifornimentoImportazioneBuilderBean implements RifornimentoImportazioneBuilder {

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private RigaImportazioneBuilder rigaImportazioneBuilder;

    @EJB
    private OperatoriManager operatoriManager;

    @EJB
    private AreaRifornimentoManager areaRifornimentoManager;

    @Resource
    private SessionContext sessionContext;

    @EJB
    private InstallazioniManager installazioniManager;

    @EJB
    private GenerazioneImportazioneManager generazioneImportazioneManager;

    private DocumentoVendingImport aggiungiRighe(DocumentoVendingImport doc, ImportazioneXml importazione,
            Rifornimenti rifornimento) {
        Collection<RifornimentiProdotti> righeArticolo = importazione.getRifornimentiRighe()
                .get(rifornimento.getProgressivo());
        Collection<RigaImport> righeImporta = rigaImportazioneBuilder.creaRigheImport(righeArticolo);
        for (RigaImport rigaImport : righeImporta) {
            doc.getRighe().add(rigaImport);
        }
        return doc;
    }

    private AreaMagazzino creaAreaMagazzino(DocumentoVendingImport doc) {
        SedeAreaMagazzinoDTO sedeAreaMagazzinoDTO = areaMagazzinoManager
                .caricaSedeAreaMagazzinoDTO(doc.getInstallazione().getDeposito().getSedeEntita());

        DatiGenerazione datiGenerazione = new DatiGenerazione();
        datiGenerazione.setDataGenerazione(Calendar.getInstance().getTime());
        datiGenerazione.setUtente(getUserName());
        datiGenerazione.setNote("Importazione da palmare");
        datiGenerazione.setTipoGenerazione(TipoGenerazione.ESTERNO);

        AreaMagazzino areaMagazzino = generazioneImportazioneManager.generaDocumento(doc,
                doc.getInstallazione().getDeposito().getSedeEntita(), sedeAreaMagazzinoDTO, datiGenerazione);
        return areaMagazzino;
    }

    private AreaRifornimento creaAreaRifornimento(DocumentoVendingImport doc, AreaMagazzino areaMagazzino,
            Rifornimenti rifornimentoXML) {
        AreaRifornimento areaRifornimento = new AreaRifornimento();
        areaRifornimento.setOperatore(doc.getOperatore());
        areaRifornimento.setInstallazione(doc.getInstallazione());
        Distributore distributore = PanjeaEJBUtil
                .getImplementationClass((HibernateProxy) doc.getInstallazione().getArticolo());
        if (!(distributore instanceof Distributore)) {
            throw new GenericException("Il rifornimento " + rifornimentoXML.getProgressivo()
                    + " non è importabile perchè sull'installazione " + rifornimentoXML.getInstallazione()
                    + " non ho installato un distributore\n");
        }
        areaRifornimento.setDistributore(distributore);
        areaRifornimento.setAreaMagazzino(areaMagazzino);
        areaRifornimento.setNumeroSacchetto(rifornimentoXML.getCassetta());
        if (rifornimentoXML.getIncassato().compareTo(BigDecimal.ZERO) > 0) {
            areaRifornimento.setIncasso(rifornimentoXML.getIncassato());
        }

        DatiVendingArea datiVendingArea = new DatiVendingArea();
        Battute battute = new Battute();
        battute.setRifornite(rifornimentoXML.getLettura());
        datiVendingArea.setBattute(battute);

        LettureContatore letture = new LettureContatore();
        letture.setPrecedente(rifornimentoXML.getLetturaPrecedente());
        letture.setPrecedente(rifornimentoXML.getProve());
        datiVendingArea.setLettureContatore(letture);

        areaRifornimento.setDatiVendingArea(datiVendingArea);
        areaRifornimento = areaRifornimentoManager.salva(areaRifornimento);
        return areaRifornimento;
    }

    private DocumentoVendingImport creaDocumentoImportazione(ImportazioneXml importazioneXml, String codiceOperatore,
            Rifornimenti rifornimentoXML) throws DocumentoFatturatoNonPrevistoException, ImportazioneException {
        DocumentoVendingImport doc = new DocumentoVendingImport();
        doc.setDataDocumento(rifornimentoXML.getDataIntervento().toGregorianCalendar().getTime());

        Installazione installazione = installazioniManager
                .caricaByCodice((Integer.toString(rifornimentoXML.getInstallazione())));
        if (rifornimentoXML.getNumeroFattura() > 0
                && installazione.getTipoAreaMagazzino().getTipoDocumentoPerFatturazione() == null) {
            throw new DocumentoFatturatoNonPrevistoException(Integer.toString(rifornimentoXML.getInstallazione()),
                    rifornimentoXML.getMatricola(), rifornimentoXML.getProgressivo(),
                    installazione.getDeposito().getSedeEntita().getEntita().getAnagrafica().getDenominazione());
        }

        if (installazione == null) {
            throw new ImportazioneException(MessageFormat.format(
                    "Il rifornimento {0} non è importabile perchè l''installazione {1} non esiste\n",
                    rifornimentoXML.getProgressivo(), rifornimentoXML.getInstallazione()));
        }

        Distributore distributore = PanjeaEJBUtil.getImplementationClass((HibernateProxy) installazione.getArticolo());
        if (!(distributore instanceof Distributore)) {
            throw new ImportazioneException("Il rifornimento " + rifornimentoXML.getProgressivo()
                    + " non è importabile perchè sull'installazione " + rifornimentoXML.getInstallazione()
                    + " non ho installato un distributore\n");
        }

        doc.setIdSede(installazione.getDeposito().getSedeEntita().getId());
        doc.setIdEntita(installazione.getDeposito().getSedeEntita().getEntita().getId());
        doc.setDepositoDestinazione(installazione.getDeposito().getDepositoLite());
        doc.setTipoAreaMagazzino(installazione.getTipoAreaMagazzino());
        doc.setInstallazione(installazione);

        Operatore operatore = operatoriManager.caricaByCodice(codiceOperatore);
        doc.setDepositoOrigine(operatore.getMezzoTrasporto().getDeposito());
        doc.setOperatore(operatore);
        return doc;
    }

    /**
     * @return userName loggato
     */
    protected String getUserName() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getUserName();
    }

    @Override
    public AreaRifornimento importa(ImportazioneXml importazioneXml, String codiceOperatore,
            Rifornimenti rifornimentoXML) throws DocumentoFatturatoNonPrevistoException, ImportazioneException {
        DocumentoVendingImport doc = creaDocumentoImportazione(importazioneXml, codiceOperatore, rifornimentoXML);
        doc = aggiungiRighe(doc, importazioneXml, rifornimentoXML);
        AreaMagazzino am = creaAreaMagazzino(doc);
        AreaRifornimento rifornimento = creaAreaRifornimento(doc, am, rifornimentoXML);
        return rifornimento;
    }

}
