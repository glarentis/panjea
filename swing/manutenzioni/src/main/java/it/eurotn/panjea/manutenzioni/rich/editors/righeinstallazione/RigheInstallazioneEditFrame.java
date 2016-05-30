package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import org.springframework.richclient.command.ActionCommandInterceptor;

import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class RigheInstallazioneEditFrame extends EditFrame<RigaInstallazione> {

    private static final long serialVersionUID = 4316348363591680398L;

    /**
     *
     * @param editView
     *            editView
     * @param pageEditor
     *            pageEditor
     * @param startQuickAction
     *            start quick action
     */
    public RigheInstallazioneEditFrame(final EEditPageMode editView,
            final AbstractTablePageEditor<RigaInstallazione> pageEditor, final String startQuickAction) {
        super(editView, pageEditor, startQuickAction);
    }

    @Override
    protected ActionCommandInterceptor getDeleteCommandInterceptor(IPageEditor editPage) {
        return new DeleteActionCommandInterceptor(tableWidget, pageEditor);
    }

    @Override
    protected ActionCommandInterceptor getSaveCommandInterceptor(IPageEditor editPage) {
        return new SaveActionCommandInterceptor(tableWidget, editPage, this);
    }

}
