package com.patch

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class Register implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension)
        project.logger.warn "================Plugin success=========="
        android.registerTransform(new PreDexTransform(project))
    }
}