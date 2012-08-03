/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.input.api.event;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.input.api.PointerInput;

// TODO documentation
/**
 * A <code>ButtonNotifyEvent</code> notifies that a mouse button's state has
 * changed.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class ButtonNotifyEvent extends InputNotifyEvent<PointerInput> {
	/*****************************************
 * 
 ****************************************/
	public ButtonNotifyEvent(	final DisplayEventSource displayEventSource,
								final PointerInput pointerInput) {
		super(displayEventSource, pointerInput);
	}
}
