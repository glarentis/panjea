package it.eurotn.rich.binding;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

public class FileChooserBinding extends CustomBinding {
    private final FileChooser field;
    private final boolean useFile;

    public FileChooserBinding(FormModel model, String path, Class<?> class1, FileChooser field,
            FileChooser.FileChooserMode mode, boolean useFile) {
        super(model, path, class1);
        this.field = field;
        this.field.setMode(mode);
        this.useFile = useFile;
    }

    @Override
    protected JComponent doBindControl() {
        if (!useFile && getValue() != null) {
            field.setText((String) getValue());
        } else if (useFile && getValue() != null) {
            field.setText(((java.io.File) getValue()).getAbsolutePath());
        } else {
            field.setText("");
        }
        field.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            void changeData() {
                if (field.isEditable()) {
                    // provo a creare il file, se il file è valido avviso i controlli
                    File file = new java.io.File(field.getText());
                    if (file.exists()) {
                        if (useFile) {
                            controlValueChanged(file);
                        } else {
                            controlValueChanged(field.getText());
                        }
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changeData();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                changeData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changeData();
            }
        });
        return field;
    }

    @Override
    protected void enabledChanged() {
        field.setEnabled(isEnabled());
        readOnlyChanged();
    }

    @Override
    protected void readOnlyChanged() {
        field.setEditable(isEnabled() && !isReadOnly());
    }

    @Override
    protected void valueModelChanged(Object newValue) {
        if (!useFile) {
            field.setText((String) newValue);
        } else {
            if (newValue != null) {
                field.setText(((java.io.File) newValue).getAbsolutePath());
            }
        }
        readOnlyChanged();
    }
}
