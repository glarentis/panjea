package it.eurotn.panjea.ordini.manager.documento.evasione;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.ordini.manager.documento.evasione.interfaces.DatiDistintaCaricoEvasioneManager;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(mappedName = "Panjea.DatiDistintaCaricoEvasioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DatiDistintaCaricoEvasioneManager")
public class DatiDistintaCaricoEvasioneManagerBean implements DatiDistintaCaricoEvasioneManager {

    @EJB
    private PanjeaDAO panjeaDAO;

    /**
     * Per ogni areaOrdine delle distinte di carico carica i relativi codici di pagamento.
     *
     * @return Mappa con chiave l'id dell'area ordine e valore l'id del codice di pagamento
     */
    private Map<Integer, CodicePagamento> getCodiciPagamentoPerAreaOrdine() {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("ao.id,ap.codicePagamento.id,ap.codicePagamento.version ");
        sb.append("from RigaDistintaCarico rdc join rdc.rigaArticolo.areaOrdine ao,AreaPartite ap ");
        sb.append("where ap.documento=ao.documento ");
        Map<Integer, CodicePagamento> codiciPagamento = new HashMap<Integer, CodicePagamento>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            CodicePagamento codicePagamento = new CodicePagamento();
            codicePagamento.setId((Integer) risultato[1]);
            codicePagamento.setVersion((Integer) risultato[2]);
            codiciPagamento.put((Integer) risultato[0], codicePagamento);
        }
        return codiciPagamento;
    }

    /**
     * Per ogni sede nelle distinte di carico carica i codici pagamento impostato direttamente sulla sede.
     *
     * @return Mappa con chiave l'id della sede e valore il codice pagamento
     */
    private Map<Integer, CodicePagamento> getCodiciPagamentoPerSedi() {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct se.id,sp.codicePagamento.id,sp.codicePagamento.version ");
        sb.append("from RigaDistintaCarico rdc join rdc.rigaArticolo.areaOrdine.documento.sedeEntita se, ");
        sb.append("SedePagamento sp  ");
        sb.append("where se.id=sp.sedeEntita.id ");
        Map<Integer, CodicePagamento> codiciPagamento = new HashMap<Integer, CodicePagamento>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            CodicePagamento codicePagamento = new CodicePagamento();
            codicePagamento.setId((Integer) risultato[1]);
            codicePagamento.setVersion((Integer) risultato[2]);
            codiciPagamento.put((Integer) risultato[0], codicePagamento);
        }
        return codiciPagamento;
    }

    /**
     * Per ogni sede nelle distinte di carico carica i codici pagamento considerando il flag di ereditaDatiCommerciali.
     *
     * @return Mappa con chiave l'id della sede e valore il codice pagamento (se eredita i dati commerciali il codice
     *         pagamento della sede principale)
     */
    private Map<Integer, CodicePagamento> getCodiciPagamentoPerSediConDatiCommerialiEreditati() {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct se.id,spp.codicePagamento.id,spp.codicePagamento.version ");
        sb.append("from RigaDistintaCarico rdc join rdc.rigaArticolo.areaOrdine.documento.sedeEntita se, ");
        sb.append("SedeOrdine so, ");
        sb.append("SedeEntita sep, ");
        sb.append("SedePagamento spp ");
        sb.append("where sep.entita.id=se.entita.id  ");
        sb.append("and sep.id=spp.sedeEntita.id ");
        sb.append("and sep.id=so.sedeEntita.id ");
        sb.append("and sep.tipoSede.sedePrincipale=true ");
        sb.append("and se.ereditaDatiCommerciali=true ");
        Map<Integer, CodicePagamento> codiciPagamento = new HashMap<Integer, CodicePagamento>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            CodicePagamento codicePagamento = new CodicePagamento();
            codicePagamento.setId((Integer) risultato[1]);
            codicePagamento.setVersion((Integer) risultato[2]);
            codiciPagamento.put((Integer) risultato[0], codicePagamento);
        }
        return codiciPagamento;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public DatiDistintaCaricoEvasione getDatiDistintaCaricoEvasione() {
        return new DatiDistintaCaricoEvasione(getTipiAreaMagazzinoPerSediConDatiCommerialiEreditati(),
                getTipiAreaMagazzinoPerSedi(), getTipiAreaMagazzinoPerArticoliContoTerziEntita(),
                getCodiciPagamentoPerAreaOrdine(), getCodiciPagamentoPerSedi(),
                getCodiciPagamentoPerSediConDatiCommerialiEreditati(),
                getTAMPerSedeRifatturazioneArticoliContoTerziEntita(), getTamPerArticoloContoTerziFornitore());
    }

    /**
     * Carica tutti gli articolo con tipo area magazzino di evasione che hanno un fornitore conto tezi.
     *
     * @return mappa degli articoli
     */
    private Map<Integer, TipoAreaMagazzino> getTamPerArticoloContoTerziFornitore() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select distinct art.id,so.tipoAreaEvasione.id,so.tipoAreaEvasione.version,so.tipoAreaEvasione.tipoDocumento.id,so.tipoAreaEvasione.tipoDocumento.version,so.tipoAreaEvasione.tipoDocumento.codice,so.tipoAreaEvasione.tipoDocumento.descrizione ");
        sb.append("from RigaDistintaCarico rd inner join rd.articolo art ");
        sb.append("										  inner join art.codiciEntita cae ");
        sb.append("										  inner join cae.entita ent, ");
        sb.append("										  SedeEntita sede, ");
        sb.append("										  SedeOrdine so ");
        sb.append("where cae.entita.class = FornitoreLite and ");
        sb.append("            cae.consegnaContoTerzi = true and ");
        sb.append("            sede.entita = ent and ");
        sb.append("            sede.tipoSede.sedePrincipale = true and ");
        sb.append("            so.sedeEntita = sede ");
        Map<Integer, TipoAreaMagazzino> articoli = new HashMap<Integer, TipoAreaMagazzino>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            TipoAreaMagazzino tam = new TipoAreaMagazzino();
            tam.setId((Integer) risultato[1]);
            tam.setVersion((Integer) risultato[2]);
            tam.getTipoDocumento().setId((Integer) risultato[3]);
            tam.getTipoDocumento().setVersion((Integer) risultato[4]);
            tam.getTipoDocumento().setCodice((String) risultato[5]);
            tam.getTipoDocumento().setDescrizione((String) risultato[6]);
            articoli.put((Integer) risultato[0], tam);
        }

        return articoli;
    }

    /**
     * Carica tutti gli articolo con tipo area magazzino di evasione che hanno una sede di rifatturazione conto tezi.
     *
     * @return mappa degli articoli
     */
    private Map<String, TipoAreaMagazzino> getTAMPerSedeRifatturazioneArticoliContoTerziEntita() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "select distinct se.id,art.id,so.tipoAreaEvasione.id,so.tipoAreaEvasione.version,so.tipoAreaEvasione.tipoDocumento.id,so.tipoAreaEvasione.tipoDocumento.version,so.tipoAreaEvasione.tipoDocumento.codice,so.tipoAreaEvasione.tipoDocumento.descrizione ");
        sb.append("from RigaDistintaCarico rd inner join rd.articolo art ");
        sb.append("										  inner join art.codiciEntita cae ");
        sb.append("										  inner join rd.rigaArticolo ro ");
        sb.append("										  inner join ro.areaOrdine ao ");
        sb.append("										  inner join ao.documento.sedeEntita se ");
        sb.append("										  inner join se.sedeMagazzino sm ");
        sb.append("										  inner join sm.sedePerRifatturazione smrif ");
        sb.append("										  inner join smrif.sedeEntita serif, ");
        sb.append("										  SedeOrdine so ");
        sb.append("where cae.entita = serif.entita and ");
        sb.append("            cae.consegnaContoTerzi = true and ");
        sb.append("            so.sedeEntita = serif ");

        Map<String, TipoAreaMagazzino> articoli = new HashMap<String, TipoAreaMagazzino>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            TipoAreaMagazzino tam = new TipoAreaMagazzino();
            tam.setId((Integer) risultato[2]);
            tam.setVersion((Integer) risultato[3]);
            tam.getTipoDocumento().setId((Integer) risultato[4]);
            tam.getTipoDocumento().setVersion((Integer) risultato[5]);
            tam.getTipoDocumento().setCodice((String) risultato[6]);
            tam.getTipoDocumento().setDescrizione((String) risultato[7]);

            Integer idSede = (Integer) risultato[0];
            Integer idArticolo = (Integer) risultato[1];
            String chiave = idSede + "#" + idArticolo;
            articoli.put(chiave, tam);
        }

        return articoli;
    }

    /**
     *
     * @return per ogni articolo conto terzi contenuto nelle righe distinta carico restituisce il tam sulla sede
     *         principale
     */
    private Map<String, TipoAreaMagazzino> getTipiAreaMagazzinoPerArticoliContoTerziEntita() {
        StringBuffer sb = new StringBuffer();
        sb.append(
                "select distinct se.id,art.id,so.tipoAreaEvasione.id,so.tipoAreaEvasione.version,so.tipoAreaEvasione.tipoDocumento.id,so.tipoAreaEvasione.tipoDocumento.version,so.tipoAreaEvasione.tipoDocumento.codice,so.tipoAreaEvasione.tipoDocumento.descrizione ");
        sb.append("from RigaDistintaCarico rd inner join rd.articolo art ");
        sb.append("inner join art.codiciEntita cae ");
        sb.append("inner join rd.rigaArticolo ro ");
        sb.append("inner join ro.areaOrdine ao ");
        sb.append("inner join ao.documento.sedeEntita se, ");
        sb.append("SedeOrdine so ");
        sb.append("where cae.entita = se.entita and ");
        sb.append("cae.consegnaContoTerzi = true and ");
        sb.append("so.sedeEntita = se ");
        Map<String, TipoAreaMagazzino> articoli = new HashMap<String, TipoAreaMagazzino>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            TipoAreaMagazzino tam = new TipoAreaMagazzino();
            tam.setId((Integer) risultato[2]);
            tam.setVersion((Integer) risultato[3]);
            tam.getTipoDocumento().setId((Integer) risultato[4]);
            tam.getTipoDocumento().setVersion((Integer) risultato[5]);
            tam.getTipoDocumento().setCodice((String) risultato[6]);
            tam.getTipoDocumento().setDescrizione((String) risultato[7]);

            Integer idSede = (Integer) risultato[0];
            Integer idArticolo = (Integer) risultato[1];
            String chiave = idSede + "#" + idArticolo;
            articoli.put(chiave, tam);
        }

        return articoli;
    }

    /**
     * Per ogni sede nelle distinte di carico carica le relative TipoAreaOrdine impostato direttamente sulla sede.
     *
     * @return Mappa con chiave l'id della sede e valore l'id del tipo area ordine
     */
    private Map<Integer, TipoAreaMagazzino> getTipiAreaMagazzinoPerSedi() {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("distinct se.id, ");
        sb.append("so.tipoAreaEvasione.id, ");
        sb.append("so.tipoAreaEvasione.version, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.id, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.version, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.codice, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.descrizione, ");
        sb.append("depD.id, ");
        sb.append("depD.version, ");
        sb.append("depD.codice, ");
        sb.append("depD.descrizione ");
        sb.append("from RigaDistintaCarico rdc join rdc.rigaArticolo.areaOrdine.documento.sedeEntita se, ");
        sb.append("SedeOrdine so join so.tipoAreaEvasione tae left join tae.depositoDestinazione depD ");
        sb.append("where se.id=so.sedeEntita.id ");
        Map<Integer, TipoAreaMagazzino> sedi = new HashMap<Integer, TipoAreaMagazzino>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            TipoAreaMagazzino tam = new TipoAreaMagazzino();
            tam.setId((Integer) risultato[1]);
            tam.setVersion((Integer) risultato[2]);
            tam.getTipoDocumento().setId((Integer) risultato[3]);
            tam.getTipoDocumento().setVersion((Integer) risultato[4]);
            tam.getTipoDocumento().setCodice((String) risultato[5]);
            tam.getTipoDocumento().setDescrizione((String) risultato[6]);
            if ((Integer) risultato[7] != null) {
                DepositoLite depositoDest = new DepositoLite();
                depositoDest.setId((Integer) risultato[7]);
                depositoDest.setVersion((Integer) risultato[8]);
                depositoDest.setCodice((String) risultato[9]);
                depositoDest.setDescrizione((String) risultato[10]);
                tam.setDepositoDestinazione(depositoDest);
            }
            sedi.put((Integer) risultato[0], tam);
        }
        return sedi;
    }

    /**
     * Per ogni sede nelle distinte di carico carica le relative TipoAreaOrdine considerando il flag di
     * ereditaDatiCommerciali.
     *
     * @return Mappa con chiave l'id della sede e valore l'id del tipoAreaOrdine (se eredita i dati commerciali il
     *         tipoAreaOrdine della sede principale)
     */
    private Map<Integer, TipoAreaMagazzino> getTipiAreaMagazzinoPerSediConDatiCommerialiEreditati() {
        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        sb.append("distinct se.id, ");
        sb.append("so.tipoAreaEvasione.id, ");
        sb.append("so.tipoAreaEvasione.version, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.id, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.version, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.codice, ");
        sb.append("so.tipoAreaEvasione.tipoDocumento.descrizione, ");
        sb.append("depD.id, ");
        sb.append("depD.version, ");
        sb.append("depD.codice, ");
        sb.append("depD.descrizione ");
        sb.append("from RigaDistintaCarico rdc join rdc.rigaArticolo.areaOrdine.documento.sedeEntita se, ");
        sb.append("SedeOrdine so,SedeEntita sep join so.tipoAreaEvasione tae left join tae.depositoDestinazione depD ");
        sb.append("where sep.entita.id=se.entita.id ");
        sb.append("and sep.id=so.sedeEntita.id ");
        sb.append("and sep.tipoSede.sedePrincipale=true ");
        sb.append("and se.ereditaDatiCommerciali=true ");
        HashMap<Integer, TipoAreaMagazzino> tipiAreaOrdineSediEreditate = new HashMap<Integer, TipoAreaMagazzino>();
        Query query = panjeaDAO.prepareQuery(sb.toString());
        @SuppressWarnings("unchecked")
        List<Object[]> risultati = query.getResultList();
        for (Object[] risultato : risultati) {
            TipoAreaMagazzino tam = new TipoAreaMagazzino();
            tam.setId((Integer) risultato[1]);
            tam.setVersion((Integer) risultato[2]);
            tam.getTipoDocumento().setId((Integer) risultato[3]);
            tam.getTipoDocumento().setVersion((Integer) risultato[4]);
            tam.getTipoDocumento().setCodice((String) risultato[5]);
            tam.getTipoDocumento().setDescrizione((String) risultato[6]);
            if ((Integer) risultato[7] != null) {
                DepositoLite depositoDest = new DepositoLite();
                depositoDest.setId((Integer) risultato[7]);
                depositoDest.setVersion((Integer) risultato[8]);
                depositoDest.setCodice((String) risultato[9]);
                depositoDest.setDescrizione((String) risultato[10]);
                tam.setDepositoDestinazione(depositoDest);
            }
            tipiAreaOrdineSediEreditate.put((Integer) risultato[0], tam);
        }
        return tipiAreaOrdineSediEreditate;
    }

}
