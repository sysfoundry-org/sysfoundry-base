package org.sysfoundry.security.keycloak.webmodule;

import org.sysfoundry.security.keycloak.IsisModuleSecurityKeycloak;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
//import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;

//import java.io.InputStream;

public class IsisKeycloakConfigResolver implements KeycloakConfigResolver {
    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {
        //InputStream inputStream = IsisKeycloakConfigResolver.class.getResourceAsStream("/config/keycloak.json");
        //return KeycloakDeploymentBuilder.build(inputStream);
        //just return the global keycloak deployment instance here
        return IsisModuleSecurityKeycloak.globalKeycloakDeployment;
    }
}
