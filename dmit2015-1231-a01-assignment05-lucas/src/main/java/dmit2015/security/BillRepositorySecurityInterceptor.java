package dmit2015.security;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;

/** Sales role are permitted to call methods that begin with add, update, and delete.
* IT role are permitted to call that begins with delete. */
public class BillRepositorySecurityInterceptor {
@Inject
private SecurityContext securityContext;
    @AroundInvoke
    public Object verifyAccess(InvocationContext ic) throws Exception {
        String methodName = ic.getMethod().getName();
        // Permit only Finance role to call methods that begin with add and update.
        if (methodName.startsWith("create") || methodName.startsWith("update")) {
            boolean isInRole = securityContext.isCallerInRole("Finance");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }
        // Permit only Accounting role to call delete and remove methods.
        if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            boolean isInRole = securityContext.isCallerInRole("Accounting");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }
        // Permit only Accounting role to call delete and remove methods.
        if (methodName.startsWith("findOne")) {
            boolean isInRole = securityContext.isCallerInRole("Accounting") || securityContext.isCallerInRole("Finance") || securityContext.isCallerInRole("Executive");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }
        return ic.proceed();
    }
}