/*******************************************************************************
 *   Copyright (c) 2021 CrossBreeze
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
 *      Jacob Siemaszko - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate;

public abstract class CrossGenerateException extends Exception {
	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 350438359716246610L;
	
	/**
	 * Constructor.
	 * Use when only throwing a message.
	 * @param message The message of the exception.
	 */
	public CrossGenerateException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * Used for unhandled exception.
	 * @param throwable The throwable.
	 */
	public CrossGenerateException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * Constructor.
	 * @param message The message of the exception.
	 * @param throwable The throwable.
	 */
	public CrossGenerateException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
