package cn.yyx.labtask.afix.classmodification;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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

	public static final String lockpoolinipath = "selfuseclassbackup/lockpool.jar";
	public static final String lockpoolpath = "selfuseclasscode/lockpool.jar";
	public static final String lockpoolfinalpath = "selfuseclassfinalcode/lockpool.jar";

	private OfflineInstrumenter instrumenter = null;
	private OfflineInstrumenter lockpoolinstrumenter = null;
	String jar = null;
	Class<?> lockpool = null;
	String InputJar = null;
	String OutputJar = null;

	public JarModifier(String inputjar, String outputjar) {
		this.InputJar = inputjar;
		this.OutputJar = outputjar;
	}

	private void InitialLockPoolInstrumentor(String inijarpath, String finaljarpath)
			throws IllegalArgumentException, IOException {
		lockpoolinstrumenter = new OfflineInstrumenter(false);
		String[] args = new String[] { inijarpath, "-o", finaljarpath };
		lockpoolinstrumenter.parseStandardArgs(args);
		lockpoolinstrumenter.setPassUnmodifiedClasses(true);
	}

	private void DestroyLockPoolInstrumentor() throws IllegalStateException, IOException {
		lockpoolinstrumenter.close();
		lockpoolinstrumenter = null;
	}

	private void InitialInstrumentor() throws IllegalArgumentException, IOException {
		instrumenter = new OfflineInstrumenter(false);
		String[] args = new String[] { InputJar, "-o", OutputJar };
		instrumenter.parseStandardArgs(args);
		instrumenter.setPassUnmodifiedClasses(true);
		instrumenter.addInputJarEntry(new File(lockpoolfinalpath), "cn/yyx/labtask/afix/LockPool.class");
	}

	private void TranverseFromBeginning(OfflineInstrumenter instrumenter) {
		instrumenter.beginTraversal();
	}

	private void DestroyInstrumentor() throws IllegalStateException, IOException {
		instrumenter.close();
		instrumenter = null;
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
				String dsig = null;
				ClassInstrumenter ci = GetClassInstrumenter(msig, instrumenter);
				MethodEditor me = null;
				for (int m = 0; m < ci.getReader().getMethodCount(); m++) {
					MethodData d = ci.visitMethod(m);
					ClassReader cr = ci.getReader();
					dsig = cr.getMethodName(m) + cr.getMethodType(m);
					// System.err.println("dsig:" + dsig);
					// System.err.println("msig:" + msig);
					if (dsig.endsWith(msig) || msig.endsWith(dsig)) {
						me = new MethodEditor(d);
						break;
					}
				}
				me.beginPass();
				Iterator<Integer> bpos = op.GetInsertPosBeginIterator();
				while (bpos.hasNext()) {
					int bp = bpos.next();
					// testing
					System.out.println("methodsig:" + op.getMethodsig() + ";bp:" + bp);
					me.insertBefore(bp, new MethodEditor.Patch() {
						@Override
						public void emitTo(MethodEditor.Output w) {
							w.emit(Util.makeGet(lockpool, lockname));
							w.emit(Util.makeInvoke(Lock.class, "lock", new Class[] {}));
						}
					});
				}
				Iterator<Integer> epos = op.GetInsertPosEndIterator();
				while (epos.hasNext()) {
					int ep = epos.next();
					// testing
					System.out.println("methodsig:" + op.getMethodsig() + ";ep:" + ep);
					me.insertAfter(ep, new MethodEditor.Patch() {
						@Override
						public void emitTo(MethodEditor.Output w) {
							w.emit(Util.makeGet(lockpool, lockname));
							w.emit(Util.makeInvoke(Lock.class, "unlock", new Class[] {}));
						}
					});
				}
				me.applyPatches();
				ClassWriter cw = ci.emitClass();
				instrumenter.outputModifiedClass(ci, cw);
			}
		}
		DestroyInstrumentor();
	}

	private ClassInstrumenter GetClassInstrumenter(String msig, OfflineInstrumenter instrumenter)
			throws IOException, InvalidClassFileException {
		TranverseFromBeginning(instrumenter);
		ClassInstrumenter ci = null;
		while ((ci = instrumenter.nextClass()) != null) {
			ClassReader cls = ci.getReader();
			String classname = cls.getName();
			// System.out.println("classname:"+classname);
			classname = classname.replace('/', '.');
			int bra = msig.indexOf('(');
			String mtype = msig.substring(0, bra);
			int lddx = mtype.lastIndexOf('.');
			mtype = mtype.substring(0, lddx);
			if (mtype.equals(classname)) {
				break;
			}
		}
		/*
		 * if (ci == null) { System.out.println(msig); }
		 */
		return ci;
	}

	private void InitialLockPool(int asize) throws IOException, InvalidClassFileException, ClassNotFoundException {
		System.out.println("lock size:" + asize);

		{
			InitialLockPoolInstrumentor(lockpoolinipath, lockpoolpath);
			ClassInstrumenter ci = SearchForSpecifiedClass("cn/yyx/labtask/afix/LockPool", lockpoolinstrumenter);
			ClassWriter cw = ci.emitClass();
			for (int i = 0; i < asize; i++) {
				cw.addField(ClassReader.ACC_PUBLIC | ClassReader.ACC_STATIC, "lock" + (i + 1),
						"Ljava/util/concurrent/locks/Lock;", new ClassWriter.Element[0]);
			}
			lockpoolinstrumenter.outputModifiedClass(ci, cw);
			DestroyLockPoolInstrumentor();
		}
		
		LoadLockPool(lockpoolpath);

		{
			InitialLockPoolInstrumentor(lockpoolpath, lockpoolfinalpath);
			ClassInstrumenter ci = SearchForSpecifiedClass("cn/yyx/labtask/afix/LockPool", lockpoolinstrumenter);
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
						w.emit(NewInstruction.make(Util.makeType(ReentrantLock.class), 0));
						w.emit(DupInstruction.make(0));
						// w.emit(Util.makeGet(lockpool, name));
						w.emit(Util.makeInvoke(ReentrantLock.class, "<init>", new Class[] {}));
						w.emit(Util.makePut(lockpool, name));
					}
				});
			}
			me.applyPatches();
			ClassWriter cw = ci.emitClass();
			lockpoolinstrumenter.outputModifiedClass(ci, cw);
			DestroyLockPoolInstrumentor();
		}
		
		LoadLockPool(lockpoolfinalpath);
	}

	private void LoadLockPool(String path) throws ClassNotFoundException, MalformedURLException {
		File ojf = new File(path);
		// System.out.println(OutputJar + " exists? " + ojf.exists());
		@SuppressWarnings("resource")
		ClassLoader cl = new URLClassLoader(new URL[] { ojf.toURI().toURL() });
		lockpool = cl.loadClass("cn.yyx.labtask.afix.LockPool");
	}

	private ClassInstrumenter SearchForSpecifiedClass(String specifiedclassname, OfflineInstrumenter instrumenter)
			throws IOException, InvalidClassFileException {
		TranverseFromBeginning(instrumenter);
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