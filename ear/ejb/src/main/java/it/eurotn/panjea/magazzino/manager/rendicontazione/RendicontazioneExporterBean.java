package it.eurotn.panjea.magazzino.manager.rendicontazione;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneWriter;
import it.eurotn.panjea.magazzino.manager.export.exporter.interfaces.RendicontazioneExporter;
import it.eurotn.panjea.magazzino.service.interfaces.MagazzinoDocumentoService;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTOStampa;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRendicontazione;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

@Stateless(name = "Panjea.RendicontazioneExporter")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RendicontazioneExporter")
public class RendicontazioneExporterBean implements RendicontazioneExporter {

    @EJB
    private MagazzinoDocumentoService magazzinoDocumentoService;

    @Override
    public List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
            Map<String, Object> parametri) {

        List<Object> rendicontazioneBeans = new ArrayList<Object>();

        for (AreaMagazzinoRicerca area : areeMagazzino) {
            Map<Object, Object> params = new HashMap<>();
            params.put("id", area.getIdAreaMagazzino());
            AreaMagazzinoFullDTOStampa fullDTO = magazzinoDocumentoService.caricaAreaMagazzinoFullDTO(params);
            AreaMagazzinoRendicontazione areaRendicontazione = new AreaMagazzinoRendicontazione(fullDTO);
            areaRendicontazione.setCodiceCliente(tipoEsportazione.getCodiceCliente());
            rendicontazioneBeans.add(areaRendicontazione);
        }

        RendicontazioneWriter writer = new RendicontazioneWriter();
        List<byte[]> flussi = new ArrayList<byte[]>();
        flussi.add(writer.write(tipoEsportazione.getTemplate(), rendicontazioneBeans));

        return flussi;
    }
}
