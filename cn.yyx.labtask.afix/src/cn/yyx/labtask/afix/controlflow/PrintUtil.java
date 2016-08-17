package cn.yyx.labtask.afix.controlflow;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.ibm.wala.examples.drivers.PDFTypeHierarchy;
import com.ibm.wala.examples.properties.WalaExamplesProperties;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.properties.WalaProperties;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.graph.Graph;
import com.ibm.wala.viz.DotUtil;
import com.ibm.wala.viz.PDFViewUtil;

public class PrintUtil {

	private final static String PDF_FILE = "cg.pdf";

	public static Process PrintPDF(Graph<CGNode> g) throws WalaException {
		Properties p = null;
		p = WalaExamplesProperties.loadProperties();
		/*try {
			p = WalaExamplesProperties.loadProperties();
			p.putAll(WalaProperties.loadProperties());
		} catch (WalaException e) {
			e.printStackTrace();
			Assertions.UNREACHABLE();
		}*/
		String pdfFile = PDF_FILE;// p.getProperty(WalaProperties.OUTPUT_DIR) + File.separatorChar + 
		File pdff = new File(pdfFile);
		if (!pdff.exists())
		{
			try {
				pdff.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String dotExe = p.getProperty(WalaExamplesProperties.DOT_EXE);
		DotUtil.dotify(g, null, PDFTypeHierarchy.DOT_FILE, pdfFile, dotExe);

		String gvExe = p.getProperty(WalaExamplesProperties.PDFVIEW_EXE);
		return PDFViewUtil.launchPDFView(pdfFile, gvExe);
	}

	public static Process PrintIR(ClassHierarchy cha, IR ir) throws WalaException {
		Properties wp = null;
		try {
			wp = WalaProperties.loadProperties();
			wp.putAll(WalaExamplesProperties.loadProperties());
		} catch (WalaException e) {
			e.printStackTrace();
			Assertions.UNREACHABLE();
		}
		String psFile = wp.getProperty(WalaProperties.OUTPUT_DIR) + File.separatorChar + AFixWalaIR.PDF_FILE;
		String dotFile = wp.getProperty(WalaProperties.OUTPUT_DIR) + File.separatorChar + PDFTypeHierarchy.DOT_FILE;
		String dotExe = wp.getProperty(WalaExamplesProperties.DOT_EXE);
		String gvExe = wp.getProperty(WalaExamplesProperties.PDFVIEW_EXE);

		return PDFViewUtil.ghostviewIR(cha, ir, psFile, dotFile, dotExe, gvExe);
	}

}