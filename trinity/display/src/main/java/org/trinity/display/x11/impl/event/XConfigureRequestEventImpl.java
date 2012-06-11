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
package org.trinity.display.x11.impl.event;

import org.trinity.display.x11.api.XWindow;
import org.trinity.display.x11.api.event.XConfigureRequestEvent;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class XConfigureRequestEventImpl implements XConfigureRequestEvent {

	private final int eventCode;
	private final int stackMode;
	private final int sequence;
	private final XWindow parent;
	private final XWindow window;
	private final XWindow aboveSibling;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final int borderWidth;
	private final int valueMask;

	public XConfigureRequestEventImpl(	final int eventCode,
										final int stackMode,
										final int sequence,
										final XWindow parent,
										final XWindow window,
										final XWindow aboveSibling,
										final int x,
										final int y,
										final int width,
										final int height,
										final int borderWidth,
										final int valueMask) {
		this.eventCode = eventCode;
		this.stackMode = stackMode;
		this.sequence = sequence;
		this.parent = parent;
		this.window = window;
		this.aboveSibling = aboveSibling;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.borderWidth = borderWidth;
		this.valueMask = valueMask;
	}

	/*
	 * (non-Javadoc)
	 * @see org.fusion.display.x11.api.event.XEvent#getEventCode()
	 */
	@Override
	public int getEventCode() {
		return this.eventCode;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XConfigureRequestEvent#getStackMode()
	 */
	@Override
	public int getStackMode() {
		return this.stackMode;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XConfigureRequestEvent#getSequence()
	 */
	@Override
	public int getSequence() {
		return this.sequence;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureRequestEvent#getEvent()
	 */
	@Override
	public XWindow getParent() {
		return this.parent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureRequestEvent#getWindow()
	 */
	@Override
	public XWindow getWindow() {
		return this.window;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XConfigureRequestEvent#getAboveSibling
	 * ()
	 */
	@Override
	public XWindow getSibling() {
		return this.aboveSibling;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureRequestEvent#getX()
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureRequestEvent#getY()
	 */
	@Override
	public int getY() {
		return this.y;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureRequestEvent#getWidth()
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.event.XConfigureRequestEvent#getHeight()
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XConfigureRequestEvent#getBorderWidth()
	 */
	@Override
	public int getBorderWidth() {
		return this.borderWidth;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.event.XConfigureRequestEvent#getValueMask()
	 */
	@Override
	public int getValueMask() {
		return this.valueMask;
	}
}