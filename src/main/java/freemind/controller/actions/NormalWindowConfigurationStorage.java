
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" type="window_configuration_storage" name="normal_window_configuration_storage"/>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "normal_window_configuration_storage")
public class NormalWindowConfigurationStorage
        extends
            WindowConfigurationStorage
{
}
