package it.eurotn.panjea.contabilita.interceptor;

import it.eurotn.panjea.anagrafica.domain.Entita;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class ConfermaClientePotenzialeInterceptor extends EntitaAggiornaInterceptor {

	@Override
	@AroundInvoke
	public Object associaContoEntita(InvocationContext ctx) throws Exception {
		Entita entita = (Entita) ctx.proceed();
		creaSottoContoEntita(entita);
		return entita;
	}
}
