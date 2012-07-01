/*
 * This file is part of Fusion-X11. Fusion-X11 is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-X11 is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Fusion-X11. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.display.x11.impl.xcb.error;

import java.util.HashMap;
import java.util.Map;

import org.trinity.display.x11.core.api.XProtocolConstants;

/**
 * A <code>XErrorCodes</code> statically groups all possible native X errors and
 * wraps them in a <code>XErrorCode</code>. A <code>XErrorCode</code> can be
 * obtained by it's native X error code with a call to
 * {@link XErrorCodes#getByXErrorIntCode(int)}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XErrorCodes {

	private static final Map<Integer, XErrorCode> ALL_CODES = new HashMap<Integer, XErrorCode>();

	public static final XErrorCode BAD_ACCESS = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_ACCESS, "Bad access"));
	public static final XErrorCode BAD_ALLOC = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_ALLOC, "Bad alloc"));
	public static final XErrorCode BAD_ATOM = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_ATOM, "Bad atom"));
	public static final XErrorCode BAD_COLOR = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_COLOR, "Bad color"));
	public static final XErrorCode BAD_CURSOR = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_CURSOR, "Bad cursor"));
	public static final XErrorCode BAD_DRAWABLE = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_DRAWABLE, "Bad drawable"));
	public static final XErrorCode BAD_FIRST_EXTENSION_ERROR = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_FIRST_EXTENSION_ERROR,
								"Bad first extension error"));
	public static final XErrorCode BAD_FONT = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_FONT, "Bad font"));
	public static final XErrorCode BAD_GC = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_GC, "Bad gc"));
	public static final XErrorCode BAD_ID_CHOICE = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_ID_CHOICE,
								"Bad id choice"));
	public static final XErrorCode BAD_IMPLEMENTATION = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_IMPLEMENTATION,
								"Bad implementation"));
	public static final XErrorCode BAD_LAST_EXTENSION_ERROR = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_LAST_EXTENSION_ERROR,
								"Bad last extension error"));
	public static final XErrorCode BAD_LENGTH = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_LENGTH, "Bad lenght"));
	public static final XErrorCode BAD_MATCH = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_MATCH, "Bad match"));
	public static final XErrorCode BAD_NAME = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_NAME, "Bad name"));
	public static final XErrorCode BAD_PIXMAP = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_PIXMAP, "Bad pixmap"));
	public static final XErrorCode BAD_REQUEST = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_REQUEST, "Bad request"));
	public static final XErrorCode BAD_VALUE = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_VALUE, "Bad value"));
	public static final XErrorCode BAD_WINDOW = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.BAD_WINDOW, "Bad Window"));
	public static final XErrorCode SUCCESS = XErrorCodes
			.add(new XErrorCode(XProtocolConstants.SUCCESS, "Success"));

	/**
	 * An <code>XErrorCodes</code> can not be instantiated. Calling this will
	 * result in an <code>InstantiationError</code>.
	 */
	private XErrorCodes() {
		throw new InstantiationError("This class can not be instaniated.\n"
				+ "Instead, directly use the provided static methods.");
	}

	/**
	 * Add a <code>XErrorCode</code>.
	 * 
	 * @param code
	 *            An {@link XErrorCode}.
	 * @return The added <code>XErrorCode</code>.
	 */
	private static XErrorCode add(final XErrorCode code) {
		ALL_CODES.put(code.xCode, code);
		return code;
	}

	/**
	 * Query an <code>XErrorCode</code> by it's native code.
	 * 
	 * @param code
	 *            The native X error code.
	 * @return An {@link XErrorCode}.
	 */
	public static XErrorCode getByXErrorIntCode(final int code) {
		return ALL_CODES.get(code);
	}
}