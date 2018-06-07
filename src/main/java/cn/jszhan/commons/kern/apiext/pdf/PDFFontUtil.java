package cn.jszhan.commons.kern.apiext.pdf;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class PDFFontUtil {
	public static Font getFont(float size, int[] styles) throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font font = new Font(bfChinese, size);
		if (styles != null && styles.length > 0) {
			for (int style : styles) {
				font.setStyle(style);
			}
		}
		return font;
	}

	public static Font getSTFont(float size, int[] styles) throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font font = new Font(bfChinese, size);
		if (styles != null && styles.length > 0) {
			for (int style : styles) {
				font.setStyle(style);
			}
		}
		return font;
	}

	public static Font getMainFont() throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		return new Font(bfChinese, 9);
	}

	public static Font getMainBoldFont() throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		return new Font(bfChinese, 9, Font.BOLD);
	}

	public static Font getMainUnderlineFont() throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		return new Font(bfChinese, 9, Font.UNDERLINE);
	}

	public static Font getTitelFont() throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		return new Font(bfChinese, 16, Font.BOLD);
	}

	public static Font getMain11Font() throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		return new Font(bfChinese, 11);
	}

	public static Font getMain11UnderlineFont() throws DocumentException, IOException {
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		return new Font(bfChinese, 11, Font.UNDERLINE);
	}
}
