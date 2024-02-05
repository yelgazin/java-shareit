package ru.practicum.shareit.gateway.common.client;

import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.gateway.helper.ObjectGenerator;

import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public abstract class AbstractClientTest {

    @Mock
    protected RestTemplate restTemplate;

    @Mock
    protected RestTemplateBuilder restTemplateBuilder;

    protected final ObjectGenerator objectGenerator = new ObjectGenerator();

    protected void init() {
        when(restTemplateBuilder.uriTemplateHandler(any()))
                .thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.requestFactory(ArgumentMatchers.<Supplier<ClientHttpRequestFactory>>any()))
                .thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build())
                .thenReturn(restTemplate);
    }
}
