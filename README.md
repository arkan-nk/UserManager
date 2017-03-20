# User Manager
Web application is core part for "security/access subsystem" for other project to manage users in LDAP. 
The subsystem is part of large enterprise JavaEE application which has name "ASCUPE-2.0". 
UserManager is service application for grant and revoke permissions for users to work with different part of "ASCUPE-2.0".

Each part of ASCUPE-2.0 can grant own set of user permission.    
User Manager has to deploy on JavaEE application server (we use WildFly) which must have intergation with LDAP. 
UserManager is web interface for LDAP directory.

Ldap-domain has a structure:
![domain](https://github.com/arkan-nk/UserManager/tree/master/doc/domain.png)


Admin of domain is **cn=admin,dc=a2,dc=chipn,dc=ru**
If user is member of role cn=fg_operator0,ou=Roles,dc=manage,dc=a2,dc=chipn,dc=ru
then that user is the **user_manager**.
In this case user_manager can authenticate and authorize in application and perform following operations:

 1. show all users in ou=Users, dc=a2,dc=chipn,dc=ru 
 2. add new user to ou=Users, dc=a2,dc=chipn,dc=ru 
 3. edit some attributes of *selected user* (cn, sn, telephoneNumber, postalAddress and others) 
 4. replace password of *selected user* 
 5. put *selected user* to any group in ldap except сn=fg_operator0, ou=Roles, dc=manage, dc=a2, dc=chipn, dc=ru 
 6. remove *seleted user* from any group in ldap except  cn=fg_operator0,ou=Roles,dc=manage,dc=a2,dc=chipn,dc=ru 
 7. remove *seleted user* from ldap if user have no membership in any group

Domain gets configuration from web.xml deployment descriptor. 
Frameworks picketlink, atmosphere and primefaces removes from artifact and joins to modules in wildfly application server.
Framework apache deltaspike are built with scope compile.
Dependencies provided explicitly by jboss-deployment-structure.xml 

Application has developed by JDK8u101, PicketLink-2.7.1.Final, CDI, JSF-2.2.12,
Primefaces-6.0, OpenLDAP, and can deploy to JBoss Application Server
(WildFly-9.0.2.Final or WildFly-10.1.0.Final) with standalone.xml as sample configuration.
