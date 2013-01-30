package com.unrulyrecursion.talc;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;

/*
 * Expansion possibilities:
 * - Another algorithm that doesn't care if there is more than one letter in a word
 */

/**
 * This class/program takes in lyrics and checks to see if throughout the course of the 
 * song if (in successive new words) the entire alphabet is present in order. This is a 
 * companion/helper/cheating program for the wildly successful Alphabet Lyric car game.
 * @author Taylor
 *
 */
public class alphabetLyricChecker {

	private static JFrame window;
	private JLabel result;
	private JPanel fbPanel, lyricsPanel;
	private JTextArea lyricsText;
	private JTextPane lyricsResults;
	private JButton checkButton, newButton;
	private String eol = System.getProperty("line.separator");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//                JFrame.setDefaultLookAndFeelDecorated(true);
		window = new JFrame("TALC - The Alphabet Lyric Checker");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                window.setIconImage(new ImageIcon("../img/abc.png").getImage());
		alphabetLyricChecker alc = new alphabetLyricChecker();
		
		window.getContentPane().add(alc.new topPanel());
		window.pack();
		window.setVisible(true);
		
	}
        
	//TODO "New" button that resets the text area
	/**
         * Panel that takes up entire JFrame, and sets layout for content.
         */
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
			
			optionPanel option = new optionPanel();
			add(option, BorderLayout.NORTH);
			
			fbPanel = new feedbackPanel();
			add(fbPanel, BorderLayout.SOUTH);
		}
	}
	
        /**
         * Panel where options go. Located at top of the frame.
         */
	private class optionPanel extends JPanel {
		public optionPanel() {
			add(new JLabel("Options under construction"));
		}
	}
	
        /**
         * Panel that reports on success of the algorithm. Located at bottom
         * of the frame, and only shown after the check button is pressed.
         */
	private class feedbackPanel extends JPanel {
		public feedbackPanel() {
			result = new JLabel("");
			add(result);
			setVisible(false);
		}
	}
	
        /**
         * Method that does all the work for the program. Takes in the lyrics, 
         * one word at a time and searches to see if it has the current letter. 
         * If it does, it increments the letter. Also retains the letter to show 
         * which the first letter was that couldn't be found. It is assumed that 
         * the lyrics are already semi-sanitized (human readable). Algorithm 
         * should preserve the capitalization of the words.
         * 
         * @param lyricText Lyrics to be tested.
         */
	private void checkIt(String lyricText) {
		Scanner lyrics = new Scanner(lyricText);
		char letter = 'a';
		lyricsResults.setText("");
		StringBuilder resultsBuffer = new StringBuilder("<html><body>");
		String p = "<br />";
		String before, after, newWord, currentWord, lowers;
		int letterIndex, wordLength;
		
		while (lyrics.hasNextLine() && letter <= 'z') {
			Scanner lyricsLine = new Scanner(lyrics.nextLine());
			while (lyricsLine.hasNext()) {
				currentWord = lyricsLine.next();
				lowers = currentWord.toLowerCase();

				if (lowers.contains(letter + "")) {
					wordLength = lowers.length();
					letterIndex = lowers.indexOf(letter);
					newWord = "<font color=red>" + currentWord.charAt(letterIndex) + "</font>";
					if (letterIndex != 0) {
						before = currentWord.substring(0, letterIndex);
						newWord = before + newWord;
					}
					
					if (letterIndex != wordLength-1) {
						after = currentWord.substring(letterIndex + 1);
						newWord = newWord + after;
					}
					
					currentWord = "*" + newWord + "*";
					letter++;
				}
				resultsBuffer.append(currentWord);
                                resultsBuffer.append(" ");
			}
			resultsBuffer.append(p);
		}
		resultsBuffer.append("</body></html>");
		lyricsResults.setText(resultsBuffer.toString());
		
		if (letter > 'z') { //Has to actually go through the entire alphabet
			result.setText("These lyrics work!");
		}
		else {
			result.setText("These lyrics don't work. Didn't find: *" + letter + "*");
		}
		
		lyricsPanel.add(lyricsResults);
		fbPanel.setVisible(true);
	}
	
        /**
         * Method that performs actions when user input is detected. Specifically 
         * it activates the program when buttons are used.
         */
	private class ButtonListener implements ActionListener {
		
            @Override
                public void actionPerformed(ActionEvent e) {
			String rawText = lyricsText.getText();
			if (e.getSource() == checkButton) {
				if (rawText.equals(""))
					return;
				
				checkIt(rawText);
			}
			
			if (e.getSource() == newButton) {
				lyricsPanel.remove(lyricsResults);
				lyricsText.setText("");
				fbPanel.setVisible(false);
			}
		}
	}
}
