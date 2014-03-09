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
package org.trinity.shell.api.scene;

import org.trinity.foundation.api.display.DisplaySurface;

import javax.annotation.Nonnull;

/***************************************
 * Creates shell surfaces from a display surface.
 ***************************************
 */
public interface ShellSurfaceFactory {
	/***************************************
	 * Create a new shell surface that is backed by the given display surface.
	 * The new shell surface will have the root shell surface as its parent.
	 *
	 * @param displaySurface
	 *            a {@link DisplaySurface} created by an external client
	 *            program.
	 * @return a {@link ShellSurface}.
	 ***************************************
	 */
	ShellSurface construct(@Nonnull ShellNodeParent parent,
						   @Nonnull DisplaySurface displaySurface);
}