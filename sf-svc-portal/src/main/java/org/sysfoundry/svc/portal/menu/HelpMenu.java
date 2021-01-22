package org.sysfoundry.svc.portal.menu;

import org.apache.isis.applib.annotation.*;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.sysfoundry.svc.portal.Config;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.Map;

@DomainService(objectType = "sf.helpMenu")
@DomainServiceLayout(menuBar = DomainServiceLayout.MenuBar.TERTIARY)
public class HelpMenu {

    public static final String PUBLIC_URI = "public-uri";

    @Inject
    private Config portalConfig;

    @Inject
    private ConsulDiscoveryProperties consulDiscoveryProperties;

    @Action(semantics= SemanticsOf.SAFE)
    @ActionLayout(
            cssClassFa = "fa-question-circle"
    )
    public java.net.URL help() throws MalformedURLException {
        String helpURI = getHelpURI();
        if(helpURI == null){
            return new java.net.URL(getHelpNotFoundURI());
        }
        //get the public portal url from the consul discovery config metadata for the portal service
        return new java.net.URL(helpURI);
    }

    private String getHelpNotFoundURI(){
        Map<String, String> metadata = consulDiscoveryProperties.getMetadata();
        if(metadata.containsKey(PUBLIC_URI)){
            return String.format("%s/%s",metadata.get(PUBLIC_URI),portalConfig.getHelpNotFoundErrorPagePath());
        }
        return "";
    }

    private String getHelpURI(){
        Map<String, String> metadata = consulDiscoveryProperties.getMetadata();
        if(metadata.containsKey(PUBLIC_URI)){
            return String.format("%s%s/%s",metadata.get(PUBLIC_URI),portalConfig.getHelpPath(),"index.html");
        }
        return null;
    }
    /**
     * This method is used to conditionally disable the account menu
     * @return
     */
    public String disableHelp(){
        String helpURI = getHelpURI();
        return helpURI == null ? "" : null;
    }

}
