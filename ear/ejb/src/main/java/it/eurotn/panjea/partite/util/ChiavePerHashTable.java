package it.eurotn.panjea.partite.util;

import it.eurotn.panjea.anagrafica.domain.TipoPagamento;

import java.util.Date;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class ChiavePerHashTable {

	/**
	 * @uml.property name="data"
	 */
	private Date data;

	/**
	 * @uml.property name="tipoPagamento"
	 * @uml.associationEnd
	 */
	private TipoPagamento tipoPagamento;

	public ChiavePerHashTable() {
		super();
	}

	public ChiavePerHashTable(Date data, TipoPagamento tipoPagamento) {
		super();
		this.data = data;
		this.tipoPagamento = tipoPagamento;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof ChiavePerHashTable) {
			ChiavePerHashTable kiave = (ChiavePerHashTable) arg0;
			if (kiave.getData().equals(this.data) && kiave.tipoPagamento.equals(this.tipoPagamento)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 * @uml.property name="data"
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @return
	 * @uml.property name="tipoPagamento"
	 */
	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((tipoPagamento == null) ? 0 : tipoPagamento.hashCode());
		return result;
	}

	/**
	 * @param data
	 * @uml.property name="data"
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param tipoPagamento
	 * @uml.property name="tipoPagamento"
	 */
	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

}
