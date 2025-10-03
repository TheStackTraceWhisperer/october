package application;

import engine.Launcher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

  public static void main(String[] args) {
    Launcher.run(Main.class, args);
  }

}
