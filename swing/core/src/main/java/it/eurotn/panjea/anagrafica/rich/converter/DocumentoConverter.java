package it.eurotn.panjea.anagrafica.rich.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.rich.converter.PanjeaConverter;

public class DocumentoConverter extends PanjeaConverter<Documento> {

    public static final ConverterContext DOCUMENTO_ABBREVIATO_CONVERTER_CONTEXT = new ConverterContext(
            "documentoCodiceContext", "documentoAbbreviato");

    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<Documento> getClasse() {
        return Documento.class;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public String toString(Object value, ConverterContext context) {
        String result = "";

        if (value != null && value instanceof Documento) {
            Documento doc = (Documento) value;
            StringBuffer sb = new StringBuffer();
            if (context == null || context.getUserObject() == null) {
                if (doc.getTipoDocumento() != null && doc.getTipoDocumento().getCodice() != null
                        && !doc.getTipoDocumento().getCodice().isEmpty()) {
                    sb.append(doc.getTipoDocumento().getCodice());
                    sb.append(" n° " + ObjectConverterManager.toString(doc.getCodice()));
                    sb.append(" del " + ObjectConverterManager.toString(doc.getDataDocumento()));
                }
            } else if (context.getUserObject() != null
                    && "documentoAbbreviato".equals(context.getUserObject().toString())) {
                if (doc.getTipoDocumento() != null && !doc.getTipoDocumento().getCodice().isEmpty()) {
                    sb.append(doc.getTipoDocumento().getCodice());
                }
                sb.append(" numero " + ObjectConverterManager.toString(doc.getCodice()));
            } else if (context.getUserObject() != null && "dataDocumento".equals(context.getUserObject().toString())) {
                sb.append(dateFormat.format(doc.getDataDocumento()));
            } else if (context.getUserObject() != null && "codice".equals(context.getUserObject().toString())) {
                sb.append(ObjectConverterManager.toString(doc.getCodice()));
            }
            result = sb.toString();
        }
        return result;
    }
}
