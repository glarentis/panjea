package it.eurotn.panjea.magazzino.manager.export.exporter.latteriavipiteno;

import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.magazzino.domain.AttributoRiga;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.manager.export.exporter.RendicontazioneWriter;
import it.eurotn.panjea.magazzino.manager.export.exporter.interfaces.RendicontazioneExporter;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RendicontazioneExporter.LatteriaVipiteno")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RendicontazioneExporter.LatteriaVipiteno")
public class LatteriaVipitenoRendicontazioneExporterBean implements RendicontazioneExporter {

	private static Logger logger = Logger.getLogger(LatteriaVipitenoRendicontazioneExporterBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public List<byte[]> export(List<AreaMagazzinoRicerca> areeMagazzino, TipoEsportazione tipoEsportazione,
			Map<String, Object> parametri) {

		Date dataCreazione = Calendar.getInstance().getTime();
		int progressivo = 1;
		Double quantitaTotale = 0.0;

		List<Object> rendicontazioneBeans = new ArrayList<Object>();

		// record di testata
		LatteriaVipitenoTestataRendicontazioneBeanExporter beanTestata = new LatteriaVipitenoTestataRendicontazioneBeanExporter(
				tipoEsportazione.getCodiceCliente(), dataCreazione);
		rendicontazioneBeans.add(beanTestata);

		for (AreaMagazzinoRicerca areaMagazzinoRicerca : areeMagazzino) {
			AreaMagazzino areaMagazzinoLoad;
			try {
				areaMagazzinoLoad = panjeaDAO.load(AreaMagazzino.class, areaMagazzinoRicerca.getIdAreaMagazzino());

				for (RigaMagazzino rigaMagazzino : areaMagazzinoLoad.getRigheMagazzino()) {
					if (rigaMagazzino instanceof RigaArticolo) {
						RigaArticolo rigaArticolo = (RigaArticolo) rigaMagazzino;
						LatteriaVipitenoRigaRendicontazioneBeanExporter bean = null;
						// se ci sono righe lotto devo generare 1 riga per ogni
						// lotto. Moltiplico la quantità per il numero pezzi.
						AttributoRiga attributoPezzi = rigaArticolo.getAttributo("nrpezzi");
						Number nrPezzi = 1.0;
						if (attributoPezzi != null && attributoPezzi.getValoreTipizzato() != null) {
							nrPezzi = (Number) attributoPezzi.getValoreTipizzato();
						}

						if (rigaArticolo.getRigheLotto() != null && !rigaArticolo.getRigheLotto().isEmpty()) {
							for (RigaLotto rigaLotto : rigaArticolo.getRigheLotto()) {
								bean = new LatteriaVipitenoRigaRendicontazioneBeanExporter(
										tipoEsportazione.getCodiceCliente(), dataCreazione, progressivo, rigaArticolo,
										rigaLotto.getQuantita() * nrPezzi.doubleValue(), rigaLotto.getLotto()
												.getCodice());
								rendicontazioneBeans.add(bean);
							}
						} else {
							bean = new LatteriaVipitenoRigaRendicontazioneBeanExporter(
									tipoEsportazione.getCodiceCliente(), dataCreazione, progressivo, rigaArticolo,
									rigaArticolo.getQta() * nrPezzi.doubleValue(), "");
							rendicontazioneBeans.add(bean);
						}

						progressivo++;
						quantitaTotale = quantitaTotale + ((RigaArticolo) rigaMagazzino).getQta();
					}
				}
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento dell'area magazzino.", e);
				throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e);
			}
		}

		// record di piede
		LatteriaVipitenoPiedeRendicontazioneBeanExporter beanPiede = new LatteriaVipitenoPiedeRendicontazioneBeanExporter(
				tipoEsportazione.getCodiceCliente(), dataCreazione, progressivo, quantitaTotale);
		rendicontazioneBeans.add(beanPiede);

		RendicontazioneWriter writer = new RendicontazioneWriter();
		List<byte[]> flussi = new ArrayList<byte[]>();
		flussi.add(writer.write(tipoEsportazione.getTemplate(), rendicontazioneBeans));

		return flussi;
	}

}
