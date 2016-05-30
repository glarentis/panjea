package it.eurotn.panjea.rich.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import java.awt.Color;

import com.jidesoft.converter.ConverterContext;

public class ColorConverter extends PanjeaConverter {

    @Override
    public Object fromString(String source, ConverterContext arg1) {
        String colorString = source;

        if (colorString == null || colorString.isEmpty()) {
            return Color.black;
        }

        if (colorString.startsWith("#")) {
            colorString = colorString.substring(1);
        }
        colorString = colorString.toLowerCase();
        if (colorString.length() > 6) {
            throw new NumberFormatException("nm is not a 24 bit representation of the color, string too long");
        }
        Color color = new Color(Integer.parseInt(colorString, 16));
        return color;
    }

    @Override
    public Class<?> getClasse() {
        return Color.class;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public String toString(Object source, ConverterContext arg1) {
        if (source == null) {
            return "";
        }
        Color color = (Color) source;
        String colorString = Integer.toHexString(color.getRGB() & 0xFFFFFF);
        return ("#" + "000000".substring(colorString.length()) + colorString.toUpperCase());
    }

}
