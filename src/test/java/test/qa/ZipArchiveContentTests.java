package test.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;


public class ZipArchiveContentTests {
    private ClassLoader cl = ZipArchiveContentTests.class.getClassLoader();

    @ValueSource(strings = {
            "arhiv.zip", "empty.zip", "test.zip"
    })
    @ParameterizedTest(name = "Вывести названия файлов из архива {0}")
    @DisplayName("Вывести названия файлов из архива")
    void zipFileParsingTest(String arhivName) throws Exception {
        InputStream is = cl.getResourceAsStream(arhivName);
        assertNotNull(is, "Архив не найден");

        try (ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry = zis.getNextEntry();
            assertNotNull(entry, "Архив пустой");
            System.out.println(entry.getName());

            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }
        }
    }

    @ValueSource(strings = {
            "arhiv.zip", "empty.zip", "test.zip"
    })
    @ParameterizedTest(name = "Открыть файл pdf из архива {0}")
    @DisplayName("Открыть файл pdf из архива")
    public void pdfFileParsingTest(String arhivName) throws Exception {

        InputStream is = cl.getResourceAsStream(arhivName);
        assertNotNull(is, "Архив не найден");

        boolean foundPdf = false;
        try (ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".pdf")) {
                    foundPdf = true;
                    PDF pdf = new PDF(zis);
                    assertEquals(null, pdf.creator);
                }
            }
        }
        assertTrue(foundPdf, "PDF-файл не найден в архиве");
    }

    @ValueSource(strings = {
            "arhiv.zip", "empty.zip", "test.zip"
    })
    @ParameterizedTest(name = "Открыть файл xls из архива {0}")
    @DisplayName("Открыть файл xls из архива")
    public void xlsFileParsingTest(String arhivName) throws Exception {
        InputStream is = cl.getResourceAsStream(arhivName);
        assertNotNull(is, "Архив не найден");

        boolean foundXls = false;
        try (ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".xls")) {
                    foundXls = true;
                    XLS xls = new XLS(zis);
                    String value = xls.excel.getSheetAt(0).getRow(2).getCell(2).getStringCellValue();
                    assertTrue(value.contains("Боже, помоги мне это доделать!"));
                }
            }
        }
        assertTrue(foundXls, "XLS-файл не найден в архиве");
    }

    @ValueSource(strings = {
            "arhiv.zip", "empty.zip", "test.zip"
    })
    @ParameterizedTest(name = "Открыть файл csv из архива {0}")
    @DisplayName("Открыть файл csv из архива")
    public void csvFileParsingTest(String arhivName) throws Exception {
        InputStream is = cl.getResourceAsStream(arhivName);
        assertNotNull(is, "Архив не найден");

        boolean foundCsv = false;
        try (ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".csv")) {
                    foundCsv = true;
                    CSVReader csv = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csv.readAll();
                    assertEquals(2, data.size(), "Неверное количество строк");
                    assertArrayEquals(new String[]{"Вася Иванов", "Москва"}, data.get(0));
                    assertArrayEquals(new String[]{"Петя Сидоров", "Ростов"}, data.get(1));
                }
            }
        }
        assertTrue(foundCsv, "CSV-файл не найден в архиве");
    }
}



