package it.eurotn.rich.exception.handler;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.sicurezza.license.exception.InvalidLicenseException;

public class InvalidLicenseExceptionHandlerDelegate extends JecMessageDialogExceptionHandler {

    @Override
    public Object createExceptionContent(Throwable throwable) {

        InvalidLicenseException ex = (InvalidLicenseException) throwable;

        return RcpSupport.getMessage("", InvalidLicenseException.class.getName(), "description",
                new Object[] { DateFormatUtils.format(ex.getDataScadenza(), "dd MMMM yyyy") });
    }
}
