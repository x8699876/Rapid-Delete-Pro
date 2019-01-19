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
	public void unlinkTest() {
			try {

				String linkDir = FileUtilsTest.testDir+ "/rdpro-target-link";
				FileUtilsTest.setupTestLinks(linkDir) ;


				String realDirNoLink = FileUtilsTest.testDir +"/notalink";
				System.out.println(realDirNoLink);
				System.out.println("isJunction=" + FileUtils.isJunction(realDirNoLink));

				boolean unlinked = UnlinkDirHelper.unLinkDir(ui,  props,  new File(realDirNoLink));
				System.out.println("tried unlink dir "+ realDirNoLink+", unlinked:"+unlinked +", expected:" + false);
				Assert.assertFalse(unlinked);
				System.out.println("pass");

				System.out.println("----------hard link test");
				unlinked = UnlinkDirHelper.unLinkDir(ui,  props,  new File(linkDir));
				System.out.println("tried unlink dir "+ linkDir+" ,resp:"+unlinked+", expected:" + true);
				Assert.assertTrue(unlinked);
				System.out.println("pass");

				System.out.println("----------soft, symbolic link test");
				String softLink = FileUtilsTest.testDir+"/symbolic-link";
				unlinked = UnlinkDirHelper.unLinkDir(ui,  props,  new File(softLink));
				System.out.println("tried remove soft link: "+ softLink+" ,resp:"+unlinked+", expected:" + true);
				Assert.assertTrue(unlinked);
				System.out.println("pass");

				//FileUtilsTest.setupTestLinks(linkDir) ;



			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
