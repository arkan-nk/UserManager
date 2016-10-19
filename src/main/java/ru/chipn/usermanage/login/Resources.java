package ru.chipn.usermanage.login;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Resources {

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static String getCaption(String key) {
        if (properties==null) {
            try {
                properties = new Properties();
                InputStream is = Resources.class.getResourceAsStream("/config/rusmessages.properties");
                properties.load(is);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        return properties.getProperty(key);
    }
    private static Properties properties;
}
