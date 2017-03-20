package ru.chipn.usermanage.login;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Resources {

    private static Properties properties;
    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static String getCaption(String key) {
        if (properties==null) properties = new Properties();
        if (!properties.contains(key))
            try {
                InputStream is = Resources.class.getResourceAsStream("/config/rusmessages.properties");
                properties.load(is);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        return properties.getProperty(key);
    }
    public static String getParam(String key){
    	String value=FacesContext.getCurrentInstance().getExternalContext().getInitParameter(key);
        if (properties==null) properties = new Properties();
        properties.put(key, value);
        return value;
    }

}
