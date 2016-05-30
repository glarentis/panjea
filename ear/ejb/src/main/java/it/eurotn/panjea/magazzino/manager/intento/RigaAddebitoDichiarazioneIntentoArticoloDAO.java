package it.eurotn.panjea.magazzino.manager.intento;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.intento.RigaAddebitoDichiarazioneIntentoArticolo;
import it.eurotn.panjea.magazzino.manager.documento.RigaMagazzinoAbstractDAOBean;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoDAO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;

/**
 * @author leonardo
 */
@Stateless(mappedName = "Panjea.RigaAddebitoDichiarazioneIntentoArticoloDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigaAddebitoDichiarazioneIntentoArticoloDAO")
public class RigaAddebitoDichiarazioneIntentoArticoloDAO extends RigaMagazzinoAbstractDAOBean
        implements RigaMagazzinoDAO {

    @Override
    public RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo) {
        RigaAddebitoDichiarazioneIntentoArticolo rigaDichiarazione = (RigaAddebitoDichiarazioneIntentoArticolo) rigaDocumentoManager
                .creaRigaArticoloDocumento(new RigaAddebitoDichiarazioneIntentoArticolo(),
                        parametriCreazioneRigaArticolo);

        // La riga di addebito deve avere sempre il codice iva dell'articolo quindi dopo aver creato la riga standard
        // cambio il codice iva. (potrebbe non essere quello dell'articolo per via dello split payment, esenzione, ecc)
        Articolo articolo;
        try {
            articolo = panjeaDAO.load(Articolo.class, parametriCreazioneRigaArticolo.getIdArticolo());
        } catch (ObjectNotFoundException e) {
            logger.error("--> articolo non trovato: idArticolo " + parametriCreazioneRigaArticolo.getIdArticolo(), e);
            throw new RuntimeException(e);
        }
        rigaDichiarazione.setCodiceIva(articolo.getCodiceIva());

        return rigaDichiarazione;
    }

}
