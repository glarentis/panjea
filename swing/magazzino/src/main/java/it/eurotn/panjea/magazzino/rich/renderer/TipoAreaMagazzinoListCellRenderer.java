package it.eurotn.panjea.magazzino.rich.renderer;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.springframework.richclient.util.RcpSupport;

public class TipoAreaMagazzinoListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 422759230962399961L;

	private boolean opaque = false;

	/**
	 * Costruttore.
	 * 
	 */
	public TipoAreaMagazzinoListCellRenderer() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 */
	public TipoAreaMagazzinoListCellRenderer(boolean opaque) {
		super();
		this.opaque = opaque;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

		if (value instanceof TipoAreaMagazzino) {
			TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) value;
			setIcon(RcpSupport.getIcon(TipoDocumento.class.getName()));
			setText(tipoAreaMagazzino.getTipoDocumento().getCodice() + " - "
					+ tipoAreaMagazzino.getTipoDocumento().getDescrizione());
		}

		setOpaque(opaque);

		return component;
	}
}
