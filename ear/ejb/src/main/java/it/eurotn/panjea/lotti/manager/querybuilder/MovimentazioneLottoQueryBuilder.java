package it.eurotn.panjea.lotti.manager.querybuilder;

import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;

public final class MovimentazioneLottoQueryBuilder {

	public static final String PARAM_LOTTO = "lotto";
	public static final String PARAM_CODICE_LOTTO = "codice";
	public static final String PARAM_CODICE_AZIENDA = "codiceAzienda";
	public static final String PARAM_ID_ARTICOLO = "idArticolo";

	/**
	 * @param tipoLotto
	 *            tipoLotto
	 * @return from della query
	 */
	private static String getFromQuery(TipoLotto tipoLotto) {
		StringBuilder sb = new StringBuilder();

		switch (tipoLotto) {
		case LOTTO:
			sb.append("from Lotto lotto ");
			sb.append("join lotto.righeLotti rigaLotto  ");
			break;
		case LOTTO_INTERNO:
			sb.append("from LottoInterno lottoInterno ");
			sb.append("join lottoInterno.lotto lottoForn ");
			sb.append("join lottoInterno.righeLotti rigaLotto ");
			break;
		default:
			break;
		}
		sb.append("join rigalotto.rigaArticolo.areaMagazzino am ");
		sb.append("left join am.depositoDestinazione depDest ");
		sb.append("left join am.depositoOrigine depOri ");
		sb.append("join am.documento doc ");
		sb.append("left join doc.rapportoBancarioAzienda rapp ");
		sb.append("left join doc.entita ent ");
		sb.append("left join ent.anagrafica ana ");

		return sb.toString();
	}

	/**
	 * Restituisce la query che carica tutti i movimenti di carico, scarico, inventario e trasferimento ( scarico )
	 * relativi al lotto.
	 * 
	 * @param tipoLotto
	 *            tipoLotto
	 * @return query
	 */
	public static String getMovimentiQuery1(TipoLotto tipoLotto) {

		StringBuilder sb = new StringBuilder();
		sb.append("select rigaLotto.id as id, ");
		sb.append("		  (coalesce(rigalotto.quantita,0)* if(rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoDocumento.notaCreditoEnable,-1,1) * case ");
		sb.append("				when  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento=1 then 1 ");
		sb.append("				when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento=4 then 1 ");
		sb.append("		  		when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento=2 then -1 ");
		sb.append("		  		when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento=3 then -1 ");
		sb.append("				when  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento=7 then 1 ");
		sb.append("		  end) as quantita, ");
		sb.append("		  depOri.id as idDeposito, ");
		sb.append("		  depOri.codice as codiceDeposito, ");
		sb.append("		  depOri.descrizione as descrizioneDeposito, ");
		sb.append("		  (case when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento<7 then depDest.id end) as idDepositoDestinazione, ");
		sb.append("		  (case when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento<7 then  depDest.codice end) as codiceDepositoDestinazione, ");
		sb.append("		  (case when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento<7 then  depDest.descrizione end) as descrizioneDepositoDestinazione, ");
		sb.append(getSelectFields(tipoLotto));
		sb.append(getFromQuery(tipoLotto));
		sb.append(getWhereLotto(tipoLotto));
		sb.append("and (rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento < 7 or (rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento = 7 and rigalotto.rigaArticolo.class != it.eurotn.panjea.magazzino.domain.RigaArticoloComponente) )");

		return sb.toString();
	}

	/**
	 * Restituisce la query che carica tutti i movimenti di trasferimento ( carico ) relativi al lotto.
	 * 
	 * @param tipoLotto
	 *            tipoLotto
	 * @return query
	 */
	public static String getMovimentiQuery2(TipoLotto tipoLotto) {

		StringBuilder sb = new StringBuilder();
		sb.append("select rigaLotto.id as id, ");
		sb.append("		  (coalesce(rigalotto.quantita,0)* case ");
		sb.append("				when  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento=3 then 1 ");
		sb.append("				when  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento=7 then -1 ");
		sb.append("          end) as quantita, ");
		sb.append("		  (case when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento<7 then depOri.id end) as idDeposito, ");
		sb.append("		  (case when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento<7 then depOri.codice end) as codiceDeposito, ");
		sb.append("		  (case when rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento<7 then depOri.descrizione end) as descrizioneDeposito, ");
		sb.append("		  depDest.id as idDepositoDestinazione, ");
		sb.append("		  depDest.codice as codiceDepositoDestinazione, ");
		sb.append("		  depDest.descrizione as descrizioneDepositoDestinazione, ");
		sb.append(getSelectFields(tipoLotto));
		sb.append(getFromQuery(tipoLotto));
		sb.append(getWhereLotto(tipoLotto));
		sb.append("and (rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento = 3 or (rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento = 7 and rigalotto.rigaArticolo.class = it.eurotn.panjea.magazzino.domain.RigaArticoloComponente))");

		return sb.toString();
	}

	/**
	 * @param tipoLotto
	 *            tipoLotto
	 * @return campi comuni delle select
	 */
	private static String getSelectFields(TipoLotto tipoLotto) {

		StringBuilder sb = new StringBuilder();
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.id as idAreaMagazzino, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoMovimento as tipoMovimento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoDocumento.id as idTipoDocumento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoDocumento.codice as codiceTipoDocumento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.tipoAreaMagazzino.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.dataRegistrazione as dataRegistrazione, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.documento.dataDocumento as dataDocumento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.documento.codice as codiceDocumento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.documento.id as idDocumento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.documento.tipoDocumento.tipoEntita as tipoEntitaTipoDocumento, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.documento.tipoDocumento.notaCreditoEnable as storno, ");
		sb.append("		  rigalotto.rigaArticolo.areaMagazzino.documento.codiceAzienda as codiceAziendaTipoDocumento, ");
		sb.append("		  rapp.id as idRapportoBancarioDocumento, ");
		sb.append("		  rapp.descrizione as descrizioneRapportoBancarioDocumento, ");
		sb.append("		  ent.id as idEntitaDocumento, ");
		sb.append("		  ent.codice as codiceEntitaDocumento, ");
		sb.append("		  ent.anagrafica.denominazione as denominazioneEntitaDocumento, ");
		sb.append("          rigalotto.rigaArticolo.numeroDecimaliQta as numeroDecimaliQta, ");
		sb.append("         lotto.articolo.codice as codiceArticolo, ");
		sb.append("          lotto.articolo.descrizioneLinguaAziendale as descrizioneArticolo, ");

		switch (tipoLotto) {
		case LOTTO:
			sb.append("		  lotto.id as idLotto, ");
			sb.append("		  lotto.codice as codiceLotto, ");
			sb.append("		  lotto.dataScadenza as dataScadenzaLotto ");
			break;
		case LOTTO_INTERNO:
			sb.append("		  lottoForn.id as idLotto, ");
			sb.append("		  lottoForn.codice as codiceLotto, ");
			sb.append("		  lottoForn.dataScadenza as dataScadenzaLotto, ");
			sb.append("		  lottoInterno.id as idLottoInterno, ");
			sb.append("		  lottoInterno.codice as codiceLottoInterno ");
			break;
		default:
			throw new UnsupportedOperationException("Tipo lotto non previsto");
		}

		return sb.toString();
	}

	/**
	 * Restituisce la where in base al tipo di ricerca.
	 * 
	 * @param tipoLotto
	 *            tipoLotto
	 * @return where
	 */
	private static String getWhereLotto(TipoLotto tipoLotto) {

		StringBuilder sb = new StringBuilder();

		switch (tipoLotto) {
		case LOTTO:
			sb.append("where lotto = :lotto ");
			break;
		case LOTTO_INTERNO:
			sb.append("where lottoInterno.lotto = :lotto ");
			break;
		default:
			throw new UnsupportedOperationException("Tipo lotto non previsto");
		}

		return sb.toString();
	}

	/**
	 * Costruttore.
	 * 
	 */
	private MovimentazioneLottoQueryBuilder() {
		super();
	}
}
