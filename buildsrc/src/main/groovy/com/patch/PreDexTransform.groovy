package com.patch

import com.android.build.api.transform.Context
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project

/**
 * the Gradle plugin includes a Transform API allowing 3rd party plugins to
 * manipulate compiled class files before they are converted to dex files.
 *
 *
 * The goal of this API is to simplify injecting custom class manipulations without having to deal with tasks,
 * and to offer more flexibility on what is manipulated.
 *
 * Notes:
 * There's no way to control ordering of the transforms.
 * >>> 非常重要的Note, 解决混淆问题的前提条件
 *
 */
public class PreDexTransform extends Transform {

    Project mProject;

    PreDexTransform(Project project) {
        mProject = project
    }

    /**
     * Transfrom在Task列表中的名字
     * TransfromClassesWithPreDexForXXXX(Debug)
     */
    @Override
    String getName() {
        return "preDexFor"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        Collection<TransformInput> inputs = transformInvocation.getInputs()//输入文件集合
        transformInvocation.getOutputProvider()//outputs file path
    }
}