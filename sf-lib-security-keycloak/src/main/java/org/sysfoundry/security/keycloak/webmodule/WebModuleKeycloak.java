package org.sysfoundry.security.keycloak.webmodule;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.KeycloakDeployment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import org.apache.isis.applib.annotation.OrderPrecedence;
import org.apache.isis.applib.services.inject.ServiceInjector;
import org.apache.isis.commons.collections.Can;
import org.apache.isis.core.webapp.modules.WebModuleAbstract;

import lombok.Getter;

/**
 * WebModule to enable support for Keycloak.
 */
@Service
@Named("security.WebModuleCustom")
@Order(OrderPrecedence.FIRST + 100)
//@Order(OrderPrecedence.FIRST)
@Qualifier("Custom")
@Slf4j
public final class WebModuleKeycloak extends WebModuleAbstract {

    private static final String KEYCLOAK_FILTER_NAME = "KeycloakFilter";

    @Getter
    private final String name = "Keycloak";

    @Inject
    public WebModuleKeycloak(ServiceInjector serviceInjector,
                             //note ensure that the KeycloakDeployment is injected to ensure that the
                             //static globalkeycloakdeployment object is initialized
                             KeycloakDeployment keycloakDeployment//,
                             //to ensure that the AdapterConfig is injected in the global static constant
                             //AdapterConfig keycloakAdapterConfig
                             ) {
        super(serviceInjector);

        /*log.info("$$$$$$$$Adapter Config {}",keycloakAdapterConfig);
        log.info("Security Realm {}",keycloakAdapterConfig.getRealm());
        log.info("auth-server-url {}",keycloakAdapterConfig.getAuthServerUrl());*/
    }


    @Override
    public Can<ServletContextListener> init(ServletContext ctx) throws ServletException {

        log.info("USUAL ORDER ADDED ONE MORE NEEE INSIDE Keycloak web module init...");
        registerFilter(ctx, KEYCLOAK_FILTER_NAME, KeycloakOIDCFilterWrapper.class)
                .ifPresent(filterReg -> {
                    filterReg.addMappingForUrlPatterns(
                            null,
                            false, // filter is forced first
                            "/wicket/signin","/keycloak/*");
                    filterReg.setInitParameter("keycloak.config.resolver", IsisKeycloakConfigResolver.class.getName());

                });

        //log.info("Registering the Session creator filter for keycloak...");
        /*registerFilter(ctx, KEYCLOAK_FILTER_NAME+"2", MyCustomFilter.class)
                .ifPresent(filterReg -> {
                    filterReg.addMappingForUrlPatterns(
                            null,
                            false, // filter is forced first
                            "/wicket/signin");
                    //filterReg.setInitParameter("keycloak.config.path","/config/keycloak.json");
                    //filterReg.setInitParameter("keycloak.config.resolver",CustomKeycloakConfigResolver.class.getName());

                });*/

        return Can.empty(); // registers no listeners
    }

}
