package cn.yyx.labtask.afix.classmodification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import com.ibm.wala.shrikeBT.Constants;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.shrikeCT.ClassInstrumenter;
import com.ibm.wala.shrikeBT.shrikeCT.OfflineInstrumenter;
import com.ibm.wala.shrikeCT.ClassReader;
import com.ibm.wala.shrikeCT.ClassWriter;
import com.ibm.wala.shrikeCT.InvalidClassFileException;

import cn.yyx.labtask.afix.patchgeneration.ExclusivePatchesManager;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;

public class JarModifier {

	private OfflineInstrumenter instrumenter;
	String jar = null;
	Writer w = null;

	public JarModifier(String jar) {
		this.jar = jar;
	}

	private void InitialInstrumentor() throws IllegalArgumentException, IOException {
		w = new BufferedWriter(new FileWriter("report", false));
		instrumenter = new OfflineInstrumenter(false);
		String[] args = new String[] { "TestInputJar/WTest.jar", "-o", "TestOutputJar/WTest.jar" };
		instrumenter.parseStandardArgs(args);
		instrumenter.setPassUnmodifiedClasses(true);
		instrumenter.addInputClass(new File("selfuseclasscode"),
				new File("selfuseclasscode/cn/yyx/labtask/afix/LockPool.class"));
		instrumenter.beginTraversal();
	}

	private void DestroyInstrumentor() throws IllegalStateException, IOException {
		instrumenter.close();
	}

	public void HandleExclusivePatchesManager(ExclusivePatchesManager epm)
			throws IllegalArgumentException, IOException, InvalidClassFileException {
		InitialInstrumentor();

		int asize = epm.getSize();
		InitialLockPool(asize);
		Iterator<SameLockExclusivePatches> itr = epm.Iterator();
		ClassInstrumenter ci = null;
		while ((ci = instrumenter.nextClass()) != null) {
			ClassReader cls = ci.getReader();
			String classname = cls.getName();
			System.out.println(classname);
			// doClass(ci, w);
			// TODO
		}

		DestroyInstrumentor();
	}

	private void InitialLockPool(int asize) throws IOException, InvalidClassFileException {
		ClassInstrumenter ci = null;
		while ((ci = instrumenter.nextClass()) != null) {
			
			ClassReader cls = ci.getReader();
			String classname = cls.getName();
			if (classname.equals("cn/yyx/labtask/afix/LockPool")) {
				for (int m = 0; m < ci.getReader().getMethodCount(); m++) {
					MethodData d = ci.visitMethod(m);
					
					MethodEditor me = new MethodEditor(d);
					me.beginPass();
					// TODO
				}
				ClassWriter cw = ci.emitClass();
				cw.addField(ClassReader.ACC_PUBLIC | ClassReader.ACC_STATIC, "lock"+idx, "java.util.concurrent.locks.Lock",
						new ClassWriter.Element[0]);
				instrumenter.outputModifiedClass(ci, cw);
				for (int i=0;i<asize;i++)
				{
					
				}
				
				break;
			}
		}
	}

}