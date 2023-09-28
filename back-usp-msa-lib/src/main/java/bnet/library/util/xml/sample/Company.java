package bnet.library.util.xml.sample;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import bnet.library.util.xml.JaxbCDataAdapter;

@XmlRootElement(name = "Company")
@XmlAccessorType(XmlAccessType.FIELD)
public class Company {
    @XmlJavaTypeAdapter(value=JaxbCDataAdapter.class)
    @XmlElement(name = "COMPANY_NAME")
    private String companyName = "Bakery 한글 & 테스트";

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String bar) { this.companyName = bar; }

//    public static void main(String[] args) throws Exception {
//        String v = xml.marshal(new Company(), "EUC-KR");
//        System.out.println(v);
//        Company c = xml.unmarshal(v, Company.class);
//        System.out.println("RESULT:" + c);
//    }
}
