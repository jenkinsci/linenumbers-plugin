/*
 * The MIT License
 * 
 * Copyright (c) 2014, Vincent Latombe
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.linenumbers;

import hudson.Extension;
import hudson.model.BuildListener;
import hudson.model.BuildStepListener;
import hudson.model.AbstractBuild;
import hudson.tasks.BuildStep;

import java.io.IOException;

import org.jenkinsci.plugins.linenumbers.StepConsoleNote.Kind;

@Extension
public class MarkerBuildStepListener extends BuildStepListener {

  @Override
  public void finished(AbstractBuild build, BuildStep bs, BuildListener listener, boolean canContinue) {
    try {
      listener.annotate(new StepConsoleNote<Object>(Kind.BUILDSTEP_END, getCounter(build)));
    } catch (IOException e) {
      listener.getLogger().println("Unable to insert StepConsoleNote");
    }
  }

  @Override
  public void started(final AbstractBuild build, final BuildStep bs, final BuildListener listener) {
    try {
      listener.annotate(new StepConsoleNote<Object>(Kind.BUILDSTEP_START, incrementCounter(build)));
    } catch (IOException e) {
      listener.getLogger().println("Unable to insert StepConsoleNote");
    }
  }

  private BuildStepAction getAction(AbstractBuild build) {
    BuildStepAction action = build.getAction(BuildStepAction.class);
    if (action == null) {
      action = new BuildStepAction();
      build.addAction(action);
    }
    return action;
  }

  private int getCounter(AbstractBuild build) {
    return getAction(build).getValue();
  }

  private int incrementCounter(AbstractBuild build) {
    return getAction(build).increment();
  }

}
