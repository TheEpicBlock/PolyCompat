package nl.theepicblock.polycompat;

import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.register.PolyRegistry;
import io.github.theepicblock.polymc.resource.ResourcePackMaker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import nl.theepicblock.polycompat.module.forceheroes.Init;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PolyCompat implements ModInitializer, PolyMcEntrypoint {
	@Override
	public void onInitialize() {
		for (ModContainer c : FabricLoader.getInstance().getAllMods()) {
			String modid = c.getMetadata().getId();
			if (doesModuleExist(modid)) {
				try {
					Class<?> myClass = Class.forName(getClassPathForModule(modid) + ".Init");
					Method method = myClass.getDeclaredMethod("onInitialize");
					method.invoke(null);
				} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
					printReflectionError(modid,"onInitialize", e);
				} catch (NoSuchMethodException ignored) {
				}
			}
		}
	}

	@Override
	public void registerPolys(PolyRegistry polyRegistry) {
		for (ModContainer c : FabricLoader.getInstance().getAllMods()) {
			String modid = c.getMetadata().getId();
			if (doesModuleExist(modid)) {
				try {
					Class<?> myClass = Class.forName(getClassPathForModule(modid) + ".Init");
					Method method = myClass.getDeclaredMethod("registerPolys", PolyRegistry.class);
					method.invoke(null, polyRegistry);
				} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
					printReflectionError(modid,"registerPolys", e);
				} catch (NoSuchMethodException ignored) {
				}
			}
		}
	}

	@Override
	public void registerModSpecificResources(ResourcePackMaker pack) {
		for (ModContainer c : FabricLoader.getInstance().getAllMods()) {
			String modid = c.getMetadata().getId();
			if (doesModuleExist(modid)) {
				try {
					Class<?> myClass = Class.forName(getClassPathForModule(modid) + ".Init");
					Method method = myClass.getDeclaredMethod("registerPolys", ResourcePackMaker.class);
					method.invoke(null, pack);
				} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
					printReflectionError(modid,"registerPolys", e);
				} catch (NoSuchMethodException ignored) {
				}
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

	private boolean doesModuleExist(String modid) {
		Package[] packages = Package.getPackages();
		String nameToCheckFor = getClassPathForModule(modid);
		for (Package p : packages) {
			if (p.getName().startsWith(nameToCheckFor)) {
				return true;
			}
		}
		return false;
	}
}
