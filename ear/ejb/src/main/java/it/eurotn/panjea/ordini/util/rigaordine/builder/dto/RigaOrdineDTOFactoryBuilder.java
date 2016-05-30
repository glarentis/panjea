package it.eurotn.panjea.ordini.util.rigaordine.builder.dto;

import java.util.HashMap;
import java.util.Map;

public class RigaOrdineDTOFactoryBuilder {
	private static Map<String, RigaOrdineDTOBuilder> builder = new HashMap<String, RigaOrdineDTOBuilder>();

	static {
		builder.put("N", new RigaOrdineNotaDTOBuilder());
		builder.put("A", new RigaOrdineArticoloDTOBuilder());
		builder.put("C", new RigaOrdineComponenteDTOBuilder());
		builder.put("T", new RigaOrdineTestataDTOBuilder());
		builder.put("D", new RigaOrdineDistintaDTOBuilder());
	}

	/**
	 * 
	 * @param rigaOrdineDTOResult
	 *            tiopRiga per il quale ritornare il builder
	 * @return builder per creare il tipo riga richiesto
	 */
	public RigaOrdineDTOBuilder getBuilder(RigaOrdineDTOResult rigaOrdineDTOResult) {
		// i componenti delle distinte non hanno dto
		return builder.get(rigaOrdineDTOResult.getTipoRiga());
	}
}
