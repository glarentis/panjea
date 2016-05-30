package it.eurotn.panjea.cosaro.sync;

import it.eurotn.panjea.cosaro.sync.exporter.CosaroEliminaDOCUMENTOExporter;
import it.eurotn.panjea.cosaro.sync.exporter.interfaces.DataDocumentoExporter;
import it.eurotn.panjea.exporter.exception.FileCreationException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class CosaroAreaMagazzinoCancellaSyncInterceptor {

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
	@AroundInvoke
	public Object execute(InvocationContext ctx) throws Exception {
		Object result = null;
		Object object = ctx.getParameters()[0];
		result = ctx.proceed();
		if (object instanceof AreaMagazzino) {
			AreaMagazzino areaMagazzino = (AreaMagazzino) object;

			System.setProperty("line.separator", "\r\n");
			DataDocumentoExporter dataDocumentoExporter = (DataDocumentoExporter) context
					.lookup(CosaroEliminaDOCUMENTOExporter.BEAN_NAME);
			try {
				dataDocumentoExporter.esporta(areaMagazzino);
			} catch (FileCreationException e) {
				throw new RuntimeException("errore durante la creazione del file", e);
			}
		}
		return result;
	}
}
