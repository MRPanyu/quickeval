package mrpanyu.quickeval;

import org.codehaus.groovy.control.CompilerConfiguration;

import groovy.lang.GroovyShell;

/**
 * Groovy shell based script evaluation utility with custom functions
 */
public class QuickEval {

	private static GroovyShell groovyShell;

	static {
		CompilerConfiguration config = new CompilerConfiguration();
		config.setScriptBaseClass(QuickEvalScriptBase.class.getName());
		groovyShell = new GroovyShell(config);
	}

	public static Object eval(String scriptText) throws Exception {
		Object retVal = groovyShell.evaluate(scriptText);
		return retVal;
	}

}
