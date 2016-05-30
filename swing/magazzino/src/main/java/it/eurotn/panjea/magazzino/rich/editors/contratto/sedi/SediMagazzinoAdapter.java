/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.sedi;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;

import java.util.Comparator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GroupingList;

/**
 * Adapter che trasforma una lista di <code>SedeMagazzino</code> in un albero di nodi con la seguente struttura:<br>
 * + root<br>
 * &nbsp;&nbsp;+ entità<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- sedi<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- sedi<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- sedi<br>
 * &nbsp;&nbsp;+ entità<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;- sedi<br>
 * 
 * @author fattazzo
 * 
 */
public class SediMagazzinoAdapter {

	public SediMagazzinoAdapter() {
		super();
	}

	public DefaultMutableTreeNode transform(List<SedeMagazzinoLite> sedi, List<EntitaLite> entita) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();

		EventList<SedeMagazzinoLite> eventList = new BasicEventList<SedeMagazzinoLite>();
		if (sedi != null) {
			eventList.addAll(sedi);
		}
		// creo la sede fittizia per le entita legate
		if (entita != null) {
			for (EntitaLite entitaLite : entita) {
				SedeMagazzinoLite sedeMagazzinoLite = new SedeMagazzinoLite();
				sedeMagazzinoLite.getSedeEntita().setEntita(entitaLite);
				sedeMagazzinoLite.getSedeEntita().getSede().setDescrizione("Tutte le sedi");
				eventList.add(sedeMagazzinoLite);
			}
		}

		// creo il comparator che esegue la comparTo sull'entità delle sedi
		Comparator<SedeMagazzinoLite> sedeComparator = new Comparator<SedeMagazzinoLite>() {
			@Override
			public int compare(SedeMagazzinoLite o1, SedeMagazzinoLite o2) {
				return o1.getSedeEntita().getEntita().getId().compareTo(o2.getSedeEntita().getEntita().getId());
			}
		};

		// creo la gorupinglist
		GroupingList<SedeMagazzinoLite> groupingList = new GroupingList<SedeMagazzinoLite>(eventList, sedeComparator);

		DefaultMutableTreeNode nodeEntita = null;
		for (List<SedeMagazzinoLite> listSediGroup : groupingList) {
			// creo il nodo dell'entità
			nodeEntita = new DefaultMutableTreeNode(listSediGroup.get(0).getSedeEntita().getEntita());

			// aggiungo al nodo entità tutti i nodi delle sedi
			for (SedeMagazzinoLite sedeMagazzino : listSediGroup) {
				DefaultMutableTreeNode sedeNode = new DefaultMutableTreeNode(sedeMagazzino);
				nodeEntita.add(sedeNode);
			}

			root.add(nodeEntita);
		}

		return root;
	}
}
