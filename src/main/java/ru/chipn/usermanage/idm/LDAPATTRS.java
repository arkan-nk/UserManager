package ru.chipn.usermanage.idm;

/**
 * Attributes in LDAP
 */
public enum LDAPATTRS {
    NAME("name"), INETORGPERSON("inetOrgPerson"), PERSON("person"), POSIXACCOUNT("posixAccount"),
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
