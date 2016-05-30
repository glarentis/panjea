package it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.situazione;

import it.eurotn.panjea.contabilita.util.SituazioneRitenuteAccontoDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RisultatiRicercaSituazioneRitenuteTableModel extends DefaultBeanTableModel<SituazioneRitenuteAccontoDTO> {

	private static final long serialVersionUID = -6971923108008422202L;
	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	{
		TOTALE_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaSituazioneRitenuteTableModel() {
		super("risultatiRicercaSituazioneRitenuteTableModel", new String[] { "dataRegistrazione", "tributo",
				"percentualeAliquota", "tipoDocumento", "fornitore", "totaleDocumento", "numeroDocumento",
				"protocollo", "residuo", "totalePagato", "dataPagamento", "totaleRitenute", "totaleRitenuteAperte",
				"totaleRitenutePagate", "ultimaDataPagamentoRitenuta" }, SituazioneRitenuteAccontoDTO.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 2:
		case 5:
		case 8:
		case 9:
		case 11:
		case 12:
		case 13:
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}
}
