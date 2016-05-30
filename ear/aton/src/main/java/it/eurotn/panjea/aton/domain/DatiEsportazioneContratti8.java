package it.eurotn.panjea.aton.domain;

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

		chiaveCondizRiga.setNomeTabella1("T_ARTICOL");
		chiaveCondizRiga.setNomeCampo1("Attributo2");

		chiaveCondizRiga.setNomeTabella2("T_CLIENTI");
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
