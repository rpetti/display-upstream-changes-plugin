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

import hudson.model.*;
import hudson.scm.ChangeLogSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//@ExportedBean(defaultVisibility=2)
public class DisplayUpstreamChangesSummaryAction implements Action {
    private AbstractBuild build;
    
    public DisplayUpstreamChangesSummaryAction(AbstractBuild build) {
        this.build = build;
    }
    
    /* Action methods */
    public String getUrlName() { return ""; }
    public String getDisplayName() { return ""; }
    public String getIconFileName() { return null; }
    
    public static class UpstreamChangeLog {
        private ChangeLogSet changeLogSet;
        private AbstractBuild build;

        public UpstreamChangeLog(ChangeLogSet changeLogSet, AbstractBuild build) {
            this.changeLogSet = changeLogSet;
            this.build = build;
        }

        public AbstractBuild getBuild() {
            return build;
        }

        public void setBuild(AbstractBuild build) {
            this.build = build;
        }

        public ChangeLogSet getChangeLogSet() {
            return changeLogSet;
        }

        public void setChangeLogSet(ChangeLogSet changeLogSet) {
            this.changeLogSet = changeLogSet;
        }
        
        public String getDisplayName() {
            return build.getProject().getDisplayName() + " " + build.getDisplayName();
        }
        
        public String getAbsoluteBuildUrl() {
            return Hudson.getInstance().getRootUrl() + "/" + build.getUrl();
        }
        
        public String getSCMDisplayName() {
            return build.getProject().getScm().getDescriptor().getDisplayName();
        }
        
    }
    
    public List<UpstreamChangeLog> getUpstreamChangeLogs(){
        List<UpstreamChangeLog> upstreamChangeLogs = new ArrayList<UpstreamChangeLog>();
        List<ChangeLogSet> changeLogSets = new ArrayList<ChangeLogSet>();
        Map<AbstractProject<?,?>,Integer> transitiveUpstreamBuilds = build.getTransitiveUpstreamBuilds();
        for(Entry<AbstractProject<?,?>,Integer> e : transitiveUpstreamBuilds.entrySet()){
            AbstractBuild<?,?> run = e.getKey().getBuildByNumber(e.getValue());
            if (run.hasChangeSetComputed()) {
                ChangeLogSet<?> cls = run.getChangeSet();
                if (cls != null) {
                    changeLogSets.add(cls);
                    upstreamChangeLogs.add(new UpstreamChangeLog(cls, run));
                }
            }
        }
        //Upstream builds via cause
        List<AbstractBuild> upstreamBuilds = new ArrayList<AbstractBuild>();
        getAllUpstreamByCause(this.build, upstreamBuilds);
        for(AbstractBuild build : upstreamBuilds) {
            if(build.hasChangeSetComputed()){
                ChangeLogSet cls = build.getChangeSet();
                if(!changeLogSets.contains(cls)){
                    changeLogSets.add(cls);
                    upstreamChangeLogs.add(new UpstreamChangeLog(cls, build));
                }
            }
        }
        
        return upstreamChangeLogs;
    }

    private static void getAllUpstreamByCause(AbstractBuild build, List<AbstractBuild> list) {
        for(AbstractBuild upstreamBuild : getUpstreamByCause(build)) {
            //Duplication and cycle protection
            if(list.contains(upstreamBuild)){
                continue;
            } else {
                list.add(upstreamBuild);
                getAllUpstreamByCause(upstreamBuild, list);
            }
        }
        return;
    }

    private static List<AbstractBuild> getUpstreamByCause(AbstractBuild build) {
        List<AbstractBuild> upstreamBuilds = new ArrayList<AbstractBuild>();
        for(Cause cause: (List<Cause>) build.getCauses()){
            if(cause instanceof Cause.UpstreamCause) {
                TopLevelItem upstreamProject = Hudson.getInstance().getItemByFullName(((Cause.UpstreamCause)cause).getUpstreamProject(), TopLevelItem.class);
                if(upstreamProject instanceof AbstractProject){
                    int buildId = ((Cause.UpstreamCause)cause).getUpstreamBuild();
                    Run run = ((AbstractProject) upstreamProject).getBuildByNumber(buildId);
                    if(run instanceof AbstractBuild){
                        upstreamBuilds.add((AbstractBuild) run);
                    }
                }
            }
        }
        return upstreamBuilds;
    }
    
}
