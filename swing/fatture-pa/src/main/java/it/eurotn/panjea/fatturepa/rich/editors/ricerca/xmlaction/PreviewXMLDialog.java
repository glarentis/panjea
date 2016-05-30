package it.eurotn.panjea.fatturepa.rich.editors.ricerca.xmlaction;

import java.awt.Dimension;
import java.awt.Window;

import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.tokenmarker.XMLTokenMarker;

import it.eurotn.panjea.fatturepa.domain.XMLFatturaPA;
import it.eurotn.rich.dialog.InputApplicationDialog;

public class PreviewXMLDialog extends InputApplicationDialog {

    /**
     * Costruttore.
     *
     * @param xmlFatturaPA
     *            xml fattura
     */
    public PreviewXMLDialog(final XMLFatturaPA xmlFatturaPA) {
        super(xmlFatturaPA.getXmlFileName(), (Window) null);

        CodeEditor codeEditor = new CodeEditor();
        codeEditor.setPreferredSize(new Dimension(750, 550));
        codeEditor.setTokenMarker(new XMLTokenMarker());
        codeEditor.setEditable(false);
        codeEditor.setText(xmlFatturaPA.getXmlFattura());
        setInputField(codeEditor);
        setInputLabelMessage("");
    }

    @Override
    protected String getCancelCommandId() {
        return "okCommand";
    }

    @Override
    protected Object[] getCommandGroupMembers() {
        return new Object[] { getCancelCommand() };
    }
}
