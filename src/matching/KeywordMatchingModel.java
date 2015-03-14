/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matching;
/**
*
* @author quy
* Algorithm: RabinKarpPatternSearch for phrases instead of string
*/
//import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class KeywordMatchingModel {
	private static final int AT_LEAST_ONE = 1;
	public static final KeywordMatchingModel Instance = new KeywordMatchingModel();

	private int search(String document, HashPattern hashPatterns, int searchMode) {
		if (document.isEmpty()) {
			return 0;
		}
		int match = 0;
		int windowSize = hashPatterns.windowSize;
		Map<Integer, List<Pattern>> hashValues = hashPatterns.hashValues;
		List<Pattern> setPatterns;
		int hash;
		String word;
		try {
			// search in document, sentence by sentence
			String sentences[] = document.trim().toLowerCase()
					.split("[\\p{P}\t\n\r]+");
			ArrayList<Integer> hashCached = new ArrayList<>();
			ArrayList<String> wordCached = new ArrayList<>();
			for (String sentence : sentences) {
				// analyze words in this sentence">
				String[] texts = sentence.trim().split("\\s+");
				hashCached.clear();
				wordCached.clear();
				int nWordsInWindow;
				int j;
				for (int i = 0; i < texts.length; i++) {
					nWordsInWindow = Math.min(windowSize, texts.length - i);
					word = texts[i];
					// hash = hash of the first word in this window>
					if (hashCached.size() > 0) {
						hash = hashCached.get(0);
					} else {
						hash = word.hashCode();
						hashCached.add(hash);
					}
					// can get setPatterns from this hash? ">
					setPatterns = hashValues.get(hash);// get patterns to
														// compare
					if (setPatterns == null) {
						hashCached.remove(0);
						continue;
					}
					// load hashCached for all words in window
					for (j = i + hashCached.size(); j < i + nWordsInWindow; j++) {
						hash = texts[j].hashCode();
						hashCached.add(hash);
					}
					// get windowString
					StringBuilder windowStringBuidler = new StringBuilder(word);
					for (j = i + 1; j < i + nWordsInWindow; j++) {
						windowStringBuidler.append(" ").append(texts[j]);
					}
					String windowString = windowStringBuidler.toString();
					for (Pattern p : setPatterns) {
						if (p.hashes.size() == 1) {
							// compare to this single word pattern
							if (p.keyword.equals(word)) {
								if (searchMode == AT_LEAST_ONE) {
									return 1;
								}
								match++;
							}
						} else {
							//are all hashes matched?
							boolean bMatched = true;
							for (int k = 1; k < Math.min(nWordsInWindow,
									p.hashes.size()); k++) {
								if (!Objects.equals(hashCached.get(k),
										p.hashes.get(k))) {
									bMatched = false;
									break;
								}
							}
							if (bMatched) {
								//compare string
								if (windowString.contains(p.keyword)) { 
									// windowString may have more words than  p  
									if (searchMode == AT_LEAST_ONE) {
										return 1;
									}
									match++;
								}
							}
						}
					}
					hashCached.remove(0);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return match;
	}

	private HashPattern hashPatterns(String wordList) {
		if (wordList == null || wordList.isEmpty()) {
			return null;
		}
		HashPattern result = new HashPattern();
		try {
			Map<Integer, List<Pattern>> hashValues = new HashMap<>();
			String[] keywords = wordList.trim().toLowerCase().split(",");
			int windowSize = 1;
			List<Pattern> setPatterns;
			for (String keyword : keywords) {
				Pattern pattern = new Pattern();
				pattern.keyword = keyword.trim();
				pattern.hashes = new ArrayList<>();
				if (pattern.keyword.contains(" ")) {
					//compound word, hash each word
					String[] parts = pattern.keyword.split(" ");
					if (parts.length > windowSize) { // determine window size for later
						windowSize = parts.length;
					}
					for (String part : parts) {
						pattern.hashes.add(part.trim().hashCode());
					}
				} else { // single words
					pattern.hashes.add(pattern.keyword.hashCode());
				}
				//group patterns which have the same 1st word
				int hash = pattern.hashes.get(0);
				setPatterns = hashValues.get(hash);
				if (setPatterns == null) {
					setPatterns = new ArrayList<>();
				}
				setPatterns.add(pattern);
				hashValues.put(hash, setPatterns);
			}
			result.hashValues = hashValues;
			result.windowSize = windowSize;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}

class Pattern {
	String keyword;
	ArrayList<Integer> hashes;

	public Pattern() {
	}
}

class HashPattern {
	int windowSize;
	Map<Integer, List<Pattern>> hashValues;

	public HashPattern() {
	}
}