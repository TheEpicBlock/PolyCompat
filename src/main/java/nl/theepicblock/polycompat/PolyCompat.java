package nl.theepicblock.polycompat;

import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.resource.ResourcePackMaker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PolyCompat implements ModInitializer, PolyMcEntrypoint {
	@Override
	public void onInitialize() {
		for (ModContainer c : FabricLoader.getInstance().getAllMods()) {
			String modId = c.getMetadata().getId();
			try {
				Class<?> myClass = Class.forName(getClassPathForModule(modId) + ".Init");
				Method method = myClass.getDeclaredMethod("onInitialize");
				method.invoke(null);
			} catch (InvocationTargetException | IllegalAccessException e) {
				printReflectionError(modId,"onInitialize", e);
			} catch (ClassNotFoundException | NoSuchMethodException ignored) {
			}
		}
	}

	@Override
	public void registerPolys(PolyRegistry registry) {
		for (ModContainer c : FabricLoader.getInstance().getAllMods()) {
			String modId = c.getMetadata().getId();
			try {
				Class<?> myClass = Class.forName(getClassPathForModule(modId) + ".Init");
				Method method = myClass.getDeclaredMethod("registerPolys", PolyRegistry.class);
				method.invoke(null, registry);
			} catch (InvocationTargetException | IllegalAccessException e) {
				printReflectionError(modId,"registerPolys", e);
			} catch (ClassNotFoundException | NoSuchMethodException ignored) {
			}
		}
	}

	@Override
	public void registerModSpecificResources(ResourcePackMaker pack) {
		for (ModContainer c : FabricLoader.getInstance().getAllMods()) {
			String modId = c.getMetadata().getId();
			try {
				Class<?> myClass = Class.forName(getClassPathForModule(modId) + ".Init");
				Method method = myClass.getDeclaredMethod("registerModSpecificResources", ResourcePackMaker.class);
				method.invoke(null, pack);
			} catch (InvocationTargetException | IllegalAccessException e) {
				printReflectionError(modId,"registerModSpecificResources", e);
			} catch (ClassNotFoundException | NoSuchMethodException ignored) {
			}
		}
	}

	private void printReflectionError(String modid, String origin, Exception e) {
		String helpfulDevMessage = null;
		if (e instanceof ClassNotFoundException) {
			helpfulDevMessage = "Make sure there is a class called Init in your module's package";
		} else if (e instanceof IllegalAccessException) {
			helpfulDevMessage = "Make sure all methods in Init are public";
		}

		boolean isDev = FabricLoader.getInstance().isDevelopmentEnvironment();
		System.out.printf("Poly Compat reflection error in %s! Whilst looking for module for mod: %s%n", origin, modid);
		if (isDev) {
			if (helpfulDevMessage != null) System.out.println(helpfulDevMessage);
		} else {
			System.out.println("Unless you're a Poly Compat dev, there's nothing you can do about this. Please post a github issue");
		}
		e.printStackTrace();
		System.out.println("");
		System.out.printf("Poly Compat reflection error in %s!%n", origin);
		if (isDev) {
			if (helpfulDevMessage != null) System.out.println(helpfulDevMessage);
		} else {
			System.out.println("Unless you're a Poly Compat dev, there's nothing you can do about this. Please post a github issue");
		}
	}

	private String getClassPathForModule(String modid) {
		return "nl.theepicblock.polycompat.module."+modid;
	}
}
