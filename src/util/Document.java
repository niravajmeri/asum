package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;


public class Document {

	private int docNo;
	private int author;
	private int senti;
	private int[] topicDist;
	
	protected Vector<Word> words;
	
	public Document(){
		this.words = new Vector<Word>();
	}
	
	public Document(int docLength){
		this.words = new Vector<Word>(docLength);
	}
	
	public void newTopicVector(int numOfTopic){
		topicDist = new int[numOfTopic];
	}

	public int getDocNo() {
		return docNo;
	}

	public void setDocNo(int docNo) {
		this.docNo = docNo;
	}
	
	public void addWord(Word word){
		words.add(word);
	}
	
	public void addWord(int wordNo) {
		addWord(new Word(wordNo));
	}
	
	public int getLength() {
		return words.size();
	}

	public int getNumWords() {
		return words.size();
	}
	
	public Vector<Word> getWords() {
		return words;
	}
	
	public void increaseTopic(int topic){
		topicDist[topic] += 1;
	}
	
	public void decreaseTopic(int topic){
		topicDist[topic] -= 1;
	}
	
	public int getTopicCount(int topic){
		return topicDist[topic];
	}
	
	public void setWordsList(Vector<Word> wordsList){
		this.words = wordsList;
	}

	public void setAuthor(int author) {
		this.author = author;
	}

	public int getAuthor() {
		return author;
	}

	public void setSenti(int senti) {
		this.senti = senti;
	}

	public int getSenti() {
		return senti;
	}
	
	public TreeMap<Integer,Integer> getWordCount() {
		TreeMap<Integer,Integer> wordCntTable = new TreeMap<Integer,Integer>();
		for (Word word : this.words) {
			Integer cnt = wordCntTable.get(word.wordNo);
			if (cnt == null) wordCntTable.put(word.wordNo, 1);
			else wordCntTable.put(word.wordNo, cnt+1);
		}
		return wordCntTable;
	}
	
	public static Vector<Document> instantiate (String path, List<Integer> authors) throws Exception {
		Vector<Document> documents = new Vector<Document>();
		BufferedReader wordDocFile = new BufferedReader(new FileReader(new File(path)));
		
		int docCount=0;
		String line;
		while(true){
			line = wordDocFile.readLine();
			if(line == null) break;
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken();
			int docLength = Integer.valueOf( st.nextToken() );
			
			Document currentDoc = new Document(docLength);
			currentDoc.setDocNo(docCount++);
			
			line = wordDocFile.readLine();
			st = new StringTokenizer(line);
			while(st.hasMoreElements()){
				int wordNo = Integer.valueOf(st.nextToken());
				int times = Integer.valueOf(st.nextToken());
				for(int w=0; w<times; w++){
					currentDoc.addWord(new SentiWord(wordNo));
				}
			}			
			
			if (authors != null) {
				int author = authors.get(currentDoc.getDocNo());
				if (author == -1) {
					System.out.println(currentDoc.getDocNo()+","+authors.get(currentDoc.getDocNo()) + ",");
					throw new Exception();
				}
				currentDoc.setAuthor(author);
			}
			
			documents.add(currentDoc);
		}
		wordDocFile.close();
		
		return documents;
	}
	
}

