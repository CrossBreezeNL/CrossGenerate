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
package com.xbreeze.xgenerate.config;

import java.util.logging.Logger;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;

public class UnmarshallValidationEventHandler implements ValidationEventHandler {
	// The logger for the XGenConfig class.
	private static final Logger logger = Logger.getLogger(UnmarshallValidationEventHandler.class.getName());
	
	@Override
	public boolean handleEvent(ValidationEvent event) {
		if (event.getLocator() != null)
			logger.warning(String.format("Error in config file on line %d, column %d, node '%s':\n%s", event.getLocator().getLineNumber(), event.getLocator().getOffset(), event.getLocator().getNode(), event.getMessage()));
		else
			logger.warning(String.format("Error in config file:\n%s", event.getMessage()));
		return false;
	}
}
