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

import hudson.model.Hudson;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link Descriptor} for {@link CheckCondition}.
 *
 * @author Chris Johnson
 *
 * @see CheckCondition#all()
 * @see CheckCondition#getApplicableTriggers(AbstractProject)
 */
public abstract class CheckConditionDescriptor extends Descriptor<CheckCondition> {

    protected CheckConditionDescriptor(Class<? extends CheckCondition> clazz) {
        super(clazz);
    }

    protected CheckConditionDescriptor() {
        super();
    }

    /**
     * Returns true if this condition is applicable to the given project.
     *
     * @return
     *      true to allow user to configure this promotion condition for the given project.
     */
    public boolean isApplicable(AbstractProject<?,?> item){
        return true;
    }
    /**
     * Returns a subset of {@link ConditionCheckerDescriptor}s that applies to the given project.
     */
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
