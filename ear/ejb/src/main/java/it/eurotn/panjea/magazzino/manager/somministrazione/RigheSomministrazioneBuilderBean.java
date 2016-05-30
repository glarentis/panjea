package it.eurotn.panjea.magazzino.manager.somministrazione;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaNota;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.somministrazione.interfaces.RigheSomministrazioneBuilder;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.RigheSomministrazioneBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheSomministrazioneBuilder")
public class RigheSomministrazioneBuilderBean implements RigheSomministrazioneBuilder {

    private static final Logger LOGGER = Logger.getLogger(RigheSomministrazioneBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    /**
     * Verifica se deve essere creata la riga automatica per la somministrazione.<br>
     * Genero la riga nota di somministrazione solo in questi 2 casi:<br>
     * 1. l'area magazzino genera fatturato, la tipologia iva alternativa è SOMMINISTRAZIONE e almeno una sua riga ha un
     * articolo con flag somministrazione ( caso della fattura accompagnatoria ).<br>
     * 2. una delle sue aree collegate rispetta le condizioni del punto 1 ( caso della fattura differita ).
     *
     * @param areaMagazzino
     *            area magazzino
     * @return <code>true</code> se deve essere generata la riga automatica di somministrazione
     */
    private boolean checkGeneraRigaSomministrazione(AreaMagazzino areaMagazzino) {

        String descrizioneRiga = magazzinoSettingsManager.caricaMagazzinoSettings()
                .getDescrizioneNotaAutomaticaSomministrazione();
        if (StringUtils.isBlank(descrizioneRiga)) {
            return false;
        }

        // Caso di una fattura accompagnatoria
        int result = getRigheSomministrazioneAreaMagazzino(areaMagazzino);

        // Caso di una fattura differita
        result += getRigheSomministrazioneAreeCollegate(areaMagazzino);

        return result > 0;
    }

    @Override
    public List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino) {
        List<RigaMagazzino> righe = new ArrayList<>();

        if (checkGeneraRigaSomministrazione(areaMagazzino)) {
            RigaNota rigaNota = new RigaNota();
            rigaNota.setAreaMagazzino(areaMagazzino);
            String descrizioneRiga = magazzinoSettingsManager.caricaMagazzinoSettings()
                    .getDescrizioneNotaAutomaticaSomministrazione();
            rigaNota.setNota(descrizioneRiga);
            rigaNota.setRigaAutomatica(true);

            try {
                rigaNota = (RigaNota) rigaMagazzinoManager.getDao(rigaNota).salvaRigaMagazzino(rigaNota);
                righe.add(rigaNota);
            } catch (RimanenzaLottiNonValidaException e) {
                // Non verrà mai rilanciata perchè stò salvando una riga nota
                throw new GenericException(e);
            } catch (RigheLottiNonValideException e) {
                // Non verrà mai rilanciata perchè stò salvando una riga nota
                throw new GenericException(e);
            } catch (QtaLottiMaggioreException e) {
                // Non verrà mai rilanciata perchè stò salvando una riga nota
                throw new GenericException(e);
            }
        }
        return righe;
    }

    private int getRigheSomministrazioneAreaMagazzino(AreaMagazzino areaMagazzino) {
        int result = 0;

        StringBuilder sb = new StringBuilder(200);
        sb.append("select count(ra.id) ");
        sb.append("from RigaArticolo ra inner join ra.areaMagazzino am ");
        sb.append("inner join am.tipoAreaMagazzino tam ");
        sb.append("where am.tipologiaCodiceIvaAlternativo =:paramTipoIva ");
        sb.append(" and ra.somministrazione = true ");
        sb.append(" and tam.valoriFatturato = true ");
        sb.append(" and am = :paramAreaMagazzino ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramAreaMagazzino", areaMagazzino);
        query.setParameter("paramTipoIva", ETipologiaCodiceIvaAlternativo.SOMMINISTRAZIONE);
        try {
            result = ((Long) panjeaDAO.getSingleResult(query)).intValue();
        } catch (Exception e) {
            LOGGER.error("--> errore durante la verifica delle righe somministrazione", e);
            throw new GenericException("errore durante la verifica delle righe somministrazione", e);
        }

        return result;
    }

    private int getRigheSomministrazioneAreeCollegate(AreaMagazzino areaMagazzino) {
        int result = 0;

        StringBuilder sb = new StringBuilder(200);
        sb.append("select count(rigaDDT.id) ");
        sb.append("from RigaArticolo ra inner join ra.areaMagazzino am ");
        sb.append("inner join am.tipoAreaMagazzino tam ");
        sb.append("inner join ra.rigaMagazzinoCollegata rigaDDT ");
        sb.append("inner join rigaDDT.areaMagazzino amDDT ");
        sb.append("where amDDT.tipologiaCodiceIvaAlternativo = :paramTipoIva ");
        sb.append(" and rigaDDT.somministrazione = true ");
        sb.append(" and tam.valoriFatturato = true ");
        sb.append(" and am = :paramAreaMagazzino ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramAreaMagazzino", areaMagazzino);
        query.setParameter("paramTipoIva", ETipologiaCodiceIvaAlternativo.SOMMINISTRAZIONE);
        try {
            result = ((Long) panjeaDAO.getSingleResult(query)).intValue();
        } catch (Exception e) {
            LOGGER.error("--> errore durante la verifica delle righe somministrazione", e);
            throw new GenericException("errore durante la verifica delle righe somministrazione", e);
        }

        return result;
    }

}
