package de.mathit.imagetool.rename;

import java.util.List;

public class ScriptCreatingRenameQueue extends RenameQueueSupport {

    @Override
    protected void doExecute(final List<RenameCommand> commands) {
        commands.stream().forEach(c -> System.out.println(String.format("ren %s %s", c.getSourceFile().getAbsolutePath(), c.getTargetFile().getAbsolutePath())));
    }

}