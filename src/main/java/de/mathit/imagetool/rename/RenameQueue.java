package de.mathit.imagetool.rename;

import java.io.File;

public interface RenameQueue {

    void add(final String directory, final String sourceName, final String targetName);

    void add(final File sourceFile, final File targetFile);

    void execute();

}