package it.eurotn.panjea.cosaro.rendicontazione;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.cosaro.CosaroSettingsBean;
import it.eurotn.panjea.cosaro.RigaFileCoop;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.manager.export.exporter.interfaces.RendicontazioneExporter;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

public abstract class AbstractCoopRendicontazioneExporterBean implements RendicontazioneExporter {

    private static Logger logger = Logger.getLogger(AbstractCoopRendicontazioneExporterBean.class);

    @EJB
    protected PanjeaDAO panjeaDAO;

    /**
     *
     * @param righeMagazzino
     *            .
     * @param fileDaEsportare
     *            .
     */
    private void aggiornaRigaFile(List<Object[]> righeMagazzino, Set<String> fileDaEsportare) {
        for (Object[] objects : righeMagazzino) {
            RigaArticolo rigaArticolo = (RigaArticolo) objects[0];
            RigaFileCoop rigaFileCoop = (RigaFileCoop) objects[1];
            // Refresh per ricaricare un eventuale riga modificata
            try {
                panjeaDAO.load(RigaFileCoop.class, rigaFileCoop.getId());
                fileDaEsportare.add(rigaFileCoop.getUuid());
                rigaFileCoop.setNumeroBolla(rigaArticolo.getAreaMagazzino().getDocumento().getCodice().getCodice());
                rigaFileCoop.setDataBolla(new SimpleDateFormat("yyMMdd")
                        .format(rigaArticolo.getAreaMagazzino().getDocumento().getDataDocumento()));
                rigaFileCoop.setQta(rigaFileCoop.getQta() + rigaArticolo.getQtaMagazzino());
                rigaFileCoop.setPezziEvasi("0");
                rigaFileCoop.setColliEvasi("0");
                if (!"0".equals(rigaArticolo.getAttributo(CosaroSettingsBean.PEZZI_ATTRIBUTO).getValore())) {
                    rigaFileCoop
                            .setPezziEvasi(rigaArticolo.getAttributo(CosaroSettingsBean.PEZZI_ATTRIBUTO).getValore());
                }
                if (!"0".equals(rigaArticolo.getAttributo(CosaroSettingsBean.COLLI_ATTRIBUTO).getValore())) {
                    rigaFileCoop
                            .setColliEvasi(rigaArticolo.getAttributo(CosaroSettingsBean.COLLI_ATTRIBUTO).getValore());
                }
                if (rigaFileCoop.getLottoFornitore() == null && rigaArticolo.getRigheLotto().size() > 0) {
                    // associo il primo lotto della riga e lo associo. Gli altri lotti sono sempre
                    // uguali.
                    rigaFileCoop
                            .setLottoFornitore(rigaArticolo.getRigheLotto().iterator().next().getLotto().getCodice());
                }
                AttributoArticolo attributoMoltiplicatore = rigaArticolo.getArticolo()
                        .getAttributo(CosaroSettingsBean.TARA_CARTONE_ATTRIBUTO);
                if (attributoMoltiplicatore != null) {
                    rigaFileCoop.setMoltTara(0);
                    try {
                        double moltiplicatore = Double
                                .parseDouble(attributoMoltiplicatore.getValore().replace(',', '.'));
                        rigaFileCoop.setMoltTara(moltiplicatore);
                    } catch (Exception e) {
                        rigaFileCoop.setMoltTara(0);
                    }
                }
                panjeaDAO.save(rigaFileCoop);
            } catch (ObjectNotFoundException e) {
                e.printStackTrace();
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * aggiorna la testata salvata durante l'importazione.
     *
     * @param rigaFileCoop
     *            .
     * @param rigaArticolo
     *            .
     */
    @SuppressWarnings("unchecked")
    private void aggiornaTestata(RigaFileCoop rigaFileCoop, RigaArticolo rigaArticolo) {
        try {
            StringBuilder sb = new StringBuilder("select r from RigaFileCoop r where r.uuid=:uuid and r.tipoRiga='T'");
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("uuid", rigaFileCoop.getUuid());
            List<RigaFileCoop> result = panjeaDAO.getResultList(query);
            RigaFileCoop testata = result.get(0);
            testata.setNumeroBolla(rigaArticolo.getAreaMagazzino().getDocumento().getCodice().getCodice());
            testata.setDataBolla(new SimpleDateFormat("yyMMdd")
                    .format(rigaArticolo.getAreaMagazzino().getDocumento().getDataDocumento()));
            panjeaDAO.save(testata);
        } catch (Exception e) {
            logger.error("-->errore nell'aggiornare la testata dei file importati", e);
        }
    }

    private Query getRigheMagazzinoColliQuery() {

        StringBuilder sb = new StringBuilder();
        sb.append("select rm,rc from AreaMagazzino am ");
        sb.append("inner join am.righeMagazzino rm ");
        sb.append("inner join rm.rigaOrdineCollegata ro ");
        sb.append("inner join ro.areaOrdine ao,RigaFileCoop rc ");
        sb.append("where rc.numeroOrdine=ao.riferimentiOrdine.numeroOrdine ");
        sb.append("and rc.dataOrdine=ao.riferimentiOrdine.dataOrdine ");
        sb.append("and rc.numeroRigaColli=ro.numeroRiga ");
        sb.append("and am.id=:idAreaMagazzino ");
        Query queryColli = panjeaDAO.prepareQuery(sb.toString());

        return queryColli;
    }

    private Query getRigheMagazzinoPezziQuery() {

        StringBuilder sb = new StringBuilder();
        sb.append("select rm,rc from AreaMagazzino am ");
        sb.append("inner join am.righeMagazzino rm ");
        sb.append("inner join rm.rigaOrdineCollegata ro ");
        sb.append("inner join ro.areaOrdine ao,RigaFileCoop rc ");
        sb.append("where rc.numeroOrdine=ao.riferimentiOrdine.numeroOrdine ");
        sb.append("and rc.dataOrdine=ao.riferimentiOrdine.dataOrdine ");
        sb.append("and rc.numeroRigaPezzi=ro.numeroRiga ");
        sb.append("and am.id=:idAreaMagazzino ");
        Query queryPezzi = panjeaDAO.prepareQuery(sb.toString());

        return queryPezzi;
    }

    @SuppressWarnings("unchecked")
    protected Set<String> getRigheToExport(List<AreaMagazzinoRicerca> areeMagazzino) {
        // per ogni riga delle area magazzino recupero la riga del file di importazione e le ordino
        // in base al numero
        // riga del file di importazione.
        Query queryColli = getRigheMagazzinoColliQuery();
        Query queryPezzi = getRigheMagazzinoPezziQuery();

        Set<String> uuid = new HashSet<String>();

        // Se per caso esportassi 2 volte aggiorno tutte le qta sulle righe impportate per evitare
        // di sommare ad una qta
        // di una precedente esportazione
        Query query = panjeaDAO.getEntityManager().createNativeQuery("update cosa_riga_file_coop set qta=0");
        query.executeUpdate();
        for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {
            // Carico le righe magazzino e le righe del file coop
            queryColli.setParameter("idAreaMagazzino", areaMagazzinoRicerca.getIdAreaMagazzino());
            queryPezzi.setParameter("idAreaMagazzino", areaMagazzinoRicerca.getIdAreaMagazzino());
            try {
                List<Object[]> righeMagazzinoColli = panjeaDAO.getResultList(queryColli);
                aggiornaRigaFile(righeMagazzinoColli, uuid);

                List<Object[]> righeMagazzinoPezzi = panjeaDAO.getResultList(queryPezzi);
                aggiornaRigaFile(righeMagazzinoPezzi, uuid);

                // Aggiorno la testata recuperando una riga
                if (righeMagazzinoColli.size() > 0) {
                    aggiornaTestata((RigaFileCoop) righeMagazzinoColli.get(0)[1],
                            (RigaArticolo) righeMagazzinoColli.get(0)[0]);
                } else if (righeMagazzinoPezzi.size() > 0) {
                    aggiornaTestata((RigaFileCoop) righeMagazzinoPezzi.get(0)[0],
                            (RigaArticolo) righeMagazzinoPezzi.get(0)[1]);
                }
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }

        return uuid;
    }

}
