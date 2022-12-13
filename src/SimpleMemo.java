import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class SimpleMemo extends JFrame implements ActionListener, CaretListener {
	public static void main(String[] args) {
		SimpleMemo memo = new SimpleMemo();
		memo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		memo.setVisible(true);
	}

	JTextPane textPane;
	DefaultStyledDocument doc;
	StyleContext sc;

	JToolBar toolBar;
	JComboBox<Object> comboFonts;
	JComboBox<Object> comboSizes;

	String currentFontName = "";
	int currentFontSize = 0;
	boolean flag = false;

	SimpleMemo() {
		setTitle("しんぷるなめもちょう");
		setBounds(200, 200, 300, 200);
		initToolbar();
		getContentPane().add(toolBar, BorderLayout.NORTH);

		textPane = new JTextPane();// テキストエリア的なやつ
		JScrollPane scroll = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// テキストエリアをスクロールできるようにするやつ
		getContentPane().add(scroll, BorderLayout.CENTER);
		sc = new StyleContext();
		doc = new DefaultStyledDocument(sc);
//		getContentPane().add(scroll);// スクロールできるテキストエリアをフレームに追加

		// スタイルもいじれるように
//		StyleContext sc = new StyleContext();
//		DefaultStyledDocument doc = new DefaultStyledDocument(sc);
		textPane.setDocument(doc);
//		textPane.setDocument(new DefaultStyledDocument(new StyleContext()));
		textPane.addCaretListener(this);// きゃれっととはカーソルが今どこにいるか
		// 初期文書の読み込み
		initDocument(doc, sc);
		// スタイルの変更
//		changeStyle(doc);
	}

	protected void initDocument(DefaultStyledDocument doc, StyleContext sc) {
		StringBuffer sb = new StringBuffer();
		sb.append("スタイル付きのテキストサンプルです。\n");
		sb.append("スタイルを変えて表示しています。");

		try {
			doc.insertString(0, new String(sb), sc.getStyle(StyleContext.DEFAULT_STYLE));
		} catch (Exception e) {
			System.err.println("初期文書の読み込みに失敗しました");
		}
	}

	protected void initToolbar() {
		toolBar = new JToolBar();
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String familyName[] = ge.getAvailableFontFamilyNames();
		comboFonts = new JComboBox<Object>(familyName);
		comboFonts.setMaximumSize(comboFonts.getPreferredSize());
		comboFonts.addActionListener(this);
//		comboFonts.setActionCommand("comboFonts");
		toolBar.add(comboFonts);

		toolBar.addSeparator();

		comboSizes = new JComboBox<Object>(new String[] { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22",
				"24", "26", "28", "36", "48", "72" });
		comboSizes.setMaximumSize(comboSizes.getPreferredSize());
		comboSizes.addActionListener(this);
//		comboSizes.setActionCommand("comboSizes");
		toolBar.add(comboSizes);
	}

	protected void setAttributeSet(AttributeSet attr) {

		int start = textPane.getSelectionStart();
		int end = textPane.getSelectionEnd();
		doc.setCharacterAttributes(start, end - start, attr, false);
	}

	protected void changeStyle(DefaultStyledDocument doc) {
		// 4文字目から8文字太字にする
		MutableAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setBold(attr, true);
		doc.setCharacterAttributes(4, 8, attr, false);

		// 6文字目から4文字分斜体にする
		MutableAttributeSet attr2 = new SimpleAttributeSet();
		StyleConstants.setItalic(attr2, true);
		doc.setCharacterAttributes(6, 4, attr2, false);

		// 2文字目から4文字分色変更、文字色赤、背景黒
		MutableAttributeSet attr3 = new SimpleAttributeSet();
		StyleConstants.setForeground(attr3, Color.red);
		StyleConstants.setBackground(attr3, Color.black);
		doc.setCharacterAttributes(2, 4, attr3, false);
		doc.setCharacterAttributes(2, 4, attr3, false);

		// 20文字目から6文字分行書体にしてフォントを大きく
		MutableAttributeSet attr4 = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr4, "HGP行書体");
		StyleConstants.setFontSize(attr4, 24);
		doc.setCharacterAttributes(20, 6, attr4, false);

		// 8文字目から5文字分下線を引く
		MutableAttributeSet attr5 = new SimpleAttributeSet();
		StyleConstants.setUnderline(attr5, true);
		doc.setCharacterAttributes(8, 5, attr5, false);

		// 27文字目から5文字分取り消し線を引く
		MutableAttributeSet attr6 = new SimpleAttributeSet();
		StyleConstants.setStrikeThrough(attr6, true);
		doc.setCharacterAttributes(27, 5, attr6, false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		if (flag) {
			// キャレット変更に伴うアクションイベントの場合はパスする
			return;
		}

		MutableAttributeSet attr = new SimpleAttributeSet();
		if (e.getSource() == comboFonts) {
			String fontName = comboFonts.getSelectedItem().toString();
			StyleConstants.setFontFamily(attr, fontName);
		} else if (e.getSource() == comboSizes) {
			int fontSize = Integer.parseInt(comboSizes.getSelectedItem().toString());
			StyleConstants.setFontSize(attr, fontSize);
		}

		setAttributeSet(attr);
		textPane.requestFocusInWindow();
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		flag = true;
		int p = textPane.getSelectionStart();
		AttributeSet a = doc.getCharacterElement(p).getAttributes();
		String name = StyleConstants.getFontFamily(a);

		// 変更前と同じ場合は無視
		if (!currentFontName.equals(name)) {
			currentFontName = name;
			comboFonts.setSelectedItem(name);
		}
		int size = StyleConstants.getFontSize(a);
		if (currentFontSize != size) {
			currentFontSize = size;
			comboSizes.setSelectedItem(Integer.toString(size));
		}
		flag = false;
	}
}
