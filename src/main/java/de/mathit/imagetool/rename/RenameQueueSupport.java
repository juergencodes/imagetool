package de.mathit.imagetool.rename;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

abstract class RenameQueueSupport implements RenameQueue {

    private List<RenameCommand> commands = new LinkedList<>();

    @Override
    public void add(final String directory, final String sourceName, final String targetName) {
        add(new File(directory, sourceName), new File(directory, targetName));
    }

    @Override
    public void add(final File sourceFile, final File targetFile) {
        if (targetFile == null) {
            System.err.println("Ignoring null target file for source: " + sourceFile);
        } else {
            commands.add(new RenameCommand(sourceFile, targetFile));
        }
    }

    @Override
    public void execute() {
        if (commands == null) {
            throw new IllegalStateException("Cannot execute twice.");
        }
        doExecute(commands);
        commands = null;
    }

    protected abstract void doExecute(final List<RenameCommand> commands);

}