package it.eurotn.rich.binding;

import java.util.Arrays;
import java.util.Map;

import org.springframework.richclient.form.binding.swing.AbstractListBinding;

/**
 * Binder per selezionare degli elementi tramite una lista partendo da un tipo ENUM.
 * 
 * @author giangi
 */
public class TigerEnumCheckBoxListBinder extends CheckBoxListSelectableBinder {

    public TigerEnumCheckBoxListBinder() {
        this(null,
                new String[] { "selectableItems", "comparator", "renderer", "filter", "selectionMode", "enumClass" });
    }

    public TigerEnumCheckBoxListBinder(Class requiredSourceClass, String[] supportedContextKeys) {
        super(requiredSourceClass, supportedContextKeys);
    }

    public TigerEnumCheckBoxListBinder(String[] supportedContextKeys) {
        this(null, supportedContextKeys);
    }

    @Override
    protected void applyContext(AbstractListBinding binding, Map context) {
        super.applyContext(binding, context);
        org.springframework.util.Assert.isTrue(context.containsKey("enumClass"));
        Class enumTypeClass = (Class) context.get("enumClass");
        ((CheckBoxListSelectableBinding) binding).setRenderer(new CustomEnumListRenderer(getMessages()));
        binding.setSelectableItems(Arrays.asList(createEnumSelectableItems(enumTypeClass)));
    }

    private Enum[] createEnumSelectableItems(Class enumTypeClass) {
        return (Enum[]) enumTypeClass.getEnumConstants();
    }
}
