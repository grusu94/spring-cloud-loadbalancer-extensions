# Replacement library for ribbon extensions
 - This library replaces the existing ribbon extensions project (https://github.com/enadim/spring-cloud-ribbon-extensions) which relied on the Netflix Ribbon client-side load balancer.
 - Works with newer spring boot versions (> 3.1.x).
 - **Important to keep updated this library while migrating to newer spring boot versions.**

# Key Benefits:
 - Removes dependency on the deprecated Ribbon library.
 - Leverages server-side/service registry-based load balancing. 
 - Improves maintainability and aligns with current Spring Cloud best practices.

# Migration Notes: 
 - Ribbon-specific configuration (e.g., ribbon.* properties) has been replaced with loadbalancer keyword (e.g. loadbalancer.propagation.).
 - Service discovery and load balancing are handled via Eureka and Spring Cloud LoadBalancer.

### Make sure service names and discovery configurations are consistent with the new setup.
Enhance your microservice testing, maintenance & overall development productivity.

**Spring Cloud LoadBalancer Extensions** is a set of load balancing rules that chooses which server to target.
It comes with handy features for easy integration, configuration and customization.

## Requirements 3.1.x:
* Java: **17**
* Spring Boot: **3.1.x**
* Spring Cloud: **2022.0.5**

## Compatibility:
* Spring Cloud LoadBalancer (replaces Ribbon).
* Spring Cloud Netflix Eureka.
* Spring Cloud Gateway (replaces Zuul).
* Spring Cloud Circuitbreaker Resilience4j (replaces Hystrix).
* Spring Cloud Openfeign.
* Spring and Java Executors.
* Spring Stomp.
* Jms.

## Features:

### Routing Rules:

#### Zone Affinity: @EnableLoadBalancerZoneAffinity
 - The zone affinity routes exclusively to servers that are in the same zone using a round robin load balancing.
 - Enables routing to the same zone: see the tests for concrete usage and configurations.
 - Designed mainly for microservices that should call others in the same zone.

###### Usage
```java
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientsConfig.class)
@SpringBootApplication
public class Application {
...
}

@Configuration
@EnableLoadBalancerZoneAffinity
public class LoadBalancerClientsConfig {
}
```

###### Configuration
```properties
# default configuration
loadbalancer.extensions.rule.zone-affinity.enabled=true
# customization for specific client
loadbalancer.extensions.client.my-client-name.rule.zone-affinity.enabled=true
```

#### Favorite Zone: @EnableLoadBalancerFavoriteZone
 - The favorite zone predicate is a composite predicate evaluated as follow:
   1. Matches the favorite zone key against the target server zone using the dynamic zone matcher
   2. Matches the current service zone against the target server zone using the zone affinity matcher
   3. Matches the upstream zone key against the target server zone using the dynamic zone matcher
   4. Applies the default loadbalancer zone avoidance predicate along with the availability predicate
   5. Matches any server
 - Enables routing to a favorite zone: see the tests for concrete usage and configurations.
 - **Designed mainly for testing a microservice among an existing microservice architecture without disruption. This is the feature that triggered this project.**

###### Usage
```java
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientsConfig.class)
@SpringBootApplication
public class Application {
...
}

@Configuration
@EnableLoadBalancerFavoriteZone
public class LoadBalancerClientsConfig {
}
```

###### Configuration
```properties
# default configuration
loadbalancer.extensions.rule.favorite-zone.enabled=true
loadbalancer.extensions.rule.favorite-zone.key=favorite-zone
loadbalancer.extensions.propagation.upStreamZone.key=upstream-zone
# customization for specific client
loadbalancer.extensions.client.my-client-name.rule.favorite-zone.enabled=true
loadbalancer.extensions.rule.my-client-name.rule.favorite-zone.key=my-favorite-zone
```

#### Strict Metadata Matcher: @EnableLoadBalancerStrictMetadataMatcher
 - Strict metadata matcher is designed to target servers with a specific set of metadata info. It does a round robin balancing when multiple servers have the desired set of metadata info.
 - Enables routing to servers that have a set of metadata: see the tests for concrete usage and configurations.
 - Designed to target a specific microservice that holds a point to point connection with an external system (like FIX,...)
 - Requires Eureka.

###### Usage
```java
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientsConfig.class)
@SpringBootApplication
public class Application {
...
}

@Configuration
@EnableLoadBalancerStrictMetadataMatcher
public class LoadBalancerClientsConfig {
}
```

###### Configuration
```properties
# default configuration
loadbalancer.extensions.rule.strict-metadata-matcher.enabled=true
# customization for specific client
loadbalancer.extensions.client.my-client-name.rule.strict-metadata-matcher.enabled=true
```

#### Dynamic Metadata Matcher: @EnableLoadBalancerDynamicMetadataMatcher
 - Dynamic metadata matcher is designed to target servers with a specific metadata info. It does a round robin balancing when multiple servers have the desired metadata info.
 - Enables routing against a dynamic key: see the tests for concrete usage and configurations.
 - Designed to target a specific microservices that holds multiple point to point connections with many external systems (like FIX,...)
 - Requires Eureka.

###### Usage
```java
@LoadBalancerClients(defaultConfiguration = LoadBalancerClientsConfig.class)
@SpringBootApplication
public class Application {
...
}

@Configuration
@EnableLoadBalancerDynamicMetadataMatcher
public class LoadBalancerClientsConfig {
}
```

###### Configuration
```properties
# default configuration
loadbalancer.extensions.rule.dynamic-metadata-matcher.enabled=true
loadbalancer.extensions.rule.dynamic-metadata-matcher.matchIfMissing=true
# customization for specific client
loadbalancer.extensions.client.my-client-name.rule.dynamic-metadata-matcher.enabled=true
loadbalancer.extensions.client.my-client-name.rule.dynamic-metadata-matcher.matchIfMissing=true
```

### Context Propagation: @EnableContextPropagation
 - Enables sharing the execution context through all the microservices: see the tests for concrete usage and configurations. 
 - Designed to propagate the execution context across the microservices' architecture.
 - What does it support:
   * Transport: Http, Jms, Stomp.
   * Async: Java, Spring.
   * Gateway

**_Warning_**:You should use only lower case key names (http header limitation).

###### Usage
```java
// Enable context propagation
@SpringBootApplication
@EnableContextPropagation
public class Application {
...
}

// Disable specific strategies
@SpringBootApplication
@EnableContextPropagation(
        inboundHttpRequest=false,
        feign=false,
        executor=false,
        gateway=false,
        resilience4j=false,
        jms=false,
        stomp=false
public class Application {
...
}
```

###### Configuration
```properties
# default configuration
loadbalancer.extensions.propagation.upStreamZone.enabled=true
loadbalancer.extensions.propagation.upStreamZone.key=upstream-zone
#minimal configuration
loadbalancer.extensions.propagation.keys[0]=upstream-zone
loadbalancer.extensions.propagation.keys[1]=favorite-zone
# disable all propagation strategies
loadbalancer.extensions.propagation.enabled=false
# disable specific strategies
loadbalancer.extensions.propagation.inboundHttpRequest.enabled=false
loadbalancer.extensions.propagation.feign.enabled=false
loadbalancer.extensions.propagation.executor.enabled=false
loadbalancer.extensions.propagation.zuul.enabled=false
loadbalancer.extensions.propagation.hystrix.enabled=false
loadbalancer.extensions.propagation.jms.enabled=false
loadbalancer.extensions.propagation.stomp.enabled=false
```

###### Customization
 - The propagation strategy can be configured at component level.
 - Default strategies are defined within the annotation @EnableContextPropagation itself
 - To use custom strategy simply override default ones.

```java
@SpringBootApplication
@EnableContextPropagation(
        inboundHttpRequestStrategy=YourCustomInboundHttpRequestStrategy.class,
        feignStrategy=YourCustomFeignStrategy.class,
        executorStrategy=YourCustomExecutorStrategy.class,
        gatewayStrategy=YourCustomGatewayStrategy.class,
        resilience4jStrategy=YourCustomResilience4jStrategy.class,
        jmsStrategy=YourCustomJmsStrategy.class,
        stompStrategy=YourCustomStompStrategy.class
public class Application {
...
}
```

### Combine Favorite Zone & Context Propagation
Let's enter a world of easy development and testing with microservice architecture.
* Developers are able to deploy & debug their own microservice and get back any request they have initiated disregarding the entry point and without being annoyed by the requests they have not initiated.
* Deploying in multi region-and let our clients (that have no knowledge of eureka, load balancer, gateway) choose the zone they prefer to target.
* And other things that I have not thought about...

## Setup
Maven

```xml
<dependency>
  <groupId>com.github.grusu94</groupId>
  <artifactId>spring-cloud-loadbalancer-extensions</artifactId>
  <version>3.1.x</version>
</dependency>
```
