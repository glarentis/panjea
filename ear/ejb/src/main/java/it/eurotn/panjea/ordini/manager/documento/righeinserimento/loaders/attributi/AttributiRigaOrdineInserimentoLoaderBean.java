package it.eurotn.panjea.ordini.manager.documento.righeinserimento.loaders.attributi;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.manager.documento.righeinserimento.interfaces.AttributiInserimentoLoader;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(mappedName = "Panjea.AttributiRigaOrdineInserimentoLoaderBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AttributiRigaOrdineInserimentoLoaderBean")
public class AttributiRigaOrdineInserimentoLoaderBean implements AttributiInserimentoLoader {

    private static final Logger LOGGER = Logger.getLogger(AttributiRigaOrdineInserimentoLoaderBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @SuppressWarnings("unchecked")
    private List<AttributoRiga> caricaAttributi(String idRigheOrdine) {
        List<AttributoRiga> attributi = new ArrayList<AttributoRiga>();

        if (!StringUtils.isBlank(idRigheOrdine)) {
            StringBuilder hqlAttributi = new StringBuilder(300);
            hqlAttributi.append("select ar ");
            hqlAttributi.append("from it.eurotn.panjea.ordini.domain.AttributoRiga ar ");
            hqlAttributi.append("where ar.rigaArticolo.id in (");
            hqlAttributi.append(idRigheOrdine);
            hqlAttributi.append(")");

            Query query = panjeaDAO.prepareQuery(hqlAttributi.toString());
            try {
                attributi = panjeaDAO.getResultList(query);
            } catch (Exception e) {
                LOGGER.error("--> errore durante il caricamento degli attributi delle righe", e);
                throw new RuntimeException("errore durante il caricamento degli attributi delle righe", e);
            }
        }

        return attributi;
    }

    @Override
    public RigaOrdineInserimento fillAttributi(RigaOrdineInserimento rigaOrdineInserimento) {

        List<AttributoRiga> attributi = caricaAttributi(rigaOrdineInserimento.getIdRigheOrdine());

        Map<String, AttributoRigaArticolo> mapAttributi = new HashMap<String, AttributoRigaArticolo>();
        for (AttributoRiga attributo : attributi) {

            AttributoRiga attributoRiga = (AttributoRiga) mapAttributi.get(attributo.getTipoAttributo().getNome());
            if (attributoRiga == null) {
                attributoRiga = new AttributoRiga();
                PanjeaEJBUtil.copyProperties(attributoRiga, attributo);
                attributoRiga.setRiga(null);
                attributoRiga.setId(null);
                attributoRiga.setVersion(null);
                mapAttributi.put(attributo.getTipoAttributo().getNome(), attributoRiga);
            } else {
                // se l'attributo è un numerico sommo il valore
                if (attributoRiga.getTipoAttributo().getTipoDato().getJavaType().equals(Double.class)
                        && (attributoRiga.isUpdatable() || attributoRiga.getFormula() != null)) {
                    Double valore = ObjectUtils.defaultIfNull(attributoRiga.getValoreTipizzato(Double.class), 0.0);
                    valore = valore + ObjectUtils.defaultIfNull(attributo.getValoreTipizzato(Double.class), 0.0);

                    String resultString = null;
                    try {
                        resultString = new DefaultNumberFormatterFactory("#,##0",
                                attributoRiga.getTipoAttributo().getNumeroDecimali(), BigDecimal.class)
                                        .getDefaultFormatter().valueToString(valore);
                    } catch (ParseException e) {
                        LOGGER.error("--> errore durante il format del valore dell'attributo "
                                + attributoRiga.getTipoAttributo().getNome(), e);
                        throw new RuntimeException("errore durante il format del valore dell'attributo "
                                + attributoRiga.getTipoAttributo().getNome(), e);
                    }
                    attributoRiga.setValore(resultString);
                    mapAttributi.put(attributo.getTipoAttributo().getNome(), attributoRiga);
                }
            }
        }

        rigaOrdineInserimento.setAttributi(mapAttributi);

        return rigaOrdineInserimento;
    }

}
