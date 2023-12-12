package dmit2015.security;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

import jakarta.security.enterprise.SecurityContext;

public class BillPaymentRepositorySecurityInterceptor {
    @Inject
    private SecurityContext securityContext;
    @AroundInvoke
    public Object verifyAccess(InvocationContext ic) throws Exception {
        String methodName = ic.getMethod().getName();
        // Permit only Accounting role to call methods that begin with create and update.
        if (methodName.startsWith("create") || methodName.startsWith("update")) {
            boolean isInRole = securityContext.isCallerInRole("Accounting");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }
        // Permit only Accounting role to call methods that being with delete.
        if (methodName.startsWith("remove") || methodName.startsWith("delete")) {
            boolean isInRole = securityContext.isCallerInRole("Accounting");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }

        // Do not permit Finance, Accounting or Executive role to call findOneById method.
        if (methodName.startsWith("findOneById")) {
            boolean isInRole = securityContext.isCallerInRole("Finance")
                    || securityContext.isCallerInRole("Accounting")
                    || securityContext.isCallerInRole("Executive");
            if (!isInRole) {
                throw new RuntimeException("Access denied! You do not have permission to execute this method");
            }
        }

        return ic.proceed();
    }
}