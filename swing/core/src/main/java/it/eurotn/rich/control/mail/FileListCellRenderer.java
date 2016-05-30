package it.eurotn.rich.control.mail;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

public class FileListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 6610989129035293696L;

    private final Icon defaultIcon = RcpSupport.getIcon("allegato");
    private Map<String, Icon> extIconsMap = new HashMap<String, Icon>();

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value != null && !StringUtils.isEmpty((String) value)) {
            String fileName = (String) value;
            label.setText(FilenameUtils.getName(fileName));

            Icon extIcon = extIconsMap.get(FilenameUtils.getExtension(fileName));
            if (extIcon == null) {
                extIcon = RcpSupport.getIcon(FilenameUtils.getExtension(fileName));
                extIcon = extIcon == null ? defaultIcon : extIcon;
                extIconsMap.put(FilenameUtils.getExtension(fileName), extIcon);
            }

            label.setIcon(extIcon);
        }

        return label;
    }

}