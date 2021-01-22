package org.sysfoundry.svc.portal;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sysfoundry.portal")
@Data
public class Config {

    /**
     * Lets the user view unavailable apps in the portal.
     * Can be used for debugging purposes.
     * If true it lets the user view all unavailable apps in the portal
     * default is false
     */
    private boolean viewUnavailableApps = false;

    /**
     * The Account app name used by the portal to show the account details of the user
     */
    private String accountAppName = "account";

    /**
     * List apps with GUI only
     */
    private boolean viewAppsWithGui = true;


    /**
     * Relative url path for the Solution's help content
     */
    private String helpPath = "docs";

    /**
     * Help not found page relative path
     */
    private String helpNotFoundErrorPagePath = "help_not_found.html";

}
