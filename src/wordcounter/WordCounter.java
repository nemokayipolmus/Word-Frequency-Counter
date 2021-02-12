
package wordcounter;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JFileChooser;

public class WordCounter {

    public static void main(String[] args) throws IOException {
        
        JFileChooser fc = new JFileChooser();
        File file = null;
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
        }
        
        FileReader is = new FileReader(file);
        char[] scriptBytes = new char[(int) file.length()];
        is.read(scriptBytes);
        is.close();
        String script = new String(scriptBytes);
        String[] words = script.split("[ \\r?\\n\\-]");
        List<String> wordList = new ArrayList<>(Arrays.asList(words));
        wordList = cleaner(wordList);
        
        HashMap<String, Integer> counter = new HashMap<>();
        
        for (String word : wordList) {
            if (counter.containsKey(word))
                counter.put(word, counter.get(word) + 1);
            else
                counter.put(word, 1);
        }
        
        counter = entriesSortedByValues(counter);
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(file.getAbsolutePath().replace(file.getName(), "") + "result_" + file.getName())));
        
        for (Entry<String, Integer> ent : counter.entrySet()) {
            writer.write(ent.getKey() + " " + ent.getValue());
            writer.newLine();
        }
        writer.close();
    }

    
    private static List<String> cleaner(List<String> words) {
        
        for (int i = 0; i < words.size(); i++) {
            words.set(i, words.get(i).replaceAll("[1234567—890!@#$%^&*()_+|\\-=~`{}\\[\\]:;\"<>,.?/]","").toLowerCase());
        }
        
        words.removeAll(Arrays.asList(""));
        return words;
    }
    
    private static <K,V extends Comparable<? super V>> 
        
        HashMap<K, V> entriesSortedByValues(Map<K,V> map) {

        List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

        Collections.sort(sortedEntries, 
            new Comparator<Entry<K,V>>() {
                @Override
                public int compare(Entry<K,V> e1, Entry<K,V> e2) {
                    return e2.getValue().compareTo(e1.getValue());
                }
            }
        );
        
        HashMap<K,V> result = new LinkedHashMap<K,V>();
        
        for (Entry<K,V> ent : sortedEntries)
            result.put(ent.getKey(), ent.getValue());

        return result;
    }

}

