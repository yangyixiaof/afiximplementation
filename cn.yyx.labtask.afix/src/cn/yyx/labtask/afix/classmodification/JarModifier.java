package cn.yyx.labtask.afix.classmodification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import com.ibm.wala.shrikeBT.DupInstruction;
import com.ibm.wala.shrikeBT.MethodData;
import com.ibm.wala.shrikeBT.MethodEditor;
import com.ibm.wala.shrikeBT.NewInstruction;
import com.ibm.wala.shrikeBT.Util;
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
	public static final String InputJar = "TestInputJar/WTest.jar";
	public static final String OutputJar = "TestOutputJar/WTest.jar";

	public JarModifier(String jar) {
		this.jar = jar;
	}

	private void InitialInstrumentor() throws IllegalArgumentException, IOException {
		w = new BufferedWriter(new FileWriter("report", false));
		instrumenter = new OfflineInstrumenter(false);
		String[] args = new String[] { InputJar, "-o", OutputJar };
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
			throws IllegalArgumentException, IOException, InvalidClassFileException, ClassNotFoundException {
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

	private void InitialLockPool(int asize) throws IOException, InvalidClassFileException, ClassNotFoundException {
		ClassInstrumenter ci = null;
		while ((ci = instrumenter.nextClass()) != null) {
			ClassReader cls = ci.getReader();
			String classname = cls.getName();
			if (classname.equals("cn/yyx/labtask/afix/LockPool")) {
				ClassWriter cw = ci.emitClass();
				for (int i=0;i<asize;i++)
				{
					cw.addField(ClassReader.ACC_PUBLIC | ClassReader.ACC_STATIC, "lock"+(i+1), "java.util.concurrent.locks.Lock",
							new ClassWriter.Element[0]);
				}
				instrumenter.outputModifiedClass(ci, cw);
				
				@SuppressWarnings("resource")
				ClassLoader cl = new URLClassLoader(new URL[]{new URL(OutputJar)});
		        Class<?> c = cl.loadClass("cn/yyx/labtask/afix/LockPool");
				MethodEditor me = null;
				for (int m = 0; m < ci.getReader().getMethodCount(); m++) {
					MethodData d = ci.visitMethod(m);
					if (ci.getReader().getMethodName(m).equals("<clinit>"))
					{
						me = new MethodEditor(d);
						break;
					}
				}
				me.beginPass();
				for (int i=0;i<asize;i++)
				{
					String name = "lock"+(i+1);
					me.insertAtStart(new MethodEditor.Patch() {
						@Override
						public void emitTo(MethodEditor.Output w) {
							w.emit(NewInstruction.make(Util.makeType(c), 0));
							w.emit(DupInstruction.make(0));
							w.emit(Util.makeGet(c, name));
							w.emit(Util.makeInvoke(ReentrantLock.class, "<init>",  new Class[] {}));
							w.emit(Util.makePut(c, name));
						}
					});
				}
				me.applyPatches();
				break;
			}
		}
	}

}