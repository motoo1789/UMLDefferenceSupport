package posttest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PostTest {

	final String urlString = "http://127.0.0.1:8888/";

	public void posttest()
	{
		try {
			URL url = new URL(urlString);
			URLConnection uc = url.openConnection();
			uc.setDoOutput(true);
			uc.setRequestProperty("Accept-Language", "ja");
			uc.connect();

			OutputStream os = uc.getOutputStream();

			PrintStream ps = new PrintStream(os);
			ps.print(os);
			ps.close();

		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
