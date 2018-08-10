package de.mathit.imagetool.rename;

import java.util.List;

public class RealRenameQueue extends RenameQueueSupport {

    @Override
    protected void doExecute(final List<RenameCommand> commands) {
        for (final RenameCommand c : commands) {
            System.out.println(c.getSourceFile().renameTo(c.getTargetFile()));
        }
    }

}