package freemind.controller.services;

import freemind.main.FreeMindMain;
import freemind.main.Tools;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Manages printer job acquisition, page format configuration, and page format persistence.
 */
@Slf4j
public class PrintService {

    private static final String PAGE_FORMAT_PROPERTY = "page_format";

    private final FreeMindMain frame;

    @Getter
    @Setter
    private PageFormat pageFormat = null;

    @Getter
    private PrinterJob printerJob = null;

    @Getter
    private boolean printingAllowed = true;

    public PrintService(FreeMindMain frame) {
        this.frame = frame;
    }

    /**
     * Lazily initializes the printer job and page format. Reads orientation and paper
     * settings from stored properties.
     *
     * @return true if printing is available, false if a SecurityException was caught
     */
    public boolean acquirePrinterJobAndPageFormat() {
        if (printerJob == null) {
            try {
                printerJob = PrinterJob.getPrinterJob();
            } catch (SecurityException ex) {
                printingAllowed = false;
                return false;
            }
        }
        if (pageFormat == null) {
            pageFormat = printerJob.defaultPage();
        }
        if (Objects.equals(frame.getProperty("page_orientation"), "landscape")) {
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
        } else if (Objects.equals(frame.getProperty("page_orientation"), "portrait")) {
            pageFormat.setOrientation(PageFormat.PORTRAIT);
        } else if (Objects.equals(frame.getProperty("page_orientation"), "reverse_landscape")) {
            pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        }
        String pageFormatProperty = frame.getProperty(PAGE_FORMAT_PROPERTY);
        if (pageFormatProperty != null && !pageFormatProperty.isEmpty()) {
            log.info("Page format (stored): {}", pageFormatProperty);
            final Paper storedPaper = new Paper();
            Tools.setPageFormatFromString(storedPaper, pageFormatProperty);
            pageFormat.setPaper(storedPaper);
        }
        return true;
    }

    /**
     * Stores the current page format orientation and paper dimensions using the provided
     * property setter. The property setter is typically {@code Controller::setProperty},
     * which also fires property change listeners.
     *
     * @param propertySetter a function that accepts (key, value) to persist properties
     */
    public void storePageFormat(BiConsumer<String, String> propertySetter) {
        switch (pageFormat.getOrientation()) {
            case PageFormat.LANDSCAPE:
                propertySetter.accept("page_orientation", "landscape");
                break;
            case PageFormat.PORTRAIT:
                propertySetter.accept("page_orientation", "portrait");
                break;
            case PageFormat.REVERSE_LANDSCAPE:
                propertySetter.accept("page_orientation", "reverse_landscape");
                break;
        }
        propertySetter.accept(PAGE_FORMAT_PROPERTY, Tools.getPageFormatAsString(pageFormat.getPaper()));
    }
}
