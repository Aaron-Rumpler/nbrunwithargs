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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.api.java.source.SourceUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.WeakListeners;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "com.tusharjoshi.runargs.RunFileAction"
)
@ActionRegistration(
        displayName = "#CTL_RunFileAction",
        lazy = false
)
@ActionReferences({
    @ActionReference(path = "Menu/BuildProject", position = 651),
    @ActionReference(path = "Loaders/text/x-java/Actions", position = 1001),
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1721),
    @ActionReference(path = "Shortcuts", name = "A-S-F6")
})
@Messages("CTL_RunFileAction=Run File with Arguments...")
public final class RunFileAction extends AbstractAction implements ContextAwareAction {

    private final Lookup lkp;
    private final LookupListener listener;
    private final Lookup.Result<DataObject> result;

    public RunFileAction() {
        this(Utilities.actionsGlobalContext());
    }

    private RunFileAction(Lookup lkp) {
        this.lkp = lkp;
        putValue(NAME, Bundle.CTL_RunFileAction());
        putValue(SMALL_ICON, Icons.RUN_SMALL);
        putValue(LARGE_ICON_KEY, Icons.RUN_LARGE);
        this.listener = (LookupEvent ev) -> updateEnabled();
        this.result = lkp.lookupResult(DataObject.class);
        this.result.addLookupListener(
                WeakListeners.create(LookupListener.class, listener, this.result));
        updateEnabled();
    }

    @Override
    public Action createContextAwareInstance(Lookup lkp) {
        return new RunFileAction(lkp);
    }

    private void updateEnabled() {
        DataObject dataObject = lkp.lookup(DataObject.class);
        if (dataObject == null) {
            setEnabled(false);
            return;
        }
        FileObject fo = dataObject.getPrimaryFile();
        if (!"java".equalsIgnoreCase(fo.getExt())) {
            setEnabled(false);
            return;
        }
        setEnabled(!SourceUtils.getMainClasses(fo).isEmpty());
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DataObject dataObject = lkp.lookup(DataObject.class);
        CommandHandler commandHandler = CommandHandler.createCommandHandler(dataObject);
        if (commandHandler != null) {
            commandHandler.runFile(dataObject);
        }
    }
}
