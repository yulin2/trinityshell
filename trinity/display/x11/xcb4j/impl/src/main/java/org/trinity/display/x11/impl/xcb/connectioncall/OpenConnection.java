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
package org.trinity.display.x11.impl.xcb.connectioncall;

import javax.inject.Named;

import org.trinity.display.x11.impl.xcb.AbstractXcbCall;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
@Bind
@Named("OpenConnection")
@Singleton
public class OpenConnection extends AbstractXcbCall<Long, Void, Integer> {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.trinity.display.x11.impl.displayprotocol.XNativeCall#nativeCallImpl()
	 */
	@Override
	protected boolean callImpl() {
		// TODO Auto-generated method stub
		return false;
	}

}
