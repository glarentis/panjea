package it.eurotn.rich.editors;

import org.springframework.richclient.command.AbstractCommand;

public interface IToolbarPageCommands {

    /**
     * @return the externalCommandStart
     */
    AbstractCommand[] getExternalCommandStart();

    /**
     * @param externalCommandAppend
     *            the externalCommandAppend to set
     */
    void setExternalCommandAppend(AbstractCommand[] externalCommandAppend);

    /**
     * @param externalCommandLineEnd
     *            the externalCommandLineEnd to set
     */
    void setExternalCommandLineEnd(AbstractCommand[] externalCommandLineEnd);

    /**
     * @param externalCommandStart
     *            the externalCommandStart to set
     */
    void setExternalCommandStart(AbstractCommand[] externalCommandStart);

}