package org.sysfoundry.svc.portal.impl;

import org.sysfoundry.svc.portal.api.PortalService;
import org.sysfoundry.svc.portal.vmod.AppObject;
import org.apache.isis.applib.services.user.ResourceMemento;
import org.apache.isis.applib.services.user.UserMemento;
import org.apache.isis.applib.services.user.UserService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service
public class PortalServiceImpl implements PortalService {

    public static String urlFormat = "<a href=\"%s\" target=\"_blank\">Open</a>";


    @Inject
    private UserService userService;

    @Inject
    private DiscoveryClient discoveryClient;


    @lombok.Value
    public static class ServiceInfo{
        private String name;
        private String location;
        private String status;
        private boolean gui;

        public boolean isAvailable(){
            return status.equalsIgnoreCase(STATUS_AVAILABLE);
        }

        public boolean hasGui() {
            return isGui();
        }
    }

    @Override
    public List<AppObject> listApps(boolean viewUnavailableApps, boolean viewAppsWithGui, String... exceptions) {

        //TODO: We can think of refreshing it if the there is a new service visible if not this information can be cached for efficiency
        //

        Optional<UserMemento> optionalCurrentUser = userService.currentUser();
        List<String> exceptionAppList = Arrays.asList(exceptions);

        List<AppObject> accessibleApps = new ArrayList<>();
        //TODO: Probably think about providing support for Apps accessible as unauthenticated user

        if(optionalCurrentUser.isPresent()){
            UserMemento userMementoObj = optionalCurrentUser.get();
            List<ResourceMemento> resources = userMementoObj.getResources();
            for (ResourceMemento resource : resources) {
                String appName = resource.getName();
                if (!exceptionAppList.contains(appName)) { //filter the exception apps
                    String description = resource.getDescription();
                    Optional<ServiceInfo> serviceInfoOptional = getServiceInfo(appName);
                    if(serviceInfoOptional.isPresent()) {
                        ServiceInfo serviceInfo = serviceInfoOptional.get();
                        if (serviceInfo.isAvailable()) {
                            if(viewAppsWithGui) {
                                if(serviceInfo.hasGui()) {
                                    AppObject appObject = buildAppFromServiceInfo(serviceInfo, description);
                                    accessibleApps.add(appObject);
                                }//non gui apps are not added
                            }else{//all apps are added
                                AppObject appObject = buildAppFromServiceInfo(serviceInfo, description);
                                accessibleApps.add(appObject);
                            }

                        } else {
                            if (viewUnavailableApps) {
                                if(viewAppsWithGui) {
                                    if(serviceInfo.hasGui()) {
                                        AppObject appObject = buildAppFromServiceInfo(serviceInfo, description);
                                        accessibleApps.add(appObject);
                                    }//non gui apps are not added
                                }else{//all apps are added
                                    AppObject appObject = buildAppFromServiceInfo(serviceInfo, description);
                                    accessibleApps.add(appObject);
                                }
                            }
                        }
                    }
                }
            }


        }

        return accessibleApps;
    }

    private AppObject buildAppFromServiceInfo(ServiceInfo serviceInfo,String description) {
        String urlMarkupText = String.format(urlFormat, serviceInfo.location);
        AppObject appObject = new AppObject(serviceInfo.getName(), description, serviceInfo.status, urlMarkupText);

        return appObject;
    }

    @Override
    public Optional<ServiceInfo> getServiceInfo(String appName) {
        String status = STATUS_UNAVAILABLE;

        //TODO: We can add a "unknown" location url pointing to the Portal endpoint which can show an appropriate message
        String location = LOCATION_UNKNOWN;
        List<ServiceInstance> appInstances = discoveryClient.getInstances(appName);
        boolean gui = false;
        if(appInstances != null && appInstances.size() > 0){
            //if there is more than one instance available and healthy we can assume
            //that the app is available
            status = STATUS_AVAILABLE;
            Map<String,String> metadata= appInstances.get(0).getMetadata();
            if(metadata.containsKey(APP_URI_KEY)){
                location = metadata.get(APP_URI_KEY);
                //TODO: Need to add validation that the URI is a valid URL
            }
            if(metadata.containsKey(APP_HAS_GUI_KEY)){
                gui = Boolean.parseBoolean(metadata.getOrDefault(APP_HAS_GUI_KEY,"FALSE"));
            }
        }
        return Optional.of(new ServiceInfo(appName,location,status,gui));
    }

}
