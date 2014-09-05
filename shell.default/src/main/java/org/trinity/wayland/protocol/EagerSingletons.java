package org.trinity.wayland.protocol;

import javax.inject.Inject;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
public final class EagerSingletons {

    @Inject
    static WlDataDeviceManager wlDataDeviceManager;
    @Inject
    static WlShm wlShm;
    @Inject
    static WlShell wlShell;
    @Inject
    static WlCompositor wlCompositor;
    @Inject
    static WlSubCompositor wlSubCompositor;
    @Inject
    static WlOutput wlOutput;

    @Inject
    EagerSingletons() {
    }
}
