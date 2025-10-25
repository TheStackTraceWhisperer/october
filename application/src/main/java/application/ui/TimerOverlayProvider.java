package application.ui;

import io.micronaut.context.BeanProvider;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TimerOverlayProvider {

  private final BeanProvider<TimerOverlaySystem> timerOverlaySystemProvider;

  public TimerOverlaySystem mainMenu(Supplier<Float> progressSupplier) {
    TimerOverlaySystem s = timerOverlaySystemProvider.get();
    s.setAnchorCorner(TimerOverlaySystem.AnchorCorner.BOTTOM_RIGHT);
    s.setDimensions(220f, 12f);
    s.setMargin(20f);
    s.setBackgroundColor(0f, 0f, 0f, 0.35f);
    s.setBorderColor(1f, 1f, 1f, 0.6f);
    s.setFillColor(0.2f, 0.8f, 0.4f, 0.95f);
    s.setProgressSupplier(progressSupplier);
    return s;
  }

  public TimerOverlaySystem introCutscene(Supplier<Float> progressSupplier) {
    TimerOverlaySystem s = timerOverlaySystemProvider.get();
    s.setAnchorCorner(TimerOverlaySystem.AnchorCorner.TOP_RIGHT);
    s.setDimensions(240f, 12f);
    s.setMargin(20f);
    s.setBackgroundColor(0f, 0f, 0f, 0.35f);
    s.setBorderColor(1f, 1f, 1f, 0.6f);
    s.setFillColor(0.2f, 0.7f, 1f, 0.95f);
    s.setProgressSupplier(progressSupplier);
    return s;
  }
}

