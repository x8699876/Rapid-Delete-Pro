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
				return false; //continue the deletion.



			if (FileUtils.isSymlink(dir.getAbsolutePath())) {   //this does not work for Mac , always return true
//				if (RdPro.debug)
//					rdProUI.println("Is a Link dir=" + dir.getAbsolutePath());

				FileUtils.UnLinkResp out = FileUtils.unlinkDir(dir.getAbsolutePath());
				if (RdPro.debug)
					rdProUI.println("Try with FileUtils.unlinkDir() command, resp:" + out.toString());



//				if (out.unlinked)
//					rdProUI.println("\t*Unlinked dir " + dir.getAbsolutePath());
//				else {
//					rdProUI.println("\t*Failed to unlink dir " + dir.getAbsolutePath());
//
//				}

				//for mac we need to use the UnLinkResp from  unlinkDir();
				if (OSDetectUtils.getOS() == OSDetectUtils.OSType.MAC) {
					return out.unlinked;
				}

				if (out.unlinked)
					rdProUI.println("\t*Unlinked dir " + dir.getAbsolutePath());


				return true;
			} else {
				if (RdPro.debug)
					rdProUI.println("Not a Link dir=" + dir.getAbsolutePath());


				//not a link
				return false;
			}

		} catch (IOException e) {
			rdProUI.println("ERROR: " + e.getMessage());
			e.printStackTrace();
			return true;   //tried
		}
	}
}
