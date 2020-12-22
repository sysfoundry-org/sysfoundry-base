package org.sysfoundry.security.keycloak.webmodule;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.servlet.KeycloakOIDCFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class KeycloakOIDCFilterWrapper extends KeycloakOIDCFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.info("IN DO_FILTER OF OIDC FILTER");
        HttpServletRequest httpServletRequest = (HttpServletRequest)req;
        String requestURL = httpServletRequest.getRequestURL().toString();
        log.info("Request URL : "+requestURL);
        try {
            super.doFilter(req, res, chain);
            //handle backchannel logout ourselves
            if(isBackchannelLogoutRequest(requestURL)){

            }
        }catch(Exception e){
            log.info("Exception Class "+e.getClass()+" Cause "+e.getCause());
            throw new ServletException(e);
        }
    }

    private boolean isBackchannelLogoutRequest(String requestURL) {
        return requestURL.endsWith("k_logout");
    }
}
