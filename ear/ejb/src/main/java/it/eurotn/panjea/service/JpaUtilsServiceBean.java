package it.eurotn.panjea.service;

import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.service.interfaces.JpaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;

/**
 * Session Bean implementation class JpaUtils.
 */
@RemoteBinding(jndiBinding = "Panjea.JpaUtilsService")
@Stateless(name = "Panjea.JpaUtilsService")
public class JpaUtilsServiceBean implements JpaUtils {

	private static Logger logger = Logger.getLogger(JpaUtils.class);

	@Override
	public List<String> getAziendeDeployate() {
		MBeanServer server = org.jboss.mx.util.MBeanServerLocator.locateJBoss();
		ArrayList<String> result = new ArrayList<String>();
		try {
			Set<ObjectInstance> beans = server.queryMBeans(new ObjectName("persistence.units:*"), null);
			for (ObjectInstance objectInstance : beans) {
				// Il nome dell'mBean risulta essere <ear=Panjea.ear,jar=panjeaejb.jar,unitName=nomeup>
				String unitName = objectInstance.getObjectName().getCanonicalKeyPropertyListString().split(",")[2];
				unitName = unitName.split("=")[1];
				if (unitName.endsWith("panjeapu")) {
					unitName = unitName.replace("panjeapu", "");
					result.add(unitName);
				}
			}
		} catch (Exception e) {
			logger.error("-->errore nel caricare le aziende deployate", e);
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public List<AziendaLite> getAziendeLiteDeployate() {
		List<String> codiciAzienda = getAziendeDeployate();
		List<AziendaLite> aziende = new ArrayList<AziendaLite>();
		for (String codiceAzienda : codiciAzienda) {
			AziendaLite azienda = new AziendaLite();
			azienda.setCodice(codiceAzienda);
			aziende.add(azienda);
		}
		return aziende;
	}

	@Override
	public List<AziendaLite> getAziendeLiteDSDeployate() {
		List<AziendaLite> aziende = getAziendeLiteDeployate();
		MBeanServer server = org.jboss.mx.util.MBeanServerLocator.locateJBoss();
		try {
			Set<ObjectInstance> beans = server.queryMBeans(new ObjectName("jboss.jdbc*:service=metadata,*"), null);
			for (ObjectInstance objectInstance : beans) {
				// Il nome dell'mBean risulta essere <ear=Panjea.ear,jar=panjeaejb.jar,unitName=nomeup>
				String dataSourceName = objectInstance.getObjectName().getKeyProperty("datasource");
				if (dataSourceName != null && dataSourceName.endsWith("remote")) {
					AziendaLite azienda = new AziendaLite();
					azienda.setCodice(dataSourceName.substring(0, dataSourceName.length() - 8));
					aziende.add(azienda);
				}
			}
		} catch (Exception e) {
			logger.error("-->errore nel caricare le aziende deployate", e);
			throw new RuntimeException(e);
		}
		return aziende;
	}

}
