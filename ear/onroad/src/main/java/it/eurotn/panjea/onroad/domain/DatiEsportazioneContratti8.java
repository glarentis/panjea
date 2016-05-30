package it.eurotn.panjea.onroad.domain;

public class DatiEsportazioneContratti8 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0006";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti8();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("categoria articolo-cliente");
		chiaveCondizRiga.setNumeroCampi(2);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("ARTICO");
		chiaveCondizRiga.setNomeCampo1("Campo1");

		chiaveCondizRiga.setNomeTabella2("CLIENT");
		chiaveCondizRiga.setNomeCampo2("CodiceCliente");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti7.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceCategoriaArticolo() + getCodiceCliente();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
