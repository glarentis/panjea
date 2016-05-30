package it.eurotn.panjea.onroad.domain;

public class DatiEsportazioneContratti7 extends DatiEsportazioneContratti {

	public static final String TIPORECORD = "0007";

	@Override
	protected DatiEsportazioneContratti createNewInstance() {
		return new DatiEsportazioneContratti7();
	}

	@Override
	protected ChiaveCondizRiga getChiaveCondiz() {
		ChiaveCondizRiga chiaveCondizRiga = new ChiaveCondizRiga();
		chiaveCondizRiga.setCodiceRicerca(getTipoRecord());
		chiaveCondizRiga.setDescrizione("categoria articolo-categoria cliente");
		chiaveCondizRiga.setNumeroCampi(2);
		chiaveCondizRiga.setSequenzaRicerca(TIPORECORD);

		chiaveCondizRiga.setNomeTabella1("ARTICO");
		chiaveCondizRiga.setNomeCampo1("Campo1");

		chiaveCondizRiga.setNomeTabella2("CLIENT");
		chiaveCondizRiga.setNomeCampo2("CategoriaCliente");

		chiaveCondizRiga.setGotoT(DatiEsportazioneContratti.TIPORECORD_LISTINO);
		chiaveCondizRiga.setGotoF(DatiEsportazioneContratti6.TIPORECORD);
		return chiaveCondizRiga;
	}

	@Override
	public String getCodiceEsportazione() {
		return getCodiceCategoriaArticolo() + getCodiceCategoriaCliente();
	}

	@Override
	public String getTipoRecord() {
		return TIPORECORD;
	}

}
