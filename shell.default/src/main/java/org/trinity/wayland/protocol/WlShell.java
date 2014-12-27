package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.*;
import org.trinity.wayland.protocol.events.ResourceDestroyed;

import javax.annotation.Nonnull;
import java.util.Set;

@AutoFactory(className = "WlShellFactory")
public class WlShell extends Global<WlShellResource> implements WlShellRequests, ProtocolObject<WlShellResource> {

    private final Set<WlShellResource> resources = Sets.newHashSet();
    private final EventBus             eventBus  = new EventBus();

    private final WlShellSurfaceFactory wlShellSurfaceFactory;

    WlShell(@Provided final Display display,
            @Provided final WlShellSurfaceFactory wlShellSurfaceFactory) {
        super(display,
              WlShellResource.class,
              VERSION);
        this.wlShellSurfaceFactory = wlShellSurfaceFactory;
    }

    @Override
    public void getShellSurface(final WlShellResource requester,
                                final int id,
                                @Nonnull final WlSurfaceResource surface) {
        final WlSurface wlSurface = (WlSurface) surface.getImplementation();
        final WlShellSurface wlShellSurface = this.wlShellSurfaceFactory.create(wlSurface);
        final WlShellSurfaceResource shellSurfaceResource = wlShellSurface.add(requester.getClient(),
                                                                               requester.getVersion(),
                                                                               id);
        wlSurface.register(new Object() {
            @Subscribe
            public void handle(final ResourceDestroyed event) {
                wlSurface.unregister(this);
                wlShellSurface.destroy(shellSurfaceResource);
            }
        });
    }

    @Override
    public WlShellResource onBindClient(final Client client,
                                        final int version,
                                        final int id) {
        //FIXME check if we support requested version.
        return add(client,
                   version,
                   id);
    }

    @Override
    public Set<WlShellResource> getResources() {
        return this.resources;
    }

    @Override
    public WlShellResource create(final Client client,
                                  final int version,
                                  final int id) {
        return new WlShellResource(client,
                                   version,
                                   id,
                                   this);
    }

    @Override
    public void register(@Nonnull final Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void unregister(@Nonnull final Object listener) {
        this.eventBus.unregister(listener);
    }

    @Override
    public void post(@Nonnull final Object event) {
        this.eventBus.post(event);
    }
}
