package it.eurotn.panjea.onroad.domain;

public class DatiEsportazioneContratti12 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0003";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti12();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("articolo-categoria cliente");
		chiaveCondizRiga.setNumeroCampi(2);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("ARTICO");
		chiaveCondizRiga.setNomeCampo1("CodiceArticolo");

		chiaveCondizRiga.setNomeTabella2("CLIENT");
		chiaveCondizRiga.setNomeCampo2("CategoriaCliente");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti11.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceArticolo() + getCodiceCategoriaCliente();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
