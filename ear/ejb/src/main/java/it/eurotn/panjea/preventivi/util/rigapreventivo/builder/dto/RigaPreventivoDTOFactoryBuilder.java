package it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto;

import java.util.HashMap;
import java.util.Map;

public class RigaPreventivoDTOFactoryBuilder {
	private static Map<String, RigaPreventivoDTOBuilder> builder = new HashMap<String, RigaPreventivoDTOBuilder>();

	static {
		builder.put("N", new RigaPreventivoNotaDTOBuilder());
		builder.put("A", new RigaPreventivoArticoloDTOBuilder());
		// builder.put("C", new RigaOrdinePadreDTOBuilder());
		// builder.put("F", new RigaOrdinePadreDTOBuilder());
		// builder.put("P", new RigaOrdinePadreDTOBuilder());
		builder.put("T", new RigaPreventivoTestataDTOBuilder());
		// builder.put("D", new RigaOrdineDistintaDTOBuilder());
	}

	/**
	 * 
	 * @param rigaPreventivoDTOResult
	 *            tiopRiga per il quale ritornare il builder
	 * @return builder per creare il tipo riga richiesto
	 */
	public RigaPreventivoDTOBuilder getBuilder(RigaPreventivoDTOResult rigaPreventivoDTOResult) {
		// i componenti delle distinte non hanno dto
		return builder.get(rigaPreventivoDTOResult.getTipoRiga());
	}
}
