package freemind.main;

import freemind.view.ImageFactory;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;

/**
 * Class to put a splash during launching the application.
 */

@Slf4j
public class FreeMindSplashModern extends JFrame implements IFreeMindSplash {

    private static final String FREEMIND_SPLASH = "images/Freemind_Splash_Butterfly_Modern.png";

    private static final int SPLASH_HEIGHT = 200;
    private static final int SPLASH_WIDTH = 300;

    private class FeedBackImpl implements FeedBack {

        private int mActualValue;
        private long mActualTimeStamp = System.currentTimeMillis();
        private long mTotalTime = 0;
        private String lastTaskId = null;
        private JLabel mImageJLabel = null;

        public void progress(final int act, String messageId, Object[] pMessageParameters) {
            MessageFormat formatter = new MessageFormat(
                    frame.getResourceString(messageId));
            final String progressString = formatter.format(pMessageParameters);
            log.trace(progressString);
            this.mActualValue = act;
            long timeDifference = System.currentTimeMillis() - mActualTimeStamp;
            mActualTimeStamp = System.currentTimeMillis();
            mTotalTime += timeDifference;
            log.trace("Task: {} ({}) last {} seconds.\nTotal: {}\n", lastTaskId, act, (timeDifference) / 1000.0, mTotalTime / 1000.0);
            SwingUtilities.invokeLater(() -> {
                mProgressBar.setValue(act);
                double percent = act * 1.0 / mProgressBar.getMaximum();
                mProgressBar.setString(progressString);
                if (mImageJLabel != null) {
                    mImageJLabel.putClientProperty("progressString",
                            progressString);
                    mImageJLabel.putClientProperty("progressPercent",
                            new Double(percent));
                    mImageJLabel.repaint();
                }
            });
            log.trace("Beginnig task:{}", messageId);
            lastTaskId = messageId;
            // this is not nice, as other windows are probably more important!
//			// make it the top most window.
//			FreeMindSplashModern.this.toFront();
        }

        public int getActualValue() {
            return mActualValue;
        }

        public void setMaximumValue(int max) {
            mProgressBar.setMaximum(max);
            mProgressBar.setIndeterminate(false);
        }

        public void increase(String messageId, Object[] pMessageParameters) {
            progress(getActualValue() + 1, messageId, pMessageParameters);
        }

        public void setImageJLabel(JLabel imageJLabel) {
            mImageJLabel = imageJLabel;
        }
    }

    private final FreeMindMain frame;
    private final FeedBackImpl feedBack; // !
    private final JProgressBar mProgressBar;
    private final ImageIcon mIcon;

    public FeedBack getFeedBack() {
        return feedBack;
    }

    public FreeMindSplashModern(final FreeMindMain frame) {
        super("FreeMind");
        this.frame = frame;

        this.feedBack = new FeedBackImpl();

        // http://www.kde-look.org/content/show.php?content=76812
        // License GPLV2+
        ImageFactory imageFactory = ImageFactory.getInstance();
        mIcon = imageFactory.createIcon(
                frame.getResource("images/76812-freemind_v0.4.png"));
        setIconImage(mIcon.getImage()); // Set the icon
        setDefaultLookAndFeelDecorated(false);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Set no border
        // lamentablemente since 1.5: setAlwaysOnTop(true);

        final ImageIcon splashImage = imageFactory.createIcon(frame.getResource(FREEMIND_SPLASH));
        JLabel splashImageLabel = new JLabel(splashImage) {
            private Integer mWidth = null;
            private final Font progressFont = new Font("SansSerif", Font.PLAIN, 10);
            private final Font versionTextFont = SwingUtils.isAvailableFontFamily("Century Gothic") ? new Font("Century Gothic", Font.BOLD, 14) : new Font("Arial", Font.BOLD, 12);

            private int calcYRelative(int y) {
                return (int) (((float) y) / SPLASH_HEIGHT * splashImage.getIconHeight());
            }

            private int calcXRelative(int x) {
                return (int) (((float) x) / SPLASH_WIDTH * splashImage.getIconWidth());
            }

            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(versionTextFont);
                // Determine width of string to center it
                String freemindVersion = frame.getFreemindVersion().toString();
                if (mWidth == null) {
                    mWidth = Integer.valueOf(g2.getFontMetrics().stringWidth(
                            freemindVersion));
                }
                int yCoordinate = calcYRelative(58);
                int xCoordinate = (int) (getSize().getWidth() / 2 - (double) mWidth
                        .intValue() / 2);
                g2.setColor(new Color(0x4d, 0x63, 0xb4));
                g2.drawString(freemindVersion, xCoordinate, yCoordinate);
                // Draw progress bar
                String progressString = (String) getClientProperty("progressString");
                if (progressString != null) {
                    Double percent = (Double) getClientProperty("progressPercent");
                    int xBase = calcXRelative(7);
                    int yBase = calcYRelative(185);
                    int width = calcXRelative(281);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    g2.setFont(progressFont);
                    // g2.setColor(new Color(0x80,0x80,0x80));
                    g2.setColor(new Color(0xff, 0xff, 0xff));
                    g2.drawString(progressString, xBase + calcXRelative(1), yBase - calcYRelative(4));
                    g2.setColor(new Color(0xc8, 0xdf, 0x8b));
                    g2.draw(new Rectangle(xBase + calcXRelative(2), yBase, width, calcYRelative(3)));
                    // g2.setColor(new Color(0xd0,0xd0,0xd0));
                    // g2.draw(new Rectangle(xBase+1, yBase+1, width, 2));
                    // g2.setColor(new Color(0xf4,0xf4,0xf4));
                    // g2.fill(new Rectangle(xBase+1, yBase+1, width-1, 2));
                    // g2.setColor(new Color(0x4d,0x63,0xb4));
                    g2.setColor(new Color(0xff, 0xff, 0xff));
                    g2.fill(new Rectangle(xBase + calcXRelative(1), yBase + calcYRelative(1),
                            (int) (width * percent.doubleValue()), calcYRelative(2)));
                }
            }
        };

        feedBack.setImageJLabel(splashImageLabel);

        getContentPane().add(splashImageLabel, BorderLayout.CENTER);

        mProgressBar = new JProgressBar();
        mProgressBar.setIndeterminate(true);
        mProgressBar.setStringPainted(true);

        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = splashImageLabel.getPreferredSize();

        // Put image at the middle of the screen
        setLocation(screenSize.width / 2 - (labelSize.width / 2),
                screenSize.height / 2 - (labelSize.height / 2));

    }

    public void close() {
        setVisible(false);
        dispose();
    }

    public ImageIcon getWindowIcon() {
        return mIcon;
    }

}
