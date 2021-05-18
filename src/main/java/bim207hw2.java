
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

public class bim207hw2 {
	public static void main(String[] args) throws IOException {

		/** Body from given URL */
		String URL = args[0];

		/** Body from given URL */
		org.jsoup.nodes.Document doc = Jsoup.connect(URL).get();
		Elements body = doc.select("div.ulist");
		String text = body.select("p").text();

		InputStream is = new FileInputStream("en-sent.bin");
		SentenceModel model = new SentenceModel(is);

		// feed the model to SentenceDetectorME class
		SentenceDetectorME sdetector = new SentenceDetectorME(model);

		// detect sentences in the paragraph
		String sentences[] = sdetector.sentDetect(text);

		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;
		TokenNameFinderModel model2 = new TokenNameFinderModel((new FileInputStream(new File("en-ner-person.bin"))));
		NameFinderME finder = new NameFinderME(model2);

		for (String sentence : sentences) {

			// Split the sentence into tokens
			String[] tokens = tokenizer.tokenize(sentence);

			// Find the names in the tokens and return Span objects
			Span[] nameSpans = finder.find(tokens);

			// Print the names extracted from the tokens using the Span data
			System.out.println(Arrays.toString(Span.spansToStrings(nameSpans, tokens)));
		}
	}
}
