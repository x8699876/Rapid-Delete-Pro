package org.mhisoft.rdpro;

import java.io.File;
import java.io.IOException;

import org.mhisoft.rdpro.ui.RdProUI;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Sep, 2016
 */
public class UnlinkDirHelper {

	static String testDir =System.getProperty("user.home")+"/rdpro-test-folder";

	//todo make the hard link and verify it works.
	public static boolean verifyUnlinkToolSetup() {
		new File(testDir).mkdir();
		new File(testDir+"/notalink").mkdir();
		new File(testDir+"/folder2").mkdir();
		return true;
	}

	/**
	 * Test if it is a link, unlink it if so.
	 * return false if it is a real directory , i.e. not alink.
	 * return true if it is a link and unlinked. 
	 *
	 * @param rdProUI
	 * @param props
	 * @param dir
	 * @return
	 */
	public static boolean unLinkDir(final RdProUI rdProUI, final RdProRunTimeProperties props, final File dir) {
		try {
			if (!props.isUnLinkDirFirst())
				return false; //continue the deletion of all the files under the link based on user's choice.

			FileUtils.UnLinkResp out=null;
			if (OSDetectUtils.getOS() == OSDetectUtils.OSType.MAC) {
				if (FileUtils.isJunction(dir.getAbsolutePath())) {
					//isSymlink does not work for MAC
					out = FileUtils.removeMacHardLink(dir.getAbsolutePath());
				}
			}
			else if (FileUtils.isWindows()) {

				if (FileUtils.isSymbolicLink(dir.getAbsolutePath())) {
					out = FileUtils.removeWindowsSymbolicLink(dir.getAbsolutePath());
				}
				else if ( FileUtils.isJunction(dir.getAbsolutePath())) {
					 out = FileUtils.removeWindowsJunction(dir.getAbsolutePath());
				}
				else {
					//do not detect as a link
					return false;
				}
			}
			else {
				throw new RuntimeException("The unlink is not supported on this OS:" +  System.getProperty("os.name"));
			}

			if (out.unlinked)
				rdProUI.println("\t*Unlinked dir " + dir.getAbsolutePath());

			return out.unlinked;

		} catch (IOException e) {
			rdProUI.println("ERROR: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e); //break out
		}
	}
}
