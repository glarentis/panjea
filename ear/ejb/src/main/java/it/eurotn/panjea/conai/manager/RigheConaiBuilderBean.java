package it.eurotn.panjea.conai.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.RigaConaiArticolo;
import it.eurotn.panjea.conai.domain.RigaConaiComponente;
import it.eurotn.panjea.conai.exception.ArticoloConaiNonPresenteException;
import it.eurotn.panjea.conai.manager.interfaces.ConaiManager;
import it.eurotn.panjea.conai.manager.interfaces.RigheConaiBuilder;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloGenerata;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

/**
 * @author leonardo
 */
@Stateless(name = "Panjea.RigheConaiBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheConaiBuilder")
public class RigheConaiBuilderBean implements RigheConaiBuilder {

    private static Logger logger = Logger.getLogger(RigheConaiBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private ConaiManager conaiManager;

    /**
     * Cancella se presenti le righe conai articolo dell'area magazzino scelta.
     *
     * @param areaMagazzino
     *            l'area di cui cancellare le righe conai presenti
     */
    private void cancellaRigheConai(AreaMagazzino areaMagazzino) {
        List<RigaConaiArticolo> righeConai = conaiManager.caricaRigheConai(areaMagazzino);
        if (righeConai.size() > 0) {
            for (RigaConaiArticolo rigaConaiArticolo : righeConai) {
                rigaMagazzinoManager.getDao().cancellaRigaMagazzino(rigaConaiArticolo);
            }
        }
    }

    /**
     * Converte un array in una mappa (key = ConaiMateriale#CodiceIva).<br>
     * L'array contiene:
     * <ul>
     * <li>0 --> qta magazzino</li>
     * <li>1 --> codice iva</li>
     * <li>2 --> RigaConaiComponente</li>
     * <li>3 --> ConaiArticolo</li>
     * <li></li>
     * </ul>
     *
     * @param areaMagazzino
     *            l'area magazzino coon le informazioni per generare le righe
     * @param arrayResult
     *            l'array con i valori utili a generare le righe articolo
     * @return Map<String, RigaArticolo> dove la chiave e' identificata da ConaiMateriale#CodiceIva
     */
    private Map<String, RigaArticoloGenerata> convertTuplaToRigheArticolo(List<Object[]> arrayResult,
            AreaMagazzino areaMagazzino) {
        Map<String, RigaArticoloGenerata> righeArticoloConai = new HashMap<String, RigaArticoloGenerata>();
        Map<String, BigDecimal> righeQtaTotale = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> righeQtaEsenzione = new HashMap<String, BigDecimal>();

        Integer numeroDecimaliQta = 0;

        for (Object[] objects : arrayResult) {

            // qta
            Double qta = (Double) objects[0];

            // CODICE IVA
            CodiceIva codiceIva = (CodiceIva) objects[1];

            // RigaConaiComponente per recuperare il peso unitario / peso totale del materiale
            RigaConaiComponente rigaConaiComponente = (RigaConaiComponente) objects[2];

            numeroDecimaliQta = rigaConaiComponente.getNumeroDecimaliQta();

            // ConaiArticolo per il materiale
            ConaiArticolo conaiArticolo = (ConaiArticolo) objects[3];

            // peso totale escluso il peso di esenzione, se presente
            BigDecimal pesoUnitario = rigaConaiComponente.getPesoUnitario().abs();
            BigDecimal percEsenzione = rigaConaiComponente.getPercentualeEsenzione();

            // ricalcolo il peso totale con peso unitario, qta riga e percentuale esenzione
            BigDecimal pesoTotale = pesoUnitario.multiply(BigDecimal.valueOf(qta))
                    .setScale(rigaConaiComponente.getNumeroDecimaliQta(), RoundingMode.HALF_UP);
            BigDecimal pesoEsenzione = BigDecimal.ZERO;
            if (percEsenzione != null && BigDecimal.ZERO.compareTo(percEsenzione) != 0) {
                pesoEsenzione = pesoTotale.multiply(percEsenzione).divide(BigDecimal.valueOf(100.00))
                        .setScale(rigaConaiComponente.getNumeroDecimaliQta(), RoundingMode.HALF_UP);
            }

            // materiale articolo conai
            ArticoloLite articoloConai = conaiArticolo.getArticolo();
            if (articoloConai == null) {
                throw new ArticoloConaiNonPresenteException(conaiArticolo.getMateriale());
            }

            ConaiMateriale materiale = conaiArticolo.getMateriale();

            // KEY per la mappa
            String key = materiale.name() + "#" + codiceIva.getId();

            // se non ho già la riga articolo per la chiave la creo, le qta le imposto dopo
            if (!righeArticoloConai.containsKey(key)) {
                RigaArticoloGenerata rigaArticolo = creaRigaArticoloConai(areaMagazzino, articoloConai);
                rigaArticolo.setQta(0.0);
                rigaArticolo.setNumeroDecimaliQta(articoloConai.getNumeroDecimaliQta());
                rigaArticolo.setUnitaMisura(articoloConai.getUnitaMisura().getCodice());
                rigaArticolo.setQtaMagazzino(0.0);
                rigaArticolo.setUnitaMisuraQtaMagazzino(articoloConai.getUnitaMisura().getCodice());
                rigaArticolo.setAreaMagazzino(areaMagazzino);

                Importo importo = new Importo();
                importo.setImportoInValuta(conaiArticolo.getPrezzo(areaMagazzino.getDataRegistrazione()));
                importo.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());

                rigaArticolo.setPrezzoUnitario(importo);
                rigaArticolo.setCodiceIva(codiceIva);

                righeArticoloConai.put(key, rigaArticolo);

                righeQtaTotale.put(key, BigDecimal.ZERO);
                righeQtaEsenzione.put(key, BigDecimal.ZERO);
            }

            // imposto le qta per la riga articolo
            RigaArticolo rigaConaiMaterialeCorrente = righeArticoloConai.get(key);

            Double qtaConai = rigaConaiMaterialeCorrente.getQta() + pesoTotale.doubleValue()
                    - pesoEsenzione.doubleValue();

            rigaConaiMaterialeCorrente.setQta(qtaConai);
            rigaConaiMaterialeCorrente.setQtaMagazzino(qtaConai);

            BigDecimal qtaTotale = righeQtaTotale.get(key).add(pesoTotale);
            righeQtaTotale.put(key, qtaTotale);
            BigDecimal qtaEsenzione = righeQtaEsenzione.get(key).add(pesoEsenzione);
            righeQtaEsenzione.put(key, qtaEsenzione);
        }

        // aggiorno le note delle righe articolo conai con i totali
        DefaultNumberFormatterFactory formatterFactory = new DefaultNumberFormatterFactory("#,##0", numeroDecimaliQta,
                BigDecimal.class);
        StringBuilder sb = null;
        for (Entry<String, RigaArticoloGenerata> entry : righeArticoloConai.entrySet()) {
            sb = new StringBuilder();
            try {
                sb.append("<br>Qta totale: ");
                sb.append(formatterFactory.getDefaultFormatter().valueToString(righeQtaTotale.get(entry.getKey())));
                sb.append(" Qta in esenzione: ");
                sb.append(formatterFactory.getDefaultFormatter().valueToString(righeQtaEsenzione.get(entry.getKey())));
            } catch (ParseException e) {
                logger.error("--> errore durante la conversione delle quantità conai sulla riga materiale.", e);
                throw new RuntimeException("errore durante la conversione delle quantità conai sulla riga materiale.",
                        e);
            }

            entry.getValue().setNoteRiga(sb.toString());
        }

        return righeArticoloConai;
    }

    /**
     * Crea una singola articolo conai chiamando il dao a seconda dei parametri passati.
     *
     * @param areaMagazzino
     *            l'area magazzino per cui creare la riga
     * @param articoloConai
     *            l'articolo da associare alla riga
     * @return RigaArticolo
     */
    private RigaArticoloGenerata creaRigaArticoloConai(AreaMagazzino areaMagazzino, ArticoloLite articoloConai) {
        Integer idSedeEntita = null;
        Integer idEntita = null;
        String codiceLingua = null;
        if (areaMagazzino.getDocumento().getEntita() != null) {
            idEntita = areaMagazzino.getDocumento().getEntita().getId();
        }
        if (areaMagazzino.getDocumento().getSedeEntita() != null) {
            idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
            codiceLingua = areaMagazzino.getDocumento().getSedeEntita().getLingua();
        }

        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setIdArticolo(articoloConai.getId());
        parametri.setData(areaMagazzino.getDocumento().getDataDocumento());
        parametri.setIdEntita(idEntita);
        parametri.setIdSedeEntita(idSedeEntita);
        parametri.setImporto(areaMagazzino.getDocumento().getTotale().clone());
        parametri.setCodiceIvaAlternativo(areaMagazzino.getCodiceIvaAlternativo());
        parametri.setIdZonaGeografica(areaMagazzino.getIdZonaGeografica());
        parametri.setNoteSuDestinazione(areaMagazzino.getTipoAreaMagazzino().isNoteSuDestinazione());
        parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());
        parametri.setCodiceValuta(areaMagazzino.getDocumento().getTotale().getCodiceValuta());
        parametri.setCodiceLingua(codiceLingua);
        parametri.setTipologiaCodiceIvaAlternativo(areaMagazzino.getTipologiaCodiceIvaAlternativo());
        parametri.setIdAgente(null);
        parametri.setPercentualeScontoCommerciale(null);
        parametri.setStrategiaTotalizzazioneDocumento(
                areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());

        RigaArticoloGenerata rigaArticolo = (RigaArticoloGenerata) rigaMagazzinoManager.getDao(new RigaConaiArticolo())
                .creaRigaArticolo(parametri);
        rigaArticolo.setRigaAutomatica(true);
        return rigaArticolo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino) {
        logger.debug("--> Enter creaRigheMagazzino");

        List<RigaMagazzino> righeResult = new ArrayList<>();

        boolean isConaiEnabled = areaMagazzino.getTipoAreaMagazzino().isGestioneConai();
        // se il tipo area magazzino non ha impostato genera valori fatturato, non creo le righe conai
        boolean isValoriFatturato = areaMagazzino.getTipoAreaMagazzino().isValoriFatturato();
        if (isValoriFatturato && isConaiEnabled) {
            cancellaRigheConai(areaMagazzino);

            // verifico che non ci siano materiali senza articolo
            StringBuilder sb = new StringBuilder();
            sb.append("select rc.materiale ");
            sb.append(
                    "from maga_conai_righe_componente rc left join maga_conai_articoli ca on rc.materiale=ca.materiale ");
            sb.append(
                    "  					   		 inner join maga_righe_magazzino ra on ra.id = rc.rigaArticolo_id ");
            sb.append("where ra.areaMagazzino_id=:idAreaMagazzino and ca.materiale is null ");
            Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
            query.setParameter("idAreaMagazzino", areaMagazzino.getId());
            List<Integer> results = null;
            try {
                results = panjeaDAO.getResultList(query);
            } catch (DAOException e1) {
                logger.error("-->errore durante la verifica degli articoli conai", e1);
                throw new RuntimeException(e1);
            }
            if (!results.isEmpty()) {
                throw new ArticoloConaiNonPresenteException(ConaiMateriale.values()[results.get(0)]);
            }

            sb = new StringBuilder();
            // recupero le RigaConaiComponente di AreaMagazzino per creare una RigaArticolo (raggruppo per
            // MATERIALE,PESO,CODICE IVA DELLA RIGA ARTICOLO)
            sb.append("select sum(ra.qta), ra.codiceIva, rc, ca ");
            sb.append("from RigaConaiComponente rc,ConaiArticolo ca  ");
            sb.append("inner join rc.rigaArticolo ra ");
            sb.append("inner join ra.articolo a ");
            sb.append("where rc.materiale=ca.materiale ");
            sb.append("and ra.areaMagazzino=:areaMagazzino ");
            sb.append("group by ca.materiale,rc.pesoUnitario, ra.codiceIva ");

            query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("areaMagazzino", areaMagazzino);
            List<Object[]> result = null;
            try {
                result = panjeaDAO.getResultList(query);
            } catch (DAOException e) {
                logger.error("-->errore nel recuperare le righe per il conai.AreaMagazzino " + areaMagazzino, e);
                throw new RuntimeException(e);
            }

            // prepara le righe articolo da salvare
            Map<String, RigaArticoloGenerata> righeArticoloConai = convertTuplaToRigheArticolo(result, areaMagazzino);

            // salva le rigaArticolo se la qta è diversa da zero
            for (RigaArticoloGenerata rigaArticolo : righeArticoloConai.values()) {
                try {
                    rigaArticolo = (RigaArticoloGenerata) rigaMagazzinoManager.getDao()
                            .salvaRigaMagazzino(rigaArticolo);
                    righeResult.add(rigaArticolo);
                } catch (RimanenzaLottiNonValidaException e) {
                    logger.error("--> Attenzione si sta cercando di salvare una riga articolo conai con dei lotti", e);
                    throw new IllegalStateException(e);
                } catch (RigheLottiNonValideException e) {
                    logger.error("--> Attenzione si sta cercando di salvare una riga articolo conai con dei lotti", e);
                    throw new IllegalStateException(e);
                } catch (QtaLottiMaggioreException e) {
                    logger.error("--> Attenzione si sta cercando di salvare una riga articolo conai con dei lotti", e);
                    throw new IllegalStateException(e);
                }
            }
        }
        logger.debug("--> Exit creaRigheMagazzino");
        return righeResult;
    }

}
