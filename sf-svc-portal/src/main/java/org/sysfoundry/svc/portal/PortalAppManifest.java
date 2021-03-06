package org.sysfoundry.svc.portal;

import org.sysfoundry.configkeys.SFModuleConfigKeys;
import org.sysfoundry.svc.portal.health.HealthCheckServiceImpl;
import org.sysfoundry.svc.portal.impl.PortalServiceImpl;
import org.sysfoundry.svc.portal.menu.AccountMenu;
import org.sysfoundry.svc.portal.vmod.PortalModule;
import org.sysfoundry.security.keycloak.IsisModuleSecurityKeycloak;
import org.apache.isis.core.config.presets.IsisPresets;
import org.apache.isis.core.runtimeservices.IsisModuleCoreRuntimeServices;
//import org.apache.isis.extensions.flyway.impl.IsisModuleExtFlywayImpl;
import org.apache.isis.persistence.jdo.datanucleus5.IsisModuleJdoDataNucleus5;
import org.apache.isis.testing.fixtures.applib.IsisModuleTestingFixturesApplib;
//import org.apache.isis.testing.h2console.ui.IsisModuleTestingH2ConsoleUi;
import org.apache.isis.viewer.restfulobjects.jaxrsresteasy4.IsisModuleViewerRestfulObjectsJaxrsResteasy4;
import org.apache.isis.viewer.wicket.viewer.IsisModuleViewerWicketViewer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@Configuration
@Import({
        IsisModuleCoreRuntimeServices.class,
        IsisModuleSecurityKeycloak.class,
        IsisModuleJdoDataNucleus5.class,
        IsisModuleViewerRestfulObjectsJaxrsResteasy4.class,
        IsisModuleViewerWicketViewer.class,

        IsisModuleTestingFixturesApplib.class,
        //IsisModuleTestingH2ConsoleUi.class,

        //IsisModuleExtFlywayImpl.class,

        PortalModule.class,
        HealthCheckServiceImpl.class,
        AccountMenu.class,
        PortalServiceImpl.class,
        Config.class,

        //ApplicationModule.class,

        // discoverable fixtures
        //DomainAppDemo.class

        //Include the config key module
        SFModuleConfigKeys.class
})
@PropertySources({
        @PropertySource(IsisPresets.DebugDiscovery),
})
public class PortalAppManifest {
}
