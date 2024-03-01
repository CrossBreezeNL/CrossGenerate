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

import java.util.ArrayList;

public class GenerationResult {
	
	public enum GenerationStatus {
		OK,
		ERROR
	}
	
	/**
	 * The name of the template file.
	 */
	private String _templateFileName;
	
	/**
	 * The name of the mode file.
	 */
	private String _modelFileName;
	
	/**
	 * The generation result status.
	 */
	private GenerationStatus _status = GenerationStatus.OK;
	
	/**
	 * The exception object for when the GenerationStatus is ERROR.
	 */
	private GeneratorException _exception;
	
	/**
	 * The output of the generation cycle.
	 * This is only populated in test mode (so used for unit testing).
	 */
	private ArrayList<GenerationOutput> _generationOutputs;
	
	/**
	 * Constructor.
	 * @param modelFileName
	 * @param templateFileName
	 */
	public GenerationResult(String modelFileName, String templateFileName) {
		this._generationOutputs = new ArrayList<GenerationOutput>();
	}
	
	public void addGenerationOutput(GenerationOutput generationResult) {
		this._generationOutputs.add(generationResult);
	}

	/**
	 * @return the generationOutputs
	 */
	public ArrayList<GenerationOutput> getGenerationOutputs() {
		return _generationOutputs;
	}

	/**
	 * @return the templateFileName
	 */
	public String getTemplateFileName() {
		return _templateFileName;
	}

	/**
	 * @return the modelFileName
	 */
	public String getModelFileName() {
		return _modelFileName;
	}

	/**
	 * @return the status
	 */
	public GenerationStatus getStatus() {
		return _status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(GenerationStatus status) {
		this._status = status;
	}

	/**
	 * @return the exception
	 */
	public GeneratorException getException() {
		return _exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(GeneratorException exception) {
		this.setStatus(GenerationStatus.ERROR);
		this._exception = exception;
	}
}
