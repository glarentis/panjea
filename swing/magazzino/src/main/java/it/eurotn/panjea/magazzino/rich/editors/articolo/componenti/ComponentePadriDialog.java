package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import java.util.Set;

import javax.swing.JComponent;

import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.PanjeaTitledApplicationDialog;

public class ComponentePadriDialog extends PanjeaTitledApplicationDialog {

	private Set<Componente> componentiPadri;

	public ComponentePadriDialog(Set<Componente> componentiPadri) {
		this.componentiPadri = componentiPadri;
		setTitle("Padri");
	}

	@Override
	protected JComponent createTitledDialogContentPane() {
		JideTableWidget<Componente> tableWidget = new JideTableWidget<>("componentiPadriTableWidget",
				new String[] { "distinta" }, Componente.class);
		tableWidget.setRows(componentiPadri);
		return tableWidget.getComponent();
	}

	@Override
	protected boolean isMessagePaneVisible() {
		return false;
	}

	@Override
	protected boolean onFinish() {
		return true;
	}

}
