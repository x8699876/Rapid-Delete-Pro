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
				//isSymlink does not work for MAC
				out = FileUtils.unlinkDir(dir.getAbsolutePath());
			}
			else {
				if (FileUtils.isSymbolicLink(dir.getAbsolutePath())
						|| FileUtils.isSymlink(dir.getAbsolutePath())) {
					 out = FileUtils.unlinkDir(dir.getAbsolutePath());
				}
				else {
					//do not detect as a link
					return false;
				}
			}

			if (out.unlinked)
				rdProUI.println("\t*Unlinked dir " + dir.getAbsolutePath());

			return out.unlinked;

		} catch (IOException e) {
			rdProUI.println("ERROR: " + e.getMessage());
			e.printStackTrace();
			return true;   //tried
		}
	}
}
