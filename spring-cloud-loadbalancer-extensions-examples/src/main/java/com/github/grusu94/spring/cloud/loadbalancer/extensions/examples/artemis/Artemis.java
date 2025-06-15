package com.github.grusu94.spring.cloud.loadbalancer.extensions.examples.artemis;

import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyAcceptorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;

@SpringBootApplication
public class Artemis implements ArtemisConfigurationCustomizer {

	@Override
	public void customize(Configuration configuration) {
		configuration.addConnectorConfiguration("nettyConnector", new TransportConfiguration(NettyConnectorFactory.class.getName()));
		configuration.addAcceptorConfiguration(new TransportConfiguration(NettyAcceptorFactory.class.getName()));
	}

	public static void main(String[] args) {
		SpringApplication.run(Artemis.class, "--spring.config.name=artemis");
	}
}
