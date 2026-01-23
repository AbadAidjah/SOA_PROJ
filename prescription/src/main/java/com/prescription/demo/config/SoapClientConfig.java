package com.prescription.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Configuration des clients SOAP et REST
 */
@Configuration
public class SoapClientConfig {

    /**
     * Configuration du marshaller JAXB pour SOAP
     */
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.pharmacie.demo");
        return marshaller;
    }

    /**
     * Configuration du template SOAP pour communiquer avec le service pharmacie
     */
    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setDefaultUri("http://pharmacie-service:8080/ws");
        return template;
    }

    /**
     * Configuration du RestTemplate pour les appels REST
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        ((SimpleClientHttpRequestFactory) factory).setConnectTimeout(5000);
        ((SimpleClientHttpRequestFactory) factory).setReadTimeout(10000);
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}