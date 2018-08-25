package quoters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;

public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            final String beanClassName = beanDefinition.getBeanClassName();
            try {
                final Class<?> beanClass = Class.forName(beanClassName);
                final Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    if(method.isAnnotationPresent(PostProxy.class)) {
                        final Object bean = context.getBean(beanDefinitionName);
                        final Method currentMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                        currentMethod.invoke(bean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
