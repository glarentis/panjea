package it.eurotn.rich.report.jasperserver.hyperlink;

import java.util.List;

import net.sf.jasperreports.engine.JRPrintHyperlinkParameter;

public interface IHyperlinkExecutor {

    void execute(List<JRPrintHyperlinkParameter> list);
}
