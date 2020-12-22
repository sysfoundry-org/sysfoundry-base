package org.sysfoundry.svc.portal.vmod;

import org.sysfoundry.svc.portal.Config;
import org.sysfoundry.svc.portal.api.PortalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.HomePage;
import org.apache.isis.applib.annotation.Nature;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import java.util.List;

@DomainObject(nature = Nature.VIEW_MODEL,
        objectType = "sf.portal.PortalHomePageViewModel"
)
@DomainObjectLayout
@HomePage
@Slf4j
public class PortalHomepageViewModel {


    @Inject
    private PortalService portalService;

    @Inject
    private Config portalConfig;

    @Value("${spring.application.name}")
    private String appName;


    public List<AppObject> getApps(){
        return portalService.listApps(portalConfig.isViewUnavailableApps(),
                portalConfig.isViewAppsWithGui(),appName);
    }

    public String title(){
        return String.format("%s - App Dashboard",appName);
    }
}
