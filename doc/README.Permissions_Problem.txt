Below is the result of the search-all for "setPermissions".

This has led me back around to "FileUtils.extractByList()", a private method.  It is calling the "setPermissions(3 params)" method, once for each of a list of FileEntry objects.

This code is what creates the list.  It is using "FileList().loadXml()".  Note that the param comes from "extractJarEntry", to then be fed into the XML load.  This "loadXml()"
apparently is what is setting the permissions on the entries, that are followed by the setter later.  The next step, then, is to locate "FilesList.java"

import org.netbeans.installer.utils.helper.FilesList;


---- SAX Handler in FilesList


This is what is setting up the permissions.   <entry type="file" ... permissions="644" />     -- either the attribute is omitted, or set to 0.

        public void startElement(
                final String uri,
                final String localName,
                final String qName,
                final Attributes attributes) throws SAXException {
            if (qName.equals("entry")) {
                entryElement = true;
                
                String type = attributes.getValue("type");
                if (type.equals("file")) {
                    directory = false;
                    
                    size = Long.parseLong(attributes.getValue("size"));
                    md5 = attributes.getValue("md5");
                    jarFile = Boolean.parseBoolean(attributes.getValue("jar"));
                    
                    if (jarFile) {
                        packed = Boolean.parseBoolean(attributes.getValue("packed"));
                        signed = Boolean.parseBoolean(attributes.getValue("signed"));
                    } else {
                        packed  = false;
                        signed  = false;
                    }
                    
                    modified = Long.parseLong(attributes.getValue("modified"));
                    permissions = Integer.parseInt(attributes.getValue("permissions"), 8);
                } else {
                    directory = true;
                    empty = Boolean.parseBoolean(attributes.getValue("empty"));
                    modified = Long.parseLong(attributes.getValue("modified"));
                    permissions = Integer.parseInt(attributes.getValue("permissions"), 8);
                }
            } else {
                entryElement = false;
            }
        }


----  Now searching against the cylindrical alignment app codebase. -----

Found this.  Best bet: go look at build/installer/nbi_all.

Also study: https://installer.netbeans.org/docs/nbi-devguide.html

C:\current_projects\gitfiles\CylindricalAlignmentApp>\tools\cygwin\bin\find . -name \*.properties -exec grep entry {} ; -print
      0 [main] find 27404 find_fast_cwd: WARNING: Couldn't compute FAST_CWD pointer.  Please report this problem to
the public mailing list cygwin@cygwin.com
# used in creating the registry entry for this group
./build/installer/.common/group.properties
P.create.start.menu.shortcut.windows=Create Start menu entry
./build/installer/ext/components/products/helloworld/src/org/mycompany/wizard/panels/Bundle.properties
# used in creating the registry entry for this group
./build/installer/nbi_all/.common/group.properties
P.create.start.menu.shortcut.windows=Create Start menu entry
./build/installer/nbi_all/ext/components/products/helloworld/src/org/mycompany/wizard/panels/Bundle.properties
FU.error.output.file.entry=An file entry {0} exists and is not a file
FU.error.output.dir.entry=An directory entry {0} exists and is not a directory
./build/installer/nbi_all/ext/infra/build/engine/build/ext/engine/build/classes/org/netbeans/installer/utils/Bundle.properties
SFJA.entry.label={0} (v. {1} [{2}] by {3})
SFJA.entry.label.noarch={0} (v. {1} by {2})
SFJA.entry.label.non.final=<html><span>{0} (v. {1} [{2}] <span color="red">(non-final)</span> by {3})</span></html>
./build/installer/nbi_all/ext/infra/build/engine/build/ext/engine/build/classes/org/netbeans/installer/wizard/components/actions/Bundle.properties
P.create.start.menu.shortcut.windows=Create Start menu entry
./build/installer/nbi_all/ext/infra/build/products/helloworld/build/ext/components/products/helloworld/build/classes/org/mycompany/wizard/panels/Bundle.properties
P.create.start.menu.shortcut.windows=Create Start menu entry
./build/installer/nbi_all/ext/infra/build/products/helloworld/build/ext/components/products/helloworld/src/org/mycompany/wizard/panels/Bundle.properties


------  Search Results  ------

                    final File initialList =
                            extractJarEntry(FILES_LIST_ENTRY, file);
                    final FilesList toExtract =
                            new FilesList().loadXml(initialList, target);

C:\current_projects\hgfiles\NetBeans>\tools\cygwin\bin\find . -name \*.java -exec grep setPermissions {} ; -print
            SystemUtils.setPermissions(installScript, FileAccessMode.EU, NativeUtils.FA_MODE_ADD);
            SystemUtils.setPermissions(uninstallScript, FileAccessMode.EU, NativeUtils.FA_MODE_ADD);
./installer/components/products/mysql/src/org/netbeans/installer/products/mysql/ConfigurationLogic.java

    public void setPermissionsElevated(boolean enabled) {
./javafx2.project/src/org/netbeans/modules/javafx2/project/JFXProjectProperties.java

        jfxProps.setPermissionsElevated(sel);
./javafx2.project/src/org/netbeans/modules/javafx2/project/ui/JFXDeploymentPanel.java

    public void setPermissions(String s) {
./nbbuild/antsrc/org/netbeans/nbbuild/MakeJNLP.java

                SystemUtils.setPermissions(
./nbi/engine/src/org/netbeans/installer/utils/FileUtils.java

    public abstract void setPermissions(
./nbi/engine/src/org/netbeans/installer/utils/system/NativeUtils.java

        SystemUtils.setPermissions(file, newPermissions, FA_MODE_SET);
    public void setPermissions(File file, int mode, int change) throws IOException {
        setPermissionsNative(file.getAbsolutePath(), mode, change);
    private native void setPermissions0(String path, int mode, int change);
    private void setPermissionsJ(String path, int mode, int change) throws IOException {
    private void setPermissionsNative(String path, int mode, int change) throws IOException {
            setPermissions0(path,mode,change);
            setPermissionsJ(path,mode,change);
./nbi/engine/src/org/netbeans/installer/utils/system/UnixNativeUtils.java

    public void setPermissions(
./nbi/engine/src/org/netbeans/installer/utils/system/WindowsNativeUtils.java
    
    public static void setPermissions(final File file, final int mode, final int change) throws IOException {
        getNativeUtils().setPermissions(file, mode, change);
./nbi/engine/src/org/netbeans/installer/utils/SystemUtils.java

    public void setPermissions(final int permissions) {
./nbi/infra/build/.ant-lib/src/org/netbeans/installer/infra/build/ant/utils/FileEntry.java

    public synchronized boolean setPermissions(int permissions, String path) throws RemoteException {
./php.project/src/org/netbeans/modules/php/project/connections/ftp/FtpClient.java
                                permissionsSet = remoteClient.setPermissions(oldPermissions, fileName);

./php.project/src/org/netbeans/modules/php/project/connections/RemoteClient.java
    public synchronized boolean setPermissions(int permissions, String path) throws RemoteException {

./php.project/src/org/netbeans/modules/php/project/connections/sftp/SftpClient.java

     * @see #setPermissions(int, java.lang.String)
    boolean setPermissions(int permissions, String path) throws RemoteException;
./php.project/src/org/netbeans/modules/php/project/connections/spi/RemoteClient.java
                        .setPermissionsPreserved(ProjectPropertiesSupport.areRemotePermissionsPreserved(project))

    public RunConfigRemote setPermissionsPreserved(boolean permissionsPreserved) {
./php.project/src/org/netbeans/modules/php/project/runconfigs/RunConfigRemote.java

                .setPermissionsPreserved(preservePermissionsCheckBox.isSelected())
./php.project/src/org/netbeans/modules/php/project/ui/customizer/RunAsRemoteWeb.java
