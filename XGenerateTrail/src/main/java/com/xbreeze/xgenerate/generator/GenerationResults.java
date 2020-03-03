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
