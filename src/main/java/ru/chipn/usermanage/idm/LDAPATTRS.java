package ru.chipn.usermanage.idm;

/**
 * Created by arkan on 26.07.2016.
 */
public enum LDAPATTRS {
    NAME("name"), INETORGPERSON("inetOrgPerson"), ORGANIZATIONALPERSON("organizationalPerson"),
    CREATEDDATE("createdDate"), UNIQUEMEMBER("uniqueMember"), TITLE("title"), ORGANIZATIONNAME("o"),
    DESCRIPTION("description"), TELEPHONENUMBER("telephoneNumber"), HOMEPHONE("homePhone"),
    POSTALCODE("postalCode"), POSTALADDRESS("postalAddress");
    private String txt;
    public String getTxt(){
        return txt;
    }
    LDAPATTRS(String tt){
        txt = tt;
    }
}
