package ucas;

public class JsonObject {
	public String articalString;
	public String timeString;
	public String totalString;
	public String titleString;
	public String fileName;
	public String URL;
	
	public JsonObject()
	{
		
	}
	public JsonObject(String artical,String time,String total,String title,String url) {
		articalString = artical;
		timeString = time;
		totalString = total;
		titleString = title;
		URL = url;
	}
}
