package com.jozufozu.flywheel.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import net.minecraft.client.Minecraft;

public class OptifineHandler {
	public static final String OPTIFINE_ROOT_PACKAGE = "net.optifine";
	public static final String SHADER_PACKAGE = "net.optifine.shaders";

	private static Package optifine;
	private static OptifineHandler handler;

	public final boolean usingShaders;

	public OptifineHandler(boolean usingShaders) {
		this.usingShaders = usingShaders;
	}

	/**
	 * Get information about the current Optifine configuration.
	 *
	 * @return {@link Optional#empty()} if Optifine is not installed.
	 */
	public static Optional<OptifineHandler> get() {
		return Optional.ofNullable(handler);
	}

	public static boolean optifineInstalled() {
		return optifine != null;
	}

	public static boolean usingShaders() {
		return OptifineHandler.get()
				.map(OptifineHandler::isUsingShaders)
				.orElse(false);
	}

	public static void init() {
		optifine = Package.getPackage(OPTIFINE_ROOT_PACKAGE);

		if (optifine == null) {
			Backend.LOGGER.info("Optifine not detected.");
		} else {
			Backend.LOGGER.info("Optifine detected.");

			refresh();
		}
	}

	public static void refresh() {
		if (optifine == null) return;

		boolean shadersOff = areShadersDisabledInOptifineConfigFile();

		handler = new OptifineHandler(!shadersOff);
	}

	private static boolean areShadersDisabledInOptifineConfigFile() {
		File dir = Minecraft.getInstance().gameDirectory;

		File shaderOptions = new File(dir, "optionsshaders.txt");

		boolean shadersOff = true;
		try (BufferedReader reader = new BufferedReader(new FileReader(shaderOptions))) {

			shadersOff = reader.lines()
					.anyMatch(it -> {
						String line = it.replaceAll("\\s", "");
						if (line.startsWith("shaderPack=")) {
							String setting = line.substring("shaderPack=".length());

							return setting.equals("OFF") || setting.equals("(internal)");
						}
						return false;
					});
		} catch (IOException e) {
			Backend.LOGGER.info("No shader config found.");
		}
		return shadersOff;
	}

	public boolean isUsingShaders() {
		return usingShaders;
	}
}
