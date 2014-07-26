package grails.plugins.auditloginlogout

import org.apache.log4j.Logger
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationEvent
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent
import org.springframework.security.web.authentication.switchuser.AuthenticationSwitchUserEvent

class SpringSecurityEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    static final Logger log = Logger.getLogger(SpringSecurityEventListener)

    @Override
    void onApplicationEvent(AbstractAuthenticationEvent e) {
        SecurityAuditLog securityAuditLog = new SecurityAuditLog()
        securityAuditLog.date = new Date(e?.timestamp)
        securityAuditLog.eventType = e?.class?.simpleName
        def request  = WebUtils.retrieveGrailsWebRequest()?.request
        String ipAddress = HttpUtils.getClientIpAddr(request)
        securityAuditLog.sessionId = request.requestedSessionId
        if (e instanceof AbstractAuthenticationFailureEvent) {
            securityAuditLog.username = e.source?.principal
            securityAuditLog.ipAddress = ipAddress
            securityAuditLog.eventDescription = e.exception?.message
            securityAuditLog.exceptionType = e.exception?.class?.simpleName
        } else if (e instanceof AuthenticationSuccessEvent) {
            securityAuditLog.eventDescription = "Logged in"
            securityAuditLog.username = e.source?.principal?.username
            securityAuditLog.ipAddress = ipAddress
        } else if (e instanceof AuthenticationSwitchUserEvent) {
            securityAuditLog.username = e.targetUser.username
            securityAuditLog.fromUser = e.source?.principal?.username
            securityAuditLog.ipAddress = ipAddress
            securityAuditLog.eventDescription = SpringSecurityUtils.switchedUserOriginalUsername == securityAuditLog.username? "Switched Back" :'Switched User'
        }else if (e instanceof InteractiveAuthenticationSuccessEvent){
            securityAuditLog.eventDescription = "Log In"
            securityAuditLog.username = e.source?.principal?.username
            securityAuditLog.ipAddress = ipAddress
        }
        else {
            println "Unknown kind of event. ${e.class}"
            securityAuditLog.eventDescription = "Unknown event"
        }

        if (!securityAuditLog.save(flush: true)) {
            println("Failed to save the loginAction ${securityAuditLog?.errors}")
        } else {
            println("Login action registered successfully..${securityAuditLog?.eventType}")
        }

    }
}