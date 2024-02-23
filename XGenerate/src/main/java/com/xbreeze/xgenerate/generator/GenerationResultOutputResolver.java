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
package com.xbreeze.xgenerate.generator;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.lib.OutputURIResolver;

/**
 * 
 * Output resolver class is used to direct xsl:result-document output to an output stream instead of file for testing purposes
 *
 */
public class GenerationResultOutputResolver implements OutputURIResolver {
	
	private GenerationResult _generationResult;
	
	/** 
	 * @param model The Model object used for generating.
	 * @param rawTemplate The RawTemplate object used for generating.
	 * @param xsltTemplate The XsltTemplate object used for generating.
	 * @param generationResults, the collection of generation results where results are stored 
	 */
	public GenerationResultOutputResolver(GenerationResult generationResult) {
		this._generationResult = generationResult;
	}
	
	/**
	 * When close is invoked, the contents of the result object is stored in a GenerationResult object and added to GenerationResults
	 */
	@Override
	public void close(Result result) throws TransformerException {
		try {
			// Create a new GenerationResult object containing the results needed for reporting.
			GenerationOutput generationResult = new GenerationOutput();
			generationResult.setOutputFileContent(((StreamResult)result).getWriter().toString());
			generationResult.setOutputFileLocation(((StreamResult)result).getSystemId());
			_generationResult.addGenerationOutput(generationResult);
			((StreamResult)result).getWriter().close();
		}
		catch (IOException ex) {
			//Catch IOException and throw it as a TransformerException to comply with interface signature
			throw new TransformerException(ex.getMessage());
		}
	}

	/**
	 * Creates a new instance, passing the collection of GenerationResults and the model and template filename
	 */
	@Override
	public OutputURIResolver newInstance() {		
		return new GenerationResultOutputResolver(this._generationResult);
	}

	/**
	 * Create a new StreamResult and attach a writer
	 */
	@Override
	public Result resolve(String href, String base) throws TransformerException {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		result.setSystemId(href);		
		return result;
	}
}
