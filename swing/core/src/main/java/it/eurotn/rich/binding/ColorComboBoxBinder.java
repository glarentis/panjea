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

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.richclient.form.binding.support.CustomBinding;

import com.jidesoft.combobox.ColorComboBox;

/**
 * Binder for the JIDE ColorComboBox component
 * 
 * @author Jonny Wray
 * 
 */
public class ColorComboBoxBinder extends AbstractBinder {

    public ColorComboBoxBinder() {
        super(Color.class);

    }

    @Override
    protected JComponent createControl(Map arg0) {
        return new ColorComboBox();
    }

    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        final ColorComboBox colorComboBox = (ColorComboBox) control;
        return new CustomBinding(formModel, formPropertyPath, Color.class) {

            @Override
            protected JComponent doBindControl() {
                colorComboBox.setSelectedColor((Color) getValue());
                colorComboBox.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            controlValueChanged(colorComboBox.getSelectedColor());
                        }
                    }
                });
                return colorComboBox;
            }

            @Override
            protected void enabledChanged() {
                colorComboBox.setEnabled(isEnabled() && !isReadOnly());
            }

            @Override
            protected void readOnlyChanged() {
                colorComboBox.setEnabled(isEnabled() && !isReadOnly());
            }

            @Override
            protected void valueModelChanged(Object newValue) {
                colorComboBox.setSelectedColor((Color) newValue);
            }
        };
    }

}
