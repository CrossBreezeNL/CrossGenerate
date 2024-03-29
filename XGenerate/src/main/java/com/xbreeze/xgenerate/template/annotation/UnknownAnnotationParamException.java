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
package com.xbreeze.xgenerate.template.annotation;

public class UnknownAnnotationParamException extends AnnotationException {
	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -4850371099181650105L;

	/**
	 * Constructor.
	 * @param annotationName The annotation name.
	 * @param annotationParamName The annotation param name.
	 */
	public UnknownAnnotationParamException(String annotationName, String annotationParamName) {
		super(String.format("Unknown annotation param used (%s -> %s)", annotationName, annotationParamName));
	}
}
