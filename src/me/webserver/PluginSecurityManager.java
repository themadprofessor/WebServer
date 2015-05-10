package me.webserver;

import me.util.Log;

import java.io.FileDescriptor;
import java.security.Permission;

/**
 * Created by User on 27/04/2015.
 */
public class PluginSecurityManager extends SecurityManager {
    private ThreadLocal<Boolean> isEnabled;

    public PluginSecurityManager(boolean isEnabled) {
        this.isEnabled = new ThreadLocal<>();
        this.isEnabled.set(isEnabled);
    }

    @Override
    public void checkConnect(String host, int port) {
        if (isEnabled.get())
            throw new SecurityException("Plugins Should Not Open Connections");
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        if (isEnabled.get())
            throw new SecurityException("Plugins Should Not Open Connections");
    }

    @Override
    public void checkExit(int status) {
        if (isEnabled.get())
            throw new SecurityException("Plugins Should Not Close The VM");
    }

    @Override
    public void checkListen(int port) {
        if (isEnabled.get())
            throw new SecurityException("Plugins Should Not Listen For New Connections");
    }

    @Override
    public void checkAccept(String host, int port) {
        if (isEnabled == null) {
            Log.out("ARGH");
        }
        if (isEnabled.get() == null) {
            Log.out("FUUU");
        }
        if (isEnabled.get())
            throw new SecurityException("Plugins Should Not Accept New Connections");
    }

    @Override
    public void checkExec(String cmd) {
        if (isEnabled.get())
            throw new SecurityException("Plugins Should Not Execute System Programs");
    }

    @Override
    public void checkPropertyAccess(String key) {

    }

    @Override
    public void checkCreateClassLoader() {
        if (isEnabled.get()) {
            throw new SecurityException("Plugins Should Not Load Unloaded Classes ");
        }
    }

    @Override
    public void checkDelete(String file) {

    }

    @Override
    public void checkPermission(Permission perm) {

    }

    @Override
    public void checkPermission(Permission perm, Object context) {

    }

    @Override
    public void checkAccess(Thread t) {

    }

    @Override
    public void checkAccess(ThreadGroup g) {

    }

    @Override
    public void checkLink(String lib) {

    }

    @Override
    public void checkRead(FileDescriptor fd) {

    }

    @Override
    public void checkRead(String file) {

    }

    @Override
    public void checkRead(String file, Object context) {

    }

    @Override
    public void checkWrite(FileDescriptor fd) {

    }

    @Override
    public void checkWrite(String file) {

    }

    @Override
    public void checkPrintJobAccess() {

    }

    @Override
    public void checkSetFactory() {

    }

    public void setEnabled(boolean enabled) {
        isEnabled.set(enabled);
    }
}
