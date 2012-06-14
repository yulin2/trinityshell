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
package org.trinity.display.x11.impl.xcb.eventconverters;

import org.trinity.display.x11.api.core.XAtomFactory;
import org.trinity.display.x11.api.core.XDisplayResourceFactory;
import org.trinity.display.x11.api.core.XDisplayServer;
import org.trinity.display.x11.api.core.XEventConverter;
import org.trinity.display.x11.api.core.XProtocolConstants;
import org.trinity.display.x11.api.core.XResourceHandleFactory;
import org.trinity.display.x11.api.core.event.XEvent;
import org.trinity.display.x11.api.core.event.XEventFactory;
import org.trinity.display.x11.impl.xcb.jni.NativeBufferHelper;
import org.trinity.foundation.display.api.event.DisplayEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@Singleton
public class XSelectionClearEventConverter implements
		XEventConverter<NativeBufferHelper> {

	private final Integer eventCode = Integer
			.valueOf(XProtocolConstants.SELECTION_CLEAR);

	private final XDisplayResourceFactory xDisplayResourceFactory;
	private final XResourceHandleFactory xResourceHandleFactory;
	private final XDisplayServer xDisplayServer;
	private final XAtomFactory xAtomFactory;
	private final XEventFactory xEventFactory;

	@Inject
	public XSelectionClearEventConverter(	final XEventFactory xEventFactory,
											final XDisplayServer xDisplayServer,
											final XDisplayResourceFactory xDisplayResourceFactory,
											final XResourceHandleFactory xResourceHandleFactory,
											final XAtomFactory xAtomFactory) {
		this.xEventFactory = xEventFactory;
		this.xDisplayServer = xDisplayServer;
		this.xDisplayResourceFactory = xDisplayResourceFactory;
		this.xResourceHandleFactory = xResourceHandleFactory;
		this.xAtomFactory = xAtomFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#constructEvent(java.lang.
	 * Object)
	 */
	@Override
	public XEvent constructEvent(final NativeBufferHelper rawEvent) {
		// contents of native buffer:
		// uint8_t pad0; /**< */
		// uint16_t sequence; /**< */
		// xcb_timestamp_t time; /**< */
		// xcb_window_t owner; /**< */
		// xcb_atom_t selection; /**< */

		rawEvent.readUnsignedByte();
		final int sequence = rawEvent.readUnsignedShort();
		final int time = (int) rawEvent.readUnsignedInt();
		this.xDisplayServer.setLastServerTime(time);
		final int ownerWindowId = (int) rawEvent.readUnsignedInt();
		final int selectionAtomId = (int) rawEvent.readUnsignedInt();
		rawEvent.doneReading();

		return this.xEventFactory
				.createXSelectionClearEvent(this.eventCode.intValue(),
											sequence,
											time,
											this.xDisplayResourceFactory
													.createPlatformRenderArea(this.xResourceHandleFactory
															.createResourceHandle(Integer
																	.valueOf(ownerWindowId))),
											this.xAtomFactory
													.createAtom(selectionAtomId));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.api.XEventConverter#convertEvent(org.trinity.
	 * display.x11.api.event.XEvent)
	 */
	@Override
	public DisplayEvent convertEvent(final XEvent xEvent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.display.x11.api.XEventConverter#getXEventCode()
	 */
	@Override
	public Integer getXEventCode() {
		return this.eventCode;
	}

}