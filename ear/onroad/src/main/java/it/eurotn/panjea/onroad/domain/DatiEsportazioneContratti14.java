package it.eurotn.panjea.onroad.domain;

public class DatiEsportazioneContratti14 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0001";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti14();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("articolo-sede");
		chiaveCondizRiga.setNumeroCampi(3);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("ARTICO");
		chiaveCondizRiga.setNomeCampo1("CodiceArticolo");

		chiaveCondizRiga.setNomeTabella2("CLIENT");
		chiaveCondizRiga.setNomeCampo2("CodiceCliente");

		chiaveCondizRiga.setNomeTabella3("CLIENT");
		chiaveCondizRiga.setNomeCampo3("CodiceDestinatario");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti13.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceArticolo() + getCodiceCliente() + getCodiceSede();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
