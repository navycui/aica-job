package bnet.library.util.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbCDataAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        return "<![CDATA[" + v + "]]>";
    }
}
