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
package org.trinity.foundation.display.api.event;

import org.trinity.foundation.display.api.DisplaySurface;

/**
 * A <code>DisplayEvent</code> is a piece of information coming from a
 * <code>Display</code>. A <code>DisplayEvent</code> is usually send on the
 * behalf of another resource living on the <code>Display</code> that sends the
 * event.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class DisplayEvent {

	private final DisplayEventSource displayEventSource;

	/*****************************************
	 * 
	 ****************************************/
	public DisplayEvent(final DisplayEventSource displayEventSource) {
		this.displayEventSource = displayEventSource;
	}

	/**
	 * The original display resource where this <code>DisplayEven</code>
	 * originates from.
	 * 
	 * @return A {@link DisplaySurface}.
	 */
	public DisplayEventSource getEventSource() {
		return this.displayEventSource;
	}
}
