package it.eurotn.panjea.vending.rest.manager.palmari.importazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.domain.evadts.RilevazioniEvaDtsErrori;
import it.eurotn.panjea.vending.domain.evadts.RilevazioniFasceEva;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.interfaces.RilevazioniEvaDtsManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces.ImportazioneEvaDtsManager;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RilevazioneEva;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.NewDataSet.RilevazioneFascieEva;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.ImpotazioneEvaDtsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImpotazioneEvaDtsManager")
public class ImportazioneEvaDtsManagerBean implements ImportazioneEvaDtsManager {

    @EJB
    private RilevazioniEvaDtsManager rilevazioniEvaDtsManager;

    @Override
    public boolean importaEvaDts(AreaRifornimento rifornimento, ImportazioneXml importazioneXml,
            int progressivoRifornimento) {
        RilevazioneEva rilevazioneEva = importazioneXml.getRilevazioniEva().get(progressivoRifornimento);
        if (rilevazioneEva == null) {
            return false;
        }
        RilevazioneEvaDts rilevazionePanjea = new RilevazioneEvaDts();
        // Copio tutti i campi delle rilevazioni tramite inspector. NB divido il valore per il campo
        // divisore
        PanjeaEJBUtil.copyProperties(rilevazionePanjea, rilevazioneEva);
        rilevazionePanjea.setAreaRifornimento(rifornimento);

        // Importa fascie
        Collection<RilevazioneFascieEva> fascie = importazioneXml.getRilevazioniFasceEva()
                .get(rilevazioneEva.getProgressivo());
        List<RilevazioniFasceEva> fascieEvaPanjea = new ArrayList<>();
        for (RilevazioneFascieEva rilevazioneFascieEva : fascie) {
            RilevazioniFasceEva fasciaPanjea = new RilevazioniFasceEva();
            fasciaPanjea.setLa101(rilevazioneFascieEva.getLA101());
            fasciaPanjea.setLa102(rilevazioneFascieEva.getLA102());
            fasciaPanjea.setLa103(new BigDecimal(rilevazioneFascieEva.getLA103()));
            fasciaPanjea.setLa104(rilevazioneFascieEva.getLA104());
            fasciaPanjea.setRilevazioneEvaDts(rilevazionePanjea);
            fascieEvaPanjea.add(fasciaPanjea);
        }
        rilevazionePanjea.setFasce(fascieEvaPanjea);

        // Importa errori
        List<RilevazioniEvaDtsErrori> erroriEva = new ArrayList<>();
        for (RilevazioniEvaDtsErrori rilevazioneErroriEva : erroriEva) {
            RilevazioniEvaDtsErrori errore = new RilevazioniEvaDtsErrori();
            PanjeaEJBUtil.copyProperties(errore, rilevazioneErroriEva);
            erroriEva.add(errore);
        }
        rilevazionePanjea.setErrori(erroriEva);

        rilevazionePanjea.applicaDivisore();

        rilevazioniEvaDtsManager.salva(rilevazionePanjea);
        return false;
    }

}
