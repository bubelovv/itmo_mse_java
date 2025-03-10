package ru.itmo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;

@Mojo(name = "git", defaultPhase = LifecyclePhase.PACKAGE)
public class GitMojo extends AbstractMojo {
    public void execute() throws MojoExecutionException {
        try {
            String currentBranch = new FileRepositoryBuilder()
                    .setWorkTree(new File("."))
                    .findGitDir()
                    .build()
                    .getBranch();
            getLog().info("Git branch: " + currentBranch);
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to read", e);
        }
    }
}