package org.sysfoundry.svc.portal.api;

import org.sysfoundry.svc.portal.impl.PortalServiceImpl;
import org.sysfoundry.svc.portal.vmod.AppObject;

import java.util.List;
import java.util.Optional;

public interface PortalService {

    public static final String APP_URI_KEY = "public-uri";
    public static final String APP_HAS_GUI_KEY = "has-gui";
    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_UNAVAILABLE = "Unavailable";
    public static final String LOCATION_UNKNOWN = "Unknown";

    List<AppObject> listApps(boolean viewUnavailableApps,boolean viewGuiApps,String... exceptions);

    Optional<PortalServiceImpl.ServiceInfo> getServiceInfo(String serviceName);
}
