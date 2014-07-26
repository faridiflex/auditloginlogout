package grails.plugins.auditloginlogout

class SecurityAuditLog {

    String username
    String fromUser
    String ipAddress
    String eventType
    String exceptionType
    String sessionId

    Date date

    static constraints = {
        exceptionType nullable: true
        fromUser nullable: true
        sessionId nullable: true
    }
}
