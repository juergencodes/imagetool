package de.mathit.imagetool.rename;


import java.io.File;

class RenameCommand {

    private final File sourceFile;
    private final File targetFile;

    public RenameCommand(final File sourceFile, final File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public File getTargetFile() {
        return targetFile;
    }

}