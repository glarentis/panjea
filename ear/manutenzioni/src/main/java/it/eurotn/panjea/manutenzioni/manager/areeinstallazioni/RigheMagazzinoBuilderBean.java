package it.eurotn.panjea.manutenzioni.manager.areeinstallazioni;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.RigaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.rigadocumento.interfaces.RigaDocumentoManager;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.interfaces.RigheMagazzinoBuilder;
import it.eurotn.panjea.manutenzioni.manager.righeinstallazione.interfaces.RigheInstallazioneManager;

@Stateless(name = "Panjea.RigheMagazzinoBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RigheMagazzinoBuilder")
public class RigheMagazzinoBuilderBean implements RigheMagazzinoBuilder {

    @EJB
    private RigaDocumentoManager rigaDocumentoManager;
    @EJB
    private RigheInstallazioneManager righeInstallazioneManager;
    @EJB
    private RigaMagazzinoManager rigaMagazzinoManager;

    private void creaRigaMagazzino(AreaMagazzino areaMagazzino, RigaInstallazione rigaInstallazione, int molt,
            Articolo articolo) {
        ParametriCreazioneRigaArticolo parametri = new ParametriCreazioneRigaArticolo();
        parametri.setCalcolaGiacenza(false);
        parametri.setIdArticolo(articolo.getId());
        Importo imp = new Importo(rigaInstallazione.getAreaInstallazione().getDocumento().getTotale().getCodiceValuta(),
                rigaInstallazione.getAreaInstallazione().getDocumento().getTotale().getTassoDiCambio());
        parametri.setImporto(imp);
        parametri.setTipoMovimento(areaMagazzino.getTipoAreaMagazzino().getTipoMovimento());

        RigaArticolo rigaArticolo = (RigaArticolo) rigaDocumentoManager.creaRigaArticoloDocumento(new RigaArticolo(),
                parametri);
        rigaArticolo.setQta(1.0 * molt);
        rigaArticolo.setQtaMagazzino(1.0 * molt);
        rigaArticolo.setAreaMagazzino(areaMagazzino);
        try {
            rigaMagazzinoManager.getDao(rigaArticolo).salvaRigaMagazzino(rigaArticolo);
        } catch (RimanenzaLottiNonValidaException | RigheLottiNonValideException | QtaLottiMaggioreException e) {
            throw new GenericException("Errore non previsto", e);
        }
    }

    @Override
    public void creaRigheMagazzino(AreaInstallazione ai, AreaMagazzino areaMagazzino) {
        List<RigaInstallazione> righe = righeInstallazioneManager
                .caricaRigheInstallazioneByAreaInstallazione(ai.getId());
        for (RigaInstallazione rigaInstallazione : righe) {
            if (!rigaInstallazione.isNew()) {
                switch (rigaInstallazione.getTipoMovimento()) {
                case SOSTITUZIONE:
                    creaRigaMagazzino(areaMagazzino, rigaInstallazione, -1, rigaInstallazione.getArticoloRitiro());
                    creaRigaMagazzino(areaMagazzino, rigaInstallazione, +1,
                            rigaInstallazione.getArticoloInstallazione());
                    break;
                case INSTALLAZIONE:
                    creaRigaMagazzino(areaMagazzino, rigaInstallazione, +1,
                            rigaInstallazione.getArticoloInstallazione());
                    break;
                case RITIRO:
                    creaRigaMagazzino(areaMagazzino, rigaInstallazione, -1, rigaInstallazione.getArticoloRitiro());
                    break;
                default:
                    break;
                }
            }
        }
    }
}
