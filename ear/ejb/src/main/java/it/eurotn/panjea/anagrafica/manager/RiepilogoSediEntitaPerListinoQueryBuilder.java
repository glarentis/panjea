/**
 * 
 */
package it.eurotn.panjea.anagrafica.manager;

/**
 * @author leonardo
 * 
 */
public class RiepilogoSediEntitaPerListinoQueryBuilder extends RiepilogoSediEntitaFactoryQueryBuilder {

	private Integer idListino = null;

	/**
	 * Costruttore.
	 * 
	 * @param idListino
	 *            id del listino per cui restringere la ricerca
	 */
	public RiepilogoSediEntitaPerListinoQueryBuilder(final Integer idListino) {
		this.idListino = idListino;
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
		sb.append(" left join sm.listino listino ");
		sb.append(" left join sm.listinoAlternativo listinoAlt ");
		return sb.toString();
	}

	@Override
	protected String getHqlOptionalWhere() {
		return " and (listinoAlt=" + idListino + " or listino=" + idListino + ") ";
	}

	@Override
	protected String getHqlSelectFields() {
		StringBuilder sb = new StringBuilder();
		sb.append(" ent.id as idEntita,ent.codice as codiceEntita,anag.denominazione as descrizioneEntita,ent.class as tipoEntita, ");
		sb.append("	se.id as idSedeEntita,se.codice as codiceSedeEntita,sa.descrizione as descrizioneSedeEntita,sa.indirizzo as indirizzoSedeEntita, ");
		sb.append(" concat(listino.codice,' - ',listino.descrizione) as listino, ");
		sb.append(" concat(listinoAlt.codice,' - ',listinoAlt.descrizione) as listinoAlternativo, ");
		sb.append(" listino.id as idListino, ");
		sb.append(" listinoAlt.id as idListinoAlternativo ");
		return sb.toString();
	}

}
