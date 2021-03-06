/*
 * Copyright 2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package it.eurotn.rich.binding;

import java.io.File;
import java.util.Map;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;

/**
 * Binder between the JIDE FileChooserComboBox and java.io.File
 * 
 * @author Jonny Wray
 * 
 */
public class FileChooserComboBoxBinder extends AbstractBinder {

    public FileChooserComboBoxBinder() {
        super(File.class);
    }

    @Override
    protected JComponent createControl(Map arg0) {
        return null;
        // return new FileChooserComboBox();
    }

    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        return null;
        /*
         * final FileChooserComboBox fileComboBox = (FileChooserComboBox)control; return new CustomBinding(formModel,
         * formPropertyPath, File.class) {
         * 
         * protected JComponent doBindControl() { fileComboBox.setSelectedItem(getValue());
         * fileComboBox.addItemListener(new ItemListener(){
         * 
         * public void itemStateChanged(ItemEvent e) { if(e.getStateChange() == ItemEvent.SELECTED){
         * controlValueChanged(fileComboBox.getSelectedItem()); } } }); return fileComboBox; }
         * 
         * protected void readOnlyChanged() { fileComboBox.setEnabled(isEnabled() && !isReadOnly()); }
         * 
         * protected void enabledChanged() { fileComboBox.setEnabled(isEnabled() && !isReadOnly()); }
         * 
         * protected void valueModelChanged(Object newValue) { fileComboBox.setSelectedItem(newValue); } };
         */
    }

}
