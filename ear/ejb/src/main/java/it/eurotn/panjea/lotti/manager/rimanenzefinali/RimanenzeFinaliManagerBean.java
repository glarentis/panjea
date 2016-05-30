package it.eurotn.panjea.lotti.manager.rimanenzefinali;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.manager.LottiManagerBean.RimanenzaLotto;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.lotti.manager.rimanenzefinali.interfaces.RimanenzeFinaliManager;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneDepositoManager;
import it.eurotn.panjea.magazzino.manager.sqlbuilder.GiacenzaQueryBuilder;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(mappedName = "Panjea.RimanenzeFinaliManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RimanenzeFinaliManagerBean")
public class RimanenzeFinaliManagerBean implements RimanenzeFinaliManager {

    private class CodiceArticoloComparator implements Comparator<RimanenzeFinaliDTO> {

        @Override
        public int compare(RimanenzeFinaliDTO o1, RimanenzeFinaliDTO o2) {
            String denominazione1 = o1.getFornitore().getAnagrafica().getDenominazione();
            String denominazione2 = o2.getFornitore().getAnagrafica().getDenominazione();
            if (denominazione1.compareTo(denominazione2) != 0) {
                return denominazione1.compareTo(denominazione2);
            }
            return o1.getArticolo().getCodice().compareTo(o2.getArticolo().getCodice());
        }
    }

    private class CodiceCategoriaComparator implements Comparator<RimanenzeFinaliDTO> {

        @Override
        public int compare(RimanenzeFinaliDTO o1, RimanenzeFinaliDTO o2) {
            String categoria1 = o1.getArticolo().getCategoria().getCodice();
            String categoria2 = o2.getArticolo().getCategoria().getCodice();
            if (categoria1.compareTo(categoria2) != 0) {
                return categoria1.compareTo(categoria2);
            }

            return o1.getArticolo().getCodice().compareTo(o2.getArticolo().getCodice());
        }
    }

    private class UbicazioneComparator implements Comparator<RimanenzeFinaliDTO> {

        @Override
        public int compare(RimanenzeFinaliDTO o1, RimanenzeFinaliDTO o2) {
            String denominazione1 = o1.getFornitore().getAnagrafica().getDenominazione();
            String denominazione2 = o2.getFornitore().getAnagrafica().getDenominazione();
            if (denominazione1.compareTo(denominazione2) != 0) {
                return denominazione1.compareTo(denominazione2);
            }

            String ubicazione1 = o1.getUbicazioneArticolo() == null ? "" : o1.getUbicazioneArticolo();
            String ubicazione2 = o2.getUbicazioneArticolo() == null ? "" : o2.getUbicazioneArticolo();
            if (ubicazione1.compareTo(ubicazione2) != 0) {
                return ubicazione1.compareTo(ubicazione2);
            }

            return o1.getArticolo().getCodice().compareTo(o2.getArticolo().getCodice());
        }
    }

    private static Logger logger = Logger.getLogger(RimanenzeFinaliManagerBean.class);

    @Resource
    protected SessionContext context;

    @EJB
    protected PanjeaDAO panjeaDAO;

    @EJB
    private LottiManager lottiManager;

    @EJB(beanName = "Panjea.MagazzinoValorizzazioneDepositoManager")
    protected MagazzinoValorizzazioneDepositoManager magazzinoValorizzazioneDepositoManager;

    private Map<String, Comparator<RimanenzeFinaliDTO>> mapComparator;

    {
        mapComparator = new HashMap<String, Comparator<RimanenzeFinaliDTO>>();
        mapComparator.put("A", new CodiceArticoloComparator());
        mapComparator.put("U", new UbicazioneComparator());
        mapComparator.put("C", new CodiceCategoriaComparator());
    }

    /**
     * Calcola le giacenze per deposito e fornitore ( se presente ).
     *
     * @param depositoLite
     *            deposito di riferimento
     * @param data
     *            data di calcolo
     * @param idFornitore
     *            fornitore ( se null tutti )
     * @param idCategoria
     *            categoria ( se null tutti )
     * @param caricaGiacenzeAZero
     *            indica se escludere dal caricamento delle rimanenze gli articoli che non hanno giacenza
     * @return rimanenze
     */
    protected List<RimanenzeFinaliDTO> calcolaGiacenze(DepositoLite depositoLite, Date data, Integer idFornitore,
            Integer idCategoria, boolean caricaGiacenzeAZero) {

        ParametriRicercaValorizzazione parametri = new ParametriRicercaValorizzazione();
        parametri.setConsideraGiacenzaZero(caricaGiacenzeAZero);
        parametri.setData(data);
        if (idCategoria != null) {
            parametri.setTutteCategorie(false);
            Categoria categoria = new Categoria();
            categoria.setId(idCategoria);
            parametri.getCategorie().add(categoria);
        }
        String valQuery = magazzinoValorizzazioneDepositoManager.getSqlString(parametri, depositoLite.getId());

        StringBuilder sb = new StringBuilder();
        sb.append(
                "select giacTbl.qtaInventario+giacTbl.qtaMagazzinoCarico+giacTbl.qtaMagazzinoCaricoAltro-giacTbl.qtaMagazzinoScarico-giacTbl.qtaMagazzinoScaricoAltro as giacenza, ");
        sb.append("              idArticolo as idArticolo, ");
        sb.append("              codiceArticolo as codiceArticolo, ");
        sb.append("              descrizioneArticolo as descrizioneArticolo, ");
        sb.append("              idCategoria as idCategoria, ");
        sb.append("              codiceCategoria as codiceCategoria, ");
        sb.append("              descrizioneCategoria as descrizioneCategoria, ");
        sb.append("              forn.id as idFornitore, ");
        sb.append("              forn.codice as codiceFornitore, ");
        sb.append("              anag.denominazione as denominazioneFornitore, ");
        sb.append("              um.codice as unitaMisura, ");
        sb.append("              idDeposito as idDeposito, ");
        sb.append("              codiceDeposito as codiceDeposito, ");
        sb.append("              descrizioneDeposito as descrizioneDeposito, ");
        sb.append("              sedeAnag.telefono as telefonoFornitore, ");
        sb.append("              sedeAnag.fax as faxFornitore, ");
        sb.append("              art.posizione as ubicazioneArticolo ");
        sb.append("from (");
        sb.append(valQuery);
        sb.append(
                ") giacTbl inner join anag_entita forn on giacTbl.codiceFornitoreAbituale = forn.codice and forn.TIPO_ANAGRAFICA = 'F' ");
        sb.append("                 inner join anag_anagrafica anag on anag.id = forn.anagrafica_id ");
        sb.append("                 inner join anag_sedi_entita sede on sede.entita_id = forn.id ");
        sb.append(
                "                 inner join anag_sedi_anagrafica sedeAnag on sedeAnag.id = sede.sede_anagrafica_id ");
        sb.append("                 inner join maga_articoli art on giacTbl.idArticolo = art.id ");
        sb.append("                 left join anag_unita_misura um on um.id = art.unitaMisura_id ");
        sb.append(createWhereSQL(idFornitore, idCategoria, caricaGiacenzeAZero));
        sb.append(" order by anag.denominazione,codiceArticolo ");

        QueryImpl queryGiacenza = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        SQLQuery sqlQuery = ((SQLQuery) queryGiacenza.getHibernateQuery());
        sqlQuery.setResultTransformer(Transformers.aliasToBean((RimanenzeFinaliDTO.class)));

        String[] scalar = new String[] { "giacenza", "idArticolo", "codiceArticolo", "descrizioneArticolo",
                "idCategoria", "codiceCategoria", "descrizioneCategoria", "idFornitore", "codiceFornitore",
                "denominazioneFornitore", "unitaMisura", "idDeposito", "codiceDeposito", "descrizioneDeposito",
                "telefonoFornitore", "faxFornitore", "ubicazioneArticolo" };
        for (int i = 0; i < scalar.length; i++) {
            sqlQuery.addScalar(scalar[i]);
        }
        @SuppressWarnings("unchecked")
        List<RimanenzeFinaliDTO> results = queryGiacenza.getResultList();

        return results;

    }

    /**
     * Carica la situazione dei lotti alla data indicata.
     *
     * @param deposito
     *            deposito di riferimento
     * @param data
     *            data di riferimento
     * @return situazione lotti
     */
    private Map<ArticoloLite, List<Lotto>> caricaLottiArticoli(DepositoLite deposito, Date data) {

        Map<ArticoloLite, List<Lotto>> mapResult = new HashMap<ArticoloLite, List<Lotto>>();

        List<StatisticaLotto> situazioneLotti = lottiManager.caricaSituazioneLotti(null, deposito, data, null,
                RimanenzaLotto.TUTTE, null, null, null);
        for (StatisticaLotto statisticaLotto : situazioneLotti) {
            if (statisticaLotto.getRimanenza().doubleValue() > 0) {
                List<Lotto> lotti = mapResult.get(statisticaLotto.getArticolo());
                if (lotti == null) {
                    lotti = new ArrayList<Lotto>();
                }
                Lotto lotto = statisticaLotto.getLotto();
                BigDecimal val = BigDecimal.valueOf(statisticaLotto.getRimanenza())
                        .setScale(lotto.getArticolo().getNumeroDecimaliQta(), RoundingMode.HALF_UP);
                lotto.setRimanenza(val.doubleValue());
                lotti.add(lotto);
                mapResult.put(statisticaLotto.getArticolo(), lotti);
            }
        }

        return mapResult;
    }

    @Override
    public List<RimanenzeFinaliDTO> caricaRimanenzeFinali(Date data, Integer idFornitore, Integer idDeposito,
            Integer idCategoria, boolean caricaGiacenzeAZero, String ordinamento) {

        List<RimanenzeFinaliDTO> rimanenzeResult = new ArrayList<RimanenzeFinaliDTO>();
        DepositoLite deposito = new DepositoLite();
        deposito.setId(idDeposito);

        try {
            // calcolo giacenze, vendite mensili e annuali
            List<RimanenzeFinaliDTO> rimanenzeGiacenzeEVendite = calcolaGiacenze(deposito, data, idFornitore,
                    idCategoria, caricaGiacenzeAZero);

            // calcolo le vendite mensili e annuali
            Map<Integer, Double[]> mapVendite = caricaVendite(deposito, data, caricaGiacenzeAZero);

            // calcolo le giacenze dei lotti
            Map<ArticoloLite, List<Lotto>> lottiArticoli = caricaLottiArticoli(deposito, data);

            for (RimanenzeFinaliDTO rimanenzeFinaliDTO : rimanenzeGiacenzeEVendite) {
                // agiungo le vendite
                Double[] vendite = mapVendite.get(rimanenzeFinaliDTO.getArticolo().getId());
                if (vendite != null) {
                    rimanenzeFinaliDTO.setQtaScaricoVenditaMese(vendite[0]);
                    rimanenzeFinaliDTO.setQtaScaricoVenditaAnno(vendite[1]);
                    rimanenzeFinaliDTO.setQtaScaricoAltroVenditaAnno(vendite[2]);
                    rimanenzeFinaliDTO.setQtaCaricoAltroVenditaAnno(vendite[3]);
                }

                // aggiungo i lotti
                List<Lotto> lotti = lottiArticoli.get(rimanenzeFinaliDTO.getArticolo());
                if (lotti == null) {
                    rimanenzeResult.add(rimanenzeFinaliDTO);
                } else {
                    // inserisco 1 rimanenza per ogni lotto presente
                    boolean pulisciValori = false;
                    for (Lotto lotto : lotti) {
                        RimanenzeFinaliDTO rimanenzaFinale;
                        try {
                            rimanenzaFinale = (RimanenzeFinaliDTO) rimanenzeFinaliDTO.clone();
                        } catch (CloneNotSupportedException e) {
                            logger.error("--> errore durante il clone della rimanenza", e);
                            throw new RuntimeException("errore durante il clone della rimanenza", e);
                        }
                        rimanenzaFinale.setLotto(lotto);
                        if (pulisciValori) {
                            rimanenzaFinale.setUnitaMisura(null);
                            rimanenzaFinale.setGiacenza(null);
                            rimanenzaFinale.setQtaScaricoVenditaMese(null);
                            rimanenzaFinale.setQtaCaricoAltroVenditaAnno(null);
                            rimanenzaFinale.setQtaScaricoVenditaAnno(null);
                            rimanenzaFinale.setQtaScaricoAltroVenditaAnno(null);
                            rimanenzaFinale.setUbicazioneArticolo(null);
                        }
                        rimanenzeResult.add(rimanenzaFinale);
                        pulisciValori = true;
                    }
                }
            }

            Collections.sort(rimanenzeResult, mapComparator.get(ordinamento));

        } catch (Exception e) {
            logger.error("Errore in caricaRimanenzeFinali ", e);
            throw new RuntimeException(e);
        }

        return rimanenzeResult;
    }

    /**
     * Carica le vendite degli articoli.
     *
     * @param depositoLite
     *            deposito di riferimento
     * @param data
     *            data
     * @param caricaGiacenzeAZero
     *            caricaGiacenzeAZero
     * @return mappa contenente come chiave l'id dell'articolo e valore la lista delle vendite ( mese, anno, scarico
     *         altro anno, carico altro anno )
     */
    protected Map<Integer, Double[]> caricaVendite(DepositoLite depositoLite, Date data, boolean caricaGiacenzeAZero) {

        GiacenzaQueryBuilder giacenzaQueryBuilder = new GiacenzaQueryBuilder();
        String giacQuery = giacenzaQueryBuilder.getSqlString(new ArrayList<Integer>(), depositoLite.getId(), data);

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("mov.articolo_id as idArticolo, ");
        sb.append(
                "coalesce(sum(if((mov.dataRegistrazione >= :paramInizioMese and mov.dataRegistrazione <= :paramData),mov.qtaMagazzinoScarico,0)),0) as qtaScaricoVenditaMese,");
        sb.append("coalesce(sum(mov.qtaMagazzinoScarico),0) as qtaScaricoVenditaAnno, ");
        sb.append("coalesce(sum(mov.qtaMagazzinoScaricoAltro),0) as qtaScaricoAltroVenditaAnno, ");
        sb.append("coalesce(sum(mov.qtaMagazzinoCaricoAltro),0) as qtaCaricoAltroVenditaAnno ");
        sb.append("from dw_movimentimagazzino mov ");
        sb.append(
                " where mov.deposito_id = :idDeposito and (mov.dataRegistrazione >= :paramInizioAnno and mov.dataRegistrazione <= :paramData) ");
        sb.append("and mov.articolo_id in (select idArticolo from( ");
        sb.append(giacQuery);
        sb.append(" ) as giac ");
        if (!caricaGiacenzeAZero) {
            sb.append(" where giac.giacenza <> 0 ");
        }
        sb.append(" ) ");
        sb.append(" group by mov.articolo_id ");
        sb.append("order by null");

        QueryImpl queryGiacenza = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
        SQLQuery sqlQuery = ((SQLQuery) queryGiacenza.getHibernateQuery());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        sqlQuery.setParameter("paramInizioMese", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        calendar.set(calendar.get(Calendar.YEAR), 0, 1, 0, 0, 0);
        sqlQuery.setParameter("paramInizioAnno", new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        sqlQuery.setParameter("paramData", new SimpleDateFormat("yyyy-MM-dd").format(data));
        sqlQuery.setParameter("idDeposito", depositoLite.getId());
        sqlQuery.addScalar("idArticolo");
        sqlQuery.addScalar("qtaScaricoVenditaMese");
        sqlQuery.addScalar("qtaScaricoVenditaAnno");
        sqlQuery.addScalar("qtaScaricoAltroVenditaAnno");
        sqlQuery.addScalar("qtaCaricoAltroVenditaAnno");

        Map<Integer, Double[]> venditeMap = new HashMap<Integer, Double[]>();

        @SuppressWarnings("unchecked")
        List<Object[]> results = queryGiacenza.getResultList();
        for (Object[] object : results) {
            Integer idArticolo = (Integer) object[0];
            Double[] qta = new Double[4];
            qta[0] = (Double) object[1];
            qta[1] = (Double) object[2];
            qta[2] = (Double) object[3];
            qta[3] = (Double) object[4];
            venditeMap.put(idArticolo, qta);
        }

        return venditeMap;
    }

    /**
     * Costruisce la where per i parametri di riferimento.
     *
     * @param idFornitore
     *            id fornitore
     * @param idCategoria
     *            id categoria
     * @param caricaGiacenzeAZero
     *            carica giacenze a zero
     * @return where creata
     */
    protected String createWhereSQL(Integer idFornitore, Integer idCategoria, boolean caricaGiacenzeAZero) {
        StringBuilder sb = new StringBuilder();
        String tmp = "where";
        if (idFornitore != null) {
            sb.append(tmp);
            sb.append(" forn.id = " + idFornitore);
            tmp = " and ";
        }
        if (idCategoria != null) {
            sb.append(tmp);
            sb.append(" idCategoria = " + idCategoria);
            tmp = " and ";
        }
        // if (!caricaGiacenzeAZero) {
        // sb.append(tmp);
        // sb.append(" giacenza <> 0 ");
        // }
        return sb.toString();
    }

}
