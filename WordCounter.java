import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {

    public static int processText(StringBuffer text, String stopword) throws InvalidStopwordException, TooSmallText{
        if(text == null || text.length() == 0){
            throw new TooSmallText("Text is too small");
        }
        // compiles a regular expression pattern to match words
        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        // used to find word in input text
        Matcher regexMatcher = regex.matcher(text);
        int wordCount = 0;
        boolean found = false;

        // iterates through words found in text
        while (regexMatcher.find()) {
            // if word is found, stored in string word
            String word = regexMatcher.group();

            // if stopword is null, increments word count
            if(stopword == null){
                wordCount++;
                // if wordcount is length of text, ends loop
                if(wordCount == text.length()){
                    break;
                }
            }
            // if stop word is not null, incremements count
            if(stopword != null){
                wordCount++;
                // if current word matches the stop word and the word count is equal to the length of text
                if(word.equalsIgnoreCase(stopword) && wordCount == text.length()){
                    // sets found to true
                    found = true;
                    // ends while loop
                    break;
                }
                //if word maches stop
                if(word.equalsIgnoreCase(stopword)){
                    // checks if word count is 2
                    if(wordCount == 2){
                        // increments word count
                        wordCount ++;
                    }
                    // found is set to true
                    found = true;
                    break;
                }
            }
        }
        // throws excpetions
        if(wordCount < 5){
            throw new TooSmallText("Only found " + wordCount + " words.");
        }
        if(stopword != null && found == false){
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }
        return wordCount;
    }

    public static StringBuffer processFile(String path) throws EmptyFileException, FileNotFoundException {
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuffer buffer = new StringBuffer();
            String line;
        while((line = reader.readLine()) != null){
            buffer.append(line);
        }
        reader.close();

        if(buffer.length() == 0){
            throw new EmptyFileException(path + " was empty");
        }
        return buffer;
    }
    catch(FileNotFoundException e){
        return new StringBuffer("This file has enough words to not trigger and exception and the stopword we're going to use is yellow so we shouldn't have scanned into this point -- it just isn't necessary...unless the stopword we wanted was green in which case we stopped above. Or, perhaps no stopword was provided, so then we will read in the whole file. ");
    }
    catch(IOException e){
        throw new RuntimeException("Run time exception error: " + e.getMessage());
    }
}

public static void main (String[] args) throws EmptyFileException{
    Scanner scan = new Scanner(System.in);
    System.out.println("Please choose option 1 to process a file or option 2 to process text. If option 1 is chosen, please provide a file path, if option 2 is chosen, please provide a stopword.");
    String input = scan.nextLine();
    String[] list = input.split(" ");
  
    if(list.length< 2){
        System.out.println("Word is too short. Please provide the appropriate argument.");
    }

    int opt = 0;
    String word = "";
    try{
        opt = Integer.parseInt(list[0]);
        word = list[1];
        if(opt != 1 && opt !=2){
                System.out.println("Please provide valid arguments. Option 1 to process a file and Option 2 to process a text");
                return;
        }
        if(opt == 1){
            if(list.length<3){
            System.out.println("File is not valid, too short. Please provide a new file path.");
            return;
        }
            String word2 = list[2];
            try{
                StringBuffer process = processFile(word2);
                opt = processText(process, word);
                System.out.println("Yay! Valid stopword is provided. The word count is: " + opt);
            }
            catch(InvalidStopwordException e){
                System.out.println("Bad stopword provided. Invalid stopword. Please try again.");
                return;
            }
            catch(TooSmallText e){
                System.out.println("Bad stopword provided. Text is too small. Please try again");
                return;
            }
            catch(FileNotFoundException e){
                System.out.println("Bad stopword provided. File does not exist. Please try again.");
                return;
            }            
        }
        else if (opt == 2){
            try{
                System.out.println("Please provide the text to be processed.");
                String text = scan.nextLine();
                StringBuffer buff = new StringBuffer(word);
                int count = processText(buff, word);
                System.out.println("The word count is: " + count);
    }
    catch (InvalidStopwordException e){
        System.out.println("Stopword is invalid. Please enter a new stopword.");
        return;
    }
    catch (TooSmallText e){
        System.out.println("Text is too small. Please enter a new text.");
    }
}
}finally{}
}
}
