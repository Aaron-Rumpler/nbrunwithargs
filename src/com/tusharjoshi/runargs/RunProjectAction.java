/*
 The MIT License (MIT)

 Copyright (c) 2014 Tushar Joshi
 Copyright (c) 2020 DAGOPT Optimization Technologies GmbH

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.tusharjoshi.runargs;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 *
 * @author Tushar Joshi
 */
@ActionID(
        category = "Build",
        id = "com.tusharjoshi.runargs.RunProjectAction"
)
@ActionRegistration(displayName = "#CTL_RunProjectAction", lazy = false)
@ActionReferences({
    @ActionReference(path = "Menu/BuildProject", position = 11),
    @ActionReference(path = "Toolbars/Build", position = 301),
    // TODO: Use context and match naming scheme
    @ActionReference(path = "Projects/org-netbeans-modules-java-j2seproject/Actions", position = 801),
    @ActionReference(path = "Projects/org-netbeans-modules-maven/Actions", position = 901),
    @ActionReference(path = "Projects/org-netbeans-modules-gradle/Actions", position = 901),
    @ActionReference(path = "ProjectsTabActions", position = 1101),
    @ActionReference(path = "Shortcuts", name = "D-A-F6")
})
@NbBundle.Messages({
    "CTL_RunProjectAction=Run Project with Arguments..."})
public class RunProjectAction extends ProjectAction 
implements ContextAwareAction {   
    public static final Icon smallIcon;
    public static final Icon largeIcon;
    
    static {
        ImageIcon badgeSmall = ImageUtilities.loadImageIcon("org/netbeans/modules/profiler/impl/icons/edit.svg", false);
        
        BufferedImage badgeScaled = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = badgeScaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(badgeSmall.getImage(), 0, 0, badgeScaled.getWidth(), badgeScaled.getHeight(), null);
        g.dispose();
        
        ImageIcon badgeLarge = new ImageIcon(badgeScaled);
        
        ImageIcon baseSmall = ImageUtilities.loadImageIcon("org/netbeans/modules/project/ui/resources/runProject.png", false);
        smallIcon = ImageUtilities.mergeIcons(baseSmall, badgeSmall, 4, 4);
        
        ImageIcon baseLarge = ImageUtilities.loadImageIcon("org/netbeans/modules/project/ui/resources/runProject24.png", false);
        largeIcon = ImageUtilities.mergeIcons(baseLarge, badgeLarge, 4, 4);
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new RunProjectAction(lkp, Constants.COMMAND_RUN_NAME, "D-A-F6");
    }
    
    public RunProjectAction() {
        this(Utilities.actionsGlobalContext(), Constants.COMMAND_RUN_NAME, "D-A-F6");
    }
    
    public RunProjectAction(final Lookup lkp, String commandName, String accKey) {
        super(lkp,commandName, accKey);
        
        putValue(Action.SMALL_ICON, smallIcon);
        putValue(Action.LARGE_ICON_KEY, largeIcon);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        Project project = getProject();
        CommandHandler commandHandler
                = CommandHandler.createCommandHandler(project);
        if (commandHandler != null) {
            commandHandler.runProject(project);
        }
    } 
}
