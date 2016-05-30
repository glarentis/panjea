package it.eurotn.rich.binding;

/*
 * Copyright 2002-2004 the original author or authors.
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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.richclient.form.binding.support.CustomBinding;

/**
 * Binds <code>Enum</code> values to a group of <code>JRadioButton</code>s.
 * 
 * @author Prashant Bhat (prashant dot mr at gmail dot com)
 * @version 0.1
 */
public class EnumRadioButtonBinder extends AbstractBinder {
    /**
     * Binds <code>Enum</code> values to a group of <code>JRadioButton</code>s.
     * 
     * @see EnumRadioButtonBinder
     * @author Prashant Bhat (prashant dot mr at gmail dot com)
     * @version 0.1
     */
    private static class EnumRadioButtonBinding extends CustomBinding implements ActionListener {
        private static final String ENUM_VALUE = "enum-value";
        private ButtonGroup buttonGroup = new ButtonGroup();
        private JComponent control;

        /**
         * Binding Constructor with form model and property to be bound.
         * 
         * @param control
         *            the panel control
         * @param formModel
         *            the form model in which this binding is used
         * @param formPropertyPath
         *            the property to which this component is bound
         * @param requiredSourceClass
         *            the property class
         */
        protected EnumRadioButtonBinding(final JComponent control, final FormModel formModel,
                final String formPropertyPath, final Class<?> requiredSourceClass) {
            super(formModel, formPropertyPath, requiredSourceClass);
            this.control = control;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            controlValueChanged(abstractButton.getClientProperty(ENUM_VALUE));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        protected JComponent doBindControl() {
            @SuppressWarnings("rawtypes")
            Class<Enum> enumPropertyType = getPropertyType();
            Enum<?>[] selectableItems = enumPropertyType.getEnumConstants();
            for (Enum<?> valueEnum : selectableItems) {
                String text = getMessage(valueEnum.getClass().getName() + "." + valueEnum.name());
                JRadioButton radioButton = new JRadioButton(text);
                radioButton.addActionListener(this);
                radioButton.putClientProperty(ENUM_VALUE, valueEnum);
                radioButton.setName(getFormModel().getId() + "." + valueEnum.getDeclaringClass().getSimpleName() + "."
                        + valueEnum.name());
                control.add(radioButton);
                buttonGroup.add(radioButton);
            }
            valueModelChanged(getValue());
            return control;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void enabledChanged() {
            Enumeration<AbstractButton> radioButtons = buttonGroup.getElements();
            while (radioButtons.hasMoreElements()) {
                radioButtons.nextElement().setEnabled(!isReadOnly());
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void readOnlyChanged() {
            Enumeration<AbstractButton> radioButtons = buttonGroup.getElements();
            while (radioButtons.hasMoreElements()) {
                radioButtons.nextElement().setEnabled(!isReadOnly());
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void valueModelChanged(Object newValue) {
            Enumeration<AbstractButton> radioButtons = buttonGroup.getElements();
            while (radioButtons.hasMoreElements()) {
                AbstractButton abstractButton = radioButtons.nextElement();
                Object enumValue = abstractButton.getClientProperty(ENUM_VALUE);
                if (enumValue.equals(newValue)) {
                    abstractButton.setSelected(true);
                    return;
                }
            }
        }
    }

    private int layout;

    /**
     * Costruttore.
     * 
     * @param requiredSourceClass
     *            classe
     */
    public EnumRadioButtonBinder(final Class<?> requiredSourceClass) {
        super(requiredSourceClass);
    }

    /**
     * Costruttore.
     * 
     * @param requiredSourceClass
     *            classe
     * @param layout
     *            tipo di layout {@link SwingConstants#VERTICAL}, {@link SwingConstants#HORIZONTAL}
     */
    public EnumRadioButtonBinder(final Class<?> requiredSourceClass, final int layout) {
        super(requiredSourceClass);
        this.layout = layout;
    }

    /**
     * Bind the form model's given property to this JRadioButton.
     * 
     * @param formModel
     *            the form model
     * @param formPropertyPath
     *            the name of the property to bind
     * @return the actual binding for this binder
     */
    public Binding bind(FormModel formModel, String formPropertyPath) {
        return bind(formModel, formPropertyPath, new HashMap<Object, Object>());
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected JComponent createControl(Map context) {
        JPanel panel = new JPanel();
        if (layout == SwingConstants.VERTICAL) {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        } else {// default horizontal layout
            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        }
        return panel;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        EnumRadioButtonBinding binding = new EnumRadioButtonBinding(control, formModel, formPropertyPath,
                getRequiredSourceClass());
        // applyContext(binding, context);
        return binding;
    }

    /**
     * @return Returns the layout.
     */
    public int getLayout() {
        return layout;
    }

    /**
     * @param layout
     *            The layout to set.
     */
    public void setLayout(int layout) {
        this.layout = layout;
    }
}
