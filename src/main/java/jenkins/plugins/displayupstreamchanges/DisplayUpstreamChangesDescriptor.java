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

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class DisplayUpstreamChangesDescriptor extends BuildStepDescriptor<Publisher> {

	private boolean enableSecurity = false;
	
    /**
     * Constructs a {@link DisplayUpstreamChangesDescriptor}.
     */
    public DisplayUpstreamChangesDescriptor() {
        super(DisplayUpstreamChangesRecorder.class);
    }

    /**
     * Gets the descriptor display name, used in the post step checkbox description.
     * @return the descriptor display name
     */
    @Override
    public final String getDisplayName() {
        return "Display Upstream Changes";
    }

    @Override
    public String getHelpFile() {
        return "/plugin/display-upstream-changes/projectconfig.html";
    }
    
    /**
     * Checks whether this descriptor is applicable.
     * @param clazz
     *            the class
     * @return true
     */
    @SuppressWarnings("unchecked")
	@Override
    public final boolean isApplicable(final Class<? extends AbstractProject> clazz) {
        return true;
    }
    
    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        save();
        return super.configure(req,formData);
    }
    
}
