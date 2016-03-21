package cn.yyx.labtask.afix.classmodification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
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
import cn.yyx.labtask.afix.patchgeneration.OnePatch;
import cn.yyx.labtask.afix.patchgeneration.SameLockExclusivePatches;

public class JarModifier {

	private OfflineInstrumenter instrumenter;
	String jar = null;
	Writer w = null;
	Class<?> lockpool = null;
	String InputJar = null;
	String OutputJar = null;

	public JarModifier(String inputjar, String outputjar) {
		this.InputJar = inputjar;
		this.OutputJar = outputjar;
	}

	private void InitialInstrumentor() throws IllegalArgumentException, IOException {
		w = new BufferedWriter(new FileWriter("report", false));
		instrumenter = new OfflineInstrumenter(false);
		String[] args = new String[] { InputJar, "-o", OutputJar };
		instrumenter.parseStandardArgs(args);
		instrumenter.setPassUnmodifiedClasses(true);
		instrumenter.addInputClass(new File("selfuseclasscode"),
				new File("selfuseclasscode/cn/yyx/labtask/afix/LockPool.class"));
	}

	private void TranverseFromBeginning() {
		instrumenter.beginTraversal();
	}

	private void DestroyInstrumentor() throws IllegalStateException, IOException {
		instrumenter.close();
	}

	public void HandleExclusivePatchesManager(ExclusivePatchesManager epm)
			throws IllegalArgumentException, IOException, InvalidClassFileException, ClassNotFoundException {

		int asize = epm.getSize();
		InitialLockPool(asize);

		InitialInstrumentor();
		int lockidx = 0;
		Iterator<SameLockExclusivePatches> itr = epm.Iterator();
		while (itr.hasNext()) {
			lockidx++;
			String lockname = "lock" + lockidx;
			SameLockExclusivePatches slep = itr.next();
			Iterator<OnePatch> opitr = slep.GetIterator();
			while (opitr.hasNext()) {
				OnePatch op = opitr.next();
				String msig = op.getMethodsig();
				ClassInstrumenter ci = GetClassInstrumenter(msig);
				MethodEditor me = null;
				for (int m = 0; m < ci.getReader().getMethodCount(); m++) {
					MethodData d = ci.visitMethod(m);
					String dsig = ci.getReader().getMethodName(m) + ci.getReader().getMethodType(m);
					if (dsig.equals(msig)) {
						me = new MethodEditor(d);
						break;
					}
				}
				Iterator<Integer> bpos = op.GetInsertPosBeginIterator();
				while (bpos.hasNext()) {
					int bp = bpos.next();
					me.insertBefore(bp, new MethodEditor.Patch() {
						@Override
						public void emitTo(MethodEditor.Output w) {
							w.emit(Util.makeGet(lockpool, lockname));
							w.emit(Util.makeInvoke(Lock.class, "lock", new Class[] {}));
						}
					});
				}
				Iterator<Integer> epos = op.GetInsertPosBeginIterator();
				while (epos.hasNext()) {
					int ep = epos.next();
					me.insertBefore(ep, new MethodEditor.Patch() {
						@Override
						public void emitTo(MethodEditor.Output w) {
							w.emit(Util.makeGet(lockpool, lockname));
							w.emit(Util.makeInvoke(Lock.class, "unlock", new Class[] {}));
						}
					});
				}
			}
		}
		DestroyInstrumentor();
	}

	private ClassInstrumenter GetClassInstrumenter(String msig) throws IOException, InvalidClassFileException {
		TranverseFromBeginning();
		ClassInstrumenter ci = null;
		while ((ci = instrumenter.nextClass()) != null) {
			ClassReader cls = ci.getReader();
			String classname = cls.getName();
			if (msig.startsWith(classname)) {
				break;
			}
		}
		return ci;
	}

	private void InitialLockPool(int asize) throws IOException, InvalidClassFileException, ClassNotFoundException {
		
		System.out.println("generate size:" + asize);
		
		{
			
		}
		
		{
			InitialInstrumentor();
			ClassInstrumenter ci = SearchForSpecifiedClass("cn/yyx/labtask/afix/LockPool");
			ClassWriter cw = ci.emitClass();
			for (int i = 0; i < asize; i++) {
				cw.addField(ClassReader.ACC_PUBLIC | ClassReader.ACC_STATIC, "lock" + (i + 1),
						"java.util.concurrent.locks.Lock", new ClassWriter.Element[0]);
			}
			instrumenter.outputModifiedClass(ci, cw);
			DestroyInstrumentor();
		}

		{
			File ojf = new File(OutputJar);
			// System.out.println(OutputJar + " exists? " + ojf.exists());
			@SuppressWarnings("resource")
			ClassLoader cl = new URLClassLoader(new URL[] { ojf.toURI().toURL() });
			lockpool = cl.loadClass("cn.yyx.labtask.afix.LockPool");
		}

		{
			InitialInstrumentor();
			ClassInstrumenter ci = SearchForSpecifiedClass("cn/yyx/labtask/afix/LockPool");
			MethodEditor me = null;
			for (int m = 0; m < ci.getReader().getMethodCount(); m++) {
				MethodData d = ci.visitMethod(m);
				if (ci.getReader().getMethodName(m).equals("<clinit>")) {
					me = new MethodEditor(d);
					break;
				}
			}
			me.beginPass();
			for (int i = 0; i < asize; i++) {
				String name = "lock" + (i + 1);
				me.insertAtStart(new MethodEditor.Patch() {
					@Override
					public void emitTo(MethodEditor.Output w) {
						w.emit(NewInstruction.make(Util.makeType(lockpool), 0));
						w.emit(DupInstruction.make(0));
						w.emit(Util.makeGet(lockpool, name));
						w.emit(Util.makeInvoke(ReentrantLock.class, "<init>", new Class[] {}));
						w.emit(Util.makePut(lockpool, name));
					}
				});
			}
			me.applyPatches();
			DestroyInstrumentor();
		}
	}

	private ClassInstrumenter SearchForSpecifiedClass(String specifiedclassname)
			throws IOException, InvalidClassFileException {
		TranverseFromBeginning();
		ClassInstrumenter ci = null;
		boolean found = false;
		while ((ci = instrumenter.nextClass()) != null) {
			ClassReader cls = ci.getReader();
			String classname = cls.getName();
			// System.out.println("classname:"+classname);
			if (classname.equals(specifiedclassname)) {
				found = true;
				break;
			}
		}
		if (!found) {
			return null;
		}
		return ci;
	}

}