package freemind.common;

import de.foltin.StringEncoder;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB XmlAdapter that encodes/decodes XML-invalid characters in strings,
 * equivalent to the JiBX StringEncoder format element.
 */
public class StringEncoderAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) {
        return StringEncoder.decode(v);
    }

    @Override
    public String marshal(String v) {
        return StringEncoder.encode(v);
    }
}
