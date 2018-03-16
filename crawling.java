//���� �Ź�
//��������(��¥��������)�����Ͽ� �ش� Ű���尡 �����ϴ� ���� ������ ��翡 ���Ͽ� 
//Ű����, ��¥, url(or ��ȣ), �з�(��ȸ,���� ��), ���� ���� ������ ũ�Ѹ� �Ͽ� csv ���Ϸ� ����
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
 	public String field = "Ű����, ��¥, URL, �з�, ���� \r\n";
 	public String csvFileName = "c:/2018.csv";
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
 		for(int i = 1 ; i< 30 ; i++){
 			format.resetData();
 			try{
 				setURL(i);
 				System.out.println(i);
 				setKeyword();
 				
 				if(format.keyword != null){
 				setArticleCategory2();
 				setArticleDate();
 				setArticleContent();
 					
 				writer.write(format.keyword+" , "+format.articleDate+" , "+url+" , "+format.articleCategory +" , "+format.articleContent +"\r\n");
 				
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
 		url = "http://www.imaeil.com/sub_news/sub_news_view.php?news_id="+i+"&yy=2018";
 		doc = Jsoup.connect(url).get();
 	}
 	public void setKeyword(){
 		ele = doc.select("META");
 		for(Element e : ele)
 			if(e.attr("NAME").equals(keyword))
 				format.keyword = e.attr("CONTENT");
 		if(format.keyword.contains("��â"))
 			format.keyword ="��â";
 		else
 			format.keyword = null;
 				
 	}
 	public void setArticleCategory2(){
 		ele = doc.select("meta");
 		for(Element e : ele)
 			{
 				if(e.attr("content").equals("������"))
 					format.articleCategory = "������";
 				else if(e.attr("content").equals("����"))
 					format.articleCategory = "����";
 				else if(e.attr("content").equals("��ȭ"))
 					format.articleCategory = "��ȭ";
 				else if(e.attr("content").equals("��ġ"))
 					format.articleCategory = "��ġ";
 				//���� ��� �߰�..
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