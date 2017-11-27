package org.mhisoft.rdpro;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.mhisoft.rdpro.ui.ConsoleRdProUIImpl;
import org.mhisoft.rdpro.ui.RdProUI;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2017
 */
public class UnlinkDirHelperTest {
	static RdProRunTimeProperties props = new RdProRunTimeProperties();
	RdProUI ui = new ConsoleRdProUIImpl() ;


	@org.junit.BeforeClass
	public static void setup() {
		props.setUnLinkDirFirst(true);
	}


	@Test
	public void macUnlinkTest() {
		if (OSDetectUtils.getOS() == OSDetectUtils.OSType.MAC) {
			try {

				String linkDir = System.getProperty("user.home")+"/bin/hlink/test-folder/rdpro-target-link";
				FileUtilsTest.makeTestLink(linkDir) ;


				String realDirNoLink = System.getProperty("user.home")+"/bin/hlink/test-folder/notalink";
				System.out.println(realDirNoLink);
				System.out.println("isSymlink=" + FileUtils.isSymlink(realDirNoLink));
				FileUtils.UnLinkResp out = FileUtils.unlinkDir(realDirNoLink);
				System.out.println("output of command:" + out);
				boolean unlinked = UnlinkDirHelper.unLinkDir(ui,  props,  new File(realDirNoLink));
				System.out.println("tried unlink dir "+ realDirNoLink+", unlinked:"+unlinked +", expected:" + false);
				Assert.assertFalse(unlinked);

				System.out.println("----------");



				unlinked = UnlinkDirHelper.unLinkDir(ui,  props,  new File(linkDir));
				System.out.println("tried unlink dir "+ linkDir+" ,resp:"+unlinked+", expected:" + true);
				Assert.assertTrue(unlinked);

				FileUtilsTest.makeTestLink(linkDir) ;



			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}
}
