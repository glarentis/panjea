package it.eurotn.panjea.magazzino.manager.intento;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.iva.util.IImponibiliIvaQueryExecutor;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.StrategiaTotalizzazione;
import it.eurotn.panjea.magazzino.manager.documento.totalizzatore.Totalizzatore;
import it.eurotn.panjea.magazzino.manager.intento.interfaces.DichiarazioneIntentoManager;
import it.eurotn.panjea.magazzino.manager.intento.interfaces.RigheAddebitoDichiarazioneIntentoBuilder;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.util.queryExecutor.ITotalizzatoriQueryExecutor;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author leonardo
 */
@Stateless(name = "Panjea.RigheAddebitoDichiarazioneIntentoBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheAddebitoDichiarazioneIntentoBuilder")
public class RigheAddebitoDichiarazioneIntentoBuilderBean implements RigheAddebitoDichiarazioneIntentoBuilder {

    private static final Logger LOGGER = Logger.getLogger(RigheAddebitoDichiarazioneIntentoBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    @EJB
    @IgnoreDependency
    private IImponibiliIvaQueryExecutor imponibiliIvaQueryExecutor;

    @EJB
    @IgnoreDependency
    private ITotalizzatoriQueryExecutor totalizzatoriQueryExecutor;

    @EJB
    @IgnoreDependency
    private StrategiaTotalizzazione strategiaTotalizzazione;

    @EJB
    private AreaIvaManager areaIvaManager;

    @EJB
    private DichiarazioneIntentoManager dichiarazioneIntentoManager;

    /**
     * Verifica se per l'area magazzino di riferimento debba essere creata la riga di addebito. Le condizioni per le
     * quali una riga deve essere creata sono:<br>
     * <ul>
     * <li>L'area rientra nel range della dichiarazione di intento e genera fatturato</li>
     * <li>La sede ha configurato l'addebito</li>
     * <li>L'area possiede delle righe magazzino proprie ( non importate da altre aree tipo evasione o fatturazione )
     * </li>
     * <li>L'importo del documento è superiore a quanto configurato nelle settings del magazzino</li>
     * </ul>
     *
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @param magazzinoSettings
     *            settings del magazzino
     * @return <code>true</code> se si deve creare la riga di addebito
     */
    private boolean checkGenerazioneRigaAddebito(AreaMagazzino areaMagazzino, MagazzinoSettings magazzinoSettings) {
        boolean generaRiga = false;
        boolean addebitoDicIntento = false;

        // trasferimenti, carichi prod, ecc non hanno sede entità
        if (areaMagazzino.getDocumento().getSedeEntita() != null
                && areaMagazzino.getDocumento().getSedeEntita().getSedeMagazzino() != null) {
            addebitoDicIntento = areaMagazzino.getDocumento().getSedeEntita().getSedeMagazzino()
                    .getDichiarazioneIntento().isAddebito();
        }
        generaRiga = areaMagazzino.isDichiarazioneIntento() && addebitoDicIntento;

        // Verifico se l'area ha righe proprie o righe proveniente da ordini (l'ordine non genera la
        // riga di addebito). Uso un query nativa per prestazioni. Controllo subito se generaRiga =
        // true perchè se non lo fosse mi risparmio query inutili
        if (generaRiga) {
            StringBuilder sb = new StringBuilder(200);
            sb.append("select count(id) from maga_righe_magazzino where areaMagazzino_id = ");
            sb.append(areaMagazzino.getId());
            sb.append(" and areaMagazzinoCollegata_id is null and TIPO_RIGA not in ('N', 'T')");

            BigInteger countRigheAM = (BigInteger) panjeaDAO.prepareNativeQuery(sb.toString()).uniqueResult();

            generaRiga = generaRiga && countRigheAM.intValue() > 0;

            // controllo se il totale del documento supera la soglia configurata nelle settings del
            // magzzino
            if (generaRiga) {
                imponibiliIvaQueryExecutor.setAreaDocumento(areaMagazzino);
                imponibiliIvaQueryExecutor.setQueryString("RigaArticolo.caricaImponibiliIva");
                totalizzatoriQueryExecutor.setAreaDocumento(areaMagazzino);
                totalizzatoriQueryExecutor.setQueryString("RigaArticolo.caricaByTipo");

                Totalizzatore totalizzatore = strategiaTotalizzazione
                        .getTotalizzatore(areaMagazzino.getTipoAreaMagazzino().getStrategiaTotalizzazioneDocumento());
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(areaMagazzino);

                List<RigaIva> righeIva = areaIvaManager.generaRigheIva(imponibiliIvaQueryExecutor,
                        areaMagazzino.getDocumento(), areaMagazzino.isAddebitoSpeseIncasso(), null, null);
                Documento documentoTotalizzato = totalizzatore.totalizzaDocumento(areaMagazzino.getDocumento(),
                        areaMagazzino.getTotaliArea(), totalizzatoriQueryExecutor, righeIva);
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(documentoTotalizzato);

                BigDecimal sogliaAddebito = magazzinoSettings.getSogliaAddebitoDichiarazioneInVigore() != null
                        ? magazzinoSettings.getSogliaAddebitoDichiarazioneInVigore().getPrezzo() : BigDecimal.ZERO;

                generaRiga = generaRiga
                        && documentoTotalizzato.getTotale().getImportoInValutaAzienda().compareTo(sogliaAddebito) > 0;
            }
        }

        return generaRiga;
    }

    @Override
    public List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino) {
        List<RigaMagazzino> righeGenerate = new ArrayList<>();

        try {
            areaMagazzino = panjeaDAO.load(AreaMagazzino.class, areaMagazzino.getId());
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dell'area magazzino " + areaMagazzino.getId(), e);
            throw new RuntimeException("Errore durante il caricamento dell'area magazzino " + areaMagazzino.getId(), e);
        }

        org.hibernate.Session session = (org.hibernate.Session) panjeaDAO.getEntityManager().getDelegate();
        session.enableFilter(MagazzinoSettings.ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE)
                .setParameter("paramDataRegistrazione", areaMagazzino.getDataRegistrazione());
        MagazzinoSettings magazzinoSettings = null;
        try {
            magazzinoSettings = magazzinoSettingsManager.caricaMagazzinoSettings();
        } finally {
            session.disableFilter(MagazzinoSettings.ADDEBITO_DICHIARAZIONE_INTENTO_IN_VIGORE);
        }

        if (checkGenerazioneRigaAddebito(areaMagazzino, magazzinoSettings)) {
            righeGenerate = dichiarazioneIntentoManager.generaRigheArticolo(areaMagazzino);
        }
        return righeGenerate;
    }

}
