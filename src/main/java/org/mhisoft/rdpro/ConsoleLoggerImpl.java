package org.mhisoft.rdpro;

/**
 * Description:
 *
 * @author Tony Xue
 * @since Nov, 2014
 */
public class ConsoleLoggerImpl implements Logger {

	@Override
	public void print(final String msg) {
		System.out.print(msg);

	}

	@Override
	public  void println(final String msg) {
		System.out.println(msg);
	}

}
