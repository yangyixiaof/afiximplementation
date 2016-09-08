package cn.yyx.labtask.afix.commonutil;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;

public class TypeUtil {
	
	public TypeUtil() {
	}
	
	@SuppressWarnings("unchecked")
	public static boolean TypeComparable(ASTNode type, final String comptype){
		if (type instanceof PrimitiveType) {
			String code = ((PrimitiveType) type).toString().trim();
			if (comptype.contains(code))
			{
				return true;
			}
			return false;
		}
		if (type instanceof SimpleType) {
			String code = ((SimpleType) type).toString().trim();
			if (comptype.contains(code))
			{
				return true;
			}
			return false;
		}
		if (type instanceof QualifiedType) {
			String code = ((QualifiedType) type).getName().toString();
			if (comptype.contains(code))
			{
				return true && TypeComparable(((QualifiedType) type).getQualifier(), comptype);
			}
			return false;
		}
		if (type instanceof NameQualifiedType) {
			NameQualifiedType nt = (NameQualifiedType) type;
			String code = nt.getName().toString();
			if (comptype.contains(code))
			{
				return true && TypeComparable(nt.getQualifier(), comptype);
			}
			return false;
		}
		if (type instanceof WildcardType) {
			WildcardType wt = (WildcardType) type;
			if (wt.getBound() != null) {
				return TypeComparable(wt.getBound(), comptype);
			}
			else
			{
				return true;
			}
		}
		if (type instanceof ArrayType) {
			ArrayType at = (ArrayType) type;
			int dimens = at.dimensions().size();
			String dimenstr = "";
			for (int i = 0; i < dimens; i++) {
				dimenstr += "[]";
			}
			if (comptype.contains(dimenstr))
			{
				return true && TypeComparable(at.getElementType(), comptype);
			}
			return false;
		}
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			if (TypeComparable(pt.getType(), comptype))
			{
				List<Type> tas = pt.typeArguments();
				Iterator<Type> itr = tas.iterator();
				while (itr.hasNext()) {
					Type tt = itr.next();
					boolean smt = TypeComparable(tt, comptype);
					if (!smt)
					{
						return false;
					}
				}
				return true;
			}
			return false;
		}
		if (type instanceof UnionType) {
			UnionType ut = (UnionType) type;
			List<Type> types = ut.types();
			Iterator<Type> itr = types.iterator();
			while (itr.hasNext()) {
				Type tt = itr.next();
				boolean smt = TypeComparable(tt, comptype);
				if (!smt)
				{
					return false;
				}
			}
			return true;
		}
		if (type instanceof IntersectionType) {
			IntersectionType ut = (IntersectionType) type;
			List<Type> types = ut.types();
			Iterator<Type> itr = types.iterator();
			while (itr.hasNext()) {
				Type tt = itr.next();
				boolean smt = TypeComparable(tt, comptype);
				if (!smt)
				{
					return false;
				}
			}
			return true;
		}
		System.err.println("Uncognized Type node:" + type + ";type class:" + (type != null ? type.getClass() : null) + ".");
		new Exception().printStackTrace(System.err);
		System.exit(1);
		return false;
	}
	
}