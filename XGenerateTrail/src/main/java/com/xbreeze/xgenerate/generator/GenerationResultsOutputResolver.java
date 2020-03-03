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
public class GenerationResultsOutputResolver implements OutputURIResolver {
	
	private GenerationResults _generationResults;
	private String _modelFileName;
	private String _templateFileName;
	
	/**
	 * 
	 * @param generationResults, the collection of generation results where results are stored 
	 * @param modelFileName filename of model
	 * @param templateFileName filename of template
	 */
	public GenerationResultsOutputResolver(GenerationResults generationResults, String modelFileName, String templateFileName) {
		_generationResults = generationResults;
		_modelFileName = modelFileName;
		_templateFileName = templateFileName;
	}
	
	/**
	 * When close is invoked, the contents of the result object is stored in a GenerationResult object and added to GenerationResults
	 */
	@Override
	public void close(Result result) throws TransformerException {
		try {
			//TODO add preprocessed output to generationResult
			GenerationResult newResult = new GenerationResult(_modelFileName, _templateFileName);
			newResult.setOutputFileContent(((StreamResult)result).getWriter().toString());
			newResult.setOutputFileLocation(((StreamResult)result).getSystemId());
			_generationResults.addGenerationResult(newResult);
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
		return new GenerationResultsOutputResolver(_generationResults, _modelFileName, _templateFileName);
	}

	/**
	 * Create a new streamresult and attach a writer
	 */
	@Override
	public Result resolve(String href, String base) throws TransformerException {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		result.setSystemId(href);		
		return result;
	}

	/**
	 * Returns the list of created results
	 * @return GenerationResults object with the generated output
	 */
	public GenerationResults getGenerationResults() {
		return _generationResults;
	}	

}
