package grails.plugins.auditloginlogout

class SecurityAuditLog {

    String username
    String fromUser
    String ipAddress
    String eventType
    String exceptionType
    String eventDescription
    String sessionId

    Date date

    static constraints = {
        eventDescription nullable: true
        exceptionType nullable: true
        fromUser nullable: true
        sessionId nullable: true
    }
}
