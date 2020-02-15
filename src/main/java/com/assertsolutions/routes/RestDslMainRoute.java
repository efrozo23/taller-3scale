package com.assertsolutions.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.assertsolutions.beans.ResponseHandler;
import com.assertsolutions.dto.Request;
import com.assertsolutions.dto.Response;

import io.swagger.annotations.Api;

/**
 * 
 * @author Assert Solutions S.A.S
 *
 */
@Component
@Api(value = "Initial Proyect Camel-REST", description = "Estrucutura Basica Proyecto Rest Y Camel")
public class RestDslMainRoute extends RouteBuilder {

    @Value("${camel.component.servlet.mapping.context-path}")
    private String contextPath;

    @Autowired
    private Environment env;
    private Logger log = LoggerFactory.getLogger(RestDslMainRoute.class);

    @Override
    public void configure() throws Exception {
    // @formatter:off
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .enableCORS(true)
            .port(env.getProperty("server.port", "8080"))
            .contextPath(contextPath.substring(0, contextPath.length() - 2))
            // turn on swagger api-doc
            .apiContextPath("/api-doc")
            .apiProperty("api.title",  env.getProperty("api.title"))
            .apiProperty("api.version", env.getProperty("api.version"));
        
        rest().description(env.getProperty("api.description"))
            .consumes(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
        
        .get(env.getProperty("endpoint.healthcheck")).description(env.getProperty("endpoint.healthcheck.description")).outType(String.class)
            .responseMessage().code(200).message("All users successfully returned").endResponseMessage()
            .route().setBody(constant("OK")).endRest()
        .get(env.getProperty("endpoint.get_text")).description(env.getProperty("endpoint.get.description"))
            .responseMessage().code(200).message("All users successfully returned").endResponseMessage()
            .route().removeHeader("*").setHeader("Content-Type", simple(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .bean(ResponseHandler.class).removeHeader("*")
            .setHeader("Accept", simple(MediaType.APPLICATION_JSON_UTF8_VALUE)).setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200)).endRest()
         .post(env.getProperty("endpoint.save")).description(env.getProperty("endpoint.save.description")).type(Request.class).description(
                 env.getProperty("endpoint.save.description")).outType(Response.class) 
             .responseMessage().code(200).message("All users successfully created").endResponseMessage()
             .to("direct:update-user");
       
        from("direct:update-user")
        	.log(LoggingLevel.INFO,log,"Body: ${body}")
            .bean(ResponseHandler.class)
            .setHeader("Content-Type", simple(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .setHeader("Accept", simple(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
            .log(LoggingLevel.INFO,log,"Response Body: ${body}");
        
        
       
        // @formatter:on
    }

}
