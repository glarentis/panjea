package it.eurotn.panjea.magazzino.manager.documento.generatorerighe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.conai.manager.interfaces.RigheConaiBuilder;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.IGeneratoreRigheArticolo;
import it.eurotn.panjea.magazzino.manager.documento.generatorerighe.interfaces.RigheArticoloBuilderFactory;
import it.eurotn.panjea.magazzino.manager.intento.interfaces.RigheAddebitoDichiarazioneIntentoBuilder;
import it.eurotn.panjea.magazzino.manager.omaggio.interfaces.RigheOmaggioBuilder;
import it.eurotn.panjea.magazzino.manager.omaggio.interfaces.RigheOmaggioNoteBuilder;
import it.eurotn.panjea.magazzino.manager.somministrazione.interfaces.RigheSomministrazioneBuilder;
import it.eurotn.panjea.magazzino.manager.trasporto.interfaces.RigheSpeseTrasportoBuilder;

/**
 * Factory che ritorna la lista di generatori di righe esistenti.
 *
 * @author leonardo
 */
@Stateless(name = "Panjea.RigheArticoloBuilderFactory")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheArticoloBuilderFactory")
public class RigheArticoloBuilderFactoryBean implements RigheArticoloBuilderFactory {

    public enum EGeneratoreRiga {
        ALL, CONAI, OMAGGIO, SPESE_TRASPORTO, ADDEBITO_DICHIARAZIONE_INTENTO, FATTURAZIONE
    }

    @EJB
    private RigheConaiBuilder generatoreRigheConaiManager;

    @EJB
    private RigheOmaggioBuilder generatoreRigheOmaggioManager;

    @EJB
    private RigheSpeseTrasportoBuilder generatoreRigheSpeseTrasportoManager;

    @EJB
    private RigheAddebitoDichiarazioneIntentoBuilder generatoreRigheAddebitoDichiarazioneIntentoBuilder;

    @EJB
    private RigheSomministrazioneBuilder generatoreRigheSomministrazioneBuilder;

    @EJB
    private RigheOmaggioNoteBuilder generatoreRigheOmaggioNoteBuilder;

    /**
     * Crea la lista di generatori righe disponibili.
     *
     * @param generatoreRiga
     *            il generatore richiesto o tutti
     * @return la lista di generatori righe presenti
     */
    @Override
    public List<IGeneratoreRigheArticolo> creaGeneratoriRigheArticolo(EGeneratoreRiga generatoreRiga) {
        List<IGeneratoreRigheArticolo> generatori = new ArrayList<IGeneratoreRigheArticolo>();

        if (generatoreRiga == null) {
            return generatori;
        }

        switch (generatoreRiga) {
        case ADDEBITO_DICHIARAZIONE_INTENTO:
            generatori.add(generatoreRigheAddebitoDichiarazioneIntentoBuilder);
            break;
        case SPESE_TRASPORTO:
            generatori.add(generatoreRigheSpeseTrasportoManager);
            break;
        case CONAI:
            generatori.add(generatoreRigheConaiManager);
            break;
        case OMAGGIO:
            generatori.add(generatoreRigheOmaggioManager);
            break;
        case FATTURAZIONE:
            generatori.add(generatoreRigheSpeseTrasportoManager);
            generatori.add(generatoreRigheConaiManager);
            generatori.add(generatoreRigheOmaggioManager);
            generatori.add(generatoreRigheOmaggioNoteBuilder);
            generatori.add(generatoreRigheSomministrazioneBuilder);
            break;
        case ALL:
            generatori.add(generatoreRigheAddebitoDichiarazioneIntentoBuilder);
            generatori.add(generatoreRigheSpeseTrasportoManager);
            generatori.add(generatoreRigheConaiManager);
            generatori.add(generatoreRigheOmaggioManager);
            generatori.add(generatoreRigheOmaggioNoteBuilder);
            generatori.add(generatoreRigheSomministrazioneBuilder);
            break;
        default:
            break;
        }

        return generatori;
    }

}
