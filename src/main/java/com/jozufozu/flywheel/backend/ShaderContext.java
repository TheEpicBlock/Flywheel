package com.jozufozu.flywheel.backend;

import java.util.function.Supplier;

import com.jozufozu.flywheel.backend.gl.shader.GlProgram;

import net.minecraft.resources.ResourceLocation;

public interface ShaderContext<P extends GlProgram> {

	default P getProgram(ResourceLocation loc) {
		return this.getProgramSupplier(loc)
				.get();
	}

	Supplier<P> getProgramSupplier(ResourceLocation loc);

	/**
	 * Load all programs associated with this context. This might be just one, if the context is very specialized.
	 */
	void load();

	void delete();
}
