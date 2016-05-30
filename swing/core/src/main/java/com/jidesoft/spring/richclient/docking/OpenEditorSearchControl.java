package com.jidesoft.spring.richclient.docking;

import it.eurotn.panjea.rich.TreeCommandPanelMenu;
import it.eurotn.panjea.rich.editors.DefaultEditor;
import it.eurotn.panjea.rich.factory.PanjeaEditorRegistry;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.list.QuickListFilterField;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.SimpleScrollPane;

public class OpenEditorSearchControl extends QuickListFilterField {

	private abstract class ActionOpenEditor {

		/**
		 * @return icona
		 */
		public abstract Icon getIcon();

		/**
		 * @return testo
		 */
		public abstract String getLabel();

		/**
		 * Azione da eseguire per l'apertura dell'editor.
		 */
		public abstract void openEditor();
	}

	private class ActionRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -8914388365023439331L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			label.setText(((ActionOpenEditor) value).getLabel());
			label.setIcon(((ActionOpenEditor) value).getIcon());
			return label;
		}
	}

	private class CommandOpenEditor extends ActionOpenEditor {

		private ActionCommand command;
		private String group;
		private final String menu;

		/**
		 * 
		 * Costruttore.
		 * 
		 * @param menu
		 *            menu del comando
		 * @param group
		 *            gruppo del comando
		 * @param command
		 *            comando
		 */
		public CommandOpenEditor(final String menu, final String group, final ActionCommand command) {
			super();
			this.menu = menu;
			this.group = group;
			this.command = command;
		}

		@Override
		public Icon getIcon() {
			return command.getIcon();
		}

		@Override
		public String getLabel() {
			String[] groupSplit = group.split(":");
			String groupName = "menu.gruppo";
			for (int i = 1; i < groupSplit.length; i++) {
				groupName = groupName + "." + groupSplit[i];
			}
			return RcpSupport.getMessage(menu + ".title") + " --> " + RcpSupport.getMessage(groupName + ".title")
					+ " -->" + command.getText();
		}

		@Override
		public void openEditor() {
			command.execute();
		}

	}

	private class EditorActionOpenEditor extends ActionOpenEditor {
		private String editor;
		private String compositePage;
		private String page;

		public EditorActionOpenEditor(final String editor) {
			this.editor = editor;
		}

		public EditorActionOpenEditor(final String editor, final String compositePage, final String page) {
			super();
			this.editor = editor;
			this.compositePage = compositePage;
			this.page = page;
		}

		@Override
		public Icon getIcon() {
			return RcpSupport.getIcon(editor + ".image");
		}

		@Override
		public String getLabel() {
			StringBuilder sb = new StringBuilder(RcpSupport.getMessage(editor + ".title"));
			if (page != null) {
				sb.append("-->");
				sb.append(RcpSupport.getMessage(compositePage + "." + page + "Command.label"));
			}
			return sb.toString();
		}

		@Override
		public void openEditor() {
			PanjeaEditorRegistry reg = RcpSupport.getBean("editorFactory");
			for (Entry<Object, String> entry1 : reg.getEditorMap().entrySet()) {
				if (editor.equals(entry1.getValue())) {
					Object chiave = entry1.getKey();
					if (chiave instanceof String) {
						if (page != null) {
							LifecycleApplicationEvent event = new OpenEditorEvent(chiave + "#" + page);
							Application.instance().getApplicationContext().publishEvent(event);
						} else {
							LifecycleApplicationEvent event = new OpenEditorEvent(chiave);
							Application.instance().getApplicationContext().publishEvent(event);
						}
					} else {
						try {
							Object newObject = ((Class) chiave).newInstance();
							LifecycleApplicationEvent event = new OpenEditorEvent(newObject);
							Application.instance().getApplicationContext().publishEvent(event);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private static final long serialVersionUID = 8344646877936340476L;

	private JidePopup popup;
	private JList listResult;

	public OpenEditorSearchControl() {
		super();
		setListModel(createModel());
		popup = new JidePopup();
		listResult = new JList(getDisplayListModel());
		listResult.setCellRenderer(new ActionRenderer());
		popup.getContentPane().add(new SimpleScrollPane(listResult));
		popup.setMovable(false);
		popup.setOwner(this);
		setWildcardEnabled(true);
		getTextField().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					listResult.setSelectedIndex(listResult.getSelectedIndex() + 1);
					listResult.ensureIndexIsVisible(listResult.getSelectedIndex() + 1);
					e.consume();
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					listResult.setSelectedIndex(listResult.getSelectedIndex() - 1);
					listResult.ensureIndexIsVisible(listResult.getSelectedIndex() - 1);
					e.consume();
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					popup.hidePopupImmediately();
					e.consume();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					popup.hidePopupImmediately();
					uninstallListeners();
					getTextField().setText("");
					installListeners();
					apriEditor();
					e.consume();
				}

				if (e.getKeyCode() == KeyEvent.VK_F11) {
					setListModel(createModel());
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	@Override
	public void applyFilter() {
		super.applyFilter();
		if (!popup.isShowing()) {
			popup.setOwner(this);
			popup.showPopup();
		}
		listResult.setSelectedIndex(0);
	}

	protected void apriEditor() {
		if (listResult.getSelectedValue() != null) {
			((ActionOpenEditor) listResult.getSelectedValue()).openEditor();
		}
	}

	@Override
	protected String convertElementToString(Object paramObject) {
		return ((ActionOpenEditor) paramObject).getLabel();
	}

	private ListModel createModel() {
		DefaultListModel listModel = new DefaultListModel();
		String[] result = Application.instance().getApplicationContext().getBeanNamesForType(EditorDescriptor.class);
		for (String editor : result) {
			EditorDescriptor descriptor = (EditorDescriptor) Application.instance().getApplicationContext()
					.getBean(editor);
			if (descriptor.getEditorClass() == DefaultEditor.class) {
				@SuppressWarnings("unchecked")
				List<String> pagine = (List<String>) (descriptor.getEditorProperties().get("idPages"));
				if (pagine == null) {
					continue;
				}

				List<String> pagineReali = new ArrayList<String>(pagine.size());
				for (String pagina : pagine) {
					if (!pagina.startsWith("group")) {
						pagineReali.add(pagina);
					}
				}
				if (pagineReali.size() == 1) {
					listModel.addElement(new EditorActionOpenEditor(editor));
				} else {
					for (String pagina : pagineReali) {
						listModel.addElement(new EditorActionOpenEditor(editor, (String) descriptor
								.getEditorProperties().get("dialogPageId"), pagina));
					}
				}
			}
		}

		// Recupero i command
		@SuppressWarnings("unchecked")
		Map<String, TreeCommandPanelMenu> menu = Application.instance().getApplicationContext()
				.getBeansOfType(TreeCommandPanelMenu.class);
		for (TreeCommandPanelMenu panelMenu : menu.values()) {
			String group = "";
			for (Object commandId : panelMenu.getCommands()) {
				if (commandId instanceof String) {
					String commandIdString = (String) commandId;
					ActionCommand command = (ActionCommand) Application.instance().getActiveWindow()
							.getCommandManager().getCommand(commandIdString);
					if (commandIdString.startsWith("group:")) {
						group = commandIdString;
					}
					if (command != null && command.isAuthorized()) {
						listModel.addElement(new CommandOpenEditor(panelMenu.getId(), group, command));
					}
				}
			}
		}
		return listModel;
	}

	@Override
	protected void showContextMenu() {
	}

}
