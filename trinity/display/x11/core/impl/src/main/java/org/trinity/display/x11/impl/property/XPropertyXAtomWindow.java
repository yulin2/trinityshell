/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.property;

import org.fusion.x11.core.FlexDataContainer;
import org.fusion.x11.core.IntDataContainer;
import org.trinity.display.x11.impl.XServerImpl;
import org.trinity.display.x11.impl.XIDImpl;
import org.trinity.display.x11.impl.XResourceHandleImpl;
import org.trinity.display.x11.impl.XWindowImpl;
import org.trinity.foundation.display.api.PlatformRenderArea;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XPropertyXAtomWindow extends
		AbstractXProperty<XPropertyInstanceXWindow> {

	/**
	 * 
	 * @param display
	 * @param atomName
	 * 
	 */
	public XPropertyXAtomWindow(final XServerImpl display, final String atomName) {
		super(display, atomName);
	}

	/**
	 * 
	 * @param display
	 * @param atomName
	 * @param atomId
	 * 
	 */
	public XPropertyXAtomWindow(final XServerImpl display, final String atomName,
			final Long atomId) {
		super(display, atomName, atomId);
	}

	@Override
	public void setPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceXWindow propertyInstance) {
		final IntDataContainer rawDataContainer = new IntDataContainer(1);
		final Integer windowId = Integer.valueOf(propertyInstance
				.getPlatformRenderArea().getDisplayResourceHandle()
				.getResourceHandle().getNativeHandle().intValue());
		rawDataContainer.writeDataBlock(windowId);
		setRawPropertyValue(platformRenderArea, propertyInstance.getType(),
				rawDataContainer);
	}

	@Override
	public XPropertyInstanceXWindow getPropertyInstance(
			final PlatformRenderArea platformRenderArea,
			final XPropertyInstanceInfo propertyInstanceInfo,
			final FlexByteContainer propertyDataContainer) {
		final Long windowId = Long
				.valueOf(propertyInstanceInfo.getLength() == 0 ? 0
						: propertyDataContainer.readUnsignedInt());
		final XIDImpl xid = new XIDImpl(getDisplay(), XResourceHandleImpl.valueOf(windowId));
		final XWindowImpl window = getDisplay().getDisplayPlatform()
				.getResourcesRegistry().getClientXWindow(xid);
		final XPropertyInstanceXWindow reply = new XPropertyInstanceXWindow(
				getDisplay(), window);
		return reply;
	}
}