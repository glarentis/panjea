package it.eurotn.panjea.onroad.domain;

public class DatiEsportazioneContratti9 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0005";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti9();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("categoria articolo-sede");
		chiaveCondizRiga.setNumeroCampi(3);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("ARTICO");
		chiaveCondizRiga.setNomeCampo1("Campo1");

		chiaveCondizRiga.setNomeTabella2("CLIENT");
		chiaveCondizRiga.setNomeCampo2("CodiceCliente");

		chiaveCondizRiga.setNomeTabella3("CLIENT");
		chiaveCondizRiga.setNomeCampo3("CodiceDestinatario");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti8.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceCategoriaArticolo() + getCodiceCliente() + getCodiceSede();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
