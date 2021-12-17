package instatistics.service;

import instatistics.filters.*;
import instatistics.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;

import org.springframework.stereotype.Service;
@Service
public class InstatisticsServiceImpl implements InstatisticsService {
	
	private String token="IGQVJXZAlI3QnhaMUFXanp6SVVhckE2VWU4NTdmaUEyd1BXaFdpMTRtRjJLSFY5NTcxTTdMWENGVEhGSXJiY3YzWm1XTGVJNWlNOU4yR3VWXzRzRUc1Vnk3dkwyVDRmaXJva0lTM1E0ZADhSalBIdlE0MQZDZD"; //da inserire
	private String idPost ="";//inserire

	private String urlUtente="https://graph.instagram.com/me/media?fields=";
	//url per gestire richieste al profilo
	private String urlPost="https://graph.instagram.com/";
	//url per gestire richieste al post
	@Override
	public JSONObject getDataUser(String field) {
	//permette di ottenere informazioni su tutti i post dell'utente 
	//il tipo di informazione è definita dalla variabile fields
		JSONObject data_user=null;

		try {
			URLConnection openConnection=new URL(urlUtente+field+"&access_token="+token).openConnection();
			InputStream in=openConnection.getInputStream();

			String data="";
			String line="";
			try {
				InputStreamReader inR= new InputStreamReader(in);
				BufferedReader buf= new BufferedReader(inR);
				while((line=buf.readLine()) != null) {
					data+=line;
				}

			}finally {in.close();}
        //String[] fields = field.split(","); per èassare gli argomenti di req param ai metodi
       
			data_user=(JSONObject) JSONValue.parseWithException(data);
		}catch (IOException e ) {System.out.println("Errore");}
		catch (Exception e) {System.out.println("Errore");}
		return data_user;
	}
	
	@Override
	public JSONObject getAllUser() {
	//permette di ottenere tutte le info possibili da tutti
	//i post dell'utente
		JSONObject all_user=null;

		try {
			URLConnection openConnection=new URL(urlUtente+"media_type,caption,timestamp"+"&access_token="+token).openConnection();
			InputStream in=openConnection.getInputStream();

			String data="";
			String line="";
			try {
				InputStreamReader inR= new InputStreamReader(in);
				BufferedReader buf= new BufferedReader(inR);
				while((line=buf.readLine()) != null) {
					data+=line;
				}

			}finally {in.close();}

			all_user=(JSONObject) JSONValue.parseWithException(data);
		}catch (IOException e ) {System.out.println("Errore");}
		catch (Exception e) {System.out.println("Errore");}
		return all_user;
	}
	@Override
	public JSONObject getDataPost(String field) {
		//permette di ottenere informazioni su un post dell'utente 
		//il tipo di informazione è definita dalla variabile fields
			JSONObject data_post=null;
			                     
			try {
				URLConnection openConnection=new URL(urlPost+idPost+"/?fields="+field+"&access_token="+token).openConnection();
				InputStream in=openConnection.getInputStream();

				String data="";
				String line="";
				try {
					InputStreamReader inR= new InputStreamReader(in);
					BufferedReader buf= new BufferedReader(inR);
					while((line=buf.readLine()) != null) {
						data+=line;
					}

				}finally {in.close();}

				data_post=(JSONObject) JSONValue.parseWithException(data);
			}catch (IOException e ) {System.out.println("Errore");}
			catch (Exception e) {System.out.println("Errore");}
			return data_post;
		}
		
	@Override
	public JSONObject getAllPost() {
		//permette di ottenere tutte le info possibili da un
		//solo post dell'utente
		JSONObject all_post=null;

		try {
			URLConnection openConnection=new URL(urlPost+idPost+"/?fields=media_type,caption,timestamp"+"&access_token="+token).openConnection();
			InputStream in=openConnection.getInputStream();

			String data="";
			String line="";
			try {
				InputStreamReader inR= new InputStreamReader(in);
				BufferedReader buf= new BufferedReader(inR);
				while((line=buf.readLine()) != null) {
					data+=line;
				}

			}finally {in.close();}

			all_post=(JSONObject) JSONValue.parseWithException(data);
		}catch (IOException e ) {System.out.println("Errore");}
		catch (Exception e) {System.out.println("Errore");}
		return all_post;
	}


	
	public ArrayList<Post>  JsonReading() {
		//prende il JSON che ritorna l'api di instagram
		//e lo mette in un ArrayList
		ArrayList <Post> postList = new ArrayList<Post>();
		JSONObject file =getAllUser();
		//Ottengo il jsonArray che contiene la lista di tutti dati di tutti post
        JSONArray jsonArrayPost = (JSONArray) file.get("data");
        //Per ogni oggetto del JSONArray prendo i value e
        //li salvo in un oggetto Post
        for (int i=0;i<jsonArrayPost.size(); i++) {
        	String caption=(((JSONObject) jsonArrayPost.get(i)).get("caption")).toString();
        	String id=(((JSONObject) jsonArrayPost.get(i)).get("id")).toString();
        	String timestamp=(((JSONObject) jsonArrayPost.get(i)).get("timestamp")).toString();
        	String media_type=(( (JSONObject) jsonArrayPost.get(i)).get("media_type")).toString();
        	
        	Post post = new Post (media_type,caption,id,timestamp);
        
        	postList.add(i, post);
        }
        return postList;
	}
	//|| Argoument == "VIDEO" || Argoument == "CAROUSEL_ALBUM"
	@SuppressWarnings("unchecked")
	public JSONObject getMedia(String Argoument) {
		
		JSONObject jj = new JSONObject();
		if (Argoument.equals("IMAGE") || Argoument.equals("VIDEO") || Argoument.equals("CARUSEL_ALBUM") ) {
		
			ArrayList<Post> pp=new ArrayList<Post>();
			pp=JsonReading();
			MediaType mt=new MediaType(pp);
			jj.put("Numero ripetizioni", mt.NumberOfRepetition(Argoument));
			jj.put("Ranking", mt.Ranking(null));
			jj.put("Suggestion", mt.Suggestion(null));
		}
		else {
			
			jj.put("Ranking", "Errore");
			
		}
		return jj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getTimestamp(String Argoument) {
		ArrayList<Post> pp=new ArrayList<Post>();
		pp=JsonReading();
		TimeStamp tp=new TimeStamp(pp);
		JSONObject jj=new JSONObject();
		jj.put("Numero di post", tp.NumberOfRepetition(Argoument));
		return jj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getRankingTimestamp(String data) {
		ArrayList<Post> pp=new ArrayList<Post>();
		pp=JsonReading();
		String[] split=data.split(",");
		TimeStamp tp=new TimeStamp(pp); 
		
		JSONObject jj=new JSONObject();
		jj.put("Risultato:",tp.Ranking(split) );
		return jj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getNumberOfCaption(String caption) {
		ArrayList<Post> pp=new ArrayList<Post>();
		pp=JsonReading();
		Caption cc=new Caption(pp);
		JSONObject jj=new JSONObject();
		jj.put("La caption è presente", cc.NumberOfRepetition(caption));
		return jj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getRankingOfCaption(String caption) {
		ArrayList<Post> pp=new ArrayList<Post>();
		pp=JsonReading();
		Caption cc=new Caption(pp);
		JSONObject jj=new JSONObject();
		String[] sp=caption.split(",");
		jj.put("Risultato:", cc.Ranking(sp));
		return jj;
		
	}
		
	@SuppressWarnings("unchecked")
	public JSONObject getSuggestionCaption(String theme) {
		JSONObject jj=new JSONObject();
		if (theme == "sport" || theme == "cerimonia" || theme == "insieme") {
			Caption cc=new Caption (null);
			jj.put("Hashtag consigliato",cc.Suggestion(theme));
			
		}
		else {
			String error = "Errore, inserire un tema  a scelta tra: sport, insieme o cerimonia";
			jj.put("Post", error );
		}
		return jj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getFilterYear(String year) {
		JSONObject jj=new JSONObject();
		if (year.length() == 4) {
		ArrayList<Post> pp=new ArrayList<Post>();
		pp=JsonReading();
		FiltroAnno fa=new FiltroAnno(pp);
		
		jj.put("Post", fa.post_annuali(year));
		}
		else {
		String error = "Errore, inserire un anno valido";
		jj.put("Post", error );
		}
		return jj;
		
	}













}

		