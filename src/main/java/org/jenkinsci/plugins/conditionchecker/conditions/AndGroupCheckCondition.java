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
import hudson.Launcher;
import hudson.model.Descriptor;
import hudson.model.Describable;
import hudson.model.Hudson;
import hudson.model.BuildListener;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;

import org.jenkinsci.plugins.conditionchecker.CheckCondition;
import org.jenkinsci.plugins.conditionchecker.CheckConditionDescriptor;
/**
 * Extension point for defining a check criteria
 * Before extending this in a seperate plugin
 * consider forking this plugin and adding it to the common conditions
  *
 * @author Chris Johnson
 */
public class AndGroupCheckCondition extends CheckCondition {
    /**
     * List of conditions in this group
     */
    public final ArrayList<CheckCondition> conditions;
    /**
     * negate the result of the OR
     */
    public final boolean negate;
    /**
     * Constructor taking a list of conditions that need to be checked
     *
     * @param conditions - list of conditions that need to be checked
     * @param negate - invert the result of the OR
     */
    @DataBoundConstructor
    public AndGroupCheckCondition(ArrayList<CheckCondition> conditions, boolean negate){
        this.conditions = conditions;
        this.negate = negate;
    }

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
    public boolean isMet(AbstractBuild build, Launcher launcher,
                    BuildListener listener) throws IOException, InterruptedException {

        boolean result = true;

        Iterator<CheckCondition> iterator = conditions.iterator();
        /*  work through the conditions
         * Behave as AND implementation so return
         * so stop checking as soon as we hit a false result.
         */
        while (result && iterator.hasNext()) {
                result = iterator.next().isMet(build, launcher, listener);
        }

        return negate ? !result : result;
    }
    @Extension
    public static final class DescriptorImpl extends CheckConditionDescriptor {

            /**
             * This human readable name is used in the configuration screen.
             */
            @Override
            public String getDisplayName() {
                    return "And together contained conditions";
            }

            public boolean isApplicable(AbstractProject<?,?> item){
                return true;
            }
    }
}

