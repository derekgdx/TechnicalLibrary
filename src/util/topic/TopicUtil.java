package util.topic;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

public class TopicUtil {
	private static List<TopicHeader> allTopicHeader;
	
	public static List<TopicHeader> getAllTopicHeader(ServletContext context) {
		if(allTopicHeader==null)
			allTopicHeader = new ArrayList<TopicHeader>();
		else
			allTopicHeader.clear();
		InputStream is = null;
		Set topicSet = null;

		topicSet = context.getResourcePaths("/topic");
		List topicList = new ArrayList(topicSet);
		Collections.sort(topicList);
		Iterator<String> it = topicList.iterator();
		while (it.hasNext()) {
			String path = it.next();
			if( path.indexOf("index.html")>-1 )
				continue;
			//System.out.println(path);
			try {
				is = context.getResourceAsStream( path );

				String str;
				String titleLine = null;
				String authorLine = null;
				String title = null;
				String author = null;
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				while ((str = br.readLine()) != null) {
					// System.out.println(str);
					if (str.indexOf("<title>") > -1)
						titleLine = str;
					if (str.indexOf("作者：") > -1) {
						authorLine = br.readLine();
						break;
					}
				}
				if(titleLine!=null){
					title = titleLine.substring(
							titleLine.indexOf("<title>") + 7,
							titleLine.indexOf("</title>"));
				}
				if(authorLine!=null){
					//System.out.println(authorLine);
					if(authorLine.length()>18)
						author = authorLine.substring(
							authorLine.indexOf("<span id=\"author\">") + 18,
							authorLine.indexOf("</span>"));
					else
						author = null;
				}
				//System.out.println(title == null ? "" : title);
				//System.out.println(author == null ? "" : author);
				title = title == null ? "" : title;
				author = author == null ? "" : author;
				allTopicHeader.add(new TopicHeader(path, title, author));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return allTopicHeader;
	}
}