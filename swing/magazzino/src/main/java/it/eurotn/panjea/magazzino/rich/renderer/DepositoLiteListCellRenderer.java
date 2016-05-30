package it.eurotn.panjea.magazzino.rich.renderer;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

public class DepositoLiteListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1265792016474770640L;

	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		setIcon(RcpSupport.getIcon(DepositoLite.class.getName()));
		setText(ObjectConverterManager.toString(value));
		setOpaque(true);
		return component;
	}

}
