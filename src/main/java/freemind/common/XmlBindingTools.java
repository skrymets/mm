package freemind.common;

import freemind.controller.Controller;
import freemind.controller.actions.*;
import freemind.main.Resources;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

;

/**
 * XML serialization facade using JAXB (Jakarta XML Binding).
 * Replaces previous JiBX-based implementation.
 */
@Slf4j
public class XmlBindingTools {

    private static XmlBindingTools instance;
    private static JAXBContext jaxbContext;

    private XmlBindingTools() {
    }

    public static XmlBindingTools getInstance() {
        if (instance == null) {
            instance = new XmlBindingTools();
            try {
                jaxbContext = JAXBContext.newInstance(
                        XmlAction.class,
                        MenuStructure.class,
                        Plugin.class,
                        MenuCategoryBase.class,
                        MenuSubmenu.class,
                        MenuAction.class,
                        MenuCheckedAction.class,
                        MenuRadioAction.class,
                        MenuSeparator.class,
                        PluginClasspath.class,
                        PluginRegistration.class,
                        PluginAction.class,
                        PluginStrings.class
                );
            } catch (JAXBException e) {
                log.error("Failed to initialize JAXB context", e);
            }
        }
        return instance;
    }

    public void storeDialogPositions(Controller controller, JDialog dialog, WindowConfigurationStorage storage, String window_preference_storage_property) {
        String result = storeDialogPositions(storage, dialog);
        controller.setProperty(window_preference_storage_property, result);
    }

    protected String storeDialogPositions(WindowConfigurationStorage storage, JDialog dialog) {
        storage.setX((dialog.getX()));
        storage.setY((dialog.getY()));
        storage.setWidth((dialog.getWidth()));
        storage.setHeight((dialog.getHeight()));
        return marshall(storage);
    }

    public WindowConfigurationStorage decorateDialog(Controller controller, JDialog dialog, String windowPreferenceStorageProperty) {
        String marshalled = controller.getProperty(windowPreferenceStorageProperty);
        return decorateDialog(marshalled, dialog, controller.getResources());
    }

    public WindowConfigurationStorage decorateDialog(String marshalled, JDialog dialog, Resources resources) {
        if (marshalled != null) {
            WindowConfigurationStorage storage = (WindowConfigurationStorage) unMarshall(marshalled);
            if (storage != null) {
                Dimension screenSize;
                if (resources.getBoolProperty("place_dialogs_on_first_screen")) {
                    Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                    screenSize = defaultToolkit.getScreenSize();
                } else {
                    screenSize = new Dimension();
                    screenSize.height = Integer.MAX_VALUE;
                    screenSize.width = Integer.MAX_VALUE;
                }
                int delta = 20;
                dialog.setLocation(Math.min(storage.getX(), screenSize.width - delta), Math.min(storage.getY(), screenSize.height - delta));
                dialog.setSize(new Dimension(storage.getWidth(), storage.getHeight()));
                return storage;
            }
        }

        final Frame rootFrame = JOptionPane.getFrameForComponent(dialog);
        final Dimension prefSize = rootFrame.getSize();
        prefSize.width = prefSize.width * 3 / 4;
        prefSize.height = prefSize.height * 3 / 4;
        dialog.setSize(prefSize);
        return null;
    }

    public String marshall(XmlAction action) {
        try {
            StringWriter writer = new StringWriter();
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(action, writer);
            return writer.toString();
        } catch (JAXBException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public XmlAction unMarshall(String inputString) {
        return unMarshall(new StringReader(inputString));
    }

    public XmlAction unMarshall(Reader reader) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (XmlAction) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public Object unMarshall(InputStream inputStream) {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
