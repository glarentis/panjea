package it.eurotn.panjea.bi.domain.analisi.sql;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;

import java.sql.Connection;

import org.hibernate.jdbc.Work;

public class AnalisiBiFillerExecuter implements Work {
	private AnalisiBi analisiBi;
	private AnalisiBIResult result;

	/**
	 *
	 * @param analisiBi
	 *            analisi da eseguire
	 */
	public AnalisiBiFillerExecuter(final AnalisiBi analisiBi) {
		super();
		this.analisiBi = analisiBi;
	}

	@Override
	public void execute(Connection con) {
		result = new AnalisiBiFiller().fill(con, analisiBi.getAnalisiLayout(), new AnalisiBISqlGenerator(analisiBi));
	}

	/**
	 * @return Returns the result.
	 */
	public AnalisiBIResult getResult() {
		return result;
	}

}