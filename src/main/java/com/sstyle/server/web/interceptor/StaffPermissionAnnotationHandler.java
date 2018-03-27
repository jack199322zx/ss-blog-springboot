package com.sstyle.server.web.interceptor;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * Created by ss on 2018/3/27.
 */
public class StaffPermissionAnnotationHandler extends AuthorizingAnnotationHandler {

    /**
     * Default no-argument constructor that ensures this handler looks for
     * {@link RequiresPermissions RequiresPermissions} annotations.
     */
    public StaffPermissionAnnotationHandler() {
        super(RequiresPermissions.class);
    }

    /**
     * Returns the annotation {@link RequiresPermissions#value value}, from which the Permission will be constructed.
     *
     * @param a the RequiresPermissions annotation being inspected.
     * @return the annotation's <code>value</code>, from which the Permission will be constructed.
     */
    protected String[] getAnnotationValue(Annotation a) {
        RequiresPermissions rpAnnotation = (RequiresPermissions) a;
        return rpAnnotation.value();
    }

    /**
     * 自定义 PermissionAnnotationHandler 增加了对类上加此注解的实现
     */
    public void assertAuthorized(MethodInvocation mi) throws AuthorizationException {
        Subject subject = getSubject();
        String[] perms = {};
        Logical logical = Logical.AND;

        RequiresPermissions classAnnotation = mi.getThis().getClass().getAnnotation(RequiresPermissions.class);
        if (null != classAnnotation) {
            perms = classAnnotation.value();
            logical = classAnnotation.logical();
        }
        RequiresPermissions methodAnnotation = mi.getMethod().getAnnotation(RequiresPermissions.class);
        if (null != methodAnnotation) {
            String[] methodPerms = methodAnnotation.value();
            perms = ArrayUtils.addAll(perms, methodPerms);
            logical = methodAnnotation.logical();
        }

        if (Logical.AND.equals(logical)) {
            subject.checkPermissions(perms);
            return;
        }

        if (Logical.OR.equals(logical)) {
            for (String permission : perms) {
                if (subject.isPermitted(permission)) {
                    // hasAtLeastOnePermission
                    return;
                }
            }
            // Cause the exception if none of the role match, note that the exception message will be a bit misleading
            subject.checkPermission(perms[0]);

        }

    }

    public void assertAuthorized(Annotation a) throws AuthorizationException {

    }

}