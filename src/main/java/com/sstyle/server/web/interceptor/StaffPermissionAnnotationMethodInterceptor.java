package com.sstyle.server.web.interceptor;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;

/**
 * Created by ss on 2018/3/27.
 */
public class StaffPermissionAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    /**
     * Default no-argument constructor that ensures this interceptor looks for
     * {@link org.apache.shiro.authz.annotation.RequiresPermissions RequiresPermissions} annotations in a method declaration.
     */
    public StaffPermissionAnnotationMethodInterceptor() {
        super(new StaffPermissionAnnotationHandler());
    }

    /**
     * @param resolver
     * @since 1.1
     */
    public StaffPermissionAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new StaffPermissionAnnotationHandler(), resolver);
    }

    @Override
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        try {
            AuthorizingAnnotationHandler
                    handler = (AuthorizingAnnotationHandler) getHandler();
            if (handler instanceof StaffPermissionAnnotationHandler) {
                ((StaffPermissionAnnotationHandler) handler).assertAuthorized(mi);
            } else {
                handler.assertAuthorized(getAnnotation(mi));
            }
        } catch (AuthorizationException ae) {
            // Annotation handler doesn't know why it was called, so add the information here if possible.
            // Don't wrap the exception here since we don't want to mask the specific exception, such as
            // UnauthenticatedException etc.
            if (ae.getCause() == null)
                ae.initCause(new AuthorizationException("Not authorized to invoke method: " + mi.getMethod()));
            throw ae;
        }
    }
}
