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

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.AbstractListBinder;
import org.springframework.richclient.form.binding.swing.AbstractListBinding;
import org.springframework.util.Assert;

import com.jidesoft.swing.CheckBoxList;

/**
 * Binder for the JIDE check box list with selectable.
 * 
 * This is a copy, paste and modification from the Spring RCP ListBinder.
 * 
 * @author Jonny Wray
 * 
 */
public class CheckBoxListSelectableBinder extends AbstractListBinder {

    public static final String RENDERER_KEY = "renderer";
    public static final String LAYOUT_ORIENTATION = "layoutOrientation";

    public static final String SELECTION_MODE_KEY = "selectionMode";

    private ListCellRenderer renderer;

    private Integer selectionMode;

    public CheckBoxListSelectableBinder() {
        this(null, new String[] { SELECTABLE_ITEMS_KEY, COMPARATOR_KEY, RENDERER_KEY, FILTER_KEY, SELECTION_MODE_KEY });
    }

    public CheckBoxListSelectableBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    public CheckBoxListSelectableBinder(String[] supportedContextKeys) {
        this(null, supportedContextKeys);
    }

    @Override
    protected void applyContext(AbstractListBinding binding, Map context) {
        super.applyContext(binding, context);
        CheckBoxListSelectableBinding listBinding = (CheckBoxListSelectableBinding) binding;
        if (context.containsKey(RENDERER_KEY)) {
            listBinding.setRenderer((ListCellRenderer) decorate(context.get(RENDERER_KEY), listBinding.getRenderer()));
        } else if (renderer != null) {
            listBinding.setRenderer((ListCellRenderer) decorate(renderer, listBinding.getRenderer()));
        }
        if (context.containsKey(SELECTION_MODE_KEY)) {
            Object contextSelectionMode = context.get(SELECTION_MODE_KEY);
            if (contextSelectionMode instanceof Integer) {
                listBinding.setSelectionMode(((Integer) contextSelectionMode).intValue());
            } else {
                try {
                    listBinding.setSelectionMode(
                            ((Integer) ListSelectionModel.class.getField((String) contextSelectionMode).get(null))
                                    .intValue());
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException("Unable to access selection mode field in ListSelectionModel",
                            e);
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException("Unknown selection mode '" + contextSelectionMode + "'", e);
                }
            }
        } else if (selectionMode != null) {
            listBinding.setSelectionMode(selectionMode.intValue());
        }
    }

    protected void applyContext(CheckBoxListSelectableBinding binding, Map context) {
        if (context.containsKey(SELECTION_MODE_KEY)) {
        }
    }

    @Override
    protected JComponent createControl(Map context) {
        CheckBoxList boxListWithSelectable = new CheckBoxList();
        // faccio qui il controllo sulla proprieta' layout orientation perche'
        // devo modificare il componente
        if (context.containsKey(LAYOUT_ORIENTATION)) {
            int componentOrientation = (Integer) context.get(LAYOUT_ORIENTATION);
            boxListWithSelectable.setLayoutOrientation(componentOrientation);
            if (componentOrientation == JList.HORIZONTAL_WRAP) {
                // se HORIZONTAL devo forzare una singola riga
                boxListWithSelectable.setVisibleRowCount(1);
            }
        }

        boxListWithSelectable.setCheckBoxEnabled(true);
        return boxListWithSelectable;
    }

    @Override
    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {
        Assert.isInstanceOf(CheckBoxList.class, control);
        return new CheckBoxListSelectableBinding((CheckBoxList) control, formModel, formPropertyPath,
                getRequiredSourceClass());
    }

    public ListCellRenderer getRenderer() {
        return renderer;
    }

    public Integer getSelectionMode() {
        return selectionMode;
    }

    public void setRenderer(ListCellRenderer renderer) {
        this.renderer = renderer;
    }

    public void setSelectionMode(Integer selectionMode) {
        this.selectionMode = selectionMode;
    }
}
