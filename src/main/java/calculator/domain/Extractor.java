package calculator.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extractor {

    private static final String customSeparatorPattern = "//(.*?)\n";
    private static final String[] defaultSeparators = {":", ","};

    private List<Number> numbers;
    private List<Separator> customSeparators;

    public Extractor(String input) {
        this.customSeparators = extractCustomSeparators(input);
        this.numbers = extractNumbers(input);
    }

    public List<Separator> getCustomSeparators() {
        return customSeparators;
    }

    public List<Number> getNumbers() {
        return numbers;
    }

    public List<Separator> extractCustomSeparators(String input) {
        Pattern pattern = Pattern.compile(customSeparatorPattern);
        Matcher matcher = pattern.matcher(input);
        List<Separator> separators = new ArrayList<>();

        while (matcher.find()) {
            String customSection = matcher.group(1);
            separators.add(new Separator(customSection));
        }

        return separators;
    }

    public List<Number> extractNumbers(String input) {
        String modifiedInput = input.replaceAll(customSeparatorPattern, "");
        StringBuilder allSeparatorsPattern = new StringBuilder();

        for (String sep : defaultSeparators) {  // 기본 구분자 추가
            allSeparatorsPattern.append(Pattern.quote(sep)).append("|");
        }
        for (Separator separator : customSeparators) {  // 커스텀 구분자 추가
            allSeparatorsPattern.append(Pattern.quote(separator.getSeparator())).append("|");
        }
        // 마지막 | 제거
        if (allSeparatorsPattern.length() > 0 && allSeparatorsPattern.charAt(allSeparatorsPattern.length() - 1) == '|') {
            allSeparatorsPattern.setLength(allSeparatorsPattern.length() - 1);
        }

        String[] tokens = modifiedInput.split(allSeparatorsPattern.toString());
        List<Number> numberList = new ArrayList<>();

        for (String token : tokens) {
            numberList.add(new Number(Integer.parseInt(token)));
        }

        return numberList;
    }
}