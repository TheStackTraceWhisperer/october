//package plugin;
//
//import api.events.Ping;
//import api.plugin.IPlugin;
//import io.micronaut.runtime.event.annotation.EventListener;
//import jakarta.inject.Singleton;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Singleton
//public class ExamplePlugin implements IPlugin {
//
//  @Override
//  public String getName() {
//    return "ExamplePlugin";
//  }
//
//  @Override
//  public void start() {
//    log.info("Starting plugin...");
//  }
//
//  @Override
//  public void stop() {
//    log.info("Stopping plugin...");
//  }
//
//  @EventListener
//  public void onPing(Ping ping) {
//    log.info("RECEIVED PING!: {}", ping.message());
//  }
//
//}
