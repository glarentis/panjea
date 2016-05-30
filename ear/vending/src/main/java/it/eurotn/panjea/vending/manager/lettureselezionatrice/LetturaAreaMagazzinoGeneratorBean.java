package it.eurotn.panjea.vending.manager.lettureselezionatrice;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces.DepositoInstallazioneManager;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces.LetturaAreaMagazzinoGenerator;

@Stateless(name = "Panjea.LetturaAreaMagazzinoGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LetturaAreaMagazzinoGenerator")
public class LetturaAreaMagazzinoGeneratorBean implements LetturaAreaMagazzinoGenerator {

    @EJB
    private AreaMagazzinoManager areaMagazzinoManager;

    @EJB
    private DepositoInstallazioneManager depositoInstallazioneManager;

    @Override
    public AreaMagazzino aggiornaAreaMagazzino(AreaRifornimento areaRifornimento, LetturaSelezionatrice lettura) {
        AreaMagazzino areaMagazzino = areaRifornimento.getAreaMagazzino();

        areaMagazzino.setTipoAreaDocumento(areaRifornimento.getInstallazione().getTipoAreaMagazzino());
        areaMagazzino.getDocumento()
                .setTipoDocumento(areaRifornimento.getInstallazione().getTipoAreaMagazzino().getTipoDocumento());
        areaMagazzino.getDocumento().setDataDocumento(lettura.getDataRifornimento());
        Importo importo = new Importo("EUR", BigDecimal.ONE);
        importo.setImportoInValutaAzienda(lettura.getImporto());
        importo.calcolaImportoValutaAzienda(2);
        areaMagazzino.getDocumento().setTotale(importo);

        areaMagazzino.setDataRegistrazione(lettura.getDataRifornimento());
        Calendar calendarData = Calendar.getInstance();
        calendarData.setTime(lettura.getDataRifornimento());
        areaMagazzino.setAnnoMovimento(calendarData.get(Calendar.YEAR));
        areaMagazzino.setDepositoOrigine(null);
        if (areaRifornimento.getOperatore() != null && areaRifornimento.getOperatore().getMezzoTrasporto() != null) {
            areaMagazzino.setDepositoOrigine(areaRifornimento.getOperatore().getMezzoTrasporto().getDeposito());
        }
        areaMagazzino = areaMagazzinoManager.aggiornaDatiSede(areaMagazzino,
                areaRifornimento.getInstallazione().getDeposito().getSedeEntita());

        DepositoLite depositoDestinazione = depositoInstallazioneManager
                .caricaOCreaDeposito(areaMagazzino.getDocumento().getSedeEntita(), null).creaLite();
        areaMagazzino.setDepositoDestinazione(depositoDestinazione);

        return areaMagazzino;
    }
}
