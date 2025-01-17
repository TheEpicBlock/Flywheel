package com.jozufozu.flywheel.backend.pipeline;

import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.backend.source.ShaderLoadingException;
import com.jozufozu.flywheel.backend.source.SourceFile;
import com.jozufozu.flywheel.backend.source.error.ErrorReporter;
import com.jozufozu.flywheel.backend.source.parse.ShaderFunction;
import com.jozufozu.flywheel.backend.source.parse.ShaderStruct;
import com.jozufozu.flywheel.backend.source.parse.Variable;
import com.jozufozu.flywheel.backend.source.span.Span;

public class InstancingProgramMetaData {

	public final SourceFile file;
	public final ShaderFunction vertexMain;
	public final ShaderFunction fragmentMain;
	public final Span interpolantName;
	public final Span vertexName;
	public final Span instanceName;
	public final ShaderStruct interpolant;
	public final ShaderStruct vertex;
	public final ShaderStruct instance;

	public InstancingProgramMetaData(SourceFile file) {
		this.file = file;

		Optional<ShaderFunction> vertexFunc = file.findFunction("vertex");
		Optional<ShaderFunction> fragmentFunc = file.findFunction("fragment");

		if (fragmentFunc.isEmpty()) {
			ErrorReporter.generateMissingFunction(file, "fragment", "\"fragment\" function not defined");
		}
		if (vertexFunc.isEmpty()) {
			ErrorReporter.generateFileError(file, "could not find \"vertex\" function");
		}

		if (fragmentFunc.isEmpty() || vertexFunc.isEmpty()) {
			throw new ShaderLoadingException();
		}

		fragmentMain = fragmentFunc.get();
		vertexMain = vertexFunc.get();
		ImmutableList<Variable> parameters = fragmentMain.getParameters();
		ImmutableList<Variable> vertexParams = vertexMain.getParameters();

		if (parameters.size() != 1) {
			ErrorReporter.generateSpanError(fragmentMain.getArgs(), "instancing requires fragment function to have 1 argument");
		}

		if (vertexParams.size() != 2) {
			ErrorReporter.generateSpanError(vertexMain.getArgs(), "instancing requires vertex function to have 2 arguments");
			throw new ShaderLoadingException();
		}

		interpolantName = vertexMain.getType();
		vertexName = vertexParams.get(0)
				.typeName();
		instanceName = vertexParams.get(1)
				.typeName();

		Optional<ShaderStruct> maybeInterpolant = file.findStruct(interpolantName);
		Optional<ShaderStruct> maybeVertex = file.findStruct(vertexName);
		Optional<ShaderStruct> maybeInstance = file.findStruct(instanceName);

		if (maybeVertex.isEmpty()) {
			ErrorReporter.generateMissingStruct(file, vertexName, "struct not defined");
		}

		if (maybeInterpolant.isEmpty()) {
			ErrorReporter.generateMissingStruct(file, interpolantName, "struct not defined");
		}

		if (maybeInstance.isEmpty()) {
			ErrorReporter.generateMissingStruct(file, instanceName, "struct not defined");
		}

		if (maybeVertex.isEmpty() || maybeInterpolant.isEmpty() || maybeInstance.isEmpty()) {
			throw new ShaderLoadingException();
		}

		interpolant = maybeInterpolant.get();
		vertex = maybeVertex.get();
		instance = maybeInstance.get();
	}
}
