package it.eurotn.panjea.rich;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

public class CategoriaAnalisiNode extends IconNode {

	private static final long serialVersionUID = 1L;

	private boolean loaded = false;

	private int depth;

	private final int index;

	public CategoriaAnalisiNode(int index, int depth) {
		super();
		this.index = index;
		this.depth = depth;
		add(new IconNode("Loading..."));
		setAllowsChildren(true);
		setUserObject("Child " + index + " at level " + depth);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	public void loadChildren(final DefaultTreeModel model, final PropertyChangeListener progressListener) {
		if (loaded) {
			return;
		}
		SwingWorker<List<CategoriaAnalisiNode>, Void> worker = new SwingWorker<List<CategoriaAnalisiNode>, Void>() {
			@Override
			protected List<CategoriaAnalisiNode> doInBackground() throws Exception {
				// Here access database if needed
				setProgress(0);
				List<CategoriaAnalisiNode> children = new ArrayList<CategoriaAnalisiNode>();
				if (depth < 5) {
					for (int i = 0; i < 5; i++) {
						Thread.sleep(300);
						children.add(new CategoriaAnalisiNode(i + 1, depth + 1));
						setProgress((i + 1) * 20);
					}
				} else {
					Thread.sleep(1000);
				}
				setProgress(0);
				return children;
			}

			@Override
			protected void done() {
				try {
					setChildren(get());
					model.nodeStructureChanged(CategoriaAnalisiNode.this);
				} catch (Exception e) {
					e.printStackTrace();
					// Notify user of error.
				}
				super.done();
			}
		};
		if (progressListener != null) {
			worker.getPropertyChangeSupport().addPropertyChangeListener("progress", progressListener);
		}
		worker.execute();
	}

	private void setChildren(List<CategoriaAnalisiNode> children) {
		removeAllChildren();
		setAllowsChildren(children.size() > 0);
		for (MutableTreeNode node : children) {
			add(node);
		}
		loaded = true;
	}

}