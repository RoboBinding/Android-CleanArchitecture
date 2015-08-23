package com.fernandocejas.android10.sample.data;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.util.Logger;
import org.robolectric.util.ReflectionHelpers;

public class MyRunner extends RobolectricTestRunner {

    private static final String BUILD_OUTPUT = "build/intermediates";

    public MyRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        if (config.constants() == Void.class) {
            Logger.error("Field 'constants' not specified in @Config annotation");
            Logger.error("This is required when using RobolectricGradleTestRunner!");
            throw new RuntimeException("No 'constants' field in @Config annotation!");
        }

        final String type = getType(config);
        final String flavor = getFlavor(config);
        final String packageName = getPackageName(config);

        final FileFsFile res;
        final FileFsFile assets;
        final FileFsFile manifest;

        if (FileFsFile.from(BUILD_OUTPUT, "res").exists()) {
            res = FileFsFile.from(BUILD_OUTPUT, "res", flavor, type);
        } else {
            res = FileFsFile.from(BUILD_OUTPUT, "bundles", flavor, type, "res");
        }

        if (FileFsFile.from(BUILD_OUTPUT, "assets").exists()) {
            assets = FileFsFile.from(BUILD_OUTPUT, "assets", flavor, type);
        } else {
            assets = FileFsFile.from(BUILD_OUTPUT, "bundles", flavor, type, "assets");
        }

        if (FileFsFile.from(BUILD_OUTPUT, "manifests").exists()) {
            manifest = FileFsFile.from(BUILD_OUTPUT, "manifests", "full", flavor, type, "AndroidManifest.xml");
        } else {
            manifest = FileFsFile.from(BUILD_OUTPUT, "bundles", flavor, type, "AndroidManifest.xml");
        }

        Logger.debug("Robolectric assets directory: " + assets.getPath());
        Logger.debug("   Robolectric res directory: " + res.getPath());
        Logger.info("   Robolectric manifest path: " + manifest.getPath());
        Logger.debug("    Robolectric package name: " + packageName);
        return new AndroidManifest(manifest, res, assets, packageName);
    }

    private String getType(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "BUILD_TYPE");
        } catch (Throwable e) {
            return null;
        }
    }

    private String getFlavor(Config config) {
        try {
            return ReflectionHelpers.getStaticField(config.constants(), "FLAVOR");
        } catch (Throwable e) {
            return null;
        }
    }

    private String getPackageName(Config config) {
        try {
            final String packageName = config.packageName();
            if (packageName != null && !packageName.isEmpty()) {
                return packageName;
            } else {
                return ReflectionHelpers.getStaticField(config.constants(), "APPLICATION_ID");
            }
        } catch (Throwable e) {
            return null;
        }
    }
}