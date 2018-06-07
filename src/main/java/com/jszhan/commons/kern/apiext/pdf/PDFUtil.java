package com.jszhan.commons.kern.apiext.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

//pdf生成的一些公共方法
public class PDFUtil {
	public static final String INDENTATION2 = "          ";
	public static final String passwd = "3SDEcowEjM1=";

	// 返回 Object[] { writer, document }
	public static Object[] getWriterDocument(Rectangle pageSize, OutputStream out, PdfPageEventHelper... headerFooters) throws FileNotFoundException, DocumentException {
		Document document = null;
		PdfWriter writer = null;
		if (headerFooters != null && headerFooters.length > 0) {
			// marginLeft, marginRight, marginTop, marginBotton
			document = new Document(pageSize, 26, 26, 72, 72);
			writer = PdfWriter.getInstance(document, out);
			Rectangle rect = new Rectangle(26, 72, 569, 770); // 595*842
			// llx, lly, urx, ury
			rect.setBorderColor(BaseColor.BLACK);
			writer.setBoxSize("art", rect);
			for (int i = 0; i < headerFooters.length; i++) {
				writer.setPageEvent(headerFooters[i]);
			}
		} else {
			document = new Document(pageSize);
			writer = PdfWriter.getInstance(document, out);
		}

		document.open();
		return new Object[] { writer, document };
	}

	public static Document getDocument(Rectangle pageSize, OutputStream out, PdfPageEventHelper... headerFooters) throws FileNotFoundException, DocumentException {
		Document document = null;
		if (headerFooters != null && headerFooters.length > 0) {
			// marginLeft, marginRight, marginTop, marginBotton
			document = new Document(pageSize, 26, 26, 72, 72);
			PdfWriter writer = PdfWriter.getInstance(document, out);
			Rectangle rect = new Rectangle(26, 72, 569, 770); // 595*842
			// llx, lly, urx, ury
			rect.setBorderColor(BaseColor.BLACK);
			writer.setBoxSize("art", rect);
			for (int i = 0; i < headerFooters.length; i++) {
				writer.setPageEvent(headerFooters[i]);
			}
		} else {
			document = new Document(pageSize);
			PdfWriter.getInstance(document, out);
		}

		document.open();
		return document;
	}

	// 捷越的标识位于首页首位
	public static void setTopHeader(PdfPCell cell, String leftTitel, String webContext) throws DocumentException, IOException {
		String imgFilePathName = webContext + File.separator + "static" + File.separator + "images" + File.separator + "pdf_images" + File.separator + "logo.png";
		Image logoImage = Image.getInstance(imgFilePathName);
		logoImage.setBorder(Rectangle.NO_BORDER);
		logoImage.scaleAbsolute(120, 80);

		PdfPTable table = PDFUtil.getTable(2);
		int[] widths = new int[] { 300, 200 };
		table.setWidths(widths);
		PdfPCell theCell = new PdfPCell();
		theCell.setBorder(Rectangle.NO_BORDER);
		theCell.addElement(new Paragraph(org.apache.commons.lang3.StringUtils.isBlank(leftTitel) ? " " : leftTitel, PDFFontUtil.getMainFont()));
		table.addCell(theCell);
		theCell = new PdfPCell();
		theCell.setBorder(Rectangle.NO_BORDER);
		theCell.addElement(logoImage);
		table.addCell(theCell);
		cell.addElement(table);
		cell.addElement(new Paragraph(" "));
	}

	// 表格，空表
	public static PdfPTable getTable(int cellSize) {
		PdfPTable table = new PdfPTable(cellSize);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		// table.getDefaultCell().setFixedHeight(CELLHIGHT);
		// 设置表格背景颜色
		// table.getDefaultCell().setBackgroundColor(new BaseColor(0, 0, 0));
		// 设置表格边框颜色
		// table.getDefaultCell().setBorderColor(new BaseColor(250, 250, 250));
		// 设置单元格的边距间隔等
		table.getDefaultCell().setPadding(5);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSplitLate(false);// 表示单元格是否跨页显示
		table.setSplitRows(true);// 表示行是否跨页显示
		return table;
	}

	// 表格，传入sellSize,body,padding{左右上下}一般用于签章
	public static PdfPTable getTable(String path, int cellSize, String[] body, int[] padding, Font... mainFonts) throws DocumentException, IOException {
		Font mainFont;
		if (null == mainFonts || mainFonts.length <= 0) {
			mainFont = PDFFontUtil.getMainFont();
		} else {
			mainFont = mainFonts[0];
		}
		PdfPTable table = PDFUtil.getTable(cellSize);
		table.setWidthPercentage(100);
		table.setSplitRows(false);

		// table.setKeepTogether(true);
		for (String signature : body) {
			Paragraph paragraph = new Paragraph(signature, mainFont);

			PdfPCell cell = new PdfPCell(paragraph);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(1);
			cell.setBorder(Rectangle.NO_BORDER);
			if (padding != null) {
				cell.setPaddingLeft(padding[0]);
				cell.setPaddingRight(padding[1]);
				cell.setPaddingTop(padding[2]);
				cell.setPaddingBottom(padding[3]);
			}
			/*
			 * if (signature.contains("签章")) { Image png =
			 * Image.getInstance(path + "static/images/about_num001.png");
			 * png.scaleToFit(150, 150); png.setAlignment(Image.MIDDLE |
			 * Image.TEXTWRAP); paragraph.add(new Chunk(png, -20, -120, true));
			 * }
			 */
			table.addCell(cell);
		}
		return table;
	}

	// 表格，可以将不太规则的标头设置好后，传入表格
	public static PdfPTable getTable(PdfPTable table, Paragraph tableTitleP, String[] header, List<String[]> body) throws DocumentException, IOException {
		Font mainFont = PDFFontUtil.getMainFont();
		// Font mainFont_bold = PDFFontUtil.getMainBoldFont();

		// 创建表格对象，设置cell
		int cellSize = header.length;
		if (table == null) {
			table = getTable(cellSize);
		}

		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		// 标题，整行
		PdfPCell cell;
		if (tableTitleP != null) {
			tableTitleP.setAlignment(Element.ALIGN_LEFT);
			cell = new PdfPCell(tableTitleP);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(cellSize);
			cell.setBorder(0);
			table.addCell(cell);
		}
		Paragraph cel;
		for (String head : header) {
			cel = new Paragraph(head, mainFont);
			cel.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(cel);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(1);
			table.addCell(cell);
		}
		for (String[] row : body) {
			for (String data : row) {
				cel = new Paragraph(data, mainFont);
				cel.setAlignment(Element.ALIGN_CENTER);
				cell = new PdfPCell(cel);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setColspan(1);
				table.addCell(cell);
			}
		}
		return table;
	}

	// 表格，可以将不太规则的标头设置好后，传入表格
	public static PdfPTable getTableForDailyCount(PdfPTable table, Paragraph tableTitleP, String[] header, List<String[]> body) throws DocumentException, IOException {
		Font mainFont = PDFFontUtil.getMainFont();
		// Font mainFont_bold = PDFFontUtil.getMainBoldFont();

		// 创建表格对象，设置cell
		int cellSize = 0;
		if (null != header) {
			cellSize = header.length;
		} else if (CollectionUtils.isNotEmpty(body)) {
			cellSize = body.get(0).length;
		}
		if (table == null) {
			table = new PdfPTable(cellSize);
			table.setWidthPercentage(90);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			// 设置表格边框颜色
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			// 设置单元格的边距间隔等
			table.getDefaultCell().setPadding(5);
			table.getDefaultCell().setBorderWidth(0);
			table.setSplitLate(false);
			table.setSplitRows(true);
		}
		// 标题，整行
		PdfPCell cell;
		if (tableTitleP != null) {
			tableTitleP.setAlignment(Element.ALIGN_LEFT);
			cell = new PdfPCell(tableTitleP);
			cell.setFixedHeight(30);
			cell.setBorderWidthBottom(5);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(cellSize);
			cell.setBorder(0);
			table.addCell(cell);
		}
		Paragraph cel;
		if (null != header) {
			for (String head : header) {
				cel = new Paragraph(head, mainFont);
				cel.setAlignment(Element.ALIGN_CENTER);
				cell = new PdfPCell(cel);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setColspan(1);
				cell.setBackgroundColor(new BaseColor(230, 230, 230));
				table.addCell(cell);
			}
		}

		for (String[] row : body) {
			for (int i = 0; i < row.length; i++) {
				cel = new Paragraph(row[i], mainFont);
				cel.setAlignment(Element.ALIGN_CENTER);
				cell = new PdfPCell(cel);
				if (i == 0) {
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);// 设置内容水平居中显示
				} else {
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);// 设置内容水平居中显示
				}
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setColspan(1);
				table.addCell(cell);

			}
		}
		return table;
	}

	/* 标头加黑 */
	public static PdfPTable getTableBoldHeader(PdfPTable table, Paragraph tableTitleP, String[] header, List<String[]> body) throws DocumentException, IOException {
		Font mainFont = PDFFontUtil.getMainFont();
		Font mainFont_bold = PDFFontUtil.getMainBoldFont();

		// 创建表格对象，设置cell
		int cellSize = header.length;
		if (table == null) {
			table = getTable(cellSize);
		}

		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		// 标题，整行
		PdfPCell cell;
		if (tableTitleP != null) {
			tableTitleP.setAlignment(Element.ALIGN_LEFT);
			cell = new PdfPCell(tableTitleP);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(cellSize);
			cell.setBorder(0);
			table.addCell(cell);
		}
		Paragraph cel;
		for (String head : header) {
			cel = new Paragraph(head, mainFont_bold);
			cel.setAlignment(Element.ALIGN_CENTER);
			cell = new PdfPCell(cel);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setColspan(1);
			table.addCell(cell);
		}
		for (String[] row : body) {
			for (String data : row) {
				cel = new Paragraph(data, mainFont);
				cel.setAlignment(Element.ALIGN_CENTER);
				cell = new PdfPCell(cel);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setColspan(1);
				table.addCell(cell);
			}
		}
		return table;
	}

	// 称谓
//	public static Paragraph getCustomerTitel(Customer customerInfo) throws DocumentException, IOException {
//		Font mainFont = PDFFontUtil.getMainFont();
//		Font mainFont_underline = PDFFontUtil.getMainUnderlineFont();
//
//		String gender = "公司";
//
//		String content1_2 = "尊敬的";
//		String content1_2_1_under = "  " + customerInfo.getName() + "  ";
//		Paragraph content1_2Pa = new Paragraph(content1_2, mainFont);
//		content1_2Pa.add(new Phrase(content1_2_1_under, mainFont_underline));
//		content1_2Pa.add(new Phrase(gender, mainFont));
//		return content1_2Pa;
//	}
//
//	// 信封
//	public static void setEnvelope(PdfPCell cell, Customer customer) throws IOException, DocumentException {
//		Font postCodeFont = PDFFontUtil.getFont(22, new int[] { Font.BOLD });
//		Font envelopeFont = PDFFontUtil.getFont(11, null);
//
//		cell.addElement(new Paragraph(customer.getPostalCode(), postCodeFont));
//		cell.addElement(new Paragraph(" "));
//		String[] infos = null;
//		if (!Strings.isNullOrEmpty(customer.getAddress())) {
//			infos = customer.getAddress().split("\\|");
//			for (int i = 0; i < infos.length; i++) {
//				cell.addElement(new Paragraph(infos[i], envelopeFont));
//			}
//		}
//		cell.addElement(new Paragraph(customer.getName() + "    收", envelopeFont));
//		cell.addElement(new Paragraph(" "));
//	}

	// 信封
	/*
	 * public static void setEnvelopeFrontCover(PdfPCell cell, Customer
	 * customer, String webContext) throws MalformedURLException, IOException,
	 * DocumentException { Font envelopeFont = PDFFontUtil.getFont(10, null);
	 * PdfPTable table = PDFUtil.getTable(2); int[] widths = new int[] { 300,
	 * 200 }; table.setWidths(widths); PdfPCell theCell = new PdfPCell();
	 * theCell.setBorder(Rectangle.NO_BORDER); theCell.addElement(new
	 * Paragraph("                   " + customer.getPostCode(), envelopeFont));
	 * String[] infos = customer.getAdress().split("\\|"); for (int i = 0; i <
	 * infos.length; i++) { theCell.addElement(new
	 * Paragraph("                   " + infos[i], envelopeFont)); }
	 * 
	 * String gender = "";
	 * 
	 * if (!"7".equals(customer.getCardType())) { if
	 * (customer.getGender().equals("1")) { gender = "先生"; } else if
	 * (customer.getGender().equals("0")) { gender = "女士"; } }
	 * theCell.addElement(new Paragraph("                   " + customer.getn +
	 * "   " + gender + "   亲启", envelopeFont));
	 * 
	 * table.addCell(theCell); theCell = new PdfPCell();
	 * theCell.setBorder(Rectangle.NO_BORDER); table.addCell(theCell);
	 * cell.addElement(table); cell.addElement(new Paragraph(" "));
	 * 
	 * }
	 */

	/*
	 * 构建单元格：文本，文本格式，单元格内容对齐， 占据几行,默认黑色边框
	 */
	public static PdfPCell getCell(Paragraph paragraph, int align, int colspan, BaseColor... borderColor) {
		PdfPCell cell = new PdfPCell(paragraph);
		cell.setHorizontalAlignment(align);// 设置内容水平居中显示
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(colspan);
		if (borderColor != null && borderColor.length > 0) {
			cell.setBorderColor(borderColor[0]);
		} else {
			cell.setBorder(Rectangle.NO_BORDER);
		}

		return cell;
	}

	public static Paragraph getHorizontallyCell() {
		String horizontally = "-------------------------------------------------------------------";
		Paragraph paragraph = new Paragraph(horizontally);
		paragraph.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
		return paragraph;
	}

	public static Paragraph getHoriCell() throws Exception {
		String horizontally = "   ";
		Paragraph paragraph = new Paragraph(horizontally, PDFFontUtil.getMainUnderlineFont());
		paragraph.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
		return paragraph;
	}

	public static Paragraph getPartHorizontallyCell() {
		String horizontally = "------------                                                                           ------------";
		Paragraph paragraph = new Paragraph(horizontally);
		paragraph.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
		return paragraph;
	}

	public static Paragraph getParagraph(String text, Font font, int alignment) {
		Paragraph paragraph = new Paragraph(text, font);
		paragraph.setAlignment(alignment);
		return paragraph;
	}

}
