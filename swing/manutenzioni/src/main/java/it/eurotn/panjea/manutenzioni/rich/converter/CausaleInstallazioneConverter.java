package it.eurotn.panjea.manutenzioni.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class CausaleInstallazioneConverter extends PanjeaCompositeConverter<CausaleInstallazione>
        implements Comparator<CausaleInstallazione> {

    @Override
    public int compare(CausaleInstallazione o1, CausaleInstallazione o2) {
        return CausaleInstallazioneConverter.this.toString(o1, null)
                .compareTo(CausaleInstallazioneConverter.this.toString(o2, null));
    }

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(CausaleInstallazione value) {
        return value.getCodice();
    }

    @Override
    protected String getCampo2(CausaleInstallazione value) {
        return value.getDescrizione();
    }

    @Override
    public Class<CausaleInstallazione> getClasse() {
        return CausaleInstallazione.class;
    }

    @Override
    protected Comparator<CausaleInstallazione> getComparatorCampo1() {
        return this;
    }

    @Override
    protected Comparator<CausaleInstallazione> getComparatorCampo2() {
        return this;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }
}
