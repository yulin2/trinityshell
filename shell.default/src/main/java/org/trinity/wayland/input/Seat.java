package org.trinity.wayland.input;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.WlPointerResource;
import org.freedesktop.wayland.server.WlSurfaceResource;
import org.freedesktop.wayland.shared.WlPointerButtonState;
import org.freedesktop.wayland.util.Fixed;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.output.Compositor;
import org.trinity.wayland.protocol.WlPointer;
import org.trinity.wayland.protocol.WlSeat;
import org.trinity.wayland.protocol.WlSurface;

import javax.media.nativewindow.util.PointImmutable;
import java.util.Optional;

@AutoFactory(className = "SeatFactory")
public class Seat {

    private final Display    display;
    private final WlSeat     wlSeat;
    private final Compositor compositor;

    private Optional<WlSurface> currentFocus = Optional.empty();

    Seat(@Provided final Display display,
         final WlSeat wlSeat,
         final Compositor compositor) {
        this.display = display;
        this.wlSeat = wlSeat;
        this.compositor = compositor;
    }

    public void handlePointerMove(final int time,
                                  final int absX,
                                  final int absY) {
        final Optional<ShellSurface> newFocusShellSurface = this.compositor.getScene()
                                                                           .findSurfaceAtCoordinate(absX,
                                                                                                    absY);
        final Optional<WlSurface> newFocus;
        if (newFocusShellSurface.isPresent()) {
            newFocus = Optional.of(WlSurface.SHELL_SURFACE_WL_SURFACE_MAP.get(newFocusShellSurface.get()));
        }
        else {
            newFocus = Optional.empty();
        }

        if (!this.currentFocus.equals(newFocus)) {
            shiftFocus(newFocus,
                       absX,
                       absY);
        }

        if (this.currentFocus.isPresent()) {
            reportMotion(time,
                         absX,
                         absY);
        }
    }

    private void reportMotion(final int time,
                              final int absX,
                              final int absY) {
        final WlSurfaceResource wlSurfaceResource = this.currentFocus.get()
                                                                     .getResource()
                                                                     .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
        if (pointerResource.isPresent()) {
            final PointImmutable relativePoint = this.compositor.getScene()
                                                                .relativeCoordinate(this.currentFocus.get()
                                                                                                     .getShellSurface(),
                                                                                    absX,
                                                                                    absY);
            pointerResource.get()
                           .motion(time,
                                   Fixed.create(relativePoint.getX()),
                                   Fixed.create(relativePoint.getY()));
        }
    }

    private void shiftFocus(Optional<WlSurface> newFocus,
                            final int absX,
                            final int absY) {

        if (this.currentFocus.isPresent()) {
            leaveFocus();
        }

        if (newFocus.isPresent()) {
            gainFocus(newFocus.get(),
                      absX,
                      absY);
        }

        this.currentFocus = newFocus;
    }

    private void gainFocus(final WlSurface newFocus,
                           final int absX,
                           final int absY) {
        //a new surface has the focus, it now gains focus
        final WlSurfaceResource wlSurfaceResource = newFocus.getResource()
                                                            .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
        if (pointerResource.isPresent()) {
            final PointImmutable relativePoint = this.compositor.getScene()
                                                                .relativeCoordinate(newFocus.getShellSurface(),
                                                                                    absX,
                                                                                    absY);

            pointerResource.get()
                           .enter(this.display.nextSerial(),
                                  wlSurfaceResource,
                                  Fixed.create(relativePoint.getX()),
                                  Fixed.create(relativePoint.getY()));
        }
    }

    private void leaveFocus() {
        final WlSurfaceResource wlSurfaceResource = this.currentFocus.get()
                                                                     .getResource()
                                                                     .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
        if (pointerResource.isPresent()) {
            pointerResource.get()
                           .leave(this.display.nextSerial(),
                                  wlSurfaceResource);
        }
    }

    private Optional<WlPointerResource> findPointerResource(WlSurfaceResource wlSurfaceResource) {
        final WlPointer wlPointer = this.wlSeat.getOptionalWlPointer()
                                               .get();
        for (WlPointerResource wlPointerResource : wlPointer.getResources()) {
            if (wlSurfaceResource.getClient()
                                 .equals(wlPointerResource.getClient())) {
                return Optional.of(wlPointerResource);
            }
        }
        return Optional.empty();
    }

    public void handleButton(final int time,
                             final int button,
                             final WlPointerButtonState buttonState) {
        if (this.currentFocus.isPresent()) {
            final Optional<WlSurfaceResource> wlSurfaceResource = this.currentFocus.get()
                                                                                   .getResource();
            if (wlSurfaceResource.isPresent()) {
                final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource.get());
                if (pointerResource.isPresent()) {
                    pointerResource.get()
                                   .button(display.nextSerial(),
                                           time,
                                           button,
                                           buttonState.getValue());
                }
            }
        }
    }
}
