/*
 * The MIT License
 *
 * Copyright (c) 2004-2010, Sun Microsystems, Inc., Serban Iordache
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
package jenkins.plugins.displayupstreamchanges;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;
import java.io.IOException;
import java.util.logging.Logger;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * This class associates {@link DisplayUpstreamChangesAction}s to a build.
 */
@SuppressWarnings("unchecked")
public class DisplayUpstreamChangesRecorder extends Recorder {

    private static final Logger LOGGER = Logger.getLogger(DisplayUpstreamChangesRecorder.class.getName());

    @DataBoundConstructor
    public DisplayUpstreamChangesRecorder() {
        LOGGER.fine("DisplayUpstreamChangesRecorder created");
    }

    @Override
    public final Action getProjectAction(final AbstractProject<?, ?> project) {
        return null;
    }

    @Override
    public DisplayUpstreamChangesDescriptor getDescriptor() {
        return (DisplayUpstreamChangesDescriptor) super.getDescriptor();
    }

    @Override
    public final boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final BuildListener listener) throws InterruptedException, IOException {
        build.addAction(new DisplayUpstreamChangesSummaryAction(build));
        return true;
    }

    public final BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
}
