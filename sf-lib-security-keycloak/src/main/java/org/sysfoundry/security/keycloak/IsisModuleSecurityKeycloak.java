package org.sysfoundry.security.keycloak;

import org.sysfoundry.security.keycloak.isis.AuthenticatorKeycloak;
import org.sysfoundry.security.keycloak.isis.AuthorizerKeycloak;
import org.sysfoundry.security.keycloak.webmodule.WebModuleKeycloak;
import org.apache.isis.core.runtimeservices.IsisModuleCoreRuntimeServices;
import org.apache.isis.core.webapp.IsisModuleCoreWebapp;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        // modules
        IsisModuleCoreRuntimeServices.class,
        IsisModuleCoreWebapp.class,

        // @Service's
        AuthenticatorKeycloak.class,
        AuthorizerKeycloak.class,

        //The below module registers the necessary servlet filters
        WebModuleKeycloak.class,

        //This is for loading keycloak client config from the application configuration file
        Config.class


})
public class IsisModuleSecurityKeycloak {

    public static KeycloakDeployment globalKeycloakDeployment;
    //public static AdapterConfig globalKeycloakAdapterConfig;

    @Bean
    public KeycloakDeployment keycloakConfig(AdapterConfig adapterConfig){
        //InputStream inputStream = IsisKeycloakConfigResolver.class.getResourceAsStream("/config/keycloak.json");
        //KeycloakDeployment keycloakDeployment = KeycloakDeploymentBuilder.build(inputStream);
        KeycloakDeployment keycloakDeployment = KeycloakDeploymentBuilder.build(adapterConfig);
        globalKeycloakDeployment = keycloakDeployment;
        return keycloakDeployment;
    }

    //@Bean
    /*public AdapterConfig keycloakAdapterConfig(AdapterConfig adapterConfig){
        globalKeycloakAdapterConfig = adapterConfig;
        return adapterConfig;
    }*/
}
