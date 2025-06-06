package com.example.spring.cloud.loadbalancer.extensions.support;

import com.example.spring.cloud.loadbalancer.extensions.support.strategy.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExecutionContextPropagationImportTest {
    private final ExecutionContextPropagationImport imports = new ExecutionContextPropagationImport();
    private final AnnotationMetadata metadata = mock(AnnotationMetadata.class);

    @Test
    public void selectImports() throws Exception {
        assertThat(imports.selectImports(metadata).length, is(0));

        when(metadata.getAnnotationAttributes(EnableContextPropagation.class.getName(), true)).thenReturn(null);
        assertThat(imports.selectImports(metadata).length, is(0));

        assertThat(imports.selectImports(metadata).length, is(0));

        List<String> actual = Arrays.asList(imports.selectImports(
                new SimpleMetadataReaderFactory().getMetadataReader(Annotated.class.getName()).getAnnotationMetadata()));
        assertThat(actual, Matchers.containsInAnyOrder(
                PreservesHeadersInboundHttpRequestStrategy.class.getName(),
                PreservesHttpHeadersFeignStrategy.class.getName(),
                PreservesExecutionContextExecutorStrategy.class.getName(),
                PreservesHttpHeadersGatewayStrategy.class.getName(),
                PreservesExecutionContextResilience4jStrategy.class.getName(),
                PreservesJmsMessagePropertiesStrategy.class.getName(),
                PreservesStompHeadersStrategy.class.getName()
        ));
    }

    @EnableContextPropagation
    static class Annotated {
    }

}