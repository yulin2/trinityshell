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
package org.trinity.foundation.api.render.binding.view;

import javax.annotation.Nullable;

/***************************************
 * Transforms a model property value to one or more other values so it can be
 * used to invoke a view method. To transform to multiple different types of
 * objects, simply return an array of objects containing the values in same
 * order as the view method arguments.
 *
 * @see PropertySlot
 ***************************************
 */
public interface PropertyAdapter<T> {

	/***************************************
	 * Transforms a model property value to one or more other values.
	 *
	 * @param property
	 *            A model property
	 * @return the transformatin.
	 ***************************************
	 */
	Object adapt(@Nullable T property);
}
