package ru.chipn.usermanage.login;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * Created by arkan on 30.08.2016.
 */
@Startup
@Singleton
public class SecurityInitializer {
    @Inject
    private PartitionManager partitionManager;


    //@PostConstruct
    public void init(){
        boolean tt =(this.partitionManager.getConfigurations()!=null);
        //this.partitionManager.createIdentityManager();
        //this.partitionManager.createRelationshipManager();
    }
}