package com.ssh.sys.web.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PdfExporter {

    public static BaseFont baseFont;

    public PdfExporter() {
        try {
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void pdfWrite(PdfRow pdfRow, float[] widths, OutputStream outputStream) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.addTitle(pdfRow.getPdfTitle());
        document.open();
        Font f12 = new Font(baseFont, 12, Font.BOLD);
        Paragraph p = new Paragraph(pdfRow.getPdfTitle(), f12);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        document.add(new Paragraph(Chunk.NEWLINE));
        int cols = pdfRow.getContent().get(0).length;
        PdfPTable table = new PdfPTable(cols);
        table.setWidthPercentage(100);
        if (widths.length == cols) {
            table.setWidths(widths);
        }
        for (Object[] str : pdfRow.getContent()) {
            for (Object obj : str) {
                if (obj != null) {
                    PdfPCell cell = setCell(StringUtils.trim(String.valueOf(obj)));
                    table.addCell(cell);
                } else {
                    table.addCell("");
                }
            }
        }
        document.add(table);
        document.close();
    }

    public static PdfPCell setCell(String text) {
        Font fontChinese = new Font(baseFont, 10);
        PdfPCell cell = new PdfPCell(new Paragraph(text, fontChinese));
        boolean isNumeric = StringUtils.isNumeric(text);
        if (isNumeric) {
            cell.setNoWrap(true);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        } else {
            cell.setNoWrap(false);
        }
        return cell;
    }

    public class PdfRow {

        private String pdfTitle;
        private List<Object[]> content;
        private String PdfOther;

        public String getPdfTitle() {
            return pdfTitle;
        }

        public void setPdfTitle(String pdfTitle) {
            this.pdfTitle = pdfTitle;
        }

        public String getPdfOther() {
            return PdfOther;
        }

        public void setPdfOther(String pdfOther) {
            PdfOther = pdfOther;
        }

        public List<Object[]> getContent() {
            return content;
        }

        public void setContent(List<Object[]> content) {
            this.content = content;
        }

    }

}
