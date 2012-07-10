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
import org.trinity.shell.widget.api.Button;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class ButtonImpl extends WidgetImpl implements Button {

	/*****************************************
	 * @param painterFactory
	 * @param view
	 ****************************************/
	@Inject
	protected ButtonImpl(	final EventBus eventBus,
							final GeoEventFactory geoEventFactory,
							final ManagedDisplay managedDisplay,
							final PainterFactory painterFactory,
							@Named("Widget") final GeoExecutor geoExecutor,
							final Button.View view) {
		super(	eventBus,
				geoEventFactory,
				managedDisplay,
				painterFactory,
				geoExecutor,
				view);
	}

	// pressed?
	// released?
}
