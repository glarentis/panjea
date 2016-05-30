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

import it.eurotn.panjea.JecTreeSearchable;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.util.Assert;

import com.jidesoft.swing.CheckBoxTree;

/**
 * Binder for the JIDE check box tree.
 *
 * @author Jonny Wray
 *
 */
public class CheckBoxTreeBinder extends AbstractBinder {

    public static final String GROUPABLE_PROPERTIES = "GroupableProperties";
    public static final String SELECTED_ITEM_HOLDER_KEY = "selectedItemHolder";
    public static final String RENDERER_KEY = "renderer";
    public static final String TABLE_MODEL_KEY = "tableModelKey";

    /**
     *
     * Costruttore.
     */
    public CheckBoxTreeBinder() {
        super(null, new String[] { GROUPABLE_PROPERTIES, SELECTED_ITEM_HOLDER_KEY, RENDERER_KEY, TABLE_MODEL_KEY });
    }

    /**
     * Applica i parametri del context.
     *
     * @param binding
     *            binding del controllo
     * @param context
     *            context con i valori da applicare
     */
    @SuppressWarnings("rawtypes")
    protected void applyContext(CheckBoxTreeBinding binding, Map context) {
        if (context.containsKey(GROUPABLE_PROPERTIES)) {
            binding.setGroupableProperty((String[]) context.get(GROUPABLE_PROPERTIES));
        }
        if (context.containsKey(SELECTED_ITEM_HOLDER_KEY)) {
            binding.setSelectedItemHolder((ValueModel) context.get(SELECTED_ITEM_HOLDER_KEY));
        }
        if (context.containsKey(RENDERER_KEY)) {
            binding.setRenderer((TreeCellRenderer) context.get(RENDERER_KEY));
        }
        if (context.containsKey(TABLE_MODEL_KEY)) {
            binding.setModel((TreeModel) context.get(TABLE_MODEL_KEY));
        }
    }

    @Override
    protected JComponent createControl(@SuppressWarnings("rawtypes") Map arg0) {
        CheckBoxTree tree = new CheckBoxTree() {
            private static final long serialVersionUID = 52226519744023742L;

            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(250, 100);
            }
        };
        return tree;
    }

    @Override
    protected Binding doBind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        Assert.isTrue(control instanceof CheckBoxTree, formPropertyPath);
        CheckBoxTree list = (CheckBoxTree) control;
        new JecTreeSearchable(list, formModel);
        CheckBoxTreeBinding binding = new CheckBoxTreeBinding(list, formModel, formPropertyPath);
        applyContext(binding, context);
        return binding;
    }

}
