package com.example.ontherun.clients;

import android.util.Log;

import com.example.ontherun.page.ParsedResult;
import com.example.ontherun.page.Line;
import com.example.ontherun.page.Word;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RedactorClient {

    static String capitalLetters = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪИЬЭЮЯ";
    static String numbers = "1234567890";
    public static List<String> getPageText(ParsedResult page) {
        List<Line> lines = page.getTextOverlay().getLines();
        List<String> text = new ArrayList<>();
        StringBuilder sentence = new StringBuilder();
        lines = sortLines(neededLines(lines));
        for (int i = 0; i < lines.size(); ++i) {
            if (i > 0 && lines.get(i).getMaxHeight() < lines.get(i - 1).getMaxHeight()
                    && capitalLetters.contains(lines.get(i)
                    .getWords().get(0).getWordText().substring(0, 1)) && sentence.length() > 0) {
                text.add(sentence.toString());
                sentence = new StringBuilder();
            }
            List<Word> words = lines.get(i).getWords();
            for (Word word : words) {
                String wordText = word.getWordText();
                wordText = redactWord(wordText);
                sentence.append(wordText);
                if (wordText.endsWith(".") || wordText.endsWith("?") || wordText.endsWith("!")) {
                    text.add(sentence.toString());
                    sentence = new StringBuilder();
                }
            }

        }
        text.add(sentence.toString());
        return text;
    }

    private static String redactWord(String word) {
        if (word.equals("в.")) {
            return "век ";
        } else if (word.equals("вв.")) {
            return "веках ";
        }else if (word.equals("г.")) {
            return "году ";
        } else if (word.equals("др.")) {
            return "другие ";
        } else if (word.equals("н.")) {
            return "нашей ";
        } else if (word.equals("э.")) {
            return "эры ";
        } else if (word.length() == 2 && word.endsWith(".") && capitalLetters.contains(word.substring(0, 1))) {
            return "";
        } else if (word.endsWith("-")) {
            return word.substring(0, word.length() - 1);
        } else if (word.endsWith(".") || word.endsWith("?") || word.endsWith("!")) {
            return word;
        }
        return word + " ";
    }

    private static List<Line> sortLines(List<Line> lines) {
        lines.sort(new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                Word firstWord1 = o1.getWords().get(0);
                Word firstWord2 = o2.getWords().get(0);
                if (firstWord1.getTop() - firstWord2.getTop() > 2) {
                    return 1;
                } else if (firstWord1.getTop() - firstWord2.getTop() < 0) {
                    return -1;
                } else {
                    if (firstWord1.getLeft() - firstWord2.getLeft() > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        });
        return lines;
    }

    private static List<Line> neededLines(List<Line> lines) {
        // TODO: improve redactor
        List<Line> good = new ArrayList<>();
        good.add(lines.get(0));
        for (int i = 1; i < lines.size(); ++i) {
            Line line = lines.get(i);
            if (line.getMinTop() - good.get(good.size() - 1).getMinTop() > 200) {
                if (line.getMaxHeight() > good.get(good.size() - 1).getMaxHeight()
                        && line.getWords().size() > 3) {
                    good.clear();
                    good.add(line);
                }
            } else {
                good.add(line);
            }
        }
        return good;
    }

    public static int getFirstPage(List<List<String>> list) {
        for (int i = 0; i < list.size(); ++i) {
            List<String> innerList = list.get(i);
            for (String s : innerList) {
                if (s.contains(" S1")) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int getParagraphsAmount(List<List<String>> list) {
        int amount = 0;
        for (List<String> page : list) {
            for (String s : page) {
                if (s.contains("S" + (amount + 1))) {
                    Log.d("pPrive", "first: " + amount);
                    amount++;
                }
            }
        }
        return amount;
    }
}
