import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;

public class Start {
	
	// en set f�r att h�lla domain object
		public static HashSet<String> domains = new HashSet<String>();
		//url f�r att b�rja crawlingen
		public String url = "https://sv.wikipedia.org/wiki/Portal:Huvudsida";
		
		// konstruktor f�r Start
		public Start(){
			
			newDomain(url);
			
			
		}
		//metod som tar hand om nya dom�ner
		public static void newDomain(String u){
			//TODO l�gg till setDissalowd annrop
			// object f�r uri
			URI uri = null;
			// f�rs�ker skapa ett URI object med l�nken till konstruktorn
			try {
				uri = new URI(u);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			// om jag lyckades skapa objectet l�gg till ett nytt object f�r domain med dom�nen som h�mtas fr�n objectet
			if(uri != null){	
		    String domain = uri.getHost();
		    	if(domains.add(domain)){
		    domain = domain.startsWith("www.") ? domain.substring(4) : domain;
		    Domain d  = new Domain(domain, u);
		    //System.out.println(domains.size());
		    Thread thread = new Thread(d);
			thread.start();
		    
				}
		    }
		}
		// main metod
		public static void main(String args[]){
			new Start();
		}
}
