package io.github.patternatlas.api.security;

import io.github.patternatlas.api.repositories.RoleRepository;
import io.github.patternatlas.api.service.RoleService;
import io.github.patternatlas.api.service.UserAuthService;
import io.github.patternatlas.api.service.UserService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

import java.util.LinkedList;

public class ResourceMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private ApplicationContext applicationContext;
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    /** Overrides filter to support filtering CollectionModel as it is used in API */
    @Override
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {
        if(filterTarget instanceof CollectionModel) {
            // The collection has to be extracted so filter can work appropriately
            /* From the Spring-Documentation
                "If a Collection or Map is used, the original instance will be modified
                to contain the elements for which the permission expression evaluates to true."
                => super.filter should return a collection again, which can be added to
                CollectionModel
             */
            CollectionModel<?> filterCollectionModel = (CollectionModel<?>)filterTarget;
            // Since the Collection obtained from getContent() is immutable,
            // a new List has to be created to be filtered
            Object filterResult = super.filter(new LinkedList<>(filterCollectionModel.getContent()), filterExpression, ctx);
            return CollectionModel.of(filterResult);
        }
        // Default case - proceed as usual
        return super.filter(filterTarget, filterExpression, ctx);
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        ResourceSecurityExpressionRoot root = new ResourceSecurityExpressionRoot(
                authentication,
                this.applicationContext.getBean(UserService.class),
                this.applicationContext.getBean(RoleService.class),
                this.applicationContext.getBean(RoleRepository.class),
                this.applicationContext.getBean(UserAuthService.class));

        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }
}
