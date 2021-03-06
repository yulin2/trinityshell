/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.api.display.event;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Notifies that the geometry (size, place) of a {@link DisplaySurface} has
 * changed.
 *
 */
@Immutable
@ExecutionContext(DisplayExecutor.class)
public class GeometryNotify extends DisplayEvent {

	private final Rectangle geometry;

	/***************************************
	 * Create a new <code>GeometryNotify</code> that targets a
	 * {@link DisplaySurface}. The new geometry is specified by the
	 * {@link Rectangle} argument.
	 *
	 * @param geometry
	 *            The new geometry as a {@link Rectangle}.
	 ***************************************
	 */
	public GeometryNotify(@Nonnull final Rectangle geometry) {
		this.geometry = geometry;
	}

	/***************************************
	 * The new geometry of the {@link DisplaySurface}.
	 *
	 * @return The new geometry as a {@link Rectangle}.
	 ***************************************
	 */
	public Rectangle getGeometry() {
		return this.geometry;
	}
}
