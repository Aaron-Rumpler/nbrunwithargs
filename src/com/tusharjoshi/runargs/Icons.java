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
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.util.ImageUtilities;

/**
 * Shared run/debug-with-arguments icons. The base NetBeans run/debug icons are
 * merged with an "edit" badge so they visually distinguish the "with
 * Arguments..." actions from the vanilla run/debug ones. Centralised here so
 * every action in this module (and any consumer resolving our actions through
 * the system filesystem) shares the same pre-computed instances.
 */
final class Icons {

    static final Icon RUN_SMALL;
    static final Icon RUN_LARGE;
    static final Icon DEBUG_SMALL;
    static final Icon DEBUG_LARGE;

    static {
        ImageIcon runBaseSmall = ImageUtilities.loadImageIcon(
                "org/netbeans/modules/project/ui/resources/runProject.png", false);
        ImageIcon runBaseLarge = ImageUtilities.loadImageIcon(
                "org/netbeans/modules/project/ui/resources/runProject24.png", false);
        ImageIcon debugBaseSmall = ImageUtilities.loadImageIcon(
                "org/netbeans/modules/debugger/resources/debugProject.png", false);
        ImageIcon debugBaseLarge = ImageUtilities.loadImageIcon(
                "org/netbeans/modules/debugger/resources/debugProject24.png", false);

        // ImageUtilities.loadImageIcon(..., false) returns null when the
        // resource can't be resolved -- e.g. when the profiler module that
        // owns edit.svg isn't enabled, or simply hasn't been wired into this
        // module's classloader yet at the moment <clinit> runs. Without this
        // guard, badgeSmall.getImage() throws NPE in the static initialiser,
        // which marks Icons erroneous and cascades into NoClassDefFoundError
        // for every action class that references it -- and through the
        // Toolbars/Build FolderInstance, a misleading cyclic-reference
        // IOException. Fall back to the un-badged base icons in that case;
        // the "with Arguments..." actions just render without the edit badge.
        ImageIcon badgeSmall = ImageUtilities.loadImageIcon(
                "org/netbeans/modules/profiler/impl/icons/edit.svg", false);
        if (badgeSmall == null) {
            RUN_SMALL = runBaseSmall;
            RUN_LARGE = runBaseLarge;
            DEBUG_SMALL = debugBaseSmall;
            DEBUG_LARGE = debugBaseLarge;
        } else {
            BufferedImage badgeScaled = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = badgeScaled.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.drawImage(badgeSmall.getImage(), 0, 0, badgeScaled.getWidth(), badgeScaled.getHeight(), null);
            g.dispose();
            ImageIcon badgeLarge = new ImageIcon(badgeScaled);

            RUN_SMALL = ImageUtilities.mergeIcons(runBaseSmall, badgeSmall, 4, 4);
            RUN_LARGE = ImageUtilities.mergeIcons(runBaseLarge, badgeLarge, 4, 4);
            DEBUG_SMALL = ImageUtilities.mergeIcons(debugBaseSmall, badgeSmall, 4, 4);
            DEBUG_LARGE = ImageUtilities.mergeIcons(debugBaseLarge, badgeLarge, 4, 4);
        }
    }

    private Icons() {}
}
