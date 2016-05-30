/**
 *
 */
package it.eurotn.panjea.magazzino.manager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaArticoloEstesa;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.StrategiaTotalizzazione;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.Totalizzatore;
import it.eurotn.panjea.magazzino.manager.interfaces.GestioneInventarioArticoloDocumentoManager;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.SqlExecuter;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.GestioneInventarioArticoloDocumentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.GestioneInventarioArticoloDocumentoManager")
public class GestioneInventarioArticoloDocumentoManagerBean implements GestioneInventarioArticoloDocumentoManager {

    private static Logger logger = Logger.getLogger(GestioneInventarioArticoloDocumentoManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private StrategiaTotalizzazione strategiaTotalizzazione;

    @EJB
    private ITotalizzatoriQueryExecutor totalizzatoriQueryExecutor;

    @EJB
    private AreaIvaManager areaIvaManager;

    @Override
    public void cancellaInventarioArticolo(Date data, DepositoLite deposito) {
        logger.debug("--> Enter cancellaInventarioArticolo");

        Query query = panjeaDAO.prepareNamedQuery("InventarioArticolo.cancellaInventarioDeposito");
        query.setParameter("data", data);
        query.setParameter("deposito", deposito);

        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            logger.error("--> errore durante la cancellazione dell'inventario articolo.", e);
            throw new RuntimeException("errore durante la cancellazione dell'inventario articolo.", e);
        }

        logger.debug("--> Exit cancellaInventarioArticolo");
    }

    @Override
    public AreaMagazzino creaAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino, Date dataMovimento,
            DepositoLite deposito) {
        Documento documento = new Documento();

        documento.setCodiceAzienda(getAzienda());
        documento.setDataDocumento(dataMovimento);
        documento.setTipoDocumento(tipoAreaMagazzino.getTipoDocumento());
        Importo importoDoc = new Importo("EUR", BigDecimal.ONE);
        importoDoc.setImportoInValuta(BigDecimal.ZERO);
        importoDoc.setImportoInValutaAzienda(BigDecimal.ZERO);
        documento.setTotale(importoDoc);

        AreaMagazzino areaMagazzino = new AreaMagazzino();

        areaMagazzino.setDocumento(documento);
        areaMagazzino.setDataRegistrazione(dataMovimento);
        areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataMovimento);
        areaMagazzino.setAnnoMovimento(calendar.get(Calendar.YEAR));
        areaMagazzino.setTipoAreaMagazzino(tipoAreaMagazzino);
        areaMagazzino.setDepositoOrigine(deposito);

        // salva l'area magazzino
        try {
            areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, true);
        } catch (Exception e) {
            logger.error("--> Errore durante la creazione del documento di fatturazione", e);
            throw new RuntimeException("Errore durante la creazione del documento di fatturazione", e);
        }

        return areaMagazzino;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void creaRigaArticolo(AreaMagazzino areaMagazzino, Articolo articolo, double quantita,
            String linguaAzienda) {

        String codiceLingua = null;
        if (areaMagazzino.getDocumento().getSedeEntita() != null) {
            codiceLingua = areaMagazzino.getDocumento().getSedeEntita().getLingua();
        }

        RigaArticolo rigaArticolo = new RigaArticolo();
        rigaArticolo.getPrezzoUnitario().setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
        rigaArticolo.setArticolo(articolo.getArticoloLite());

        if (articolo.getNumeroDecimaliQta() != null) {
            rigaArticolo.setNumeroDecimaliQta(articolo.getNumeroDecimaliQta());
        }

        CodiceIva codiceIva = ETipologiaCodiceIvaAlternativo.NESSUNO.getCodiceIva(articolo, null);
        // if (codiceIva != null) {
        // // codice iva è lazy. Lo carico
        // codiceIva = panjeaDAO.loadLazy(CodiceIva.class, codiceIva.getId());
        // }
        rigaArticolo.setCodiceIva(codiceIva);
        rigaArticolo.setUnitaMisura(articolo.getUnitaMisura().getCodice());
        // l'unita' di misura qt.magazzino non e' obbligatoria nella anagrafica
        // articoli,
        // ma sulla riga articolo e' utile averla per statistiche e completezza;
        // se non ho impostato l'unita' di misura di magazzino prendo di default
        // l'unita' di misura standard
        if (articolo.getUnitaMisuraQtaMagazzino() != null) {
            rigaArticolo.setUnitaMisuraQtaMagazzino(articolo.getUnitaMisuraQtaMagazzino().getCodice());
        } else {
            rigaArticolo.setUnitaMisuraQtaMagazzino(articolo.getUnitaMisura().getCodice());
        }
        rigaArticolo.setDescrizione(articolo.getDescrizione());

        // Devo caricarmi la lingua aziendale perchè l'articolo ha solo una
        // mappa delle descrizioni in lingua estesa
        // quindi è l'unico modo che ho per avere quella in lingua azienda
        String linguaEntita = codiceLingua;

        DescrizioneLinguaArticoloEstesa descrizioneEstesa = articolo.getDescrizioniLinguaEstesa().get(linguaAzienda);
        if (descrizioneEstesa != null) {
            rigaArticolo.setNoteRiga(descrizioneEstesa.getDescrizione());
        }

        if (linguaEntita != null && !linguaEntita.isEmpty() && !linguaEntita.equals(linguaAzienda)) {
            // in lingua
            rigaArticolo.setDescrizioneLingua(articolo.getDescrizione(linguaEntita));

            // in lungua entità
            DescrizioneLinguaArticoloEstesa descrizioneLingua = articolo.getDescrizioniLinguaEstesa().get(linguaEntita);
            String descrizioneLinguaEstesa = (descrizioneLingua != null) ? descrizioneLingua.getDescrizione()
                    : rigaArticolo.getNoteRiga();
            if (descrizioneLinguaEstesa != null) {
                descrizioneLinguaEstesa = descrizioneLinguaEstesa.trim();
            }
            rigaArticolo.setNoteLinguaRiga(descrizioneLinguaEstesa);
        }

        // imposto se riportare le note dell'articolo sulla riga
        rigaArticolo.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());

        rigaArticolo.setFormulaTrasformazione(null);
        rigaArticolo.setFormulaTrasformazioneQtaMagazzino(null);
        rigaArticolo.setArticoloLibero(articolo.isArticoloLibero());
        rigaArticolo.setTipoOmaggio(TipoOmaggio.NESSUNO);
        rigaArticolo.setCategoriaContabileArticolo(articolo.getCategoriaContabileArticolo());

        rigaArticolo.setVariazione1(BigDecimal.ZERO);
        rigaArticolo.setVariazione2(BigDecimal.ZERO);
        rigaArticolo.setVariazione3(BigDecimal.ZERO);
        rigaArticolo.setVariazione4(BigDecimal.ZERO);

        // initImporti
        Importo importoDocumento = areaMagazzino.getDocumento().getTotale().clone();

        Importo importoBase = new Importo();
        importoBase.setCodiceValuta(importoDocumento.getCodiceValuta());
        importoBase.setTassoDiCambio(importoDocumento.getTassoDiCambio());
        importoBase.setImportoInValuta(BigDecimal.ZERO);
        importoBase.setImportoInValutaAzienda(BigDecimal.ZERO);
        rigaArticolo.setPrezzoUnitario(importoBase);

        rigaArticolo.setPrezzoDeterminato(BigDecimal.ZERO);

        rigaArticolo.setQta(quantita);
        rigaArticolo.setQtaMagazzino(quantita);

        rigaArticolo.setAreaMagazzino(areaMagazzino);

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("##0.00", otherSymbols);
        decimalFormat.setGroupingUsed(false);

        String pattern = "##0.00";
        if (rigaArticolo.getNumeroDecimaliQta().intValue() > 0) {
            pattern = "##0." + StringUtils.repeat("0", rigaArticolo.getNumeroDecimaliQta().intValue());
        }
        DecimalFormat qtaDecimalFormat = new DecimalFormat(pattern, otherSymbols);
        qtaDecimalFormat.setGroupingUsed(false);

        // ho costruito la riga articolo in modo che mi vengano calcolati il prezzo netto, totale ecc... per usarla per
        // costruire l'SQL di inserimento per prestazioni
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO maga_righe_magazzino ");
        sb.append(
                "(TIPO_RIGA,id,userInsert,version,numeroRiga,nota,descrizione,descrizioneLingua,noteRiga,codiceValutaNetto,importoInValutaNetto,importoInValutaAziendaNetto,tassoDiCambioNetto,");
        sb.append(
                "codiceValutaTotale,importoInValutaTotale,importoInValutaAziendaTotale,tassoDiCambioTotale,codiceValuta,importoInValuta,importoInValutaAzienda,tassoDiCambio,");
        sb.append(
                "qta,variazione1,variazione2,variazione3,variazione4,areaMagazzino_id,articolo_id,codiceIva_id,formulaTrasformazione_id,ordinamento,articoloLibero,descrizioneCalcolo,unitaMisura,");
        sb.append(
                "numeroDecimaliPrezzo,numeroDecimaliQta,chiave,codiceTipoDocumentoCollegato,dataAreaMagazzinoCollegata,livello,rigaMagazzinoCollegata_id,categoriaContabileArticolo_id,");
        sb.append(
                "formulaTrasformazioneQtaMagazzino_id,qtaMagazzino,unitaMisuraQtaMagazzino,prezzoDeterminato,noteSuDestinazione,rigaOrdineCollegata_Id,areaOrdineCollegata_Id,timeStamp,");
        sb.append(
                "moltQtaOrdine,agente_id,areaMagazzinoCollegata_id,noteLinguaRiga,percProvvigione,rigaAutomatica,sconto1Bloccato,articoloDistinta_id,rigaDistintaCollegata_id,tipoOmaggio,");
        sb.append(
                "formulaComponente,formulaConversioneUnitaMisura,prezzoUnitarioBaseProvvigionale,ivata,strategiaTotalizzazioneDocumento,importoInValutaUnitarioImposta,importoInValutaAziendaUnitarioImposta,tassoDiCambioUnitarioImposta,codiceValutaUnitarioImposta,somministrazione) ");
        sb.append(" VALUES ('A',null,");
        sb.append("'" + getPrincipal().getUserName() + "',");
        sb.append("0,0,null,");

        if (rigaArticolo.getDescrizione() != null) {
            sb.append("'" + rigaArticolo.getDescrizione().replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\") + "',");
        } else {
            sb.append("null,");
        }
        if (rigaArticolo.getDescrizioneLingua() != null) {
            sb.append("'" + rigaArticolo.getDescrizioneLingua().replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\")
                    + "',");
        } else {
            sb.append("null,");
        }
        if (rigaArticolo.getNoteRiga() != null) {
            sb.append("'" + rigaArticolo.getNoteRiga().replaceAll("'", "''").replaceAll("\\\\", "\\\\\\\\") + "',");
        } else {
            sb.append("null,");
        }
        sb.append("'" + rigaArticolo.getPrezzoNetto().getCodiceValuta() + "',");
        sb.append(rigaArticolo.getPrezzoNetto().getImportoInValuta() + ",");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoNetto().getImportoInValutaAzienda()) + ",");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoNetto().getTassoDiCambio()) + ",");

        sb.append("'" + rigaArticolo.getPrezzoTotale().getCodiceValuta() + "',");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoTotale().getImportoInValuta()) + ",");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoTotale().getImportoInValutaAzienda()) + ",");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoTotale().getTassoDiCambio()) + ",");

        sb.append("'" + rigaArticolo.getPrezzoUnitario().getCodiceValuta() + "',");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoUnitario().getImportoInValuta()) + ",");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoUnitario().getImportoInValutaAzienda()) + ",");
        sb.append(decimalFormat.format(rigaArticolo.getPrezzoUnitario().getTassoDiCambio()) + ",");

        sb.append(qtaDecimalFormat.format(rigaArticolo.getQta()) + ",");

        // gli sconti non sono mai avvalorati
        sb.append("0,0,0,0,");

        sb.append(rigaArticolo.getAreaMagazzino().getId() + ",");

        sb.append(rigaArticolo.getArticolo().getId() + ",");

        sb.append(rigaArticolo.getCodiceIva().getId() + ",");

        if (rigaArticolo.getFormulaTrasformazione() != null) {
            sb.append(rigaArticolo.getFormulaTrasformazione().getId() + ",");
        } else {
            sb.append("null,");
        }

        sb.append(Calendar.getInstance().getTimeInMillis() + ",");

        sb.append("0,null,");

        sb.append("'" + rigaArticolo.getUnitaMisura() + "',");

        sb.append(rigaArticolo.getNumeroDecimaliPrezzo() + ",");
        sb.append(rigaArticolo.getNumeroDecimaliQta() + ",");

        sb.append("null,null,null,0,null,");

        if (rigaArticolo.getCategoriaContabileArticolo() != null) {
            sb.append(rigaArticolo.getCategoriaContabileArticolo().getId() + ",");
        } else {
            sb.append("null,");
        }

        if (rigaArticolo.getFormulaTrasformazioneQtaMagazzino() != null) {
            sb.append(rigaArticolo.getFormulaTrasformazioneQtaMagazzino().getId() + ",");
        } else {
            sb.append("null,");
        }

        sb.append(qtaDecimalFormat.format(rigaArticolo.getQtaMagazzino()) + ",");

        sb.append("'" + rigaArticolo.getUnitaMisuraQtaMagazzino() + "',");

        if (rigaArticolo.getPrezzoDeterminato() != null) {
            sb.append(decimalFormat.format(rigaArticolo.getPrezzoDeterminato()) + ",");
        } else {
            sb.append("null,");
        }

        sb.append("0,null,null,");

        sb.append(Calendar.getInstance().getTimeInMillis() + ",");

        sb.append("0,null,null,null,null,0,0,null,null,0,null,null,null,0,0,0,0,1,");
        sb.append("'" + rigaArticolo.getPrezzoUnitario().getCodiceValuta() + "',false) ");

        try {
            SqlExecuter executer = new SqlExecuter();
            executer.setSql(sb.toString());
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio della riga articolo.", e);
            throw new RuntimeException("errore durante il salvataggio della riga articolo.", e);
        }
    }

    /**
     *
     * @return codice Azienda loggata
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    public List<AreaMagazzinoRicerca> totalizzaAreeMagazzino(List<AreaMagazzino> areeMagazzino) {
        List<AreaMagazzinoRicerca> result = new ArrayList<AreaMagazzinoRicerca>();
        for (AreaMagazzino areaMagazzino : areeMagazzino) {
            Totalizzatore totalizzatore = strategiaTotalizzazione
                    .getTotalizzatore(areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

            totalizzatoriQueryExecutor.setAreaDocumento(areaMagazzino);
            totalizzatoriQueryExecutor.setQueryString("RigaArticolo.caricaByTipo");

            AreaIva areaIva = areaIvaManager.generaAreaIvaDaMagazzino(areaMagazzino, null);
            areaMagazzino.setDocumento(totalizzatore.totalizzaDocumento(areaMagazzino.getDocumento(),
                    areaMagazzino.getTotaliArea(), totalizzatoriQueryExecutor, areaIva.getRigheIva()));
            areaMagazzino.setStatoAreaMagazzino(StatoAreaMagazzino.PROVVISORIO);

            try {
                areaMagazzino = areaMagazzinoManager.salvaAreaMagazzino(areaMagazzino, true);
                areaMagazzinoManager.validaRigheMagazzino(areaMagazzino, null, false, false);
            } catch (Exception e) {
                logger.error("--> Errore durante il salvataggio dell'area magazzino", e);
                throw new RuntimeException("Errore durante il salvataggio dell'area magazzino", e);
            }

            AreaMagazzinoRicerca areaRicerca = new AreaMagazzinoRicerca();
            areaRicerca.setAzienda(getAzienda());
            areaRicerca.setCodiceDepositoOrigine(areaMagazzino.getDepositoOrigine().getCodice());
            areaRicerca.setCodiceDocumento(areaMagazzino.getDocumento().getCodice());
            areaRicerca.setCodiceTipoDocumento(areaMagazzino.getDocumento().getTipoDocumento().getCodice());
            areaRicerca.setDataDocumento(areaMagazzino.getDocumento().getDataDocumento());
            areaRicerca.setDataRegistrazione(areaMagazzino.getDataRegistrazione());
            areaRicerca.setDenominazioneEntita(getAzienda());
            areaRicerca.setDescrizioneDepositoOrigine(areaMagazzino.getDepositoOrigine().getDescrizione());
            areaRicerca.setDescrizioneTipoDocumento(areaMagazzino.getDocumento().getTipoDocumento().getDescrizione());
            areaRicerca.setIdAreaMagazzino(areaMagazzino.getId());
            areaRicerca.setIdDepositoOrigine(areaMagazzino.getDepositoOrigine().getId());
            areaRicerca.setIdDocumento(areaMagazzino.getDocumento().getId());
            areaRicerca.setIdTipoDocumento(areaMagazzino.getDocumento().getTipoDocumento().getId());
            areaRicerca.setStato(areaMagazzino.getStatoAreaMagazzino());
            areaRicerca.setTipoEntita(TipoEntita.AZIENDA);
            areaRicerca.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
            areaRicerca.setTotaleDocumentoInValutaAzienda(
                    areaMagazzino.getDocumento().getTotale().getImportoInValutaAzienda());
            areaRicerca.setTotaleDocumentoInValuta(areaMagazzino.getDocumento().getTotale().getImportoInValuta());
            areaRicerca.setCodiceValuta("EUR");
            areaRicerca.setValoriFatturato(areaMagazzino.getTipoAreaMagazzino().isValoriFatturato());
            areaRicerca.setVersionDepositoOrigine(areaMagazzino.getDepositoOrigine().getVersion());

            result.add(areaRicerca);
        }

        return result;
    }

}
