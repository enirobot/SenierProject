//매일 신문
//뉴스개수(날짜기준으로)지정하여 해당 키워드가 존재하는 뉴스 제목의 기사에 한하여 
//키워드, 날짜, url(or 번호), 분류(사회,경제 등), 본문 내용 각각을 크롤링 하여 csv 파일로 저장
 import java.io.BufferedWriter;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStreamWriter;
 
 import org.jsoup.Jsoup;
 import org.jsoup.nodes.Document;
 import org.jsoup.nodes.Element;
 import org.jsoup.select.Elements;
 
 class CSVFormat{
 	public String articleContent;
 	public String articleCategory;
 	public String articleDate;
 	public String keyword;
 	public void resetData(){
 		this.articleCategory = null;
 		this.articleContent = null;
 		this.articleDate = null;
 		this.keyword = null;
 	}
 }
 public class crawling{
 	public String field = "키워드, 날짜, URL, 분류, 내용 \r\n";
 	public String csvFileName = "c:/2015.csv";
 	public String dateTag = "BUILD";
 	public String category = "KEYWORDS";
 	public String keyword = "NEWS-KEYWORDS";
 	private CSVFormat format = new CSVFormat();
 	public String url;
 	private Document doc;
 	private Elements ele;// = doc.select("div.article");
 	private BufferedWriter writer;
 	
 	public crawling() throws Exception{
 		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName), "MS949"));
 		writer.write(field);
 		
 	}
 	public void run(){//54500
 		for(int i = 1 ; i< 10 ; i++){
 			format.resetData();
 			try{
 				setURL(i);
 				System.out.println(i);
 				setKeyword();
 				
 				if(format.keyword != null){
 				setArticleCategory2();
 				setArticleDate();
 				setArticleContent();
 					
 				writer.write(format.keyword+" , "+format.articleDate+" , "+i+" , "+format.articleCategory +" , "+format.articleContent +"\r\n");
 				
 				}
 			}catch(Exception e){
 
 			}
 		}
 		try {
 			writer.close();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 	}
 	public void setURL(int i) throws Exception{
 		url = "http://www.imaeil.com/sub_news/sub_news_view.php?news_id="+i+"&yy=2014";
 		doc = Jsoup.connect(url).get();
 	}
 	public void setKeyword(){
 		ele = doc.select("META");
 		for(Element e : ele)
 			if(e.attr("NAME").equals(keyword))
 				format.keyword = e.attr("CONTENT");
 		if(format.keyword.contains("올림픽"))
 			format.keyword ="올림픽";
 		else
 			format.keyword = null;
 				
 	}
 	public void setArticleCategory2(){
 		ele = doc.select("meta");
 		for(Element e : ele)
 			{
 				if(e.attr("content").equals("사회"))
 					format.articleCategory = e.attr("content");
 				else if(e.attr("content").equals("경제"))
 					format.articleCategory = e.attr("content");
 				else if(e.attr("content").equals("문화"))
 					format.articleCategory = e.attr("content");
 				//종류 계속 추가..
 			}
 				
 	}
 	
 	public void setArticleDate(){
 		ele = doc.select("META");
 		for(Element e : ele)
 			if(e.attr("NAME").equals(dateTag))
 				format.articleDate = e.attr("CONTENT");
 	}
 	public void setArticleContent(){
 		ele = doc.select("div#_article");
 		format.articleContent = ele.text();
 		format.articleContent = format.articleContent.replaceAll(",", "");
 	}
 }