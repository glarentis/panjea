package it.eurotn.panjea.cosaro.sync.bilance;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.cosaro.CosaroSettings;
import it.eurotn.panjea.cosaro.sync.EsportaDistinta;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

@Stateless(name = "Panjea.EsportaDistintaGammaMeat")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.EsportaDistintaGammaMeat")
public class EsportaDistintaGammaMeatBean implements EsportaDistinta {

    private static final Logger LOGGER = Logger.getLogger(EsportaDistintaGammaMeatBean.class);

    @EJB
    private CosaroSettings cosaroSettings;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private SediEntitaManager sediEntitaManager;

    private BeanWriter createWriter(CodiceDocumento codiceDocumento, EntitaDocumento entitaDocumento) {
        StreamFactory factory = cosaroSettings.getStreamTemplate("DISTINTEGAMMAMEATTEMPLATE.xml");
        File outfile = new File(cosaroSettings.caricaFilePathBilanceExport() + "/" + codiceDocumento.getCodice() + "_"
                + entitaDocumento.getCodice() + "-ORD.csv");
        return factory.createWriter("distinta", outfile);
    }

    @Override
    public void esporta(List<DistintaCarico> distinteSelezionate) throws Exception {
        LOGGER.debug("--> Enter esporta distinte per gamma meat");

        // Raccolgo per numero ordine
        MultiValueMap ordini = new MultiValueMap();
        for (DistintaCarico distintaCarico : distinteSelezionate) {
            for (RigaDistintaCarico rigaDistintaCarico : distintaCarico.getRigheEvasione()) {
                ordini.put(rigaDistintaCarico.getIdAreaOrdine(), rigaDistintaCarico);
            }
        }

        for (Object key : ordini.keySet()) {
            @SuppressWarnings("unchecked")
            List<RigaDistintaCarico> righe = (List<RigaDistintaCarico>) ordini.get(key);
            BeanWriter out = createWriter(righe.get(0).getNumeroDocumento(), righe.get(0).getEntita());
            AreaOrdine areaOrdine = panjeaDAO.load(AreaOrdine.class, righe.get(0).getIdAreaOrdine());
            SedeEntita sedePrincipale = sediEntitaManager
                    .caricaSedePrincipaleEntita(areaOrdine.getDocumento().getEntita().creaProxyEntita());
            out.write(new RigaTestataGammaMeat(areaOrdine, sedePrincipale));
            for (RigaDistintaCarico rigaDistintaCarico : righe) {
                out.write(new RigaOrdineLottoGammaMeat(rigaDistintaCarico));
                // if (rigaDistintaCarico.getRigheDistintaLotto() != null) {
                // for (RigaDistintaCaricoLotto rigaDistintaCaricoLotto :
                // rigaDistintaCarico.getRigheDistintaLotto()) {
                // out.write(new RigaOrdineLottoGammaMeat(rigaDistintaCaricoLotto));
                // }
                // }
            }
            out.close();
        }
        LOGGER.debug("--> Exit esporta distinte per gamma meat");
    }
}
