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
 *      Jacob Siemaszko - Crossbreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.generator;

import java.util.ArrayList;

public class GenerationResults {
	
	private ArrayList<GenerationResult> _generationResults;
	
	public GenerationResults() {
		this._generationResults = new ArrayList<GenerationResult>();
	}
	
	public void addGenerationResult(GenerationResult generationResult) {
		this._generationResults.add(generationResult);
	}

	/**
	 * @return the generationResults
	 */
	public ArrayList<GenerationResult> getGenerationResults() {
		return _generationResults;
	}
}
