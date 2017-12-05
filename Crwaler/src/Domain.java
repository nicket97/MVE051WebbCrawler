
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Domain implements Runnable {
	//hashset f�r att h�lla totala listen med sidor
	private HashSet<String> urls = new HashSet<String>();
	//queue f�r att h�lla de urler som ska crawlas
	private PriorityQueue<String> queue = new PriorityQueue<String>();
	//en Arraylist som h�ller sidor under Dissalow:
	private ArrayList<String> dAllow = new ArrayList<String>();
	//h�ller crawldelayen
	private int crawlDealy = 1;
	//Sting som h�ller dom�nnamnet till inl�gget i databasen
	private String d = "";
	public static final boolean DEBUG = false;
    public static final String DISALLOW = "Disallow:";

    public static Index index;
	// konstruktor f�r Domain
	public Domain(String domain, String startUrl){
		try {
			index = new Index();
			Thread t = new Thread(index);
			t.start();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		urls.add(startUrl);
		d = domain;
		queue.offer(startUrl);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		HashSet<String> current = new HashSet<String>();
		while(true){
		Document doc;
		try {
			//System.out.println(queue.peek());
			if(queue.size() > 0){
				doc = Jsoup.connect(queue.peek())
						.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
						.referrer("http://www.google.com") 
						.get();
				queue.poll();
				org.jsoup.select.Elements links = doc.select("a");
				//System.out.printf("%s", links);
				for(Element e: links){
					String link = e.attr("abs:href");
					//System.out.println(link);
	                if((link.contains(".se") || link.contains("/sv.")) && !link.contains("mailto:") && !link.contains(".JPG") && !link.contains(".PNG")
	                        && !link.contains(".JPEG") && !link.contains(".SVG") && !link.contains(".jpg") && !link.contains(".png") && !link.contains(".jpeg")
	                        && !link.contains(".svg") && !link.contains("@")){
	                    if(link.contains("#")){
	                        link = link.substring(0, link.indexOf("#"));
	                    }
	                    if(link.contains("&")){
	                        link = link.substring(0, link.indexOf("&"));
	                    }
	                    if(link.contains("?")){
	                        link = link.substring(0, link.indexOf("?"));
	                    }
	                    link = link.replaceAll(",","");
	                    link = link.replaceAll("'","");
	                    
	                    if(link.contains(d)){
	                    	//if(robotSafe(link)){
	                    	//System.out.println(d);
			                if(urls.add(link)){
			                	queue.offer(link);
			                	current.add(link);
			                	//System.out.println(queue.size());
			                //}
	                    	}
		                }
		                else{
		                	//System.out.println(d);
		                	//System.out.println("ny dom�n "+ link);
		                	//Start.newDomain(link);
		                	break;
		                }
	                }
	                
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try {
			Thread.sleep(1);
			//System.out.println(queue.size() + " URLER");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		Index.indexInsert(current);
		current.clear();
		}
	}
	public void setDisallow(ArrayList<String> d){
		dAllow.addAll(d);
	}
	
}
	
