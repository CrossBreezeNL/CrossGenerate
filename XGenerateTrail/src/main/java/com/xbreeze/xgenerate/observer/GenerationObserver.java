package com.xbreeze.xgenerate.observer;

import java.time.LocalDateTime;

public interface GenerationObserver {
	
	public void generationStarting(int totalGenerationSteps, LocalDateTime eventDateTime);
	
	public void generationStepStarting(int generationStepIndex, String generationStepName, LocalDateTime eventDateTime);
	
	public void generationStepFinished(int generationStepIndex, String generationStepName, LocalDateTime eventDateTime);
	
	public void generationStepFailed(int generationStepIndex, String generationStepName, String errorMessage, LocalDateTime eventDateTime);
	
	public void generationFinished(LocalDateTime eventDateTime);
}
