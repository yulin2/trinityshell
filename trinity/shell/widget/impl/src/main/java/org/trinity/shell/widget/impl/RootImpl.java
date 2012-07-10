/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.widget.impl;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.widget.api.Root;
import org.trinity.shell.widget.api.Widget;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
// TODO shutdown logic
// TODO implement resizing+moving? (with XRANDR extension in x11)
/**
 * A <code>RealRoot</code> represents a <code>Widget</code> that is backed by
 * the native root window. It is the base of the
 * <code>GeoTransformableRectangle</code> tree hierarchy for a
 * <code>ManagedDisplay</code>. Multiple <code>RealRoot</code> widgets can be
 * constructed from the same <code>ManagedDisplay</code> but will represent the
 * same on-screen drawable, it is thus recommended to use the
 * {@link ManagedDisplay#getRealRootRenderArea()} method to reference the real
 * root.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
@javax.inject.Named("Root")
@Singleton
public final class RootImpl extends WidgetImpl implements Root {

	@Inject
	protected RootImpl(	final EventBus eventBus,
						final GeoEventFactory geoEventFactory,
						final ManagedDisplay managedDisplay,
						final PainterFactory painterFactory,
						@Named("Widget") final GeoExecutor geoExecutor,
						final Root.View view) {
		super(	eventBus,
				geoEventFactory,
				managedDisplay,
				painterFactory,
				geoExecutor,
				view);
		init(getParent());
		requestShow();
		syncGeoToPlatformRenderAreaGeo();
	}

	@Override
	protected void init(final Widget paintableParent) {
		super.init(paintableParent);
		syncGeoToPlatformRenderAreaGeo();
	}

	@Override
	public int getAbsoluteX() {
		return getX();
	}

	@Override
	public int getAbsoluteY() {
		return getY();
	}

	@Override
	public RootImpl getParent() {
		return this;
	}
}
