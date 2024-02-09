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
package com.xbreeze.xgenerate.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

public class FileUtils {

	public static String getFileContent(URI fileLocation) throws IOException {
		// Create a input stream from the template file.
		FileInputStream fis = new FileInputStream(new File(fileLocation));
		// Wrap the input stream in a BOMInputStream so it is invariant for the BOM.
		BOMInputStream bomInputStream = BOMInputStream.builder().setInputStream(fis).get();
		// Create a String using the BOMInputStream and the charset.
		// The charset can be null, this gives no errors.
		return IOUtils.toString(bomInputStream, bomInputStream.getBOMCharsetName());
	}
}
