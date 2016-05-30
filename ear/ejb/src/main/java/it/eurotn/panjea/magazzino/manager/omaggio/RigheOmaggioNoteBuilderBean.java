package it.eurotn.panjea.magazzino.manager.omaggio;

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
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoSettingsManager;
import it.eurotn.panjea.magazzino.manager.omaggio.interfaces.RigheOmaggioNoteBuilder;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.RigheOmaggioNoteBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheOmaggioNoteBuilder")
public class RigheOmaggioNoteBuilderBean implements RigheOmaggioNoteBuilder {

    private static final Logger LOGGER = Logger.getLogger(RigheOmaggioNoteBuilderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    @EJB
    private MagazzinoSettingsManager magazzinoSettingsManager;

    private boolean checkGeneraRigaOmaggio(AreaMagazzino areaMagazzino) {

        String descrizioneRiga = magazzinoSettingsManager.caricaMagazzinoSettings()
                .getDescrizioneNotaAutomaticaOmaggio();
        if (StringUtils.isBlank(descrizioneRiga)) {
            return false;
        }

        return getRigheOmaggioAreaMagazzino(areaMagazzino) > 0;
    }

    @Override
    public List<RigaMagazzino> generaRigheArticolo(AreaMagazzino areaMagazzino) {
        List<RigaMagazzino> righe = new ArrayList<>();

        if (checkGeneraRigaOmaggio(areaMagazzino)) {
            RigaNota rigaNota = new RigaNota();
            rigaNota.setAreaMagazzino(areaMagazzino);
            String descrizioneRiga = magazzinoSettingsManager.caricaMagazzinoSettings()
                    .getDescrizioneNotaAutomaticaOmaggio();
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

    private int getRigheOmaggioAreaMagazzino(AreaMagazzino areaMagazzino) {
        int result = 0;

        StringBuilder sb = new StringBuilder(200);
        sb.append("select count(ra.id) ");
        sb.append("from RigaArticolo ra inner join ra.areaMagazzino am ");
        sb.append("inner join am.tipoAreaMagazzino tam ");
        sb.append("where tipoOmaggio > 0 ");
        sb.append(" and tam.valoriFatturato = true ");
        sb.append(" and am = :paramAreaMagazzino ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramAreaMagazzino", areaMagazzino);
        try {
            result = ((Long) panjeaDAO.getSingleResult(query)).intValue();
        } catch (Exception e) {
            LOGGER.error("--> errore durante la verifica delle righe somministrazione", e);
            throw new GenericException("errore durante la verifica delle righe somministrazione", e);
        }

        return result;
    }

}
