/**
 *
 */
package it.eurotn.panjea.anagrafica.manager;

/**
 * @author leonardo
 *
 */
public class RiepilogoSediEntitaPerAgenteQueryBuilder extends RiepilogoSediEntitaFactoryQueryBuilder {

	private Integer idAgente = null;

	/**
	 * Costruttore.
	 *
	 * @param idAgente
	 *            id del listino per cui restringere la ricerca
	 */
	public RiepilogoSediEntitaPerAgenteQueryBuilder(final Integer idAgente) {
		this.idAgente = idAgente;
	}

	@Override
	protected String getHqlLeftJoins() {
		StringBuilder sb = new StringBuilder();
		sb.append(" left join sa.datiGeografici.nazione naz ");
		sb.append(" left join sa.datiGeografici.localita loc ");
		sb.append(" left join sa.datiGeografici.cap cap ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo1 l1 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo2 l2 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo3 l3 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo4 l4 ");
		sb.append(" left join se.agente agente ");
		return sb.toString();
	}

	@Override
	protected String getHqlOptionalWhere() {
		return " and (agente.id=" + idAgente + ") ";
	}

	@Override
	protected String getHqlSelectFields() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ent.id as idEntita,ent.codice as codiceEntita,anag.denominazione as descrizioneEntita,ent.class as tipoEntita, ");
		sb.append("	se.id as idSedeEntita,se.codice as codiceSedeEntita,sa.descrizione as descrizioneSedeEntita,sa.indirizzo as indirizzoSedeEntita, ");
		sb.append("	se.tipoSede.sedePrincipale as sedePrincipale, ");
		sb.append("naz.codice as nazione, ");
		sb.append("loc.descrizione as localita, ");
		sb.append("cap.descrizione as cap, ");
		sb.append("l1.nome as lvl1, ");
		sb.append("l2.sigla as lvl2, ");
		sb.append("l3.nome as lvl3, ");
		sb.append("l4.nome as lvl4, ");
		sb.append("sa.indirizzoMail as indirizzoMail, ");
		sb.append("sa.indirizzoPEC as indirizzoPEC, ");
		sb.append("sa.telefono as telefono, ");
		sb.append("sa.fax as fax ");
		return sb.toString();
	}

}
