package com.unrulyrecursion.talc;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;

/**
 * This class/program takes in lyrics and checks to see if throughout the course
 * of the song if (in successive new words) the entire alphabet is present in
 * order. This is a companion/helper/cheating program for the wildly successful
 * Alphabet Lyric car game.
 *
 * @author Taylor
 *
 */
public class alphabetLyricChecker {

    //Some of these are not used because of implementation changes, but I'm leaving until I have finished updating the program
    private static JFrame window;
    private JLabel result;
    private JPanel fbPanel, lyricsPanel;
    private static JPanel top;
    private JTextArea lyricsText;
    private JTextPane lyricsResults;
    private JButton checkButton, newButton;
    private String eol = System.getProperty("line.separator"); // Later when writing lyrics to results this will save time

    /**
     * @param args
     */
    public static void main(String[] args) { //Driver class for Standalone Application
        window = new JFrame("TALC - The Alphabet Lyric Checker");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        alphabetLyricChecker alc = new alphabetLyricChecker();

        window.getContentPane().add(alc.new topPanel());
        window.pack();
        window.setVisible(true);

    }

    private class topPanel extends JPanel {

        public topPanel() {
            setLayout(new BorderLayout());

            ButtonListener buttonListen = new ButtonListener();

            checkButton = new JButton("Check");
            checkButton.addActionListener(buttonListen);
            newButton = new JButton("New");
            newButton.addActionListener(buttonListen);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.add(Box.createVerticalGlue());
            buttonPanel.add(checkButton);
            buttonPanel.add(newButton);
            buttonPanel.add(Box.createVerticalGlue());
            add(buttonPanel, BorderLayout.EAST);

            lyricsText = new JTextArea(25, 25);
            lyricsText.setLineWrap(true);
            lyricsResults = new JTextPane();
            lyricsResults.setContentType("text/html");
            lyricsResults.setEditable(false);

            lyricsPanel = new JPanel();
            lyricsPanel.add(lyricsText);

            JScrollPane lyricWindow = new JScrollPane(lyricsPanel);
            add(lyricWindow);

            //optionPanel option = new optionPanel(); //Comment unimplemented code
            //add(option, BorderLayout.NORTH);

            fbPanel = new JPanel();
            result = new JLabel("");
            fbPanel.add(result);
            fbPanel.setVisible(false);
            add(fbPanel, BorderLayout.SOUTH);
        }
    }

    /**
     * Unimplemented as of yet private class optionPanel extends JPanel { public
     * optionPanel() { add(new JLabel("Options under construction")); } }
	*
     */
    /**
     * This method does the work of checking lyrics and forming results in this
     * program.
     *
     * @param lyricText Lyrics to be checked.
     */
    private void checkIt(String lyricText) {
        Scanner lyrics = new Scanner(lyricText);
        Scanner lyricsLine; //Instantiate outside of the loop
        char letter = 'a'; //Starting point for alphabet iteration
        lyricsResults.setText("");
        //String Builder used because we will constantly be changing this 
        //until we are ready to write it out
        StringBuilder resultsBuilder = new StringBuilder("<html><body>");
        String p = "<br />";
        
        //Initialize variables, but no initial value is needed yet
        String before, after, newWord, currentWord, lowers;
        int letterIndex, wordLength;

        //Two loop solution, one for lines and one for words
        while (lyrics.hasNextLine() && letter <= 'z') {//Continue while there is another 
            //line of the scanner, and the alphabet has not all been found
            lyricsLine = new Scanner(lyrics.nextLine());
            while (lyricsLine.hasNext()) {
                currentWord = lyricsLine.next();
                //Lowercase used to normalize word, separate variable to 
                //preserve capitalization for results
                lowers = currentWord.toLowerCase();

                if (lowers.contains(letter + "")) {
                    wordLength = lowers.length();
                    letterIndex = lowers.indexOf(letter);
                    //Build words with found letters
                    //Inject some html to make the letter stand out
                    newWord = "<font color=red>" + currentWord.charAt(letterIndex) + "</font>";
                    if (letterIndex != 0) {
                        before = currentWord.substring(0, letterIndex);
                        newWord = before + newWord;
                    }

                    if (letterIndex != wordLength - 1) {
                        after = currentWord.substring(letterIndex + 1);
                        newWord = newWord + after;
                    }

                    currentWord = "*" + newWord + "*";

                    //Since I store the letter as a char, I can 
                    //do math operations on it to get to the next
                    letter++;
                }
                resultsBuilder.append(currentWord + " ");
            }
            resultsBuilder.append(p);
        }
        resultsBuilder.append("</body></html>");
        lyricsResults.setText(resultsBuilder.toString());


        //Success Checking for Feedback
        if (letter > 'z') {
            result.setText("These lyrics work!");
        } else {
            result.setText("These lyrics don't work. Got to *" + letter + "*");
        }

        lyricsPanel.add(lyricsResults);
        fbPanel.setVisible(true);
    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String rawText = lyricsText.getText();

            if (e.getSource() == checkButton) {
                //Just return if no lyrics
                if (rawText.equals("")) {
                    return;
                }

                checkIt(rawText);
            }
            //Clear field
            if (e.getSource() == newButton) {
                lyricsPanel.remove(lyricsResults);
                lyricsText.setText("");
                fbPanel.setVisible(false);
            }
        }
    }
}
