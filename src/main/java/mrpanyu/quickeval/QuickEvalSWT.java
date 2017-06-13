package mrpanyu.quickeval;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

/**
 * SWT UI interface for QuickEval.
 */
public class QuickEvalSWT {

	private static Display display;
	private static Shell shell;
	private static Text textScript;
	private static Text textResult;
	private static Button chkAutoCopyToClipboard;
	private static Clipboard clipboard;

	/**
	 * Should the program copy evaluation results to clipboard.
	 */
	private static boolean autoCopyToClipboard = true;

	/**
	 * Should the program copy script from clipboard on startup.
	 */
	private static boolean copyScriptFromClipboard = false;

	public static void main(String[] args) throws Exception {
		if (args != null && args.length > 0 && "-copyScriptFromClipboard".equals(args[0])) {
			copyScriptFromClipboard = true;
		}
		display = new Display();
		clipboard = new Clipboard(display);
		shell = new Shell(display);
		buildShell(display, shell);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		shell.dispose();
		clipboard.dispose();
		display.dispose();
	}

	public static void buildShell(Display display, Shell shell) throws Exception {
		shell.setText("QuickEval");
		shell.setImage(new Image(display, QuickEvalSWT.class.getResourceAsStream("calculator.png")));
		shell.setLayout(new FillLayout());
		shell.setSize(800, 400);

		TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);
		TabItem tabItem1 = new TabItem(tabFolder, SWT.NONE);
		tabItem1.setText("Eval");
		Composite comp1 = new Composite(tabFolder, SWT.NONE);
		tabItem1.setControl(comp1);
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
		tabItem2.setText("Help");
		Composite comp2 = new Composite(tabFolder, SWT.NONE);
		tabItem2.setControl(comp2);

		GridLayout gridLayout = new GridLayout(1, false);
		comp1.setLayout(gridLayout);

		Group group1 = new Group(comp1, SWT.NONE);
		group1.setLayout(new FillLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		group1.setLayoutData(gridData);
		group1.setText("Script");

		textScript = new Text(group1, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		textScript.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.keyCode == 13 || e.keyCode == 16777296) && (e.stateMask & SWT.CTRL) != 0) {
					e.doit = false;
					doEval();
				}
			}
		});

		Button button = new Button(comp1, SWT.PUSH);
		button.setText("Eval");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		button.setLayoutData(gridData);

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				doEval();
			}
		});

		Group group2 = new Group(comp1, SWT.NONE);
		group2.setLayout(new GridLayout(1, false));
		group2.setText("Result");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		group2.setLayoutData(gridData);

		textResult = new Text(group2, SWT.SINGLE | SWT.BORDER);
		textResult.setEditable(false);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		textResult.setLayoutData(gridData);
		textResult.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				textResult.setSelection(0, textResult.getText().length());
			}
		});

		chkAutoCopyToClipboard = new Button(group2, SWT.CHECK);
		chkAutoCopyToClipboard.setSelection(autoCopyToClipboard);
		chkAutoCopyToClipboard.setText("Auto copy result to clipboard");
		chkAutoCopyToClipboard.setLayoutData(gridData);
		chkAutoCopyToClipboard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				autoCopyToClipboard = chkAutoCopyToClipboard.getSelection();
			}
		});

		String helpText = IOUtils.toString(QuickEvalSWT.class.getResourceAsStream("help.txt"), "UTF-8");

		comp2.setLayout(new FillLayout());
		Text textHelp = new Text(comp2, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		textHelp.setText(helpText);
		textHelp.setEditable(false);

		if (copyScriptFromClipboard) {
			String script = (String) clipboard.getContents(TextTransfer.getInstance());
			if (script == null) {
				script = "";
			}
			textScript.setText(script);
			doEval();
		}

		textScript.setFocus();
		textScript.setSelection(0, textScript.getText().length());
	}

	private static void doEval() {
		String script = textScript.getSelectionText();
		if (StringUtils.isEmpty(script)) {
			script = textScript.getText();
		}
		if (StringUtils.isNotBlank(script)) {
			try {
				Object objResult = QuickEval.eval(script);
				textResult.setBackground(new Color(display, 255, 255, 255));
				textResult.setText(objResult.toString());
				if (autoCopyToClipboard) {
					clipboard.setContents(new Object[] { objResult.toString() },
							new Transfer[] { TextTransfer.getInstance() });
				}
			} catch (Exception e) {
				e.printStackTrace();
				textResult.setBackground(new Color(display, 255, 128, 128));
				textResult.setText("Error: " + e.getMessage());
			}
		}
	}

}
