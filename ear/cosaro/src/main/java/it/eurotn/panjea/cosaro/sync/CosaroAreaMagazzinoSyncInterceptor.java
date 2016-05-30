package it.eurotn.panjea.cosaro.sync;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.cosaro.sync.exporter.CosaroDOCUMENTOExporter;
import it.eurotn.panjea.cosaro.sync.exporter.interfaces.DataDocumentoExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class CosaroAreaMagazzinoSyncInterceptor {

	@Resource
	private SessionContext context;

	/**
	 * .
	 * 
	 * @param ctx
	 *            .
	 * @return .
	 * @throws Exception .
	 */
	@SuppressWarnings("unchecked")
	@AroundInvoke
	public Object execute(InvocationContext ctx) throws Exception {
		try {
			Object object = ctx.getParameters()[0];
			boolean export = false;

			// prima di continuare con la chiamata del metodo verifico se l'id e' null --> l'area magazzino e' stata
			// appena
			// salvata e quindi non esporto nulla

			// se l'id invece e' valorizzato sto modificando una area magazzino esistente
			if (object instanceof IDefProperty) {
				export = object != null && ((IDefProperty) object).getId() != null;
			}
			object = ctx.proceed();

			// l'interceptor è legato al salvataggio e alla conferma delle righe, ma in un caso il parametro è
			// AreaMagazzino, nell'altro AreaMagazzinoFullDTO
			if (object instanceof AreaMagazzino) {
				// salva entità
				exportAreaMagazzino((AreaMagazzino) object, export);
			} else if (object instanceof AreaMagazzinoFullDTO) {
				// conferma righe magazzino
				exportAreaMagazzino(((AreaMagazzinoFullDTO) object).getAreaMagazzino(), export);
			} else if (object instanceof List) {
				List<AreaMagazzino> aree = (List<AreaMagazzino>) object;
				for (AreaMagazzino area : aree) {
					exportAreaMagazzino(area, true);
				}
			}

			return object;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Esporta il documento.
	 * 
	 * @param areaMagazzino
	 *            areaMagazzino
	 * @param export
	 *            true o false
	 */
	private void exportAreaMagazzino(AreaMagazzino areaMagazzino, boolean export) {
		if (export) {
			System.setProperty("line.separator", "\r\n");
			DataDocumentoExporter dataDocumentoExporter = (DataDocumentoExporter) context
					.lookup(CosaroDOCUMENTOExporter.BEAN_NAME);
			try {
				dataDocumentoExporter.esporta(areaMagazzino);
			} catch (FileCreationException e) {
				throw new RuntimeException("errore durante la creazione del file", e);
			}
		}
	}
}
