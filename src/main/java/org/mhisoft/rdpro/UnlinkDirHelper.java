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

			if (FileUtils.isSymlink(dir.getAbsolutePath())) {

				FileUtils.UnLinkResp out = FileUtils.unlinkDir(dir.getAbsolutePath());
				if (RdPro.debug)
					rdProUI.println("Is a Link dir=" + dir.getAbsolutePath());

				if (out.unlinked)
					rdProUI.println("\t*Unlinked dir " + dir.getAbsolutePath());
				else
					rdProUI.println("\t*Failed to unlink dir " + dir.getAbsolutePath());

				if (RdPro.debug)
					rdProUI.println("command resp:" + out.toString());

				return true;   //tried
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
