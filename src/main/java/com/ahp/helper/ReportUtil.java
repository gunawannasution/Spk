package com.ahp.helper;

import com.ahp.content.dao.PdfExportable;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.awt.Desktop;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportUtil {

    public static <T extends PdfExportable> void generatePdfReport(
            List<T> dataList,
            String[] headers,
            String reportTitle,
            String baseFileName,
            String kota,
            String namaDirektur
    ) throws Exception {

        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        // Nama file unik setiap generate
        String tanggal = new SimpleDateFormat("EEEE_dd_MMMM_yyyy_HHmmss", new Locale("id", "ID"))
                .format(new Date());
        String safeFileName = baseFileName.replaceAll("\\s+", "_").toLowerCase();
        String outputFilePath = "reports/" + safeFileName + "_" + tanggal + ".pdf";

        // agar stream selalu tertutup ketika file di print
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, fos);
            document.open();

            addKopSurat(document);

            Paragraph title = new Paragraph(reportTitle,
                    new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(20);
            title.setSpacingAfter(20);
            document.add(title);

            // Tabel data
            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            float[] columnWidths = new float[headers.length];
            for (int i = 0; i < headers.length; i++) {
                columnWidths[i] = 1f;
            }
            table.setWidths(columnWidths);

            addTableHeader(table, headers);

            int no = 1;
            for (T item : dataList) {
                List<String> row = item.toPdfRow();
                for (int i = 0; i < headers.length; i++) {
                    String value;
                    if (i == 0 && headers[0].equalsIgnoreCase("No")) {
                        value = String.valueOf(no++);
                    } else {
                        value = row.get(i - (headers[0].equalsIgnoreCase("No") ? 1 : 0));
                    }
                    PdfPCell cell = new PdfPCell(new Phrase(value));
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
            }

            document.add(table);

            // Footer tanda tangan
            addFooter(document, kota, namaDirektur);

            document.close();
        }

        // Buka salinan file agar file asli tidak terkunci
        openPdfFromCopy(outputFilePath);
    }

    private static void addTableHeader(PdfPTable table, String[] headers) {
        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor bgColor = new BaseColor(63, 81, 181);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private static void addKopSurat(Document document) throws Exception {
        URL imageUrl = ReportUtil.class.getClassLoader().getResource("images/kopOK.png");
        if (imageUrl == null) {
            throw new RuntimeException("File gambar kop surat tidak ditemukan di resources/images/kopOK.png");
        }
        byte[] imageBytes = readImageBytes(imageUrl);
        Image kop = Image.getInstance(imageBytes);
        kop.scaleToFit(530, 100);
        kop.setAlignment(Image.ALIGN_CENTER);
        document.add(kop);
    }

    private static void addFooter(Document document, String kota, String namaDirektur) throws DocumentException {
        String tanggal = new SimpleDateFormat("EEEE dd MMMM yyyy", new Locale("id", "ID")).format(new Date());

        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(40);
        footerTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        footerTable.setSpacingBefore(40f);

        PdfPCell cell1 = new PdfPCell(new Phrase(kota + ", " + tanggal, normalFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerTable.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase("Direktur", normalFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerTable.addCell(cell2);

        PdfPCell space = new PdfPCell(new Phrase("\n\n\n", normalFont));
        space.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(space);

        PdfPCell cell4 = new PdfPCell(new Phrase(namaDirektur, boldFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerTable.addCell(cell4);

        document.add(footerTable);
    }

    private static byte[] readImageBytes(URL imageUrl) throws Exception {
        try (InputStream is = imageUrl.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = is.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            return baos.toByteArray();
        }
    }

    private static void openPdfFromCopy(String filePath) {
        try {
            File originalFile = new File(filePath);
            if (!originalFile.exists()) {
                System.err.println("File PDF tidak ditemukan: " + filePath);
                return;
            }

            // Copy ke folder temp agar file asli tidak terkunci
            Path tempFile = Files.createTempFile("preview_", ".pdf");
            Files.copy(originalFile.toPath(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(tempFile.toFile());
                    System.out.println("Membuka salinan PDF untuk preview.");
                } else {
                    System.err.println("Action OPEN tidak didukung di Desktop.");
                }
            } else {
                System.err.println("Desktop tidak didukung di sistem ini.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal membuka file PDF: " + e.getMessage());
        }
    }
}
