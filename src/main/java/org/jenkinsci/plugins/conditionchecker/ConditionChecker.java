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

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.Environment;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.DescriptorExtensionList;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.tasks.BuildStep;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import hudson.model.Hudson;
import hudson.tasks.Publisher;
import hudson.tasks.Notifier;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Class to allow any build step to be performed before the SCM checkout occurs.
 *
 * @author Chris Johnson
 *
 */
public class ConditionChecker extends Notifier{
    /**
     * Stored conditions to check
     */
    public final ArrayList<CheckCondition> conditions;
    public final boolean negate;

    /**
     * Constructor taking a list of conditions that need to be checked
     *
     * @param conditions - list of conditions that need to be checked
     */
    @DataBoundConstructor
    public ConditionChecker(ArrayList<CheckCondition> conditions, boolean negate) {
            this.conditions = conditions;
            this.negate = negate;
    }

    /**
     * Main public methods that checks all of the containing conditions
     * Default behaviour is to AND all of the different checkers together,
     * see the other condition for OR and NOT behaviour.
     *
     * @param build
     * @param launcher
     * @param listener
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
        if(negate){
            result = !result;
        }
        return result;
    }
    public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}
    @Override
	public boolean perform(AbstractBuild build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {


		listener.getLogger().println("ConditionChecker...");
        boolean result = isMet(build, launcher,listener);

        listener.getLogger().println("Result: " + result);


     	return true;

    }
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
                return "configuration tests";
        }

        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
                    return true;
            }
    /**
     * Returns a subset of {@link CheckConditionDescriptor}s that applies to the given project.
     */
    public static List<CheckConditionDescriptor> getCheckConditionDescriptors(AbstractProject<?,?> p) {
        List<CheckConditionDescriptor> r = new ArrayList<CheckConditionDescriptor>();
        for (CheckConditionDescriptor t : Hudson.getInstance().<CheckCondition,CheckConditionDescriptor>getDescriptorList(CheckCondition.class)) {
            if(t.isApplicable(p))
                r.add(t);
        }
        return r;
    }
    }

}
