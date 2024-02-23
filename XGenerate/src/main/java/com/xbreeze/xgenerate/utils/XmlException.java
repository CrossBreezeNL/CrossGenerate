/*******************************************************************************
 *   Copyright (c) 2024 CrossBreeze
 *
 *   This file is part of CrossGenerate.
 *
 *      CrossGenerate is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      CrossGenerate is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with CrossGenerate.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *      Willem Otten - CrossBreeze
 *      Harmen Wessels - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.utils;

import com.xbreeze.xgenerate.CrossGenerateException;

public class XmlException extends CrossGenerateException {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -2222917882180986246L;

	/**
	 * Constructor.
	 * @param message The message of the exception.
	 */
	public XmlException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * @param throwable Throwable of the exception.
	 */
	public XmlException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * Constructor.
	 * @param message The message of the exception.
	 * @param throwable The throwable.
	 */
	public XmlException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
