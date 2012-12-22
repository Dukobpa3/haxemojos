/**
 * Copyright (C) 2012 https://github.com/yelbota/haxe-maven-plugin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yelbota.plugins.haxe;

import com.google.common.base.Joiner;
import com.yelbota.plugins.haxe.utils.HaxeFileExtensions;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoFailureException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCompileHaxeMojo extends CommandHaxeMojo {

    /**
     * @parameter property="main"
     * @required
     */
    public String main;

    /**
     * @parameter property="main" default-value="false"
     * @required
     */
    public boolean debug;

    @Override
    protected void prepareArguments() throws MojoFailureException
    {
        arguments = Joiner.on(" ").join(prepareArgumentsList());
    }

    protected List<String> prepareArgumentsList()
    {
        List<String> argumentsList = new ArrayList<String>();

        // Add project compile source roots (such as src/main/haxe or target/generated-sources/*)
        for (String path : project.getCompileSourceRoots())
            addSourcePath(argumentsList, path);

        addDependenciesSourcePaths(argumentsList);
        addHaxelib(argumentsList);
        addMain(argumentsList);
        addDebug(argumentsList);

        return argumentsList;
    }

    private void addHaxelib(List<String> argumentsList)
    {
        for (Artifact artifact : project.getDependencyArtifacts())
        {
            if (artifact.getType().equals(HaxeFileExtensions.HAXELIB))
            {
                String haxelibId = artifact.getArtifactId() + ":" + artifact.getVersion();
                argumentsList.add("-lib");
                argumentsList.add(haxelibId);
            }
        }
    }

    private void addDebug(List<String> argumentsList)
    {
        if (debug)
            argumentsList.add("-debug");
    }

    private void addDependenciesSourcePaths(List<String> argumentsList)
    {
        // TODO unpack sources of haxe projects which attached as dependencies.
    }

    protected void addMain(List<String> argumentsList)
    {
        argumentsList.add("-main");
        argumentsList.add(main);
    }

    protected void addSourcePath(List<String> argumentsList, String sourcePath)
    {
        argumentsList.add("-cp");
        argumentsList.add(sourcePath);
    }
}
