package org.jenkinsci.plugins.conditionchecker;
/* The MIT License
 *
 * Copyright (c) 2011 Chris Johnson
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

import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.Launcher;
import hudson.model.Hudson;
import hudson.model.BuildListener;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension point for defining a check criteria
 * Before extending this in a separate plugin
 * consider forking this plugin and adding it to the common conditions
  *
 * @author Chris Johnson
 */
public abstract class CheckCondition implements ExtensionPoint, Describable<CheckCondition> {
    /**
     * Checks if the check criteria is met.
     *
     * @param build
     * @param launcher
     * @param listener
     *
     * @return
     *      true if the condition is met,
     *      false if condition is not met
     */
    public abstract boolean isMet(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException ;

    public CheckConditionDescriptor getDescriptor() {
        return (CheckConditionDescriptor)Hudson.getInstance().getDescriptor(getClass());
    }

    /**
     * Returns all the registered {@link CheckConditionDescriptor}s.
     */
    public static DescriptorExtensionList<CheckCondition,CheckConditionDescriptor> all() {
        return Hudson.getInstance().<CheckCondition,CheckConditionDescriptor>getDescriptorList(CheckCondition.class);
    }

    /**
     * Returns a subset of {@link CheckConditionDescriptor}s that applies to the given project.
     */
    public static List<CheckConditionDescriptor> getCheckConditionDescriptors(AbstractProject<?,?> p) {
        List<CheckConditionDescriptor> r = new ArrayList<CheckConditionDescriptor>();
        for (CheckConditionDescriptor t : all()) {
            if(t.isApplicable(p))
                r.add(t);
        }
        return r;
    }
    
}
