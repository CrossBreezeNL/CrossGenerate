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
package com.xbreeze.xgenerate.observer;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class GenerationObserverSource {
	// The list of observers for the generation phases.
	private final ArrayList<GenerationObserver> _generationObservers = new ArrayList<>();
	
	/**
	 * Add a generation observer.
	 * @param generationObserver
	 */
	protected void addGenerationObserver(GenerationObserver generationObserver) {
		this._generationObservers.add(generationObserver);
	}
	
	/**
	 * Notify the generation observers the generation is starting.
	 * @param totalGenerationSteps The total number of generation steps.
	 */
	protected void notifyGenerationStarting(int totalGenerationSteps, LocalDateTime eventDateTime) {
		_generationObservers.forEach(generationObserver -> generationObserver.generationStarting(totalGenerationSteps, eventDateTime));
	}
	
	/**
	 * Notify the generation observers the generation step is starting.
	 * @param generationStepIndex The index of the generation step.
	 * @param generationStepName The name of the generation step.
	 */
	protected void notifyGenerationStepStarting(int generationStepIndex, String generationStepName, LocalDateTime eventDateTime) {
		_generationObservers.forEach(generationObserver -> generationObserver.generationStepStarting(generationStepIndex, generationStepName, eventDateTime));
	}
	
	/**
	 * Notify the generation observers the generation step is finished.
	 * @param generationStepIndex The index of the generation step.
	 * @param generationStepName The name of the generation step.
	 */
	protected void notifyGenerationStepFinished(int generationStepIndex, String generationStepName, LocalDateTime eventDateTime) {
		_generationObservers.forEach(generationObserver -> generationObserver.generationStepFinished(generationStepIndex, generationStepName, eventDateTime));
	}
	
	/**
	 * Notify the generation observers the generation step is failed.
	 * @param generationStepIndex The index of the generation step.
	 * @param generationStepName The name of the generation step.
	 * @param errorMessage The error message.
	 */
	protected void notifyGenerationStepFailed(int generationStepIndex, String generationStepName, String errorMessage, LocalDateTime eventDateTime) {
		_generationObservers.forEach(generationObserver -> generationObserver.generationStepFailed(generationStepIndex, generationStepName, errorMessage, eventDateTime));
	}
	
	/**
	 * Notify the generation observers the generation is finished.
	 */
	protected void notifyGenerationFinished(LocalDateTime eventDateTime) {
		_generationObservers.forEach(generationObserver -> generationObserver.generationFinished(eventDateTime));
	}
}
