/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.ShellWidgetView;
import org.trinity.shellplugin.widget.api.BaseShellWidgetStyled;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(value = @Named("ShellWidgetContainer"))
public class ShellWidgetContainer extends BaseShellWidgetStyled {

	@Inject
	ShellWidgetContainer(	final EventBus eventBus,
							final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
							final PainterFactory painterFactory,
							@Named("ShellWidgetContainerView") final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				view);
	}
}