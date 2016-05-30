package it.eurotn.panjea.rich.commands;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JViewport;
import javax.swing.Timer;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.springframework.core.io.Resource;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.ApplicationDialog;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.text.HtmlPane;
import org.springframework.util.FileCopyUtils;

public class AboutBox {
    protected class AboutDialog extends ApplicationDialog {
        private AboutBox.HtmlScroller scroller;

        public AboutDialog() {
            setTitle("About " + getApplicationName());
            setResizable(false);
            setCloseAction(CloseAction.DISPOSE);
        }

        @Override
        protected void addDialogComponents() {
            JComponent dialogContentPane = createDialogContentPane();
            getDialogContentPane().add(dialogContentPane);
            getDialogContentPane().add(createButtonBar(), "South");
        }

        @Override
        protected JComponent createDialogContentPane() {
            JPanel dialogPanel = new JPanel(new BorderLayout());

            if (AboutBox.this.aboutTextPath != null) {
                try {
                    this.scroller = new AboutBox.HtmlScroller(false, 2000, 45, 40);
                    String text = FileCopyUtils.copyToString(
                            new BufferedReader(new InputStreamReader(AboutBox.this.aboutTextPath.getInputStream())));
                    this.scroller.setHtml(text);
                } catch (IOException e) {
                    IllegalStateException exp = new IllegalStateException(
                            "About text not accessible: " + e.getMessage());
                    exp.setStackTrace(e.getStackTrace());
                    throw exp;
                }
                dialogPanel.add(this.scroller);
                dialogPanel.setPreferredSize(new Dimension(this.scroller.getPreferredSize().width, 300));
            } else {
                dialogPanel.setPreferredSize(new Dimension(300, 200));
            }
            dialogPanel.add(new JSeparator(), "South");
            return dialogPanel;
        }

        @Override
        protected Object[] getCommandGroupMembers() {
            return new AbstractCommand[] { getFinishCommand() };
        }

        protected AboutBox.HtmlScroller getHtmlScroller() {
            return this.scroller;
        }

        @Override
        protected void onAboutToShow() {
            if (this.scroller == null) {
                return;
            }
            try {
                String text = FileCopyUtils.copyToString(
                        new BufferedReader(new InputStreamReader(AboutBox.this.aboutTextPath.getInputStream())));
                this.scroller.setHtml(text);
            } catch (IOException e) {
                IllegalStateException exp = new IllegalStateException("About text not accessible: " + e.getMessage());
                exp.setStackTrace(e.getStackTrace());
                throw exp;
            }
            this.scroller.reset();
            this.scroller.startScrolling();
        }

        @Override
        protected boolean onFinish() {
            if (this.scroller != null) {
                this.scroller.pauseScrolling();
            }
            return true;
        }
    }

    protected static class HtmlScroller extends JViewport implements HyperlinkListener {
        private static final long serialVersionUID = -618721087361690658L;
        private HtmlPane htmlPane;
        private Timer timer;
        private int initalDelay;
        private double incY = 0.0D;

        private double currentY = 0.0D;

        private double currentX = 0.0D;

        public HtmlScroller(final boolean antiAlias, final int initalDelay, final int speedPixSec, final int fps) {
            this.initalDelay = initalDelay;

            this.incY = (speedPixSec / fps);

            this.htmlPane = new HtmlPane();
            this.htmlPane.setAntiAlias(antiAlias);
            this.htmlPane.addHyperlinkListener(this);
            setView(this.htmlPane);
            this.timer = new Timer(1000 / fps, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    int maxY = AboutBox.HtmlScroller.this.htmlPane.getHeight() - AboutBox.HtmlScroller.this.getHeight();
                    AboutBox.HtmlScroller.this.currentY = Math.max(0.0D,
                            Math.min(AboutBox.HtmlScroller.this.currentY + AboutBox.HtmlScroller.this.incY, maxY));
                    if ((AboutBox.HtmlScroller.this.currentY <= 0.0D)
                            || (AboutBox.HtmlScroller.this.currentY == maxY)) {
                        AboutBox.HtmlScroller.this.pauseScrolling();
                    }
                    AboutBox.HtmlScroller.this.setViewPositionInternal(new Point(
                            (int) AboutBox.HtmlScroller.this.currentX, (int) AboutBox.HtmlScroller.this.currentY));
                }
            });
            reset();
        }

        private void enteredLink() {
            pauseScrolling();
        }

        private void exitedLink() {
            startScrolling();
        }

        @Override
        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType().equals(HyperlinkEvent.EventType.ENTERED)) {
                enteredLink();
            } else if (event.getEventType().equals(HyperlinkEvent.EventType.EXITED)) {
                exitedLink();
            }
        }

        public void pauseScrolling() {
            this.timer.stop();
            this.timer.setInitialDelay(0);
        }

        public final void reset() {
            this.currentX = 0.0D;
            this.currentY = 0.0D;
            this.timer.setInitialDelay(this.initalDelay);
            setViewPositionInternal(new Point((int) this.currentX, (int) this.currentY));
        }

        public void setHtml(String html) {
            this.htmlPane.setText(html);
            setPreferredSize(this.htmlPane.getPreferredSize());
        }

        @Override
        public void setViewPosition(Point point) {
        }

        private void setViewPositionInternal(Point point) {
            super.setViewPosition(point);
        }

        public void startScrolling() {
            this.timer.start();
        }
    }

    private Resource aboutTextPath;

    /**
     * visualizza l'about
     *
     * @param parent
     *            parent
     */
    public void display(Window parent) {
        AboutDialog aboutMainDialog = new AboutDialog();
        aboutMainDialog.setParentComponent(parent);
        aboutMainDialog.showDialog();
    }

    /**
     *
     * @return aboutTextPath
     */
    public Resource getAboutTextPath() {
        return this.aboutTextPath;
    }

    /**
     *
     * @param path
     *            set path resource
     */
    public void setAboutTextPath(Resource path) {
        this.aboutTextPath = path;
    }
}