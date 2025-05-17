package test.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;


public class ZipArchiveContentTests {
    private ClassLoader cl = ZipArchiveContentTests.class.getClassLoader();

    @Test
    @DisplayName("Вывести названия файлов из архива")
    void zipFileParsingTest() throws Exception {
        InputStream is = cl.getResourceAsStream("arhiv.zip");
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

    @Test
    @DisplayName("Открыть файл pdf из архива")
    public void pdfFileParsingTest() throws Exception {

        try (InputStream inputStream = cl.getResourceAsStream("arhiv.zip")) {
            assert inputStream != null;
            try (ZipInputStream zis = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    if (zipEntry.getName().endsWith(".pdf")) {
                        PDF pdf = new PDF(zis);
                        Assertions.assertEquals(null, pdf.creator);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Открыть файл xls из архива")
    public void xlsFileParsingTest() throws Exception {

        try (InputStream inputStream = cl.getResourceAsStream("arhiv.zip")) {
            assert inputStream != null;
            try (ZipInputStream zis = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    if (zipEntry.getName().endsWith(".xls")) {
                        XLS xls = new XLS(zis);
                        String value = xls.excel.getSheetAt(0).getRow(2).getCell(2).getStringCellValue();
                        Assertions.assertTrue(value.contains("Боже, помоги мне это доделать!"));
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Открыть файл csv из архива")
    public void csvFileParsingTest() throws Exception {

        try (InputStream inputStream = cl.getResourceAsStream("arhiv.zip")) {
            assert inputStream != null;
            try (ZipInputStream zis = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zis.getNextEntry()) != null) {
                    if (zipEntry.getName().endsWith(".csv")) {
                        CSVReader csv = new CSVReader(new InputStreamReader(zis));
                        List<String[]> data = csv.readAll();
                        Assertions.assertEquals(2, data.size());
                        Assertions.assertArrayEquals(new String[]{"Вася Иванов", "Москва"}, data.get(0));
                        Assertions.assertArrayEquals(new String[]{"Петя Сидоров", "Ростов"}, data.get(1));
                    }
                }
            }
        }
    }
}


