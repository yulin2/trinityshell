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
package org.trinity.foundation.api.display;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Describes a visible part of the screen. It is the base interface of all
 * objects wishing to describe an on-screen surface.
 * <p>
 * Implementation advice:
 * <p>
 * <code>DisplayArea</code> implementations should have a corresponding
 * {@link DisplayAreaManipulator} to provide interaction.
 *
 *
 */
@Deprecated
@ExecutionContext(DisplayExecutor.class)
@ThreadSafe
public interface DisplayArea {

}
