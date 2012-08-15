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
package org.trinity.foundation.display.api;

/*****************************************
 * @author Erik De Rijcke
 * 
 ****************************************/
public class DisplayProtocol {

	/**
	 * query response:
	 * <p>
	 * <ul>
	 * <li>"name":String</li>
	 * <li>"friendlyName":String</li>
	 * <li>"programName":String</li>
	 * </ul>
	 */
	public static final DisplayProtocol NAMES = new DisplayProtocol();

	/**
	 * query response:
	 * <p>
	 * none
	 */
	public static final DisplayProtocol CLOSE_REQUEST = new DisplayProtocol();

}
