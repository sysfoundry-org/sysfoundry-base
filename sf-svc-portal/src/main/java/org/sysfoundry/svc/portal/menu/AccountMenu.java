package org.sysfoundry.svc.portal.menu;

import org.sysfoundry.svc.portal.Config;
import org.sysfoundry.svc.portal.api.PortalService;
import org.sysfoundry.svc.portal.impl.PortalServiceImpl;
import org.apache.isis.applib.annotation.*;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.Optional;

@DomainService(objectType = "sf.accountMenu")
@DomainServiceLayout(menuBar = DomainServiceLayout.MenuBar.TERTIARY)
public class AccountMenu {

    @Inject
    private PortalService portalService;

    @Inject
    private Config portalConfig;

    @Action(semantics= SemanticsOf.SAFE)
    @ActionLayout(
            cssClassFa = "fa-user"
    )
    public java.net.URL myAccount() throws MalformedURLException {
        return new java.net.URL(getLink());
    }

    private String getLink() {
        Optional<PortalServiceImpl.ServiceInfo> serviceInfoOptional = portalService.getServiceInfo(portalConfig.getAccountAppName());
        if(serviceInfoOptional.isPresent()){
            PortalServiceImpl.ServiceInfo serviceInfo = serviceInfoOptional.get();
            if(serviceInfo.isAvailable() && serviceInfo.hasGui()){
                return serviceInfoOptional.get().getLocation();
            }
        }
        return PortalService.STATUS_UNAVAILABLE;
    }

    /**
     * This method is used to conditionally disable the account menu
     * @return
     */
    public String disableMyAccount(){
        String statusOfAccount = getLink();
        if(!PortalService.STATUS_UNAVAILABLE.equalsIgnoreCase(statusOfAccount)){
            return null;
        }
        return statusOfAccount;
    }
}
