import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        //создаем файл
        String fileName = "src/input.txt";
        String outputFileName = "src/output.txt";
        File file = new File(fileName);
        File outputFile = new File(outputFileName);

        //читаем
        try (FileInputStream in = new FileInputStream(file)) {
            String input = new String(in.readAllBytes());

            //разбиваем на части
            String[] str = input.split("\n");

            List<String> firstList = new ArrayList<>();
            List<String> secondList = new ArrayList<>();

            //считаем количество и позиции нужных строк
            int rows1 = Integer.parseInt(str[0].trim());
            int rows2 = Integer.parseInt(str[rows1 + 1].trim());

            //заполняем две коллекции переданными списками
            for (int i = 1; i <= rows1; i++) {
                firstList.add(str[i].trim());
            }
            for (int i = rows1 + 2; i < str.length; i++) {
                secondList.add(str[i].trim());
            }

            //для записи результата заводим StringBuilder
            StringBuffer buffer = new StringBuffer();

            //сравниваем списки
            for (int i = 0; i < firstList.size(); i++) {
                //введем индекс сравнения и счетчик позиции для совпадения
                int posOfMaxGrade = -1;
                int comparingGrade;
                for (int j = 0; j < secondList.size(); j++) {
                    //напишем метод для пословного сравнения строк
                    comparingGrade = Main.compare(firstList.get(i).trim(), secondList.get(j).trim());
                    if (comparingGrade > -1) {
                        posOfMaxGrade = j;
                    }
                }
                buffer.append(firstList.get(i).trim());
                buffer.append(':');
                if (posOfMaxGrade == -1) {
                    buffer.append('?');
                } else {
                    buffer.append(secondList.get(posOfMaxGrade));
                }
                buffer.append("\n");
            }

            //создадим файл
            outputFile.createNewFile();

            //пишем в файл результат и закрываем поток
            FileOutputStream ous = new FileOutputStream(outputFile, false);
            ous.write(buffer.toString().getBytes(StandardCharsets.UTF_8));
            ous.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int compare(String first, String second) {

        //У нас требуется проверять синонимы, поэтому введем список синонимов,
        // в идеале конечно String -> List<String> но для примера избыточно
        Map<String, String> synonyms = new HashMap<>();
        synonyms.put("бетон", "цемент");
        synonyms.put("цемент", "бетон");


        int comparingGrade = -1;
        String[] f = first.trim().split(" ");
        String[] s = second.trim().split(" ");
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < s.length; j++) {
                //будем считать что слова схожи от аналогичной последовательности в 3 знака
                if (compareByWords(f[i].trim(), s[j].trim()) > 2) {
                    comparingGrade++;
                } else {
                    if (synonyms.containsKey(f[i].trim().toLowerCase(Locale.ROOT))) {
                        if (compareByWords(s[j],synonyms.get(f[i].trim().toLowerCase(Locale.ROOT)))>2) {
                            comparingGrade++;
                        }
                    }
                }
            }
        }
        return comparingGrade;
    }

    //так как стандартные методы сравнивают только количество схожих символов без учета последовательности допишем свой метод
    public static int compareByWords(String f, String s) {
        int counter = 0;
        int size = 0;
        for (int i = 0; i < f.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                if (f.toLowerCase(Locale.ROOT).charAt(i) != s.toLowerCase(Locale.ROOT).charAt(j)) {
                    continue;
                } else {
                    for (int k = 0; k < f.length() - i & k < s.length() - j; k++) {
                        if (f.toLowerCase(Locale.ROOT).charAt(i + k) == s.toLowerCase(Locale.ROOT).charAt(j + k)) {
                            counter++;
                            size = counter;
                        } else {
                            counter = 0;
                        }
                    }
                }
            }
        }
        return size;
    }
}
