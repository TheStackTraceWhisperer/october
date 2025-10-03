package application;

import engine.ApplicationLoopPolicy;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

@Factory
public class Conf {
  @Bean
  ApplicationLoopPolicy loopPolicy() {
    return ApplicationLoopPolicy.standard();
  }
}
